package geoactivity.common.blocks;

import java.util.List;

import geoactivity.common.util.GeneralHelper;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;

public class HardenedBrick extends BaseBlock
{
	public static final PropertyEnum<EnumHardenedBrick> TYPE = PropertyEnum.create("type", EnumHardenedBrick.class);

	public HardenedBrick(String name)
	{
		super(Material.ROCK, "pickaxe", 3, GeneralHelper.appendStringToArray(
				GeneralHelper.getEnumStrings(EnumHardenedBrick.class), name + "_"));
		this.setHardness(20.0F);
		this.setResistance(150.0F);
		this.setSoundType(SoundType.STONE);
		this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, EnumHardenedBrick.NORMAL));
	}

	@Override
	public int damageDropped(IBlockState state)
	{
		EnumHardenedBrick type = state.getValue(TYPE);
		return type.getMetadata();
	}

	@Override
	public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> subItems)
	{
		for(int i = 0;i < EnumHardenedBrick.values().length;i++)
		{
			subItems.add(new ItemStack(this, 1, i));
		}
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		EnumHardenedBrick[] values = EnumHardenedBrick.values();
		if(meta >= 0 && meta < values.length)
			return this.getDefaultState().withProperty(TYPE, values[meta]);
		return this.getDefaultState();
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		EnumHardenedBrick type = state.getValue(TYPE);

		return type.getMetadata();
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] {TYPE});
	}

	public static enum EnumHardenedBrick implements IStringSerializable
	{
		NORMAL(0, "normal"), ADVANCED(1, "advanced"), THERMAL(2, "thermal");

		int meta;
		String name;

		private EnumHardenedBrick(int meta, String name)
		{
			this.meta = meta;
			this.name = name;
		}

		public int getMetadata()
		{
			return this.meta;
		}

		@Override
		public String toString()
		{
			return this.name;
		}

		@Override
		public String getName()
		{
			return this.name;
		}
	}
}
