package com.cousinware.arete.utils.verification;

public class LoginThread implements Runnable{

    public static boolean invoked = false;

    @Override
    public void run() {
        //create logins creen and basic while loop to keep thread alive
        new LoginGui();
        invoked = true;
        while (true) {

        }
    }
}
