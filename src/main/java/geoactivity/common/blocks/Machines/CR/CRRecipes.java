package geoactivity.common.blocks.Machines.CR;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import geoactivity.common.GAMod;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CRRecipes
{
	private static final CRRecipes instance = new CRRecipes();

    private Map<ItemStack, ItemStack> smeltingList = new HashMap<ItemStack, ItemStack>();
    private Map<ItemStack, Float> experienceList = new HashMap<ItemStack, Float>();

	public static final CRRecipes getInstance()
	{
		return instance;
	}

	public CRRecipes()
	{
		this.addSmelting(Items.COAL, new ItemStack(GAMod.graphite), 0.2F);
		this.addSmelting(GAMod.gemLigniteCoal, new ItemStack(GAMod.graphite, 8), 0.2F);
		this.addSmelting(GAMod.gemBituminousCoal, new ItemStack(GAMod.graphite, 16), 0.2F);
		this.addSmelting(GAMod.gemAnthraciteCoal, new ItemStack(GAMod.graphite, 24), 0.2F);

		this.addSmelting(Blocks.COAL_ORE, new ItemStack(GAMod.graphite, 4), 0.2F);
		this.addSmelting(GAMod.oreLignite, new ItemStack(GAMod.gemLigniteCoal, 2), 0.3F);
		this.addSmelting(GAMod.oreBituminous, new ItemStack(GAMod.gemBituminousCoal, 2), 0.4F);
		this.addSmelting(GAMod.oreAnthracite, new ItemStack(GAMod.gemAnthraciteCoal, 2), 0.5F);
	}

	public void addSmelting(Block block, ItemStack result, float experience)
	{
		this.addSmelting(Item.getItemFromBlock(block), result, experience);
	}

	public void addSmelting(Item item, ItemStack result, float experience)
	{
		this.addSmelting(new ItemStack(item), result, experience);
	}

	public void addSmelting(ItemStack stack, ItemStack result, float experience)
	{
		smeltingList.put(stack, result);
		experienceList.put(result, experience);
	}

    public ItemStack getSmeltingResult(ItemStack stack)
    {
        Iterator<Entry<ItemStack, ItemStack>> iterator = this.smeltingList.entrySet().iterator();
        Entry<ItemStack, ItemStack> entry;

        do
        {
            if (!iterator.hasNext())
            {
                return null;
            }

            entry = iterator.next();
        }
        while (!this.compareItemStacks(stack, entry.getKey()));

        return entry.getValue();
    }

    private boolean compareItemStacks(ItemStack stack1, ItemStack stack2)
    {
        return stack2.getItem() == stack1.getItem() && (stack2.getMetadata() == 32767 || stack2.getMetadata() == stack1.getMetadata());
    }

    public float getExperience(ItemStack stack)
    {
        Iterator<Entry<ItemStack, Float>> iterator = this.experienceList.entrySet().iterator();
        Entry<ItemStack, Float> entry;

        do
        {
            if (!iterator.hasNext())
            {
                return 0.0F;
            }

            entry = iterator.next();
        }
        while (!this.compareItemStacks(stack, entry.getKey()));

        return entry.getValue().floatValue();
    }

	public Map<ItemStack, ItemStack> getSmeltingList()
	{
		return smeltingList;
	}
}
