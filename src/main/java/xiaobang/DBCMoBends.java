package xiaobang;

import JinRyuu.JBRA.RenderPlayerJBRA;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.gobbob.mobends.data.Data_Player;
import net.gobbob.mobends.settings.SettingsBoolean;
import net.gobbob.mobends.settings.SettingsNode;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
@Mod(modid = "dbcmobends", name = "DBCMoBends", version = "0.1", dependencies = "required-after:jinryuubetterrenderaddon;after:mobends")
public class DBCMoBends {
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onRenderPlayer(RenderPlayerEvent.Pre event) {
        if (RenderManager.instance.renderEngine != null && RenderManager.instance.livingPlayer != null) {
            if (event.entity instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) event.entity;
                if (event.renderer instanceof RenderPlayerJBRA) {
                    if (!(event.renderer.modelArmor instanceof JinRyuu.JRMCore.entity.ModelBipedBody)) {
                        event.renderer.modelArmor = new JinRyuu.JRMCore.entity.ModelBipedBody(0.5F);
                    }
                    if (!(event.renderer.modelArmorChestplate instanceof JinRyuu.JRMCore.entity.ModelBipedBody)) {
                        event.renderer.modelArmorChestplate = new JinRyuu.JRMCore.entity.ModelBipedBody(1.0F);
                    }
                    Data_Player data = Data_Player.get(player.getEntityId());
                    float f2 = interpolateRotation(player.prevRenderYawOffset, player.renderYawOffset, event.partialRenderTick);
                    if (((SettingsBoolean) SettingsNode.getSetting("swordTrail")).data) {
                        GL11.glPushMatrix();
                        float f5 = 0.0625F;
                        rotateSuperCorpse(player, f2, event.partialRenderTick);
                        GL11.glTranslatef(0.0F, -24.0F * f5 - 0.0078125F - 2.0F * f5, 0.0F);
                        GL11.glScalef(f5, f5, f5);
                        data.swordTrail.render(ModelBipedBody.modelBendsPlayer);
                        GL11.glPopMatrix();
                    }
                }
            }
        }
    }


    private void rotateSuperCorpse(EntityLivingBase p_77043_1_, float p_77043_3_, float p_77043_4_) {
        GL11.glRotatef(180.0F - p_77043_3_, 0.0F, 1.0F, 0.0F);
        if (p_77043_1_.deathTime > 0) {
            float f3 = (p_77043_1_.deathTime + p_77043_4_ - 1.0F) / 20.0F * 1.6F;
            f3 = MathHelper.sqrt_float(f3);
            if (f3 > 1.0F)
                f3 = 1.0F;
            GL11.glRotatef(f3 * 90.0F, 0.0F, 0.0F, 1.0F);
        } else {
            String s = EnumChatFormatting.getTextWithoutFormattingCodes(p_77043_1_.getCommandSenderName());
            if ((s.equals("Dinnerbone") || s.equals("Grumm")) && (!(p_77043_1_ instanceof EntityPlayer) || !((EntityPlayer) p_77043_1_).getHideCape())) {
                GL11.glTranslatef(0.0F, p_77043_1_.height + 0.1F, 0.0F);
                GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
            }
        }
    }

    private float interpolateRotation(float p_77034_1_, float p_77034_2_, float p_77034_3_) {
        float f3 = p_77034_2_ - p_77034_1_;
        while (f3 < -180.0F)
            f3 += 360.0F;
        while (f3 >= 180.0F)
            f3 -= 360.0F;
        return p_77034_1_ + p_77034_3_ * f3;
    }
}
