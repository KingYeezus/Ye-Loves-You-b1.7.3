package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;

public class Vec3D {
	public static final Vec3D ZERO = new Vec3D(0.0D, 0.0D, 0.0D);
    private static List vectorList = new ArrayList();
    private static int nextVector = 0;
    public double xCoord;
    public double yCoord;
    public double zCoord;

    public static Vec3D createVectorHelper(double var0, double var2, double var4) {
        return new Vec3D(var0, var2, var4);
    }

    public static void func_28215_a() {
        vectorList.clear();
        nextVector = 0;
    }

    public static void initialize() {
        nextVector = 0;
    }

    public static Vec3D createVector(double var0, double var2, double var4) {
        if (nextVector >= vectorList.size()) {
            vectorList.add(createVectorHelper(0.0D, 0.0D, 0.0D));
        }

        return ((Vec3D)vectorList.get(nextVector++)).setComponents(var0, var2, var4);
    }

    public Vec3D(double var1, double var3, double var5) {
        if (var1 == -0.0D) {
            var1 = 0.0D;
        }

        if (var3 == -0.0D) {
            var3 = 0.0D;
        }

        if (var5 == -0.0D) {
            var5 = 0.0D;
        }

        this.xCoord = var1;
        this.yCoord = var3;
        this.zCoord = var5;
    }

    private Vec3D setComponents(double var1, double var3, double var5) {
        this.xCoord = var1;
        this.yCoord = var3;
        this.zCoord = var5;
        return this;
    }
    
    public Vec3D rotatePitch(float pitch)
    {
        float f = MathHelper.cos(pitch);
        float f1 = MathHelper.sin(pitch);
        double d0 = this.xCoord;
        double d1 = this.yCoord * (double)f + this.zCoord * (double)f1;
        double d2 = this.zCoord * (double)f - this.yCoord * (double)f1;
        return new Vec3D(d0, d1, d2);
    }
    
    public Vec3D rotateYaw(float yaw)
    {
        float f = MathHelper.cos(yaw);
        float f1 = MathHelper.sin(yaw);
        double d0 = this.xCoord * (double)f + this.zCoord * (double)f1;
        double d1 = this.yCoord;
        double d2 = this.zCoord * (double)f - this.xCoord * (double)f1;
        return new Vec3D(d0, d1, d2);
    }
    

    public Vec3D add(int x, int y, int z) {
        return (x == 0 && y == 0 && z == 0) ? this : new Vec3D(x + x, y + y, z + z);
      }
    
    public Vec3D scale(double p_186678_1_)
    {
        return new Vec3D(this.xCoord * p_186678_1_, this.yCoord * p_186678_1_, this.zCoord * p_186678_1_);
    }
    
    public Vec3D subtract(double x, double y, double z)
    {
        return this.addVector(-x, -y, -z);
    }

    public Vec3D subtract(Vec3D var1) {
        return createVector(var1.xCoord - this.xCoord, var1.yCoord - this.yCoord, var1.zCoord - this.zCoord);
    }

    public Vec3D normalize() {
        double var1 = (double)MathHelper.sqrt_double(this.xCoord * this.xCoord + this.yCoord * this.yCoord + this.zCoord * this.zCoord);
        return var1 < 1.0E-4D ? createVector(0.0D, 0.0D, 0.0D) : createVector(this.xCoord / var1, this.yCoord / var1, this.zCoord / var1);
    }

    public Vec3D crossProduct(Vec3D var1) {
        return createVector(this.yCoord * var1.zCoord - this.zCoord * var1.yCoord, this.zCoord * var1.xCoord - this.xCoord * var1.zCoord, this.xCoord * var1.yCoord - this.yCoord * var1.xCoord);
    }

    public Vec3D addVector(double var1, double var3, double var5) {
        return createVector(this.xCoord + var1, this.yCoord + var3, this.zCoord + var5);
    }

    public double distanceTo(Vec3D var1) {
        double var2 = var1.xCoord - this.xCoord;
        double var4 = var1.yCoord - this.yCoord;
        double var6 = var1.zCoord - this.zCoord;
        return (double)MathHelper.sqrt_double(var2 * var2 + var4 * var4 + var6 * var6);
    }

    public double squareDistanceTo(Vec3D var1) {
        double var2 = var1.xCoord - this.xCoord;
        double var4 = var1.yCoord - this.yCoord;
        double var6 = var1.zCoord - this.zCoord;
        return var2 * var2 + var4 * var4 + var6 * var6;
    }

    public double squareDistanceTo(double var1, double var3, double var5) {
        double var7 = var1 - this.xCoord;
        double var9 = var3 - this.yCoord;
        double var11 = var5 - this.zCoord;
        return var7 * var7 + var9 * var9 + var11 * var11;
    }

    public double lengthVector() {
        return (double)MathHelper.sqrt_double(this.xCoord * this.xCoord + this.yCoord * this.yCoord + this.zCoord * this.zCoord);
    }

    public Vec3D getIntermediateWithXValue(Vec3D var1, double var2) {
        double var4 = var1.xCoord - this.xCoord;
        double var6 = var1.yCoord - this.yCoord;
        double var8 = var1.zCoord - this.zCoord;
        if (var4 * var4 < 1.0000000116860974E-7D) {
            return null;
        } else {
            double var10 = (var2 - this.xCoord) / var4;
            return var10 >= 0.0D && var10 <= 1.0D ? createVector(this.xCoord + var4 * var10, this.yCoord + var6 * var10, this.zCoord + var8 * var10) : null;
        }
    }

    public Vec3D getIntermediateWithYValue(Vec3D var1, double var2) {
        double var4 = var1.xCoord - this.xCoord;
        double var6 = var1.yCoord - this.yCoord;
        double var8 = var1.zCoord - this.zCoord;
        if (var6 * var6 < 1.0000000116860974E-7D) {
            return null;
        } else {
            double var10 = (var2 - this.yCoord) / var6;
            return var10 >= 0.0D && var10 <= 1.0D ? createVector(this.xCoord + var4 * var10, this.yCoord + var6 * var10, this.zCoord + var8 * var10) : null;
        }
    }

    public Vec3D getIntermediateWithZValue(Vec3D var1, double var2) {
        double var4 = var1.xCoord - this.xCoord;
        double var6 = var1.yCoord - this.yCoord;
        double var8 = var1.zCoord - this.zCoord;
        if (var8 * var8 < 1.0000000116860974E-7D) {
            return null;
        } else {
            double var10 = (var2 - this.zCoord) / var8;
            return var10 >= 0.0D && var10 <= 1.0D ? createVector(this.xCoord + var4 * var10, this.yCoord + var6 * var10, this.zCoord + var8 * var10) : null;
        }
    }

    public String toString() {
        return "(" + this.xCoord + ", " + this.yCoord + ", " + this.zCoord + ")";
    }

    public void rotateAroundX(float var1) {
        float var2 = MathHelper.cos(var1);
        float var3 = MathHelper.sin(var1);
        double var4 = this.xCoord;
        double var6 = this.yCoord * (double)var2 + this.zCoord * (double)var3;
        double var8 = this.zCoord * (double)var2 - this.yCoord * (double)var3;
        this.xCoord = var4;
        this.yCoord = var6;
        this.zCoord = var8;
    }

    public void rotateAroundY(float var1) {
        float var2 = MathHelper.cos(var1);
        float var3 = MathHelper.sin(var1);
        double var4 = this.xCoord * (double)var2 + this.zCoord * (double)var3;
        double var6 = this.yCoord;
        double var8 = this.zCoord * (double)var2 - this.xCoord * (double)var3;
        this.xCoord = var4;
        this.yCoord = var6;
        this.zCoord = var8;
    }
}
