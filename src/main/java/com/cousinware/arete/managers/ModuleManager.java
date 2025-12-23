package com.cousinware.arete.managers;

import com.cousinware.arete.module.Client.*;
import com.cousinware.arete.module.Module;
import com.cousinware.arete.module.combat.Aura2;
import com.cousinware.arete.module.combat.MaceTweaks;
import com.cousinware.arete.module.hud.*;
import com.cousinware.arete.module.misc.*;
import com.cousinware.arete.module.movement.ElytraFly;
import com.cousinware.arete.module.movement.LongJump;
import com.cousinware.arete.module.movement.Step;
import com.cousinware.arete.module.movement.Velocity;
import com.cousinware.arete.module.player.*;
import com.cousinware.arete.module.render.EntityESP;
import com.cousinware.arete.module.render.NoRender;
import com.cousinware.arete.module.render.RenderDebug;
import com.cousinware.arete.module.render.StorageESP;
import com.cousinware.arete.module.world.*;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;

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
        modules.add(new NewClickGui());
        modules.add(new NewHudClickGui());
        modules.add(new Core());
        modules.add(new Rendering());

        //Combat
        modules.add(new Aura2());
        modules.add(new MaceTweaks());

        //misc
        modules.add(new AutoIgnite());
        modules.add(new AutoMapArt());
        modules.add(new AutoRename());
        modules.add(new AutoSign());
        modules.add(new ChestStealer());
        modules.add(new ExtraTab());
        modules.add(new LogoutCoords());
        modules.add(new NewPlayerDetector());



        //PLAYER
        modules.add(new AntiVoid());
        modules.add(new AutoClicker());
        modules.add(new AutoRightClick());
        modules.add(new FireworkTweaks());
        modules.add(new Tracker());


        //RENDER
        modules.add(new EntityESP());
        modules.add(new NoRender());
        if (FabricLoader.getInstance().isDevelopmentEnvironment()) modules.add(new RenderDebug());
        modules.add(new StorageESP());

        //MOVEMENT
        modules.add(new ElytraFly());
        modules.add(new LongJump());
        modules.add(new Step());
        modules.add(new Velocity());

        //HUD
        modules.add(new ActiveModuleList());
        modules.add(new CrawlStatus());
        modules.add(new EmpLogo());
        modules.add(new Overlay());
        modules.add(new StickyNote());
        modules.add(new Watermark());
        modules.add(new com.cousinware.arete.module.hud.Welcomer());

        //WORLD
        modules.add(new AntiPearlLoad());
        modules.add(new AutoBuild());
        modules.add(new AutoLight());
        modules.add(new NoBlockRotation());
        modules.add(new NoSound());


    }

    public Module getModuleByName(String s) {
        for (Module module : modules) {
            if (module.getName().equalsIgnoreCase(s)) {
                return module;
            }
        }
        return null;
    }

    public ArrayList<Module> getHudModules() {
        ArrayList<Module> list = new ArrayList<>();
        for (Module module : modules) {
            if (module.getCategory().equals(Module.Category.Hud)) list.add(module);
        }
        return list;
    }

    public void processTicks() {
        if (MinecraftClient.getInstance().player == null || MinecraftClient.getInstance().world == null) return;

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
