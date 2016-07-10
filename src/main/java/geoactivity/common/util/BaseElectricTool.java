package geoactivity.common.util;

import java.util.List;

import cofh.api.energy.IEnergyContainerItem;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BaseElectricTool extends BaseRedstoneTool implements IEnergyContainerItem
{
	protected int maxEnergy = 0;
	protected short maxReceive = 0;
	protected short energyPerUse = 0;

	protected BaseElectricTool(String name, ToolMaterial material, int damage, int maxEnergy, int maxReceive, int energyPerUse)
	{
		super(name, material, 0, damage);
		this.maxEnergy = maxEnergy * 1000;
		this.maxReceive = (short) maxReceive;
		this.energyPerUse = (short) energyPerUse;
		this.setMaxDamage(20);
	}

	@Override
	public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> list)
	{
		list.add(getUnchargedItem(this));
		list.add(this.getChargedItem());
	}

	public static ItemStack getUnchargedItem(Item item)
	{
		ItemStack uncharged = new ItemStack(item, 1, 20);

		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("energy", 0);

		uncharged.setTagCompound(tag);

		return uncharged.copy();
	}

	public ItemStack getChargedItem()
	{
		ItemStack charged = new ItemStack(this, 1, 0);

		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("energy", this.maxEnergy);

		charged.setTagCompound(tag);

		return charged.copy();
	}

	@Override
	public void onCreated(ItemStack stack, World world, EntityPlayer player)
	{
		stack = getUnchargedItem(this);
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean flag)
	{
		list.add("Energy: " + this.getEnergyStored(stack) / 1000 + "k / " + this.maxEnergy / 1000 + "k RF");
		list.add("Transfer(in): " + this.maxReceive + " RF/t");
		list.add("");

		super.addInformation(stack, player, list, flag);
	}

	@Override
	public boolean isToolUsable(ItemStack stack, EntityPlayer player)
	{
		if(this.getEnergyStored(stack) <= energyPerUse && player.capabilities.isCreativeMode == false)
		{
			NBTTagCompound tag = stack.getTagCompound();
			tag.setBoolean("destroyed", true);
			stack.setTagCompound(tag);
		}

		if(stack.getTagCompound().getBoolean("destroyed"))
			return false;

		return true;
	}

	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase entity, EntityLivingBase entityBase)
	{
		if(!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());

		EntityPlayer player = (EntityPlayer) entityBase;

		if(!isToolUsable(stack, player))
			return false;

		this.setEnergy(stack, this.getEnergyStored(stack) - energyPerUse);
		return true;
	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World world, IBlockState block, BlockPos pos, EntityLivingBase entity)
	{
		if(block != null && block.getBlockHardness(world, pos) != 0.0D)
		{
			this.setEnergy(stack, this.getEnergyStored(stack) - energyPerUse);

			if(stack.getTagCompound().getByte("widerRadius") > 2)
				this.setEnergy(stack, this.getEnergyStored(stack) - energyPerUse);
		}

		return true;
	}

	public void setEnergy(ItemStack stack, int energy)
	{
		NBTTagCompound tag = stack.getTagCompound();

		if(energy < 0)
			energy = 0;

		if(energy > this.maxEnergy)
			energy = this.maxEnergy;

		stack.setItemDamage(20 - energy * 20 / this.maxEnergy);

		tag.setInteger("energy", energy);

		if(energy > this.energyPerUse)
			tag.setBoolean("destroyed", false);

		stack.setTagCompound(tag);
	}

	@Override
	public int receiveEnergy(ItemStack stack, int maxReceive, boolean simulate)
	{
		int received = Math.min(this.maxEnergy - this.getEnergyStored(stack), maxReceive);
		received = Math.min(received, this.maxReceive);

		if(!simulate)
			this.setEnergy(stack, this.getEnergyStored(stack) + received);

		return received;
	}

	@Override
	public int extractEnergy(ItemStack stack, int maxExtract, boolean simulate)
	{
		return 0;
	}

	@Override
	public int getEnergyStored(ItemStack stack)
	{
		return stack.getTagCompound().getInteger("energy");
	}

	@Override
	public int getMaxEnergyStored(ItemStack stack)
	{
		return this.maxEnergy;
	}
}
