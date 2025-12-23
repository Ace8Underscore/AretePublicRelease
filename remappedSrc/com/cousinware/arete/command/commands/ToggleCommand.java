package com.cousinware.arete.command.commands;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.command.Command;
import com.cousinware.arete.module.Module;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;

public class ToggleCommand extends Command {
    @Override
    public void onCommand(String[] args) throws Exception {
        Module module = AreteClient.moduleManager.getModuleByName(args[1]);
        if (module == null) Command.sendClientSideMessage("Module "+ Formatting.AQUA + args[1]+ Formatting.GRAY + " not found");
        else {
            Command.sendClientSideMessage("Toggled " + Formatting.AQUA + args[1]);
            module.toggle();
        }
    }

    @Override
    public String commandHelpMessage() {
        return "Turns on modules in the client";
    }

    @Override
    public @NotNull String commandUsage() {
        return getPrefix() + "Toggle (Module)";
    }

    @Override
    public String[] commandCallName() {
        return new String[] {"Toggle"};
    }
}
