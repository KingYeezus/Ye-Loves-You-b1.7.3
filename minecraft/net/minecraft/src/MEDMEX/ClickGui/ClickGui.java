package net.minecraft.src.MEDMEX.ClickGui;

import java.util.List;

import net.minecraft.src.Gui;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.ScaledResolution;
import net.minecraft.src.MEDMEX.Client;
import net.minecraft.src.MEDMEX.Modules.Module;
import net.minecraft.src.MEDMEX.Modules.Module.Category;


public class ClickGui extends GuiScreen{
	
	public void initGui() {
		Panel.panels.clear();
		for(Category c : Module.Category.values()) {
			List<Module> modules = Client.getModuleByCategory(c);
			Panel.addPanel(c, modules);
		}
		
	}
	
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		ScaledResolution sr = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
		Gui.drawRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), 0x33232323);
	}

}
