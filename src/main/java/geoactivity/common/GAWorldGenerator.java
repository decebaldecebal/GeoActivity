package geoactivity.common;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;

public class GAWorldGenerator implements IWorldGenerator
{
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
	{
		switch (world.provider.getDimension())
		{
			case -1:
				generateNether(world, random, chunkX * 16, chunkZ * 16);
				break;

			case 0:
				generateSurface(world, random, chunkX * 16, chunkZ * 16);
				break;

			case 1:
				generateEnd(world, random, chunkX * 16, chunkZ * 16);
				break;
		}
	}

	private void generateSurface(World world, Random random, int x, int z)
	{
		int i,Xc,Zc,Yc;

		for (i = 0; i < GeoActivity.config.ligniteBlocks; i++)
		{
			Xc = x + random.nextInt(16);
			Zc = z + random.nextInt(16);
			Yc = GeoActivity.config.ligniteMinY + random.nextInt(GeoActivity.config.ligniteMaxY - GeoActivity.config.ligniteMinY);
			(new WorldGenMinable(GAMod.oreLignite.getDefaultState(), GeoActivity.config.ligniteSize))
			.generate(world, random, new BlockPos(Xc, Yc, Zc));
		}

		for (i = 0; i < GeoActivity.config.bituminousBlocks; i++)
		{
			Xc = x + random.nextInt(16);
			Zc = z + random.nextInt(16);
			Yc = GeoActivity.config.bituminousMinY + random.nextInt(GeoActivity.config.bituminousMaxY - GeoActivity.config.bituminousMinY);
			(new WorldGenMinable(GAMod.oreBituminous.getDefaultState(), GeoActivity.config.bituminousSize))
			.generate(world, random, new BlockPos(Xc, Yc, Zc));
		}

		for (i = 0; i < GeoActivity.config.anthraciteBlocks; i++)
		{
			Xc = x + random.nextInt(16);
			Zc = z + random.nextInt(16);
			Yc = GeoActivity.config.anthraciteMinY + random.nextInt(GeoActivity.config.anthraciteMaxY - GeoActivity.config.anthraciteMinY);
			(new WorldGenMinable(GAMod.oreAnthracite.getDefaultState(), GeoActivity.config.anthraciteSize))
			.generate(world, random, new BlockPos(Xc, Yc, Zc));
		}
	}

	private void generateEnd(World world, Random random, int x, int z)
	{
	}

	private void generateNether(World world, Random random, int x, int z)
	{
	}
}
