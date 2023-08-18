package xiaobang.renderer;

import java.nio.FloatBuffer;

public class Render {//一个方便存储当前矩阵状态的类
    private final int textureId;
    private final FloatBuffer colors;

    private final FloatBuffer modelViewMartix;


    public Render(int textureId, FloatBuffer colors, FloatBuffer modelViewMartix) {
        this.textureId = textureId;
        this.colors = colors;
        this.modelViewMartix = modelViewMartix;
    }

    public int getTextureId() {
        return this.textureId;
    }

    public FloatBuffer getColors() {
        return this.colors;
    }

    public FloatBuffer getModelViewMartix() {
        return modelViewMartix;
    }
}
