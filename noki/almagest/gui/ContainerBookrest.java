package noki.almagest.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import noki.almagest.AlmagestData;
import noki.almagest.helper.HelperConstellation;


/**********
 * @class ContainerBookrest
 *
 * @description
 */
public class ContainerBookrest  extends Container {
	
	//******************************//
	// define member variables.
	//******************************//
	private EntityPlayer player;
	private World world;
	private int posX;
	private int posY;
	private int posZ;
	
	private IInventory outputInventory;
	private IInventory inputInventory;
	
	
	//******************************//
	// define member methods.
	//******************************//
	public ContainerBookrest(EntityPlayer player, World world, int x, int y, int z) {

		this.player = player;
		this.world = world;
		this.posX = x;
		this.posY = y;
		this.posZ = z;
		
		// bookrest's inventory.
		this.inputInventory = new InventoryBasic("Bookrest", true, 2) {
			public void markDirty() {
				super.markDirty();
				ContainerBookrest.this.onCraftMatrixChanged(this);
			}
		};
		this.outputInventory = new InventoryCraftResult() {
			public void markDirty() {
				super.markDirty();
				ContainerBookrest.this.onCraftMatrixChanged(this);
			}
		};
		
		this.addSlotToContainer(new Slot(this.inputInventory, 0, 27, 21));
		this.addSlotToContainer(new Slot(this.inputInventory, 1, 76, 21));
		this.addSlotToContainer(new Slot(this.outputInventory, 2, 134, 21) {
			// can't place any stack in this slot.
			@Override
			public boolean isItemValid(ItemStack stack) {
				return false;
			}
			public void onPickupFromSlot(EntityPlayer playerIn, ItemStack stack) {
				ContainerBookrest.this.inputInventory.decrStackSize(0, 1);
				ContainerBookrest.this.inputInventory.decrStackSize(1, 1);
			}
		});
		
		// player's inventory.
		for(int i = 0; i < 9; ++i) {
			this.addSlotToContainer(new Slot(this.player.inventory, i, 8 + i * 18, 116));
		}
		for(int i = 0; i < 3; ++i) {
			for(int j = 0; j < 9; ++j) {
				this.addSlotToContainer(new Slot(this.player.inventory, j + i * 9 + 9, 8 + j * 18, 58 + i * 18));
			}
		}
		
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		
		return player.getDistanceSq(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D) <= 64D;
		
	}
	
	@Override
	public void onCraftMatrixChanged(IInventory inventory) {
		
		super.onCraftMatrixChanged(inventory);
		
		if(inventory == this.inputInventory) {
			this.updateOutput();
		}
		
	}
	
	private void updateOutput() {
		
		ItemStack stack1 = this.inputInventory.getStackInSlot(0);
		ItemStack stack2 = this.inputInventory.getStackInSlot(1);
		
		if(stack1 == null || stack2 == null) {
			this.outputInventory.setInventorySlotContents(0, null);
			return;
		}
		if(stack1.getItem() != Item.getItemFromBlock(AlmagestData.blockConstellation)
				|| stack2.getItem() != AlmagestData.itemMissingStar) {
			this.outputInventory.setInventorySlotContents(0, null);
			return;
		}
		
		int constNumber = HelperConstellation.getConstNumber(stack1);
		int[] missingStars = HelperConstellation.getMissingStars(stack1);
		int givenStar = HelperConstellation.getMissingStarNumber(stack2);
		
		boolean flag = false;
		for(int each: missingStars) {
			if(each == givenStar) {
				flag = true;
				break;
			}
		}
		
		if(flag == false) {
			this.outputInventory.setInventorySlotContents(0, null);
			return;
		}
		
		if(missingStars.length == 1) {
			this.outputInventory.setInventorySlotContents(0, HelperConstellation.getConstStack(constNumber, 1));
		}
		else {
			int[] newMissingStars = new int[missingStars.length-1];
			int count = 0;
			for(int each: missingStars) {
				if(each != givenStar) {
					newMissingStars[count] = each;
					count++;
				}
			}
			this.outputInventory.setInventorySlotContents(0, HelperConstellation.getConstStack(constNumber, newMissingStars, 1));
		}
		
		this.detectAndSendChanges();
		
	}
	
	@Override
	public void onContainerClosed(EntityPlayer player) {
		
		super.onContainerClosed(player);
		
		if(this.world.isRemote == false) {
			for(int i = 0; i < this.inputInventory.getSizeInventory(); ++i) {
				ItemStack stack = this.inputInventory.getStackInSlotOnClosing(i);
				if(stack != null) {
					player.dropPlayerItemWithRandomChoice(stack, false);
				}
			}
		}
		
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		
		ItemStack itemstack = null;
		Slot slot = (Slot)this.inventorySlots.get(index);
		
		if(slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			
			if(index == 2) {
				if (!this.mergeItemStack(itemstack1, 3, 39, true)) {
					return null;
				}
				slot.onSlotChange(itemstack1, itemstack);
			}
			else if (index != 0 && index != 1) {
				if(index >= 3 && index < 39 && !this.mergeItemStack(itemstack1, 0, 2, false)) {
					return null;
				}
			}
			else if(!this.mergeItemStack(itemstack1, 3, 39, false)) {
				return null;
			}
			
			if(itemstack1.stackSize == 0) {
				slot.putStack((ItemStack)null);
			}
			else {
				slot.onSlotChanged();
			}
			
			if(itemstack1.stackSize == itemstack.stackSize) {
				return null;
			}
			
			slot.onPickupFromSlot(playerIn, itemstack1);
		}
		
		return itemstack;
		
	}
	
}
