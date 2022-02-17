package net.minecraft.src.MEDMEX.UI;


import java.awt.Color;

import org.lwjgl.opengl.GL11;

import net.minecraft.src.GuiMultiplayer;
import net.minecraft.src.GuiOptions;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.GuiSelectWorld;
import net.minecraft.src.GuiTexturePacks;
import net.minecraft.src.MEDMEX.Client;
import net.minecraft.src.MEDMEX.AltLogin.GuiAccountManager;



public class MainMenu extends GuiScreen {
	long timer = 1l;

	public MainMenu() {
		
	}
	
	public void initGui() {
		
	}
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, this.mc.renderEngine.getTexture("/background.png"));
		this.drawModalRectWithCustomSizedTexture(0, 0, 0,0 , this.width, this.height, this.width, this.height);
		
		
		this.drawGradientRect(0, height - 100, width, height, 0x00000000, 0xff000000);
		
		String[] buttons = {"Singleplayer", "Multiplayer",  "Settings", "Packs" ,  "Account Manager", "Quit"};
		
		int count = 0;
		for(String name : buttons) {
			
			float x = (width/buttons.length) * count + (width/buttons.length)/2f - mc.fontRenderer.getStringWidth(name)/2f;
			float y = height - 20;
			
			boolean hovered = (mouseX >= x && mouseY >= y && mouseX < x + mc.fontRenderer.getStringWidth(name) && mouseY < y + mc.fontRenderer.charHeight);
				
			
			
			this.drawCenteredString(mc.fontRenderer, name, (width/buttons.length) * count + (width/buttons.length)/2 , height - 20, hovered ?  Color.RED.getRGB() : -1);
			count++;
		}
		
		GL11.glPushMatrix();
		GL11.glTranslatef(width/2f, height/2f, 0);
		GL11.glScalef(2, 2, 1);
		GL11.glTranslatef(-( width/2f), -(height/2f), 0);
		this.drawCenteredString(mc.fontRenderer, Client.name, width/2, height/2 - 11, -1);
		GL11.glPopMatrix();
	}
	
	public void mouseClicked(int mouseX, int mouseY, int button) {
		String[] buttons = {"Singleplayer", "Multiplayer", "Settings", "Packs" ,  "Account Manager", "Quit"};
		
		int count = 0;
		for(String name : buttons) {
			float x = (width/buttons.length) * count + (width/buttons.length)/2f - mc.fontRenderer.getStringWidth(name)/2f;
			float y = height - 20;
			
			if(mouseX >= x && mouseY >= y && mouseX < x + mc.fontRenderer.getStringWidth(name) && mouseY < y + mc.fontRenderer.charHeight) {
				switch(name) {
				case "Singleplayer":
					mc.displayGuiScreen(new GuiSelectWorld(this));
					break;
				case "Multiplayer":
					mc.displayGuiScreen(new GuiMultiplayer(this));
					break;
				case "Settings":
					mc.displayGuiScreen(new GuiOptions(this, mc.gameSettings));
					break;
				case "Account Manager":
					mc.displayGuiScreen(new GuiAccountManager(this));
					break;
				case "Packs":
					mc.displayGuiScreen(new GuiTexturePacks(this));
					break;
				case "Quit":
					mc.shutdown();
					break;	
				
				}
			}
			
			count++;
		}
	}
	
	public void onGuiClosed() {
		
	}
	
	
}
