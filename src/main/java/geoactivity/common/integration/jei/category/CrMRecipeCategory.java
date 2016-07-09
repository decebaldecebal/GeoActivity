package geoactivity.common.integration.jei.category;

import java.util.List;

import javax.annotation.Nonnull;

import geoactivity.common.integration.jei.GeoActivityRecipeCategoryUid;
import geoactivity.common.integration.jei.wrapper.CrMShapedRecipeWrapper;
import geoactivity.common.integration.jei.wrapper.CrMShapelessRecipeWrapper;
import geoactivity.common.util.GeneralHelper;
import geoactivity.common.util.Translator;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.ICraftingGridHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class CrMRecipeCategory implements IRecipeCategory<IRecipeWrapper>
{
	@Nonnull
	private final IDrawable background;
	@Nonnull
	private final String localizedName;
	@Nonnull
	private final ICraftingGridHelper craftingGridHelper;
	@Nonnull
	protected final IDrawableAnimated flame;
	@Nonnull
	protected final IDrawableAnimated arrow;

	public CrMRecipeCategory(IGuiHelper guiHelper)
	{
		ResourceLocation location = new ResourceLocation("geoactivity", "textures/gui/CrM.png");

		background = guiHelper.createDrawable(location, 4, 4, 169, 78);
		localizedName = Translator.translateToLocal("ga.jei.category.crafting_machine");
		craftingGridHelper = guiHelper.createCraftingGridHelper(0, 9);

		IDrawableStatic flameDrawable = guiHelper.createDrawable(location, 176, 0, 14, 14);
		flame = guiHelper.createAnimatedDrawable(flameDrawable, 164, IDrawableAnimated.StartDirection.TOP, true);

		IDrawableStatic arrowDrawable = guiHelper.createDrawable(location, 176, 14, 78, 16);
		arrow = guiHelper.createAnimatedDrawable(arrowDrawable, 80, IDrawableAnimated.StartDirection.LEFT, false);
	}

	@Override
	public String getUid()
	{
		return GeoActivityRecipeCategoryUid.CRAFTING_MACHINE;
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
		flame.draw(minecraft, 62, 59);
		arrow.draw(minecraft, 57, 37);
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, IRecipeWrapper recipeWrapper)
	{
		recipeLayout.setRecipeTransferButton(148, 60);
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

		guiItemStacks.init(9, false, 145, 37); //init output slot
		guiItemStacks.init(10, false, 84, 57); //init fuel slot
		guiItemStacks.init(11, false, 84, 19); //init perk slot

		for (int y = 0; y < 3; ++y) {
			for (int x = 0; x < 3; ++x) {
				int index = x + (y * 3);
				guiItemStacks.init(index, true, x * 18 + 1, y * 18 + 1); //init input slots
			}
		}

		if (recipeWrapper instanceof CrMShapedRecipeWrapper)
		{
			CrMShapedRecipeWrapper wrapper = (CrMShapedRecipeWrapper) recipeWrapper;

			craftingGridHelper.setInput(guiItemStacks, wrapper.getCraftingInputs()); //set input slots
			craftingGridHelper.setOutput(guiItemStacks, wrapper.getOutputs()); //set output slots
			guiItemStacks.set(10, GeneralHelper.getCraftingMachineFuels()); //set fuel slot

			List<ItemStack> perkList = wrapper.getCompatiblePerks();

			if (perkList != null)
				guiItemStacks.set(11, perkList);
		}
		else if (recipeWrapper instanceof CrMShapelessRecipeWrapper)
		{
			CrMShapelessRecipeWrapper wrapper = (CrMShapelessRecipeWrapper) recipeWrapper;

			craftingGridHelper.setInput(guiItemStacks, wrapper.getCraftingInputs()); //set input slots
			craftingGridHelper.setOutput(guiItemStacks, wrapper.getOutputs()); //set output slots
			guiItemStacks.set(10, GeneralHelper.getCraftingMachineFuels()); //set fuel slot

			List<ItemStack> perkList = wrapper.getCompatiblePerks();

			if (perkList != null)
				guiItemStacks.set(11, perkList);
		}
		else
		{
			System.out.println("RecipeWrapper is not a known crafting wrapper type: " + recipeWrapper.toString());
		}
	}
}
