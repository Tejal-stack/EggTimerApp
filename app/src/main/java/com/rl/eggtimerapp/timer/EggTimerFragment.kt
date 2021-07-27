package com.rl.eggtimerapp.timer

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.firebase.messaging.FirebaseMessaging
import com.rl.eggtimerapp.databinding.FragmentEggTimerBinding
import com.rl.eggtimerapp.util.createChannel
import com.rl.eggtimerapp.util.getNotificationManager
import com.rl.eggtimerapp.util.subscribeToTopicBreakfast


class EggTimerFragment : Fragment() {

    private val viewModel: EggTimerViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentEggTimerBinding.inflate(inflater, container, false)
        .apply {
            eggTimerViewModel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        .root


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.run {
            createEggTimerNotificationChannel()
            createBreakfastNotificationChannel()
            subscribeToTopicBreakfast()
        }
    }

    private fun Context.subscribeToTopicBreakfast() {
        FirebaseMessaging.getInstance().subscribeToTopicBreakfast(this)
    }

    private fun Context.createBreakfastNotificationChannel() =

        getNotificationManager().createChannel(
            "fcm_default_channel",
            "Breakfast",
            "Breakfast reminder"
        )


    private fun Context.createEggTimerNotificationChannel() =

        getNotificationManager().createChannel(
            "egg_channel",
            "Egg",
            "Time for breakfast"

        )


    companion object {

        fun newInstance() = EggTimerFragment()
    }
}