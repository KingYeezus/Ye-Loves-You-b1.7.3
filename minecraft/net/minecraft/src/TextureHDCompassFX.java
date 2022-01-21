package net.minecraft.src;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;

public class TextureHDCompassFX extends TextureFX implements TextureHDFX {
    private Minecraft mc;
    private int tileWidth = 0;
    private TexturePackBase texturePackBase = null;
    private byte[] baseImageData;
    private int[] compassIconImageData;
    private double showAngle;
    private double angleDiff;

    public TextureHDCompassFX(Minecraft minecraft) {
        super(Item.compass.getIconFromDamage(0));
        this.mc = minecraft;
        this.tileWidth = 16;
        this.setup();
    }

    public void setTileWidth(int tileWidth) {
        this.tileWidth = tileWidth;
        this.setup();
    }

    public void setTexturePackBase(TexturePackBase tpb) {
        this.texturePackBase = tpb;
    }

    private void setup() {
        this.imageData = new byte[this.tileWidth * this.tileWidth * 4];
        this.compassIconImageData = new int[this.tileWidth * this.tileWidth];
        this.tileImage = 1;

        try {
            BufferedImage bufferedimage = ImageIO.read(Minecraft.class.getResource("/gui/items.png"));
            if (this.texturePackBase != null) {
                bufferedimage = ImageIO.read(this.texturePackBase.getResourceAsStream("/gui/items.png"));
            }

            int x = this.iconIndex % 16 * this.tileWidth;
            int y = this.iconIndex / 16 * this.tileWidth;
            bufferedimage.getRGB(x, y, this.tileWidth, this.tileWidth, this.compassIconImageData, 0, this.tileWidth);
            this.baseImageData = new byte[this.imageData.length];
            int tileWidth2 = this.tileWidth * this.tileWidth;

            for(int i = 0; i < tileWidth2; ++i) {
                int j = this.compassIconImageData[i] >> 24 & 255;
                int k = this.compassIconImageData[i] >> 16 & 255;
                int l = this.compassIconImageData[i] >> 8 & 255;
                int i1 = this.compassIconImageData[i] >> 0 & 255;
                if (this.anaglyphEnabled) {
                    int j1 = (k * 30 + l * 59 + i1 * 11) / 100;
                    int k1 = (k * 30 + l * 70) / 100;
                    int l1 = (k * 30 + i1 * 70) / 100;
                    k = j1;
                    l = k1;
                    i1 = l1;
                }

                this.baseImageData[i * 4 + 0] = (byte)k;
                this.baseImageData[i * 4 + 1] = (byte)l;
                this.baseImageData[i * 4 + 2] = (byte)i1;
                this.baseImageData[i * 4 + 3] = (byte)j;
            }
        } catch (IOException var13) {
            var13.printStackTrace();
        }

    }

    public void onTick() {
        int var10000 = this.tileWidth * this.tileWidth;
        double cx = (double)(this.tileWidth / 2) + 0.5D;
        double cy = (double)(this.tileWidth / 2) - 0.5D;
        double needleLen = 0.3D * (double)(this.tileWidth / 16);
        System.arraycopy(this.baseImageData, 0, this.imageData, 0, this.imageData.length);
        double d = 0.0D;
        if (this.mc.theWorld != null && this.mc.thePlayer != null) {
            ChunkCoordinates chunkcoordinates = this.mc.theWorld.getSpawnPoint();
            double d2 = (double)chunkcoordinates.x - this.mc.thePlayer.posX;
            double d4 = (double)chunkcoordinates.z - this.mc.thePlayer.posZ;
            d = (double)(this.mc.thePlayer.rotationYaw - 90.0F) * 3.141592653589793D / 180.0D - Math.atan2(d4, d2);
            if (this.mc.theWorld.worldProvider.isNether) {
                d = Math.random() * 3.1415927410125732D * 2.0D;
            }
        }

        double d1;
        for(d1 = d - this.showAngle; d1 < -3.141592653589793D; d1 += 6.283185307179586D) {
        }

        while(d1 >= 3.141592653589793D) {
            d1 -= 6.283185307179586D;
        }

        if (d1 < -1.0D) {
            d1 = -1.0D;
        }

        if (d1 > 1.0D) {
            d1 = 1.0D;
        }

        this.angleDiff += d1 * 0.1D;
        this.angleDiff *= 0.8D;
        this.showAngle += this.angleDiff;
        double d3 = Math.sin(this.showAngle);
        double d5 = Math.cos(this.showAngle);

        int j2;
        int l2;
        int j3;
        int l3;
        int j4;
        int l4;
        int j5;
        short c1;
        int l34;
        int j6;
        int l6;
        for(j2 = -4; j2 <= 4; ++j2) {
            l2 = (int)(cx + d5 * (double)j2 * needleLen);
            j3 = (int)(cy - d3 * (double)j2 * needleLen * 0.5D);
            l3 = j3 * this.tileWidth + l2;
            j4 = 100;
            l4 = 100;
            j5 = 100;
            c1 = 255;
            if (this.anaglyphEnabled) {
                l34 = (j4 * 30 + l4 * 59 + j5 * 11) / 100;
                j6 = (j4 * 30 + l4 * 70) / 100;
                l6 = (j4 * 30 + j5 * 70) / 100;
                j4 = l34;
                l4 = j6;
                j5 = l6;
            }

            l34 = l3 * 4;
            this.imageData[l34 + 0] = (byte)j4;
            this.imageData[l34 + 1] = (byte)l4;
            this.imageData[l34 + 2] = (byte)j5;
            this.imageData[l34 + 3] = (byte)c1;
        }

        for(j2 = -8; j2 <= 16; ++j2) {
            l2 = (int)(cx + d3 * (double)j2 * needleLen);
            j3 = (int)(cy + d5 * (double)j2 * needleLen * 0.5D);
            l3 = j3 * this.tileWidth + l2;
            j4 = j2 < 0 ? 100 : 255;
            l4 = j2 < 0 ? 100 : 20;
            j5 = j2 < 0 ? 100 : 20;
            c1 = 255;
            if (this.anaglyphEnabled) {
                l34 = (j4 * 30 + l4 * 59 + j5 * 11) / 100;
                j6 = (j4 * 30 + l4 * 70) / 100;
                l6 = (j4 * 30 + j5 * 70) / 100;
                j4 = l34;
                l4 = j6;
                j5 = l6;
            }

            l34 = l3 * 4;
            this.imageData[l34 + 0] = (byte)j4;
            this.imageData[l34 + 1] = (byte)l4;
            this.imageData[l34 + 2] = (byte)j5;
            this.imageData[l34 + 3] = (byte)c1;
        }

    }

	@Override
	public void a() {
		// TODO Auto-generated method stub
		
	}
}
