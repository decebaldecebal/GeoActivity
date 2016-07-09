package geoactivity.common.items;

import java.util.List;

import geoactivity.common.GeoActivity;
import geoactivity.common.items.ASBLogic.ASBContainer;
import geoactivity.common.items.ASBLogic.ASBGUI;
import geoactivity.common.items.ASBLogic.ASBInventory;
import geoactivity.common.lib.IOpenableGUI;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class AutoStoneBuilder extends BaseItem implements IOpenableGUI
{
	public AutoStoneBuilder(String name)
	{
		super(name);
		this.setMaxDamage(1000);
		this.setMaxStackSize(1);
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean bool)
	{
		if(stack.hasTagCompound())
		{
			if(stack.getTagCompound().getBoolean("destroyed"))
				list.add("\u00A7cDestroyed");

			list.add("\u00A77" + stack.getTagCompound().getInteger("stackSize") + " blocks");
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isFull3D()
	{
		return true;
	}

	@Override
	public void onCreated(ItemStack stack, World world, EntityPlayer player)
	{
		if(!stack.hasTagCompound())
		{
			NBTTagCompound tag = new NBTTagCompound();
			tag.setBoolean("destroyed", true);
			tag.setBoolean("canCraft", false);
			tag.setByte("itemdamage", (byte) 1);
			tag.setInteger("stackSize", 0);
			tag.setTag("resultStack", new ItemStack(Blocks.AIR).writeToNBT(new NBTTagCompound()));
			stack.setTagCompound(tag);
		}
	}

	@Override
	public EnumActionResult onItemUse(ItemStack itemStack, EntityPlayer player, World world, BlockPos pos,
		EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if(itemStack.hasTagCompound() && !itemStack.getTagCompound().getBoolean("destroyed"))
		{
			NBTTagCompound tag = itemStack.getTagCompound();
			if(tag.getBoolean("canCraft"))
				if(tag.getInteger("stackSize") > 0)
				{
					NBTTagList nbttaglist = tag.getTagList("Items", Constants.NBT.TAG_COMPOUND);
					int i = 0;
					ItemStack stack = null;
					while(i < nbttaglist.tagCount() && stack == null)
					{
						NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
						byte b0 = nbttagcompound1.getByte("Slot");
						if(b0 == 2)
							stack = ItemStack.loadItemStackFromNBT(nbttagcompound1);
						i++;
					}

					if(stack != null)
					{
						ItemStack resultStack = ItemStack.loadItemStackFromNBT((NBTTagCompound) tag.getTag("resultStack"));
						if(stack.getItem() == resultStack.getItem() && stack.getMetadata() == resultStack.getMetadata())
						{
							if(!world.isRemote)
							{
								BlockPos newPos;

								newPos = pos.offset(side);

								if(player.canPlayerEdit(newPos, side, itemStack))
								{
									itemStack.damageItem(tag.getByte("itemdamage"), player);
									tag.setInteger("stackSize", tag.getInteger("stackSize") - 1);
									world.setBlockState(newPos, Block.getBlockFromItem(stack.getItem()).getStateFromMeta(stack.getMetadata()));

									if(itemStack.getItemDamage() >= 998)
										tag.setBoolean("destroyed", true);

									itemStack.setTagCompound(tag);

									if(tag.getInteger("stackSize") == 1)
									{
										ASBInventory inv = new ASBInventory(player.inventory.getCurrentItem(), player);
										inv.searchItem(player);
									}
									return EnumActionResult.SUCCESS;
								}
							}
						}
						else
						{
							tag.setInteger("stackSize", 0);
							itemStack.setTagCompound(tag);
						}
					}
				}
		}
		return EnumActionResult.PASS;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand)
	{
		if(!world.isRemote)
			if(player.isSneaking()) {
				player.openGui(GeoActivity.instance, 0, world, (int) player.posX, (int) player.posY, (int) player.posZ);
			}
			else if(stack.getItemDamage() > 150) {
				ASBInventory inv = new ASBInventory(player.inventory.getCurrentItem(), player);
				inv.setCharge();
			}
		player.setActiveHand(hand);
		return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		ASBInventory inv = new ASBInventory(player.getHeldItemMainhand(), player);
		return new ASBGUI(inv, player);
	}

	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		ASBInventory inv = new ASBInventory(player.getHeldItemMainhand(), player);
		return new ASBContainer(inv, player);
	}
}
