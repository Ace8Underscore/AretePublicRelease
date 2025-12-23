package com.cousinware.arete.utils;

public class Timer {

    private long startTime;

    private int fps = 0;
    private int ticksExecuted = 0;
    private long tpsStartTime;
    private int delay = -1;

    String timerName = "Timer";

    public Timer() {
        startTimer();
    }

    public Timer(int delayMS) {
        this.delay = delayMS;
        startTimer();
    }
    public Timer(int delayMS, String name) {
        this.delay = delayMS;
        this.timerName = name;
        startTimer();

    }

    private void startTimer() {
        this.startTime = System.currentTimeMillis();
        this.tpsStartTime = System.currentTimeMillis();

    }

    public long timePassed() {
        return System.currentTimeMillis() - this.startTime;
    }

    public boolean canTick() {
        if (timePassed() > delay) {
            //simple debug
            this.ticksExecuted++;
            //set time to current time so we can get ready to time for next ms
            this.startTime = System.currentTimeMillis();
            return true;
        }
        return false;
    }

    public int getTps() {
        if (System.currentTimeMillis() - this.tpsStartTime >= 1000) {
            int temp = this.fps;
            this.fps = 0;
            this.tpsStartTime = System.currentTimeMillis();
            return temp;
        } else {
            this.fps++;
        }
        return -1;
    }

    public int getTicksExecuted() {
        return this.ticksExecuted;
    }

    public void reset() {
        this.ticksExecuted = 0;
        this.startTime = System.currentTimeMillis();
    }

}