package xiaobang.renderer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

public class PartRenderer extends ModelRendererBends2 {
    protected int displayList;
    public ModelBase modelBase;
    //public ArrayList<ModelRenderer2> thisTicksToRender = new ArrayList<>();
    public PartRenderer(ModelBase argModel, int argTexOffsetX, int argTexOffsetY) {
        super(argModel, argTexOffsetX, argTexOffsetY);
        this.modelBase = argModel;
    }

    @SideOnly(Side.CLIENT)
    public void compileDisplayList(float p_78788_1_) {
        this.displayList = GLAllocation.generateDisplayLists(1);
        GL11.glNewList(this.displayList, 4864);
        Tessellator tessellator = Tessellator.instance;

        for(int i = 0; i < this.cubeList.size(); ++i) {

            ((ModelBox)this.cubeList.get(i)).render(tessellator, p_78788_1_);
        }

        GL11.glEndList();
        this.compiled = true;
    }

    public void renderChildModels(float par1){
        if(this.childModels != null) {
            for (int i = childModels.size() - 1; i >= 0;i--) {
                if(childModels.get(i) instanceof ModelRenderer2) {
                    ((ModelRenderer2) childModels.get(i)).render2(par1);
                }else{
                    ((ModelRenderer) childModels.get(i)).render(par1);
                }
                    //childModels.set(i,new ModelRenderer2(modelBase,this, (ModelRenderer) childModels.get(i)));
            }
        }
        /*if(this.thisTicksToRender != null){
            for (int i = thisTicksToRender.size() - 1; i >= 0;i--) {
                thisTicksToRender.get(i).render2(par1);
                //childModels.set(i,new ModelRenderer2(modelBase,this, (ModelRenderer) childModels.get(i)));
            }
        }*/
    }

    public void render(float p_78785_1_) {
        this.updateBends(p_78785_1_);
        if (!this.compiled) {
            this.compileDisplayList(p_78785_1_);
        }

        GL11.glTranslatef(this.offsetX, this.offsetY, this.offsetZ);
        int i;
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

            GL11.glPopMatrix();
        }

        GL11.glTranslatef(-this.offsetX, -this.offsetY, -this.offsetZ);
    }
}
