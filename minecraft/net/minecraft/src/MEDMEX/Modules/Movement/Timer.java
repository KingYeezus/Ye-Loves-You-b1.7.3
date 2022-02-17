package net.minecraft.src.MEDMEX.Modules.Movement;

import org.lwjgl.input.Keyboard;

import net.minecraft.src.MathHelper;
import net.minecraft.src.Packet10Flying;
import net.minecraft.src.Packet70Bed;
import net.minecraft.src.MEDMEX.Client;
import net.minecraft.src.MEDMEX.Event.Event;
import net.minecraft.src.MEDMEX.Event.listeners.EventUpdate;
import net.minecraft.src.MEDMEX.Modules.Module;
import net.minecraft.src.de.Hero.settings.Setting;

public class Timer extends Module{
	public static Timer instance;
	private float[] ticks;
	private long prevTime;
	public static int currentTick;
	public Timer() {
		super("Timer", Keyboard.KEY_NONE, Category.MOVEMENT);
		this.ticks = new float[20];
		for (int i = 0, len = this.ticks.length; i < len; i++)
		      this.ticks[i] = 0.0F; 
		this.prevTime = -1L;
		instance = this;
	}
	
	public void setup() {
		Client.settingsmanager.rSetting(new Setting("Timer", this, 1.0, 1, 2, false));
	}
	
	public void onDisable() {
		mc.timer.timerSpeed = 1.0f;
	}
	
	public void onEvent(Event e) {
		if(e instanceof EventUpdate) {
			if(e.isPre()) {
				mc.timer.timerSpeed = (float) Client.settingsmanager.getSettingByName("Timer").getValDouble();
			

			}
		}
	}
	public float getTickRate() {
	    int tickCount = 0;
	    float tickRate = 0.0F;
	    for (int i = 0; i < this.ticks.length; i++) {
	      float tick = this.ticks[i];
	      if (tick > 0.0F) {
	        tickRate += tick;
	        tickCount++;
	      } 
	    } 
	    return (float) (20.0F - (20.0F - MathHelper.clamp_double(tickRate / tickCount, 0.0F, 20.0F)) * 2.0F);
	  }
	
	public void TimeUpdate() {
	    if (this.prevTime != -1L) {
	      this.ticks[currentTick % this.ticks.length] = (float) MathHelper.clamp_double(20.0F / (float)(System.currentTimeMillis() - this.prevTime) / 1000.0F, 0.0F, 20.0F);
	      currentTick++;
	    } 
	    this.prevTime = System.currentTimeMillis();
	  }
	
}
