package com.example.musicapp;

public class Lyric {
    private String text;
    private int startTime;

    public Lyric(String text, int startTime) {
        this.text = text;
        this.startTime = startTime;
    }

    public String getText() {
        return text;
    }

    public int getStartTime() {
        return startTime;
    }
}
