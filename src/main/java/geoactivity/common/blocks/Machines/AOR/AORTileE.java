package geoactivity.common.blocks.Machines.AOR;

import geoactivity.common.GAMod;
import geoactivity.common.blocks.HardenedBrick;
import geoactivity.common.blocks.HardenedBrick.EnumHardenedBrick;
import geoactivity.common.blocks.Machines.AdvancedOreRefiner;
import geoactivity.common.items.MachinePerks.EnumMachinePerks;
import geoactivity.common.util.BaseTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

public class AORTileE extends BaseTileEntity
{
	BlockPos[] blocks = new BlockPos[28];

	public int furnaceBurnTime = 0;
	public int currentItemBurnTime = 0;
	public int furnaceCookTime = 0;
	public byte furnaceSpecial = 0;

	private BlockPos corePos = null;
	public byte perkEff = 0, perkSpeed = 0;
	private boolean isHeating = false;
	public boolean isBrick = false;
	private boolean isValid = false;
	private boolean isCore = false;

	public AORTileE()
	{
		super(15);
	}

	public void makeMultiblock()
	{
		byte d = 0;
		BlockPos axis = checkFacing();
		BlockPos offset = pos.add(axis);
		BlockPos temp = null;

		for(byte i = -1;i <= 2;i++)
		{
			temp = offset.add(0, i, 0);
			if(worldObj.getBlockState(temp).getBlock() != GAMod.hardenedbrick
					|| worldObj.getBlockState(temp).getValue(HardenedBrick.TYPE) != EnumHardenedBrick.ADVANCED)
				return;

			blocks[d] = temp;
			d++;
		}

		offset = offset.add(axis);

		for(byte i = -1;i <= 2;i++)
		{
			temp = offset.add(0, i, 0);
			if((i == 0 || i == 1) && !worldObj.isAirBlock(temp))
				return;
			else if((i == -1 || i == 2)
					&& (worldObj.getBlockState(temp).getBlock() != GAMod.hardenedbrick
						|| worldObj.getBlockState(temp).getValue(HardenedBrick.TYPE) != EnumHardenedBrick.ADVANCED))
				return;

			if(i == -1 || i == 2)
			{
				blocks[d] = temp;
				d++;
			}
		}

		EnumFacing facing = worldObj.getBlockState(pos).getValue(AdvancedOreRefiner.FACING);
		int qq = 0;

		switch(facing)
		{
			case NORTH:
			case WEST:
				qq = 0;
			break;
			case SOUTH:
			case EAST:
				qq = -1;
			break;
			default:
			break;
		}

		for(int j = 0;j <= 1;j++)
			for(int i = -2;i <= 2;i++)
				for(int q = qq;q <= qq + 1;q++)
				{
					switch(facing)
					{
						case NORTH:
						case SOUTH:
							temp = pos.add(i, j, axis.getZ() * 2 + q);
						break;
						case EAST:
						case WEST:
							temp = pos.add(axis.getX() * 2 + q, j, i);
						break;
						default:
						break;
					}

					if(i != 0
							&& worldObj.getBlockState(temp).getBlock() != GAMod.advancedcoalrefiner)
						return;

					if(i != 0)
					{
						blocks[d] = temp;
						d++;
					}
				}

		offset = offset.add(axis);

		for(byte i = -1;i <= 2;i++)
		{
			temp = offset.add(0, i, 0);
			if((i == 0 || i == 1) && !worldObj.isAirBlock(temp))
				return;
			else if((i == -1 || i == 2)
					&& (worldObj.getBlockState(temp).getBlock() != GAMod.hardenedbrick
						|| worldObj.getBlockState(temp).getValue(HardenedBrick.TYPE) != EnumHardenedBrick.ADVANCED))
				return;

			if(i == -1 || i == 2)
			{
				blocks[d] = temp;
				d++;
			}
		}

		offset = offset.add(axis);

		for(byte i = -1;i <= 2;i++)
		{
			temp = offset.add(0, i, 0);
			if(worldObj.getBlockState(temp).getBlock() != GAMod.hardenedbrick
					|| worldObj.getBlockState(temp).getValue(HardenedBrick.TYPE) != EnumHardenedBrick.ADVANCED)
				return;

			blocks[d] = temp;
			d++;
		}

		for(int i = 0;i < blocks.length;i++)
		{
			if(blocks[i] != null)
			{
				boolean tisBrick = false;

				if(worldObj.getBlockState(blocks[i]).getBlock() == GAMod.hardenedbrick)
					tisBrick = true;

				worldObj.setBlockState(blocks[i], GAMod.advancedorerefiner.getDefaultState().withProperty(AdvancedOreRefiner.CORE, false)
						.withProperty(AdvancedOreRefiner.FORMED, true).withProperty(AdvancedOreRefiner.FACING, EnumFacing.SOUTH), 2);

				AORTileE tile_entity = (AORTileE) worldObj.getTileEntity(blocks[i]);
				if(tile_entity != null)
				{
					tile_entity.isBrick = tisBrick;
					tile_entity.setCore(this);
				}
			}
		}
		this.isCore = true;
		this.setCore(this);
		this.worldObj.setBlockState(pos, worldObj.getBlockState(pos).withProperty(AdvancedOreRefiner.FORMED, true), 2);
	}

	private BlockPos checkFacing()
	{
		byte x = 0, z = 0;
		EnumFacing facing = worldObj.getBlockState(pos).getValue(AdvancedOreRefiner.FACING);
		switch(facing)
		{
			case NORTH:
				z++;
			break;
			case SOUTH:
				z--;
			break;
			case WEST:
				x++;
			break;
			case EAST:
				x--;
			break;
			default:
			break;
		}
		return new BlockPos(x, 0, z);
	}

	public void destroyMultiBlock()
	{
		if(getCore() != null)
		{
			AORTileE core_tile = getCore();
			this.blocks = core_tile.blocks;

			for(int i = 0;i < blocks.length;i++)
			{
				if(blocks[i] != null)
				{
					AORTileE tile = (AORTileE) worldObj.getTileEntity(blocks[i]);

					if(tile != null && !blocks[i].equals(this.pos))
					{
						tile.setCore(null);
						if(tile.isBrick)
							worldObj.setBlockState(blocks[i], GAMod.hardenedbrick.getDefaultState()
									.withProperty(HardenedBrick.TYPE, EnumHardenedBrick.ADVANCED), 2);
						else
							worldObj.setBlockState(blocks[i], GAMod.advancedcoalrefiner.getDefaultState(), 2);
					}
				}
				blocks[i] = null;
			}

			core_tile.blocks = this.blocks;
			core_tile.isCore = false;
			core_tile.setCore(null);
			if (worldObj.getBlockState(core_tile.getPos()).getBlock() == GAMod.advancedorerefiner)
				worldObj.setBlockState(core_tile.getPos(), worldObj.getBlockState(core_tile.getPos()).withProperty(AdvancedOreRefiner.FORMED, false), 2);
			this.setCore(null);
		}
	}

	public AORTileE getCore()
	{
		if(this.isValid && corePos != null)
			return (AORTileE) worldObj.getTileEntity(corePos);

		return null;
	}

	public void setCore(AORTileE core)
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

		isBrick = tagCompound.getBoolean("isbrick");
		isCore = tagCompound.getBoolean("iscore");
		isValid = tagCompound.getBoolean("isvalid");
		if (isValid)
			corePos = new BlockPos(tagCompound.getInteger("CoreX"), tagCompound.getInteger("CoreY"), tagCompound.getInteger("CoreZ"));
		else
			corePos = null;

		if (this.isCore)
		{
			isHeating = tagCompound.getBoolean("isheating");
			furnaceSpecial = tagCompound.getByte("special");

			furnaceBurnTime = tagCompound.getShort("BurnTime");
			furnaceCookTime = tagCompound.getShort("CookTime");
			currentItemBurnTime = tagCompound.getShort("ItemBurnTime");

			perkEff = tagCompound.getByte("perkeff");
			perkSpeed = tagCompound.getByte("perkspeed");

			NBTTagList tagList2 = tagCompound.getTagList("Blocks", Constants.NBT.TAG_COMPOUND);
			if(tagList2.tagCount() == 28)
			{
				for(int i = 0;i < tagList2.tagCount();i++)
				{
					NBTTagCompound tag = tagList2.getCompoundTagAt(i);
					blocks[i] = new BlockPos(tag.getInteger("xC"), tag.getInteger("yC"), tag.getInteger("zC"));
				}
			}
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound)
	{
		super.writeToNBT(tagCompound);

		tagCompound.setBoolean("isbrick", isBrick);
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
			tagCompound.setBoolean("isheating", isHeating);
			tagCompound.setByte("special", furnaceSpecial);

			tagCompound.setShort("BurnTime", (short) furnaceBurnTime);
			tagCompound.setShort("CookTime", (short) furnaceCookTime);
			tagCompound.setShort("ItemBurnTime", (short) currentItemBurnTime);

			tagCompound.setByte("perkeff", perkEff);
			tagCompound.setByte("perkspeed", perkSpeed);

			NBTTagList blockList = new NBTTagList();
			for(int i = 0;i < blocks.length;i++)
			{
				if(blocks[i] != null)
				{
					NBTTagCompound tag = new NBTTagCompound();

					tag.setInteger("xC", blocks[i].getX());
					tag.setInteger("yC", blocks[i].getY());
					tag.setInteger("zC", blocks[i].getZ());

					blockList.appendTag(tag);
				}
			}
			tagCompound.setTag("Blocks", blockList);
		}

		return tagCompound;
	}

	@SideOnly(Side.CLIENT)
	public int getCookProgressScaled(int par1)
	{
		double basetime = 2.0 - (0.25 * perkSpeed);

		return (int) (furnaceCookTime * par1 / 80 / basetime);
	}

	@SideOnly(Side.CLIENT)
	public int getBurnTimeRemainingScaled(int par1)
	{
		double basetime = 2.0 - (0.25 * perkSpeed);

		if(currentItemBurnTime == 0)
		{
			currentItemBurnTime = (int) (50 * basetime);
		}

		return furnaceBurnTime * par1 / currentItemBurnTime;
	}

	private void checkPerks()
	{
		perkEff = perkSpeed = 0;
		for(int i = 0;i < 4;i++)
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

	public boolean[] hasStacks()
	{
		boolean[] stacks = new boolean[4];

		if(inventory[4] != null && FurnaceRecipes.instance().getSmeltingResult(inventory[4]) != null)
			stacks[0] = true;
		if(inventory[5] != null && FurnaceRecipes.instance().getSmeltingResult(inventory[5]) != null)
			stacks[1] = true;
		if(inventory[6] != null && FurnaceRecipes.instance().getSmeltingResult(inventory[6]) != null)
			stacks[2] = true;
		if(inventory[7] != null && FurnaceRecipes.instance().getSmeltingResult(inventory[7]) != null)
			stacks[3] = true;
		return stacks;
	}

	private boolean hasWood()
	{
		if(inventory[13] != null && inventory[13].stackSize >= 8)
			return true;
		return false;
	}

	@Override
	public void update()
	{
		boolean var1 = furnaceBurnTime > 0;
		boolean var2 = false;

		if(!worldObj.isRemote)
		{
			if(inventory[14] != null && this.furnaceSpecial == 0)
			{
				--inventory[14].stackSize;
				if(inventory[14].stackSize <= 0)
					inventory[14] = inventory[14].getItem().getContainerItem(inventory[14]);

				furnaceSpecial = 16;
			}

			if(furnaceBurnTime > 0 && !this.isHeating)
				--furnaceBurnTime;

			if(this.isHeating)
			{
				currentItemBurnTime = getItemBurnTime(inventory[12]);

				if(furnaceBurnTime < currentItemBurnTime)
					furnaceBurnTime += (10 + 2 * perkSpeed);
				else
				{
					furnaceBurnTime = 0;
					this.isHeating = false;
				}
			}

			if(!this.isHeating && this.canSmelt())
			{
				if(this.hasWood() && furnaceBurnTime == 0)
				{
					checkPerks();
					currentItemBurnTime = getItemBurnTime(inventory[12]);

					if(currentItemBurnTime > 0)
					{
						inventory[13].stackSize -= 8;
						if(inventory[13].stackSize <= 0)
							inventory[13] = inventory[13].getItem().getContainerItem(inventory[13]);

						this.isHeating = true;
					}
				}

				if(furnaceBurnTime > 0)
				{
					++furnaceCookTime;

					double basetime = 2.0 - (0.25 * perkSpeed);

					if(furnaceCookTime >= 50 * basetime)
					{
						furnaceCookTime = 0;
						this.smeltItem();
						var2 = true;
						if(furnaceSpecial > 0)
							furnaceSpecial--;
					}
				}
				else
					furnaceCookTime = 0;
			}

			if(furnaceBurnTime >= currentItemBurnTime && this.isHeating)
			{
				var2 = true;

				if(inventory[12] != null)
				{
					--inventory[12].stackSize;

					if(inventory[12].stackSize <= 0)
						inventory[12] = inventory[12].getItem().getContainerItem(inventory[12]);
				}

				isHeating = false;
			}

			if(var1 != furnaceBurnTime > 0)
				var2 = true;
		}
		if(var2)
			this.markDirty();
	}

	private boolean canSmelt()
	{
		boolean[] stack = hasStacks();

		for(int i = 0;i < stack.length;i++)
			if(stack[i] == true)
			{
				if (OreSlot.isItemStackValidOre(inventory[i + 4]))
				{
					ItemStack tresult = FurnaceRecipes.instance().getSmeltingResult(inventory[i + 4]);
					if(tresult != null)
					{
						ItemStack result = tresult.copy();
						result.stackSize = 4;

						if(inventory[i + 8] == null)
							return true;
						else if(inventory[i + 8].isItemEqual(result))
						{
							int result1 = inventory[i + 8].stackSize + result.stackSize;
							if((result1 <= getInventoryStackLimit() && result1 <= result.getMaxStackSize()))
								return true;
						}
					}
				}
			}
		return false;
	}

	public void smeltItem()
	{
		for(int i = 4;i < 8;i++)
		{
			ItemStack tresult = null;
			if(inventory[i] != null)
				tresult = FurnaceRecipes.instance().getSmeltingResult(inventory[i]);

			if(tresult != null)
			{
				ItemStack result = tresult.copy();
				result.stackSize = 2 + worldObj.rand.nextInt(4) / 3;
				if(furnaceSpecial > 0)
					result.stackSize = 3 + worldObj.rand.nextInt(6) / 5;

				if(inventory[i + 4] == null)
					inventory[i + 4] = result.copy();
				else if(inventory[i + 4].isItemEqual(result))
					inventory[i + 4].stackSize += result.stackSize;
				else
					++inventory[i].stackSize;

				--inventory[i].stackSize;
				if(inventory[i].stackSize <= 0)
					inventory[i] = null;
			}
		}
	}

	public int getItemBurnTime(ItemStack itemStack)
	{
		if(itemStack == null)
			return 0;
		else
		{
			Item item = itemStack.getItem();

			double basefuel = 1.0 + (0.25 * perkEff);
			double basetime = 2.0 - (0.25 * perkSpeed);

			if(item == GAMod.gemLigniteCoal)
				return (int) (1600 * basetime * basefuel);
			if(item == GAMod.gemBituminousCoal)
				return (int) (3200 * basetime * basefuel);
			if(item == GAMod.gemAnthraciteCoal)
				return (int) (4800 * basetime * basefuel);

			return 0;
		}
	}

	@Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
    {
        return oldState.getBlock() != newState.getBlock();
    }

	@Override
	public String getName()
	{
		return "AdvancedOreRefiner";
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side)
	{
		return new int[] {4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14};
	}

	@Override
	public boolean canInsertItem(int i, ItemStack itemStack, EnumFacing direction)
	{
		if(isValid)
		{
			if(getCore() == worldObj.getTileEntity(pos))
			{
				switch(i)
				{
					case 4:
					case 5:
					case 6:
					case 7:
						Block block = Block.getBlockFromItem(itemStack.getItem());
						if(block != null
								&& block.getLocalizedName().contains("Ore")
								&& block.getItemDropped(block.getStateFromMeta(itemStack.getMetadata()), worldObj.rand, 0) == itemStack
										.getItem())
							return true;
					break;

					case 12:
						Item item = itemStack.getItem();
						if(item == GAMod.gemLigniteCoal || item == GAMod.gemBituminousCoal
								|| item == GAMod.gemAnthraciteCoal)
							return true;
					break;

					case 13:
					{
						int wood = OreDictionary.getOreID("logWood");

						for(int id : OreDictionary.getOreIDs(itemStack))
						{
							if (id == wood)
								return true;
						}
					}
					break;

					case 14:
						return itemStack.getItem() == GAMod.carbonOreCutter;

					default:
						return false;
				}
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
				if(i >= 8 && i <= 11)
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
		return new AORGUI(player.inventory, this);
	}

	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		return new AORContainer(this, player.inventory);
	}
}
