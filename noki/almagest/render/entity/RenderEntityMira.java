package noki.almagest.render.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import noki.almagest.ModInfo;

public class RenderEntityMira extends RenderLiving {
	
	public static final ResourceLocation texture = new ResourceLocation(ModInfo.ID.toLowerCase(), "textures/entity/mira.png");
	
	public RenderEntityMira() {
		
		// 引数:(ModelBase以降を継承したクラスのインスタンス、影の大きさ)
		super(Minecraft.getMinecraft().getRenderManager(), new ModelMira(), 0.6f);
		
	}
	
	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		
		return texture;
		
	}
	
}
