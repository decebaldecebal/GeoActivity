package geoactivity.common.integration.jei.wrapper;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import geoactivity.common.GAMod;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class AtomExtractorRecipeWrapper implements IRecipeWrapper
{
	@Nonnull
	private final List<ItemStack> inputs;
	@Nonnull
	private final ItemStack[] outputs;
	protected String timeString;
	private int elementContainerNumber;

	public AtomExtractorRecipeWrapper(ItemStack input, ItemStack[] outputs, int time, int elementContainerNumber)
	{
		this.inputs = Collections.singletonList(input);
		this.outputs = outputs;
		this.timeString = "Time: " + time/20 + " seconds";
		this.elementContainerNumber = elementContainerNumber;
	}

	@Override
	public List<ItemStack> getInputs()
	{
		List<ItemStack> allInputs = new ArrayList<ItemStack>();

		allInputs.addAll(this.inputs);
		allInputs.add(new ItemStack(GAMod.elementContainer, this.getElementContainerNumber()));

		return allInputs;
	}

	@Override
	public List<ItemStack> getOutputs()
	{
		return Arrays.asList(outputs);
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
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY)
	{
		if (timeString != null) {
			FontRenderer fontRendererObj = minecraft.fontRendererObj;
			fontRendererObj.drawString(timeString, 110 - fontRendererObj.getStringWidth(timeString) / 2, 4, Color.gray.getRGB());
		}
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

	public int getElementContainerNumber()
	{
		return this.elementContainerNumber;
	}

	public List<ItemStack> getInputStack()
	{
		return this.inputs;
	}

	public ItemStack[] getOutputElements()
	{
		return this.outputs;
	}
}
