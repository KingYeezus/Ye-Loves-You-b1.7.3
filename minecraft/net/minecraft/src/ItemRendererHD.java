package net.minecraft.src;

import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class ItemRendererHD extends ItemRenderer {
    private Minecraft minecraft = null;

    public ItemRendererHD(Minecraft minecraft) {
        super(minecraft);
        this.minecraft = minecraft;
    }

    public void renderItem(EntityLiving entityliving, ItemStack itemstack) {
        if (itemstack.itemID < 256 && RenderBlocks.renderItemIn3d(Block.blocksList[itemstack.itemID].getRenderType())) {
            super.renderItem(entityliving, itemstack);
        } else {
            int num = Config.getIconWidthTerrain();
            if (num <= 16) {
                super.renderItem(entityliving, itemstack);
            } else {
                GL11.glPushMatrix();
                if (itemstack.itemID < 256) {
                    GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, this.minecraft.renderEngine.getTexture("/terrain.png"));
                    num = Config.getIconWidthTerrain();
                } else {
                    GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, this.minecraft.renderEngine.getTexture("/gui/items.png"));
                    num = Config.getIconWidthItems();
                }

                Tessellator tessellator = Tessellator.instance;
                int i = entityliving.getItemIcon(itemstack);
                float f = ((float)(i % 16 * 16) + 0.0F) / 256.0F;
                float f1 = ((float)(i % 16 * 16) + 15.99F) / 256.0F;
                float f2 = ((float)(i / 16 * 16) + 0.0F) / 256.0F;
                float f3 = ((float)(i / 16 * 16) + 15.99F) / 256.0F;
                float f4 = 1.0F;
                float f5 = 0.0F;
                float f6 = 0.3F;
                GL11.glEnable(32826 /*GL_RESCALE_NORMAL_EXT*/);
                GL11.glTranslatef(-f5, -f6, 0.0F);
                float f7 = 1.5F;
                GL11.glScalef(f7, f7, f7);
                GL11.glRotatef(50.0F, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(335.0F, 0.0F, 0.0F, 1.0F);
                GL11.glTranslatef(-0.9375F, -0.0625F, 0.0F);
                float f8 = 0.0625F;
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0F, 0.0F, 1.0F);
                tessellator.addVertexWithUV(0.0D, 0.0D, 0.0D, (double)f1, (double)f3);
                tessellator.addVertexWithUV((double)f4, 0.0D, 0.0D, (double)f, (double)f3);
                tessellator.addVertexWithUV((double)f4, 1.0D, 0.0D, (double)f, (double)f2);
                tessellator.addVertexWithUV(0.0D, 1.0D, 0.0D, (double)f1, (double)f2);
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0F, 0.0F, -1.0F);
                tessellator.addVertexWithUV(0.0D, 1.0D, (double)(0.0F - f8), (double)f1, (double)f2);
                tessellator.addVertexWithUV((double)f4, 1.0D, (double)(0.0F - f8), (double)f, (double)f2);
                tessellator.addVertexWithUV((double)f4, 0.0D, (double)(0.0F - f8), (double)f, (double)f3);
                tessellator.addVertexWithUV(0.0D, 0.0D, (double)(0.0F - f8), (double)f1, (double)f3);
                tessellator.draw();
                float du = 1.0F / (float)(32 * num);
                float dz = 1.0F / (float)num;
                tessellator.startDrawingQuads();
                tessellator.setNormal(-1.0F, 0.0F, 0.0F);

                int i1;
                float f12;
                float f16;
                float f20;
                for(i1 = 0; i1 < num; ++i1) {
                    f12 = (float)i1 / ((float)num * 1.0F);
                    f16 = f1 + (f - f1) * f12 - du;
                    f20 = f4 * f12;
                    tessellator.addVertexWithUV((double)f20, 0.0D, (double)(0.0F - f8), (double)f16, (double)f3);
                    tessellator.addVertexWithUV((double)f20, 0.0D, 0.0D, (double)f16, (double)f3);
                    tessellator.addVertexWithUV((double)f20, 1.0D, 0.0D, (double)f16, (double)f2);
                    tessellator.addVertexWithUV((double)f20, 1.0D, (double)(0.0F - f8), (double)f16, (double)f2);
                }

                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(1.0F, 0.0F, 0.0F);

                for(i1 = 0; i1 < num; ++i1) {
                    f12 = (float)i1 / ((float)num * 1.0F);
                    f16 = f1 + (f - f1) * f12 - du;
                    f20 = f4 * f12 + dz;
                    tessellator.addVertexWithUV((double)f20, 1.0D, (double)(0.0F - f8), (double)f16, (double)f2);
                    tessellator.addVertexWithUV((double)f20, 1.0D, 0.0D, (double)f16, (double)f2);
                    tessellator.addVertexWithUV((double)f20, 0.0D, 0.0D, (double)f16, (double)f3);
                    tessellator.addVertexWithUV((double)f20, 0.0D, (double)(0.0F - f8), (double)f16, (double)f3);
                }

                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0F, 1.0F, 0.0F);

                for(i1 = 0; i1 < num; ++i1) {
                    f12 = (float)i1 / ((float)num * 1.0F);
                    f16 = f3 + (f2 - f3) * f12 - du;
                    f20 = f4 * f12 + dz;
                    tessellator.addVertexWithUV(0.0D, (double)f20, 0.0D, (double)f1, (double)f16);
                    tessellator.addVertexWithUV((double)f4, (double)f20, 0.0D, (double)f, (double)f16);
                    tessellator.addVertexWithUV((double)f4, (double)f20, (double)(0.0F - f8), (double)f, (double)f16);
                    tessellator.addVertexWithUV(0.0D, (double)f20, (double)(0.0F - f8), (double)f1, (double)f16);
                }

                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0F, -1.0F, 0.0F);

                for(i1 = 0; i1 < num; ++i1) {
                    f12 = (float)i1 / ((float)num * 1.0F);
                    f16 = f3 + (f2 - f3) * f12 - du;
                    f20 = f4 * f12;
                    tessellator.addVertexWithUV((double)f4, (double)f20, 0.0D, (double)f, (double)f16);
                    tessellator.addVertexWithUV(0.0D, (double)f20, 0.0D, (double)f1, (double)f16);
                    tessellator.addVertexWithUV(0.0D, (double)f20, (double)(0.0F - f8), (double)f1, (double)f16);
                    tessellator.addVertexWithUV((double)f4, (double)f20, (double)(0.0F - f8), (double)f, (double)f16);
                }

                tessellator.draw();
                GL11.glDisable(32826 /*GL_RESCALE_NORMAL_EXT*/);
                GL11.glPopMatrix();
            }
        }
    }
}
