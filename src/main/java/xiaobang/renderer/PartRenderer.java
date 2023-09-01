package xiaobang.renderer;

import JinRyuu.JBRA.ModelBipedDBC;
import JinRyuu.JBRA.ModelRendererJBRA;
import JinRyuu.JRMCore.entity.ModelBipedBody;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import joptsimple.internal.Strings;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;
import xiaobang.LWJGLTools;

import java.nio.FloatBuffer;

import static JinRyuu.JRMCore.entity.ModelBipedBody.g;

public class PartRenderer extends ModelRendererBends2 {//ModelRenderBends
    public FloatBuffer matrix;
    protected int displayList;
    public ModelBase modelBase;


    public String part;


    public PartRenderer(ModelBase argModel, int argTexOffsetX, int argTexOffsetY) {
        super(argModel, argTexOffsetX, argTexOffsetY);
        this.modelBase = argModel;
    }

    public PartRenderer(ModelBase argModel, int argTexOffsetX, int argTexOffsetY, String part) {
        super(argModel, argTexOffsetX, argTexOffsetY);
        this.modelBase = argModel;
        this.part = part;
    }

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


    public void renderChildModels(float par1) {
        if (this.childModels != null) {
            for (int i = childModels.size() - 1; i >= 0; i--) {
                if (childModels.get(i) instanceof ModelRenderer) {
                    ((ModelRenderer) childModels.get(i)).render(par1);
                }
            }
        }
        if (!Strings.isNullOrEmpty(this.part) && this.part.equalsIgnoreCase("head")) {
            if (modelBase instanceof ModelBipedDBC) {
                ModelBipedDBC dbc = (ModelBipedDBC) modelBase;
                GL11.glPushMatrix();
                //if(ModelRendererJBRA2.martix != null){
                //    LWJGLTools.loadMartix(ModelRendererJBRA2.martix);
                //}
                //System.out.println("render child");
                GL11.glScalef((0.5F + 0.5F / ModelBipedDBC.f) * ((g <= 1) ? 1.0F : 0.85F), 0.5F + 0.5F / ModelBipedDBC.f, (0.5F + 0.5F / ModelBipedDBC.f) * ((g <= 1) ? 1.0F : 0.85F));
                GL11.glTranslatef(0.0F, (ModelBipedDBC.f - 1.0F) / ModelBipedDBC.f * (2.0F - ((ModelBipedDBC.f >= 1.5F && ModelBipedDBC.f <= 2.0F) ? ((2.0F - ModelBipedDBC.f) / 2.5F) : ((ModelBipedDBC.f < 1.5F && ModelBipedDBC.f >= 1.0F) ? ((ModelBipedDBC.f * 2.0F - 2.0F) * 0.2F) : 0.0F))), 0.0F);
                if (ModelRendererJBRA2.textureId != 0) {
                    GL11.glBindTexture(GL11.GL_TEXTURE_2D, ModelRendererJBRA2.textureId);//此处绑定了未知材质，使得腿部材质出现，为毛啊
                }
                if (ModelRendererJBRA2.colors != null) {
                    GL11.glColor4f(ModelRendererJBRA2.colors.get(0), ModelRendererJBRA2.colors.get(1), ModelRendererJBRA2.colors.get(2), ModelRendererJBRA2.colors.get(3));
                }

                for (int face = 0; face < 56; face++) {
                    ModelRendererJBRA jbra = dbc.hairall[face * 4];
                    if (jbra instanceof ModelRendererJBRA2) {

                        ((ModelRendererJBRA2) jbra).render2(par1);//渲染自定义头发
                    }
                }

                GL11.glPopMatrix();
            }
        }
    }

    public void render(float p_78785_1_) {
        this.updateBends(p_78785_1_);
        if (!this.compiled) {
            this.compileDisplayList(p_78785_1_);
        }

        GL11.glTranslatef(this.offsetX, this.offsetY, this.offsetZ);
        if (this.rotateAngleX == 0.0F && this.rotateAngleY == 0.0F && this.rotateAngleZ == 0.0F) {
            if (this.rotationPointX == 0.0F && this.rotationPointY == 0.0F && this.rotationPointZ == 0.0F) {
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
                matrix = LWJGLTools.getCurrentModelViewMatrix();
            } else {
                GL11.glTranslatef(this.rotationPointX * p_78785_1_, this.rotationPointY * p_78785_1_, this.rotationPointZ * p_78785_1_);
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
                matrix = LWJGLTools.getCurrentModelViewMatrix();
                GL11.glTranslatef(-this.rotationPointX * p_78785_1_, -this.rotationPointY * p_78785_1_, -this.rotationPointZ * p_78785_1_);
            }
        } else {
            GL11.glPushMatrix();
            GL11.glTranslatef(this.rotationPointX * p_78785_1_, this.rotationPointY * p_78785_1_, this.rotationPointZ * p_78785_1_);
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

            if ((this.showChildIfHidden || !this.isHidden & this.showModel)) {
                renderChildModels(p_78785_1_);
            }
            matrix = LWJGLTools.getCurrentModelViewMatrix();
            GL11.glPopMatrix();
        }

        GL11.glTranslatef(-this.offsetX, -this.offsetY, -this.offsetZ);
    }
}
