package net.minecraft.src.MEDMEX.Modules.World;

import java.awt.Color;

import org.lwjgl.input.Keyboard;

import net.minecraft.src.Block;
import net.minecraft.src.Material;
import net.minecraft.src.Packet14BlockDig;
import net.minecraft.src.PlayerControllerMP;
import net.minecraft.src.Vec3D;
import net.minecraft.src.MEDMEX.Event.Event;
import net.minecraft.src.MEDMEX.Event.listeners.EventUpdate;
import net.minecraft.src.MEDMEX.Modules.Module;
import net.minecraft.src.MEDMEX.Modules.Render.BreakProgress;
import net.minecraft.src.MEDMEX.Utils.RenderUtils;

public class Packetmine extends Module{
	  boolean ESP;
	  boolean swing;  
	  boolean hasStarted;
	  Vec3D pos; 
	  float progress;
	public static Packetmine instance;
	public Packetmine() {
		super("Packetmine", Keyboard.KEY_NONE, Category.WORLD);
		this.ESP = true;
	    this.swing = false;
	    this.hasStarted = false;
	    this.progress = 0.0F;	
		instance = this;
	}
	
	public void onRender() {
	    if (this.isEnabled() && this.ESP)
	      if (this.pos != null) {
	    	 int bID = mc.theWorld.getBlockId((int)pos.xCoord, (int)pos.yCoord, (int)pos.zCoord);
	        Material b = mc.theWorld.getBlockMaterial(pos);
	        if (b != Material.air)
	          if (b != Material.air && BreakProgress.instance.isEnabled()) {
	            float i = Math.min(1.0F, this.progress);
	            this.progress += Block.blocksList[bID].blockStrength(this.mc.thePlayer) / 5;
	            RenderUtils.blockESPBoxFilled(this.pos, new Color((int)(40.0F + 100.0F * i), (int)(160.0F - 140.0F * i), 40, 130));
	          }  
	      }  
	  }
	
	
	public boolean onBreak(Vec3D posBlock, int directionFacing) {
	    if (!this.isEnabled())
	      return true; 
	    if (!can_break(posBlock))
	      return true; 
	    if (this.pos == null || !this.pos.equals(posBlock)) {
	      this.progress = 0.0F;
	      this.hasStarted = false;
	    } 
	    this.pos = posBlock;
	    int bID = mc.theWorld.getBlockId((int)posBlock.xCoord, (int)posBlock.yCoord, (int)posBlock.zCoord);
	    this.progress += Block.blocksList[bID].blockStrength(this.mc.thePlayer);
	    mc.getSendQueue().addToSendQueue(new Packet14BlockDig(0, (int)posBlock.xCoord, (int)posBlock.yCoord, (int)posBlock.zCoord, directionFacing));
	    mc.getSendQueue().addToSendQueue(new Packet14BlockDig(2, (int)posBlock.xCoord, (int)posBlock.yCoord, (int)posBlock.zCoord, directionFacing));
	    return false;
	  }
	
	private boolean can_break(Vec3D pos) {
		return mc.theWorld.getBlockId((int)pos.xCoord, (int)pos.yCoord, (int)pos.zCoord) != 7;
	  }
}
