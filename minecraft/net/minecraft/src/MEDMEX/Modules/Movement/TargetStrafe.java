package net.minecraft.src.MEDMEX.Modules.Movement;


import java.awt.Color;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.MathHelper;
import net.minecraft.src.RenderManager;
import net.minecraft.src.MEDMEX.Client;
import net.minecraft.src.MEDMEX.Event.Event;
import net.minecraft.src.MEDMEX.Event.listeners.EventUpdate;
import net.minecraft.src.MEDMEX.Modules.Module;
import net.minecraft.src.MEDMEX.Modules.Combat.ForceField;
import net.minecraft.src.de.Hero.settings.Setting;
import net.minecraft.client.Minecraft;



public class TargetStrafe extends Module {
	public static TargetStrafe instance;
	static int direction = -1;

	public TargetStrafe() {
		super("TargetStrafe", Keyboard.KEY_NONE, Category.MOVEMENT);
		instance = this;
	}
	 public void setup() {
	        Client.settingsmanager.rSetting(new Setting("Strafe Speed", this, 0.25, 0, 1, false));
	        Client.settingsmanager.rSetting(new Setting("Strafe Distance", this, 4, 0, 6, false));
	       
	        
	    }
	
	public void onEvent(Event e) {
		if(e instanceof EventUpdate) {
			if(e.isPre()) {
				 if (mc.thePlayer.isCollidedHorizontally) {
		             switchDirection();
		         }
				 
				 if(ForceField.target != null) {
					 doStrafeAtSpeed(Client.settingsmanager.getSettingByName("Strafe Speed").getValDouble());
				 }
			}	
		}		
	}

	
	private void switchDirection() {
        if (direction == 1) {
            direction = -1;
        } else {
            direction = 1;
        }
    }
	
	public void onRender() {
		if(ForceField.target != null)
		drawCircle(ForceField.target, mc.timer.renderPartialTicks, Client.settingsmanager.getSettingByName("Strafe Distance").getValDouble());
	}
	
	 public final boolean doStrafeAtSpeed(final double moveSpeed) {
	        final boolean strafe = canStrafe();

	        if (strafe) {
	            float[] rotations = getNeededRotations(ForceField.target);
	            if (mc.thePlayer.getDistanceToEntity(ForceField.target) <= Client.settingsmanager.getSettingByName("Strafe Distance").getValDouble()) {
	               setSpeed(moveSpeed, rotations[0], direction, 0);
	            } else {
	                setSpeed(moveSpeed, rotations[0], direction, 1);
	            }
	        }

	        return strafe;
	    }
	 public void setSpeed(final double moveSpeed, final float pseudoYaw, final double pseudoStrafe, final double pseudoForward) {
	        double forward = pseudoForward;
	        double strafe = pseudoStrafe;
	        float yaw = pseudoYaw;

	        if (forward == 0.0 && strafe == 0.0) {
	            mc.thePlayer.motionZ = 0;
	            mc.thePlayer.motionX = 0;
	        } else {
	            if (forward != 0.0) {
	                if (strafe > 0.0) {
	                    yaw += ((forward > 0.0) ? -45 : 45);
	                } else if (strafe < 0.0) {
	                    yaw += ((forward > 0.0) ? 45 : -45);
	                }
	                strafe = 0.0;
	                if (forward > 0.0) {
	                    forward = 1.0;
	                } else if (forward < 0.0) {
	                    forward = -1.0;
	                }
	            }
	            final double cos = Math.cos(Math.toRadians(yaw + 90.0f));
	            final double sin = Math.sin(Math.toRadians(yaw + 90.0f));

	            mc.thePlayer.motionX = (forward * moveSpeed * cos + strafe * moveSpeed * sin);
	            mc.thePlayer.motionZ = (forward * moveSpeed * sin - strafe * moveSpeed * cos);
	        }
	    }
	 
	 public  float[] getNeededRotations(Entity entityIn) {
	        double d0 = entityIn.posX - mc.thePlayer.posX;
	        double d1 = entityIn.posZ - mc.thePlayer.posZ;
	        double d2 = entityIn.posY + entityIn.getEyeHeight() - (mc.thePlayer.boundingBox.minY + (mc.thePlayer.boundingBox.maxY - mc.thePlayer.boundingBox.minY));
	        double d3 = MathHelper.sqrt_double(d0 * d0 + d1 * d1);
	        float f = (float) (Math.atan2(d1, d0) * 180.0D / Math.PI) - 90.0F;
	        float f1 = (float) (-(Math.atan2(d2, d3) * 180.0D / Math.PI));
	        return new float[]{f, f1};
	    }
	 
	 
	 private void drawCircle(Entity entity, float partialTicks, double rad) {
	       	GL11.glPushMatrix();
	        GL11.glDisable(GL11.GL_TEXTURE_2D);
	        GL11.glDisable(GL11.GL_DEPTH_TEST);
	        GL11.glDepthMask(false);
	        GL11.glLineWidth(1.0f);
	        GL11.glBegin(GL11.GL_LINE_STRIP);

	        final double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks - RenderManager.field_1222_l;
	        final double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks - RenderManager.field_1221_m;
	        final double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks - RenderManager.field_1220_n;

	        final float r = ((float) 1 / 255) * Color.WHITE.getRed();
	        final float g = ((float) 1 / 255) * Color.WHITE.getGreen();
	        final float b = ((float) 1 / 255) * Color.WHITE.getBlue();

	        final double pix2 = Math.PI * 2.0D;

	        for (int i = 0; i <= 90; ++i) {
	        	GL11.glColor3f(r, g, b);
	        	GL11.glVertex3d(x + rad * Math.cos(i * pix2 / 45.0), y, z + rad * Math.sin(i * pix2 / 45.0));
	        }

	        GL11.glEnd();
	        GL11.glDepthMask(true);
	        GL11.glEnable(GL11.GL_DEPTH_TEST);
	        GL11.glEnable(GL11.GL_TEXTURE_2D);
	        GL11.glPopMatrix();
	    }
	 
	 public static boolean canStrafe() {
	        return ForceField.target != null && TargetStrafe.instance.isEnabled();
	    }
	
	
	

}
