package noki.almagest.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import noki.almagest.ModInfo;


/**********
 * @class GuiContainerBookrest
 *
 * @description
 */
public class GuiContainerBookrest extends GuiContainer {
	
	//******************************//
	// define member variables.
	//******************************//
	private static final ResourceLocation texture = new ResourceLocation(ModInfo.ID.toLowerCase(), "textures/gui/bookrest.png");
	
	
	//******************************//
	// define member methods.
	//******************************//
	public GuiContainerBookrest(EntityPlayer player, World world, int x, int y, int z) {
		
		super(new ContainerBookrest(player, world, x, y, z));
		this.xSize = 176;
		this.ySize = 140;
		
	}
	
	
	/*GUIの文字等の描画処理*/
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseZ) {
		
		this.fontRendererObj.drawString(I18n.format("container.bookrest", new Object[0]), 27, 6, 4210752);
		
	}
	
	/*GUIの背景の描画処理*/
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTick, int mouseX, int mouseZ) {
		
		this.mc.renderEngine.bindTexture(texture);
		this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, xSize, ySize);
		
	}
	
	/*GUIが開いている時にゲームの処理を止めるかどうか。*/
	@Override
	public boolean doesGuiPauseGame() {
		
		return false;
		
	}
	
}
