package geoactivity.common.blocks.Machines.CrM;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import geoactivity.common.GAMod;
import geoactivity.common.blocks.Machines.CrM.recipes.CrMShapedRecipe;
import geoactivity.common.blocks.Machines.CrM.recipes.CrMShapelessRecipe;
import geoactivity.common.items.ArmorPerks.EnumArmorPerks;
import geoactivity.common.items.MachinePerks.EnumMachinePerks;
import geoactivity.common.items.ToolPerks.EnumToolPerks;
import geoactivity.common.util.BaseElectricTool;
import geoactivity.common.util.PerkHelper;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public class CrMCrafting
{
	public static final CrMCrafting instance = new CrMCrafting();
	private List<IRecipe> recipes = new ArrayList<IRecipe>();

	public static final CrMCrafting getInstance()
	{
		return instance;
	}

	public CrMCrafting()
	{
		/* Perks */

		addRecipe(new ItemStack(GAMod.tool_perks, 1, EnumToolPerks.BLANK.getMetadata()), new Object[] {" Z ", "XSX",
				" Y ", 'X', Items.REDSTONE, 'S', GAMod.carbonfiber, 'Z', Items.GLOWSTONE_DUST, 'Y', Items.QUARTZ});

		//Tool Perks
		addShapelessRecipe(new ItemStack(GAMod.tool_perks, 1, EnumToolPerks.SPEED.getMetadata()), new Object[] {
				GAMod.tool_perks, Items.ENDER_PEARL, Items.GOLD_INGOT});
		addShapelessRecipe(new ItemStack(GAMod.tool_perks, 1, EnumToolPerks.EFFICIENCY.getMetadata()), new Object[] {
				GAMod.tool_perks, Items.MAGMA_CREAM, Items.IRON_INGOT, Items.IRON_INGOT});
		addShapelessRecipe(new ItemStack(GAMod.tool_perks, 1, EnumToolPerks.DAMAGE.getMetadata()), new Object[] {
				GAMod.tool_perks, Blocks.REDSTONE_BLOCK, Blocks.QUARTZ_BLOCK});
		addShapelessRecipe(new ItemStack(GAMod.tool_perks, 1, EnumToolPerks.BYPASSARMOR.getMetadata()), new Object[] {
				GAMod.tool_perks, Items.DIAMOND, Items.DIAMOND, Items.BLAZE_ROD});

		//Machine Perks
		addShapelessRecipe(new ItemStack(GAMod.machine_perks, 1, EnumMachinePerks.SPEED.getMetadata()), new Object[] {
				new ItemStack(GAMod.tool_perks, 1, 0), Items.ENDER_PEARL, Items.GOLD_INGOT, Items.GOLD_INGOT,
				Items.GOLD_INGOT, Items.GOLD_INGOT});
		addShapelessRecipe(new ItemStack(GAMod.machine_perks, 1, EnumMachinePerks.EFFICIENCY.getMetadata()),
				new Object[] {new ItemStack(GAMod.tool_perks, 1, 0), Items.MAGMA_CREAM, Blocks.IRON_BLOCK});

		//Armor Perks
		addShapelessRecipe(new ItemStack(GAMod.armor_perks, 1, EnumArmorPerks.GENERAL.getMetadata()), new Object[] {
				GAMod.tool_perks, Blocks.OBSIDIAN, Items.DIAMOND});
		addShapelessRecipe(new ItemStack(GAMod.armor_perks, 1, EnumArmorPerks.BLAST.getMetadata()), new Object[] {
				GAMod.tool_perks, Blocks.OBSIDIAN, Blocks.OBSIDIAN, Items.GOLD_INGOT, Items.GOLD_INGOT});
		addShapelessRecipe(new ItemStack(GAMod.armor_perks, 1, EnumArmorPerks.FIRE.getMetadata()), new Object[] {
				GAMod.tool_perks, Blocks.NETHERRACK, Items.FIRE_CHARGE, Items.BLAZE_ROD});
		addShapelessRecipe(new ItemStack(GAMod.armor_perks, 1, EnumArmorPerks.MAGIC.getMetadata()), new Object[] {
				GAMod.tool_perks, Items.GHAST_TEAR, Items.MAGMA_CREAM, Items.FERMENTED_SPIDER_EYE});
		addShapelessRecipe(new ItemStack(GAMod.armor_perks, 1, EnumArmorPerks.PROJECTILE.getMetadata()), new Object[] {
				GAMod.tool_perks, Blocks.BRICK_BLOCK, Blocks.BRICK_BLOCK, Items.GOLD_INGOT, Items.GOLD_INGOT});

		/* Tools */

		//Tier II
		addRecipe(new ItemStack(GAMod.advancedSword, 1, 998), new Object[] {" Y ", " Y ", " X ", 'X',
				GAMod.carbonstick, 'Y', new ItemStack(GAMod.preciousAlloy, 1, 0)}, PerkHelper.getAdvancedSwordPerks());
		addRecipe(new ItemStack(GAMod.advancedPickaxe, 1, 998), new Object[] {"YYY", " X ", " X ", 'X',
				GAMod.carbonstick, 'Y', new ItemStack(GAMod.preciousAlloy, 1, 0)}, PerkHelper.getAdvancedPickaxePerks());
		addRecipe(new ItemStack(GAMod.advancedAxe, 1, 998), new Object[] {"YY ", "YX ", " X ", 'X', GAMod.carbonstick,
				'Y', new ItemStack(GAMod.preciousAlloy, 1, 0)}, PerkHelper.getAdvancedAxePerks());
		addRecipe(new ItemStack(GAMod.advancedShovel, 1, 998), new Object[] {" Y ", " X ", " X ", 'X',
				GAMod.carbonstick, 'Y', new ItemStack(GAMod.preciousAlloy, 1, 0)}, PerkHelper.getAdvancedShovelPerks());

		//Tier III
		addRecipe(new ItemStack(GAMod.redstoneMiner, 1, 998), new Object[] {"RYR", " X ", " X ", 'X',
				GAMod.carbonstick, 'Y', new ItemStack(GAMod.preciousAlloy, 1, 1), 'R', Blocks.REDSTONE_BLOCK});
		addRecipe(new ItemStack(GAMod.redstoneBattleAxe, 1, 998), new Object[] {"RYR", "RXR", " X ", 'X',
				GAMod.carbonstick, 'Y', new ItemStack(GAMod.preciousAlloy, 1, 1), 'R', Blocks.REDSTONE_BLOCK});
		addRecipe(new ItemStack(GAMod.redstoneBattleMiner, 1, 1998), new Object[] {"RYR", "RXR", " X ", 'X',
				GAMod.carbonstick, 'Y', new ItemStack(GAMod.preciousAlloy, 1, 2), 'R', Blocks.REDSTONE_BLOCK});
		addRecipe(BaseElectricTool.getUnchargedItem(GAMod.aredstoneBattleMiner), new Object[] {"YYY", "RXR", " X ",
				'X', GAMod.carbonstick, 'Y', new ItemStack(GAMod.preciousAlloy, 1, 2), 'R', Blocks.REDSTONE_BLOCK});

		/* Armor */

		//Tier II
		addRecipe(new ItemStack(GAMod.advancedHelmet, 1, GAMod.advancedHelmet.getMaxDamage() - 2), new Object[] {"XXX",
				"X X", 'X', new ItemStack(GAMod.preciousAlloy, 1, 0)}, PerkHelper.getAdvancedHelmetPerks());
		addRecipe(new ItemStack(GAMod.advancedChest, 1, GAMod.advancedChest.getMaxDamage() - 2), new Object[] {"X X",
				"XXX", "XXX", 'X', new ItemStack(GAMod.preciousAlloy, 1, 0)}, PerkHelper.getAdvancedChestplatePerks());
		addRecipe(new ItemStack(GAMod.advancedPants, 1, GAMod.advancedPants.getMaxDamage() - 2), new Object[] {"X X",
				"X X", "XXX", 'X', new ItemStack(GAMod.preciousAlloy, 1, 0)}, PerkHelper.getAdvancedPantsPerks());
		addRecipe(new ItemStack(GAMod.advancedBoots, 1, GAMod.advancedBoots.getMaxDamage() - 2), new Object[] {"X X",
				"X X", 'X', new ItemStack(GAMod.preciousAlloy, 1, 0)}, PerkHelper.getAdvancedBootsPerks());

		/* Shapeless Recipes */

		addShapelessRecipe(new ItemStack(GAMod.advancedSword, 1, 998), new Object[] {new ItemStack(GAMod.advancedSword,
				1, OreDictionary.WILDCARD_VALUE)}, PerkHelper.getAdvancedSwordPerks());
		addShapelessRecipe(new ItemStack(GAMod.advancedPickaxe, 1, 998), new Object[] {new ItemStack(
				GAMod.advancedPickaxe, 1, OreDictionary.WILDCARD_VALUE)}, PerkHelper.getAdvancedPickaxePerks());
		addShapelessRecipe(new ItemStack(GAMod.advancedAxe, 1, 998), new Object[] {new ItemStack(GAMod.advancedAxe, 1,
				OreDictionary.WILDCARD_VALUE)}, PerkHelper.getAdvancedAxePerks());
		addShapelessRecipe(new ItemStack(GAMod.advancedShovel, 1, 998), new Object[] {new ItemStack(
				GAMod.advancedShovel, 1, OreDictionary.WILDCARD_VALUE)}, PerkHelper.getAdvancedShovelPerks());

		addShapelessRecipe(new ItemStack(GAMod.advancedHelmet, 1, GAMod.advancedHelmet.getMaxDamage() - 2),
				new Object[] {new ItemStack(GAMod.advancedHelmet, 1, OreDictionary.WILDCARD_VALUE)}, PerkHelper.getAdvancedHelmetPerks());
		addShapelessRecipe(new ItemStack(GAMod.advancedChest, 1, GAMod.advancedChest.getMaxDamage() - 2),
				new Object[] {new ItemStack(GAMod.advancedChest, 1, OreDictionary.WILDCARD_VALUE)}, PerkHelper.getAdvancedChestplatePerks());
		addShapelessRecipe(new ItemStack(GAMod.advancedPants, 1, GAMod.advancedPants.getMaxDamage() - 2),
				new Object[] {new ItemStack(GAMod.advancedPants, 1, OreDictionary.WILDCARD_VALUE)}, PerkHelper.getAdvancedPantsPerks());
		addShapelessRecipe(new ItemStack(GAMod.advancedBoots, 1, GAMod.advancedBoots.getMaxDamage() - 2),
				new Object[] {new ItemStack(GAMod.advancedBoots, 1, OreDictionary.WILDCARD_VALUE)}, PerkHelper.getAdvancedBootsPerks());
	}

	void addRecipe(ItemStack stack, Object obj[])
	{
		this.addRecipe(stack, obj, null);
	}

	void addRecipe(ItemStack stack, Object obj[], List<ItemStack> compatiblePerks)
	{
		String s = "";
		int i = 0;
		int j = 0;
		int k = 0;

		if(obj[i] instanceof String[])
		{
			String as[] = (String[]) obj[i++];

			for(int l = 0;l < as.length;l++)
			{
				String s2 = as[l];
				k++;
				j = s2.length();
				s = (new StringBuilder()).append(s).append(s2).toString();
			}
		}
		else
			while(obj[i] instanceof String)
			{
				String s1 = (String) obj[i++];
				k++;
				j = s1.length();
				s = (new StringBuilder()).append(s).append(s1).toString();
			}

		HashMap<Character, ItemStack> hashmap = new HashMap<Character, ItemStack>();

		for(;i < obj.length;i += 2)
		{
			Character character = (Character) obj[i];
			ItemStack ItemStack = null;

			if(obj[i + 1] instanceof Item)
				ItemStack = new ItemStack((Item) obj[i + 1]);
			else if(obj[i + 1] instanceof Block)
				ItemStack = new ItemStack((Block) obj[i + 1], 1, -1);
			else if(obj[i + 1] instanceof ItemStack)
				ItemStack = (ItemStack) obj[i + 1];

			hashmap.put(character, ItemStack);
		}

		ItemStack aItemStack[] = new ItemStack[j * k];

		for(int i1 = 0;i1 < j * k;i1++)
		{
			char c = s.charAt(i1);

			if(hashmap.containsKey(Character.valueOf(c)))
				aItemStack[i1] = hashmap.get(Character.valueOf(c)).copy();
			else
				aItemStack[i1] = null;
		}

		recipes.add(new CrMShapedRecipe(j, k, aItemStack, stack, compatiblePerks));
	}

	public void addShapelessRecipe(ItemStack stack, Object obj[])
	{
		this.addShapelessRecipe(stack, obj, null);
	}

	public void addShapelessRecipe(ItemStack stack, Object obj[], List<ItemStack> compatiblePerks)
	{
		ArrayList<ItemStack> arraylist = new ArrayList<ItemStack>();
		Object[] aobject = obj;
		int i = obj.length;

		for(int j = 0;j < i;++j)
		{
			Object object1 = aobject[j];

			if(object1 instanceof ItemStack)
			{
				arraylist.add(((ItemStack) object1).copy());
			}
			else if(object1 instanceof Item)
			{
				arraylist.add(new ItemStack((Item) object1));
			}
			else
			{
				if(!(object1 instanceof Block))
				{
					throw new RuntimeException("Invalid shapeless recipy!");
				}

				arraylist.add(new ItemStack((Block) object1));
			}
		}

		this.recipes.add(new CrMShapelessRecipe(stack, arraylist, compatiblePerks));
	}

	public ItemStack findMatchingRecipe(InventoryCrafting craftingInv, World world)
	{
		int i = 0;
		ItemStack itemstack = null;
		ItemStack itemstack1 = null;
		int j;

		for(j = 0;j < craftingInv.getSizeInventory();++j)
		{
			ItemStack itemstack2 = craftingInv.getStackInSlot(j);

			if(itemstack2 != null)
			{
				if(i == 0)
				{
					itemstack = itemstack2;
				}

				if(i == 1)
				{
					itemstack1 = itemstack2;
				}

				++i;
			}
		}

		if(i == 2 && itemstack.getItem() == itemstack1.getItem() && itemstack.stackSize == 1
				&& itemstack1.stackSize == 1 && itemstack.getItem().isRepairable())
		{
			Item item = itemstack.getItem();
			int j1 = item.getMaxDamage() - itemstack.getItemDamage();
			int k = item.getMaxDamage() - itemstack1.getItemDamage();
			int l = j1 + k + item.getMaxDamage() * 5 / 100;
			int i1 = item.getMaxDamage() - l;

			if(i1 < 0)
			{
				i1 = 0;
			}

			return new ItemStack(itemstack.getItem(), 1, i1);
		}
		else
		{
			for(j = 0;j < this.recipes.size();++j)
			{
				IRecipe irecipe = this.recipes.get(j);

				if(irecipe.matches(craftingInv, world))
				{
					return irecipe.getCraftingResult(craftingInv);
				}
			}

			return null;
		}
	}

	public List<IRecipe> getRecipeList()
	{
		return recipes;
	}
}
