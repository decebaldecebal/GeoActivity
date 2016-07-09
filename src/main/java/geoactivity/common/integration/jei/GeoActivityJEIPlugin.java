package geoactivity.common.integration.jei;

import geoactivity.common.GAMod;
import geoactivity.common.blocks.Machines.ACR.ACRContainer;
import geoactivity.common.blocks.Machines.ACR.ACRGUI;
import geoactivity.common.blocks.Machines.AE.AEContainer;
import geoactivity.common.blocks.Machines.AE.AEGUI;
import geoactivity.common.blocks.Machines.CR.CRContainer;
import geoactivity.common.blocks.Machines.CR.CRGUI;
import geoactivity.common.blocks.Machines.ChM.ChMContainer;
import geoactivity.common.blocks.Machines.ChM.ChMGUI;
import geoactivity.common.blocks.Machines.CrM.CrMContainer;
import geoactivity.common.blocks.Machines.CrM.CrMCrafting;
import geoactivity.common.blocks.Machines.CrM.CrMGUI;
import geoactivity.common.integration.jei.category.AdvancedCoalRefinerRecipeCategory;
import geoactivity.common.integration.jei.category.AtomExtractorRecipeCategory;
import geoactivity.common.integration.jei.category.ChemistryMachineRecipeCategory;
import geoactivity.common.integration.jei.category.CoalRefinerRecipeCategory;
import geoactivity.common.integration.jei.category.CrMRecipeCategory;
import geoactivity.common.integration.jei.handler.AdvancedCoalRefinerRecipeHandler;
import geoactivity.common.integration.jei.handler.AtomExtractorRecipeHandler;
import geoactivity.common.integration.jei.handler.ChemistryMachineRecipeHandler;
import geoactivity.common.integration.jei.handler.CoalRefinerRecipeHandler;
import geoactivity.common.integration.jei.handler.CrMShapedRecipeHandler;
import geoactivity.common.integration.jei.handler.CrMShapelessRecipeHandler;
import geoactivity.common.util.Translator;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import net.minecraft.item.ItemStack;

@JEIPlugin
public class GeoActivityJEIPlugin implements IModPlugin
{
	public GeoActivityJEIPlugin()
	{}

	@Override
	public void register(IModRegistry registry)
	{
		IJeiHelpers jeiHelpers = registry.getJeiHelpers();
		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();

		registry.addRecipeCategories(
			new CrMRecipeCategory(guiHelper),
			new CoalRefinerRecipeCategory(guiHelper),
			new AdvancedCoalRefinerRecipeCategory(guiHelper),
			new AtomExtractorRecipeCategory(guiHelper),
			new ChemistryMachineRecipeCategory(guiHelper)
		);

		registry.addRecipeHandlers(
			new CrMShapedRecipeHandler(),
			new CrMShapelessRecipeHandler(),
			new CoalRefinerRecipeHandler(),
			new AdvancedCoalRefinerRecipeHandler(),
			new AtomExtractorRecipeHandler(),
			new ChemistryMachineRecipeHandler()
		);

		registry.addRecipeClickArea(CrMGUI.class, 84, 44, 24, 12, GeoActivityRecipeCategoryUid.CRAFTING_MACHINE);
		registry.addRecipeClickArea(CRGUI.class, 88, 36, 24, 12, GeoActivityRecipeCategoryUid.COAL_REFINER);
		registry.addRecipeClickArea(ACRGUI.class, 84, 34, 24, 12, GeoActivityRecipeCategoryUid.ADVANCED_COAL_REFINER);
		registry.addRecipeClickArea(AEGUI.class, 45, 30, 24, 12, GeoActivityRecipeCategoryUid.ATOM_EXTRACTOR);
		registry.addRecipeClickArea(ChMGUI.class, 76, 32, 24, 12, GeoActivityRecipeCategoryUid.CHEMISTRY_MACHINE);

		IRecipeTransferRegistry recipeTransferRegistry = registry.getRecipeTransferRegistry();
		recipeTransferRegistry.addRecipeTransferHandler(CrMContainer.class, GeoActivityRecipeCategoryUid.CRAFTING_MACHINE, 4, 9, 13, 36);
		recipeTransferRegistry.addRecipeTransferHandler(CRContainer.class, GeoActivityRecipeCategoryUid.COAL_REFINER, 0, 1, 3, 36);
		recipeTransferRegistry.addRecipeTransferHandler(ACRContainer.class, GeoActivityRecipeCategoryUid.ADVANCED_COAL_REFINER, 1, 2, 7, 36);
		recipeTransferRegistry.addRecipeTransferHandler(AEContainer.class, GeoActivityRecipeCategoryUid.ATOM_EXTRACTOR, 0, 2, 5, 36);
		recipeTransferRegistry.addRecipeTransferHandler(ChMContainer.class, GeoActivityRecipeCategoryUid.CHEMISTRY_MACHINE, 0, 2, 2, 36);

		registry.addRecipes(CrMCrafting.getInstance().getRecipeList());
		registry.addRecipes(CoalRefinerRecipeCategory.getCoalRefinerRecipes(jeiHelpers));
		registry.addRecipes(AdvancedCoalRefinerRecipeCategory.getAdvancedCoalRefinerRecipes(jeiHelpers));
		registry.addRecipes(AtomExtractorRecipeCategory.getAtomExtractorRecipes(jeiHelpers));
		registry.addRecipes(ChemistryMachineRecipeCategory.getChemistryMachineRecipes(jeiHelpers));

		registry.addDescription(new ItemStack(GAMod.advancedorerefiner), new String(Translator.translateToLocal("ga.jei.description.advanced_ore_refiner")));
		registry.addDescription(new ItemStack(GAMod.chemistrymachine), new String(Translator.translateToLocal("ga.jei.description.chemistry_machine")));
	}

	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime)
	{}

}
