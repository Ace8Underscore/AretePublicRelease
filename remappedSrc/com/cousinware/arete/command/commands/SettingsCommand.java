package com.cousinware.arete.command.commands;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.command.Command;
import com.cousinware.arete.module.Module;
import com.cousinware.arete.utils.settings.*;
import org.jetbrains.annotations.NotNull;

public class SettingsCommand extends Command {
    @Override
    public void onCommand(String[] args) throws Exception {
        Module module = AreteClient.moduleManager.getModuleByName(args[1]);
        Setting setting = AreteClient.settingManager.getSettingByName(module, args[2]);


        if (args.length < 4 || args[3].equalsIgnoreCase(" ")) {
            Command.sendClientSideMessage(module.getName() + " " + setting.getName() + " Value: " + setting.getValue());
            return;
        }

        String arg = args[3];

        if (setting instanceof BoolSetting) {
            if (arg.equalsIgnoreCase("true")) ((BoolSetting) setting).setValue(true);
            else if (arg.equalsIgnoreCase("false")) ((BoolSetting) setting).setValue(false);
            else {
                Command.sendClientSideMessage("Incorrect Value");
                return;
            }

        } else if (setting instanceof DoubleSetting) {
            ((DoubleSetting) setting).setValue(Double.parseDouble(arg));

        } else if (setting instanceof IntSetting) {
            ((IntSetting) setting).setValue(Integer.parseInt(arg));

        } else if (setting instanceof ModeSetting) {
            ((ModeSetting) setting).setValue(arg);
        }

        Command.sendClientSideMessage("Set " + module.getName() + " " + setting.getName() + " To " + arg);
    }

    @Override
    public String commandHelpMessage() {
        return "Used to change settings for a module";
    }

    @Override
    public @NotNull String commandUsage() {
        return "Settings (Module) (Setting) (Value)";
    }

    @Override
    public String[] commandCallName() {
        return new String[] {"Setting", "Settings"};
    }
}
