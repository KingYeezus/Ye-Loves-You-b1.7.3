package net.minecraft.src;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;

public class RenderEngine {
    public static boolean useMipmaps = false;
    private HashMap textureMap = new HashMap();
    private HashMap field_28151_c = new HashMap();
    private HashMap textureNameToImageMap = new HashMap();
    private IntBuffer singleIntBuffer = GLAllocation.createDirectIntBuffer(1);
    private ByteBuffer imageData;
    private List textureList;
    private Map urlToImageDataMap;
    private GameSettings options;
    private boolean clampTexture;
    private boolean blurTexture;
    private TexturePackList texturePack;
    private BufferedImage missingTextureImage;
    private int terrainTextureId = -1;
    private int guiItemsTextureId = -1;
    private boolean hdTexturesInstalled = false;
    private Map textureDimensionsMap = new HashMap();
    private Map textureDataMap = new HashMap();
    private int tickCounter = 0;
    private ByteBuffer[] mipImageDatas;
    private boolean dynamicTexturesUpdated = false;

    public RenderEngine(TexturePackList texturepacklist, GameSettings gamesettings) {
        this.allocateImageData(256);
        this.textureList = new ArrayList();
        this.urlToImageDataMap = new HashMap();
        this.clampTexture = false;
        this.blurTexture = false;
        this.missingTextureImage = new BufferedImage(64, 64, 2);
        this.texturePack = texturepacklist;
        this.options = gamesettings;
        Graphics g = this.missingTextureImage.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 64, 64);
        g.setColor(Color.BLACK);
        g.drawString("missingtex", 1, 10);
        g.dispose();
    }

    public int[] func_28149_a(String s) {
        TexturePackBase texturepackbase = this.texturePack.selectedTexturePack;
        int[] ai = (int[])((int[])this.field_28151_c.get(s));
        if (ai != null) {
            return ai;
        } else {
            int[] ai1;
            try {
                int[] ai11 = null;
                if (s.startsWith("##")) {
                    ai11 = this.func_28148_b(this.unwrapImageByColumns(this.readTextureImage(texturepackbase.getResourceAsStream(s.substring(2)))));
                } else if (s.startsWith("%clamp%")) {
                    this.clampTexture = true;
                    ai11 = this.func_28148_b(this.readTextureImage(texturepackbase.getResourceAsStream(s.substring(7))));
                    this.clampTexture = false;
                } else if (s.startsWith("%blur%")) {
                    this.blurTexture = true;
                    ai11 = this.func_28148_b(this.readTextureImage(texturepackbase.getResourceAsStream(s.substring(6))));
                    this.blurTexture = false;
                } else {
                    InputStream inputstream = texturepackbase.getResourceAsStream(s);
                    if (inputstream == null) {
                        ai11 = this.func_28148_b(this.missingTextureImage);
                    } else {
                        ai11 = this.func_28148_b(this.readTextureImage(inputstream));
                    }
                }

                this.field_28151_c.put(s, ai11);
                return ai11;
            } catch (IOException var6) {
                var6.printStackTrace();
                ai1 = this.func_28148_b(this.missingTextureImage);
                this.field_28151_c.put(s, ai1);
                return ai1;
            }
        }
    }

    private int[] func_28148_b(BufferedImage bufferedimage) {
        int i = bufferedimage.getWidth();
        int j = bufferedimage.getHeight();
        int[] ai = new int[i * j];
        bufferedimage.getRGB(0, 0, i, j, ai, 0, i);
        return ai;
    }

    private int[] func_28147_a(BufferedImage bufferedimage, int[] ai) {
        int i = bufferedimage.getWidth();
        int j = bufferedimage.getHeight();
        bufferedimage.getRGB(0, 0, i, j, ai, 0, i);
        return ai;
    }

    public int getTexture(String s) {
        TexturePackBase texturepackbase = this.texturePack.selectedTexturePack;
        Integer integer = (Integer)this.textureMap.get(s);
        if (integer != null) {
            return integer;
        } else {
            int i;
            try {
                this.singleIntBuffer.clear();
                GLAllocation.generateTextureNames(this.singleIntBuffer);
                i = this.singleIntBuffer.get(0);
                if (s.startsWith("##")) {
                    this.setupTexture(this.unwrapImageByColumns(this.readTextureImage(texturepackbase.getResourceAsStream(s.substring(2)))), i);
                } else if (s.startsWith("%clamp%")) {
                    this.clampTexture = true;
                    this.setupTexture(this.readTextureImage(texturepackbase.getResourceAsStream(s.substring(7))), i);
                    this.clampTexture = false;
                } else if (s.startsWith("%blur%")) {
                    this.blurTexture = true;
                    this.setupTexture(this.readTextureImage(texturepackbase.getResourceAsStream(s.substring(6))), i);
                    this.blurTexture = false;
                } else {
                    InputStream inputstream = texturepackbase.getResourceAsStream(s);
                    if (inputstream == null) {
                        this.setupTexture(this.missingTextureImage, i);
                    } else {
                        if (s.equals("/terrain.png")) {
                            this.terrainTextureId = i;
                        }

                        if (s.equals("/gui/items.png")) {
                            this.guiItemsTextureId = i;
                        }

                        this.setupTexture(this.readTextureImage(inputstream), i);
                    }
                }

                this.textureMap.put(s, i);
                return i;
            } catch (IOException var6) {
                var6.printStackTrace();
                GLAllocation.generateTextureNames(this.singleIntBuffer);
                i = this.singleIntBuffer.get(0);
                this.setupTexture(this.missingTextureImage, i);
                this.textureMap.put(s, i);
                return i;
            }
        }
    }

    private BufferedImage unwrapImageByColumns(BufferedImage bufferedimage) {
        int i = bufferedimage.getWidth() / 16;
        BufferedImage bufferedimage1 = new BufferedImage(16, bufferedimage.getHeight() * i, 2);
        Graphics g = bufferedimage1.getGraphics();

        for(int j = 0; j < i; ++j) {
            g.drawImage(bufferedimage, -j * 16, j * bufferedimage.getHeight(), (ImageObserver)null);
        }

        g.dispose();
        return bufferedimage1;
    }

    public int allocateAndSetupTexture(BufferedImage bufferedimage) {
        this.singleIntBuffer.clear();
        GLAllocation.generateTextureNames(this.singleIntBuffer);
        int i = this.singleIntBuffer.get(0);
        this.setupTexture(bufferedimage, i);
        this.textureNameToImageMap.put(i, bufferedimage);
        return i;
    }

    public void setupTexture(BufferedImage bufferedimage, int i) {
        GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, i);
        useMipmaps = Config.isUseMipmaps();
        int width;
        int height;
        if (useMipmaps && i != this.guiItemsTextureId) {
            width = Config.getMipmapType();
            GL11.glTexParameteri(3553 /*GL_TEXTURE_2D*/, 10241 /*GL_TEXTURE_MIN_FILTER*/, width);
            GL11.glTexParameteri(3553 /*GL_TEXTURE_2D*/, 10240 /*GL_TEXTURE_MAG_FILTER*/, 9728 /*GL_NEAREST*/);
            if (GLContext.getCapabilities().OpenGL12) {
                GL11.glTexParameteri(3553 /*GL_TEXTURE_2D*/, 33084 /*GL_TEXTURE_BASE_LEVEL*/, 0);
                height = Config.getMipmapLevel();
                if (height >= 4) {
                    int minDim = Math.min(bufferedimage.getWidth(), bufferedimage.getHeight());
                    height = this.getMaxMipmapLevel(minDim) - 4;
                    if (height < 0) {
                        height = 0;
                    }
                }

                GL11.glTexParameteri(3553 /*GL_TEXTURE_2D*/, 33085 /*GL_TEXTURE_MAX_LEVEL*/, height);
            }
        } else {
            GL11.glTexParameteri(3553 /*GL_TEXTURE_2D*/, 10241 /*GL_TEXTURE_MIN_FILTER*/, 9728 /*GL_NEAREST*/);
            GL11.glTexParameteri(3553 /*GL_TEXTURE_2D*/, 10240 /*GL_TEXTURE_MAG_FILTER*/, 9728 /*GL_NEAREST*/);
        }

        if (this.blurTexture) {
            GL11.glTexParameteri(3553 /*GL_TEXTURE_2D*/, 10241 /*GL_TEXTURE_MIN_FILTER*/, 9729 /*GL_LINEAR*/);
            GL11.glTexParameteri(3553 /*GL_TEXTURE_2D*/, 10240 /*GL_TEXTURE_MAG_FILTER*/, 9729 /*GL_LINEAR*/);
        }

        if (this.clampTexture) {
            GL11.glTexParameteri(3553 /*GL_TEXTURE_2D*/, 10242 /*GL_TEXTURE_WRAP_S*/, 10496 /*GL_CLAMP*/);
            GL11.glTexParameteri(3553 /*GL_TEXTURE_2D*/, 10243 /*GL_TEXTURE_WRAP_T*/, 10496 /*GL_CLAMP*/);
        } else {
            GL11.glTexParameteri(3553 /*GL_TEXTURE_2D*/, 10242 /*GL_TEXTURE_WRAP_S*/, 10497 /*GL_REPEAT*/);
            GL11.glTexParameteri(3553 /*GL_TEXTURE_2D*/, 10243 /*GL_TEXTURE_WRAP_T*/, 10497 /*GL_REPEAT*/);
        }

        width = bufferedimage.getWidth();
        height = bufferedimage.getHeight();
        this.setTextureDimension(i, new Dimension(width, height));
        int[] ai = new int[width * height];
        byte[] byteBuf = new byte[width * height * 4];
        bufferedimage.getRGB(0, 0, width, height, ai, 0, width);

        for(int l = 0; l < ai.length; ++l) {
            int alpha = ai[l] >> 24 & 255;
            int red = ai[l] >> 16 & 255;
            int green = ai[l] >> 8 & 255;
            int blue = ai[l] & 255;
            if (this.options != null && this.options.anaglyph) {
                int j3 = (red * 30 + green * 59 + blue * 11) / 100;
                int l3 = (red * 30 + green * 70) / 100;
                int j4 = (red * 30 + blue * 70) / 100;
                red = j3;
                green = l3;
                blue = j4;
            }

            if (alpha == 0) {
                red = 255;
                green = 255;
                blue = 255;
            }

            byteBuf[l * 4 + 0] = (byte)red;
            byteBuf[l * 4 + 1] = (byte)green;
            byteBuf[l * 4 + 2] = (byte)blue;
            byteBuf[l * 4 + 3] = (byte)alpha;
        }

        this.checkImageDataSize(width);
        this.imageData.clear();
        this.imageData.put(byteBuf);
        this.imageData.position(0).limit(byteBuf.length);
        GL11.glTexImage2D(3553 /*GL_TEXTURE_2D*/, 0, 6408 /*GL_RGBA*/, width, height, 0, 6408 /*GL_RGBA*/, 5121 /*GL_UNSIGNED_BYTE*/, this.imageData);
        if (useMipmaps) {
            this.generateMipMaps(this.imageData, width, height);
        }

    }

    private void generateMipMaps(ByteBuffer data, int width, int height) {
        ByteBuffer parMipData = data;

        for(int level = 1; level <= 16; ++level) {
            int parWidth = width >> level - 1;
            int mipWidth = width >> level;
            int mipHeight = height >> level;
            if (mipWidth <= 0 || mipHeight <= 0) {
                break;
            }

            ByteBuffer mipData = this.mipImageDatas[level - 1];

            for(int mipX = 0; mipX < mipWidth; ++mipX) {
                for(int mipY = 0; mipY < mipHeight; ++mipY) {
                    int p1 = parMipData.getInt((mipX * 2 + 0 + (mipY * 2 + 0) * parWidth) * 4);
                    int p2 = parMipData.getInt((mipX * 2 + 1 + (mipY * 2 + 0) * parWidth) * 4);
                    int p3 = parMipData.getInt((mipX * 2 + 1 + (mipY * 2 + 1) * parWidth) * 4);
                    int p4 = parMipData.getInt((mipX * 2 + 0 + (mipY * 2 + 1) * parWidth) * 4);
                    int pixel = this.weightedAverageColor(p1, p2, p3, p4);
                    mipData.putInt((mipX + mipY * mipWidth) * 4, pixel);
                }
            }

            GL11.glTexImage2D(3553 /*GL_TEXTURE_2D*/, level, 6408 /*GL_RGBA*/, mipWidth, mipHeight, 0, 6408 /*GL_RGBA*/, 5121 /*GL_UNSIGNED_BYTE*/, mipData);
            parMipData = mipData;
        }

    }

    public void func_28150_a(int[] ai, int i, int j, int k) {
        GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, k);
        if (useMipmaps) {
            GL11.glTexParameteri(3553 /*GL_TEXTURE_2D*/, 10241 /*GL_TEXTURE_MIN_FILTER*/, 9986 /*GL_NEAREST_MIPMAP_LINEAR*/);
            GL11.glTexParameteri(3553 /*GL_TEXTURE_2D*/, 10240 /*GL_TEXTURE_MAG_FILTER*/, 9728 /*GL_NEAREST*/);
        } else {
            GL11.glTexParameteri(3553 /*GL_TEXTURE_2D*/, 10241 /*GL_TEXTURE_MIN_FILTER*/, 9728 /*GL_NEAREST*/);
            GL11.glTexParameteri(3553 /*GL_TEXTURE_2D*/, 10240 /*GL_TEXTURE_MAG_FILTER*/, 9728 /*GL_NEAREST*/);
        }

        if (this.blurTexture) {
            GL11.glTexParameteri(3553 /*GL_TEXTURE_2D*/, 10241 /*GL_TEXTURE_MIN_FILTER*/, 9729 /*GL_LINEAR*/);
            GL11.glTexParameteri(3553 /*GL_TEXTURE_2D*/, 10240 /*GL_TEXTURE_MAG_FILTER*/, 9729 /*GL_LINEAR*/);
        }

        if (this.clampTexture) {
            GL11.glTexParameteri(3553 /*GL_TEXTURE_2D*/, 10242 /*GL_TEXTURE_WRAP_S*/, 10496 /*GL_CLAMP*/);
            GL11.glTexParameteri(3553 /*GL_TEXTURE_2D*/, 10243 /*GL_TEXTURE_WRAP_T*/, 10496 /*GL_CLAMP*/);
        } else {
            GL11.glTexParameteri(3553 /*GL_TEXTURE_2D*/, 10242 /*GL_TEXTURE_WRAP_S*/, 10497 /*GL_REPEAT*/);
            GL11.glTexParameteri(3553 /*GL_TEXTURE_2D*/, 10243 /*GL_TEXTURE_WRAP_T*/, 10497 /*GL_REPEAT*/);
        }

        byte[] abyte0 = new byte[i * j * 4];

        for(int l = 0; l < ai.length; ++l) {
            int i1 = ai[l] >> 24 & 255;
            int j1 = ai[l] >> 16 & 255;
            int k1 = ai[l] >> 8 & 255;
            int l1 = ai[l] & 255;
            if (this.options != null && this.options.anaglyph) {
                int i2 = (j1 * 30 + k1 * 59 + l1 * 11) / 100;
                int j2 = (j1 * 30 + k1 * 70) / 100;
                int k2 = (j1 * 30 + l1 * 70) / 100;
                j1 = i2;
                k1 = j2;
                l1 = k2;
            }

            abyte0[l * 4 + 0] = (byte)j1;
            abyte0[l * 4 + 1] = (byte)k1;
            abyte0[l * 4 + 2] = (byte)l1;
            abyte0[l * 4 + 3] = (byte)i1;
        }

        this.imageData.clear();
        this.imageData.put(abyte0);
        this.imageData.position(0).limit(abyte0.length);
        GL11.glTexSubImage2D(3553 /*GL_TEXTURE_2D*/, 0, 0, 0, i, j, 6408 /*GL_RGBA*/, 5121 /*GL_UNSIGNED_BYTE*/, this.imageData);
    }

    public void deleteTexture(int i) {
        this.textureNameToImageMap.remove(i);
        this.singleIntBuffer.clear();
        this.singleIntBuffer.put(i);
        this.singleIntBuffer.flip();
        GL11.glDeleteTextures(this.singleIntBuffer);
    }

    public int getTextureForDownloadableImage(String s, String s1) {
        ThreadDownloadImageData threaddownloadimagedata = (ThreadDownloadImageData)this.urlToImageDataMap.get(s);
        if (threaddownloadimagedata != null && threaddownloadimagedata.image != null && !threaddownloadimagedata.textureSetupComplete) {
            if (threaddownloadimagedata.textureName < 0) {
                threaddownloadimagedata.textureName = this.allocateAndSetupTexture(threaddownloadimagedata.image);
            } else {
                this.setupTexture(threaddownloadimagedata.image, threaddownloadimagedata.textureName);
            }

            threaddownloadimagedata.textureSetupComplete = true;
        }

        if (threaddownloadimagedata != null && threaddownloadimagedata.textureName >= 0) {
            return threaddownloadimagedata.textureName;
        } else {
            return s1 == null ? -1 : this.getTexture(s1);
        }
    }

    public ThreadDownloadImageData obtainImageData(String s, ImageBuffer imagebuffer) {
        ThreadDownloadImageData threaddownloadimagedata = (ThreadDownloadImageData)this.urlToImageDataMap.get(s);
        if (threaddownloadimagedata == null) {
            this.urlToImageDataMap.put(s, new ThreadDownloadImageData(s, imagebuffer));
        } else {
            ++threaddownloadimagedata.referenceCount;
        }

        return threaddownloadimagedata;
    }

    public void releaseImageData(String s) {
        ThreadDownloadImageData threaddownloadimagedata = (ThreadDownloadImageData)this.urlToImageDataMap.get(s);
        if (threaddownloadimagedata != null) {
            --threaddownloadimagedata.referenceCount;
            if (threaddownloadimagedata.referenceCount == 0) {
                if (threaddownloadimagedata.textureName >= 0) {
                    this.deleteTexture(threaddownloadimagedata.textureName);
                }

                this.urlToImageDataMap.remove(s);
            }
        }

    }

    public void registerTextureFX(TextureFX texturefx) {
        for(int i = 0; i < this.textureList.size(); ++i) {
            TextureFX tex = (TextureFX)this.textureList.get(i);
            if (tex.tileImage == texturefx.tileImage && tex.iconIndex == texturefx.iconIndex) {
                this.textureList.remove(i);
                --i;
                Config.dbg("Texture removed: " + tex + ", image: " + tex.tileImage + ", index: " + tex.iconIndex);
            }
        }

        this.textureList.add(texturefx);
        texturefx.onTick();
        Config.dbg("Texture registered: " + texturefx + ", image: " + texturefx.tileImage + ", index: " + texturefx.iconIndex);
        this.dynamicTexturesUpdated = false;
    }

    private void generateMipMapsSub(int xOffset, int yOffset, int width, int height, ByteBuffer data, int numTiles, boolean fastColor) {
        ByteBuffer parMipData = data;

        for(int level = 1; level <= 16; ++level) {
            int parWidth = width >> level - 1;
            int mipWidth = width >> level;
            int mipHeight = height >> level;
            int xMipOffset = xOffset >> level;
            int yMipOffset = yOffset >> level;
            if (mipWidth <= 0 || mipHeight <= 0) {
                break;
            }

            ByteBuffer mipData = this.mipImageDatas[level - 1];

            int mipX;
            int mipY;
            int dx;
            int dy;
            for(mipX = 0; mipX < mipWidth; ++mipX) {
                for(mipY = 0; mipY < mipHeight; ++mipY) {
                    dx = parMipData.getInt((mipX * 2 + 0 + (mipY * 2 + 0) * parWidth) * 4);
                    dy = parMipData.getInt((mipX * 2 + 1 + (mipY * 2 + 0) * parWidth) * 4);
                    int p3 = parMipData.getInt((mipX * 2 + 1 + (mipY * 2 + 1) * parWidth) * 4);
                    int p4 = parMipData.getInt((mipX * 2 + 0 + (mipY * 2 + 1) * parWidth) * 4);
                    int pixel;
                    if (fastColor) {
                        pixel = this.averageColor(this.averageColor(dx, dy), this.averageColor(p3, p4));
                    } else {
                        pixel = this.weightedAverageColor(dx, dy, p3, p4);
                    }

                    mipData.putInt((mipX + mipY * mipWidth) * 4, pixel);
                }
            }

            for(mipX = 0; mipX < numTiles; ++mipX) {
                for(mipY = 0; mipY < numTiles; ++mipY) {
                    dx = mipX * mipWidth;
                    dy = mipY * mipHeight;
                    GL11.glTexSubImage2D(3553 /*GL_TEXTURE_2D*/, level, xMipOffset + dx, yMipOffset + dy, mipWidth, mipHeight, 6408 /*GL_RGBA*/, 5121 /*GL_UNSIGNED_BYTE*/, mipData);
                }
            }

            parMipData = mipData;
        }

    }

    public void updateDynamicTextures() {
        this.checkHdTextures();
        ++this.tickCounter;
        this.terrainTextureId = this.getTexture("/terrain.png");
        this.guiItemsTextureId = this.getTexture("/gui/items.png");

        int i;
        TextureFX texturefx;
        for(i = 0; i < this.textureList.size(); ++i) {
            texturefx = (TextureFX)this.textureList.get(i);
            texturefx.anaglyphEnabled = this.options.anaglyph;
            if (!texturefx.getClass().getName().equals("ModTextureStatic") || !this.dynamicTexturesUpdated) {
                int tid = 0;
                int tid1;
                if (texturefx.tileImage == 0) {
                    tid1 = this.terrainTextureId;
                } else {
                    tid1 = this.guiItemsTextureId;
                }

                Dimension dim = this.getTextureDimensions(tid1);
                if (dim == null) {
                    throw new IllegalArgumentException("Unknown dimensions for texture id: " + tid1);
                }

                int tileWidth = dim.width / 16;
                int tileHeight = dim.height / 16;
                this.checkImageDataSize(dim.width);
                this.imageData.limit(0);
                boolean customOk = this.updateCustomTexture(texturefx, this.imageData, dim.width / 16);
                if (!customOk || this.imageData.limit() > 0) {
                    boolean fastColor;
                    if (this.imageData.limit() <= 0) {
                        fastColor = this.updateDefaultTexture(texturefx, this.imageData, dim.width / 16);
                        if (fastColor && this.imageData.limit() <= 0) {
                            continue;
                        }
                    }

                    if (this.imageData.limit() <= 0) {
                        texturefx.onTick();
                        if (texturefx.imageData == null) {
                            continue;
                        }

                        int targetDataLen = tileWidth * tileHeight * 4;
                        if (texturefx.imageData.length == targetDataLen) {
                            this.imageData.clear();
                            this.imageData.put(texturefx.imageData);
                            this.imageData.position(0).limit(texturefx.imageData.length);
                        } else {
                            this.copyScaled(texturefx.imageData, this.imageData, tileWidth);
                        }
                    }

                    texturefx.bindImage(this);
                    fastColor = this.scalesWithFastColor(texturefx);

                    for(int ix = 0; ix < texturefx.tileSize; ++ix) {
                        for(int iy = 0; iy < texturefx.tileSize; ++iy) {
                            int xOffset = texturefx.iconIndex % 16 * tileWidth + ix * tileWidth;
                            int yOffset = texturefx.iconIndex / 16 * tileHeight + iy * tileHeight;
                            GL11.glTexSubImage2D(3553 /*GL_TEXTURE_2D*/, 0, xOffset, yOffset, tileWidth, tileHeight, 6408 /*GL_RGBA*/, 5121 /*GL_UNSIGNED_BYTE*/, this.imageData);
                            if (useMipmaps && ix == 0 && iy == 0) {
                                this.generateMipMapsSub(xOffset, yOffset, tileWidth, tileHeight, this.imageData, texturefx.tileSize, fastColor);
                            }
                        }
                    }
                }
            }
        }

        this.dynamicTexturesUpdated = true;

        for(i = 0; i < this.textureList.size(); ++i) {
            texturefx = (TextureFX)this.textureList.get(i);
            if (texturefx.textureId > 0) {
                this.imageData.clear();
                this.imageData.put(texturefx.imageData);
                this.imageData.position(0).limit(texturefx.imageData.length);
                GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, texturefx.textureId);
                GL11.glTexSubImage2D(3553 /*GL_TEXTURE_2D*/, 0, 0, 0, 16, 16, 6408 /*GL_RGBA*/, 5121 /*GL_UNSIGNED_BYTE*/, this.imageData);
                if (useMipmaps) {
                    this.generateMipMapsSub(0, 0, 16, 16, this.imageData, texturefx.tileSize, false);
                }
            }
        }

    }

    private int averageColor(int i, int j) {
        int k = (i & -16777216) >> 24 & 255;
        int l = (j & -16777216) >> 24 & 255;
        return (k + l >> 1 << 24) + ((i & 16711422) + (j & 16711422) >> 1);
    }

    private int weightedAverageColor(int c1, int c2, int c3, int c4) {
        int cx1 = this.weightedAverageColor(c1, c2);
        int cx2 = this.weightedAverageColor(c3, c4);
        int cx = this.weightedAverageColor(cx1, cx2);
        return cx;
    }

    private int weightedAverageColor(int c1, int c2) {
        int a1 = (c1 & -16777216) >> 24 & 255;
        int a2 = (c2 & -16777216) >> 24 & 255;
        int ax = (a1 + a2) / 2;
        if (a1 == 0 && a2 == 0) {
            a1 = 1;
            a2 = 1;
        } else {
            if (a1 == 0) {
                c1 = c2;
                ax /= 2;
            }

            if (a2 == 0) {
                c2 = c1;
                ax /= 2;
            }
        }

        int r1 = (c1 >> 16 & 255) * a1;
        int g1 = (c1 >> 8 & 255) * a1;
        int b1 = (c1 & 255) * a1;
        int r2 = (c2 >> 16 & 255) * a2;
        int g2 = (c2 >> 8 & 255) * a2;
        int b2 = (c2 & 255) * a2;
        int rx = (r1 + r2) / (a1 + a2);
        int gx = (g1 + g2) / (a1 + a2);
        int bx = (b1 + b2) / (a1 + a2);
        return ax << 24 | rx << 16 | gx << 8 | bx;
    }

    public void refreshTextures() {
        this.textureDataMap.clear();
        this.dynamicTexturesUpdated = false;
        Config.setFontRendererUpdated(false);
        TexturePackBase texturepackbase = this.texturePack.selectedTexturePack;
        Iterator iterator3 = this.textureNameToImageMap.keySet().iterator();

        while(iterator3.hasNext()) {
            int i = (Integer)iterator3.next();
            BufferedImage bufferedimage = (BufferedImage)this.textureNameToImageMap.get(i);
            this.setupTexture(bufferedimage, i);
        }

        ThreadDownloadImageData threaddownloadimagedata;
        for(iterator3 = this.urlToImageDataMap.values().iterator(); iterator3.hasNext(); threaddownloadimagedata.textureSetupComplete = false) {
            threaddownloadimagedata = (ThreadDownloadImageData)iterator3.next();
        }

        iterator3 = this.textureMap.keySet().iterator();

        BufferedImage bufferedimage2;
        String s1;
        while(iterator3.hasNext()) {
            s1 = (String)iterator3.next();

            try {
                if (s1.startsWith("##")) {
                    bufferedimage2 = this.unwrapImageByColumns(this.readTextureImage(texturepackbase.getResourceAsStream(s1.substring(2))));
                } else if (s1.startsWith("%clamp%")) {
                    this.clampTexture = true;
                    bufferedimage2 = this.readTextureImage(texturepackbase.getResourceAsStream(s1.substring(7)));
                } else if (s1.startsWith("%blur%")) {
                    this.blurTexture = true;
                    bufferedimage2 = this.readTextureImage(texturepackbase.getResourceAsStream(s1.substring(6)));
                } else {
                    bufferedimage2 = this.readTextureImage(texturepackbase.getResourceAsStream(s1));
                }

                int j = (Integer)this.textureMap.get(s1);
                this.setupTexture(bufferedimage2, j);
                this.blurTexture = false;
                this.clampTexture = false;
            } catch (IOException var9) {
                var9.printStackTrace();
            }
        }

        iterator3 = this.field_28151_c.keySet().iterator();

        while(iterator3.hasNext()) {
            s1 = (String)iterator3.next();

            try {
                if (s1.startsWith("##")) {
                    bufferedimage2 = this.unwrapImageByColumns(this.readTextureImage(texturepackbase.getResourceAsStream(s1.substring(2))));
                } else if (s1.startsWith("%clamp%")) {
                    this.clampTexture = true;
                    bufferedimage2 = this.readTextureImage(texturepackbase.getResourceAsStream(s1.substring(7)));
                } else if (s1.startsWith("%blur%")) {
                    this.blurTexture = true;
                    bufferedimage2 = this.readTextureImage(texturepackbase.getResourceAsStream(s1.substring(6)));
                } else {
                    bufferedimage2 = this.readTextureImage(texturepackbase.getResourceAsStream(s1));
                }

                this.func_28147_a(bufferedimage2, (int[])((int[])this.field_28151_c.get(s1)));
                this.blurTexture = false;
                this.clampTexture = false;
            } catch (IOException var8) {
                var8.printStackTrace();
            }
        }

    }

    private BufferedImage readTextureImage(InputStream inputstream) throws IOException {
        BufferedImage bufferedimage = ImageIO.read(inputstream);
        inputstream.close();
        return bufferedimage;
    }

    public void bindTexture(int i) {
        if (i >= 0) {
            GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, i);
        }
    }

    private void setTextureDimension(int id, Dimension dim) {
        this.textureDimensionsMap.put(new Integer(id), dim);
        if (id == this.terrainTextureId) {
            Config.setIconWidthTerrain(dim.width / 16);
            this.updateDinamicTextures(0, dim);
        }

        if (id == this.guiItemsTextureId) {
            Config.setIconWidthItems(dim.width / 16);
            this.updateDinamicTextures(1, dim);
        }

    }

    private Dimension getTextureDimensions(int id) {
        return (Dimension)this.textureDimensionsMap.get(new Integer(id));
    }

    private void updateDinamicTextures(int texNum, Dimension dim) {
        this.checkHdTextures();

        for(int i = 0; i < this.textureList.size(); ++i) {
            TextureFX tex = (TextureFX)this.textureList.get(i);
            if (tex.tileImage == texNum && tex instanceof TextureHDFX) {
                TextureHDFX texHD = (TextureHDFX)tex;
                texHD.setTexturePackBase(this.texturePack.selectedTexturePack);
                texHD.setTileWidth(dim.width / 16);
                texHD.a();
            }
        }

    }

    public boolean updateCustomTexture(TextureFX texturefx, ByteBuffer imgData, int tileWidth) {
        if (texturefx.iconIndex == Block.waterStill.blockIndexInTexture) {
            return Config.isGeneratedWater() ? false : this.updateCustomTexture(texturefx, "/custom_water_still.png", imgData, tileWidth, Config.isAnimatedWater(), 1);
        } else if (texturefx.iconIndex == Block.waterStill.blockIndexInTexture + 1) {
            return Config.isGeneratedWater() ? false : this.updateCustomTexture(texturefx, "/custom_water_flowing.png", imgData, tileWidth, Config.isAnimatedWater(), 1);
        } else if (texturefx.iconIndex == Block.lavaStill.blockIndexInTexture) {
            return Config.isGeneratedLava() ? false : this.updateCustomTexture(texturefx, "/custom_lava_still.png", imgData, tileWidth, Config.isAnimatedLava(), 1);
        } else if (texturefx.iconIndex == Block.lavaStill.blockIndexInTexture + 1) {
            return Config.isGeneratedLava() ? false : this.updateCustomTexture(texturefx, "/custom_lava_flowing.png", imgData, tileWidth, Config.isAnimatedLava(), 1);
        } else if (texturefx.iconIndex == Block.portal.blockIndexInTexture) {
            return this.updateCustomTexture(texturefx, "/custom_portal.png", imgData, tileWidth, Config.isAnimatedPortal(), 1);
        } else if (texturefx.iconIndex == Block.fire.blockIndexInTexture) {
            return this.updateCustomTexture(texturefx, "/custom_fire_n_s.png", imgData, tileWidth, Config.isAnimatedFire(), 1);
        } else {
            return texturefx.iconIndex == Block.fire.blockIndexInTexture + 16 ? this.updateCustomTexture(texturefx, "/custom_fire_e_w.png", imgData, tileWidth, Config.isAnimatedFire(), 1) : false;
        }
    }

    private boolean updateDefaultTexture(TextureFX texturefx, ByteBuffer imgData, int tileWidth) {
        if (this.texturePack.selectedTexturePack instanceof TexturePackDefault) {
            return false;
        } else if (texturefx.iconIndex == Block.waterStill.blockIndexInTexture) {
            return Config.isGeneratedWater() ? false : this.updateDefaultTexture(texturefx, imgData, tileWidth, false, 1);
        } else if (texturefx.iconIndex == Block.waterStill.blockIndexInTexture + 1) {
            return Config.isGeneratedWater() ? false : this.updateDefaultTexture(texturefx, imgData, tileWidth, Config.isAnimatedWater(), 1);
        } else if (texturefx.iconIndex == Block.lavaStill.blockIndexInTexture) {
            return Config.isGeneratedLava() ? false : this.updateDefaultTexture(texturefx, imgData, tileWidth, false, 1);
        } else if (texturefx.iconIndex == Block.lavaStill.blockIndexInTexture + 1) {
            return Config.isGeneratedLava() ? false : this.updateDefaultTexture(texturefx, imgData, tileWidth, Config.isAnimatedLava(), 3);
        } else {
            return false;
        }
    }

    private boolean updateDefaultTexture(TextureFX texturefx, ByteBuffer imgData, int tileWidth, boolean scrolling, int scrollDiv) {
        int iconIndex = texturefx.iconIndex;
        if (!scrolling && this.dynamicTexturesUpdated) {
            return true;
        } else {
            byte[] tileData = this.getTerrainIconData(iconIndex, tileWidth);
            if (tileData == null) {
                return false;
            } else {
                imgData.clear();
                int imgLen = tileData.length;
                if (scrolling) {
                    int movNum = tileWidth - this.tickCounter / scrollDiv % tileWidth;
                    int offset = movNum * tileWidth * 4;
                    imgData.put(tileData, offset, imgLen - offset);
                    imgData.put(tileData, 0, offset);
                } else {
                    imgData.put(tileData, 0, imgLen);
                }

                imgData.position(0).limit(imgLen);
                return true;
            }
        }
    }

    private boolean updateCustomTexture(TextureFX texturefx, String imagePath, ByteBuffer imgData, int tileWidth, boolean animated, int animDiv) {
        byte[] imageBytes = this.getCustomTextureData(imagePath, tileWidth);
        if (imageBytes == null) {
            return false;
        } else if (!animated && this.dynamicTexturesUpdated) {
            return true;
        } else {
            int imgLen = tileWidth * tileWidth * 4;
            int imgCount = imageBytes.length / imgLen;
            int imgNum = this.tickCounter / animDiv % imgCount;
            int offset = 0;
            if (animated) {
                offset = imgLen * imgNum;
            }

            imgData.clear();
            imgData.put(imageBytes, offset, imgLen);
            imgData.position(0).limit(imgLen);
            return true;
        }
    }

    private byte[] getTerrainIconData(int tileNum, int tileWidth) {
        String tileIdStr = "Tile-" + tileNum;
        byte[] tileData = this.getCustomTextureData(tileIdStr, tileWidth);
        if (tileData != null) {
            return tileData;
        } else {
            byte[] terrainData = this.getCustomTextureData("/terrain.png", tileWidth * 16);
            if (terrainData == null) {
                return null;
            } else {
                tileData = new byte[tileWidth * tileWidth * 4];
                int tx = tileNum % 16;
                int ty = tileNum / 16;
                int xMin = tx * tileWidth;
                int yMin = ty * tileWidth;
                int var10000 = xMin + tileWidth;
                var10000 = yMin + tileWidth;

                for(int y = 0; y < tileWidth; ++y) {
                    int ys = yMin + y;

                    for(int x = 0; x < tileWidth; ++x) {
                        int xs = xMin + x;
                        int posSrc = 4 * (xs + ys * tileWidth * 16);
                        int posDst = 4 * (x + y * tileWidth);
                        tileData[posDst + 0] = terrainData[posSrc + 0];
                        tileData[posDst + 1] = terrainData[posSrc + 1];
                        tileData[posDst + 2] = terrainData[posSrc + 2];
                        tileData[posDst + 3] = terrainData[posSrc + 3];
                    }
                }

                this.setCustomTextureData(tileIdStr, tileData);
                return tileData;
            }
        }
    }

    public byte[] getCustomTextureData(String imagePath, int tileWidth) {
        byte[] imageBytes = (byte[])((byte[])this.textureDataMap.get(imagePath));
        if (imageBytes == null) {
            if (this.textureDataMap.containsKey(imagePath)) {
                return null;
            }

            imageBytes = this.loadImage(imagePath, tileWidth);
            this.textureDataMap.put(imagePath, imageBytes);
        }

        return imageBytes;
    }

    private void setCustomTextureData(String imagePath, byte[] data) {
        this.textureDataMap.put(imagePath, data);
    }

    private byte[] loadImage(String name, int targetWidth) {
        try {
            TexturePackBase texturePackBase = this.texturePack.selectedTexturePack;
            if (texturePackBase == null) {
                return null;
            } else {
                InputStream in = texturePackBase.getResourceAsStream(name);
                if (in == null) {
                    return null;
                } else {
                    BufferedImage image = this.readTextureImage(in);
                    if (image == null) {
                        return null;
                    } else {
                        if (targetWidth > 0 && image.getWidth() != targetWidth) {
                            double aspectHW = (double)(image.getHeight() / image.getWidth());
                            int targetHeight = (int)((double)targetWidth * aspectHW);
                            image = scaleBufferedImage(image, targetWidth, targetHeight);
                        }

                        int width = image.getWidth();
                        int height = image.getHeight();
                        int[] ai = new int[width * height];
                        byte[] byteBuf = new byte[width * height * 4];
                        image.getRGB(0, 0, width, height, ai, 0, width);

                        for(int l = 0; l < ai.length; ++l) {
                            int alpha = ai[l] >> 24 & 255;
                            int red = ai[l] >> 16 & 255;
                            int green = ai[l] >> 8 & 255;
                            int blue = ai[l] & 255;
                            if (this.options != null && this.options.anaglyph) {
                                int j3 = (red * 30 + green * 59 + blue * 11) / 100;
                                int l3 = (red * 30 + green * 70) / 100;
                                int j4 = (red * 30 + blue * 70) / 100;
                                red = j3;
                                green = l3;
                                blue = j4;
                            }

                            byteBuf[l * 4 + 0] = (byte)red;
                            byteBuf[l * 4 + 1] = (byte)green;
                            byteBuf[l * 4 + 2] = (byte)blue;
                            byteBuf[l * 4 + 3] = (byte)alpha;
                        }

                        return byteBuf;
                    }
                }
            }
        } catch (Exception var18) {
            var18.printStackTrace();
            return null;
        }
    }

    public static BufferedImage scaleBufferedImage(BufferedImage image, int width, int height) {
        BufferedImage scaledImage = new BufferedImage(width, height, 2);
        Graphics2D gr = scaledImage.createGraphics();
        gr.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        gr.drawImage(image, 0, 0, width, height, (ImageObserver)null);
        return scaledImage;
    }

    private void checkImageDataSize(int width) {
        if (this.imageData != null) {
            int len = width * width * 4;
            if (this.imageData.capacity() >= len) {
                return;
            }
        }

        this.allocateImageData(width);
    }

    private void allocateImageData(int width) {
        int imgLen = width * width * 4;
        this.imageData = GLAllocation.createDirectByteBuffer(imgLen);
        List list = new ArrayList();

        for(int mipWidth = width / 2; mipWidth > 0; mipWidth /= 2) {
            int mipLen = mipWidth * mipWidth * 4;
            ByteBuffer buf = GLAllocation.createDirectByteBuffer(mipLen);
            list.add(buf);
        }

        this.mipImageDatas = (ByteBuffer[])((ByteBuffer[])list.toArray(new ByteBuffer[list.size()]));
    }

    public void checkHdTextures() {
        if (!this.hdTexturesInstalled) {
            Minecraft mc = Config.getMinecraft();
            if (mc != null) {
                this.registerTextureFX(new TextureHDLavaFX());
                this.registerTextureFX(new TextureHDWaterFX());
                this.registerTextureFX(new TextureHDPortalFX());
                this.registerTextureFX(new TextureHDCompassFX(mc));
                this.registerTextureFX(new TextureHDWatchFX(mc));
                this.registerTextureFX(new TextureHDWaterFlowFX());
                this.registerTextureFX(new TextureHDLavaFlowFX());
                this.registerTextureFX(new TextureHDFlamesFX(0));
                this.registerTextureFX(new TextureHDFlamesFX(1));
                this.hdTexturesInstalled = true;
            }
        }
    }

    private int getMaxMipmapLevel(int size) {
        int level;
        for(level = 0; size > 0; ++level) {
            size /= 2;
        }

        return level - 1;
    }

    private void copyScaled(byte[] buf, ByteBuffer dstBuf, int dstWidth) {
        int srcWidth = (int)Math.sqrt((double)(buf.length / 4));
        int scale = dstWidth / srcWidth;
        byte[] buf4 = new byte[4];
        int len = dstWidth * dstWidth;
        dstBuf.clear();
        if (scale > 1) {
            for(int y = 0; y < srcWidth; ++y) {
                int yMul = y * srcWidth;
                int ty = y * scale;
                int tyMul = ty * dstWidth;

                for(int x = 0; x < srcWidth; ++x) {
                    int srcPos = (x + yMul) * 4;
                    buf4[0] = buf[srcPos];
                    buf4[1] = buf[srcPos + 1];
                    buf4[2] = buf[srcPos + 2];
                    buf4[3] = buf[srcPos + 3];
                    int tx = x * scale;
                    int dstPosBase = tx + tyMul;

                    for(int tdy = 0; tdy < scale; ++tdy) {
                        int dstPosY = dstPosBase + tdy * dstWidth;
                        dstBuf.position(dstPosY * 4);

                        for(int tdx = 0; tdx < scale; ++tdx) {
                            dstBuf.put(buf4);
                        }
                    }
                }
            }
        }

        dstBuf.position(0).limit(dstWidth * dstWidth * 4);
    }

    private boolean scalesWithFastColor(TextureFX texturefx) {
        return !texturefx.getClass().getName().equals("ModTextureStatic");
    }
}
