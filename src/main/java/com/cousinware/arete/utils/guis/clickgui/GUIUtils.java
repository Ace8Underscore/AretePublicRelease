package com.cousinware.arete.utils.guis.clickgui;

import com.cousinware.arete.utils.MinecraftInterface;

public class GUIUtils implements MinecraftInterface {

    public static double lastMouseX = -1;
    public static double lastMouseY = -1;

    public static int getMouseX() {
        return (int) lastMouseX;
    }

    public static int getMouseY() {
        return (int) lastMouseY;

    }

    public static void setMousePos(double x, double y) {
        GUIUtils.lastMouseX = x;
        GUIUtils.lastMouseY = y;
    }
}
