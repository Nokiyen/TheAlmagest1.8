package noki.almagest.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import noki.almagest.AlmagestCore;
import noki.almagest.AlmagestData;


/**********
 * @class BlockBookrest
 *
 * @description
 */
public class BlockBookrest extends Block {

	//******************************//
	// define member variables.
	//******************************//
	
	
	//******************************//
	// define member methods.
	//******************************//
	public BlockBookrest(String unlocalizedName, CreativeTabs tab) {
		
		super(Material.wood);
		this.setUnlocalizedName(unlocalizedName);
		this.setHardness(2.5F);
		this.setStepSound(soundTypeWood);
		this.setCreativeTab(tab);
		
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state,
			EntityPlayer player, EnumFacing facing, float hitX, float hitY, float hitZ) {
		
		player.openGui(AlmagestCore.instance, AlmagestData.guiID_bookrest, world, pos.getX(), pos.getY(), pos.getZ());
		return true;
		
	}

}
