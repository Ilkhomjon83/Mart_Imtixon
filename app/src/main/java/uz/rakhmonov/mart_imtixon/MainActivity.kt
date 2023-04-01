package uz.rakhmonov.mart_imtixon

import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.SeekBar
import uz.rakhmonov.mart_imtixon.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var runnable: Runnable
    private var handler: Handler = Handler()

    lateinit var mediaPlayer: MediaPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val ringtoneUri: Uri? = RingtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_RINGTONE)


            mediaPlayer = MediaPlayer.create(this, R.raw.my_music)

            // Use the ringtoneUri as needed
//        }

        // Media playerni yaratish

        // "Play" tugmasini bosganda musiqa ijro etish
        binding.play.setOnClickListener {
            mediaPlayer.start()
            binding.play.visibility=View.GONE
            binding.pause.visibility=View.VISIBLE

        }

        // "Pause" tugmasini bosganda musiqani to'xtatish
        binding.pause.setOnClickListener {
            mediaPlayer.pause()
            binding.pause.visibility=View.GONE
            binding.play.visibility=View.VISIBLE
        }
        val seekBar = findViewById<SeekBar>(R.id.my_seekbar)
        seekBar.max = mediaPlayer.duration
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // SeekBar o'zgartirishni boshlaganda MediaPlayer to'xtatiladi
                mediaPlayer.pause()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // SeekBar o'zgartirishni tugatganda MediaPlayer qayta boshlanadi
                mediaPlayer.start()
            }
        })

        // Runnable obyektini yaratish
        runnable = Runnable {
            seekBar.progress = mediaPlayer.currentPosition
            handler.postDelayed(runnable, 1000)
        }



        // Runnable obyektini boshlash
        handler.postDelayed(runnable, 1000)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        handler.removeCallbacks(runnable)
    }



}








