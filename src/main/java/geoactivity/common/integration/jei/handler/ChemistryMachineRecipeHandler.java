package geoactivity.common.integration.jei.handler;

import geoactivity.common.integration.jei.GeoActivityRecipeCategoryUid;
import geoactivity.common.integration.jei.wrapper.ChemistryMachineRecipeWrapper;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

public class ChemistryMachineRecipeHandler implements IRecipeHandler<ChemistryMachineRecipeWrapper>
{
	@Override
	public Class<ChemistryMachineRecipeWrapper> getRecipeClass()
	{
		return ChemistryMachineRecipeWrapper.class;
	}

	@Override
	public String getRecipeCategoryUid()
	{
		return GeoActivityRecipeCategoryUid.CHEMISTRY_MACHINE;
	}

	@Override
	public IRecipeWrapper getRecipeWrapper(ChemistryMachineRecipeWrapper recipe)
	{
		return recipe;
	}

	@Override
	public boolean isRecipeValid(ChemistryMachineRecipeWrapper recipe)
	{
		return recipe.getInputs().size() > 0 && recipe.getOutputs().size() > 0;
	}

	@Override
	public String getRecipeCategoryUid(ChemistryMachineRecipeWrapper recipe)
	{
		return GeoActivityRecipeCategoryUid.CHEMISTRY_MACHINE;
	}
}
