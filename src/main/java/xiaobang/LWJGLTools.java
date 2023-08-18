package xiaobang;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

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
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadMatrix(matrix);
    }

    //对矩阵进行乘法
    public static void multMatrix(FloatBuffer matrix) {
        if(matrix != null) {
            GL11.glMultMatrix(matrix);
        }
    }

    //对矩阵进行加法,待优化
    public static void addMatrix(FloatBuffer matrix){
        if(matrix != null) {
            FloatBuffer current = getCurrentModelViewMatrix();//获取当前模型视图矩阵状态
            current.rewind(); // 重置缓冲区位置
            for (int i = 0; i < 16; i++) {
                current.put(i, current.get(i) + matrix.get(i));//此处并不符合逻辑,与目标结果有点偏离
            }
            current.rewind();
            loadMatrix(current);
        }
    }
}
