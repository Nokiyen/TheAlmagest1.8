package noki.almagest.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import noki.almagest.AlmagestData;
import noki.almagest.helper.HelperNBTStack;
import noki.almagest.world.AtlasTeleporter;


/**********
 * @class ItemAtlas
 *
 * @description
 */
public class ItemAtlas extends Item {
	
	//******************************//
	// define member variables.
	//******************************//
	private static final String NBT_dimensionID = "dimensionID";
	private static final String NBT_posX = "posX";
	private static final String NBT_posY = "posY";
	private static final String NBT_posZ = "posZ";
	
	
	//******************************//
	// define member methods.
	//******************************//
	public ItemAtlas(String unlocalizedName, CreativeTabs tab) {
		
		this.setUnlocalizedName(unlocalizedName);
		this.setCreativeTab(tab);
		
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		
		if(world.isRemote) {
			return stack;
		}
		
		EntityPlayerMP entityPlayerMP = (EntityPlayerMP)player;
		HelperNBTStack helper = new HelperNBTStack(stack);
		// teleport from Atlas
		if(world.provider.getDimensionId() == AlmagestData.dimensionID) {
			// in case that the last position is memorized.
			if(helper.hasChild()) {
				int dimensionID = helper.getInteger(NBT_dimensionID);
				int posX = helper.getInteger(NBT_posX);
				int posY = helper.getInteger(NBT_posY);
				int posZ = helper.getInteger(NBT_posZ);
				teleportPlayer(dimensionID, (double)posX+0.5D, (double)posY+0.5D, (double)posZ+0.5D, entityPlayerMP);
//				player.travelToDimension(0);
			}
			// otherwise.
			else {
				teleportPlayer(0, entityPlayerMP);
//				player.travelToDimension(0);
			}			
		}
		// teleport from others.
		else {
			helper.setInteger(NBT_dimensionID, world.provider.getDimensionId());
			helper.setInteger(NBT_posX, (int)entityPlayerMP.posX);
			helper.setInteger(NBT_posY, (int)entityPlayerMP.posY);
			helper.setInteger(NBT_posZ, (int)entityPlayerMP.posZ);
			
			teleportPlayer(AlmagestData.dimensionID, entityPlayerMP);
//			player.travelToDimension(AlmagestData.dimensionID);
		}
		
		return stack;
		
	}
	
	
	//----------
	//Static Method.
	//----------
	public static void teleportPlayer(int dimensionID, EntityPlayerMP player) {
		
		BlockPos pos = player.mcServer.worldServerForDimension(dimensionID).getSpawnPoint();
		teleportPlayer(dimensionID, pos.getX(), pos.getY(), pos.getZ(), player);		
		
	}
	
	public static void teleportPlayer(int dimensionID, double posX, double posY, double posZ, EntityPlayerMP player) {
		
		ServerConfigurationManager serverConfigurationManager = player.mcServer.getConfigurationManager();
		WorldServer worldServer = player.mcServer.worldServerForDimension(AlmagestData.dimensionID);
		
		// travel to dimension.
		player.isDead = false;
		player.forceSpawn = true;
		serverConfigurationManager.transferPlayerToDimension(player, dimensionID, new AtlasTeleporter(worldServer));
		player.addExperienceLevel(0);
		// set player's position.
		player.mountEntity(null);
		player.playerNetServerHandler.setPlayerLocation(posX, posY, posZ, player.rotationYaw, player.rotationPitch);		
		
	}
	
}
