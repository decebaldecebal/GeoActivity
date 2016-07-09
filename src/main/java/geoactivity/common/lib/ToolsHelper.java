package geoactivity.common.lib;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Enchantments;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
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

	public static boolean onLeftClickEntity(ItemStack stack, EntityLivingBase player, Entity entity, int baseDamage)
	{
		if(!stack.hasTagCompound())
		{
			NBTTagCompound tag = new NBTTagCompound();
			tag.setByte("damage", (byte) 0);
			stack.setTagCompound(tag);
		}
		if(entity.canBeAttackedWithItem() && stack.hasTagCompound())
			if(!entity.hitByEntity(player))
			{
				int damage = stack.getTagCompound().getByte("damage") + baseDamage;

				if(player.isPotionActive(MobEffects.STRENGTH))
					damage += 3 << player.getActivePotionEffect(MobEffects.STRENGTH).getAmplifier();

				if(player.isPotionActive(MobEffects.WEAKNESS))
					damage -= 2 << player.getActivePotionEffect(MobEffects.WEAKNESS).getAmplifier();

				float knockback = 0;
				float enchantDamage = 0;

				if(entity instanceof EntityLivingBase)
				{
					 enchantDamage = EnchantmentHelper.getEnchantmentLevel(Enchantments.SHARPNESS, player.getHeldItemMainhand());
					 knockback += EnchantmentHelper.getKnockbackModifier(player);
				}

				if(damage < 1)
					damage = 1;

				if(player.isSprinting())
					knockback++;

				if(damage > 0 || enchantDamage > 0)
				{
					boolean criticalHit = player.fallDistance > 0.0F && !player.onGround && !player.isOnLadder() && !player.isInWater()
						&& !player.isPotionActive(MobEffects.BLINDNESS)
						&& player.getRidingEntity() == null && entity instanceof EntityLivingBase;

					if(criticalHit)
						damage += random.nextInt(damage / 2 + 2);

					damage += enchantDamage;

					boolean var6 = false;
					int fireAspect = EnchantmentHelper.getFireAspectModifier(player);

					if(entity instanceof EntityLivingBase && fireAspect > 0 && !entity.isBurning())
					{
						var6 = true;
						entity.setFire(1);
					}

					boolean causedDamage = false;

					if(player instanceof EntityPlayer)
					{
						if(stack.getTagCompound().getBoolean("barmor"))
						{
							entity.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) player).setDamageBypassesArmor(), damage / 3);
							damage -= damage / 3;
						}
						causedDamage = entity.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) player), damage);
					}
					else
					{
						if(stack.getTagCompound().getBoolean("barmor"))
						{
							entity.attackEntityFrom(DamageSource.causeMobDamage(player).setDamageBypassesArmor(), damage / 3);
							damage -= damage / 3;
						}
						causedDamage = entity.attackEntityFrom(DamageSource.causeMobDamage(player), damage);
					}

					if(causedDamage)
					{
						if(knockback > 0)
						{
							entity.addVelocity(-MathHelper.sin(player.rotationYaw * (float) Math.PI / 180.0F) * knockback * 0.5F, 0.1D,
								MathHelper.cos(player.rotationYaw * (float) Math.PI / 180.0F) * knockback * 0.5F);
							player.motionX *= 0.6D;
							player.motionZ *= 0.6D;
							player.setSprinting(false);
						}

						if(player instanceof EntityPlayer)
						{
							if(criticalHit)
								((EntityPlayer) player).onCriticalHit(entity);

							if(enchantDamage > 0)
								((EntityPlayer) player).onEnchantmentCritical(entity);

							if(damage >= 18f)
								((EntityPlayer) player).addStat(AchievementList.OVERKILL);
						}

						player.setLastAttacker(entity);

						if(entity instanceof EntityLivingBase)
							DamageSource.causeThornsDamage(entity);
					}

					if(entity instanceof EntityLivingBase)
					{
						if(entity instanceof EntityPlayer)
						{
							stack.hitEntity((EntityLivingBase) entity, (EntityPlayer) player);
							if(entity.isEntityAlive())
								alertPlayerWolves((EntityPlayer) player, (EntityLivingBase) entity, true);

							((EntityPlayer) player).addStat(StatList.DAMAGE_DEALT, damage);
						}
						else
							stack.getItem().hitEntity(stack, (EntityLivingBase) entity, player);

						if(fireAspect > 0 && causedDamage)
						{
							fireAspect *= 4;
							entity.setFire(fireAspect);
						}
						else if(var6)
							entity.extinguish();
					}

					if(entity instanceof EntityPlayer)
						((EntityPlayer) player).addExhaustion(0.3F);
					if(causedDamage)
						return true;
				}
			}
		return false;
	}

	@SuppressWarnings("rawtypes")
	private static void alertPlayerWolves(EntityPlayer player, EntityLivingBase living, boolean par2)
	{
		if(!(living instanceof EntityCreeper) && !(living instanceof EntityGhast))
		{
			if(living instanceof EntityWolf)
			{
				EntityWolf var3 = (EntityWolf) living;

				if(var3.isTamed() && player == var3.getOwner())
				 return;
			}

			if(!(living instanceof EntityPlayer) || player.canAttackPlayer((EntityPlayer) living))
			{
				List var6 =
					player.worldObj.getEntitiesWithinAABB(
						EntityWolf.class,
						new AxisAlignedBB(player.posX, player.posY, player.posZ, player.posX + 1.0D, player.posY + 1.0D, player.posZ + 1.0D)
							.expand(16.0D, 4.0D, 16.0D));
				Iterator var4 = var6.iterator();

				while(var4.hasNext())
				{
					EntityWolf var5 = (EntityWolf) var4.next();

					if(var5.isTamed() && var5.getAttackTarget() == null && player == var5.getOwner()
						&& (!par2 || !var5.isSitting()))
					{
						var5.setSitting(false);
						var5.setAttackTarget(living);
					}
				}
			}
		}
	}

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
