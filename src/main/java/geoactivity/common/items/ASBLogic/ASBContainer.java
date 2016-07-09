package geoactivity.common.items.ASBLogic;

import geoactivity.common.GAMod;
import geoactivity.common.items.tools.Adv.Logic.FuelSlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ASBContainer extends Container
{
	static ASBInventory containerInv;
	int invSize;

	public ASBContainer(ASBInventory inv, EntityPlayer player)
	{
		containerInv = inv;
		invSize = containerInv.getSizeInventory();

		addSlotToContainer(new FuelSlot(inv, 0, 80, 34));
		addSlotToContainer(new Slot(inv, 1, 53, 58) {
			@Override
			public int getSlotStackLimit()
			{
				return 1;
			}
		});
		addSlotToContainer(new Slot(inv, 2, 107, 58) {
			@Override
			public int getSlotStackLimit()
			{
				return 1;
			}
		});

		int var3;
		for(var3 = 0;var3 < 3;++var3)
			for(int var4 = 0;var4 < 9;++var4)
				this.addSlotToContainer(new Slot(player.inventory, var4 + var3 * 9 + 9, 8 + var4 * 18, 84 + var3 * 18));

		for(var3 = 0;var3 < 9;++var3)
			this.addSlotToContainer(new Slot(player.inventory, var3, 8 + var3 * 18, 142));
	}

	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return true;
	}

	@Override
	public void onContainerClosed(EntityPlayer player)
	{
		ItemStack itemStack = player.getHeldItemMainhand();
		super.onContainerClosed(player);
		if(!itemStack.hasTagCompound())
			itemStack.setTagCompound(new NBTTagCompound());
		containerInv.searchItem(player);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int toSlot)
	{
		ItemStack tempStack = null;
		Slot invSlot = inventorySlots.get(toSlot);

		if(invSlot != null && invSlot.getHasStack())
		{
			ItemStack tempStack2 = invSlot.getStack();
			tempStack = tempStack2.copy();

			Item item = tempStack2.getItem();

			if(toSlot >= invSize && toSlot < 30)
			{
				if(item == GAMod.gemLigniteCoal || item == GAMod.gemBituminousCoal || item == GAMod.gemAnthraciteCoal)
				{
					if(!this.mergeItemStack(tempStack2, 0, 1, false))
					{
						if(!this.mergeItemStack(tempStack2, 30, 39, false))
							return null;
					}
				}
				else if(!this.mergeItemStack(tempStack2, 30, 39, false))
					return null;

			}
			else if(toSlot >= 30 && toSlot < 39)
			{
				if(!this.mergeItemStack(tempStack2, invSize, 30, false))
					return null;
			}
			else if(!this.mergeItemStack(tempStack2, invSize, 39, false))
				return null;

			if(tempStack2.stackSize == 0)
				invSlot.putStack((ItemStack) null);
			else
				invSlot.onSlotChanged();

			if(tempStack2.stackSize == tempStack.stackSize)
				return null;
			invSlot.onPickupFromSlot(player, tempStack2);
		}
		return tempStack;
	}

	@Override
	public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player)
	{
		Slot tmpSlot;
		if(slotId >= 0 && slotId < inventorySlots.size()) {
			tmpSlot = inventorySlots.get(slotId);
		}
		else {
			tmpSlot = null;
		}

		if (tmpSlot != null &&
			player.inventory.getStackInSlot(player.inventory.currentItem) == tmpSlot.getStack()) {
			return tmpSlot.getStack();
		}

		return super.slotClick(slotId, dragType, clickTypeIn, player);
	}
}
