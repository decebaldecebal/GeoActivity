package geoactivity.common.items;

import geoactivity.common.GeoActivity;
import geoactivity.common.lib.IHasName;
import geoactivity.common.lib.Reference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BaseItem extends Item implements IHasName
{
	protected final String name[];

	public BaseItem(String... name)
	{
		this.name = name;
		this.setUnlocalizedName(name[0]);
		this.setMaxStackSize(64);
		this.setCreativeTab(GeoActivity.tabMain);
		GameRegistry.register(this.setRegistryName(Reference.MOD_ID, name[0]));
	}

	@Override
	public String getName()
	{
		return getName(0);
	}

	@Override
	public String getName(int meta)
	{
		return name[meta];
	}

	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		if(stack.getHasSubtypes() == true)
			return getName(stack.getMetadata());
		return getName();
	}
}
