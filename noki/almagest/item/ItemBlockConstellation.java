package noki.almagest.item;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import noki.almagest.AlmagestData;
import noki.almagest.helper.HelperConstellation;
import noki.almagest.helper.HelperConstellation.Constellation;
import noki.almagest.helper.HelperNBTStack;
import noki.almagest.helper.HelperConstellation.MissingStar;
import noki.almagest.tile.TileConstellation;


/**********
 * @class ItemBlockConstellation
 *
 * @description 星座ブロックのItemBlockです。
 * @see BlockConstellation, TileConstelattion, RenderTileConstellation.
 */
public class ItemBlockConstellation extends ItemBlock {
	
	//******************************//
	// define member variables.
	//******************************//
	public static final String NBT_constNumber = "constNumber";
	public static final String NBT_complete = "complete";
	public static final String NBT_missingStars = "missingStars";
	
	
	//******************************//
	// define member methods.
	//******************************//
	public ItemBlockConstellation(Block block) {
		
		super(block);
		this.setHasSubtypes(true);
		this.setUnlocalizedName(AlmagestData.nameConstellation);
		
	}
	
	@Override
	public int getMetadata(int metadata) {
		
		// 0-3.
		// 0: ecliptical=false,	complete=true
		// 1: ecliptical=true,	complete=true
		// 2: ecliptical=false,	complete=false
		// 3: ecliptical=false,	complete=false
		return MathHelper.clamp_int(metadata, 0, 3);
		
	}
	
	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player,
			World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState state) {
		
		boolean res = super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, state);
		if(res == false) {
			return false;
		}
		
		if(!world.isRemote) {
			TileEntity tile = world.getTileEntity(pos);
			if(tile != null && tile instanceof TileConstellation) {
				((TileConstellation)tile).setConstNumber(getConstNumber(stack));
				((TileConstellation)tile).setMissingStars(getMissingStars(stack));
			}
		}
		
		return true;
		
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		
		HelperNBTStack helper = new HelperNBTStack(stack);
		if(helper.getBoolean(NBT_complete)) {
			return this.getUnlocalizedName() + "." + helper.getInteger(NBT_constNumber);
		}
		else {
			return this.getUnlocalizedName() + "." + helper.getInteger(NBT_constNumber) + ".incomplete";
		}
		
	}
	
	
	//----------
	//Static Methods.
	//----------
	public static ItemStack getConstStack(int constNumber, int stackSize) {
		
		constNumber = MathHelper.clamp_int(constNumber, 1, 88);
		ItemStack stack = new ItemStack(AlmagestData.blockConstellation, stackSize,
				HelperConstellation.getConstMetadata(constNumber));
		
		return new HelperNBTStack(stack).setInteger(NBT_constNumber, constNumber).setBoolean(NBT_complete, true).getStack();
		
	}
	
	public static ItemStack getConstStack(int constNumber, int[] missingStars, int stackSize) {
		
		constNumber = MathHelper.clamp_int(constNumber, 1, 88);
		ItemStack stack = new ItemStack(AlmagestData.blockConstellation, stackSize,
				HelperConstellation.getConstMetadata(constNumber) + 2);
		
		return new HelperNBTStack(stack).setInteger(NBT_constNumber, constNumber).setBoolean(NBT_complete, false)
				.setIntArray(NBT_missingStars, missingStars).getStack();
		
	}
	
	public static ItemStack getIncompleteConstStack(Constellation constellation, int stackSize) {
		
		if(constellation.isIncomplete() == false) {
			return getConstStack(constellation.getId(), stackSize);
		}
		else {
			MissingStar[] missingStars = constellation.getMissingStars();
			
			int[] stars = new int[missingStars.length];
			for(int i = 0; i < missingStars.length; i++) {
				stars[i] = missingStars[i].getStarNumber();
			}
			return getConstStack(constellation.getId(), stars, stackSize);
		}
		
	}
	
	public static int getConstNumber(ItemStack stack) {
		
		return new HelperNBTStack(stack).getInteger(NBT_constNumber);
		
	}
	
	public static int[] getMissingStars(ItemStack stack) {
		
		return new HelperNBTStack(stack).getIntArray(NBT_missingStars);
		
	}

}
