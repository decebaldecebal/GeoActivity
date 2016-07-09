package geoactivity.common.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;

import org.lwjgl.input.Keyboard;

import geoactivity.common.GAMod;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class GeneralHelper
{
	public static String shiftForInfo = "\u00A77Hold \u00A7aShift \u00A77for more info.";

	public static boolean isShiftKeyDown()
	{
		return (Keyboard.isKeyDown(42)) || (Keyboard.isKeyDown(54));
	}

	public static String[] appendStringToArray(String[] array, String append)
	{
		for(int i = 0;i < array.length;i++)
			array[i] = append + array[i];
		return array;
	}

	public static <E extends Enum<E>> String[] getEnumStrings(Class<E> clazz)
	{
		List<String> names = new ArrayList<String>();

		for(E en : EnumSet.allOf(clazz))
			names.add(en.toString());

		return names.toArray(new String[names.size()]);
	}

	public static String getEnergyUnits(int number)
	{
		float temp = number / 1000f;

		if (temp >= 1000)
		{
			temp /= 1000f;

			if (temp < 100)
				return String.format("%.1f", temp) + "M";
			else
				return (int)temp + "M";
		}

		if (temp < 100)
			return String.format("%.1f", temp) + "K";
		else
			return (int)temp + "K";
	}

	public enum GuiColors
	{
		BLACK(0), BLUE(1), GREEN(2), CYAN(3), RED(4), PURPLE(5), ORANGE(6), LIGHTGRAY(7), GRAY(8), LIGHTBLUE(9),
		LIME(10), TURQUISE(11), PINK(12), MAGENTA(13), YELLOW(14), WHITE(15);

		private int number;

		GuiColors(int number)
		{
			this.number = number;
		}

		@Override
		public String toString()
		{
			return "\u00a7" + Integer.toHexString(number);
		}
	}

	public static Collection<ItemStack> getCraftingMachineFuels()
	{
		Collection<ItemStack> fuels = new ArrayList<ItemStack>();

		fuels.add(new ItemStack(Blocks.COAL_BLOCK));
		fuels.add(new ItemStack(GAMod.gemLigniteCoal));
		fuels.add(new ItemStack(GAMod.gemBituminousCoal));
		fuels.add(new ItemStack(GAMod.gemAnthraciteCoal));


		return fuels;
	}

	public static Collection<ItemStack> getCoalRefinerFuels()
	{
		Collection<ItemStack> fuels = new ArrayList<ItemStack>();

		fuels.add(new ItemStack(Items.COAL));
		fuels.add(new ItemStack(GAMod.gemLigniteCoal));
		fuels.add(new ItemStack(GAMod.gemBituminousCoal));
		fuels.add(new ItemStack(GAMod.gemAnthraciteCoal));


		return fuels;
	}
}
