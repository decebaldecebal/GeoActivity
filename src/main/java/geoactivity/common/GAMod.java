package geoactivity.common;

import org.apache.commons.lang3.ArrayUtils;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.common.util.EnumHelper;

public class GAMod
{
	// Materials
	public static Item.ToolMaterial ReinforcedMaterial = EnumHelper.addToolMaterial("Reinforced", 2, 500, 8.0F, 3.0F, 18);
	public static Item.ToolMaterial AdvancedMaterial = EnumHelper.addToolMaterial("Advanced", 3, 1000, 10.0F, 0.0F, 8);

	public static ItemArmor.ArmorMaterial ReinforcedArmorMaterial = EnumHelper.addArmorMaterial("ReinforcedArmor",
			"geoactivity:reinforced", 30, new int[]{ 2, 6, 5, 2 }, 18, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 0.0F);
	public static ItemArmor.ArmorMaterial AdvancedArmorMaterial = EnumHelper.addArmorMaterial("AdvancedArmor",
			"geoactivity:advanced", 35, new int[]{ 4, 10, 7, 4 }, 8, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 2.0F);

	public static Material[] pickaxeMaterials = {Material.ROCK, Material.IRON, Material.ANVIL, Material.CIRCUITS,
		Material.GLASS, Material.ICE, Material.PISTON};
	public static Material[] axeMaterials = {Material.WOOD, Material.LEAVES, Material.PLANTS, Material.VINE, Material.CIRCUITS,
		Material.CACTUS, Material.GOURD};
	public static Material[] shovelMaterials = {Material.GRASS, Material.GROUND, Material.CLAY, Material.SAND, Material.SNOW};
	public static Material[] minerMaterials = ArrayUtils.addAll(pickaxeMaterials, shovelMaterials);
	public static Material[] battleminerMaterials = ArrayUtils.addAll(minerMaterials, axeMaterials);

	// Blocks
	public static Block oreAnthracite, oreBituminous, oreLignite, hardenedbrick, hardenedglass, coalrefiner,
		graphitebase, craftingmachine, advancedcoalrefiner, atomextractor, chemistrymachine, advancedhardenedbrick, advancedorerefiner;
	public static Block thermicgenerator, toolcharger, geothermalgenerator, thermicmelter;

	// Item Blocks
	public static ItemBlock oreAnthraciteItem, oreBituminousItem, oreLigniteItem, hardenedbrickItem, hardenedglassItem, coalrefinerItem,
		graphitebaseItem, craftingmachineItem, advancedcoalrefinerItem, atomextractorItem, chemistrymachineItem, advancedhardenedbrickItem, advancedorerefinerItem;
	public static ItemBlock thermicgeneratorItem, toolchargerItem, geothermalgeneratorItem, thermicmelterItem;

	// Items
	public static Item gemAnthraciteCoal, gemBituminousCoal, gemLigniteCoal, graphite, carbonfiber, carbonstick, tool_perks,
		machine_perks, armor_perks, preciousAlloy, elementContainer, carbonOreCutter;

	//Tools
	public static Item reinforcedSword, reinforcedPickaxe, reinforcedAxe, reinforcedShovel, reinforcedHoe, reinforcedMiner;
	public static Item advancedSword, advancedPickaxe, advancedAxe, advancedShovel, advancedHoe;
	public static Item redstoneMiner, redstoneBattleAxe, redstoneBattleMiner, aredstoneBattleMiner;
	public static Item autoSBuilder;

	//Armor
	public static Item reinforcedHelmet, reinforcedChest, reinforcedPants, reinforcedBoots;
	public static Item advancedHelmet, advancedChest, advancedPants, advancedBoots;
}
