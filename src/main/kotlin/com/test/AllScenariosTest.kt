package com.test

fun main() {
    println("=== Kotlin 最佳实践性能测试综合分析 ===")
    println("测试环境: JVM ${System.getProperty("java.version")}")
    println("处理器: ${Runtime.getRuntime().availableProcessors()} 核心")
    println("最大内存: ${Runtime.getRuntime().maxMemory() / 1024 / 1024}MB")
    println("=" * 50)
    
    // 原始场景测试
    println("\n>>> 原始场景: if条件中的null检查")
    val comparison = NullCheckComparison()
    comparison.performanceTest()
    comparison.multiThreadTest()
    
    // 场景1测试
    println("\n\n>>> 场景1: 连续访问对象成员")
    val scenario1 = Scenario1ChainedAccess()
    scenario1.performanceTest()
    scenario1.multiThreadTest()
    
    // 场景2测试
    println("\n\n>>> 场景2: 多次赋值操作")
    val scenario2 = Scenario2MultipleAssignments()
    scenario2.performanceTest()
    scenario2.multiThreadTest()
    
    // 场景3测试
    println("\n\n>>> 场景3: 集合操作")
    val scenario3 = Scenario3Collections()
    scenario3.performanceTest()
    
    // 场景4测试
    println("\n\n>>> 场景4: 字符串拼接")
    val scenario4 = Scenario4StringConcat()
    scenario4.performanceTest()
    
    // 场景5测试
    println("\n\n>>> 场景5: when vs if-else")
    val scenario5 = Scenario5WhenVsIf()
    scenario5.performanceTest()
    
    // 场景6测试
    println("\n\n>>> 场景6: data class vs 普通 class")
    val scenario6 = Scenario6DataClass()
    scenario6.performanceTest()
    
    // 场景7测试
    println("\n\n>>> 场景7: inline 函数")
    val scenario7 = Scenario7InlineFunctions()
    scenario7.performanceTest()
    
    println("\n\n=== 测试完成 ===")
    println("详细分析报告请查看: KOTLIN_BEST_PRACTICES.md")
}

operator fun String.times(count: Int): String = this.repeat(count)