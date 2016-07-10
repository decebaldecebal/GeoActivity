package geoactivity.common.items.tools;

import geoactivity.common.GAMod;
import geoactivity.common.GeoActivity;
import geoactivity.common.lib.IHasName;
import geoactivity.common.lib.Reference;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ReinforcedAxe extends ItemAxe implements IHasName
{
	private String name;

	public ReinforcedAxe(String name)
	{
		super(GAMod.ReinforcedMaterial, 2.0f + GAMod.ReinforcedMaterial.getDamageVsEntity(), -3.1f);
		this.name = name;
		this.setUnlocalizedName(name);
		this.setCreativeTab(GeoActivity.tabMain);
		GameRegistry.register(this.setRegistryName(Reference.MOD_ID, name));
	}

    @Override
	public float getStrVsBlock(ItemStack stack, IBlockState state)
    {
        Material material = state.getMaterial();
        return material != Material.WOOD && material != Material.PLANTS && material != Material.VINE ? super.getStrVsBlock(stack, state) : this.efficiencyOnProperMaterial;
    }

	@Override
	public String getName()
	{
		return this.name;
	}

	@Override
	public String getName(int meta)
	{
		return this.getName();
	}
}
