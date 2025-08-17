package com.test

// 用于精确字节码分析的简化版本
class BytecodeScenario2 {
    // 多次 !! 访问
    class MultipleNonNull {
        private var data: MusicInfoData? = null
        
        fun assign(musicInfo: MusicInfo) {
            data!!.bgDrmInfo = musicInfo.bgDrmInfo
            data!!.singDrmInfo = musicInfo.singDrmInfo
            data!!.tuneAlignOffset = musicInfo.tuneAlignOffset
        }
    }
    
    // ?.apply 访问
    class ApplyAccess {
        private var data: MusicInfoData? = null
        
        fun assign(musicInfo: MusicInfo) {
            data?.apply {
                bgDrmInfo = musicInfo.bgDrmInfo
                singDrmInfo = musicInfo.singDrmInfo
                tuneAlignOffset = musicInfo.tuneAlignOffset
            }
        }
    }
}