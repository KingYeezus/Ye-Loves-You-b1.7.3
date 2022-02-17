package net.minecraft.src;

public class TextureHDLavaFX extends TextureFX implements TextureHDFX {
    private TexturePackBase texturePackBase;
    private int tileWidth = 0;
    protected float[] buf1;
    protected float[] buf2;
    protected float[] buf3;
    protected float[] buf4;

    public TextureHDLavaFX() {
        super(Block.lavaMoving.blockIndexInTexture);
        this.tileWidth = 16;
        this.imageData = new byte[this.tileWidth * this.tileWidth * 4];
        this.buf1 = new float[this.tileWidth * this.tileWidth];
        this.buf2 = new float[this.tileWidth * this.tileWidth];
        this.buf3 = new float[this.tileWidth * this.tileWidth];
        this.buf4 = new float[this.tileWidth * this.tileWidth];
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
    }

    public void setTexturePackBase(TexturePackBase tpb) {
        this.texturePackBase = tpb;
    }

    public void onTick() {
        if (!Config.isAnimatedLava()) {
            this.imageData = null;
        }

        if (this.imageData != null) {
            int widthMask = this.tileWidth - 1;

            int y;
            int j1;
            int l1;
            int j2;
            int l2;
            for(int x = 0; x < this.tileWidth; ++x) {
                for(y = 0; y < this.tileWidth; ++y) {
                    float f = 0.0F;
                    int l = (int)(MathHelper.sin((float)y * 3.141593F * 2.0F / 16.0F) * 1.2F);
                    int i1 = (int)(MathHelper.sin((float)x * 3.141593F * 2.0F / 16.0F) * 1.2F);

                    for(j1 = x - 1; j1 <= x + 1; ++j1) {
                        for(l1 = y - 1; l1 <= y + 1; ++l1) {
                            j2 = j1 + l & widthMask;
                            l2 = l1 + i1 & widthMask;
                            f += this.buf1[j2 + l2 * this.tileWidth];
                        }
                    }

                    this.buf2[x + y * this.tileWidth] = f / 10.0F + (this.buf3[(x + 0 & widthMask) + (y + 0 & widthMask) * this.tileWidth] + this.buf3[(x + 1 & widthMask) + (y + 0 & widthMask) * this.tileWidth] + this.buf3[(x + 1 & widthMask) + (y + 1 & widthMask) * this.tileWidth] + this.buf3[(x + 0 & widthMask) + (y + 1 & widthMask) * this.tileWidth]) / 4.0F * 0.8F;
                    float[] var10000 = this.buf3;
                    int var10001 = x + y * this.tileWidth;
                    var10000[var10001] += this.buf4[x + y * this.tileWidth] * 0.01F;
                    if (this.buf3[x + y * this.tileWidth] < 0.0F) {
                        this.buf3[x + y * this.tileWidth] = 0.0F;
                    }

                    var10000 = this.buf4;
                    var10001 = x + y * this.tileWidth;
                    var10000[var10001] -= 0.06F;
                    if (Math.random() < 0.005D) {
                        this.buf4[x + y * this.tileWidth] = 1.5F;
                    }
                }
            }

            float[] af = this.buf2;
            this.buf2 = this.buf1;
            this.buf1 = af;
            y = this.tileWidth * this.tileWidth;

            for(int k = 0; k < y; ++k) {
                float f1 = this.buf1[k] * 2.0F;
                if (f1 > 1.0F) {
                    f1 = 1.0F;
                }

                if (f1 < 0.0F) {
                    f1 = 0.0F;
                }

                j1 = (int)(f1 * 100.0F + 155.0F);
                l1 = (int)(f1 * f1 * 255.0F);
                j2 = (int)(f1 * f1 * f1 * f1 * 128.0F);
                if (this.anaglyphEnabled) {
                    l2 = (j1 * 30 + l1 * 59 + j2 * 11) / 100;
                    int j3 = (j1 * 30 + l1 * 70) / 100;
                    int k3 = (j1 * 30 + j2 * 70) / 100;
                    j1 = l2;
                    l1 = j3;
                    j2 = k3;
                }

                this.imageData[k * 4 + 0] = (byte)j1;
                this.imageData[k * 4 + 1] = (byte)l1;
                this.imageData[k * 4 + 2] = (byte)j2;
                this.imageData[k * 4 + 3] = -1;
            }

        }
    }

	@Override
	public void a() {
		// TODO Auto-generated method stub
		
	}
}
