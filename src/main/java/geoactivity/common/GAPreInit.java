package geoactivity.common;

import geoactivity.common.blocks.CustomCoalOres;
import geoactivity.common.blocks.HardenedBrick;
import geoactivity.common.blocks.TransparentBlock;
import geoactivity.common.blocks.Machines.AdvancedCoalRefiner;
import geoactivity.common.blocks.Machines.AdvancedOreRefiner;
import geoactivity.common.blocks.Machines.AtomExtractor;
import geoactivity.common.blocks.Machines.ChemistryMachine;
import geoactivity.common.blocks.Machines.CoalRefiner;
import geoactivity.common.blocks.Machines.CraftingMachine;
import geoactivity.common.blocks.Machines.GeothermalGenerator;
import geoactivity.common.blocks.Machines.ThermicGenerator;
import geoactivity.common.blocks.Machines.ThermicMelter;
import geoactivity.common.blocks.Machines.ToolCharger;
import geoactivity.common.itemblocks.BrickItemBlock;
import geoactivity.common.itemblocks.MultiItemBlock;
import geoactivity.common.items.ArmorPerks;
import geoactivity.common.items.AutoStoneBuilder;
import geoactivity.common.items.BaseItem;
import geoactivity.common.items.CarbonOreCutter;
import geoactivity.common.items.CustomCoalItems;
import geoactivity.common.items.ElementContainer;
import geoactivity.common.items.MachinePerks;
import geoactivity.common.items.PreciousAlloy;
import geoactivity.common.items.ToolPerks;
import geoactivity.common.items.armor.AdvancedArmor;
import geoactivity.common.items.armor.ReinforcedArmor;
import geoactivity.common.items.tools.ReinforcedAxe;
import geoactivity.common.items.tools.ReinforcedHoe;
import geoactivity.common.items.tools.ReinforcedMiner;
import geoactivity.common.items.tools.ReinforcedPickaxe;
import geoactivity.common.items.tools.ReinforcedShovel;
import geoactivity.common.items.tools.ReinforcedSword;
import geoactivity.common.items.tools.Adv.AdvAxe;
import geoactivity.common.items.tools.Adv.AdvPick;
import geoactivity.common.items.tools.Adv.AdvShovel;
import geoactivity.common.items.tools.Adv.AdvSword;
import geoactivity.common.items.tools.Red.ARedstoneBattleMiner;
import geoactivity.common.items.tools.Red.RedstoneBattleAxe;
import geoactivity.common.items.tools.Red.RedstoneBattleMiner;
import geoactivity.common.items.tools.Red.RedstoneMiner;
import geoactivity.common.lib.IHasName;
import geoactivity.common.lib.Reference;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GAPreInit
{
	public static void preInit(FMLPreInitializationEvent event)
	{
		registerItems();
		registerBlocks();

		if (event.getSide() == Side.CLIENT)
			registerMetadataTextures();

		GameRegistry.registerWorldGenerator(new GAWorldGenerator(), 2);
	}

	private static void registerItems()
	{
		/* Items */
		GAMod.gemLigniteCoal = new CustomCoalItems("lignitecoal");
		GAMod.gemBituminousCoal = new CustomCoalItems("bituminouscoal");
		GAMod.gemAnthraciteCoal = new CustomCoalItems("anthracitecoal");

		GAMod.graphite = new BaseItem("graphitecoal");
		GAMod.carbonfiber = new BaseItem("carbonfiber");
		GAMod.carbonstick = new BaseItem("carbonstick");
		GAMod.carbonOreCutter = new CarbonOreCutter("carbonorecutter");

		GAMod.preciousAlloy = new PreciousAlloy("preciousalloyingot");

		GAMod.tool_perks = new ToolPerks("perk");
		GAMod.machine_perks = new MachinePerks("perk_m");
		GAMod.armor_perks = new ArmorPerks("perk_a");

		GAMod.elementContainer = new ElementContainer("elementcontainer");

		GAMod.autoSBuilder = new AutoStoneBuilder("autostonebuilder");

		/* Tools */

		//Tier I
		GAMod.reinforcedSword = new ReinforcedSword("reinforcedsword");
		GAMod.reinforcedPickaxe = new ReinforcedPickaxe("reinforcedpickaxe");
		GAMod.reinforcedAxe = new ReinforcedAxe("reinforcedaxe");
		GAMod.reinforcedShovel = new ReinforcedShovel("reinforcedshovel");
		GAMod.reinforcedHoe = new ReinforcedHoe("reinforcedhoe");
		GAMod.reinforcedMiner = new ReinforcedMiner("reinforcedminer");

		//Tier II
		GAMod.advancedSword = new AdvSword("advancedsword");
		GAMod.advancedPickaxe = new AdvPick("advancedpick");
		GAMod.advancedAxe = new AdvAxe("advancedaxe");
		GAMod.advancedShovel = new AdvShovel("advancedshovel");

		//Tier III
		GAMod.redstoneMiner = new RedstoneMiner("redstoneminer");
		GAMod.redstoneBattleAxe = new RedstoneBattleAxe("redstonebattleaxe");
		GAMod.redstoneBattleMiner = new RedstoneBattleMiner("redstonebattleminer");
		GAMod.aredstoneBattleMiner = new ARedstoneBattleMiner("aredstonebattleminer");

		/* Armor */

		//Tier I
		GAMod.reinforcedHelmet = new ReinforcedArmor("reinforcedhelmet", 1, EntityEquipmentSlot.HEAD);
		GAMod.reinforcedChest = new ReinforcedArmor("reinforcedchest", 1, EntityEquipmentSlot.CHEST);
		GAMod.reinforcedPants = new ReinforcedArmor("reinforcedpants", 2, EntityEquipmentSlot.LEGS);
		GAMod.reinforcedBoots = new ReinforcedArmor("reinforcedboots", 1, EntityEquipmentSlot.FEET);

		//Tier II
		GAMod.advancedHelmet = new AdvancedArmor("advancedhelmet", 1, EntityEquipmentSlot.HEAD);
		GAMod.advancedChest = new AdvancedArmor("advancedchest", 1, EntityEquipmentSlot.CHEST);
		GAMod.advancedPants = new AdvancedArmor("advancedpants", 2, EntityEquipmentSlot.LEGS);
		GAMod.advancedBoots = new AdvancedArmor("advancedboots", 1, EntityEquipmentSlot.FEET);
	}

	private static void registerBlocks()
	{
		//Ores
		GAMod.oreLignite = new CustomCoalOres("ligniteore", 2).setHardness(3.0F);
		GAMod.oreLigniteItem = new ItemBlock(GAMod.oreLignite);
		GameRegistry.register(GAMod.oreLigniteItem.setRegistryName(GAMod.oreLignite.getRegistryName()));

		GAMod.oreBituminous = new CustomCoalOres("bituminousore", 2).setHardness(4.0F);
		GAMod.oreBituminousItem = new ItemBlock(GAMod.oreBituminous);
		GameRegistry.register(GAMod.oreBituminousItem.setRegistryName(GAMod.oreBituminous.getRegistryName()));

		GAMod.oreAnthracite = new CustomCoalOres("anthraciteore", 3).setHardness(5.0F);
		GAMod.oreAnthraciteItem = new ItemBlock(GAMod.oreAnthracite);
		GameRegistry.register(GAMod.oreAnthraciteItem.setRegistryName(GAMod.oreAnthracite.getRegistryName()));

		//General Blocks
		GAMod.hardenedglass = new TransparentBlock("hardenedglass", Material.GLASS, 3).setHardness(20.0F).setResistance(130.0F);
		GAMod.hardenedglassItem = new ItemBlock(GAMod.hardenedglass);
		GameRegistry.register(GAMod.hardenedglassItem.setRegistryName(GAMod.hardenedglass.getRegistryName()));

		GAMod.graphitebase = new TransparentBlock("graphitebase", Material.ROCK, 2).setHardness(3.0F).setResistance(15.0F);
		GAMod.graphitebaseItem = new ItemBlock(GAMod.graphitebase);
		GameRegistry.register(GAMod.graphitebaseItem.setRegistryName(GAMod.graphitebase.getRegistryName()));

		GAMod.hardenedbrick = new HardenedBrick("hardenedbrick");
		GAMod.hardenedbrickItem = new BrickItemBlock(GAMod.hardenedbrick);
		GameRegistry.register(GAMod.hardenedbrickItem.setRegistryName(GAMod.hardenedbrick.getRegistryName()));

		/* Machines */

		//Tier I
		GAMod.coalrefiner = new CoalRefiner("coalrefiner");
		GAMod.coalrefinerItem = new ItemBlock(GAMod.coalrefiner);
		GameRegistry.register(GAMod.coalrefinerItem.setRegistryName(GAMod.coalrefiner.getRegistryName()));

		GAMod.craftingmachine = new CraftingMachine("craftingmachine");
		GAMod.craftingmachineItem = new ItemBlock(GAMod.craftingmachine);
		GameRegistry.register(GAMod.craftingmachineItem.setRegistryName(GAMod.craftingmachine.getRegistryName()));

		//Tier II
		GAMod.atomextractor = new AtomExtractor("atomextractor");
		GAMod.atomextractorItem = new MultiItemBlock(GAMod.atomextractor);
		GameRegistry.register(GAMod.atomextractorItem.setRegistryName(GAMod.atomextractor.getRegistryName()));

		GAMod.chemistrymachine = new ChemistryMachine("chemistrymachine");
		GAMod.chemistrymachineItem = new MultiItemBlock(GAMod.chemistrymachine);
		GameRegistry.register(GAMod.chemistrymachineItem.setRegistryName(GAMod.chemistrymachine.getRegistryName()));

		GAMod.advancedcoalrefiner = new AdvancedCoalRefiner("advancedcoalrefiner");
		GAMod.advancedcoalrefinerItem = new MultiItemBlock(GAMod.advancedcoalrefiner);
		GameRegistry.register(GAMod.advancedcoalrefinerItem.setRegistryName(GAMod.advancedcoalrefiner.getRegistryName()));

		GAMod.advancedorerefiner = new AdvancedOreRefiner("advancedorerefiner");
		GAMod.advancedorerefinerItem = new MultiItemBlock(GAMod.advancedorerefiner);
		GameRegistry.register(GAMod.advancedorerefinerItem.setRegistryName(GAMod.advancedorerefiner.getRegistryName()));

		GAMod.thermicgenerator = new ThermicGenerator("thermicgenerator");
		GAMod.thermicgeneratorItem = new MultiItemBlock(GAMod.thermicgenerator);
		GameRegistry.register(GAMod.thermicgeneratorItem.setRegistryName(GAMod.thermicgenerator.getRegistryName()));

		GAMod.toolcharger = new ToolCharger("toolcharger");
		GAMod.toolchargerItem = new MultiItemBlock(GAMod.toolcharger);
		GameRegistry.register(GAMod.toolchargerItem.setRegistryName(GAMod.toolcharger.getRegistryName()));

		GAMod.geothermalgenerator = new GeothermalGenerator("geothermalgenerator");
		GAMod.geothermalgeneratorItem = new MultiItemBlock(GAMod.geothermalgenerator);
		GameRegistry.register(GAMod.geothermalgeneratorItem.setRegistryName(GAMod.geothermalgenerator.getRegistryName()));

		GAMod.thermicmelter = new ThermicMelter("thermicmelter");
		GAMod.thermicmelterItem = new MultiItemBlock(GAMod.thermicmelter);
		GameRegistry.register(GAMod.thermicmelterItem.setRegistryName(GAMod.thermicmelter.getRegistryName()));
	}

	@SideOnly(Side.CLIENT)
	private static void registerMetadataTextures()
	{
		int i;

		for(i = 0;i < PreciousAlloy.alloy_names.length;i++)
			ModelBakery.registerItemVariants(GAMod.preciousAlloy,
				new ResourceLocation(Reference.MOD_ID + ":" + ((IHasName)GAMod.preciousAlloy).getName(i)));

		for(i = 0;i < ToolPerks.EnumToolPerks.values().length;i++)
			ModelBakery.registerItemVariants(GAMod.tool_perks,
				new ResourceLocation(Reference.MOD_ID + ":" + ((IHasName)GAMod.tool_perks).getName(i)));

		for(i = 0;i < MachinePerks.EnumMachinePerks.values().length;i++)
			ModelBakery.registerItemVariants(GAMod.machine_perks,
				new ResourceLocation(Reference.MOD_ID + ":" + ((IHasName)GAMod.machine_perks).getName(i)));

		for(i = 0;i < ArmorPerks.EnumArmorPerks.values().length;i++)
			ModelBakery.registerItemVariants(GAMod.armor_perks,
				new ResourceLocation(Reference.MOD_ID + ":" + ((IHasName)GAMod.armor_perks).getName(i)));

		for(i = 0;i < ElementContainer.EnumElements.values().length;i++)
			ModelBakery.registerItemVariants(GAMod.elementContainer,
				new ResourceLocation(Reference.MOD_ID + ":" + ((IHasName)GAMod.elementContainer).getName(i)));

		for(i = 0;i < HardenedBrick.EnumHardenedBrick.values().length;i++)
			ModelBakery.registerItemVariants(GAMod.hardenedbrickItem,
				new ResourceLocation(Reference.MOD_ID + ":" + ((IHasName)GAMod.hardenedbrick).getName(i)));
	}
}
