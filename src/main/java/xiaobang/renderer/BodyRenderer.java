package xiaobang.renderer;

import net.gobbob.mobends.client.model.ModelRendererBends;
import net.minecraft.client.model.ModelBase;
import org.lwjgl.opengl.GL11;

public class BodyRenderer extends PartRenderer {
    public BodyRenderer(ModelBase argModel, int argTexOffsetX, int argTexOffsetY) {
        super(argModel, argTexOffsetX, argTexOffsetY);
    }




    public float rotationPointY2;

    public ModelRendererBends setRotationPointY2(float par1){
        this.rotationPointY2 = par1;
        return this;
    }



    public void render(float p_78785_1_) {
        this.updateBends(p_78785_1_);
        if (!this.compiled) {
            this.compileDisplayList(p_78785_1_);
        }

        GL11.glTranslatef(this.offsetX, this.offsetY, this.offsetZ);
        int i;
        if (this.rotateAngleX == 0.0F && this.rotateAngleY == 0.0F && this.rotateAngleZ == 0.0F) {
            if (this.rotationPointX == 0.0F && this.rotationPointY == 0.0F && this.rotationPointZ == 0.0F && this.rotationPointY2 == 0.0F) {
                GL11.glRotatef(-this.pre_rotation.getY(), 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(this.pre_rotation.getX(), 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(this.pre_rotation.getZ(), 0.0F, 0.0F, 1.0F);
                GL11.glScalef(this.scaleX, this.scaleY, this.scaleZ);
                if (!this.isHidden & this.showModel) {
                    GL11.glCallList(this.displayList);
                }

                if ((this.showChildIfHidden || !this.isHidden & this.showModel)) {
                    renderChildModels(p_78785_1_);
                }
            } else {
                if(this.rotationPointY2 == 0.0F) {
                    GL11.glTranslatef(this.rotationPointX * p_78785_1_, this.rotationPointY * p_78785_1_, this.rotationPointZ * p_78785_1_);
                }else{
                    GL11.glTranslatef(this.rotationPointX * p_78785_1_, this.rotationPointY2 * p_78785_1_, this.rotationPointZ * p_78785_1_);
                }
                GL11.glRotatef(-this.pre_rotation.getY(), 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(this.pre_rotation.getX(), 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(this.pre_rotation.getZ(), 0.0F, 0.0F, 1.0F);
                GL11.glScalef(this.scaleX, this.scaleY, this.scaleZ);
                if (!this.isHidden & this.showModel) {
                    GL11.glCallList(this.displayList);
                }
                if(this.rotationPointY2 != 0.0F) {
                    GL11.glTranslatef(0, -this.rotationPointY2 * p_78785_1_, 0);
                    GL11.glTranslatef(0, this.rotationPointY * p_78785_1_, 0);
                }
                if ((this.showChildIfHidden || !this.isHidden & this.showModel)) {
                    renderChildModels(p_78785_1_);
                }

                GL11.glTranslatef(-this.rotationPointX * p_78785_1_, -this.rotationPointY * p_78785_1_, -this.rotationPointZ * p_78785_1_);

            }
        } else {
            GL11.glPushMatrix();
            if(this.rotationPointY2 == 0.0F) {
                GL11.glTranslatef(this.rotationPointX * p_78785_1_, this.rotationPointY * p_78785_1_, this.rotationPointZ * p_78785_1_);
            }else{
                GL11.glTranslatef(this.rotationPointX * p_78785_1_, this.rotationPointY2 * p_78785_1_, this.rotationPointZ * p_78785_1_);
            }
            GL11.glRotatef(-this.pre_rotation.getY(), 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(this.pre_rotation.getX(), 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(this.pre_rotation.getZ(), 0.0F, 0.0F, 1.0F);
            if (this.rotateAngleZ != 0.0F) {
                GL11.glRotatef(this.rotateAngleZ * 57.295776F, 0.0F, 0.0F, 1.0F);
            }

            if (this.rotateAngleY != 0.0F) {
                GL11.glRotatef(this.rotateAngleY * 57.295776F, 0.0F, 1.0F, 0.0F);
            }

            if (this.rotateAngleX != 0.0F) {
                GL11.glRotatef(this.rotateAngleX * 57.295776F, 1.0F, 0.0F, 0.0F);
            }

            GL11.glScalef(this.scaleX, this.scaleY, this.scaleZ);
            if (!this.isHidden & this.showModel) {
                GL11.glCallList(this.displayList);
            }

            if(this.rotationPointY2 != 0.0F) {
                GL11.glTranslatef(0, -this.rotationPointY2 * p_78785_1_, 0);
                GL11.glTranslatef(0, this.rotationPointY * p_78785_1_, 0);
            }

            if ((this.showChildIfHidden || !this.isHidden & this.showModel)) {
                renderChildModels(p_78785_1_);
            }

            GL11.glPopMatrix();
        }

        GL11.glTranslatef(-this.offsetX, -this.offsetY, -this.offsetZ);
    }
}
