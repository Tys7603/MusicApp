package com.example.musicapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.SeekBar;

import com.example.musicapp.databinding.ActivityMainBinding;

import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize the MediaPlayer
        mediaPlayer = MediaPlayer.create(this, R.raw.beat);

       onPlayMusic();
       onPauseMusic();
       setTimeTotalSong();
       setOnSeekBar();

    }

    public void onPlayMusic(){
        binding.play.setOnClickListener(view -> {
            binding.play.setVisibility(View.GONE);
            binding.stop.setVisibility(View.VISIBLE);
            mediaPlayer.start();
            upTimeSong();
        });
    }

    public void onPauseMusic(){
        binding.stop.setOnClickListener(view -> {
            binding.stop.setVisibility(View.GONE);
            binding.play.setVisibility(View.VISIBLE);
            mediaPlayer.pause();
        });
    }

    public void setTimeTotalSong(){
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss");
        binding.tvTimeTotal.setText(dateFormat.format(mediaPlayer.getDuration()));
        binding.seekBar.setMax(mediaPlayer.getDuration());
    }

    public void setOnSeekBar(){
        binding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });
    }

    public void upTimeSong(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss");
                binding.tvTimeCurrent.setText(dateFormat.format(mediaPlayer.getCurrentPosition()));
                binding.seekBar.setProgress(mediaPlayer.getCurrentPosition());
                handler.postDelayed(this, 500);
            }
        }, 100);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}