package geoactivity.common.blocks.Machines.Tiles;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;
import geoactivity.common.GAMod;
import geoactivity.common.blocks.HardenedBrick;
import geoactivity.common.blocks.HardenedBrick.EnumHardenedBrick;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

public class TGTileE extends TileEntity implements ITickable, IEnergyProvider
{
	private static byte RFPerBrickPerTick = 20;

	private EnergyStorage buffer = new EnergyStorage(10000, RFPerBrickPerTick * 5, 1000);
	private byte bricks = 0;

	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);

		buffer.readFromNBT(tag);
		bricks = tag.getByte("bricks");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag)
	{
		super.writeToNBT(tag);

		buffer.writeToNBT(tag);
		tag.setByte("bricks", bricks);

		return tag;
	}

	@Override
	public void update()
	{
		if(!worldObj.isRemote)
		{
			if(bricks > 0 && worldObj.isDaytime()
					&& buffer.getEnergyStored() <= buffer.getMaxEnergyStored() - RFPerBrickPerTick * bricks)
				buffer.modifyEnergyStored(RFPerBrickPerTick * bricks);

			if(buffer.getEnergyStored() > 0)
			{
				TileEntity tileEntity = worldObj.getTileEntity(pos.down());

				if(tileEntity != null && tileEntity instanceof IEnergyReceiver)
					buffer.modifyEnergyStored(-((IEnergyReceiver) tileEntity).receiveEnergy(EnumFacing.UP, Math.min(buffer.getEnergyStored(),
							buffer.getMaxExtract()), false));
			}
		}
	}

	public void checkBricks()
	{
		bricks = 0;

		if(isThermalBrick(pos.up()))
			bricks++;
		if(isThermalBrick(pos.east()))
			bricks++;
		if(isThermalBrick(pos.west()))
			bricks++;
		if(isThermalBrick(pos.north()))
			bricks++;
		if(isThermalBrick(pos.south()))
			bricks++;
	}

	private boolean isThermalBrick(BlockPos pos)
	{
		return worldObj.getBlockState(pos).getBlock() == GAMod.hardenedbrick
				&& worldObj.getBlockState(pos).getValue(HardenedBrick.TYPE) == EnumHardenedBrick.THERMAL &&
				worldObj.canBlockSeeSky(pos.up());
	}

	@Override
	public boolean canConnectEnergy(EnumFacing facing)
	{
		return facing == EnumFacing.DOWN;
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
}
