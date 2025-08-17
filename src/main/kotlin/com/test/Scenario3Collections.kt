package com.test

// 场景3：集合操作性能对比
class Scenario3Collections {
    private val testList = (1..1000).toList()
    
    // 方式1：传统 for 循环
    fun method1_forLoop(): Int {
        var sum = 0
        for (i in testList) {
            if (i % 2 == 0) {
                sum += i * 2
            }
        }
        return sum
    }
    
    // 方式2：forEach
    fun method2_forEach(): Int {
        var sum = 0
        testList.forEach { i ->
            if (i % 2 == 0) {
                sum += i * 2
            }
        }
        return sum
    }
    
    // 方式3：filter + map + sum
    fun method3_functionalChain(): Int {
        return testList
            .filter { it % 2 == 0 }
            .map { it * 2 }
            .sum()
    }
    
    // 方式4：asSequence
    fun method4_sequence(): Int {
        return testList.asSequence()
            .filter { it % 2 == 0 }
            .map { it * 2 }
            .sum()
    }
    
    // 方式5：sumOf (Kotlin 1.4+)
    fun method5_sumOf(): Int {
        return testList.sumOf { 
            if (it % 2 == 0) it * 2 else 0
        }
    }
    
    fun performanceTest() {
        val iterations = 100_000
        
        // 预热
        repeat(1000) {
            method1_forLoop()
            method2_forEach()
            method3_functionalChain()
            method4_sequence()
            method5_sumOf()
        }
        
        println("\n=== 场景3: 集合操作性能对比 ($iterations 次迭代) ===")
        
        val start1 = System.nanoTime()
        repeat(iterations) { method1_forLoop() }
        val time1 = (System.nanoTime() - start1) / 1_000_000.0
        
        val start2 = System.nanoTime()
        repeat(iterations) { method2_forEach() }
        val time2 = (System.nanoTime() - start2) / 1_000_000.0
        
        val start3 = System.nanoTime()
        repeat(iterations) { method3_functionalChain() }
        val time3 = (System.nanoTime() - start3) / 1_000_000.0
        
        val start4 = System.nanoTime()
        repeat(iterations) { method4_sequence() }
        val time4 = (System.nanoTime() - start4) / 1_000_000.0
        
        val start5 = System.nanoTime()
        repeat(iterations) { method5_sumOf() }
        val time5 = (System.nanoTime() - start5) / 1_000_000.0
        
        println("for循环: ${time1}ms")
        println("forEach: ${time2}ms (相比for循环: ${((time2-time1)/time1*100).format(2)}%)")
        println("filter+map+sum: ${time3}ms (相比for循环: ${((time3-time1)/time1*100).format(2)}%)")
        println("sequence: ${time4}ms (相比for循环: ${((time4-time1)/time1*100).format(2)}%)")
        println("sumOf: ${time5}ms (相比for循环: ${((time5-time1)/time1*100).format(2)}%)")
        
        // 内存分配测试
        println("\n内存分配影响:")
        val runtime = Runtime.getRuntime()
        
        System.gc()
        val memBefore = runtime.totalMemory() - runtime.freeMemory()
        repeat(1000) { method3_functionalChain() }
        System.gc()
        val memAfterFunctional = runtime.totalMemory() - runtime.freeMemory()
        
        System.gc()
        val memBefore2 = runtime.totalMemory() - runtime.freeMemory()
        repeat(1000) { method4_sequence() }
        System.gc()
        val memAfterSequence = runtime.totalMemory() - runtime.freeMemory()
        
        println("函数式链: 约 ${(memAfterFunctional - memBefore) / 1024}KB")
        println("Sequence: 约 ${(memAfterSequence - memBefore2) / 1024}KB")
    }
}