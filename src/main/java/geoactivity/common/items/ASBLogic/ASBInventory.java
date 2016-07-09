package geoactivity.common.items.ASBLogic;

import geoactivity.common.GAMod;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.util.Constants;

public class ASBInventory implements IInventory
{
	protected ItemStack current;
	protected ItemStack[] myInventory;

	public ASBInventory(ItemStack stack, EntityPlayer player)
	{
		current = stack;
		if(!stack.hasTagCompound())
		{
			NBTTagCompound tag = new NBTTagCompound();
			tag.setBoolean("destroyed", false);
			tag.setBoolean("canCraft", false);
			tag.setByte("itemdamage", (byte) 1);
			tag.setInteger("stackSize", 0);
			tag.setTag("resultStack", new ItemStack(Blocks.AIR).writeToNBT(new NBTTagCompound()));
			stack.setTagCompound(tag);
		}
		myInventory = new ItemStack[3];

		readFromNBT(stack.getTagCompound());
	}

	public void setCharge()
	{
		int fuel = 0;
		if(myInventory[0] != null)
		{
			Item item = myInventory[0].getItem();
			if(item == GAMod.gemLigniteCoal)
				fuel = 150;
			else if(item == GAMod.gemBituminousCoal)
				fuel = 300;
			else if(item == GAMod.gemAnthraciteCoal)
				fuel = 450;
		}
		while(fuel != 0 && myInventory[0] != null)
		{
			int damage = current.getItemDamage();
			if(damage - fuel > 0)
			{
				current.setItemDamage(damage - fuel);
				decrStackSize(0, 1);
			}
			else
				fuel = 0;
		}
		if(current.getItemDamage() <= 998)
		{
			NBTTagCompound tag = current.getTagCompound();
			tag.setBoolean("destroyed", false);
			current.setTagCompound(tag);
		}

		writeToNBT(current.getTagCompound());
	}

	public void writeToNBT(NBTTagCompound myCompound)
	{
		NBTTagList nbttaglist = new NBTTagList();

		for(int i = 0;i < myInventory.length;++i)
		{
			if(myInventory[i] != null)
			{
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte) i);
				myInventory[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}

		myCompound.setTag("Items", nbttaglist);
	}

	public void readFromNBT(NBTTagCompound myCompound)
	{
		NBTTagList nbttaglist = myCompound.getTagList("Items", Constants.NBT.TAG_COMPOUND);

		myInventory = new ItemStack[this.getSizeInventory()];

		for(int i = 0;i < nbttaglist.tagCount();++i)
		{
			NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
			byte b0 = nbttagcompound1.getByte("Slot");

			if(b0 >= 0 && b0 < myInventory.length)
				myInventory[b0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
		}
	}

	public void searchItem(EntityPlayer player)
	{
		current = player.getHeldItemMainhand();
		if(myInventory[1] != null && myInventory[2] != null)
		{
			ItemStack result = FurnaceRecipes.instance().getSmeltingResult(myInventory[1]);
			ItemStack temp = myInventory[2];

			if(result != null)
			{
				if(result.getItem() == temp.getItem() && result.getMetadata() == temp.getMetadata())
				{
					searchStack(1, result.stackSize, player);

					NBTTagCompound tag = current.getTagCompound();
					tag.setByte("itemdamage", (byte) 2);
					current.setTagCompound(tag);
				}
				else
				{
					ItemStack result2 = searchCrafting(result, player);
					if(result2 != null)
					{
						if(result2.getItem() == temp.getItem() && result2.getMetadata() == temp.getMetadata())
						{
							searchStack(4 / result.stackSize, result2.stackSize, player);

							NBTTagCompound tag = current.getTagCompound();
							tag.setByte("itemdamage", (byte) 3);
							current.setTagCompound(tag);
						}
					}
				}
			}
			else
			{
				ItemStack result2 = searchCrafting(myInventory[1], player);
				if(result2 != null)
				{
					if(result2.getItem() == temp.getItem() && result2.getMetadata() == temp.getMetadata())
					{
						searchStack(4, result2.stackSize, player);

						NBTTagCompound tag = current.getTagCompound();
						tag.setByte("itemdamage", (byte) 1);
						current.setTagCompound(tag);
					}
					else
					{
						ItemStack result3 = FurnaceRecipes.instance().getSmeltingResult(result2);
						if(result3 != null)
						{
							if(result3.getItem() == temp.getItem() && result3.getMetadata() == temp.getMetadata())
							{
								searchStack(4 / result2.stackSize, result3.stackSize, player);

								NBTTagCompound tag = current.getTagCompound();
								tag.setByte("itemdamage", (byte) 3);
								current.setTagCompound(tag);
							}
						}
					}
				}
			}
		}

		if(myInventory[1] == null || myInventory[2] == null)
		{
			NBTTagCompound tag = current.getTagCompound();
			tag.setInteger("stackSize", 0);
			tag.setBoolean("canCraft", false);
			current.setTagCompound(tag);
		}

		writeToNBT(current.getTagCompound());
	}

	private void searchStack(int size1, int size2, EntityPlayer player)
	{
		int i = 0;
		int nr = current.getTagCompound().getInteger("stackSize");
		while(i < 36 && nr < 64)
		{
			if(player.inventory.mainInventory[i] != null)
			{
				if(player.inventory.mainInventory[i].getItem() == myInventory[1].getItem()
					&& player.inventory.mainInventory[i].getMetadata() == myInventory[1].getMetadata())
				{
					while(player.inventory.mainInventory[i] != null && player.inventory.mainInventory[i].stackSize >= size1
						&& nr + size2 <= 64)
					{
						nr += size2;
						player.inventory.mainInventory[i].stackSize -= size1;
						if(player.inventory.mainInventory[i].stackSize == 0)
							player.inventory.mainInventory[i] = null;
					}
				}
			}
			i++;
		}
		NBTTagCompound tag = current.getTagCompound();
		tag.setInteger("stackSize", nr);
		if(nr > 0)
		{
			tag.setBoolean("canCraft", true);

			NBTTagCompound nbttag = new NBTTagCompound();
			myInventory[2].writeToNBT(nbttag);
			tag.setTag("resultStack", nbttag);
		}
		else
			tag.setBoolean("canCraft", false);
		current.setTagCompound(tag);
	}

	private ItemStack searchCrafting(ItemStack stack, EntityPlayer player)
	{
		ContainerWorkbench container = new ContainerWorkbench(player.inventory, player.worldObj, new BlockPos(0, 0, 0));

		container.craftMatrix.setInventorySlotContents(0, stack);
		container.craftMatrix.setInventorySlotContents(1, stack);
		container.craftMatrix.setInventorySlotContents(3, stack);
		container.craftMatrix.setInventorySlotContents(4, stack);

		return container.craftResult.getStackInSlot(0);
	}

	@Override
	public int getSizeInventory()
	{
		return 3;
	}

	@Override
	public ItemStack getStackInSlot(int i)
	{
		return myInventory[i];
	}

	@Override
	public ItemStack decrStackSize(int slot, int number)
	{
		if(myInventory[slot] == null)
			return null;
		ItemStack returnStack;
		if(myInventory[slot].stackSize > number)
			returnStack = myInventory[slot].splitStack(number);
		else
		{
			returnStack = myInventory[slot];
			myInventory[slot] = null;
		}
		onInventoryChanged();
		return returnStack;
	}

	@Override
	public ItemStack removeStackFromSlot(int slot)
	{
		if(myInventory[slot] != null)
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
		myInventory[slot] = stack;
		if(stack != null && stack.stackSize > this.getInventoryStackLimit())
			stack.stackSize = this.getInventoryStackLimit();
		onInventoryChanged();
	}

	public void onInventoryChanged()
	{
		ItemStack tempStack = getStackInSlot(0);
		if(tempStack != null && tempStack.stackSize == 0)
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
	public String getName()
	{
		return "AutoStoneBuilder";
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
	public void openInventory(EntityPlayer player)
	{}

	@Override
	public void closeInventory(EntityPlayer player)
	{}

	@Override
	public int getField(int id)
	{
		return 0;
	}

	@Override
	public void setField(int id, int value)
	{}

	@Override
	public int getFieldCount()
	{
		return 0;
	}

	@Override
	public void clear()
	{}

	@Override
	public void markDirty()
	{}
}
