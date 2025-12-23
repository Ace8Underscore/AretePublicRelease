package com.cousinware.arete.managers;

import com.cousinware.arete.utils.verification.LoginThread;

public class ThreadManager {

    public static Thread loginThread = new Thread(new LoginThread());

    public ThreadManager() {
    }

}
