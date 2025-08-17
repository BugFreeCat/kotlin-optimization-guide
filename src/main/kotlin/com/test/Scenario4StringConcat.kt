package com.test

// 场景4：字符串拼接性能对比
class Scenario4StringConcat {
    private val items = (1..20).map { "Item$it" }
    
    // 方式1：使用 + 操作符
    fun method1_plusOperator(): String {
        var result = ""
        for (item in items) {
            result = result + item + ", "
        }
        return result
    }
    
    // 方式2：使用 StringBuilder
    fun method2_stringBuilder(): String {
        val sb = StringBuilder()
        for (item in items) {
            sb.append(item).append(", ")
        }
        return sb.toString()
    }
    
    // 方式3：使用 buildString
    fun method3_buildString(): String {
        return buildString {
            for (item in items) {
                append(item)
                append(", ")
            }
        }
    }
    
    // 方式4：使用 joinToString
    fun method4_joinToString(): String {
        return items.joinToString(", ")
    }
    
    // 方式5：字符串模板
    fun method5_stringTemplate(): String {
        var result = ""
        for (item in items) {
            result = "$result$item, "
        }
        return result
    }
    
    fun performanceTest() {
        val iterations = 50_000
        
        // 预热
        repeat(1000) {
            method1_plusOperator()
            method2_stringBuilder()
            method3_buildString()
            method4_joinToString()
            method5_stringTemplate()
        }
        
        println("\n=== 场景4: 字符串拼接性能对比 ($iterations 次迭代) ===")
        
        val start1 = System.nanoTime()
        repeat(iterations) { method1_plusOperator() }
        val time1 = (System.nanoTime() - start1) / 1_000_000.0
        
        val start2 = System.nanoTime()
        repeat(iterations) { method2_stringBuilder() }
        val time2 = (System.nanoTime() - start2) / 1_000_000.0
        
        val start3 = System.nanoTime()
        repeat(iterations) { method3_buildString() }
        val time3 = (System.nanoTime() - start3) / 1_000_000.0
        
        val start4 = System.nanoTime()
        repeat(iterations) { method4_joinToString() }
        val time4 = (System.nanoTime() - start4) / 1_000_000.0
        
        val start5 = System.nanoTime()
        repeat(iterations) { method5_stringTemplate() }
        val time5 = (System.nanoTime() - start5) / 1_000_000.0
        
        println("+ 操作符: ${time1}ms (基准)")
        println("StringBuilder: ${time2}ms (提升 ${((time1-time2)/time1*100).format(2)}%)")
        println("buildString: ${time3}ms (提升 ${((time1-time3)/time1*100).format(2)}%)")
        println("joinToString: ${time4}ms (提升 ${((time1-time4)/time1*100).format(2)}%)")
        println("字符串模板: ${time5}ms (差异 ${((time5-time1)/time1*100).format(2)}%)")
        
        // 内存分配测试
        println("\n内存影响分析:")
        val runtime = Runtime.getRuntime()
        
        System.gc()
        val memBefore = runtime.totalMemory() - runtime.freeMemory()
        repeat(100) { method1_plusOperator() }
        val memAfter = runtime.totalMemory() - runtime.freeMemory()
        
        println("+ 操作符产生的临时对象: 约 ${(memAfter - memBefore) / 1024}KB/100次")
    }
}