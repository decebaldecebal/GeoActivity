package geoactivity.common.blocks.Machines.GG;

import geoactivity.common.GAMod;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GGContainer extends Container
{
	protected GGTileE tile_entity;
	private int lastLavaBlocks = 0;
	private byte lastHightEfficiency = 0;
	private int lastRFPerTick = 0;
	private int lastEnergy = 0;
	private int lastUpdateTicks = 0;
	private World worldObj;
	private BlockPos pos;

	public GGContainer(GGTileE tile, InventoryPlayer playerInv)
	{
		tile_entity = tile;
		int var6;
		int var7;
		worldObj = tile.getWorld();
		pos = tile.getPos();

		for(var6 = 0;var6 < 3;++var6)
			for(var7 = 0;var7 < 9;++var7)
				this.addSlotToContainer(new Slot(playerInv, var7 + var6 * 9 + 9, 8 + var7 * 18, 84 + var6 * 18));

		for(var6 = 0;var6 < 9;++var6)
			this.addSlotToContainer(new Slot(playerInv, var6, 8 + var6 * 18, 142));
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

		for(int var1 = 0;var1 < listeners.size();++var1)
		{
			IContainerListener var2 = listeners.get(var1);

			if(lastLavaBlocks != tile_entity.lavaBlocks)
				var2.sendProgressBarUpdate(this, 0, tile_entity.lavaBlocks);

			if(lastHightEfficiency != tile_entity.hightEfficiency)
				var2.sendProgressBarUpdate(this, 1, tile_entity.hightEfficiency);

			if(lastRFPerTick != tile_entity.RFPerTick)
				var2.sendProgressBarUpdate(this, 2, tile_entity.RFPerTick);

			if(lastEnergy != tile_entity.buffer.getEnergyStored())
				var2.sendProgressBarUpdate(this, 3, tile_entity.buffer.getEnergyStored());

			if(lastUpdateTicks != tile_entity.ticksTillUpdate)
				var2.sendProgressBarUpdate(this, 4, tile_entity.ticksTillUpdate);
		}

		lastLavaBlocks = tile_entity.lavaBlocks;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int par1, int par2)
	{
		if(par1 == 0)
			tile_entity.lavaBlocks = par2;
		else if(par1 == 1)
			tile_entity.hightEfficiency = (byte) par2;
		else if(par1 == 2)
			tile_entity.RFPerTick = par2;
		else if(par1 == 3)
			tile_entity.buffer.setEnergyStored(par2);
		else if(par1 == 4)
			tile_entity.ticksTillUpdate = par2;
	}

	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		if(worldObj.getBlockState(pos).getBlock() != GAMod.geothermalgenerator)
			return false;
		return player.getDistanceSq(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) <= 64D;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int par2)
	{
		ItemStack var3 = null;
		Slot var4 = inventorySlots.get(par2);

		if(var4 != null && var4.getHasStack())
		{
			ItemStack var5 = var4.getStack();
			var3 = var5.copy();

			if(par2 >= 27 && par2 < 36)
			{
				if(!this.mergeItemStack(var5, 0, 27, false))
					return null;
			}
			else if(!this.mergeItemStack(var5, 27, 36, false))
				return null;

			if(var5.stackSize == 0)
				var4.putStack((ItemStack) null);
			else
				var4.onSlotChanged();

			if(var5.stackSize == var3.stackSize)
				return null;
			var4.onPickupFromSlot(player, var5);
		}
		return var3;
	}
}
