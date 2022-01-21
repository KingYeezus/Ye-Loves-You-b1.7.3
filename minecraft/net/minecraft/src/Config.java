package net.minecraft.src;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Drawable;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.opengl.Pbuffer;
import org.lwjgl.opengl.PixelFormat;

public class Config {
    private static GameSettings gameSettings = null;
    private static Minecraft minecraft = null;
    private static float[] lightLevels = null;
    private static int iconWidthTerrain = 16;
    private static int iconWidthItems = 16;
    private static Map foundClassesMap = new HashMap();
    private static boolean fontRendererUpdated = false;
    private static UpdateThread updateThread = null;
    private static File logFile = null;
    public static final Boolean DEF_FOG_FANCY = true;
    public static final Float DEF_FOG_START = 0.2F;
    public static final Boolean DEF_OPTIMIZE_RENDER_DISTANCE = false;
    public static final Boolean DEF_OCCLUSION_ENABLED = false;
    public static final Integer DEF_MIPMAP_LEVEL = 0;
    public static final Integer DEF_MIPMAP_TYPE = 9984 /*GL_NEAREST_MIPMAP_NEAREST*/;
    public static final Float DEF_ALPHA_FUNC_LEVEL = 0.1F;
    public static final Boolean DEF_LOAD_CHUNKS_FAR = false;
    public static final Integer DEF_PRELOADED_CHUNKS = 0;
    public static final Integer DEF_CHUNKS_LIMIT = 25;
    public static final Integer DEF_UPDATES_PER_FRAME = 3;
    public static final Boolean DEF_DYNAMIC_UPDATES = false;

    private Config() {
    }

    private static String getVersion() {
        return "OptiFine_1.7.3_HD_MT_G";
    }

    private static void checkOpenGlCaps() {
        log("");
        log(getVersion());
        log("" + new Date());
        log("OS: " + System.getProperty("os.name") + " (" + System.getProperty("os.arch") + ") version " + System.getProperty("os.version"));
        log("Java: " + System.getProperty("java.version") + ", " + System.getProperty("java.vendor"));
        log("VM: " + System.getProperty("java.vm.name") + " (" + System.getProperty("java.vm.info") + "), " + System.getProperty("java.vm.vendor"));
        log("LWJGL: " + Sys.getVersion());
        log("OpenGL: " + GL11.glGetString(7937 /*GL_RENDERER*/) + " version " + GL11.glGetString(7938 /*GL_VERSION*/) + ", " + GL11.glGetString(7936 /*GL_VENDOR*/));
        int ver = getOpenGlVersion();
        String verStr = "" + ver / 10 + "." + ver % 10;
        log("OpenGL Version: " + verStr);
        if (!GLContext.getCapabilities().OpenGL12) {
            log("OpenGL Mipmap levels: Not available (GL12.GL_TEXTURE_MAX_LEVEL)");
        }

        if (!GLContext.getCapabilities().GL_NV_fog_distance) {
            log("OpenGL Fancy fog: Not available (GL_NV_fog_distance)");
        }

        if (!GLContext.getCapabilities().GL_ARB_occlusion_query) {
            log("OpenGL Occlussion culling: Not available (GL_ARB_occlusion_query)");
        }

    }

    public static boolean isFancyFogAvailable() {
        return GLContext.getCapabilities().GL_NV_fog_distance;
    }

    public static boolean isOcclusionAvailable() {
        return GLContext.getCapabilities().GL_ARB_occlusion_query;
    }

    private static int getOpenGlVersion() {
        if (!GLContext.getCapabilities().OpenGL11) {
            return 10;
        } else if (!GLContext.getCapabilities().OpenGL12) {
            return 11;
        } else if (!GLContext.getCapabilities().OpenGL13) {
            return 12;
        } else if (!GLContext.getCapabilities().OpenGL14) {
            return 13;
        } else if (!GLContext.getCapabilities().OpenGL15) {
            return 14;
        } else if (!GLContext.getCapabilities().OpenGL20) {
            return 15;
        } else if (!GLContext.getCapabilities().OpenGL21) {
            return 20;
        } else if (!GLContext.getCapabilities().OpenGL30) {
            return 21;
        } else if (!GLContext.getCapabilities().OpenGL31) {
            return 30;
        } else if (!GLContext.getCapabilities().OpenGL32) {
            return 31;
        } else if (!GLContext.getCapabilities().OpenGL33) {
            return 32;
        } else {
            return !GLContext.getCapabilities().OpenGL40 ? 33 : 40;
        }
    }

    public static void setGameSettings(GameSettings options) {
        if (gameSettings == null) {
            checkOpenGlCaps();
        }

        gameSettings = options;
    }

    public static boolean isUseMipmaps() {
        int mipmapLevel = getMipmapLevel();
        return mipmapLevel > 0;
    }

    public static int getMipmapLevel() {
        return gameSettings == null ? DEF_MIPMAP_LEVEL : gameSettings.ofMipmapLevel;
    }

    public static int getMipmapType() {
        if (gameSettings == null) {
            return DEF_MIPMAP_TYPE;
        } else {
            return gameSettings.ofMipmapLinear ? 9986 /*GL_NEAREST_MIPMAP_LINEAR*/ : 9984 /*GL_NEAREST_MIPMAP_NEAREST*/;
        }
    }

    public static boolean isUseAlphaFunc() {
        float alphaFuncLevel = getAlphaFuncLevel();
        return alphaFuncLevel > DEF_ALPHA_FUNC_LEVEL + 1.0E-5F;
    }

    public static float getAlphaFuncLevel() {
        return DEF_ALPHA_FUNC_LEVEL;
    }

    public static boolean isFogFancy() {
        if (!GLContext.getCapabilities().GL_NV_fog_distance) {
            return false;
        } else {
            return gameSettings == null ? false : gameSettings.ofFogFancy;
        }
    }

    public static float getFogStart() {
        return gameSettings == null ? DEF_FOG_START : gameSettings.ofFogStart;
    }

    public static boolean isOcclusionEnabled() {
        return gameSettings == null ? DEF_OCCLUSION_ENABLED : gameSettings.advancedOpengl;
    }

    public static boolean isOcclusionFancy() {
        if (!isOcclusionEnabled()) {
            return false;
        } else {
            return gameSettings == null ? false : gameSettings.ofOcclusionFancy;
        }
    }

    public static boolean isLoadChunksFar() {
        return gameSettings == null ? DEF_LOAD_CHUNKS_FAR : gameSettings.ofLoadFar;
    }

    public static int getPreloadedChunks() {
        return gameSettings == null ? DEF_PRELOADED_CHUNKS : gameSettings.ofPreloadedChunks;
    }

    public static void dbg(String s) {
        System.out.println(s);
    }

    public static void log(String s) {
        dbg(s);

        try {
            if (logFile == null) {
                logFile = new File(Minecraft.getMinecraftDir(), "optifog.log");
                logFile.delete();
                logFile.createNewFile();
            }

            FileOutputStream fos = new FileOutputStream(logFile, true);
            OutputStreamWriter logFileWriter = new OutputStreamWriter(fos, "ASCII");

            try {
                logFileWriter.write(s);
                logFileWriter.write("\n");
                logFileWriter.flush();
            } finally {
                logFileWriter.close();
            }
        } catch (IOException var7) {
            var7.printStackTrace();
        }

    }

    public static int getUpdatesPerFrame() {
        return gameSettings != null ? gameSettings.ofChunkUpdates : 1;
    }

    public static boolean isDynamicUpdates() {
        return gameSettings != null ? gameSettings.ofChunkUpdatesDynamic : true;
    }

    public static boolean isRainFancy() {
        if (gameSettings.ofRain == 0) {
            return gameSettings.fancyGraphics;
        } else {
            return gameSettings.ofRain == 2;
        }
    }

    public static boolean isWaterFancy() {
        if (gameSettings.ofWater == 0) {
            return gameSettings.fancyGraphics;
        } else {
            return gameSettings.ofWater == 2;
        }
    }

    public static boolean isRainOff() {
        return gameSettings.ofRain == 3;
    }

    public static boolean isCloudsFancy() {
        if (gameSettings.ofClouds == 0) {
            return gameSettings.fancyGraphics;
        } else {
            return gameSettings.ofClouds == 2;
        }
    }

    public static boolean isCloudsOff() {
        return gameSettings.ofClouds == 3;
    }

    public static boolean isTreesFancy() {
        if (gameSettings.ofTrees == 0) {
            return gameSettings.fancyGraphics;
        } else {
            return gameSettings.ofTrees == 2;
        }
    }

    public static boolean isGrassFancy() {
        if (gameSettings.ofGrass == 0) {
            return gameSettings.fancyGraphics;
        } else {
            return gameSettings.ofGrass == 2;
        }
    }

    public static int limit(int val, int min, int max) {
        if (val < min) {
            return min;
        } else {
            return val > max ? max : val;
        }
    }

    public static float limit(float val, float min, float max) {
        if (val < min) {
            return min;
        } else {
            return val > max ? max : val;
        }
    }

    public static boolean isAnimatedWater() {
        if (gameSettings != null) {
            return gameSettings.ofAnimatedWater != 2;
        } else {
            return true;
        }
    }

    public static boolean isGeneratedWater() {
        if (gameSettings != null) {
            return gameSettings.ofAnimatedWater == 1;
        } else {
            return true;
        }
    }

    public static boolean isAnimatedPortal() {
        return gameSettings != null ? gameSettings.ofAnimatedPortal : true;
    }

    public static boolean isAnimatedLava() {
        if (gameSettings != null) {
            return gameSettings.ofAnimatedLava != 2;
        } else {
            return true;
        }
    }

    public static boolean isGeneratedLava() {
        if (gameSettings != null) {
            return gameSettings.ofAnimatedLava == 1;
        } else {
            return true;
        }
    }

    public static boolean isAnimatedFire() {
        return gameSettings != null ? gameSettings.ofAnimatedFire : true;
    }

    public static boolean isAnimatedRedstone() {
        return gameSettings != null ? gameSettings.ofAnimatedRedstone : true;
    }

    public static boolean isAnimatedExplosion() {
        return gameSettings != null ? gameSettings.ofAnimatedExplosion : true;
    }

    public static boolean isAnimatedFlame() {
        return gameSettings != null ? gameSettings.ofAnimatedFlame : true;
    }

    public static boolean isAnimatedSmoke() {
        return gameSettings != null ? gameSettings.ofAnimatedSmoke : true;
    }

    public static float getAmbientOcclusionLevel() {
        return gameSettings != null ? gameSettings.ofAoLevel : 0.0F;
    }

    public static float fixAoLight(float light, float defLight) {
        if (lightLevels == null) {
            return light;
        } else {
            float level_0 = lightLevels[0];
            float level_1 = lightLevels[1];
            if (light > level_0) {
                return light;
            } else if (defLight <= level_1) {
                return light;
            } else {
                float mul = 1.0F - getAmbientOcclusionLevel();
                return light + (defLight - light) * mul;
            }
        }
    }

    public static void setLightLevels(float[] levels) {
        lightLevels = levels;
    }

    public static boolean callBoolean(String className, String methodName, Object[] params) {
        try {
            Class cls = getClass(className);
            if (cls == null) {
                return false;
            } else {
                Method method = getMethod(cls, methodName, params);
                if (method == null) {
                    return false;
                } else {
                    Boolean retVal = (Boolean)method.invoke((Object)null, params);
                    return retVal;
                }
            }
        } catch (Throwable var6) {
            var6.printStackTrace();
            return false;
        }
    }

    public static void callVoid(String className, String methodName, Object[] params) {
        try {
            Class cls = getClass(className);
            if (cls == null) {
                return;
            }

            Method method = getMethod(cls, methodName, params);
            if (method == null) {
                return;
            }

            method.invoke((Object)null, params);
        } catch (Throwable var5) {
            var5.printStackTrace();
        }

    }

    public static void callVoid(Object obj, String methodName, Object[] params) {
        try {
            if (obj == null) {
                return;
            }

            Class cls = obj.getClass();
            if (cls == null) {
                return;
            }

            Method method = getMethod(cls, methodName, params);
            if (method == null) {
                return;
            }

            method.invoke(obj, params);
        } catch (Throwable var5) {
            var5.printStackTrace();
        }

    }

    public static Object getFieldValue(String className, String fieldName) {
        try {
            Class cls = getClass(className);
            if (cls == null) {
                return null;
            } else {
                Field field = cls.getDeclaredField(fieldName);
                if (field == null) {
                    return null;
                } else {
                    Object value = field.get((Object)null);
                    return value;
                }
            }
        } catch (Throwable var5) {
            var5.printStackTrace();
            return null;
        }
    }

    public static Object getFieldValue(Object obj, String fieldName) {
        try {
            if (obj == null) {
                return null;
            } else {
                Class cls = obj.getClass();
                if (cls == null) {
                    return null;
                } else {
                    Field field = cls.getField(fieldName);
                    if (field == null) {
                        return null;
                    } else {
                        Object value = field.get(obj);
                        return value;
                    }
                }
            }
        } catch (Throwable var5) {
            var5.printStackTrace();
            return null;
        }
    }

    private static Method getMethod(Class cls, String methodName, Object[] params) {
        Method[] methods = cls.getMethods();

        for(int i = 0; i < methods.length; ++i) {
            Method m = methods[i];
            if (m.getName().equals(methodName) && m.getParameterTypes().length == params.length) {
                return m;
            }
        }

        dbg("No method found for: " + cls.getName() + "." + methodName + "(" + arrayToString(params) + ")");
        return null;
    }

    public static String arrayToString(Object[] arr) {
        StringBuffer buf = new StringBuffer(arr.length * 5);

        for(int i = 0; i < arr.length; ++i) {
            Object obj = arr[i];
            if (i > 0) {
                buf.append(", ");
            }

            buf.append(String.valueOf(obj));
        }

        return buf.toString();
    }

    public static boolean hasModLoader() {
        Class cls = getClass("ModLoader");
        return cls != null;
    }

    private static Class getClass(String className) {
        Class cls = (Class)foundClassesMap.get(className);
        if (cls != null) {
            return cls;
        } else if (foundClassesMap.containsKey(className)) {
            return null;
        } else {
            try {
                cls = Class.forName(className);
            } catch (ClassNotFoundException var3) {
                log("Class not found: " + className);
            } catch (Throwable var4) {
                var4.printStackTrace();
            }

            foundClassesMap.put(className, cls);
            return cls;
        }
    }

    public static void setMinecraft(Minecraft mc) {
        minecraft = mc;
    }

    public static Minecraft getMinecraft() {
        return minecraft;
    }

    public static int getIconWidthTerrain() {
        return iconWidthTerrain;
    }

    public static int getIconWidthItems() {
        return iconWidthItems;
    }

    public static void setIconWidthItems(int iconWidth) {
        iconWidthItems = iconWidth;
    }

    public static void setIconWidthTerrain(int iconWidth) {
        iconWidthTerrain = iconWidth;
    }

    public static int getMaxDynamicTileWidth() {
        return 64;
    }

    public static int getSideGrassTexture(IBlockAccess blockAccess, int x, int y, int z, int side) {
        if (!isBetterGrass()) {
            return 3;
        } else {
            if (isBetterGrassFancy()) {
                --y;
                switch(side) {
                case 2:
                    --z;
                    break;
                case 3:
                    ++z;
                    break;
                case 4:
                    --x;
                    break;
                case 5:
                    ++x;
                }

                int blockId = blockAccess.getBlockId(x, y, z);
                if (blockId != 2) {
                    return 3;
                }
            }

            return 0;
        }
    }

    public static int getSideSnowGrassTexture(IBlockAccess blockAccess, int x, int y, int z, int side) {
        if (!isBetterGrass()) {
            return 68;
        } else {
            if (isBetterGrassFancy()) {
                switch(side) {
                case 2:
                    --z;
                    break;
                case 3:
                    ++z;
                    break;
                case 4:
                    --x;
                    break;
                case 5:
                    ++x;
                }

                int blockId = blockAccess.getBlockId(x, y, z);
                if (blockId != 78 && blockId != 80) {
                    return 68;
                }
            }

            return 66;
        }
    }

    public static boolean isBetterGrass() {
        if (gameSettings == null) {
            return false;
        } else {
            return gameSettings.ofBetterGrass != 3;
        }
    }

    public static boolean isBetterGrassFancy() {
        if (gameSettings == null) {
            return false;
        } else {
            return gameSettings.ofBetterGrass == 2;
        }
    }

    public static boolean isFontRendererUpdated() {
        return fontRendererUpdated;
    }

    public static void setFontRendererUpdated(boolean fontRendererUpdated) {
        Config.fontRendererUpdated = fontRendererUpdated;
    }

    public static boolean isWeatherEnabled() {
        return gameSettings == null ? true : gameSettings.ofWeather;
    }

    public static boolean isSkyEnabled() {
        return gameSettings == null ? true : gameSettings.ofSky;
    }

    public static boolean isStarsEnabled() {
        return gameSettings == null ? true : gameSettings.ofStars;
    }

    public static UpdateThread getUpdateThread() {
        return updateThread;
    }

    public static UpdateThread createUpdateThread(Drawable displayDrawable) {
        if (updateThread != null) {
            throw new IllegalStateException("UpdateThread is already existing");
        } else {
            try {
                Pbuffer pbuffer = new Pbuffer(1, 1, new PixelFormat(), displayDrawable);
                updateThread = new UpdateThread(pbuffer);
                updateThread.setPriority(1);
                updateThread.start();
                return updateThread;
            } catch (Exception var2) {
                throw new RuntimeException(var2);
            }
        }
    }

    public static boolean isUpdateThread() {
        return Thread.currentThread() == updateThread;
    }

    public static boolean isBackgroundChunkLoading() {
        return true;
    }

    public static boolean isFarView() {
        return gameSettings == null ? false : gameSettings.ofFarView;
    }

    public static void sleep(long ms) {
        try {
            Thread.currentThread();
            Thread.sleep(ms);
        } catch (InterruptedException var3) {
            var3.printStackTrace();
        }

    }

    public static boolean isTimeDayOnly() {
        if (gameSettings == null) {
            return false;
        } else {
            return gameSettings.ofTime == 1;
        }
    }

    public static boolean isTimeNightOnly() {
        if (gameSettings == null) {
            return false;
        } else {
            return gameSettings.ofTime == 2;
        }
    }

    public static boolean isClearWater() {
        return gameSettings == null ? false : gameSettings.ofClearWater;
    }
}
