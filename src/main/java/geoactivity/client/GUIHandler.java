package geoactivity.client;

import geoactivity.common.lib.IOpenableGUI;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GUIHandler implements IGuiHandler
{
	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		IOpenableGUI openableGui = getOpenableGUI(id, player, world, x, y, z);
		return openableGui != null ? openableGui.getServerGuiElement(id, player, world, x, y, z) : null;
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		IOpenableGUI openableGui = getOpenableGUI(id, player, world, x, y, z);
		return openableGui != null ? openableGui.getClientGuiElement(id, player, world, x, y, z) : null;
	}

	private IOpenableGUI getOpenableGUI(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		BlockPos pos = new BlockPos(x, y, z);
		TileEntity tileEntity = world.getTileEntity(pos);

		ItemStack itemStack = player.getHeldItemMainhand();

		return tileEntity instanceof IOpenableGUI ? (IOpenableGUI)tileEntity
			: (itemStack != null) && (itemStack.getItem() instanceof IOpenableGUI) ?
			(IOpenableGUI) itemStack.getItem() : null;
	}
}
