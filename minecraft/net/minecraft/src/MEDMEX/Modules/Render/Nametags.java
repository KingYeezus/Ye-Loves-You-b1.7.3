package net.minecraft.src.MEDMEX.Modules.Render;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.GuiIngame;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.RenderHelper;
import net.minecraft.src.RenderManager;
import net.minecraft.src.ScaledResolution;
import net.minecraft.src.Tessellator;
import net.minecraft.src.Vec3D;
import net.minecraft.src.MEDMEX.Client;
import net.minecraft.src.MEDMEX.Event.Event;
import net.minecraft.src.MEDMEX.Event.listeners.EventUpdate;
import net.minecraft.src.MEDMEX.Modules.Module;

public class Nametags extends Module{
	public static int hotbarwidth;
	public static Nametags instance;
	public Nametags() {
		super("Nametags", Keyboard.KEY_NONE, Category.RENDER);
		instance = this;
	}
	
	public void onRender() {
		for(Entity e : mc.theWorld.loadedEntityList) {
			if(e != mc.thePlayer && e instanceof EntityPlayer) {
				EntityPlayer p = (EntityPlayer)e;
				renderNameTag(p);
			}
		}
	}
	
	
	public static void renderNameTag(EntityPlayer e) {
		Minecraft mc = Minecraft.theMinecraft;
		int MaxSize = 4;
		int MinSize = 4;
		float dis = Math.max(3.0F, Math.min(getDistanceRender((Entity)e), 80.0F)) - 2.0F;
	    float ma = MaxSize * 0.05F;
	    float mi = MinSize * 0.005F;
	    float m = ma - mi;
	    float size = mi + m / 80.0F * dis;
	    double p_188388_2_ = mc.timer.renderPartialTicks;
	    double d0 = e.lastTickPosX + (e.posX - e.lastTickPosX) * p_188388_2_;
	    double d1 = e.lastTickPosY + (e.posY - e.lastTickPosY) * p_188388_2_;
	    double d2 = e.lastTickPosZ + (e.posZ - e.lastTickPosZ) * p_188388_2_;
	    float x = (float)d0;
	    boolean flag = e.isSneaking();
	    float f2 = e.height + 0.5F - (flag ? 0.25F : 0.0F);
	    float y = (float)d1 + f2;
	    float z = (float)d2;
	    y += size * 10.0F;
	    if (RenderManager.options == null)
	      return; 
	    float p_189692_6_ = RenderManager.playerViewY;
	    float p_189692_7_ = RenderManager.playerViewX;
	    float p_189692_2_ = (float)(x - (RenderManager.field_1222_l));
	    float p_189692_3_ = (float)(y - (RenderManager.field_1221_m));
	    float p_189692_4_ = (float)(z - (RenderManager.field_1220_n));
	    FontRenderer p_189692_0_ = mc.fontRenderer;
	    GL11.glPushMatrix();
	    GL11.glTranslatef(p_189692_2_, p_189692_3_, p_189692_4_);
	    GL11.glNormal3f(0.0F, 1.0F, 0.0F);
	    GL11.glRotatef(-p_189692_6_, 0.0F, 1.0F, 0.0F);
	    GL11.glRotatef((1 * p_189692_7_), 1.0F, 0.0F, 0.0F);
	    GL11.glScalef(-size, -size, size);
	    GL11.glDisable(2896 /*GL_LIGHTING*/);
	    GL11.glDepthMask(false);
	    GL11.glDisable(2929 /*GL_DEPTH_TEST*/);
	    String name = getDisplayName((EntityPlayer) e);
	    GL11.glEnable(3042 /*GL_BLEND*/);
	    GL11.glBlendFunc(770, 771);
	    int i = p_189692_0_.getStringWidth(name) / 2;
	    GL11.glDisable(3553 /*GL_TEXTURE_2D*/);
	    Tessellator tessellator = Tessellator.instance;
	    tessellator.startDrawingQuads();
	    EntityPlayer p = (EntityPlayer)e;
	    if(Client.friends.contains(((EntityPlayer) e).username)) {
	    	tessellator.setColorRGBA_F(0.1F, 0.8F, 0.1F, 0.5F);
	    }else {
	    tessellator.setColorRGBA_F(0.1F, 0.1F, 0.1F, 0.5F);
	    }
	    tessellator.addVertex((-i - 1), -1.0D, 0.0D);
	    tessellator.addVertex((-i - 1), 8.0D, 0.0D);
	    tessellator.addVertex((i + 1), 8.0D, 0.0D);
	    tessellator.addVertex((i + 1), -1.0D, 0.0D);
	    tessellator.draw();
	    tessellator.startDrawingQuads();
	    tessellator.setColorRGBA_F(0.886f, 0.854f, 0.854f, 0.5f);
	    tessellator.addVertex((-i - 1.5), -1, 0.0D);
	    tessellator.addVertex((-i - 1.5), 8, 0.0D);
	    tessellator.addVertex((-i - 1), 8, 0.0D);
	    tessellator.addVertex((-i - 1), -1, 0.0D);
	    tessellator.draw();
	    tessellator.startDrawingQuads();
	    tessellator.setColorRGBA_F(0.886f, 0.854f, 0.854f, 0.5f);
	    tessellator.addVertex((i + 1), -1, 0.0D);
	    tessellator.addVertex((i + 1), 8, 0.0D);
	    tessellator.addVertex((i + 1.5), 8, 0.0D);
	    tessellator.addVertex((i + 1.5), -1, 0.0D);
	    tessellator.draw();
	    tessellator.startDrawingQuads();
	    tessellator.setColorRGBA_F(0.886f, 0.854f, 0.854f, 0.5f);
	    tessellator.addVertex((-i - 1), -1.5, 0.0D);
	    tessellator.addVertex((-i - 1), -1, 0.0D);
	    tessellator.addVertex((i + 1), -1, 0.0D);
	    tessellator.addVertex((i + 1), -1.5, 0.0D);
	    tessellator.draw();
	    tessellator.startDrawingQuads();
	    tessellator.setColorRGBA_F(0.886f, 0.854f, 0.854f, 0.5f);
	    tessellator.addVertex((-i - 1), 8, 0.0D);
	    tessellator.addVertex((-i - 1), 8.5, 0.0D);
	    tessellator.addVertex((i + 1), 8.5, 0.0D);
	    tessellator.addVertex((i + 1), 8, 0.0D);
	    tessellator.draw();
	    GL11.glEnable(3553 /*GL_TEXTURE_2D*/);
	    GL11.glDepthMask(true);
	    ArrayList<ItemStack> items = getRenderStacks((EntityPlayer) e);
	    int xOffset = items.size() * 8;
	    for (ItemStack stack : items) {
	        xOffset -= 16;
	        renderItemStack(stack, xOffset, -20, 0.7f, false);
	     }  
	    
	    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	    //GlStateManager.enableRescaleNormal();
	    GL11.glDisable(2896 /*GL_LIGHTING*/);
	    GL11.glDisable(2929 /*GL_DEPTH_TEST*/);
	    GL11.glDisable(3042 /*GL_BLEND*/);
	    p_189692_0_.drawString(name, -p_189692_0_.getStringWidth(name) / 2, 0, 16777215);
	    GL11.glEnable(2929 /*GL_DEPTH_TEST*/);
	    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	    GL11.glScalef(-1.0F, -1.0F, size);
	    GL11.glPopMatrix();
	}
	
	
	public static float getDistanceRender(Entity entityIn) {
	    Vec3D pPos = 
	      (Minecraft.theMinecraft).thePlayer.func_174824_e(Minecraft.theMinecraft.timer.renderPartialTicks);
	    float f = (float)(pPos.xCoord - entityIn.posX + (entityIn.posX - entityIn.lastTickPosX) * Minecraft.theMinecraft.timer.renderPartialTicks);
	    float f1 = (float)(pPos.yCoord - entityIn.posY + (entityIn.posY - entityIn.lastTickPosY) * Minecraft.theMinecraft.timer.renderPartialTicks);
	    float f2 = (float)(pPos.zCoord - entityIn.posZ + (entityIn.posZ - entityIn.lastTickPosZ) * Minecraft.theMinecraft.timer.renderPartialTicks);
	    return MathHelper.sqrt_float(f * f + f1 * f1 + f2 * f2);
	  }
    
	public static String getDisplayName(EntityPlayer e) {
	    String[] hs = { "§4", "§6", "§2"};
	    Minecraft mc = Minecraft.theMinecraft;
	    String name = e.username;
	    int h = Math.round(mc.thePlayer.getDistanceToEntity(e));
	    name = String.valueOf(name) + " " + hs[Math.round(0.1F * Math.min(h, 20))] + h;
	    return name;
	  }
	
	public static ArrayList<ItemStack> getRenderStacks(EntityPlayer e) {
	    ArrayList<ItemStack> stacks = new ArrayList<>();
	    for (ItemStack stack : e.inventory.armorInventory) {
	      if (stack != null) {
	        Item item = stack.getItem();
	        if (item != null)
	          stacks.add(stack); 
	      } 
	    } 
	    if (e.inventory.mainInventory != null)
	      stacks.add(e.inventory.mainInventory[0]); 
	    return stacks;
	  }
	
	
	public static void renderItemStack(ItemStack item, int xOffset, int yOffset, float scale, boolean enchants) {
	    GL11.glPushMatrix();
	    GL11.glDepthMask(true);
	    GL11.glScalef(scale, scale, scale);
	    Minecraft mc = Minecraft.theMinecraft;
	    //GL11.glClear(256);
	    RenderHelper.enableStandardItemLighting();
	    GL11.glDepthMask(true);
	    //(mc.getRenderItem()).zLevel = -150.0F;
	    GL11.glEnable(2929 /*GL_DEPTH_TEST*/);
	    //GlStateManager.enableRescaleNormal();
	    GuiIngame.itemRenderer.renderItemIntoGUI(mc.fontRenderer, mc.renderEngine ,item, xOffset, yOffset);
	    GuiIngame.itemRenderer.renderItemOverlayIntoGUI(mc.fontRenderer, mc.renderEngine, item, xOffset, yOffset);
	    RenderHelper.disableStandardItemLighting();
	    //(mc.getRenderItem()).zLevel = 0.0F;
	    GL11.glScalef(0.5F, 0.5F, 0.5F);
	    GL11.glDisable(2896 /*GL_LIGHTING*/);
	    GL11.glDisable(2929 /*GL_DEPTH_TEST*/);
	    GL11.glDisable(3042 /*GL_BLEND*/); 
	    GL11.glEnable(2896 /*GL_LIGHTING*/);
	    GL11.glEnable(2929 /*GL_DEPTH_TEST*/);
	    GL11.glPopMatrix();
	  }
	
	}
