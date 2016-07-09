package geoactivity.common.blocks;

import java.util.Random;

import geoactivity.common.GAMod;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CustomCoalOres extends BaseBlock
{
	public CustomCoalOres(String name, int level)
	{
		super(Material.IRON, "pickaxe", level, name);
		this.setResistance(15.0F);
		this.setSoundType(SoundType.STONE);
	}

	@Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		if(state.getBlock() == GAMod.oreAnthracite)
			return GAMod.gemAnthraciteCoal;
		else if(state.getBlock() == GAMod.oreBituminous)
			return GAMod.gemBituminousCoal;
		else if(state.getBlock() == GAMod.oreLignite)
			return GAMod.gemLigniteCoal;

		return null;
	}

	@Override
    public void onBlockDestroyedByPlayer(World world, BlockPos pos, IBlockState state)
    {
		this.dropXpOnBlockBreak(world, pos, 3);
    }

	@Override
    public int quantityDropped(IBlockState state, int fortune, Random random)
    {
		switch(fortune)
		{
			case 1:
				return 1 + random.nextInt(3)/2;
			case 2:
				return 2 + random.nextInt(4)/3;
			case 3:
				return 2 + random.nextInt(3)/2;
			case 4:
				return 3;
			default:
				return 1;
		}
    }
}
