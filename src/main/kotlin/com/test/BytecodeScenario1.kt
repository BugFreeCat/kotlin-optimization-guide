package com.test

// 用于精确字节码分析的简化版本
class BytecodeScenario1 {
    private var userProfile: UserProfile? = null
    
    // 连续 ?. 访问
    class ChainedAccess {
        private var userProfile: UserProfile? = null
        
        fun access(): String {
            val a = userProfile?.name ?: ""
            val b = userProfile?.address?.street ?: ""
            val c = userProfile?.address?.city?.name ?: ""
            return a + b + c
        }
    }
    
    // ?.let 访问
    class LetAccess {
        private var userProfile: UserProfile? = null
        
        fun access(): String {
            return userProfile?.let { profile ->
                val a = profile.name
                val b = profile.address?.let { it.street } ?: ""
                val c = profile.address?.city?.let { it.name } ?: ""
                a + b + c
            } ?: ""
        }
    }
}