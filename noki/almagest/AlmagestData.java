package noki.almagest;

import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.item.Item;
import net.minecraft.world.biome.BiomeGenBase;
import noki.almagest.block.BlockBookrest;
import noki.almagest.block.BlockConstellation;
import noki.almagest.block.BlockStar;
import noki.almagest.entity.EntityMira;
import noki.almagest.event.EventConstellation;
import noki.almagest.gui.GuiHandler;
import noki.almagest.item.ItemAlmagest;
import noki.almagest.item.ItemAtlas;
import noki.almagest.item.ItemBlockConstellation;
import noki.almagest.item.ItemBlockStar;
import noki.almagest.item.ItemMissingStar;
import noki.almagest.world.AtlasWorldProvider;


/**********
 * @class AlmagestData
 *
 * @description このModの各種データの格納、登録をするクラスです。
 */
public class AlmagestData {
	
	//******************************//
	// define member variables.
	//******************************//
	//--------------------
	// Creative Tab.
	//--------------------
	public static CreativeTabs tab;
	
	//--------------------
	// Blocks.
	//--------------------
	public static Block blockConstellation;
	public static final String nameConstellation = "constellation_block";
	public static Block blockStar;
	public static final String nameStar = "atlas_star";
	public static Block blockBookrest;
	public static final String nameBookrest = "bookrest";
	public static Block blockTreasure;
	public static final String nameTreasure = "treasure_box";
	
	//--------------------
	// Items.
	//--------------------
	public static Item itemMissingStar;
	public static final String nameMissingStar = "missing_star";
	public static Item itemAtlas;
	public static final String nameAtlas ="atlas";
	public static Item itemAlmagest;
	public static final String nameAlmagest = "almagest";
	public static Item itemStardust;
	public static final String nameStardust = "stardust";
	
	//--------------------
	// Tags of NBT.
	//--------------------
	public static final String NBT_prefix = "8fcd6c65_Almagest";
	
	//--------------------
	// Dimension.
	//--------------------
	public static int dimensionID;
	
	//--------------------
	// GUI.
	//--------------------
	public static int guiID_almagest = 0;
	public static int guiID_bookrest = 1;
	
	
	//******************************//
	// define member methods.
	//******************************//
	public static void registerPre() {
		
		// creative tab.
		tab = new TabAlmagest();
		
		// block.
		blockConstellation = new BlockConstellation(nameConstellation, tab);
		GameRegistry.registerBlock(blockConstellation, ItemBlockConstellation.class, nameConstellation);
		blockStar = new BlockStar(nameStar, tab);
		GameRegistry.registerBlock(blockStar, ItemBlockStar.class, nameStar);
		blockBookrest = new BlockBookrest(nameBookrest, tab);
		GameRegistry.registerBlock(blockBookrest, nameBookrest);
		
		// item.
		itemMissingStar = new ItemMissingStar(nameMissingStar, tab);
		GameRegistry.registerItem(itemMissingStar, nameMissingStar);
		itemAtlas = new ItemAtlas(nameAtlas, tab);
		GameRegistry.registerItem(itemAtlas, nameAtlas);
		itemAlmagest = new ItemAlmagest(nameAlmagest, tab);
		GameRegistry.registerItem(itemAlmagest, nameAlmagest);
		
		// name for json.
		if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
			// block.
			ModelBakery.addVariantName(Item.getItemFromBlock(blockConstellation),
					ModInfo.ID.toLowerCase() + ":" + "block_constellation_0",
					ModInfo.ID.toLowerCase() + ":" + "block_constellation_1"
			);
			ModelBakery.addVariantName(Item.getItemFromBlock(blockStar),
					ModInfo.ID.toLowerCase() + ":" + "block_atlas_star_0",
					ModInfo.ID.toLowerCase() + ":" + "block_atlas_star_1",
					ModInfo.ID.toLowerCase() + ":" + "block_atlas_star_2",
					ModInfo.ID.toLowerCase() + ":" + "block_atlas_star_3",
					ModInfo.ID.toLowerCase() + ":" + "block_atlas_star_4",
					ModInfo.ID.toLowerCase() + ":" + "block_atlas_star_5",
					ModInfo.ID.toLowerCase() + ":" + "block_atlas_star_6",
					ModInfo.ID.toLowerCase() + ":" + "block_atlas_star_7"
			);
			ModelBakery.addVariantName(Item.getItemFromBlock(blockBookrest),
					ModInfo.ID.toLowerCase() + ":" + "block_bookrest"
			);
			
			// item.
			ModelBakery.addVariantName(itemMissingStar,
					ModInfo.ID.toLowerCase() + ":" + "item_missing_star_0",
					ModInfo.ID.toLowerCase() + ":" + "item_missing_star_1",
					ModInfo.ID.toLowerCase() + ":" + "item_missing_star_2",
					ModInfo.ID.toLowerCase() + ":" + "item_missing_star_3",
					ModInfo.ID.toLowerCase() + ":" + "item_missing_star_4",
					ModInfo.ID.toLowerCase() + ":" + "item_missing_star_5",
					ModInfo.ID.toLowerCase() + ":" + "item_missing_star_6",
					ModInfo.ID.toLowerCase() + ":" + "item_missing_star_7"
			);
			ModelBakery.addVariantName(itemAtlas,
					ModInfo.ID.toLowerCase() + ":" + "item_atlas"
			);
			ModelBakery.addVariantName(itemAlmagest,
					ModInfo.ID.toLowerCase() + ":" + "item_almagest"
			);
		}
		
		// world.
		dimensionID = DimensionManager.getNextFreeDimId();
		DimensionManager.registerProviderType(dimensionID, AtlasWorldProvider.class, false);
		DimensionManager.registerDimension(dimensionID, dimensionID);

	}
	
	public static void register() {
		
		// json.
		if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
			// block.
			registerItemJson(Item.getItemFromBlock(blockConstellation), 0, "block_constellation_0");
			registerItemJson(Item.getItemFromBlock(blockConstellation), 1, "block_constellation_1");
			registerItemJson(Item.getItemFromBlock(blockConstellation), 2, "block_constellation_0");
			registerItemJson(Item.getItemFromBlock(blockConstellation), 3, "block_constellation_1");
			
			registerItemJson(Item.getItemFromBlock(blockStar), 0, "block_atlas_star_0");
			registerItemJson(Item.getItemFromBlock(blockStar), 1, "block_atlas_star_1");
			registerItemJson(Item.getItemFromBlock(blockStar), 2, "block_atlas_star_2");
			registerItemJson(Item.getItemFromBlock(blockStar), 3, "block_atlas_star_3");
			registerItemJson(Item.getItemFromBlock(blockStar), 4, "block_atlas_star_4");
			registerItemJson(Item.getItemFromBlock(blockStar), 5, "block_atlas_star_5");
			registerItemJson(Item.getItemFromBlock(blockStar), 6, "block_atlas_star_6");
			registerItemJson(Item.getItemFromBlock(blockStar), 7, "block_atlas_star_7");
			
			registerItemJson(Item.getItemFromBlock(blockBookrest), 0, "block_bookrest");
			
			// item.
			registerItemJson(itemMissingStar, 0, "item_missing_star_0");
			registerItemJson(itemMissingStar, 1, "item_missing_star_1");
			registerItemJson(itemMissingStar, 2, "item_missing_star_2");
			registerItemJson(itemMissingStar, 3, "item_missing_star_3");
			registerItemJson(itemMissingStar, 4, "item_missing_star_4");
			registerItemJson(itemMissingStar, 5, "item_missing_star_5");
			registerItemJson(itemMissingStar, 6, "item_missing_star_6");
			registerItemJson(itemMissingStar, 7, "item_missing_star_7");
			
			registerItemJson(itemAtlas, 0, "item_atlas");
			registerItemJson(itemAlmagest, 0, "item_almagest");
		}
		
		//gui.
		NetworkRegistry.INSTANCE.registerGuiHandler(AlmagestCore.instance, new GuiHandler());
		
		//event.
		MinecraftForge.EVENT_BUS.register(new EventConstellation() );
		
		//entity.
		EntityRegistry.registerGlobalEntityID(EntityMira.class, "Mira",
				EntityRegistry.findGlobalUniqueEntityId(), 0xAAAAAA, 0xCCCCCC);
		EntityRegistry.registerModEntity(EntityMira.class, "Mira", 0, AlmagestCore.instance, 250, 1, false);
		EntityRegistry.addSpawn(EntityMira.class, 20, 1, 4, EnumCreatureType.CREATURE, BiomeGenBase.plains);
		
	}
	
	public static void registerItemJson(Item item, int metadata, String name) {
		
		ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
		ModelResourceLocation resource = new ModelResourceLocation(ModInfo.ID.toLowerCase() + ":" + name, "inventory");
		mesher.register(item, metadata, resource);		
		
	}
	
	//--------------------
	// Inner Class.
	//--------------------
	public static class TabAlmagest extends CreativeTabs {
		
		//******************************//
		// define member variables.
		//******************************//
		public static String label = "Almagest";

		
		//******************************//
		// define member methods.
		//******************************//
		public TabAlmagest() {
			
			super(label);
			
		}
		
		@Override
		@SideOnly(Side.CLIENT)
		public Item getTabIconItem() {
			
			return Item.getItemFromBlock(blockConstellation);

		}
		
	}

}
