package xiaobang;

import JinRyuu.JRMCore.entity.ModelBipedBody;
import com.google.gson.*;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.gobbob.mobends.client.model.ModelBoxBends;
import net.gobbob.mobends.client.model.ModelRendererBends;
import net.gobbob.mobends.client.model.ModelRendererBends_SeperatedChild;
import net.gobbob.mobends.util.SmoothVector3f;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Editor {
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
    
    }

    public static void edit(ModelBipedBody body){
        try {
            Field field1 = ModelBipedBody.class.getField("bipedRightForeArm");
            Field field2 = ModelBipedBody.class.getField("bipedLeftForeArm");
            Field field3 = ModelBipedBody.class.getField("bipedLeftForeLeg");
            Field field4 = ModelBipedBody.class.getField("bipedRightForeLeg");

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
            bipedRightForeLeg.setRotationPoint(0.0F, 6.0F, -2.0F);
            bipedRightForeLeg.getBox().offsetTextureQuad(bipedRightForeLeg, 3, 0.0F, -6.0F);

            ModelRendererBends bipedLeftForeLeg = new ModelRendererBends(body, 0, 22);
            bipedLeftForeLeg.mirror = true;
            bipedLeftForeLeg.addBox(-2.0F, 0.0F, 0.0F, 4, 6, 4, 0);
            bipedLeftForeLeg.setRotationPoint(0.0F, 6.0F, -2.0F);

            setField(field1,body,bipedRightForeArm);
            setField(field2,body,bipedLeftForeArm);
            setField(field3,body,bipedLeftForeLeg);
            setField(field4,body,bipedRightForeLeg);

            body.bipedBody = convertBends(body,false,body.bipedBody).setShowChildIfHidden(true);
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
        }catch (NoSuchFieldException | IllegalAccessException noSuchFieldException){
            throw new RuntimeException(noSuchFieldException);
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
