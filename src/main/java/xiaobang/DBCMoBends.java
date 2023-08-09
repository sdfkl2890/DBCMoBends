package xiaobang;


import JinRyuu.JBRA.ModelBipedDBC;
import JinRyuu.JBRA.RenderPlayerJBRA;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderPlayerEvent;

@Mod(modid = "dbcmobends", name = "DBC Mobends", version = "0.1", dependencies = "required-after:jinryuubetterrenderaddon;after:mobends")
@SideOnly(Side.CLIENT)
public class DBCMoBends {
    RenderPlayerDBCBends renderer = new RenderPlayerDBCBends();
    @Mod.EventHandler
    public void load(FMLInitializationEvent event) {
        renderer.setRenderManager(RenderManager.instance);
        RenderingRegistry.registerEntityRenderingHandler(EntityPlayer.class, renderer);
        FMLCommonHandler.instance().bus().register(this);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onRenderPlayer(RenderPlayerEvent.Pre event){
        if (RenderManager.instance.renderEngine != null && RenderManager.instance.livingPlayer != null) {
            if(event.entity instanceof EntityPlayer){
                EntityPlayer player = (EntityPlayer)event.entity;
                if (event.renderer instanceof RenderPlayerJBRA && !(event.renderer instanceof RenderPlayerDBCBends)){
                    //RenderPlayerJBRA jbra = (RenderPlayerJBRA) event.renderer;
                    //if(!(jbra.modelMain instanceof ModelBiped)){
                    //    jbra.modelBipedMain = jbra.modelMain = new ModelBiped(0.0F,0.0F,64,32);
                    //}
                    event.setCanceled(true);
                    Minecraft mc = Minecraft.getMinecraft();
                    double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * event.partialRenderTick;
                    double d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * event.partialRenderTick;
                    double d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * event.partialRenderTick;
                    double d3 = mc.thePlayer.lastTickPosX + (mc.thePlayer.posX - mc.thePlayer.lastTickPosX) * event.partialRenderTick;
                    double d4 = mc.thePlayer.lastTickPosY + (mc.thePlayer.posY - mc.thePlayer.lastTickPosY) * event.partialRenderTick;
                    double d5 = mc.thePlayer.lastTickPosZ + (mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ) * event.partialRenderTick;
                    this.renderer.doRender(player,d0 - d3,d1 - d4,d2 - d5,0,event.partialRenderTick);
                }
            }
        }
    }
}
