package geoactivity.common;

import geoactivity.common.blocks.HardenedBrick;
import geoactivity.common.blocks.HardenedBrick.EnumHardenedBrick;
import geoactivity.common.blocks.Machines.ACR.ACRTileE;
import geoactivity.common.blocks.Machines.AE.AETileE;
import geoactivity.common.blocks.Machines.AOR.AORTileE;
import geoactivity.common.blocks.Machines.CR.CRTileE;
import geoactivity.common.blocks.Machines.ChM.ChMTileE;
import geoactivity.common.blocks.Machines.CrM.CrMTileE;
import geoactivity.common.blocks.Machines.GG.GGTileE;
import geoactivity.common.blocks.Machines.TC.TCTileE;
import geoactivity.common.blocks.Machines.Tiles.TGTileE;
import geoactivity.common.blocks.Machines.Tiles.TMTileE;
import geoactivity.common.items.ArmorPerks;
import geoactivity.common.items.ElementContainer;
import geoactivity.common.items.MachinePerks;
import geoactivity.common.items.PreciousAlloy;
import geoactivity.common.items.ToolPerks;
import geoactivity.common.lib.IHasName;
import geoactivity.common.lib.Reference;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

public class GAInit
{
	//TODO
	public static void init(FMLInitializationEvent event)
	{
		//Chests
//		ChestGenHooks.addItem(ChestGenHooks.DUNGEON_CHEST, new WeightedRandomChestContent(GAMod.gemBituminousCoal, 0, 3, 8, 1));
//		ChestGenHooks.addItem(ChestGenHooks.DUNGEON_CHEST, new WeightedRandomChestContent(GAMod.gemAnthraciteCoal, 0, 1, 2, 1));
//		ChestGenHooks.addItem(ChestGenHooks.MINESHAFT_CORRIDOR, new WeightedRandomChestContent(GAMod.gemBituminousCoal, 0, 4, 12, 1));
//		ChestGenHooks.addItem(ChestGenHooks.MINESHAFT_CORRIDOR, new WeightedRandomChestContent(GAMod.gemAnthraciteCoal, 0, 2, 3, 1));
//		ChestGenHooks.addItem(ChestGenHooks.VILLAGE_BLACKSMITH, new WeightedRandomChestContent(GAMod.gemAnthraciteCoal, 0, 2, 5, 1));

		// REGISTER GAME
		craftingRecipes();
		registerOreDict();
		registerSmelting();
		registerEntities();

		if(event.getSide() == Side.CLIENT)
		{
			registerItemRenderers();
			registerBlockRenderers();
		}
	}

	private static void registerOreDict()
	{
		OreDictionary.registerOre("gemAnthraciteCoal", GAMod.gemAnthraciteCoal);
		OreDictionary.registerOre("gemBituminousCoal", GAMod.gemBituminousCoal);
		OreDictionary.registerOre("gemLigniteCoal", GAMod.gemLigniteCoal);
	}

	private static void registerSmelting()
	{
		GameRegistry.addSmelting(GAMod.oreAnthracite, new ItemStack(GAMod.gemAnthraciteCoal), 4F);
		GameRegistry.addSmelting(GAMod.oreBituminous, new ItemStack(GAMod.gemBituminousCoal), 3F);
		GameRegistry.addSmelting(GAMod.oreLignite, new ItemStack(GAMod.gemLigniteCoal), 2F);

		GameRegistry.addSmelting(GAMod.elementContainer, new ItemStack(GAMod.elementContainer, 1, 0), 0.5F);
	}

	private static void registerEntities()
	{
		GameRegistry.registerTileEntity(CrMTileE.class, "CrMTileE");
		GameRegistry.registerTileEntity(CRTileE.class, "CRTileE");
		GameRegistry.registerTileEntity(ACRTileE.class, "ACRTileE");
		GameRegistry.registerTileEntity(AETileE.class, "AETileE");
		GameRegistry.registerTileEntity(ChMTileE.class, "ChMTileE");
		GameRegistry.registerTileEntity(AORTileE.class, "AORTileE");
		GameRegistry.registerTileEntity(TGTileE.class, "TGTileE");
		GameRegistry.registerTileEntity(TCTileE.class, "TCTileE");
		GameRegistry.registerTileEntity(GGTileE.class, "GGTileE");
		GameRegistry.registerTileEntity(TMTileE.class, "TMTileE");
	}

	private static void craftingRecipes()
	{
		// Tools
		GameRegistry.addRecipe(new ItemStack(GAMod.reinforcedSword, 1), new Object[]
		{" X ", " S ", " Y ", 'X', Items.IRON_INGOT, 'S', Items.IRON_SWORD, 'Y', Items.GOLD_INGOT});
		GameRegistry.addRecipe(new ItemStack(GAMod.reinforcedPickaxe, 1), new Object[]
		{"XXX", " S ", " Y ", 'X', Items.IRON_INGOT, 'S', Items.IRON_PICKAXE, 'Y', Items.GOLD_INGOT});
		GameRegistry.addRecipe(new ItemStack(GAMod.reinforcedAxe, 1), new Object[]
		{"XX ", "XS ", " Y ", 'X', Items.IRON_INGOT, 'S', Items.IRON_AXE, 'Y', Items.GOLD_INGOT});
		GameRegistry.addRecipe(new ItemStack(GAMod.reinforcedShovel, 1), new Object[]
		{" X ", " S ", " Y ", 'X', Items.IRON_INGOT, 'S', Items.IRON_SHOVEL, 'Y', Items.GOLD_INGOT});
		GameRegistry.addRecipe(new ItemStack(GAMod.reinforcedHoe, 1), new Object[]
		{"XX ", " S ", " Y ", 'X', Items.IRON_INGOT, 'S', Items.IRON_HOE, 'Y', Items.GOLD_INGOT});
		GameRegistry.addRecipe(new ItemStack(GAMod.reinforcedMiner, 1, 498), new Object[]
		{"YXZ", " X ", " S ", 'Y', new ItemStack(GAMod.reinforcedPickaxe, 1, OreDictionary.WILDCARD_VALUE), 'S', GAMod.carbonstick, 'X',
				GAMod.carbonfiber, 'Z',
				new ItemStack(GAMod.reinforcedShovel, 1, OreDictionary.WILDCARD_VALUE)});
		GameRegistry.addRecipe(new ItemStack(GAMod.autoSBuilder, 1, 998), new Object[]
		{" YZ", " XY", "X  ", 'X', GAMod.carbonstick, 'Z', new ItemStack(GAMod.preciousAlloy, 1, 0), 'Y', Blocks.REDSTONE_BLOCK});

		//Blocks
		GameRegistry.addRecipe(new ItemStack(GAMod.hardenedbrick, 8), new Object[]
		{"XYX", "YZY", "XYX", 'Z', GAMod.carbonstick, 'Y', Blocks.STONEBRICK, 'X', GAMod.graphite});
		GameRegistry.addRecipe(new ItemStack(GAMod.hardenedglass, 8), new Object[]
		{"XYX", "XZX", "XYX", 'X', Blocks.GLASS, 'Z', GAMod.carbonfiber, 'Y', GAMod.carbonstick});
		GameRegistry.addRecipe(new ItemStack(GAMod.graphitebase, 1), new Object[]
		{"XYX", "ZXZ", "XYX", 'X', GAMod.graphite, 'Z', GAMod.carbonfiber, 'Y', GAMod.carbonstick});
		GameRegistry.addRecipe(new ItemStack(GAMod.hardenedbrick, 1, 1), new Object[]
		{"XYX", "ZZZ", "XYX", 'Y', GAMod.carbonstick, 'Z', new ItemStack(GAMod.hardenedbrick, 1, 0), 'X', Items.GOLD_INGOT});
		GameRegistry.addRecipe(new ItemStack(GAMod.hardenedbrick, 1, 2), new Object[]
		{" W ", "ZYZ", "XXX", 'Y', GAMod.carbonstick, 'Z', new ItemStack(GAMod.hardenedbrick, 1, 0), 'X', GAMod.carbonfiber, 'W', Blocks.IRON_BLOCK});

		//Machines
		GameRegistry.addRecipe(new ItemStack(GAMod.coalrefiner, 1), new Object[]
		{"XYX", "WZW", "XYX", 'X', Items.GOLD_INGOT, 'W', Items.IRON_INGOT, 'Y', Blocks.FURNACE, 'Z', Items.COAL});
		GameRegistry.addRecipe(new ItemStack(GAMod.craftingmachine, 1), new Object[]
		{"XYX", "WZW", "XUX", 'X', Items.GLOWSTONE_DUST, 'Z', GAMod.graphitebase, 'Y', GAMod.coalrefiner, 'W', Items.DIAMOND, 'U', Items.QUARTZ});
		GameRegistry.addRecipe(new ItemStack(GAMod.advancedcoalrefiner, 2), new Object[]
		{"XYX", "WZW", "XYX", 'X', Items.IRON_INGOT, 'Z', GAMod.coalrefiner, 'Y', new ItemStack(GAMod.preciousAlloy, 1, 0), 'W', Items.QUARTZ});
		GameRegistry.addRecipe(new ItemStack(GAMod.atomextractor, 1), new Object[]
		{"XYX", "YWY", "XZX", 'Y', new ItemStack(GAMod.elementContainer, 1, 0), 'W', GAMod.graphitebase, 'X',
				new ItemStack(GAMod.preciousAlloy, 1, 0), 'Z',
				Items.EMERALD});
		GameRegistry.addRecipe(new ItemStack(GAMod.chemistrymachine, 1), new Object[]
		{"XXX", "WZW", "XXX", 'W', GAMod.graphitebase, 'X', new ItemStack(GAMod.elementContainer, 1, 0), 'Z',
				new ItemStack(GAMod.preciousAlloy, 1, 1)});
		GameRegistry.addRecipe(new ItemStack(GAMod.advancedorerefiner, 1), new Object[]
		{"XWX", "WZW", "XWX", 'W', Items.DIAMOND, 'X', Items.EMERALD, 'Z', GAMod.graphitebase});

		//Machines (energy)
		GameRegistry.addRecipe(new ItemStack(GAMod.thermicgenerator, 1), new Object[]
		{"XXX", "XZX", "XRX", 'X', Items.IRON_INGOT, 'Z', GAMod.graphitebase, 'R', Blocks.REDSTONE_BLOCK});
		GameRegistry.addRecipe(new ItemStack(GAMod.toolcharger, 1), new Object[]
		{"XRX", "WZW", "XWX", 'X', Items.IRON_INGOT, 'Z', GAMod.graphitebase, 'R', Blocks.REDSTONE_BLOCK, 'W', Items.REDSTONE});
		GameRegistry.addRecipe(new ItemStack(GAMod.geothermalgenerator, 1), new Object[]
		{"XRX", "WZW", "XRX", 'X', GAMod.carbonstick, 'Z', GAMod.graphitebase, 'R', Blocks.REDSTONE_BLOCK, 'W',
				new ItemStack(GAMod.hardenedbrick, 1, EnumHardenedBrick.NORMAL.getMetadata())});
		GameRegistry.addRecipe(new ItemStack(GAMod.thermicmelter, 1), new Object[]
		{"XRX", "RIR", "XZX", 'X', GAMod.carbonstick, 'Z', GAMod.graphitebase, 'R', Items.REDSTONE, 'I', Blocks.IRON_BLOCK});

		// Armor
		GameRegistry.addRecipe(new ItemStack(GAMod.reinforcedHelmet, 1), new Object[]
		{"YSY", "X X", 'X', Items.IRON_INGOT, 'S', Items.IRON_HELMET, 'Y', Items.GOLD_INGOT});
		GameRegistry.addRecipe(new ItemStack(GAMod.reinforcedChest, 1), new Object[]
		{"X X", "YSY", "XYX", 'X', Items.IRON_INGOT, 'S', Items.IRON_CHESTPLATE, 'Y', Items.GOLD_INGOT});
		GameRegistry.addRecipe(new ItemStack(GAMod.reinforcedPants, 1), new Object[]
		{"X X", "X X", "YSY", 'X', Items.IRON_INGOT, 'S', Items.IRON_LEGGINGS, 'Y', Items.GOLD_INGOT});
		GameRegistry.addRecipe(new ItemStack(GAMod.reinforcedBoots, 1), new Object[]
		{"X X", "YSY", 'X', Items.IRON_INGOT, 'S', Items.IRON_BOOTS, 'Y', Items.GOLD_INGOT});

		//Others
		GameRegistry.addRecipe(new ItemStack(Items.DIAMOND, 1), new Object[]
		{"YXY", "XZX", "YXY", 'X', GAMod.carbonfiber, 'Y', GAMod.carbonstick, 'Z', GAMod.graphite});
		GameRegistry.addRecipe(new ItemStack(Items.COAL, 1), new Object[]
		{"XX", "XX", 'X', GAMod.graphite});
		GameRegistry.addRecipe(new ItemStack(GAMod.carbonfiber, 1), new Object[]
		{"XXX", "XXX", 'X', GAMod.graphite});
		GameRegistry.addRecipe(new ItemStack(GAMod.carbonstick, 1), new Object[]
		{"X", "X", 'X', GAMod.carbonfiber});
		GameRegistry.addRecipe(new ItemStack(GAMod.preciousAlloy, 1, 0), new Object[]
		{"X", "Y", "X", 'X', Items.GOLD_INGOT, 'Y', Items.DIAMOND});
		GameRegistry.addRecipe(new ItemStack(GAMod.preciousAlloy, 1, 1), new Object[]
		{"XYX", "YXY", "XYX", 'Y', new ItemStack(GAMod.preciousAlloy, 1, 0), 'X', Items.EMERALD});
		GameRegistry.addRecipe(new ItemStack(GAMod.preciousAlloy, 1, 2),
			new Object[]
			{"XYX", "ZYZ", "XYX", 'Y', new ItemStack(GAMod.preciousAlloy, 1, 1), 'X', new ItemStack(GAMod.preciousAlloy, 1, 0), 'Z',
					Blocks.EMERALD_BLOCK});
		GameRegistry.addRecipe(new ItemStack(GAMod.elementContainer, 8, 0), new Object[]
		{" Y ", "XZX", " Y ", 'Y', GAMod.carbonfiber, 'X', GAMod.carbonstick, 'Z', GAMod.graphite});
	}

	@SideOnly(Side.CLIENT)
	private static void registerItemRenderers()
	{
		RenderItem ri = Minecraft.getMinecraft().getRenderItem();

		registerItemTexture(ri, GAMod.gemLigniteCoal);
		registerItemTexture(ri, GAMod.gemBituminousCoal);
		registerItemTexture(ri, GAMod.gemAnthraciteCoal);

		registerItemTexture(ri, GAMod.graphite);
		registerItemTexture(ri, GAMod.carbonfiber);
		registerItemTexture(ri, GAMod.carbonstick);
		registerItemTexture(ri, GAMod.carbonOreCutter);

		for(int i = 0;i < PreciousAlloy.alloy_names.length;i++)
			registerItemTexture(ri, GAMod.preciousAlloy, i);

		for(int i = 0;i < ToolPerks.EnumToolPerks.values().length;i++)
			registerItemTexture(ri, GAMod.tool_perks, i);

		for(int i = 0;i < MachinePerks.EnumMachinePerks.values().length;i++)
			registerItemTexture(ri, GAMod.machine_perks, i);

		for(int i = 0;i < ArmorPerks.EnumArmorPerks.values().length;i++)
			registerItemTexture(ri, GAMod.armor_perks, i);

		for(int i = 0;i < ElementContainer.EnumElements.values().length;i++)
			registerItemTexture(ri, GAMod.elementContainer, i);

		registerItemTexture(ri, GAMod.autoSBuilder);

		/* Tools */

		//Tier I
		registerItemTexture(ri, GAMod.reinforcedSword);
		registerItemTexture(ri, GAMod.reinforcedPickaxe);
		registerItemTexture(ri, GAMod.reinforcedAxe);
		registerItemTexture(ri, GAMod.reinforcedShovel);
		registerItemTexture(ri, GAMod.reinforcedHoe);
		registerItemTexture(ri, GAMod.reinforcedMiner);

		//Tier II
		registerItemTexture(ri, GAMod.advancedSword);
		registerItemTexture(ri, GAMod.advancedPickaxe);
		registerItemTexture(ri, GAMod.advancedAxe);
		registerItemTexture(ri, GAMod.advancedShovel);

		//Tier III
		registerItemTexture(ri, GAMod.redstoneMiner);
		registerItemTexture(ri, GAMod.redstoneBattleAxe);
		registerItemTexture(ri, GAMod.redstoneBattleMiner);
		registerItemTexture(ri, GAMod.aredstoneBattleMiner);

		/* Armor */

		//Tier I
		registerItemTexture(ri, GAMod.reinforcedHelmet);
		registerItemTexture(ri, GAMod.reinforcedChest);
		registerItemTexture(ri, GAMod.reinforcedPants);
		registerItemTexture(ri, GAMod.reinforcedBoots);

		//Tier II
		registerItemTexture(ri, GAMod.advancedHelmet);
		registerItemTexture(ri, GAMod.advancedChest);
		registerItemTexture(ri, GAMod.advancedPants);
		registerItemTexture(ri, GAMod.advancedBoots);
	}

	@SideOnly(Side.CLIENT)
	private static void registerBlockRenderers()
	{
		RenderItem ri = Minecraft.getMinecraft().getRenderItem();

		registerBlockTexture(ri, GAMod.oreLignite);
		registerBlockTexture(ri, GAMod.oreBituminous);
		registerBlockTexture(ri, GAMod.oreAnthracite);

		registerBlockTexture(ri, GAMod.hardenedglass);
		registerBlockTexture(ri, GAMod.graphitebase);

		for(int i = 0;i < HardenedBrick.EnumHardenedBrick.values().length;i++)
			registerBlockTexture(ri, GAMod.hardenedbrick, i);

		registerBlockTexture(ri, GAMod.coalrefiner);
		registerBlockTexture(ri, GAMod.craftingmachine);
		registerBlockTexture(ri, GAMod.atomextractor);
		registerBlockTexture(ri, GAMod.chemistrymachine);
		registerBlockTexture(ri, GAMod.advancedcoalrefiner);
		registerBlockTexture(ri, GAMod.advancedorerefiner);

		registerBlockTexture(ri, GAMod.thermicgenerator);
		registerBlockTexture(ri, GAMod.toolcharger);
		registerBlockTexture(ri, GAMod.geothermalgenerator);
		registerBlockTexture(ri, GAMod.thermicmelter);
	}

	private static void registerItemTexture(RenderItem renderItem, Item item, int meta)
	{
		renderItem.getItemModelMesher().register(item, meta,
			new ModelResourceLocation(Reference.MOD_ID + ":" + ((IHasName) item).getName(meta), "inventory"));
	}

	private static void registerItemTexture(RenderItem renderItem, Item item)
	{
		renderItem.getItemModelMesher().register(item, 0,
			new ModelResourceLocation(Reference.MOD_ID + ":" + ((IHasName) item).getName(), "inventory"));
	}

	private static void registerBlockTexture(RenderItem renderItem, Block block, int meta)
	{
		renderItem.getItemModelMesher().register(Item.getItemFromBlock(block), meta,
			new ModelResourceLocation(Reference.MOD_ID + ":" + ((IHasName) block).getName(meta), "inventory"));
	}

	private static void registerBlockTexture(RenderItem renderItem, Block block)
	{
		renderItem.getItemModelMesher().register(Item.getItemFromBlock(block), 0,
			new ModelResourceLocation(Reference.MOD_ID + ":" + ((IHasName) block).getName(), "inventory"));
	}
}
