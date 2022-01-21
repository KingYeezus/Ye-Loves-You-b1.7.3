package net.minecraft.src;

public class GuiOtherSettingsOF extends GuiScreen {
    private GuiScreen prevScreen;
    protected String title = "Other Settings";
    private GameSettings settings;
    private static EnumOptions[] enumOptions;
    private int lastMouseX = 0;
    private int lastMouseY = 0;
    private long mouseStillTime = 0L;

    public GuiOtherSettingsOF(GuiScreen guiscreen, GameSettings gamesettings) {
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
        if (btnName.equals("Smooth FPS")) {
            return new String[]{"Stabilizes FPS by flushing the graphic driver buffers", "  OFF - no stabilization, FPS may fluctuate", "  ON - FPS stabilization", "This option is graphic driver dependant and its effect", "is not always visible"};
        } else if (btnName.equals("Smooth Input")) {
            return new String[]{"Fixes stuck keys, slow input response and sound lag", "  OFF - no fix for stuck keys", "  ON - fixes stuck keys", "This option adds a small delay (1ms) to the game loop", "which fixes the stuck keys, slow input and sound lag."};
        } else if (btnName.equals("Autosave")) {
            return new String[]{"Autosave interval", "Default autosave interval (2s) is NOT RECOMMENDED.", "Autosave causes the famous Lag Spike of Death."};
        } else {
            return btnName.equals("Fast Debug Info") ? new String[]{"Fast Debug Info", " OFF - default debug info screen, slower", " ON - debug info screen without lagometer, faster", "Removes the lagometer from the debug screen (F3)."} : null;
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
        enumOptions = new EnumOptions[]{EnumOptions.SMOOTH_FPS, EnumOptions.SMOOTH_INPUT, EnumOptions.AUTOSAVE_TICKS, EnumOptions.FAST_DEBUG_INFO};
    }
}
