package noki.almagest.proxy;

import noki.almagest.tile.TileConstellation;
import net.minecraftforge.fml.common.registry.GameRegistry;


/**********
 * @class ProxyServer
 *
 * @description サーバ用proxyクラス。
 * @description_en Proxy class for Server.
 */
public class ProxyServer implements ProxyCommon {
	
	//******************************//
	// define member variables.
	//******************************//

	
	//******************************//
	// define member methods.
	//******************************//
	@Override
	public void registerTileEntities() {
		
		GameRegistry.registerTileEntity(TileConstellation.class, "TileConstellation");
				
	}
	
	@Override
	public void registerEntities() {
		
	}

}
