package xiaobang.renderer;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

import java.util.ArrayList;

public class ModelRenderer2 extends ModelRenderer {
    public ModelRenderer source;
    public PartRenderer part;
    public ModelRenderer2(ModelBase base,PartRenderer partRenderer,ModelRenderer source) {
        super(base);
        this.source = source;
        this.part = partRenderer;
    }

    public void render(float par1){
        if(part.thisTicksToRender == null){
            part.thisTicksToRender = new ArrayList<>();
        }
        part.thisTicksToRender.add(this);
    }

    public void render2(float par1){
        source.render(par1);
    }
}
