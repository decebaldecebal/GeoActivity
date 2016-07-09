package geoactivity.common.items.tools.Red;

import java.util.List;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import geoactivity.common.GAMod;
import geoactivity.common.GeoActivity;
import geoactivity.common.items.tools.Red.ABMLogic.ABMContainer;
import geoactivity.common.items.tools.Red.ABMLogic.ABMGUI;
import geoactivity.common.items.tools.Red.ABMLogic.ABMInventory;
import geoactivity.common.lib.ToolsHelper;
import geoactivity.common.util.BaseElectricTool;
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
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class ARedstoneBattleMiner extends BaseElectricTool
{
	public ARedstoneBattleMiner(String name)
	{
		super(name, GAMod.AdvancedMaterial, 12, 5000, 20000, 1000);
		this.efficiencyOnProperMaterial = 12;
		this.setHarvestLevel("pickaxe", GAMod.AdvancedMaterial.getHarvestLevel());
		this.setHarvestLevel("shovel", GAMod.AdvancedMaterial.getHarvestLevel());
		this.setHarvestLevel("axe", GAMod.AdvancedMaterial.getHarvestLevel());
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean bool)
	{
		list.add("Energy: " + this.getEnergyStored(stack) / 1000 + "k / " + this.maxEnergy / 1000 + "k RF");
		list.add("Transfer(in): " + this.maxReceive + " RF");
		list.add("");

		if(stack.getTagCompound().getBoolean("destroyed"))
			list.add("\u00A7cDestroyed");

		if(!GeneralHelper.isShiftKeyDown())
		{
			list.add(GeneralHelper.shiftForInfo);
			return;
		}

		if(stack.getTagCompound().getByte("speed") >= 1)
			list.add("\u00A77Has extra speed! [" + stack.getTagCompound().getByte("speed") + "]");
		if(stack.getTagCompound().getBoolean("experience"))
			list.add("\u00A77Gives you more experience.");
		if(stack.getTagCompound().getBoolean("auto-smelt"))
			list.add("\u00A77Auto smelts!");
		if(stack.getTagCompound().getByte("fortune") >= 1)
			list.add("\u00A77Has Fortune " + stack.getTagCompound().getByte("fortune") + " enchant!");
		if(stack.getTagCompound().getBoolean("silk-touch"))
			list.add("\u00A77Has Silk-Touch enchant!");
		if(stack.getTagCompound().getByte("widerRadius") > 0)
			list.add("\u00A77Has wider mining radius! [" + stack.getTagCompound().getByte("widerRadius") + "]");
		if(stack.getTagCompound().getBoolean("capitator"))
			list.add("\u00A77Destroys blocks in a vertical line.");
		if(stack.getTagCompound().getBoolean("nodrops"))
			list.add("\u00A77Gives no block drops.");
		if(stack.getTagCompound().getBoolean("droptp"))
			list.add("\u00A77Teleports block drops.");
		if(stack.getTagCompound().getBoolean("barmor"))
			list.add("\u00A77Deals damage that bypasses armor.");

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
					((EntityPlayer) entityBase).addExperience(1);
				((EntityPlayer) entityBase).addExperience(1);
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

		if(stack.getTagCompound().getByte("widerRadius") > 0)
		{
			RayTraceResult mop = ToolsHelper.raytraceFromEntity(world, player, true, 5.0D);
			if(mop == null)
				return super.onBlockStartBreak(stack, pos, player);

			byte radius = stack.getTagCompound().getByte("widerRadius");

			int xRange = radius;
			int yRange = radius;
			int zRange = radius;
			switch(mop.sideHit)
			{
				case DOWN:
				case UP:
					yRange = 0;
				break;
				case NORTH:
				case SOUTH:
					zRange = 0;
				break;
				case EAST:
				case WEST:
					xRange = 0;
				break;
			}

			if(radius > 2)
				if(xRange == 0)
					xRange = radius / 2;
				else if(yRange == 0)
					yRange = radius / 2;
				else if(zRange == 0)
					zRange = radius / 2;

			Block block = world.getBlockState(pos).getBlock();
			if(block != null)
			{
				BlockPos newPos = null;

				for(int xPos = pos.getX() - xRange;xPos <= pos.getX() + xRange;xPos++)
					for(int yPos = pos.getY() - yRange;yPos <= pos.getY() + yRange;yPos++)
						for(int zPos = pos.getZ() - zRange;zPos <= pos.getZ() + zRange;zPos++)
						{
							if(!isToolUsable(stack, player))
								return true;

							newPos = new BlockPos(xPos, yPos, zPos);

							destroyBlock(stack, world, newPos, player);
						}

				return false;
			}
		}
		else if(stack.getTagCompound().getBoolean("capitator"))
		{
			Block block = world.getBlockState(pos).getBlock();
			if(block != null)
			{
				if(block.isWood(world, pos))
				{
					breakTree(world, stack, pos, player);
					return false;
				}

				BlockPos newPos = pos;
				int yPos = pos.getY();
				while(yPos != pos.getY() + 6)
				{
					if(!isToolUsable(stack, player))
						return true;

					newPos = newPos.add(0, 1, 0);

					destroyBlock(stack, world, newPos, player);

					yPos++;
				}

				newPos = pos;
				yPos = pos.getY() - 1;
				while(yPos != pos.getY() - 6)
				{
					if(!isToolUsable(stack, player))
						return true;

					newPos = newPos.add(0, -1, 0);

					destroyBlock(stack, world, newPos, player);

					yPos--;
				}

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

			if(hlvl <= 3 && (tool == null || tool.equals("pickaxe") || tool.equals("shovel") || tool.equals("axe")))
				super.destroyBlock(stack, world, pos, player, block, blockState);
		}
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand)
	{
		if(!world.isRemote)
			if(player.isSneaking())
				player.openGui(GeoActivity.instance, 0, world, (int) player.posX, (int) player.posY,
					(int) player.posZ);

		player.setActiveHand(hand);
		return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos,
		EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if(!player.canPlayerEdit(pos.offset(side), side, stack))
			return EnumActionResult.FAIL;
		else
		{
			int hook = net.minecraftforge.event.ForgeEventFactory.onHoeUse(stack, player, world, pos);
			if(hook != 0) {
				return hook > 0 ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;
			}

			IBlockState iblockstate = world.getBlockState(pos);
			Block block = iblockstate.getBlock();

			if(side != EnumFacing.DOWN && world.isAirBlock(pos.up()))
			{
				if(block == Blocks.GRASS)
				{
					return this.useHoe(stack, player, world, pos, Blocks.FARMLAND.getDefaultState());
				}

				if(block == Blocks.DIRT)
				{
					switch(ARedstoneBattleMiner.SwitchDirtType.TYPE_LOOKUP[iblockstate.getValue(BlockDirt.VARIANT).ordinal()])
					{
						case 1:
							return this.useHoe(stack, player, world, pos, Blocks.FARMLAND.getDefaultState());
						case 2:
							return this.useHoe(stack, player, world, pos,
								Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT));
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

		if(!world.isRemote) {
			world.setBlockState(target, state);
			this.setEnergy(stack, this.getEnergyStored(stack) - energyPerUse);
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

		if(stack.getTagCompound().getByte("widerRadius") > 0 || stack.getTagCompound().getBoolean("capitator"))
			eff -= 8.0F;

		if(stack.hasTagCompound() && stack.getTagCompound().getByte("speed") >= 1)
			eff += 2.0F * stack.getTagCompound().getByte("speed");

		if(stack.getTagCompound().getByte("widerRadius") > 2)
			eff /= 2;

		Block block = state.getBlock();

		if(block.getHarvestLevel(state) <= this.getToolMaterial().getHarvestLevel()
			&& (block.isToolEffective("pickaxe", state)
				|| block.isToolEffective("shovel", state) || block.isToolEffective("axe", state)))
			return eff;

		Material mat = state.getMaterial();
		for(Material m : GAMod.battleminerMaterials)
			if(m == mat)
				return eff;

		return 1.0F;
	}

	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack)
	{
		return HashMultimap.create();
	}

	static final class SwitchDirtType
	{
		static final int[] TYPE_LOOKUP = new int[BlockDirt.DirtType.values().length];

		static
		{
			try
			{
				TYPE_LOOKUP[BlockDirt.DirtType.DIRT.ordinal()] = 1;
			}
			catch(NoSuchFieldError var2)
			{
				;
			}

			try
			{
				TYPE_LOOKUP[BlockDirt.DirtType.COARSE_DIRT.ordinal()] = 2;
			}
			catch(NoSuchFieldError var1)
			{
				;
			}
		}
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		ABMInventory inv = new ABMInventory(player.getHeldItemMainhand(), player);
		return new ABMGUI(inv, player);
	}

	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		ABMInventory inv = new ABMInventory(player.getHeldItemMainhand(), player);
		return new ABMContainer(inv, player);
	}
}