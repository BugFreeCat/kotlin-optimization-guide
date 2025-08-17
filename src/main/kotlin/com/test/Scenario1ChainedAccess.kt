package com.test

// 场景1：连续使用 ?. 访问成员
data class UserProfile(
    val name: String,
    val address: Address?,
    val preferences: Preferences?
)

data class Address(
    val street: String,
    val city: City?,
    val zipCode: String?
)

data class City(
    val name: String,
    val country: Country?
)

data class Country(
    val name: String,
    val code: String
)

data class Preferences(
    val theme: String,
    val notifications: NotificationSettings?
)

data class NotificationSettings(
    val email: Boolean,
    val push: Boolean,
    val frequency: String
)

class Scenario1ChainedAccess {
    private var userProfile: UserProfile? = null
    
    // 方式1：连续使用 ?. 访问
    fun method1_chainedSafeCall(): String {
        val result = StringBuilder()
        
        // 多次访问同一对象的不同属性
        result.append(userProfile?.name ?: "")
        result.append(userProfile?.address?.street ?: "")
        result.append(userProfile?.address?.city?.name ?: "")
        result.append(userProfile?.address?.city?.country?.name ?: "")
        result.append(userProfile?.address?.city?.country?.code ?: "")
        result.append(userProfile?.address?.zipCode ?: "")
        result.append(userProfile?.preferences?.theme ?: "")
        result.append(userProfile?.preferences?.notifications?.email ?: false)
        result.append(userProfile?.preferences?.notifications?.push ?: false)
        result.append(userProfile?.preferences?.notifications?.frequency ?: "")
        
        return result.toString()
    }
    
    // 方式2：使用 ?.let
    fun method2_safeLet(): String {
        return userProfile?.let { profile ->
            val result = StringBuilder()
            result.append(profile.name)
            
            profile.address?.let { address ->
                result.append(address.street)
                address.city?.let { city ->
                    result.append(city.name)
                    city.country?.let { country ->
                        result.append(country.name)
                        result.append(country.code)
                    }
                }
                result.append(address.zipCode ?: "")
            }
            
            profile.preferences?.let { prefs ->
                result.append(prefs.theme)
                prefs.notifications?.let { notif ->
                    result.append(notif.email)
                    result.append(notif.push)
                    result.append(notif.frequency)
                }
            }
            
            result.toString()
        } ?: ""
    }
    
    // 方式3：临时赋值为非空类型
    fun method3_tempAssignment(): String {
        val profile = userProfile ?: return ""
        val result = StringBuilder()
        
        result.append(profile.name)
        
        val address = profile.address
        if (address != null) {
            result.append(address.street)
            val city = address.city
            if (city != null) {
                result.append(city.name)
                val country = city.country
                if (country != null) {
                    result.append(country.name)
                    result.append(country.code)
                }
            }
            address.zipCode?.let { result.append(it) }
        }
        
        val prefs = profile.preferences
        if (prefs != null) {
            result.append(prefs.theme)
            val notif = prefs.notifications
            if (notif != null) {
                result.append(notif.email)
                result.append(notif.push)
                result.append(notif.frequency)
            }
        }
        
        return result.toString()
    }
    
    fun performanceTest() {
        // 初始化测试数据
        userProfile = UserProfile(
            name = "John Doe",
            address = Address(
                street = "123 Main St",
                city = City(
                    name = "New York",
                    country = Country("USA", "US")
                ),
                zipCode = "10001"
            ),
            preferences = Preferences(
                theme = "dark",
                notifications = NotificationSettings(
                    email = true,
                    push = false,
                    frequency = "daily"
                )
            )
        )
        
        val iterations = 5_000_000
        
        // 预热
        repeat(1000) {
            method1_chainedSafeCall()
            method2_safeLet()
            method3_tempAssignment()
        }
        
        // 测试方式1
        val start1 = System.nanoTime()
        for (i in 0 until iterations) {
            method1_chainedSafeCall()
        }
        val time1 = (System.nanoTime() - start1) / 1_000_000.0
        
        // 测试方式2
        val start2 = System.nanoTime()
        for (i in 0 until iterations) {
            method2_safeLet()
        }
        val time2 = (System.nanoTime() - start2) / 1_000_000.0
        
        // 测试方式3
        val start3 = System.nanoTime()
        for (i in 0 until iterations) {
            method3_tempAssignment()
        }
        val time3 = (System.nanoTime() - start3) / 1_000_000.0
        
        println("\n=== 场景1: 连续访问对象成员 ($iterations 次迭代) ===")
        println("方式1 (连续?.): ${time1}ms")
        println("方式2 (?.let): ${time2}ms")
        println("方式3 (临时赋值): ${time3}ms")
        println("\n性能对比:")
        println("?.let vs 连续?.: ${((time1 - time2) / time1 * 100).format(2)}% 提升")
        println("临时赋值 vs 连续?.: ${((time1 - time3) / time1 * 100).format(2)}% 提升")
    }
    
    fun multiThreadTest() {
        val threads = 50
        val iterations = 50_000
        var exceptions1 = 0
        var exceptions2 = 0
        var exceptions3 = 0
        
        println("\n=== 场景1: 多线程稳定性测试 ===")
        
        // 测试方式1
        val threadList1 = (0 until threads).map {
            Thread {
                for (i in 0 until iterations) {
                    try {
                        method1_chainedSafeCall()
                        // 模拟并发修改
                        if (i % 100 == 0) {
                            userProfile = if (i % 200 == 0) null else UserProfile(
                                "User$i", null, null
                            )
                        }
                    } catch (e: Exception) {
                        exceptions1++
                    }
                }
            }
        }
        
        threadList1.forEach { it.start() }
        threadList1.forEach { it.join() }
        
        // 重置数据，测试方式2
        userProfile = UserProfile("Test", null, null)
        
        val threadList2 = (0 until threads).map {
            Thread {
                for (i in 0 until iterations) {
                    try {
                        method2_safeLet()
                        if (i % 100 == 0) {
                            userProfile = if (i % 200 == 0) null else UserProfile(
                                "User$i", null, null
                            )
                        }
                    } catch (e: Exception) {
                        exceptions2++
                    }
                }
            }
        }
        
        threadList2.forEach { it.start() }
        threadList2.forEach { it.join() }
        
        // 重置数据，测试方式3
        userProfile = UserProfile("Test", null, null)
        
        val threadList3 = (0 until threads).map {
            Thread {
                for (i in 0 until iterations) {
                    try {
                        method3_tempAssignment()
                        if (i % 100 == 0) {
                            userProfile = if (i % 200 == 0) null else UserProfile(
                                "User$i", null, null
                            )
                        }
                    } catch (e: Exception) {
                        exceptions3++
                    }
                }
            }
        }
        
        threadList3.forEach { it.start() }
        threadList3.forEach { it.join() }
        
        println("连续?. 异常次数: $exceptions1")
        println("?.let 异常次数: $exceptions2")
        println("临时赋值 异常次数: $exceptions3")
    }
}