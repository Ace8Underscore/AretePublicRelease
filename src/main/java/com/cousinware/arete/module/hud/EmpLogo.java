package com.cousinware.arete.module.hud;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.events.event.RenderOverlayEvent;
import com.cousinware.arete.managers.AssetManager;
import com.cousinware.arete.module.Module;
import com.cousinware.arete.utils.Timer;
import com.cousinware.arete.utils.guis.clickgui.defaultguis.hudgui.HudInterface;
import com.cousinware.arete.utils.rendering.nvg.NVGContext;
import com.cousinware.arete.utils.rendering.nvg.NVGWrapper;
import com.cousinware.arete.utils.settings.BoolSetting;
import com.cousinware.arete.utils.settings.DoubleSetting;

public class EmpLogo extends Module implements HudInterface {

    DoubleSetting scale = new DoubleSetting();
    BoolSetting bounce = new BoolSetting();
    DoubleSetting speed = new DoubleSetting();
    BoolSetting onlyInGui = new BoolSetting();
    Timer timer;
    int dimension = 0;
    double directionX = 1;
    double directionY = 1;

    public EmpLogo() {
        super("EmpLogo", Category.Hud, -1, "");
        scale.setName("Scale").setValue(2.5).setMin(.5).setMax(10).build(this);
        speed.setName("Speed").setValue(.25).setMin(0.1).setMax(1).build(this).setModifyAction(() -> {
            timer.setDelay(1);
        });
        onlyInGui.setName("OnlyInGui").setValue(true).build(this);
        bounce.setName("Bounce").setValue(true).build(this);
        setDrawn(false);
        timer = new Timer(1);

    }

    @Override
    public void render(RenderOverlayEvent context) {
        //if (!shouldRender()) return;
        if (bounce.getValue() && (mc.currentScreen != AreteClient.newHudGui))
            bounce();

        drawOutline(this, context.getContext());
        dimension = (int) (16 * scale.getValue());


        AssetManager.Asset asset = AreteClient.assetManager.getAsset("emplogo");
        if (!onlyInGui.getValue()) {
            NVGContext.render(nvg -> {
                NVGWrapper.drawImage(nvg, asset, getRealX(), getYSetting().getValue().floatValue(), dimension, dimension, 255);
            });
        } else if (mc.currentScreen != null) {
                NVGContext.render(nvg -> {
                    NVGWrapper.drawImage(nvg, asset, getRealX(), getYSetting().getValue().floatValue(), dimension, dimension, 255);
                });
        }
    }

    public void bounce() {
        getXSetting().setValue((getXSetting().getValue() + (directionX * speed.getValue())));
        if (getRealX() + dimension >= mc.getWindow().getScaledWidth()) directionX = -1;
        else if (getRealX() <= 0) directionX = 1;

        getYSetting().setValue((getYSetting().getValue() + (directionY * speed.getValue())));
        if (getYSetting().getValue() + dimension >= mc.getWindow().getScaledHeight()) directionY = -1;
        else if (getYSetting().getValue() <= 0) directionY = 1;
    }

    @Override
    public int[] hitBox() {
        return new int[]{dimension, dimension};
    }
}