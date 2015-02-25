package noki.almagest.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import noki.almagest.AlmagestCore;
import noki.almagest.helper.HelperConstellation;
import noki.almagest.helper.HelperNBTTag;


/**********
 * @class TileConstellation
 *
 * @description 星座ブロックのTileEntityです。
 * @see BlockConstellation, ItemBlockConstellation, RenderTileConstellation.
 */
public class TileConstellation extends TileEntity implements IUpdatePlayerListBox {
	
	//******************************//
	// define member variables.
	//******************************//
	private int constNumber = 0;
	private int rotation = 0;
	private boolean rotationSwitch = true;
	private int[] missingStars;
	
	private static final String NBT_constNumber = "constNumber";
	private static final String NBT_constRotation = "constRotation";
	private static final String NBT_constRotationSwitch = "constRotationSwitch";
	private static final String NBT_missingStars = "missingStars";
	
	
	//******************************//
	// define member methods.
	//******************************//
	public void setConstNumber(int number) {
		
		number = MathHelper.clamp_int(number, 0, HelperConstellation.constSize);
		this.constNumber = number;
		
	}
	
	public int getConstNumber() {
		
		return this.constNumber;
		
	}
	
	public int getRotation() {
		
		return this.rotation;
		
	}
	
	public void switchRotation() {
		
		this.rotationSwitch = !this.rotationSwitch;
		
	}
	
	public void setMissingStars(int[] stars) {
		
		this.missingStars = stars;
		
	}
	
	public int[] getMissingStars() {
		
		return this.missingStars;
		
	}
	
	@Override
	public void update() {
		
		if(this.rotationSwitch == true) {
			this.rotation = (this.rotation+1)%(60*20);
		}
		
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		
		super.readFromNBT(nbt);
		HelperNBTTag helper = new HelperNBTTag(nbt);
		this.constNumber = helper.getInteger(NBT_constNumber);
		this.rotation = helper.getInteger(NBT_constRotation);
		this.rotationSwitch = helper.getBoolean(NBT_constRotationSwitch);
		this.missingStars = helper.getIntArray(NBT_missingStars);
		
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		
		super.writeToNBT(nbt);
		HelperNBTTag helper = new HelperNBTTag(nbt);
		helper.setInteger(NBT_constNumber, this.constNumber);
		helper.setInteger(NBT_constRotation, this.rotation);
		helper.setBoolean(NBT_constRotationSwitch, this.rotationSwitch);
		helper.setIntArray(NBT_missingStars, this.missingStars);
		
	}
	
	@Override
	public Packet getDescriptionPacket() {
		
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		this.writeToNBT(nbttagcompound);
		return new S35PacketUpdateTileEntity(this.getPos(), 3, nbttagcompound);
		
	}
	
	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
		
		AlmagestCore.log("on packet received.");
		this.readFromNBT(packet.getNbtCompound());
		
	}

}
