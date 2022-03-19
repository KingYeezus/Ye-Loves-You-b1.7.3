package net.minecraft.src.MEDMEX.Modules.Render;

import java.awt.Color;
import java.awt.Point;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Gui;
import net.minecraft.src.GuiIngame;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.RenderHelper;
import net.minecraft.src.ScaledResolution;
import net.minecraft.src.MEDMEX.Client;
import net.minecraft.src.MEDMEX.Event.Event;
import net.minecraft.src.MEDMEX.Event.listeners.EventUpdate;
import net.minecraft.src.MEDMEX.Modules.Module;
import net.minecraft.src.MEDMEX.Utils.RenderUtils;

public class Radar extends Module {
	public static Radar instance;

	public Radar() {
		super("Radar", Keyboard.KEY_NONE, Category.RENDER);
		instance = this;
	}

	public void onRenderGUI() {
		if (this.isEnabled()) 
		{
			if(mc.gameSettings.showDebugInfo)
				return;
			int miX = -22, miY = 8;
	        int maX = miX + 100, maY = miY + 100;
			Gui.drawRegularPolygon(42, 60, 41, 360, 0x8042f54b);
			Gui.drawRegularPolygon(42, 60, 40, 360, 0x80232323);
			Gui.drawRegularPolygon(42, 60, 1, 360, 0xff42f54b);
			GL11.glPushMatrix();
		    int scale = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight).scaleFactor;
			for(Entity e : mc.theWorld.loadedEntityList) {
				if(e instanceof EntityPlayer) {
					EntityPlayer p = (EntityPlayer)e;
					if(e != mc.thePlayer && p.username != mc.thePlayer.username) {
						if(mc.thePlayer.getDistanceToEntity(e) < 90) {
							double dist_sq = mc.thePlayer.getDistanceSqToEntity(e);
							double x = e.posX - mc.thePlayer.posX, z = e.posZ - mc.thePlayer.posZ;
				            double calc = Math.atan2(x, z) * 57.2957795131f;
				            double angle = ((mc.thePlayer.rotationYaw + calc) % 360) * 0.01745329251f;
				            double hypotenuse = dist_sq / 200;
				            double x_shift = hypotenuse * Math.sin(angle), y_shift = hypotenuse * Math.cos(angle);
				            Gui.drawRegularPolygon(maX / 2 + 3 - x_shift, miY + 52 - y_shift, 2f, 4, Color.red.getRGB());
				            GL11.glScalef(0.6f, 0.6f, 1f);
				            mc.fontRenderer.drawCenteredString(mc.fontRenderer,p.username + " [§b"+(int)mc.thePlayer.getDistanceToEntity(e)+"§r]", (maX / 2 + 3 - x_shift) * 1.6, (miY + 57 - y_shift) * 1.6, -1);
						}
					}
				}
			}
		    GL11.glPopMatrix();
		}
	}
}