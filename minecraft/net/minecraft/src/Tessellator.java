package net.minecraft.src;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.opengl.ARBVertexBufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.opengl.Util;

import net.minecraft.src.MEDMEX.Modules.Render.Xray;

public class Tessellator {
    private static boolean convertQuadsToTriangles = true;
    private static boolean tryVBO = false;
    private ByteBuffer byteBuffer;
    private IntBuffer intBuffer;
    private FloatBuffer floatBuffer;
    private int vertexCount = 0;
    private double textureU;
    private double textureV;
    private int color;
    private boolean hasColor = false;
    private boolean hasTexture = false;
    private boolean hasNormals = false;
    private int rawBufferIndex = 0;
    private int addedVertices = 0;
    private boolean isColorDisabled = false;
    private int drawMode;
    private double xOffset;
    private double yOffset;
    private double zOffset;
    private int normal;
    public static final int BUFFER_SIZE = 262144;
    public static volatile Tessellator instance = new Tessellator(262144);
    private boolean isDrawing = false;
    private boolean useVBO = false;
    private IntBuffer vertexBuffers;
    private int vboIndex = 0;
    private int vboCount = 10;
    private int bufferSize;
    private boolean renderingChunk = false;
    public static int chunkOffsetX = 0;
    public static int chunkOffsetZ = 0;
    String pogchamp = "thx historian for fix <3";

    public Tessellator(int i) {
        this.bufferSize = i;
        this.byteBuffer = GLAllocation.createDirectByteBuffer(i * 4);
        this.intBuffer = this.byteBuffer.asIntBuffer();
        this.floatBuffer = this.byteBuffer.asFloatBuffer();
        this.useVBO = tryVBO && GLContext.getCapabilities().GL_ARB_vertex_buffer_object;
        if (this.useVBO) {
            this.vertexBuffers = GLAllocation.createDirectIntBuffer(this.vboCount);
            ARBVertexBufferObject.glGenBuffersARB(this.vertexBuffers);
        }

    }

    public void draw() {
        if (!this.isDrawing) {
            throw new IllegalStateException("Not tesselating!");
        } else {
            this.isDrawing = false;
            if (!this.renderingChunk) {
                GL11.glEnd();
                this.checkOpenGlError();
            } else if (this.vertexCount > 0) {
                this.byteBuffer.position(0);
                this.byteBuffer.limit(this.rawBufferIndex * 4);
                GL11.glEnableClientState(32888 /*GL_TEXTURE_COORD_ARRAY_EXT*/);
                GL11.glEnableClientState(32886 /*GL_COLOR_ARRAY_EXT*/);
                GL11.glEnableClientState(32884 /*GL_VERTEX_ARRAY_EXT*/);
                if (this.useVBO) {
                    this.vboIndex = (this.vboIndex + 1) % this.vboCount;
                    ARBVertexBufferObject.glBindBufferARB(35044 /*GL_STATIC_DRAW_ARB*/, this.vertexBuffers.get(this.vboIndex));
                    ARBVertexBufferObject.glBufferDataARB(35044 /*GL_STATIC_DRAW_ARB*/, this.byteBuffer, 35040 /*GL_STREAM_DRAW_ARB*/);
                    GL11.glTexCoordPointer(2, 5126 /*GL_FLOAT*/, 32, 12L);
                    GL11.glColorPointer(4, 5121 /*GL_UNSIGNED_BYTE*/, 32, 20L);
                    GL11.glVertexPointer(3, 5126 /*GL_FLOAT*/, 32, 0L);
                } else {
                    this.floatBuffer.position(3);
                    GL11.glTexCoordPointer(2, 32, this.floatBuffer);
                    this.byteBuffer.position(20);
                    GL11.glColorPointer(4, true, 32, this.byteBuffer);
                    this.floatBuffer.position(0);
                    GL11.glVertexPointer(3, 32, this.floatBuffer);
                }

                if (this.drawMode == 7 && convertQuadsToTriangles) {
                    GL11.glDrawArrays(4, 0, this.vertexCount);
                } else {
                    GL11.glDrawArrays(this.drawMode, 0, this.vertexCount);
                }

                GL11.glDisableClientState(32888 /*GL_TEXTURE_COORD_ARRAY_EXT*/);
                GL11.glDisableClientState(32886 /*GL_COLOR_ARRAY_EXT*/);
                GL11.glDisableClientState(32884 /*GL_VERTEX_ARRAY_EXT*/);
            }

            this.reset();
        }
    }

    private void reset() {
        this.vertexCount = 0;
        this.byteBuffer.clear();
        this.intBuffer.clear();
        this.rawBufferIndex = 0;
        this.addedVertices = 0;
    }

    public void startDrawingQuads() {
        this.startDrawing(7);
    }

    public void startDrawing(int i) {
        if (this.isDrawing) {
            throw new IllegalStateException("Already tesselating!");
        } else {
            if (!this.renderingChunk) {
                GL11.glBegin(i);
            }

            this.isDrawing = true;
            this.reset();
            this.drawMode = i;
            this.hasNormals = false;
            this.hasColor = false;
            this.hasTexture = false;
            this.isColorDisabled = false;
        }
    }

    public void setTextureUV(double d, double d1) {
        this.hasTexture = true;
        this.textureU = d;
        this.textureV = d1;
        if (!this.renderingChunk) {
            GL11.glTexCoord2f((float)d, (float)d1);
        }

    }

    public void setColorOpaque_F(float f, float f1, float f2) {
        this.setColorOpaque((int)(f * 255.0F), (int)(f1 * 255.0F), (int)(f2 * 255.0F));
    }

    public void setColorRGBA_F(float f, float f1, float f2, float f3) {
        this.setColorRGBA((int)(f * 255.0F), (int)(f1 * 255.0F), (int)(f2 * 255.0F), (int)(f3 * 255.0F));
    }

    public void setColorOpaque(int i, int j, int k) {
    	if(Xray.instance != null && Xray.instance.isEnabled()) {
        this.setColorRGBA(i, j, k, (int)(255 * 0.2));
    	}else {
    		this.setColorRGBA(i, j, k, 255);
    	}
    }

    public void setColorRGBA(int i, int j, int k, int l) {
        if (!this.isColorDisabled) {
            if (i > 255) {
                i = 255;
            }

            if (j > 255) {
                j = 255;
            }

            if (k > 255) {
                k = 255;
            }

            if (l > 255) {
                l = 255;
            }

            if (i < 0) {
                i = 0;
            }

            if (j < 0) {
                j = 0;
            }

            if (k < 0) {
                k = 0;
            }

            if (l < 0) {
                l = 0;
            }

            this.hasColor = true;
            if (!this.renderingChunk) {
                GL11.glColor4ub((byte)i, (byte)j, (byte)k, (byte)l);
            } else if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
                this.color = l << 24 | k << 16 | j << 8 | i;
            } else {
                this.color = i << 24 | j << 16 | k << 8 | l;
            }

        }
    }

    public void addVertexWithUV(double d, double d1, double d2, double d3, double d4) {
        this.setTextureUV(d3, d4);
        this.addVertex(d, d1, d2);
    }

    public void addVertex(double d, double d1, double d2) {
        if (!this.renderingChunk) {
            GL11.glVertex3f((float)(d + this.xOffset), (float)(d1 + this.yOffset), (float)(d2 + this.zOffset));
        } else {
            ++this.addedVertices;
            if (this.drawMode == 7 && convertQuadsToTriangles && this.addedVertices % 4 == 0) {
                for(int i = 0; i < 2; ++i) {
                    int j = 8 * (3 - i);
                    this.intBuffer.put(this.intBuffer.get(this.rawBufferIndex - j + 0));
                    this.intBuffer.put(this.intBuffer.get(this.rawBufferIndex - j + 1));
                    this.intBuffer.put(this.intBuffer.get(this.rawBufferIndex - j + 2));
                    this.intBuffer.put(this.intBuffer.get(this.rawBufferIndex - j + 3));
                    this.intBuffer.put(this.intBuffer.get(this.rawBufferIndex - j + 4));
                    this.intBuffer.put(this.intBuffer.get(this.rawBufferIndex - j + 5));
                    this.intBuffer.put(0);
                    this.intBuffer.put(0);
                    ++this.vertexCount;
                    this.rawBufferIndex += 8;
                }
            }

            if (this.renderingChunk) {
                this.intBuffer.put(Float.floatToRawIntBits((float)(d + this.xOffset) - (float)chunkOffsetX));
                this.intBuffer.put(Float.floatToRawIntBits((float)(d1 + this.yOffset)));
                this.intBuffer.put(Float.floatToRawIntBits((float)(d2 + this.zOffset) - (float)chunkOffsetZ));
            } else {
                this.intBuffer.put(Float.floatToRawIntBits((float)(d + this.xOffset)));
                this.intBuffer.put(Float.floatToRawIntBits((float)(d1 + this.yOffset)));
                this.intBuffer.put(Float.floatToRawIntBits((float)(d2 + this.zOffset)));
            }

            this.intBuffer.put(Float.floatToRawIntBits((float)this.textureU));
            this.intBuffer.put(Float.floatToRawIntBits((float)this.textureV));
            this.intBuffer.put(this.color);
            this.intBuffer.put(0);
            this.intBuffer.put(0);
            this.rawBufferIndex += 8;
            ++this.vertexCount;
            if (this.renderingChunk && this.addedVertices % 4 == 0 && this.rawBufferIndex >= this.bufferSize - 32) {
                this.draw();
                this.isDrawing = true;
            }

        }
    }

    public void setColorOpaque_I(int i) {
        int j = i >> 16 & 255;
        int k = i >> 8 & 255;
        int l = i & 255;
        this.setColorOpaque(j, k, l);
    }

    public void setColorRGBA_I(int i, int j) {
        int k = i >> 16 & 255;
        int l = i >> 8 & 255;
        int i1 = i & 255;
        this.setColorRGBA(k, l, i1, j);
    }

    public void disableColor() {
        this.isColorDisabled = true;
    }

    public void setNormal(float f, float f1, float f2) {
        if (!this.isDrawing) {
            System.out.println("Error: Not drawing !!!");
        }

        this.hasNormals = true;
        byte byte0 = (byte)((int)(f * 128.0F));
        byte byte1 = (byte)((int)(f1 * 127.0F));
        byte byte2 = (byte)((int)(f2 * 127.0F));
        if (!this.renderingChunk) {
            GL11.glNormal3b(byte0, byte1, byte2);
        } else {
            System.out.println("ERROR: NORMALS IN CHUNK MODE !!!");
        }

    }

    public void setTranslationD(double d, double d1, double d2) {
        this.xOffset = d;
        this.yOffset = d1;
        this.zOffset = d2;
    }

    public void setTranslationF(float f, float f1, float f2) {
        this.xOffset += (double)f;
        this.yOffset += (double)f1;
        this.zOffset += (double)f2;
    }

    public void setRenderingChunk(boolean flag) {
        this.renderingChunk = flag;
    }

    private void checkOpenGlError() {
        int i = GL11.glGetError();
        if (i != 0) {
            String s = "OpenGL Error: " + i + " " + Util.translateGLErrorString(i);
            Exception exception = new Exception(s);
            exception.printStackTrace();
        }

    }
}
