package geoactivity.common.items;

import java.util.List;

import geoactivity.common.util.GeneralHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ToolPerks extends BaseItem
{
	public ToolPerks(String name)
	{
		super(GeneralHelper.appendStringToArray(GeneralHelper.getEnumStrings(EnumToolPerks.class), name + "_"));
		this.setHasSubtypes(true);
	    this.setMaxDamage(0);
	    this.setMaxStackSize(1);
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean bool)
	{
		if (stack.getMetadata() >= EnumToolPerks.values().length)
			return ;

		if(!GeneralHelper.isShiftKeyDown())
		{
			list.add(GeneralHelper.shiftForInfo);
			return ;
		}

		switch(EnumToolPerks.values()[stack.getMetadata()])
		{
			case SPEED:
				list.add("\u00A7cTools mine faster.");
				list.add("\u00A77Works on:");
				list.add("\u00A77 - pickaxes");
				list.add("\u00A77 - shovels");
				list.add("\u00A77 - axes");
			break;
			case EFFICIENCY:
				list.add("\u00A7cTools or armor use less fuel.");
				list.add("\u00A77Doesn't work on RF powered tools.");
			break;
			case DAMAGE:
				list.add("\u00A7cTools do more damage.");
			break;
			case EXPERIENCE:
				list.add("\u00A7cMore experience from mining.");
				list.add("\u00A77Works on:");
				list.add("\u00A77 - pickaxes");
				list.add("\u00A77 - swords");
			break;
			case SMELTING:
				list.add("\u00A7cAuto smelt mined blocks.");
				list.add("\u00A77Works on:");
				list.add("\u00A77 - pickaxes");
				list.add("\u00A77 - shovels");
				list.add("\u00A77 - axes");
			break;
			case FORTUNE:
				list.add("\u00A7cMore drops from certain blocks.");
				list.add("\u00A77Works on:");
				list.add("\u00A77 - pickaxes");
				list.add("\u00A77 - shovels");
			break;
			case SILKTOUCH:
				list.add("\u00A7cGet the exact block you mine.");
				list.add("\u00A77Works on:");
				list.add("\u00A77 - pickaxes");
				list.add("\u00A77 - shovels");
				list.add("\u00A77 - axes");
			break;
			case RADIUS:
				list.add("\u00A7cIncreases the mining radius to 3x3x1 on Advanced and Redstone Tools.");
				list.add("\u00A77Further increases the range on Advanced Redstone Tools.");
				list.add("\u00A77Works on:");
				list.add("\u00A77 - pickaxes");
				list.add("\u00A77 - shovels");
			break;
			case CAPITATOR:
				list.add("\u00A7cTools destroy blocks in a certain range.");
				list.add("\u00A77Works on:");
				list.add("\u00A77 - pickaxes");
				list.add("\u00A77 - shovels");
				list.add("\u00A77 - axes");
			break;
			case NODROPS:
				list.add("\u00A7cTools do not drop any blocks.");
				list.add("\u00A77Works on:");
				list.add("\u00A77 - pickaxes");
				list.add("\u00A77 - shovels");
				list.add("\u00A77 - axes");
				break;
			case DROPTP:
			{
				if(stack.hasTagCompound() && stack.getTagCompound().getIntArray("coords").length>0)
				{
					int[] coords = stack.getTagCompound().getIntArray("coords");
					list.add("\u00A7cBound to inventory located at: " + coords[0] + " " + coords[1] + " " + coords[2]);
					list.add("\u00A7cRight Click in air to unbound.");
				}
				else
				{
					list.add("\u00A7cAutomatically teleport block drops into a chosen inventory.");
					list.add("\u00A7cShift + Right Click on an inventory to bind the perk to it.");
					list.add("\u00A77Not compatible with Advanced Tools.");
					list.add("\u00A77Works only on Redstone Tools.");
				}
			}
			break;
			case BYPASSARMOR:
				list.add("\u00A7cA third of the tool's damage bypasses armor.");
			break;
			default:
				list.add("\u00A7aJust a blank perk");
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tabs, List<ItemStack> list)
    {
        for(int i = 0;i < EnumToolPerks.values().length;i++)
        {
        	list.add(new ItemStack(item, 1, i));
        }
    }

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos,
		EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
		if(stack.getMetadata() == EnumToolPerks.DROPTP.getMetadata() && player.isSneaking())
		{
			if(world.getTileEntity(pos) != null && world.getTileEntity(pos) instanceof IInventory)
			{
				IInventory inventory = (IInventory)world.getTileEntity(pos);

				if(inventory.getSizeInventory() > 0)
				{
					NBTTagCompound tag = new NBTTagCompound();
					tag.setIntArray("coords", new int[]{pos.getX(),pos.getY(),pos.getZ()});

					stack.setTagCompound(tag);

					if(!world.isRemote)
						player.addChatMessage(new TextComponentTranslation("ga.perks.droptp"));
				}
			}
		}
		return EnumActionResult.PASS;
    }

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand)
    {
		if(stack.getMetadata() == EnumToolPerks.DROPTP.getMetadata() && !player.isSneaking()
				&& stack.hasTagCompound() && stack.getTagCompound().getIntArray("coords").length > 0)
			stack.setTagCompound(null);

		return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
    }

	public static enum EnumToolPerks
	{
		BLANK(0, "blank"),
		SPEED(1, "speed"),
		EFFICIENCY(2, "efficiency"),
		DAMAGE(3, "damage"),
		EXPERIENCE(4, "experience"),
		SMELTING(5, "smelting"),
		FORTUNE(6, "fortune"),
		SILKTOUCH(7, "silktouch"),
		RADIUS(8, "radius"),
		CAPITATOR(9, "capitator"),
		NODROPS(10, "nodrops"),
		DROPTP(11, "droptp"),
		BYPASSARMOR(12, "bypassarmor");

		int meta;
		String name;

		private EnumToolPerks(int meta, String name)
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
