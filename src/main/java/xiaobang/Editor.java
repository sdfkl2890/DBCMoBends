package xiaobang;

import JinRyuu.JBRA.ModelBipedDBC;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.gobbob.mobends.client.model.ModelRendererBends;
import net.minecraft.client.entity.AbstractClientPlayer;
import org.lwjgl.opengl.GL11;

import static xiaobang.ModelBipedBody.modelBendsPlayer;

@SideOnly(Side.CLIENT)
public class Editor {



    public static void hairRotate(ModelBipedDBC dbc,float par1){
        ModelRendererBends head = (ModelRendererBends) dbc.bipedHead;
        GL11.glTranslatef(head.rotationPointX * par1, head.rotationPointY * par1, head.rotationPointZ * par1);
        GL11.glRotatef(-head.pre_rotation.getY(), 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(head.pre_rotation.getX(), 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(head.pre_rotation.getZ(), 0.0F, 0.0F, 1.0F);
        if (head.rotateAngleZ != 0.0F) {
            GL11.glRotatef(head.rotateAngleZ * 57.295776F, 0.0F, 0.0F, 1.0F);
        }
        if(head.rotateAngleY != 0.0F){
            GL11.glRotatef(head.rotateAngleY * 57.295776F, 0.0F, 1.0F, 0.0F);
        }
        if(head.rotateAngleX != 0.0F){
            GL11.glRotatef(head.rotateAngleX * 57.295776F, 1.0F, 0.0F, 0.0F);
        }
        //if(dbc.bipedHead instanceof ModelRendererBends) {

        //}
        GL11.glPushMatrix();
    }


    public static void rotateCorpse(AbstractClientPlayer player) {
        if (player.isEntityAlive() && player.isPlayerSleeping()) {
            GL11.glRotatef(player.getBedOrientationInDegrees(), 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(270.0F, 0.0F, 1.0F, 0.0F);
        } else {
            modelBendsPlayer.updateWithEntityData(player);
            modelBendsPlayer.postRender(0.0625F);
        }
    }




}
