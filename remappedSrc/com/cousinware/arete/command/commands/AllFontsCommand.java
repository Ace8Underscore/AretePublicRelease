package com.cousinware.arete.command.commands;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.command.Command;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class AllFontsCommand extends Command {
    @Override
    public void onCommand(String[] args) throws Exception {

        Command.sendClientSideMessage(AreteClient.fontManager.getLoadedFontNames().toString());

    }

    @Override
    public String commandHelpMessage() {
        return "Shows all the available fonts for the client";
    }

    @Override
    public @NotNull String commandUsage() {
        return "";
    }

    @Override
    public String[] commandCallName() {
        return new String[]{"AllFonts"};
    }

    private ClassLoader getContextClassLoader() {
        return this.getClass().getClassLoader();
    }
}
