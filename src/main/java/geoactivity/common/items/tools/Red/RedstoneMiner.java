package geoactivity.common.items.tools.Red;

import java.util.Set;

import geoactivity.common.GAMod;
import geoactivity.common.lib.ToolsHelper;
import geoactivity.common.util.BaseRedstoneTool;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class RedstoneMiner extends BaseRedstoneTool
{
	public RedstoneMiner(String name)
	{
		super(name, GAMod.AdvancedMaterial, 998, 6);
		this.setHarvestLevel("pickaxe", GAMod.AdvancedMaterial.getHarvestLevel());
		this.setHarvestLevel("shovel", GAMod.AdvancedMaterial.getHarvestLevel());
	}

	@Override
	public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, EntityPlayer player)
	{
		if(!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());

		if(!isToolUsable(stack, player))
			return true;

		World world = player.worldObj;

		if(stack.getTagCompound().getBoolean("widerRadius"))
		{
			RayTraceResult mop = ToolsHelper.raytraceFromEntity(world, player, true, 5.0D);
	        if (mop == null)
	            return super.onBlockStartBreak(stack, pos, player);

	        int xRange = 1;
	        int yRange = 1;
	        int zRange = 1;
	        switch (mop.sideHit)
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
	        Block block = world.getBlockState(pos).getBlock();
	        if(block != null)
	        {
	        	BlockPos newPos = null;

	        	for (int xPos = pos.getX() - xRange; xPos <= pos.getX() + xRange; xPos++)
	        		for (int yPos = pos.getY() - yRange; yPos <= pos.getY() + yRange; yPos++)
	        			for (int zPos = pos.getZ() - zRange; zPos <= pos.getZ() + zRange; zPos++)
		                {
	        				if(!isToolUsable(stack, player))
	        					return true;

	        				newPos = new BlockPos(xPos, yPos, zPos);
		                	Block nblock = world.getBlockState(newPos).getBlock();

		                	if(block == nblock) {
		                  		destroyBlock(stack, world, newPos, player);
		                	}
		                }

		        return false;
	        }
		}
		else if(stack.getTagCompound().getBoolean("capitator"))
		{
	        Block block = world.getBlockState(pos).getBlock();
	        if(block != null)
	        {
	        	Block nblock = block;
	        	BlockPos newPos = pos;
	        	int yPos = pos.getY();
	        	while(yPos != pos.getY() + 10 && nblock != null && nblock==block)
	        	{
	        		if(!isToolUsable(stack, player))
	        			return true;

	        		newPos = newPos.add(0, 1, 0);
                	nblock = world.getBlockState(newPos).getBlock();

                	if(block==nblock) {
                  		destroyBlock(stack, world, newPos, player);
                	}

	        		yPos++;
	        	}

	        	newPos = pos;
	        	yPos = pos.getY()-1;
	        	nblock = block;
	        	while(yPos != pos.getY() - 10 && nblock != null && nblock == block)
	        	{
	        		if(!isToolUsable(stack, player))
	        			return true;

	        		newPos = newPos.add(0, -1, 0);
                	nblock = world.getBlockState(newPos).getBlock();

                	if(block==nblock) {
                		destroyBlock(stack, world, newPos, player);
                	}

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

	     	if(hlvl <= 3 && (tool==null || tool.equals("pickaxe") || tool.equals("shovel")))
	 	        super.destroyBlock(stack, world, pos, player, block, blockState);
        }
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

		if(stack.getTagCompound().getBoolean("widerRadius") || stack.getTagCompound().getBoolean("capitator"))
			eff -= 8.0F;

		if(stack.hasTagCompound() && stack.getTagCompound().getByte("speed") >= 1)
			eff += 2.0F * stack.getTagCompound().getByte("speed");

		Block block = state.getBlock();

		if (block.getHarvestLevel(state) <= this.getToolMaterial().getHarvestLevel()
				&& (block.isToolEffective("pickaxe", state)
						|| block.isToolEffective("shovel", state)))
			return eff;

		Material mat = state.getMaterial();
		for (Material m : GAMod.minerMaterials)
            if (m == mat)
                return eff;

		return 1.0F;
	}

    @Override
    public Set<String> getToolClasses(ItemStack stack)
    {
        return com.google.common.collect.ImmutableSet.of("pickaxe", "shovel");
    }
}
