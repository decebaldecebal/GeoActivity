package geoactivity.common.integration.jei.handler;

import geoactivity.common.blocks.Machines.CrM.recipes.CrMShapedRecipe;
import geoactivity.common.integration.jei.GeoActivityRecipeCategoryUid;
import geoactivity.common.integration.jei.wrapper.CrMShapedRecipeWrapper;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;

public class CrMShapedRecipeHandler implements IRecipeHandler<CrMShapedRecipe>
{
	@Override
	public Class<CrMShapedRecipe> getRecipeClass()
	{
		return CrMShapedRecipe.class;
	}

	@Override
	public String getRecipeCategoryUid()
	{
		return GeoActivityRecipeCategoryUid.CRAFTING_MACHINE;
	}

	@Override
	public IRecipeWrapper getRecipeWrapper(CrMShapedRecipe recipe)
	{
		return new CrMShapedRecipeWrapper(recipe);
	}

	@Override
	public boolean isRecipeValid(CrMShapedRecipe recipe)
	{
		if (recipe.getRecipeOutput() == null) {
			return false;
		}

		int inputCount = 0;
		for (ItemStack input : recipe.recipeItems) {
			if (input != null) {
				inputCount++;
			}
		}

		return inputCount > 0;
	}

	@Override
	public String getRecipeCategoryUid(CrMShapedRecipe recipe)
	{
		return GeoActivityRecipeCategoryUid.CRAFTING_MACHINE;
	}
}
