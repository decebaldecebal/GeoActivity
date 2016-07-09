package geoactivity.common.blocks.Machines.ACR;

import geoactivity.common.GAMod;
import geoactivity.common.blocks.Machines.AdvancedCoalRefiner;
import geoactivity.common.blocks.Machines.CR.CRRecipes;
import geoactivity.common.items.MachinePerks.EnumMachinePerks;
import geoactivity.common.util.BaseTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ACRTileE extends BaseTileEntity
{
	public int furnaceBurnTime = 0;
	public int currentItemBurnTime = 0;
	public int furnaceCookTime = 0;
	public int tneighbors[][];
	public int tneighbors2[][];

	private BlockPos corePos = null;
	public byte perkEff = 0, perkSpeed = 0;
	private boolean isValid = false;
	private boolean isCore = false;

	public ACRTileE()
	{
		super(7);
	}

	public void makeMultiblock()
	{
		int neighbors[][] = checkNeighbors(pos.getX(), pos.getY(), pos.getZ());

		if(neighbors[3][0] != 0 && neighbors[3][1] != 0 && neighbors[3][2] != 0)
			return ;
		else if(neighbors[2][0] != 0 && neighbors[2][1] != 0 && neighbors[2][2] != 0)
		{
			if(neighbors[0][1] == pos.getY() || neighbors[1][0] == pos.getX() || neighbors[2][2] == pos.getZ())
				return ;
			else
			{
				int neighbors2[][] = checkNeighbors(neighbors[1][0], neighbors[0][1], neighbors[2][2]);

				if(neighbors2[3][0] != 0 && neighbors2[3][1] != 0 && neighbors2[3][2] != 0)
					return ;
				else if(neighbors2[2][0] != 0 && neighbors2[2][1] != 0 && neighbors2[2][2] != 0)
				{
					if(neighbors2[0][1] != pos.getY() || neighbors2[1][0] != neighbors[2][0]
							|| neighbors2[2][2] != neighbors[1][2])
						return ;
					else
					{
						tneighbors = neighbors;
						tneighbors2 = neighbors2;
						BlockPos tempPos;
						ACRTileE tile_entity;

						for(int i = 0;i < 3;i++)
						{
							tempPos = new BlockPos(neighbors[i][0], neighbors[i][1], neighbors[i][2]);
							tile_entity = (ACRTileE) worldObj.getTileEntity(tempPos);

							if(tile_entity != null)
							{
								tile_entity.setCore(this);
								AdvancedCoalRefiner.setState(true, worldObj, tempPos);
							}

							tempPos = new BlockPos(neighbors2[i][0], neighbors2[i][1], neighbors2[i][2]);
							tile_entity = (ACRTileE) worldObj.getTileEntity(tempPos);

							if(tile_entity != null)
							{
								tile_entity.setCore(this);
								AdvancedCoalRefiner.setState(true, worldObj, tempPos);
							}
						}

						this.isCore = true;
						this.setCore(this);
						AdvancedCoalRefiner.setState(true, worldObj, pos);

						tempPos = new BlockPos(neighbors[1][0], neighbors[0][1], neighbors[2][2]);
						tile_entity = (ACRTileE) worldObj.getTileEntity(tempPos);

						if(tile_entity != null)
						{
							tile_entity.setCore(this);
							AdvancedCoalRefiner.setState(true, worldObj, tempPos);
						}
					}
				}
			}
		}
	}

	public boolean destroyMultiBlock()
	{
		if(getCore() != null)
		{
			ACRTileE coretile = getCore();
			tneighbors = coretile.tneighbors;
			tneighbors2 = coretile.tneighbors2;

			for(int i = 0;i < coretile.getSizeInventory();i++)
				coretile.inventory[i] = null;

			coretile.furnaceBurnTime = 0;
			coretile.currentItemBurnTime = 0;
			coretile.furnaceCookTime = 0;
			coretile.isCore = false;
			coretile.setCore(null);
			AdvancedCoalRefiner.setState(false, worldObj, coretile.getPos());

			BlockPos tempPos;
			ACRTileE tile_entity;

			for(int i = 0;i < 3;i++)
			{
				tempPos = new BlockPos(tneighbors[i][0], tneighbors[i][1], tneighbors[i][2]);
				tile_entity = (ACRTileE) worldObj.getTileEntity(tempPos);

				if(tile_entity != null)
				{
					tile_entity.setCore(null);
					AdvancedCoalRefiner.setState(false, worldObj, tempPos);
				}

				tempPos = new BlockPos(tneighbors2[i][0], tneighbors2[i][1], tneighbors2[i][2]);
				tile_entity = (ACRTileE) worldObj.getTileEntity(tempPos);

				if(tile_entity != null)
				{
					tile_entity.setCore(null);
					AdvancedCoalRefiner.setState(false, worldObj, tempPos);
				}
			}

			tempPos = new BlockPos(tneighbors[1][0], tneighbors[0][1], tneighbors[2][2]);
			tile_entity = (ACRTileE) worldObj.getTileEntity(tempPos);

			if(tile_entity != null)
			{
				tile_entity.setCore(null);
				AdvancedCoalRefiner.setState(false, worldObj, tempPos);
			}
			return true;
		}
		else
			return false;
	}

	private int[][] checkNeighbors(int x, int y, int z)
	{
		int a[][] = new int[6][3];
		int i = 0;
		Block block = GAMod.advancedcoalrefiner;

		if(worldObj.getBlockState(new BlockPos(x, y + 1, z)).getBlock() == block)
		{
			a[i][0] = x;
			a[i][1] = y + 1;
			a[i][2] = z;
			i++;
		}
		if(worldObj.getBlockState(new BlockPos(x, y - 1, z)).getBlock() == block)
		{
			a[i][0] = x;
			a[i][1] = y - 1;
			a[i][2] = z;
			i++;
		}
		if(worldObj.getBlockState(new BlockPos(x + 1, y, z)).getBlock() == block)
		{
			a[i][0] = x + 1;
			a[i][1] = y;
			a[i][2] = z;
			i++;
		}
		if(worldObj.getBlockState(new BlockPos(x - 1, y, z)).getBlock() == block)
		{
			a[i][0] = x - 1;
			a[i][1] = y;
			a[i][2] = z;
			i++;
		}
		if(worldObj.getBlockState(new BlockPos(x, y, z + 1)).getBlock() == block)
		{
			a[i][0] = x;
			a[i][1] = y;
			a[i][2] = z + 1;
			i++;
		}
		if(worldObj.getBlockState(new BlockPos(x, y, z - 1)).getBlock() == block)
		{
			a[i][0] = x;
			a[i][1] = y;
			a[i][2] = z - 1;
		}
		return a;
	}

	public ACRTileE getCore()
	{
		if(this.isValid && corePos != null)
			return (ACRTileE) worldObj.getTileEntity(corePos);

		return null;
	}

	public void setCore(ACRTileE core)
	{
		if(core != null)
		{
			this.isValid = true;
			corePos = new BlockPos(core.getPos().getX(), core.getPos().getY(), core.getPos().getZ());
		}
		else
		{
			this.isValid = false;
			corePos = null;
		}
	}

	@Override
	public ItemStack getStackInSlot(int slot)
	{
		if(isValid && getCore() != null && getCore() != worldObj.getTileEntity(pos))
			return getCore().getStackInSlot(slot);

		return super.getStackInSlot(slot);
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack)
	{
		if(isValid && getCore() != null && getCore() != worldObj.getTileEntity(pos))
		{
			getCore().setInventorySlotContents(slot, stack);
			return;
		}

		super.setInventorySlotContents(slot, stack);
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount)
	{
		if(isValid && getCore() != null && getCore() != worldObj.getTileEntity(pos))
			return getCore().decrStackSize(slot, amount);

		return super.decrStackSize(slot, amount);
	}

	@Override
	public ItemStack removeStackFromSlot(int slot)
	{
		if(isValid && getCore() != null && getCore() != worldObj.getTileEntity(pos))
			return getCore().removeStackFromSlot(slot);

		return super.removeStackFromSlot(slot);
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound)
	{
		super.readFromNBT(tagCompound);

		isCore = tagCompound.getBoolean("iscore");
		isValid = tagCompound.getBoolean("isvalid");
		if (isValid)
			corePos = new BlockPos(tagCompound.getInteger("CoreX"), tagCompound.getInteger("CoreY"), tagCompound.getInteger("CoreZ"));
		else
			corePos = null;

		if (this.isCore)
		{
			furnaceBurnTime = tagCompound.getShort("BurnTime");
			furnaceCookTime = tagCompound.getShort("CookTime");
			currentItemBurnTime = tagCompound.getShort("ItemBurnTime");

			perkEff = tagCompound.getByte("perkeff");
			perkSpeed = tagCompound.getByte("perkspeed");

			NBTTagList temp = tagCompound.getTagList("tempneighbors", Constants.NBT.TAG_COMPOUND);
			int[][] tlist = new int[6][3];
			for(int i = 0;i < temp.tagCount();i++)
			{
				NBTTagCompound tag = temp.getCompoundTagAt(i);
				tlist[i] = tag.getIntArray("temp");
			}
			tneighbors = tlist;

			temp = tagCompound.getTagList("tempneighbors2", Constants.NBT.TAG_COMPOUND);
			int[][] tlist1 = new int[6][3];
			for(int i = 0;i < temp.tagCount();i++)
			{
				NBTTagCompound tag1 = temp.getCompoundTagAt(i);
				tlist1[i] = tag1.getIntArray("temp");
			}
			tneighbors2 = tlist1;
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound)
	{
		super.writeToNBT(tagCompound);

		tagCompound.setBoolean("iscore", isCore);
		tagCompound.setBoolean("isvalid", isValid);
		if (isValid && corePos != null)
		{
			tagCompound.setInteger("CoreX", corePos.getX());
			tagCompound.setInteger("CoreY", corePos.getY());
			tagCompound.setInteger("CoreZ", corePos.getZ());
		}

		if (this.isCore)
		{
			tagCompound.setShort("BurnTime", (short) furnaceBurnTime);
			tagCompound.setShort("CookTime", (short) furnaceCookTime);
			tagCompound.setShort("ItemBurnTime", (short) currentItemBurnTime);

			tagCompound.setByte("perkeff", perkEff);
			tagCompound.setByte("perkspeed", perkSpeed);

			if(tneighbors != null)
			{
				NBTTagList temp = new NBTTagList();
				for(int i = 0;i < tneighbors.length;i++)
				{
					NBTTagCompound tag = new NBTTagCompound();
					tag.setIntArray("temp", tneighbors[i]);
					temp.appendTag(tag);
				}
				tagCompound.setTag("tempneighbors", temp);
			}

			if(tneighbors2 != null)
			{
				NBTTagList temp1 = new NBTTagList();
				for(int i = 0;i < tneighbors2.length;i++)
				{
					NBTTagCompound tag1 = new NBTTagCompound();
					tag1.setIntArray("temp", tneighbors2[i]);
					temp1.appendTag(tag1);
				}
				tagCompound.setTag("tempneighbors2", temp1);
			}
		}

		return tagCompound;
	}

	@SideOnly(Side.CLIENT)
	public int getCookProgressScaled(int par1)
	{
		double basetime = 1.0 - (0.25 * perkSpeed);
		return (int) (furnaceCookTime * par1 / 150 / basetime);
	}

	@SideOnly(Side.CLIENT)
	public int getBurnTimeRemainingScaled(int par1)
	{
		double basetime = 1.0 - (0.25 * perkSpeed);

		if(currentItemBurnTime == 0)
		{
			currentItemBurnTime = (int) (200 * basetime);
		}

		return furnaceBurnTime * par1 / currentItemBurnTime;
	}

	public boolean isBurning()
	{
		return furnaceBurnTime > 0;
	}

	public boolean isstackone()
	{
		return inventory[1] != null;
	}

	public boolean isstacktwo()
	{
		return inventory[2] != null;
	}

	private void checkPerks()
	{
		perkEff = perkSpeed = 0;
		for(int i = 5;i < 7;i++)
		{
			if(inventory[i] != null)
			{
				if(inventory[i].getItemDamage() == EnumMachinePerks.EFFICIENCY.getMetadata())
					perkEff++;
				else if(inventory[i].getItemDamage() == EnumMachinePerks.SPEED.getMetadata())
					perkSpeed++;
			}
		}
	}

	@Override
	public void update()
	{
		if (this.isCore)
		{
			boolean var1 = furnaceBurnTime > 0;
			boolean var2 = false;

			if(furnaceBurnTime > 0)
				--furnaceBurnTime;

			if(!worldObj.isRemote)
			{
				if(furnaceBurnTime == 0 && this.canSmelt())
				{
					checkPerks();
					currentItemBurnTime = furnaceBurnTime = getItemBurnTime(inventory[0]);

					if(furnaceBurnTime > 0)
					{
						var2 = true;

						if(inventory[0] != null)
						{
							--inventory[0].stackSize;

							if(inventory[0].stackSize == 0)
								inventory[0] = inventory[0].getItem().getContainerItem(inventory[0]);
						}
					}
				}

				if(this.isBurning() && this.canSmelt())
				{
					++furnaceCookTime;
					double basetime = 1.0 - (0.25 * perkSpeed);

					if(furnaceCookTime >= 200 * basetime)
					{
						furnaceCookTime = 0;
						this.smeltItem();
						var2 = true;
					}
				}
				else
					furnaceCookTime = 0;

				if(var1 != furnaceBurnTime > 0)
				{
					var2 = true;
				}
			}

			if(var2)
				this.markDirty();
		}
	}

	private boolean canSmelt()
	{
		if(inventory[1] == null && inventory[2] == null)
			return false;
		else
		{
			ItemStack var1 = null;
			ItemStack var2 = null;
			if(inventory[1] != null)
				var1 = CRRecipes.getInstance().getSmeltingResult(inventory[1]);
			if(inventory[2] != null)
				var2 = CRRecipes.getInstance().getSmeltingResult(inventory[2]);

			if(var1 != null)
			{
				if(inventory[3] == null)
				{
					return true;
				}
				else if(inventory[3].isItemEqual(var1))
				{
					int result1 = inventory[3].stackSize + var1.stackSize;
					return (result1 <= getInventoryStackLimit() && result1 <= var1.getMaxStackSize());
				}
			}
			if(var2 != null)
			{
				if(inventory[4] == null)
				{
					return true;
				}
				else if(inventory[4].isItemEqual(var2))
				{
					int result2 = inventory[4].stackSize + var2.stackSize;
					return (result2 <= getInventoryStackLimit() && result2 <= var2.getMaxStackSize());
				}
			}
			return false;
		}
	}

	public void smeltItem()
	{
		if(this.canSmelt())
		{
			ItemStack var1 = null;
			ItemStack var2 = null;
			if(inventory[1] != null)
				var1 = CRRecipes.getInstance().getSmeltingResult(inventory[1]);
			if(inventory[2] != null)
				var2 = CRRecipes.getInstance().getSmeltingResult(inventory[2]);

			if(var1 != null)
			{
				if(inventory[3] == null)
					inventory[3] = var1.copy();
				else if(inventory[3].isItemEqual(var1))
					inventory[3].stackSize += var1.stackSize;

				--inventory[1].stackSize;
				if(inventory[1].stackSize <= 0)
					inventory[1] = null;
			}

			if(var2 != null)
			{
				if(inventory[4] == null)
					inventory[4] = var2.copy();
				else if(inventory[4].isItemEqual(var2))
					inventory[4].stackSize += var2.stackSize;

				--inventory[2].stackSize;
				if(inventory[2].stackSize <= 0)
					inventory[2] = null;
			}
			//checkPerks();
		}

	}

	public int getItemBurnTime(ItemStack stack)
	{
		if(stack == null)
			return 0;
		else
		{
			Item item = stack.getItem();

			double basefuel = 1.0 + (0.5 * perkEff);
			double basetime = 1.0 - (0.25 * perkSpeed);

			if(item == Items.COAL)
				return (int) (200 * basetime * basefuel); // fuel times
			if(item == GAMod.gemLigniteCoal)
				return (int) (1600 * basetime * basefuel);
			if(item == GAMod.gemBituminousCoal)
				return (int) (3200 * basetime * basefuel);
			if(item == GAMod.gemAnthraciteCoal)
				return (int) (4800 * basetime * basefuel);
			return 0;
		}
	}

	public boolean isItemFuel(ItemStack stack)
	{
		return getItemBurnTime(stack) > 0;
	}

	@Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
    {
        return oldState.getBlock() != newState.getBlock();
    }

	@Override
	public String getName()
	{
		return "GeoActivity Inventory";
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side)
	{
		return new int[] {0, 1, 2, 3, 4};
	}

	@Override
	public boolean canInsertItem(int i, ItemStack itemStack, EnumFacing direction)
	{
		if(isValid)
		{
			if(getCore() == worldObj.getTileEntity(pos))
			{
				if(i > 0 && i < 3 && CRRecipes.getInstance().getSmeltingResult(itemStack) != null)
					return true;
				else if(i == 0 && FuelSlot.isFuel(itemStack))
					return true;
			}
			else if(getCore() != null)
				return getCore().canInsertItem(i, itemStack, direction);
		}
		return false;
	}

	@Override
	public boolean canExtractItem(int i, ItemStack itemStack, EnumFacing direction)
	{
		if(isValid)
		{
			if(getCore() == worldObj.getTileEntity(pos))
			{
				if(i > 2 && i < 5)
					return true;
			}
			else if(getCore() != null)
				return getCore().canExtractItem(i, itemStack, direction);
		}
		return false;
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		return new ACRGUI(player.inventory, this);
	}

	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		return new ACRContainer(this, player.inventory);
	}
}
