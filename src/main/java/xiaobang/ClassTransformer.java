package xiaobang;


import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.ClassNode;

public class ClassTransformer implements IClassTransformer {


    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if ("JinRyuu.JRMCore.entity.ModelBipedBody".equals(transformedName)) {//修改init,setRotationAngles方法,加上bipedRightForeArm,bipedLeftForeArm,bipedRightForeLeg,bipedLeftForeLeg属性
            ClassReader cr = new ClassReader(basicClass);
            ClassNode cn = new ClassNode();
            cr.accept(cn, 0);
            ClassWriter cw = new ClassWriter(cr, 0);
            cr.accept(new ClassVisitor(Opcodes.ASM5, cw) {

                @Override
                public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {


                    MethodVisitor methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);
                    if (name.equals("<init>")) {
                        if (desc.equals("(FFII)V")) {
                            return new MyMethodAdapter(Opcodes.ASM5, methodVisitor, "init");
                        }
                    } else if (name.equals("setRotationAngles") || name.equals("func_78087_a")) {
                        return new MyMethodAdapter(Opcodes.ASM5, methodVisitor, "setRotationAngles");
                    } /*else if(name.equals("renderBody")){
                        return new MyMethodAdapter(Opcodes.ASM5,methodVisitor,name);
                    }*/
                    return methodVisitor;
                }

                @Override
                public void visitEnd() {
                    FieldVisitor fv = cv.visitField(Opcodes.ACC_PUBLIC, "bipedRightForeArm", "Lnet/minecraft/client/model/ModelRenderer;", null, null);
                    if (fv != null) {
                        fv.visitEnd();
                    }
                    FieldVisitor fv2 = cv.visitField(Opcodes.ACC_PUBLIC, "bipedLeftForeArm", "Lnet/minecraft/client/model/ModelRenderer;", null, null);
                    if (fv2 != null) {
                        fv2.visitEnd();
                    }
                    FieldVisitor fv3 = cv.visitField(Opcodes.ACC_PUBLIC, "bipedLeftForeLeg", "Lnet/minecraft/client/model/ModelRenderer;", null, null);
                    if (fv3 != null) {
                        fv3.visitEnd();
                    }
                    FieldVisitor fv4 = cv.visitField(Opcodes.ACC_PUBLIC, "bipedRightForeLeg", "Lnet/minecraft/client/model/ModelRenderer;", null, null);
                    if (fv4 != null) {
                        fv4.visitEnd();
                    }
                    cv.visitEnd();
                }

            }, ClassReader.EXPAND_FRAMES);
            return cw.toByteArray();
        } else if ("JinRyuu.JBRA.RenderPlayerJBRA".equals(transformedName)) {//玩家身体转动,Editor.rotateCorpse
            ClassReader cr = new ClassReader(basicClass);
            ClassNode cn = new ClassNode();
            cr.accept(cn, 0);
            ClassWriter cw = new ClassWriter(cr, 0);
            cr.accept(new ClassVisitor(Opcodes.ASM5, cw) {
                @Override
                public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                    MethodVisitor methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);
                    if (name.equals("func_77043_a") || name.equals("rotateCorpse")) {
                        if (desc.equals("(Lnet/minecraft/client/entity/AbstractClientPlayer;FFF)V")) {
                            return new MyMethodAdapter(Opcodes.ASM5, methodVisitor,"rotateCorpse");
                        }
                    }
                    return methodVisitor;
                }
            }, ClassReader.EXPAND_FRAMES);
            return cw.toByteArray();
        } else if ("net.gobbob.mobends.client.model.ModelBoxBends".equals(transformedName)) {//将offsetTextureQuad方法里面的输出关掉
            ClassReader cr = new ClassReader(basicClass);
            ClassNode cn = new ClassNode();
            cr.accept(cn, 0);
            ClassWriter cw = new ClassWriter(cr, 0);
            cr.accept(new ClassVisitor(Opcodes.ASM5, cw) {
                @Override
                public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                    MethodVisitor methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);
                    if (name.equals("offsetTextureQuad")) {
                        return new MyMethodAdapter(Opcodes.ASM5, methodVisitor, name);
                    }
                    return methodVisitor;
                }
            }, ClassReader.EXPAND_FRAMES);
            return cw.toByteArray();
        }else if("JinRyuu.JBRA.ModelBipedDBC".equals(transformedName)){
            ClassReader cr = new ClassReader(basicClass);
            ClassNode cn = new ClassNode();
            cr.accept(cn, 0);
            ClassWriter cw = new ClassWriter(cr, 0);
            cr.accept(new ClassVisitor(Opcodes.ASM5, cw) {
                @Override
                public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                    MethodVisitor methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);
                    if (name.equals("renderHairsV2")) {
                        if(desc.equals("(FLjava/lang/String;FIIIILJinRyuu/JBRA/RenderPlayerJBRA;Lnet/minecraft/client/entity/AbstractClientPlayer;)V")) {
                            return new MyMethodAdapter(Opcodes.ASM5, methodVisitor, name);
                        }
                    }
                    return methodVisitor;
                }
            }, ClassReader.EXPAND_FRAMES);
            return cw.toByteArray();
        }else if("net.minecraft.client.renderer.texture.TextureUtil".equals(transformedName)){
            ClassReader cr = new ClassReader(basicClass);
            ClassNode cn = new ClassNode();
            cr.accept(cn, 0);
            ClassWriter cw = new ClassWriter(cr, 0);
            cr.accept(new ClassVisitor(Opcodes.ASM5, cw) {
                @Override
                public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                    MethodVisitor methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);
                    if (name.equals("bindTexture") || name.equals("func_94277_a")) {
                        return new MyMethodAdapter(Opcodes.ASM5, methodVisitor,"bindTexture");
                    }
                    return methodVisitor;
                }
            }, ClassReader.EXPAND_FRAMES);
            return cw.toByteArray();
        }
        return basicClass;
    }

    static class MyMethodAdapter extends MethodVisitor {
        String methodName;

        byte times = 0;

        public MyMethodAdapter(int api, MethodVisitor methodVisitor, String methodName) {
            super(api, methodVisitor);
            this.methodName = methodName;
        }

        /*@Override
        public void visitCode(){
            if(methodName.equals("renderBody")) {
                mv.visitVarInsn(Opcodes.ALOAD, 0);
                mv.visitVarInsn(Opcodes.FLOAD, 1);
                mv.visitMethodInsn(Opcodes.INVOKESTATIC, "xiaobang/ModelBipedBody", "renderBody", "(LJinRyuu/JRMCore/entity/ModelBipedBody;F)V", false);
                mv.visitInsn(Opcodes.RETURN);
                mv.visitEnd();
            }
        }*/

        @Override
        public void visitInsn(int opcode) {
            if (opcode == Opcodes.ARETURN || opcode == Opcodes.RETURN) {
                switch (methodName) {
                    case "init":
                        mv.visitVarInsn(Opcodes.ALOAD, 0);
                        mv.visitVarInsn(Opcodes.FLOAD,1);
                        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "xiaobang/ModelBipedBody", "init", "(LJinRyuu/JRMCore/entity/ModelBipedBody;F)V", false);
                        break;
                    case "setRotationAngles":
                        mv.visitVarInsn(Opcodes.ALOAD, 0);
                        mv.visitVarInsn(Opcodes.FLOAD, 1);
                        mv.visitVarInsn(Opcodes.FLOAD, 2);
                        mv.visitVarInsn(Opcodes.FLOAD, 3);
                        mv.visitVarInsn(Opcodes.FLOAD, 4);
                        mv.visitVarInsn(Opcodes.FLOAD, 5);
                        mv.visitVarInsn(Opcodes.FLOAD, 6);
                        mv.visitVarInsn(Opcodes.ALOAD, 7);
                        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "xiaobang/ModelBipedBody", "setRotationAngles", "(LJinRyuu/JRMCore/entity/ModelBipedBody;FFFFFFLnet/minecraft/entity/Entity;)V", false);
                        break;
                    case "rotateCorpse":
                        mv.visitVarInsn(Opcodes.ALOAD, 1);
                        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "xiaobang/Editor", "rotateCorpse", "(Lnet/minecraft/client/entity/AbstractClientPlayer;)V", false);
                        break;
                    case "bindTexture":
                        mv.visitVarInsn(Opcodes.ILOAD, 1);
                        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "xiaobang/Editor", "bindTexture", "(I)V", false);
                        break;
                }
            }
            super.visitInsn(opcode);
        }

        @Override
        public void visitMethodInsn(int opcode, String owner, String name,
                                    String desc, boolean itf) {
            if(methodName.equals("offsetTextureQuad")){
                if(opcode == Opcodes.INVOKEVIRTUAL && owner.equals("java/io/PrintStream") && name.equals("println") && desc.equals("(Ljava/lang/String;)V")){
                    return;
                }
            }else if(methodName.equals("renderHairsV2")){
                if(opcode == Opcodes.INVOKESTATIC && owner.equals("org/lwjgl/opengl/GL11") && name.equals("glPushMatrix") && desc.equals("()V")){
                    times++;
                    if (times == 3){
                        mv.visitMethodInsn(Opcodes.INVOKESTATIC,"org/lwjgl/opengl/GL11","glPopMatrix","()V",false);
                        super.visitMethodInsn(opcode, owner, name, desc, itf);
                        mv.visitVarInsn(Opcodes.ALOAD,0);
                        mv.visitVarInsn(Opcodes.FLOAD,1);
                        mv.visitMethodInsn(Opcodes.INVOKESTATIC,"xiaobang/Editor","hairRotate","(LJinRyuu/JBRA/ModelBipedDBC;F)V",false);
                        return;
                    }
                }
            }
            super.visitMethodInsn(opcode, owner, name, desc, itf);
        }

    }

}
