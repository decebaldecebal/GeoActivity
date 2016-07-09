package geoactivity.common.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.util.Constants;

public abstract class BaseInventory implements IInventory
{
	protected ItemStack current;
	protected ItemStack[] inventory;

	public BaseInventory(ItemStack stack, EntityPlayer player, int size)
	{
		current = stack;

		if (!stack.hasTagCompound())
		{
			NBTTagCompound tag = new NBTTagCompound();
			tag.setBoolean("destroyed", false);
			tag.setTag("Items", new NBTTagList());
			stack.setTagCompound(tag);
		}

		inventory = new ItemStack[size];

		readFromNBT(stack.getTagCompound());
	}

	public void readFromNBT(NBTTagCompound myCompound)
	{
		NBTTagList nbttaglist = myCompound.getTagList("Items", Constants.NBT.TAG_COMPOUND);

		inventory  = new ItemStack[this.getSizeInventory()];

		for (int i = 0; i < nbttaglist.tagCount(); ++i)
		{
			NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
			byte b0 = nbttagcompound1.getByte("Slot");

			if (b0 >= 0 && b0 < inventory.length)
				inventory[b0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
		}
	}

	public void writeToNBT(NBTTagCompound myCompound)
	{
		NBTTagList nbttaglist = new NBTTagList();

		for (int i = 0; i < inventory.length; ++i)
		{
			if (inventory[i] != null)
			{
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte) i);
				inventory[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}
		myCompound.setTag("Items", nbttaglist);
	}

	@Override
	public String getName()
	{
		return "GeoActivity Inventory";
	}

	//Do not Override past this point
	@Override
	public int getSizeInventory()
	{
		return inventory.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot)
	{
		return inventory[slot];
	}

	@Override
	public ItemStack decrStackSize(int slot, int number)
	{
		if (inventory[slot] == null)
			return null;
		ItemStack returnStack;
		if (inventory[slot].stackSize > number)
			returnStack = inventory[slot].splitStack(number);
		else
		{
			returnStack = inventory[slot];
			inventory[slot] = null;
		}
		onInventoryChanged();
		return returnStack;
	}

	@Override
	public ItemStack removeStackFromSlot(int slot)
	{
		if (inventory[slot] != null)
		{
			ItemStack returnStack = getStackInSlot(slot);
			setInventorySlotContents(slot, null);
			return returnStack;
		}
		else
			return null;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack)
	{
		inventory[slot] = stack;
		if (stack != null && stack.stackSize > this.getInventoryStackLimit())
			stack.stackSize = this.getInventoryStackLimit();
		onInventoryChanged();
	}

	public void onInventoryChanged()
	{
		ItemStack tempStack = getStackInSlot(0);
		if (tempStack != null && tempStack.stackSize == 0)
			setInventorySlotContents(0, null);
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		return true;
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack stack)
	{
		return false;
	}

	@Override
	public boolean hasCustomName()
	{
		return true;
	}

	@Override
	public ITextComponent getDisplayName()
	{
		return null;
	}

	@Override
	public void markDirty()
	{
	}

	@Override
	public void openInventory(EntityPlayer player) {}

	@Override
	public void closeInventory(EntityPlayer player) {}

	@Override
	public int getField(int id)
	{
		return 0;
	}

	@Override
	public void setField(int id, int value) {}

	@Override
	public int getFieldCount()
	{
		return 0;
	}

	@Override
	public void clear() {}
}
