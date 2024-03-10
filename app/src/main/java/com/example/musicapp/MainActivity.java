package com.example.musicapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.musicapp.databinding.ActivityMainBinding;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private ActivityMainBinding binding;
    private List<YourData> mLyrics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mLyrics = new ArrayList<>();
        // Initialize the MediaPlayer
        // c1
        // mediaPlayer = MediaPlayer.create(this, R.raw.beat);
        // c2

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            String URL = "https://storage.googleapis.com/ikara-storage/tmp/beat.mp3";
            mediaPlayer.setDataSource(URL);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(mediaPlayer -> {
                onPlayMusic();
                callApiLyrics();
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

//       onPlayMusic();
        onPauseMusic();
//       setTimeTotalSong();
        setOnSeekBar();
    }

    public void onPlayMusic(){
        binding.play.setOnClickListener(view -> {
            binding.play.setVisibility(View.GONE);
            binding.stop.setVisibility(View.VISIBLE);
            if (mediaPlayer != null) {
                mediaPlayer.start();
                upTimeSong();
                setTimeTotalSong();
            }
        });
    }

    public void onPauseMusic(){
        binding.stop.setOnClickListener(view -> {
            binding.stop.setVisibility(View.GONE);
            binding.play.setVisibility(View.VISIBLE);
            if (mediaPlayer != null) {
                mediaPlayer.pause();
            }
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
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    @SuppressLint("SimpleDateFormat")
                    SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss");
                    binding.tvTimeCurrent.setText(dateFormat.format(mediaPlayer.getCurrentPosition()));
                    binding.seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    mediaPlayer.setOnCompletionListener(mp -> {
                        binding.stop.setVisibility(View.GONE);
                        binding.play.setVisibility(View.VISIBLE);
                    });
                    handler.postDelayed(this, 500);
                }
            }
        }, 100);
    }

    public void callApiLyrics(){
        ApiClient.getClient().create(ApiService.class).getData().enqueue(new Callback<YourXMLModel>() {
            @Override
            public void onResponse(Call<YourXMLModel> call, Response<YourXMLModel> response) {
                if (response.isSuccessful()){
                    YourXMLModel data = response.body();
                    if (data.params != null){
                        for (Param param : data.params) {
                            if (param.yourData != null) {
                                mLyrics.addAll(param.yourData);
                            } else {
                                Log.d("TAG", "No data found in this param");
                            }
                        }
                        Log.d("TAG", "response" + mLyrics.toString());
                        binding.textView3.setText(mLyrics.get(0).getContent());
                        displayLyrics();
                    } else {
                        Log.d("TAG", "No params found in response");
                    }
                }else {
                    Log.d("TAG", "Response not successful");
                }
            }

            @Override
            public void onFailure(Call<YourXMLModel> call, Throwable t) {
                Log.e("TAG", "onFailure: " + t.getMessage());
            }
        });
    }

    private void displayLyrics() {
        new Thread(() -> {
            while (mediaPlayer.isPlaying()) {
                try {
                    Thread.sleep(100); // Kiểm tra mỗi 100ms
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    int currentMillis = mediaPlayer.getCurrentPosition(); // Thời gian hiện tại của âm thanh
                    YourData currentLyric = findCurrentLyric(currentMillis);

                    // Hiển thị lời nhạc trên giao diện người dùng
                    runOnUiThread(() -> {
//                        Log.e("TAG", "onRes: " + currentLyric.toString());
                        if (currentLyric != null) {
                            // Hiển thị lời nhạc trên TextView
                            SpannableString spannableString = new SpannableString(currentLyric.getContent());
                            spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, currentLyric.getContent().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            binding.textView3.setText(spannableString);
                        }
                    });
                }
            }
        }).start();
    }

    private YourData findCurrentLyric(int currentTime) {
        for (YourData lyric : mLyrics) {
            if (lyric.getTime() <= currentTime) {
                return lyric;
            }
        }
        return null;
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