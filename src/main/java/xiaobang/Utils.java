package xiaobang;

import JinRyuu.JRMCore.JRMCoreH;
import com.google.gson.*;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.gobbob.mobends.client.model.ModelBoxBends;
import net.gobbob.mobends.client.model.ModelRendererBends;
import net.gobbob.mobends.util.SmoothVector3f;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import xiaobang.renderer.PartRenderer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static xiaobang.ModelBipedBody.modelBendsPlayer;

@SideOnly(Side.CLIENT)
public class Utils {
    public static ArrayList<Vector3f> scale = new ArrayList<>();
    public static ArrayList<Vector3f> translate = new ArrayList<>();
    public static ArrayList<Vector4f> rotate = new ArrayList<>();

    public static void glScalef(float x,float y,float z){
        GL11.glScalef(x,y,z);
        scale.add(new Vector3f(x,y,z));
    }
    public static void glTranslatef(float x,float y,float z){

        GL11.glTranslatef(x,y,z);
        translate.add(new Vector3f(x,y,z));
    }
    public static void glRotatef(float angle,float x,float y,float z){
        GL11.glRotatef(angle, x, y, z);
        rotate.add(new Vector4f(x,y,z,angle));
    }

    //自定义初始化Gson
    static Gson gson = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
        @Override
        public boolean shouldSkipField(FieldAttributes fieldAttributes) {//对以下属性不进行序列化
            List<String> list = new ArrayList<>();
            list.add("field_78812_q");
            list.add("field_78810_s");
            list.add("field_78811_r");
            list.add("displayList");
            list.add("compiled");
            list.add("baseModel");
            list.add("cubeList");
            list.add("childModels");
            return list.contains(fieldAttributes.getName());
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    }).create();

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


    public static void setField(Field field, Object instance, Object value) throws IllegalAccessException {
        field.setAccessible(true);
        field.set(instance, value);
    }

    public static void setRotation(ModelRenderer model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    //通过Gson将父类ModelRenderer转换成子类ModelRendererBends及其子类
    public static ModelRendererBends convertBends(ModelBase base, Class<? extends ModelRendererBends> cl, ModelRenderer modelRenderer) {
        if (modelRenderer != null) {
            if (cl.isAssignableFrom(modelRenderer.getClass())) {
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
            ModelRendererBends bends = gson.fromJson(jsonObject, cl);

            if (bends instanceof PartRenderer) {
                //((PartRenderer)bends).thisTicksToRender = new ArrayList<>();
                ((PartRenderer) bends).modelBase = base;
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
        return null;
    }

    public static List convertBends(ModelRenderer modelRenderer, int textureOffsetX, int textureOffsetY, List boxes) {//将ModelBox转换成ModelBoxBends
        ArrayList list = new ArrayList<>();
        for (Object obj : boxes) {
            if (obj instanceof ModelBox) {
                ModelBox box = (ModelBox) obj;
                list.add(new ModelBoxBends(modelRenderer, textureOffsetX, textureOffsetY, box.posX1, box.posY1, box.posZ1, (int) (box.posX2 - box.posX1), (int) (box.posY2 - box.posY1), (int) (box.posZ2 - box.posZ1), 0));
            }
        }
        return list;
    }

    public static void setOffset(ModelRenderer mr1, ModelRenderer mr2) {
        mr1.offsetX = mr2.offsetX;
        mr1.offsetY = mr2.offsetY;
        mr1.offsetZ = mr2.offsetZ;
    }

    public static int containsPlayer(String name) {//判断当前客户端是否存在玩家
        if (JRMCoreH.plyrs != null && JRMCoreH.plyrs.length > 0 && JRMCoreH.dnn(2) && JRMCoreH.dnn(10)) {
            for (int i = 0; i < JRMCoreH.plyrs.length; i++) {
                if (JRMCoreH.plyrs[i].equals(name)) {
                    return i;
                }
            }
        }

        return 9527;
    }

    public static void rotation(ModelRendererBends bends){
        GL11.glRotatef(-bends.pre_rotation.getY(), 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(bends.pre_rotation.getX(), 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(bends.pre_rotation.getZ(), 0.0F, 0.0F, 1.0F);
        if (bends.rotateAngleZ != 0.0F) {
            GL11.glRotatef(bends.rotateAngleZ * 57.295776F, 0.0F, 0.0F, 1.0F);
        }

        if (bends.rotateAngleY != 0.0F) {
            GL11.glRotatef(bends.rotateAngleY * 57.295776F, 0.0F, 1.0F, 0.0F);
        }

        if (bends.rotateAngleX != 0.0F) {
            GL11.glRotatef(bends.rotateAngleX * 57.295776F, 1.0F, 0.0F, 0.0F);
        }
    }
}
