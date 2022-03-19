package net.minecraft.src.MEDMEX.ClickGui;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import net.minecraft.src.MEDMEX.Modules.Module;
import net.minecraft.src.MEDMEX.Modules.Module.Category;

public class Panel {
	
	public static CopyOnWriteArrayList<Panel> panels = new CopyOnWriteArrayList<Panel>();
	
	Category c;
	List<Module> modules;
	
	public Panel(Category c, List<Module> modules) {
		this.c = c;
		this.modules = modules;
	}
	
	public static void addPanel(Category c, List<Module> modules2) {
		panels.add(new Panel(c, modules2));
	}

}
