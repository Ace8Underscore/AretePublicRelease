package com.cousinware.arete.command.commands;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.command.Command;
import com.cousinware.arete.module.Module;
import net.minecraft.client.util.InputUtil;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import java.util.Locale;

public class BindCommand extends Command {
    @Override
    public void onCommand(String[] args) throws Exception {

        for (Module module : AreteClient.moduleManager.getModules()) {
            if (module.getName().equalsIgnoreCase(args[1])) {
                if (args[2] == null) return;
                Command.sendClientSideMessage(args[2]);
                String key1 = args[2];
                if (key1.equalsIgnoreCase("MB1") || key1.equalsIgnoreCase("MB2") || key1.equalsIgnoreCase("MB3")) {
                    if (key1.equalsIgnoreCase("MB1")) module.setKeybind(GLFW.GLFW_MOUSE_BUTTON_1);
                    else if (key1.equalsIgnoreCase("MB2")) module.setKeybind(GLFW.GLFW_MOUSE_BUTTON_2);
                    else if (key1.equalsIgnoreCase("MB3")) module.setKeybind(GLFW.GLFW_MOUSE_BUTTON_3);
                    return;

                }

                int key = InputUtil.fromTranslationKey("key.keyboard." + args[2].toLowerCase(Locale.ENGLISH)).getCode();
                Command.sendClientSideMessage("HERE1");
                module.setKeybind(key);
            }
        }

    }

    @Override
    public String commandHelpMessage() {
        return "Binds modules to a certain key on the keyboard";
    }

    @Override
    public @NotNull String commandUsage() {
        return "Bind (Module) (Key)" ;
    }

    @Override
    public String[] commandCallName() {
        return new String[] {"bind"};
    }
}
