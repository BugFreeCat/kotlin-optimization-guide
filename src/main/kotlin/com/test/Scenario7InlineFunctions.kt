package com.test

// 场景7：inline 函数性能对比
class Scenario7InlineFunctions {
    
    // 普通高阶函数
    fun normalHigherOrder(block: () -> Int): Int {
        return block()
    }
    
    // inline 高阶函数
    inline fun inlineHigherOrder(block: () -> Int): Int {
        return block()
    }
    
    // crossinline
    inline fun crossinlineHigherOrder(crossinline block: () -> Int): Int {
        return block()
    }
    
    // noinline
    inline fun partialInline(block1: () -> Int, noinline block2: () -> Int): Int {
        return block1() + block2()
    }
    
    // 复杂的 inline 函数示例
    inline fun <T> measureTime(block: () -> T): Pair<T, Long> {
        val start = System.nanoTime()
        val result = block()
        val time = System.nanoTime() - start
        return result to time
    }
    
    // reified 类型参数
    inline fun <reified T> isInstanceOf(value: Any): Boolean {
        return value is T
    }
    
    fun performanceTest() {
        val iterations = 10_000_000
        var sum = 0
        
        println("\n=== 场景7: inline 函数性能对比 ===")
        
        // 预热
        repeat(10000) {
            normalHigherOrder { 42 }
            inlineHigherOrder { 42 }
        }
        
        // 测试普通高阶函数
        println("\n1. 高阶函数调用性能 ($iterations 次):")
        val normalStart = System.nanoTime()
        repeat(iterations) {
            sum += normalHigherOrder { 42 }
        }
        val normalTime = (System.nanoTime() - normalStart) / 1_000_000.0
        
        // 测试 inline 高阶函数
        val inlineStart = System.nanoTime()
        repeat(iterations) {
            sum += inlineHigherOrder { 42 }
        }
        val inlineTime = (System.nanoTime() - inlineStart) / 1_000_000.0
        
        // 测试 crossinline
        val crossinlineStart = System.nanoTime()
        repeat(iterations) {
            sum += crossinlineHigherOrder { 42 }
        }
        val crossinlineTime = (System.nanoTime() - crossinlineStart) / 1_000_000.0
        
        println("普通函数: ${normalTime}ms (基准)")
        println("inline函数: ${inlineTime}ms (提升 ${((normalTime-inlineTime)/normalTime*100).format(2)}%)")
        println("crossinline: ${crossinlineTime}ms (提升 ${((normalTime-crossinlineTime)/normalTime*100).format(2)}%)")
        
        // 测试复杂场景
        println("\n2. 复杂 lambda 场景 (${iterations/100} 次):")
        
        var complexSum = 0
        val complexNormalStart = System.nanoTime()
        repeat(iterations/100) { i ->
            complexSum += normalHigherOrder { 
                var result = 0
                for (j in 1..10) {
                    result += i * j
                }
                result
            }
        }
        val complexNormalTime = (System.nanoTime() - complexNormalStart) / 1_000_000.0
        
        complexSum = 0
        val complexInlineStart = System.nanoTime()
        repeat(iterations/100) { i ->
            complexSum += inlineHigherOrder { 
                var result = 0
                for (j in 1..10) {
                    result += i * j
                }
                result
            }
        }
        val complexInlineTime = (System.nanoTime() - complexInlineStart) / 1_000_000.0
        
        println("普通函数(复杂): ${complexNormalTime}ms")
        println("inline函数(复杂): ${complexInlineTime}ms (提升 ${((complexNormalTime-complexInlineTime)/complexNormalTime*100).format(2)}%)")
        
        // 测试 reified
        println("\n3. reified 类型检查性能 (${iterations/10} 次):")
        val testObjects = listOf("String", 123, 45.6, true, listOf(1,2,3))
        
        val reifiedStart = System.nanoTime()
        repeat(iterations/10) {
            testObjects.forEach { obj ->
                isInstanceOf<String>(obj)
                isInstanceOf<Int>(obj)
                isInstanceOf<Double>(obj)
            }
        }
        val reifiedTime = (System.nanoTime() - reifiedStart) / 1_000_000.0
        
        println("reified 类型检查: ${reifiedTime}ms")
        
        // 内存和字节码影响
        println("\n4. 内存和字节码影响:")
        println("普通函数: 创建 Function 对象，增加内存分配")
        println("inline函数: 代码直接内联，无额外对象，但增加字节码大小")
        println("适用场景: 小型频繁调用的函数，带 lambda 参数的函数")
        
        // 防止编译器优化
        println("\n(Sum: $sum, ComplexSum: $complexSum)")
    }
}