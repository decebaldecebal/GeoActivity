package geoactivity.common.blocks;

import geoactivity.common.GeoActivity;
import geoactivity.common.lib.IHasName;
import geoactivity.common.lib.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.common.registry.GameRegistry;

public abstract class BaseBlock extends Block implements IHasName
{
	protected final String[] name;

	public BaseBlock(Material mat, String tool, int tier, String... name)
	{
		super(mat);
		this.name = name;
		this.setUnlocalizedName(name[0]);
		this.setHarvestLevel(tool, tier);
		this.setCreativeTab(GeoActivity.tabMain);
		GameRegistry.register(this.setRegistryName(Reference.MOD_ID, name[0]));
	}

	@Override
	public String getName()
	{
		return name[0];
	}

	@Override
	public String getName(int meta)
	{
		return name[meta];
	}
}
