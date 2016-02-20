package geoactivity.common.util;

import java.util.List;
import java.util.Random;

import geoactivity.common.GeoActivity;
import geoactivity.common.items.tools.Adv.Logic.AdvTContainer;
import geoactivity.common.items.tools.Adv.Logic.AdvTGUI;
import geoactivity.common.items.tools.Adv.Logic.AdvTInventory;
import geoactivity.common.lib.IHasName;
import geoactivity.common.lib.IOpenableGUI;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public abstract class BaseGUITool extends ItemTool implements IHasName, IOpenableGUI
{
	protected static Random random = new Random();
	private String name;

	public BaseGUITool(String name, ToolMaterial material)
	{
		super(0, material, null);

		this.setMaxStackSize(1);
		this.setHasSubtypes(false);
		this.setNoRepair();
		this.name = name;
		this.setUnlocalizedName(name);
		this.setCreativeTab(GeoActivity.tabMain);
		GameRegistry.registerItem(this, name);
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
	
	public static void addDamageInfo(ItemStack stack, int baseDamage, List list)
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