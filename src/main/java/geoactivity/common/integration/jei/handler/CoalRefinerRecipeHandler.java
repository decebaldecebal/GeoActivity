package geoactivity.common.integration.jei.handler;

import geoactivity.common.integration.jei.GeoActivityRecipeCategoryUid;
import geoactivity.common.integration.jei.wrapper.CoalRefinerRecipeWrapper;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

public class CoalRefinerRecipeHandler implements IRecipeHandler<CoalRefinerRecipeWrapper>
{
	@Override
	public Class<CoalRefinerRecipeWrapper> getRecipeClass()
	{
		return CoalRefinerRecipeWrapper.class;
	}

	@Override
	public String getRecipeCategoryUid()
	{
		return GeoActivityRecipeCategoryUid.COAL_REFINER;
	}

	@Override
	public IRecipeWrapper getRecipeWrapper(CoalRefinerRecipeWrapper recipe)
	{
		return recipe;
	}

	@Override
	public boolean isRecipeValid(CoalRefinerRecipeWrapper recipe)
	{
		return recipe.getInputs().size() > 0 && recipe.getOutputs().size() > 0;
	}

	@Override
	public String getRecipeCategoryUid(CoalRefinerRecipeWrapper recipe)
	{
		return GeoActivityRecipeCategoryUid.COAL_REFINER;
	}
}
