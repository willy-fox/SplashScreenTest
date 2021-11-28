package com.example.splashscreentest

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.activity.ComponentActivity
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class SplashActivity:ComponentActivity() {
    var isLoading=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val splashScreen=installSplashScreen()
        splashScreen.setKeepVisibleCondition(SplashScreen.KeepOnScreenCondition { !isLoading })

        //设置退出动画
        splashScreen.setOnExitAnimationListener {
                splashScreenViewProvider->
            val onExit={splashScreenViewProvider.remove()}
            showSplashExitAnimator(splashScreenViewProvider.view,onExit)
            showSplashIconExitAnimator(splashScreenViewProvider.iconView,onExit)
        }

        setContentView(R.layout.activity_main)
        doLoadingWork()
    }
    fun showSplashExitAnimator(splashScreenView: View,onExit:()->Unit={}){
        val alphaOut=ObjectAnimator.ofFloat(
            splashScreenView,View.ALPHA,1f,0f)
        val slideUp=ObjectAnimator.ofFloat(
            splashScreenView,View.SCALE_X,1f,0.3f
        )
        AnimatorSet().run {
            duration=1000L
            interpolator=DecelerateInterpolator()
            playTogether(alphaOut,slideUp)
            start()
        }
    }
    fun showSplashIconExitAnimator(icon:View,onExit: () -> Unit){
        val alphaOut=ObjectAnimator.ofFloat(
            icon,View.ALPHA,1f,0f)
        val slideUp=ObjectAnimator.ofFloat(
            icon,View.TRANSLATION_Y,0f,-icon.height.toFloat()*2f
        )
        AnimatorSet().run {
            duration=1000L
            interpolator=DecelerateInterpolator()
            playTogether(alphaOut,slideUp)
            doOnEnd {
                onExit()
                }
            start()
        }
    }
//    fun toMain(){
//        val intent= Intent(this,MainActivity::class.java)
//        startActivity(intent)
//        finish()
//    }
    fun doLoadingWork(){
        Handler(Looper.getMainLooper()).postDelayed({
            isLoading=true
            },3000)
    }
}