package geoactivity.common.integration.jei.category;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import geoactivity.common.blocks.Machines.CR.CRRecipes;
import geoactivity.common.integration.jei.GeoActivityRecipeCategoryUid;
import geoactivity.common.integration.jei.wrapper.AdvancedCoalRefinerRecipeWrapper;
import geoactivity.common.util.GeneralHelper;
import geoactivity.common.util.PerkHelper;
import geoactivity.common.util.Translator;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class AdvancedCoalRefinerRecipeCategory implements IRecipeCategory<AdvancedCoalRefinerRecipeWrapper>
{
	@Nonnull
	protected IDrawable background;
	@Nonnull
	protected String localizedName;
	@Nonnull
	protected IDrawableAnimated flame;
	@Nonnull
	protected IDrawableAnimated arrow;

	public AdvancedCoalRefinerRecipeCategory(IGuiHelper guiHelper)
	{
		ResourceLocation location = new ResourceLocation("geoactivity", "textures/gui/ACR.png");

		background = guiHelper.createDrawable(location, 4, 4, 170, 66);
		localizedName = Translator.translateToLocal("ga.jei.category.advanced_coal_refiner");

		IDrawableStatic flameDrawable = guiHelper.createDrawable(location, 176, 0, 14, 14);
		flame = guiHelper.createAnimatedDrawable(flameDrawable, 200, IDrawableAnimated.StartDirection.TOP, true);

		IDrawableStatic arrowDrawable = guiHelper.createDrawable(location, 176, 14, 22, 16);
		arrow = guiHelper.createAnimatedDrawable(arrowDrawable, 200, IDrawableAnimated.StartDirection.LEFT, false);
	}

	@Override
	public String getUid()
	{
		return GeoActivityRecipeCategoryUid.ADVANCED_COAL_REFINER;
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
	{
		flame.draw(minecraft, 57, 30);
		arrow.draw(minecraft, 85, 29);
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, AdvancedCoalRefinerRecipeWrapper wrapper)
	{
		recipeLayout.setRecipeTransferButton(140, 50);
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

		guiItemStacks.init(0, false, 36, 28);
		guiItemStacks.init(1, true, 75, 10);
		guiItemStacks.init(2, true, 75, 45);
		guiItemStacks.init(3, false, 112, 28);
		guiItemStacks.init(4, false, 138, 28);
		guiItemStacks.init(5, false, 3, 3);
		guiItemStacks.init(6, false, 3, 23);

		guiItemStacks.set(0, GeneralHelper.getCoalRefinerFuels());
		guiItemStacks.set(1, wrapper.getInputs());
		guiItemStacks.set(2, wrapper.getInputs());
		guiItemStacks.set(3, wrapper.getOutputs());
		guiItemStacks.set(4, wrapper.getOutputs());
		guiItemStacks.set(5, PerkHelper.getMachinePerks());
		guiItemStacks.set(6, PerkHelper.getMachinePerks());
	}

	@Nonnull
	public static List<AdvancedCoalRefinerRecipeWrapper> getAdvancedCoalRefinerRecipes(IJeiHelpers helpers)
	{
		Map<ItemStack, ItemStack> smeltingMap = CRRecipes.getInstance().getSmeltingList();

		List<AdvancedCoalRefinerRecipeWrapper> recipes = new ArrayList<AdvancedCoalRefinerRecipeWrapper>();

		for (Map.Entry<ItemStack, ItemStack> itemStackItemStackEntry : smeltingMap.entrySet())
		{
			ItemStack input = itemStackItemStackEntry.getKey();
			ItemStack output = itemStackItemStackEntry.getValue();

			float experience = CRRecipes.getInstance().getExperience(output);

			AdvancedCoalRefinerRecipeWrapper recipe = new AdvancedCoalRefinerRecipeWrapper(input, output, experience);
			recipes.add(recipe);
		}

		return recipes;
	}
}
