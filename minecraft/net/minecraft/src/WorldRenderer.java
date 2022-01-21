package net.minecraft.src;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.lwjgl.opengl.GL11;

public class WorldRenderer {
    public World worldObj;
    private int glRenderList = -1;
    public static volatile int chunksUpdated = 0;
    public int posX;
    public int posY;
    public int posZ;
    public int sizeWidth;
    public int sizeHeight;
    public int sizeDepth;
    public int posXMinus;
    public int posYMinus;
    public int posZMinus;
    public int posXClip;
    public int posYClip;
    public int posZClip;
    public boolean isInFrustum = false;
    public boolean[] skipRenderPass = new boolean[2];
    public int posXPlus;
    public int posYPlus;
    public int posZPlus;
    public float rendererRadius;
    public volatile boolean needsUpdate;
    public AxisAlignedBB rendererBoundingBox;
    public int chunkIndex;
    public boolean isVisible = true;
    public boolean isWaitingOnOcclusionQuery;
    public int glOcclusionQuery;
    public boolean isChunkLit;
    private boolean isInitialized = false;
    public List tileEntityRenderers = new ArrayList();
    private List tileEntities;
    public boolean isVisibleFromPosition = false;
    public double visibleFromX;
    public double visibleFromY;
    public double visibleFromZ;
    private boolean needsBoxUpdate = false;
    public boolean isInFrustrumFully = false;
    public volatile boolean isUpdating = false;
    private int glRenderListStable;
    private int glRenderListBoundingBox;

    public WorldRenderer(World world, List list, int i, int j, int k, int l, int i1) {
        this.worldObj = world;
        this.tileEntities = list;
        this.sizeWidth = this.sizeHeight = this.sizeDepth = l;
        this.rendererRadius = MathHelper.sqrt_float((float)(this.sizeWidth * this.sizeWidth + this.sizeHeight * this.sizeHeight + this.sizeDepth * this.sizeDepth)) / 2.0F;
        this.glRenderList = i1;
        this.glRenderListStable = this.glRenderList + 393216;
        this.glRenderListBoundingBox = this.glRenderList + 2;
        this.posX = -999;
        this.setPosition(i, j, k);
        this.needsUpdate = false;
    }

    public void setPosition(int px, int py, int pz) {
        if (px != this.posX || py != this.posY || pz != this.posZ) {
            this.setDontDraw();
            this.posX = px;
            this.posY = py;
            this.posZ = pz;
            this.posXPlus = px + this.sizeWidth / 2;
            this.posYPlus = py + this.sizeHeight / 2;
            this.posZPlus = pz + this.sizeDepth / 2;
            this.posXClip = px & 1023;
            this.posYClip = py;
            this.posZClip = pz & 1023;
            this.posXMinus = px - this.posXClip;
            this.posYMinus = py - this.posYClip;
            this.posZMinus = pz - this.posZClip;
            float f = 0.0F;
            this.rendererBoundingBox = AxisAlignedBB.getBoundingBox((double)((float)px - f), (double)((float)py - f), (double)((float)pz - f), (double)((float)(px + this.sizeWidth) + f), (double)((float)(py + this.sizeHeight) + f), (double)((float)(pz + this.sizeDepth) + f));
            this.needsBoxUpdate = true;
            this.markDirty();
            this.isVisibleFromPosition = false;
        }
    }

    private void setupGLTranslation() {
        GL11.glTranslatef((float)this.posXClip, (float)this.posYClip, (float)this.posZClip);
    }

    public void updateRenderer() {
        this.updateRenderer((IUpdateListener)null);
        this.finishUpdate();
    }

    public void updateRenderer(IUpdateListener updateListener) {
        this.needsUpdate = false;
        ++chunksUpdated;
        int xMin = this.posX;
        int yMin = this.posY;
        int zMin = this.posZ;
        int xMax = this.posX + this.sizeWidth;
        int yMax = this.posY + this.sizeHeight;
        int zMax = this.posZ + this.sizeDepth;
        boolean[] tempSkipRenderPass = new boolean[2];

        for(int i = 0; i < tempSkipRenderPass.length; ++i) {
            tempSkipRenderPass[i] = true;
        }

        Object lightCache = Config.getFieldValue("LightCache", "cache");
        if (lightCache != null) {
            Config.callVoid(lightCache, "clear", new Object[0]);
            Config.callVoid("BlockCoord", "resetPool", new Object[0]);
        }

        Chunk.isLit = false;
        HashSet hashset = new HashSet();
        hashset.addAll(this.tileEntityRenderers);
        this.tileEntityRenderers.clear();
        int one = 1;
        ChunkCache chunkcache = new ChunkCache(this.worldObj, xMin - one, yMin - one, zMin - one, xMax + one, yMax + one, zMax + one);
        RenderBlocks renderblocks = new RenderBlocks(chunkcache);
        Tessellator tessellator = Tessellator.instance;

        int renderPass;
        for(renderPass = 0; renderPass < 2; ++renderPass) {
            boolean flag = false;
            boolean hasRenderedBlocks = false;
            boolean hasGlList = false;

            for(int y = yMin; y < yMax; ++y) {
                if (hasRenderedBlocks && updateListener != null) {
                    updateListener.updating();
                }

                for(int z = zMin; z < zMax; ++z) {
                    for(int x = xMin; x < xMax; ++x) {
                        int i3 = chunkcache.getBlockId(x, y, z);
                        if (i3 > 0) {
                            if (!hasGlList) {
                                hasGlList = true;
                                GL11.glNewList(this.glRenderList + renderPass, 4864 /*GL_COMPILE*/);
                                tessellator.setRenderingChunk(true);
                                tessellator.startDrawingQuads();
                            }

                            if (renderPass == 0 && Block.isBlockContainer[i3]) {
                                TileEntity tileentity = chunkcache.getBlockTileEntity(x, y, z);
                                if (TileEntityRenderer.instance.hasSpecialRenderer(tileentity)) {
                                    this.tileEntityRenderers.add(tileentity);
                                }
                            }

                            Block block = Block.blocksList[i3];
                            int blockPass = block.getRenderBlockPass();
                            if (blockPass != renderPass) {
                                flag = true;
                            } else if (blockPass == renderPass) {
                                hasRenderedBlocks |= renderblocks.renderBlockByRenderType(block, x, y, z);
                            }
                        }
                    }
                }
            }

            if (hasGlList) {
                if (updateListener != null) {
                    updateListener.updating();
                }

                tessellator.draw();
                GL11.glEndList();
                tessellator.setRenderingChunk(false);
            } else {
                hasRenderedBlocks = false;
            }

            if (hasRenderedBlocks) {
                tempSkipRenderPass[renderPass] = false;
            }

            if (!flag) {
                break;
            }
        }

        for(renderPass = 0; renderPass < 2; ++renderPass) {
            this.skipRenderPass[renderPass] = tempSkipRenderPass[renderPass];
        }

        HashSet hashset1 = new HashSet();
        hashset1.addAll(this.tileEntityRenderers);
        hashset1.removeAll(hashset);
        this.tileEntities.addAll(hashset1);
        hashset.removeAll(this.tileEntityRenderers);
        this.tileEntities.removeAll(hashset);
        this.isChunkLit = Chunk.isLit;
        this.isInitialized = true;
        this.isVisible = true;
        this.isVisibleFromPosition = false;
    }

    public float distanceToEntitySquared(Entity entity) {
        float f = (float)(entity.posX - (double)this.posXPlus);
        float f1 = (float)(entity.posY - (double)this.posYPlus);
        float f2 = (float)(entity.posZ - (double)this.posZPlus);
        return f * f + f1 * f1 + f2 * f2;
    }

    public void setDontDraw() {
        for(int i = 0; i < 2; ++i) {
            this.skipRenderPass[i] = true;
        }

        this.isInFrustum = false;
        this.isInitialized = false;
    }

    public void func_1204_c() {
        this.setDontDraw();
        this.worldObj = null;
    }

    public int getGLCallListForPass(int i) {
        if (!this.isInFrustum) {
            return -1;
        } else {
            return !this.skipRenderPass[i] ? this.glRenderListStable + i : -1;
        }
    }

    public void updateInFrustrum(ICamera icamera) {
        this.isInFrustum = icamera.isBoundingBoxInFrustum(this.rendererBoundingBox);
        if (this.isInFrustum && Config.isOcclusionEnabled() && Config.isOcclusionFancy()) {
            this.isInFrustrumFully = icamera.isBoundingBoxInFrustumFully(this.rendererBoundingBox);
        } else {
            this.isInFrustrumFully = false;
        }

    }

    public void callOcclusionQueryList() {
        GL11.glCallList(this.glRenderListBoundingBox);
    }

    public boolean skipAllRenderPasses() {
        if (!this.isInitialized) {
            return false;
        } else {
            return this.skipRenderPass[0] && this.skipRenderPass[1];
        }
    }

    public void markDirty() {
        this.needsUpdate = true;
    }

    public void finishUpdate() {
        int temp = this.glRenderList;
        this.glRenderList = this.glRenderListStable;
        this.glRenderListStable = temp;

        for(int i = 0; i < 2; ++i) {
            if (!this.skipRenderPass[i]) {
                GL11.glNewList(this.glRenderList + i, 4864 /*GL_COMPILE*/);
                GL11.glEndList();
            }
        }

        if (this.needsBoxUpdate && !this.skipAllRenderPasses()) {
            float f = 0.0F;
            GL11.glNewList(this.glRenderListBoundingBox, 4864 /*GL_COMPILE*/);
            RenderItem.renderAABB(AxisAlignedBB.getBoundingBoxFromPool((double)((float)this.posXClip - f), (double)((float)this.posYClip - f), (double)((float)this.posZClip - f), (double)((float)(this.posXClip + this.sizeWidth) + f), (double)((float)(this.posYClip + this.sizeHeight) + f), (double)((float)(this.posZClip + this.sizeDepth) + f)));
            GL11.glEndList();
        }

    }
}
