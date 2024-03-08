package com.example.musicapp;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

import com.example.musicapp.databinding.ActivityMainBinding;

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

    }

    public void onPlayMusic(){
        binding.play.setOnClickListener(view -> {
            binding.play.setVisibility(View.GONE);
            binding.stop.setVisibility(View.VISIBLE);
            mediaPlayer.start();
        });
    }

    public void onPauseMusic(){
        binding.stop.setOnClickListener(view -> {
            binding.stop.setVisibility(View.GONE);
            binding.play.setVisibility(View.VISIBLE);
            mediaPlayer.pause();
        });
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