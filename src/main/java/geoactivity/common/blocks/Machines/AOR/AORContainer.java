package geoactivity.common.blocks.Machines.AOR;

import geoactivity.common.GAMod;
import geoactivity.common.blocks.Machines.ACR.PerkSlot;
import geoactivity.common.blocks.Machines.CrM.FuelSlot;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

public class AORContainer extends Container
{
	protected AORTileE tile_entity;
	private int lastCookTime = 0;
	private int lastBurnTime = 0;
	private int lastItemBurnTime = 0;
	private byte lastperkEff = 0, lastperkSpeed = 0, lastfurnaceSpecial = 0;

	public AORContainer(AORTileE inv, InventoryPlayer playerInv)
	{
		tile_entity = inv;

		this.addSlotToContainer(new PerkSlot(inv, 0, 8, 8));
		this.addSlotToContainer(new PerkSlot(inv, 1, 8, 28));
		this.addSlotToContainer(new PerkSlot(inv, 2, 8, 48));
		this.addSlotToContainer(new PerkSlot(inv, 3, 8, 68));

		this.addSlotToContainer(new OreSlot(inv, 4, 41, 18));
		this.addSlotToContainer(new OreSlot(inv, 5, 125, 18));
		this.addSlotToContainer(new OreSlot(inv, 6, 41, 58));
		this.addSlotToContainer(new OreSlot(inv, 7, 125, 58));

		this.addSlotToContainer(new AORSlot(playerInv.player, inv, 8, 74, 18));
		this.addSlotToContainer(new AORSlot(playerInv.player, inv, 9, 92, 18));
		this.addSlotToContainer(new AORSlot(playerInv.player, inv, 10, 74, 58));
		this.addSlotToContainer(new AORSlot(playerInv.player, inv, 11, 92, 58));

		this.addSlotToContainer(new FuelSlot(inv, 12, 134, 99));
		this.addSlotToContainer(new WoodSlot(inv, 13, 98, 99));
		this.addSlotToContainer(new SpecialSlot(inv, 14, 8, 99));


		int var3;

		for (var3 = 0; var3 < 3; ++var3)
			for (int var4 = 0; var4 < 9; ++var4)
				this.addSlotToContainer(new Slot(playerInv, var4 + var3 * 9 + 9, 8 + var4 * 18, 124 + var3 * 18));

		for (var3 = 0; var3 < 9; ++var3)
			this.addSlotToContainer(new Slot(playerInv, var3, 8 + var3 * 18, 182));
	}

	@Override
    public void addListener(IContainerListener listener)
    {
        super.addListener(listener);
        listener.sendAllWindowProperties(this, this.tile_entity);
    }

	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();

		for (int var1 = 0; var1 < listeners.size(); ++var1)
		{
			IContainerListener var2 = listeners.get(var1);

			if (lastCookTime != tile_entity.furnaceCookTime)
				var2.sendProgressBarUpdate(this, 0, tile_entity.furnaceCookTime);

			if (lastBurnTime != tile_entity.furnaceBurnTime)
				var2.sendProgressBarUpdate(this, 1, tile_entity.furnaceBurnTime);

			if (lastItemBurnTime != tile_entity.currentItemBurnTime)
				var2.sendProgressBarUpdate(this, 2, tile_entity.currentItemBurnTime);

			if (lastperkEff != tile_entity.perkEff)
				var2.sendProgressBarUpdate(this, 3, tile_entity.perkEff);

			if (lastperkSpeed != tile_entity.perkSpeed)
				var2.sendProgressBarUpdate(this, 4, tile_entity.perkSpeed);

			if (lastfurnaceSpecial != tile_entity.furnaceSpecial)
				var2.sendProgressBarUpdate(this, 5, tile_entity.furnaceSpecial);
		}

		lastCookTime = tile_entity.furnaceCookTime;
		lastBurnTime = tile_entity.furnaceBurnTime;
		lastItemBurnTime = tile_entity.currentItemBurnTime;
		lastperkEff = tile_entity.perkEff;
		lastperkSpeed =  tile_entity.perkSpeed;
		lastfurnaceSpecial = tile_entity.furnaceSpecial;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int par1, int par2)
	{
		if (par1 == 0)
			tile_entity.furnaceCookTime = par2;

		if (par1 == 1)
			tile_entity.furnaceBurnTime = par2;

		if (par1 == 2)
			tile_entity.currentItemBurnTime = par2;

		if (par1 == 3)
			tile_entity.perkEff = (byte)par2;

		if (par1 == 4)
			tile_entity.perkSpeed = (byte)par2;

		if (par1 == 5)
			tile_entity.furnaceSpecial = (byte)par2;
	}

	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return tile_entity.isUseableByPlayer(player);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int toSlot)
	{
		ItemStack tempStack = null;
		Slot invSlot = inventorySlots.get(toSlot);

		if (invSlot != null && invSlot.getHasStack())
		{
			ItemStack tempStack2 = invSlot.getStack();
			tempStack = tempStack2.copy();

			Item item = tempStack2.getItem();

			if(toSlot >= 15 && toSlot < 42)
			{
				if (item == GAMod.gemLigniteCoal || item == GAMod.gemBituminousCoal || item == GAMod.gemAnthraciteCoal)
				{
					if (!this.mergeItemStack(tempStack2, 12, 13, false))
					{
						if(!this.mergeItemStack(tempStack2, 42, 51, false))
							return null;
					}
				}
				else if(item == GAMod.machine_perks)
				{
					if (!this.mergeItemStack(tempStack2, 0, 4, false))
					{
						if(!this.mergeItemStack(tempStack2, 42, 51, false))
							return null;
					}
				}
				else if(item == GAMod.carbonOreCutter)
				{
					if (!this.mergeItemStack(tempStack2, 14, 15, false))
					{
						if(!this.mergeItemStack(tempStack2, 42, 51, false))
							return null;
					}
				}
				else if (Block.getBlockFromItem(item) != null)
				{
					boolean wood = false;

					for (int id : OreDictionary.getOreIDs(tempStack2))
					{
						if (id == OreDictionary.getOreID("logWood"))
						{
							wood = true;
							break ;
						}
					}

					if(OreSlot.isItemStackValidOre(tempStack2))
					{
						if(!this.mergeItemStack(tempStack2, 4, 8, false))
							if(!this.mergeItemStack(tempStack2, 42, 51, false))
								return null;
					}
					else if(wood)
					{
						if(!this.mergeItemStack(tempStack2, 13, 14, false))
							if(!this.mergeItemStack(tempStack2, 42, 51, false))
								return null;
					}
					else if(!this.mergeItemStack(tempStack2, 42, 51, false))
							return null;
				}
				else if(!this.mergeItemStack(tempStack2, 42, 51, false))
					return null;
			}
			else if(toSlot >= 42 && toSlot < 51)
			{
				if(!this.mergeItemStack(tempStack2, 15, 42, false))
					return null;
			}
			else if(!this.mergeItemStack(tempStack2, 15, 51, false))
				return null;

			if (tempStack2.stackSize == 0)
				invSlot.putStack((ItemStack) null);
			else
				invSlot.onSlotChanged();

			if (tempStack2.stackSize == tempStack.stackSize)
				return null;
			invSlot.onPickupFromSlot(player, tempStack2);
		}
		return tempStack;
	}
}
