package geoactivity.common.integration.jei.category;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import geoactivity.common.blocks.Machines.ChM.ChMCrafting;
import geoactivity.common.integration.jei.GeoActivityRecipeCategoryUid;
import geoactivity.common.integration.jei.wrapper.ChemistryMachineRecipeWrapper;
import geoactivity.common.util.Translator;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ChemistryMachineRecipeCategory implements IRecipeCategory<ChemistryMachineRecipeWrapper>
{
	@Nonnull
	private final IDrawable background;
	@Nonnull
	private final String localizedName;

	public ChemistryMachineRecipeCategory(IGuiHelper guiHelper)
	{
		ResourceLocation location = new ResourceLocation("geoactivity", "textures/gui/ChM.png");

		background = guiHelper.createDrawable(location, 10, 10, 160, 65);
		localizedName = Translator.translateToLocal("ga.jei.category.chemistry_machine");
	}

	@Override
	public String getUid()
	{
		return GeoActivityRecipeCategoryUid.CHEMISTRY_MACHINE;
	}

	@Override
	public String getTitle()
	{
		return this.localizedName;
	}

	@Override
	public IDrawable getBackground()
	{
		return this.background;
	}

	@Override
	public void drawExtras(Minecraft minecraft)
	{}

	@Override
	public void drawAnimations(Minecraft minecraft)
	{}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, ChemistryMachineRecipeWrapper wrapper)
	{
		recipeLayout.setRecipeTransferButton(140, 24);
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

		guiItemStacks.init(0, true, 42, 20);
		guiItemStacks.init(1, true, 96, 20);
		guiItemStacks.init(2, false, 21, 0);
		guiItemStacks.init(3, false, 116, 0);
		guiItemStacks.init(4, false, 21, 40);
		guiItemStacks.init(5, false, 116, 40);
		guiItemStacks.init(6, false, 69, 20);

		ItemStack[] inputs = wrapper.getInputStacks();
		ItemStack[] outputs = wrapper.getOutputStacks();

		guiItemStacks.set(0, inputs[0]);
		guiItemStacks.set(1, inputs[1]);

		guiItemStacks.set(2, outputs[1]);
		guiItemStacks.set(3, outputs[2]);
		guiItemStacks.set(4, outputs[3]);
		guiItemStacks.set(5, outputs[4]);

		guiItemStacks.set(6, outputs[0]);
	}

	@Nonnull
	public static List<ChemistryMachineRecipeWrapper> getChemistryMachineRecipes(IJeiHelpers helpers)
	{
		Map<ItemStack[], ItemStack[]> smeltingMap = ChMCrafting.getInstance().getMetaSmeltingList();

		List<ChemistryMachineRecipeWrapper> recipes = new ArrayList<ChemistryMachineRecipeWrapper>();

		for (Map.Entry<ItemStack[], ItemStack[]> itemStackItemStackEntry : smeltingMap.entrySet())
		{
			ItemStack[] inputs = itemStackItemStackEntry.getKey();
			ItemStack[] outputs = itemStackItemStackEntry.getValue();

			ChemistryMachineRecipeWrapper recipe = new ChemistryMachineRecipeWrapper(inputs, outputs);
			recipes.add(recipe);
		}

		return recipes;
	}
}
