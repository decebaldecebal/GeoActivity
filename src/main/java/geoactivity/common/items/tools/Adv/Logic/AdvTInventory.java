package geoactivity.common.items.tools.Adv.Logic;

import geoactivity.common.GAMod;
import geoactivity.common.items.ToolPerks.EnumToolPerks;
import geoactivity.common.util.BaseInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class AdvTInventory extends BaseInventory
{
	public AdvTInventory(ItemStack stack, EntityPlayer player)
	{
		super(stack, player, 2);
	}

	@Override
	public String getName()
	{
		return "AdvancedTool";
	}

	public void setCharge()
	{
		int fuel = 0;
		if(inventory[0] != null)
		{
			int basefuel = 0;
			if(current.getTagCompound().getByte("efficiency") == 1)
				basefuel = 10;

			Item item = inventory[0].getItem();
			if(item == GAMod.gemLigniteCoal)
				fuel = 100 + basefuel * 2;
			else if(item == GAMod.gemBituminousCoal)
				fuel = 200 + basefuel * 3;
			else if(item == GAMod.gemAnthraciteCoal)
				fuel = 300 + basefuel * 4;
		}
		while(fuel != 0 && inventory[0] != null)
		{
			int damage = current.getItemDamage();
			if(damage - fuel > 0)
			{
				current.setItemDamage(damage - fuel);
				decrStackSize(0, 1);
			}
			else
				fuel = 0;
		}
		if(current.getItemDamage() <= 998)
		{
			NBTTagCompound tag = current.getTagCompound();
			tag.setBoolean("destroyed", false);
			current.setTagCompound(tag);
		}
		writeToNBT(current.getTagCompound());
	}

	@Override
	public void readFromNBT(NBTTagCompound myCompound)
	{
		super.readFromNBT(myCompound);

		if(myCompound.getByte("speed") == 1)
			inventory[1] = new ItemStack(GAMod.tool_perks, 1, EnumToolPerks.SPEED.getMetadata());
		else if(myCompound.getByte("efficiency") == 1)
			inventory[1] = new ItemStack(GAMod.tool_perks, 1, EnumToolPerks.EFFICIENCY.getMetadata());
		else if(myCompound.getByte("damage") == 1)
			inventory[1] = new ItemStack(GAMod.tool_perks, 1, EnumToolPerks.DAMAGE.getMetadata());
		else if(myCompound.getBoolean("experience"))
			inventory[1] = new ItemStack(GAMod.tool_perks, 1, EnumToolPerks.EXPERIENCE.getMetadata());
		else if(myCompound.getBoolean("auto-smelt"))
			inventory[1] = new ItemStack(GAMod.tool_perks, 1, EnumToolPerks.SMELTING.getMetadata());
		else if(myCompound.getByte("fortune") == 1)
			inventory[1] = new ItemStack(GAMod.tool_perks, 1, EnumToolPerks.FORTUNE.getMetadata());
		else if(myCompound.getBoolean("silk-touch"))
			inventory[1] = new ItemStack(GAMod.tool_perks, 1, EnumToolPerks.SILKTOUCH.getMetadata());
		else if(myCompound.getBoolean("widerRadius"))
			inventory[1] = new ItemStack(GAMod.tool_perks, 1, EnumToolPerks.RADIUS.getMetadata());
		else if(myCompound.getBoolean("capitator"))
			inventory[1] = new ItemStack(GAMod.tool_perks, 1, EnumToolPerks.CAPITATOR.getMetadata());
		else if(myCompound.getBoolean("nodrops"))
			inventory[1] = new ItemStack(GAMod.tool_perks, 1, EnumToolPerks.NODROPS.getMetadata());
		else if(myCompound.getBoolean("barmor"))
			inventory[1] = new ItemStack(GAMod.tool_perks, 1, EnumToolPerks.BYPASSARMOR.getMetadata());
	}
}
