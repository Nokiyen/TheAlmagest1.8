package noki.almagest.item;

import java.util.List;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import noki.almagest.AlmagestData;
import noki.almagest.helper.HelperConstellation;
import noki.almagest.helper.HelperConstellation.MissingStar;
import noki.almagest.helper.HelperConstellation.StarData;
import noki.almagest.helper.HelperNBTStack;


/**********
 * @class ItemMissingStar
 *
 * @description
 */
public class ItemMissingStar extends Item {
	
	//******************************//
	// define member variables.
	//******************************//
	private static final String NBT_starNumber = "starNumber";
	
	
	//******************************//
	// define member methods.
	//******************************//
	public ItemMissingStar(String unlocalizedName, CreativeTabs tab) {
		
		this.setUnlocalizedName(unlocalizedName);
		this.setHasSubtypes(true);
		this.setCreativeTab(tab);
		
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		
		return this.getUnlocalizedName() + "." + new HelperNBTStack(stack).getInteger(NBT_starNumber);
		
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	@SuppressWarnings({ "rawtypes", "unchecked" })	// about list.
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		
		for(MissingStar each: MissingStar.values()) {
			list.add(getMissingStar(each.getStarNumber(), 1));
		}
		
	}
	
	
	//----------
	//Static Methods.
	//----------
	public static ItemStack getMissingStar(int starNumber, int size) {
		
		// check star's existence.
		StarData star = HelperConstellation.getStar(starNumber);
		if(star == null) {
			return null;
		}
		
		// check size.
		size = MathHelper.clamp_int(size, 1, 64);
		
		// create ItemStack with NBT.
		ItemStack stack = new ItemStack(AlmagestData.itemMissingStar, size, star.spectrum.getMetadata());
		return new HelperNBTStack(stack).setInteger(NBT_starNumber, starNumber).getStack();
		
	}
	
	public static int getMissingStarNumber(ItemStack stack) {
		
		return new HelperNBTStack(stack).getInteger(NBT_starNumber);
		
	}
	
}
