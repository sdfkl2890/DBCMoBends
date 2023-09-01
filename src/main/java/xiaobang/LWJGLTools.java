package xiaobang;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

import java.nio.FloatBuffer;

public class LWJGLTools {

    //获取当前绑定纹理材质id
    public static int getCurrentBindingTexture() {
        return GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);
    }

    //获取当前纹理绑定颜色,分别为red,green,blue,alpha
    public static FloatBuffer getCurrentColorRGBA() {
        FloatBuffer currentColor = BufferUtils.createFloatBuffer(16);
        GL11.glGetFloat(GL11.GL_CURRENT_COLOR, currentColor);
        return currentColor;
    }

    //获取当前模型视图矩阵状态
    public static FloatBuffer getCurrentModelViewMatrix() {
        FloatBuffer modelViewMatrix = BufferUtils.createFloatBuffer(16);
        GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, modelViewMatrix);
        return modelViewMatrix;
    }

    //覆盖当前模型视图矩阵
    public static void loadMatrix(FloatBuffer matrix) {
        if (matrix != null) {
            GL11.glMatrixMode(GL11.GL_MODELVIEW);
            GL11.glLoadMatrix(matrix);
        }
    }
}
