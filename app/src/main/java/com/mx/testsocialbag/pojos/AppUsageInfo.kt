package com.mx.testsocialbag.pojos


data class AppUsageInfo(
    var packageName: String? = null,
    var timeInForeground: Long? = null,
    var launchCount: Long? = null

) {


    init {
        timeInForeground = 0
        launchCount = 0

    }

    fun toHash(): HashMap<String, Any> {
        val hash: HashMap<String, Any> = HashMap()
        hash["packageName"] = packageName!!
        hash["timeInForeground"] = timeInForeground!!
        return hash
    }
}