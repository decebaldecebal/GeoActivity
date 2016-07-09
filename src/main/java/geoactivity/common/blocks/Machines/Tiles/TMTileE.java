package geoactivity.common.blocks.Machines.Tiles;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyReceiver;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

public class TMTileE extends TileEntity implements ITickable, IEnergyReceiver
{
	private static final int updateAfter = 800; //40 seconds
	private static final int RFPerObsidian = 50000;
	private static final byte passiveRFPerTick = 10;

	private EnergyStorage buffer = new EnergyStorage(RFPerObsidian * 4, 500);
	private int ticksTillUpdate = updateAfter / 2;

	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);

		buffer.readFromNBT(tag);
		ticksTillUpdate = tag.getInteger("ticksTillUpdate");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag)
	{
		super.writeToNBT(tag);

		buffer.writeToNBT(tag);
		tag.setInteger("ticksTillUpdate", ticksTillUpdate);

		return tag;
	}

	@Override
	public void update()
	{
		if(!worldObj.isRemote)
		{
			if(buffer.getEnergyStored() >= passiveRFPerTick)
				buffer.modifyEnergyStored(-passiveRFPerTick);

			ticksTillUpdate++;

			if(ticksTillUpdate >= updateAfter)
			{
				if(buffer.getEnergyStored() >= RFPerObsidian)
					changeObsidianToLava();

				ticksTillUpdate = 0;
			}
		}
	}

	private void changeObsidianToLava()
	{
		BlockPos temp;

		for(int i = -2;i <= 2;i++)
			for(int j = -2;j <= 2;j++)
				for(int k = -2;k <= 2;k++)
				{
					temp = this.pos.add(i, j, k);
					if(worldObj.getBlockState(temp).getBlock() == Blocks.OBSIDIAN)
					{
						worldObj.setBlockState(temp, Blocks.LAVA.getDefaultState());
						buffer.modifyEnergyStored(-RFPerObsidian);

						if(buffer.getEnergyStored() < RFPerObsidian)
							return ;
						break;
					}
				}
	}

	@Override
	public boolean canConnectEnergy(EnumFacing facing)
	{
		return true;
	}

	@Override
	public int receiveEnergy(EnumFacing facing, int maxReceive, boolean simulate)
	{
		return this.buffer.receiveEnergy(maxReceive, simulate);
	}

	@Override
	public int getEnergyStored(EnumFacing facing)
	{
		return buffer.getEnergyStored();
	}

	@Override
	public int getMaxEnergyStored(EnumFacing facing)
	{
		return buffer.getMaxEnergyStored();
	}
}
