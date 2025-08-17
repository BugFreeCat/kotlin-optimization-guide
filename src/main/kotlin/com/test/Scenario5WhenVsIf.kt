package com.test

// 场景5：when vs if-else 性能对比
class Scenario5WhenVsIf {
    
    // 方式1：多个 if-else
    fun method1_ifElse(value: Int): String {
        return if (value == 1) {
            "One"
        } else if (value == 2) {
            "Two"
        } else if (value == 3) {
            "Three"
        } else if (value == 4) {
            "Four"
        } else if (value == 5) {
            "Five"
        } else if (value == 6) {
            "Six"
        } else if (value == 7) {
            "Seven"
        } else if (value == 8) {
            "Eight"
        } else if (value == 9) {
            "Nine"
        } else if (value == 10) {
            "Ten"
        } else {
            "Other"
        }
    }
    
    // 方式2：when 表达式
    fun method2_when(value: Int): String {
        return when (value) {
            1 -> "One"
            2 -> "Two"
            3 -> "Three"
            4 -> "Four"
            5 -> "Five"
            6 -> "Six"
            7 -> "Seven"
            8 -> "Eight"
            9 -> "Nine"
            10 -> "Ten"
            else -> "Other"
        }
    }
    
    // 方式3：when with ranges
    fun method3_whenRanges(value: Int): String {
        return when (value) {
            in 1..3 -> "Low"
            in 4..7 -> "Medium"
            in 8..10 -> "High"
            else -> "Other"
        }
    }
    
    // 方式4：Map 查找
    private val valueMap = mapOf(
        1 to "One",
        2 to "Two",
        3 to "Three",
        4 to "Four",
        5 to "Five",
        6 to "Six",
        7 to "Seven",
        8 to "Eight",
        9 to "Nine",
        10 to "Ten"
    )
    
    fun method4_mapLookup(value: Int): String {
        return valueMap[value] ?: "Other"
    }
    
    fun performanceTest() {
        val iterations = 5_000_000
        val testValues = (1..15).toList()
        
        // 预热
        repeat(1000) {
            testValues.forEach { 
                method1_ifElse(it)
                method2_when(it)
                method3_whenRanges(it)
                method4_mapLookup(it)
            }
        }
        
        println("\n=== 场景5: when vs if-else 性能对比 ($iterations 次迭代) ===")
        
        // 测试 if-else
        val start1 = System.nanoTime()
        repeat(iterations) {
            testValues.forEach { method1_ifElse(it) }
        }
        val time1 = (System.nanoTime() - start1) / 1_000_000.0
        
        // 测试 when
        val start2 = System.nanoTime()
        repeat(iterations) {
            testValues.forEach { method2_when(it) }
        }
        val time2 = (System.nanoTime() - start2) / 1_000_000.0
        
        // 测试 when with ranges
        val start3 = System.nanoTime()
        repeat(iterations) {
            testValues.forEach { method3_whenRanges(it) }
        }
        val time3 = (System.nanoTime() - start3) / 1_000_000.0
        
        // 测试 Map lookup
        val start4 = System.nanoTime()
        repeat(iterations) {
            testValues.forEach { method4_mapLookup(it) }
        }
        val time4 = (System.nanoTime() - start4) / 1_000_000.0
        
        println("if-else链: ${time1}ms (基准)")
        println("when表达式: ${time2}ms (提升 ${((time1-time2)/time1*100).format(2)}%)")
        println("when ranges: ${time3}ms (差异 ${((time3-time1)/time1*100).format(2)}%)")
        println("Map查找: ${time4}ms (提升 ${((time1-time4)/time1*100).format(2)}%)")
        
        // 字节码分析
        println("\n字节码影响:")
        println("if-else: 生成多个条件跳转指令")
        println("when: 编译为 tableswitch/lookupswitch (更高效)")
        println("Map: 一次哈希查找，O(1)复杂度")
    }
}