package net.minecraft.src.MEDMEX.UI;

import java.awt.Color;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.Comparator;
import org.lwjgl.opengl.GL11;

import net.minecraft.src.MEDMEX.Modules.Module;
import net.minecraft.src.MEDMEX.Modules.Movement.Timer;
import net.minecraft.src.MEDMEX.Modules.Render.Coords;
import net.minecraft.src.MEDMEX.Utils.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.GameSettings;
import net.minecraft.src.Gui;
import net.minecraft.src.GuiIngame;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.RenderHelper;
import net.minecraft.src.ScaledResolution;

import net.minecraft.src.MEDMEX.Client;

public class HUD {
	public static int height, width;
	public static boolean potionHUD = false;
	public static boolean clickgui;
	long timer = 0;
	public static boolean infoenabled = false;
	public static int itemcount = 0;
	public static String var;
	public static String coords;
	public static boolean antiss = false;

	
	public Minecraft mc = Minecraft.theMinecraft;
	
	public static class ModuleComparator implements Comparator<Module> {

		@Override
		public int compare(Module o1, Module o2) {
			if(Minecraft.theMinecraft.fontRenderer.getStringWidth(o1.name + o1.attribute) > Minecraft.theMinecraft.fontRenderer.getStringWidth(o2.name + o2.attribute))
				return -1;
			if(Minecraft.theMinecraft.fontRenderer.getStringWidth(o1.name + o1.attribute) < Minecraft.theMinecraft.fontRenderer.getStringWidth(o2.name + o2.attribute))
				return 1;
			
			
			return 0;
		}
		
	}
	
	
	
	public void draw() {
		ScaledResolution sr = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
		FontRenderer fr = mc.fontRenderer;
		 
		if(Coords.instance.isEnabled()) {
		int X = (int) mc.thePlayer.posX;
		int Y = (int) mc.thePlayer.posY;
		int Z = (int) mc.thePlayer.posZ;
		//mc.fontRenderer.drawStringWithShadow("§4TPS: §r"+(int)Timer.instance.getTickRate(), sr.getScaledWidth() - fr.getStringWidth("§4TPS: §r"+(int)Timer.instance.getTickRate()) - 4, sr.getScaledHeight() - 30, -1);
		if(mc.thePlayer.dimension == -1) {
		mc.fontRenderer.drawStringWithShadow("§4O:§r §7[§f"+X*8+", "+Y+", "+Z*8+"§7]", sr.getScaledWidth() - fr.getStringWidth("O: ["+X*8+", "+Y+", "+Z*8+"]") - 4, sr.getScaledHeight() - 20, -1);
		mc.fontRenderer.drawStringWithShadow("§4N:§r §7[§f"+X+", "+Y+", "+Z+"§7]", sr.getScaledWidth() - fr.getStringWidth("N: ["+X+", "+Y+", "+Z+"]") - 4, sr.getScaledHeight() - 10, -1);
		}else {	
		mc.fontRenderer.drawStringWithShadow("§4O:§r §7[§f"+X+", "+Y+", "+Z+"§7]", sr.getScaledWidth() - fr.getStringWidth("O: ["+X+", "+Y+", "+Z+"]") - 4, sr.getScaledHeight() - 20, -1);
		mc.fontRenderer.drawStringWithShadow("§4N:§r §7[§f"+X/8+", "+Y+", "+Z/8+"§7]", sr.getScaledWidth() - fr.getStringWidth("N: ["+X/8+", "+Y+", "+Z/8+"]") - 4, sr.getScaledHeight() - 10, -1);
		}
		}
		
		if(mc.getSendQueue().netManager.timeSinceLastRead >= 20) {
			double time = (double)mc.getSendQueue().netManager.timeSinceLastRead / 20;
			mc.fontRenderer.drawCenteredString(fr, "Server has been frozen for: "+String.format("%.1f", time)+"s", sr.getScaledWidth() / 2, 10, 16777215);
		}
		
		
		
		
		Collections.sort(Client.modules, new ModuleComparator());
		if(GameSettings.showDebugInfo == false) {
			mc.fontRenderer.drawStringWithShadow("§c"+Client.name +"§f b"+Client.version, 4, 4, -1);
			//mc.fontRenderer.drawStringWithShadow("N", 10+fr.getStringWidth(Client.name), 4,  ColorUtil.astolfoColorsDraw(1000, -1000));
		}
		
		int count = 0;
		
		for(Module m : Client.modules) {
			
			if(!m.toggled)
				continue;	
			if(Client.drawn.contains(m))
				continue;

			if(GameSettings.showDebugInfo == false) {
			mc.fontRenderer.drawStringWithShadow(m.name + m.attribute, sr.getScaledWidth() - fr.getStringWidth(m.name + m.attribute) - 4, 4 + count * (9), ColorUtil.astolfoColorsDraw(1000, -count*1000));
			GL11.glScaled(1, 1, 1);
			}
			count++;
			
			
		}
		}
		
		
	
}

	
