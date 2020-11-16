package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import com.example.finalproject.databinding.ActivityExamBinding;
import com.example.finalproject.models.MyDatabase;

import java.io.File;
import java.io.IOException;

public class ExamActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener {
    private ActivityExamBinding binding;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityExamBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build());
        mediaPlayer.setOnPreparedListener(this);
        binding.btnClick.setOnClickListener((view) -> {
            try {
                AssetFileDescriptor afd = getAssets().openFd("course_1/part1_direction.mp3");
                if (mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                }
                try {
                    mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                    mediaPlayer.prepareAsync();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        MyDatabase.getInstance(this).courseDAO().getCourseAndQuestion(1).forEach(c -> {
            c.questions.forEach(q -> {
                Log.e("TAG", q.getDescription());
                Log.e("TAG", q.getAnswer());
                Log.e("TAG", q.getPart()+"");
            });
        });
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }

    @Override
    protected void onDestroy() {
        if (mediaPlayer.isPlaying())
            mediaPlayer.stop();
        super.onDestroy();
    }
}