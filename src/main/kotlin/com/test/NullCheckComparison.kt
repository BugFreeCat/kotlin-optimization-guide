package com.test

data class MvEditData(val isClipMv: Boolean)

class ClipMvViewModel {
    var mvEditData: MvEditData? = null
}

class NullCheckComparison {
    private var mClipMvViewModel: ClipMvViewModel? = null
    
    // 方式1：显式 null 检查 + 非空断言
    fun method1(): Boolean {
        return if (mClipMvViewModel?.mvEditData != null &&
            (mClipMvViewModel?.mvEditData!!.isClipMv)
        ) {
            true
        } else {
            false
        }
    }
    
    // 方式2：安全调用链 + == true
    fun method2(): Boolean {
        return if (mClipMvViewModel?.mvEditData?.isClipMv == true) {
            true
        } else {
            false
        }
    }
    
    // 用于性能测试的函数
    fun performanceTest() {
        mClipMvViewModel = ClipMvViewModel()
        mClipMvViewModel?.mvEditData = MvEditData(true)
        
        val iterations = 10_000_000
        
        // 测试方式1
        val start1 = System.nanoTime()
        for (i in 0 until iterations) {
            method1()
        }
        val end1 = System.nanoTime()
        val time1 = (end1 - start1) / 1_000_000.0 // 转换为毫秒
        
        // 测试方式2
        val start2 = System.nanoTime()
        for (i in 0 until iterations) {
            method2()
        }
        val end2 = System.nanoTime()
        val time2 = (end2 - start2) / 1_000_000.0 // 转换为毫秒
        
        println("方式1 (显式null检查 + !!): ${time1}ms")
        println("方式2 (安全调用链): ${time2}ms")
        println("性能差异: ${time1 - time2}ms (正数表示方式2更快)")
        println("性能提升: ${((time1 - time2) / time1 * 100).format(2)}%")
    }
    
    // 多线程稳定性测试
    fun multiThreadTest() {
        val threads = 100
        val iterations = 100_000
        var exceptions1 = 0
        var exceptions2 = 0
        
        // 准备数据
        mClipMvViewModel = ClipMvViewModel()
        mClipMvViewModel?.mvEditData = MvEditData(true)
        
        // 测试方式1的线程安全性
        val threadList1 = (0 until threads).map { 
            Thread {
                for (i in 0 until iterations) {
                    try {
                        method1()
                        // 模拟并发修改
                        if (i % 1000 == 0) {
                            mClipMvViewModel?.mvEditData = if (i % 2000 == 0) null else MvEditData(true)
                        }
                    } catch (e: NullPointerException) {
                        exceptions1++
                    }
                }
            }
        }
        
        threadList1.forEach { it.start() }
        threadList1.forEach { it.join() }
        
        // 重置数据
        mClipMvViewModel = ClipMvViewModel()
        mClipMvViewModel?.mvEditData = MvEditData(true)
        
        // 测试方式2的线程安全性
        val threadList2 = (0 until threads).map {
            Thread {
                for (i in 0 until iterations) {
                    try {
                        method2()
                        // 模拟并发修改
                        if (i % 1000 == 0) {
                            mClipMvViewModel?.mvEditData = if (i % 2000 == 0) null else MvEditData(true)
                        }
                    } catch (e: NullPointerException) {
                        exceptions2++
                    }
                }
            }
        }
        
        threadList2.forEach { it.start() }
        threadList2.forEach { it.join() }
        
        println("\n多线程稳定性测试 ($threads 线程, 每线程 $iterations 次迭代):")
        println("方式1 NullPointerException 次数: $exceptions1")
        println("方式2 NullPointerException 次数: $exceptions2")
    }
}

fun Double.format(digits: Int) = "%.${digits}f".format(this)

fun main() {
    val comparison = NullCheckComparison()
    
    println("=== Kotlin Null 检查方式对比分析 ===\n")
    
    // 预热 JVM
    println("预热 JVM...")
    repeat(5) {
        comparison.performanceTest()
    }
    
    println("\n正式测试:")
    comparison.performanceTest()
    
    comparison.multiThreadTest()
}