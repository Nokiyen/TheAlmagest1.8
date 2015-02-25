package noki.almagest.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.MathHelper;
import noki.almagest.AlmagestData;


/**********
 * @class ItemBlockStar
 *
 * @description
 */
public class ItemBlockStar extends ItemBlock {
	
	//******************************//
	// define member variables.
	//******************************//
	
	
	//******************************//
	// define member methods.
	//******************************//
	public ItemBlockStar(Block block) {
		
		super(block);
		this.setHasSubtypes(true);
		this.setUnlocalizedName(AlmagestData.nameStar);
		
	}
	
	@Override
	public int getMetadata(int metadata) {
		
		return MathHelper.clamp_int(metadata, 0, 7);
		
	}
	
}
