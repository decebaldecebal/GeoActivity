package geoactivity.common.blocks.Machines;

import java.util.Random;

import geoactivity.common.GeoActivity;
import geoactivity.common.blocks.Machines.CrM.CrMTileE;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CraftingMachine extends BaseContainerBlock
{
	public CraftingMachine(String name)
	{
		super(Material.IRON, name, "pickaxe", 2);
		this.setHardness(3.0F);
		this.setResistance(15.0F);
		this.setSoundType(SoundType.STONE);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new CrMTileE();
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player,
		EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if(world.isRemote)
			return true;
		else
		{
			TileEntity tile_entity = world.getTileEntity(pos);
			if(tile_entity == null || player.isSneaking())
				return false;
			player.openGui(GeoActivity.instance, 0, world, pos.getX(), pos.getY(), pos.getZ());
			return true;
		}
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state)
	{
		CrMTileE tile = (CrMTileE) world.getTileEntity(pos);

		if(tile != null)
		{
			InventoryHelper.dropInventoryItems(world, pos, tile.craftMatrix);

			Random furnaceRand = new Random();
			for (int i = 1; i < tile.getSizeInventory(); i++)
			{
				ItemStack var9 = tile.getStackInSlot(i);

				if (var9 != null)
				{
					float var10 = furnaceRand.nextFloat() * 0.8F + 0.1F;
					float var11 = furnaceRand.nextFloat() * 0.8F + 0.1F;
					float var12 = furnaceRand.nextFloat() * 0.8F + 0.1F;

					while (var9.stackSize > 0)
					{
						int var13 = furnaceRand.nextInt(21) + 10;

						if (var13 > var9.stackSize)
							var13 = var9.stackSize;

						var9.stackSize -= var13;
						EntityItem entityItem = new EntityItem(world, pos.getX() + var10, pos.getY() + var11, pos.getZ() + var12,
								new ItemStack(var9.getItem(), var13,
								var9.getItemDamage()));

						if (var9.hasTagCompound())
							entityItem.getEntityItem().setTagCompound((NBTTagCompound) var9.getTagCompound().copy());

						float var15 = 0.05F;
						entityItem.motionX = (float) furnaceRand.nextGaussian() * var15;
						entityItem.motionY = (float) furnaceRand.nextGaussian() * var15 + 0.2F;
						entityItem.motionZ = (float) furnaceRand.nextGaussian() * var15;
						world.spawnEntityInWorld(entityItem);
					}
				}
			}

			world.updateComparatorOutputLevel(pos, this);
		}

		super.breakBlock(world, pos, state);
	}
}
