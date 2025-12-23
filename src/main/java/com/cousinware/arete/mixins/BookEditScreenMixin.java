package com.cousinware.arete.mixins;

import com.cousinware.arete.module.Client.Core;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.BookEditScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.SelectionManager;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = BookEditScreen.class, priority = 1001)
public class BookEditScreenMixin extends Screen {


    public List<String> pages;

    public int currentPage;

    @Shadow
    private SelectionManager currentPageSelectionManager;
    //Colors
    public ButtonWidget blackButton;
    public ButtonWidget blueButton;
    public ButtonWidget greenButton;
    public ButtonWidget cyanButton;
    public ButtonWidget redButton;
    public ButtonWidget purpleButton;
    public ButtonWidget goldButton;
    public ButtonWidget lightGrayButton;
    public ButtonWidget grayButton;
    public ButtonWidget lightBlueButton;
    public ButtonWidget limeButton;
    public ButtonWidget aquaButton;
    public ButtonWidget lightRedButton;
    public ButtonWidget magentaButton;
    public ButtonWidget yellowButton;
    public ButtonWidget whiteButton;
    //Formatting
    public ButtonWidget spinningButton;
    public ButtonWidget boldButton;
    public ButtonWidget strikeButton;
    public ButtonWidget underlineButton;
    public ButtonWidget italicButton;
    public ButtonWidget resetButton;


    public BookEditScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("HEAD"))
    public void addBookButtons(CallbackInfo ci) {
        int width1 = 20;
        int height1 = 20;
        final String box = "■";
        if (Core.bookEditor.getValue()) {

            blackButton = ButtonWidget.builder(Text.of(Formatting.BLACK + box), button -> {
                magic("0");
            }).dimensions(20, 20, width1, height1).build();

            blueButton = ButtonWidget.builder(Text.of(Formatting.DARK_BLUE + box), button -> {
                magic("1");
            }).dimensions(20, 40, width1, height1).build();

            greenButton = ButtonWidget.builder(Text.of(Formatting.DARK_GREEN + box), button -> {
                magic("2");
            }).dimensions(20, 60, width1, height1).build();

            cyanButton = ButtonWidget.builder(Text.of(Formatting.DARK_AQUA + box), button -> {
                magic("3");
            }).dimensions(20, 80, width1, height1).build();

            redButton = ButtonWidget.builder(Text.of(Formatting.DARK_RED + box), button -> {
                magic("4");
            }).dimensions(20, 100, width1, height1).build();

            purpleButton = ButtonWidget.builder(Text.of(Formatting.BLUE + box), button -> {
                magic("9");
            }).dimensions(20, 120, width1, height1).build();

            goldButton = ButtonWidget.builder(Text.of(Formatting.GOLD + box), button -> {
                magic("6");
            }).dimensions(20, 140, width1, height1).build();

            lightGrayButton = ButtonWidget.builder(Text.of(Formatting.GRAY + box), button -> {
                magic("7");
            }).dimensions(20, 160, width1, height1).build();

            grayButton = ButtonWidget.builder(Text.of(Formatting.DARK_GRAY + box), button -> {
                magic("8");
            }).dimensions(40, 20, width1, height1).build();

            lightBlueButton = ButtonWidget.builder(Text.of(Formatting.DARK_PURPLE + box), button -> {
                magic("5");
            }).dimensions(40, 40, width1, height1).build();

            limeButton = ButtonWidget.builder(Text.of(Formatting.GREEN + box), button -> {
                magic("a");
            }).dimensions(40, 60, width1, height1).build();

            aquaButton = ButtonWidget.builder(Text.of(Formatting.AQUA + box), button -> {
                magic("b");
            }).dimensions(40, 80, width1, height1).build();

            lightRedButton = ButtonWidget.builder(Text.of(Formatting.RED + box), button -> {
                magic("c");
            }).dimensions(40, 100, width1, height1).build();

            magentaButton = ButtonWidget.builder(Text.of(Formatting.LIGHT_PURPLE + box), button -> {
                magic("d");
            }).dimensions(40, 120, width1, height1).build();

            yellowButton = ButtonWidget.builder(Text.of(Formatting.YELLOW + box), button -> {
                magic("e");
            }).dimensions(40, 140, width1, height1).build();

            whiteButton = ButtonWidget.builder(Text.of(Formatting.WHITE + box), button -> {
                magic("f");
            }).dimensions(40, 160, width1, height1).build();


            //Formatting

            spinningButton = ButtonWidget.builder(Text.of(Formatting.OBFUSCATED + box), button -> {
                magic("k");
            }).dimensions(60, 20, width1, height1).build();
            boldButton = ButtonWidget.builder(Text.of(Formatting.BOLD + "B"), button -> {
                magic("l");
            }).dimensions(60, 40, width1, height1).build();

            strikeButton = ButtonWidget.builder(Text.of(Formatting.STRIKETHROUGH + "S"), button -> {
                magic("m");
            }).dimensions(60, 60, width1, height1).build();

            underlineButton = ButtonWidget.builder(Text.of(Formatting.UNDERLINE + "U"), button -> {
                magic("n");
            }).dimensions(60, 80, width1, height1).build();

            italicButton = ButtonWidget.builder(Text.of(Formatting.ITALIC + "I"), button -> {
                magic("o");
            }).dimensions(60, 100, width1, height1).build();
            resetButton = ButtonWidget.builder(Text.of(Formatting.RESET + "reset"), button -> {
                magic("r");
            }).dimensions(60, 120, width1, height1).build();


            this.addDrawableChild(blackButton);
            this.addDrawableChild(blueButton);
            this.addDrawableChild(lightBlueButton);
            this.addDrawableChild(lightGrayButton);
            this.addDrawableChild(lightRedButton);
            this.addDrawableChild(redButton);
            this.addDrawableChild(greenButton);
            this.addDrawableChild(cyanButton);
            this.addDrawableChild(purpleButton);
            this.addDrawableChild(goldButton);
            this.addDrawableChild(yellowButton);
            this.addDrawableChild(magentaButton);
            this.addDrawableChild(whiteButton);
            this.addDrawableChild(limeButton);
            this.addDrawableChild(aquaButton);
            this.addDrawableChild(grayButton);

            this.addDrawableChild(spinningButton);
            this.addDrawableChild(boldButton);
            this.addDrawableChild(strikeButton);
            this.addDrawableChild(underlineButton);
            this.addDrawableChild(italicButton);
            this.addDrawableChild(resetButton);
        }
    }

    public void magic(String s) {
        currentPageSelectionManager.insert("§" + s);
        currentPageSelectionManager.moveCursor(2, true);
    }


}
