package xiaobang.renderer;

import net.gobbob.mobends.client.model.ModelRendererBends;
import net.minecraft.client.model.ModelBase;
import org.lwjgl.opengl.GL11;

public class PartRenderer_SeperatedChild extends PartRenderer {
    ModelRendererBends momModel;
    ModelRendererBends seperatedModel;

    public PartRenderer_SeperatedChild(ModelBase argModel, int argTexOffsetX, int argTexOffsetY) {
        super(argModel, argTexOffsetX, argTexOffsetY);
    }

    public PartRenderer_SeperatedChild setMother(ModelRendererBends argMom) {
        this.momModel = argMom;
        return this;
    }

    public PartRenderer_SeperatedChild setSeperatedPart(ModelRendererBends argPart) {
        this.seperatedModel = argPart;
        return this;
    }

    public void postRender(float p_78794_1_) {
        this.updateBends(p_78794_1_);
        this.momModel.postRender(p_78794_1_);
        if (!this.isHidden && this.showModel) {
            if (this.rotateAngleX == 0.0F && this.rotateAngleY == 0.0F && this.rotateAngleZ == 0.0F) {
                if (this.rotationPointX != 0.0F || this.rotationPointY != 0.0F || this.rotationPointZ != 0.0F) {
                    GL11.glTranslatef(this.rotationPointX * p_78794_1_, this.rotationPointY * p_78794_1_, this.rotationPointZ * p_78794_1_);
                    GL11.glRotatef(-this.pre_rotation.getY(), 0.0F, 1.0F, 0.0F);
                    GL11.glRotatef(this.pre_rotation.getX(), 1.0F, 0.0F, 0.0F);
                    GL11.glRotatef(this.pre_rotation.getZ(), 0.0F, 0.0F, 1.0F);
                }
            } else {
                GL11.glTranslatef(this.rotationPointX * p_78794_1_, this.rotationPointY * p_78794_1_, this.rotationPointZ * p_78794_1_);
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
            }
        }

        this.seperatedModel.postRender(p_78794_1_);
        GL11.glTranslatef(-this.seperatedModel.rotationPointX * p_78794_1_, -this.seperatedModel.rotationPointY * p_78794_1_, -this.seperatedModel.rotationPointZ * p_78794_1_);
    }
}
