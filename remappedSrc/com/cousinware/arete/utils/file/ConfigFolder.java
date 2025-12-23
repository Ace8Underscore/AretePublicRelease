package com.cousinware.arete.utils.file;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.module.Module;
import com.cousinware.arete.utils.settings.*;
import net.minecraft.client.MinecraftClient;

import java.io.*;
import java.util.Iterator;

public class ConfigFolder {

    public static File arete;
    public static File fonts;
    public static File settings;
    String dir = MinecraftClient.getInstance().runDirectory.getAbsolutePath();

    public ConfigFolder() throws IOException {
        arete = new File(dir + File.separator + "Arete");
        fonts = new File(arete.getAbsolutePath() + File.separator + "fonts");
        if (!arete.exists()) genFolder();
        loadBooleanFile();
        loadIntFile();
        loadDoubleFile();
        loadModeFile();
        loadBindFile();
        loadEnabledFile();


    }

    public void loadEnabledFile() throws IOException {
        File enabledFile = new File(arete.getAbsolutePath(), "enabled.txt");
        if (!enabledFile.exists()) return;

        FileInputStream fstream = new FileInputStream(enabledFile.getAbsolutePath());
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        String line;
        while ((line = br.readLine()) != null) {
            String curLine = line.trim();
            String moduleName = curLine.split(":")[0];
            String moduleVal = curLine.split(":")[1];
            for (Module m : AreteClient.moduleManager.getModules()) {
                if (m != null && m.getName().equalsIgnoreCase(moduleName) &&moduleVal.equalsIgnoreCase("true")) {
                    m.enable();


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
            String curLine = line.trim();
            String moduleName = curLine.split(":")[0];
            String settingName = curLine.split(":")[1];
            String moduleVal = curLine.split(":")[2];
            for (Module m : AreteClient.moduleManager.getModules()) {
                if (m != null && m.getName().equalsIgnoreCase(moduleName)) {
                    for (Setting s : m.getSettings()) {
                        if (s.getName().equalsIgnoreCase(settingName)) {
                            ((BoolSetting)s ).setValue(Boolean.parseBoolean(moduleVal));
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
            String curLine = line.trim();
            String moduleName = curLine.split(":")[0];
            String settingName = curLine.split(":")[1];
            String moduleVal = curLine.split(":")[2];
            for (Module m : AreteClient.moduleManager.getModules()) {
                if (m != null && m.getName().equalsIgnoreCase(moduleName)) {
                    for (Setting s : m.getSettings()) {
                        if (s.getName().equalsIgnoreCase(settingName)) {
                            ((IntSetting)s ).setValue(Integer.parseInt(moduleVal));
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
            String curLine = line.trim();
            String moduleName = curLine.split(":")[0];
            String settingName = curLine.split(":")[1];
            String moduleVal = curLine.split(":")[2];
            for (Module m : AreteClient.moduleManager.getModules()) {
                if (m != null && m.getName().equalsIgnoreCase(moduleName)) {
                    for (Setting s : m.getSettings()) {
                        if (s.getName().equalsIgnoreCase(settingName)) {
                            ((DoubleSetting)s ).setValue(Double.parseDouble(moduleVal));
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
            String curLine = line.trim();
            String moduleName = curLine.split(":")[0];
            String settingName = curLine.split(":")[1];
            String moduleVal = curLine.split(":")[2];
            for (Module m : AreteClient.moduleManager.getModules()) {
                if (m != null && m.getName().equalsIgnoreCase(moduleName)) {
                    for (Setting s : m.getSettings()) {
                        if (s.getName().equalsIgnoreCase(settingName)) {
                            ((ModeSetting)s ).setValue(String.valueOf(moduleVal));
                        }
                    }


                }
            }
        }

        br.close();
    }



    public void genFolder() {
        arete.mkdirs();
        fonts.mkdirs();
    }





    public static class ShutDown extends Thread {

        public ShutDown() {

        }

        public void saveConfig() throws IOException {
            saveBooleanFile();
            saveIntFile();
            saveDoubleFile();
            saveModeFile();
            saveBindFile();
            saveEnabledFile();
        }

        public void saveEnabledFile() throws IOException {
            File enabledFile = new File(arete.getAbsolutePath(), "enabled.txt");
            BufferedWriter outEnabled = new BufferedWriter(new FileWriter(enabledFile));


            for (Module module : AreteClient.moduleManager.getModules()) {

                outEnabled.write(module.getName() + ":" + module.isEnabled() + "\r\n");

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
            Iterator settingIterator = AreteClient.settingManager.getRegisteredSettings().iterator();

            while (settingIterator.hasNext()) {
                Setting setting = (Setting) settingIterator.next();
                if (setting instanceof BoolSetting) {
                    outBoolean.write(setting.getParent().getName() + ":" + setting.getName() + ":" + setting.getValue() + "\r\n");
                }
            }
            outBoolean.close();
        }

        public void saveIntFile() throws IOException {
            File intFile = new File(arete.getAbsolutePath(), "intSetting.txt");
            BufferedWriter outInt = new BufferedWriter(new FileWriter(intFile));
            Iterator settingIterator = AreteClient.settingManager.getRegisteredSettings().iterator();

            while (settingIterator.hasNext()) {
                Setting setting = (Setting) settingIterator.next();
                if (setting instanceof IntSetting) {
                    outInt.write(setting.getParent().getName() + ":" + setting.getName() + ":" + setting.getValue() + "\r\n");
                }
            }
            outInt.close();
        }

        public void saveDoubleFile() throws IOException {
            File doubleFile = new File(arete.getAbsolutePath(), "doubleSetting.txt");
            BufferedWriter outDouble = new BufferedWriter(new FileWriter(doubleFile));
            Iterator settingIterator = AreteClient.settingManager.getRegisteredSettings().iterator();

            while (settingIterator.hasNext()) {
                Setting setting = (Setting) settingIterator.next();
                if (setting instanceof DoubleSetting) {
                    outDouble.write(setting.getParent().getName() + ":" + setting.getName() + ":" + setting.getValue() + "\r\n");
                }
            }
            outDouble.close();
        }

        public void saveModeFile() throws IOException {
            File intFile = new File(arete.getAbsolutePath(), "modeSetting.txt");
            BufferedWriter outInt = new BufferedWriter(new FileWriter(intFile));
            Iterator settingIterator = AreteClient.settingManager.getRegisteredSettings().iterator();

            while (settingIterator.hasNext()) {
                Setting setting = (Setting) settingIterator.next();
                if (setting instanceof ModeSetting) {
                    outInt.write(setting.getParent().getName() + ":" + setting.getName() + ":" + setting.getValue() + "\r\n");
                }
            }
            outInt.close();
        }

        public void run() {
            try {
                saveConfig();
            } catch (Exception e) {
                System.out.println(e.fillInStackTrace());
            }
        }

    }



}
