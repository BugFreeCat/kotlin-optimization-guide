package com.test

import java.util.concurrent.atomic.AtomicReference

// 场景2：多次赋值操作
data class MusicInfo(
    var bgDrmInfo: String,
    var singDrmInfo: String,
    var backingTrackUrl: String,
    var originalSingUrl: String,
    var tuneAlignOffset: Int,
    var showAiGalleryTab: Boolean,
    var duetGroups: List<String>,
    var coworkGroups: List<String>,
    var cryptMelMidiUrlList: List<String>
)

class MusicInfoData(
    var bgDrmInfo: String = "",
    var singDrmInfo: String = "",
    var backingTrackUrl: String = "",
    var originalSingUrl: String = "",
    var tuneAlignOffset: Int = 0,
    var showAiGalleryTab: Boolean = false,
    var duetGroups: List<String> = emptyList(),
    var coworkGroups: List<String> = emptyList(),
    var cryptMelMidiUrlList: List<String> = emptyList()
)

class Scenario2MultipleAssignments {
    private val mMusicInfoData = AtomicReference<MusicInfoData?>()
    
    // 方式1：多次使用 !!
    fun method1_multipleNonNull(musicInfo: MusicInfo) {
        mMusicInfoData.get()!!.bgDrmInfo = musicInfo.bgDrmInfo
        mMusicInfoData.get()!!.singDrmInfo = musicInfo.singDrmInfo
        mMusicInfoData.get()!!.backingTrackUrl = musicInfo.backingTrackUrl
        mMusicInfoData.get()!!.originalSingUrl = musicInfo.originalSingUrl
        mMusicInfoData.get()!!.tuneAlignOffset = musicInfo.tuneAlignOffset
        mMusicInfoData.get()!!.showAiGalleryTab = musicInfo.showAiGalleryTab
        mMusicInfoData.get()!!.duetGroups = musicInfo.duetGroups
        mMusicInfoData.get()!!.coworkGroups = musicInfo.coworkGroups
        mMusicInfoData.get()!!.cryptMelMidiUrlList = musicInfo.cryptMelMidiUrlList
    }
    
    // 方式2：使用 ?.apply
    fun method2_safeApply(musicInfo: MusicInfo) {
        mMusicInfoData.get()?.apply {
            bgDrmInfo = musicInfo.bgDrmInfo
            singDrmInfo = musicInfo.singDrmInfo
            backingTrackUrl = musicInfo.backingTrackUrl
            originalSingUrl = musicInfo.originalSingUrl
            tuneAlignOffset = musicInfo.tuneAlignOffset
            showAiGalleryTab = musicInfo.showAiGalleryTab
            duetGroups = musicInfo.duetGroups
            coworkGroups = musicInfo.coworkGroups
            cryptMelMidiUrlList = musicInfo.cryptMelMidiUrlList
        }
    }
    
    // 方式3：临时变量 + 非空检查
    fun method3_tempVariable(musicInfo: MusicInfo) {
        val data = mMusicInfoData.get()
        if (data != null) {
            data.bgDrmInfo = musicInfo.bgDrmInfo
            data.singDrmInfo = musicInfo.singDrmInfo
            data.backingTrackUrl = musicInfo.backingTrackUrl
            data.originalSingUrl = musicInfo.originalSingUrl
            data.tuneAlignOffset = musicInfo.tuneAlignOffset
            data.showAiGalleryTab = musicInfo.showAiGalleryTab
            data.duetGroups = musicInfo.duetGroups
            data.coworkGroups = musicInfo.coworkGroups
            data.cryptMelMidiUrlList = musicInfo.cryptMelMidiUrlList
        }
    }
    
    // 方式4：requireNotNull
    fun method4_requireNotNull(musicInfo: MusicInfo) {
        val data = requireNotNull(mMusicInfoData.get()) { "MusicInfoData must not be null" }
        data.bgDrmInfo = musicInfo.bgDrmInfo
        data.singDrmInfo = musicInfo.singDrmInfo
        data.backingTrackUrl = musicInfo.backingTrackUrl
        data.originalSingUrl = musicInfo.originalSingUrl
        data.tuneAlignOffset = musicInfo.tuneAlignOffset
        data.showAiGalleryTab = musicInfo.showAiGalleryTab
        data.duetGroups = musicInfo.duetGroups
        data.coworkGroups = musicInfo.coworkGroups
        data.cryptMelMidiUrlList = musicInfo.cryptMelMidiUrlList
    }
    
    fun performanceTest() {
        // 初始化测试数据
        mMusicInfoData.set(MusicInfoData())
        val musicInfo = MusicInfo(
            bgDrmInfo = "drm123",
            singDrmInfo = "singDrm456",
            backingTrackUrl = "http://example.com/backing.mp3",
            originalSingUrl = "http://example.com/original.mp3",
            tuneAlignOffset = 100,
            showAiGalleryTab = true,
            duetGroups = listOf("group1", "group2"),
            coworkGroups = listOf("cowork1", "cowork2"),
            cryptMelMidiUrlList = listOf("midi1", "midi2")
        )
        
        val iterations = 2_000_000
        
        // 预热
        repeat(1000) {
            method1_multipleNonNull(musicInfo)
            method2_safeApply(musicInfo)
            method3_tempVariable(musicInfo)
            method4_requireNotNull(musicInfo)
        }
        
        // 测试方式1
        val start1 = System.nanoTime()
        for (i in 0 until iterations) {
            method1_multipleNonNull(musicInfo)
        }
        val time1 = (System.nanoTime() - start1) / 1_000_000.0
        
        // 测试方式2
        val start2 = System.nanoTime()
        for (i in 0 until iterations) {
            method2_safeApply(musicInfo)
        }
        val time2 = (System.nanoTime() - start2) / 1_000_000.0
        
        // 测试方式3
        val start3 = System.nanoTime()
        for (i in 0 until iterations) {
            method3_tempVariable(musicInfo)
        }
        val time3 = (System.nanoTime() - start3) / 1_000_000.0
        
        // 测试方式4
        val start4 = System.nanoTime()
        for (i in 0 until iterations) {
            method4_requireNotNull(musicInfo)
        }
        val time4 = (System.nanoTime() - start4) / 1_000_000.0
        
        println("\n=== 场景2: 多次赋值操作 ($iterations 次迭代) ===")
        println("方式1 (多次!!): ${time1}ms")
        println("方式2 (?.apply): ${time2}ms")
        println("方式3 (临时变量): ${time3}ms")
        println("方式4 (requireNotNull): ${time4}ms")
        println("\n性能对比:")
        println("?.apply vs 多次!!: ${((time1 - time2) / time1 * 100).format(2)}% 提升")
        println("临时变量 vs 多次!!: ${((time1 - time3) / time1 * 100).format(2)}% 提升")
        println("requireNotNull vs 多次!!: ${((time1 - time4) / time1 * 100).format(2)}% 提升")
    }
    
    fun multiThreadTest() {
        val threads = 100
        val iterations = 50_000
        var exceptions1 = 0
        var exceptions2 = 0
        var exceptions3 = 0
        var exceptions4 = 0
        
        val musicInfo = MusicInfo(
            "drm", "sing", "backing", "original", 
            100, true, emptyList(), emptyList(), emptyList()
        )
        
        println("\n=== 场景2: 多线程稳定性测试 ===")
        
        // 测试方式1
        mMusicInfoData.set(MusicInfoData())
        val threadList1 = (0 until threads).map {
            Thread {
                for (i in 0 until iterations) {
                    try {
                        method1_multipleNonNull(musicInfo)
                        // 模拟并发修改
                        if (i % 1000 == 0) {
                            mMusicInfoData.set(if (i % 2000 == 0) null else MusicInfoData())
                        }
                    } catch (e: Exception) {
                        exceptions1++
                    }
                }
            }
        }
        
        threadList1.forEach { it.start() }
        threadList1.forEach { it.join() }
        
        // 测试方式2
        mMusicInfoData.set(MusicInfoData())
        val threadList2 = (0 until threads).map {
            Thread {
                for (i in 0 until iterations) {
                    try {
                        method2_safeApply(musicInfo)
                        if (i % 1000 == 0) {
                            mMusicInfoData.set(if (i % 2000 == 0) null else MusicInfoData())
                        }
                    } catch (e: Exception) {
                        exceptions2++
                    }
                }
            }
        }
        
        threadList2.forEach { it.start() }
        threadList2.forEach { it.join() }
        
        // 测试方式3
        mMusicInfoData.set(MusicInfoData())
        val threadList3 = (0 until threads).map {
            Thread {
                for (i in 0 until iterations) {
                    try {
                        method3_tempVariable(musicInfo)
                        if (i % 1000 == 0) {
                            mMusicInfoData.set(if (i % 2000 == 0) null else MusicInfoData())
                        }
                    } catch (e: Exception) {
                        exceptions3++
                    }
                }
            }
        }
        
        threadList3.forEach { it.start() }
        threadList3.forEach { it.join() }
        
        // 测试方式4
        mMusicInfoData.set(MusicInfoData())
        val threadList4 = (0 until threads).map {
            Thread {
                for (i in 0 until iterations) {
                    try {
                        method4_requireNotNull(musicInfo)
                        if (i % 1000 == 0) {
                            mMusicInfoData.set(if (i % 2000 == 0) null else MusicInfoData())
                        }
                    } catch (e: Exception) {
                        exceptions4++
                    }
                }
            }
        }
        
        threadList4.forEach { it.start() }
        threadList4.forEach { it.join() }
        
        println("多次!! 异常次数: $exceptions1")
        println("?.apply 异常次数: $exceptions2")
        println("临时变量 异常次数: $exceptions3")
        println("requireNotNull 异常次数: $exceptions4")
    }
}