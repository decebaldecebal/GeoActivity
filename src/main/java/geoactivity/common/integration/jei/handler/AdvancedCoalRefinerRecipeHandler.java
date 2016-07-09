package geoactivity.common.integration.jei.handler;

import geoactivity.common.integration.jei.GeoActivityRecipeCategoryUid;
import geoactivity.common.integration.jei.wrapper.AdvancedCoalRefinerRecipeWrapper;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

public class AdvancedCoalRefinerRecipeHandler implements IRecipeHandler<AdvancedCoalRefinerRecipeWrapper>
{
	@Override
	public Class<AdvancedCoalRefinerRecipeWrapper> getRecipeClass()
	{
		return AdvancedCoalRefinerRecipeWrapper.class;
	}

	@Override
	public String getRecipeCategoryUid(AdvancedCoalRefinerRecipeWrapper recipe)
	{
		return GeoActivityRecipeCategoryUid.ADVANCED_COAL_REFINER;
	}

	@Override
	public IRecipeWrapper getRecipeWrapper(AdvancedCoalRefinerRecipeWrapper recipe)
	{
		return recipe;
	}

	@Override
	public boolean isRecipeValid(AdvancedCoalRefinerRecipeWrapper recipe)
	{
		return recipe.getInputs().size() > 0 && recipe.getOutputs().size() > 0;
	}

	@Override
	public String getRecipeCategoryUid()
	{
		return GeoActivityRecipeCategoryUid.ADVANCED_COAL_REFINER;
	}
}
