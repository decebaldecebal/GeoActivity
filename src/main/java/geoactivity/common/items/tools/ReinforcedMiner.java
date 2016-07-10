package geoactivity.common.items.tools;

import java.util.List;
import java.util.Set;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import geoactivity.common.GAMod;
import geoactivity.common.GeoActivity;
import geoactivity.common.items.tools.RMLogic.RMContainer;
import geoactivity.common.items.tools.RMLogic.RMGUI;
import geoactivity.common.items.tools.RMLogic.RMInventory;
import geoactivity.common.lib.IHasName;
import geoactivity.common.lib.IOpenableGUI;
import geoactivity.common.lib.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ReinforcedMiner extends ItemTool implements IHasName, IOpenableGUI
{
	private String name;

	public ReinforcedMiner(String name)
	{
		super(2, -3.0f, GAMod.ReinforcedMaterial, null);
		this.name = name;
		this.setUnlocalizedName(name);
		this.setCreativeTab(GeoActivity.tabMain);
		this.setNoRepair();
		this.setHarvestLevel("pickaxe", GAMod.ReinforcedMaterial.getHarvestLevel());
		this.setHarvestLevel("shovel", GAMod.ReinforcedMaterial.getHarvestLevel());
		GameRegistry.register(this.setRegistryName(Reference.MOD_ID, name));
	}

	@Override
	public String getName()
	{
		return this.name;
	}

	@Override
	public String getName(int meta)
	{
		return this.getName();
	}

	@Override
	public void onCreated(ItemStack stack, World world, EntityPlayer player)
	{
		if(!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());
		NBTTagCompound tag = stack.getTagCompound();
		tag.setBoolean("destroyed", true);
		stack.setTagCompound(tag);
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean bool)
	{
		if(stack.hasTagCompound() && stack.getTagCompound().getBoolean("destroyed"))
			list.add("\u00A7cDestroyed");
	}

	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase entity, EntityLivingBase entityBase)
	{
		if(!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());
		EntityPlayer player = (EntityPlayer) entityBase;
		if(this.getDamage(stack) >= 498 && player.capabilities.isCreativeMode == false)
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

		if(this.getDamage(stack) >= 498 && player.capabilities.isCreativeMode == false)
		{
			NBTTagCompound tag = stack.getTagCompound();
			tag.setBoolean("destroyed", true);
			stack.setTagCompound(tag);
		}

		if(stack.getTagCompound().getBoolean("destroyed"))
			return true;
		return false;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand)
	{
		if(!world.isRemote)
			if(player.isSneaking())
				player.openGui(GeoActivity.instance, 0, world, (int) player.posX, (int) player.posY,
					(int) player.posZ);
			else
			{
				RMInventory inv = new RMInventory(player.inventory.getCurrentItem(), player);
				inv.setCharge();
			}
		return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
	}

	@Override
	public boolean canHarvestBlock(IBlockState state, ItemStack stack)
	{
		return this.getToolSpeed(state) > 1.0F;
	}

	@Override
	public float getStrVsBlock(ItemStack stack, IBlockState state)
	{
		return this.getToolSpeed(state);
	}

	private float getToolSpeed(IBlockState state)
	{
		Block block = state.getBlock();

		if(block.getHarvestLevel(state) <= this.getToolMaterial().getHarvestLevel()
			&& (block.isToolEffective("pickaxe", state)
			|| block.isToolEffective("shovel", state)))
			return this.efficiencyOnProperMaterial;

		Material mat = state.getMaterial();
		for(Material m : GAMod.minerMaterials)
			if(m == mat)
				return this.efficiencyOnProperMaterial;

		return 1.0F;
	}

    @Override
    public Set<String> getToolClasses(ItemStack stack)
    {
        return com.google.common.collect.ImmutableSet.of("pickaxe", "axe");
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack)
    {
        Multimap<String, AttributeModifier> multimap = HashMultimap.<String, AttributeModifier>create();

        if (slot == EntityEquipmentSlot.MAINHAND)
        {
        	if(stack.getTagCompound() == null || !stack.getTagCompound().getBoolean("destroyed")) {
        		multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getAttributeUnlocalizedName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Tool modifier", this.damageVsEntity, 0));
        		multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getAttributeUnlocalizedName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Tool modifier", this.attackSpeed, 0));
        	}
        }

        return multimap;
    }

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		RMInventory inv = new RMInventory(player.getHeldItemMainhand(), player);
		return new RMGUI(inv, player);
	}

	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		RMInventory inv = new RMInventory(player.getHeldItemMainhand(), player);
		return new RMContainer(inv, player);
	}
}
