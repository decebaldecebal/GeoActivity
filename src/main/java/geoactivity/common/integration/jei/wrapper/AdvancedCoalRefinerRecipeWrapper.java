package geoactivity.common.integration.jei.wrapper;

import java.awt.Color;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class AdvancedCoalRefinerRecipeWrapper implements IRecipeWrapper
{
	@Nonnull
	private final List<ItemStack> inputs;
	@Nonnull
	private final List<ItemStack> outputs;
	protected String experienceString;

	public AdvancedCoalRefinerRecipeWrapper(ItemStack input, ItemStack output, float experience)
	{
		this.inputs = Collections.singletonList(input);
		this.outputs = Collections.singletonList(output);
		this.experienceString = "EXP: " + experience;
	}

	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY)
	{
		if (this.experienceString != null) {
			FontRenderer fontRendererObj = minecraft.fontRendererObj;
			fontRendererObj.drawString(this.experienceString, 135 - fontRendererObj.getStringWidth(this.experienceString) / 2, 12, Color.gray.getRGB());
		}
	}

	@Override
	public List<ItemStack> getInputs()
	{
		return inputs;
	}

	@Override
	public List<ItemStack> getOutputs()
	{
		return outputs;
	}

	@Override
	public List<FluidStack> getFluidInputs()
	{
		return null;
	}

	@Override
	public List<FluidStack> getFluidOutputs()
	{
		return null;
	}

	@Override
	public void drawAnimations(Minecraft minecraft, int recipeWidth, int recipeHeight)
	{}

	@Override
	public List<String> getTooltipStrings(int mouseX, int mouseY)
	{
		return null;
	}

	@Override
	public boolean handleClick(Minecraft minecraft, int mouseX, int mouseY, int mouseButton)
	{
		return false;
	}
}
