package geoactivity.common.util;

import java.util.List;
import java.util.Random;

import geoactivity.common.GeoActivity;
import geoactivity.common.lib.IHasName;
import geoactivity.common.lib.IOpenableGUI;
import geoactivity.common.lib.Reference;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraftforge.fml.common.registry.GameRegistry;

public abstract class BaseGUITool extends ItemTool implements IHasName, IOpenableGUI
{
	protected static Random random = new Random();
	private String name;

	public BaseGUITool(String name, ToolMaterial material)
	{
		super(0, -1.0f, material, null);

		this.setMaxStackSize(1);
		this.setHasSubtypes(false);
		this.setNoRepair();
		this.name = name;
		this.setUnlocalizedName(name);
		this.setCreativeTab(GeoActivity.tabMain);
		GameRegistry.register(this.setRegistryName(Reference.MOD_ID, name));
	}

	@Override
	public String getName()
	{
		return this.name;
	}

	@Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
	{
		return false;
	}

	public static void addDamageInfo(ItemStack stack, int baseDamage, List<String> list)
	{
		if(stack.hasTagCompound() && stack.getTagCompound().getByte("damage") >= 1)
			baseDamage += stack.getTagCompound().getByte("damage");

		if(stack.hasTagCompound() && stack.getTagCompound().getBoolean("barmor"))
		{
			list.add("\u00A79+" + (baseDamage - baseDamage / 3) + " Attack Damage");
			list.add("\u00A79+" + baseDamage / 3 + " Armor Bypass Damage");
		}
		else
			list.add("\u00A79+" + baseDamage + " Attack Damage");
	}
}
