package xiaobang.renderer;

import net.gobbob.mobends.client.model.ModelRendererBends;
import net.minecraft.client.model.ModelBase;
import org.lwjgl.opengl.GL11;
import xiaobang.Editor;

public class ModelRenderer2 extends ModelRendererBends {
    public ModelRendererBends source;
    private int textureId;

    private boolean render;

    public boolean bindTexture;
    //public PartRenderer part;
    public ModelRenderer2(ModelBase base, ModelRendererBends source,boolean bindTexture) {
        super(base);
        base.boxList.remove(this);
        //this.cubeList = source.cubeList;
        this.source = source;
        this.bindTexture = bindTexture;
        //this.part = partRenderer;
    }

    public ModelRenderer2(ModelBase base, ModelRendererBends source) {
        super(base);
        base.boxList.remove(this);
        //this.cubeList = source.cubeList;
        this.source = source;

        //this.part = partRenderer;
    }

    public void render(float par1){
        this.textureId = Editor.lastTextureId;
        //GL11.glScalef(0.5f,0.5f,0.5f);
        source.sync(this);
        this.render = true;
        //System.out.println("g : " + ModelBipedBody.g);
        //render2(par1);
        //source.sync(this);
    }



    public void render2(float par1){
        /*try{
            throw new Exception();
        }catch (Exception e){
            e.printStackTrace();
        }*/
        if(this.render) {
            if (bindTexture) {
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
            }
            source.render(par1);
            this.render = false;
        }
    }
}
