package xiaobang.renderer;

import java.nio.FloatBuffer;

public class Render {
    private final int textureId;
    private final FloatBuffer colors;


    public Render(int textureId,FloatBuffer colors){
        this.textureId = textureId;
        this.colors = colors;
    }

    public int getTextureId(){
        return this.textureId;
    }

    public FloatBuffer getColors(){
        return this.colors;
    }
}
