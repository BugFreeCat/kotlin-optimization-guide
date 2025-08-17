package com.test

// 单独的类用于精确的字节码分析
class BytecodeMethod1 {
    private var mClipMvViewModel: ClipMvViewModel? = null
    
    fun check(): Boolean {
        return if (mClipMvViewModel?.mvEditData != null &&
            (mClipMvViewModel?.mvEditData!!.isClipMv)
        ) {
            true
        } else {
            false
        }
    }
}

class BytecodeMethod2 {
    private var mClipMvViewModel: ClipMvViewModel? = null
    
    fun check(): Boolean {
        return if (mClipMvViewModel?.mvEditData?.isClipMv == true) {
            true
        } else {
            false
        }
    }
}