package noki.almagest.block;

import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import noki.almagest.AlmagestCore;
import noki.almagest.helper.HelperConstellation;
import noki.almagest.helper.HelperConstellation.Constellation;
import noki.almagest.tile.TileConstellation;


/**********
 * @class BlockConstellation
 *
 * @description 97の星図(IAUの88星座+オリジナル9星座)を表すブロックです。種類はNBTで管理。
 * @see ItemBlockConstellation, TileConstellation, RenderTileConstellation.
 */
public class BlockConstellation extends BlockContainer {

	//******************************//
	// define member variables.
	//******************************//
	// indicating whether it is ecliptical constellation or not.
	public static final PropertyBool ECLIPTICAL = PropertyBool.create("ecliptical");
	// indicating whether the constellation is lacking stars or not.
	public static final PropertyBool COMPLETE = PropertyBool.create("complete");
	
	
	//******************************//
	// define member methods.
	//******************************//
	public BlockConstellation(String unlocalizedName, CreativeTabs tab) {
		
		super(Material.glass);
		this.setUnlocalizedName(unlocalizedName);
		this.setHardness(0.3F);
		this.setStepSound(soundTypeGlass);
		this.setLightLevel(1.0F);
		this.setDefaultState(this.blockState.getBaseState().withProperty(ECLIPTICAL, false).withProperty(COMPLETE, true));
		this.setCreativeTab(tab);
		
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		
		return new TileConstellation();
		
	}
	
	@Override
	public int getRenderType() {
		
		return 3;
		
	}
	
	@Override
	public boolean isOpaqueCube() {
		
		return false;
		
	}
	
	@Override
	public boolean isFullCube() {
		
		return false;
		
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public EnumWorldBlockLayer getBlockLayer() {
		
		return EnumWorldBlockLayer.TRANSLUCENT;
		
	}
	
/*	@Override
	public int getLightValue(IBlockAccess world, BlockPos pos) {
		
		if((Boolean)world.getBlockState(pos).getValue(COMPLETE) == true) {
			return 15;
		}
		else {
			return 8;
		}
		
	}*/
	
	@Override
	public int getMetaFromState(IBlockState state) {
		
		if((Boolean)state.getValue(ECLIPTICAL) == false) {
			return (Boolean)state.getValue(COMPLETE) == true ? 0 : 2;
		}
		else {
			return (Boolean)state.getValue(COMPLETE) == true ? 1 : 3;
		}
		
	}
	
	@Override
	public IBlockState getStateFromMeta(int metadata) {
		
		switch(metadata) {
			case 0:
				return this.getDefaultState().withProperty(ECLIPTICAL, false).withProperty(COMPLETE, true);
			case 1:
				return this.getDefaultState().withProperty(ECLIPTICAL, true).withProperty(COMPLETE, true);
			case 2:
				return this.getDefaultState().withProperty(ECLIPTICAL, false).withProperty(COMPLETE, false);
			case 3:
				return this.getDefaultState().withProperty(ECLIPTICAL, true).withProperty(COMPLETE, false);
			default:
				return this.getDefaultState().withProperty(ECLIPTICAL, false).withProperty(COMPLETE, true);		
		}
		
	}
	
	@Override
	protected BlockState createBlockState() {
		
		return new BlockState(this, ECLIPTICAL, COMPLETE);
		
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state,
			EntityPlayer player, EnumFacing facing, float hitX, float hitY, float hitZ) {
		
		if(!world.isRemote) {
			TileEntity tile = world.getTileEntity(pos);
			if(tile != null && tile instanceof TileConstellation) {
//				AlmagestCore.log("switch rotation.");
				((TileConstellation)tile).switchRotation();
				world.markBlockForUpdate(pos);
				tile.markDirty();
			}
		}
		
		return true;
		
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		
		List<ItemStack> stacks = new ArrayList<ItemStack>();
		
		TileEntity tile = world.getTileEntity(pos);
		if(tile != null && tile instanceof TileConstellation) {
			int constNumber = ((TileConstellation)tile).getConstNumber();
			stacks.add(HelperConstellation.getConstStack(constNumber, 1));
		}
		
		return stacks;
		
	}

	// NBT付スタックをドロップさせるには、removedByPlayer()とharvesBlock()のオーバライドが必要。
	// setBlockToAir()のタイミングを遅らせる。
	@Override
	public boolean removedByPlayer(World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
		
		if(willHarvest == true)  {
			return true;
		}
		return super.removedByPlayer(world, pos, player, willHarvest);
		
	}
	
	@Override
	public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te) {
		
		super.harvestBlock(world, player, pos, state, te);
		world.setBlockToAir(pos);
		
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	@SuppressWarnings({ "rawtypes", "unchecked" })	//about List.
	public void getSubBlocks(Item item, CreativeTabs tab, List list) {
		
		for(Constellation each: Constellation.values()) {
			list.add(HelperConstellation.getConstStack(each.getId(), 1));
			if(each.isIncomplete() == true) {
				AlmagestCore.log("const number is %s.", each.getId());
				list.add(HelperConstellation.getIncompleteConstStack(each, 1));
			}
		}
		
	}

}
