package net.minecraft.src.MEDMEX.Modules.Render;

import java.awt.Color;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.MathHelper;
import net.minecraft.src.RenderManager;
import net.minecraft.src.Tessellator;
import net.minecraft.src.Vec3D;
import net.minecraft.src.MEDMEX.Client;
import net.minecraft.src.MEDMEX.Event.Event;
import net.minecraft.src.MEDMEX.Event.listeners.EventUpdate;
import net.minecraft.src.MEDMEX.Modules.Module;
import net.minecraft.src.MEDMEX.Utils.RenderUtils;

public class Waypoints extends Module{
	public ArrayList<WayPoint> wayPointList;
	public static Waypoints instance;
	float tagScale;
	  float pillarRadius;
	  int closeClip;
	  float distanceScaleAmount;
	public Waypoints() {
		super("Waypoints", Keyboard.KEY_NONE, Category.RENDER);
		this.tagScale = 4.0F;
	    this.pillarRadius = 0.5F;
	    this.closeClip = 0;
	    this.distanceScaleAmount = 0.5F;
		instance = this;
		this.wayPointList = new ArrayList<>();
	}
	public class WayPoint {
	    public Vec3D pos;
	    public String name;
	    public String server;
	    public int dim;
	    
	    public WayPoint(Vec3D p, String n, String s, int d) {
	      this.pos = p;
	      this.name = n;
	      this.server = s;
	      this.dim = d;
	    }
	  }
	
	public void remove(String s) {
	    for (WayPoint w : instance.wayPointList) {
	      if (w.name.equalsIgnoreCase(s)) {
	        instance.wayPointList.remove(w);
	        Client.addChatMessage("Removed Waypoint §8" + s);
	        return;
	      } 
	    } 
	    Client.addChatMessage("Waypoint §8" + s + "can not be found!");
	  }
	  
	  public void addPoint(Vec3D p, String n, String s, int d) {
	    for (WayPoint w : instance.wayPointList) {
	      if (w.name.equalsIgnoreCase(n)) {
	        Client.addChatMessage("The wayPoint §8" + n + "already exists");
	        return;
	      } 
	    } 
	    add(p, n, s, d);
	    Client.addChatMessage("Added new waypoint!");
	    Client.addChatMessage("Name: §8"+ n);
	    Client.addChatMessage("Coords: §7"+ (int)p.xCoord + "/" + (int)p.yCoord + "/" + (int)p.zCoord + "]");
	    String dim = "§2overworld";
	    if (d == 1)
	      dim = "§6end"; 
	    if (d == -1)
	      dim = "§4nether"; 
	    Client.addChatMessage("Dimension: " + dim);
	  }
	  public void add(Vec3D p, String n, String s, int d) {
		    this.wayPointList.add(new WayPoint(p, n, s, d));
		  }
	  
	  public void onRender() {
		    String currentAddress = (this.mc.serverName != null) ? (this.mc.serverName) : "singleplayer";
		    if (this.isEnabled())
		      for (WayPoint w : this.wayPointList) {
		        if (!w.server.equalsIgnoreCase(currentAddress))
		          continue; 
		        if (w.dim == 1 && this.mc.thePlayer.dimension != 1)
		          continue; 
		        if (this.mc.thePlayer.dimension == 1 && w.dim != 1)
		          continue; 
		        double x = w.pos.xCoord;
		        double y = w.pos.yCoord;
		        double z = w.pos.zCoord;
		        if (w.dim == -1 && this.mc.thePlayer.dimension == 0) {
		          x *= 8.0D;
		          z *= 8.0D;
		        } else if (w.dim == 0 && this.mc.thePlayer.dimension == -1) {
		          x /= 8.0D;
		          z /= 8.0D;
		        } 
		        double distance = this.mc.thePlayer.getDistance(x, y, z);
		        if (distance < this.closeClip)
		          continue; 
		        String displayName = String.valueOf(w.name) + " " + Math.round(distance) + "m";
		        if (w.dim != this.mc.thePlayer.dimension)
		          switch (w.dim) {
		            case 0:
		              displayName = String.valueOf(displayName) + "§2Overworld" ;
		              break;
		            case -1:
		              displayName = String.valueOf(displayName) + "§4Nether" ;
		              break;
		          }  
		        if (distance < 500.0D && distance > 10.0D && this.pillarRadius > 0.0F)
		        	RenderUtils.boundingESPBox(new AxisAlignedBB(x - this.pillarRadius, -50.0D, z - this.pillarRadius, x + this.pillarRadius, 250.0D, z + this.pillarRadius), new Color(240, 20, 20, 80)); 
		        if (distance > 10.0D) {
		          Vec3D pos = this.mc.thePlayer.getPositionEyes(this.mc.timer.renderPartialTicks);
		          double playerPosx = pos.xCoord;
		          double playerPosy = pos.yCoord;
		          double playerPosz = pos.zCoord;
		          Vec3D dir = (new Vec3D(-playerPosx + x, -playerPosy + y, -playerPosz + z).normalize().scale(10.0D));
		          x = playerPosx + dir.xCoord;
		          y = playerPosy + dir.yCoord;
		          z = playerPosz + dir.zCoord;
		        } 
		        float baseScale = 0.01F * this.tagScale;
		        baseScale = (float)(baseScale - (0.005F * this.tagScale * this.distanceScaleAmount) * Math.min(1.0D, 9.999999747378752E-6D * distance));
		        renderTagString(displayName, baseScale, 0, (float)x, (float)y, (float)z, 0.33F, 0.33F, 0.33F, 1F, (new Color(255, 0, 0)).getRGB());
		      }  
		  }
	  
	  	public float[] getLegitRenderRotations(Vec3D vec, EntityPlayer me) {
		    Vec3D eyesPos = getEyesPosRender();
		    double diffX = vec.xCoord - eyesPos.xCoord;
		    double diffY = vec.yCoord - eyesPos.yCoord;
		    double diffZ = vec.zCoord - eyesPos.zCoord;
		    double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
		    float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0F;
		    float pitch = (float)-Math.toDegrees(Math.atan2(diffY, diffXZ));
		    return 
		      new float[] { (Minecraft.theMinecraft).thePlayer.rotationYaw + MathHelper.wrapDegrees(yaw - (Minecraft.theMinecraft).thePlayer.rotationYaw), 
		        (Minecraft.theMinecraft).thePlayer.rotationPitch + MathHelper.wrapDegrees(pitch - (Minecraft.theMinecraft).thePlayer.rotationPitch) };
		  }
		  
		  private Vec3D getEyesPosRender() {
		    double x = RenderManager.field_1222_l;
		    double y = RenderManager.field_1221_m;
		    double z = RenderManager.field_1220_n;
		    return new Vec3D(x, y + (Minecraft.theMinecraft).thePlayer.getEyeHeight(), z);
		  }
	  public static void renderTagString(String name, float size, int height, float x, float y, float z, float r, float g, float b, float a, int textCol) {
		    Minecraft mc = Minecraft.theMinecraft;
		    if (RenderManager.options == null)
		      return; 
		    float p_189692_6_ = RenderManager.playerViewY;
		    float p_189692_7_ = RenderManager.playerViewX;
		    float p_189692_2_ = (float)(x - RenderManager.field_1222_l);
		    float p_189692_3_ = (float)(y - RenderManager.field_1221_m);
		    float p_189692_4_ = (float)(z - RenderManager.field_1220_n);
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
		    GL11.glEnable(3042 /*GL_BLEND*/);
		    GL11.glBlendFunc(770, 771);
		    int i = p_189692_0_.getStringWidth(name) / 2;
		    GL11.glDisable(3553 /*GL_TEXTURE_2D*/);
		    Tessellator tessellator = Tessellator.instance;
		    tessellator.startDrawingQuads();
		    tessellator.setColorRGBA_F(r, g, b, a);
		    tessellator.addVertex((-i - 1), (-1 + height), 0.0D);
		    tessellator.addVertex((-i - 1), (8 + height), 0.0D);
		    tessellator.addVertex((i + 1), (8 + height), 0.0D);
		    tessellator.addVertex((i + 1), (-1 + height), 0.0D);
		    tessellator.draw();
		    tessellator.startDrawingQuads();
		    tessellator.setColorRGBA_F(255, 0, 0, 255);
		    tessellator.addVertex((-i - 2), (-1 + height), 0.0D);
		    tessellator.addVertex((-i - 2), (8 + height), 0.0D);
		    tessellator.addVertex((-i - 1), (8 + height), 0.0D);
		    tessellator.addVertex((-i - 1), (-1 + height), 0.0D);
		    tessellator.draw();
		    tessellator.startDrawingQuads();
		    tessellator.setColorRGBA_F(255, 0, 0, 255);
		    tessellator.addVertex((i + 1), (-1 + height), 0.0D);
		    tessellator.addVertex((i + 1), (8 + height), 0.0D);
		    tessellator.addVertex((i + 2), (8 + height), 0.0D);
		    tessellator.addVertex((i + 2), (-1 + height), 0.0D);
		    tessellator.draw();
		    tessellator.startDrawingQuads();
		    tessellator.setColorRGBA_F(255, 0, 0, 255);
		    tessellator.addVertex((-i - 1), (-2 + height), 0.0D);
		    tessellator.addVertex((-i - 1), (-1 + height), 0.0D);
		    tessellator.addVertex((i + 1), (-1 + height), 0.0D);
		    tessellator.addVertex((i + 1), (-2 + height), 0.0D);
		    tessellator.draw();
		    tessellator.startDrawingQuads();
		    tessellator.setColorRGBA_F(255, 0, 0, 255);
		    tessellator.addVertex((-i - 1), (8 + height), 0.0D);
		    tessellator.addVertex((-i - 1), (9 + height), 0.0D);
		    tessellator.addVertex((i + 1), (9 + height), 0.0D);
		    tessellator.addVertex((i + 1), (8 + height), 0.0D);
		    tessellator.draw();


		    
		    GL11.glEnable(3553 /*GL_TEXTURE_2D*/);
		    GL11.glDepthMask(true);
		    p_189692_0_.drawString(name, -p_189692_0_.getStringWidth(name) / 2, height, textCol);
		    GL11.glEnable(2929 /*GL_DEPTH_TEST*/);
		    
		    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		    GL11.glPopMatrix();
		  }
}
