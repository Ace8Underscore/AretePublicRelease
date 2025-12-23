package com.cousinware.arete.utils.rendering.nvg.string;

import com.cousinware.arete.utils.Timer;
import lombok.Getter;
import lombok.Setter;

import java.util.Random;

@Getter
@Setter
public class RevealingString {

    String fullString;
    String displayString;
    String lastAppendMessage = "";
    Timer timer;
    boolean revealing = true;
    int delayMs = 250;
    int delayOffsetMs = 25;


    public RevealingString(String fullString) {
        this.fullString = fullString;
        timer = new Timer(delayMs);
        displayString = fullString.substring(0, 1);
    }

    public RevealingString() {
        timer = new Timer(delayMs);
        displayString = "placeholder";
        fullString = "placeholder";
    }


    public void setFullString(String string) {
        if (!fullString.contains(string)) {
            fullString = string;
            displayString = fullString.substring(0, 1);
            lastAppendMessage = "";
        }
    }

    public void appendToFullString(String string) {
        if (!lastAppendMessage.equals(string)) {
            fullString = fullString + string;
            lastAppendMessage = string;
        }
    }

    public String getTextAndTick() {
        return getUpdatedDisplay();

    }


//    private String getUpdatedDisplay() {
//        if (timer.canTick()) {
//            if (displayString.length() < fullString.length() && displayString.length() > 1) {
//                int offSet = revealing ? 1 : -1;
//                displayString = fullString.substring(0, displayString.length() + offSet);
//                return displayString;
//            }
//            if (displayString.isEmpty() || displayString.length() == fullString.length()) {
//                revealing = !revealing;

    /// /                if (!revealing) displayString = fullString.substring(0, displayString.length() - 1);
    /// /                else displayString = fullString.substring(0, 1);
//                System.out.println("switched");
//            }
//
//        }
//        return displayString;
//    }
    private String getUpdatedDisplay() {
        if (timer.canTick()) {
            //random ms offset
            int randomOffset = new Random().nextInt(delayOffsetMs - -delayOffsetMs + 1) + -delayOffsetMs;
            timer.setDelay(delayMs + randomOffset);

            if (displayString.length() < fullString.length() && !displayString.isEmpty()) {
                int offSet = revealing ? 1 : -1;
                displayString = fullString.substring(0, displayString.length() + offSet);
                return displayString;
            }
            if (displayString.isEmpty() || displayString.length() == fullString.length()) {
                revealing = !revealing;
                if (!revealing) displayString = fullString.substring(0, displayString.length() - 1);
                else displayString = fullString.substring(0, 1);
            }

        }
        return displayString;
    }
}
