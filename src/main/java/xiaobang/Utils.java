package xiaobang;

import JinRyuu.JRMCore.JRMCoreH;
import com.google.gson.*;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.gobbob.mobends.client.model.ModelBoxBends;
import net.gobbob.mobends.client.model.ModelRendererBends;
import net.gobbob.mobends.client.model.entity.ModelBendsPlayer;
import net.gobbob.mobends.data.Data_Player;
import net.gobbob.mobends.pack.BendsAction;
import net.gobbob.mobends.pack.BendsTarget;
import net.gobbob.mobends.util.EnumAxis;
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

import static net.gobbob.mobends.pack.BendsPack.getTargetByID;
import static xiaobang.ModelBipedBody.modelBendsPlayer;

@SideOnly(Side.CLIENT)
public class Utils {


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


    //通过Gson将父类ModelRenderer转换成子类ModelRendererBends及其子类
    public static <T extends ModelRendererBends> T convertBends(ModelBase base, Class<T> cl, ModelRenderer modelRenderer) {
        if (modelRenderer != null) {
            if (cl.isAssignableFrom(modelRenderer.getClass())) {
                return (T) modelRenderer;
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
            T bends = gson.fromJson(jsonObject, cl);

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

    public static void setOffset(ModelRenderer mr,float offsetX,float offsetY,float offsetZ) {
        mr.offsetX = offsetX;
        mr.offsetY = offsetY;
        mr.offsetZ = offsetZ;
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

    public static void rotation(ModelRendererBends bends) {
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

    public static void applyToModel(BendsTarget target, ModelRendererBends box, String anim, String model) {
        for(int i = 0; i < target.actions.size(); ++i) {
            if ((target.actions.get(i).anim.equalsIgnoreCase(anim) | target.actions.get(i).anim.equalsIgnoreCase("all")) & target.actions.get(i).model.equalsIgnoreCase(model)) {
                if (target.actions.get(i).prop == BendsAction.EnumBoxProperty.ROT) {
                    box.rotation.setSmooth(target.actions.get(i).axis, target.actions.get(i).getNumber(target.actions.get(i).axis == EnumAxis.X ? box.rotation.vFinal.x : (target.actions.get(i).axis == EnumAxis.Y ? box.rotation.vFinal.y : box.rotation.vFinal.z)), target.actions.get(i).smooth);
                } else if (target.actions.get(i).prop == BendsAction.EnumBoxProperty.SCALE) {
                    if (target.actions.get(i).axis != null) {
                        if(target.actions.get(i).axis == EnumAxis.X) {
                            box.scaleX = target.actions.get(i).getNumber(box.scaleX);
                        }else if(target.actions.get(i).axis == EnumAxis.Y){
                            box.scaleY = target.actions.get(i).getNumber(box.scaleY);
                        }else if(target.actions.get(i).axis == EnumAxis.Z){
                            box.scaleZ = target.actions.get(i).getNumber(box.scaleZ);
                        }
                    }
                }else if(target.actions.get(i).prop == BendsAction.EnumBoxProperty.PREROT){//offset
                    if (target.actions.get(i).axis != null) {

                        if(target.actions.get(i).axis == EnumAxis.X) {
                            box.offsetX = target.actions.get(i).getNumber(target.actions.get(i).smooth);

                        }else if(target.actions.get(i).axis == EnumAxis.Y){
                            box.offsetY = target.actions.get(i).getNumber(target.actions.get(i).smooth);
                        }else if(target.actions.get(i).axis == EnumAxis.Z){
                            box.offsetZ = target.actions.get(i).getNumber(target.actions.get(i).smooth);
                        }
                    }
                }
            }
        }
    }

    public static void animate(ModelBendsPlayer model, String target, String anim) {
        if (getTargetByID(target) != null) {
            BendsTarget bendsTarget = getTargetByID(target);
            applyToModel(bendsTarget,(ModelRendererBends)model.bipedBody, anim, "body");
            applyToModel(bendsTarget,(ModelRendererBends)model.bipedHead, anim, "head");
            applyToModel(bendsTarget,(ModelRendererBends)model.bipedLeftArm, anim, "leftArm");
            applyToModel(bendsTarget,(ModelRendererBends)model.bipedRightArm, anim, "rightArm");
            applyToModel(bendsTarget,(ModelRendererBends)model.bipedLeftLeg, anim, "leftLeg");
            applyToModel(bendsTarget,(ModelRendererBends)model.bipedRightLeg, anim, "rightLeg");
            applyToModel(bendsTarget,(ModelRendererBends)model.bipedLeftForeArm, anim, "leftForeArm");
            applyToModel(bendsTarget,(ModelRendererBends)model.bipedRightForeArm, anim, "rightForeArm");
            applyToModel(bendsTarget,(ModelRendererBends)model.bipedLeftForeLeg, anim, "leftForeLeg");
            applyToModel(bendsTarget,(ModelRendererBends)model.bipedRightForeLeg, anim, "rightForeLeg");
        }
    }

    public static void setRotation(ModelRenderer model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    public static void syncOffset(ModelBendsPlayer modelBendsPlayer, Data_Player data){
        ModelRendererBends[] dataBends = new ModelRendererBends[]{data.head,data.body,data.rightArm,data.leftArm,data.rightLeg,data.leftLeg,data.rightForeArm,data.leftForeArm,data.rightForeLeg,data.leftForeLeg};
        ModelRenderer[] playerBends = new ModelRenderer[]{modelBendsPlayer.bipedHead,modelBendsPlayer.bipedBody,modelBendsPlayer.bipedRightArm,modelBendsPlayer.bipedLeftArm,modelBendsPlayer.bipedRightLeg,modelBendsPlayer.bipedLeftLeg,modelBendsPlayer.bipedRightForeArm,modelBendsPlayer.bipedLeftForeArm,modelBendsPlayer.bipedRightForeLeg,modelBendsPlayer.bipedLeftForeLeg};
        for(int i = 0;i < 10;i++){
            setOffset(dataBends[i],playerBends[i]);
        }
    }

    public static void setOffsetToZero(ModelBendsPlayer modelBendsPlayer){
        ModelRenderer[] playerBends = new ModelRenderer[]{modelBendsPlayer.bipedHead,modelBendsPlayer.bipedBody,modelBendsPlayer.bipedRightArm,modelBendsPlayer.bipedLeftArm,modelBendsPlayer.bipedRightLeg,modelBendsPlayer.bipedLeftLeg,modelBendsPlayer.bipedRightForeArm,modelBendsPlayer.bipedLeftForeArm,modelBendsPlayer.bipedRightForeLeg,modelBendsPlayer.bipedLeftForeLeg};
        for(int i = 0;i < 10;i++){
            setOffset(playerBends[i],0,0,0);
        }
    }
}
