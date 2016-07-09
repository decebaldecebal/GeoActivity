package geoactivity.common.blocks.Machines.ChM;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class ChMGUI extends GuiContainer
{
	private static final ResourceLocation guitexture = new ResourceLocation("GeoActivity:textures/gui/ChM.png");

	public ChMGUI(InventoryPlayer playerInv, ChMTileE tile)
	{
		super(new ChMContainer(tile, playerInv));
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
	{
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

		mc.renderEngine.bindTexture(guitexture);

		int x = (width - xSize) / 2;

		int y = (height - ySize) / 2;

		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
	}
}
