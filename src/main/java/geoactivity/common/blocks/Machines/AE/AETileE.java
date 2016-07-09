package geoactivity.common.blocks.Machines.AE;

import geoactivity.common.GAMod;
import geoactivity.common.blocks.HardenedBrick;
import geoactivity.common.blocks.HardenedBrick.EnumHardenedBrick;
import geoactivity.common.blocks.Machines.AtomExtractor;
import geoactivity.common.items.ElementContainer.EnumElements;
import geoactivity.common.util.BaseTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class AETileE extends BaseTileEntity
{
	private ItemStack tempStack = null;

	public int currentItemBurnTime = 0;
	public int furnaceCookTime = 0;

	private int resultNumber = 0;
	public byte upgrades = 0;

	public AETileE()
	{
		super(5);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbtTag)
	{
		super.readFromNBT(nbtTag);

		NBTTagCompound tag = nbtTag.getCompoundTag("TempStack");
		if(tag.getBoolean("exists"))
			tempStack = ItemStack.loadItemStackFromNBT(tag);

		furnaceCookTime = nbtTag.getShort("CookTime");
		currentItemBurnTime = nbtTag.getShort("ItemBurnTime");
		upgrades = nbtTag.getByte("upgrades");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound)
	{
		super.writeToNBT(tagCompound);
		tagCompound.setShort("CookTime", (short) furnaceCookTime);
		tagCompound.setShort("ItemBurnTime", (short) currentItemBurnTime);
		tagCompound.setByte("upgrades", upgrades);

		NBTTagCompound nbtTempStack = new NBTTagCompound();

		nbtTempStack.setBoolean("exists", false);

		if(tempStack != null)
		{
			nbtTempStack.setBoolean("exists", true);
			tempStack.writeToNBT(nbtTempStack);
		}

		tagCompound.setTag("TempStack", nbtTempStack);

		return tagCompound;
	}

	@SideOnly(Side.CLIENT)
	public int getCookProgressScaled(int par1)
	{
		if(currentItemBurnTime > 0)
			return furnaceCookTime * par1 / currentItemBurnTime;
		return 0;
	}

	@Override
	public void update()
	{
		boolean var1 = furnaceCookTime > 0;
		boolean var2 = false;

		if(!worldObj.isRemote)
		{
			if(furnaceCookTime == 0 && this.canSmelt())
			{
				currentItemBurnTime = getItemBurnTime(inventory[0]);
				resultNumber = getResultNumber(inventory[0]);

				if(currentItemBurnTime > 0)
				{
					var2 = true;

					if(inventory[0] != null)
					{
						tempStack = inventory[0].copy();
						tempStack.stackSize = 1;

						--inventory[0].stackSize;
						inventory[1].stackSize -= resultNumber;

						if(inventory[0].stackSize == 0)
						{
							inventory[0] = inventory[0].getItem().getContainerItem(inventory[0]);
						}

						if(inventory[1].stackSize == 0)
							inventory[1] = null;
					}
				}
			}

			if(tempStack != null)
			{
				++furnaceCookTime;

				if(furnaceCookTime >= currentItemBurnTime)
				{
					furnaceCookTime = 0;
					currentItemBurnTime = 0;
					this.smeltItem();
					tempStack = null;
					var2 = true;
				}
			}
			else
			{
				furnaceCookTime = 0;
				currentItemBurnTime = 0;
			}

			if(var1 != furnaceCookTime > 0)
			{
				var2 = true;
				AtomExtractor.setState(furnaceCookTime > 0, worldObj, this.pos);
			}
		}

		if(var2)
			this.markDirty();
	}

	private boolean canSmelt()
	{
		if(inventory[0] == null)
			return false;
		else
		{
			ItemStack[] result = AERecipes.getInstance().getSmeltingResult(inventory[0]);
			if(result == null)
				return false;
			if(result[0] != null && inventory[2] != null)
				if(!inventory[2].isItemEqual(result[0]))
					return false;
			if(result[1] != null && inventory[3] != null)
				if(!inventory[3].isItemEqual(result[1]))
					return false;
			if(result[2] != null && inventory[4] != null)
				if(!inventory[4].isItemEqual(result[2]))
					return false;

			if(inventory[1] != null)
				if(inventory[1].stackSize >= AERecipes.getInstance().getNumber(inventory[0]))
				{
					int result1 = 0, result2 = 0, result3 = 0;

					if(result[0] != null)
					{
						result1 = result[0].stackSize;
						if(inventory[2] != null)
							result1 = inventory[2].stackSize + result[0].stackSize;
						if(result1 > getInventoryStackLimit() || result1 > result[0].getMaxStackSize())
							return false;
					}
					if(result[1] != null)
					{
						result2 = result[1].stackSize;
						if(inventory[3] != null)
							result2 = inventory[3].stackSize + result[1].stackSize;
						if(result2 > getInventoryStackLimit() || result2 > result[1].getMaxStackSize())
							return false;
					}
					if(result[2] != null)
					{
						result3 = result[2].stackSize;
						if(inventory[4] != null)
							result3 = inventory[4].stackSize + result[2].stackSize;
						if(result3 > getInventoryStackLimit() || result3 > result[2].getMaxStackSize())
							return false;
					}
					return true;
				}

			return false;
		}
	}

	public void smeltItem()
	{
		ItemStack[] result = AERecipes.getInstance().getSmeltingResult(tempStack);

		if(result[0] != null)
			if(inventory[2] == null)
				inventory[2] = result[0].copy();
			else if(inventory[2].isItemEqual(result[0]))
				inventory[2].stackSize += result[0].stackSize;
		if(result[1] != null)
			if(inventory[3] == null)
				inventory[3] = result[1].copy();
			else if(inventory[3].isItemEqual(result[1]))
				inventory[3].stackSize += result[1].stackSize;
		if(result[2] != null)
			if(inventory[4] == null)
				inventory[4] = result[2].copy();
			else if(inventory[4].isItemEqual(result[2]))
				inventory[4].stackSize += result[2].stackSize;

		tempStack = null;
	}

	public int getItemBurnTime(ItemStack item)
	{
		if(item == null)
			return 0;
		else
		{
			int time = AERecipes.getInstance().getTime(item);
			if(upgrades != 0)
			{
				time -= time * upgrades / 8;
				time -= 10 * upgrades;
				if(time < 0)
					return 1;
			}
			if(time > 0)
				return time;
			return 1;
		}
	}

	public int getResultNumber(ItemStack item)
	{
		if(item == null)
			return 0;
		else
		{
			int number = AERecipes.getInstance().getNumber(item);
			if(number > 0)
				return number;
			return 0;
		}
	}

	public void checkUpgrades()
	{
		byte temp = 0;
		BlockPos tempPos = null;

		for(int i = -1;i <= 1;i += 2)
		{
			tempPos = pos.add(0, i, 0);
			if(worldObj.getBlockState(tempPos).getBlock() == GAMod.hardenedbrick
					&& worldObj.getBlockState(tempPos).getValue(HardenedBrick.TYPE) == EnumHardenedBrick.ADVANCED)
				temp++;

			tempPos = pos.add(i, 0, 0);
			if(worldObj.getBlockState(tempPos).getBlock() == GAMod.hardenedbrick
					&& worldObj.getBlockState(tempPos).getValue(HardenedBrick.TYPE) == EnumHardenedBrick.ADVANCED)
				temp++;

			tempPos = pos.add(0, 0, i);
			if(worldObj.getBlockState(tempPos).getBlock() == GAMod.hardenedbrick
					&& worldObj.getBlockState(tempPos).getValue(HardenedBrick.TYPE) == EnumHardenedBrick.ADVANCED)
				temp++;

			if(temp > 4)
				upgrades = 4;
			else
				upgrades = temp;

		}
	}

	@Override
	public String getName()
	{
		return "AtomExtractor";
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side)
	{
		return new int[] {0, 1, 2, 3, 4};
	}

	@Override
	public boolean canInsertItem(int index, ItemStack stack, EnumFacing direction)
	{
		if(stack.getItem() == GAMod.elementContainer && stack.getMetadata() == EnumElements.EMPTY.getMetadata()
				&& index == 1)
			return true;

		if(AERecipes.getInstance().getSmeltingResult(stack) != null && index == 0)
			return true;

		return false;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction)
	{
		if(index == 2 || index == 3 || index == 4)
			return true;
		return false;
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		return new AEGUI(player.inventory, this);
	}

	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		return new AEContainer(this, player.inventory);
	}
}
