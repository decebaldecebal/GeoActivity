package geoactivity.common.items.armor;

import java.util.List;

import geoactivity.common.GAMod;
import geoactivity.common.GeoActivity;
import geoactivity.common.items.armor.AdvLogic.AdvAGUI;
import geoactivity.common.items.armor.AdvLogic.AdvAInventory;
import geoactivity.common.items.tools.Adv.Logic.AdvTContainer;
import geoactivity.common.lib.IHasName;
import geoactivity.common.lib.IOpenableGUI;
import geoactivity.common.lib.Reference;
import geoactivity.common.util.GeneralHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class AdvancedArmor extends ItemArmor implements ISpecialArmor, IHasName, IOpenableGUI
{
	private String name;

	public AdvancedArmor(String name, int renderIndex, EntityEquipmentSlot type)
	{
		super(GAMod.AdvancedArmorMaterial, renderIndex, type);
		this.setNoRepair();
		this.name = name;
		this.setUnlocalizedName(name);
		this.setCreativeTab(GeoActivity.tabMain);
		GameRegistry.register(this.setRegistryName(Reference.MOD_ID, name));
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public String getName(int meta)
	{
		return this.getName();
	}

	@SuppressWarnings("incomplete-switch")
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean bool)
	{
		if(stack.hasTagCompound())
		{
			if(stack.getTagCompound().getBoolean("destroyed"))
				list.add("\u00A7cDestroyed");

			if(!GeneralHelper.isShiftKeyDown())
			{
				list.add(GeneralHelper.shiftForInfo);
				return;
			}

			if(stack.getTagCompound().getByte("all") > 0)
				list.add("\u00A77Extra protection against all damage!");
			else if(stack.getTagCompound().getByte("blast") > 0)
				list.add("\u00A77Extra protection against explosions!");
			else if(stack.getTagCompound().getByte("fire") > 0)
				list.add("\u00A77Extra protection against fire damage!");
			else if(stack.getTagCompound().getByte("magic") > 0)
				list.add("\u00A77Extra protection against magic damage!");
			else if(stack.getTagCompound().getByte("projectile") > 0)
				list.add("\u00A77Extra protection against projectiles!");

			switch(this.armorType)
			{
				case HEAD:
					if(stack.getTagCompound().getBoolean("respiration"))
						list.add("\u00A77Breathe underwater.");
				break;
				case LEGS:
				case FEET:
					if(stack.getTagCompound().getBoolean("fall"))
						list.add("\u00A77Take no fall damage.");
			}
		}
	}

	@Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
	{
		return false;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand)
	{
		if(!world.isRemote)
			if(player.isSneaking())
				player.openGui(GeoActivity.instance, 0, world, (int) player.posX,
						(int) player.posY, (int) player.posZ);
			else
			{
				AdvAInventory inv = new AdvAInventory(player.inventory.getCurrentItem(), player);
				inv.setCharge();
			}
		player.setActiveHand(hand);
		return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
	}

	@SuppressWarnings("incomplete-switch")
	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack stack)
	{
		if(!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());

		if(stack.getItemDamage() >= stack.getMaxDamage() - 2)
		{
			NBTTagCompound tag = stack.getTagCompound();
			tag.setBoolean("destroyed", true);
			stack.setTagCompound(tag);

			return;
		}

		switch(this.armorType)
		{
			case HEAD:
				if(stack.getTagCompound().getBoolean("respiration")
						&& stack.getItemDamage() < stack.getMaxDamage() - 16)
					if(player.getAir() == 0)
					{
						player.setAir(150);
						stack.damageItem(16, player);
					}
			break;
			case LEGS:
			case FEET:
				if(stack.getTagCompound().getBoolean("fall"))
				{
					if(player.fallDistance > 0)
						player.fallDistance = 0;

				}
		}
	}

	@Override
	public ArmorProperties getProperties(EntityLivingBase player, ItemStack stack, DamageSource source,
			double damage, int slot)
	{
		int h = getArmorMaterial().getDamageReductionAmount(EntityEquipmentSlot.HEAD);
		int c = getArmorMaterial().getDamageReductionAmount(EntityEquipmentSlot.CHEST);
		int p = getArmorMaterial().getDamageReductionAmount(EntityEquipmentSlot.LEGS);
		int b = getArmorMaterial().getDamageReductionAmount(EntityEquipmentSlot.FEET);

		if(!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());

		if(stack.getTagCompound().getBoolean("destroyed"))
			return new ArmorProperties(0, 1 / damage, 1);

		if(stack.getTagCompound().getByte("all") > 0)
		{
			h += 1;
			c += 2;
			p += 1;
			b += 1;
		}
		else if(stack.getTagCompound().getByte("blast") > 0 && source.isExplosion())
		{
			h += 2;
			c += 3;
			p += 2;
			b += 2;
		}
		else if(stack.getTagCompound().getByte("fire") > 0 && source.isFireDamage())
		{
			h += 2;
			c += 3;
			p += 2;
			b += 2;
		}
		else if(stack.getTagCompound().getByte("magic") > 0 && source.isMagicDamage())
		{
			h += 2;
			c += 3;
			p += 2;
			b += 2;
		}
		else if(stack.getTagCompound().getByte("projectile") > 0 && source.isProjectile())
		{
			h += 2;
			c += 3;
			p += 2;
			b += 2;
		}

		switch(slot)
		{
			case 3:
				return new ArmorProperties(0, h / 25D, Integer.MAX_VALUE);
			case 2:
				return new ArmorProperties(1, c / 25D, Integer.MAX_VALUE);
			case 1:
				return new ArmorProperties(1, p / 25D, Integer.MAX_VALUE);
			case 0:
				return new ArmorProperties(0, b / 25D, Integer.MAX_VALUE);
			default:
				return null;
		}
	}

	@Override
	public int getArmorDisplay(EntityPlayer player, ItemStack stack, int slot)
	{
		if(!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());

		if(stack.getTagCompound().getBoolean("destroyed"))
			return 1;

		switch(slot)
		{
			case 3:
				return 3;
			case 2:
				return 8;
			case 1:
				return 6;
			case 0:
				return 3;
			default:
				return 0;
		}
	}

	@Override
	public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot)
	{
		if(!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());

		if(stack.getItemDamage() >= stack.getMaxDamage() - 2)
		{
			NBTTagCompound tag = stack.getTagCompound();
			tag.setBoolean("destroyed", true);
			stack.setTagCompound(tag);

			return;
		}

		if(stack.getTagCompound().getByte("all") > 0)
			stack.damageItem(1, entity);
		else if(stack.getTagCompound().getByte("blast") > 0 && source.isExplosion())
			stack.damageItem(1, entity);
		else if(stack.getTagCompound().getByte("fire") > 0 && source.isFireDamage())
			stack.damageItem(1, entity);
		else if(stack.getTagCompound().getByte("magic") > 0 && source.isMagicDamage())
			stack.damageItem(1, entity);
		else if(stack.getTagCompound().getByte("projectile") > 0 && source.isProjectile())
			stack.damageItem(1, entity);
		else if(!source.isUnblockable())
			stack.damageItem(2, entity);
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		AdvAInventory inv = new AdvAInventory(player.getHeldItemMainhand(), player);
		return new AdvAGUI(inv, player);
	}

	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		AdvAInventory inv = new AdvAInventory(player.getHeldItemMainhand(), player);
		return new AdvTContainer(inv, player);
	}
}
