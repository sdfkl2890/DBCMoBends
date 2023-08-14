package xiaobang;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

import static xiaobang.ModelBipedBody.modelBendsPlayer;

@SideOnly(Side.CLIENT)
public class Editor {



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

    public static void renderFirstPersonArm(EntityPlayer player){

    }


}
