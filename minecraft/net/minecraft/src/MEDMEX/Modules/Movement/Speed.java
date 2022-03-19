package net.minecraft.src.MEDMEX.Modules.Movement;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Entity;
import net.minecraft.src.MathHelper;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet11PlayerPosition;
import net.minecraft.src.Packet15Place;
import net.minecraft.src.Packet7UseEntity;
import net.minecraft.src.Timer;
import net.minecraft.src.MEDMEX.Client;
import net.minecraft.src.MEDMEX.Event.Event;
import net.minecraft.src.MEDMEX.Event.listeners.EventUpdate;
import net.minecraft.src.MEDMEX.Modules.Module;
import net.minecraft.src.de.Hero.settings.Setting;

public class Speed extends Module{
	public static Speed instance;
	

	public Speed() {
		super("Speed", Keyboard.KEY_NONE, Category.MOVEMENT);

	    instance = this;
	}
	public void setup() {
		Client.settingsmanager.rSetting(new Setting("Speed", this, 1.2, 1, 1.5, false));
	}

	
	public double pythagorasMovement() {
        return Math.sqrt(mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ);
     }
	
	 public void fixMovementSpeed(double s, boolean m) {
         if (!m || isMoving()) {
            mc.thePlayer.motionX = -Math.sin(correctRotations()) * s;
            mc.thePlayer.motionZ = Math.cos(correctRotations()) * s;
         }
      }
	 
	 public float correctRotations() {
         float yw = mc.thePlayer.rotationYaw;
         if (mc.thePlayer.moveForward < 0.0F) {
            yw += 180.0F;
         }

         float f;
         if (mc.thePlayer.moveForward < 0.0F) {
            f = -0.5F;
         } else if (mc.thePlayer.moveForward > 0.0F) {
            f = 0.5F;
         } else {
            f = 1.0F;
         }

         if (mc.thePlayer.moveStrafing > 0.0F) {
            yw -= 90.0F * f;
         }
         if (mc.thePlayer.moveStrafing < 0.0F) {
            yw += 90.0F * f;
         }

         yw *= 0.017453292F;
         return yw;
      }
	 
	 public boolean isMoving() {
         return mc.thePlayer.moveForward != 0.0F || mc.thePlayer.moveStrafing != 0.0F;
      }
	
	public void onEvent(Event e) {
		if(e instanceof EventUpdate) {
			if(e.isPre()) {
				boolean b = false;
				 double csp = pythagorasMovement();
			      if (csp != 0.0D) {
			         if (mc.thePlayer.onGround) {
			            if (!b || mc.thePlayer.moveStrafing != 0.0F) {
			               if (mc.thePlayer.hurtTime != mc.thePlayer.maxHurtTime || mc.thePlayer.maxHurtTime <= 0) {
			                  if (!Keyboard.isKeyDown(mc.gameSettings.keyBindJump.keyCode)) {
			                     double val = Client.settingsmanager.getSettingByName("Speed").getValDouble() - (Client.settingsmanager.getSettingByName("Speed").getValDouble() - 1.0D) * 0.5D;		                
			                     
			                     fixMovementSpeed(csp * val, true);
			                  }
			               }
			            }
			         }
			}
		}
	}
}
}
