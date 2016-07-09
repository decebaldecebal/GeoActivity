package geoactivity.common.blocks.Machines;

import geoactivity.common.blocks.Machines.Tiles.TMTileE;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class ThermicMelter extends BaseContainerBlock
{
	public ThermicMelter(String name)
	{
		super(Material.IRON, name, "pickaxe", 2);
		this.setHardness(10.0F);
		this.setResistance(15.0F);
		this.setSoundType(SoundType.STONE);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int var)
	{
		return new TMTileE();
	}
}
