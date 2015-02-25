package noki.almagest.item;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noki.almagest.AlmagestData;
import noki.almagest.gui.GuiAlmagest;
import noki.almagest.helper.HelperNBTStack;

public class ItemAlmagest extends Item {
	
	private static String NBT_constPrefix = "constState:";
	
	public ItemAlmagest(String unlocalizedName, CreativeTabs tab) {
		
		this.setUnlocalizedName(unlocalizedName);
		this.setHasSubtypes(true);
		this.setCreativeTab(tab);
		
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		
		if(world.isRemote == true) {
			Minecraft.getMinecraft().displayGuiScreen(new GuiAlmagest(stack));
		}
		
		return stack;
		
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	@SuppressWarnings({ "rawtypes", "unchecked" })	// about list.
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		
		ItemStack stack = new ItemStack(AlmagestData.itemAlmagest, 1, 0);
		for(int i = 1; i <= 88; i++) {
			setConstFlag(stack, i, 1);
		}
		
		list.add(stack);
		
	}
	
	// flag: 0=not get, 1=get&new, 2=get&known
	public static ItemStack setConstFlag(ItemStack stack, int constNumber, int flag) {
		
		return new HelperNBTStack(stack).setInteger(NBT_constPrefix+constNumber, flag).getStack();
		
	}
	
	public static int getConstFlag(ItemStack stack, int constNumber) {
		
		return new HelperNBTStack(stack).getInteger(NBT_constPrefix+constNumber);
		
	}
	
}
