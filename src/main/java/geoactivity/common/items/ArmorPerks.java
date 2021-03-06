package geoactivity.common.items;

import java.util.List;

import geoactivity.common.util.GeneralHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ArmorPerks extends BaseItem
{
	public ArmorPerks(String name)
	{
		super(GeneralHelper.appendStringToArray(GeneralHelper.getEnumStrings(EnumArmorPerks.class), name + "_"));
		this.setHasSubtypes(true);
	    this.setMaxDamage(0);
	    this.setMaxStackSize(1);
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean bool)
	{
		if (stack.getMetadata() >= EnumArmorPerks.values().length)
			return ;

		if(!GeneralHelper.isShiftKeyDown())
		{
			list.add(GeneralHelper.shiftForInfo);
			return ;
		}

		switch(EnumArmorPerks.values()[stack.getMetadata()])
		{
			case GENERAL:
				list.add("\u00A7cProvides more protection against all damage taken.");
			break;
			case BLAST:
				list.add("\u00A7cProvides more protection against explosions.");
			break;
			case FIRE:
				list.add("\u00A7cProvides more protection against fire damage.");
			break;
			case MAGIC:
				list.add("\u00A7cProvides more protection against magic damage.");
			break;
			case PROJECTILE:
				list.add("\u00A7cProvides more protection against projectiles.");
			break;
			case NOFALL:
				list.add("\u00A7cTake no damage from falling.");
				list.add("\u00A77Works on:");
				list.add("\u00A77 - pants");
				list.add("\u00A77 - boots");
			break;
			case RESPIRATION:
				list.add("\u00A7cBreathe underwater.");
				list.add("\u00A77Works on:");
				list.add("\u00A77 - helmets");
			break;
			default:
				list.add("\u00A7aJust a blank armor perk");
		}
	}

	@SuppressWarnings("all")
	@Override
	@SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tabs, List list)
    {
        for(int i = 0;i < EnumArmorPerks.values().length;i++)
        {
        	list.add(new ItemStack(item, 1, i));
        }
    }

	public static enum EnumArmorPerks
	{
		GENERAL(0, "general"),
		BLAST(1, "blast"),
		FIRE(2, "fire"),
		MAGIC(3, "magic"),
		PROJECTILE(4, "projectile"),
		NOFALL(5, "nofall"),
		RESPIRATION(6, "respiration");

		int meta;
		String name;

		private EnumArmorPerks(int meta, String name)
		{
			this.meta = meta;
			this.name = name;
		}

		public int getMetadata()
		{
			return this.meta;
		}

		@Override
		public String toString()
		{
			return this.name;
		}
	}
}
