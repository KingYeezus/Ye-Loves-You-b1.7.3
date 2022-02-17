package net.minecraft.src;

public class Frustrum implements ICamera {
    private ClippingHelper clippingHelper = ClippingHelperImpl.getInstance();
    private double xPosition;
    private double yPosition;
    private double zPosition;

    public void setPosition(double d, double d1, double d2) {
        this.xPosition = d;
        this.yPosition = d1;
        this.zPosition = d2;
    }

    public boolean isBoxInFrustum(double d, double d1, double d2, double d3, double d4, double d5) {
        return this.clippingHelper.isBoxInFrustum(d - this.xPosition, d1 - this.yPosition, d2 - this.zPosition, d3 - this.xPosition, d4 - this.yPosition, d5 - this.zPosition);
    }

    public boolean isBoundingBoxInFrustum(AxisAlignedBB axisalignedbb) {
        return this.isBoxInFrustum(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ, axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
    }

    public boolean isBoxInFrustumFully(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        return this.clippingHelper.isBoxInFrustumFully(minX - this.xPosition, minY - this.yPosition, minZ - this.zPosition, maxX - this.xPosition, maxY - this.yPosition, maxZ - this.zPosition);
    }

    public boolean isBoundingBoxInFrustumFully(AxisAlignedBB aab) {
        return this.isBoxInFrustumFully(aab.minX, aab.minY, aab.minZ, aab.maxX, aab.maxY, aab.maxZ);
    }
}
