package net.minecraft.src;

import java.nio.FloatBuffer;
import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.src.MEDMEX.Modules.Render.Fullbright;
import net.minecraft.src.MEDMEX.Modules.Render.NoHurtCam;
import net.minecraft.src.MEDMEX.Modules.World.NoWeather;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.GLU;

public class EntityRenderer {
    public static boolean anaglyphEnable = false;
    public static int anaglyphField;
    private Minecraft mc;
    private float farPlaneDistance = 0.0F;
    public ItemRenderer itemRenderer;
    private int rendererUpdateCount;
    private Entity pointedEntity = null;
    private MouseFilter mouseFilterXAxis = new MouseFilter();
    private MouseFilter mouseFilterYAxis = new MouseFilter();
    private MouseFilter mouseFilterDummy1 = new MouseFilter();
    private MouseFilter mouseFilterDummy2 = new MouseFilter();
    private MouseFilter mouseFilterDummy3 = new MouseFilter();
    private MouseFilter mouseFilterDummy4 = new MouseFilter();
    private float thirdPersonDistance = 4.0F;
    private float thirdPersonDistanceTemp = 4.0F;
    private float debugCamYaw = 0.0F;
    private float prevDebugCamYaw = 0.0F;
    private float debugCamPitch = 0.0F;
    private float prevDebugCamPitch = 0.0F;
    private float debugCamFOV = 0.0F;
    private float prevDebugCamFOV = 0.0F;
    private float camRoll = 0.0F;
    private float prevCamRoll = 0.0F;
    private boolean cloudFog = false;
    private double cameraZoom = 1.0D;
    private double cameraYaw = 0.0D;
    private double cameraPitch = 0.0D;
    private long prevFrameTime = System.currentTimeMillis();
    private long renderEndNanoTime = 0L;
    private Random random = new Random();
    private int rainSoundCounter = 0;
    volatile int unusedVolatile0 = 0;
    volatile int unusedVolatile1 = 0;
    FloatBuffer fogColorBuffer = GLAllocation.createDirectFloatBuffer(16);
    float fogColorRed;
    float fogColorGreen;
    float fogColorBlue;
    private float fogColor2;
    private float fogColor1;
    private WorldProvider updatedWorldProvider = null;
    private boolean showDebugInfo = false;
    private boolean zoomMode = false;

    public EntityRenderer(Minecraft minecraft) {
        this.mc = minecraft;
        this.itemRenderer = new ItemRenderer(minecraft);
    }

    public void updateRenderer() {
        this.fogColor2 = this.fogColor1;
        this.thirdPersonDistanceTemp = this.thirdPersonDistance;
        this.prevDebugCamYaw = this.debugCamYaw;
        this.prevDebugCamPitch = this.debugCamPitch;
        this.prevDebugCamFOV = this.debugCamFOV;
        this.prevCamRoll = this.camRoll;
        if (this.mc.renderViewEntity == null) {
            this.mc.renderViewEntity = this.mc.thePlayer;
        }

        float f = this.mc.theWorld.getLightBrightness(MathHelper.floor_double(this.mc.renderViewEntity.posX), MathHelper.floor_double(this.mc.renderViewEntity.posY), MathHelper.floor_double(this.mc.renderViewEntity.posZ));
        float f1 = (float)(3 - this.mc.gameSettings.renderDistance) / 3.0F;
        float f2 = f * (1.0F - f1) + f1;
        this.fogColor1 += (f2 - this.fogColor1) * 0.1F;
        ++this.rendererUpdateCount;
        this.itemRenderer.updateEquippedItem();
        this.addRainParticles();
    }

    public void getMouseOver(float f) {
        if (this.mc.renderViewEntity != null) {
            if (this.mc.theWorld != null) {
                double d = (double)this.mc.playerController.getBlockReachDistance();
                this.mc.objectMouseOver = this.mc.renderViewEntity.rayTrace(d, f);
                double d1 = d;
                Vec3D vec3d = this.mc.renderViewEntity.getPosition(f);
                if (this.mc.objectMouseOver != null) {
                    d1 = this.mc.objectMouseOver.hitVec.distanceTo(vec3d);
                }

                if (this.mc.playerController instanceof PlayerControllerTest) {
                    d = 32.0D;
                    d1 = 32.0D;
                } else {
                    if (d1 > 3.0D) {
                        d1 = 3.0D;
                    }

                    d = d1;
                }

                Vec3D vec3d1 = this.mc.renderViewEntity.getLook(f);
                Vec3D vec3d2 = vec3d.addVector(vec3d1.xCoord * d, vec3d1.yCoord * d, vec3d1.zCoord * d);
                this.pointedEntity = null;
                float f1 = 1.0F;
                List list = this.mc.theWorld.getEntitiesWithinAABBExcludingEntity(this.mc.renderViewEntity, this.mc.renderViewEntity.boundingBox.addCoord(vec3d1.xCoord * d, vec3d1.yCoord * d, vec3d1.zCoord * d).expand((double)f1, (double)f1, (double)f1));
                double d2 = 0.0D;

                for(int i = 0; i < list.size(); ++i) {
                    Entity entity = (Entity)list.get(i);
                    if (entity.canBeCollidedWith()) {
                        float f2 = entity.getCollisionBorderSize();
                        AxisAlignedBB axisalignedbb = entity.boundingBox.expand((double)f2, (double)f2, (double)f2);
                        MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3d, vec3d2);
                        if (axisalignedbb.isVecInside(vec3d)) {
                            if (0.0D < d2 || d2 == 0.0D) {
                                this.pointedEntity = entity;
                                d2 = 0.0D;
                            }
                        } else if (movingobjectposition != null) {
                            double d3 = vec3d.distanceTo(movingobjectposition.hitVec);
                            if (d3 < d2 || d2 == 0.0D) {
                                this.pointedEntity = entity;
                                d2 = d3;
                            }
                        }
                    }
                }

                if (this.pointedEntity != null && !(this.mc.playerController instanceof PlayerControllerTest)) {
                    this.mc.objectMouseOver = new MovingObjectPosition(this.pointedEntity);
                }

            }
        }
    }

    private float getFOVModifier(float f) {
        EntityLiving entityliving = this.mc.renderViewEntity;
        float f1 = mc.gameSettings.fov * 110;
        if(mc.gameSettings.fov > 1)
        	mc.gameSettings.fov = 0.64f;
        if (entityliving.isInsideOfMaterial(Material.water)) {
            f1 = 60.0F;
        }

        if (Keyboard.isKeyDown(this.mc.gameSettings.ofKeyBindZoom.keyCode) && mc.currentScreen == null) {
            if (!this.zoomMode) {
                this.zoomMode = true;
                this.mc.gameSettings.smoothCamera = true;
            }

            if (this.zoomMode) {
                f1 /= 4.0F;
            }
        } else if (this.zoomMode) {
            this.zoomMode = false;
            this.mc.gameSettings.smoothCamera = false;
        }

        if (entityliving.health <= 0) {
            float f2 = (float)entityliving.deathTime + f;
            f1 /= (1.0F - 500.0F / (f2 + 500.0F)) * 2.0F + 1.0F;
        }

        return f1 + this.prevDebugCamFOV + (this.debugCamFOV - this.prevDebugCamFOV) * f;
    }

    private void hurtCameraEffect(float f) {
    	if(!NoHurtCam.instance.isEnabled()) {
        EntityLiving entityliving = this.mc.renderViewEntity;
        float f1 = (float)entityliving.hurtTime - f;
        float f3;
        if (entityliving.health <= 0) {
            f3 = (float)entityliving.deathTime + f;
            GL11.glRotatef(40.0F - 8000.0F / (f3 + 200.0F), 0.0F, 0.0F, 1.0F);
        }

        if (f1 >= 0.0F) {
            f1 /= (float)entityliving.maxHurtTime;
            f1 = MathHelper.sin(f1 * f1 * f1 * f1 * 3.141593F);
            f3 = entityliving.attackedAtYaw;
            GL11.glRotatef(-f3, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-f1 * 14.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(f3, 0.0F, 1.0F, 0.0F);
        }
    	}
    }

    private void setupViewBobbing(float f) {
        if (this.mc.renderViewEntity instanceof EntityPlayer) {
            EntityPlayer entityplayer = (EntityPlayer)this.mc.renderViewEntity;
            float f1 = entityplayer.distanceWalkedModified - entityplayer.prevDistanceWalkedModified;
            float f2 = -(entityplayer.distanceWalkedModified + f1 * f);
            float f3 = entityplayer.prevCameraYaw + (entityplayer.cameraYaw - entityplayer.prevCameraYaw) * f;
            float f4 = entityplayer.prevCameraPitch + (entityplayer.cameraPitch - entityplayer.prevCameraPitch) * f;
            GL11.glTranslatef(MathHelper.sin(f2 * 3.141593F) * f3 * 0.5F, -Math.abs(MathHelper.cos(f2 * 3.141593F) * f3), 0.0F);
            GL11.glRotatef(MathHelper.sin(f2 * 3.141593F) * f3 * 3.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(Math.abs(MathHelper.cos(f2 * 3.141593F - 0.2F) * f3) * 5.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(f4, 1.0F, 0.0F, 0.0F);
        }
    }

    private void orientCamera(float f) {
        EntityLiving entityliving = this.mc.renderViewEntity;
        float f1 = entityliving.yOffset - 1.62F;
        double d = entityliving.prevPosX + (entityliving.posX - entityliving.prevPosX) * (double)f;
        double d1 = entityliving.prevPosY + (entityliving.posY - entityliving.prevPosY) * (double)f - (double)f1;
        double d2 = entityliving.prevPosZ + (entityliving.posZ - entityliving.prevPosZ) * (double)f;
        GL11.glRotatef(this.prevCamRoll + (this.camRoll - this.prevCamRoll) * f, 0.0F, 0.0F, 1.0F);
        if (entityliving.isPlayerSleeping()) {
            f1 = (float)((double)f1 + 1.0D);
            GL11.glTranslatef(0.0F, 0.3F, 0.0F);
            if (!this.mc.gameSettings.debugCamEnable) {
                int i = this.mc.theWorld.getBlockId(MathHelper.floor_double(entityliving.posX), MathHelper.floor_double(entityliving.posY), MathHelper.floor_double(entityliving.posZ));
                if (i == Block.blockBed.blockID) {
                    int j = this.mc.theWorld.getBlockMetadata(MathHelper.floor_double(entityliving.posX), MathHelper.floor_double(entityliving.posY), MathHelper.floor_double(entityliving.posZ));
                    int k = j & 3;
                    GL11.glRotatef((float)(k * 90), 0.0F, 1.0F, 0.0F);
                }

                GL11.glRotatef(entityliving.prevRotationYaw + (entityliving.rotationYaw - entityliving.prevRotationYaw) * f + 180.0F, 0.0F, -1.0F, 0.0F);
                GL11.glRotatef(entityliving.prevRotationPitch + (entityliving.rotationPitch - entityliving.prevRotationPitch) * f, -1.0F, 0.0F, 0.0F);
            }
        } else if (this.mc.gameSettings.thirdPersonView) {
            double d3 = (double)(this.thirdPersonDistanceTemp + (this.thirdPersonDistance - this.thirdPersonDistanceTemp) * f);
            float f5;
            float f3;
            if (this.mc.gameSettings.debugCamEnable) {
                f3 = this.prevDebugCamYaw + (this.debugCamYaw - this.prevDebugCamYaw) * f;
                f5 = this.prevDebugCamPitch + (this.debugCamPitch - this.prevDebugCamPitch) * f;
                GL11.glTranslatef(0.0F, 0.0F, (float)(-d3));
                GL11.glRotatef(f5, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(f3, 0.0F, 1.0F, 0.0F);
            } else {
                f3 = entityliving.rotationYaw;
                f5 = entityliving.rotationPitch;
                double d4 = (double)(-MathHelper.sin(f3 / 180.0F * 3.141593F) * MathHelper.cos(f5 / 180.0F * 3.141593F)) * d3;
                double d5 = (double)(MathHelper.cos(f3 / 180.0F * 3.141593F) * MathHelper.cos(f5 / 180.0F * 3.141593F)) * d3;
                double d6 = (double)(-MathHelper.sin(f5 / 180.0F * 3.141593F)) * d3;

                for(int l = 0; l < 8; ++l) {
                    float f6 = (float)((l & 1) * 2 - 1);
                    float f7 = (float)((l >> 1 & 1) * 2 - 1);
                    float f8 = (float)((l >> 2 & 1) * 2 - 1);
                    f6 *= 0.1F;
                    f7 *= 0.1F;
                    f8 *= 0.1F;
                    MovingObjectPosition movingobjectposition = this.mc.theWorld.rayTraceBlocks(Vec3D.createVector(d + (double)f6, d1 + (double)f7, d2 + (double)f8), Vec3D.createVector(d - d4 + (double)f6 + (double)f8, d1 - d6 + (double)f7, d2 - d5 + (double)f8));
                    if (movingobjectposition != null) {
                        double d7 = movingobjectposition.hitVec.distanceTo(Vec3D.createVector(d, d1, d2));
                        if (d7 < d3) {
                            d3 = d7;
                        }
                    }
                }

                GL11.glRotatef(entityliving.rotationPitch - f5, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(entityliving.rotationYaw - f3, 0.0F, 1.0F, 0.0F);
                GL11.glTranslatef(0.0F, 0.0F, (float)(-d3));
                GL11.glRotatef(f3 - entityliving.rotationYaw, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(f5 - entityliving.rotationPitch, 1.0F, 0.0F, 0.0F);
            }
        } else {
            GL11.glTranslatef(0.0F, 0.0F, -0.1F);
        }

        if (!this.mc.gameSettings.debugCamEnable) {
            GL11.glRotatef(entityliving.prevRotationPitch + (entityliving.rotationPitch - entityliving.prevRotationPitch) * f, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(entityliving.prevRotationYaw + (entityliving.rotationYaw - entityliving.prevRotationYaw) * f + 180.0F, 0.0F, 1.0F, 0.0F);
        }

        GL11.glTranslatef(0.0F, f1, 0.0F);
        d = entityliving.prevPosX + (entityliving.posX - entityliving.prevPosX) * (double)f;
        d1 = entityliving.prevPosY + (entityliving.posY - entityliving.prevPosY) * (double)f - (double)f1;
        d2 = entityliving.prevPosZ + (entityliving.posZ - entityliving.prevPosZ) * (double)f;
        this.cloudFog = this.mc.renderGlobal.hasCloudFog(d, d1, d2, f);
    }

    public void setupCameraTransform(float f, int i) {
        this.farPlaneDistance = (float)(32 << 3 - this.mc.gameSettings.renderDistance);
        if (Config.isFarView()) {
            if (this.farPlaneDistance < 256.0F) {
                this.farPlaneDistance *= 3.0F;
            } else {
                this.farPlaneDistance *= 2.0F;
            }
        }

        if (Config.isFogFancy()) {
            this.farPlaneDistance *= 0.95F;
        } else {
            this.farPlaneDistance *= 0.83F;
        }

        GL11.glMatrixMode(5889 /*GL_PROJECTION*/);
        GL11.glLoadIdentity();
        float f1 = 0.07F;
        if (this.mc.gameSettings.anaglyph) {
            GL11.glTranslatef((float)(-(i * 2 - 1)) * f1, 0.0F, 0.0F);
        }

        if (this.cameraZoom != 1.0D) {
            GL11.glTranslatef((float)this.cameraYaw, (float)(-this.cameraPitch), 0.0F);
            GL11.glScaled(this.cameraZoom, this.cameraZoom, 1.0D);
            GLU.gluPerspective(this.getFOVModifier(f), (float)this.mc.displayWidth / (float)this.mc.displayHeight, 0.05F, this.farPlaneDistance * 2.0F);
        } else {
            GLU.gluPerspective(this.getFOVModifier(f), (float)this.mc.displayWidth / (float)this.mc.displayHeight, 0.05F, this.farPlaneDistance * 2.0F);
        }

        GL11.glMatrixMode(5888 /*GL_MODELVIEW0_ARB*/);
        GL11.glLoadIdentity();
        if (this.mc.gameSettings.anaglyph) {
            GL11.glTranslatef((float)(i * 2 - 1) * 0.1F, 0.0F, 0.0F);
        }

        this.hurtCameraEffect(f);
        if (this.mc.gameSettings.viewBobbing) {
            this.setupViewBobbing(f);
        }

        float f2 = this.mc.thePlayer.prevTimeInPortal + (this.mc.thePlayer.timeInPortal - this.mc.thePlayer.prevTimeInPortal) * f;
        if (f2 > 0.0F) {
            float f3 = 5.0F / (f2 * f2 + 5.0F) - f2 * 0.04F;
            f3 *= f3;
            GL11.glRotatef(((float)this.rendererUpdateCount + f) * 20.0F, 0.0F, 1.0F, 1.0F);
            GL11.glScalef(1.0F / f3, 1.0F, 1.0F);
            GL11.glRotatef(-((float)this.rendererUpdateCount + f) * 20.0F, 0.0F, 1.0F, 1.0F);
        }

        this.orientCamera(f);
    }

    private void renderHand(float f, int i) {
        GL11.glLoadIdentity();
        if (this.mc.gameSettings.anaglyph) {
            GL11.glTranslatef((float)(i * 2 - 1) * 0.1F, 0.0F, 0.0F);
        }

        GL11.glPushMatrix();
        this.hurtCameraEffect(f);
        if (this.mc.gameSettings.viewBobbing) {
            this.setupViewBobbing(f);
        }

        if (!this.mc.gameSettings.thirdPersonView && !this.mc.renderViewEntity.isPlayerSleeping() && !this.mc.gameSettings.hideGUI) {
            this.itemRenderer.renderItemInFirstPerson(f);
        }

        GL11.glPopMatrix();
        if (!this.mc.gameSettings.thirdPersonView && !this.mc.renderViewEntity.isPlayerSleeping()) {
            this.itemRenderer.renderOverlays(f);
            this.hurtCameraEffect(f);
        }

        if (this.mc.gameSettings.viewBobbing) {
            this.setupViewBobbing(f);
        }

    }

    public void updateCameraAndRender(float partialTicks) {
        World world = this.mc.theWorld;
        if (world != null && world.worldProvider != null && this.updatedWorldProvider != world.worldProvider) {
            this.updateWorldLightLevels();
            this.updatedWorldProvider = this.mc.theWorld.worldProvider;
        }

        Minecraft.hasPaidCheckTime = 0L;
        RenderBlocks.fancyGrass = Config.isGrassFancy();
        if (Config.isBetterGrassFancy()) {
            RenderBlocks.fancyGrass = true;
        }

        Block.leaves.setGraphicsLevel(Config.isTreesFancy());
        Config.setMinecraft(this.mc);
        if (Config.getIconWidthTerrain() > 16 && !(this.itemRenderer instanceof ItemRendererHD)) {
            this.itemRenderer = new ItemRendererHD(this.mc);
            RenderManager.instance.itemRenderer = this.itemRenderer;
        }

        if (world != null) {
            world.autosavePeriod = this.mc.gameSettings.ofAutoSaveTicks;
        }

        if (!Config.isWeatherEnabled() && world != null && world.worldInfo != null) {
            world.worldInfo.setRaining(false);
        }

        if (world != null) {
            long time = world.getWorldTime();
            long timeOfDay = time % 24000L;
            if (Config.isTimeDayOnly()) {
                if (timeOfDay <= 1000L) {
                    world.setWorldTime(time - timeOfDay + 1001L);
                }

                if (timeOfDay >= 11000L) {
                    world.setWorldTime(time - timeOfDay + 24001L);
                }
            }

            if (Config.isTimeNightOnly()) {
                if (timeOfDay <= 14000L) {
                    world.setWorldTime(time - timeOfDay + 14001L);
                }

                if (timeOfDay >= 22000L) {
                    world.setWorldTime(time - timeOfDay + 24000L + 14001L);
                }
            }
        }

        if (!Display.isActive()) {
            if (System.currentTimeMillis() - this.prevFrameTime > 500L) {
                this.mc.displayInGameMenu();
            }
        } else {
            this.prevFrameTime = System.currentTimeMillis();
        }

        if (this.mc.inGameHasFocus) {
            this.mc.mouseHelper.mouseXYChange();
            float f1 = this.mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
            float f2 = f1 * f1 * f1 * 8.0F;
            float f3 = (float)this.mc.mouseHelper.deltaX * f2;
            float f4 = (float)this.mc.mouseHelper.deltaY * f2;
            int l = 1;
            if (this.mc.gameSettings.invertMouse) {
                l = -1;
            }

            if (this.mc.gameSettings.smoothCamera) {
                f3 = this.mouseFilterXAxis.smooth(f3, 0.05F * f2);
                f4 = this.mouseFilterYAxis.smooth(f4, 0.05F * f2);
            }

            this.mc.thePlayer.setAngles(f3, f4 * (float)l);
        }

        if (!this.mc.skipRenderWorld) {
            anaglyphEnable = this.mc.gameSettings.anaglyph;
            ScaledResolution scaledresolution = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
            int i = scaledresolution.getScaledWidth();
            int j = scaledresolution.getScaledHeight();
            int k = Mouse.getX() * i / this.mc.displayWidth;
            int i1 = j - Mouse.getY() * j / this.mc.displayHeight - 1;
            char fpsLimit = 200;
            if (this.mc.gameSettings.limitFramerate == 1) {
                fpsLimit = 120;
            }

            if (this.mc.gameSettings.limitFramerate == 2) {
                fpsLimit = 40;
            }

            long l1;
            if (this.mc.theWorld != null) {
                if (this.mc.gameSettings.limitFramerate == 0) {
                    this.renderWorld(partialTicks, 0L);
                } else {
                    this.renderWorld(partialTicks, this.renderEndNanoTime + (long)(1.0E9D / (double)fpsLimit));
                }

                if (this.mc.gameSettings.limitFramerate == 2) {
                    l1 = (this.renderEndNanoTime + (long)(1.0E9D / (double)fpsLimit) - System.nanoTime()) / 1000000L;
                    if (l1 > 0L && l1 < 500L) {
                        try {
                            Thread.sleep(l1);
                        } catch (InterruptedException var13) {
                            var13.printStackTrace();
                        }
                    }
                }

                this.renderEndNanoTime = System.nanoTime();
                if (!this.mc.gameSettings.hideGUI || this.mc.currentScreen != null) {
                    if (this.mc.gameSettings.ofFastDebugInfo) {
                        Minecraft var10000 = this.mc;
                        if (Minecraft.isDebugInfoEnabled()) {
                            this.showDebugInfo = !this.showDebugInfo;
                        }

                        if (this.showDebugInfo) {
                            this.mc.gameSettings.showDebugInfo = true;
                        }
                    }

                    this.mc.ingameGUI.renderGameOverlay(partialTicks, this.mc.currentScreen != null, k, i1);
                    if (this.mc.gameSettings.ofFastDebugInfo) {
                        this.mc.gameSettings.showDebugInfo = false;
                    }
                }
            } else {
                GL11.glViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
                GL11.glMatrixMode(5889 /*GL_PROJECTION*/);
                GL11.glLoadIdentity();
                GL11.glMatrixMode(5888 /*GL_MODELVIEW0_ARB*/);
                GL11.glLoadIdentity();
                this.setupOverlayRendering();
                if (this.mc.gameSettings.limitFramerate == 2) {
                    l1 = (this.renderEndNanoTime + (long)(1.0E9D / (double)fpsLimit) - System.nanoTime()) / 1000000L;
                    if (l1 < 0L) {
                        l1 += 10L;
                    }

                    if (l1 > 0L && l1 < 500L) {
                        try {
                            Thread.sleep(l1);
                        } catch (InterruptedException var12) {
                            var12.printStackTrace();
                        }
                    }
                }

                this.renderEndNanoTime = System.nanoTime();
            }

            if (this.mc.currentScreen != null) {
                GL11.glClear(256);
                this.mc.currentScreen.drawScreen(k, i1, partialTicks);
                if (this.mc.currentScreen != null && this.mc.currentScreen.guiParticles != null) {
                    this.mc.currentScreen.guiParticles.draw(partialTicks);
                }
            }

        }
    }

    public void updateWorldLightLevels() {
        if (this.mc != null) {
            if (this.mc.theWorld != null) {
                if (this.mc.theWorld.worldProvider != null) {
                    float brightness = this.mc.gameSettings.ofBrightness;
                    float[] lightLevels = this.mc.theWorld.worldProvider.lightBrightnessTable;
                    float minLevel = 0.05F;
                    if (this.mc.theWorld.worldProvider != null && this.mc.theWorld.worldProvider.isNether) {
                        minLevel = 0.1F + brightness * 0.15F;
                    }

                    float k = 3.0F * (1.0F - brightness);

                    for(int i = 0; i <= 15; ++i) {
                    	if(Fullbright.instance.isEnabled()) {
                    		float f1 = 1.0F - (float)15 / 15.0F;
                            lightLevels[i] = (1.0F - f1) / (f1 * k + 1.0F) * (1.0F - minLevel) + minLevel;
                    	}else {
                        float f1 = 1.0F - (float)i / 15.0F;
                        lightLevels[i] = (1.0F - f1) / (f1 * k + 1.0F) * (1.0F - minLevel) + minLevel;
                    	}
                    }

                    Config.setLightLevels(lightLevels);
                }
            }
        }
    }

    public void renderWorld(float partialTicks, long n) {
        GL11.glEnable(2884 /*GL_CULL_FACE*/);
        GL11.glEnable(2929 /*GL_DEPTH_TEST*/);
        if (this.mc.renderViewEntity == null) {
            this.mc.renderViewEntity = this.mc.thePlayer;
        }

        this.getMouseOver(partialTicks);
        EntityLiving entityliving = this.mc.renderViewEntity;
        RenderGlobal renderglobal = this.mc.renderGlobal;
        EffectRenderer effectrenderer = this.mc.effectRenderer;
        double d = entityliving.lastTickPosX + (entityliving.posX - entityliving.lastTickPosX) * (double)partialTicks;
        double d1 = entityliving.lastTickPosY + (entityliving.posY - entityliving.lastTickPosY) * (double)partialTicks;
        double d2 = entityliving.lastTickPosZ + (entityliving.posZ - entityliving.lastTickPosZ) * (double)partialTicks;
        IChunkProvider ichunkprovider = this.mc.theWorld.getIChunkProvider();
        if (ichunkprovider instanceof ChunkProviderLoadOrGenerate) {
            ChunkProviderLoadOrGenerate chunkproviderloadorgenerate = (ChunkProviderLoadOrGenerate)ichunkprovider;
            int j = MathHelper.floor_float((float)((int)d)) >> 4;
            int k = MathHelper.floor_float((float)((int)d2)) >> 4;
            chunkproviderloadorgenerate.setCurrentChunkOver(j, k);
        }

        for(int i = 0; i < 2; ++i) {
            if (this.mc.gameSettings.anaglyph) {
                anaglyphField = i;
                if (anaglyphField == 0) {
                    GL11.glColorMask(false, true, true, false);
                } else {
                    GL11.glColorMask(true, false, false, false);
                }
            }

            GL11.glViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
            this.updateFogColor(partialTicks);
            GL11.glClear(16640);
            GL11.glEnable(2884 /*GL_CULL_FACE*/);
            this.setupCameraTransform(partialTicks, i);
            ClippingHelperImpl.getInstance();
            if (this.mc.gameSettings.renderDistance < 2 || Config.isFarView()) {
                this.setupFog(-1, partialTicks);
                renderglobal.renderSky(partialTicks);
            }

            GL11.glEnable(2912 /*GL_FOG*/);
            this.setupFog(1, partialTicks);
            if (this.mc.gameSettings.ambientOcclusion) {
                GL11.glShadeModel(7425 /*GL_SMOOTH*/);
            }

            Frustrum frustrum = new Frustrum();
            frustrum.setPosition(d, d1, d2);
            this.mc.renderGlobal.clipRenderersByFrustrum(frustrum, partialTicks);
            if (i == 0) {
                while(!this.mc.renderGlobal.updateRenderers(entityliving, false) && n != 0L) {
                    long l1 = n - System.nanoTime();
                    if (l1 < 0L || (double)l1 > 1.0E9D) {
                        break;
                    }
                }
            }

            this.setupFog(0, partialTicks);
            GL11.glEnable(2912 /*GL_FOG*/);
            GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, this.mc.renderEngine.getTexture("/terrain.png"));
            RenderHelper.disableStandardItemLighting();
            if (Config.isUseAlphaFunc()) {
                GL11.glAlphaFunc(516, Config.getAlphaFuncLevel());
            }

            renderglobal.sortAndRender(entityliving, 0, (double)partialTicks);
            GL11.glShadeModel(7424 /*GL_FLAT*/);
            RenderHelper.enableStandardItemLighting();
            renderglobal.renderEntities(entityliving.getPosition(partialTicks), frustrum, partialTicks);
            effectrenderer.renderLitParticles(entityliving, partialTicks);
            RenderHelper.disableStandardItemLighting();
            this.setupFog(0, partialTicks);
            effectrenderer.renderParticles(entityliving, partialTicks);
            if (this.mc.objectMouseOver != null && entityliving.isInsideOfMaterial(Material.water) && entityliving instanceof EntityPlayer) {
                EntityPlayer entityplayer = (EntityPlayer)entityliving;
                GL11.glDisable(3008 /*GL_ALPHA_TEST*/);
                renderglobal.drawBlockBreaking(entityplayer, this.mc.objectMouseOver, 0, entityplayer.inventory.getCurrentItem(), partialTicks);
                renderglobal.drawSelectionBox(entityplayer, this.mc.objectMouseOver, 0, entityplayer.inventory.getCurrentItem(), partialTicks);
                GL11.glEnable(3008 /*GL_ALPHA_TEST*/);
            }

            GL11.glBlendFunc(770, 771);
            this.setupFog(0, partialTicks);
            GL11.glEnable(3042 /*GL_BLEND*/);
            GL11.glDisable(2884 /*GL_CULL_FACE*/);
            GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, this.mc.renderEngine.getTexture("/terrain.png"));
            UpdateThread updateThread = Config.getUpdateThread();
            if (updateThread != null) {
                updateThread.unpause();
            }

            if (Config.isWaterFancy()) {
                if (this.mc.gameSettings.ambientOcclusion) {
                    GL11.glShadeModel(7425 /*GL_SMOOTH*/);
                }

                GL11.glColorMask(false, false, false, false);
                int num = renderglobal.renderAllSortedRenderers(1, (double)partialTicks);
                if (this.mc.gameSettings.anaglyph) {
                    if (anaglyphField == 0) {
                        GL11.glColorMask(false, true, true, true);
                    } else {
                        GL11.glColorMask(true, false, false, true);
                    }
                } else {
                    GL11.glColorMask(true, true, true, true);
                }

                if (num > 0) {
                    renderglobal.renderAllSortedRenderers(1, (double)partialTicks);
                }

                GL11.glShadeModel(7424 /*GL_FLAT*/);
            } else {
                renderglobal.sortAndRender(entityliving, 1, (double)partialTicks);
            }

            if (updateThread != null) {
                updateThread.pause();
            }

            GL11.glDepthMask(true);
            GL11.glEnable(2884 /*GL_CULL_FACE*/);
            GL11.glDisable(3042 /*GL_BLEND*/);
            if (this.cameraZoom == 1.0D && entityliving instanceof EntityPlayer && this.mc.objectMouseOver != null && !entityliving.isInsideOfMaterial(Material.water)) {
                EntityPlayer entityplayer1 = (EntityPlayer)entityliving;
                GL11.glDisable(3008 /*GL_ALPHA_TEST*/);
                renderglobal.drawBlockBreaking(entityplayer1, this.mc.objectMouseOver, 0, entityplayer1.inventory.getCurrentItem(), partialTicks);
                renderglobal.drawSelectionBox(entityplayer1, this.mc.objectMouseOver, 0, entityplayer1.inventory.getCurrentItem(), partialTicks);
                GL11.glEnable(3008 /*GL_ALPHA_TEST*/);
            }

            this.renderRainSnow(partialTicks);
            GL11.glDisable(2912 /*GL_FOG*/);
            if (this.pointedEntity == null) {
            }

            this.setupFog(0, partialTicks);
            GL11.glEnable(2912 /*GL_FOG*/);
            renderglobal.renderClouds(partialTicks);
            GL11.glDisable(2912 /*GL_FOG*/);
            this.setupFog(1, partialTicks);
            if (this.cameraZoom == 1.0D) {
                GL11.glClear(256);
                this.renderHand(partialTicks, i);
            }

            if (!this.mc.gameSettings.anaglyph) {
                return;
            }
        }

        GL11.glColorMask(true, true, true, false);
    }

    private void addRainParticles() {
    	if(!NoWeather.instance.isEnabled()) {
        float f = this.mc.theWorld.getRainStrength(1.0F);
        if (!Config.isRainFancy()) {
            f /= 2.0F;
        }

        if (f != 0.0F) {
            this.random.setSeed((long)this.rendererUpdateCount * 312987231L);
            EntityLiving entityliving = this.mc.renderViewEntity;
            World world = this.mc.theWorld;
            int i = MathHelper.floor_double(entityliving.posX);
            int j = MathHelper.floor_double(entityliving.posY);
            int k = MathHelper.floor_double(entityliving.posZ);
            byte byte0 = 10;
            double d = 0.0D;
            double d1 = 0.0D;
            double d2 = 0.0D;
            int l = 0;

            for(int i1 = 0; i1 < (int)(100.0F * f * f); ++i1) {
                int j1 = i + this.random.nextInt(byte0) - this.random.nextInt(byte0);
                int k1 = k + this.random.nextInt(byte0) - this.random.nextInt(byte0);
                int l1 = world.findTopSolidBlock(j1, k1);
                int i2 = world.getBlockId(j1, l1 - 1, k1);
                if (l1 <= j + byte0 && l1 >= j - byte0 && world.getWorldChunkManager().getBiomeGenAt(j1, k1).canSpawnLightningBolt()) {
                    float f1 = this.random.nextFloat();
                    float f2 = this.random.nextFloat();
                    if (i2 > 0) {
                        if (Block.blocksList[i2].blockMaterial == Material.lava) {
                            this.mc.effectRenderer.addEffect(new EntitySmokeFX(world, (double)((float)j1 + f1), (double)((float)l1 + 0.1F) - Block.blocksList[i2].minY, (double)((float)k1 + f2), 0.0D, 0.0D, 0.0D));
                        } else {
                            ++l;
                            if (this.random.nextInt(l) == 0) {
                                d = (double)((float)j1 + f1);
                                d1 = (double)((float)l1 + 0.1F) - Block.blocksList[i2].minY;
                                d2 = (double)((float)k1 + f2);
                            }

                            this.mc.effectRenderer.addEffect(new EntityRainFX(world, (double)((float)j1 + f1), (double)((float)l1 + 0.1F) - Block.blocksList[i2].minY, (double)((float)k1 + f2)));
                        }
                    }
                }
            }

            if (l > 0 && this.random.nextInt(3) < this.rainSoundCounter++) {
                this.rainSoundCounter = 0;
                if (d1 > entityliving.posY + 1.0D && world.findTopSolidBlock(MathHelper.floor_double(entityliving.posX), MathHelper.floor_double(entityliving.posZ)) > MathHelper.floor_double(entityliving.posY)) {
                    this.mc.theWorld.playSoundEffect(d, d1, d2, "ambient.weather.rain", 0.1F, 0.5F);
                } else {
                    this.mc.theWorld.playSoundEffect(d, d1, d2, "ambient.weather.rain", 0.2F, 1.0F);
                }
            }

        }
    	}
    }

    protected void renderRainSnow(float f) {
    	if(!NoWeather.instance.isEnabled()) {
        float f1 = this.mc.theWorld.getRainStrength(f);
        if (f1 > 0.0F) {
            if (!Config.isRainOff()) {
                EntityLiving entityliving = this.mc.renderViewEntity;
                World world = this.mc.theWorld;
                int i = MathHelper.floor_double(entityliving.posX);
                int j = MathHelper.floor_double(entityliving.posY);
                int k = MathHelper.floor_double(entityliving.posZ);
                Tessellator tessellator = Tessellator.instance;
                GL11.glDisable(2884 /*GL_CULL_FACE*/);
                GL11.glNormal3f(0.0F, 1.0F, 0.0F);
                GL11.glEnable(3042 /*GL_BLEND*/);
                GL11.glBlendFunc(770, 771);
                GL11.glAlphaFunc(516, 0.01F);
                GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, this.mc.renderEngine.getTexture("/environment/snow.png"));
                double d = entityliving.lastTickPosX + (entityliving.posX - entityliving.lastTickPosX) * (double)f;
                double d1 = entityliving.lastTickPosY + (entityliving.posY - entityliving.lastTickPosY) * (double)f;
                double d2 = entityliving.lastTickPosZ + (entityliving.posZ - entityliving.lastTickPosZ) * (double)f;
                int l = MathHelper.floor_double(d1);
                int i1 = 5;
                if (Config.isRainFancy()) {
                    i1 = 10;
                }

                BiomeGenBase[] abiomegenbase = world.getWorldChunkManager().getBiomeGenBaseAt(i - i1, k - i1, i1 * 2 + 1, i1 * 2 + 1);
                int j1 = 0;

                int l1;
                int j2;
                BiomeGenBase biomegenbase1;
                int l2;
                int j3;
                int l3;
                float f4;
                for(l1 = i - i1; l1 <= i + i1; ++l1) {
                    for(j2 = k - i1; j2 <= k + i1; ++j2) {
                        biomegenbase1 = abiomegenbase[j1++];
                        if (biomegenbase1.getEnableSnow()) {
                            l2 = world.findTopSolidBlock(l1, j2);
                            if (l2 < 0) {
                                l2 = 0;
                            }

                            j3 = l2;
                            if (l2 < l) {
                                j3 = l;
                            }

                            l3 = j - i1;
                            int i4 = j + i1;
                            if (l3 < l2) {
                                l3 = l2;
                            }

                            if (i4 < l2) {
                                i4 = l2;
                            }

                            f4 = 1.0F;
                            if (l3 != i4) {
                                this.random.setSeed((long)(l1 * l1 * 3121 /*GL_RGBA_MODE*/ + l1 * 45238971 + j2 * j2 * 418711 + j2 * 13761));
                                float f5 = (float)this.rendererUpdateCount + f;
                                float f6 = ((float)(this.rendererUpdateCount & 511) + f) / 512.0F;
                                float f7 = this.random.nextFloat() + f5 * 0.01F * (float)this.random.nextGaussian();
                                float f8 = this.random.nextFloat() + f5 * (float)this.random.nextGaussian() * 0.001F;
                                double d5 = (double)((float)l1 + 0.5F) - entityliving.posX;
                                double d6 = (double)((float)j2 + 0.5F) - entityliving.posZ;
                                float f11 = MathHelper.sqrt_double(d5 * d5 + d6 * d6) / (float)i1;
                                tessellator.startDrawingQuads();
                                float f12 = world.getLightBrightness(l1, j3, j2);
                                GL11.glColor4f(f12, f12, f12, ((1.0F - f11 * f11) * 0.3F + 0.5F) * f1);
                                tessellator.setTranslationD(-d * 1.0D, -d1 * 1.0D, -d2 * 1.0D);
                                tessellator.addVertexWithUV((double)(l1 + 0), (double)l3, (double)j2 + 0.5D, (double)(0.0F * f4 + f7), (double)((float)l3 * f4 / 4.0F + f6 * f4 + f8));
                                tessellator.addVertexWithUV((double)(l1 + 1), (double)l3, (double)j2 + 0.5D, (double)(1.0F * f4 + f7), (double)((float)l3 * f4 / 4.0F + f6 * f4 + f8));
                                tessellator.addVertexWithUV((double)(l1 + 1), (double)i4, (double)j2 + 0.5D, (double)(1.0F * f4 + f7), (double)((float)i4 * f4 / 4.0F + f6 * f4 + f8));
                                tessellator.addVertexWithUV((double)(l1 + 0), (double)i4, (double)j2 + 0.5D, (double)(0.0F * f4 + f7), (double)((float)i4 * f4 / 4.0F + f6 * f4 + f8));
                                tessellator.addVertexWithUV((double)l1 + 0.5D, (double)l3, (double)(j2 + 0), (double)(0.0F * f4 + f7), (double)((float)l3 * f4 / 4.0F + f6 * f4 + f8));
                                tessellator.addVertexWithUV((double)l1 + 0.5D, (double)l3, (double)(j2 + 1), (double)(1.0F * f4 + f7), (double)((float)l3 * f4 / 4.0F + f6 * f4 + f8));
                                tessellator.addVertexWithUV((double)l1 + 0.5D, (double)i4, (double)(j2 + 1), (double)(1.0F * f4 + f7), (double)((float)i4 * f4 / 4.0F + f6 * f4 + f8));
                                tessellator.addVertexWithUV((double)l1 + 0.5D, (double)i4, (double)(j2 + 0), (double)(0.0F * f4 + f7), (double)((float)i4 * f4 / 4.0F + f6 * f4 + f8));
                                tessellator.setTranslationD(0.0D, 0.0D, 0.0D);
                                tessellator.draw();
                            }
                        }
                    }
                }

                GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, this.mc.renderEngine.getTexture("/environment/rain.png"));
                if (Config.isRainFancy()) {
                    i1 = 10;
                }

                j1 = 0;

                for(l1 = i - i1; l1 <= i + i1; ++l1) {
                    for(j2 = k - i1; j2 <= k + i1; ++j2) {
                        biomegenbase1 = abiomegenbase[j1++];
                        if (biomegenbase1.canSpawnLightningBolt()) {
                            l2 = world.findTopSolidBlock(l1, j2);
                            j3 = j - i1;
                            l3 = j + i1;
                            if (j3 < l2) {
                                j3 = l2;
                            }

                            if (l3 < l2) {
                                l3 = l2;
                            }

                            float f2 = 1.0F;
                            if (j3 != l3) {
                                this.random.setSeed((long)(l1 * l1 * 3121 /*GL_RGBA_MODE*/ + l1 * 45238971 + j2 * j2 * 418711 + j2 * 13761));
                                f4 = ((float)(this.rendererUpdateCount + l1 * l1 * 3121 /*GL_RGBA_MODE*/ + l1 * 45238971 + j2 * j2 * 418711 + j2 * 13761 & 31) + f) / 32.0F * (3.0F + this.random.nextFloat());
                                double d3 = (double)((float)l1 + 0.5F) - entityliving.posX;
                                double d4 = (double)((float)j2 + 0.5F) - entityliving.posZ;
                                float f9 = MathHelper.sqrt_double(d3 * d3 + d4 * d4) / (float)i1;
                                tessellator.startDrawingQuads();
                                float f10 = world.getLightBrightness(l1, 128, j2) * 0.85F + 0.15F;
                                GL11.glColor4f(f10, f10, f10, ((1.0F - f9 * f9) * 0.5F + 0.5F) * f1);
                                tessellator.setTranslationD(-d * 1.0D, -d1 * 1.0D, -d2 * 1.0D);
                                tessellator.addVertexWithUV((double)(l1 + 0), (double)j3, (double)j2 + 0.5D, (double)(0.0F * f2), (double)((float)j3 * f2 / 4.0F + f4 * f2));
                                tessellator.addVertexWithUV((double)(l1 + 1), (double)j3, (double)j2 + 0.5D, (double)(1.0F * f2), (double)((float)j3 * f2 / 4.0F + f4 * f2));
                                tessellator.addVertexWithUV((double)(l1 + 1), (double)l3, (double)j2 + 0.5D, (double)(1.0F * f2), (double)((float)l3 * f2 / 4.0F + f4 * f2));
                                tessellator.addVertexWithUV((double)(l1 + 0), (double)l3, (double)j2 + 0.5D, (double)(0.0F * f2), (double)((float)l3 * f2 / 4.0F + f4 * f2));
                                tessellator.addVertexWithUV((double)l1 + 0.5D, (double)j3, (double)(j2 + 0), (double)(0.0F * f2), (double)((float)j3 * f2 / 4.0F + f4 * f2));
                                tessellator.addVertexWithUV((double)l1 + 0.5D, (double)j3, (double)(j2 + 1), (double)(1.0F * f2), (double)((float)j3 * f2 / 4.0F + f4 * f2));
                                tessellator.addVertexWithUV((double)l1 + 0.5D, (double)l3, (double)(j2 + 1), (double)(1.0F * f2), (double)((float)l3 * f2 / 4.0F + f4 * f2));
                                tessellator.addVertexWithUV((double)l1 + 0.5D, (double)l3, (double)(j2 + 0), (double)(0.0F * f2), (double)((float)l3 * f2 / 4.0F + f4 * f2));
                                tessellator.setTranslationD(0.0D, 0.0D, 0.0D);
                                tessellator.draw();
                            }
                        }
                    }
                }

                GL11.glEnable(2884 /*GL_CULL_FACE*/);
                GL11.glDisable(3042 /*GL_BLEND*/);
                GL11.glAlphaFunc(516, 0.1F);
            }
        }
    	}
    }

    public void setupOverlayRendering() {
        ScaledResolution scaledresolution = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        GL11.glClear(256);
        GL11.glMatrixMode(5889 /*GL_PROJECTION*/);
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0D, scaledresolution.scaledWidthD, scaledresolution.scaledHeightD, 0.0D, 1000.0D, 3000.0D);
        GL11.glMatrixMode(5888 /*GL_MODELVIEW0_ARB*/);
        GL11.glLoadIdentity();
        GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
    }

    private void updateFogColor(float f) {
        World world = this.mc.theWorld;
        EntityLiving entityliving = this.mc.renderViewEntity;
        float f1 = 1.0F / (float)(4 - this.mc.gameSettings.renderDistance);
        f1 = 1.0F - (float)Math.pow((double)f1, 0.25D);
        Vec3D vec3d = world.getSkyColor(this.mc.renderViewEntity, f);
        float f2 = (float)vec3d.xCoord;
        float f3 = (float)vec3d.yCoord;
        float f4 = (float)vec3d.zCoord;
        Vec3D vec3d1 = world.getFogColor(f);
        this.fogColorRed = (float)vec3d1.xCoord;
        this.fogColorGreen = (float)vec3d1.yCoord;
        this.fogColorBlue = (float)vec3d1.zCoord;
        this.fogColorRed += (f2 - this.fogColorRed) * f1;
        this.fogColorGreen += (f3 - this.fogColorGreen) * f1;
        this.fogColorBlue += (f4 - this.fogColorBlue) * f1;
        float f5 = world.getRainStrength(f);
        float f7;
        float f10;
        if (f5 > 0.0F) {
            f7 = 1.0F - f5 * 0.5F;
            f10 = 1.0F - f5 * 0.4F;
            this.fogColorRed *= f7;
            this.fogColorGreen *= f7;
            this.fogColorBlue *= f10;
        }

        f7 = world.getWeightedThunderStrength(f);
        if (f7 > 0.0F) {
            f10 = 1.0F - f7 * 0.5F;
            this.fogColorRed *= f10;
            this.fogColorGreen *= f10;
            this.fogColorBlue *= f10;
        }

        if (this.cloudFog) {
            Vec3D vec3d2 = world.drawClouds(f);
            this.fogColorRed = (float)vec3d2.xCoord;
            this.fogColorGreen = (float)vec3d2.yCoord;
            this.fogColorBlue = (float)vec3d2.zCoord;
        } else if (entityliving.isInsideOfMaterial(Material.water)) {
            this.fogColorRed = 0.02F;
            this.fogColorGreen = 0.02F;
            this.fogColorBlue = 0.2F;
        } else if (entityliving.isInsideOfMaterial(Material.lava)) {
            this.fogColorRed = 0.6F;
            this.fogColorGreen = 0.1F;
            this.fogColorBlue = 0.0F;
        }

        f10 = this.fogColor2 + (this.fogColor1 - this.fogColor2) * f;
        this.fogColorRed *= f10;
        this.fogColorGreen *= f10;
        this.fogColorBlue *= f10;
        if (this.mc.gameSettings.anaglyph) {
            float f11 = (this.fogColorRed * 30.0F + this.fogColorGreen * 59.0F + this.fogColorBlue * 11.0F) / 100.0F;
            float f12 = (this.fogColorRed * 30.0F + this.fogColorGreen * 70.0F) / 100.0F;
            float f13 = (this.fogColorRed * 30.0F + this.fogColorBlue * 70.0F) / 100.0F;
            this.fogColorRed = f11;
            this.fogColorGreen = f12;
            this.fogColorBlue = f13;
        }

        GL11.glClearColor(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 0.0F);
    }

    private void setupFog(int i, float partialTicks) {
        EntityLiving entityliving = this.mc.renderViewEntity;
        GL11.glFog(2918 /*GL_FOG_COLOR*/, this.setFogColorBuffer(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 1.0F));
        GL11.glNormal3f(0.0F, -1.0F, 0.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        float fogStart;
        float fogEnd;
        float f5;
        float f8;
        float f11;
        float f14;
        if (this.cloudFog) {
            GL11.glFogi(2917 /*GL_FOG_MODE*/, 2048 /*GL_EXP*/);
            GL11.glFogf(2914 /*GL_FOG_DENSITY*/, 0.1F);
            fogStart = 1.0F;
            fogEnd = 1.0F;
            f5 = 1.0F;
            if (this.mc.gameSettings.anaglyph) {
                f8 = (fogStart * 30.0F + fogEnd * 59.0F + f5 * 11.0F) / 100.0F;
                f11 = (fogStart * 30.0F + fogEnd * 70.0F) / 100.0F;
                f14 = (fogStart * 30.0F + f5 * 70.0F) / 100.0F;
            }
        } else if (entityliving.isInsideOfMaterial(Material.water)) {
            GL11.glFogi(2917 /*GL_FOG_MODE*/, 2048 /*GL_EXP*/);
            fogStart = 0.1F;
            if (Config.isClearWater()) {
                fogStart = 0.02F;
            }

            GL11.glFogf(2914 /*GL_FOG_DENSITY*/, fogStart);
            fogEnd = 0.4F;
            f5 = 0.4F;
            f8 = 0.9F;
            if (this.mc.gameSettings.anaglyph) {
                f11 = (fogEnd * 30.0F + f5 * 59.0F + f8 * 11.0F) / 100.0F;
                f14 = (fogEnd * 30.0F + f5 * 70.0F) / 100.0F;
                float var10 = (fogEnd * 30.0F + f8 * 70.0F) / 100.0F;
            }
        } else if (entityliving.isInsideOfMaterial(Material.lava)) {
            GL11.glFogi(2917 /*GL_FOG_MODE*/, 2048 /*GL_EXP*/);
            GL11.glFogf(2914 /*GL_FOG_DENSITY*/, 2.0F);
            fogStart = 0.4F;
            fogEnd = 0.3F;
            f5 = 0.3F;
            if (this.mc.gameSettings.anaglyph) {
                f8 = (fogStart * 30.0F + fogEnd * 59.0F + f5 * 11.0F) / 100.0F;
                f11 = (fogStart * 30.0F + fogEnd * 70.0F) / 100.0F;
                f14 = (fogStart * 30.0F + f5 * 70.0F) / 100.0F;
            }
        } else {
            GL11.glFogi(2917 /*GL_FOG_MODE*/, 9729 /*GL_LINEAR*/);
            if (GLContext.getCapabilities().GL_NV_fog_distance) {
                if (Config.isFogFancy()) {
                    GL11.glFogi(34138, 34139);
                } else {
                    GL11.glFogi(34138, 34140);
                }
            }

            fogStart = Config.getFogStart();
            fogEnd = 1.0F;
            if (i < 0) {
                fogStart = 0.0F;
                fogEnd = 0.8F;
            }

            if (this.mc.theWorld.worldProvider.isNether) {
                fogStart = 0.0F;
                fogEnd = 1.0F;
            }

            GL11.glFogf(2915 /*GL_FOG_START*/, this.farPlaneDistance * fogStart);
            GL11.glFogf(2916 /*GL_FOG_END*/, this.farPlaneDistance * fogEnd);
        }

        GL11.glEnable(2903 /*GL_COLOR_MATERIAL*/);
        GL11.glColorMaterial(1028 /*GL_FRONT*/, 4608 /*GL_AMBIENT*/);
    }

    private FloatBuffer setFogColorBuffer(float f, float f1, float f2, float f3) {
        this.fogColorBuffer.clear();
        this.fogColorBuffer.put(f).put(f1).put(f2).put(f3);
        this.fogColorBuffer.flip();
        return this.fogColorBuffer;
    }
}
