package geoactivity.common.integration.jei.handler;

import geoactivity.common.integration.jei.GeoActivityRecipeCategoryUid;
import geoactivity.common.integration.jei.wrapper.AtomExtractorRecipeWrapper;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

public class AtomExtractorRecipeHandler implements IRecipeHandler<AtomExtractorRecipeWrapper>
{
	@Override
	public Class<AtomExtractorRecipeWrapper> getRecipeClass()
	{
		return AtomExtractorRecipeWrapper.class;
	}

	@Override
	public String getRecipeCategoryUid()
	{
		return GeoActivityRecipeCategoryUid.ATOM_EXTRACTOR;
	}

	@Override
	public IRecipeWrapper getRecipeWrapper(AtomExtractorRecipeWrapper recipe)
	{
		return recipe;
	}

	@Override
	public boolean isRecipeValid(AtomExtractorRecipeWrapper recipe)
	{
		return recipe.getInputs().size() > 0 && recipe.getOutputs().size() > 0;
	}

	@Override
	public String getRecipeCategoryUid(AtomExtractorRecipeWrapper recipe)
	{
		return GeoActivityRecipeCategoryUid.ATOM_EXTRACTOR;
	}
}
