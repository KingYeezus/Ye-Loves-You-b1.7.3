package net.minecraft.src;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

public class GameSettings {
    private static final String[] RENDER_DISTANCES = new String[]{"options.renderDistance.far", "options.renderDistance.normal", "options.renderDistance.short", "options.renderDistance.tiny"};
    private static final String[] DIFFICULTIES = new String[]{"options.difficulty.peaceful", "options.difficulty.easy", "options.difficulty.normal", "options.difficulty.hard"};
    private static final String[] GUISCALES = new String[]{"options.guiScale.auto", "options.guiScale.small", "options.guiScale.normal", "options.guiScale.large"};
    private static final String[] LIMIT_FRAMERATES = new String[]{"performance.max", "performance.balanced", "performance.powersaver"};
    public float fov = 70;
    public float musicVolume = 1.0F;
    public float soundVolume = 1.0F;
    public float mouseSensitivity = 0.5F;
    public boolean invertMouse = false;
    public int renderDistance = 0;
    public boolean viewBobbing = true;
    public boolean anaglyph = false;
    public boolean advancedOpengl = false;
    public int limitFramerate = 1;
    public boolean fancyGraphics = true;
    public boolean ambientOcclusion = true;
    public boolean ofFogFancy = false;
    public float ofFogStart = 0.8F;
    public int ofMipmapLevel = 0;
    public boolean ofMipmapLinear = false;
    public boolean ofLoadFar = false;
    public int ofPreloadedChunks = 0;
    public boolean ofOcclusionFancy = false;
    public boolean ofSmoothFps = false;
    public boolean ofSmoothInput = false;
    public float ofBrightness = 0.0F;
    public float ofAoLevel = 0.0F;
    public int ofClouds = 0;
    public float ofCloudsHeight = 0.0F;
    public int ofTrees = 0;
    public int ofGrass = 0;
    public int ofRain = 0;
    public int ofWater = 0;
    public int ofBetterGrass = 3;
    public int ofAutoSaveTicks = 4000;
    public boolean ofFastDebugInfo = false;
    public boolean ofWeather = true;
    public boolean ofSky = true;
    public boolean ofStars = true;
    public int ofChunkUpdates = 1;
    public boolean ofChunkUpdatesDynamic = true;
    public boolean ofFarView = false;
    public int ofTime = 0;
    public boolean ofClearWater = false;
    public int ofAnimatedWater = 0;
    public int ofAnimatedLava = 0;
    public boolean ofAnimatedFire = true;
    public boolean ofAnimatedPortal = true;
    public boolean ofAnimatedRedstone = true;
    public boolean ofAnimatedExplosion = true;
    public boolean ofAnimatedFlame = true;
    public boolean ofAnimatedSmoke = true;
    public static final int DEFAULT = 0;
    public static final int FAST = 1;
    public static final int FANCY = 2;
    public static final int OFF = 3;
    public static final int ANIM_ON = 0;
    public static final int ANIM_GENERATED = 1;
    public static final int ANIM_OFF = 2;
    public KeyBinding ofKeyBindZoom;
    public String skin = "Default";
    public KeyBinding keyBindForward = new KeyBinding("key.forward", 17);
    public KeyBinding keyBindLeft = new KeyBinding("key.left", 30);
    public KeyBinding keyBindBack = new KeyBinding("key.back", 31);
    public KeyBinding keyBindRight = new KeyBinding("key.right", 32);
    public KeyBinding keyBindJump = new KeyBinding("key.jump", 57);
    public KeyBinding keyBindInventory = new KeyBinding("key.inventory", 18);
    public KeyBinding keyBindDrop = new KeyBinding("key.drop", 16);
    public KeyBinding keyBindChat = new KeyBinding("key.chat", 20);
    public KeyBinding keyBindToggleFog = new KeyBinding("key.fog", 33);
    public KeyBinding keyBindSneak = new KeyBinding("key.sneak", 42);
    public KeyBinding[] keyBindings;
    protected Minecraft mc;
    private File optionsFile;
    public int difficulty;
    public boolean hideGUI;
    public boolean thirdPersonView;
    public static boolean showDebugInfo;
    public String lastServer;
    public boolean noclip;
    public boolean smoothCamera;
    public boolean debugCamEnable;
    public float noclipRate;
    public float debugCamRate;
    public int guiScale;

    public GameSettings(Minecraft minecraft, File file) {
        this.ofKeyBindZoom = new KeyBinding("Zoom", 46);
        this.keyBindings = new KeyBinding[]{this.keyBindForward, this.keyBindLeft, this.keyBindBack, this.keyBindRight, this.keyBindJump, this.keyBindSneak, this.keyBindDrop, this.keyBindInventory, this.keyBindChat, this.keyBindToggleFog, this.ofKeyBindZoom};
        this.difficulty = 2;
        this.hideGUI = false;
        this.thirdPersonView = false;
        this.showDebugInfo = false;
        this.lastServer = "";
        this.noclip = false;
        this.smoothCamera = false;
        this.debugCamEnable = false;
        this.noclipRate = 1.0F;
        this.debugCamRate = 1.0F;
        this.guiScale = 0;
        this.mc = minecraft;
        this.optionsFile = new File(file, "options.txt");
        this.loadOptions();
        Config.setGameSettings(this);
    }

    public GameSettings() {
        this.keyBindings = new KeyBinding[]{this.keyBindForward, this.keyBindLeft, this.keyBindBack, this.keyBindRight, this.keyBindJump, this.keyBindSneak, this.keyBindDrop, this.keyBindInventory, this.keyBindChat, this.keyBindToggleFog};
        this.difficulty = 2;
        this.hideGUI = false;
        this.thirdPersonView = false;
        this.showDebugInfo = false;
        this.lastServer = "";
        this.noclip = false;
        this.smoothCamera = false;
        this.debugCamEnable = false;
        this.noclipRate = 1.0F;
        this.debugCamRate = 1.0F;
        this.guiScale = 0;
    }

    public String getKeyBindingDescription(int i) {
        StringTranslate stringtranslate = StringTranslate.getInstance();
        return stringtranslate.translateKey(this.keyBindings[i].keyDescription);
    }

    public String getOptionDisplayString(int i) {
        return Keyboard.getKeyName(this.keyBindings[i].keyCode);
    }

    public void setKeyBinding(int i, int j) {
        this.keyBindings[i].keyCode = j;
        this.saveOptions();
    }

    public void setOptionFloatValue(EnumOptions enumoptions, float f) {
        if (enumoptions == EnumOptions.MUSIC) {
            this.musicVolume = f;
            this.mc.sndManager.onSoundOptionsChanged();
        }

        if (enumoptions == EnumOptions.SOUND) {
            this.soundVolume = f;
            this.mc.sndManager.onSoundOptionsChanged();
        }

        if (enumoptions == EnumOptions.SENSITIVITY) {
            this.mouseSensitivity = f;
        }
        if(enumoptions == EnumOptions.FOV) {
        	this.fov = f;
        }

        if (enumoptions == EnumOptions.BRIGHTNESS) {
            this.ofBrightness = f;
            this.updateWorldLightLevels();
        }

        if (enumoptions == EnumOptions.CLOUD_HEIGHT) {
            this.ofCloudsHeight = f;
        }

        if (enumoptions == EnumOptions.AO_LEVEL) {
            this.ofAoLevel = f;
            this.ambientOcclusion = this.ofAoLevel > 0.0F;
            this.mc.renderGlobal.loadRenderers();
        }

    }

    public void updateWorldLightLevels() {
        if (this.mc.entityRenderer != null) {
            this.mc.entityRenderer.updateWorldLightLevels();
        }

        if (this.mc.renderGlobal != null) {
            this.mc.renderGlobal.loadRenderers();
        }

    }

    private void updateWaterOpacity() {
        int opacity = 3;
        if (this.ofClearWater) {
            opacity = 1;
        }

        Block.waterStill.setLightOpacity(opacity);
        Block.waterMoving.setLightOpacity(opacity);
        if (this.mc.theWorld != null) {
            IChunkProvider cp = this.mc.theWorld.chunkProvider;
            if (cp != null) {
                for(int x = -512; x < 512; ++x) {
                    for(int z = -512; z < 512; ++z) {
                        if (cp.chunkExists(x, z)) {
                            Chunk c = cp.provideChunk(x, z);
                            if (c != null) {
                                byte[] data = c.skylightMap.data;

                                for(int i = 0; i < data.length; ++i) {
                                    data[i] = 0;
                                }

                                c.generateSkylightMap();
                            }
                        }
                    }
                }

                this.mc.renderGlobal.loadRenderers();
            }
        }
    }

    public void setOptionValue(EnumOptions enumoptions, int i) {
        if (enumoptions == EnumOptions.INVERT_MOUSE) {
            this.invertMouse = !this.invertMouse;
        }

        if (enumoptions == EnumOptions.RENDER_DISTANCE) {
            this.renderDistance = this.renderDistance + i & 3;
        }

        if (enumoptions == EnumOptions.GUI_SCALE) {
            this.guiScale = this.guiScale + i & 3;
        }

        if (enumoptions == EnumOptions.VIEW_BOBBING) {
            this.viewBobbing = !this.viewBobbing;
        }

        if (enumoptions == EnumOptions.ADVANCED_OPENGL) {
            if (!Config.isOcclusionAvailable()) {
                this.ofOcclusionFancy = false;
                this.advancedOpengl = false;
            } else if (!this.advancedOpengl) {
                this.advancedOpengl = true;
                this.ofOcclusionFancy = false;
            } else if (!this.ofOcclusionFancy) {
                this.ofOcclusionFancy = true;
            } else {
                this.ofOcclusionFancy = false;
                this.advancedOpengl = false;
            }

            this.mc.renderGlobal.setAllRenderesVisible();
        }

        if (enumoptions == EnumOptions.ANAGLYPH) {
            this.anaglyph = !this.anaglyph;
            this.mc.renderEngine.refreshTextures();
        }

        if (enumoptions == EnumOptions.FRAMERATE_LIMIT) {
            this.limitFramerate = (this.limitFramerate + i) % 4;
            Display.setVSyncEnabled(this.limitFramerate == 3);
        }

        if (enumoptions == EnumOptions.DIFFICULTY) {
            this.difficulty = this.difficulty + i & 3;
        }

        if (enumoptions == EnumOptions.GRAPHICS) {
            this.fancyGraphics = !this.fancyGraphics;
            this.mc.renderGlobal.loadRenderers();
        }

        if (enumoptions == EnumOptions.AMBIENT_OCCLUSION) {
            this.ambientOcclusion = !this.ambientOcclusion;
            this.mc.renderGlobal.loadRenderers();
        }

        if (enumoptions == EnumOptions.FOG_FANCY) {
            if (!Config.isFancyFogAvailable()) {
                this.ofFogFancy = false;
            } else {
                this.ofFogFancy = !this.ofFogFancy;
            }
        }

        if (enumoptions == EnumOptions.FOG_START) {
            this.ofFogStart += 0.2F;
            if (this.ofFogStart > 0.81F) {
                this.ofFogStart = 0.2F;
            }
        }

        if (enumoptions == EnumOptions.MIPMAP_LEVEL) {
            ++this.ofMipmapLevel;
            if (this.ofMipmapLevel > 4) {
                this.ofMipmapLevel = 0;
            }

            this.mc.renderEngine.refreshTextures();
        }

        if (enumoptions == EnumOptions.MIPMAP_TYPE) {
            this.ofMipmapLinear = !this.ofMipmapLinear;
            this.mc.renderEngine.refreshTextures();
        }

        if (enumoptions == EnumOptions.LOAD_FAR) {
            this.ofLoadFar = !this.ofLoadFar;
            this.mc.renderGlobal.loadRenderers();
        }

        if (enumoptions == EnumOptions.PRELOADED_CHUNKS) {
            this.ofPreloadedChunks += 2;
            if (this.ofPreloadedChunks > 8) {
                this.ofPreloadedChunks = 0;
            }

            this.mc.renderGlobal.loadRenderers();
        }

        if (enumoptions == EnumOptions.SMOOTH_FPS) {
            this.ofSmoothFps = !this.ofSmoothFps;
        }

        if (enumoptions == EnumOptions.SMOOTH_INPUT) {
            this.ofSmoothInput = !this.ofSmoothInput;
        }

        if (enumoptions == EnumOptions.CLOUDS) {
            ++this.ofClouds;
            if (this.ofClouds > 3) {
                this.ofClouds = 0;
            }
        }

        if (enumoptions == EnumOptions.TREES) {
            ++this.ofTrees;
            if (this.ofTrees > 2) {
                this.ofTrees = 0;
            }

            this.mc.renderGlobal.loadRenderers();
        }

        if (enumoptions == EnumOptions.GRASS) {
            ++this.ofGrass;
            if (this.ofGrass > 2) {
                this.ofGrass = 0;
            }

            RenderBlocks.fancyGrass = Config.isGrassFancy();
            this.mc.renderGlobal.loadRenderers();
        }

        if (enumoptions == EnumOptions.RAIN) {
            ++this.ofRain;
            if (this.ofRain > 3) {
                this.ofRain = 0;
            }
        }

        if (enumoptions == EnumOptions.WATER) {
            ++this.ofWater;
            if (this.ofWater > 2) {
                this.ofWater = 0;
            }
        }

        if (enumoptions == EnumOptions.ANIMATED_WATER) {
            ++this.ofAnimatedWater;
            if (this.ofAnimatedWater > 2) {
                this.ofAnimatedWater = 0;
            }

            this.mc.renderEngine.refreshTextures();
        }

        if (enumoptions == EnumOptions.ANIMATED_LAVA) {
            ++this.ofAnimatedLava;
            if (this.ofAnimatedLava > 2) {
                this.ofAnimatedLava = 0;
            }

            this.mc.renderEngine.refreshTextures();
        }

        if (enumoptions == EnumOptions.ANIMATED_FIRE) {
            this.ofAnimatedFire = !this.ofAnimatedFire;
            this.mc.renderEngine.refreshTextures();
        }

        if (enumoptions == EnumOptions.ANIMATED_PORTAL) {
            this.ofAnimatedPortal = !this.ofAnimatedPortal;
            this.mc.renderEngine.refreshTextures();
        }

        if (enumoptions == EnumOptions.ANIMATED_REDSTONE) {
            this.ofAnimatedRedstone = !this.ofAnimatedRedstone;
        }

        if (enumoptions == EnumOptions.ANIMATED_EXPLOSION) {
            this.ofAnimatedExplosion = !this.ofAnimatedExplosion;
        }

        if (enumoptions == EnumOptions.ANIMATED_FLAME) {
            this.ofAnimatedFlame = !this.ofAnimatedFlame;
        }

        if (enumoptions == EnumOptions.ANIMATED_SMOKE) {
            this.ofAnimatedSmoke = !this.ofAnimatedSmoke;
        }

        if (enumoptions == EnumOptions.FAST_DEBUG_INFO) {
            this.ofFastDebugInfo = !this.ofFastDebugInfo;
        }

        if (enumoptions == EnumOptions.AUTOSAVE_TICKS) {
            this.ofAutoSaveTicks *= 10;
            if (this.ofAutoSaveTicks > 40000) {
                this.ofAutoSaveTicks = 40;
            }
        }

        if (enumoptions == EnumOptions.BETTER_GRASS) {
            ++this.ofBetterGrass;
            if (this.ofBetterGrass > 3) {
                this.ofBetterGrass = 1;
            }

            this.mc.renderGlobal.loadRenderers();
        }

        if (enumoptions == EnumOptions.WEATHER) {
            this.ofWeather = !this.ofWeather;
        }

        if (enumoptions == EnumOptions.SKY) {
            this.ofSky = !this.ofSky;
        }

        if (enumoptions == EnumOptions.STARS) {
            this.ofStars = !this.ofStars;
        }

        if (enumoptions == EnumOptions.CHUNK_UPDATES) {
            ++this.ofChunkUpdates;
            if (this.ofChunkUpdates > 5) {
                this.ofChunkUpdates = 1;
            }
        }

        if (enumoptions == EnumOptions.CHUNK_UPDATES_DYNAMIC) {
            this.ofChunkUpdatesDynamic = !this.ofChunkUpdatesDynamic;
        }

        if (enumoptions == EnumOptions.FAR_VIEW) {
            this.ofFarView = !this.ofFarView;
            this.mc.renderGlobal.loadRenderers();
        }

        if (enumoptions == EnumOptions.TIME) {
            ++this.ofTime;
            if (this.ofTime > 2) {
                this.ofTime = 0;
            }
        }

        if (enumoptions == EnumOptions.CLEAR_WATER) {
            this.ofClearWater = !this.ofClearWater;
            this.updateWaterOpacity();
        }

        this.saveOptions();
    }

    public float getOptionFloatValue(EnumOptions enumoptions) {
        if (enumoptions == EnumOptions.MUSIC) {
            return this.musicVolume;
        } else if (enumoptions == EnumOptions.SOUND) {
            return this.soundVolume;
        } else if (enumoptions == EnumOptions.SENSITIVITY) {
            return this.mouseSensitivity;
        } else if (enumoptions == EnumOptions.BRIGHTNESS) {
            return this.ofBrightness;
        } else if (enumoptions == EnumOptions.CLOUD_HEIGHT) {
            return this.ofCloudsHeight;
        } else if (enumoptions == EnumOptions.FOV) {
        	return this.fov;
        } else {
            return enumoptions == EnumOptions.AO_LEVEL ? this.ofAoLevel : 0.0F;
        }
    }

    public boolean getOptionOrdinalValue(EnumOptions enumoptions) {
        switch(EnumOptionsMappingHelper.enumOptionsMappingHelperArray[enumoptions.ordinal()]) {
        case 1:
            return this.invertMouse;
        case 2:
            return this.viewBobbing;
        case 3:
            return this.anaglyph;
        case 4:
            return this.advancedOpengl;
        case 5:
            return this.ambientOcclusion;
        default:
            return false;
        }
    }

    public String getKeyBinding(EnumOptions enumoptions) {
        StringTranslate stringtranslate = StringTranslate.getInstance();
        String prefix = stringtranslate.translateKey(enumoptions.getEnumString());
        if (prefix == null) {
            prefix = enumoptions.getEnumString();
        }

        String s = prefix + ": ";
        if (enumoptions.getEnumFloat()) {
            float f = this.getOptionFloatValue(enumoptions);
            if (enumoptions == EnumOptions.SENSITIVITY) {
                if (f == 0.0F) {
                    return s + stringtranslate.translateKey("options.sensitivity.min");
                } else {
                    return f == 1.0F ? s + stringtranslate.translateKey("options.sensitivity.max") : s + (int)(f * 200.0F) + "%";
                }
            } else if(enumoptions == EnumOptions.FOV) {
            	if (f == 70.0F) {
                    return s + stringtranslate.translateKey("FOV");
                } else {
                    return f == 70.0F ? s + stringtranslate.translateKey("Quake Pro") : s + (int)(f * 110.0F) + "";
                }
            }
            
            else {
                return f == 0.0F ? s + stringtranslate.translateKey("options.off") : s + (int)(f * 100.0F) + "%";
            }
        } else if (enumoptions == EnumOptions.ADVANCED_OPENGL) {
            if (!this.advancedOpengl) {
                return s + "OFF";
            } else {
                return this.ofOcclusionFancy ? s + "Fancy" : s + "Fast";
            }
        } else if (enumoptions.getEnumBoolean()) {
            boolean flag = this.getOptionOrdinalValue(enumoptions);
            return flag ? s + stringtranslate.translateKey("options.on") : s + stringtranslate.translateKey("options.off");
        } else if (enumoptions == EnumOptions.RENDER_DISTANCE) {
            return s + stringtranslate.translateKey(RENDER_DISTANCES[this.renderDistance]);
        } else if (enumoptions == EnumOptions.DIFFICULTY) {
            return s + stringtranslate.translateKey(DIFFICULTIES[this.difficulty]);
        } else if (enumoptions == EnumOptions.GUI_SCALE) {
            return s + stringtranslate.translateKey(GUISCALES[this.guiScale]);
        } else if (enumoptions == EnumOptions.FRAMERATE_LIMIT) {
            return this.limitFramerate == 3 ? s + "VSync" : s + StatCollector.translateToLocal(LIMIT_FRAMERATES[this.limitFramerate]);
        } else if (enumoptions == EnumOptions.FOG_FANCY) {
            return this.ofFogFancy ? s + "Fancy" : s + "Fast";
        } else if (enumoptions == EnumOptions.FOG_START) {
            return s + this.ofFogStart;
        } else if (enumoptions == EnumOptions.MIPMAP_LEVEL) {
            return s + this.ofMipmapLevel;
        } else if (enumoptions == EnumOptions.MIPMAP_TYPE) {
            return this.ofMipmapLinear ? s + "Linear" : s + "Nearest";
        } else if (enumoptions == EnumOptions.LOAD_FAR) {
            return this.ofLoadFar ? s + "ON" : s + "OFF";
        } else if (enumoptions == EnumOptions.PRELOADED_CHUNKS) {
            return this.ofPreloadedChunks == 0 ? s + "OFF" : s + this.ofPreloadedChunks;
        } else if (enumoptions == EnumOptions.SMOOTH_FPS) {
            return this.ofSmoothFps ? s + "ON" : s + "OFF";
        } else if (enumoptions == EnumOptions.SMOOTH_INPUT) {
            return this.ofSmoothInput ? s + "ON" : s + "OFF";
        } else if (enumoptions == EnumOptions.CLOUDS) {
            switch(this.ofClouds) {
            case 1:
                return s + "Fast";
            case 2:
                return s + "Fancy";
            case 3:
                return s + "OFF";
            default:
                return s + "Default";
            }
        } else if (enumoptions == EnumOptions.TREES) {
            switch(this.ofTrees) {
            case 1:
                return s + "Fast";
            case 2:
                return s + "Fancy";
            default:
                return s + "Default";
            }
        } else if (enumoptions == EnumOptions.GRASS) {
            switch(this.ofGrass) {
            case 1:
                return s + "Fast";
            case 2:
                return s + "Fancy";
            default:
                return s + "Default";
            }
        } else if (enumoptions == EnumOptions.RAIN) {
            switch(this.ofRain) {
            case 1:
                return s + "Fast";
            case 2:
                return s + "Fancy";
            case 3:
                return s + "OFF";
            default:
                return s + "Default";
            }
        } else if (enumoptions == EnumOptions.WATER) {
            switch(this.ofWater) {
            case 1:
                return s + "Fast";
            case 2:
                return s + "Fancy";
            case 3:
                return s + "OFF";
            default:
                return s + "Default";
            }
        } else if (enumoptions == EnumOptions.ANIMATED_WATER) {
            switch(this.ofAnimatedWater) {
            case 1:
                return s + "Dynamic";
            case 2:
                return s + "OFF";
            default:
                return s + "ON";
            }
        } else if (enumoptions == EnumOptions.ANIMATED_LAVA) {
            switch(this.ofAnimatedLava) {
            case 1:
                return s + "Dynamic";
            case 2:
                return s + "OFF";
            default:
                return s + "ON";
            }
        } else if (enumoptions == EnumOptions.ANIMATED_FIRE) {
            return this.ofAnimatedFire ? s + "ON" : s + "OFF";
        } else if (enumoptions == EnumOptions.ANIMATED_PORTAL) {
            return this.ofAnimatedPortal ? s + "ON" : s + "OFF";
        } else if (enumoptions == EnumOptions.ANIMATED_REDSTONE) {
            return this.ofAnimatedRedstone ? s + "ON" : s + "OFF";
        } else if (enumoptions == EnumOptions.ANIMATED_EXPLOSION) {
            return this.ofAnimatedExplosion ? s + "ON" : s + "OFF";
        } else if (enumoptions == EnumOptions.ANIMATED_FLAME) {
            return this.ofAnimatedFlame ? s + "ON" : s + "OFF";
        } else if (enumoptions == EnumOptions.ANIMATED_SMOKE) {
            return this.ofAnimatedSmoke ? s + "ON" : s + "OFF";
        } else if (enumoptions == EnumOptions.FAST_DEBUG_INFO) {
            return this.ofFastDebugInfo ? s + "ON" : s + "OFF";
        } else if (enumoptions == EnumOptions.AUTOSAVE_TICKS) {
            if (this.ofAutoSaveTicks <= 40) {
                return s + "Default (2s)";
            } else if (this.ofAutoSaveTicks <= 400) {
                return s + "20s";
            } else {
                return this.ofAutoSaveTicks <= 4000 ? s + "3min" : s + "30min";
            }
        } else if (enumoptions == EnumOptions.BETTER_GRASS) {
            switch(this.ofBetterGrass) {
            case 1:
                return s + "Fast";
            case 2:
                return s + "Fancy";
            default:
                return s + "OFF";
            }
        } else if (enumoptions == EnumOptions.WEATHER) {
            return this.ofWeather ? s + "ON" : s + "OFF";
        } else if (enumoptions == EnumOptions.SKY) {
            return this.ofSky ? s + "ON" : s + "OFF";
        } else if (enumoptions == EnumOptions.STARS) {
            return this.ofStars ? s + "ON" : s + "OFF";
        } else if (enumoptions == EnumOptions.CHUNK_UPDATES) {
            return s + this.ofChunkUpdates;
        } else if (enumoptions == EnumOptions.CHUNK_UPDATES_DYNAMIC) {
            return this.ofChunkUpdatesDynamic ? s + "ON" : s + "OFF";
        } else if (enumoptions == EnumOptions.FAR_VIEW) {
            return this.ofFarView ? s + "ON" : s + "OFF";
        } else if (enumoptions == EnumOptions.TIME) {
            if (this.ofTime == 1) {
                return s + "Day Only";
            } else {
                return this.ofTime == 2 ? s + "Night Only" : s + "Default";
            }
        } else if (enumoptions == EnumOptions.CLEAR_WATER) {
            return this.ofClearWater ? s + "ON" : s + "OFF";
        } else if (enumoptions == EnumOptions.GRAPHICS) {
            return this.fancyGraphics ? s + stringtranslate.translateKey("options.graphics.fancy") : s + stringtranslate.translateKey("options.graphics.fast");
        } else {
            return s;
        }
    }

    public void loadOptions() {
        try {
            if (!this.optionsFile.exists()) {
                return;
            }

            BufferedReader bufferedreader = new BufferedReader(new FileReader(this.optionsFile));
            String s = "";

            while((s = bufferedreader.readLine()) != null) {
                try {
                    String[] as = s.split(":");
                    if (as[0].equals("music")) {
                        this.musicVolume = this.parseFloat(as[1]);
                    }

                    if (as[0].equals("sound")) {
                        this.soundVolume = this.parseFloat(as[1]);
                    }

                    if (as[0].equals("mouseSensitivity")) {
                        this.mouseSensitivity = this.parseFloat(as[1]);
                    }
                    
                    if(as[0].equals("fov")) {
                    	this.fov = this.parseFloat(as[1]);
                    }

                    if (as[0].equals("invertYMouse")) {
                        this.invertMouse = as[1].equals("true");
                    }

                    if (as[0].equals("viewDistance")) {
                        this.renderDistance = Integer.parseInt(as[1]);
                    }

                    if (as[0].equals("guiScale")) {
                        this.guiScale = Integer.parseInt(as[1]);
                    }

                    if (as[0].equals("bobView")) {
                        this.viewBobbing = as[1].equals("true");
                    }

                    if (as[0].equals("anaglyph3d")) {
                        this.anaglyph = as[1].equals("true");
                    }

                    if (as[0].equals("advancedOpengl")) {
                        this.advancedOpengl = as[1].equals("true");
                    }

                    if (as[0].equals("fpsLimit")) {
                        this.limitFramerate = Integer.parseInt(as[1]);
                        Display.setVSyncEnabled(this.limitFramerate == 3);
                    }

                    if (as[0].equals("difficulty")) {
                        this.difficulty = Integer.parseInt(as[1]);
                    }

                    if (as[0].equals("fancyGraphics")) {
                        this.fancyGraphics = as[1].equals("true");
                    }

                    if (as[0].equals("ao")) {
                        this.ambientOcclusion = as[1].equals("true");
                        if (this.ambientOcclusion) {
                            this.ofAoLevel = 1.0F;
                        } else {
                            this.ofAoLevel = 0.0F;
                        }
                    }

                    if (as[0].equals("skin")) {
                        this.skin = as[1];
                    }

                    if (as[0].equals("lastServer") && as.length >= 2) {
                        this.lastServer = as[1];
                    }

                    for(int i = 0; i < this.keyBindings.length; ++i) {
                        if (as[0].equals("key_" + this.keyBindings[i].keyDescription)) {
                            this.keyBindings[i].keyCode = Integer.parseInt(as[1]);
                        }
                    }

                    if (as[0].equals("ofFogFancy") && as.length >= 2) {
                        this.ofFogFancy = as[1].equals("true");
                    }

                    if (as[0].equals("ofFogStart") && as.length >= 2) {
                        this.ofFogStart = Float.valueOf(as[1]);
                        if (this.ofFogStart < 0.2F) {
                            this.ofFogStart = 0.2F;
                        }

                        if (this.ofFogStart > 0.81F) {
                            this.ofFogStart = 0.8F;
                        }
                    }

                    if (as[0].equals("ofMipmapLevel") && as.length >= 2) {
                        this.ofMipmapLevel = Integer.valueOf(as[1]);
                        if (this.ofMipmapLevel < 0) {
                            this.ofMipmapLevel = 0;
                        }

                        if (this.ofMipmapLevel > 4) {
                            this.ofMipmapLevel = 4;
                        }
                    }

                    if (as[0].equals("ofMipmapLinear") && as.length >= 2) {
                        this.ofMipmapLinear = Boolean.valueOf(as[1]);
                    }

                    if (as[0].equals("ofLoadFar") && as.length >= 2) {
                        this.ofLoadFar = Boolean.valueOf(as[1]);
                    }

                    if (as[0].equals("ofPreloadedChunks") && as.length >= 2) {
                        this.ofPreloadedChunks = Integer.valueOf(as[1]);
                        if (this.ofPreloadedChunks < 0) {
                            this.ofPreloadedChunks = 0;
                        }

                        if (this.ofPreloadedChunks > 8) {
                            this.ofPreloadedChunks = 8;
                        }
                    }

                    if (as[0].equals("ofOcclusionFancy") && as.length >= 2) {
                        this.ofOcclusionFancy = Boolean.valueOf(as[1]);
                    }

                    if (as[0].equals("ofSmoothFps") && as.length >= 2) {
                        this.ofSmoothFps = Boolean.valueOf(as[1]);
                    }

                    if (as[0].equals("ofSmoothInput") && as.length >= 2) {
                        this.ofSmoothInput = Boolean.valueOf(as[1]);
                    }

                    if (as[0].equals("ofBrightness") && as.length >= 2) {
                        this.ofBrightness = Float.valueOf(as[1]);
                        this.ofBrightness = Config.limit(this.ofBrightness, 0.0F, 1.0F);
                        this.updateWorldLightLevels();
                    }

                    if (as[0].equals("ofAoLevel") && as.length >= 2) {
                        this.ofAoLevel = Float.valueOf(as[1]);
                        this.ofAoLevel = Config.limit(this.ofAoLevel, 0.0F, 1.0F);
                        this.ambientOcclusion = this.ofAoLevel > 0.0F;
                    }

                    if (as[0].equals("ofClouds") && as.length >= 2) {
                        this.ofClouds = Integer.valueOf(as[1]);
                        this.ofClouds = Config.limit(this.ofClouds, 0, 3);
                    }

                    if (as[0].equals("ofCloudsHeight") && as.length >= 2) {
                        this.ofCloudsHeight = Float.valueOf(as[1]);
                        this.ofCloudsHeight = Config.limit(this.ofCloudsHeight, 0.0F, 1.0F);
                    }

                    if (as[0].equals("ofTrees") && as.length >= 2) {
                        this.ofTrees = Integer.valueOf(as[1]);
                        this.ofTrees = Config.limit(this.ofTrees, 0, 2);
                    }

                    if (as[0].equals("ofGrass") && as.length >= 2) {
                        this.ofGrass = Integer.valueOf(as[1]);
                        this.ofGrass = Config.limit(this.ofGrass, 0, 2);
                    }

                    if (as[0].equals("ofRain") && as.length >= 2) {
                        this.ofRain = Integer.valueOf(as[1]);
                        this.ofRain = Config.limit(this.ofRain, 0, 3);
                    }

                    if (as[0].equals("ofWater") && as.length >= 2) {
                        this.ofWater = Integer.valueOf(as[1]);
                        this.ofWater = Config.limit(this.ofWater, 0, 3);
                    }

                    if (as[0].equals("ofAnimatedWater") && as.length >= 2) {
                        this.ofAnimatedWater = Integer.valueOf(as[1]);
                        this.ofAnimatedWater = Config.limit(this.ofAnimatedWater, 0, 2);
                    }

                    if (as[0].equals("ofAnimatedLava") && as.length >= 2) {
                        this.ofAnimatedLava = Integer.valueOf(as[1]);
                        this.ofAnimatedLava = Config.limit(this.ofAnimatedLava, 0, 2);
                    }

                    if (as[0].equals("ofAnimatedFire") && as.length >= 2) {
                        this.ofAnimatedFire = Boolean.valueOf(as[1]);
                    }

                    if (as[0].equals("ofAnimatedPortal") && as.length >= 2) {
                        this.ofAnimatedPortal = Boolean.valueOf(as[1]);
                    }

                    if (as[0].equals("ofAnimatedRedstone") && as.length >= 2) {
                        this.ofAnimatedRedstone = Boolean.valueOf(as[1]);
                    }

                    if (as[0].equals("ofAnimatedExplosion") && as.length >= 2) {
                        this.ofAnimatedExplosion = Boolean.valueOf(as[1]);
                    }

                    if (as[0].equals("ofAnimatedFlame") && as.length >= 2) {
                        this.ofAnimatedFlame = Boolean.valueOf(as[1]);
                    }

                    if (as[0].equals("ofAnimatedSmoke") && as.length >= 2) {
                        this.ofAnimatedSmoke = Boolean.valueOf(as[1]);
                    }

                    if (as[0].equals("ofFastDebugInfo") && as.length >= 2) {
                        this.ofFastDebugInfo = Boolean.valueOf(as[1]);
                    }

                    if (as[0].equals("ofAutoSaveTicks") && as.length >= 2) {
                        this.ofAutoSaveTicks = Integer.valueOf(as[1]);
                        this.ofAutoSaveTicks = Config.limit(this.ofAutoSaveTicks, 40, 40000);
                    }

                    if (as[0].equals("ofBetterGrass") && as.length >= 2) {
                        this.ofBetterGrass = Integer.valueOf(as[1]);
                        this.ofBetterGrass = Config.limit(this.ofBetterGrass, 1, 3);
                    }

                    if (as[0].equals("ofWeather") && as.length >= 2) {
                        this.ofWeather = Boolean.valueOf(as[1]);
                    }

                    if (as[0].equals("ofSky") && as.length >= 2) {
                        this.ofSky = Boolean.valueOf(as[1]);
                    }

                    if (as[0].equals("ofStars") && as.length >= 2) {
                        this.ofStars = Boolean.valueOf(as[1]);
                    }

                    if (as[0].equals("ofChunkUpdates") && as.length >= 2) {
                        this.ofChunkUpdates = Integer.valueOf(as[1]);
                        this.ofChunkUpdates = Config.limit(this.ofChunkUpdates, 1, 5);
                    }

                    if (as[0].equals("ofChunkUpdatesDynamic") && as.length >= 2) {
                        this.ofChunkUpdatesDynamic = Boolean.valueOf(as[1]);
                    }

                    if (as[0].equals("ofFarView") && as.length >= 2) {
                        this.ofFarView = Boolean.valueOf(as[1]);
                    }

                    if (as[0].equals("ofTime") && as.length >= 2) {
                        this.ofTime = Integer.valueOf(as[1]);
                        this.ofTime = Config.limit(this.ofTime, 0, 2);
                    }

                    if (as[0].equals("ofClearWater") && as.length >= 2) {
                        this.ofClearWater = Boolean.valueOf(as[1]);
                        this.updateWaterOpacity();
                    }
                } catch (Exception var5) {
                    System.out.println("Skipping bad option: " + s);
                }
            }

            bufferedreader.close();
        } catch (Exception var6) {
            System.out.println("Failed to load options");
            var6.printStackTrace();
        }

    }

    private float parseFloat(String s) {
        if (s.equals("true")) {
            return 1.0F;
        } else {
            return s.equals("false") ? 0.0F : Float.parseFloat(s);
        }
    }

    public void saveOptions() {
        try {
            PrintWriter printwriter = new PrintWriter(new FileWriter(this.optionsFile));
            printwriter.println("music:" + this.musicVolume);
            printwriter.println("sound:" + this.soundVolume);
            printwriter.println("invertYMouse:" + this.invertMouse);
            printwriter.println("mouseSensitivity:" + this.mouseSensitivity);
            printwriter.println("fov: "+this.fov);
            printwriter.println("viewDistance:" + this.renderDistance);
            printwriter.println("guiScale:" + this.guiScale);
            printwriter.println("bobView:" + this.viewBobbing);
            printwriter.println("anaglyph3d:" + this.anaglyph);
            printwriter.println("advancedOpengl:" + this.advancedOpengl);
            printwriter.println("fpsLimit:" + this.limitFramerate);
            printwriter.println("difficulty:" + this.difficulty);
            printwriter.println("fancyGraphics:" + this.fancyGraphics);
            printwriter.println("ao:" + this.ambientOcclusion);
            printwriter.println("skin:" + this.skin);
            printwriter.println("lastServer:" + this.lastServer);

            for(int i = 0; i < this.keyBindings.length; ++i) {
                printwriter.println("key_" + this.keyBindings[i].keyDescription + ":" + this.keyBindings[i].keyCode);
            }

            printwriter.println("ofFogFancy:" + this.ofFogFancy);
            printwriter.println("ofFogStart:" + this.ofFogStart);
            printwriter.println("ofMipmapLevel:" + this.ofMipmapLevel);
            printwriter.println("ofMipmapLinear:" + this.ofMipmapLinear);
            printwriter.println("ofLoadFar:" + this.ofLoadFar);
            printwriter.println("ofPreloadedChunks:" + this.ofPreloadedChunks);
            printwriter.println("ofOcclusionFancy:" + this.ofOcclusionFancy);
            printwriter.println("ofSmoothFps:" + this.ofSmoothFps);
            printwriter.println("ofSmoothInput:" + this.ofSmoothInput);
            printwriter.println("ofBrightness:" + this.ofBrightness);
            printwriter.println("ofAoLevel:" + this.ofAoLevel);
            printwriter.println("ofClouds:" + this.ofClouds);
            printwriter.println("ofCloudsHeight:" + this.ofCloudsHeight);
            printwriter.println("ofTrees:" + this.ofTrees);
            printwriter.println("ofGrass:" + this.ofGrass);
            printwriter.println("ofRain:" + this.ofRain);
            printwriter.println("ofWater:" + this.ofWater);
            printwriter.println("ofAnimatedWater:" + this.ofAnimatedWater);
            printwriter.println("ofAnimatedLava:" + this.ofAnimatedLava);
            printwriter.println("ofAnimatedFire:" + this.ofAnimatedFire);
            printwriter.println("ofAnimatedPortal:" + this.ofAnimatedPortal);
            printwriter.println("ofAnimatedRedstone:" + this.ofAnimatedRedstone);
            printwriter.println("ofAnimatedExplosion:" + this.ofAnimatedExplosion);
            printwriter.println("ofAnimatedFlame:" + this.ofAnimatedFlame);
            printwriter.println("ofAnimatedSmoke:" + this.ofAnimatedSmoke);
            printwriter.println("ofFastDebugInfo:" + this.ofFastDebugInfo);
            printwriter.println("ofAutoSaveTicks:" + this.ofAutoSaveTicks);
            printwriter.println("ofBetterGrass:" + this.ofBetterGrass);
            printwriter.println("ofWeather:" + this.ofWeather);
            printwriter.println("ofSky:" + this.ofSky);
            printwriter.println("ofStars:" + this.ofStars);
            printwriter.println("ofChunkUpdates:" + this.ofChunkUpdates);
            printwriter.println("ofChunkUpdatesDynamic:" + this.ofChunkUpdatesDynamic);
            printwriter.println("ofFarView:" + this.ofFarView);
            printwriter.println("ofTime:" + this.ofTime);
            printwriter.println("ofClearWater:" + this.ofClearWater);
            printwriter.close();
        } catch (Exception var3) {
            System.out.println("Failed to save options");
            var3.printStackTrace();
        }

    }
}
