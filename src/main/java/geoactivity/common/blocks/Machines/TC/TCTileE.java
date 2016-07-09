package geoactivity.common.blocks.Machines.TC;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyContainerItem;
import cofh.api.energy.IEnergyReceiver;
import geoactivity.common.util.BaseTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TCTileE extends BaseTileEntity implements IEnergyReceiver
{
	public static int internalStorage = 10000000;

	public EnergyStorage buffer = new EnergyStorage(internalStorage, 20000);

	public TCTileE()
	{
		super(1);
	}

	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);

		buffer.readFromNBT(tag);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag)
	{
		super.writeToNBT(tag);

		buffer.writeToNBT(tag);

		return tag;
	}

	@SideOnly(Side.CLIENT)
	public int getEnergyScaled(int par1)
	{
		return buffer.getEnergyStored() / 10000 / par1;
	}

	@Override
	public void update()
	{
		if (!worldObj.isRemote)
		{
			if (buffer.getEnergyStored() > 0 && inventory[0] != null)
			{
				IEnergyContainerItem item = (IEnergyContainerItem) inventory[0].getItem();

				buffer.modifyEnergyStored(-item.receiveEnergy(inventory[0], Math.min(buffer.getEnergyStored(),
						buffer.getMaxExtract()), false));
			}
		}
	}

	@Override
	public boolean canConnectEnergy(EnumFacing facing)
	{
		return true;
	}

	@Override
	public int receiveEnergy(EnumFacing facing, int maxReceive, boolean simulate)
	{
		return buffer.receiveEnergy(maxReceive, simulate);
	}

	@Override
	public int getEnergyStored(EnumFacing facing)
	{
		return buffer.getEnergyStored();
	}

	@Override
	public int getMaxEnergyStored(EnumFacing facing)
	{
		return buffer.getMaxEnergyStored();
	}

	@Override
	public String getName()
	{
		return "ToolCharger";
	}

	@Override
	public boolean isItemValidForSlot(int var1, ItemStack var2)
	{
		return ToolSlot.isRFTool(var2);
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side)
	{
		return new int[]{0};
	}

	@Override
	public boolean canInsertItem(int index, ItemStack stack, EnumFacing direction)
	{
		if(ToolSlot.isRFTool(stack))
			return true;

		return false;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction)
	{
		return true;
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		return new TCGUI(player.inventory, this);
	}

	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		return new TCContainer(this, player.inventory);
	}
}
