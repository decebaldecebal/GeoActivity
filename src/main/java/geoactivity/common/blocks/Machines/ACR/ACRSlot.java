package geoactivity.common.blocks.Machines.ACR;

import geoactivity.common.blocks.Machines.CR.CRRecipes;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

public class ACRSlot extends Slot
{
	private EntityPlayer thePlayer;
	private int field_75228_b;

	public ACRSlot(EntityPlayer player, IInventory inv, int par3, int par4, int par5)
	{
		super(inv, par3, par4, par5);
		thePlayer = player;
	}

	@Override
	public boolean isItemValid(ItemStack par1ItemStack)
	{
		return false;
	}

	@Override
	public ItemStack decrStackSize(int par1)
	{
		if (this.getHasStack())
			field_75228_b += Math.min(par1, this.getStack().stackSize);

		return super.decrStackSize(par1);
	}

	@Override
	public void onPickupFromSlot(EntityPlayer par1EntityPlayer, ItemStack par2ItemStack)
	{
		this.onCrafting(par2ItemStack);
		super.onPickupFromSlot(par1EntityPlayer, par2ItemStack);
	}

	@Override
	protected void onCrafting(ItemStack par1ItemStack, int par2)
	{
		field_75228_b += par2;
		this.onCrafting(par1ItemStack);
	}

	@Override
	protected void onCrafting(ItemStack stack)
	{
		stack.onCrafting(thePlayer.worldObj, thePlayer, field_75228_b);

		if (!thePlayer.worldObj.isRemote)
		{
			int var2 = field_75228_b;
			float var3 = CRRecipes.getInstance().getExperience(stack);
			int var4;

			if (var3 == 0.0F)
				var2 = 0;
			else if (var3 < 1.0F)
			{
				var4 = MathHelper.floor_float(var2 * var3);

				if (var4 < MathHelper.ceiling_float_int(var2 * var3) && (float) Math.random() < var2 * var3 - var4)
					++var4;

				var2 = var4;
			}

			while (var2 > 0)
			{
				var4 = EntityXPOrb.getXPSplit(var2);
				var2 -= var4;
				thePlayer.worldObj.spawnEntityInWorld(new EntityXPOrb(thePlayer.worldObj, thePlayer.posX, thePlayer.posY + 0.5D,
						thePlayer.posZ + 0.5D, var4));
			}
		}

		field_75228_b = 0;
	}
}
