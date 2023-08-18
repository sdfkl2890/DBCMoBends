package xiaobang.renderer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.gobbob.mobends.client.model.ModelRendererBends;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;

public class ModelRendererBends2 extends ModelRendererBends {//一个完全离经叛道的ModelRenderer
    public float rotationPointX2;
    public float rotationPointY2;
    public float rotationPointZ2;

    public float rotateAngleX2;
    public float rotateAngleY2;
    public float rotateAngleZ2;

    public ModelRendererBends2 setRotationPointX2(float par) {
        this.rotationPointX2 = par;
        return this;
    }

    public ModelRendererBends2 setRotationPointY2(float par) {
        this.rotationPointY2 = par;
        return this;
    }

    public ModelRendererBends2 setRotationPointZ2(float par) {
        this.rotationPointZ2 = par;
        return this;
    }

    protected int displayList;

    @SideOnly(Side.CLIENT)
    public void compileDisplayList(float p_78788_1_) {
        this.displayList = GLAllocation.generateDisplayLists(1);
        GL11.glNewList(this.displayList, 4864);
        Tessellator tessellator = Tessellator.instance;

        for (int i = 0; i < this.cubeList.size(); ++i) {
            ((ModelBox) this.cubeList.get(i)).render(tessellator, p_78788_1_);
        }

        GL11.glEndList();
        this.compiled = true;
    }

    public ModelRendererBends2(ModelBase argModel, int argTexOffsetX, int argTexOffsetY) {
        super(argModel, argTexOffsetX, argTexOffsetY);
    }

    public void render(float p_78785_1_) {
        this.updateBends(p_78785_1_);
        if (!this.compiled) {
            this.compileDisplayList(p_78785_1_);
        }

        GL11.glTranslatef(this.offsetX, this.offsetY, this.offsetZ);
        int i;
        if (this.rotateAngleX2 == 0.0F && this.rotateAngleY2 == 0.0F && this.rotateAngleZ2 == 0.0F) {
            if (this.rotationPointX2 == 0.0F && this.rotationPointY2 == 0.0F && this.rotationPointZ2 == 0.0F) {
                GL11.glRotatef(-this.pre_rotation.getY(), 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(this.pre_rotation.getX(), 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(this.pre_rotation.getZ(), 0.0F, 0.0F, 1.0F);
                GL11.glScalef(this.scaleX, this.scaleY, this.scaleZ);
                if (!this.isHidden & this.showModel) {
                    GL11.glCallList(this.displayList);
                }

                if ((this.showChildIfHidden || !this.isHidden & this.showModel) && this.childModels != null) {
                    for (i = 0; i < this.childModels.size(); ++i) {
                        ((ModelRenderer) this.childModels.get(i)).render(p_78785_1_);
                    }
                }
            } else {
                GL11.glTranslatef(this.rotationPointX2 * p_78785_1_, this.rotationPointY2 * p_78785_1_, this.rotationPointZ2 * p_78785_1_);
                GL11.glRotatef(-this.pre_rotation.getY(), 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(this.pre_rotation.getX(), 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(this.pre_rotation.getZ(), 0.0F, 0.0F, 1.0F);
                GL11.glScalef(this.scaleX, this.scaleY, this.scaleZ);
                if (!this.isHidden & this.showModel) {
                    GL11.glCallList(this.displayList);
                }

                if ((this.showChildIfHidden || !this.isHidden & this.showModel) && this.childModels != null) {
                    for (i = 0; i < this.childModels.size(); ++i) {
                        ((ModelRenderer) this.childModels.get(i)).render(p_78785_1_);
                    }
                }

                GL11.glTranslatef(-this.rotationPointX2 * p_78785_1_, -this.rotationPointY2 * p_78785_1_, -this.rotationPointZ2 * p_78785_1_);
            }
        } else {
            GL11.glPushMatrix();
            GL11.glTranslatef(this.rotationPointX2 * p_78785_1_, this.rotationPointY2 * p_78785_1_, this.rotationPointZ2 * p_78785_1_);
            GL11.glRotatef(-this.pre_rotation.getY(), 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(this.pre_rotation.getX(), 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(this.pre_rotation.getZ(), 0.0F, 0.0F, 1.0F);
            if (this.rotateAngleZ2 != 0.0F) {
                GL11.glRotatef(this.rotateAngleZ2 * 57.295776F, 0.0F, 0.0F, 1.0F);
            }

            if (this.rotateAngleY2 != 0.0F) {
                GL11.glRotatef(this.rotateAngleY2 * 57.295776F, 0.0F, 1.0F, 0.0F);
            }

            if (this.rotateAngleX2 != 0.0F) {
                GL11.glRotatef(this.rotateAngleX2 * 57.295776F, 1.0F, 0.0F, 0.0F);
            }

            GL11.glScalef(this.scaleX, this.scaleY, this.scaleZ);
            if (!this.isHidden & this.showModel) {
                GL11.glCallList(this.displayList);
            }

            if ((this.showChildIfHidden || !this.isHidden & this.showModel) && this.childModels != null) {
                for (i = 0; i < this.childModels.size(); ++i) {
                    ((ModelRenderer) this.childModels.get(i)).render(p_78785_1_);
                }
            }

            GL11.glPopMatrix();
        }

        GL11.glTranslatef(-this.offsetX, -this.offsetY, -this.offsetZ);
    }
}
