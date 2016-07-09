package geoactivity.common.integration.jei.wrapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class ChemistryMachineRecipeWrapper implements IRecipeWrapper
{
	@Nonnull
	private final ItemStack[] inputs;
	@Nonnull
	private final ItemStack[] outputs;

	public ChemistryMachineRecipeWrapper(ItemStack[] inputs, ItemStack[] outputs)
	{
		this.inputs = inputs;
		this.outputs = outputs;
	}

	@Override
	public List<ItemStack> getInputs()
	{
		List<ItemStack> allInputs = new ArrayList<ItemStack>();

		allInputs.addAll(Arrays.asList(inputs));
		allInputs.add(outputs[1]);
		allInputs.add(outputs[2]);
		allInputs.add(outputs[3]);
		allInputs.add(outputs[4]);

		return allInputs;
	}

	@Override
	public List<ItemStack> getOutputs()
	{
		List<ItemStack> allOutputs = new ArrayList<ItemStack>(1);

		allOutputs.add(outputs[0]);

		return allOutputs;
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
	{}

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

	public ItemStack[] getInputStacks()
	{
		return this.inputs;
	}

	public ItemStack[] getOutputStacks()
	{
		return this.outputs;
	}
}
