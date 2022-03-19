package net.minecraft.src.MEDMEX.Modules.Client;



import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import net.minecraft.src.MEDMEX.Client;
import net.minecraft.src.MEDMEX.ClickGui.ClickGui;
import net.minecraft.src.MEDMEX.Modules.Module;
import net.minecraft.src.MEDMEX.Modules.Module.Category;
import net.minecraft.src.MEDMEX.MyClickGUI.GuiClick;
import net.minecraft.src.de.Hero.settings.Setting;




public class ClickGUI extends Module {
	
	
	
	public ClickGUI() {
		super("ClickGUI", Keyboard.KEY_RSHIFT, Category.CLIENT);
	}
	
	public void onEnable() {
		super.onEnable();
		mc.displayGuiScreen(new ClickGui());
		toggle();
	}
	

	

	

	

}
