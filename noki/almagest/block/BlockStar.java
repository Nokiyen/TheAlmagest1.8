package noki.almagest.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


/**********
 * @class BlockStar
 *
 * @description
 * @see ItemBlockStar.
 */
public class BlockStar extends Block {
	
	//******************************//
	// define member variables.
	//******************************//
	public static final PropertyInteger METADATA = PropertyInteger.create("metadata", 0, 7);

	
	//******************************//
	// define member methods.
	//******************************//
	public BlockStar(String unlocalizedName, CreativeTabs tab) {
		
		super(Material.rock);
		this.setUnlocalizedName(unlocalizedName);
		this.setHardness(3.0F);
		this.setResistance(5.0F);
		this.setStepSound(soundTypePiston);
		this.setLightLevel(1.0F);
		this.setDefaultState(this.blockState.getBaseState().withProperty(METADATA, 0));
		this.setCreativeTab(tab);
		
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		
		return ((Integer)state.getValue(METADATA)).intValue();
		
	}
	
	@Override
	public IBlockState getStateFromMeta(int metadata) {
		
		return this.getDefaultState().withProperty(METADATA, Integer.valueOf(metadata));
		
	}
	
	@Override
	protected BlockState createBlockState() {
		
		return new BlockState(this, METADATA);
		
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	@SuppressWarnings({ "rawtypes", "unchecked" })	//about List.
	public void getSubBlocks(Item item, CreativeTabs tab, List list) {
		
		for(int i=0; i<=7; i++) {
			list.add(new ItemStack(item, 1, i));
		}
		
	}

}
