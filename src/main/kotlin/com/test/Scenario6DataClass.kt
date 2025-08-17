package com.test

// 场景6：data class vs 普通 class 性能对比
class Scenario6DataClass {
    
    // 方式1：data class
    data class PersonData(
        val id: Int,
        val name: String,
        val age: Int,
        val email: String
    )
    
    // 方式2：普通 class
    class PersonRegular(
        val id: Int,
        val name: String,
        val age: Int,
        val email: String
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is PersonRegular) return false
            
            if (id != other.id) return false
            if (name != other.name) return false
            if (age != other.age) return false
            if (email != other.email) return false
            
            return true
        }
        
        override fun hashCode(): Int {
            var result = id
            result = 31 * result + name.hashCode()
            result = 31 * result + age
            result = 31 * result + email.hashCode()
            return result
        }
        
        override fun toString(): String {
            return "PersonRegular(id=$id, name='$name', age=$age, email='$email')"
        }
    }
    
    // 方式3：手动优化的 class
    class PersonOptimized(
        val id: Int,
        val name: String,
        val age: Int,
        val email: String
    ) {
        private val cachedHashCode = computeHashCode()
        
        private fun computeHashCode(): Int {
            var result = id
            result = 31 * result + name.hashCode()
            result = 31 * result + age
            result = 31 * result + email.hashCode()
            return result
        }
        
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false
            
            other as PersonOptimized
            return id == other.id && 
                   name == other.name && 
                   age == other.age && 
                   email == other.email
        }
        
        override fun hashCode(): Int = cachedHashCode
    }
    
    fun performanceTest() {
        val iterations = 1_000_000
        
        val dataPersons = List(100) { 
            PersonData(it, "Person$it", 20 + it % 50, "person$it@email.com")
        }
        val regularPersons = List(100) { 
            PersonRegular(it, "Person$it", 20 + it % 50, "person$it@email.com")
        }
        val optimizedPersons = List(100) { 
            PersonOptimized(it, "Person$it", 20 + it % 50, "person$it@email.com")
        }
        
        println("\n=== 场景6: data class vs 普通 class 性能对比 ===")
        
        // 创建对象性能
        println("\n1. 创建对象性能 ($iterations 次):")
        
        val createStart1 = System.nanoTime()
        repeat(iterations) {
            PersonData(it, "Test", 25, "test@email.com")
        }
        val createTime1 = (System.nanoTime() - createStart1) / 1_000_000.0
        
        val createStart2 = System.nanoTime()
        repeat(iterations) {
            PersonRegular(it, "Test", 25, "test@email.com")
        }
        val createTime2 = (System.nanoTime() - createStart2) / 1_000_000.0
        
        println("data class: ${createTime1}ms")
        println("普通 class: ${createTime2}ms (提升 ${((createTime1-createTime2)/createTime1*100).format(2)}%)")
        
        // equals 性能
        println("\n2. equals 比较性能 ($iterations 次):")
        
        val equalsStart1 = System.nanoTime()
        repeat(iterations) {
            dataPersons[it % 100] == dataPersons[(it + 1) % 100]
        }
        val equalsTime1 = (System.nanoTime() - equalsStart1) / 1_000_000.0
        
        val equalsStart2 = System.nanoTime()
        repeat(iterations) {
            regularPersons[it % 100] == regularPersons[(it + 1) % 100]
        }
        val equalsTime2 = (System.nanoTime() - equalsStart2) / 1_000_000.0
        
        val equalsStart3 = System.nanoTime()
        repeat(iterations) {
            optimizedPersons[it % 100] == optimizedPersons[(it + 1) % 100]
        }
        val equalsTime3 = (System.nanoTime() - equalsStart3) / 1_000_000.0
        
        println("data class: ${equalsTime1}ms")
        println("普通 class: ${equalsTime2}ms (差异 ${((equalsTime2-equalsTime1)/equalsTime1*100).format(2)}%)")
        println("优化 class: ${equalsTime3}ms (提升 ${((equalsTime1-equalsTime3)/equalsTime1*100).format(2)}%)")
        
        // hashCode 性能
        println("\n3. hashCode 性能 ($iterations 次):")
        
        val hashStart1 = System.nanoTime()
        repeat(iterations) {
            dataPersons[it % 100].hashCode()
        }
        val hashTime1 = (System.nanoTime() - hashStart1) / 1_000_000.0
        
        val hashStart2 = System.nanoTime()
        repeat(iterations) {
            regularPersons[it % 100].hashCode()
        }
        val hashTime2 = (System.nanoTime() - hashStart2) / 1_000_000.0
        
        val hashStart3 = System.nanoTime()
        repeat(iterations) {
            optimizedPersons[it % 100].hashCode()
        }
        val hashTime3 = (System.nanoTime() - hashStart3) / 1_000_000.0
        
        println("data class: ${hashTime1}ms")
        println("普通 class: ${hashTime2}ms (差异 ${((hashTime2-hashTime1)/hashTime1*100).format(2)}%)")
        println("缓存优化: ${hashTime3}ms (提升 ${((hashTime1-hashTime3)/hashTime1*100).format(2)}%)")
        
        // copy 功能（仅 data class）
        println("\n4. copy 功能性能 (${iterations/10} 次):")
        val copyStart = System.nanoTime()
        repeat(iterations/10) {
            dataPersons[it % 100].copy(age = 30)
        }
        val copyTime = (System.nanoTime() - copyStart) / 1_000_000.0
        println("data class copy: ${copyTime}ms")
        
        // 内存占用
        println("\n5. 内存占用分析:")
        println("data class: 额外生成 componentN() 方法")
        println("普通 class: 更少的方法，更小的字节码")
    }
}