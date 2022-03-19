package net.minecraft.src.MEDMEX.Modules.Render;

import java.awt.Color;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.RenderGlobal;
import net.minecraft.src.RenderManager;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntityChest;
import net.minecraft.src.TileEntityFurnace;
import net.minecraft.src.Vec3D;
import net.minecraft.src.MEDMEX.Event.Event;
import net.minecraft.src.MEDMEX.Event.listeners.EventUpdate;
import net.minecraft.src.MEDMEX.Modules.Module;
import net.minecraft.src.MEDMEX.Utils.RenderUtils;

public class ChestESP extends Module{
	public static ChestESP instance;
	public Color ChestColor;
	public Color FurnaceColor;
	public ChestESP() {
		super("ChestESP", Keyboard.KEY_NONE, Category.RENDER);
		this.ChestColor = new Color(40, 40, 220);
		this.FurnaceColor = new Color(10, 10, 10);
		instance = this;
	}
		
	public void onRender() {
		if(this.isEnabled()) {
		try {
		
			for(TileEntity o : mc.theWorld.loadedTileEntityList) {
				int cX = o.xCoord;
				int cY = o.yCoord;
				int cZ = o.zCoord;
				double renderX = cX - RenderManager.renderPosX;
		    	double renderY = cY - RenderManager.renderPosY;
		    	double renderZ = cZ - RenderManager.renderPosZ;
		    	float Alpha = (float)Math.max(0.20000000298023224D, Math.min(0.6D, (0.02F * mc.thePlayer.getDistance(o.xCoord, o.yCoord, o.zCoord))));
		    	if(o instanceof TileEntityChest) {
		    		if(mc.theWorld.getBlockTileEntity(cX, cY, cZ).getBlockType() != null) {
		    		 AxisAlignedBB B = (mc.theWorld.getBlockTileEntity(cX, cY, cZ).getBlockType().getSelectedBoundingBoxFromPool(mc.theWorld, cX, cY, cZ));
		    		 Vec3D adjacentchest = findAdjacentChest(cX, cY, cZ);
		    		 if(adjacentchest != null) {
		    			 if(adjacentchest.xCoord == 1) {
		    				 B.maxX += 1;
		    			 }
		    			 if(adjacentchest.xCoord == -1) {
		    				 B.minX -= 1;
		    			 }
		    			 if(adjacentchest.zCoord == 1) {
		    				 B.maxZ += 1;
		    			 }
		    			 if(adjacentchest.zCoord == -1) {
		    				 B.minZ -= 1;
		    			 }
		    		 }
		    			 
			    	 if(!hasAdjacent(cX, cY, cZ)) {
			    		 RenderUtils.boundingESPBox(B, new Color(ChestColor.getRed(), ChestColor.getGreen(), ChestColor.getBlue(), (int)((120.0F + ChestColor.getAlpha() / 2.0F) * Alpha)));
			   	      	 Alpha *= 0.8F;
			   	      	 RenderUtils.boundingESPBoxFilled(B, new Color(ChestColor.getRed(), ChestColor.getGreen(), ChestColor.getBlue(), (int)((120.0F + ChestColor.getAlpha() / 2.0F) * Alpha)));
		    		 }
		    	}
		    	}
		    	if(o instanceof TileEntityFurnace) {
		    		if(mc.theWorld.getBlockTileEntity(cX, cY, cZ).getBlockType() != null) {
		    	 AxisAlignedBB B = (mc.theWorld.getBlockTileEntity(cX, cY, cZ).getBlockType().getSelectedBoundingBoxFromPool(mc.theWorld, cX, cY, cZ));
	    		 RenderUtils.boundingESPBox(B, new Color(FurnaceColor.getRed(), FurnaceColor.getGreen(), FurnaceColor.getBlue(), (int)((120.0F + FurnaceColor.getAlpha() / 2.0F) * Alpha)));
	   	      	 Alpha *= 0.8F;
	   	      	 RenderUtils.boundingESPBoxFilled(B, new Color(FurnaceColor.getRed(), FurnaceColor.getGreen(), FurnaceColor.getBlue(), (int)((120.0F + FurnaceColor.getAlpha() / 2.0F) * Alpha)));
		    		
		    		}
		    	}
				
			}
			}catch(Exception e) {
				
			}
		}
	}
	
	public boolean hasAdjacent(int x, int y, int z) {
		if(mc.theWorld.getBlockTileEntity(x-1, y, z) instanceof TileEntityChest){
			return true;
		}
		if(mc.theWorld.getBlockTileEntity(x, y, z-1) instanceof TileEntityChest){
			return true;
		}

		return false;
	}
	
	public Vec3D findAdjacentChest(int x, int y, int z) {
		if(mc.theWorld.getBlockTileEntity(x+1, y, z) instanceof TileEntityChest){
			return new Vec3D(1, 0, 0);
		}
		if(mc.theWorld.getBlockTileEntity(x, y, z+1) instanceof TileEntityChest){
			return new Vec3D(0, 0, 1);
		}

		return null;
	}
	
}
