package geoactivity.common.blocks.Machines.CrM;

import geoactivity.common.GAMod;
import geoactivity.common.blocks.Machines.ACR.FuelSlot;
import geoactivity.common.items.ArmorPerks.EnumArmorPerks;
import geoactivity.common.items.ToolPerks.EnumToolPerks;
import geoactivity.common.util.BaseTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CrMTileE extends BaseTileEntity
{
	public int furnaceBurnTime = 0;
	public int currentItemBurnTime = 0;
	public int furnaceCookTime = 0;
	ItemStack varStack;

	public InventoryCrafting craftMatrix = new InventoryCrafting(new Container(){
		@Override
		public boolean canInteractWith(EntityPlayer player)
		{
			return false;
		}
		@Override
		public void onCraftMatrixChanged(IInventory inv)
		{
			super.onCraftMatrixChanged(inv);

			craftResult.setInventorySlotContents(0, CrMCrafting.getInstance().findMatchingRecipe(craftMatrix, worldObj));
			inventory[0] = craftResult.getStackInSlot(0);
		}

	}, 3, 3);
	public IInventory craftResult = new InventoryCraftResult();

	public CrMTileE()
	{
		super(13);
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound)
	{
		super.readFromNBT(tagCompound);

		NBTTagList tagList = tagCompound.getTagList("Inventory", Constants.NBT.TAG_COMPOUND);

		for (int i = 0; i < tagList.tagCount(); i++)
		{
			NBTTagCompound tag = tagList.getCompoundTagAt(i);
			byte slot = tag.getByte("Slot");
			if(slot > 3)
				craftMatrix.setInventorySlotContents(slot - 4, ItemStack.loadItemStackFromNBT(tag));
			else
				inventory[slot]=ItemStack.loadItemStackFromNBT(tag);
		}

		furnaceBurnTime = tagCompound.getShort("BurnTime");
		furnaceCookTime = tagCompound.getShort("CookTime");
		currentItemBurnTime = tagCompound.getShort("ItemBurnTime");

		NBTTagCompound tag = tagCompound.getCompoundTag("tempStack");
		varStack = ItemStack.loadItemStackFromNBT(tag);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound)
	{
		super.writeToNBT(tagCompound);

		tagCompound.setShort("BurnTime", (short) furnaceBurnTime);
		tagCompound.setShort("CookTime", (short) furnaceCookTime);
		tagCompound.setShort("ItemBurnTime", (short) currentItemBurnTime);

		NBTTagCompound tag1 = new NBTTagCompound();
		if(varStack!=null)
			varStack.writeToNBT(tag1);
		tagCompound.setTag("tempStack", tag1);

		NBTTagList itemList = new NBTTagList();

		for (int i = 0; i < inventory.length; i++)
		{
			ItemStack stack = inventory[i];
			if(i>3)
			{
				stack = craftMatrix.getStackInSlot(i-4);
			}
			if (stack != null)
			{
				NBTTagCompound tag = new NBTTagCompound();
				tag.setByte("Slot", (byte) i);
				stack.writeToNBT(tag);
				itemList.appendTag(tag);
			}
		}
		tagCompound.setTag("Inventory", itemList);

		return tagCompound;
	}

	@SideOnly(Side.CLIENT)
	public int getCookProgressScaled(int par1)
	{
		return furnaceCookTime * par1 / 40;
	}

	@SideOnly(Side.CLIENT)
	public int getBurnTimeRemainingScaled(int par1)
	{
		if (currentItemBurnTime == 0)
			currentItemBurnTime = 80;

		return furnaceBurnTime * par1 / currentItemBurnTime;
	}

	public boolean isBurning()
	{
		return furnaceBurnTime > 0;
	}

	@Override
	public void update()
	{
		boolean var1 = furnaceBurnTime > 0;
		boolean var2 = false;

		if (furnaceBurnTime > 0)
			--furnaceBurnTime;

		if (!worldObj.isRemote)
		{
			if(furnaceCookTime == 0 && this.canSmelt() && furnaceBurnTime >= 80)
				varStack = inventory[0].copy();

			if (furnaceBurnTime == 0 && this.canSmelt())
			{
				currentItemBurnTime = furnaceBurnTime = getItemBurnTime(inventory[1]);

				if (furnaceBurnTime > 0)
				{
					var2 = true;

					if (inventory[1] != null)
					{
						--inventory[1].stackSize;

						if (inventory[1].stackSize == 0)
							inventory[1] = inventory[1].getItem().getContainerItem(inventory[1]);
					}
				}
			}

			if (this.isBurning() && inventory[2] == null && varStack!=null)
			{
				++furnaceCookTime;
				if(furnaceCookTime==1)
					updateMatrix();
				if (furnaceCookTime == 80)
				{
					furnaceCookTime = 0;
					this.smeltItem();
					var2 = true;
				}
			}
			else
				furnaceCookTime = 0;

			if (var1 != furnaceBurnTime > 0)
				var2 = true;
		}

		if (var2)
			this.markDirty();
	}

	private void updateMatrix()
	{
		for(int i=0;i < 9;i++)
			craftMatrix.decrStackSize(i, 1);
	}

	private boolean canSmelt()
	{
		if(inventory[0] == null)
			return false;
		if (inventory[2] == null)
			return true;
		return false;
	}

	public void smeltItem()
	{
		if (inventory[2] == null)
			inventory[2] = varStack.copy();
		else if (inventory[2].isItemEqual(varStack))
			inventory[2].stackSize += varStack.stackSize;

		if(inventory[3] != null)
		{
			if((inventory[2].getItem() instanceof ItemTool || inventory[2].getItem() instanceof ItemSword)
				&& inventory[3].getItem() == GAMod.tool_perks)
			{
				if(inventory[3].getMetadata() == EnumToolPerks.SPEED.getMetadata())
				{
					NBTTagCompound tag = new NBTTagCompound();
					tag.setByte("speed", (byte)1);
					inventory[2].setTagCompound(tag);
				}
				else if(inventory[3].getMetadata() == EnumToolPerks.EFFICIENCY.getMetadata())
				{
					NBTTagCompound tag = new NBTTagCompound();
					tag.setByte("efficiency", (byte)1);
					inventory[2].setTagCompound(tag);
				}
				else if(inventory[3].getMetadata() == EnumToolPerks.DAMAGE.getMetadata())
				{
					NBTTagCompound tag = new NBTTagCompound();
					tag.setByte("damage", (byte)1);
					inventory[2].setTagCompound(tag);
				}
				else if(inventory[3].getMetadata() == EnumToolPerks.EXPERIENCE.getMetadata())
				{
					NBTTagCompound tag = new NBTTagCompound();
					tag.setBoolean("experience", true);
					inventory[2].setTagCompound(tag);
				}
				else if(inventory[3].getMetadata() == EnumToolPerks.SMELTING.getMetadata())
				{
					NBTTagCompound tag = new NBTTagCompound();
					tag.setBoolean("auto-smelt", true);
					inventory[2].setTagCompound(tag);
				}
				else if(inventory[3].getMetadata() == EnumToolPerks.FORTUNE.getMetadata())
				{
					NBTTagCompound tag = new NBTTagCompound();
					tag.setByte("fortune", (byte)1);
					inventory[2].setTagCompound(tag);
				}
				else if(inventory[3].getMetadata() == EnumToolPerks.SILKTOUCH.getMetadata())
				{
					NBTTagCompound tag = new NBTTagCompound();
					tag.setBoolean("silk-touch", true);
					inventory[2].setTagCompound(tag);
				}
				else if(inventory[3].getMetadata() == EnumToolPerks.RADIUS.getMetadata())
				{
					NBTTagCompound tag = new NBTTagCompound();
					tag.setBoolean("widerRadius", true);
					inventory[2].setTagCompound(tag);
				}
				else if(inventory[3].getMetadata() == EnumToolPerks.CAPITATOR.getMetadata())
				{
					NBTTagCompound tag = new NBTTagCompound();
					tag.setBoolean("capitator", true);
					inventory[2].setTagCompound(tag);
				}
				else if(inventory[3].getMetadata() == EnumToolPerks.NODROPS.getMetadata())
				{
					NBTTagCompound tag = new NBTTagCompound();
					tag.setBoolean("nodrops", true);
					inventory[2].setTagCompound(tag);
				}
				else if(inventory[3].getMetadata() == EnumToolPerks.BYPASSARMOR.getMetadata())
				{
					NBTTagCompound tag = new NBTTagCompound();
					tag.setBoolean("barmor", true);
					inventory[2].setTagCompound(tag);
				}

				if((inventory[3].getMetadata() >= EnumToolPerks.SPEED.getMetadata()
						&& inventory[3].getMetadata() <= EnumToolPerks.NODROPS.getMetadata())
						|| inventory[3].getMetadata() == EnumToolPerks.BYPASSARMOR.getMetadata())
					inventory[3] = null;
			}
			else if(inventory[2].getItem() instanceof ItemArmor)
			{
				if(inventory[3].getItem() == GAMod.tool_perks && inventory[3].getMetadata() == EnumToolPerks.EFFICIENCY.getMetadata())
				{
					NBTTagCompound tag = new NBTTagCompound();
					tag.setByte("efficiency", (byte)1);
					inventory[2].setTagCompound(tag);
					inventory[3] = null;
				}
				if (inventory[3].getItem() == GAMod.armor_perks)
				{
					if(inventory[3].getMetadata() == EnumArmorPerks.GENERAL.getMetadata())
					{
						NBTTagCompound tag = new NBTTagCompound();
						tag.setByte("all", (byte)1);
						inventory[2].setTagCompound(tag);
					}
					else if(inventory[3].getMetadata() == EnumArmorPerks.BLAST.getMetadata())
					{
						NBTTagCompound tag = new NBTTagCompound();
						tag.setByte("blast", (byte)1);
						inventory[2].setTagCompound(tag);
					}
					else if(inventory[3].getMetadata() == EnumArmorPerks.FIRE.getMetadata())
					{
						NBTTagCompound tag = new NBTTagCompound();
						tag.setByte("fire", (byte)1);
						inventory[2].setTagCompound(tag);
					}
					else if(inventory[3].getMetadata() == EnumArmorPerks.MAGIC.getMetadata())
					{
						NBTTagCompound tag = new NBTTagCompound();
						tag.setByte("magic", (byte)1);
						inventory[2].setTagCompound(tag);
					}
					else if(inventory[3].getMetadata() == EnumArmorPerks.PROJECTILE.getMetadata())
					{
						NBTTagCompound tag = new NBTTagCompound();
						tag.setByte("projectile", (byte)1);
						inventory[2].setTagCompound(tag);
					}
					else if(inventory[3].getMetadata() == EnumArmorPerks.NOFALL.getMetadata())
					{
						NBTTagCompound tag = new NBTTagCompound();
						tag.setBoolean("fall", true);
						inventory[2].setTagCompound(tag);
					}
					else if(inventory[3].getMetadata() == EnumArmorPerks.RESPIRATION.getMetadata())
					{
						NBTTagCompound tag = new NBTTagCompound();
						tag.setBoolean("respiration", true);
						inventory[2].setTagCompound(tag);
					}

					inventory[3] = null;
				}
			}
		}
		varStack = null;
	}

	public static int getItemBurnTime(ItemStack par0ItemStack)
	{
		if (par0ItemStack == null)
			return 0;
		else
		{
			Item var1 = par0ItemStack.getItem();

			if(var1 == Item.getItemFromBlock(Blocks.COAL_BLOCK))
				return 82;
			if (var1 == GAMod.gemLigniteCoal)
				return 164;
			if (var1 == GAMod.gemBituminousCoal)
				return 328;
			if (var1 == GAMod.gemAnthraciteCoal)
				return 490;
			return 0;
		}
	}

	public static boolean isItemFuel(ItemStack stack)
	{
		return getItemBurnTime(stack) > 0;
	}

	@Override
	public String getName()
	{
		return "CraftingMachine";
	}


	@Override
	public int[] getSlotsForFace(EnumFacing side)
	{
		return new int[]{1,2};
	}

	@Override
	public boolean canInsertItem(int index, ItemStack stack, EnumFacing direction)
	{
		if(index == 1 && FuelSlot.isFuel(stack))
			return true;
		return false;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction)
	{
		if(index == 2)
			return true;
		return false;
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		return new CrMGUI(player.inventory, this);
	}

	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		return new CrMContainer(this, player.inventory);
	}
}
