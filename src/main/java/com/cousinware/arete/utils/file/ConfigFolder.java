package com.cousinware.arete.utils.file;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.module.Client.Core;
import com.cousinware.arete.module.Module;
import com.cousinware.arete.utils.settings.*;
import net.minecraft.client.MinecraftClient;

import java.io.*;
import java.util.Iterator;

public class ConfigFolder {

    public static File arete;
    public static File settings;
    String dir = MinecraftClient.getInstance().runDirectory.getAbsolutePath();

    public ConfigFolder() throws IOException {
        arete = new File(dir + File.separator + "Arete");
        if (!arete.exists()) genFolder();
        loadBooleanFile();
        loadIntFile();
        loadDoubleFile();
        loadModeFile();
        loadBindFile();
        loadEnabledFile();
        loadDrawnFile();
        loadBooleanContainerFile();
        loadStringFile();
        loadFontFile();
        AreteClient.LOGGER.info("Loaded Config");


    }

    public void loadEnabledFile() throws IOException {
        File enabledFile = new File(arete.getAbsolutePath(), "enabled.txt");
        if (!enabledFile.exists()) return;

        FileInputStream fstream = new FileInputStream(enabledFile.getAbsolutePath());
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        String line;
        while ((line = br.readLine()) != null) {
            if (line.isBlank()) continue;
            String curLine = line.trim();
            String moduleName = curLine.split(":")[0];
            String moduleVal = curLine.split(":")[1];
            for (Module m : AreteClient.moduleManager.getModules()) {
                if (m != null && m.getName().equalsIgnoreCase(moduleName) && moduleVal.equalsIgnoreCase("true")) {

                    m.enable();


                }
            }
        }

        br.close();
    }

    public void loadDrawnFile() throws IOException {
        File enabledFile = new File(arete.getAbsolutePath(), "drawn.txt");
        if (!enabledFile.exists()) return;

        FileInputStream fstream = new FileInputStream(enabledFile.getAbsolutePath());
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        String line;
        while ((line = br.readLine()) != null) {
            if (line.isBlank()) continue;
            String curLine = line.trim();
            String moduleName = curLine.split(":")[0];
            String moduleVal = curLine.split(":")[1];
            for (Module m : AreteClient.moduleManager.getModules()) {
                if (m != null && m.getName().equalsIgnoreCase(moduleName) && !moduleVal.equalsIgnoreCase("true")) {
                    m.setDrawn(false);


                }
            }
        }

        br.close();
    }

    public void loadBindFile() throws IOException {
        File booleanFile = new File(arete.getAbsolutePath(), "bind.txt");
        if (!booleanFile.exists()) return;

        FileInputStream fstream = new FileInputStream(booleanFile.getAbsolutePath());
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        String line;
        while ((line = br.readLine()) != null) {
            if (line.isBlank()) continue;

            String curLine = line.trim();
            String moduleName = curLine.split(":")[0];
            String moduleVal = curLine.split(":")[1];
            for (Module m : AreteClient.moduleManager.getModules()) {
                if (m != null && m.getName().equalsIgnoreCase(moduleName)) {
                    m.setKeybind(Integer.parseInt(moduleVal));


                }
            }
        }

        br.close();
    }

    public void loadBooleanFile() throws IOException {
        File booleanFile = new File(arete.getAbsolutePath(), "boolSetting.txt");
        if (!booleanFile.exists()) return;

        FileInputStream fstream = new FileInputStream(booleanFile.getAbsolutePath());
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        String line;
        while ((line = br.readLine()) != null) {
            if (line.isBlank()) continue;

            String curLine = line.trim();
            String moduleName = curLine.split(":")[0];
            String settingName = curLine.split(":")[1];
            String moduleVal = curLine.split(":")[2];
            if (!moduleName.equalsIgnoreCase("BoolSettingContainerWidget")) {
                for (Module m : AreteClient.moduleManager.getModules()) {
                    if (m != null && m.getName().equalsIgnoreCase(moduleName)) {
                        for (Setting s : m.getSettings()) {
                            if (s.getName().equalsIgnoreCase(settingName) && s instanceof BoolSetting) {
                                ((BoolSetting) s).setValue(Boolean.parseBoolean(moduleVal));
                            }
                        }


                    }
                }
            }
            if (moduleName.equalsIgnoreCase("BoolSettingContainerWidget")) {
                moduleName = curLine.split(":")[1];
                settingName = curLine.split(":")[2];
                moduleVal = curLine.split(":")[3];
                String id = curLine.split(":")[4];
                for (Setting setting : AreteClient.settingManager.getExtraSettingArrayList()) {
                    if (id != null) {
                        if (setting.getParent().getName().equalsIgnoreCase(moduleName) && id.equalsIgnoreCase(setting.getId())) {
                            if (setting instanceof BoolSetting) {
                                ((BoolSetting) setting).setValue(Boolean.parseBoolean(moduleVal));
                            }
                        }
                    }
                }
            }
        }

        br.close();
    }

    public void loadBooleanContainerFile() throws IOException {
        File booleanFile = new File(arete.getAbsolutePath(), "boolContainerSetting.txt");
        if (!booleanFile.exists()) return;

        FileInputStream fstream = new FileInputStream(booleanFile.getAbsolutePath());
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        String line;
        while ((line = br.readLine()) != null) {
            if (line.isBlank()) continue;

            String curLine = line.trim();
            String moduleName = curLine.split(":")[0];
            String settingName = curLine.split(":")[1];
            String moduleVal = curLine.split(":")[2];
            for (Module m : AreteClient.moduleManager.getModules()) {
                if (m != null && m.getName().equalsIgnoreCase(moduleName)) {
                    for (Setting s : m.getSettings()) {
                        if (s == null) continue;
                        if (s.getName().equalsIgnoreCase(settingName) && s instanceof BoolSettingContainer) {
                            ((BoolSettingContainer) s).setValue(Boolean.parseBoolean(moduleVal));
                        }
                    }


                }
            }


        }
        br.close();
    }

    public void loadIntFile() throws IOException {
        File intSetting = new File(arete.getAbsolutePath(), "intSetting.txt");
        if (!intSetting.exists()) return;

        FileInputStream fstream = new FileInputStream(intSetting.getAbsolutePath());
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        String line;
        while ((line = br.readLine()) != null) {
            if (line.isBlank()) continue;

            String curLine = line.trim();
            String moduleName = curLine.split(":")[0];
            String settingName = curLine.split(":")[1];
            String moduleVal = curLine.split(":")[2];
            if (!moduleName.equalsIgnoreCase("BoolSettingContainerWidget")) {

                for (Module m : AreteClient.moduleManager.getModules()) {
                    if (m != null && m.getName().equalsIgnoreCase(moduleName)) {
                        for (Setting s : m.getSettings()) {
                            if (s.getName().equalsIgnoreCase(settingName) && s instanceof IntSetting) {
                                ((IntSetting) s).setValue(Integer.parseInt(moduleVal));
                            }
                        }


                    }
                }
            }
            if (moduleName.equalsIgnoreCase("BoolSettingContainerWidget")) {
                moduleName = curLine.split(":")[1];
                settingName = curLine.split(":")[2];
                moduleVal = curLine.split(":")[3];
                String id = curLine.split(":")[4];
                for (Setting setting : AreteClient.settingManager.getExtraSettingArrayList()) {
                    if (id != null) {
                        if (setting.getParent().getName().equalsIgnoreCase(moduleName) && id.equalsIgnoreCase(setting.getId())) {
                            if (setting instanceof IntSetting) {
                                ((IntSetting) setting).setValue(Integer.parseInt(moduleVal));
                            }
                        }
                    }
                }
            }
        }

        br.close();
    }

    public void loadDoubleFile() throws IOException {
        File doubleSetting = new File(arete.getAbsolutePath(), "doubleSetting.txt");
        if (!doubleSetting.exists()) return;

        FileInputStream fstream = new FileInputStream(doubleSetting.getAbsolutePath());
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        String line;
        while ((line = br.readLine()) != null) {
            if (line.isBlank()) continue;

            String curLine = line.trim();
            String moduleName = curLine.split(":")[0];
            String settingName = curLine.split(":")[1];
            String moduleVal = curLine.split(":")[2];
            if (!moduleName.equalsIgnoreCase("BoolSettingContainerWidget")) {

                for (Module m : AreteClient.moduleManager.getModules()) {
                    if (m != null && m.getName().equalsIgnoreCase(moduleName)) {
                        for (Setting s : m.getSettings()) {
                            if (s.getName().equalsIgnoreCase(settingName) && s instanceof DoubleSetting) {
                                ((DoubleSetting) s).setValue(Double.parseDouble(moduleVal));
                            }
                        }


                    }
                }
            }
            if (moduleName.equalsIgnoreCase("BoolSettingContainerWidget")) {
                moduleName = curLine.split(":")[1];
                settingName = curLine.split(":")[2];
                moduleVal = curLine.split(":")[3];
                String id = curLine.split(":")[4];

                for (Setting setting : AreteClient.settingManager.getExtraSettingArrayList()) {
                    if (id != null) {
                        if (setting.getName().equalsIgnoreCase(settingName) && id.equalsIgnoreCase(setting.getId())) {
                            if (setting instanceof DoubleSetting) {
                                ((DoubleSetting) setting).setValue(Double.parseDouble(moduleVal));
                            }
                        }
                    }
                }
            }
        }

        br.close();
    }

    public void loadModeFile() throws IOException {
        File intSetting = new File(arete.getAbsolutePath(), "modeSetting.txt");
        if (!intSetting.exists()) return;

        FileInputStream fstream = new FileInputStream(intSetting.getAbsolutePath());
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        String line;
        while ((line = br.readLine()) != null) {
            if (line.isBlank()) continue;

            String curLine = line.trim();
            String moduleName = curLine.split(":")[0];
            String settingName = curLine.split(":")[1];
            String moduleVal = curLine.split(":")[2];
            if (!moduleName.equalsIgnoreCase("BoolSettingContainerWidget")) {
                for (Module m : AreteClient.moduleManager.getModules()) {
                    if (m != null && m.getName().equalsIgnoreCase(moduleName)) {
                        for (Setting s : m.getSettings()) {
                            if (s.getName().equalsIgnoreCase(settingName) && s instanceof ModeSetting) {
                                ((ModeSetting) s).setValue(String.valueOf(moduleVal));
                            }


                        }


                    }
                }
            }
            if (moduleName.equalsIgnoreCase("BoolSettingContainerWidget")) {
                moduleName = curLine.split(":")[1];
                settingName = curLine.split(":")[2];
                moduleVal = curLine.split(":")[3];
                String id = curLine.split(":")[4];
                for (Setting setting : AreteClient.settingManager.getExtraSettingArrayList()) {
                    if (id != null) {
                        if (setting.getParent().getName().equalsIgnoreCase(moduleName) && id.equalsIgnoreCase(setting.getId())) {
                            if (setting instanceof ModeSetting) {
                                ((ModeSetting) setting).setValue(moduleVal);
                            }
                        }
                    }
                }


            }
        }

        br.close();
    }

    public void loadStringFile() throws IOException {
        File intSetting = new File(arete.getAbsolutePath(), "stringSetting.txt");
        if (!intSetting.exists()) return;

        FileInputStream fstream = new FileInputStream(intSetting.getAbsolutePath());
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        String line;
        while ((line = br.readLine()) != null) {
            if (line.isBlank()) continue;

            String curLine = line.trim();
            String moduleName = curLine.split(":")[0];
            String settingName = curLine.split(":")[1];
            String moduleVal = "";
            //Sometimes players can leave this line empty this checks so we dont crash
            if (curLine.split(":").length > 2) moduleVal = curLine.split(":")[2];
            if (!moduleName.equalsIgnoreCase("BoolSettingContainerWidget")) {
                for (Module m : AreteClient.moduleManager.getModules()) {
                    if (m != null && m.getName().equalsIgnoreCase(moduleName)) {
                        for (Setting s : m.getSettings()) {
                            if (s.getName().equalsIgnoreCase(settingName) && s instanceof StringSetting) {
                                ((StringSetting) s).setValue(String.valueOf(moduleVal));
                            }


                        }


                    }
                }
            }
            if (moduleName.equalsIgnoreCase("BoolSettingContainerWidget")) {
                moduleName = curLine.split(":")[1];
                settingName = curLine.split(":")[2];
                moduleVal = curLine.split(":")[3];
                String id = curLine.split(":")[4];
                for (Setting setting : AreteClient.settingManager.getExtraSettingArrayList()) {
                    if (id != null) {
                        if (setting.getParent().getName().equalsIgnoreCase(moduleName) && id.equalsIgnoreCase(setting.getId())) {
                            if (setting instanceof StringSetting) {
                                ((Setting<String>) setting).setValue(moduleVal);
                            }
                        }
                    }
                }


            }
        }

        br.close();
    }

    public void loadFontFile() throws IOException {
        File booleanFile = new File(arete.getAbsolutePath(), "font.txt");
        if (!booleanFile.exists()) return;

        FileInputStream fstream = new FileInputStream(booleanFile.getAbsolutePath());
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        String line;
        while ((line = br.readLine()) != null) {
            if (line.isBlank() || line.length() < 2) continue;

            AreteClient.fontManager.setAltFont(line);
        }

        br.close();
    }


    public void genFolder() {
        arete.mkdirs();

    }


    public static class ShutDown extends Thread {

        @Override
        public void run() {
            try {
                saveConfig();
                AreteClient.LOGGER.debug("Config Saved");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public ShutDown() {

        }

        public void saveConfig() throws IOException {
            if (AreteClient.isGameLaunched) {
                saveBooleanFile();
                saveIntFile();
                saveDoubleFile();
                saveModeFile();
                saveBindFile();
                saveEnabledFile();
                saveBooleanContainerFile();
                saveStringFile();
                saveFontFile();
                saveDrawnFile();
            }
        }

        public void saveEnabledFile() throws IOException {
            File enabledFile = new File(arete.getAbsolutePath(), "enabled.txt");
            BufferedWriter outEnabled = new BufferedWriter(new FileWriter(enabledFile));


            for (Module module : AreteClient.moduleManager.getModules()) {
                if (!module.isSaveToConfig()) {
                    outEnabled.write(module.getName() + ":" + "false" + "\r\n");
                    continue;
                }
                outEnabled.write(module.getName() + ":" + module.isEnabled() + "\r\n");

            }
            outEnabled.close();
        }

        public void saveDrawnFile() throws IOException {
            File enabledFile = new File(arete.getAbsolutePath(), "drawn.txt");
            BufferedWriter outEnabled = new BufferedWriter(new FileWriter(enabledFile));


            for (Module module : AreteClient.moduleManager.getModules()) {

                outEnabled.write(module.getName() + ":" + module.isDrawn() + "\r\n");

            }
            outEnabled.close();
        }

        public void saveBindFile() throws IOException {
            File booleanFile = new File(arete.getAbsolutePath(), "bind.txt");
            BufferedWriter outBoolean = new BufferedWriter(new FileWriter(booleanFile));


            for (Module module : AreteClient.moduleManager.getModules()) {
                int keybind = module.getKeybind();
                outBoolean.write(module.getName() + ":" + keybind + "\r\n");

            }

            outBoolean.close();
        }

        public void saveBooleanFile() throws IOException {
            File booleanFile = new File(arete.getAbsolutePath(), "boolSetting.txt");
            BufferedWriter outBoolean = new BufferedWriter(new FileWriter(booleanFile));
            Iterator settingIterator = AreteClient.settingManager.getAllSettings().iterator();

            while (settingIterator.hasNext()) {
                Setting setting = (Setting) settingIterator.next();
                if (setting instanceof BoolSetting) {
                    boolean hasId = !setting.getId().isEmpty();
                    if (hasId)
                        outBoolean.write("BoolSettingContainerWidget" + ":" + setting.getParent().getName() + ":" + setting.getName() + ":" + setting.getValue() + ":" + setting.getId() + "\r\n");
                    else
                        outBoolean.write(setting.getParent().getName() + ":" + setting.getName() + ":" + setting.getValue() + "\r\n");
                }
            }
            outBoolean.close();
        }

        public void saveBooleanContainerFile() throws IOException {
            File booleanFile = new File(arete.getAbsolutePath(), "boolContainerSetting.txt");
            BufferedWriter outBoolean = new BufferedWriter(new FileWriter(booleanFile));
            Iterator settingIterator = AreteClient.settingManager.getAllSettings().iterator();

            while (settingIterator.hasNext()) {
                Setting setting = (Setting) settingIterator.next();
                if (setting instanceof BoolSettingContainer) {
                    boolean hasId = !setting.getId().isEmpty();
                    outBoolean.write(setting.getParent().getName() + ":" + setting.getName() + ":" + setting.getValue() + "\r\n");
                }
            }
            outBoolean.close();
        }

        public void saveIntFile() throws IOException {
            File intFile = new File(arete.getAbsolutePath(), "intSetting.txt");
            BufferedWriter outInt = new BufferedWriter(new FileWriter(intFile));
            Iterator settingIterator = AreteClient.settingManager.getAllSettings().iterator();

            while (settingIterator.hasNext()) {
                Setting setting = (Setting) settingIterator.next();
                if (setting instanceof IntSetting) {
                    boolean hasId = !setting.getId().isEmpty();
                    if (hasId)
                        outInt.write("BoolSettingContainerWidget" + ":" + setting.getParent().getName() + ":" + setting.getName() + ":" + setting.getValue() + ":" + setting.getId() + "\r\n");
                    else
                        outInt.write(setting.getParent().getName() + ":" + setting.getName() + ":" + setting.getValue() + "\r\n");
                }
            }
            outInt.close();
        }

        public void saveDoubleFile() throws IOException {
            File doubleFile = new File(arete.getAbsolutePath(), "doubleSetting.txt");
            BufferedWriter outDouble = new BufferedWriter(new FileWriter(doubleFile));
            Iterator settingIterator = AreteClient.settingManager.getAllSettings().iterator();

            while (settingIterator.hasNext()) {
                Setting setting = (Setting) settingIterator.next();
                if (setting instanceof DoubleSetting) {
                    boolean hasId = !setting.getId().isEmpty();
                    if (hasId)
                        outDouble.write("BoolSettingContainerWidget" + ":" + setting.getParent().getName() + ":" + setting.getName() + ":" + setting.getValue() + ":" + setting.getId() + "\r\n");
                    else
                        outDouble.write(setting.getParent().getName() + ":" + setting.getName() + ":" + setting.getValue() + "\r\n");
                }
            }
            outDouble.close();
        }

        public void saveModeFile() throws IOException {
            File intFile = new File(arete.getAbsolutePath(), "modeSetting.txt");
            BufferedWriter outInt = new BufferedWriter(new FileWriter(intFile));
            Iterator settingIterator = AreteClient.settingManager.getAllSettings().iterator();

            while (settingIterator.hasNext()) {
                Setting setting = (Setting) settingIterator.next();
                if (setting instanceof ModeSetting) {
                    boolean hasId = !setting.getId().isEmpty();
                    if (hasId)
                        outInt.write("BoolSettingContainerWidget" + ":" + setting.getParent().getName() + ":" + setting.getName() + ":" + setting.getValue() + ":" + setting.getId() + "\r\n");
                    else
                        outInt.write(setting.getParent().getName() + ":" + setting.getName() + ":" + setting.getValue() + "\r\n");
                }
            }
            outInt.close();
        }

        public void saveStringFile() throws IOException {
            File intFile = new File(arete.getAbsolutePath(), "stringSetting.txt");
            BufferedWriter outInt = new BufferedWriter(new FileWriter(intFile));
            Iterator settingIterator = AreteClient.settingManager.getAllSettings().iterator();

            while (settingIterator.hasNext()) {
                Setting setting = (Setting) settingIterator.next();
                if (setting instanceof StringSetting) {
                    boolean hasId = !setting.getId().isEmpty();
                    if (hasId)
                        outInt.write("BoolSettingContainerWidget" + ":" + setting.getParent().getName() + ":" + setting.getName() + ":" + setting.getValue() + ":" + setting.getId() + "\r\n");
                    else
                        outInt.write(setting.getParent().getName() + ":" + setting.getName() + ":" + setting.getValue() + "\r\n");
                }
            }
            outInt.close();
        }


        public void saveFontFile() throws IOException {
            File booleanFile = new File(arete.getAbsolutePath(), "font.txt");
            BufferedWriter outBoolean = new BufferedWriter(new FileWriter(booleanFile));


            if (!Core.customFont.getValue()) outBoolean.write(AreteClient.fontManager.altFont);
            else outBoolean.write(AreteClient.fontManager.selectedFont);

            outBoolean.close();
        }
    }


}
