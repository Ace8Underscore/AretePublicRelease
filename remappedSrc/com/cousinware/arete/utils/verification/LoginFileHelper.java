package com.cousinware.arete.utils.verification;

import com.cousinware.arete.managers.ThreadManager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class LoginFileHelper {

    static String userLocation =  System.getProperty("user.dir");
    public static File userFile = new File(userLocation + File.separator + "AreteVerify.txt");
    boolean loginScreen = false;

    public LoginFileHelper() throws IOException {

        //Start the LoginThread which has the GUI we do this because the main thread we put to sleep to stop MC from starting up
        ThreadManager.loginThread = new Thread(new LoginThread());
        ThreadManager.loginThread.start();
        //gens AreteVerify File if one does not exist
        genFile();
        //puts MC main thread to sleep until we get past the login screen
        suspendThread();
        // if the player clicks the login button we save their data to a file so we dont require a user and pass everytime they login
        saveFileWithData();

    }

    public void genFile() throws IOException {
        loginScreen = true;
        if (!userFile.exists()) {
            //make file
            userFile.createNewFile();
            //popup login gui so we can get users credentials
            System.out.println("login screen");

        }
    }

    public void suspendThread() {
        try {
            while (loginScreen) {
                if (!ThreadManager.loginThread.isAlive()) loginScreen = false;
                //System.out.println("sleeping");
                Thread.sleep(1);
            }
        }catch (Exception e) {

        }
    }

    public void saveFileWithData() {
        if (!LoginThread.invoked) return;
        try {
            FileWriter writer = new FileWriter(userFile.getAbsoluteFile());
            writer.write(LoginGui.username + ":" + LoginGui.password);
            writer.close();
        } catch (Exception e) {
            System.out.println("Failed to writte");
        }
    }

}
