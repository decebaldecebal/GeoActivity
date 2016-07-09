package geoactivity.common.blocks.Machines.AE;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import geoactivity.common.GAMod;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class AERecipes
{
	private static final AERecipes instance = new AERecipes();

	private HashMap<ItemStack, ItemStack[]> smeltingList = new HashMap<ItemStack, ItemStack[]>();
	private HashMap<ItemStack, Integer> timeList = new HashMap<ItemStack, Integer>();

	public static final AERecipes getInstance()
	{
		return instance;
	}

	private AERecipes()
	{
		/* Vanilla */
		//Items
		this.addSmelting(new ItemStack(Items.APPLE), new ItemStack(GAMod.elementContainer, 1, 4), new ItemStack(
				GAMod.elementContainer, 1, 15), new ItemStack(GAMod.elementContainer, 1, 6), 200);
		this.addSmelting(new ItemStack(Items.COAL), new ItemStack(GAMod.elementContainer, 2, 4), null, null, 200);
		this.addSmelting(new ItemStack(Items.DIAMOND), new ItemStack(GAMod.elementContainer, 32, 4), null, null, 1200);
		this.addSmelting(new ItemStack(Items.IRON_INGOT), new ItemStack(GAMod.elementContainer, 2, 17), new ItemStack(
				GAMod.elementContainer, 1, 3), null, 400);
		this.addSmelting(new ItemStack(Items.GOLD_INGOT), new ItemStack(GAMod.elementContainer, 2, 19), new ItemStack(
				GAMod.elementContainer, 1, 3), null, 600);
		this.addSmelting(new ItemStack(Items.STRING), new ItemStack(GAMod.elementContainer, 1, 4), new ItemStack(
				GAMod.elementContainer, 1, 5), null, 100);
		this.addSmelting(new ItemStack(Items.GUNPOWDER), new ItemStack(GAMod.elementContainer, 1, 12), new ItemStack(
				GAMod.elementContainer, 1, 13), new ItemStack(GAMod.elementContainer, 1, 18), 400);
		this.addSmelting(new ItemStack(Items.WHEAT_SEEDS), new ItemStack(GAMod.elementContainer, 1, 15), new ItemStack(
				GAMod.elementContainer, 1, 16), null, 50);
		this.addSmelting(new ItemStack(Items.WHEAT), new ItemStack(GAMod.elementContainer, 1, 9), new ItemStack(
				GAMod.elementContainer, 1, 12), null, 300);
		this.addSmelting(new ItemStack(Items.GOLDEN_APPLE), new ItemStack(GAMod.elementContainer, 2, 15),
				new ItemStack(GAMod.elementContainer, 16, 19), null, 1500);
		this.addSmelting(new ItemStack(Items.REDSTONE), new ItemStack(GAMod.elementContainer, 1, 17), new ItemStack(
				GAMod.elementContainer, 1, 3), new ItemStack(GAMod.elementContainer, 1, 7), 300);
		this.addSmelting(new ItemStack(Items.LEATHER), new ItemStack(GAMod.elementContainer, 1, 4), new ItemStack(
				GAMod.elementContainer, 1, 11), null, 100);
		this.addSmelting(new ItemStack(Items.CLAY_BALL), new ItemStack(GAMod.elementContainer, 1, 10), new ItemStack(
				GAMod.elementContainer, 1, 11), null, 200);
		this.addSmelting(new ItemStack(Items.SLIME_BALL), new ItemStack(GAMod.elementContainer, 2, 11), new ItemStack(
				GAMod.elementContainer, 2, 8), null, 500);
		this.addSmelting(new ItemStack(Items.EGG), new ItemStack(GAMod.elementContainer, 1, 16), new ItemStack(
				GAMod.elementContainer, 1, 12), null, 100);
		this.addSmelting(new ItemStack(Items.GLOWSTONE_DUST), new ItemStack(GAMod.elementContainer, 2, 13),
				new ItemStack(GAMod.elementContainer, 2, 7), new ItemStack(GAMod.elementContainer, 1, 2), 600);
		this.addSmelting(new ItemStack(Items.FISH), new ItemStack(GAMod.elementContainer, 1, 16), new ItemStack(
				GAMod.elementContainer, 1, 18), null, 200);
		this.addSmelting(new ItemStack(Items.DYE), new ItemStack(GAMod.elementContainer, 1, 12), new ItemStack(
				GAMod.elementContainer, 1, 13), null, 50);
		this.addSmelting(new ItemStack(Items.BONE), new ItemStack(GAMod.elementContainer, 1, 9), new ItemStack(
				GAMod.elementContainer, 1, 16), null, 200);
		this.addSmelting(new ItemStack(Items.MELON), new ItemStack(GAMod.elementContainer, 1, 1), new ItemStack(
				GAMod.elementContainer, 1, 6), new ItemStack(GAMod.elementContainer, 1, 5), 400);
		this.addSmelting(new ItemStack(Items.PUMPKIN_SEEDS), new ItemStack(GAMod.elementContainer, 1, 15),
				new ItemStack(GAMod.elementContainer, 1, 16), null, 60);
		this.addSmelting(new ItemStack(Items.MELON_SEEDS), new ItemStack(GAMod.elementContainer, 1, 15), new ItemStack(
				GAMod.elementContainer, 1, 16), null, 60);
		this.addSmelting(new ItemStack(Items.ROTTEN_FLESH), new ItemStack(GAMod.elementContainer, 1, 18), null, null,
				300);
		this.addSmelting(new ItemStack(Items.ENDER_PEARL), new ItemStack(GAMod.elementContainer, 2, 2), new ItemStack(
				GAMod.elementContainer, 2, 3), new ItemStack(GAMod.elementContainer, 4, 11), 800);
		this.addSmelting(new ItemStack(Items.BLAZE_ROD), new ItemStack(GAMod.elementContainer, 4, 1), new ItemStack(
				GAMod.elementContainer, 1, 2), new ItemStack(GAMod.elementContainer, 2, 10), 600);
		this.addSmelting(new ItemStack(Items.GHAST_TEAR), new ItemStack(GAMod.elementContainer, 2, 2), new ItemStack(
				GAMod.elementContainer, 2, 5), new ItemStack(GAMod.elementContainer, 2, 14), 800);
		this.addSmelting(new ItemStack(Items.NETHER_WART), new ItemStack(GAMod.elementContainer, 2, 8), new ItemStack(
				GAMod.elementContainer, 1, 13), null, 200);
		this.addSmelting(new ItemStack(Items.SPIDER_EYE), new ItemStack(GAMod.elementContainer, 1, 5), new ItemStack(
				GAMod.elementContainer, 1, 14), null, 400);
		this.addSmelting(new ItemStack(Items.EMERALD), new ItemStack(GAMod.elementContainer, 12, 10), new ItemStack(
				GAMod.elementContainer, 10, 11), new ItemStack(GAMod.elementContainer, 10, 6), 2000);
		this.addSmelting(new ItemStack(Items.CARROT), new ItemStack(GAMod.elementContainer, 1, 12), new ItemStack(
				GAMod.elementContainer, 1, 15), new ItemStack(GAMod.elementContainer, 1, 8), 200);
		this.addSmelting(new ItemStack(Items.POTATO), new ItemStack(GAMod.elementContainer, 1, 5), new ItemStack(
				GAMod.elementContainer, 1, 18), null, 200);
		this.addSmelting(new ItemStack(Items.GOLDEN_CARROT), new ItemStack(GAMod.elementContainer, 1, 12),
				new ItemStack(GAMod.elementContainer, 1, 19), null, 300);
		this.addSmelting(new ItemStack(Items.NETHER_STAR), new ItemStack(GAMod.elementContainer, 10, 2), new ItemStack(
				GAMod.elementContainer, 10, 3), new ItemStack(GAMod.elementContainer, 12, 7), 5000);
		this.addSmelting(new ItemStack(Items.NETHERBRICK), new ItemStack(GAMod.elementContainer, 1, 12), new ItemStack(
				GAMod.elementContainer, 1, 15), null, 200);
		this.addSmelting(new ItemStack(Items.QUARTZ), new ItemStack(GAMod.elementContainer, 1, 7), new ItemStack(
				GAMod.elementContainer, 1, 19), null, 300);
		this.addSmelting(new ItemStack(Items.FLINT), new ItemStack(GAMod.elementContainer, 1, 18), new ItemStack(
				GAMod.elementContainer, 1, 12), null, 200);
		this.addSmelting(new ItemStack(Items.DYE, 1, 4), new ItemStack(GAMod.elementContainer, 1, 14), new ItemStack(
				GAMod.elementContainer, 2, 11), new ItemStack(GAMod.elementContainer, 1, 9), 600);
		this.addSmelting(new ItemStack(Items.FEATHER), new ItemStack(GAMod.elementContainer, 1, 8), new ItemStack(
				GAMod.elementContainer, 1, 5), null, 200);
		this.addSmelting(new ItemStack(Items.REEDS), new ItemStack(GAMod.elementContainer, 1, 5), new ItemStack(
				GAMod.elementContainer, 1, 6), null, 200);

		//Blocks
		this.addSmelting(new ItemStack(Blocks.GRASS), new ItemStack(GAMod.elementContainer, 1, 5), new ItemStack(
				GAMod.elementContainer, 1, 18), null, 100);
		this.addSmelting(new ItemStack(Blocks.SAPLING), new ItemStack(GAMod.elementContainer, 1, 1), new ItemStack(
				GAMod.elementContainer, 1, 9), null, 200);
		this.addSmelting(new ItemStack(Blocks.GOLD_ORE), new ItemStack(GAMod.elementContainer, 5, 19), null, null, 700);
		this.addSmelting(new ItemStack(Blocks.IRON_ORE), new ItemStack(GAMod.elementContainer, 5, 17), null, null, 500);
		this.addSmelting(new ItemStack(Blocks.LOG, 1, OreDictionary.WILDCARD_VALUE), new ItemStack(
				GAMod.elementContainer, 1, 1), new ItemStack(GAMod.elementContainer, 1, 12), null, 400);
		this.addSmelting(new ItemStack(Blocks.LOG2, 1, OreDictionary.WILDCARD_VALUE), new ItemStack(
				GAMod.elementContainer, 1, 1), new ItemStack(GAMod.elementContainer, 1, 12), null, 400);
		this.addSmelting(new ItemStack(Blocks.LAPIS_ORE), new ItemStack(GAMod.elementContainer, 3, 14), new ItemStack(
				GAMod.elementContainer, 4, 11), new ItemStack(GAMod.elementContainer, 4, 9), 600);
		this.addSmelting(new ItemStack(Blocks.WEB), new ItemStack(GAMod.elementContainer, 2, 4), new ItemStack(
				GAMod.elementContainer, 3, 5), null, 400);
		this.addSmelting(new ItemStack(Blocks.WOOL), new ItemStack(GAMod.elementContainer, 1, 4), null, null, 200);
		this.addSmelting(new ItemStack(Blocks.OBSIDIAN), new ItemStack(GAMod.elementContainer, 2, 3), new ItemStack(
				GAMod.elementContainer, 1, 7), new ItemStack(GAMod.elementContainer, 2, 9), 1000);
		this.addSmelting(new ItemStack(Blocks.REDSTONE_ORE), new ItemStack(GAMod.elementContainer, 2, 7),
				new ItemStack(GAMod.elementContainer, 3, 3), null, 400);
		this.addSmelting(new ItemStack(Blocks.SNOW), new ItemStack(GAMod.elementContainer, 1, 2), new ItemStack(
				GAMod.elementContainer, 1, 5), null, 200);
		this.addSmelting(new ItemStack(Blocks.CACTUS), new ItemStack(GAMod.elementContainer, 2, 13), null, null, 200);
		this.addSmelting(new ItemStack(Blocks.PUMPKIN), new ItemStack(GAMod.elementContainer, 1, 15), new ItemStack(
				GAMod.elementContainer, 1, 9), null, 400);
		this.addSmelting(new ItemStack(Blocks.DRAGON_EGG), new ItemStack(GAMod.elementContainer, 16, 3), new ItemStack(
				GAMod.elementContainer, 16, 11), null, 5000);
		this.addSmelting(new ItemStack(Blocks.QUARTZ_ORE), new ItemStack(GAMod.elementContainer, 3, 7), null, null, 300);

		/* GeoActivity */
		//Items
		this.addSmelting(new ItemStack(GAMod.gemLigniteCoal), new ItemStack(GAMod.elementContainer, 8, 4), null, null,
				400);
		this.addSmelting(new ItemStack(GAMod.gemBituminousCoal), new ItemStack(GAMod.elementContainer, 16, 4), null,
				null, 600);
		this.addSmelting(new ItemStack(GAMod.gemAnthraciteCoal), new ItemStack(GAMod.elementContainer, 24, 4), null,
				null, 800);
		this.addSmelting(new ItemStack(GAMod.graphite), new ItemStack(GAMod.elementContainer, 1, 4), null, null, 100);
	}

	public void addSmelting(ItemStack itemstack, ItemStack result0, ItemStack result1, ItemStack result2, int time)
	{
		ItemStack result[] = new ItemStack[3];
		result[0] = result0;
		result[1] = result1;
		result[2] = result2;

		smeltingList.put(itemstack, result);
		timeList.put(itemstack, time);
	}

	public ItemStack[] getSmeltingResult(ItemStack item)
	{
		Iterator<Entry<ItemStack, ItemStack[]>> iterator = this.smeltingList.entrySet().iterator();
		Entry<ItemStack, ItemStack[]> entry;
		boolean ok = false;

		do
		{
			if(!iterator.hasNext())
			{
				return null;
			}

			entry = iterator.next();

			for(int id : OreDictionary.getOreIDs(item))
			{
				for(int id2 : OreDictionary.getOreIDs(entry.getKey()))
					if(id == id2)
					{
						ok = true;
						break;
					}
				if(ok)
					break;
			}

		} while(!this.compareItemStacks(item, entry.getKey()) && !ok);
		return entry.getValue();
	}

	private boolean compareItemStacks(ItemStack stack1, ItemStack stack2)
	{
		return stack2.getItem() == stack1.getItem()
				&& (stack2.getMetadata() == 32767 || stack2.getMetadata() == stack1.getMetadata());
	}

	public int getTime(ItemStack item)
	{
		Iterator<Entry<ItemStack, Integer>> iterator = this.timeList.entrySet().iterator();
		Entry<ItemStack, Integer> entry;

		do
		{
			if(!iterator.hasNext())
			{
				return 0;
			}
			entry = iterator.next();
		} while(!this.compareItemStacks(item, entry.getKey()));

		return entry.getValue().intValue();
	}

	public int getNumber(ItemStack item)
	{
		if(item == null || item.getItem() == null)
			return 0;

		int number = 0;
		ItemStack[] result = getSmeltingResult(item);

		if(result[0] != null)
			number += result[0].stackSize;
		if(result[1] != null)
			number += result[1].stackSize;
		if(result[2] != null)
			number += result[2].stackSize;

		return number;
	}

	public HashMap<ItemStack, ItemStack[]> getSmeltingList()
	{
		return smeltingList;
	}
}
