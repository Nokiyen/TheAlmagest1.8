package noki.almagest.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import noki.almagest.ModInfo;
import noki.almagest.helper.HelperConstellation;
import noki.almagest.helper.HelperConstellation.LineData;
import noki.almagest.helper.HelperConstellation.StarData;

public class GuiAlmagest extends GuiScreen {
	
	private static final String domain = ModInfo.ID.toLowerCase();
	private static final ResourceLocation texture = new ResourceLocation(domain, "textures/gui/almagest.png");
	
	private static int pageWidth = 140;
	private static int pageHeight = 198;
	
	private ItemStack stack;
	private HashMap<Integer, PageContent> pages = new HashMap<Integer, PageContent>();
	private int currentPage = 0;
	
	private GuiButton nextButton;
	private GuiButton prevButton;
	
	
	
	public GuiAlmagest(ItemStack stack) {
		
		this.stack = stack;
		int count = 1;
		for(int i = 1; i <= 88; i++) {
			int constFlag = HelperConstellation.getConstFlagFromAlmagest(this.stack, i);
			if(constFlag == 1 || constFlag == 2) {
				this.pages.put(count, new PageContent(i, constFlag));
				count++;
			}
		}
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		
		this.buttonList.clear();
//		this.nextButton = new PagingButton(0, (this.width-pageWidth)/2 + 113, 2 + 174, false);
//		this.prevButton = new PagingButton(1, (this.width-pageWidth)/2 + 9, 2 + 174, true);
		this.nextButton = new PagingButton(0, (this.width-pageWidth)/2 + 114, 2 + 11, false);
		this.prevButton = new PagingButton(1, (this.width-pageWidth)/2 + 5, 2 + 11, true);
		this.buttonList.add(this.nextButton);
		this.buttonList.add(this.prevButton);
		
		this.updateButtons();
		
	}
	
	public void updateButtons() {
		
		this.nextButton.visible = this.currentPage == this.pages.size() ? false : true;
		this.prevButton.visible = this.currentPage == 0 ? false: true;
		
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		
		if(button.id == this.nextButton.id) {
			this.currentPage++;
		}
		if(button.id == this.prevButton.id) {
			this.currentPage--;
		}
		this.updateButtons();
		
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		
		this.mc.getTextureManager().bindTexture(texture);
		this.drawTexturedModalRect((this.width-pageWidth)/2, 2, 0, 0, pageWidth, pageHeight);
		
		super.drawScreen(mouseX, mouseY, partialTicks);
		
		if(this.currentPage == 0) {
			this.fontRendererObj.drawString(this.pages.size()+"/88", (this.width-pageWidth)/2+50, 100, 0);
		}
		else {
			PageContent page = this.pages.get(this.currentPage);
			this.drawTexturedModalRect((this.width-pageWidth)/2+18, 30, 140, 0, 95, 70);
			page.renderConstellation((this.width-pageWidth)/2+18+22+50, 30+10+50);
			
			String name = I18n.format("gui.almagest_book."+page.constNumber+".name", new Object[0]);
			int stringWidth = this.fontRendererObj.getStringWidth(name);
			this.fontRendererObj.drawString(name, (this.width-pageWidth)/2 + 66 - stringWidth/2, 15, 0);

			for(int i = 1; i <= 7; i++) {
				String line = I18n.format("gui.almagest_book."+page.constNumber+".line"+i, new Object[0]);
				this.fontRendererObj.drawString(line, (this.width-pageWidth)/2 + 7, 105 + (i-1)*12, 0);
			}
		}
		
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		
		return false;
		
	}
	
	private class PageContent {
		
		public int constNumber;
		public int flag;
		public ArrayList<StarData> stars;
		public List<LineData> lines;
		
		private static final double size = 48;
		private static final float scale = 0.00390625F;
		private double weight;
		private double adjustLong;
		private double adjustLat;
		private double relativeAddLong;
		private double relativeAddLat;
		
		public PageContent(int constNumber, int flag) {
			
			this.constNumber = constNumber;
			this.flag = flag;
			this.stars = HelperConstellation.getStars(this.constNumber);
			this.lines = HelperConstellation.getLines(this.constNumber);
			
			double minLong = Integer.MAX_VALUE;
			double minLat = Integer.MAX_VALUE;
			double maxLong = Integer.MIN_VALUE;
			double maxLat = Integer.MIN_VALUE;
			for(StarData each: stars) {
				minLong = Math.min(minLong, each.getCalculatedLong());
				minLat = Math.min(minLat, each.getCalculatedLat());
				maxLong = Math.max(maxLong, each.getCalculatedLong());
				maxLat = Math.max(maxLat, each.getCalculatedLat());
			}
			double defLong = Math.abs(maxLong - minLong);
			double defLat = Math.abs(maxLat - minLat);
			this.weight = size / Math.max(defLong, defLat);
			this.adjustLong = minLong * -1;
			this.adjustLat = minLat * -1;
			this.relativeAddLong = (size-defLong*this.weight)/2;
			this.relativeAddLat = (size-defLat*this.weight)/2;
			
		}
		
		public void renderConstellation(double startX, double startY) {
			
			for(LineData each: this.lines) {
				double relativeX1 = startX - ((each.star1.getCalculatedLong() + this.adjustLong) * this.weight + this.relativeAddLong);
				double relativeY1 = startY - ((each.star1.getCalculatedLat() + this.adjustLat) * this.weight + this.relativeAddLat);
				double relativeX2 = startX - ((each.star2.getCalculatedLong() + this.adjustLong) * this.weight + this.relativeAddLong);
				double relativeY2 = startY - ((each.star2.getCalculatedLat() + this.adjustLat) * this.weight + this.relativeAddLat);
				this.drawLine(relativeX1, relativeY1, relativeX2, relativeY2);
			}

			for(StarData each: this.stars) {
				double relativeX = startX - ((each.getCalculatedLong() + this.adjustLong) * this.weight + this.relativeAddLong);
				double relativeY = startY - ((each.getCalculatedLat() + this.adjustLat) * this.weight + this.relativeAddLat);

				double scale = 0.4 - Math.min(each.magnitude*0.05, 0.25);
				double adjust = 16*scale/2;
				this.drawRect(relativeX-adjust, relativeY-adjust,
						140, 81+(each.spectrum.getMetadata()-1)*16, 16, 16, scale);
			}
			
		}
		
		private void drawRect(double x, double y, int textureX, int textureY, int width, int height, double scale) {
		
			float f = 0.00390625F;
			float f1 = 0.00390625F;
			Tessellator tessellator = Tessellator.getInstance();
			WorldRenderer worldrenderer = tessellator.getWorldRenderer();
			worldrenderer.startDrawingQuads();
			worldrenderer.addVertexWithUV(x + 0*scale, y + height*scale,
					GuiAlmagest.this.zLevel, (double)((float)(textureX + 0) * f), (double)((float)(textureY + height) * f1));
			worldrenderer.addVertexWithUV(x + width*scale, y + height*scale,
					GuiAlmagest.this.zLevel, (double)((float)(textureX + width) * f), (double)((float)(textureY + height) * f1));
			worldrenderer.addVertexWithUV(x + width*scale, y + 0*scale,
					GuiAlmagest.this.zLevel, (double)((float)(textureX + width) * f), (double)((float)(textureY + 0) * f1));
			worldrenderer.addVertexWithUV(x + 0*scale, y + 0*scale,
					GuiAlmagest.this.zLevel, (double)((float)(textureX + 0) * f), (double)((float)(textureY + 0) * f1));
			tessellator.draw();
			
		}
		
		private void drawLine(double relativeX1, double relativeY1, double relativeX2, double relativeY2) {
		
			WorldRenderer renderer= Tessellator.getInstance().getWorldRenderer();
/*			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);	// alpha blend.
			GL11.glEnable(GL11.GL_BLEND);*/
			GlStateManager.color(1F, 1F, 1F);
			
			renderer.startDrawing(GL11.GL_LINES);
			GL11.glLineWidth(2);
			renderer.addVertex(relativeX1, relativeY1, GuiAlmagest.this.zLevel);
			renderer.addVertex(relativeX2, relativeY2, GuiAlmagest.this.zLevel);
			Tessellator.getInstance().draw();
			
//			GL11.glDisable(GL11.GL_BLEND);
			
		}
		
	}
	
	private class PagingButton extends GuiButton {
		
		private final boolean isLeft;
		
		public PagingButton(int buttonID, int x, int y, boolean isLeft) {
			
			super(buttonID, x, y, 10, 10, "test");
			this.isLeft = isLeft;
			
		}
		
		public void drawButton(Minecraft mc, int mouseX, int mouseY) {
			
			if(this.visible) {
				boolean mouseOver = false;
				if(mouseX >= this.xPosition && mouseY >= this.yPosition
						&& mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height) {
					mouseOver = true;
				}
				
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				mc.getTextureManager().bindTexture(GuiAlmagest.texture);
				int x = 140;
				int y = 70;
				
				if(mouseOver) {
					x += 22;
				}
				if(isLeft) {
					x += 10;
				}
				
				this.drawTexturedModalRect(this.xPosition, this.yPosition, x, y, this.width, this.height);
			}
		}
		
	}
	
}
