package geoactivity.common.util;

import java.util.List;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import geoactivity.common.GeoActivity;
import geoactivity.common.items.tools.Red.Logic.RedContainer;
import geoactivity.common.items.tools.Red.Logic.RedGUI;
import geoactivity.common.items.tools.Red.Logic.RedInventory;
import geoactivity.common.lib.ToolsHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BaseRedstoneTool extends BaseGUITool
{
	protected int durability, damage;

	public BaseRedstoneTool(String name, ToolMaterial material, int durability, int damage)
	{
		super(name, material, 0.0f);

		this.durability = durability;
		this.damage = damage;
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

			if(stack.getTagCompound().getByte("speed") >= 1)
				list.add("\u00A77Has extra speed! [" + stack.getTagCompound().getByte("speed") + "]");
			if(stack.getTagCompound().getByte("efficiency") >= 1)
				list.add("\u00A77Uses less fuel! [" + stack.getTagCompound().getByte("efficiency") + "]");
			if(stack.getTagCompound().getBoolean("experience"))
				list.add("\u00A77Gives you more experience.");
			if(stack.getTagCompound().getBoolean("auto-smelt"))
				list.add("\u00A77Auto smelts!");
			if(stack.getTagCompound().getByte("fortune") >= 1)
				list.add("\u00A77Has Fortune " + stack.getTagCompound().getByte("fortune") + " enchant!");
			if(stack.getTagCompound().getBoolean("silk-touch"))
				list.add("\u00A77Has Silk-Touch enchant!");
			if(stack.getTagCompound().getBoolean("widerRadius"))
				list.add("\u00A77Has wider mining radius!");
			if(stack.getTagCompound().getBoolean("capitator"))
				list.add("\u00A77Destroys blocks in a vertical line.");
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
	public int getItemEnchantability()
	{
		return 0;
	}

	public boolean isToolUsable(ItemStack stack, EntityPlayer player)
	{
		if(this.getDamage(stack) >= this.durability && player.capabilities.isCreativeMode == false)
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

		stack.damageItem(1, entityBase);
		return true;
	}

	protected void breakTree(World world, ItemStack stack, BlockPos pos, EntityPlayer player)
	{
		for(int xPos = pos.getX() - 1;xPos <= pos.getX() + 1;xPos++)
			for(int yPos = pos.getY() - 1;yPos <= pos.getY() + 1;yPos++)
				for(int zPos = pos.getZ() - 1;zPos <= pos.getZ() + 1;zPos++)
				{
					BlockPos newPos = new BlockPos(xPos, yPos, zPos);
					Block nblock = world.getBlockState(newPos).getBlock();

					if(nblock.isWood(world, pos))
					{
						if(!isToolUsable(stack, player))
							return;

						destroyBlock(stack, world, newPos, player);
						breakTree(world, stack, newPos, player);
					}
				}
	}

	protected abstract void destroyBlock(ItemStack stack, World world, BlockPos pos, EntityPlayer player);

	protected void destroyBlock(ItemStack stack, World world, BlockPos pos, EntityPlayer player, Block block, IBlockState state)
	{
		if(player.canPlayerEdit(pos, EnumFacing.DOWN, stack))
			if(!player.capabilities.isCreativeMode)
			{
				if(stack.getTagCompound().getBoolean("experience"))
					if(block.getLocalizedName().contains("Ore")
						&& block.getItemDropped(block.getDefaultState(), world.rand, 0) != Item.getItemFromBlock(block))
						player.addExperience(1);

				if(stack.getTagCompound().getBoolean("nodrops"))
				{
					this.onBlockDestroyed(stack, world, state, pos, player);
					world.setBlockToAir(pos);
					world.playSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
						SoundEvents.BLOCK_STONE_BREAK, SoundCategory.BLOCKS, 1.0f,
						world.rand.nextFloat() * 0.1F + 0.9F, false);

					return;
				}
				else if(stack.getTagCompound().getBoolean("silk-touch"))
				{
					ItemStack result = new ItemStack(block, 1, block.getMetaFromState(state));

					this.onBlockDestroyed(stack, world, state, pos, player);
					ToolsHelper.spawnAlternateStack(stack, world, pos, result);
				}
				else
				{
					if(stack.getTagCompound().getBoolean("auto-smelt"))
					{
						ItemStack smeltStack = new ItemStack(block.getItemDropped(state, random, 0),
							block.quantityDropped(state, 0, random), block.damageDropped(state));

						if(smeltStack.getItem() != null)
						{
							ItemStack result = FurnaceRecipes.instance().getSmeltingResult(smeltStack);

							if(result != null)
							{
								result.stackSize = block.quantityDropped(state, 0, random);

								if(block.getLocalizedName().contains("Ore"))
									if(stack.getTagCompound().getByte("fortune") == 1)
										result.stackSize += world.rand.nextInt(2);
									else if(stack.getTagCompound().getByte("fortune") == 2)
										result.stackSize += 1;
									else if(stack.getTagCompound().getByte("fortune") == 3)
										result.stackSize += 1 + world.rand.nextInt(7) / 6;
									else if(stack.getTagCompound().getByte("fortune") == 4)
										result.stackSize += 1 + world.rand.nextInt(4) / 3;

								this.onBlockDestroyed(stack, world, state, pos, player);
								ToolsHelper.spawnAlternateStack(stack, world, pos, result);

								return;
							}
						}
					}
					if(stack.getTagCompound().getByte("fortune") >= 1)
					{
						ItemStack result = new ItemStack(block.getItemDropped(state, random, stack.getTagCompound().getByte("fortune")),
							block.quantityDropped(state, stack.getTagCompound().getByte("fortune"), random),
							block.damageDropped(state));

						this.onBlockDestroyed(stack, world, state, pos, player);
						ToolsHelper.spawnAlternateStack(stack, world, pos, result);

						return;
					}
					if(stack.getTagCompound().getBoolean("widerRadius") || stack.getTagCompound().getBoolean("capitator")
						|| stack.getTagCompound().getBoolean("droptp"))
					{
						ItemStack result = new ItemStack(block.getItemDropped(state, random, 0),
							block.quantityDropped(state, 0, random), block.damageDropped(state));

						this.onBlockDestroyed(stack, world, state, pos, player);
						ToolsHelper.spawnAlternateStack(stack, world, pos, result);
					}
				}
			}
			else
				world.setBlockToAir(pos);
	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World world, IBlockState block, BlockPos pos, EntityLivingBase entity)
	{
		if(block != null && block.getBlockHardness(world, pos) != 0.0D)
			stack.damageItem(1, entity);
		return true;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand)
    {
		if (!world.isRemote)
			if (player.isSneaking())
				player.openGui(GeoActivity.instance, 0, world, (int) player.posX, (int) player.posY,
						(int) player.posZ);
			else
			{
				RedInventory inv = new RedInventory(player.getHeldItemMainhand(), player, 8);
				inv.setCharge();
			}
		player.setActiveHand(hand);
		return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
    }

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		RedInventory inv = new RedInventory(player.getHeldItemMainhand(), player, 8);
		return new RedGUI(inv, player);
	}

	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		RedInventory inv = new RedInventory(player.getHeldItemMainhand(), player, 8);
		return new RedContainer(inv, player);
	}

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack)
    {
        Multimap<String, AttributeModifier> multimap = HashMultimap.<String, AttributeModifier>create();

        if (slot == EntityEquipmentSlot.MAINHAND)
        {
        	NBTTagCompound tag = stack.getTagCompound();
        	if(tag == null || !stack.getTagCompound().getBoolean("destroyed")) {
        		float damage = this.damage + (tag != null ? tag.getByte("damage") : 0);
        		multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getAttributeUnlocalizedName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Tool modifier", damage, 0));
        		multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getAttributeUnlocalizedName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Tool modifier", this.attackSpeed, 0));
        	}
        }

        return multimap;
    }
}
