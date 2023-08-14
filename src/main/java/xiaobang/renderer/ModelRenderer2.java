package xiaobang.renderer;

import net.gobbob.mobends.client.model.ModelRendererBends;
import net.minecraft.client.model.ModelBase;
import org.lwjgl.opengl.GL11;
import xiaobang.LWJGLTools;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Iterator;

public class ModelRenderer2 extends ModelRendererBends {
    public ModelRendererBends source;

    private ArrayList<Render> renders = new ArrayList<>();
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
        if(this.bindTexture) {
            this.renders.add(new Render(LWJGLTools.getCurrentBindingTexture(), LWJGLTools.getCurrentColorRGBA()));
        }
        //GL11.glScalef(0.5f,0.5f,0.5f);
        source.sync(this);
        this.render = true;
        //System.out.println("g : " + ModelBipedBody.g);
        //render2(par1);
        //source.sync(this);
    }



    public void render2(float par1){
        if(this.render) {
            GL11.glPushMatrix();
            if (this.renders == null){
                this.renders = new ArrayList<>();
            }
            if (bindTexture) {
                for (Iterator<Render> it = this.renders.iterator(); it.hasNext();) {
                    Render render = it.next();
                    FloatBuffer colors = render.getColors();
                    GL11.glColor4f(colors.get(0), colors.get(1), colors.get(2), colors.get(3));
                    GL11.glBindTexture(GL11.GL_TEXTURE_2D, render.getTextureId());
                    source.render(par1);
                    it.remove();
                }
            }
            if(this.source == this){
                super.render(par1);
            }else {
                source.render(par1);

            }
            GL11.glPopMatrix();

            this.renders.clear();
            this.render = false;
        }
    }
}
