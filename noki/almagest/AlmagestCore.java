package noki.almagest;

import noki.almagest.helper.HelperConstellation;
import noki.almagest.proxy.ProxyCommon;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.Mod.Metadata;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;


/**********
 * @Mod Almagest
 *
 * @author Nokiyen
 * 
 * @description 失われた星図を求めて。
 */
@Mod(modid = ModInfo.ID, version = ModInfo.VERSION, name = ModInfo.NAME)
public class AlmagestCore {
	
	//******************************//
	// define member variables.
	//******************************//
	@Instance(value = ModInfo.ID)
	public static AlmagestCore instance;
	@Metadata
	public static ModMetadata metadata;	//	extract from mcmod.info file, not java internal coding.
	@SidedProxy(clientSide = ModInfo.PROXY_LOCATION+"ProxyClient", serverSide = ModInfo.PROXY_LOCATION+"ProxyServer")
	public static ProxyCommon proxy;

	public static VersionInfo versionInfo;

	
	//******************************//
	// define member methods.
	//******************************//
	//----------
	//Core Event Methods.
	//----------
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		
		// for mod's specific data.
		versionInfo = new VersionInfo(metadata.modId.toLowerCase(), metadata.version, metadata.updateUrl);
		HelperConstellation.registerStarData(event);
		
		// for items and blocks.
		AlmagestData.registerPre();
		
	}

	@EventHandler
	public void Init(FMLInitializationEvent event) {
		
		AlmagestData.register();
		proxy.registerTileEntities();
		proxy.registerEntities();
				
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		
		// nothing to do.
			
	}
	
	
	//----------
	//Static Method.
	//----------
	public static void log(String message, Object... data) {
		
		FMLLog.fine("[Almagest:LOG] " + message, data);
		
	}
	
}
