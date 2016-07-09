package geoactivity.common.blocks.Machines.ACR;

import geoactivity.common.GAMod;
import geoactivity.common.blocks.Machines.CR.CRRecipes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ACRContainer extends Container
{
	protected ACRTileE tile_entity;
	private int lastCookTime = 0;
	private int lastBurnTime = 0;
	private int lastItemBurnTime = 0;
	private byte lastperkEff = 0, lastperkSpeed = 0;

	public ACRContainer(ACRTileE tile, InventoryPlayer playerInv)
	{
		tile_entity = tile;

		this.addSlotToContainer(new FuelSlot(tile, 0, 41, 33));
		this.addSlotToContainer(new Slot(tile, 1, 80, 15));
		this.addSlotToContainer(new Slot(tile, 2, 80, 50));
		this.addSlotToContainer(new ACRSlot(playerInv.player, tile, 3, 117, 33));
		this.addSlotToContainer(new ACRSlot(playerInv.player, tile, 4, 143, 33));
		this.addSlotToContainer(new PerkSlot(tile, 5, 8, 8));
		this.addSlotToContainer(new PerkSlot(tile, 6, 8, 28));

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

			if (lastCookTime != tile_entity.furnaceCookTime)
				var2.sendProgressBarUpdate(this, 0, tile_entity.furnaceCookTime);

			if (lastBurnTime != tile_entity.furnaceBurnTime)
				var2.sendProgressBarUpdate(this, 1, tile_entity.furnaceBurnTime);

			if (lastItemBurnTime != tile_entity.currentItemBurnTime)
				var2.sendProgressBarUpdate(this, 2, tile_entity.currentItemBurnTime);

			if (lastperkEff != tile_entity.currentItemBurnTime)
				var2.sendProgressBarUpdate(this, 3, tile_entity.perkEff);

			if (lastperkSpeed != tile_entity.currentItemBurnTime)
				var2.sendProgressBarUpdate(this, 4, tile_entity.perkSpeed);
		}

		lastCookTime = tile_entity.furnaceCookTime;
		lastBurnTime = tile_entity.furnaceBurnTime;
		lastItemBurnTime = tile_entity.currentItemBurnTime;
		lastperkEff = tile_entity.perkEff;
		lastperkSpeed =  tile_entity.perkSpeed;
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

		if (par1 == 3)
			tile_entity.perkEff = (byte)par2;

		if (par1 == 4)
			tile_entity.perkSpeed = (byte)par2;
	}

	@Override
	public boolean canInteractWith(EntityPlayer par1EntityPlayer)
	{
		return tile_entity.isUseableByPlayer(par1EntityPlayer);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer myPlayer, int toSlot)
	 {
		ItemStack tempStack = null;
		Slot invSlot = inventorySlots.get(toSlot);

		if (invSlot != null && invSlot.getHasStack())
		{
			ItemStack tempStack2 = invSlot.getStack();
			tempStack = tempStack2.copy();

			Item item = tempStack2.getItem();

			if(toSlot >= 7 && toSlot < 34)
			{
				if (item == GAMod.gemLigniteCoal || item == GAMod.gemBituminousCoal || item == GAMod.gemAnthraciteCoal
					 || item == Items.COAL)
				{
					if (!this.mergeItemStack(tempStack2, 0, 1, false))
					{
						if (CRRecipes.getInstance().getSmeltingResult(tempStack2) != null)
						{
							if(!this.mergeItemStack(tempStack2, 1, 3, false))
								if(!this.mergeItemStack(tempStack2, 34, 43, false))
									return null;
						}
						else if(!this.mergeItemStack(tempStack2, 34, 43, false))
							return null;
					}
				}
				else if(item == GAMod.machine_perks)
				{
					if (!this.mergeItemStack(tempStack2, 5, 7, false))
					{
						if(!this.mergeItemStack(tempStack2, 34, 43, false))
							return null;
					}
				}
				else if(!this.mergeItemStack(tempStack2, 1, 3, false))
				{
					if(!this.mergeItemStack(tempStack2, 34, 43, false))
						return null;
				}
			}
			else if(toSlot >= 34 && toSlot < 43)
			{
				if(!this.mergeItemStack(tempStack2, 7, 34, false))
					return null;
			}
			else if(!this.mergeItemStack(tempStack2, 7, 43, false))
				return null;

			if (tempStack2.stackSize == 0)
				invSlot.putStack((ItemStack) null);
			else
				invSlot.onSlotChanged();

			if (tempStack2.stackSize == tempStack.stackSize)
				return null;
			invSlot.onPickupFromSlot(myPlayer, tempStack2);
		}
		return tempStack;
	}
}
