package geoactivity.common.items.tools.Red.ABMLogic;

import geoactivity.common.GAMod;
import geoactivity.common.items.tools.Red.Logic.PerkSlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ABMContainer extends Container
{
	static ABMInventory containerInv;
	int invSize;

	public ABMContainer(ABMInventory inv, EntityPlayer player)
	{
		containerInv = inv;
		invSize = containerInv.getSizeInventory();

		addSlotToContainer(new PerkSlot(inv, 0, 9, 9));
		addSlotToContainer(new PerkSlot(inv, 1, 9, 34));
		addSlotToContainer(new PerkSlot(inv, 2, 9, 58));
		addSlotToContainer(new PerkSlot(inv, 3, 45, 9));
		addSlotToContainer(new PerkSlot(inv, 4, 45, 34));
		addSlotToContainer(new PerkSlot(inv, 5, 45, 58));
		addSlotToContainer(new PerkSlot(inv, 6, 115, 9));
		addSlotToContainer(new PerkSlot(inv, 7, 115, 34));
		addSlotToContainer(new PerkSlot(inv, 8, 115, 58));
		addSlotToContainer(new PerkSlot(inv, 9, 151, 9));
		addSlotToContainer(new PerkSlot(inv, 10, 151, 34));
		addSlotToContainer(new PerkSlot(inv, 11, 151, 58));

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
		containerInv.writeToNBT(itemStack.getTagCompound());
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

			if(toSlot >= invSize && toSlot < 39)
			{
				if(item == GAMod.tool_perks)
				{
					if(!this.mergeItemStack(tempStack2, 0, 12, false))
					{
						if(!this.mergeItemStack(tempStack2, 39, 48, false))
							return null;
					}
				}
				else if(!this.mergeItemStack(tempStack2, 39, 48, false))
					return null;
			}
			else if(toSlot >= 39 && toSlot < 48)
			{
				if(!this.mergeItemStack(tempStack2, invSize, 39, false))
					return null;
			}
			else if(!this.mergeItemStack(tempStack2, invSize, 48, false))
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
	public ItemStack slotClick(int slotID, int buttonPressed, ClickType click, EntityPlayer player)
	{
		Slot tmpSlot;
		if(slotID >= 0 && slotID < inventorySlots.size())
			tmpSlot = inventorySlots.get(slotID);
		else
			tmpSlot = null;

		if (tmpSlot != null &&
			player.inventory.getStackInSlot(player.inventory.currentItem) == tmpSlot.getStack()) {
			return tmpSlot.getStack();
		}

		return super.slotClick(slotID, buttonPressed, click, player);
	}
}
