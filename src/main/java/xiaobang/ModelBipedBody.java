package xiaobang;

import JinRyuu.JBRA.ModelBipedDBC;
import JinRyuu.JRMCore.JRMCoreH;
import com.google.gson.*;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.gobbob.mobends.AnimatedEntity;
import net.gobbob.mobends.client.model.ModelBoxBends;
import net.gobbob.mobends.client.model.ModelRendererBends;
import net.gobbob.mobends.client.model.ModelRendererBends_SeperatedChild;
import net.gobbob.mobends.client.model.entity.ModelBendsPlayer;
import net.gobbob.mobends.data.Data_Player;
import net.gobbob.mobends.pack.BendsPack;
import net.gobbob.mobends.pack.BendsVar;
import net.gobbob.mobends.util.SmoothVector3f;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;
import xiaobang.renderer.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static JinRyuu.JRMCore.entity.ModelBipedBody.*;

public class ModelBipedBody {
    public static ModelBendsPlayer modelBendsPlayer = new ModelBendsPlayer();

    static Field rightForeArm;
    static Field leftForeArm;
    static Field rightForeLeg;
    static Field leftForeLeg;


    static HashMap<String,Integer> keys = new HashMap<>();

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

    static Gson gson = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
        @Override
        public boolean shouldSkipField(FieldAttributes fieldAttributes) {
            List<String> list = new ArrayList<>();
            list.add("field_78812_q");
            list.add("field_78810_s");
            list.add("field_78811_r");
            list.add("displayList");
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

    private static void setField(Field field, Object instance, Object value) throws IllegalAccessException {
        field.setAccessible(true);
        field.set(instance, value);
    }

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

    private static void setRotation(ModelRenderer mr1,ModelRendererBends mr2){
        mr1.rotateAngleX = (float) (mr2.rotation.getX() / 180 / 180 * Math.PI * Math.PI * 57.295776 + mr2.pre_rotation.getX() / 180 * Math.PI);
        mr1.rotateAngleY = (float) (mr2.rotation.getY() / 180 / 180 * Math.PI * Math.PI * 57.295776 - mr2.pre_rotation.getY() / 180 * Math.PI);
        mr1.rotateAngleZ = (float) (mr2.rotation.getZ() / 180 / 180 * Math.PI * Math.PI * 57.295776 + mr2.pre_rotation.getZ() / 180 * Math.PI);
    }

    private static void setRotation(ModelRenderer model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    public static void init(JinRyuu.JRMCore.entity.ModelBipedBody body,float par1) {
        try {
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

            setField(rightForeArm, body, bipedRightForeArm);
            setField(leftForeArm, body, bipedLeftForeArm);
            setField(rightForeLeg, body, bipedRightForeLeg);
            setField(leftForeLeg, body, bipedLeftForeLeg);


            body.bipedBody = ((PartRenderer)convertBends(body, BodyRenderer.class,body.bipedBody)).setRotationPointY2(12);
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


            body.bipedHead = convertBends(body, PartRenderer.class, body.bipedHead);
            ((BodyRenderer)body.bipedBody).head = (PartRenderer)body.bipedHead;
            ((BodyRenderer) body.bipedBody).head.part = "head";
            body.bipedHeadwear = new ModelRenderer2(body, convertBends(body,ModelRendererBends.class,body.bipedHeadwear));
            body.bipedRightArm = ((PartRenderer_SeperatedChild)convertBends(body, PartRenderer_SeperatedChild.class, body.bipedRightArm)).setMother((ModelRendererBends) body.bipedBody).setSeperatedPart(bipedRightForeArm);
            body.bipedLeftArm = ((PartRenderer_SeperatedChild)convertBends(body, PartRenderer_SeperatedChild.class, body.bipedLeftArm)).setMother((ModelRendererBends) body.bipedBody).setSeperatedPart(bipedLeftForeArm);
            body.bipedRightLeg = convertBends(body, ModelRendererBends.class, body.bipedRightLeg);
            body.bipedRightLeg.cubeList.clear();
            body.bipedRightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, par1);

            body.bipedLeftLeg = convertBends(body, ModelRendererBends.class, body.bipedLeftLeg);
            body.bipedLeftLeg.cubeList.clear();
            body.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, par1);


            //body.bipedHead.offsetY -= 12;
            //body.bipedRightArm.offsetY -= 12;
            //body.bipedLeftArm.offsetY -= 12;

            body.bipedHead.addChild(body.bipedHeadwear);
            body.bipedBody.addChild(body.bipedHead);
            body.bipedBody.addChild(body.bipedRightArm);
            body.bipedBody.addChild(body.bipedLeftArm);
            //body.bipedBody.addChild(body.bipedRightLeg);
            //body.bipedBody.addChild(body.bipedLeftLeg);
            body.bipedRightArm.addChild(bipedRightForeArm);
            body.bipedLeftArm.addChild(bipedLeftForeArm);
            body.bipedRightLeg.addChild(bipedRightForeLeg);
            body.bipedLeftLeg.addChild(bipedLeftForeLeg);


            ((ModelRendererBends)body.bipedRightArm).offsetBox_Add(-0.01F, 0.0F, -0.01F).resizeBox(4.02F, 6.0F, 4.02F).updateVertices();
            ((ModelRendererBends)body.bipedLeftArm).offsetBox_Add(-0.01F, 0.0F, -0.01F).resizeBox(4.02F, 6.0F, 4.02F).updateVertices();
            ((ModelRendererBends) body.bipedRightLeg).offsetBox_Add(-0.01F, 0.0F, -0.01F).resizeBox(4.02F, 6.0F, 4.02F).updateVertices();
            ((ModelRendererBends) body.bipedLeftLeg).offsetBox_Add(-0.01F, 0.0F, -0.01F).resizeBox(4.02F, 6.0F, 4.02F).updateVertices();



            //System.out.println("edit " + body.getClass().getName());
        } catch (IllegalAccessException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static void setRotationAngles(JinRyuu.JRMCore.entity.ModelBipedBody body, float argSwingTime, float argSwingAmount, float argArmSway, float argHeadY, float argHeadX, float argNr6, Entity argEntity) {
        //System.out.println("rotation " + body.getClass().getName());
        try {
            if (rightForeArm.get(body) == null) {
                init(body,1.0F);
            }
        }catch (Exception e){
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

                    /*((PartRenderer)body.bipedBody).thisTicksToRender.clear();
                    ((PartRenderer)((ModelRenderer2)body.bipedHead).source).thisTicksToRender.clear();
                    ((PartRenderer)((ModelRenderer2)body.bipedRightArm).source).thisTicksToRender.clear();
                    ((PartRenderer)((ModelRenderer2)body.bipedLeftArm).source).thisTicksToRender.clear();*/

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
                        //if (modelBendsPlayer.bipedBody != null) {
                        //    ((ModelRendererBends) modelBendsPlayer.bipedBody).rotation = new net.gobbob.mobends.util.SmoothVector3f();
                        //}

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
                        //if (modelBendsPlayer.bipedBody != null) {
                        //    ((ModelRendererBends) modelBendsPlayer.bipedBody).rotation = new net.gobbob.mobends.util.SmoothVector3f();
                        //}

                    }

                    if (JinRyuu.JRMCore.i.ExtendedPlayer.get((EntityPlayer) argEntity).getBlocking() == 1) {
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
                                    ((ModelRendererBends) modelBendsPlayer.bipedHead).rotation.setSmoothX(modelBendsPlayer.headRotationX - 90, 0.3F);
                                }
                            }
                        }
                    }

                    if(Minecraft.getMinecraft().thePlayer.getEntityId() == data.entityID) {
                        for (String key : keys.keySet()) {
                            if (Keyboard.isKeyDown(keys.get(key))) {
                                BendsPack.animate(modelBendsPlayer, "player", key);
                            }
                        }
                    }

                    //((ModelRendererBends)modelBendsPlayer.bipedBody).pre_rotation = new SmoothVector3f();

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
    //同步脸部,死亡光环之类
    private static void sync(JinRyuu.JRMCore.entity.ModelBipedBody body) throws IllegalAccessException {
        ArrayList<String> head = new ArrayList<>(Arrays.asList("halo", "Fro", "Fro0", "Fro1", "Fro2", "Fro5", "SaiO", "SaiE", "Nam","face1","face2","face3","face4","face5","face6","bipedHeadg", "bipedHeadt", "bipedHeadv", "bipedHeadgh", "bipedHeadg2", "bipedHeadght", "bipedHeadgt", "bipedHeadgtt", "bipedHeadc7", "bipedHeadc8", "bipedHeadrad", "bipedHeadradl", "bipedHeadradl2", "bipedHeadssj3t", "bipedHeadsg", "bipedHeadst", "bipedHeadsv", "bipedHeadsgh", "bipedHeadssg", "bipedHeadsst", "bipedHeadssv", "bipedHeadssgh"));
        ArrayList<String> rightArm = new ArrayList<>();
        rightArm.add("rightarmshoulder");//GiTurtleMdl
        rightArm.add("Rarm");//DBC_GiTurtleMdl
        ArrayList<String> leftArm = new ArrayList<>();
        leftArm.add("leftarmshoulder");//GiTurtleMdl
        leftArm.add("Larm");//DBC_GiTurtleMdl
        for (Field field : body.getClass().getFields()) {
            if (field.getType().getName().equals("net.minecraft.client.model.ModelRenderer")) {
                field.setAccessible(true);
                ModelRenderer mr1 = (ModelRenderer) field.get(body);
                if (mr1 != null) {
                    if(head.contains(field.getName())){
                            if (!(field.get(body) instanceof ModelRenderer2)) {
                                ModelRenderer2 modelRenderer2 = new ModelRenderer2(body,convertBends(body,ModelRendererBends.class,(ModelRenderer)field.get(body)),true);
                                body.bipedBody.addChild(modelRenderer2);
                                field.set(body, modelRenderer2);
                            }
                            ((ModelRendererBends) field.get(body)).sync((ModelRendererBends) body.bipedHead);

                        //sync(((ModelRendererBends2)field.get(body)), (BodyRenderer) body.bipedBody, (ModelRendererBends) body.bipedHead);
                        setOffset(mr1,body.bipedHead);
                    }else if(rightArm.contains(field.getName())){
                        /*if(body.bipedRightArm.childModels != null){
                            if(!body.bipedRightArm.childModels.contains(field.get(body))){
                                ModelRenderer2 modelRenderer2 = new ModelRenderer2(body,(PartRenderer) body.bipedRightArm,(ModelRenderer)field.get(body));
                                field.set(body,modelRenderer2);
                                body.bipedRightArm.addChild(modelRenderer2);
                            }
                        }*/
                        if (!(field.get(body) instanceof ModelRenderer2)) {
                            ModelRenderer2 modelRenderer2 = new ModelRenderer2(body,convertBends(body,ModelRendererBends.class,(ModelRenderer)field.get(body)),true);
                            body.bipedBody.addChild(modelRenderer2);
                            field.set(body, modelRenderer2);
                        }
                        ((ModelRendererBends) field.get(body)).sync((ModelRendererBends) body.bipedRightArm);
                        //sync(((ModelRendererBends)field.get(body)), (BodyRenderer) body.bipedBody, (PartRenderer) ((ModelRenderer2)body.bipedRightArm).source);
                        setOffset(mr1,body.bipedRightArm);
                    }else if(leftArm.contains(field.getName())){
                        /*if(body.bipedLeftArm.childModels != null){
                            if(!body.bipedLeftArm.childModels.contains(field.get(body))){
                                ModelRenderer2 modelRenderer2 = new ModelRenderer2(body, (PartRenderer) body.bipedHead,(ModelRenderer)field.get(body));
                                field.set(body,modelRenderer2);
                                body.bipedLeftArm.addChild(modelRenderer2);
                            }
                        }*/
                        if (!(field.get(body) instanceof ModelRenderer2)) {
                            ModelRenderer2 modelRenderer2 = new ModelRenderer2(body,convertBends(body,ModelRendererBends.class,(ModelRenderer)field.get(body)),true);
                            body.bipedBody.addChild(modelRenderer2);
                            field.set(body, modelRenderer2);
                        }
                        ((ModelRendererBends) field.get(body)).sync((ModelRendererBends) body.bipedLeftArm);
                        //sync(((ModelRendererBends)field.get(body)), (BodyRenderer) body.bipedBody, (PartRenderer) ((ModelRenderer2)body.bipedLeftArm).source);
                        setOffset(mr1,body.bipedLeftArm);
                    }
                }
            }
        }
    }

    private static void sync(ModelRendererBends2 bends1,BodyRenderer bodyRenderer,ModelRendererBends partRenderer){
        if(bends1 != null && bodyRenderer != null && partRenderer != null){
            bends1.sync(bodyRenderer);
            bends1.offsetX = bodyRenderer.offsetX + partRenderer.offsetX;
            bends1.offsetY = bodyRenderer.offsetY + partRenderer.offsetY;
            bends1.offsetZ = bodyRenderer.offsetZ + partRenderer.offsetZ;
            bends1.rotationPointX2 = bodyRenderer.rotationPointX + partRenderer.rotationPointX;
            bends1.rotationPointY2 = bodyRenderer.rotationPointY + partRenderer.rotationPointY;
            bends1.rotationPointZ2 = bodyRenderer.rotationPointZ + partRenderer.rotationPointZ;
            bends1.rotateAngleX2 = bodyRenderer.rotateAngleX + partRenderer.rotateAngleX;
            bends1.rotateAngleY2 = bodyRenderer.rotateAngleY + partRenderer.rotateAngleY;
            bends1.rotateAngleZ2 = bodyRenderer.rotateAngleZ + partRenderer.rotateAngleZ;
            translate(bends1.rotation.vFinal,partRenderer.rotation.vFinal);
            translate(bends1.rotation.vOld,partRenderer.rotation.vOld);
            translate(bends1.rotation.vSmooth,partRenderer.rotation.vSmooth);
            translate(bends1.rotation.completion,partRenderer.rotation.completion);
            translate(bends1.rotation.smoothness,partRenderer.rotation.smoothness);
            translate(bends1.pre_rotation.vFinal,partRenderer.pre_rotation.vFinal);
            translate(bends1.pre_rotation.vOld,partRenderer.pre_rotation.vOld);
            translate(bends1.pre_rotation.vSmooth,partRenderer.pre_rotation.vSmooth);
            translate(bends1.pre_rotation.completion,partRenderer.pre_rotation.completion);
            translate(bends1.pre_rotation.smoothness,partRenderer.pre_rotation.smoothness);
        }
    }

    private static void translate(Vector3f vec1,Vector3f vec2){
        vec1.translate(vec2.getX(),vec2.getY(), vec2.getZ());
    }





    private static ModelRendererBends convertBends(ModelBase base,Class<? extends ModelRendererBends> cl, ModelRenderer modelRenderer) {
        if(cl.isAssignableFrom(modelRenderer.getClass())){
            return (ModelRendererBends) modelRenderer;
        }
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
        ModelRendererBends bends = gson.fromJson(jsonObject,cl);

        if(bends instanceof ModelRendererBends2){
            ((ModelRendererBends2)bends).rotationPointX2 = modelRenderer.rotationPointX;
            ((ModelRendererBends2)bends).rotationPointY2 = modelRenderer.rotationPointY;
            ((ModelRendererBends2)bends).rotationPointZ2 = modelRenderer.rotationPointZ;
        }
        if(bends instanceof PartRenderer){
            //((PartRenderer)bends).thisTicksToRender = new ArrayList<>();
            ((PartRenderer)bends).modelBase = base;
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

    private static void setOffset(ModelRenderer mr1,ModelRenderer mr2){
        mr1.offsetX = mr2.offsetX;
        mr1.offsetY = mr2.offsetY;
        mr1.offsetZ = mr2.offsetZ;
    }

    public static void renderBody(JinRyuu.JRMCore.entity.ModelBipedBody body, float par2){
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
                body.bipedBody.render(par2);
                body.bipedRightLeg.render(par2);
                body.bipedLeftLeg.render(par2);
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
            float scale = (float)b * 0.03F;
            float br = 0.4235988F + scale;
            float bs = 0.8F + scale;
            float bsY = 0.85F + scale * 0.5F;
            float bt = 0.1F * scale;
            boolean bounce = body.Entity.onGround || body.Entity.isInWater();
            float bspeed = body.Entity.isSprinting() ? 1.5F : (body.Entity.isSneaking() ? 0.5F : 1.0F);
            float bbY = (bounce ? MathHelper.sin(body.rot1 * 0.6662F * bspeed * 1.5F + 3.1415927F) * body.rot2 * 0.03F : 0.0F) * (float)b * 0.1119F;
            GL11.glTranslatef(0.0F, (f6 - 1.0F) * 1.5F + bbY, 0.015F + bt);
            GL11.glScalef(1.0F, bsY, bs);
            setRotation(body.breast, -br, 0.0F, 0.0F);
            setRotation(body.breast2, br, 3.141593F, 0.0F);
            if (bounce) {
                ModelRenderer var10000 = body.breast;
                var10000.rotateAngleX += -MathHelper.cos(body.rot1 * 0.6662F * bspeed + 3.1415927F) * body.rot2 * 0.05F * (float)b * 0.1119F;
                var10000.rotateAngleY += MathHelper.cos(body.rot1 * 0.6662F * bspeed + 3.1415927F) * body.rot2 * 0.02F * (float)b * 0.1119F;
                var10000 = body.breast2;
                var10000.rotateAngleX += MathHelper.cos(body.rot1 * 0.6662F * bspeed + 3.1415927F) * body.rot2 * 0.05F * (float)b * 0.1119F;
                var10000.rotateAngleY += MathHelper.cos(body.rot1 * 0.6662F * bspeed + 3.1415927F) * body.rot2 * 0.02F * (float)b * 0.1119F;
            }

            body.Bbreast.render(par2);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glScalef(1.0F / f6 * (g <= 1 ? 1.0F : 0.7F), 1.0F / f6, 1.0F / f6 * (g <= 1 ? 1.0F : 0.7F));
            GL11.glTranslatef(0.0F, (f6 - 1.0F) * 1.5F, 0.0F);
            body.body.render(par2);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glScalef(1.0F / f6 * (g <= 1 ? 1.0F : 0.75F), 1.0F / f6, 1.0F / f6 * (g <= 1 ? 1.0F : 0.75F) * (1.0F + 0.005F * (float)p));
            if (body.isSneak) {
                GL11.glTranslatef(0.0F, (f6 - 1.0F) * 1.5F, 0.0F);
            } else {
                GL11.glTranslatef(0.0F, (f6 - 1.0F) * 1.5F, -0.02F - 5.0E-4F * (float)p);
            }

            body.hip.render(par2);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glScalef(1.0F / f6 * (g <= 1 ? 1.0F : 0.65F), 1.0F / f6, 1.0F / f6 * (g <= 1 ? 1.0F : 0.65F) * (1.0F + 0.001F * (float)p));
            if (body.isSneak) {
                GL11.glTranslatef(0.0F, (f6 - 1.0F) * 1.5F, 0.0F);
            } else {
                GL11.glTranslatef(0.0F, (f6 - 1.0F) * 1.5F, -0.04F - 1.0E-4F * (float)p);
            }

            body.waist.render(par2);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glScalef(1.0F / f6 * (g <= 1 ? 1.0F : 0.85F), 1.0F / f6, 1.0F / f6 * (g <= 1 ? 1.0F : 0.85F) * (1.0F + 0.005F * (float)p));
            GL11.glTranslatef(0.0F, (f6 - 1.0F) * 1.5F, 0.0F - 5.0E-4F * (float)p);
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
