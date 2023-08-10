package xiaobang;


import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.ClassNode;

public class ClassTransformer implements IClassTransformer {



    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {

        if ("JinRyuu.JRMCore.entity.ModelBipedBody".equals(transformedName)) {
            ClassReader cr = new ClassReader(basicClass);
            ClassNode cn = new ClassNode();
            cr.accept(cn, 0);
            ClassWriter cw = new ClassWriter(cr, 0);
            cr.accept(new ClassVisitor(Opcodes.ASM5,cw) {

                @Override
                public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                    MethodVisitor methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);
                    if (name.equals("<init>")) {
                        if(desc.equals("(FFII)V")){
                            return new MyMethodAdapter(Opcodes.ASM5, methodVisitor,"init");
                        }
                    }else if(name.equals("setRotationAngles") || name.equals("func_78087_a")){
                        return new MyMethodAdapter(Opcodes.ASM5,methodVisitor,"setRotationAngles");
                    }
                    return methodVisitor;
                }

                @Override
                public void visitEnd() {
                    FieldVisitor fv = cv.visitField(Opcodes.ACC_PUBLIC,"bipedRightForeArm", "Lnet/minecraft/client/model/ModelRenderer;", null, null);
                    if (fv != null) {
                        fv.visitEnd();
                    }
                    FieldVisitor fv2 = cv.visitField(Opcodes.ACC_PUBLIC,"bipedLeftForeArm", "Lnet/minecraft/client/model/ModelRenderer;", null, null);
                    if (fv2 != null) {
                        fv2.visitEnd();
                    }
                    FieldVisitor fv3 = cv.visitField(Opcodes.ACC_PUBLIC,"bipedLeftForeLeg", "Lnet/minecraft/client/model/ModelRenderer;", null, null);
                    if (fv3 != null) {
                        fv3.visitEnd();
                    }
                    FieldVisitor fv4 = cv.visitField(Opcodes.ACC_PUBLIC,"bipedRightForeLeg", "Lnet/minecraft/client/model/ModelRenderer;", null, null);
                    if (fv4 != null) {
                        fv4.visitEnd();
                    }
                    cv.visitEnd();
                }

            }, ClassReader.EXPAND_FRAMES);
            return cw.toByteArray();
        }else if("JinRyuu.JBRA.RenderPlayerJBRA".equals(transformedName)){
            ClassReader cr = new ClassReader(basicClass);
            ClassNode cn = new ClassNode();
            cr.accept(cn, 0);
            ClassWriter cw = new ClassWriter(cr, 0);
            cr.accept(new ClassVisitor(Opcodes.ASM5,cw) {
                @Override
                public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                    MethodVisitor methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);
                    if (name.equals("func_77043_a") || name.equals("rotateCorpse")) {
                        if(desc.equals("(Lnet/minecraft/client/entity/AbstractClientPlayer;FFF)V")){
                            return new MyMethodAdapter(Opcodes.ASM5, methodVisitor,name);
                        }
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
        public MyMethodAdapter(int api,MethodVisitor methodVisitor,String methodName) {
            super(api,methodVisitor);
            this.methodName = methodName;
        }

        @Override
        public void visitInsn(int opcode) {
            if(opcode == Opcodes.ARETURN || opcode == Opcodes.RETURN ) {
                if(methodName.equals("init")) {
                    mv.visitVarInsn(Opcodes.ALOAD, 0);
                    mv.visitMethodInsn(Opcodes.INVOKESTATIC, "xiaobang/Editor", "edit", "(LJinRyuu/JRMCore/entity/ModelBipedBody;)V", false);
                }else if(methodName.equals("setRotationAngles")){
                    mv.visitVarInsn(Opcodes.ALOAD, 0);
                    mv.visitVarInsn(Opcodes.FLOAD, 1);
                    mv.visitVarInsn(Opcodes.FLOAD, 2);
                    mv.visitVarInsn(Opcodes.FLOAD, 3);
                    mv.visitVarInsn(Opcodes.FLOAD, 4);
                    mv.visitVarInsn(Opcodes.FLOAD, 5);
                    mv.visitVarInsn(Opcodes.FLOAD, 6);
                    mv.visitVarInsn(Opcodes.ALOAD, 7);
                    mv.visitMethodInsn(Opcodes.INVOKESTATIC, "xiaobang/Editor", "setRotationAngles", "(LJinRyuu/JRMCore/entity/ModelBipedBody;FFFFFFLnet/minecraft/entity/Entity;)V", false);
                }else if(methodName.equals("rotateCorpse") || methodName.equals("func_77043_a")){
                    //mv.visitVarInsn(Opcodes.ALOAD,0);
                    //mv.visitVarInsn(Opcodes.ALOAD,1);
                    //mv.visitVarInsn(Opcodes.FLOAD,2);
                    //mv.visitVarInsn(Opcodes.FLOAD,3);
                    //mv.visitVarInsn(Opcodes.FLOAD,4);
                    //mv.visitMethodInsn(Opcodes.INVOKESPECIAL,"net/minecraft/client/renderer/entity/RenderPlayer",methodName,"(Lnet/minecraft/client/entity/AbstractClientPlayer;FFF)V",false);
                    mv.visitVarInsn(Opcodes.ALOAD,1);
                    mv.visitMethodInsn(Opcodes.INVOKESTATIC, "xiaobang/Editor", "rotateCorpse", "(Lnet/minecraft/client/entity/AbstractClientPlayer;)V", false);
                }
            }
            super.visitInsn(opcode);
        }

    }

}
