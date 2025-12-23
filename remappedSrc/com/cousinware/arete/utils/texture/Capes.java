package com.cousinware.arete.utils.texture;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.module.Client.Core;
import com.cousinware.arete.utils.MinecraftInterface;
import lombok.Getter;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

public class Capes implements MinecraftInterface {

    //capes are stored in textures/cape/ || arete/textures/cape/
    private static final String[] CAPE_URL = new String[] {"https://raw.githubusercontent.com/Ace8Underscore/PublicResources/refs/heads/main/empcape.png", "https://raw.githubusercontent.com/Ace8Underscore/PublicResources/refs/heads/main/oldcape.png"};

    @Getter
    public static ConcurrentHashMap<String, Identifier> capesMap = new ConcurrentHashMap<>();


    public Capes() {
        downloadCapes();
    }

    public void downloadCapes() {
        try {
            for (String link : CAPE_URL) {
                URL url = new URL(link);
                NativeImage nativeImage = NativeImage.read(url.openStream());
                String capeName = link.split("/")[link.split("/").length - 1].split("\\.")[0];
                Identifier cape = AreteClient.getFileLocation("textures/cape/" + capeName);
                capesMap.put(capeName, cape);
                mc.getTextureManager().registerTexture(cape, new NativeImageBackedTexture(nativeImage));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Identifier getCurrentCapeLocation() {
        String cape = Core.cape.getValue();
        return capesMap.getOrDefault(cape, capesMap.elements().nextElement());

    }

    public String[] getCapeNames() {
        ArrayList<String> capeNames = new ArrayList<>();
        Iterator<String> iterator = capesMap.keys().asIterator();
        while (iterator.hasNext()) {
            String name = iterator.next();
            capeNames.add(name);
        }
        return capeNames.toArray(new String[]{});
    }

    public static String[] getCapesBeforeLaunch() {
        ArrayList<String> capeNames = new ArrayList<>();
        for (String s : CAPE_URL) {
            capeNames.add(s.split("/")[s.split("/").length - 1].split("\\.")[0]);

        }
        return capeNames.toArray(new String[]{});
    }



}
