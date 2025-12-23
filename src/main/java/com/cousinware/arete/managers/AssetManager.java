package com.cousinware.arete.managers;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.utils.MinecraftInterface;
import com.cousinware.arete.utils.rendering.nvg.NVGContext;
import com.cousinware.arete.utils.rendering.nvg.NVGWrapper;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.Identifier;
import org.lwjgl.system.MemoryUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

public class AssetManager implements MinecraftInterface {

    //*
    //*Assets are named based on their file naming IE the link https://telsera.me/assets/emplogo.png ->>>>
    //*
    //*This gets added to our assetsMap and is findable by just looking up the phrase "emplogo"
    //*
    //*
    //*


    private static final String[] ASSETS_URL = new String[]{"https://telsera.me/assets/emplogo.png"};
    @Getter
    public static ConcurrentHashMap<String, Asset> assetsMap = new ConcurrentHashMap<>();

    public AssetManager() {


        downloadAssets();
    }

    public void downloadAssets() {
        try {
            for (String link : ASSETS_URL) {
                URL url = new URL(link);
                BufferedImage bufferedImage = ImageIO.read(url.openStream());
                String assetName = link.split("/")[link.split("/").length - 1].split("\\.")[0];
                Identifier identifier = AreteClient.getFileLocation("textures/icons/" + assetName);
                Asset asset = new Asset(identifier, bufferedImage, bufferedImage.getWidth(), bufferedImage.getHeight());
                assetsMap.put(assetName, asset);

            }
            linkAssetsToNanoVG();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void linkAssetsToNanoVG() {
        assetsMap.forEach((s, asset) -> {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            try {
                //convert image into ByteArrayOutputStream so we can access a clean byte array
                ImageIO.write(asset.bufferedImage, "png", os);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            byte[] imageBytes = os.toByteArray();
            //Also MemoryUtil should be used for created byte buffers NanoVG "REQUIRES" it
            ByteBuffer byteBuffer = MemoryUtil.memAlloc(imageBytes.length);
            byteBuffer.put(imageBytes);
            //why do we flip i have no idea someone on stack over flow said to
            byteBuffer.flip();

            asset.image = NVGWrapper.loadImageFromInputStream(NVGContext.context, byteBuffer);
            //gotta love freeing memory
            MemoryUtil.memFree(byteBuffer);

        });
    }

    public Identifier getAssetLocation(String assetName) {
        return assetsMap.get(assetName).getIdentifier();

    }

    public Asset getAsset(String assetName) {
        return assetsMap.get(assetName);

    }

    public Enumeration<String> allAssets() {
        return assetsMap.keys();
    }


    @Getter
    @Setter
    public static class Asset {

        Identifier identifier;
        BufferedImage bufferedImage;
        int image;
        int width;
        int height;

        public Asset(Identifier identifier, BufferedImage bufferedImage, int width, int height) {
            this.identifier = identifier;
            this.bufferedImage = bufferedImage;
            this.width = width;
            this.height = height;
        }


    }


}
