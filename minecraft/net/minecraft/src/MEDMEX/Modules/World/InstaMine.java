package net.minecraft.src.MEDMEX.Modules.World;



import java.awt.Color;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.GuiInventory;
import net.minecraft.src.Item;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemFood;
import net.minecraft.src.ItemPickaxe;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Packet130UpdateSign;
import net.minecraft.src.Packet14BlockDig;
import net.minecraft.src.Packet31RelEntityMove;
import net.minecraft.src.Vec3D;
import net.minecraft.src.MEDMEX.Client;
import net.minecraft.src.MEDMEX.Event.Event;
import net.minecraft.src.MEDMEX.Event.EventPacket;
import net.minecraft.src.MEDMEX.Event.listeners.EventUpdate;
import net.minecraft.src.MEDMEX.Modules.Module;
import net.minecraft.src.MEDMEX.Utils.RenderUtils;
import net.minecraft.src.MEDMEX.Utils.Timer;
import net.minecraft.src.de.Hero.settings.Setting;



public class InstaMine extends Module {
	public static Timer timer = new Timer();
	public static Timer timer2 = new Timer();
	public static boolean runningloop = false;
	int prevItem = -1;
	boolean ESP;
	  boolean swing;
	  boolean ifBlock;
	  boolean onDone;  
	  boolean onPick; 
	  boolean sameItem;  
	  int delay;  
	  public static InstaMine instance;  
	  boolean hasStarted;  
	  public Vec3D pos;  
	  int facing;  
	  float progress;  
	  int cooldown;  
	  Item usedToBreak;
	
	public InstaMine() {
		super("InstaMine", Keyboard.KEY_NONE, Category.WORLD);
		this.ESP = true;
	    this.swing = false;
	    this.ifBlock = true;
	    this.onDone = false;
	    this.onPick = false;
	    this.sameItem = true;
	    this.delay = 0;
	    this.hasStarted = false;
	    this.progress = 0.0F;
	    this.cooldown = 0;
	    this.usedToBreak = null;
	    instance = this;
	}
	
	 public boolean cancelMiningAbortPacket() {
		 
		    return this.isEnabled();
	}
	 
	 public void onRender() {
		    if (this.isEnabled())
		      if (this.pos != null) {
		        RenderUtils.blockESPBox(this.pos, new Color(200, 200, 200, 120));
		        int b = this.mc.theWorld.getBlockId((int)pos.xCoord, (int)pos.yCoord, (int)pos.zCoord);
		        if (b != 0) {
		          float i = Math.min(1.0F, this.progress);
		          RenderUtils.blockESPBoxFilled(this.pos, new Color((int)(40.0F + 100.0F * i), (int)(160.0F - 140.0F * i), 40, 130));
		        } 
		      }  
		  }
	
	public void onEvent(Event e) {
		if(e instanceof EventUpdate) {
			if(e.isPre()) {
				if (this.cooldown > 0) {
				      this.cooldown--;
				    } else if (this.pos != null) {
				    	int b = this.mc.theWorld.getBlockId((int)pos.xCoord, (int)pos.yCoord, (int)pos.zCoord);
				      if (this.hasStarted && (b != 0 || !this.ifBlock)) {
				    	  if(!runningloop)
				    		  prevItem = mc.thePlayer.inventory.currentItem;
				        if (this.swing)
				          this.mc.thePlayer.swingItem(); 
				        this.progress = 1;
				        if (this.onDone && this.progress < 1.0F) 
				          return; 
				        
				        if (this.cooldown > 0)
				          return; 
				        
				        int bestSlot = -1;
						for(int i = 0; i < 9; i++) {
							ItemStack stack =
									mc.thePlayer.inventory.getStackInSlot(i);
								if(stack == null || !(stack.getItem() instanceof ItemPickaxe))
									continue;
								bestSlot = i;
								mc.thePlayer.inventory.currentItem = bestSlot;
								if(timer.hasTimeElapsed(50, true)) {
									runningloop = true;
								}
								
								}
						
						
						}
				      
				      if(runningloop) {
							if(timer.hasTimeElapsed(55, true)) {
								mc.thePlayer.inventory.currentItem = prevItem;
								runningloop = false;
							}
								
								
						}
						if(mc.thePlayer.inventory.getCurrentItem() != null)
				        if (this.sameItem && this.usedToBreak != mc.thePlayer.inventory.getCurrentItem().getItem() )
				          return; 
				        mc.getSendQueue().addToSendQueue(new Packet14BlockDig(2, (int)pos.xCoord, (int)pos.yCoord, (int)pos.zCoord, this.facing));
				        this.cooldown = this.delay;
				        if(mc.thePlayer.inventory.getCurrentItem() != null)
				        this.usedToBreak = mc.thePlayer.inventory.getCurrentItem().getItem();
				      } 
				    } 
				
			}
				
			}
		
		
				
			
	public boolean onBreak(Vec3D posBlock,int directionFacing) {
	    if (this.onPick && Item.pickaxeDiamond != mc.thePlayer.inventory.getCurrentItem().getItem())
	      return true; 
	    if (this.pos == null || !this.pos.equals(posBlock)) {
	      this.progress = 0.0F;
	      this.hasStarted = false;
	      this.usedToBreak = null;
	    } 
	    this.pos = posBlock;
	    this.facing = directionFacing;
	    if (!this.hasStarted) {
	    	mc.getSendQueue().addToSendQueue(new Packet14BlockDig(0, (int)pos.xCoord, (int)pos.yCoord, (int)pos.zCoord, this.facing));
	    	mc.getSendQueue().addToSendQueue(new Packet14BlockDig(0, (int)pos.xCoord, (int)pos.yCoord, (int)pos.zCoord, this.facing));
	    	if(mc.thePlayer.inventory.getCurrentItem() != null)
	      this.usedToBreak = mc.thePlayer.inventory.getCurrentItem().getItem();
	      this.hasStarted = true;
	    } 
	    return false;
	  }
	

	
	

	

	

}
