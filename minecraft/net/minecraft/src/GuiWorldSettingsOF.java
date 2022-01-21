package net.minecraft.src;

public class GuiWorldSettingsOF extends GuiScreen {
    private GuiScreen prevScreen;
    protected String title = "World Settings";
    private GameSettings settings;
    private static EnumOptions[] enumOptions;
    private int lastMouseX = 0;
    private int lastMouseY = 0;
    private long mouseStillTime = 0L;

    public GuiWorldSettingsOF(GuiScreen guiscreen, GameSettings gamesettings) {
        this.prevScreen = guiscreen;
        this.settings = gamesettings;
    }

    public void initGui() {
        StringTranslate stringtranslate = StringTranslate.getInstance();
        int i = 0;
        EnumOptions[] aenumoptions = enumOptions;
        int j = aenumoptions.length;

        for(int k = 0; k < j; ++k) {
            EnumOptions enumoptions = aenumoptions[k];
            int x = this.width / 2 - 155 + i % 2 * 160;
            int y = this.height / 6 + 21 * (i / 2) - 10;
            if (!enumoptions.getEnumFloat()) {
                this.controlList.add(new GuiSmallButton(enumoptions.returnEnumOrdinal(), x, y, enumoptions, this.settings.getKeyBinding(enumoptions)));
            } else {
                this.controlList.add(new GuiSlider(enumoptions.returnEnumOrdinal(), x, y, enumoptions, this.settings.getKeyBinding(enumoptions), this.settings.getOptionFloatValue(enumoptions)));
            }

            ++i;
        }

        this.controlList.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 168 + 11, stringtranslate.translateKey("gui.done")));
    }

    protected void actionPerformed(GuiButton guibutton) {
        if (guibutton.enabled) {
            if (guibutton.id < 100 && guibutton instanceof GuiSmallButton) {
                this.settings.setOptionValue(((GuiSmallButton)guibutton).returnEnumOptions(), 1);
                guibutton.displayString = this.settings.getKeyBinding(EnumOptions.getEnumOptions(guibutton.id));
            }

            if (guibutton.id == 200) {
                this.mc.gameSettings.saveOptions();
                this.mc.displayGuiScreen(this.prevScreen);
            }

            if (guibutton.id != EnumOptions.CLOUD_HEIGHT.ordinal()) {
                ScaledResolution scaledresolution = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
                int i = scaledresolution.getScaledWidth();
                int j = scaledresolution.getScaledHeight();
                this.setWorldAndResolution(this.mc, i, j);
            }

        }
    }

    public void drawScreen(int x, int y, float f) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, this.title, this.width / 2, 20, 16777215);
        super.drawScreen(x, y, f);
        if (Math.abs(x - this.lastMouseX) <= 5 && Math.abs(y - this.lastMouseY) <= 5) {
            int activateDelay = 700;
            if (System.currentTimeMillis() >= this.mouseStillTime + (long)activateDelay) {
                int x1 = this.width / 2 - 150;
                int y1 = this.height / 6 - 5;
                if (y <= y1 + 98) {
                    y1 += 105;
                }

                int x2 = x1 + 150 + 150;
                int y2 = y1 + 84 + 10;
                GuiButton btn = this.getSelectedButton(x, y);
                if (btn != null) {
                    String s = this.getButtonName(btn.displayString);
                    String[] lines = this.getTooltipLines(s);
                    if (lines == null) {
                        return;
                    }

                    this.drawGradientRect(x1, y1, x2, y2, -536870912, -536870912);

                    for(int i = 0; i < lines.length; ++i) {
                        String line = lines[i];
                        this.fontRenderer.drawStringWithShadow(line, x1 + 5, y1 + 5 + i * 11, 14540253);
                    }
                }

            }
        } else {
            this.lastMouseX = x;
            this.lastMouseY = y;
            this.mouseStillTime = System.currentTimeMillis();
        }
    }

    private String[] getTooltipLines(String btnName) {
        if (btnName.equals("Load Far")) {
            return new String[]{"Loads the world chunks at distance Far.", "Switching the render distance does not cause all chunks ", "to be loaded again.", "  OFF - world chunks loaded up to render distance", "  ON - world chunks loaded at distance Far, allows", "       fast render distance switching"};
        } else if (btnName.equals("Preloaded Chunks")) {
            return new String[]{"Defines an area in which no chunks will be loaded", "  OFF - after 5m new chunks will be loaded", "  2 - after 32m  new chunks will be loaded", "  8 - after 128m new chunks will be loaded", "Higher values need more time to load all the chunks"};
        } else if (btnName.equals("Chunk Updates")) {
            return new String[]{"Chunk updates per frame", " 1 - (default) slower world loading, higher FPS", " 3 - faster world loading, lower FPS", " 5 - fastest world loading, lowest FPS"};
        } else if (btnName.equals("Dynamic Updates")) {
            return new String[]{"Chunk updates per frame", " OFF - (default) standard chunk updates per frame", " ON - more updates while the player is standing still", "Dynamic updates force more chunk updates while", "the player is standing still to load the world faster."};
        } else if (btnName.equals("Far View")) {
            return new String[]{"Far View", " OFF - (default) standard view distance", " ON - 3x view distance", "Far View is very resource demanding!", "3x view distance => 9x chunks to be loaded => FPS / 9", "Standard view distances: 32, 64, 128, 256", "Far view distances: 96, 192, 384, 512"};
        } else if (btnName.equals("Time")) {
            return new String[]{"Time", " Default - normal day/night cycles", " Day Only - day only", " Night Only - night only"};
        } else {
            return btnName.equals("Weather") ? new String[]{"Weather", "  ON - weather is active, slower", "  OFF  - weather is not active, faster", "The weather controls rain, snow and thunderstorms."} : null;
        }
    }

    private String getButtonName(String displayString) {
        int pos = displayString.indexOf(58);
        return pos < 0 ? displayString : displayString.substring(0, pos);
    }

    private GuiButton getSelectedButton(int i, int j) {
        for(int k = 0; k < this.controlList.size(); ++k) {
            GuiButton btn = (GuiButton)this.controlList.get(k);
            boolean flag = i >= btn.xPosition && j >= btn.yPosition && i < btn.xPosition + btn.width && j < btn.yPosition + btn.height;
            if (flag) {
                return btn;
            }
        }

        return null;
    }

    static {
        enumOptions = new EnumOptions[]{EnumOptions.LOAD_FAR, EnumOptions.PRELOADED_CHUNKS, EnumOptions.CHUNK_UPDATES, EnumOptions.CHUNK_UPDATES_DYNAMIC, EnumOptions.WEATHER, EnumOptions.TIME, EnumOptions.FAR_VIEW};
    }
}
