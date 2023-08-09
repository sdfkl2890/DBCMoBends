package xiaobang;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.*;
import org.objectweb.asm.commons.AdviceAdapter;
import org.objectweb.asm.tree.ClassNode;

import java.io.FileOutputStream;
import java.io.IOException;

public class ClassTransformer implements IClassTransformer {



    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {

        if (!"JinRyuu.JRMCore.entity.ModelBipedBody".equals(transformedName))
            return basicClass;
        ClassReader cr = new ClassReader(basicClass);
        ClassNode cn = new ClassNode();
        cr.accept(cn, 0);
        ClassWriter cw = new ClassWriter(cr,0);
        MyClassVisitor cv = new MyClassVisitor(cw);
        cr.accept(cv, ClassReader.EXPAND_FRAMES);
        try {
            FileOutputStream fos = new FileOutputStream("C:\\ModelBipedBody.class");
            fos.write(cw.toByteArray());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cw.toByteArray();
    }
    static class MyClassVisitor extends ClassVisitor {
        MyClassVisitor(ClassVisitor classVisitor) {
            super(Opcodes.ASM5, classVisitor);
        }
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
        public FieldVisitor visitField(int access, String name, String desc,
                                       String signature, Object value) {
            return super.visitField(access, name, desc, signature, value);
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
                    mv.visitVarInsn(Opcodes.ALOAD, 1);
                    mv.visitVarInsn(Opcodes.ALOAD, 2);
                    mv.visitVarInsn(Opcodes.ALOAD, 3);
                    mv.visitVarInsn(Opcodes.ALOAD, 4);
                    mv.visitVarInsn(Opcodes.ALOAD, 5);
                    mv.visitVarInsn(Opcodes.ALOAD, 6);
                    mv.visitVarInsn(Opcodes.ALOAD, 7);
                    mv.visitMethodInsn(Opcodes.INVOKESTATIC, "xiaobang/Editor", "setRotationAngles", "(LJinRyuu/JRMCore/entity/ModelBipedBody;FFFFFFLnet/minecraft/entity/Entity;)V", false);
                }
            }
            super.visitInsn(opcode);
        }

    }

}
