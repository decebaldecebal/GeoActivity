package geoactivity.common.blocks.Machines.CrM;

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

public class CrMContainer extends Container
{
	protected CrMTileE tile_entity;
	private int lastCookTime = 0;
	private int lastBurnTime = 0;
	private int lastItemBurnTime = 0;
	private World worldObj;
	private BlockPos pos;

	public CrMContainer(CrMTileE tile, InventoryPlayer playerInv)
	{
		tile_entity = tile;
		int var6;
		int var7;
		worldObj = tile.getWorld();
		pos = tile.getPos();

		addSlotToContainer(new CraftSlot(playerInv.player, tile_entity.craftMatrix, tile_entity.craftResult, 0, 42, 62));
		addSlotToContainer(new FuelSlot(tile_entity, 1, 89, 62));
		addSlotToContainer(new ResultSlot(tile_entity, 2, 150, 42));
		addSlotToContainer(new PerkSlot(tile_entity, 3, 89, 24));


		for (var6 = 0; var6 < 3; ++var6)
			for (var7 = 0; var7 < 3; ++var7)
				this.addSlotToContainer(new Slot(tile_entity.craftMatrix, var7 + var6 * 3, 6 + var7 * 18, 6 + var6 * 18));

		for (var6 = 0; var6 < 3; ++var6)
			for (var7 = 0; var7 < 9; ++var7)
				this.addSlotToContainer(new Slot(playerInv, var7 + var6 * 9 + 9, 8 + var7 * 18, 84 + var6 * 18));

		for (var6 = 0; var6 < 9; ++var6)
			this.addSlotToContainer(new Slot(playerInv, var6, 8 + var6 * 18, 142));
		this.onCraftMatrixChanged(tile_entity.craftMatrix);
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

			if (lastCookTime != tile_entity.furnaceCookTime)
				var2.sendProgressBarUpdate(this, 0, tile_entity.furnaceCookTime);

			if (lastBurnTime != tile_entity.furnaceBurnTime)
				var2.sendProgressBarUpdate(this, 1, tile_entity.furnaceBurnTime);

			if (lastItemBurnTime != tile_entity.currentItemBurnTime)
				var2.sendProgressBarUpdate(this, 2, tile_entity.currentItemBurnTime);
		}

		lastCookTime = tile_entity.furnaceCookTime;
		lastBurnTime = tile_entity.furnaceBurnTime;
		lastItemBurnTime = tile_entity.currentItemBurnTime;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int par1, int par2)
	{
		if (par1 == 0)
			tile_entity.furnaceCookTime = par2;

		if (par1 == 1)
			tile_entity.furnaceBurnTime = par2;

		if (par1 == 2)
			tile_entity.currentItemBurnTime = par2;
	}

	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		if (worldObj.getBlockState(pos).getBlock() != GAMod.craftingmachine)
			return false;
		return player.getDistanceSq(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) <= 64D;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int par2)
	{
		ItemStack var3 = null;
		Slot var4 = inventorySlots.get(par2);

		if (var4 != null && var4.getHasStack())
		{
			ItemStack var5 = var4.getStack();
			var3 = var5.copy();

			if (par2 >= 13 && par2 < 40)
			{
				if (var5.getItem() == GAMod.tool_perks || var5.getItem() == GAMod.armor_perks)
				{
					if (!this.mergeItemStack(var5, 3, 4, false))
					{
						if(!this.mergeItemStack(var5, 40, 49, false))
							return null;
					}
				}
				else if(FuelSlot.isFuel(var5))
				{
					if(!this.mergeItemStack(var5, 1, 2, false))
					{
						if(!this.mergeItemStack(var5, 40, 49, false))
							return null;
					}
				}
				else if (!this.mergeItemStack(var5, 40, 49, false))
					return null;
			}
			else if (par2 >= 40 && par2 < 49)
			{
				if (!this.mergeItemStack(var5, 13, 40, false))
					return null;
			}
			else if (!this.mergeItemStack(var5, 13, 49, false))
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
