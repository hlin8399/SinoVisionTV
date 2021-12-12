package com.han.sinovisiontv

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.han.sinovisiontv.databinding.ActivityMainBinding

class MainActivity: Activity(), Player.Listener {

    private lateinit var binding: ActivityMainBinding

    private val videoPlayer by lazy {
        ExoPlayer.Builder(this)
            .build().apply {
                val mediaSource = HlsMediaSource.Factory(DefaultDataSource.Factory(this@MainActivity))
                    .createMediaSource(MediaItem.fromUri(VIDEO_URL))
                setMediaSource(mediaSource)
                addListener(this@MainActivity)
                prepare()
                playWhenReady = true
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.videoPlayerView.player = videoPlayer
    }

    override fun onPause() {
        super.onPause()

        videoPlayer.playWhenReady = false
        videoPlayer.removeListener(this)
        videoPlayer.stop()
        videoPlayer.release()

        if (!isFinishing) {
            finish()
        }
    }

    override fun onPlayerError(error: PlaybackException) {
        super.onPlayerError(error)
        showAlert()
    }






    private fun showAlert() {
        AlertDialog.Builder(this)
            .setTitle("错误！")
            .setMessage("无法播放视频。请重启应用重试")
            .setPositiveButton("好的") { _, _ ->
                finish()
            }
            .create()
            .show()
    }


    companion object {
        private const val VIDEO_URL = "https://cdnts.sinovision.net/livestream.m3u8"
    }
}