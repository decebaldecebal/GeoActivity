package geoactivity.common.itemblocks;

import java.util.List;

import geoactivity.common.GAMod;
import geoactivity.common.util.GeneralHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class MultiItemBlock extends ItemBlock
{
	public MultiItemBlock(Block block)
	{
		super(block);
	}

	@SuppressWarnings("all")
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean bool)
	{
		if(!GeneralHelper.isShiftKeyDown())
		{
			list.add(GeneralHelper.shiftForInfo);
			return ;
		}

		if(block == GAMod.advancedcoalrefiner)
			list.add("\u00A77Multiblock: 2x2x2");
		else if(block == GAMod.advancedorerefiner)
			list.add("\u00A77Complicated multiblock. Check JEI description page.");
		else if (block == GAMod.atomextractor)
		{
			list.add("\u00A77Upgrade the speed by placing Advanced Hardened Bricks");
			list.add("\u00A77adjacent to it.(up to a maximum of 4 bricks)");
		}
		else if (block == GAMod.chemistrymachine)
		{
			list.add("\u00A77Multiblock.Requires 4 Advanced Hardened Bricks and item frames.");
			list.add("\u00A77Check JEI description page.");
		}
		else if(block == GAMod.thermicgenerator)
		{
			list.add("\u00A77Requires Thermal Hardened Bricks to be adjacent to it.");
			list.add("\u00A77Produces 20RF/t per Thermal Hardened Brick.");
			list.add("\u00A77Only outputs power through the bottom.");
		}
		else if(block == GAMod.toolcharger)
			list.add("\u00A77Used to charge RF powered Redstone Tools.");
		else if(block == GAMod.geothermalgenerator)
		{
			list.add("\u00A77Generates RF from lava pools.Placed in a lava pool, it will scan all lava blocks");
			list.add("\u00A77recursively to a distance of up to 25 meters from the block every minute.");
			list.add("\u00A77For each lava block found, it will generate 4 RF/t.Output to bottom/top only.");
			list.add("\u00A77Works better at lower altitudes.");
			list.add("\u00A77It has a 40% chance to turn lava blocks in a 3x3x3 cube centered around the block");
			list.add("\u00A77into obsidian, so it will eventually stop working.");
			list.add("\u00A77Use the Thermic Melter to solve this problem.");
		}
		else if(block == GAMod.thermicmelter)
		{
			list.add("\u00A77Turns obsidian in a 5x5x5 cube centered around the block slowly into lava.");
			list.add("\u00A77Uses 10RF/t passively and 50k RF per obsidian block.");
			list.add("\u00A77Transfer (in): 500 RF/t");
		}
	}
}
