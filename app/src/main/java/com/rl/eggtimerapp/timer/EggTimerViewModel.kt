package com.rl.eggtimerapp.timer

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.CountDownTimer
import android.os.SystemClock
import android.provider.AlarmClock
import androidx.core.app.AlarmManagerCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rl.eggtimerapp.R
import com.rl.eggtimerapp.receiver.AlarmReciever
import com.rl.eggtimerapp.util.cancelNotifications
import com.rl.eggtimerapp.util.getNotificationManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EggTimerViewModel(private val app: Application) : AndroidViewModel(app) {



    val REQUEST_CODE = 0
    val TRIGGER_TIME = "TRIGGER_AT"


    private val minute: Long = 60_000L
    private  val second: Long = 1_000L

    private val timerLengthOptions: IntArray
    private val notifyPendingIntent: PendingIntent


    private val alarmManager = app.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    private val prefs =
        app.getSharedPreferences("com.rl.eggtimerapp.timer.eggtimer",Context.MODE_PRIVATE)

    private val notifyIntent = Intent(app,AlarmReciever::class.java)

    private val _timeSelection = MutableLiveData<Int>()

    val timeSelection: LiveData<Int>
        get() = _timeSelection

    private val _elapsedTime = MutableLiveData<Long>()
    val elapsedTime: LiveData<Long>
        get() = _elapsedTime


    private var _alarmOn = MutableLiveData<Boolean>()
    val isAlarmOn: LiveData<Boolean>
     get() = _alarmOn


    private lateinit var timer: CountDownTimer


    init {

        _alarmOn.value = PendingIntent.getBroadcast(

            getApplication(),
            REQUEST_CODE,
            notifyIntent,
            PendingIntent.FLAG_NO_CREATE
        ) != null



        notifyPendingIntent = PendingIntent.getBroadcast(

            getApplication(),
            REQUEST_CODE,
            notifyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT


        )


        timerLengthOptions = app.resources.getIntArray(R.array.minutes_array)

        if(_alarmOn.value!!){

            createTimer()
        }

    }



    fun setAlarm(isChecked: Boolean){

        when{

            true -> timeSelection.value?.let { startTimer(it) }
            false ->    cancelNotification()

        }

    }


    fun setTimeSelected(timerLengthSelection: Int){
        _timeSelection.value = timerLengthSelection
    }

    private fun cancelNotification() {
        TODO("Not yet implemented")
    }

    private fun startTimer(timerLengthSelection: Int) {

        _alarmOn.value?.let {

            if (!it){

                _alarmOn.value = true
                val selectedInterval = when(timerLengthSelection){
                    0 -> second *10
                    else -> timerLengthOptions[timerLengthSelection] * minute

                }

                val triggerTime = SystemClock.elapsedRealtime() + selectedInterval

                app.applicationContext.getNotificationManager().cancelNotifications()

                AlarmManagerCompat.setExactAndAllowWhileIdle(

                    alarmManager,
                    AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    triggerTime,
                    notifyPendingIntent

                )

                viewModelScope.launch {

                    saveTime(triggerTime)
                }

            }

        }

        createTimer()

    }

    private fun createTimer() {
        viewModelScope.launch {

            val triggerTime = loadTime()

            timer = object :CountDownTimer(triggerTime,second){
                override fun onTick(milliUntilFinished: Long) {
                    _elapsedTime.value= triggerTime - SystemClock.elapsedRealtime()
                    if (_elapsedTime.value !! <=0){

                        resetTimer()
                    }
                }

                override fun onFinish() {
                   resetTimer()
                }
            }

            timer.start()

        }
    }


    private fun resetTimer(){
        timer.cancel()
        _elapsedTime.value = 0
        _alarmOn.value = false

    }

    private suspend fun saveTime(triggerTime: Long) =
        withContext(Dispatchers.IO){
            prefs.edit().putLong(TRIGGER_TIME,triggerTime).apply()
        }

    private suspend fun loadTime():Long =
        withContext(Dispatchers.IO){
            prefs.getLong(TRIGGER_TIME,0)
        }


}