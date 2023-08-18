package xiaobang.renderer;

import JinRyuu.JRMCore.entity.ModelBipedBody;
import net.gobbob.mobends.client.model.ModelRendererBends;
import org.lwjgl.opengl.GL11;
import xiaobang.LWJGLTools;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Iterator;

public class ModelRenderer2 extends ModelRendererBends {
    public ModelBipedBody modelBipedBody;
    public ModelRendererBends source;

    public PartRenderer part;

    public ModelRenderer2(ModelBipedBody base, ModelRendererBends source,PartRenderer part) {
        super(base);
        this.modelBipedBody = base;
        base.boxList.remove(this);
        this.source = source;
        this.part = part;
    }

    public void render(float par1) {
        source.sync(this);

        //xiaobang.ModelBipedBody.setOffset(source,part);
        GL11.glPushMatrix();
        LWJGLTools.addMatrix(part.matrix);//将模型视图矩阵叠加到当前
        GL11.glTranslatef(0f,0f,-0.0886f);//模型前移
        source.render(par1);
        GL11.glPopMatrix();
    }
    public static class ModelRenderer3 extends ModelRenderer2{
        private ArrayList<Render> renders = new ArrayList<>();
        private boolean render;

        public boolean bindTexture = true;
        public ModelRenderer3(ModelBipedBody base, ModelRendererBends source, PartRenderer part) {
            super(base, source, part);
        }

        public void render(float par1){
            if(this.bindTexture) {
                this.renders.add(new Render(LWJGLTools.getCurrentBindingTexture(), LWJGLTools.getCurrentColorRGBA(),LWJGLTools.getCurrentModelViewMatrix()));
            }

            source.sync(this);
            this.render = true;

        }



        public void render2(float par1) {
            if (this.render) {
                GL11.glPushMatrix();
                //source.render(par1);
                if (this.renders == null) {
                    this.renders = new ArrayList<>();
                }
                if (bindTexture) {
                    for (Iterator<Render> it = this.renders.iterator(); it.hasNext(); ) {
                        GL11.glPushMatrix();
                        Render render = it.next();
                        FloatBuffer colors = render.getColors();
                        //LWJGLTools.multMatrix(render.getModelViewMartix());
                        GL11.glColor4f(colors.get(0), colors.get(1), colors.get(2), colors.get(3));
                        GL11.glBindTexture(GL11.GL_TEXTURE_2D, render.getTextureId());
                        source.render(par1);
                        it.remove();
                        GL11.glPopMatrix();
                    }
                }
                GL11.glPopMatrix();

                this.renders.clear();
                this.render = false;
            }
        }
    }


}
