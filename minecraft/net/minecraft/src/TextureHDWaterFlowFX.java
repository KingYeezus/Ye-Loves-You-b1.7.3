package net.minecraft.src;

public class TextureHDWaterFlowFX extends TextureFX implements TextureHDFX {
    private TexturePackBase texturePackBase;
    private int tileWidth = 16;
    protected float[] buf1;
    protected float[] buf2;
    protected float[] buf3;
    protected float[] buf4;
    private int tickCounter;

    public TextureHDWaterFlowFX() {
        super(Block.waterMoving.blockIndexInTexture + 1);
        this.tileWidth = 16;
        this.imageData = new byte[this.tileWidth * this.tileWidth * 4];
        this.buf1 = new float[this.tileWidth * this.tileWidth];
        this.buf2 = new float[this.tileWidth * this.tileWidth];
        this.buf3 = new float[this.tileWidth * this.tileWidth];
        this.buf4 = new float[this.tileWidth * this.tileWidth];
        this.tileSize = 2;
    }

    public void setTileWidth(int tileWidth) {
        if (tileWidth > Config.getMaxDynamicTileWidth()) {
            tileWidth = Config.getMaxDynamicTileWidth();
        }

        this.tileWidth = tileWidth;
        this.imageData = new byte[tileWidth * tileWidth * 4];
        this.buf1 = new float[tileWidth * tileWidth];
        this.buf2 = new float[tileWidth * tileWidth];
        this.buf3 = new float[tileWidth * tileWidth];
        this.buf4 = new float[tileWidth * tileWidth];
        this.tickCounter = 0;
    }

    public void setTexturePackBase(TexturePackBase tpb) {
        this.texturePackBase = tpb;
    }

    public void onTick() {
        if (!Config.isAnimatedWater()) {
            this.imageData = null;
        }

        if (this.imageData != null) {
            ++this.tickCounter;
            int widthMask = this.tileWidth - 1;

            int j;
            int l;
            int r;
            for(j = 0; j < this.tileWidth; ++j) {
                for(l = 0; l < this.tileWidth; ++l) {
                    float f = 0.0F;

                    for(int s = l - 2; s <= l; ++s) {
                        int tx = j & widthMask;
                        r = s & widthMask;
                        f += this.buf1[tx + r * this.tileWidth];
                    }

                    this.buf2[j + l * this.tileWidth] = f / 3.2F + this.buf3[j + l * this.tileWidth] * 0.8F;
                }
            }

            for(j = 0; j < this.tileWidth; ++j) {
                for(l = 0; l < this.tileWidth; ++l) {
                    float[] var10000 = this.buf3;
                    int var10001 = j + l * this.tileWidth;
                    var10000[var10001] += this.buf4[j + l * this.tileWidth] * 0.05F;
                    if (this.buf3[j + l * this.tileWidth] < 0.0F) {
                        this.buf3[j + l * this.tileWidth] = 0.0F;
                    }

                    var10000 = this.buf4;
                    var10001 = j + l * this.tileWidth;
                    var10000[var10001] -= 0.3F;
                    if (Math.random() < 0.2D) {
                        this.buf4[j + l * this.tileWidth] = 0.5F;
                    }
                }
            }

            float[] af = this.buf2;
            this.buf2 = this.buf1;
            this.buf1 = af;
            l = this.tileWidth * this.tileWidth - 1;

            for(int i = 0; i < this.tileWidth * this.tileWidth; ++i) {
                float f1 = this.buf1[i - this.tickCounter * this.tileWidth & l];
                if (f1 > 1.0F) {
                    f1 = 1.0F;
                }

                if (f1 < 0.0F) {
                    f1 = 0.0F;
                }

                float f2 = f1 * f1;
                r = (int)(32.0F + f2 * 32.0F);
                int g = (int)(50.0F + f2 * 64.0F);
                int b = 255;
                int a = (int)(146.0F + f2 * 50.0F);
                if (this.anaglyphEnabled) {
                    int i3 = (r * 30 + g * 59 + b * 11) / 100;
                    int j3 = (r * 30 + g * 70) / 100;
                    int k3 = (r * 30 + b * 70) / 100;
                    r = i3;
                    g = j3;
                    b = k3;
                }

                this.imageData[i * 4 + 0] = (byte)r;
                this.imageData[i * 4 + 1] = (byte)g;
                this.imageData[i * 4 + 2] = (byte)b;
                this.imageData[i * 4 + 3] = (byte)a;
            }

        }
    }

	@Override
	public void a() {
		// TODO Auto-generated method stub
		
	}
}
