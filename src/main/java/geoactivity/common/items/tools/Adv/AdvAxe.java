package geoactivity.common.items.tools.Adv;

import java.util.List;
import java.util.Set;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import geoactivity.common.GAMod;
import geoactivity.common.GeoActivity;
import geoactivity.common.items.tools.Adv.Logic.AdvTContainer;
import geoactivity.common.items.tools.Adv.Logic.AdvTGUI;
import geoactivity.common.items.tools.Adv.Logic.AdvTInventory;
import geoactivity.common.lib.ToolsHelper;
import geoactivity.common.util.BaseGUITool;
import geoactivity.common.util.GeneralHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AdvAxe extends BaseGUITool
{
	public AdvAxe(String name)
	{
		super(name, GAMod.AdvancedMaterial, 2.0f);

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
				return;
			}

			if(stack.getTagCompound().getByte("speed") == 1)
				list.add("\u00A77Has extra speed!");
			else if(stack.getTagCompound().getByte("efficiency") == 1)
				list.add("\u00A77Uses less fuel!");
			else if(stack.getTagCompound().getBoolean("auto-smelt"))
				list.add("\u00A77Auto smelts!");
			else if(stack.getTagCompound().getBoolean("silk-touch"))
				list.add("\u00A77Has Silk-Touch enchant!");
			else if(stack.getTagCompound().getBoolean("capitator"))
				list.add("\u00A77Destroys trees very quickly!");
			else if(stack.getTagCompound().getBoolean("nodrops"))
				list.add("\u00A77Gives no block drops.");
			else if(stack.getTagCompound().getBoolean("barmor"))
				list.add("\u00A77Deals damage that bypasses armor.");
		}
		list.add("");

		addDamageInfo(stack, this.damageVsEntity, list);
	}

	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase entity, EntityLivingBase entityBase)
	{
		if(!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());
		EntityPlayer player = (EntityPlayer) entityBase;
		if(this.getDamage(stack) >= 998 && player.capabilities.isCreativeMode == false)
		{
			NBTTagCompound tag = stack.getTagCompound();
			tag.setBoolean("destroyed", true);
			stack.setTagCompound(tag);
		}
		if(stack.getTagCompound().getBoolean("destroyed"))
			return false;
		stack.damageItem(2, entityBase);
		return true;
	}

	@Override
	public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, EntityPlayer player)
	{
		if(!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());

		if(this.getDamage(stack) >= 998 && player.capabilities.isCreativeMode == false)
		{
			NBTTagCompound tag = stack.getTagCompound();
			tag.setBoolean("destroyed", true);
			stack.setTagCompound(tag);
		}
		if(stack.getTagCompound().getBoolean("destroyed"))
			return true;

		World world = player.worldObj;

		if(stack.getTagCompound().getBoolean("capitator"))
		{
			Block block = world.getBlockState(pos).getBlock();
			if(block != null && block.isWood(world, pos))
			{
				breakTree(world, stack, block, pos, player);
				return false;
			}
		}

		destroyBlock(stack, world, pos, player);

		return false;
	}

	private void breakTree(World world, ItemStack stack, Block block, BlockPos pos, EntityPlayer player)
	{
		for(int xPos = pos.getX() - 1;xPos <= pos.getX() + 1;xPos++)
			for(int yPos = pos.getY() - 1;yPos <= pos.getY() + 1;yPos++)
				for(int zPos = pos.getZ() - 1;zPos <= pos.getZ() + 1;zPos++)
				{
					BlockPos newPos = new BlockPos(xPos, yPos, zPos);
					Block nblock = world.getBlockState(newPos).getBlock();

					if(block == nblock)
					{
						if(this.getDamage(stack) >= 998 && player.capabilities.isCreativeMode == false)
						{
							NBTTagCompound tag = stack.getTagCompound();
							tag.setBoolean("destroyed", true);
							stack.setTagCompound(tag);
						}
						if(stack.getTagCompound().getBoolean("destroyed"))
							return;

						destroyBlock(stack, world, newPos, player);
						breakTree(world, stack, block, newPos, player);
					}
				}
	}

	private void destroyBlock(ItemStack stack, World world, BlockPos pos, EntityPlayer player)
	{
		IBlockState blockState = world.getBlockState(pos);

		if(blockState != null)
		{
			Block block = blockState.getBlock();

			int hlvl = block.getHarvestLevel(blockState);

			String tool = block.getHarvestTool(blockState);

			if(hlvl <= 3 && (tool == null || tool.equals("axe")))
			{
				if(player.canPlayerEdit(pos, EnumFacing.DOWN, stack))
					if(!player.capabilities.isCreativeMode)
					{
						if(stack.getTagCompound().getBoolean("nodrops"))
						{
							this.onBlockDestroyed(stack, world, blockState, pos, player);

							if(!world.isRemote)
								world.setBlockToAir(pos);
						}
						else if(stack.getTagCompound().getBoolean("silk-touch"))
						{
							ItemStack result = new ItemStack(block, 1, block.getMetaFromState(blockState));

							this.onBlockDestroyed(stack, world, blockState, pos, player);
							ToolsHelper.spawnStackInWorld(world, pos, result);
						}
						else if(stack.getTagCompound().getBoolean("auto-smelt"))
						{
							ItemStack smeltStack =
								new ItemStack(block.getItemDropped(blockState, random, 0), block.quantityDropped(
									blockState, 0, random), block.damageDropped(blockState));

							if(smeltStack.getItem() != null)
							{
								ItemStack result = FurnaceRecipes.instance().getSmeltingResult(smeltStack);

								if(result != null)
								{
									result.stackSize = block.quantityDropped(blockState, 0, random);

									this.onBlockDestroyed(stack, world, blockState, pos, player);
									ToolsHelper.spawnStackInWorld(world, pos, result);
								}
							}
						}
						else if(stack.getTagCompound().getBoolean("capitator"))
						{
							ItemStack result =
								new ItemStack(block.getItemDropped(blockState, random, 0), block.quantityDropped(
									blockState, 0, random), block.damageDropped(blockState));

							if(EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) > 0)
								result = new ItemStack(block, 1, block.getMetaFromState(blockState));

							byte fortune = (byte) EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack);

							if(fortune > 0)
								result =
									new ItemStack(block.getItemDropped(blockState, random, fortune),
										block.quantityDropped(blockState, fortune, random),
										block.damageDropped(blockState));

							this.onBlockDestroyed(stack, world, blockState, pos, player);
							ToolsHelper.spawnStackInWorld(world, pos, result);
						}
					}
					else
						world.setBlockToAir(pos);
			}
		}
	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World world, IBlockState block, BlockPos pos, EntityLivingBase entity)
	{
		if(block != null && block.getBlockHardness(world, pos) != 0.0D)
		{
			stack.damageItem(1, entity);
		}
		return true;
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
				AdvTInventory inv = new AdvTInventory(player.inventory.getCurrentItem(), player);
				inv.setCharge();
			}
		player.setActiveHand(hand);
		return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
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
		else if(stack.hasTagCompound() && stack.getTagCompound().getByte("speed") == 1)
			eff += 2.0F;

		Block block = state.getBlock();

		if(block.getHarvestLevel(state) <= this.getToolMaterial().getHarvestLevel()
			&& block.isToolEffective("axe", state))
			return eff;

		Material mat = state.getMaterial();
		for(Material m : GAMod.axeMaterials)
			if(m == mat)
				return eff;

		return 1.0F;
	}

    @Override
    public Set<String> getToolClasses(ItemStack stack)
    {
        return com.google.common.collect.ImmutableSet.of("axe");
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack)
    {
        Multimap<String, AttributeModifier> multimap = HashMultimap.<String, AttributeModifier>create();

        if (slot == EntityEquipmentSlot.MAINHAND)
        {
        	NBTTagCompound tag = stack.getTagCompound();
        	if(tag == null || !stack.getTagCompound().getBoolean("destroyed")) {
        		float damage = this.damageVsEntity + (tag != null ? tag.getByte("damage") : 0);
        		multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getAttributeUnlocalizedName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Tool modifier", damage, 0));
        		multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getAttributeUnlocalizedName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Tool modifier", this.attackSpeed, 0));
        	}
        }

        return multimap;
    }

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		AdvTInventory inv = new AdvTInventory(player.getHeldItemMainhand(), player);
		return new AdvTGUI(inv, player);
	}

	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		AdvTInventory inv = new AdvTInventory(player.getHeldItemMainhand(), player);
		return new AdvTContainer(inv, player);
	}
}
