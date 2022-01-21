package net.minecraft.src;

import java.util.Random;

public class TextureHDPortalFX extends TextureFX implements TextureHDFX {
    private int tileWidth = 0;
    private int tickCounter;
    private byte[][] buffer;

    public TextureHDPortalFX() {
        super(Block.portal.blockIndexInTexture);
        this.tileWidth = 16;
        this.tickCounter = 0;
        this.setup();
    }

    public void setTileWidth(int tileWidth) {
        if (tileWidth > Config.getMaxDynamicTileWidth()) {
            tileWidth = Config.getMaxDynamicTileWidth();
        }

        this.tileWidth = tileWidth;
        this.setup();
        this.tickCounter = 0;
    }

    public void setTexturePackBase(TexturePackBase tpb) {
    }

    private void setup() {
        this.imageData = new byte[this.tileWidth * this.tileWidth * 4];
        this.buffer = new byte[32][this.tileWidth * this.tileWidth * 4];
        Random random = new Random(100L);

        for(int i = 0; i < 32; ++i) {
            for(int x = 0; x < this.tileWidth; ++x) {
                for(int y = 0; y < this.tileWidth; ++y) {
                    float f = 0.0F;

                    int l;
                    for(l = 0; l < 2; ++l) {
                        float f1 = (float)(l * (this.tileWidth / 2));
                        float f2 = (float)(l * (this.tileWidth / 2));
                        float f3 = ((float)x - f1) / (float)this.tileWidth * 2.0F;
                        float f4 = ((float)y - f2) / (float)this.tileWidth * 2.0F;
                        if (f3 < -1.0F) {
                            f3 += 2.0F;
                        }

                        if (f3 >= 1.0F) {
                            f3 -= 2.0F;
                        }

                        if (f4 < -1.0F) {
                            f4 += 2.0F;
                        }

                        if (f4 >= 1.0F) {
                            f4 -= 2.0F;
                        }

                        float f5 = f3 * f3 + f4 * f4;
                        float f6 = (float)Math.atan2((double)f4, (double)f3) + ((float)i / 32.0F * 3.141593F * 2.0F - f5 * 10.0F + (float)(l * 2)) * (float)(l * 2 - 1);
                        f6 = (MathHelper.sin(f6) + 1.0F) / 2.0F;
                        f6 /= f5 + 1.0F;
                        f += f6 * 0.5F;
                    }

                    f += random.nextFloat() * 0.1F;
                    l = (int)(f * 100.0F + 155.0F);
                    int j1 = (int)(f * f * 200.0F + 55.0F);
                    int k1 = (int)(f * f * f * f * 255.0F);
                    int l1 = (int)(f * 100.0F + 155.0F);
                    int pos = y * this.tileWidth + x;
                    this.buffer[i][pos * 4 + 0] = (byte)j1;
                    this.buffer[i][pos * 4 + 1] = (byte)k1;
                    this.buffer[i][pos * 4 + 2] = (byte)l;
                    this.buffer[i][pos * 4 + 3] = (byte)l1;
                }
            }
        }

    }

    public void onTick() {
        if (!Config.isAnimatedPortal()) {
            this.imageData = null;
        }

        if (this.imageData != null) {
            ++this.tickCounter;
            byte[] abyte0 = this.buffer[this.tickCounter & 31];

            for(int i = 0; i < this.tileWidth * this.tileWidth; ++i) {
                int j = abyte0[i * 4 + 0] & 255;
                int k = abyte0[i * 4 + 1] & 255;
                int l = abyte0[i * 4 + 2] & 255;
                int i1 = abyte0[i * 4 + 3] & 255;
                if (this.anaglyphEnabled) {
                    int j1 = (j * 30 + k * 59 + l * 11) / 100;
                    int k1 = (j * 30 + k * 70) / 100;
                    int l1 = (j * 30 + l * 70) / 100;
                    j = j1;
                    k = k1;
                    l = l1;
                }

                this.imageData[i * 4 + 0] = (byte)j;
                this.imageData[i * 4 + 1] = (byte)k;
                this.imageData[i * 4 + 2] = (byte)l;
                this.imageData[i * 4 + 3] = (byte)i1;
            }

        }
    }

	@Override
	public void a() {
		// TODO Auto-generated method stub
		
	}
}
