package net.minecraft.src.MEDMEX.Modules.Render;

import java.awt.Color;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityAnimal;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityMob;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.RenderGlobal;
import net.minecraft.src.RenderManager;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntityChest;
import net.minecraft.src.TileEntityFurnace;
import net.minecraft.src.Vec3D;
import net.minecraft.src.MEDMEX.Client;
import net.minecraft.src.MEDMEX.Event.Event;
import net.minecraft.src.MEDMEX.Event.listeners.EventUpdate;
import net.minecraft.src.MEDMEX.Modules.Module;
import net.minecraft.src.MEDMEX.Utils.RenderUtils;
import net.minecraft.src.de.Hero.settings.Setting;

public class Tracers extends Module{

	public static Tracers instance;
	public Tracers() {
		super("Tracers", Keyboard.KEY_NONE, Category.RENDER);
		instance = this;
	}
	
	public static Vec3D interpolateEntity(Entity entity, float time)
    {
        return new Vec3D(entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * time,
                entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * time,
                entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * time);
    }
	
	public void onRender() {
		if(this.isEnabled()) {
			double size = 0.45;
            double ytSize = 0.35;
            for(int x = 0; x < mc.theWorld.playerEntities.size(); x ++) {
                EntityPlayer entity = (EntityPlayer) mc.theWorld.playerEntities.get(x); 
                double X = entity.posX;
                double Y = entity.posY;
                double Z = entity.posZ;
                double RenderX =  X - RenderManager.renderPosX;
                double RenderY = Y - RenderManager.renderPosY;
                double RenderZ = Z - RenderManager.renderPosZ;
                if(!entity.username.contains(" ") && (entity instanceof EntityPlayer)) {
                	if(!entity.username.equals(mc.thePlayer.username)) {
                	
                	final Vec3D pos = interpolateEntity(entity, mc.timer.renderPartialTicks).subtract(RenderManager.renderPosX, RenderManager.renderPosY, RenderManager.renderPosZ);
                	if(pos != null) {
                	 final boolean bobbing = mc.gameSettings.viewBobbing;
                     mc.gameSettings.viewBobbing = false;
                     mc.entityRenderer.setupCameraTransform(mc.timer.renderPartialTicks, 0);
                     final Vec3D forward = new Vec3D(0, 0, 1).rotatePitch(-(float) Math.toRadians(Minecraft.theMinecraft.thePlayer.rotationPitch)).rotateYaw(-(float) Math.toRadians(Minecraft.theMinecraft.thePlayer.rotationYaw));
                     if(Client.friends.contains(entity.username)) {
                     RenderUtils.drawLine3D((float) forward.xCoord, (float) forward.yCoord, (float) forward.zCoord, (float) pos.xCoord, (float) pos.yCoord, (float) pos.zCoord, 1f, new Color(25, 204, 25).getRGB());
                     }else {
                     RenderUtils.drawLine3D((float) forward.xCoord, (float) forward.yCoord, (float) forward.zCoord, (float) pos.xCoord, (float) pos.yCoord, (float) pos.zCoord, 1f, new Color(181, 147, 219).getRGB());
                     }
                     mc.gameSettings.viewBobbing = bobbing;
                     mc.entityRenderer.setupCameraTransform(mc.timer.renderPartialTicks, 0);
                	}
                	}
                }
            }
                
                
		}
	}
}
