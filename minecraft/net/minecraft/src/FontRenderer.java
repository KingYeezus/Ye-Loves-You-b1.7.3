package net.minecraft.src;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Formatter;
import java.util.Random;
import javax.imageio.ImageIO;
import org.lwjgl.opengl.GL11;

public class FontRenderer {
    private int[] renderViewEntity = new int[32];
    private byte[] charTexWidths = new byte[256];
    private byte[] charPixelWidths = new byte[256];
    private byte[] unicodeWidth = new byte[65536];
    private int[] charTexIds = new int[256];
    private int basicTexID;
    private int lastBoundTexID;
    private RenderEngine tex;
    private float xPos;
    private float yPos;
    private Random random = new Random();
    private int imgWidth = 128;
    private int imgHeight = 128;
    private int charWidth = 8;
    public int charHeight = 8;
    private String textureName;
    private GameSettings gameSettings;

    public FontRenderer(GameSettings gamesettings, String s, RenderEngine renderengine) {
        this.tex = renderengine;
        this.textureName = s;
        this.gameSettings = gamesettings;
        this.init();
    }

    private void init() {
        BufferedImage bufferedimage;
        try {
            InputStream inputstream;
            if (Config.getMinecraft() != null) {
                bufferedimage = ImageIO.read(Config.getMinecraft().texturePackList.selectedTexturePack.getResourceAsStream(this.textureName));
                inputstream = Config.getMinecraft().texturePackList.selectedTexturePack.getResourceAsStream("/font/glyph_sizes.bin");
                if (inputstream != null) {
                    inputstream.read(this.unicodeWidth);
                }
            } else {
                bufferedimage = ImageIO.read(RenderChicken.class.getResourceAsStream(this.textureName));
                inputstream = RenderChicken.class.getResourceAsStream("/font/glyph_sizes.bin");
                if (inputstream != null) {
                    inputstream.read(this.unicodeWidth);
                }
            }
        } catch (IOException var11) {
            throw new RuntimeException(var11);
        }

        this.imgWidth = bufferedimage.getWidth();
        this.imgHeight = bufferedimage.getHeight();
        this.charWidth = this.imgWidth / 16;
        this.charHeight = this.imgHeight / 16;
        int[] pixels = new int[this.imgWidth * this.imgHeight];
        bufferedimage.getRGB(0, 0, this.imgWidth, this.imgHeight, pixels, 0, this.imgWidth);

        int i;
        int j1;
        int l1;
        int j2;
        int l2;
        int k3;
        int i4;
        for(i = 0; i < 256; ++i) {
            j1 = i % 16;
            l1 = i / 16;

            for(j2 = this.charWidth - 1; j2 >= 0; --j2) {
                l2 = j1 * this.charWidth + j2;
                boolean flag = true;

                for(k3 = 0; k3 < this.charHeight && flag; ++k3) {
                    i4 = pixels[l2 + (l1 * this.charHeight + k3) * this.imgWidth] & 255;
                    if (i4 > 0) {
                        flag = false;
                    }
                }

                if (!flag) {
                    break;
                }
            }

            if (i == 32) {
                j2 = 2;
            }

            this.charTexWidths[i] = (byte)(j2 + 2);
            this.charPixelWidths[i] = (byte)((j2 + 2) * 128 / this.imgWidth);
        }

        this.basicTexID = this.tex.allocateAndSetupTexture(bufferedimage);

        for(i = 0; i < 32; ++i) {
            j1 = (i >> 3 & 1) * 85;
            l1 = (i >> 2 & 1) * 170 + j1;
            j2 = (i >> 1 & 1) * 170 + j1;
            l2 = (i >> 0 & 1) * 170 + j1;
            if (i == 6) {
                l1 += 85;
            }

            if (this.gameSettings.anaglyph) {
                int i3 = (l1 * 30 + j2 * 59 + l2 * 11) / 100;
                k3 = (l1 * 30 + j2 * 70) / 100;
                i4 = (l1 * 30 + l2 * 70) / 100;
                l1 = i3;
                j2 = k3;
                l2 = i4;
            }

            if (i >= 16) {
                l1 /= 4;
                j2 /= 4;
                l2 /= 4;
            }

            this.renderViewEntity[i] = (l1 & 255) << 16 | (j2 & 255) << 8 | l2 & 255;
        }

    }

    private void convertMapFormat(int i) {
        float px = (float)(i % 16 * this.charWidth);
        float py = (float)(i / 16 * this.charHeight);
        if (this.lastBoundTexID != this.basicTexID) {
            GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, this.basicTexID);
            this.lastBoundTexID = this.basicTexID;
        }

        float cw = (float)this.charTexWidths[i] - 0.01F;
        float cwp = (float)this.charPixelWidths[i];
        GL11.glBegin(5);
        GL11.glTexCoord2f(px / (float)this.imgWidth, py / (float)this.imgHeight);
        GL11.glVertex3f(this.xPos, this.yPos, 0.0F);
        GL11.glTexCoord2f(px / (float)this.imgWidth, (py + (float)this.charHeight) / (float)this.imgHeight);
        GL11.glVertex3f(this.xPos, this.yPos + 8.0F, 0.0F);
        GL11.glTexCoord2f((px + cw) / (float)this.imgWidth, py / (float)this.imgHeight);
        GL11.glVertex3f(this.xPos + cwp, this.yPos, 0.0F);
        GL11.glTexCoord2f((px + cw) / (float)this.imgWidth, (py + (float)this.charHeight) / (float)this.imgHeight);
        GL11.glVertex3f(this.xPos + cwp, this.yPos + 8.0F, 0.0F);
        GL11.glEnd();
        this.xPos += cwp;
    }

    private void lineIsCommand(int i) {
        StringBuilder stringbuilder = new StringBuilder();
        (new Formatter(stringbuilder)).format("/font/glyph_%02X.png", i);

        BufferedImage bufferedimage;
        try {
            if (Config.getMinecraft() != null) {
                bufferedimage = ImageIO.read(Config.getMinecraft().texturePackList.selectedTexturePack.getResourceAsStream(stringbuilder.toString()));
            } else {
                bufferedimage = ImageIO.read(PositionTextureVertex.class.getResourceAsStream(stringbuilder.toString()));
            }
        } catch (IOException var5) {
            throw new RuntimeException(var5);
        }

        this.charTexIds[i] = this.tex.allocateAndSetupTexture(bufferedimage);
        this.lastBoundTexID = this.charTexIds[i];
    }

    private String isFancyGraphicsEnabled(int i) {
        char[] ac = new char[i];

        while(i-- != 0) {
            int j = this.random.nextInt() & 15;
            if (j == 0) {
                ac[i] = 167;
            } else {
                ac[i] = (char)(48 + j);
            }
        }

        return new String(ac);
    }

    private void getSaveLoader(char c) {
        if (this.unicodeWidth[c] != 0) {
            int i = c / 256;
            if (this.charTexIds[i] == 0) {
                this.lineIsCommand(i);
            }

            if (this.lastBoundTexID != this.charTexIds[i]) {
                GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, this.charTexIds[i]);
                this.lastBoundTexID = this.charTexIds[i];
            }

            int j = this.unicodeWidth[c] >> 4;
            int k = this.unicodeWidth[c] & 15;
            float f;
            float f1;
            if (k > 7) {
                f1 = 16.0F;
                f = 0.0F;
            } else {
                f1 = (float)(k + 1);
                f = (float)j;
            }

            float f2 = (float)(c % 16 * 16) + f;
            float f3 = (float)((c & 255) / 16 * 16);
            float f4 = f1 - f - 0.02F;
            GL11.glBegin(5);
            GL11.glTexCoord2f(f2 / 256.0F, f3 / 256.0F);
            GL11.glVertex3f(this.xPos, this.yPos, 0.0F);
            GL11.glTexCoord2f(f2 / 256.0F, (f3 + 15.98F) / 256.0F);
            GL11.glVertex3f(this.xPos, this.yPos + 7.99F, 0.0F);
            GL11.glTexCoord2f((f2 + f4) / 256.0F, f3 / 256.0F);
            GL11.glVertex3f(this.xPos + f4 / 2.0F, this.yPos, 0.0F);
            GL11.glTexCoord2f((f2 + f4) / 256.0F, (f3 + 15.98F) / 256.0F);
            GL11.glVertex3f(this.xPos + f4 / 2.0F, this.yPos + 7.99F, 0.0F);
            GL11.glEnd();
            this.xPos += (f1 - f) / 2.0F + 1.0F;
        }
    }

    private void renderStringImpl(String s, boolean flag) {
        for(int i = 0; i < s.length(); ++i) {
            char c = s.charAt(i);
            int k;
            if (c == 167 && i + 1 < s.length()) {
                k = "0123456789abcdef".indexOf(s.toLowerCase().charAt(i + 1));
                if (k < 0 || k > 15) {
                    k = 15;
                }

                if (flag) {
                    k += 16;
                }

                int l = this.renderViewEntity[k];
                GL11.glColor3f((float)(l >> 16) / 255.0F, (float)(l >> 8 & 255) / 255.0F, (float)(l & 255) / 255.0F);
                ++i;
            } else {
                k = ChatAllowedCharacters.allowedCharacters.indexOf(c);
                if (c == ' ') {
                    this.xPos += 4.0F;
                } else if (k > 0) {
                    this.convertMapFormat(k + 32);
                } else {
                    this.getSaveLoader(c);
                }
            }
        }

    }

    private void renderString(String s, double x, double y, int col, boolean flag) {
    	
        this.checkUpdated();
        if (s != null) {
            this.lastBoundTexID = 0;
            if ((col & -16777216) == 0) {
                col |= -16777216;
            }

            if (flag) {
                col = (col & 16579836) >> 2 | col & -16777216;
            }

            GL11.glColor4f((float)(col >> 16 & 255) / 255.0F, (float)(col >> 8 & 255) / 255.0F, (float)(col & 255) / 255.0F, (float)(col >> 24 & 255) / 255.0F);
            this.xPos = (float)x;
            this.yPos = (float)y;
            this.renderStringImpl(s, flag);
        }

    }

    public void drawCenteredString(FontRenderer var1, String var2, int var3, int var4, int var5) {
    	var1.drawStringWithShadow(var2, var3 - var1.getStringWidth(var2) / 2, var4, var5);
    }

    public void drawStringWithShadow(String s, double x, double y, int col) {
        this.renderString(s, x + 1, y + 1, col, true);
        this.renderString(s, x, y, col, false);
    }

    public void drawString(String s, double x, double y, int col) {
        this.renderString(s, x, y, col, false);
    }

    public int getStringWidth(String s) {
        this.checkUpdated();
        if (s == null) {
            return 0;
        } else {
            int i = 0;

            for(int j = 0; j < s.length(); ++j) {
                char c = s.charAt(j);
                if (c == 167) {
                    ++j;
                } else {
                    int k = ChatAllowedCharacters.allowedCharacters.indexOf(c);
                    if (k >= 0) {
                        i += this.charPixelWidths[k + 32];
                    } else if (this.unicodeWidth[c] != 0) {
                        int l = this.unicodeWidth[c] >> 4;
                        int i1 = this.unicodeWidth[c] & 15;
                        if (i1 > 7) {
                            i1 = 15;
                            l = 0;
                        }

                        ++i1;
                        i += (i1 - l) / 2 + 1;
                    }
                }
            }

            return i;
        }
    }

    private void checkUpdated() {
        if (!Config.isFontRendererUpdated()) {
            this.init();
            Config.setFontRendererUpdated(true);
        }
    }

    public void drawSplitString(String s, int x, int y, int col, int lx) {
        this.checkUpdated();
        String[] strs = s.split("\n");
        if (strs.length > 1) {
            for(int i1 = 0; i1 < strs.length; ++i1) {
                this.drawSplitString(strs[i1], x, y, col, lx);
                y += this.splitStringWidth(strs[i1], col);
            }

        } else {
            String[] as1 = s.split(" ");
            int j1 = 0;

            while(j1 < as1.length) {
                String s1;
                for(s1 = as1[j1++] + " "; j1 < as1.length && this.getStringWidth(s1 + as1[j1]) < col; s1 = s1 + as1[j1++] + " ") {
                }

                int k1;
                for(; this.getStringWidth(s1) > col; s1 = s1.substring(k1)) {
                    for(k1 = 0; this.getStringWidth(s1.substring(0, k1 + 1)) <= col; ++k1) {
                    }

                    if (s1.substring(0, k1).trim().length() > 0) {
                        this.drawString(s1.substring(0, k1), x, y, lx);
                        y += this.charHeight;
                    }
                }

                if (s1.trim().length() > 0) {
                    this.drawString(s1, x, y, lx);
                    y += this.charHeight;
                }
            }

        }
    }

    public int splitStringWidth(String s, int i) {
        this.checkUpdated();
        String[] as = s.split("\n");
        int l;
        if (as.length > 1) {
            int j = 0;

            for(l = 0; l < as.length; ++l) {
                j += this.splitStringWidth(as[l], i);
            }

            return j;
        } else {
            String[] as1 = s.split(" ");
            l = 0;
            int i1 = 0;

            while(l < as1.length) {
                String s1;
                for(s1 = as1[l++] + " "; l < as1.length && this.getStringWidth(s1 + as1[l]) < i; s1 = s1 + as1[l++] + " ") {
                }

                int j1;
                for(; this.getStringWidth(s1) > i; s1 = s1.substring(j1)) {
                    for(j1 = 0; this.getStringWidth(s1.substring(0, j1 + 1)) <= i; ++j1) {
                    }

                    if (s1.substring(0, j1).trim().length() > 0) {
                        i1 += this.charHeight;
                    }
                }

                if (s1.trim().length() > 0) {
                    i1 += this.charHeight;
                }
            }

            if (i1 < 8) {
                i1 += this.charHeight;
            }

            return i1;
        }
    }
}
