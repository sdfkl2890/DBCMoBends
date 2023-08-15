package xiaobang;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.nio.FloatBuffer;

public class LWJGLTools {

    public static int getCurrentBindingTexture() {
        return GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);
    }//材质id

    public static FloatBuffer getCurrentColorRGBA() {//red,green,blue,alpha
        FloatBuffer currentColor = BufferUtils.createFloatBuffer(16);
        GL11.glGetFloat(GL11.GL_CURRENT_COLOR, currentColor);
        return currentColor;
    }

    public static FloatBuffer getCurrentModelViewMatrix() {
        FloatBuffer modelViewMatrix = BufferUtils.createFloatBuffer(16);
        GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, modelViewMatrix);
        return modelViewMatrix;
    }

    public static void loadMatrix(FloatBuffer matrix) {//覆盖当前矩阵
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadMatrix(matrix);
    }

    public static void multMatrix(FloatBuffer matrix) {//对矩阵进行乘法
        if(matrix != null) {
            GL11.glMultMatrix(matrix);
        }
    }

    public static void addMatrix(FloatBuffer matrix){//对矩阵进行加法
        if(matrix != null) {
            FloatBuffer current = getCurrentModelViewMatrix();//获取当前矩阵状态
            current.rewind(); // 重置缓冲区位置
            for (int i = 0; i < 16; i++) {
                current.put(i, current.get(i) + matrix.get(i));
            }
            current.rewind();
            loadMatrix(current);
        }
    }
}
