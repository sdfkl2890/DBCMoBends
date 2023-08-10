package xiaobang;

import JinRyuu.JRMCore.entity.ModelBipedBody;
import com.google.gson.*;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.gobbob.mobends.client.model.ModelBoxBends;
import net.gobbob.mobends.client.model.ModelRendererBends;
import net.gobbob.mobends.client.model.ModelRendererBends_SeperatedChild;
import net.gobbob.mobends.client.model.entity.ModelBendsPlayer;
import net.gobbob.mobends.data.Data_Player;
import net.gobbob.mobends.util.SmoothVector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SideOnly(Side.CLIENT)
public class Editor {
    static Field rightForeArm;
    static Field leftForeArm;
    static Field rightForeLeg;
    static Field leftForeLeg;

    static{
        try {
            rightForeArm = ModelBipedBody.class.getField("bipedRightForeArm");
            rightForeArm.setAccessible(true);
            leftForeArm = ModelBipedBody.class.getField("bipedLeftForeArm");
            leftForeArm.setAccessible(true);
            leftForeLeg = ModelBipedBody.class.getField("bipedLeftForeLeg");
            leftForeLeg.setAccessible(true);
            rightForeLeg = ModelBipedBody.class.getField("bipedRightForeLeg");
            rightForeLeg.setAccessible(true);
        }catch (Exception e){
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
            if(list.contains(fieldAttributes.getName())){
                return true;
            }
            return false;
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    }).create();

    public static void setRotationAngles(ModelBipedBody body,float argSwingTime, float argSwingAmount, float argArmSway, float argHeadY, float argHeadX, float argNr6, Entity argEntity){
        modelBendsPlayer.setRotationAngles(argSwingTime,argSwingAmount,argArmSway,argHeadY,argHeadX,argNr6,argEntity);
        if (Minecraft.getMinecraft().theWorld != null) {
            if (!Minecraft.getMinecraft().theWorld.isRemote || !Minecraft.getMinecraft().isGamePaused()) {
                Data_Player data = Data_Player.get(argEntity.getEntityId());
                ((ModelRendererBends)body.bipedHead).sync(data.head);
                ((ModelRendererBends)body.bipedBody).sync(data.body);
                ((ModelRendererBends)body.bipedRightArm).sync(data.rightArm);
                ((ModelRendererBends)body.bipedLeftArm).sync(data.leftArm);
                ((ModelRendererBends)body.bipedRightLeg).sync(data.rightLeg);
                ((ModelRendererBends)body.bipedLeftLeg).sync(data.leftLeg);
                try {
                    ((ModelRendererBends) rightForeArm.get(body)).sync(data.rightForeArm);
                    ((ModelRendererBends) leftForeArm.get(body)).sync(data.leftForeArm);
                    ((ModelRendererBends) rightForeLeg.get(body)).sync(data.rightForeLeg);
                    ((ModelRendererBends) leftForeLeg.get(body)).sync(data.leftForeLeg);
                    sync(body);
                }catch (IllegalAccessException e){e.printStackTrace();}

            }
        }
    }

    //将脸部,死亡光环之类同步
    private static void sync(ModelBiped body) throws IllegalAccessException {
        ModelRenderer[] mrs = new ModelRenderer[]{body.bipedHead,body.bipedRightArm,body.bipedLeftArm,body.bipedRightLeg,body.bipedLeftLeg};
        //ArrayList<String> list = new ArrayList<>(Arrays.asList("field_78114_d", "halo", "Fro", "Fro0", "Fro1", "Fro2", "Fro5", "SaiO", "SaiE", "Nam", "face1", "face2", "face3", "face4", "face5", "face6", "bipedHeadg", "bipedHeadt", "bipedHeadv", "bipedHeadgh", "bipedHeadg2", "bipedHeadght", "bipedHeadgt", "bipedHeadgtt", "bipedHeadc7", "bipedHeadc8", "bipedHeadrad", "bipedHeadradl", "bipedHeadradl2", "bipedHeadssj3t", "bipedHeadsg", "bipedHeadst", "bipedHeadsv", "bipedHeadsgh", "bipedHeadssg", "bipedHeadsst", "bipedHeadssv", "bipedHeadssgh"));
        //System.out.println("class " + body.getClass().getName());
        for (Field field : body.getClass().getFields()){
            if(field.getType().getName().equals("net.minecraft.client.model.ModelRenderer")) {
                field.setAccessible(true);
                ModelRenderer mr1 = (ModelRenderer) field.get(body);
                if(mr1 != null) {
                    ModelBox box1 = (ModelBox) mr1.cubeList.get(0);
                    for (ModelRenderer mr2 : mrs) {
                        ModelBox box2 = (ModelBox) mr2.cubeList.get(0);
                        if ((box1.posX1 + mr1.rotationPointX) == (box2.posX1 + mr2.rotationPointX) && (box1.posY1 + mr1.rotationPointY) == (box2.posY1 + mr2.rotationPointY) && (box1.posZ1 + mr1.rotationPointZ) == (box2.posZ1 + mr2.rotationPointZ)) {
                            //System.out.println("fieldwa " + field.getName());
                            mr1.rotateAngleX = mr2.rotateAngleX;
                            mr1.rotateAngleY = mr2.rotateAngleY;
                            mr1.rotateAngleZ = mr2.rotateAngleZ;
                        }
                    }
                }
            }
        }

    }

    public static void edit(ModelBipedBody body){
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

            setField(rightForeArm,body,bipedRightForeArm);
            setField(leftForeArm,body,bipedLeftForeArm);
            setField(rightForeLeg,body,bipedRightForeLeg);
            setField(leftForeLeg,body,bipedLeftForeLeg);
            body.bipedHead = convertBends(body,false,body.bipedHead);

            body.bipedBody = convertBends(body,false,body.bipedBody).setShowChildIfHidden(true);
            //body.bipedBody.offsetY += 12.0F;
            //((ModelRendererBends)body.bipedBody).offsetBox_Add(0.0F,-12.0F,0.0F).updateVertices();

            body.bipedRightArm = ((ModelRendererBends_SeperatedChild)convertBends(body,true,body.bipedRightArm)).setMother((ModelRendererBends) body.bipedBody).setSeperatedPart(bipedRightForeArm);
            body.bipedLeftArm = ((ModelRendererBends_SeperatedChild)convertBends(body,true,body.bipedLeftArm)).setMother((ModelRendererBends) body.bipedBody).setSeperatedPart(bipedLeftForeArm);
            body.bipedRightLeg = convertBends(body,false,body.bipedRightLeg);
            body.bipedLeftLeg = convertBends(body,false,body.bipedLeftLeg);

            body.bipedRightArm.addChild(bipedRightForeArm);
            body.bipedLeftArm.addChild(bipedLeftForeArm);
            body.bipedRightLeg.addChild(bipedRightForeLeg);
            body.bipedLeftLeg.addChild(bipedRightForeLeg);

            ((ModelRendererBends)body.bipedRightArm).offsetBox_Add(-0.01F, 0.0F, -0.01F).resizeBox(4.02F, 6.0F, 4.02F).updateVertices();
            ((ModelRendererBends)body.bipedLeftArm).offsetBox_Add(-0.01F, 0.0F, -0.01F).resizeBox(4.02F, 6.0F, 4.02F).updateVertices();
            ((ModelRendererBends)body.bipedRightLeg).offsetBox_Add(-0.01F, 0.0F, -0.01F).resizeBox(4.02F, 6.0F, 4.02F).updateVertices();
            ((ModelRendererBends)body.bipedLeftLeg).offsetBox_Add(-0.01F, 0.0F, -0.01F).resizeBox(4.02F, 6.0F, 4.02F).updateVertices();
        }catch (IllegalAccessException exception){
            throw new RuntimeException(exception);
        }
    }

    public static void rotateCorpse(AbstractClientPlayer player) {
        if (!(player.isEntityAlive() && player.isPlayerSleeping())) {
            modelBendsPlayer.updateWithEntityData(player);
            modelBendsPlayer.postRender(0.0625F);
        }
    }

    private static void setField(Field field,Object instance, Object value) throws IllegalAccessException {
        field.setAccessible(true);
        field.set(instance, value);
    }

    private static ModelRendererBends convertBends(ModelBase base, boolean seperated, ModelRenderer modelRenderer) {
        JsonObject jsonObject = new JsonParser().parse(gson.toJson(modelRenderer)).getAsJsonObject();
        jsonObject.addProperty("scaleX",1.0F);
        jsonObject.addProperty("scaleY",1.0F);
        jsonObject.addProperty("scaleZ",1.0F);
        if(jsonObject.has("textureOffsetY")) {
            jsonObject.addProperty("txOffsetX", jsonObject.get("textureOffsetX").getAsInt());
            jsonObject.addProperty("txOffsetY", jsonObject.get("textureOffsetY").getAsInt());
        }else{
            jsonObject.addProperty("txOffsetX", jsonObject.get("field_78803_o").getAsInt());
            jsonObject.addProperty("txOffsetY", jsonObject.get("field_78813_p").getAsInt());
        }
        ModelRendererBends bends;
        if (seperated){
            bends = gson.fromJson(jsonObject, ModelRendererBends_SeperatedChild.class);
        }else {
            bends = gson.fromJson(jsonObject, ModelRendererBends.class);
        }
        bends.rotation = new SmoothVector3f();
        bends.pre_rotation = new SmoothVector3f();
        bends.setShowChildIfHidden(false);
        bends.cubeList = convertBends(bends,jsonObject.get("txOffsetX").getAsInt(),jsonObject.get("txOffsetY").getAsInt(),modelRenderer.cubeList);
        bends.childModels = modelRenderer.childModels;
        base.boxList.remove(modelRenderer);
        base.boxList.add(bends);

        ReflectionHelper.setPrivateValue(ModelRenderer.class,bends,base,"baseModel","field_78810_s");

        return bends;
    }

    private static List convertBends(ModelRenderer modelRenderer,int textureOffsetX,int textureOffsetY,List boxes){
        ArrayList list = new ArrayList<>();
        for (Object obj : boxes){
            if(obj instanceof ModelBox){
                ModelBox box = (ModelBox) obj;
                list.add(new ModelBoxBends(modelRenderer, textureOffsetX, textureOffsetY, box.posX1, box.posY1,box.posZ1, (int) (box.posX2 - box.posX1), (int) (box.posY2 - box.posY1), (int) (box.posZ2 - box.posZ1), 0));
            }
        }
        return list;
    }
}
