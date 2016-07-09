package geoactivity.common.blocks.Machines.GG;

import java.util.ArrayList;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;
import geoactivity.common.GAMod;
import geoactivity.common.lib.IOpenableGUI;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

public class GGTileE extends TileEntity implements ITickable, IEnergyProvider, IOpenableGUI, IInventory
{
	public static final int updateAfter = 1200; //one minute 1200
	private static final int maxDistanceSq = 625; //max 25 blocks away
	private static final byte RFPerBlock = 4;
	private static final byte chanceToChangeToObsidian = 40;
	public static final int internalStorage = 50000;

	public int ticksTillUpdate = updateAfter / 2;
	public int lavaBlocks = 0;
	public byte hightEfficiency = 1; //max 100
	public int RFPerTick = 0;
	ArrayList<BlockPos> blocks = new ArrayList<BlockPos>();

	public EnergyStorage buffer = new EnergyStorage(internalStorage);

	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);

		buffer.readFromNBT(tag);
		ticksTillUpdate = tag.getInteger("ticksTillUpdate");
		lavaBlocks = tag.getInteger("lavaBlocks");
		hightEfficiency = tag.getByte("hightEfficiency");
		RFPerTick = tag.getInteger("RFPerTick");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag)
	{
		super.writeToNBT(tag);

		buffer.writeToNBT(tag);
		tag.setInteger("ticksTillUpdate", ticksTillUpdate);
		tag.setInteger("lavaBlocks", lavaBlocks);
		tag.setByte("hightEfficiency", hightEfficiency);
		tag.setInteger("RFPerTick", RFPerTick);

		return tag;
	}

	@Override
	public void update()
	{
		if(!worldObj.isRemote)
		{
			if(ticksTillUpdate == 0)
			{
				blocks.clear();
				lavaBlocks = 0;
				hightEfficiency = (byte) Math.max(1f, Math.min(((120f - this.pos.getY()) / 100f) * 100f, 100f));

				scanLavaBlocks(this.pos.north());
				scanLavaBlocks(this.pos.south());
				scanLavaBlocks(this.pos.east());
				scanLavaBlocks(this.pos.west());

				this.updateRFRates();
			}

			ticksTillUpdate++;

			if(ticksTillUpdate == updateAfter / 2 && lavaBlocks > 0)
				changeLavaToObsidian();

			if(ticksTillUpdate >= updateAfter)
				ticksTillUpdate = 0;

			if(this.RFPerTick > 0 && buffer.getEnergyStored() <= buffer.getMaxEnergyStored() - this.RFPerTick)
				buffer.modifyEnergyStored(RFPerTick);

			if(buffer.getEnergyStored() > 0)
			{
				int usedEnergy = Math.min(buffer.getEnergyStored(), buffer.getMaxExtract());
				int outputEnergy = usedEnergy;

				usedEnergy -= this.outputEnergy(EnumFacing.DOWN, usedEnergy);
				usedEnergy -= this.outputEnergy(EnumFacing.UP, usedEnergy);

				this.buffer.modifyEnergyStored(usedEnergy - outputEnergy);
			}
		}
	}

	private int outputEnergy(EnumFacing dir, int usedEnergy)
	{
		if(usedEnergy > 0)
		{
			TileEntity tileEntity = this.worldObj.getTileEntity(this.pos.offset(dir));

			if(tileEntity instanceof IEnergyReceiver)
				return ((IEnergyReceiver) tileEntity).receiveEnergy(dir.getOpposite(), usedEnergy, false);
		}
		return 0;
	}

	private void updateRFRates()
	{
		this.RFPerTick = (int) (lavaBlocks * RFPerBlock * (hightEfficiency / 100f));
		this.buffer.setMaxTransfer(Math.max(this.RFPerTick * 2, 80));
	}

	private void scanLavaBlocks(BlockPos start)
	{
		if(worldObj.getBlockState(start) == Blocks.LAVA.getDefaultState())
		{
			if(this.pos.distanceSq(start) < maxDistanceSq && !blocks.contains(start))
			{
				blocks.add(start);
				lavaBlocks++;

				scanLavaBlocks(start.north());
				scanLavaBlocks(start.south());
				scanLavaBlocks(start.east());
				scanLavaBlocks(start.west());
				scanLavaBlocks(start.up());
				scanLavaBlocks(start.down());
			}
		}
		else if(worldObj.getBlockState(start).getBlock() == GAMod.geothermalgenerator && !this.pos.equals(start))
		{
			this.lavaBlocks /= 2;

			GGTileE tile = (GGTileE) worldObj.getTileEntity(start);
			if(tile != null)
			{
				tile.lavaBlocks /= 2;
				tile.updateRFRates();
			}
		}
	}

	private void changeLavaToObsidian()
	{
		BlockPos temp;

		for(int i = -1;i <= 1;i++)
			for(int j = -1;j <= 1;j++)
				for(int k = -1;k <= 1;k++)
				{
					temp = this.pos.add(i, j, k);
					if(worldObj.getBlockState(temp) == Blocks.LAVA.getDefaultState())
					{
						if(worldObj.rand.nextInt(100) < chanceToChangeToObsidian)
						{
							worldObj.setBlockState(temp, Blocks.OBSIDIAN.getDefaultState());
							break;
						}
					}
				}
	}

	@Override
	public boolean canConnectEnergy(EnumFacing facing)
	{
		return facing == EnumFacing.DOWN || facing == EnumFacing.UP;
	}

	@Override
	public int extractEnergy(EnumFacing facing, int maxExtract, boolean simulate)
	{
		if(canConnectEnergy(facing))
			return buffer.extractEnergy(maxExtract, simulate);

		return 0;
	}

	@Override
	public int getEnergyStored(EnumFacing facing)
	{
		if(canConnectEnergy(facing))
			return buffer.getEnergyStored();

		return 0;
	}

	@Override
	public int getMaxEnergyStored(EnumFacing facing)
	{
		if(canConnectEnergy(facing))
			return buffer.getMaxEnergyStored();

		return 0;
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		return new GGGUI(player.inventory, this);
	}

	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		return new GGContainer(this, player.inventory);
	}

	@Override
	public String getName()
	{
		return null;
	}

	@Override
	public boolean hasCustomName()
	{
		return false;
	}

	@Override
	public ITextComponent getDisplayName()
	{
		return null;
	}

	@Override
	public int getSizeInventory()
	{
		return 0;
	}

	@Override
	public ItemStack getStackInSlot(int index)
	{
		return null;
	}

	@Override
	public ItemStack decrStackSize(int index, int count)
	{
		return null;
	}

	@Override
	public ItemStack removeStackFromSlot(int index)
	{
		return null;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack)
	{}

	@Override
	public int getInventoryStackLimit()
	{
		return 0;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		return true;
	}

	@Override
	public void openInventory(EntityPlayer player)
	{}

	@Override
	public void closeInventory(EntityPlayer player)
	{}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack)
	{
		return false;
	}

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
}
