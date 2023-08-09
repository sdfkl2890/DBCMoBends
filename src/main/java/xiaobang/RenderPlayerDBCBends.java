package xiaobang;

import JinRyuu.JBRA.ModelBipedDBC;
import JinRyuu.JBRA.RenderPlayerJBRA;
import net.gobbob.mobends.MoBends;
import net.gobbob.mobends.client.model.entity.ModelBendsPlayer;
import net.gobbob.mobends.data.Data_Player;
import net.gobbob.mobends.settings.SettingsBoolean;
import net.gobbob.mobends.settings.SettingsNode;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

public class RenderPlayerDBCBends extends RenderPlayerJBRA {
    public int refreshModel = 0;

    public RenderPlayerDBCBends(){
        super();
        this.modelMain = new ModelBiped(0.0F,0.0F,64,32);
        this.mainModel = this.modelMain;
        this.modelArmorChestplate = new ModelBiped(1.0F,0.0F,64,32);
        this.modelArmor = new ModelBiped(0.5F,0.0F,64,32);
    }



    private float interpolateRotation(float p_77034_1_, float p_77034_2_, float p_77034_3_) {
        float f3;
        for (f3 = p_77034_2_ - p_77034_1_; f3 < -180.0F; f3 += 360.0F);
        while (f3 >= 180.0F)
            f3 -= 360.0F;
        return p_77034_1_ + p_77034_3_ * f3;
    }

    protected void rotateSuperCorpse(EntityLivingBase p_77043_1_, float p_77043_2_, float p_77043_3_, float p_77043_4_) {
        GL11.glRotatef(180.0F - p_77043_3_, 0.0F, 1.0F, 0.0F);
        if (p_77043_1_.deathTime > 0) {
            float f3 = (p_77043_1_.deathTime + p_77043_4_ - 1.0F) / 20.0F * 1.6F;
            f3 = MathHelper.sqrt_float(f3);
            if (f3 > 1.0F)
                f3 = 1.0F;
            GL11.glRotatef(f3 * getDeathMaxRotation(p_77043_1_), 0.0F, 0.0F, 1.0F);
        } else {
            String s = EnumChatFormatting.getTextWithoutFormattingCodes(p_77043_1_.getCommandSenderName());
            if ((s.equals("Dinnerbone") || s.equals("Grumm")) && (!(p_77043_1_ instanceof EntityPlayer) || !((EntityPlayer)p_77043_1_).getHideCape())) {
                GL11.glTranslatef(0.0F, p_77043_1_.height + 0.1F, 0.0F);
                GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
            }
        }
    }

    public void doRender(AbstractClientPlayer p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_){
        if (this.refreshModel != MoBends.refreshModel) {
            this.mainModel = new ModelBiped(0.0F,0.0F,64,32);
            this.modelBipedMain = (net.minecraft.client.model.ModelBiped) this.mainModel;
            this.modelArmorChestplate = new ModelBiped(1.0F,0.0F,64,32);
            this.modelArmor = new ModelBiped(0.5F,0.0F,64,32);
            this.refreshModel = MoBends.refreshModel;
        }
        Data_Player data = Data_Player.get(p_76986_1_.getEntityId());
        float f2 = interpolateRotation(p_76986_1_.prevRenderYawOffset, p_76986_1_.renderYawOffset, p_76986_9_);
        if (((SettingsBoolean) SettingsNode.getSetting("swordTrail")).data) {
            GL11.glPushMatrix();
            float f5 = 0.0625F;
            float f4 = handleRotationFloat(p_76986_1_, p_76986_9_);
            rotateSuperCorpse(p_76986_1_, f4, f2, p_76986_9_);
            GL11.glTranslatef(0.0F, -24.0F * f5 - 0.0078125F - 2.0F * f5, 0.0F);
            GL11.glScalef(f5, f5, f5);
            data.swordTrail.render(((ModelBiped)this.modelBipedMain).modelBendsPlayer);
            GL11.glPopMatrix();
        }
        super.doRender(p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }

    protected void rotateCorpse(AbstractClientPlayer p_77043_1_, float p_77043_2_, float p_77043_3_, float p_77043_4_) {
        if (p_77043_1_.isEntityAlive() && p_77043_1_.isPlayerSleeping()) {
            GL11.glRotatef(p_77043_1_.getBedOrientationInDegrees(), 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(this.getDeathMaxRotation(p_77043_1_), 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(270.0F, 0.0F, 1.0F, 0.0F);
        }
        else {
            super.rotateCorpse(p_77043_1_, p_77043_2_, p_77043_3_, p_77043_4_);
            ((ModelBiped)this.modelBipedMain).updateWithEntityData(p_77043_1_);
            ((ModelBiped)this.modelBipedMain).postRender(0.0625F);
        }
    }
}
