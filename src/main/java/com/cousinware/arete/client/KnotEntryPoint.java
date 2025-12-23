package com.cousinware.arete.client;

import com.cousinware.arete.utils.file.ConfigFolder;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

import java.io.IOException;

public class KnotEntryPoint implements PreLaunchEntrypoint {

    @Override
    public void onPreLaunch() {

        Runtime.getRuntime().addShutdownHook(new ConfigFolder.ShutDown());
    }
}
