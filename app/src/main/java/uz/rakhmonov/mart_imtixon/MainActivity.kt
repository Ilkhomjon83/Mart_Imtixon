package uz.rakhmonov.mart_imtixon

import android.media.MediaPlayer
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import uz.rakhmonov.mart_imtixon.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var runnable: Runnable
    private var handler: Handler = Handler()
    lateinit var reference: DatabaseReference
    lateinit var database: FirebaseDatabase

    lateinit var mediaPlayer: MediaPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        database= FirebaseDatabase.getInstance()
        reference=database.getReference("isPlaying")

        val ringtoneUri: Uri? = RingtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_RINGTONE)


            mediaPlayer = MediaPlayer()
        mediaPlayer.setDataSource(this, ringtoneUri!!)

        mediaPlayer.prepare()

        reference.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var isPlaying= snapshot.getValue<Boolean>()
                if (isPlaying!=null){


                    if (isPlaying){
                        mediaPlayer.start()
                        binding.play.visibility=View.GONE
                        binding.pause.visibility=View.VISIBLE
                        Toast.makeText(this@MainActivity, "Playyyiiiiinggg", Toast.LENGTH_SHORT).show()

                    }else{
                        mediaPlayer.pause()
                        binding.pause.visibility=View.GONE
                        binding.play.visibility=View.VISIBLE
                        Toast.makeText(this@MainActivity, "Sttoooooop", Toast.LENGTH_SHORT).show()

                    }
                }}


            override fun onCancelled(error: DatabaseError) {

            }
        })

            // Use the ringtoneUri as needed
//        }

        // Media playerni yaratish

        // "Play" tugmasini bosganda musiqa ijro etish
        binding.play.setOnClickListener {
            reference.setValue(true)

        }

        // "Pause" tugmasini bosganda musiqani to'xtatish
        binding.pause.setOnClickListener {
            reference.setValue(false)

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
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // SeekBar o'zgartirishni tugatganda MediaPlayer qayta boshlanadi
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








