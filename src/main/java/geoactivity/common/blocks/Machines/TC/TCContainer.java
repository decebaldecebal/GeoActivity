package geoactivity.common.blocks.Machines.TC;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TCContainer extends Container
{
	protected TCTileE tile_entity;

	private int lastEnergy = 0;

	public TCContainer(TCTileE tile, InventoryPlayer playerInv)
	{
		tile_entity = tile;

		this.addSlotToContainer(new ToolSlot(tile, 0, 80, 35));

		int var3;

		for (var3 = 0; var3 < 3; ++var3)
			for (int var4 = 0; var4 < 9; ++var4)
				this.addSlotToContainer(new Slot(playerInv, var4 + var3 * 9 + 9, 8 + var4 * 18, 84 + var3 * 18));

		for (var3 = 0; var3 < 9; ++var3)
			this.addSlotToContainer(new Slot(playerInv, var3, 8 + var3 * 18, 142));
	}

	@Override
    public void addListener(IContainerListener listener)
    {
        super.addListener(listener);
        listener.sendAllWindowProperties(this, this.tile_entity);
    }

	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();

		for (int var1 = 0; var1 < listeners.size(); ++var1)
		{
			IContainerListener var2 = listeners.get(var1);

			if (lastEnergy != tile_entity.buffer.getEnergyStored())
				var2.sendProgressBarUpdate(this, 0, tile_entity.buffer.getEnergyStored());
		}

		lastEnergy = tile_entity.buffer.getEnergyStored();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int par1, int par2)
	{
		if(par1 == 0)
			tile_entity.buffer.setEnergyStored(par2);
	}

	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return tile_entity.isUseableByPlayer(player);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int par2)
	{
		ItemStack var3 = null;
		Slot var4 = inventorySlots.get(par2);

		if ((var4 != null) && var4.getHasStack())
		{
			ItemStack var5 = var4.getStack();
			var3 = var5.copy();

			if (par2 > 0 && par2 < 28)
			{
				if (ToolSlot.isRFTool(var5))
				{
					if(!mergeItemStack(var5, 0, 1, false))
						if (!mergeItemStack(var5, 28, 37, false))
							return null;
				}
				else if(!mergeItemStack(var5, 28, 37, false))
					return null;
			}
			else if(par2 >= 28 && par2 < 37)
			{
				if(!this.mergeItemStack(var5, 1, 28, false))
					return null;
			}
			else if(!this.mergeItemStack(var5, 1, 37, false))
				return null;

			if (var5.stackSize == 0)
				var4.putStack((ItemStack) null);
			else
				var4.onSlotChanged();

			if (var5.stackSize == var3.stackSize)
				return null;

			var4.onPickupFromSlot(player, var5);
		}

		return var3;
	}
}
