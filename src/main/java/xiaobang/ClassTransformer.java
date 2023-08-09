package xiaobang;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.*;
import org.objectweb.asm.commons.AdviceAdapter;
import org.objectweb.asm.tree.ClassNode;

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
                    return new MyMethodAdapter(Opcodes.ASM5, methodVisitor, access, name, desc);
                }
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
    static class MyMethodAdapter extends AdviceAdapter {

        /**
         * Creates a new {@link AdviceAdapter}.
         *
         * @param api    the ASM API version implemented by this visitor. Must be one
         *               of {@link Opcodes#ASM4} or {@link Opcodes#ASM5}.
         * @param mv     the method visitor to which this adapter delegates calls.
         * @param access the method's access flags (see {@link Opcodes}).
         * @param name   the method's name.
         * @param desc   the method's descriptor (see {@link Type Type}).
         */
        protected MyMethodAdapter(int api, MethodVisitor mv, int access, String name, String desc) {
            super(api, mv, access, name, desc);
        }

        @Override
        protected void onMethodEnter() {
            super.onMethodEnter();
            mv.visitMethodInsn(INVOKESTATIC, "xiaobang/DBCMoBends", "edit", "(LJinRyuu/JRMCore/entity/ModelBipedBody;)V", false);
        }
    }

}
