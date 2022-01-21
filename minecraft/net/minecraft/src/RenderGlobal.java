package net.minecraft.src;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.src.MEDMEX.Client;
import net.minecraft.src.MEDMEX.Modules.Render.NewChunks;
import net.minecraft.src.MEDMEX.Utils.RenderUtils;

import org.lwjgl.BufferUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.ARBOcclusionQuery;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public class RenderGlobal implements IWorldAccess {
	public static List<Integer> x= new ArrayList<>();
	public static List<Integer> z= new ArrayList<>();
	public static int newchunkx, newchunkz;
	public static int ncheight = 0;
    private long lastMovedTime = System.currentTimeMillis();
    public List tileEntities = new ArrayList();
    private World worldObj;
    private RenderEngine renderEngine;
    private List worldRenderersToUpdate = new ArrayList();
    private WorldRenderer[] sortedWorldRenderers;
    private WorldRenderer[] worldRenderers;
    private int renderChunksWide;
    private int renderChunksTall;
    private int renderChunksDeep;
    private int glRenderListBase;
    private Minecraft mc;
    private RenderBlocks globalRenderBlocks;
    private IntBuffer glOcclusionQueryBase;
    private boolean occlusionEnabled = false;
    private int cloudOffsetX = 0;
    private int starGLCallList;
    private int glSkyList;
    private int glSkyList2;
    private int minBlockX;
    private int minBlockY;
    private int minBlockZ;
    private int maxBlockX;
    private int maxBlockY;
    private int maxBlockZ;
    private int renderDistance = -1;
    private int renderEntitiesStartupCounter = 2;
    private int countEntitiesTotal;
    private int countEntitiesRendered;
    private int countEntitiesHidden;
    int[] dummyBuf50k = new int['\uc350'];
    IntBuffer occlusionResult = GLAllocation.createDirectIntBuffer(64);
    private int renderersLoaded;
    private int renderersBeingClipped;
    private int renderersBeingOccluded;
    private int renderersBeingRendered;
    private int renderersSkippingRenderPass;
    private int worldRenderersCheckIndex;
    private IntBuffer bed = BufferUtils.createIntBuffer(65536);
    int dummyInt0 = 0;
    int glDummyList = GLAllocation.generateDisplayLists(1);
    double prevSortX = -9999.0D;
    double prevSortY = -9999.0D;
    double prevSortZ = -9999.0D;
    public float damagePartialTime;
    int frustrumCheckOffset = 0;
    double prevReposX;
    double prevReposY;
    double prevReposZ;
    private float timePerUpdateMs = 10.0F;
    private long updateStartTimeNs = 0L;
    private boolean firstUpdate = true;
    String pogchamp = "thx historian for fix <3";

    public RenderGlobal(Minecraft minecraft, RenderEngine renderengine) {
        this.mc = minecraft;
        this.renderEngine = renderengine;
        byte byte0 = 64;
        this.glRenderListBase = GLAllocation.generateDisplayLists(byte0 * byte0 * byte0 * 3);
        this.occlusionEnabled = minecraft.getOpenGlCapsChecker().checkARBOcclusion();
        if (this.occlusionEnabled) {
            this.occlusionResult.clear();
            this.glOcclusionQueryBase = GLAllocation.createDirectIntBuffer(byte0 * byte0 * byte0);
            this.glOcclusionQueryBase.clear();
            this.glOcclusionQueryBase.position(0);
            this.glOcclusionQueryBase.limit(byte0 * byte0 * byte0);
            ARBOcclusionQuery.glGenQueriesARB(this.glOcclusionQueryBase);
        }

        this.starGLCallList = GLAllocation.generateDisplayLists(3);
        GL11.glPushMatrix();
        GL11.glNewList(this.starGLCallList, 4864 /*GL_COMPILE*/);
        this.renderStars();
        GL11.glEndList();
        GL11.glPopMatrix();
        Tessellator tessellator = Tessellator.instance;
        this.glSkyList = this.starGLCallList + 1;
        GL11.glNewList(this.glSkyList, 4864 /*GL_COMPILE*/);
        byte byte1 = 64;
        int i = 256 / byte1 + 2;
        float f = 16.0F;

        int k;
        int i1;
        for(k = -byte1 * i; k <= byte1 * i; k += byte1) {
            for(i1 = -byte1 * i; i1 <= byte1 * i; i1 += byte1) {
                tessellator.startDrawingQuads();
                tessellator.addVertex((double)(k + 0), (double)f, (double)(i1 + 0));
                tessellator.addVertex((double)(k + byte1), (double)f, (double)(i1 + 0));
                tessellator.addVertex((double)(k + byte1), (double)f, (double)(i1 + byte1));
                tessellator.addVertex((double)(k + 0), (double)f, (double)(i1 + byte1));
                tessellator.draw();
            }
        }

        GL11.glEndList();
        this.glSkyList2 = this.starGLCallList + 2;
        GL11.glNewList(this.glSkyList2, 4864 /*GL_COMPILE*/);
        f = -16.0F;
        tessellator.startDrawingQuads();

        for(k = -byte1 * i; k <= byte1 * i; k += byte1) {
            for(i1 = -byte1 * i; i1 <= byte1 * i; i1 += byte1) {
                tessellator.addVertex((double)(k + byte1), (double)f, (double)(i1 + 0));
                tessellator.addVertex((double)(k + 0), (double)f, (double)(i1 + 0));
                tessellator.addVertex((double)(k + 0), (double)f, (double)(i1 + byte1));
                tessellator.addVertex((double)(k + byte1), (double)f, (double)(i1 + byte1));
            }
        }

        tessellator.draw();
        GL11.glEndList();
    }

    private void renderStars() {
        Random random = new Random(10842L);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();

        for(int i = 0; i < 1500; ++i) {
            double d = (double)(random.nextFloat() * 2.0F - 1.0F);
            double d1 = (double)(random.nextFloat() * 2.0F - 1.0F);
            double d2 = (double)(random.nextFloat() * 2.0F - 1.0F);
            double d3 = (double)(0.25F + random.nextFloat() * 0.25F);
            double d4 = d * d + d1 * d1 + d2 * d2;
            if (d4 < 1.0D && d4 > 0.01D) {
                d4 = 1.0D / Math.sqrt(d4);
                d *= d4;
                d1 *= d4;
                d2 *= d4;
                double d5 = d * 100.0D;
                double d6 = d1 * 100.0D;
                double d7 = d2 * 100.0D;
                double d8 = Math.atan2(d, d2);
                double d9 = Math.sin(d8);
                double d10 = Math.cos(d8);
                double d11 = Math.atan2(Math.sqrt(d * d + d2 * d2), d1);
                double d12 = Math.sin(d11);
                double d13 = Math.cos(d11);
                double d14 = random.nextDouble() * 3.141592653589793D * 2.0D;
                double d15 = Math.sin(d14);
                double d16 = Math.cos(d14);

                for(int j = 0; j < 4; ++j) {
                    double d17 = 0.0D;
                    double d18 = (double)((j & 2) - 1) * d3;
                    double d19 = (double)((j + 1 & 2) - 1) * d3;
                    double d20 = d18 * d16 - d19 * d15;
                    double d21 = d19 * d16 + d18 * d15;
                    double d22 = d20 * d12 + d17 * d13;
                    double d23 = d17 * d12 - d20 * d13;
                    double d24 = d23 * d9 - d21 * d10;
                    double d25 = d21 * d9 + d23 * d10;
                    tessellator.addVertex(d5 + d24, d6 + d22, d7 + d25);
                }
            }
        }

        tessellator.draw();
    }

    public void changeWorld(World world) {
        if (this.worldObj != null) {
            this.worldObj.removeWorldAccess(this);
        }

        this.prevSortX = -9999.0D;
        this.prevSortY = -9999.0D;
        this.prevSortZ = -9999.0D;
        RenderManager.instance.func_852_a(world);
        this.worldObj = world;
        this.globalRenderBlocks = new RenderBlocks(world);
        if (world != null) {
            world.addWorldAccess(this);
            this.loadRenderers();
        }

    }

    public void loadRenderers() {
        UpdateThread updatethread = Config.getUpdateThread();
        if (updatethread != null) {
            updatethread.clearAllUpdates();
        }

        this.firstUpdate = true;
        Block.leaves.setGraphicsLevel(Config.isTreesFancy());
        this.renderDistance = this.mc.gameSettings.renderDistance;
        int j;
        if (this.worldRenderers != null) {
            for(j = 0; j < this.worldRenderers.length; ++j) {
                this.worldRenderers[j].func_1204_c();
            }
        }

        j = 64 << 3 - this.renderDistance;
        if (Config.isLoadChunksFar()) {
            j = 512;
        }

        if (Config.isFarView()) {
            if (j < 512) {
                j *= 3;
            } else {
                j *= 2;
            }
        }

        j += Config.getPreloadedChunks() * 2 * 16;
        if (!Config.isFarView() && j > 400) {
            j = 400;
        }

        this.prevReposX = -9999.0D;
        this.prevReposY = -9999.0D;
        this.prevReposZ = -9999.0D;
        this.renderChunksWide = j / 16 + 1;
        this.renderChunksTall = 8;
        this.renderChunksDeep = j / 16 + 1;
        this.worldRenderers = new WorldRenderer[this.renderChunksWide * this.renderChunksTall * this.renderChunksDeep];
        this.sortedWorldRenderers = new WorldRenderer[this.renderChunksWide * this.renderChunksTall * this.renderChunksDeep];
        int k = 0;
        int l = 0;
        this.minBlockX = 0;
        this.minBlockY = 0;
        this.minBlockZ = 0;
        this.maxBlockX = this.renderChunksWide;
        this.maxBlockY = this.renderChunksTall;
        this.maxBlockZ = this.renderChunksDeep;

        int j1;
        for(j1 = 0; j1 < this.worldRenderersToUpdate.size(); ++j1) {
            WorldRenderer worldrenderer = (WorldRenderer)this.worldRenderersToUpdate.get(j1);
            if (worldrenderer != null) {
                worldrenderer.needsUpdate = false;
            }
        }

        this.worldRenderersToUpdate.clear();
        this.tileEntities.clear();

        for(j1 = 0; j1 < this.renderChunksWide; ++j1) {
            for(int k1 = 0; k1 < this.renderChunksTall; ++k1) {
                for(int l1 = 0; l1 < this.renderChunksDeep; ++l1) {
                    int i2 = (l1 * this.renderChunksTall + k1) * this.renderChunksWide + j1;
                    this.worldRenderers[i2] = new WorldRenderer(this.worldObj, this.tileEntities, j1 * 16, k1 * 16, l1 * 16, 16, this.glRenderListBase + k);
                    if (this.occlusionEnabled) {
                        this.worldRenderers[i2].glOcclusionQuery = this.glOcclusionQueryBase.get(l);
                    }

                    this.worldRenderers[i2].isWaitingOnOcclusionQuery = false;
                    this.worldRenderers[i2].isVisible = true;
                    this.worldRenderers[i2].isInFrustum = false;
                    this.worldRenderers[i2].chunkIndex = l++;
                    this.worldRenderers[i2].markDirty();
                    this.sortedWorldRenderers[i2] = this.worldRenderers[i2];
                    this.worldRenderersToUpdate.add(this.worldRenderers[i2]);
                    k += 3;
                }
            }
        }

        if (this.worldObj != null) {
            Object obj = this.mc.renderViewEntity;
            if (obj == null) {
                obj = this.mc.thePlayer;
            }

            if (obj != null) {
                this.markRenderersForNewPosition(MathHelper.floor_double(((Entity)((Entity)obj)).posX), MathHelper.floor_double(((Entity)((Entity)obj)).posY), MathHelper.floor_double(((Entity)((Entity)obj)).posZ));
                Arrays.sort(this.sortedWorldRenderers, new EntitySorter((Entity)((Entity)obj)));
            }
        }

        this.renderEntitiesStartupCounter = 2;
    }
    
    public static void drawNewChunks(boolean flag) {
    	if(x.size() >= 500) {
    		x.remove(0);
    	}
    	if(z.size() >= 500) {
    		z.remove(0);
    	}
    	
    	for(int i = 0; i < x.size(); i++) {
    	double renderX = x.get(i) - RenderManager.renderPosX;
    	Double RenderY = Client.settingsmanager.getSettingByName("Newchunks Height").getValDouble() - RenderManager.renderPosY;
    	double renderZ = z.get(i) - RenderManager.renderPosZ;
    	RenderUtils.drawOutlinedEntityESP(renderX, RenderY, renderZ, 8, 0, 255, 0, 0, 255);
    	}
    }

    public void renderEntities(Vec3D vec3d, ICamera icamera, float f) {
    	if(NewChunks.instance.isEnabled()) {
    		drawNewChunks(true);
    	}
    	Client.onRenderEntities();
    	
        if (this.renderEntitiesStartupCounter > 0) {
            --this.renderEntitiesStartupCounter;
        } else {
            TileEntityRenderer.instance.cacheActiveRenderInfo(this.worldObj, this.renderEngine, this.mc.fontRenderer, this.mc.renderViewEntity, f);
            RenderManager.instance.cacheActiveRenderInfo(this.worldObj, this.renderEngine, this.mc.fontRenderer, this.mc.renderViewEntity, this.mc.gameSettings, f);
            this.countEntitiesTotal = 0;
            this.countEntitiesRendered = 0;
            this.countEntitiesHidden = 0;
            EntityLiving entityliving = this.mc.renderViewEntity;
            RenderManager.renderPosX = entityliving.lastTickPosX + (entityliving.posX - entityliving.lastTickPosX) * (double)f;
            RenderManager.renderPosY = entityliving.lastTickPosY + (entityliving.posY - entityliving.lastTickPosY) * (double)f;
            RenderManager.renderPosZ = entityliving.lastTickPosZ + (entityliving.posZ - entityliving.lastTickPosZ) * (double)f;
            TileEntityRenderer.staticPlayerX = entityliving.lastTickPosX + (entityliving.posX - entityliving.lastTickPosX) * (double)f;
            TileEntityRenderer.staticPlayerY = entityliving.lastTickPosY + (entityliving.posY - entityliving.lastTickPosY) * (double)f;
            TileEntityRenderer.staticPlayerZ = entityliving.lastTickPosZ + (entityliving.posZ - entityliving.lastTickPosZ) * (double)f;
            List list = this.worldObj.getLoadedEntityList();
            this.countEntitiesTotal = list.size();

            int k;
            Entity entity1;
            for(k = 0; k < this.worldObj.weatherEffects.size(); ++k) {
                entity1 = (Entity)this.worldObj.weatherEffects.get(k);
                ++this.countEntitiesRendered;
                if (entity1.isInRangeToRenderVec3D(vec3d)) {
                    RenderManager.instance.renderEntity(entity1, f);
                }
            }

            for(k = 0; k < list.size(); ++k) {
                entity1 = (Entity)list.get(k);
                if (entity1.isInRangeToRenderVec3D(vec3d) && (entity1.ignoreFrustumCheck || icamera.isBoundingBoxInFrustum(entity1.boundingBox)) && (entity1 != this.mc.renderViewEntity || this.mc.gameSettings.thirdPersonView || this.mc.renderViewEntity.isPlayerSleeping())) {
                    int l = MathHelper.floor_double(entity1.posY);
                    if (l < 0) {
                        l = 0;
                    }

                    if (l >= 128) {
                        l = 127;
                    }

                    if (this.worldObj.blockExists(MathHelper.floor_double(entity1.posX), l, MathHelper.floor_double(entity1.posZ))) {
                        ++this.countEntitiesRendered;
                        RenderManager.instance.renderEntity(entity1, f);
                    }
                }
            }

            for(k = 0; k < this.tileEntities.size(); ++k) {
                TileEntityRenderer.instance.renderTileEntity((TileEntity)this.tileEntities.get(k), f);
            }

        }
    }

    public String getDebugInfoRenders() {
        return "C: " + this.renderersBeingRendered + "/" + this.renderersLoaded + ". F: " + this.renderersBeingClipped + ", O: " + this.renderersBeingOccluded + ", E: " + this.renderersSkippingRenderPass;
    }

    public String getDebugInfoEntities() {
        return "E: " + this.countEntitiesRendered + "/" + this.countEntitiesTotal + ". B: " + this.countEntitiesHidden + ", I: " + (this.countEntitiesTotal - this.countEntitiesHidden - this.countEntitiesRendered);
    }

    private void markRenderersForNewPosition(int i, int j, int k) {
        i -= 8;
        j -= 8;
        k -= 8;
        this.minBlockX = Integer.MAX_VALUE;
        this.minBlockY = Integer.MAX_VALUE;
        this.minBlockZ = Integer.MAX_VALUE;
        this.maxBlockX = Integer.MIN_VALUE;
        this.maxBlockY = Integer.MIN_VALUE;
        this.maxBlockZ = Integer.MIN_VALUE;
        int l = this.renderChunksWide * 16;
        int i1 = l / 2;

        for(int j1 = 0; j1 < this.renderChunksWide; ++j1) {
            int k1 = j1 * 16;
            int l1 = k1 + i1 - i;
            if (l1 < 0) {
                l1 -= l - 1;
            }

            l1 /= l;
            k1 -= l1 * l;
            if (k1 < this.minBlockX) {
                this.minBlockX = k1;
            }

            if (k1 > this.maxBlockX) {
                this.maxBlockX = k1;
            }

            for(int i2 = 0; i2 < this.renderChunksDeep; ++i2) {
                int j2 = i2 * 16;
                int k2 = j2 + i1 - k;
                if (k2 < 0) {
                    k2 -= l - 1;
                }

                k2 /= l;
                j2 -= k2 * l;
                if (j2 < this.minBlockZ) {
                    this.minBlockZ = j2;
                }

                if (j2 > this.maxBlockZ) {
                    this.maxBlockZ = j2;
                }

                for(int l2 = 0; l2 < this.renderChunksTall; ++l2) {
                    int i3 = l2 * 16;
                    if (i3 < this.minBlockY) {
                        this.minBlockY = i3;
                    }

                    if (i3 > this.maxBlockY) {
                        this.maxBlockY = i3;
                    }

                    WorldRenderer worldrenderer = this.worldRenderers[(i2 * this.renderChunksTall + l2) * this.renderChunksWide + j1];
                    boolean flag = worldrenderer.needsUpdate;
                    worldrenderer.setPosition(k1, i3, j2);
                    if (!flag && worldrenderer.needsUpdate) {
                        this.worldRenderersToUpdate.add(worldrenderer);
                    }
                }
            }
        }

    }

    public int sortAndRender(EntityLiving entityliving, int i, double d) {
        if (this.worldRenderersToUpdate.size() < 10) {
            byte byte0 = 10;

            for(int j = 0; j < byte0; ++j) {
                this.worldRenderersCheckIndex = (this.worldRenderersCheckIndex + 1) % this.worldRenderers.length;
                WorldRenderer worldrenderer = this.worldRenderers[this.worldRenderersCheckIndex];
                if (worldrenderer.needsUpdate && !this.worldRenderersToUpdate.contains(worldrenderer)) {
                    this.worldRenderersToUpdate.add(worldrenderer);
                }
            }
        }

        if (this.mc.gameSettings.renderDistance != this.renderDistance && !Config.isLoadChunksFar()) {
            this.loadRenderers();
        }

        if (i == 0) {
            this.renderersLoaded = 0;
            this.renderersBeingClipped = 0;
            this.renderersBeingOccluded = 0;
            this.renderersBeingRendered = 0;
            this.renderersSkippingRenderPass = 0;
        }

        double d1 = entityliving.lastTickPosX + (entityliving.posX - entityliving.lastTickPosX) * d;
        double d2 = entityliving.lastTickPosY + (entityliving.posY - entityliving.lastTickPosY) * d;
        double d3 = entityliving.lastTickPosZ + (entityliving.posZ - entityliving.lastTickPosZ) * d;
        double d4 = entityliving.posX - this.prevSortX;
        double d5 = entityliving.posY - this.prevSortY;
        double d6 = entityliving.posZ - this.prevSortZ;
        double d7 = d4 * d4 + d5 * d5 + d6 * d6;
        int s;
        if (d7 > 16.0D) {
            this.prevSortX = entityliving.posX;
            this.prevSortY = entityliving.posY;
            this.prevSortZ = entityliving.posZ;
            s = Config.getPreloadedChunks() * 16;
            double d8 = entityliving.posX - this.prevReposX;
            double d9 = entityliving.posY - this.prevReposY;
            double d10 = entityliving.posZ - this.prevReposZ;
            double d11 = d8 * d8 + d9 * d9 + d10 * d10;
            if (d11 > (double)(s * s) + 16.0D) {
                this.prevReposX = entityliving.posX;
                this.prevReposY = entityliving.posY;
                this.prevReposZ = entityliving.posZ;
                this.markRenderersForNewPosition(MathHelper.floor_double(entityliving.posX), MathHelper.floor_double(entityliving.posY), MathHelper.floor_double(entityliving.posZ));
            }

            Arrays.sort(this.sortedWorldRenderers, new EntitySorter(entityliving));
        }

        s = (int)entityliving.posX;
        int e = (int)entityliving.posZ;
        char x = 2000;
        if (Math.abs(s - Tessellator.chunkOffsetX) > x || Math.abs(e - Tessellator.chunkOffsetZ) > x) {
            Tessellator.chunkOffsetX = s;
            Tessellator.chunkOffsetZ = e;
            this.loadRenderers();
        }

        int l = 0;
        UpdateThread updatethread = Config.getUpdateThread();
        if (updatethread != null) {
            if (this.updateStartTimeNs == 0L) {
                this.updateStartTimeNs = System.nanoTime();
            }

            if (updatethread.hasWorkToDo()) {
                l = Config.getUpdatesPerFrame();
                if (Config.isDynamicUpdates() && !this.isMoving(entityliving)) {
                    l *= 3;
                }

                l = Math.min(l, updatethread.getPendingUpdatesCount());
                if (l > 0) {
                    updatethread.unpause();
                }
            }
        }

        if (this.mc.gameSettings.ofSmoothFps && i == 0) {
            GL11.glFinish();
        }

        if (this.mc.gameSettings.ofSmoothInput && i == 0) {
            Config.sleep(1L);
        }

        int i1 = 0;
        int j1 = 0;
        int j2;
        int i3;
        int i11 = 0;
        if (this.occlusionEnabled && this.mc.gameSettings.advancedOpengl && !this.mc.gameSettings.anaglyph && i == 0) {
            int k1 = 0;
            byte byte1 = 20;
            this.checkOcclusionQueryResult(k1, byte1, entityliving.posX, entityliving.posY, entityliving.posZ);

            for(j2 = k1; j2 < byte1; ++j2) {
                this.sortedWorldRenderers[j2].isVisible = true;
            }

            i11 = i11 + this.renderSortedRenderers(k1, byte1, i, d);
            j2 = byte1;
            int l2 = 0;
            byte byte2 = 30;

            byte byte3;
            for(i3 = this.renderChunksWide / 2; j2 < this.sortedWorldRenderers.length; i11 += this.renderSortedRenderers(byte3, j2, i, d)) {
                byte3 = (byte)j2;
                if (l2 < i3) {
                    ++l2;
                } else {
                    --l2;
                }

                j2 = byte3 + l2 * byte2;
                if (j2 <= byte3) {
                    j2 = byte3 + 10;
                }

                if (j2 > this.sortedWorldRenderers.length) {
                    j2 = this.sortedWorldRenderers.length;
                }

                GL11.glDisable(3553 /*GL_TEXTURE_2D*/);
                GL11.glDisable(2896 /*GL_LIGHTING*/);
                GL11.glDisable(3008 /*GL_ALPHA_TEST*/);
                GL11.glDisable(2912 /*GL_FOG*/);
                GL11.glColorMask(false, false, false, false);
                GL11.glDepthMask(false);
                this.checkOcclusionQueryResult(byte3, j2, entityliving.posX, entityliving.posY, entityliving.posZ);
                GL11.glPushMatrix();
                float f4 = 0.0F;
                float f5 = 0.0F;
                float f6 = 0.0F;

                for(int k3 = byte3; k3 < j2; ++k3) {
                    WorldRenderer worldrenderer1 = this.sortedWorldRenderers[k3];
                    if (worldrenderer1.skipAllRenderPasses()) {
                        worldrenderer1.isInFrustum = false;
                    } else if (worldrenderer1.isInFrustum) {
                        if (worldrenderer1.isUpdating) {
                            worldrenderer1.isVisible = true;
                        } else if (Config.isOcclusionFancy() && !worldrenderer1.isInFrustrumFully) {
                            worldrenderer1.isVisible = true;
                        } else if (worldrenderer1.isInFrustum && !worldrenderer1.isWaitingOnOcclusionQuery) {
                            float f8;
                            float f10;
                            float f12;
                            float f14;
                            if (worldrenderer1.isVisibleFromPosition) {
                                f8 = Math.abs((float)(worldrenderer1.visibleFromX - entityliving.posX));
                                f10 = Math.abs((float)(worldrenderer1.visibleFromY - entityliving.posY));
                                f12 = Math.abs((float)(worldrenderer1.visibleFromZ - entityliving.posZ));
                                f14 = f8 + f10 + f12;
                                if ((double)f14 < 10.0D + (double)k3 / 1000.0D) {
                                    worldrenderer1.isVisible = true;
                                    continue;
                                }

                                worldrenderer1.isVisibleFromPosition = false;
                            }

                            f8 = (float)((double)worldrenderer1.posXMinus - d1);
                            f10 = (float)((double)worldrenderer1.posYMinus - d2);
                            f12 = (float)((double)worldrenderer1.posZMinus - d3);
                            f14 = f8 - f4;
                            float f15 = f10 - f5;
                            float f16 = f12 - f6;
                            if (f14 != 0.0F || f15 != 0.0F || f16 != 0.0F) {
                                GL11.glTranslatef(f14, f15, f16);
                                f4 += f14;
                                f5 += f15;
                                f6 += f16;
                            }

                            ARBOcclusionQuery.glBeginQueryARB(35092 /*GL_SAMPLES_PASSED_ARB*/, worldrenderer1.glOcclusionQuery);
                            worldrenderer1.callOcclusionQueryList();
                            ARBOcclusionQuery.glEndQueryARB(35092 /*GL_SAMPLES_PASSED_ARB*/);
                            worldrenderer1.isWaitingOnOcclusionQuery = true;
                            ++j1;
                        }
                    }
                }

                GL11.glPopMatrix();
                GL11.glColorMask(true, true, true, true);
                GL11.glDepthMask(true);
                GL11.glEnable(3553 /*GL_TEXTURE_2D*/);
                GL11.glEnable(3008 /*GL_ALPHA_TEST*/);
                GL11.glEnable(2912 /*GL_FOG*/);
            }
        } else {
            i11 = i11 + this.renderSortedRenderers(0, this.sortedWorldRenderers.length, i, d);
        }

        if (updatethread != null) {
            float f = 0.0F;
            if (l > 0) {
                long l1 = System.nanoTime() - this.updateStartTimeNs;
                float f3 = this.timePerUpdateMs * (1.0F + (float)(l - 1) / 2.0F);
                float f1 = f3 - (float)l1 / 1000000.0F;
                if (f1 > 0.0F) {
                    i3 = (int)f1;
                    Config.sleep((long)i3);
                }
            }

            updatethread.pause();
            float f2 = 0.2F;
            if (l > 0) {
                j2 = updatethread.resetUpdateCount();
                if (j2 < l) {
                    this.timePerUpdateMs += f2;
                }

                if (j2 > l) {
                    this.timePerUpdateMs -= f2;
                }

                if (j2 == l) {
                    this.timePerUpdateMs -= f2;
                }
            } else {
                this.timePerUpdateMs -= f2 / 5.0F;
            }

            if (this.timePerUpdateMs < 0.0F) {
                this.timePerUpdateMs = 0.0F;
            }

            this.updateStartTimeNs = System.nanoTime();
        }

        return i11;
    }

    private void checkOcclusionQueryResult(int i, int j, double d, double d1, double d2) {
        for(int k = i; k < j; ++k) {
            WorldRenderer worldrenderer = this.sortedWorldRenderers[k];
            if (worldrenderer.isWaitingOnOcclusionQuery) {
                this.occlusionResult.clear();
                ARBOcclusionQuery.glGetQueryObjectuARB(worldrenderer.glOcclusionQuery, 34919 /*GL_QUERY_RESULT_AVAILABLE_ARB*/, this.occlusionResult);
                if (this.occlusionResult.get(0) != 0) {
                    worldrenderer.isWaitingOnOcclusionQuery = false;
                    this.occlusionResult.clear();
                    ARBOcclusionQuery.glGetQueryObjectuARB(worldrenderer.glOcclusionQuery, 34918 /*GL_QUERY_RESULT_ARB*/, this.occlusionResult);
                    boolean flag = worldrenderer.isVisible;
                    worldrenderer.isVisible = this.occlusionResult.get(0) > 0;
                    if (flag && worldrenderer.isVisible) {
                        worldrenderer.isVisibleFromPosition = true;
                        worldrenderer.visibleFromX = d;
                        worldrenderer.visibleFromY = d1;
                        worldrenderer.visibleFromZ = d2;
                    }
                }
            }
        }

    }

    private int renderSortedRenderers(int i, int j, int k, double d) {
        this.bed.clear();
        int l = 0;

        for(int i1 = i; i1 < j; ++i1) {
            if (k == 0) {
                ++this.renderersLoaded;
                if (this.sortedWorldRenderers[i1].skipRenderPass[k]) {
                    ++this.renderersSkippingRenderPass;
                } else if (!this.sortedWorldRenderers[i1].isInFrustum) {
                    ++this.renderersBeingClipped;
                } else if (this.occlusionEnabled && !this.sortedWorldRenderers[i1].isVisible) {
                    ++this.renderersBeingOccluded;
                } else {
                    ++this.renderersBeingRendered;
                }
            }

            if (!this.sortedWorldRenderers[i1].skipRenderPass[k] && this.sortedWorldRenderers[i1].isInFrustum && (!this.occlusionEnabled || this.sortedWorldRenderers[i1].isVisible)) {
                int j1 = this.sortedWorldRenderers[i1].getGLCallListForPass(k);
                if (j1 >= 0) {
                    this.bed.put(j1);
                    ++l;
                }
            }
        }

        this.bed.flip();
        EntityLiving entityliving = this.mc.renderViewEntity;
        double d1 = entityliving.lastTickPosX + (entityliving.posX - entityliving.lastTickPosX) * d - (double)Tessellator.chunkOffsetX;
        double d2 = entityliving.lastTickPosY + (entityliving.posY - entityliving.lastTickPosY) * d;
        double d3 = entityliving.lastTickPosZ + (entityliving.posZ - entityliving.lastTickPosZ) * d - (double)Tessellator.chunkOffsetZ;
        GL11.glTranslatef((float)(-d1), (float)(-d2), (float)(-d3));
        GL11.glCallLists(this.bed);
        GL11.glTranslatef((float)d1, (float)d2, (float)d3);
        return l;
    }

    public void renderAllRenderLists(int i, double d) {
    }

    public void updateClouds() {
        ++this.cloudOffsetX;
    }

    public void renderSky(float f) {
        if (!this.mc.theWorld.worldProvider.isNether) {
            GL11.glDisable(3553 /*GL_TEXTURE_2D*/);
            Vec3D vec3d = this.worldObj.getSkyColor(this.mc.renderViewEntity, f);
            float f1 = (float)vec3d.xCoord;
            float f2 = (float)vec3d.yCoord;
            float f3 = (float)vec3d.zCoord;
            float f8;
            if (this.mc.gameSettings.anaglyph) {
                float f4 = (f1 * 30.0F + f2 * 59.0F + f3 * 11.0F) / 100.0F;
                float f5 = (f1 * 30.0F + f2 * 70.0F) / 100.0F;
                f8 = (f1 * 30.0F + f3 * 70.0F) / 100.0F;
                f1 = f4;
                f2 = f5;
                f3 = f8;
            }

            GL11.glColor3f(f1, f2, f3);
            Tessellator tessellator = Tessellator.instance;
            GL11.glDepthMask(false);
            GL11.glEnable(2912 /*GL_FOG*/);
            GL11.glColor3f(f1, f2, f3);
            if (Config.isSkyEnabled()) {
                GL11.glCallList(this.glSkyList);
            }

            GL11.glDisable(2912 /*GL_FOG*/);
            GL11.glDisable(3008 /*GL_ALPHA_TEST*/);
            GL11.glEnable(3042 /*GL_BLEND*/);
            GL11.glBlendFunc(770, 771);
            RenderHelper.disableStandardItemLighting();
            float[] af = this.worldObj.worldProvider.calcSunriseSunsetColors(this.worldObj.getCelestialAngle(f), f);
            float f9;
            float f11;
            float f13;
            float f15;
            float f18;
            if (af != null && Config.isSkyEnabled()) {
                GL11.glDisable(3553 /*GL_TEXTURE_2D*/);
                GL11.glShadeModel(7425 /*GL_SMOOTH*/);
                GL11.glPushMatrix();
                GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
                f8 = this.worldObj.getCelestialAngle(f);
                GL11.glRotatef(f8 > 0.5F ? 180.0F : 0.0F, 0.0F, 0.0F, 1.0F);
                f9 = af[0];
                f11 = af[1];
                f13 = af[2];
                float f20;
                if (this.mc.gameSettings.anaglyph) {
                    f15 = (f9 * 30.0F + f11 * 59.0F + f13 * 11.0F) / 100.0F;
                    f18 = (f9 * 30.0F + f11 * 70.0F) / 100.0F;
                    f20 = (f9 * 30.0F + f13 * 70.0F) / 100.0F;
                    f9 = f15;
                    f11 = f18;
                    f13 = f20;
                }

                tessellator.startDrawing(6);
                tessellator.setColorRGBA_F(f9, f11, f13, af[3]);
                tessellator.addVertex(0.0D, 100.0D, 0.0D);
                int i = 16;
                tessellator.setColorRGBA_F(af[0], af[1], af[2], 0.0F);

                for(int j = 0; j <= i; ++j) {
                    f20 = (float)j * 3.141593F * 2.0F / (float)i;
                    float f21 = MathHelper.sin(f20);
                    float f22 = MathHelper.cos(f20);
                    tessellator.addVertex((double)(f21 * 120.0F), (double)(f22 * 120.0F), (double)(-f22 * 40.0F * af[3]));
                }

                tessellator.draw();
                GL11.glPopMatrix();
                GL11.glShadeModel(7424 /*GL_FLAT*/);
            }

            GL11.glEnable(3553 /*GL_TEXTURE_2D*/);
            GL11.glBlendFunc(770, 1);
            GL11.glPushMatrix();
            f8 = 1.0F - this.worldObj.getRainStrength(f);
            f9 = 0.0F;
            f11 = 0.0F;
            f13 = 0.0F;
            GL11.glColor4f(1.0F, 1.0F, 1.0F, f8);
            GL11.glTranslatef(f9, f11, f13);
            GL11.glRotatef(0.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(this.worldObj.getCelestialAngle(f) * 360.0F, 1.0F, 0.0F, 0.0F);
            f15 = 30.0F;
            GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, this.renderEngine.getTexture("/terrain/sun.png"));
            tessellator.startDrawingQuads();
            tessellator.addVertexWithUV((double)(-f15), 100.0D, (double)(-f15), 0.0D, 0.0D);
            tessellator.addVertexWithUV((double)f15, 100.0D, (double)(-f15), 1.0D, 0.0D);
            tessellator.addVertexWithUV((double)f15, 100.0D, (double)f15, 1.0D, 1.0D);
            tessellator.addVertexWithUV((double)(-f15), 100.0D, (double)f15, 0.0D, 1.0D);
            tessellator.draw();
            f15 = 20.0F;
            GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, this.renderEngine.getTexture("/terrain/moon.png"));
            tessellator.startDrawingQuads();
            tessellator.addVertexWithUV((double)(-f15), -100.0D, (double)f15, 1.0D, 1.0D);
            tessellator.addVertexWithUV((double)f15, -100.0D, (double)f15, 0.0D, 1.0D);
            tessellator.addVertexWithUV((double)f15, -100.0D, (double)(-f15), 0.0D, 0.0D);
            tessellator.addVertexWithUV((double)(-f15), -100.0D, (double)(-f15), 1.0D, 0.0D);
            tessellator.draw();
            GL11.glDisable(3553 /*GL_TEXTURE_2D*/);
            f18 = this.worldObj.getStarBrightness(f) * f8;
            if (f18 > 0.0F) {
                GL11.glColor4f(f18, f18, f18, f18);
                if (Config.isStarsEnabled()) {
                    GL11.glCallList(this.starGLCallList);
                }
            }

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glDisable(3042 /*GL_BLEND*/);
            GL11.glEnable(3008 /*GL_ALPHA_TEST*/);
            GL11.glEnable(2912 /*GL_FOG*/);
            GL11.glPopMatrix();
            if (this.worldObj.worldProvider.func_28112_c()) {
                GL11.glColor3f(f1 * 0.2F + 0.04F, f2 * 0.2F + 0.04F, f3 * 0.6F + 0.1F);
            } else {
                GL11.glColor3f(f1, f2, f3);
            }

            GL11.glDisable(3553 /*GL_TEXTURE_2D*/);
            if (Config.isSkyEnabled()) {
                GL11.glCallList(this.glSkyList2);
            }

            GL11.glEnable(3553 /*GL_TEXTURE_2D*/);
            GL11.glDepthMask(true);
        }
    }

    public void renderClouds(float f) {
        if (!this.mc.theWorld.worldProvider.isNether) {
            if (this.mc.gameSettings.ofClouds != 3) {
                if (Config.isCloudsFancy()) {
                    this.renderCloudsFancy(f);
                } else {
                    GL11.glDisable(2884 /*GL_CULL_FACE*/);
                    float f1 = (float)(this.mc.renderViewEntity.lastTickPosY + (this.mc.renderViewEntity.posY - this.mc.renderViewEntity.lastTickPosY) * (double)f);
                    byte byte0 = 32;
                    int i = 256 / byte0;
                    Tessellator tessellator = Tessellator.instance;
                    GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, this.renderEngine.getTexture("/environment/clouds.png"));
                    GL11.glEnable(3042 /*GL_BLEND*/);
                    GL11.glBlendFunc(770, 771);
                    Vec3D vec3d = this.worldObj.drawClouds(f);
                    float f2 = (float)vec3d.xCoord;
                    float f3 = (float)vec3d.yCoord;
                    float f4 = (float)vec3d.zCoord;
                    float f6;
                    if (this.mc.gameSettings.anaglyph) {
                        f6 = (f2 * 30.0F + f3 * 59.0F + f4 * 11.0F) / 100.0F;
                        float f7 = (f2 * 30.0F + f3 * 70.0F) / 100.0F;
                        float f8 = (f2 * 30.0F + f4 * 70.0F) / 100.0F;
                        f2 = f6;
                        f3 = f7;
                        f4 = f8;
                    }

                    f6 = 4.882813E-4F;
                    double d = this.mc.renderViewEntity.prevPosX + (this.mc.renderViewEntity.posX - this.mc.renderViewEntity.prevPosX) * (double)f + (double)(((float)this.cloudOffsetX + f) * 0.03F);
                    double d1 = this.mc.renderViewEntity.prevPosZ + (this.mc.renderViewEntity.posZ - this.mc.renderViewEntity.prevPosZ) * (double)f;
                    int j = MathHelper.floor_double(d / 2048.0D);
                    int k = MathHelper.floor_double(d1 / 2048.0D);
                    d -= (double)(j * 2048 /*GL_EXP*/);
                    d1 -= (double)(k * 2048 /*GL_EXP*/);
                    float f9 = this.worldObj.worldProvider.getCloudHeight() - f1 + 0.33F;
                    f9 += this.mc.gameSettings.ofCloudsHeight * 25.0F;
                    float f10 = (float)(d * (double)f6);
                    float f11 = (float)(d1 * (double)f6);
                    tessellator.startDrawingQuads();
                    tessellator.setColorRGBA_F(f2, f3, f4, 0.8F);

                    for(int l = -byte0 * i; l < byte0 * i; l += byte0) {
                        for(int i1 = -byte0 * i; i1 < byte0 * i; i1 += byte0) {
                            tessellator.addVertexWithUV((double)(l + 0), (double)f9, (double)(i1 + byte0), (double)((float)(l + 0) * f6 + f10), (double)((float)(i1 + byte0) * f6 + f11));
                            tessellator.addVertexWithUV((double)(l + byte0), (double)f9, (double)(i1 + byte0), (double)((float)(l + byte0) * f6 + f10), (double)((float)(i1 + byte0) * f6 + f11));
                            tessellator.addVertexWithUV((double)(l + byte0), (double)f9, (double)(i1 + 0), (double)((float)(l + byte0) * f6 + f10), (double)((float)(i1 + 0) * f6 + f11));
                            tessellator.addVertexWithUV((double)(l + 0), (double)f9, (double)(i1 + 0), (double)((float)(l + 0) * f6 + f10), (double)((float)(i1 + 0) * f6 + f11));
                        }
                    }

                    tessellator.draw();
                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                    GL11.glDisable(3042 /*GL_BLEND*/);
                    GL11.glEnable(2884 /*GL_CULL_FACE*/);
                }
            }
        }
    }

    public boolean hasCloudFog(double d, double d1, double d2, float f) {
        return false;
    }

    public void renderCloudsFancy(float f) {
        GL11.glDisable(2884 /*GL_CULL_FACE*/);
        float f1 = (float)(this.mc.renderViewEntity.lastTickPosY + (this.mc.renderViewEntity.posY - this.mc.renderViewEntity.lastTickPosY) * (double)f);
        Tessellator tessellator = Tessellator.instance;
        float f2 = 12.0F;
        float f3 = 4.0F;
        double d = (this.mc.renderViewEntity.prevPosX + (this.mc.renderViewEntity.posX - this.mc.renderViewEntity.prevPosX) * (double)f + (double)(((float)this.cloudOffsetX + f) * 0.03F)) / (double)f2;
        double d1 = (this.mc.renderViewEntity.prevPosZ + (this.mc.renderViewEntity.posZ - this.mc.renderViewEntity.prevPosZ) * (double)f) / (double)f2 + 0.33000001311302185D;
        float f4 = this.worldObj.worldProvider.getCloudHeight() - f1 + 0.33F;
        f4 += this.mc.gameSettings.ofCloudsHeight * 25.0F;
        int i = MathHelper.floor_double(d / 2048.0D);
        int j = MathHelper.floor_double(d1 / 2048.0D);
        d -= (double)(i * 2048 /*GL_EXP*/);
        d1 -= (double)(j * 2048 /*GL_EXP*/);
        GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, this.renderEngine.getTexture("/environment/clouds.png"));
        GL11.glEnable(3042 /*GL_BLEND*/);
        GL11.glBlendFunc(770, 771);
        Vec3D vec3d = this.worldObj.drawClouds(f);
        float f5 = (float)vec3d.xCoord;
        float f6 = (float)vec3d.yCoord;
        float f7 = (float)vec3d.zCoord;
        float f9;
        float f11;
        float f13;
        if (this.mc.gameSettings.anaglyph) {
            f9 = (f5 * 30.0F + f6 * 59.0F + f7 * 11.0F) / 100.0F;
            f11 = (f5 * 30.0F + f6 * 70.0F) / 100.0F;
            f13 = (f5 * 30.0F + f7 * 70.0F) / 100.0F;
            f5 = f9;
            f6 = f11;
            f7 = f13;
        }

        f9 = (float)(d * 0.0D);
        f11 = (float)(d1 * 0.0D);
        f13 = 0.00390625F;
        f9 = (float)MathHelper.floor_double(d) * f13;
        f11 = (float)MathHelper.floor_double(d1) * f13;
        float f14 = (float)(d - (double)MathHelper.floor_double(d));
        float f15 = (float)(d1 - (double)MathHelper.floor_double(d1));
        int k = 8;
        byte byte0 = 3;
        float f16 = 9.765625E-4F;
        GL11.glScalef(f2, 1.0F, f2);

        for(int l = 0; l < 2; ++l) {
            if (l == 0) {
                GL11.glColorMask(false, false, false, false);
            } else if (this.mc.gameSettings.anaglyph) {
                if (EntityRenderer.anaglyphField == 0) {
                    GL11.glColorMask(false, true, true, true);
                } else {
                    GL11.glColorMask(true, false, false, true);
                }
            } else {
                GL11.glColorMask(true, true, true, true);
            }

            double d2 = 0.02D;

            for(int i1 = -byte0 + 1; i1 <= byte0; ++i1) {
                for(int j1 = -byte0 + 1; j1 <= byte0; ++j1) {
                    tessellator.startDrawingQuads();
                    float f17 = (float)(i1 * k);
                    float f18 = (float)(j1 * k);
                    float f19 = f17 - f14;
                    float f20 = f18 - f15;
                    tessellator.setColorRGBA_F(f5 * 0.9F, f6 * 0.9F, f7 * 0.9F, 0.8F);
                    int j2;
                    if (i1 > -1) {
                        tessellator.setNormal(-1.0F, 0.0F, 0.0F);

                        for(j2 = 0; j2 < k; ++j2) {
                            tessellator.addVertexWithUV((double)(f19 + (float)j2 + 0.0F), (double)(f4 + 0.0F) + d2, (double)(f20 + (float)k), (double)((f17 + (float)j2 + 0.5F) * f13 + f9), (double)((f18 + (float)k) * f13 + f11));
                            tessellator.addVertexWithUV((double)(f19 + (float)j2 + 0.0F), (double)(f4 + f3) - d2, (double)(f20 + (float)k), (double)((f17 + (float)j2 + 0.5F) * f13 + f9), (double)((f18 + (float)k) * f13 + f11));
                            tessellator.addVertexWithUV((double)(f19 + (float)j2 + 0.0F), (double)(f4 + f3) - d2, (double)(f20 + 0.0F), (double)((f17 + (float)j2 + 0.5F) * f13 + f9), (double)((f18 + 0.0F) * f13 + f11));
                            tessellator.addVertexWithUV((double)(f19 + (float)j2 + 0.0F), (double)(f4 + 0.0F) + d2, (double)(f20 + 0.0F), (double)((f17 + (float)j2 + 0.5F) * f13 + f9), (double)((f18 + 0.0F) * f13 + f11));
                        }
                    }

                    if (i1 <= 1) {
                        tessellator.setNormal(1.0F, 0.0F, 0.0F);

                        for(j2 = 0; j2 < k; ++j2) {
                            tessellator.addVertexWithUV((double)(f19 + (float)j2 + 1.0F - f16), (double)(f4 + 0.0F) + d2, (double)(f20 + (float)k), (double)((f17 + (float)j2 + 0.5F) * f13 + f9), (double)((f18 + (float)k) * f13 + f11));
                            tessellator.addVertexWithUV((double)(f19 + (float)j2 + 1.0F - f16), (double)(f4 + f3) - d2, (double)(f20 + (float)k), (double)((f17 + (float)j2 + 0.5F) * f13 + f9), (double)((f18 + (float)k) * f13 + f11));
                            tessellator.addVertexWithUV((double)(f19 + (float)j2 + 1.0F - f16), (double)(f4 + f3) - d2, (double)(f20 + 0.0F), (double)((f17 + (float)j2 + 0.5F) * f13 + f9), (double)((f18 + 0.0F) * f13 + f11));
                            tessellator.addVertexWithUV((double)(f19 + (float)j2 + 1.0F - f16), (double)(f4 + 0.0F) + d2, (double)(f20 + 0.0F), (double)((f17 + (float)j2 + 0.5F) * f13 + f9), (double)((f18 + 0.0F) * f13 + f11));
                        }
                    }

                    tessellator.setColorRGBA_F(f5 * 0.8F, f6 * 0.8F, f7 * 0.8F, 0.8F);
                    if (j1 > -1) {
                        tessellator.setNormal(0.0F, 0.0F, -1.0F);

                        for(j2 = 0; j2 < k; ++j2) {
                            tessellator.addVertexWithUV((double)(f19 + 0.0F), (double)(f4 + f3) - d2, (double)(f20 + (float)j2 + 0.0F), (double)((f17 + 0.0F) * f13 + f9), (double)((f18 + (float)j2 + 0.5F) * f13 + f11));
                            tessellator.addVertexWithUV((double)(f19 + (float)k), (double)(f4 + f3) - d2, (double)(f20 + (float)j2 + 0.0F), (double)((f17 + (float)k) * f13 + f9), (double)((f18 + (float)j2 + 0.5F) * f13 + f11));
                            tessellator.addVertexWithUV((double)(f19 + (float)k), (double)(f4 + 0.0F) + d2, (double)(f20 + (float)j2 + 0.0F), (double)((f17 + (float)k) * f13 + f9), (double)((f18 + (float)j2 + 0.5F) * f13 + f11));
                            tessellator.addVertexWithUV((double)(f19 + 0.0F), (double)(f4 + 0.0F) + d2, (double)(f20 + (float)j2 + 0.0F), (double)((f17 + 0.0F) * f13 + f9), (double)((f18 + (float)j2 + 0.5F) * f13 + f11));
                        }
                    }

                    if (j1 <= 1) {
                        tessellator.setNormal(0.0F, 0.0F, 1.0F);

                        for(j2 = 0; j2 < k; ++j2) {
                            tessellator.addVertexWithUV((double)(f19 + 0.0F), (double)(f4 + f3) - d2, (double)(f20 + (float)j2 + 1.0F - f16), (double)((f17 + 0.0F) * f13 + f9), (double)((f18 + (float)j2 + 0.5F) * f13 + f11));
                            tessellator.addVertexWithUV((double)(f19 + (float)k), (double)(f4 + f3) - d2, (double)(f20 + (float)j2 + 1.0F - f16), (double)((f17 + (float)k) * f13 + f9), (double)((f18 + (float)j2 + 0.5F) * f13 + f11));
                            tessellator.addVertexWithUV((double)(f19 + (float)k), (double)(f4 + 0.0F) + d2, (double)(f20 + (float)j2 + 1.0F - f16), (double)((f17 + (float)k) * f13 + f9), (double)((f18 + (float)j2 + 0.5F) * f13 + f11));
                            tessellator.addVertexWithUV((double)(f19 + 0.0F), (double)(f4 + 0.0F) + d2, (double)(f20 + (float)j2 + 1.0F - f16), (double)((f17 + 0.0F) * f13 + f9), (double)((f18 + (float)j2 + 0.5F) * f13 + f11));
                        }
                    }

                    if (f4 > -f3 - 1.0F) {
                        tessellator.setColorRGBA_F(f5 * 0.7F, f6 * 0.7F, f7 * 0.7F, 0.8F);
                        tessellator.setNormal(0.0F, -1.0F, 0.0F);
                        tessellator.addVertexWithUV((double)(f19 + 0.0F), (double)(f4 + 0.0F), (double)(f20 + (float)k), (double)((f17 + 0.0F) * f13 + f9), (double)((f18 + (float)k) * f13 + f11));
                        tessellator.addVertexWithUV((double)(f19 + (float)k), (double)(f4 + 0.0F), (double)(f20 + (float)k), (double)((f17 + (float)k) * f13 + f9), (double)((f18 + (float)k) * f13 + f11));
                        tessellator.addVertexWithUV((double)(f19 + (float)k), (double)(f4 + 0.0F), (double)(f20 + 0.0F), (double)((f17 + (float)k) * f13 + f9), (double)((f18 + 0.0F) * f13 + f11));
                        tessellator.addVertexWithUV((double)(f19 + 0.0F), (double)(f4 + 0.0F), (double)(f20 + 0.0F), (double)((f17 + 0.0F) * f13 + f9), (double)((f18 + 0.0F) * f13 + f11));
                    }

                    if (f4 <= f3 + 1.0F) {
                        tessellator.setColorRGBA_F(f5, f6, f7, 0.8F);
                        tessellator.setNormal(0.0F, 1.0F, 0.0F);
                        tessellator.addVertexWithUV((double)(f19 + 0.0F), (double)(f4 + f3 - f16), (double)(f20 + (float)k), (double)((f17 + 0.0F) * f13 + f9), (double)((f18 + (float)k) * f13 + f11));
                        tessellator.addVertexWithUV((double)(f19 + (float)k), (double)(f4 + f3 - f16), (double)(f20 + (float)k), (double)((f17 + (float)k) * f13 + f9), (double)((f18 + (float)k) * f13 + f11));
                        tessellator.addVertexWithUV((double)(f19 + (float)k), (double)(f4 + f3 - f16), (double)(f20 + 0.0F), (double)((f17 + (float)k) * f13 + f9), (double)((f18 + 0.0F) * f13 + f11));
                        tessellator.addVertexWithUV((double)(f19 + 0.0F), (double)(f4 + f3 - f16), (double)(f20 + 0.0F), (double)((f17 + 0.0F) * f13 + f9), (double)((f18 + 0.0F) * f13 + f11));
                    }

                    tessellator.draw();
                }
            }
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(3042 /*GL_BLEND*/);
        GL11.glEnable(2884 /*GL_CULL_FACE*/);
    }

    public boolean updateRenderers(EntityLiving entityliving, boolean flag) {
        UpdateThread updatethread = Config.getUpdateThread();
        if (updatethread == null && Config.isBackgroundChunkLoading()) {
            updatethread = Config.createUpdateThread(Display.getDrawable());
            updatethread.pause();
        }

        if (this.worldRenderersToUpdate.size() <= 0) {
            return true;
        } else {
            int i = 0;
            int j = 4;
            int k = 0;
            WorldRenderer worldrenderer = null;
            float f = 3.402823E38F;
            int l = -1;

            int i1;
            float f2;
            for(i1 = 0; i1 < this.worldRenderersToUpdate.size(); ++i1) {
                WorldRenderer worldrenderer1 = (WorldRenderer)this.worldRenderersToUpdate.get(i1);
                if (worldrenderer1 != null) {
                    ++k;
                    if (!worldrenderer1.isUpdating) {
                        if (!worldrenderer1.needsUpdate) {
                            this.worldRenderersToUpdate.set(i1, (Object)null);
                        } else {
                            f2 = worldrenderer1.distanceToEntitySquared(entityliving);
                            if (f2 <= 256.0F) {
                                if (this.isActingNow() || this.firstUpdate) {
                                    if (updatethread != null) {
                                        updatethread.unpauseToEndOfUpdate();
                                    }

                                    worldrenderer1.updateRenderer();
                                    worldrenderer1.needsUpdate = false;
                                    this.worldRenderersToUpdate.set(i1, (Object)null);
                                    ++i;
                                    continue;
                                }

                                if (updatethread != null) {
                                    updatethread.addRendererToUpdate(worldrenderer1, true);
                                    worldrenderer1.needsUpdate = false;
                                    this.worldRenderersToUpdate.set(i1, (Object)null);
                                    ++i;
                                    continue;
                                }
                            }

                            if (!worldrenderer1.isInFrustum) {
                                f2 *= (float)j;
                            }

                            if (worldrenderer == null) {
                                worldrenderer = worldrenderer1;
                                f = f2;
                                l = i1;
                            } else if (f2 < f) {
                                worldrenderer = worldrenderer1;
                                f = f2;
                                l = i1;
                            }
                        }
                    }
                }
            }

            i1 = Config.getUpdatesPerFrame();
            boolean flag1 = false;
            if (Config.isDynamicUpdates() && !this.isMoving(entityliving)) {
                i1 *= 3;
                boolean var18 = true;
            }

            if (updatethread != null) {
                i1 = updatethread.getUpdateCapacity();
                if (i1 <= 0) {
                    return true;
                }
            }

            int j2;
            if (worldrenderer != null) {
                this.updateRenderer(worldrenderer);
                this.worldRenderersToUpdate.set(l, (Object)null);
                ++i;
                f2 = f / 5.0F;

                for(j2 = 0; j2 < this.worldRenderersToUpdate.size() && i < i1; ++j2) {
                    WorldRenderer worldrenderer2 = (WorldRenderer)this.worldRenderersToUpdate.get(j2);
                    if (worldrenderer2 != null && !worldrenderer2.isUpdating) {
                        float f3 = worldrenderer2.distanceToEntitySquared(entityliving);
                        if (!worldrenderer2.isInFrustum) {
                            f3 *= (float)j;
                        }

                        float f4 = Math.abs(f3 - f);
                        if (f4 < f2) {
                            this.updateRenderer(worldrenderer2);
                            this.worldRenderersToUpdate.set(j2, (Object)null);
                            ++i;
                        }
                    }
                }
            }

            if (k == 0) {
                this.worldRenderersToUpdate.clear();
            }

            if (this.worldRenderersToUpdate.size() > 100 && k < this.worldRenderersToUpdate.size() * 4 / 5) {
                int k1 = 0;

                for(j2 = 0; j2 < this.worldRenderersToUpdate.size(); ++j2) {
                    Object obj = this.worldRenderersToUpdate.get(j2);
                    if (obj != null && j2 != k1) {
                        this.worldRenderersToUpdate.set(k1, obj);
                        ++k1;
                    }
                }

                for(j2 = this.worldRenderersToUpdate.size() - 1; j2 >= k1; --j2) {
                    this.worldRenderersToUpdate.remove(j2);
                }
            }

            this.firstUpdate = false;
            return true;
        }
    }

    private void updateRenderer(WorldRenderer worldrenderer) {
        UpdateThread updatethread = Config.getUpdateThread();
        if (updatethread != null) {
            updatethread.addRendererToUpdate(worldrenderer, false);
            worldrenderer.needsUpdate = false;
        } else {
            worldrenderer.updateRenderer();
            worldrenderer.needsUpdate = false;
            worldrenderer.isUpdating = false;
        }
    }

    private boolean isMoving(EntityLiving entityliving) {
        boolean flag = this.isMovingNow(entityliving);
        if (flag) {
            this.lastMovedTime = System.currentTimeMillis();
            return true;
        } else {
            return System.currentTimeMillis() - this.lastMovedTime < 2000L;
        }
    }

    private boolean isMovingNow(EntityLiving entityliving) {
        double d = 0.001D;
        if (entityliving.isJumping) {
            return true;
        } else if (entityliving.isSneaking()) {
            return true;
        } else if ((double)entityliving.prevSwingProgress > d) {
            return true;
        } else if (this.mc.mouseHelper.deltaX != 0) {
            return true;
        } else if (this.mc.mouseHelper.deltaY != 0) {
            return true;
        } else if (Math.abs(entityliving.posX - entityliving.prevPosX) > d) {
            return true;
        } else if (Math.abs(entityliving.posY - entityliving.prevPosY) > d) {
            return true;
        } else {
            return Math.abs(entityliving.posZ - entityliving.prevPosZ) > d;
        }
    }

    private boolean isActingNow() {
        return Mouse.isButtonDown(0) ? true : Mouse.isButtonDown(1);
    }

    public void drawBlockBreaking(EntityPlayer entityplayer, MovingObjectPosition movingobjectposition, int i, ItemStack itemstack, float f) {
        Tessellator tessellator = Tessellator.instance;
        GL11.glEnable(3042 /*GL_BLEND*/);
        GL11.glEnable(3008 /*GL_ALPHA_TEST*/);
        GL11.glBlendFunc(770, 1);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, (MathHelper.sin((float)System.currentTimeMillis() / 100.0F) * 0.2F + 0.4F) * 0.5F);
        int k;
        if (i == 0) {
            if (this.damagePartialTime > 0.0F) {
                GL11.glBlendFunc(774, 768);
                int j = this.renderEngine.getTexture("/terrain.png");
                GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, j);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);
                GL11.glPushMatrix();
                k = this.worldObj.getBlockId(movingobjectposition.blockX, movingobjectposition.blockY, movingobjectposition.blockZ);
                Block block = k > 0 ? Block.blocksList[k] : null;
                GL11.glDisable(3008 /*GL_ALPHA_TEST*/);
                GL11.glPolygonOffset(-3.0F, -3.0F);
                GL11.glEnable(32823 /*GL_POLYGON_OFFSET_FILL*/);
                double d = entityplayer.lastTickPosX + (entityplayer.posX - entityplayer.lastTickPosX) * (double)f;
                double d1 = entityplayer.lastTickPosY + (entityplayer.posY - entityplayer.lastTickPosY) * (double)f;
                double d2 = entityplayer.lastTickPosZ + (entityplayer.posZ - entityplayer.lastTickPosZ) * (double)f;
                if (block == null) {
                    block = Block.stone;
                }

                GL11.glEnable(3008 /*GL_ALPHA_TEST*/);
                tessellator.startDrawingQuads();
                tessellator.setTranslationD(-d, -d1, -d2);
                tessellator.disableColor();
                this.globalRenderBlocks.renderBlockUsingTexture(block, movingobjectposition.blockX, movingobjectposition.blockY, movingobjectposition.blockZ, 240 + (int)(this.damagePartialTime * 10.0F));
                tessellator.draw();
                tessellator.setTranslationD(0.0D, 0.0D, 0.0D);
                GL11.glDisable(3008 /*GL_ALPHA_TEST*/);
                GL11.glPolygonOffset(0.0F, 0.0F);
                GL11.glDisable(32823 /*GL_POLYGON_OFFSET_FILL*/);
                GL11.glEnable(3008 /*GL_ALPHA_TEST*/);
                GL11.glDepthMask(true);
                GL11.glPopMatrix();
            }
        } else if (itemstack != null) {
            GL11.glBlendFunc(770, 771);
            float f1 = MathHelper.sin((float)System.currentTimeMillis() / 100.0F) * 0.2F + 0.8F;
            GL11.glColor4f(f1, f1, f1, MathHelper.sin((float)System.currentTimeMillis() / 200.0F) * 0.2F + 0.5F);
            k = this.renderEngine.getTexture("/terrain.png");
            GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, k);
            int i1 = movingobjectposition.blockX;
            int j1 = movingobjectposition.blockY;
            int k1 = movingobjectposition.blockZ;
            if (movingobjectposition.sideHit == 0) {
                --j1;
            }

            if (movingobjectposition.sideHit == 1) {
                ++j1;
            }

            if (movingobjectposition.sideHit == 2) {
                --k1;
            }

            if (movingobjectposition.sideHit == 3) {
                ++k1;
            }

            if (movingobjectposition.sideHit == 4) {
                --i1;
            }

            if (movingobjectposition.sideHit == 5) {
                ++i1;
            }
        }

        GL11.glDisable(3042 /*GL_BLEND*/);
        GL11.glDisable(3008 /*GL_ALPHA_TEST*/);
    }

    public void drawSelectionBox(EntityPlayer entityplayer, MovingObjectPosition movingobjectposition, int i, ItemStack itemstack, float f) {
        if (i == 0 && movingobjectposition.typeOfHit == EnumMovingObjectType.TILE) {
            GL11.glEnable(3042 /*GL_BLEND*/);
            GL11.glBlendFunc(770, 771);
            GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.4F);
            GL11.glLineWidth(2.0F);
            GL11.glDisable(3553 /*GL_TEXTURE_2D*/);
            GL11.glDepthMask(false);
            float f1 = 0.002F;
            int j = this.worldObj.getBlockId(movingobjectposition.blockX, movingobjectposition.blockY, movingobjectposition.blockZ);
            if (j > 0) {
                Block.blocksList[j].setBlockBoundsBasedOnState(this.worldObj, movingobjectposition.blockX, movingobjectposition.blockY, movingobjectposition.blockZ);
                double d = entityplayer.lastTickPosX + (entityplayer.posX - entityplayer.lastTickPosX) * (double)f;
                double d1 = entityplayer.lastTickPosY + (entityplayer.posY - entityplayer.lastTickPosY) * (double)f;
                double d2 = entityplayer.lastTickPosZ + (entityplayer.posZ - entityplayer.lastTickPosZ) * (double)f;
                this.drawOutlinedBoundingBox(Block.blocksList[j].getSelectedBoundingBoxFromPool(this.worldObj, movingobjectposition.blockX, movingobjectposition.blockY, movingobjectposition.blockZ).expand((double)f1, (double)f1, (double)f1).getOffsetBoundingBox(-d, -d1, -d2));
            }

            GL11.glDepthMask(true);
            GL11.glEnable(3553 /*GL_TEXTURE_2D*/);
            GL11.glDisable(3042 /*GL_BLEND*/);
        }

    }

    public static void drawOutlinedBoundingBox(AxisAlignedBB axisalignedbb) {
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawing(3);
        tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
        tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
        tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
        tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
        tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
        tessellator.draw();
        tessellator.startDrawing(3);
        tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
        tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
        tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
        tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
        tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
        tessellator.draw();
        tessellator.startDrawing(1);
        tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
        tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
        tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
        tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
        tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
        tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
        tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
        tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
        tessellator.draw();
    }
    
    public static void drawOutlinedBoundingBox(AxisAlignedBB axisalignedbb, float red, float green, float blue, float alpha) {
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawing(3);
        tessellator.setColorRGBA_F(red, green, blue, alpha);
        tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
        tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
        tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
        tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
        tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
        tessellator.draw();
        tessellator.startDrawing(3);
        tessellator.setColorRGBA_F(red, green, blue, alpha);
        tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
        tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
        tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
        tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
        tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
        tessellator.draw();
        tessellator.startDrawing(1);
        tessellator.setColorRGBA_F(red, green, blue, alpha);
        tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
        tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
        tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
        tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
        tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
        tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
        tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
        tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
        tessellator.draw();
    }

    public void func_949_a(int i, int j, int k, int l, int i1, int j1) {
        int k1 = MathHelper.bucketInt(i, 16);
        int l1 = MathHelper.bucketInt(j, 16);
        int i2 = MathHelper.bucketInt(k, 16);
        int j2 = MathHelper.bucketInt(l, 16);
        int k2 = MathHelper.bucketInt(i1, 16);
        int l2 = MathHelper.bucketInt(j1, 16);

        for(int i3 = k1; i3 <= j2; ++i3) {
            int j3 = i3 % this.renderChunksWide;
            if (j3 < 0) {
                j3 += this.renderChunksWide;
            }

            for(int k3 = l1; k3 <= k2; ++k3) {
                int l3 = k3 % this.renderChunksTall;
                if (l3 < 0) {
                    l3 += this.renderChunksTall;
                }

                for(int i4 = i2; i4 <= l2; ++i4) {
                    int j4 = i4 % this.renderChunksDeep;
                    if (j4 < 0) {
                        j4 += this.renderChunksDeep;
                    }

                    int k4 = (j4 * this.renderChunksTall + l3) * this.renderChunksWide + j3;
                    WorldRenderer worldrenderer = this.worldRenderers[k4];
                    if (!worldrenderer.needsUpdate) {
                        this.worldRenderersToUpdate.add(worldrenderer);
                        worldrenderer.markDirty();
                    }
                }
            }
        }

    }

    public void markBlockAndNeighborsNeedsUpdate(int i, int j, int k) {
        this.func_949_a(i - 1, j - 1, k - 1, i + 1, j + 1, k + 1);
    }

    public void markBlockRangeNeedsUpdate(int i, int j, int k, int l, int i1, int j1) {
        this.func_949_a(i - 1, j - 1, k - 1, l + 1, i1 + 1, j1 + 1);
    }

    public void clipRenderersByFrustrum(ICamera icamera, float f) {
        for(int i = 0; i < this.worldRenderers.length; ++i) {
            if (!this.worldRenderers[i].skipAllRenderPasses()) {
                this.worldRenderers[i].updateInFrustrum(icamera);
            }
        }

        ++this.frustrumCheckOffset;
    }

    public void playRecord(String s, int i, int j, int k) {
        if (s != null) {
            this.mc.ingameGUI.setRecordPlayingMessage("C418 - " + s);
        }

        this.mc.sndManager.playStreaming(s, (float)i, (float)j, (float)k, 1.0F, 1.0F);
    }

    public void playSound(String s, double d, double d1, double d2, float f, float f1) {
        float f2 = 16.0F;
        if (f > 1.0F) {
            f2 *= f;
        }

        if (this.mc.renderViewEntity.getDistanceSq(d, d1, d2) < (double)(f2 * f2)) {
            this.mc.sndManager.playSound(s, (float)d, (float)d1, (float)d2, f, f1);
        }

    }

    public void spawnParticle(String s, double d, double d1, double d2, double d3, double d4, double d5) {
        if (this.mc != null && this.mc.renderViewEntity != null && this.mc.effectRenderer != null) {
            double d6 = this.mc.renderViewEntity.posX - d;
            double d7 = this.mc.renderViewEntity.posY - d1;
            double d8 = this.mc.renderViewEntity.posZ - d2;
            double d9 = 16.0D;
            if (d6 * d6 + d7 * d7 + d8 * d8 <= d9 * d9) {
                if (s.equals("bubble")) {
                    this.mc.effectRenderer.addEffect(new EntityBubbleFX(this.worldObj, d, d1, d2, d3, d4, d5));
                } else if (s.equals("smoke")) {
                    if (Config.isAnimatedSmoke()) {
                        this.mc.effectRenderer.addEffect(new EntitySmokeFX(this.worldObj, d, d1, d2, d3, d4, d5));
                    }
                } else if (s.equals("note")) {
                    this.mc.effectRenderer.addEffect(new EntityNoteFX(this.worldObj, d, d1, d2, d3, d4, d5));
                } else if (s.equals("portal")) {
                    this.mc.effectRenderer.addEffect(new EntityPortalFX(this.worldObj, d, d1, d2, d3, d4, d5));
                } else if (s.equals("explode")) {
                    if (Config.isAnimatedExplosion()) {
                        this.mc.effectRenderer.addEffect(new EntityExplodeFX(this.worldObj, d, d1, d2, d3, d4, d5));
                    }
                } else if (s.equals("flame")) {
                    if (Config.isAnimatedFlame()) {
                        this.mc.effectRenderer.addEffect(new EntityFlameFX(this.worldObj, d, d1, d2, d3, d4, d5));
                    }
                } else if (s.equals("lava")) {
                    this.mc.effectRenderer.addEffect(new EntityLavaFX(this.worldObj, d, d1, d2));
                } else if (s.equals("footstep")) {
                    this.mc.effectRenderer.addEffect(new EntityFootStepFX(this.renderEngine, this.worldObj, d, d1, d2));
                } else if (s.equals("splash")) {
                    this.mc.effectRenderer.addEffect(new EntitySplashFX(this.worldObj, d, d1, d2, d3, d4, d5));
                } else if (s.equals("largesmoke")) {
                    if (Config.isAnimatedSmoke()) {
                        this.mc.effectRenderer.addEffect(new EntitySmokeFX(this.worldObj, d, d1, d2, d3, d4, d5, 2.5F));
                    }
                } else if (s.equals("reddust")) {
                    if (Config.isAnimatedRedstone()) {
                        this.mc.effectRenderer.addEffect(new EntityReddustFX(this.worldObj, d, d1, d2, (float)d3, (float)d4, (float)d5));
                    }
                } else if (s.equals("snowballpoof")) {
                    this.mc.effectRenderer.addEffect(new EntitySlimeFX(this.worldObj, d, d1, d2, Item.snowball));
                } else if (s.equals("snowshovel")) {
                    this.mc.effectRenderer.addEffect(new EntitySnowShovelFX(this.worldObj, d, d1, d2, d3, d4, d5));
                } else if (s.equals("slime")) {
                    this.mc.effectRenderer.addEffect(new EntitySlimeFX(this.worldObj, d, d1, d2, Item.slimeBall));
                } else if (s.equals("heart")) {
                    this.mc.effectRenderer.addEffect(new EntityHeartFX(this.worldObj, d, d1, d2, d3, d4, d5));
                }

            }
        }
    }

    public void obtainEntitySkin(Entity entity) {
        entity.updateCloak();
        if (entity.skinUrl != null) {
            this.renderEngine.obtainImageData(entity.skinUrl, new ImageBufferDownload());
        }

        if (entity.cloakUrl != null) {
            this.renderEngine.obtainImageData(entity.cloakUrl, new ImageBufferDownload());
        }

    }

    public void releaseEntitySkin(Entity entity) {
        if (entity.skinUrl != null) {
            this.renderEngine.releaseImageData(entity.skinUrl);
        }

        if (entity.cloakUrl != null) {
            this.renderEngine.releaseImageData(entity.cloakUrl);
        }

    }

    public void updateAllRenderers() {
        if (this.worldRenderers != null) {
            for(int i = 0; i < this.worldRenderers.length; ++i) {
                if (this.worldRenderers[i].isChunkLit && !this.worldRenderers[i].needsUpdate) {
                    this.worldRenderersToUpdate.add(this.worldRenderers[i]);
                    this.worldRenderers[i].markDirty();
                }
            }

        }
    }

    public void setAllRenderesVisible() {
        if (this.worldRenderers != null) {
            for(int i = 0; i < this.worldRenderers.length; ++i) {
                this.worldRenderers[i].isVisible = true;
            }

        }
    }

    public void doNothingWithTileEntity(int i, int j, int k, TileEntity tileentity) {
    }

    public void func_28137_f() {
        GLAllocation.deleteDisplayLists(this.glRenderListBase);
    }

    public void playAuxSFX(EntityPlayer entityplayer, int i, int j, int k, int l, int i1) {
        Random random = this.worldObj.rand;
        int i2;
        switch(i) {
        case 1000:
            this.worldObj.playSoundEffect((double)j, (double)k, (double)l, "random.click", 1.0F, 1.0F);
            break;
        case 1001:
            this.worldObj.playSoundEffect((double)j, (double)k, (double)l, "random.click", 1.0F, 1.2F);
            break;
        case 1002:
            this.worldObj.playSoundEffect((double)j, (double)k, (double)l, "random.bow", 1.0F, 1.2F);
            break;
        case 1003:
            if (Math.random() < 0.5D) {
                this.worldObj.playSoundEffect((double)j + 0.5D, (double)k + 0.5D, (double)l + 0.5D, "random.door_open", 1.0F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
            } else {
                this.worldObj.playSoundEffect((double)j + 0.5D, (double)k + 0.5D, (double)l + 0.5D, "random.door_close", 1.0F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
            }
            break;
        case 1004:
            this.worldObj.playSoundEffect((double)((float)j + 0.5F), (double)((float)k + 0.5F), (double)((float)l + 0.5F), "random.fizz", 0.5F, 2.6F + (random.nextFloat() - random.nextFloat()) * 0.8F);
            break;
        case 1005:
            if (Item.itemsList[i1] instanceof ItemRecord) {
                this.worldObj.playRecord(((ItemRecord)Item.itemsList[i1]).recordName, j, k, l);
            } else {
                this.worldObj.playRecord((String)null, j, k, l);
            }
            break;
        case 2000:
            int j1 = i1 % 3 - 1;
            int k1 = i1 / 3 % 3 - 1;
            double d = (double)j + (double)j1 * 0.6D + 0.5D;
            double d1 = (double)k + 0.5D;
            double d2 = (double)l + (double)k1 * 0.6D + 0.5D;

            for(i2 = 0; i2 < 10; ++i2) {
                double d3 = random.nextDouble() * 0.2D + 0.01D;
                double d4 = d + (double)j1 * 0.01D + (random.nextDouble() - 0.5D) * (double)k1 * 0.5D;
                double d5 = d1 + (random.nextDouble() - 0.5D) * 0.5D;
                double d6 = d2 + (double)k1 * 0.01D + (random.nextDouble() - 0.5D) * (double)j1 * 0.5D;
                double d7 = (double)j1 * d3 + random.nextGaussian() * 0.01D;
                double d8 = -0.03D + random.nextGaussian() * 0.01D;
                double d9 = (double)k1 * d3 + random.nextGaussian() * 0.01D;
                this.spawnParticle("smoke", d4, d5, d6, d7, d8, d9);
            }

            return;
        case 2001:
            i2 = i1 & 255;
            if (i2 > 0) {
                Block block = Block.blocksList[i2];
                this.mc.sndManager.playSound(block.stepSound.stepSoundDir(), (float)j + 0.5F, (float)k + 0.5F, (float)l + 0.5F, (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);
            }

            this.mc.effectRenderer.addBlockDestroyEffects(j, k, l, i1 & 255, i1 >> 8 & 255);
        }

    }

    public int renderAllSortedRenderers(int i, double d) {
        return this.renderSortedRenderers(0, this.sortedWorldRenderers.length, i, d);
    }
}
