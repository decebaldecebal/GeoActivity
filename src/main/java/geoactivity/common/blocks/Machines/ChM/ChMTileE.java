package geoactivity.common.blocks.Machines.ChM;

import java.util.ArrayList;
import java.util.List;

import geoactivity.common.GAMod;
import geoactivity.common.blocks.HardenedBrick;
import geoactivity.common.blocks.HardenedBrick.EnumHardenedBrick;
import geoactivity.common.util.BaseTileEntity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ChMTileE extends BaseTileEntity
{
	public ChMTileE()
	{
		super(2);
	}

	private boolean checkNeighbors()
	{
		BlockPos temp;

		for(int i = -1;i <= 1;i += 2)
		{
			temp = pos.add(i, 0, i);
			if(worldObj.getBlockState(temp).getBlock() != GAMod.hardenedbrick
					|| worldObj.getBlockState(temp).getValue(HardenedBrick.TYPE) != EnumHardenedBrick.ADVANCED)
				return false;
			temp = pos.add(-i, 0, i);
			if(worldObj.getBlockState(temp).getBlock() != GAMod.hardenedbrick
					|| worldObj.getBlockState(temp).getValue(HardenedBrick.TYPE) != EnumHardenedBrick.ADVANCED)
				return false;
		}
		return true;
	}

	private boolean verifyEntities(List<EntityItemFrame> entity, ItemStack item)
	{
		int size = 0;
		for(int j = 0;j < entity.size();j++)
			if(entity.get(j).getDisplayedItem() != null)
				if(entity.get(j).getDisplayedItem().getItem() == item.getItem()
						&& entity.get(j).getDisplayedItem().getItemDamage() == item.getItemDamage())
					size++;
		if(size == item.stackSize)
			return true;
		return false;
	}

	private List<EntityItemFrame> verifyItem(ItemStack item, List<EntityItemFrame> lastEntity)
	{
		List<EntityItemFrame> entity = worldObj.getEntitiesWithinAABB(EntityItemFrame.class, new AxisAlignedBB(pos.getX() - 1.3,
						pos.getY() - 1, pos.getZ() - 1.3, pos.getX() + 0.3, pos.getY() + 1, pos.getZ() + 0.3));
		if(!entity.isEmpty() && entity.size() < 5 && !entity.equals(lastEntity))
		{
			if(verifyEntities(entity, item))
				return entity;
		}

		entity = worldObj.getEntitiesWithinAABB(EntityItemFrame.class, new AxisAlignedBB(pos.getX() + 0.7,
						pos.getY() - 1, pos.getZ() - 1.3, pos.getX() + 2.3, pos.getY() + 1, pos.getZ() + 0.3));
		if(!entity.isEmpty() && entity.size() < 5 && !entity.equals(lastEntity))
		{
			if(verifyEntities(entity, item))
				return entity;
		}

		entity = worldObj.getEntitiesWithinAABB(EntityItemFrame.class, new AxisAlignedBB(pos.getX() + 0.7,
						pos.getY() - 1, pos.getZ() + 0.7, pos.getX() + 2.3, pos.getY() + 1, pos.getZ() + 2.3));
		if(!entity.isEmpty() && entity.size() < 5 && !entity.equals(lastEntity))
		{
			if(verifyEntities(entity, item))
				return entity;
		}

		entity = worldObj.getEntitiesWithinAABB(EntityItemFrame.class, new AxisAlignedBB(pos.getX() - 1.3,
						pos.getY() - 1, pos.getZ() + 0.7, pos.getX() + 0.3, pos.getY() + 1, pos.getZ() + 2.3));
		if(!entity.isEmpty() && entity.size() < 5 && !entity.equals(lastEntity))
		{
			if(verifyEntities(entity, item))
				return entity;
		}

		return null;
	}

	public void craftRecipe()
	{
		if(worldObj.getBlockState(pos.add(0, 1, 0)).getBlock() == Blocks.COAL_BLOCK && checkNeighbors())
		{
			ItemStack inv1 = new ItemStack(Blocks.AIR);
			if(inventory[0] != null)
				inv1 = inventory[0].copy();

			ItemStack inv2 = new ItemStack(Blocks.AIR);
			if(inventory[1] != null)
				inv2 = inventory[1].copy();

			ItemStack[] items = ChMCrafting.getInstance().getItems(inv1, inv2);
			if(items == null)
				items = ChMCrafting.getInstance().getItems(inv2, inv1);
			if(items != null)
			{
				ItemStack result = items[0].copy();

				List<EntityItemFrame> allEntities = new ArrayList<EntityItemFrame>();
				List<EntityItemFrame> lastEntity = null;

				for(int i = 1;i <= 4;i++)
				{
					if(items[i] != null)
					{
						List<EntityItemFrame> entity = verifyItem(items[i], lastEntity);

						if(entity == null)
							return ;

						lastEntity = entity;
						allEntities.addAll(entity);
					}
				}

				for(int j = 0;j < allEntities.size();j++)
					if(allEntities.get(j).getDisplayedItem() != null)
						allEntities.get(j).setDisplayedItem(null);

				decrStackSize(0, 1);
				decrStackSize(1, 1);
				worldObj.setBlockToAir(pos.add(0, 1, 0));

				worldObj.spawnEntityInWorld(new EntityItem(worldObj, pos.getX(), pos.getY() + 1, pos.getZ(), result));
			}
		}
	}

	@Override
	public String getName()
	{
		return "ChemistryMachine";
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side)
	{
		return new int[] {0, 1};
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction)
	{
		return true;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction)
	{
		return true;
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		return new ChMGUI(player.inventory, this);
	}

	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		return new ChMContainer(this, player.inventory);
	}
}
