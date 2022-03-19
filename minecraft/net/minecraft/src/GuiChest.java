package net.minecraft.src;

import org.lwjgl.opengl.GL11;

import net.minecraft.src.MEDMEX.Client;
import net.minecraft.src.MEDMEX.Modules.Player.Freecam;

public class GuiChest extends GuiContainer {
    private IInventory upperChestInventory;
    private IInventory lowerChestInventory;
    private int inventoryRows = 0;

    public GuiChest(IInventory var1, IInventory var2) {
        super(new ContainerChest(var1, var2));
        this.upperChestInventory = var1;
        this.lowerChestInventory = var2;
        this.allowUserInput = false;
        short var3 = 222;
        int var4 = var3 - 108;
        this.inventoryRows = var2.getSizeInventory() / 9;
        this.ySize = var4 + this.inventoryRows * 18;
    }
    

    public void initGui() {
    	super.initGui();
    	int posY = (height - ySize)/2 - 20;
    	this.controlList.add(new GuiButton(1,width /2-88,posY,60,20,"Mount"));
    	this.controlList.add(new GuiButton(2,width /2+28,posY,60,20,"Freecam"));
    }

    protected void drawGuiContainerForegroundLayer() {
        this.fontRenderer.drawString(this.lowerChestInventory.getInvName(), 8, 6, 4210752);
        this.fontRenderer.drawString(this.upperChestInventory.getInvName(), 8, this.ySize - 96 + 2, 4210752);
    }

    protected void drawGuiContainerBackgroundLayer(float var1) {
        int var2 = this.mc.renderEngine.getTexture("/gui/container.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(var2);
        int var3 = (this.width - this.xSize) / 2;
        int var4 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var3, var4, 0, 0, this.xSize, this.inventoryRows * 18 + 17);
        this.drawTexturedModalRect(var3, var4 + this.inventoryRows * 18 + 17, 0, 126, this.xSize, 96);
    }
    
    protected void actionPerformed(GuiButton par1) {
    	if(par1.id == 1) {
    		try {
    			for(Entity e : mc.theWorld.loadedEntityList) {
    				if(e instanceof EntityBoat) {
    					if(mc.thePlayer.getDistanceSqToEntity(e) < 6) {
    						mc.playerController.interactWithEntity(mc.thePlayer, e);
    					}
    				}
    			}
    			}catch(Exception e){
    		
    		}
    	}
    	if(par1.id == 2) {
    		try {
    			Freecam.instance.toggle();
    			}catch(Exception e){
    		
    		}
    	}  	
    }
}
