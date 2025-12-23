package com.cousinware.arete.utils.guis.clickgui.defaultguis.maingui;

public class Velocity {

    double xV;
    double yV;

    double slipperyness = .125;

    public Velocity(double xV, double yV) {
        this.xV = xV;
        this.yV = yV;
    }

    public void applyVelocity(double xV, double yV) {
        this.xV += xV;
        this.yV += yV;
    }

    public void setVelocity(double xV, double yV) {
        this.xV = xV;
        this.yV = yV;
    }

    public void tick() {
        if (xV <= slipperyness && xV >= -slipperyness) {
            xV = 0;
        } else {
            if (xV!= 0) this.xV -= slipperyness;
        }
        if (yV <= slipperyness && yV >= -slipperyness) {
            yV = 0;
        } else {
            if (yV!=0) this.yV -= slipperyness;
        }
    }

    public double getxV() {
        return xV;
    }


    public void flipXVel(boolean slowDown) {
        xV = -xV;
        if (xV < 0) xV += 1;
        else if (xV > 0) xV -=1;
        if(slowDown) slipperyness *= 1.25;
    }

    public void flipYVel(boolean slowDown) {
        yV = -yV;
        if (yV < 0) yV += 1;
        else if (yV > 0) yV -=1;
        if(slowDown) slipperyness *= 1.25;
    }

    public double getyV() {
        return yV;
    }
}
