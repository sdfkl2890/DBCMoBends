package xiaobang;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.nio.FloatBuffer;

public class LWJGLTools {

    public static int getCurrentBindingTexture(){
        return GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);
    }

    public static FloatBuffer getCurrentColorRGBA(){
        FloatBuffer currentColor = BufferUtils.createFloatBuffer(16);
        GL11.glGetFloat(GL11.GL_CURRENT_COLOR, currentColor);
        return currentColor;
    }

    public static FloatBuffer getCurrentModelViewMartix(){
        FloatBuffer modelViewMatrix = BufferUtils.createFloatBuffer(16);
        GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, modelViewMatrix);
        return modelViewMatrix;
    }

    public static void loadMartix(FloatBuffer floatBuffer){
        floatBuffer.flip();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadMatrix(floatBuffer);
    }
}
