package xiaobang;

import JinRyuu.JBRA.ModelBipedDBC;
import JinRyuu.JBRA.ModelRendererJBRA;
import JinRyuu.JRMCore.JRMCoreConfig;
import JinRyuu.JRMCore.JRMCoreH;
import joptsimple.internal.Strings;
import net.gobbob.mobends.AnimatedEntity;
import net.gobbob.mobends.client.model.ModelRendererBends;
import net.gobbob.mobends.client.model.entity.ModelBendsPlayer;
import net.gobbob.mobends.data.Data_Player;
import net.gobbob.mobends.pack.BendsPack;
import net.gobbob.mobends.pack.BendsVar;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;
import xiaobang.renderer.*;

import java.lang.reflect.Field;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static JinRyuu.JRMCore.entity.ModelBipedBody.*;

public class ModelBipedBody {//对JinRyuu.JRMCore.entity.ModelBipedBody的修改

    public static ModelBendsPlayer modelBendsPlayer = new ModelBendsPlayer();

    static Field rightForeArm;
    static Field leftForeArm;
    static Field rightForeLeg;
    static Field leftForeLeg;


    static HashMap<String, Integer> keys = new HashMap<>();

    static {
        try {
            rightForeArm = JinRyuu.JRMCore.entity.ModelBipedBody.class.getField("bipedRightForeArm");
            rightForeArm.setAccessible(true);
            leftForeArm = JinRyuu.JRMCore.entity.ModelBipedBody.class.getField("bipedLeftForeArm");
            leftForeArm.setAccessible(true);
            leftForeLeg = JinRyuu.JRMCore.entity.ModelBipedBody.class.getField("bipedLeftForeLeg");
            leftForeLeg.setAccessible(true);
            rightForeLeg = JinRyuu.JRMCore.entity.ModelBipedBody.class.getField("bipedRightForeLeg");
            rightForeLeg.setAccessible(true);
            for (Field field : Keyboard.class.getFields()) {
                if (field.getName().startsWith("KEY_")) {
                    field.setAccessible(true);
                    keys.put(field.getName(), (Integer) field.get(null));//获取各个键盘按键
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void init(JinRyuu.JRMCore.entity.ModelBipedBody body, float par1) {//JinRyuu.JRMCore.entity.ModelBipedBody初始化方法插入
        try {
            System.out.println(body.getClass().getName() + par1);
            ModelRendererBends bipedRightForeArm = new ModelRendererBends(body, 40, 22);
            bipedRightForeArm.addBox(0.0F, 0.0F, -4.0F, 4, 6, 4, par1);
            bipedRightForeArm.setRotationPoint(-3.0F, 4.0F, 2.0F);
            bipedRightForeArm.getBox().offsetTextureQuad(bipedRightForeArm, 3, 0.0F, -6.0F);

            ModelRendererBends bipedLeftForeArm = new ModelRendererBends(body, 40, 22);
            bipedLeftForeArm.mirror = true;
            bipedLeftForeArm.addBox(0.0F, 0.0F, -4.0F, 4, 6, 4, par1);
            bipedLeftForeArm.setRotationPoint(-1.0F, 4.0F, 2.0F);
            bipedLeftForeArm.getBox().offsetTextureQuad(bipedRightForeArm, 3, 0.0F, -6.0F);

            ModelRendererBends bipedRightForeLeg = new ModelRendererBends(body, 0, 22);
            bipedRightForeLeg.addBox(-2.0F, 0.0F, 0.0F, 4, 6, 4, par1);
            bipedRightForeLeg.setRotationPoint(0.0F, 6.0f, -2.0F);
            bipedRightForeLeg.getBox().offsetTextureQuad(bipedRightForeLeg, 3, 0.0F, -6.0F);

            ModelRendererBends bipedLeftForeLeg = new ModelRendererBends(body, 0, 22);
            bipedLeftForeLeg.mirror = true;
            bipedLeftForeLeg.addBox(-2.0F, 0.0F, 0.0F, 4, 6, 4, par1);
            bipedLeftForeLeg.setRotationPoint(0.0F, 6.0F, -2.0F);
            bipedLeftForeLeg.getBox().offsetTextureQuad(bipedLeftForeLeg, 3, 0.0F, -6.0F);

            Utils.setField(rightForeArm, body, bipedRightForeArm);
            Utils.setField(leftForeArm, body, bipedLeftForeArm);
            Utils.setField(rightForeLeg, body, bipedRightForeLeg);
            Utils.setField(leftForeLeg, body, bipedLeftForeLeg);

            BodyRenderer bodyRenderer = Utils.convertBends(body, BodyRenderer.class, body.bipedBody);
            bodyRenderer.setRotationPointY2(par1 + 12.0F);
            //bodyRenderer.setRotationPointY2(par1 + 12.0F);//设置旋转原点为12.0F，但子模型仍然使用旋转原点0.0F
            body.bipedBody = bodyRenderer;
            //body.bipedBody.rotationPointY = 0;
            body.bipedBody.cubeList.clear();
            body.bipedBody.addBox(-4.0F, -12.0F, -2.0F, 8, 12, 4, par1);

            //body.bipedBody.cubeList.clear();
            //body.bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, par1);

            /*ModelRenderer2 rightArm = new ModelRenderer2(body, ((PartRenderer_SeperatedChild)convertBends(body, PartRenderer_SeperatedChild.class, body.bipedRightArm)).setMother((ModelRendererBends) body.bipedBody).setSeperatedPart(bipedRightForeArm));
            rightArm.source.cubeList.clear();
            rightArm.source.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, par1);
            ((BodyRenderer)body.bipedBody).rightArm = (PartRenderer) rightArm.source;
            ModelRenderer2 leftArm = new ModelRenderer2(body, ((PartRenderer_SeperatedChild) convertBends(body, PartRenderer_SeperatedChild.class, body.bipedLeftArm)).setMother((ModelRendererBends) body.bipedBody).setSeperatedPart(bipedLeftForeArm));
            leftArm.source.cubeList.clear();
            leftArm.source.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, par1);
            ((BodyRenderer)body.bipedBody).leftArm = (PartRenderer) leftArm.source;*/


            body.bipedHead = bodyRenderer.head = Utils.convertBends(body, PartRenderer.class, body.bipedHead);
            body.bipedHead.setRotationPoint(0.0F, par1 - 12.0F, 0.0F);

            bodyRenderer.head.part = "head";


            body.bipedHeadwear = Utils.convertBends(body, ModelRendererBends.class, body.bipedHeadwear);
            body.bipedHeadwear.cubeList.clear();
            body.bipedHeadwear.addBox(-4.0F, -8.0F, -4.0F, 8,8,8, par1 + 0.5F);

            body.bipedRightArm = bodyRenderer.rightArm = Utils.convertBends(body, PartRenderer_SeperatedChild.class, body.bipedRightArm).setMother((ModelRendererBends) body.bipedBody).setSeperatedPart(bipedRightForeArm);
            body.bipedRightArm.cubeList.clear();
            body.bipedRightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 6, 4, par1);


            body.bipedLeftArm = bodyRenderer.leftArm = Utils.convertBends(body, PartRenderer_SeperatedChild.class, body.bipedLeftArm).setMother((ModelRendererBends) body.bipedBody).setSeperatedPart(bipedLeftForeArm);
            body.bipedLeftArm.cubeList.clear();
            body.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 6, 4, par1);


            body.bipedRightLeg = bodyRenderer.rightLeg = Utils.convertBends(body, PartRenderer.class, body.bipedRightLeg);
            body.bipedRightLeg.cubeList.clear();
            body.bipedRightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, par1);


            body.bipedLeftLeg = bodyRenderer.leftLeg =  Utils.convertBends(body, PartRenderer.class, body.bipedLeftLeg);
            body.bipedLeftLeg.cubeList.clear();
            body.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, par1);

            //body.bipedHead.offsetY -= 12;
            //body.bipedRightArm.offsetY -= 12;
            //body.bipedLeftArm.offsetY -= 12;

            //body.bipedHead.addChild(body.bipedHeadwear);
            body.bipedBody.addChild(body.bipedHead);
            body.bipedBody.addChild(body.bipedRightArm);
            body.bipedBody.addChild(body.bipedLeftArm);
            //body.bipedBody.addChild(body.bipedRightLeg);
            //body.bipedBody.addChild(body.bipedLeftLeg);
            body.bipedRightArm.addChild(bipedRightForeArm);
            body.bipedLeftArm.addChild(bipedLeftForeArm);
            body.bipedRightLeg.addChild(bipedRightForeLeg);
            body.bipedLeftLeg.addChild(bipedLeftForeLeg);


            ((ModelRendererBends) body.bipedRightArm).offsetBox_Add(-0.01F, 0.0F, -0.01F).resizeBox(4.02F, 6.0F, 4.02F).updateVertices();
            ((ModelRendererBends) body.bipedLeftArm).offsetBox_Add(-0.01F, 0.0F, -0.01F).resizeBox(4.02F, 6.0F, 4.02F).updateVertices();
            ((ModelRendererBends) body.bipedRightLeg).offsetBox_Add(-0.01F, 0.0F, -0.01F).resizeBox(4.02F, 6.0F, 4.02F).updateVertices();
            ((ModelRendererBends) body.bipedLeftLeg).offsetBox_Add(-0.01F, 0.0F, -0.01F).resizeBox(4.02F, 6.0F, 4.02F).updateVertices();


            //System.out.println("edit " + body.getClass().getName());
        } catch (IllegalAccessException exception) {
            throw new RuntimeException(exception);
        }
    }

    //将MoBends的动作应用到龙珠
    public static void setRotationAngles(JinRyuu.JRMCore.entity.ModelBipedBody body, float argSwingTime, float argSwingAmount, float argArmSway, float argHeadY, float argHeadX, float argNr6, Entity argEntity) {
        //System.out.println("rotation " + body.getClass().getName());
        try {
            if (rightForeArm.get(body) == null) {//实际上这不太可能触发
                init(body, 0f);
            }
        } catch (Exception e) {
            System.out.println(body.getClass().getName());
            throw new RuntimeException(e);
        }
        if (Minecraft.getMinecraft().theWorld != null) {
            if (!Minecraft.getMinecraft().theWorld.isRemote || !Minecraft.getMinecraft().isGamePaused()) {
                Data_Player data = Data_Player.get(argEntity.getEntityId());
                modelBendsPlayer.armSwing = argSwingTime;
                modelBendsPlayer.armSwingAmount = argSwingAmount;
                modelBendsPlayer.headRotationX = argHeadX;
                modelBendsPlayer.headRotationY = argHeadY;
                if (Minecraft.getMinecraft().currentScreen != null) {
                    modelBendsPlayer.headRotationY = 0.0F;
                }

                ((ModelRendererBends) modelBendsPlayer.bipedHead).sync(data.head);
                //((ModelRendererBends) modelBendsPlayer.bipedHeadwear).sync(data.headwear);
                ((ModelRendererBends) modelBendsPlayer.bipedBody).sync(data.body);
                ((ModelRendererBends) modelBendsPlayer.bipedRightArm).sync(data.rightArm);
                ((ModelRendererBends) modelBendsPlayer.bipedLeftArm).sync(data.leftArm);
                ((ModelRendererBends) modelBendsPlayer.bipedRightLeg).sync(data.rightLeg);
                ((ModelRendererBends) modelBendsPlayer.bipedLeftLeg).sync(data.leftLeg);
                ((ModelRendererBends) modelBendsPlayer.bipedRightForeArm).sync(data.rightForeArm);
                ((ModelRendererBends) modelBendsPlayer.bipedLeftForeArm).sync(data.leftForeArm);
                ((ModelRendererBends) modelBendsPlayer.bipedRightForeLeg).sync(data.rightForeLeg);
                ((ModelRendererBends) modelBendsPlayer.bipedLeftForeLeg).sync(data.leftForeLeg);
                modelBendsPlayer.renderOffset.set(data.renderOffset);
                modelBendsPlayer.renderRotation.set(data.renderRotation);
                modelBendsPlayer.renderItemRotation.set(data.renderItemRotation);
                modelBendsPlayer.swordTrail = data.swordTrail;
                if (Data_Player.get(argEntity.getEntityId()).canBeUpdated()) {


                    modelBendsPlayer.renderOffset.setSmooth(new Vector3f(0.0F, -1.0F, 0.0F), 0.5F);
                    modelBendsPlayer.renderRotation.setSmooth(new Vector3f(0.0F, 0.0F, 0.0F), 0.5F);
                    modelBendsPlayer.renderItemRotation.setSmooth(new Vector3f(0.0F, 0.0F, 0.0F), 0.5F);
                    ((ModelRendererBends) modelBendsPlayer.bipedHead).resetScale();
                    //((ModelRendererBends) modelBendsPlayer.bipedHeadwear).resetScale();
                    ((ModelRendererBends) modelBendsPlayer.bipedBody).resetScale();
                    ((ModelRendererBends) modelBendsPlayer.bipedRightArm).resetScale();
                    ((ModelRendererBends) modelBendsPlayer.bipedLeftArm).resetScale();
                    ((ModelRendererBends) modelBendsPlayer.bipedRightLeg).resetScale();
                    ((ModelRendererBends) modelBendsPlayer.bipedLeftLeg).resetScale();
                    ((ModelRendererBends) modelBendsPlayer.bipedRightForeArm).resetScale();
                    ((ModelRendererBends) modelBendsPlayer.bipedLeftForeArm).resetScale();
                    ((ModelRendererBends) modelBendsPlayer.bipedRightForeLeg).resetScale();
                    ((ModelRendererBends) modelBendsPlayer.bipedLeftForeLeg).resetScale();
                    BendsVar.tempData = Data_Player.get(argEntity.getEntityId());
                    if (argEntity.isRiding()) {
                        AnimatedEntity.getByEntity(argEntity).get("riding").animate((EntityLivingBase) argEntity, modelBendsPlayer, Data_Player.get(argEntity.getEntityId()));
                        BendsPack.animate(modelBendsPlayer, "player", "riding");
                    } else if (argEntity.isInWater()) {
                        AnimatedEntity.getByEntity(argEntity).get("swimming").animate((EntityLivingBase) argEntity, modelBendsPlayer, Data_Player.get(argEntity.getEntityId()));
                        BendsPack.animate(modelBendsPlayer, "player", "swimming");
                    } else if (!Data_Player.get(argEntity.getEntityId()).isOnGround() | Data_Player.get(argEntity.getEntityId()).ticksAfterTouchdown < 2.0F) {
                        AnimatedEntity.getByEntity(argEntity).get("jump").animate((EntityLivingBase) argEntity, modelBendsPlayer, Data_Player.get(argEntity.getEntityId()));
                        BendsPack.animate(modelBendsPlayer, "player", "jump");
                        //if (modelBendsPlayer.bipedBody != null) {
                        //    ((ModelRendererBends) modelBendsPlayer.bipedBody).rotation = new net.gobbob.mobends.util.SmoothVector3f();
                        //}

                    } else {
                        if (Data_Player.get(argEntity.getEntityId()).motion.x == 0.0F & Data_Player.get(argEntity.getEntityId()).motion.z == 0.0F) {
                            AnimatedEntity.getByEntity(argEntity).get("stand").animate((EntityLivingBase) argEntity, modelBendsPlayer, Data_Player.get(argEntity.getEntityId()));
                            BendsPack.animate(modelBendsPlayer, "player", "stand");
                        } else if (argEntity.isSprinting()) {
                            AnimatedEntity.getByEntity(argEntity).get("sprint").animate((EntityLivingBase) argEntity, modelBendsPlayer, Data_Player.get(argEntity.getEntityId()));
                            BendsPack.animate(modelBendsPlayer, "player", "sprint");
                        } else {
                            AnimatedEntity.getByEntity(argEntity).get("walk").animate((EntityLivingBase) argEntity, modelBendsPlayer, Data_Player.get(argEntity.getEntityId()));
                            BendsPack.animate(modelBendsPlayer, "player", "walk");
                        }

                        if (argEntity.isSneaking()) {
                            AnimatedEntity.getByEntity(argEntity).get("sneak").animate((EntityLivingBase) argEntity, modelBendsPlayer, Data_Player.get(argEntity.getEntityId()));
                            BendsPack.animate(modelBendsPlayer, "player", "sneak");

                        }
                    }

                    if (modelBendsPlayer.aimedBow) {
                        AnimatedEntity.getByEntity(argEntity).get("bow").animate((EntityLivingBase) argEntity, modelBendsPlayer, Data_Player.get(argEntity.getEntityId()));
                        BendsPack.animate(modelBendsPlayer, "player", "bow");
                    } else if (((EntityPlayer) argEntity).getCurrentEquippedItem() != null && ((EntityPlayer) argEntity).getCurrentEquippedItem().getItem() instanceof ItemPickaxe || ((EntityPlayer) argEntity).getCurrentEquippedItem() != null && Block.getBlockFromItem(((EntityPlayer) argEntity).getCurrentEquippedItem().getItem()) != Blocks.air) {
                        AnimatedEntity.getByEntity(argEntity).get("mining").animate((EntityLivingBase) argEntity, modelBendsPlayer, Data_Player.get(argEntity.getEntityId()));
                        BendsPack.animate(modelBendsPlayer, "player", "mining");
                    } else if (((EntityPlayer) argEntity).getCurrentEquippedItem() != null && ((EntityPlayer) argEntity).getCurrentEquippedItem().getItem() instanceof ItemAxe) {
                        AnimatedEntity.getByEntity(argEntity).get("axe").animate((EntityLivingBase) argEntity, modelBendsPlayer, Data_Player.get(argEntity.getEntityId()));
                        BendsPack.animate(modelBendsPlayer, "player", "axe");
                    } else {
                        AnimatedEntity.getByEntity(argEntity).get("attack").animate((EntityLivingBase) argEntity, modelBendsPlayer, Data_Player.get(argEntity.getEntityId()));
                        BendsPack.animate(modelBendsPlayer, "player", "attack");
                        //if (modelBendsPlayer.bipedBody != null) {
                        //    ((ModelRendererBends) modelBendsPlayer.bipedBody).rotation = new net.gobbob.mobends.util.SmoothVector3f();
                        //}

                    }

                    if (JinRyuu.JRMCore.i.ExtendedPlayer.get((EntityPlayer) argEntity).getBlocking() == 1) {
                        BendsPack.animate(modelBendsPlayer, "player", "defense");//防御
                    } else if (JinRyuu.JRMCore.i.ExtendedPlayer.get((EntityPlayer) argEntity).getBlocking() == 2) {
                        BendsPack.animate(modelBendsPlayer, "player", "instantTransmissionON");//瞬间移动
                    }
                    int i = Utils.containsPlayer(argEntity.getCommandSenderName());
                    if (i != 9527) {
                        if (JRMCoreH.StusEfctsClient(7, i)) {
                            BendsPack.animate(modelBendsPlayer, "player", "swoop");//俯冲
                        } else if (JRMCoreH.StusEfctsClient(1, i)) {
                            BendsPack.animate(modelBendsPlayer, "player", "ascend");//变身
                        } else if (JRMCoreH.StusEfctsClient(4, i)) {
                            BendsPack.animate(modelBendsPlayer, "player", "auraOn");//聚气
                        } else if (JRMCoreH.StusEfctsClient(3, i)) {
                            BendsPack.animate(modelBendsPlayer, "player", "turbo");//爆气
                        } else if (JRMCoreH.StusEfctsClient(5, i)) {
                            BendsPack.animate(modelBendsPlayer, "player", "kaioken");//界王拳
                        }
                    }
                    if (((!data.isOnGround() ? 1 : 0) | ((data.ticksAfterTouchdown < 2.0) ? 1 : 0)) != 0) {
                        if ((((data.motion.x != 0.0) ? 1 : 0) | ((data.motion.z != 0.0) ? 1 : 0)) != 0) {
                            if (i != 9527) {
                                if (JRMCoreH.StusEfctsClient(7, i) && modelBendsPlayer.bipedHead != null) {//玩家冲刺时调整头部向上旋转90度
                                    ((ModelRendererBends) modelBendsPlayer.bipedHead).rotation.setSmoothX(modelBendsPlayer.headRotationX - 90, 0.3F);
                                }
                            }
                        }
                    }

                    boolean isNotFused = !JRMCoreH.isFused(argEntity);
                    boolean isFusionSelectedAvailable = (JRMCoreH.PlyrSettingsB((EntityPlayer) argEntity, 4) && isNotFused);
                    if (isFusionSelectedAvailable && JRMCoreConfig.fuzn) {//融合,fusion+变身条进度
                        BendsPack.animate(modelBendsPlayer, "player", "fusion" + JRMCoreH.TransSaiCurRg);
                    }

                    if (Minecraft.getMinecraft().thePlayer.getEntityId() == data.entityID) {
                        for (String key : keys.keySet()) {
                            if (Keyboard.isKeyDown(keys.get(key))) {//键盘按键触发
                                BendsPack.animate(modelBendsPlayer, "player", key);
                            }
                        }
                    }

                    //((ModelRendererBends)modelBendsPlayer.bipedBody).rotation = new SmoothVector3f();

                    ((ModelRendererBends) modelBendsPlayer.bipedHead).update(data.ticksPerFrame);
                    //((ModelRendererBends) modelBendsPlayer.bipedHeadwear).update(data.ticksPerFrame);
                    ((ModelRendererBends) modelBendsPlayer.bipedBody).update(data.ticksPerFrame);
                    ((ModelRendererBends) modelBendsPlayer.bipedLeftArm).update(data.ticksPerFrame);
                    ((ModelRendererBends) modelBendsPlayer.bipedRightArm).update(data.ticksPerFrame);
                    ((ModelRendererBends) modelBendsPlayer.bipedLeftLeg).update(data.ticksPerFrame);
                    ((ModelRendererBends) modelBendsPlayer.bipedRightLeg).update(data.ticksPerFrame);
                    ((ModelRendererBends) modelBendsPlayer.bipedLeftForeArm).update(data.ticksPerFrame);
                    ((ModelRendererBends) modelBendsPlayer.bipedRightForeArm).update(data.ticksPerFrame);
                    ((ModelRendererBends) modelBendsPlayer.bipedLeftForeLeg).update(data.ticksPerFrame);
                    ((ModelRendererBends) modelBendsPlayer.bipedRightForeLeg).update(data.ticksPerFrame);
                    modelBendsPlayer.renderOffset.update(data.ticksPerFrame);
                    modelBendsPlayer.renderRotation.update(data.ticksPerFrame);
                    modelBendsPlayer.renderItemRotation.update(data.ticksPerFrame);
                    modelBendsPlayer.swordTrail.update(data.ticksPerFrame);
                    data.updatedThisFrame = true;
                }

                data.syncModelInfo(modelBendsPlayer);

                ((ModelRendererBends) body.bipedHead).sync(data.head);
                //((ModelRendererBends) body.bipedHeadwear).sync(data.headwear);
                ((ModelRendererBends) body.bipedBody).sync(data.body);
                ((ModelRendererBends) body.bipedRightArm).sync(data.rightArm);
                ((ModelRendererBends) body.bipedLeftArm).sync(data.leftArm);
                ((ModelRendererBends) body.bipedRightLeg).sync(data.rightLeg);
                ((ModelRendererBends) body.bipedLeftLeg).sync(data.leftLeg);
                try {
                    ((ModelRendererBends) rightForeArm.get(body)).sync(data.rightForeArm);
                    ((ModelRendererBends) leftForeArm.get(body)).sync(data.leftForeArm);
                    ((ModelRendererBends) rightForeLeg.get(body)).sync(data.rightForeLeg);
                    ((ModelRendererBends) leftForeLeg.get(body)).sync(data.leftForeLeg);
                    sync(body);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //同步脸部,死亡光环之类
    private static void sync(JinRyuu.JRMCore.entity.ModelBipedBody modelBipedBody) throws IllegalAccessException {
        ArrayList<String> head = new ArrayList<>(Arrays.asList("Fro", "Fro0", "Fro1", "Fro2", "Fro5", "SaiO", "SaiE", "Nam", "face1", "face2", "face3", "face4", "face5", "face6", "bipedHeadg", "bipedHeadt", "bipedHeadv", "bipedHeadgh", "bipedHeadg2", "bipedHeadght", "bipedHeadgt", "bipedHeadgtt", "bipedHeadc7", "bipedHeadc8", "bipedHeadrad", "bipedHeadradl", "bipedHeadradl2", "bipedHeadssj3t", "bipedHeadsg", "bipedHeadst", "bipedHeadsv", "bipedHeadsgh", "bipedHeadssg", "bipedHeadsst", "bipedHeadssv", "bipedHeadssgh"));
        ArrayList<String> rightArm = new ArrayList<>();
        rightArm.add("rightarmshoulder");//GiTurtleMdl,衣服的右臂
        rightArm.add("Rarm");//DBC_GiTurtleMdl,右臂
        rightArm.add("Fro5r");//冰冻恶魔右臂上的附着物
        rightArm.add("WRightarm");//衣服右臂
        ArrayList<String> leftArm = new ArrayList<>();
        leftArm.add("leftarmshoulder");//GiTurtleMdl,衣服左臂
        leftArm.add("Larm");//DBC_GiTurtleMdl,衣服左臂
        leftArm.add("Fro5l");//冰冻恶魔左臂上的附着物
        leftArm.add("WLeftarm");//衣服左臂
        ArrayList<String> rightLeg = new ArrayList<>();
        rightLeg.add("WRightleg");//衣服右腿
        ArrayList<String> leftLeg = new ArrayList<>();
        leftLeg.add("WLeftleg");//衣服左腿
        ArrayList<String> body = new ArrayList<>();
        body.add("SaiT1");//赛亚人尾巴
        body.add("SaiT2");//赛亚人尾巴
        body.add("FroB");//冰冻恶魔尾巴
        body.add("Fro5b");//冰冻恶魔尾巴


        for (Field field : modelBipedBody.getClass().getFields()) {
            if (field.getType().getName().equals("net.minecraft.client.model.ModelRenderer")) {
                field.setAccessible(true);
                //ModelRenderer mr1 = (ModelRenderer) field.get(modelBipedBody);
                String fieldName = field.getName();
                if (head.contains(fieldName)) {
                    sync(modelBipedBody, (PartRenderer) modelBipedBody.bipedHead, field);
                } else if (rightArm.contains(fieldName)) {
                    sync(modelBipedBody, (PartRenderer) modelBipedBody.bipedRightArm, field);
                } else if (leftArm.contains(fieldName)) {
                    sync(modelBipedBody, (PartRenderer) modelBipedBody.bipedLeftArm, field);
                } else if (rightLeg.contains(fieldName)) {
                    sync(modelBipedBody, (PartRenderer) modelBipedBody.bipedRightLeg, field);
                } else if (leftLeg.contains(fieldName)) {
                    sync(modelBipedBody, (PartRenderer) modelBipedBody.bipedLeftLeg, field);
                } else if (body.contains(fieldName)) {
                    sync(modelBipedBody, (PartRenderer) modelBipedBody.bipedBody, field);
                }

                /*if (wears.contains(fieldName)){
                    ModelRenderer2 modelRenderer2 = (ModelRenderer2) field.get(modelBipedBody);
                    modelRenderer2.function = modelBiped -> (bends -> {
                        GL11.glScalef(1.03f, 1.03f, 1.03f);
                        return null;
                    });
                }*/
            }
        }
    }

    private static void translate(Vector3f vec1, Vector3f vec2) {
        vec1.translate(vec2.getX(), vec2.getY(), vec2.getZ());
    }

    private static void sync(JinRyuu.JRMCore.entity.ModelBipedBody modelBipedBody, PartRenderer partRenderer, Field field) throws IllegalAccessException {
        ModelRenderer modelRenderer = (ModelRenderer) field.get(modelBipedBody);
        if (modelRenderer != null) {
            if (!(modelRenderer instanceof ModelRenderer2)) {
                ModelRenderer2 modelRenderer2 = new ModelRenderer2(Utils.convertBends(modelBipedBody, ModelRendererBends.class, modelRenderer), (BodyRenderer) modelBipedBody.bipedBody, partRenderer);
                //modelRenderer2.source.offsetX = modelRenderer2.source.offsetY = modelRenderer2.source.offsetZ = 0;
                field.set(modelBipedBody, modelRenderer2);
            } else {
                if (Strings.isNullOrEmpty(partRenderer.part) || !partRenderer.part.equals("head")) {
                    ((ModelRenderer2) modelRenderer).sync(partRenderer);
                }
            }
        }
    }

    public static void renderBody(JinRyuu.JRMCore.entity.ModelBipedBody body, float par2) {//渲染身体

        int textureId = LWJGLTools.getCurrentBindingTexture();
        FloatBuffer colors = LWJGLTools.getCurrentColorRGBA();
        float f6;
        if (g <= 1) {
            if (body.isChild) {
                f6 = 2.0F;
                GL11.glPushMatrix();
                GL11.glScalef(1.5F / f6, 1.5F / f6, 1.5F / f6);
                GL11.glTranslatef(0.0F, 16.0F * par2, 0.0F);
                body.bipedHead.render(par2);
                GL11.glPopMatrix();
                GL11.glPushMatrix();
                GL11.glScalef(1.0F / f6, 1.0F / f6, 1.0F / f6);
                GL11.glTranslatef(0.0F, 24.0F * par2, 0.0F);
                body.bipedBody.render(par2);
                body.bipedRightArm.render(par2);
                body.bipedLeftArm.render(par2);
                body.bipedRightLeg.render(par2);
                body.bipedLeftLeg.render(par2);
                GL11.glPopMatrix();
            } else {
                //GL11.glScalef(0.985f,0.985f,0.985f);
                //body.bipedLeftLeg.render(par2);
                //body.bipedRightLeg.render(par2);
                //System.out.println(body.getClass().getName());




                body.bipedHeadwear.render(par2);
                body.bipedBody.render(par2);

                if(body instanceof ModelBipedDBC){
                    Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("jinryuumodscore:gui/normall.png"));
                    GL11.glColor4f(0.0f,0.0f,0.0f,1.0f);
                }
                body.bipedLeftLeg.render(par2);//腿部材质
                body.bipedRightLeg.render(par2);//腿部材质

                //body.bipedHeadwear.render(par2);

                if (textureId != 0) {
                    GL11.glPushMatrix();
                    GL11.glScalef(0.985f, 0.985f, 0.985f);//缩小腿部大小，防止腿部将材质覆盖，否则材质不显示
                    GL11.glColor4f(colors.get(0), colors.get(1), colors.get(2), colors.get(3));
                    GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);

                    body.bipedRightLeg.render(par2);
                    body.bipedLeftLeg.render(par2);
                    GL11.glPopMatrix();
                }


                //System.out.println("texture " + (LWJGLTools.getCurrentBindingTexture() == textureId ? "true" : "false"));
                //System.out.println("colors " + LWJGLTools.getCurrentColorRGBA().compareTo(colors));
                //body.bipedHead.render(par2);

            }
        } else {
            f6 = f;
            GL11.glPushMatrix();
            GL11.glScalef((0.5F + 0.5F / f6) * (g <= 1 ? 1.0F : 0.85F), 0.5F + 0.5F / f6, (0.5F + 0.5F / f6) * (g <= 1 ? 1.0F : 0.85F));
            GL11.glTranslatef(0.0F, (f6 - 1.0F) / f6 * (2.0F - (f6 >= 1.5F && f6 <= 2.0F ? (2.0F - f6) / 2.5F : (f6 < 1.5F && f6 >= 1.0F ? (f6 * 2.0F - 2.0F) * 0.2F : 0.0F))), 0.0F);
            body.bipedHead.render(par2);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glScalef(1.0F / f6 * (g <= 1 ? 1.0F : 0.7F), 1.0F / f6, 1.0F / f6 * (g <= 1 ? 1.0F : 0.7F));
            GL11.glTranslatef(0.0F, (f6 - 1.0F) * 1.5F, 0.0F);
            body.Brightarm.render(par2);
            body.Bleftarm.render(par2);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glScalef(1.0F / f6 * (g <= 1 ? 1.0F : 0.85F), 1.0F / f6, 1.0F / f6 * (g <= 1 ? 1.0F : 0.775F));
            if (body.isSneak) {
                GL11.glTranslatef(-0.015F, (f6 - 1.0F) * 1.5F, -0.0F);
            } else {
                GL11.glTranslatef(-0.015F, (f6 - 1.0F) * 1.5F, -0.015F);
            }

            body.rightleg.render(par2);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glScalef(1.0F / f6 * (g <= 1 ? 1.0F : 0.85F), 1.0F / f6, 1.0F / f6 * (g <= 1 ? 1.0F : 0.775F));
            if (body.isSneak) {
                GL11.glTranslatef(0.015F, (f6 - 1.0F) * 1.5F, -0.0F);
            } else {
                GL11.glTranslatef(0.015F, (f6 - 1.0F) * 1.5F, -0.015F);
            }

            body.leftleg.render(par2);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glScalef(1.0F / f6 * (g <= 1 ? 1.0F : 0.675F), 1.0F / f6, 1.0F / f6 * (g <= 1 ? 1.0F : 0.8F));
            String[] s = JRMCoreH.data(body.Entity.getCommandSenderName(), 1, "0;0;0;0;0;0;0;0;0").split(";");
            String dns = s[1];
            int b = JRMCoreH.dnsBreast(dns);
            float scale = (float) b * 0.03F;
            float br = 0.4235988F + scale;
            float bs = 0.8F + scale;
            float bsY = 0.85F + scale * 0.5F;
            float bt = 0.1F * scale;
            boolean bounce = body.Entity.onGround || body.Entity.isInWater();
            float bspeed = body.Entity.isSprinting() ? 1.5F : (body.Entity.isSneaking() ? 0.5F : 1.0F);
            float bbY = (bounce ? MathHelper.sin(body.rot1 * 0.6662F * bspeed * 1.5F + 3.1415927F) * body.rot2 * 0.03F : 0.0F) * (float) b * 0.1119F;
            GL11.glTranslatef(0.0F, (f6 - 1.0F) * 1.5F + bbY, 0.015F + bt);
            GL11.glScalef(1.0F, bsY, bs);
            Utils.setRotation(body.breast, -br, 0.0F, 0.0F);
            Utils.setRotation(body.breast2, br, 3.141593F, 0.0F);
            if (bounce) {
                ModelRenderer var10000 = body.breast;
                var10000.rotateAngleX += -MathHelper.cos(body.rot1 * 0.6662F * bspeed + 3.1415927F) * body.rot2 * 0.05F * (float) b * 0.1119F;
                var10000.rotateAngleY += MathHelper.cos(body.rot1 * 0.6662F * bspeed + 3.1415927F) * body.rot2 * 0.02F * (float) b * 0.1119F;
                var10000 = body.breast2;
                var10000.rotateAngleX += MathHelper.cos(body.rot1 * 0.6662F * bspeed + 3.1415927F) * body.rot2 * 0.05F * (float) b * 0.1119F;
                var10000.rotateAngleY += MathHelper.cos(body.rot1 * 0.6662F * bspeed + 3.1415927F) * body.rot2 * 0.02F * (float) b * 0.1119F;
            }

            body.Bbreast.render(par2);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glScalef(1.0F / f6 * (g <= 1 ? 1.0F : 0.7F), 1.0F / f6, 1.0F / f6 * (g <= 1 ? 1.0F : 0.7F));
            GL11.glTranslatef(0.0F, (f6 - 1.0F) * 1.5F, 0.0F);
            body.body.render(par2);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glScalef(1.0F / f6 * (g <= 1 ? 1.0F : 0.75F), 1.0F / f6, 1.0F / f6 * (g <= 1 ? 1.0F : 0.75F) * (1.0F + 0.005F * (float) p));
            if (body.isSneak) {
                GL11.glTranslatef(0.0F, (f6 - 1.0F) * 1.5F, 0.0F);
            } else {
                GL11.glTranslatef(0.0F, (f6 - 1.0F) * 1.5F, -0.02F - 5.0E-4F * (float) p);
            }

            body.hip.render(par2);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glScalef(1.0F / f6 * (g <= 1 ? 1.0F : 0.65F), 1.0F / f6, 1.0F / f6 * (g <= 1 ? 1.0F : 0.65F) * (1.0F + 0.001F * (float) p));
            if (body.isSneak) {
                GL11.glTranslatef(0.0F, (f6 - 1.0F) * 1.5F, 0.0F);
            } else {
                GL11.glTranslatef(0.0F, (f6 - 1.0F) * 1.5F, -0.04F - 1.0E-4F * (float) p);
            }

            body.waist.render(par2);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glScalef(1.0F / f6 * (g <= 1 ? 1.0F : 0.85F), 1.0F / f6, 1.0F / f6 * (g <= 1 ? 1.0F : 0.85F) * (1.0F + 0.005F * (float) p));
            GL11.glTranslatef(0.0F, (f6 - 1.0F) * 1.5F, 0.0F - 5.0E-4F * (float) p);
            body.bottom.render(par2);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glScalef(1.0F / f6 * (g <= 1 ? 1.0F : 0.675F) - 0.001F, 1.0F / f6, 1.0F / f6 * (g <= 1 ? 1.0F : 0.8F) - 0.001F);
            GL11.glTranslatef(0.0F, (f6 - 1.0F) * 1.5F + 0.001F + bbY, 0.015F + bt);
            GL11.glScalef(1.0F, bsY, bs);
            body.Bbreast2.render(par2);
            GL11.glPopMatrix();
        }
    }


}
