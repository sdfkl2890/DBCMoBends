package xiaobang;

import JinRyuu.JBRA.DBC_GiTurtleMdl;
import JinRyuu.JBRA.GiTurtleMdl;
import JinRyuu.JBRA.ModelBipedDBC;
import JinRyuu.JRMCore.JRMCoreH;
import JinRyuu.JRMCore.entity.ModelBipedBody;
import com.google.gson.*;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.gobbob.mobends.AnimatedEntity;
import net.gobbob.mobends.client.model.ModelBoxBends;
import net.gobbob.mobends.client.model.ModelRendererBends;
import net.gobbob.mobends.client.model.ModelRendererBends_SeperatedChild;
import net.gobbob.mobends.client.model.entity.ModelBendsPlayer;
import net.gobbob.mobends.client.renderer.entity.RenderBendsPlayer;
import net.gobbob.mobends.data.Data_Player;
import net.gobbob.mobends.pack.BendsPack;
import net.gobbob.mobends.pack.BendsVar;
import net.gobbob.mobends.util.SmoothVector3f;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemPickaxe;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@SideOnly(Side.CLIENT)
public class Editor {
    static Field rightForeArm;
    static Field leftForeArm;
    static Field rightForeLeg;
    static Field leftForeLeg;


    static HashMap<String,Integer> keys = new HashMap<>();

    static {
        try {
            rightForeArm = ModelBipedBody.class.getField("bipedRightForeArm");
            rightForeArm.setAccessible(true);
            leftForeArm = ModelBipedBody.class.getField("bipedLeftForeArm");
            leftForeArm.setAccessible(true);
            leftForeLeg = ModelBipedBody.class.getField("bipedLeftForeLeg");
            leftForeLeg.setAccessible(true);
            rightForeLeg = ModelBipedBody.class.getField("bipedRightForeLeg");
            rightForeLeg.setAccessible(true);
            for (Field field : Keyboard.class.getFields()){
                if (field.getName().startsWith("KEY_")) {
                    field.setAccessible(true);
                    keys.put(field.getName(), (Integer) field.get(null));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ModelBendsPlayer modelBendsPlayer = new ModelBendsPlayer();


    static Gson gson = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
        @Override
        public boolean shouldSkipField(FieldAttributes fieldAttributes) {
            List<String> list = new ArrayList<>();
            list.add("field_78812_q");
            list.add("field_78810_s");
            list.add("compiled");
            list.add("baseModel");
            list.add("cubeList");
            list.add("childModels");
            if (list.contains(fieldAttributes.getName())) {
                return true;
            }
            return false;
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    }).create();

    private static int containsPlayer(String name) {
        if (JRMCoreH.plyrs != null && JRMCoreH.plyrs.length > 0 && JRMCoreH.dnn(2) && JRMCoreH.dnn(10)) {
            for (int i = 0; i < JRMCoreH.plyrs.length; i++) {
                if (JRMCoreH.plyrs[i].equals(name)) {
                    return i;
                }
            }
        }

        return 9527;
    }

    private static void update(Data_Player data) {
        float ticksPerFrame = data.ticksPerFrame;
        data.head.update(ticksPerFrame);
        data.body.update(ticksPerFrame);
        data.rightArm.update(ticksPerFrame);
        data.leftArm.update(ticksPerFrame);
        data.rightLeg.update(ticksPerFrame);
        data.leftLeg.update(ticksPerFrame);
        data.rightForeArm.update(ticksPerFrame);
        data.leftForeArm.update(ticksPerFrame);
        data.rightForeLeg.update(ticksPerFrame);
        data.leftForeLeg.update(ticksPerFrame);
    }

    public static void setRotationAngles(ModelBipedBody body, float argSwingTime, float argSwingAmount, float argArmSway, float argHeadY, float argHeadX, float argNr6, Entity argEntity) {
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

                ((ModelRendererBends)modelBendsPlayer.bipedHead).sync(data.head);
                ((ModelRendererBends)modelBendsPlayer.bipedHeadwear).sync(data.headwear);
                ((ModelRendererBends)modelBendsPlayer.bipedBody).sync(data.body);
                ((ModelRendererBends)modelBendsPlayer.bipedRightArm).sync(data.rightArm);
                ((ModelRendererBends)modelBendsPlayer.bipedLeftArm).sync(data.leftArm);
                ((ModelRendererBends)modelBendsPlayer.bipedRightLeg).sync(data.rightLeg);
                ((ModelRendererBends)modelBendsPlayer.bipedLeftLeg).sync(data.leftLeg);
                ((ModelRendererBends)modelBendsPlayer.bipedRightForeArm).sync(data.rightForeArm);
                ((ModelRendererBends)modelBendsPlayer.bipedLeftForeArm).sync(data.leftForeArm);
                ((ModelRendererBends)modelBendsPlayer.bipedRightForeLeg).sync(data.rightForeLeg);
                ((ModelRendererBends)modelBendsPlayer.bipedLeftForeLeg).sync(data.leftForeLeg);
                modelBendsPlayer.renderOffset.set(data.renderOffset);
                modelBendsPlayer.renderRotation.set(data.renderRotation);
                modelBendsPlayer.renderItemRotation.set(data.renderItemRotation);
                modelBendsPlayer.swordTrail = data.swordTrail;
                if (Data_Player.get(argEntity.getEntityId()).canBeUpdated()) {
                    modelBendsPlayer.renderOffset.setSmooth(new Vector3f(0.0F, -1.0F, 0.0F), 0.5F);
                    modelBendsPlayer.renderRotation.setSmooth(new Vector3f(0.0F, 0.0F, 0.0F), 0.5F);
                    modelBendsPlayer.renderItemRotation.setSmooth(new Vector3f(0.0F, 0.0F, 0.0F), 0.5F);
                    ((ModelRendererBends)modelBendsPlayer.bipedHead).resetScale();
                    ((ModelRendererBends)modelBendsPlayer.bipedHeadwear).resetScale();
                    ((ModelRendererBends)modelBendsPlayer.bipedBody).resetScale();
                    ((ModelRendererBends)modelBendsPlayer.bipedRightArm).resetScale();
                    ((ModelRendererBends)modelBendsPlayer.bipedLeftArm).resetScale();
                    ((ModelRendererBends)modelBendsPlayer.bipedRightLeg).resetScale();
                    ((ModelRendererBends)modelBendsPlayer.bipedLeftLeg).resetScale();
                    ((ModelRendererBends)modelBendsPlayer.bipedRightForeArm).resetScale();
                    ((ModelRendererBends)modelBendsPlayer.bipedLeftForeArm).resetScale();
                    ((ModelRendererBends)modelBendsPlayer.bipedRightForeLeg).resetScale();
                    ((ModelRendererBends)modelBendsPlayer.bipedLeftForeLeg).resetScale();
                    BendsVar.tempData = Data_Player.get(argEntity.getEntityId());
                    if (argEntity.isRiding()) {
                        AnimatedEntity.getByEntity(argEntity).get("riding").animate((EntityLivingBase)argEntity, modelBendsPlayer, Data_Player.get(argEntity.getEntityId()));
                        BendsPack.animate(modelBendsPlayer, "player", "riding");
                    } else if (argEntity.isInWater()) {
                        AnimatedEntity.getByEntity(argEntity).get("swimming").animate((EntityLivingBase)argEntity, modelBendsPlayer, Data_Player.get(argEntity.getEntityId()));
                        BendsPack.animate(modelBendsPlayer, "player", "swimming");
                    } else if (!Data_Player.get(argEntity.getEntityId()).isOnGround() | Data_Player.get(argEntity.getEntityId()).ticksAfterTouchdown < 2.0F) {
                        AnimatedEntity.getByEntity(argEntity).get("jump").animate((EntityLivingBase)argEntity, modelBendsPlayer, Data_Player.get(argEntity.getEntityId()));
                        BendsPack.animate(modelBendsPlayer, "player", "jump");
                        if (modelBendsPlayer.bipedBody != null) {
                            ((ModelRendererBends) modelBendsPlayer.bipedBody).rotation = new net.gobbob.mobends.util.SmoothVector3f();
                        }

                    } else {
                        if (Data_Player.get(argEntity.getEntityId()).motion.x == 0.0F & Data_Player.get(argEntity.getEntityId()).motion.z == 0.0F) {
                            AnimatedEntity.getByEntity(argEntity).get("stand").animate((EntityLivingBase)argEntity, modelBendsPlayer, Data_Player.get(argEntity.getEntityId()));
                            BendsPack.animate(modelBendsPlayer, "player", "stand");
                        } else if (argEntity.isSprinting()) {
                            AnimatedEntity.getByEntity(argEntity).get("sprint").animate((EntityLivingBase)argEntity, modelBendsPlayer, Data_Player.get(argEntity.getEntityId()));
                            BendsPack.animate(modelBendsPlayer, "player", "sprint");
                        } else {
                            AnimatedEntity.getByEntity(argEntity).get("walk").animate((EntityLivingBase)argEntity, modelBendsPlayer, Data_Player.get(argEntity.getEntityId()));
                            BendsPack.animate(modelBendsPlayer, "player", "walk");
                        }

                        if (argEntity.isSneaking()) {
                            AnimatedEntity.getByEntity(argEntity).get("sneak").animate((EntityLivingBase)argEntity, modelBendsPlayer, Data_Player.get(argEntity.getEntityId()));
                            BendsPack.animate(modelBendsPlayer, "player", "sneak");
                        }
                    }

                    if (modelBendsPlayer.aimedBow) {
                        AnimatedEntity.getByEntity(argEntity).get("bow").animate((EntityLivingBase)argEntity, modelBendsPlayer, Data_Player.get(argEntity.getEntityId()));
                        BendsPack.animate(modelBendsPlayer, "player", "bow");
                    } else if (((EntityPlayer)argEntity).getCurrentEquippedItem() != null && ((EntityPlayer)argEntity).getCurrentEquippedItem().getItem() instanceof ItemPickaxe || ((EntityPlayer)argEntity).getCurrentEquippedItem() != null && Block.getBlockFromItem(((EntityPlayer)argEntity).getCurrentEquippedItem().getItem()) != Blocks.air) {
                        AnimatedEntity.getByEntity(argEntity).get("mining").animate((EntityLivingBase)argEntity, modelBendsPlayer, Data_Player.get(argEntity.getEntityId()));
                        BendsPack.animate(modelBendsPlayer, "player", "mining");
                    } else if (((EntityPlayer)argEntity).getCurrentEquippedItem() != null && ((EntityPlayer)argEntity).getCurrentEquippedItem().getItem() instanceof ItemAxe) {
                        AnimatedEntity.getByEntity(argEntity).get("axe").animate((EntityLivingBase)argEntity, modelBendsPlayer, Data_Player.get(argEntity.getEntityId()));
                        BendsPack.animate(modelBendsPlayer, "player", "axe");
                    } else {
                        AnimatedEntity.getByEntity(argEntity).get("attack").animate((EntityLivingBase)argEntity, modelBendsPlayer, Data_Player.get(argEntity.getEntityId()));
                        BendsPack.animate(modelBendsPlayer, "player", "attack");
                        if (modelBendsPlayer.bipedBody != null) {
                            ((ModelRendererBends) modelBendsPlayer.bipedBody).rotation = new net.gobbob.mobends.util.SmoothVector3f();
                        }

                    }

                    if (JinRyuu.JRMCore.i.ExtendedPlayer.get((EntityPlayer) argEntity).getBlocking() == 1) {
                        //System.out.println("defense awa");
                        BendsPack.animate(modelBendsPlayer, "player", "defense");//防御
                    } else if (JinRyuu.JRMCore.i.ExtendedPlayer.get((EntityPlayer) argEntity).getBlocking() == 2) {
                        BendsPack.animate(modelBendsPlayer, "player", "instantTransmissionON");//瞬间移动
                    }
                    int i = containsPlayer(argEntity.getCommandSenderName());
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
                                if (JRMCoreH.StusEfctsClient(7, i) && modelBendsPlayer.bipedHead != null) {
                                    //System.out.println("head roation -90");
                                    //if(entity.field_71075_bZ.field_75100_b){
                                    ((ModelRendererBends) modelBendsPlayer.bipedHead).rotation.setSmoothX(modelBendsPlayer.headRotationX - 90, 0.3F);
                                    //}
                                }
                            }
                        }
                    }

                    for(String key : keys.keySet()){
                        if(Keyboard.isKeyDown(keys.get(key))){
                            BendsPack.animate(modelBendsPlayer,"player",key);
                        }
                    }

                    ((ModelRendererBends)modelBendsPlayer.bipedHead).update(data.ticksPerFrame);
                    ((ModelRendererBends)modelBendsPlayer.bipedHeadwear).update(data.ticksPerFrame);
                    ((ModelRendererBends)modelBendsPlayer.bipedBody).update(data.ticksPerFrame);
                    ((ModelRendererBends)modelBendsPlayer.bipedLeftArm).update(data.ticksPerFrame);
                    ((ModelRendererBends)modelBendsPlayer.bipedRightArm).update(data.ticksPerFrame);
                    ((ModelRendererBends)modelBendsPlayer.bipedLeftLeg).update(data.ticksPerFrame);
                    ((ModelRendererBends)modelBendsPlayer.bipedRightLeg).update(data.ticksPerFrame);
                    ((ModelRendererBends)modelBendsPlayer.bipedLeftForeArm).update(data.ticksPerFrame);
                    ((ModelRendererBends)modelBendsPlayer.bipedRightForeArm).update(data.ticksPerFrame);
                    ((ModelRendererBends)modelBendsPlayer.bipedLeftForeLeg).update(data.ticksPerFrame);
                    ((ModelRendererBends)modelBendsPlayer.bipedRightForeLeg).update(data.ticksPerFrame);
                    modelBendsPlayer.renderOffset.update(data.ticksPerFrame);
                    modelBendsPlayer.renderRotation.update(data.ticksPerFrame);
                    modelBendsPlayer.renderItemRotation.update(data.ticksPerFrame);
                    modelBendsPlayer.swordTrail.update(data.ticksPerFrame);
                    data.updatedThisFrame = true;
                }

                data.syncModelInfo(modelBendsPlayer);
                System.out.println("test555");
                ((ModelRendererBends) body.bipedHead).sync(data.head);
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

    private static void setRotation(ModelRenderer mr1,ModelRendererBends mr2){
        mr1.rotateAngleX = mr2.rotateAngleX + mr2.pre_rotation.getX() / 57.295776F;
        mr1.rotateAngleY = mr2.rotateAngleY - mr2.pre_rotation.getY() / 57.295776F;
        mr1.rotateAngleZ = mr2.rotateAngleZ + mr2.pre_rotation.getZ() / 57.295776F;
    }

    private static void setOffset(ModelRenderer mr1,ModelRenderer mr2){
        mr1.offsetX = mr2.offsetX;
        mr1.offsetY = mr2.offsetY;
        mr1.offsetZ = mr2.offsetZ;
    }

    //同步脸部,死亡光环之类
    private static void sync(ModelBiped body) throws IllegalAccessException {
        ModelRenderer[] mrs = new ModelRenderer[]{body.bipedHead, body.bipedRightArm, body.bipedLeftArm, body.bipedRightLeg, body.bipedLeftLeg};
        ArrayList<String> head = new ArrayList<>(Arrays.asList("field_78114_d", "halo", "Fro", "Fro0", "Fro1", "Fro2", "Fro5", "SaiO", "SaiE", "Nam", "face1","face2","face3","face4","face5","face6","bipedHeadg", "bipedHeadt", "bipedHeadv", "bipedHeadgh", "bipedHeadg2", "bipedHeadght", "bipedHeadgt", "bipedHeadgtt", "bipedHeadc7", "bipedHeadc8", "bipedHeadrad", "bipedHeadradl", "bipedHeadradl2", "bipedHeadssj3t", "bipedHeadsg", "bipedHeadst", "bipedHeadsv", "bipedHeadsgh", "bipedHeadssg", "bipedHeadsst", "bipedHeadssv", "bipedHeadssgh","c20","c19"));
        ArrayList<String> rightArm = new ArrayList<>();
        rightArm.add("rightarmshoulder");//GiTurtleMdl
        rightArm.add("Rarm");//DBC_GiTurtleMdl
        rightArm.add("RRolledSleeve");//DBC_GiTurtleMdl,21号衣服右滚筒
        ArrayList<String> leftArm = new ArrayList<>();
        leftArm.add("leftarmshoulder");//GiTurtleMdl
        leftArm.add("Larm");//DBC_GiTurtleMdl
        leftArm.add("LRolledSleee");//DBC_GiTurtleMdl，21号衣服左滚筒
        //System.out.println("class " + body.getClass().getName());
        for (Field field : body.getClass().getFields()) {
            if (field.getType().getName().equals("net.minecraft.client.model.ModelRenderer")) {
                field.setAccessible(true);
                ModelRenderer mr1 = (ModelRenderer) field.get(body);
                if (mr1 != null) {
                    if(head.contains(field.getName())){
                        if(body.bipedHead instanceof ModelRendererBends) {
                            setRotation(mr1, (ModelRendererBends) body.bipedHead);
                        }
                        setOffset(mr1,body.bipedHead);
                        continue;
                    }else if(rightArm.contains(field.getName())){
                        if(body.bipedRightArm instanceof ModelRendererBends) {
                            setRotation(mr1, (ModelRendererBends) body.bipedRightArm);
                        }
                        setOffset(mr1,body.bipedRightArm);
                        continue;

                    }else if(leftArm.contains(field.getName())){
                        if(body.bipedLeftArm instanceof ModelRendererBends) {
                            setRotation(mr1, (ModelRendererBends) body.bipedLeftArm);
                        }
                        setOffset(mr1,body.bipedLeftArm);
                        continue;
                    }
                    ModelBox box1 = (ModelBox) mr1.cubeList.get(0);
                    for (ModelRenderer mr : mrs) {
                        if (mr instanceof ModelRendererBends) {
                            ModelRendererBends mr2 = (ModelRendererBends) mr;
                            ModelBox box2 = (ModelBox) mr2.cubeList.get(0);
                            if ((box1.posX1 + mr1.rotationPointX) == (box2.posX1 + mr2.rotationPointX) && (box1.posY1 + mr1.rotationPointY) == (box2.posY1 + mr2.rotationPointY) && (box1.posZ1 + mr1.rotationPointZ) == (box2.posZ1 + mr2.rotationPointZ)) {
                                setRotation(mr1, mr2);
                                setOffset(mr1, mr2);
                                if (mr1.childModels != null) {
                                    for (Object obj : mr1.childModels) {
                                        ModelRenderer mr3 = (ModelRenderer) obj;
                                        setRotation(mr3, mr2);
                                        setOffset(mr3, mr2);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static void hairRotate(ModelBipedDBC dbc,float par1){
        GL11.glTranslatef(dbc.bipedHead.rotationPointX * par1, dbc.bipedHead.rotationPointY * par1, dbc.bipedHead.rotationPointZ * par1);
        if (dbc.bipedHead.rotateAngleZ != 0.0F) {
            GL11.glRotatef(dbc.bipedHead.rotateAngleZ * 57.295776F, 0.0F, 0.0F, 1.0F);
        }
        if(dbc.bipedHead.rotateAngleY != 0.0F){
            GL11.glRotatef(dbc.bipedHead.rotateAngleY * 57.295776F, 0.0F, 1.0F, 0.0F);
        }
        if(dbc.bipedHead.rotateAngleX != 0.0F){
            GL11.glRotatef(dbc.bipedHead.rotateAngleX * 57.295776F, 1.0F, 0.0F, 0.0F);
        }
        if(dbc.bipedHead instanceof ModelRendererBends) {
            ModelRendererBends head = (ModelRendererBends) dbc.bipedHead;
            GL11.glRotatef(head.pre_rotation.getZ(), 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(-head.pre_rotation.getY(), 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(head.pre_rotation.getX(), 1.0F, 0.0F, 0.0F);
        }
        GL11.glPushMatrix();
    }

    public static void edit(ModelBipedBody body) {
        try {
            ModelRendererBends bipedRightForeArm = new ModelRendererBends(body, 40, 22);
            bipedRightForeArm.addBox(0.0F, 0.0F, -4.0F, 4, 6, 4, 0);
            bipedRightForeArm.setRotationPoint(-3.0F, 4.0F, 2.0F);
            bipedRightForeArm.getBox().offsetTextureQuad(bipedRightForeArm, 3, 0.0F, -6.0F);

            ModelRendererBends bipedLeftForeArm = new ModelRendererBends(body, 40, 22);
            bipedLeftForeArm.mirror = true;
            bipedLeftForeArm.addBox(0.0F, 0.0F, -4.0F, 4, 6, 4, 0);
            bipedLeftForeArm.setRotationPoint(-1.0F, 4.0F, 2.0F);
            bipedLeftForeArm.getBox().offsetTextureQuad(bipedRightForeArm, 3, 0.0F, -6.0F);

            ModelRendererBends bipedRightForeLeg = new ModelRendererBends(body, 0, 22);
            bipedRightForeLeg.addBox(-2.0F, 0.0F, 0.0F, 4, 6, 4, 0);
            bipedRightForeLeg.setRotationPoint(0.0F, 6.0f, -2.0F);
            bipedRightForeLeg.getBox().offsetTextureQuad(bipedRightForeLeg, 3, 0.0F, -6.0F);

            ModelRendererBends bipedLeftForeLeg = new ModelRendererBends(body, 0, 22);
            bipedLeftForeLeg.mirror = true;
            bipedLeftForeLeg.addBox(-2.0F, 0.0F, 0.0F, 4, 6, 4, 0);
            bipedLeftForeLeg.setRotationPoint(0.0F, 6.0F, -2.0F);
            bipedLeftForeLeg.getBox().offsetTextureQuad(bipedLeftForeLeg, 3, 0.0F, -6.0F);

            setField(rightForeArm, body, bipedRightForeArm);
            setField(leftForeArm, body, bipedLeftForeArm);
            setField(rightForeLeg, body, bipedRightForeLeg);
            setField(leftForeLeg, body, bipedLeftForeLeg);

            body.bipedHead = convertBends(body, false, body.bipedHead);

            body.bipedBody = convertBends(body, false, body.bipedBody).setShowChildIfHidden(true);
            //body.bipedBody.offsetY += 12.0F;
            //((ModelRendererBends)body.bipedBody).offsetBox_Add(0.0F,-12.0F,0.0F).updateVertices();

            body.bipedRightArm = ((ModelRendererBends_SeperatedChild) convertBends(body, true, body.bipedRightArm)).setMother((ModelRendererBends) body.bipedBody).setSeperatedPart(bipedRightForeArm);
            body.bipedLeftArm = ((ModelRendererBends_SeperatedChild) convertBends(body, true, body.bipedLeftArm)).setMother((ModelRendererBends) body.bipedBody).setSeperatedPart(bipedLeftForeArm);
            body.bipedRightLeg = convertBends(body, false, body.bipedRightLeg);
            body.bipedLeftLeg = convertBends(body, false, body.bipedLeftLeg);

            body.bipedRightArm.addChild(bipedRightForeArm);
            body.bipedLeftArm.addChild(bipedLeftForeArm);
            body.bipedRightLeg.addChild(bipedRightForeLeg);
            body.bipedLeftLeg.addChild(bipedRightForeLeg);

            ((ModelRendererBends) body.bipedRightArm).offsetBox_Add(-0.01F, 0.0F, -0.01F).resizeBox(4.02F, 6.0F, 4.02F).updateVertices();
            ((ModelRendererBends) body.bipedLeftArm).offsetBox_Add(-0.01F, 0.0F, -0.01F).resizeBox(4.02F, 6.0F, 4.02F).updateVertices();
            ((ModelRendererBends) body.bipedRightLeg).offsetBox_Add(-0.01F, 0.0F, -0.01F).resizeBox(4.02F, 6.0F, 4.02F).updateVertices();
            ((ModelRendererBends) body.bipedLeftLeg).offsetBox_Add(-0.01F, 0.0F, -0.01F).resizeBox(4.02F, 6.0F, 4.02F).updateVertices();
        } catch (IllegalAccessException exception) {
            throw new RuntimeException(exception);
        }
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

    private static void setField(Field field, Object instance, Object value) throws IllegalAccessException {
        field.setAccessible(true);
        field.set(instance, value);
    }

    private static ModelRendererBends convertBends(ModelBase base, boolean seperated, ModelRenderer modelRenderer) {
        JsonObject jsonObject = new JsonParser().parse(gson.toJson(modelRenderer)).getAsJsonObject();
        jsonObject.addProperty("scaleX", 1.0F);
        jsonObject.addProperty("scaleY", 1.0F);
        jsonObject.addProperty("scaleZ", 1.0F);
        if (jsonObject.has("textureOffsetY")) {
            jsonObject.addProperty("txOffsetX", jsonObject.get("textureOffsetX").getAsInt());
            jsonObject.addProperty("txOffsetY", jsonObject.get("textureOffsetY").getAsInt());
        } else {
            jsonObject.addProperty("txOffsetX", jsonObject.get("field_78803_o").getAsInt());
            jsonObject.addProperty("txOffsetY", jsonObject.get("field_78813_p").getAsInt());
        }
        ModelRendererBends bends;
        if (seperated) {
            bends = gson.fromJson(jsonObject, ModelRendererBends_SeperatedChild.class);
        } else {
            bends = gson.fromJson(jsonObject, ModelRendererBends.class);
        }
        bends.rotation = new SmoothVector3f();
        bends.pre_rotation = new SmoothVector3f();
        bends.setShowChildIfHidden(false);
        bends.cubeList = convertBends(bends, jsonObject.get("txOffsetX").getAsInt(), jsonObject.get("txOffsetY").getAsInt(), modelRenderer.cubeList);
        bends.childModels = modelRenderer.childModels;
        base.boxList.remove(modelRenderer);
        base.boxList.add(bends);

        ReflectionHelper.setPrivateValue(ModelRenderer.class, bends, base, "baseModel", "field_78810_s");

        return bends;
    }

    private static List convertBends(ModelRenderer modelRenderer, int textureOffsetX, int textureOffsetY, List boxes) {
        ArrayList list = new ArrayList<>();
        for (Object obj : boxes) {
            if (obj instanceof ModelBox) {
                ModelBox box = (ModelBox) obj;
                list.add(new ModelBoxBends(modelRenderer, textureOffsetX, textureOffsetY, box.posX1, box.posY1, box.posZ1, (int) (box.posX2 - box.posX1), (int) (box.posY2 - box.posY1), (int) (box.posZ2 - box.posZ1), 0));
            }
        }
        return list;
    }
}
