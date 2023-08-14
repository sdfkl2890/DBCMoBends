package xiaobang.renderer;

import JinRyuu.JBRA.ModelRendererJBRA;
import net.minecraft.client.model.ModelBase;
import xiaobang.LWJGLTools;

import java.nio.FloatBuffer;

public class ModelRendererJBRA2 extends ModelRendererJBRA {
    public ModelRendererJBRA source;
    private boolean render;

    protected static int textureId;
    protected static FloatBuffer colors;

    public ModelRendererJBRA2(ModelBase modelBase,ModelRendererJBRA jbra) {
        super(modelBase);
        modelBase.boxList.remove(this);
        this.source = jbra;
    }

    public void render(float par1){
        //System.out.println("render jbra");
        if(LWJGLTools.getCurrentBindingTexture() != textureId || LWJGLTools.getCurrentColorRGBA().compareTo(colors) != 0) {
            textureId = LWJGLTools.getCurrentBindingTexture();
            colors = LWJGLTools.getCurrentColorRGBA();
        }
        source.rotationPointX = this.rotationPointX;
        source.rotationPointY = this.rotationPointY;
        source.rotationPointZ = this.rotationPointZ;
        source.rotateAngleX = this.rotateAngleX;
        source.rotateAngleY = this.rotateAngleY;
        source.rotateAngleZ = this.rotateAngleZ;
        source.lengthY = this.lengthY;
        source.sizeXZ = this.sizeXZ;
        source.showModel = this.showModel;
        this.render = true;
    }

    public void render2(float par1){
        if(this.render) {
            source.render(par1);
            this.render = false;
        }
    }
}
