package geoactivity.common.items.tools.Red;

import java.util.List;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import geoactivity.common.GAMod;
import geoactivity.common.util.BaseRedstoneTool;
import geoactivity.common.util.GeneralHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RedstoneBattleAxe extends BaseRedstoneTool
{
	public RedstoneBattleAxe(String name)
	{
		super(name, GAMod.AdvancedMaterial, 998, 10);
		this.setHarvestLevel("axe", GAMod.AdvancedMaterial.getHarvestLevel());
	}

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
				return ;
			}

			if(stack.getTagCompound().getByte("speed")>=1)
				list.add("\u00A77Has extra speed! ["+stack.getTagCompound().getByte("speed")+"]");
			if(stack.getTagCompound().getByte("efficiency")>=1)
				list.add("\u00A77Uses less fuel! ["+stack.getTagCompound().getByte("efficiency")+"]");
			if(stack.getTagCompound().getBoolean("experience"))
				list.add("\u00A77Gives you more experience.");
			if(stack.getTagCompound().getBoolean("auto-smelt"))
				list.add("\u00A77Auto smelts!");
			if(stack.getTagCompound().getBoolean("silk-touch"))
				list.add("\u00A77Has Silk-Touch enchant!");
			if(stack.getTagCompound().getBoolean("capitator"))
				list.add("\u00A77Destroys trees very quickly!");
			if(stack.getTagCompound().getBoolean("nodrops"))
				list.add("\u00A77Gives no block drops.");
			if(stack.getTagCompound().getBoolean("droptp"))
				list.add("\u00A77Teleports block drops.");
			if(stack.getTagCompound().getBoolean("barmor"))
				list.add("\u00A77Deals damage that bypasses armor.");
		}
		list.add("");

		addDamageInfo(stack, this.damage, list);
	}

	@Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase entity, EntityLivingBase entityBase)
    {
		super.hitEntity(stack, entity, entityBase);

		if(stack.getTagCompound().getBoolean("experience"))
			if(!entity.isEntityAlive())
			{
				if(entity instanceof EntityCreature)
					((EntityPlayer)entityBase).addExperience(1);
				((EntityPlayer)entityBase).addExperience(1);
			}

		return true;
    }

	@Override
	public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, EntityPlayer player)
	{
		if(!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());

		if(!isToolUsable(stack, player))
			return true;

		World world = player.worldObj;

		if(stack.getTagCompound().getBoolean("capitator"))
		{
	        Block block = world.getBlockState(pos).getBlock();
	        if(block != null && block.isWood(world, pos))
	        {
	        	breakTree(world, stack, pos, player);
	        	return false;
	        }
		}

		destroyBlock(stack, world, pos, player);

		return false;
	}

	@Override
	protected void destroyBlock(ItemStack stack, World world, BlockPos pos, EntityPlayer player)
	{
		IBlockState blockState = world.getBlockState(pos);
        Block block = blockState.getBlock();

        if(block != null && blockState.getBlockHardness(world, pos) > 0.0D)
        {
        	int hlvl = block.getHarvestLevel(blockState);

        	String tool = block.getHarvestTool(blockState);

	     	if (hlvl<=3 && (tool==null || tool.equals("axe")))
	 	        super.destroyBlock(stack, world, pos, player, block, blockState);

        }
	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos,
		EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (!player.canPlayerEdit(pos.offset(side), side, stack))
			return EnumActionResult.FAIL;
		else
		{
			int hook = net.minecraftforge.event.ForgeEventFactory.onHoeUse(stack, player, world, pos);
            if (hook != 0) {
            	return hook > 0 ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;
            }

            IBlockState iblockstate = world.getBlockState(pos);
            Block block = iblockstate.getBlock();

            if (side != EnumFacing.DOWN && world.isAirBlock(pos.up()))
            {
            	 if (block == Blocks.GRASS)
                 {
                     return this.useHoe(stack, player, world, pos, Blocks.FARMLAND.getDefaultState());
                 }

                 if (block == Blocks.DIRT)
                 {
                     switch (ARedstoneBattleMiner.SwitchDirtType.TYPE_LOOKUP[iblockstate.getValue(BlockDirt.VARIANT).ordinal()])
                     {
                         case 1:
                             return this.useHoe(stack, player, world, pos, Blocks.FARMLAND.getDefaultState());
                         case 2:
                             return this.useHoe(stack, player, world, pos, Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT));
                     }
                 }
			}

			return EnumActionResult.FAIL;
		}
	}

	protected EnumActionResult useHoe(ItemStack stack, EntityPlayer player, World world, BlockPos target, IBlockState state)
    {
		world.playSound(target.getX() + 0.5F, target.getY() + 0.5F,
			target.getZ() + 0.5F, state.getBlock().getSoundType().getStepSound(),
			SoundCategory.PLAYERS, (state.getBlock().getSoundType().getVolume() + 1.0F) / 2.0F, state.getBlock().getSoundType().getPitch() * 0.8F, false);

        if (!world.isRemote) {
            world.setBlockState(target, state);
            stack.damageItem(1, player);
        }

        return EnumActionResult.SUCCESS;
    }

	@Override
	public boolean canHarvestBlock(IBlockState state, ItemStack stack)
	{
		return this.getToolSpeed(stack, state) > 1.0F;
	}

	@Override
	public float getStrVsBlock(ItemStack stack, IBlockState state)
	{
		return this.getToolSpeed(stack, state);
	}

	private float getToolSpeed(ItemStack stack, IBlockState state)
	{
		if(!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());

		float eff = this.efficiencyOnProperMaterial;

		if(stack.getTagCompound().getBoolean("capitator"))
			eff -= 8.0F;

		if(stack.hasTagCompound() && stack.getTagCompound().getByte("speed") >= 1 && eff != 1)
			eff += 2.0F * stack.getTagCompound().getByte("speed");

		Block block = state.getBlock();

		if (block.getHarvestLevel(state) <= this.getToolMaterial().getHarvestLevel()
				&& block.isToolEffective("axe", state))
			return eff;

		Material mat = state.getMaterial();
		for (Material m : GAMod.axeMaterials)
            if (m == mat)
                return eff;

		return 1.0F;
	}

	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack)
    {
		return HashMultimap.create();
    }
}
