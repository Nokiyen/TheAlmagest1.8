package noki.almagest.proxy;

import noki.almagest.entity.EntityMira;
import noki.almagest.render.entity.RenderEntityMira;
import noki.almagest.render.tile.RenderTileConstellation;
import noki.almagest.tile.TileConstellation;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;


/**********
 * @class ProxyClient
 *
 * @description クライアント用proxyクラス。
 * @description_en Proxy class for Client.
 */
public class ProxyClient implements ProxyCommon {
	
	//******************************//
	// define member variables.
	//******************************//


	//******************************//
	// define member methods.
	//******************************//
	@Override
	public void registerTileEntities() {
		
		GameRegistry.registerTileEntity(TileConstellation.class, "TileConstellation");
		ClientRegistry.bindTileEntitySpecialRenderer(TileConstellation.class, new RenderTileConstellation());
/*		MinecraftForgeClient.registerItemRenderer(
				Item.getItemFromBlock(AlmagestData.blockConstellation), new RenderItemBlockConstellation());*/
		
	}
	
	@Override
	public void registerEntities() {
		
		RenderingRegistry.registerEntityRenderingHandler(EntityMira.class, new RenderEntityMira());
		
	}
			
}
