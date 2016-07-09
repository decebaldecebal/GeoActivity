package geoactivity.common.blocks.Machines.ChM;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import geoactivity.common.GAMod;
import geoactivity.common.items.ArmorPerks.EnumArmorPerks;
import geoactivity.common.items.ToolPerks.EnumToolPerks;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class ChMCrafting
{
	public static final ChMCrafting instance = new ChMCrafting();

	private HashMap<ItemStack[], ItemStack[]> smeltingList = new HashMap<ItemStack[], ItemStack[]>();

	public static final ChMCrafting getInstance()
	{
		return instance;
	}

	public ChMCrafting()
	{
		//Tool Perks
		addSmelting(new ItemStack(GAMod.tool_perks, 1, 0), new ItemStack(Items.DYE, 1, 4), new ItemStack(
				GAMod.tool_perks, 1, EnumToolPerks.EXPERIENCE.getMetadata()), new ItemStack(GAMod.elementContainer, 2,
				19), new ItemStack(GAMod.elementContainer, 4, 14), new ItemStack(GAMod.elementContainer, 2, 2), null);

		addSmelting(new ItemStack(GAMod.tool_perks, 1, 0), new ItemStack(Items.COAL), new ItemStack(GAMod.tool_perks,
				1, EnumToolPerks.SMELTING.getMetadata()), new ItemStack(GAMod.elementContainer, 4, 13), new ItemStack(
				GAMod.elementContainer, 2, 6), new ItemStack(GAMod.elementContainer, 2, 9), null);

		addSmelting(new ItemStack(GAMod.tool_perks, 1, 0), new ItemStack(Items.ENDER_EYE), new ItemStack(
				GAMod.tool_perks, 1, EnumToolPerks.FORTUNE.getMetadata()),
				new ItemStack(GAMod.elementContainer, 4, 19), new ItemStack(GAMod.elementContainer, 4, 11), null, null);

		addSmelting(new ItemStack(GAMod.tool_perks, 1, 0), new ItemStack(Items.IRON_INGOT), new ItemStack(
				GAMod.tool_perks, 1, EnumToolPerks.SILKTOUCH.getMetadata()),
				new ItemStack(GAMod.elementContainer, 4, 3), new ItemStack(GAMod.elementContainer, 2, 8),
				new ItemStack(GAMod.elementContainer, 2, 10), new ItemStack(GAMod.elementContainer, 2, 16));

		addSmelting(new ItemStack(GAMod.tool_perks, 1, 1), new ItemStack(Items.DIAMOND), new ItemStack(
				GAMod.tool_perks, 1, EnumToolPerks.RADIUS.getMetadata()), new ItemStack(GAMod.elementContainer, 4, 1),
				new ItemStack(GAMod.elementContainer, 4, 4), new ItemStack(GAMod.elementContainer, 4, 5),
				new ItemStack(GAMod.elementContainer, 4, 7));

		addSmelting(new ItemStack(GAMod.tool_perks, 1, 2), new ItemStack(Items.DIAMOND), new ItemStack(
				GAMod.tool_perks, 1, EnumToolPerks.CAPITATOR.getMetadata()), new ItemStack(GAMod.elementContainer, 4,
				12), new ItemStack(GAMod.elementContainer, 4, 15), new ItemStack(GAMod.elementContainer, 4, 17),
				new ItemStack(GAMod.elementContainer, 4, 9));

		addSmelting(new ItemStack(GAMod.tool_perks, 1, 0), new ItemStack(Items.ENDER_PEARL), new ItemStack(
				GAMod.tool_perks, 1, EnumToolPerks.NODROPS.getMetadata()), new ItemStack(GAMod.elementContainer, 4, 4),
				new ItemStack(GAMod.elementContainer, 4, 12), new ItemStack(GAMod.elementContainer, 4, 13), null);

		addSmelting(new ItemStack(Items.EMERALD), new ItemStack(Items.ENDER_EYE), new ItemStack(GAMod.tool_perks, 1,
				EnumToolPerks.DROPTP.getMetadata()), new ItemStack(GAMod.elementContainer, 4, 1), new ItemStack(
				GAMod.elementContainer, 4, 3), new ItemStack(GAMod.elementContainer, 4, 2), new ItemStack(
				GAMod.elementContainer, 4, 6));

		//Armor Perks
		addSmelting(new ItemStack(GAMod.tool_perks, 1, 0), new ItemStack(Items.FEATHER), new ItemStack(
				GAMod.armor_perks, 1, EnumArmorPerks.NOFALL.getMetadata()),
				new ItemStack(GAMod.elementContainer, 4, 2), new ItemStack(GAMod.elementContainer, 4, 2), null, null);

		addSmelting(new ItemStack(GAMod.tool_perks, 1, 0), new ItemStack(Items.DYE), new ItemStack(GAMod.armor_perks,
				1, EnumArmorPerks.RESPIRATION.getMetadata()), new ItemStack(GAMod.elementContainer, 4, 6),
				new ItemStack(GAMod.elementContainer, 4, 1), null, null);

		//Others
		addSmelting(new ItemStack(GAMod.carbonstick, 1, 0), new ItemStack(Items.BLAZE_ROD), new ItemStack(
				GAMod.carbonOreCutter, 2, 0), new ItemStack(GAMod.elementContainer, 4, 4), new ItemStack(
				GAMod.elementContainer, 4, 4), new ItemStack(GAMod.elementContainer, 4, 14), new ItemStack(
				GAMod.elementContainer, 4, 10));

		addSmelting(new ItemStack(Items.GOLD_INGOT), new ItemStack(Items.GOLD_INGOT),
				new ItemStack(Items.BLAZE_ROD, 4), new ItemStack(GAMod.elementContainer, 4, 5), new ItemStack(
						GAMod.elementContainer, 4, 5), null, null);
		addSmelting(new ItemStack(Items.BLAZE_ROD), new ItemStack(Items.BLAZE_ROD), new ItemStack(Items.EMERALD),
				new ItemStack(GAMod.elementContainer, 4, 11), new ItemStack(GAMod.elementContainer, 4, 11),
				new ItemStack(GAMod.elementContainer, 4, 11), new ItemStack(GAMod.elementContainer, 4, 11));
		addSmelting(new ItemStack(Items.QUARTZ), new ItemStack(Items.GLOWSTONE_DUST), new ItemStack(Items.ENDER_PEARL,
				2), new ItemStack(GAMod.elementContainer, 4, 3), new ItemStack(GAMod.elementContainer, 4, 3),
				new ItemStack(GAMod.elementContainer, 4, 11), new ItemStack(GAMod.elementContainer, 4, 11));
		addSmelting(new ItemStack(Items.QUARTZ), new ItemStack(Items.GOLD_NUGGET), new ItemStack(Items.GHAST_TEAR, 2),
				new ItemStack(GAMod.elementContainer, 4, 2), new ItemStack(GAMod.elementContainer, 4, 5),
				new ItemStack(GAMod.elementContainer, 4, 14), new ItemStack(GAMod.elementContainer, 4, 14));
		addSmelting(new ItemStack(Items.LEATHER), new ItemStack(Items.BLAZE_POWDER), new ItemStack(Items.GUNPOWDER, 6),
				new ItemStack(GAMod.elementContainer, 4, 12), new ItemStack(GAMod.elementContainer, 4, 13),
				new ItemStack(GAMod.elementContainer, 4, 13), null);
		addSmelting(new ItemStack(Items.CLAY_BALL), new ItemStack(Items.CLAY_BALL), new ItemStack(Items.LEATHER, 6),
				new ItemStack(GAMod.elementContainer, 4, 4), new ItemStack(GAMod.elementContainer, 4, 4), null, null);
		addSmelting(new ItemStack(Items.ENDER_PEARL), new ItemStack(Items.GLOWSTONE_DUST), new ItemStack(
				Items.SLIME_BALL, 2), new ItemStack(GAMod.elementContainer, 4, 11), new ItemStack(
				GAMod.elementContainer, 4, 11), null, null);
		addSmelting(new ItemStack(Items.STRING), new ItemStack(Items.STRING), new ItemStack(Items.SPIDER_EYE, 2),
				new ItemStack(GAMod.elementContainer, 4, 5), new ItemStack(GAMod.elementContainer, 4, 14), null, null);
	}

	public void addSmelting(ItemStack stack1, ItemStack stack2, ItemStack result0, ItemStack ingred0,
			ItemStack ingred1, ItemStack ingred2, ItemStack ingred3)
	{
		ItemStack result[] = new ItemStack[5];
		result[0] = result0;
		result[1] = ingred0;
		result[2] = ingred1;
		result[3] = ingred2;
		result[4] = ingred3;

		if(stack1 == null)
			stack1 = new ItemStack(Blocks.AIR);
		if(stack2 == null)
			stack2 = new ItemStack(Blocks.AIR);

		ItemStack input[] = new ItemStack[2];
		input[0] = stack1;
		input[1] = stack2;

		smeltingList.put(input, result);
	}

	public ItemStack[] getItems(ItemStack item1, ItemStack item2)
	{
		ItemStack input[] = new ItemStack[2];
		input[0] = item1;
		input[1] = item2;

		Iterator<Entry<ItemStack[], ItemStack[]>> iterator = this.smeltingList.entrySet().iterator();
		Entry<ItemStack[], ItemStack[]> entry;

		do
		{
			if(!iterator.hasNext())
			{
				return null;
			}

			entry = iterator.next();
		} while(!this.compareItemStacks(input, entry.getKey()));

		return entry.getValue();
	}

	private boolean compareItemStacks(ItemStack[] input1, ItemStack[] input2)
	{
		return input2[0].getItem() == input1[0].getItem()
				&& (input2[0].getItemDamage() == 32767 || input2[0].getItemDamage() == input1[0].getItemDamage())
				&& input2[1].getItem() == input1[1].getItem()
				&& (input2[1].getItemDamage() == 32767 || input2[1].getItemDamage() == input1[1].getItemDamage());
	}

	public HashMap<ItemStack[], ItemStack[]> getMetaSmeltingList()
	{
		return smeltingList;
	}
}
