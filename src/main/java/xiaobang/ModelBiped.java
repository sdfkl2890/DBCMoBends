package xiaobang;

import JinRyuu.JBRA.ModelBipedDBC;
import net.gobbob.mobends.client.model.ModelRendererBends;
import net.gobbob.mobends.client.model.ModelRendererBends_SeperatedChild;
import net.gobbob.mobends.client.model.entity.ModelBendsPlayer;
import net.gobbob.mobends.data.Data_Player;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import org.lwjgl.opengl.GL11;

import java.lang.reflect.Field;

public class ModelBiped extends ModelBipedDBC {
    public final ModelBendsPlayer modelBendsPlayer;


    public ModelRenderer bipedRightForeArm;
    public ModelRenderer bipedLeftForeArm;
    public ModelRenderer bipedRightForeLeg;
    public ModelRenderer bipedLeftForeLeg;

    static Field field_78810_s;
    static Field field_78803_o;
    static Field field_78813_p;

    static {
        try {
            field_78810_s = ModelRenderer.class.getDeclaredField("field_78810_s");
            field_78810_s.setAccessible(true);
            field_78803_o = ModelRenderer.class.getDeclaredField("field_78803_o");
            field_78803_o.setAccessible(true);
            field_78813_p = ModelRenderer.class.getDeclaredField("field_78813_p");
            field_78813_p.setAccessible(true);
        }catch (Exception e){e.printStackTrace();}
    }


    public ModelBiped(float par1, float par2, int par3, int par4){
        super(par1,par2,par3,par4);
        modelBendsPlayer = new ModelBendsPlayer(par1);

        this.bipedHead = newModelRendererBends(false,this,this.bipedHead);
        this.bipedHeadwear = newModelRendererBends(false,this,this.bipedHeadwear);
        //this.bipedBody = newModelRendererBends(false,this,this.bipedBody).setShowChildIfHidden(true);
        this.bipedBody = (new ModelRendererBends(this, 16, 16)).setShowChildIfHidden(true);
        this.bipedBody.addBox(-4.0F, -12.0F, -2.0F, 8, 12, 4, par1);
        this.bipedBody.setRotationPoint(0.0F, 0.0F + par2 + 12.0F, 0.0F);
        //this.SaiT1.rotationPointY = 0;
        //this.SaiT2.rotationPointY = -11.5F;
        //this.FroB.rotationPointY = -10;
        this.bipedLeftArm = ((ModelRendererBends_SeperatedChild)newModelRendererBends(true,this,this.bipedLeftArm)).setMother((ModelRendererBends) bipedBody);
        this.bipedRightArm = ((ModelRendererBends_SeperatedChild)newModelRendererBends(true,this,this.bipedRightArm)).setMother((ModelRendererBends) bipedBody);
        /*this.bipedRightLeg = new ModelRendererBends(this, 0, 16);
        this.bipedRightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, par2);
        this.bipedRightLeg.setRotationPoint(-1.9F, 12.0F + par2, 0.0F);
        this.bipedLeftLeg = new ModelRendererBends(this, 0, 16);
        this.bipedLeftLeg.mirror = true;
        this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, par2);
        this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F + par2, 0.0F);*/
        this.bipedLeftLeg = newModelRendererBends(false,this,this.bipedLeftLeg);
        this.bipedRightLeg = newModelRendererBends(false,this,this.bipedRightLeg);


        this.bipedRightForeArm = new ModelRendererBends(this, 40, 22);
        this.bipedRightForeArm.addBox(0.0F, 0.0F, -4.0F, 4, 6, 4, par1);
        this.bipedRightForeArm.setRotationPoint(-3.0F, 4.0F, 2.0F);
        ((ModelRendererBends)this.bipedRightForeArm).getBox().offsetTextureQuad(this.bipedRightForeArm, 3, 0.0F, -6.0F);
        this.bipedLeftForeArm = new ModelRendererBends(this, 40, 22);
        this.bipedLeftForeArm.mirror = true;
        this.bipedLeftForeArm.addBox(0.0F, 0.0F, -4.0F, 4, 6, 4, par1);
        this.bipedLeftForeArm.setRotationPoint(-1.0F, 4.0F, 2.0F);
        ((ModelRendererBends)this.bipedLeftForeArm).getBox().offsetTextureQuad(this.bipedRightForeArm, 3, 0.0F, -6.0F);
        this.bipedRightForeLeg = new ModelRendererBends(this, 0, 22);
        this.bipedRightForeLeg.addBox(-2.0F, 0.0F, 0.0F, 4, 6, 4, par1);
        this.bipedRightForeLeg.setRotationPoint(0.0F, 6.0F, -2.0F);
        ((ModelRendererBends)this.bipedRightForeLeg).getBox().offsetTextureQuad(this.bipedRightForeLeg, 3, 0.0F, -6.0F);
        this.bipedLeftForeLeg = new ModelRendererBends(this, 0, 22);
        this.bipedLeftForeLeg.mirror = true;
        this.bipedLeftForeLeg.addBox(-2.0F, 0.0F, 0.0F, 4, 6, 4, par1);
        this.bipedLeftForeLeg.setRotationPoint(0.0F, 6.0F, -2.0F);
        ((ModelRendererBends)this.bipedLeftForeLeg).getBox().offsetTextureQuad(this.bipedLeftForeLeg, 3, 0.0F, -6.0F);

        this.bipedRightArm.addChild(this.bipedRightForeArm);
        this.bipedLeftArm.addChild(this.bipedLeftForeArm);
        this.bipedRightLeg.addChild(this.bipedRightForeLeg);
        this.bipedLeftLeg.addChild(this.bipedLeftForeLeg);

        ((ModelRendererBends_SeperatedChild)this.bipedRightArm).setSeperatedPart((ModelRendererBends)this.bipedRightForeArm);
        ((ModelRendererBends_SeperatedChild)this.bipedLeftArm).setSeperatedPart((ModelRendererBends)this.bipedLeftForeArm);
        ((ModelRendererBends)this.bipedRightArm).offsetBox_Add(-0.01F, 0.0F, -0.01F).resizeBox(4.02F, 6.0F, 4.02F).updateVertices();
        ((ModelRendererBends)this.bipedLeftArm).offsetBox_Add(-0.01F, 0.0F, -0.01F).resizeBox(4.02F, 6.0F, 4.02F).updateVertices();
        ((ModelRendererBends)this.bipedRightLeg).offsetBox_Add(-0.01F, 0.0F, -0.01F).resizeBox(4.02F, 6.0F, 4.02F).updateVertices();
        ((ModelRendererBends)this.bipedLeftLeg).offsetBox_Add(-0.01F, 0.0F, -0.01F).resizeBox(4.02F, 6.0F, 4.02F).updateVertices();

    }

    public void setModel(ModelBiped model) throws Exception {
        String[] array = new String[]{"field_78116_c","field_78112_f","field_78113_g","field_78123_h","field_78124_i"};
        Field[] fields = model.getClass().getFields();
        for (Field field1 : fields){
            if(field1.getType().getName().equals("net.minecraft.client.model.ModelRenderer")){
                field1.setAccessible(true);
                ModelRenderer mr_1 = (ModelRenderer) field1.get(model);
                ModelBox box_1 = (ModelBox) mr_1.cubeList.get(0);
                for (String str : array){
                    Field field = net.minecraft.client.model.ModelBiped.class.getField(str);
                    field.setAccessible(true);
                    ModelRenderer mr_2 = (ModelRenderer) field.get(model);
                    ModelBox box_2 = (ModelBox) mr_2.cubeList.get(0);
                    //
                    if((box_1.posX1 + mr_1.rotationPointX) == (box_2.posX1 + mr_2.rotationPointX) && (box_1.posY1 + mr_1.rotationPointY) == (box_2.posY1 + mr_2.rotationPointY) && (box_1.posZ1 + mr_1.rotationPointZ) == (box_2.posZ1 + mr_2.rotationPointZ)){
                        mr_1.rotateAngleX = mr_2.rotateAngleX;
                        mr_1.rotateAngleY = mr_2.rotateAngleY;
                        mr_1.rotateAngleZ = mr_2.rotateAngleZ;
                        mr_1.offsetX = mr_2.offsetX;
                        mr_1.offsetY = mr_2.offsetY;
                        mr_1.offsetZ = mr_2.offsetZ;
                    }
                }
            }
        }
    }
    


    public void setRotationAngles(float argSwingTime, float argSwingAmount, float argArmSway, float argHeadY, float argHeadX, float argNr6, net.minecraft.entity.Entity argEntity) {
        super.setRotationAngles(argSwingTime,argSwingAmount,argArmSway,argHeadY,argHeadX,argNr6,argEntity);
        if ((Minecraft.getMinecraft()).theWorld == null)
            return;
        if ((Minecraft.getMinecraft()).theWorld.isRemote && Minecraft.getMinecraft().isGamePaused())
            return;
        modelBendsPlayer.setRotationAngles(argSwingTime,argSwingAmount,argArmSway,argHeadY,argHeadX,argNr6,argEntity);
        syncModelInfo(this,Data_Player.get(argEntity.getEntityId()));
        try {
            setModel(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ModelRendererBends newModelRendererBends(boolean seperated,ModelBase base,ModelRenderer renderer) {
        if(!(renderer instanceof ModelRendererBends)){
            if(renderer != null && base != null){
                ModelRendererBends bends;
                if (seperated){
                    bends = new ModelRendererBends_SeperatedChild(base,renderer.boxName);
                }else{
                    bends = new ModelRendererBends(base,renderer.boxName);
                }
                try {
                    if (field_78803_o.get(renderer) != null && field_78813_p.get(renderer) != null) {
                        bends.setTextureOffset((int)field_78803_o.get(renderer),(int)field_78813_p.get(renderer));
                    }
                    bends.txOffsetX = (int) field_78803_o.get(renderer);
                    bends.txOffsetY = (int) field_78813_p.get(renderer);
                }catch (Exception e){e.printStackTrace();}
                for (Object obj : renderer.cubeList) {
                    ModelBox box = (ModelBox)obj;
                    bends.func_78790_a(box.posX1, box.posY1, box.posZ1, (int) (box.posX2 - box.posX1), (int) (box.posY2 - box.posY1), (int) (box.posZ2 - box.posZ1), 0);
                }
                bends.mirror = renderer.mirror;
                bends.rotateAngleX = renderer.rotateAngleX;
                bends.rotateAngleY = renderer.rotateAngleY;
                bends.rotateAngleZ = renderer.rotateAngleZ;
                bends.childModels = renderer.childModels;
                bends.setRotationPoint(renderer.rotationPointX,renderer.rotationPointY,renderer.rotationPointZ);
                renderer = bends;
            }
        }
        return (ModelRendererBends) renderer;
    }

    public static void syncModelInfo(ModelBiped argModel,Data_Player data){
        ((ModelRendererBends)argModel.bipedHead).sync(data.head);
        ((ModelRendererBends)argModel.bipedHeadwear).sync(data.headwear);
        ((ModelRendererBends)argModel.bipedBody).sync(data.body);
        ((ModelRendererBends)argModel.bipedRightArm).sync(data.rightArm);
        ((ModelRendererBends)argModel.bipedLeftArm).sync(data.leftArm);
        ((ModelRendererBends)argModel.bipedRightLeg).sync(data.rightLeg);
        ((ModelRendererBends)argModel.bipedLeftLeg).sync(data.leftLeg);

        ((ModelRendererBends)argModel.bipedRightForeArm).sync(data.rightForeArm);
        ((ModelRendererBends)argModel.bipedLeftForeArm).sync(data.leftForeArm);
        ((ModelRendererBends)argModel.bipedRightForeLeg).sync(data.rightForeLeg);
        ((ModelRendererBends)argModel.bipedLeftForeLeg).sync(data.leftForeLeg);

    }

    public void updateWithEntityData(AbstractClientPlayer argPlayer) {
        Data_Player data = Data_Player.get(argPlayer.getEntityId());
        if (data != null) {
            this.modelBendsPlayer.renderOffset.set(data.renderOffset);
            this.modelBendsPlayer.renderRotation.set(data.renderRotation);
            this.modelBendsPlayer.renderItemRotation.set(data.renderItemRotation);
        }
    }

    public void postRender(float argScale) {
        GL11.glTranslatef(this.modelBendsPlayer.renderOffset.vSmooth.x * argScale, this.modelBendsPlayer.renderOffset.vSmooth.y * argScale, this.modelBendsPlayer.renderOffset.vSmooth.z * argScale);
        GL11.glRotatef(-this.modelBendsPlayer.renderRotation.getX(), 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(-this.modelBendsPlayer.renderRotation.getY(), 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(this.modelBendsPlayer.renderRotation.getZ(), 0.0F, 0.0F, 1.0F);
    }



}
