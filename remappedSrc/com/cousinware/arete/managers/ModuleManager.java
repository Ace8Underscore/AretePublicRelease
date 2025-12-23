package com.cousinware.arete.managers;

import com.cousinware.arete.module.Client.*;
import com.cousinware.arete.module.Module;
import com.cousinware.arete.module.combat.Aura;
import com.cousinware.arete.module.combat.Aura2;
import com.cousinware.arete.module.misc.*;
import com.cousinware.arete.module.player.*;
import com.cousinware.arete.module.render.Box;
import com.cousinware.arete.module.render.CustomMouse;
import com.cousinware.arete.module.render.ModuleList;
import com.cousinware.arete.module.render.NoRender;
import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ModuleManager {

    ArrayList<Module> modules;

    public boolean priorityUpdated = false;

    public ModuleManager() {
        modules = new ArrayList<>();
        addModule();
    }

    public ArrayList<Module> getModules() {
        return modules;
    }

    public void addModule() {

        //Client
        modules.add(new Test());
        modules.add(new ClickGui());
        modules.add(new FontManagerModule());
        modules.add(new Game());
        modules.add(new Core());
        modules.add(new OutgoingPackets());

        //Combat
        modules.add(new Aura());
        modules.add(new Aura2());

        //misc
        modules.add(new ChestStealer());
        modules.add(new EmpChat());
        modules.add(new ExtraTab());
        modules.add(new LogoutCoords());
        modules.add(new MaceTweaks());
        modules.add(new Welcomer());

        //PLAYER
        modules.add(new AntiVoid());
        modules.add(new Freecam());
        modules.add(new Scaffold());
        modules.add(new Scaffold2());
        modules.add(new Step());

        //RENDER
        modules.add(new Box());
        modules.add(new CustomMouse());
        modules.add(new ModuleList());
        modules.add(new NoRender());
    }

    public Module getModuleByName(String s) {
        for (Module module : modules) {
            if (module.getName().equalsIgnoreCase(s)) {
                return module;
            }
        }
        return null;
    }

    public void processTicks() {
        if (MinecraftClient.getInstance().player == null || MinecraftClient.getInstance().world == null) return;
//        if (priorityUpdated) {
//            modules.sort(Comparator.comparing(Module::getPriority));
//            Collections.reverse(modules);
//            priorityUpdated = false;
//        }

        for (Module module : modules) {
            if (module.isEnabled()) module.onUpdate();
        }
    }

    public void processPostTicks() {
        if (MinecraftClient.getInstance().player == null || MinecraftClient.getInstance().world == null) return;
        for (Module module : modules) {
            if (module.isEnabled()) module.onPostUpdate();
        }
    }
}
