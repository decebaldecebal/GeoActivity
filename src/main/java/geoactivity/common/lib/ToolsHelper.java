package geoactivity.common.lib;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/*
 * Class courtesy of TinkersConstruct
 */

public class ToolsHelper
{
	public static Random random = new Random();

	public static RayTraceResult raytraceFromEntity(World world, Entity player, boolean par3, double range)
	{
		float f = 1.0F;
		float f1 = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * f;
		float f2 = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * f;
		double d0 = player.prevPosX + (player.posX - player.prevPosX) * f;
		double d1 = player.prevPosY + (player.posY - player.prevPosY) * f;
		if(!world.isRemote && player instanceof EntityPlayer)
			d1 += 1.62D;
		double d2 = player.prevPosZ + (player.posZ - player.prevPosZ) * f;
		Vec3d vec3 = new Vec3d(d0, d1, d2);
		float f3 = MathHelper.cos(-f2 * 0.017453292F - (float) Math.PI);
		float f4 = MathHelper.sin(-f2 * 0.017453292F - (float) Math.PI);
		float f5 = -MathHelper.cos(-f1 * 0.017453292F);
		float f6 = MathHelper.sin(-f1 * 0.017453292F);
		float f7 = f4 * f5;
		float f8 = f3 * f5;
		double d3 = range;
		if(player instanceof EntityPlayerMP)
//			d3 = ((EntityPlayerMP) player).theItemInWorldManager.getBlockReachDistance();
			d3 = 4.5f;
		Vec3d vec31 = vec3.addVector(f7 * d3, f6 * d3, f8 * d3);
		return world.rayTraceBlocks(vec3, vec31, par3, !par3, par3);
	}

	public static void spawnStackInWorld(World world, BlockPos pos, ItemStack stack)
	{
		if(!world.isRemote)
		{
			world.setBlockToAir(pos);
			world.spawnEntityInWorld(new EntityItem(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack.copy()));
		}
	}

	public static void spawnAlternateStack(ItemStack itemStack, World world, BlockPos pos, ItemStack stack)
	{
		if(itemStack.getTagCompound().getBoolean("droptp") && itemStack.getTagCompound().getIntArray("droptp-coords").length == 3)
		{
			int[] coords = itemStack.getTagCompound().getIntArray("droptp-coords");
			BlockPos newPos = new BlockPos(coords[0], coords[1], coords[2]);
			if(world.getTileEntity(newPos) != null && world.getTileEntity(newPos) instanceof IInventory)
			{
				IInventory inventory = (IInventory) world.getTileEntity(newPos);

				for(int i = 0;i < inventory.getSizeInventory();i++)
				{
					ItemStack invstack = inventory.getStackInSlot(i);

					if(invstack == null && inventory.getInventoryStackLimit() >= stack.stackSize)
					{
						inventory.setInventorySlotContents(i, stack.copy());
						stack.stackSize = 0;
					}
					else if(invstack.getItem() == stack.getItem() && invstack.getItemDamage() == stack.getItemDamage() &&
						invstack.stackSize < inventory.getInventoryStackLimit())
					{
						int toTransfer = Math.min(Math.min(stack.stackSize, inventory.getInventoryStackLimit() - invstack.stackSize), stack.getItem()
							.getItemStackLimit(stack));

						ItemStack newstack = new ItemStack(invstack.getItem(), toTransfer + invstack.stackSize, invstack.getItemDamage());

						inventory.setInventorySlotContents(i, newstack);

						stack.stackSize -= toTransfer;
					}

					if(stack.stackSize == 0)
					{
						if(!world.isRemote)
							world.setBlockToAir(pos);

						return;
					}
				}
			}
		}

		spawnStackInWorld(world, pos, stack);
	}
}
