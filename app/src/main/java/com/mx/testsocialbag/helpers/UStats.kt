package com.mx.testsocialbag.helpers

import android.annotation.SuppressLint
import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.util.Log
import com.mx.testsocialbag.pojos.AppUsageInfo


object UStats {


    val TAG = UStats::class.java.simpleName

    fun getUsageStatsList(
        context: Context,
        startTime: Long,
        endTime: Long
    ): ArrayList<AppUsageInfo> {
        val usm = getUsageStatsManager(context)


        val usageEvents = usm.queryEvents(startTime, endTime)
        var currentEvent: UsageEvents.Event
        val map: HashMap<String, AppUsageInfo> = HashMap()
        val sameEvents: HashMap<String, List<UsageEvents.Event>> = HashMap()


        while (usageEvents.hasNextEvent()) {
            currentEvent = UsageEvents.Event()
            usageEvents.getNextEvent(currentEvent)
            if (currentEvent.eventType == UsageEvents.Event.ACTIVITY_RESUMED ||
                currentEvent.eventType == UsageEvents.Event.ACTIVITY_PAUSED
            ) {

                val key: String = currentEvent.packageName

                if (!map.containsKey(key)) {
                    val appUsageInfo = AppUsageInfo()
                    appUsageInfo.packageName = key
                    map[key] = appUsageInfo
                    sameEvents[key] = ArrayList()

                }

                val event = sameEvents.get(key) as ArrayList
                event.add(currentEvent);

            }
        }

        for ((_, value) in sameEvents) {
            val totalEvents = value.size
            if (totalEvents > 1) {
                for (i in 0 until totalEvents - 1) {
                    val E0 = value[i]
                    val E1 = value[i + 1]
                    if (E1.eventType == 1 || E0.eventType == 1) {
                        map[E1.packageName]!!.launchCount =
                            map[E1.packageName]!!.launchCount!! + 1
                    }
                    if (E0.eventType == 1 && E1.eventType == 2) {
                        val diff = E1.timeStamp - E0.timeStamp
                        map[E0.packageName]!!.timeInForeground =
                            map[E0.packageName]!!.timeInForeground!! + diff
                    }
                }
            }


            if (value[0].eventType == 2) {
                val diff: Long = value[0].timeStamp - startTime
                map[value[0].packageName]!!.timeInForeground =
                    map[value[0].packageName]!!.timeInForeground!! + diff
            }


            if (value[totalEvents - 1].eventType == 1) {
                val diff: Long = endTime - value[totalEvents - 1].timeStamp
                map[value[totalEvents - 1].packageName]!!.timeInForeground =
                    map[value[totalEvents - 1].packageName]!!.timeInForeground!! + diff
            }
        }


        val list = ArrayList(map.values)

        Log.e(
            TAG, "LATEST APPS-------"
        )

        list.forEach {
            Log.e(
                TAG, "${it.packageName} ${it.timeInForeground}"
            )
        }

        return list
    }


    @SuppressLint("WrongConstant")
    private fun getUsageStatsManager(context: Context): UsageStatsManager {
        return context.getSystemService("usagestats") as UsageStatsManager
    }
}