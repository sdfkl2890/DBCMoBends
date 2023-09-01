package xiaobang.renderer;

import JinRyuu.JBRA.ModelBipedDBC;
import JinRyuu.JRMCore.entity.ModelBipedBody;
import net.minecraft.client.model.ModelRenderer;
import org.lwjgl.opengl.GL11;
import xiaobang.LWJGLTools;

import java.nio.FloatBuffer;
import java.util.Iterator;

public class BodyRenderer extends PartRenderer {

    public PartRenderer head;
    public PartRenderer leftArm;
    public PartRenderer rightArm;
    public PartRenderer leftLeg;
    public PartRenderer rightLeg;

    public BodyRenderer(ModelBipedBody argModel, int argTexOffsetX, int argTexOffsetY) {
        super(argModel, argTexOffsetX, argTexOffsetY);
        this.part = "body";
        this.modelBase = argModel;
    }

    public void renderChildModels(float par1) {
        int textureId = LWJGLTools.getCurrentBindingTexture();
        FloatBuffer colors = LWJGLTools.getCurrentColorRGBA();
        if (this.childModels != null) {
            for (Iterator it = childModels.iterator(); it.hasNext(); ) {
                Object obj = it.next();
                if (obj instanceof ModelRenderer) {
                    ((ModelRenderer) obj).render(par1);
                    //System.out.println("modelRenderer " + i);
                }
                GL11.glColor4f(colors.get(0), colors.get(1), colors.get(2), colors.get(3));
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
            }
        }
        if (modelBase instanceof ModelBipedDBC) {
            ModelBipedDBC dbc = (ModelBipedDBC) modelBase;
            if (!(dbc.hairall[0] instanceof ModelRendererJBRA2)) {
                for (int face = 0; face < 56; face++) {
                    if (!(dbc.hairall[face * 4] instanceof ModelRendererJBRA2)) {
                        dbc.hairall[face * 4] = new ModelRendererJBRA2(dbc, dbc.hairall[face * 4]);
                    }
                }
            }
        }

        head.render(par1);//渲染头部，以便腿部衣服材质可以正常显示,详见PartRenderer

    }


    public void render(float p_78785_1_) {
        this.updateBends(p_78785_1_);
        if (!this.compiled) {
            this.compileDisplayList(p_78785_1_);
        }

        GL11.glTranslatef(this.offsetX, this.offsetY, this.offsetZ);
        if (this.rotateAngleX == 0.0F && this.rotateAngleY == 0.0F && this.rotateAngleZ == 0.0F) {
            if (this.rotationPointX == 0.0F && this.rotationPointY == 0.0F && this.rotationPointZ == 0.0F && this.rotationPointY2 == 0.0F) {
                GL11.glRotatef(-this.pre_rotation.getY(), 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(this.pre_rotation.getX(), 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(this.pre_rotation.getZ(), 0.0F, 0.0F, 1.0F);
                GL11.glScalef(this.scaleX, this.scaleY, this.scaleZ);

                if (!this.isHidden & this.showModel) {
                    GL11.glCallList(this.displayList);
                }

                matrix = LWJGLTools.getCurrentModelViewMatrix();
                if ((this.showChildIfHidden || !this.isHidden & this.showModel)) {
                    renderChildModels(p_78785_1_);
                }

            } else {
                if (this.rotationPointY2 == 0.0F) {//调整旋转原点
                    GL11.glTranslatef(this.rotationPointX * p_78785_1_, this.rotationPointY * p_78785_1_, this.rotationPointZ * p_78785_1_);
                } else {
                    GL11.glTranslatef(this.rotationPointX * p_78785_1_, this.rotationPointY2 * p_78785_1_, this.rotationPointZ * p_78785_1_);
                }
                GL11.glRotatef(-this.pre_rotation.getY(), 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(this.pre_rotation.getX(), 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(this.pre_rotation.getZ(), 0.0F, 0.0F, 1.0F);
                GL11.glScalef(this.scaleX, this.scaleY, this.scaleZ);
                if (!this.isHidden & this.showModel) {
                    GL11.glCallList(this.displayList);
                }
                if (this.rotationPointY2 != 0.0F) {//渲染子模型时旋转原点复原
                    GL11.glTranslatef(0, -this.rotationPointY2 * p_78785_1_, 0);
                    GL11.glTranslatef(0, this.rotationPointY * p_78785_1_, 0);
                }

                matrix = LWJGLTools.getCurrentModelViewMatrix();
                if ((this.showChildIfHidden || !this.isHidden & this.showModel)) {
                    renderChildModels(p_78785_1_);
                }

                GL11.glTranslatef(-this.rotationPointX * p_78785_1_, -this.rotationPointY * p_78785_1_, -this.rotationPointZ * p_78785_1_);

            }
        } else {
            GL11.glPushMatrix();
            if (this.rotationPointY2 == 0.0F) {
                GL11.glTranslatef(this.rotationPointX * p_78785_1_, this.rotationPointY * p_78785_1_, this.rotationPointZ * p_78785_1_);
            } else {
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

            if (this.rotationPointY2 != 0.0F) {
                GL11.glTranslatef(0, -this.rotationPointY2 * p_78785_1_, 0);
                GL11.glTranslatef(0, this.rotationPointY * p_78785_1_, 0);
            }

            matrix = LWJGLTools.getCurrentModelViewMatrix();
            if ((this.showChildIfHidden || !this.isHidden & this.showModel)) {
                renderChildModels(p_78785_1_);
            }

            GL11.glPopMatrix();
        }

        GL11.glTranslatef(-this.offsetX, -this.offsetY, -this.offsetZ);

    }
}
