package net.minecraft.src.MEDMEX.MyClickGUI;

import net.minecraft.src.GuiScreen;
import net.minecraft.src.MEDMEX.Modules.Module;
import net.minecraft.src.de.Hero.settings.Setting;


public class ToggleSettingButton extends GuiScreen{
	int startX;
	int startZ;
	int endX;
	int endZ;
	Setting s;

	public ToggleSettingButton(int startX, int startZ, int endX, int endZ, Setting s) {
		this.startX = startX;
		this.startZ = startZ;
		this.endX = endX;
		this.endZ = endZ;
		this.s = s;
	}
}
