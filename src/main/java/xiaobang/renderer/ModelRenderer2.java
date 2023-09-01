package xiaobang.renderer;

import net.gobbob.mobends.client.model.ModelRendererBends;
import net.minecraft.client.model.ModelBiped;
import org.lwjgl.opengl.GL11;
import xiaobang.LWJGLTools;

import static xiaobang.ModelBipedBody.modelBendsPlayer;

public class ModelRenderer2 extends ModelRendererBends {
    public ModelBiped modelBiped;
    public ModelRendererBends source;

    public BodyRenderer bodyRenderer;
    public PartRenderer part;

    //public Function<ModelBiped, Function<ModelRendererBends, Void>> function;

    public ModelRenderer2(ModelRendererBends source, BodyRenderer bodyRenderer, PartRenderer part) {
        super(part.modelBase);
        this.modelBiped = (ModelBiped) part.modelBase;
        this.modelBiped.boxList.remove(this);
        this.source = source;
        this.bodyRenderer = bodyRenderer;
        this.part = part;
    }

    public void render(float par1) {//目前身体的转动无法应用
        source.sync(this);


        /*source.pre_rotation.setX(bodyRenderer.rotateAngleX * 57.295776F);
        source.pre_rotation.setY(bodyRenderer.rotateAngleY * 57.295776F);
        source.pre_rotation.setZ(bodyRenderer.rotateAngleZ * 57.295776F);*/

        GL11.glPushMatrix();
        //bodyRenderer.postRender(0.0625F);

        //part.postRender(0.0625F);

        LWJGLTools.loadMatrix(part.matrix);//加载模型视图矩阵

        /*if(Utils.translate.size() >= 1) {
            for (Vector3f translate : Utils.translate) {
                System.out.println(translate.getX() + "," + translate.getY() + "," + translate.getZ());
                GL11.glTranslatef(translate.getX(), translate.getY(), translate.getZ());
            }
            Utils.translate.clear();
        }
        if(Utils.rotate.size() >= 1) {
            for (Vector4f rotate : Utils.rotate) {
                GL11.glRotatef(rotate.getW(), rotate.getX(), rotate.getY(), rotate.getZ());
            }
            Utils.rotate.clear();
        }
        if(Utils.scale.size() >= 1) {
            for (Vector3f scale : Utils.translate) {
                GL11.glScalef(scale.getX(), scale.getY(), scale.getZ());
            }
            Utils.scale.clear();
        }*/
        source.render(par1);
        //GL11.glTranslatef(-bodyRenderer.offsetX,-bodyRenderer.offsetY,-bodyRenderer.offsetZ);
        GL11.glPopMatrix();
    }


}
