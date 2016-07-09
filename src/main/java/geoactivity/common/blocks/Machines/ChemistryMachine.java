package geoactivity.common.blocks.Machines;

import geoactivity.common.GeoActivity;
import geoactivity.common.blocks.Machines.ChM.ChMTileE;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class ChemistryMachine extends BaseContainerBlock
{
	public ChemistryMachine(String name)
	{
		super(Material.IRON, name, "pickaxe", 3);
		this.setHardness(10.0F);
		this.setResistance(20.0F);
		this.setSoundType(SoundType.STONE);
	}

	@Override
	public TileEntity createNewTileEntity(World var1,int var)
	{
		return new ChMTileE();
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player,
		EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
    {
		if (world.isRemote)
			return true;
		else
		{
			TileEntity tile_entity = world.getTileEntity(pos);
			if (tile_entity == null || player.isSneaking())
				return false;
			player.openGui(GeoActivity.instance, 0, world, pos.getX(), pos.getY(), pos.getZ());
			return true;
		}
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state)
    {
		ChMTileE tile = (ChMTileE) world.getTileEntity(pos);

		if (tile != null)
		{
			 InventoryHelper.dropInventoryItems(world, pos, tile);
             world.updateComparatorOutputLevel(pos, this);
		}

		super.breakBlock(world, pos, state);
	}

	@Override
	public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor)
	{
		ChMTileE tile = (ChMTileE) world.getTileEntity(pos);
		tile.craftRecipe();
	}
}
