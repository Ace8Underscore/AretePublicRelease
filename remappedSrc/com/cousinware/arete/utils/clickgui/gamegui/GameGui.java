package com.cousinware.arete.utils.guis.clickgui.defaultguis.gamegui;

import com.cousinware.arete.command.Command;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.awt.*;
import java.util.ArrayList;

public class GameGui extends Screen {

    public boolean gameRunning = false;

    public static ArrayList<Water> waterFighters;
    public static ArrayList<Fire> fireFighters;
    public static ArrayList<Sponge> spongeFighters;

    public int screenWidth;
    public int screenHeight;






    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        screenWidth = MinecraftClient.getInstance().currentScreen.width;
        screenHeight = MinecraftClient.getInstance().currentScreen.height;

        if (!gameRunning) startGame();

        //render
        for (Water water : waterFighters) {
            water.render(context);
            water.tick();
        }

        for (Fire fire : fireFighters) {
            fire.render(context);
            fire.tick();
        }

        for (Sponge sponge : spongeFighters) {
            sponge.render(context);
            sponge.tick();
        }

    }


    public GameGui() {
        super(Text.of("Game!"));
    }

    public void startGame() {
        gameRunning = true;
        generateFighters();
    }

    public void generateFighters() {
        //generate new ArrayList
        waterFighters = new ArrayList<>();
        fireFighters = new ArrayList<>();
        spongeFighters = new ArrayList<>();
        Command.sendClientSideMessage("Generating Fighters!");


        for (int i = 0; i < 60; i++) {
            int spawnX = (int) (Math.random() * screenWidth);
            int spawnY = (int) (Math.random() * screenHeight);

            if (i < 19) waterFighters.add(new Water(spawnX, spawnY));
            else if (i > 19 && i < 39) fireFighters.add(new Fire(spawnX, spawnY));
            else spongeFighters.add(new Sponge(spawnX, spawnY));
        }
    }

    //Water beats Fire
    // Fire beats Sponge
    // Sponge beats Water
























































    public static class Fighter {
        int x;
        int y;

        int width = 4;
        int height = 4;

        public Fighter(int x, int y) {
            this.x = x;
            this.y = y;

        }

        public void setX(int x) {
            this.x = x;
        }

        public int getX() {
            return x;
        }

        public void setY(int y) {
            this.y = y;
        }

        public int getY() {
            return y;
        }

        public void render(DrawContext drawContext) {
            if (this instanceof Water) {
                drawContext.fill(x, y, x + width, y + height, Color.BLUE.getRGB());
            }

            if (this instanceof Fire) {
                drawContext.fill(x, y, x + width, y + height, Color.RED.getRGB());
            }

            if (this instanceof Sponge) {
                drawContext.fill(x, y, x + width, y + height, Color.YELLOW.getRGB());
            }
        }


        public void tick() {
            //run away from
            Fighter closestEnemy = null;
            //run to
            Fighter closestTarget = null;
            if (this instanceof Water) {
                closestEnemy = findClosest(spongeFighters);
                closestTarget = findClosest(fireFighters);
            }

            if (this instanceof Fire) {
                closestEnemy = findClosest(waterFighters);
                closestTarget = findClosest(spongeFighters);
            }

            if (this instanceof Sponge) {
                closestEnemy = findClosest(fireFighters);
                closestTarget = findClosest(waterFighters);

            }
            move(closestEnemy, closestTarget);
        }

        public Fighter findClosest(ArrayList<?> fighters) {
            Fighter closestFighter = (Fighter) fighters.get(0);
            for (Object fighter : fighters) {
                if (this.distanceTo((Fighter) fighter) < this.distanceTo(closestFighter)) closestFighter = (Fighter) fighter;

            }
            return closestFighter;
        }

        public float distanceTo(Fighter fighter) {
            double x = Math.pow(this.x - fighter.getX(), 2);
            double y = Math.pow(this.y - fighter.getY(), 2);
            return (float) Math.sqrt(x + y);
        }

        public void move(Fighter enemy, Fighter target) {
            if (this.distanceTo(enemy) < 200) {
                //runaway
                if (enemy.getX() > this.getX()) this.x--;
                else if (this.getX() > enemy.getX()) this.x++;
            }

            if (this.distanceTo(target) < 1000) {
                //movetowards

                if (enemy.getY() > this.getY()) this.y++;
                else if (this.getY() > enemy.getY()) this.y--;


            }
        }
    }

    public static class Water extends Fighter {

        public Water(int x, int y) {
            super(x, y);
        }
    }

    public static class Fire extends Fighter {

        public Fire(int x, int y) {
            super(x, y);
        }
    }

    public static class Sponge extends Fighter {

        public Sponge(int x, int y) {
            super(x, y);
        }
    }


}






