package net.minecraft.src;

public enum EnumOptions {
    MUSIC("MUSIC", 0, "options.music", true, false),
    SOUND("SOUND", 1, "options.sound", true, false),
    INVERT_MOUSE("INVERT_MOUSE", 2, "options.invertMouse", false, true),
    SENSITIVITY("SENSITIVITY", 3, "options.sensitivity", true, false),
    RENDER_DISTANCE("RENDER_DISTANCE", 4, "options.renderDistance", false, false),
    VIEW_BOBBING("VIEW_BOBBING", 5, "options.viewBobbing", false, true),
    ANAGLYPH("ANAGLYPH", 6, "options.anaglyph", false, true),
    ADVANCED_OPENGL("ADVANCED_OPENGL", 7, "options.advancedOpengl", false, true),
    FRAMERATE_LIMIT("FRAMERATE_LIMIT", 8, "options.framerateLimit", false, false),
    DIFFICULTY("DIFFICULTY", 9, "options.difficulty", false, false),
    GRAPHICS("GRAPHICS", 10, "options.graphics", false, false),
    AMBIENT_OCCLUSION("AMBIENT_OCCLUSION", 11, "options.ao", false, true),
    GUI_SCALE("GUI_SCALE", 12, "options.guiScale", false, false),
    FOG_FANCY("FOG_FANCY", 13, "Fog", false, false),
    FOG_START("FOG_START", 14, "Fog Start", false, false),
    MIPMAP_LEVEL("MIPMAP_LEVEL", 15, "Mipmap Level", false, false),
    MIPMAP_TYPE("MIPMAP_TYPE", 16, "Mipmap Type", false, false),
    LOAD_FAR("LOAD_FAR", 18, "Load Far", false, false),
    PRELOADED_CHUNKS("PRELOADED_CHUNKS", 19, "Preloaded Chunks", false, false),
    SMOOTH_FPS("SMOOTH_FPS", 20, "Smooth FPS", false, false),
    BRIGHTNESS("BRIGHTNESS", 21, "Brightness", true, false),
    CLOUDS("CLOUDS", 22, "Clouds", false, false),
    CLOUD_HEIGHT("CLOUD_HEIGHT", 23, "Cloud Height", true, false),
    TREES("TREES", 24, "Trees", false, false),
    GRASS("GRASS", 25, "Grass", false, false),
    RAIN("RAIN", 27, "Rain & Snow", false, false),
    WATER("RAIN", 28, "Water", false, false),
    ANIMATED_WATER("ANIMATED_WATER", 29, "Water Animated", false, false),
    ANIMATED_LAVA("ANIMATED_LAVA", 30, "Lava Animated", false, false),
    ANIMATED_FIRE("ANIMATED_FLAMES", 31, "Fire Animated", false, false),
    ANIMATED_PORTAL("ANIMATED_PORTAL", 32, "Portal Animated", false, false),
    AO_LEVEL("AO_LEVEL", 33, "Smooth Lighting", true, false),
    FAST_DEBUG_INFO("FAST_DEBUG_INFO", 34, "Fast Debug Info", false, false),
    AUTOSAVE_TICKS("AUTOSAVE_TICKS", 35, "Autosave", false, false),
    BETTER_GRASS("BETTER_GRASS", 36, "Better Grass", false, false),
    ANIMATED_REDSTONE("ANIMATED_REDSTONE", 37, "Redstone Animated", false, false),
    ANIMATED_EXPLOSION("ANIMATED_EXPLOSION", 38, "Explosion Animated", false, false),
    ANIMATED_FLAME("ANIMATED_FLAME", 39, "Flame Animated", false, false),
    ANIMATED_SMOKE("ANIMATED_SMOKE", 40, "Smoke Animated", false, false),
    WEATHER("WEATHER", 41, "Weather", false, false),
    SKY("SKY", 42, "Sky", false, false),
    STARS("STARS", 43, "Stars", false, false),
    FAR_VIEW("FAR_VIEW", 44, "Far View", false, false),
    CHUNK_UPDATES("CHUNK_UPDATES", 45, "Chunk Updates", false, false),
    CHUNK_UPDATES_DYNAMIC("CHUNK_UPDATES_DYNAMIC", 46, "Dynamic Updates", false, false),
    TIME("TIME", 47, "Time", false, false),
    CLEAR_WATER("CLEAR_WATER", 48, "Clear Water", false, false),
    SMOOTH_INPUT("SMOOTH_INPUT", 49, "Smooth Input", false, false),
	FOV("FOV", 50, "FOV", true, false);

    private final boolean enumFloat;
    private final boolean enumBoolean;
    private final String enumString;

    public static EnumOptions getEnumOptions(int i) {
        EnumOptions[] aenumoptions = values();
        int j = aenumoptions.length;

        for(int k = 0; k < j; ++k) {
            EnumOptions enumoptions = aenumoptions[k];
            if (enumoptions.returnEnumOrdinal() == i) {
                return enumoptions;
            }
        }

        return null;
    }

    private EnumOptions(String s, int i, String s1, boolean flag, boolean flag1) {
        this.enumString = s1;
        this.enumFloat = flag;
        this.enumBoolean = flag1;
    }

    public boolean getEnumFloat() {
        return this.enumFloat;
    }

    public boolean getEnumBoolean() {
        return this.enumBoolean;
    }

    public int returnEnumOrdinal() {
        return this.ordinal();
    }

    public String getEnumString() {
        return this.enumString;
    }
}
