package me.zonalyewhd.chatcalculator.recipes;

import java.util.LinkedList;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public final class ItemData {

	private static LinkedList<ItemData> allData = new LinkedList<ItemData>();

	private String name;
	private short data;

	private ItemStack item;

	private static ItemData output;

	private ItemData(String name, int data) {
		this(name, valueOf(name.toUpperCase().replaceAll(" ", "_")), data);
	}

	private ItemData(String name, Material material) {
		this(name, material, 0);
	}

	private ItemData(String name, Material material, int data) {
		if (material == null)
			if (!name.endsWith("s"))
				throw new IllegalArgumentException("Invalid item name: " + name);
			else
				material = valueOf(name.substring(0, name.length() - 1));
		this.name = name;
		this.data = (short) data;

		this.item = new ItemStack(material, 1, (short) data);

		allData.add(this);
	}

	/**
	 * 
	 * The user-friendly name of the item.
	 * 
	 * @since 1.3
	 */
	public String getFriendlyName() {
		return name;
	}

	/**
	 * 
	 * The material used by Minecraft <br>
	 * </br><strong>Note:</strong> Some of the material names are very different
	 * from the in-game name (i.e. crafting table -> {@link Material#WORKBENCH},
	 * etc.)
	 * 
	 * @since 1.3
	 */
	public Material getMaterial() {
		return item.getType();
	}

	/**
	 * The damage (durability) value of the item.
	 * 
	 * @return The damage value of the item. Certain exceptions are as follows:
	 *         <ul>
	 *         <li><strong>0</strong> - all breakable items (i.e. swords,
	 *         shears, etc.)</li>
	 *         </ul>
	 *         <strong>Note:</strong> the potion damage values control what type
	 *         of potion it is
	 * 
	 * @since 1.3
	 */
	public short getDamage() {
		return data;
	}

	/**
	 * 
	 * The ItemData as an {@link ItemStack}, with an amount of
	 * <strong>1</strong>.
	 * 
	 * @since 1.3
	 */
	public ItemStack toItem() {
		return item;
	}
	
	public String toString() {
		return getFriendlyName();
	}

	static Material valueOf(String s) {
		try {
			return Material.valueOf(s);
		} catch (Exception e) {
			return null;
		}
	}

	@SuppressWarnings("deprecation")
	static String getEnchantmentName(Enchantment enchantment) {
		switch (enchantment.getId()) {
		case 0:
			return "Protection";
		case 1:
			return "Fire Protection";
		case 2:
			return "Feather Falling";
		case 3:
			return "Blast Protection";
		case 4:
			return "Projectile Protection";
		case 5:
			return "Respiration";
		case 6:
			return "Aqua Affinity";
		case 7:
			return "Thorns";
		case 16:
			return "Sharpness";
		case 17:
			return "Smite";
		case 18:
			return "Bane of Arthropods";
		case 19:
			return "Knockback";
		case 20:
			return "Fire Aspect";
		case 21:
			return "Looting";
		case 48:
			return "Power";
		case 49:
			return "Punch";
		case 50:
			return "Flame";
		case 51:
			return "Infinity";
		case 32:
			return "Efficiency";
		case 33:
			return "Silk Touch";
		case 34:
			return "Unbreaking";
		case 35:
			return "Fortune";
		case 61:
			return "Luck";
		case 62:
			return "Lure";
		default:
			return enchantment.getName();
		}
	}

	static String romans(int i) {
		switch (i) {
		case 1:
			return "I";
		case 2:
			return "II";
		case 3:
			return "III";
		case 4:
			return "IV";
		case 5:
			return "V";
		default:
			return String.valueOf(i);
		}
	}

	/**
	 * Returns the material equivalent to the given string
	 * 
	 * @param input
	 *            The material to search for
	 * @return The material that matches <code>input</code>, or
	 *         {@link Material#AIR} if one could not be found, to reduce the
	 *         chance of a {@link NullPointerException}.
	 * 
	 * @since 1.3
	 */
	public static ItemData fromString(String input) {
		if (input == null || input.isEmpty())
			return allData.get(0);
		for (ItemData id : allData) {
			if (input.equalsIgnoreCase(id.name))
				output = id;
		}
		return output;
	}

	/**
	 * 
	 * Returns the <strong>first</strong> ItemData with the matching material.<br>
	 * <strong>NOT</strong> advised to use for a perfect result (i.e. although
	 * Lapis Lazuli is considered an {@link Material#INK_SACK}, it would return
	 * the ItemData equivalent to {@link Material#INK_SACK}<br>
	 * 
	 * @param material
	 *            The material to search for
	 * 
	 * @see ItemData#fromMaterial(Material, short)
	 * 
	 */
	@Deprecated
	public static ItemData fromMaterial(Material material) {
		if (material == null || material == Material.AIR)
			return allData.get(0);
		for (ItemData id : allData)
			if (id.item.getType() == material)
				return id;
		return allData.get(0);
	}

	public static ItemData fromMaterial(Material material, short damage) {
		return fromItemStack(new ItemStack(material, 1, damage));
	}

	public static ItemData fromItemStack(ItemStack item) {
		if (item == null || item.getType() == null
				|| item.getType() == Material.AIR)
			return allData.get(0);
		for (ItemData id : allData) {
			if (id.item.getType() == item.getType()
					&& id.data == item.getDurability())
				return id;

		}
		return allData.get(0);
	}

	public static String enchantmentName(Enchantment enchantment, int level) {
		return getEnchantmentName(enchantment) + " " + romans(level);
	}

	public static short getData(String s) {
		if (s == null || s.isEmpty())
			return -1;
		if (s.equalsIgnoreCase("white"))
			return 0;
		if (s.equalsIgnoreCase("orange"))
			return 1;
		if (s.equalsIgnoreCase("magenta") || s.equalsIgnoreCase("light_purple"))
			return 2;
		if (s.equalsIgnoreCase("light_blue"))
			return 3;
		if (s.equalsIgnoreCase("yellow"))
			return 4;
		if (s.equalsIgnoreCase("lime") || s.equalsIgnoreCase("light_green"))
			return 5;
		if (s.equalsIgnoreCase("pink") || s.equalsIgnoreCase("light_red"))
			return 6;
		if (s.equalsIgnoreCase("gray") || s.equalsIgnoreCase("dark_gray"))
			return 7;
		if (s.equalsIgnoreCase("light_gray"))
			return 8;
		if (s.equalsIgnoreCase("cyan"))
			return 9;
		if (s.equalsIgnoreCase("purple") || s.equalsIgnoreCase("dark_purple"))
			return 10;
		if (s.equalsIgnoreCase("blue") || s.equalsIgnoreCase("dark_blue"))
			return 11;
		if (s.equalsIgnoreCase("brown"))
			return 12;
		if (s.equalsIgnoreCase("green") || s.equalsIgnoreCase("dark_green"))
			return 13;
		if (s.equalsIgnoreCase("red") || s.equalsIgnoreCase("dark_red"))
			return 14;
		if (s.equalsIgnoreCase("black"))
			return 15;
		return 0;
	}
	
	public static void main(String[] args) {
		System.out.println(new RecipeCalculator(new ItemStack(Material.STAINED_GLASS_PANE, 5, (short) 6), 12, false).calculate());
	}

	static {
		new ItemData("Air", Material.AIR);
		new ItemData("Stone", Material.STONE);
		new ItemData("Granite", Material.STONE, 1);
		new ItemData("Polished Granite", Material.STONE, 2);
		new ItemData("Diorite", Material.STONE, 3);
		new ItemData("Polished Diorite", Material.STONE, 4);
		new ItemData("Andesite", Material.STONE, 5);
		new ItemData("Polished Andesite", Material.STONE, 6);
		new ItemData("Grass", Material.GRASS);
		new ItemData("Dirt", Material.DIRT);
		new ItemData("Coarse Dirt", Material.DIRT, 1);
		new ItemData("Podzol", Material.DIRT, 2);
		new ItemData("Cobblestone", Material.COBBLESTONE);
		new ItemData("Oak Planks", Material.WOOD);
		new ItemData("Spruce Planks", Material.WOOD, 1);
		new ItemData("Birch Planks", Material.WOOD, 2);
		new ItemData("Jungle Planks", Material.WOOD, 3);
		new ItemData("Acacia Planks", Material.WOOD, 4);
		new ItemData("Dark Oak Planks", Material.WOOD, 5);
		new ItemData("Oak Sapling", Material.SAPLING);
		new ItemData("Spruce Sapling", Material.SAPLING, 1);
		new ItemData("Birch Sapling", Material.SAPLING, 2);
		new ItemData("Jungle Sapling", Material.SAPLING, 3);
		new ItemData("Acacia Sapling", Material.SAPLING, 4);
		new ItemData("Dark Oak Sapling", Material.SAPLING, 5);
		new ItemData("Bedrock", Material.BEDROCK);
		new ItemData("Sand", Material.SAND);
		new ItemData("Red Sand", Material.SAND, 1);
		new ItemData("Gravel", Material.GRAVEL);
		new ItemData("Gold Ore", Material.GOLD_ORE);
		new ItemData("Iron Ore", Material.IRON_ORE);
		new ItemData("Coal Ore", Material.COAL_ORE);
		new ItemData("Oak Logs", Material.LOG);
		new ItemData("Spruce Logs", Material.LOG, 1);
		new ItemData("Birch Logs", Material.LOG, 2);
		new ItemData("Jungle Logs", Material.LOG, 3);
		new ItemData("Oak Leaves", Material.LEAVES);
		new ItemData("Spruce Leaves", Material.LEAVES, 1);
		new ItemData("Birch Leaves", Material.LEAVES, 2);
		new ItemData("Jungle Leaves", Material.LEAVES, 3);
		new ItemData("Sponge", Material.SPONGE);
		new ItemData("Wet Sponge", Material.SPONGE, 1);
		new ItemData("Glass", Material.GLASS);
		new ItemData("Lapis Lazuli Ore", Material.LAPIS_ORE);
		new ItemData("Lapis Lazuli Block", Material.LAPIS_BLOCK);
		new ItemData("Dispenser", Material.DISPENSER);
		new ItemData("Sandstone", Material.SANDSTONE);
		new ItemData("Chiseled Sandstone", Material.SANDSTONE, 1);
		new ItemData("Smooth Sandstone", Material.SANDSTONE, 2);
		new ItemData("Note Block", Material.NOTE_BLOCK);
		new ItemData("Powered Rail", Material.POWERED_RAIL);
		new ItemData("Detector Rail", Material.DETECTOR_RAIL);
		new ItemData("Sticky Piston", Material.PISTON_STICKY_BASE);
		new ItemData("Cobweb", Material.WEB);
		new ItemData("Dead Shrub", Material.DEAD_BUSH);
		new ItemData("Tall Grass", Material.DEAD_BUSH, 1);
		new ItemData("Fern", Material.DEAD_BUSH, 2);
		new ItemData("Piston", Material.PISTON_BASE);
		new ItemData("White Wool", Material.WOOL);
		new ItemData("Orange Wool", Material.WOOL, 1);
		new ItemData("Magenta Wool", Material.WOOL, 2);
		new ItemData("Light Blue Wool", Material.WOOL, 3);
		new ItemData("Yellow Wool", Material.WOOL, 4);
		new ItemData("Lime Wool", Material.WOOL, 5);
		new ItemData("Pink Wool", Material.WOOL, 6);
		new ItemData("Gray Wool", Material.WOOL, 7);
		new ItemData("Light Gray Wool", Material.WOOL, 8);
		new ItemData("Cyan Wool", Material.WOOL, 9);
		new ItemData("Purple Wool", Material.WOOL, 10);
		new ItemData("Blue Wool", Material.WOOL, 11);
		new ItemData("Brown Wool", Material.WOOL, 12);
		new ItemData("Green Wool", Material.WOOL, 13);
		new ItemData("Red Wool", Material.WOOL, 14);
		new ItemData("Black Wool", Material.WOOL, 15);
		new ItemData("Dandelion", Material.YELLOW_FLOWER);
		new ItemData("Poppy", Material.RED_ROSE);
		new ItemData("Blue Orchid", Material.RED_ROSE, 1);
		new ItemData("Allium", Material.RED_ROSE, 2);
		new ItemData("Azure Bluet", Material.RED_ROSE, 3);
		new ItemData("Red Tulip", Material.RED_ROSE, 4);
		new ItemData("Orange Tulip", Material.RED_ROSE, 5);
		new ItemData("White Tulip", Material.RED_ROSE, 6);
		new ItemData("Pink Tulip", Material.RED_ROSE, 7);
		new ItemData("Oxeye Daisy", Material.RED_ROSE, 8);
		new ItemData("Brown Mushroom", Material.BROWN_MUSHROOM);
		new ItemData("Red Mushroom", Material.RED_MUSHROOM);
		new ItemData("Gold Block", Material.GOLD_BLOCK);
		new ItemData("Iron Block", Material.IRON_BLOCK);
		new ItemData("Stone Slab", Material.STONE_SLAB2);
		new ItemData("Sandstone Slab", Material.STONE_SLAB2, 1);
		new ItemData("Oak Slab", Material.WOOD_STEP);
		new ItemData("Cobblestone Slab", Material.STONE_SLAB2, 3);
		new ItemData("Brick Slab", Material.STONE_SLAB2, 4);
		new ItemData("Stone Brick Slab", Material.STONE_SLAB2, 5);
		new ItemData("Nether Brick Slab", Material.STONE_SLAB2, 6);
		new ItemData("Quartz Slab", Material.STONE_SLAB2, 7);
		new ItemData("Bricks", Material.BRICK);
		new ItemData("TNT", Material.TNT);
		new ItemData("Bookshelf", Material.BOOKSHELF);
		new ItemData("Mossy Cobblestone", Material.MOSSY_COBBLESTONE);
		new ItemData("Obsidian", Material.OBSIDIAN);
		new ItemData("Torch", Material.TORCH);
		new ItemData("Fire", Material.FIRE);
		new ItemData("Mob Spawner", Material.MOB_SPAWNER);
		new ItemData("Oak Stairs", Material.WOOD_STAIRS);
		new ItemData("Chest", Material.CHEST);
		new ItemData("Diamond Ore", Material.DIAMOND_ORE);
		new ItemData("Crafting Table", Material.WORKBENCH);
		new ItemData("Furnace", Material.FURNACE);
		new ItemData("Ladder", Material.LADDER);
		new ItemData("Rail", Material.RAILS);
		new ItemData("Cobblestone Stairs", Material.COBBLESTONE_STAIRS);
		new ItemData("Lever", Material.LEVER);
		new ItemData("Stone Pressure Plate", Material.STONE_PLATE);
		new ItemData("Wooden Pressure Plate", Material.WOOD_PLATE);
		new ItemData("Redstone Ore", Material.REDSTONE_ORE);
		new ItemData("Redstone Torch", Material.REDSTONE_TORCH_ON);
		new ItemData("Stone Button", Material.STONE_BUTTON);
		new ItemData("Snow", Material.SNOW);
		new ItemData("Ice", Material.ICE);
		new ItemData("Snow Block", Material.SNOW_BLOCK);
		new ItemData("Cactus", Material.CACTUS);
		new ItemData("Clay Block", Material.CLAY);
		new ItemData("Jukebox", Material.JUKEBOX);
		new ItemData("Oak Fence", Material.FENCE);
		new ItemData("Pumpkin", Material.PUMPKIN);
		new ItemData("Netherrack", Material.NETHERRACK);
		new ItemData("Soul Sand", Material.SOUL_SAND);
		new ItemData("Glowstone", Material.GLOWSTONE);
		new ItemData("Jack o'Lantern", Material.JACK_O_LANTERN);
		new ItemData("White Stained Glass", Material.STAINED_GLASS);
		new ItemData("Orange Stained Glass", Material.STAINED_GLASS, 1);
		new ItemData("Magenta Stained Glass", Material.STAINED_GLASS, 2);
		new ItemData("Light Blue Stained Glass", Material.STAINED_GLASS, 3);
		new ItemData("Yellow Stained Glass", Material.STAINED_GLASS, 4);
		new ItemData("Lime Stained Glass", Material.STAINED_GLASS, 5);
		new ItemData("Pink Stained Glass", Material.STAINED_GLASS, 6);
		new ItemData("Gray Stained Glass", Material.STAINED_GLASS, 7);
		new ItemData("Light Gray Stained Glass", Material.STAINED_GLASS, 8);
		new ItemData("Cyan Stained Glass", Material.STAINED_GLASS, 9);
		new ItemData("Purple Stained Glass", Material.STAINED_GLASS, 10);
		new ItemData("Blue Stained Glass", Material.STAINED_GLASS, 11);
		new ItemData("Brown Stained Glass", Material.STAINED_GLASS, 12);
		new ItemData("Green Stained Glass", Material.STAINED_GLASS, 13);
		new ItemData("Red Stained Glass", Material.STAINED_GLASS, 14);
		new ItemData("Black Stained Glass", Material.STAINED_GLASS, 15);
		new ItemData("Trapdoor", Material.TRAP_DOOR);
		new ItemData("Stone Bricks", Material.SMOOTH_BRICK);
		new ItemData("Mossy Stone Bricks", Material.SMOOTH_BRICK, 1);
		new ItemData("Cracked Stone Bricks", Material.SMOOTH_BRICK, 2);
		new ItemData("Chiseled Stone Bricks", Material.SMOOTH_BRICK, 3);
		new ItemData("Iron Bars", Material.IRON_FENCE);
		new ItemData("Glass Pane", Material.THIN_GLASS);
		new ItemData("Melon Block", Material.MELON_BLOCK);
		new ItemData("Vines", Material.VINE);
		new ItemData("Oak Fence Gate", Material.FENCE_GATE);
		new ItemData("Brick Stairs", Material.BRICK_STAIRS);
		new ItemData("Stone Brick Stairs", Material.SMOOTH_STAIRS);
		new ItemData("Mycelium", Material.MYCEL);
		new ItemData("Lily Pad", Material.WATER_LILY);
		new ItemData("Nether Brick", Material.NETHER_BRICK);
		new ItemData("Nether Brick Fence", Material.NETHER_FENCE);
		new ItemData("Nether Brick Stairs", Material.NETHER_BRICK_STAIRS);
		new ItemData("Enchantment Table", Material.ENCHANTMENT_TABLE);
		new ItemData("Cauldron", Material.CAULDRON_ITEM);
		new ItemData("End Portal Frame", Material.ENDER_PORTAL_FRAME);
		new ItemData("End Stone", Material.ENDER_STONE);
		new ItemData("Dragon Egg", Material.DRAGON_EGG);
		new ItemData("Redstone Lamp", Material.REDSTONE_LAMP_OFF);
		new ItemData("Spruce Slab", Material.WOOD_STEP, 1);
		new ItemData("Birch Slab", Material.WOOD_STEP, 2);
		new ItemData("Jungle Slab", Material.WOOD_STEP, 3);
		new ItemData("Acacia Slab", Material.WOOD_STEP, 4);
		new ItemData("Dark Oak Slab", Material.WOOD_STEP, 5);
		new ItemData("Sandstone Stairs", Material.SANDSTONE_STAIRS);
		new ItemData("Emerald Ore", Material.EMERALD_ORE);
		new ItemData("Ender Chest", Material.ENDER_CHEST);
		new ItemData("Tripwire Hook", Material.TRIPWIRE_HOOK);
		new ItemData("Emerald Block", Material.EMERALD_BLOCK);
		new ItemData("Spruce Stairs", Material.SPRUCE_WOOD_STAIRS);
		new ItemData("Birch Stairs", Material.BIRCH_WOOD_STAIRS);
		new ItemData("Jungle Stairs", Material.JUNGLE_WOOD_STAIRS);
		new ItemData("Command Block", Material.COMMAND);
		new ItemData("Beacon", Material.BEACON);
		new ItemData("Cobblestone Wall", Material.COBBLE_WALL);
		new ItemData("Mossy Cobblestone Wall", Material.COBBLE_WALL, 1);
		new ItemData("Wooden Button", Material.WOOD_BUTTON);
		new ItemData("Skeleton Skull", Material.SKULL_ITEM);
		new ItemData("Wither Skeleton Skull", Material.SKULL_ITEM, 1);
		new ItemData("Zombie Head", Material.SKULL_ITEM, 2);
		new ItemData("Player Head", Material.SKULL_ITEM, 3);
		new ItemData("Creeper Head", Material.SKULL_ITEM, 4);
		new ItemData("Anvil", Material.ANVIL);
		new ItemData("Slightly Damaged Anvil", Material.ANVIL, 1);
		new ItemData("Very Damaged Anvil", Material.ANVIL, 2);
		new ItemData("Trapped Chest", Material.TRAPPED_CHEST);
		new ItemData("Weighted Pressure Plate (light)", Material.GOLD_PLATE);
		new ItemData("Weighted Pressure Plate (heavy)", Material.IRON_PLATE);
		new ItemData("Daylight Sensor", Material.DAYLIGHT_DETECTOR);
		new ItemData("Redstone Block", Material.REDSTONE_BLOCK);
		new ItemData("Nether Quartz Ore", Material.QUARTZ_ORE);
		new ItemData("Hopper", Material.HOPPER);
		new ItemData("Quartz Block", Material.QUARTZ_BLOCK);
		new ItemData("Chiseled Quartz Block", Material.QUARTZ_BLOCK, 1);
		new ItemData("Pillar Quartz Block", Material.QUARTZ_BLOCK, 2);
		new ItemData("Quartz Stairs", Material.QUARTZ_STAIRS);
		new ItemData("Activator Rail", Material.ACTIVATOR_RAIL);
		new ItemData("Dropper", Material.DROPPER);
		new ItemData("White Stained Clay", Material.STAINED_CLAY);
		new ItemData("Orange Stained Clay", Material.STAINED_CLAY, 1);
		new ItemData("Magenta Stained Clay", Material.STAINED_CLAY, 2);
		new ItemData("Light Blue Stained Clay", Material.STAINED_CLAY, 3);
		new ItemData("Yellow Stained Clay", Material.STAINED_CLAY, 4);
		new ItemData("Lime Stained Clay", Material.STAINED_CLAY, 5);
		new ItemData("Pink Stained Clay", Material.STAINED_CLAY, 6);
		new ItemData("Gray Stained Clay", Material.STAINED_CLAY, 7);
		new ItemData("Light Gray Stained Clay", Material.STAINED_CLAY, 8);
		new ItemData("Cyan Stained Clay", Material.STAINED_CLAY, 9);
		new ItemData("Purple Stained Clay", Material.STAINED_CLAY, 10);
		new ItemData("Blue Stained Clay", Material.STAINED_CLAY, 11);
		new ItemData("Brown Stained Clay", Material.STAINED_CLAY, 12);
		new ItemData("Green Stained Clay", Material.STAINED_CLAY, 13);
		new ItemData("Red Stained Clay", Material.STAINED_CLAY, 14);
		new ItemData("Black Stained Clay", Material.STAINED_CLAY, 15);
		new ItemData("White Stained Glass Pane", Material.STAINED_GLASS_PANE);
		new ItemData("Orange Stained Glass Pane", Material.STAINED_GLASS_PANE,
				1);
		new ItemData("Magenta Stained Glass Pane", Material.STAINED_GLASS_PANE,
				2);
		new ItemData("Light Blue Stained Glass Pane",
				Material.STAINED_GLASS_PANE, 3);
		new ItemData("Yellow Stained Glass Pane", Material.STAINED_GLASS_PANE,
				4);
		new ItemData("Lime Stained Glass Pane", Material.STAINED_GLASS_PANE, 5);
		new ItemData("Pink Stained Glass Pane", Material.STAINED_GLASS_PANE, 6);
		new ItemData("Gray Stained Glass Pane", Material.STAINED_GLASS_PANE, 7);
		new ItemData("Light Gray Stained Glass Pane",
				Material.STAINED_GLASS_PANE, 8);
		new ItemData("Cyan Stained Glass Pane", Material.STAINED_GLASS_PANE, 9);
		new ItemData("Purple Stained Glass Pane", Material.STAINED_GLASS_PANE,
				10);
		new ItemData("Blue Stained Glass Pane", Material.STAINED_GLASS_PANE, 11);
		new ItemData("Brown Stained Glass Pane", Material.STAINED_GLASS_PANE,
				12);
		new ItemData("Green Stained Glass Pane", Material.STAINED_GLASS_PANE,
				13);
		new ItemData("Red Stained Glass Pane", Material.STAINED_GLASS_PANE, 14);
		new ItemData("Black Stained Glass Pane", Material.STAINED_GLASS_PANE,
				15);
		new ItemData("Acacia Leaves", Material.LEAVES_2);
		new ItemData("Dark Oak Leaves", Material.LEAVES_2, 1);
		new ItemData("Acacia Logs", Material.LOG_2);
		new ItemData("Dark Oak Logs", Material.LOG_2, 1);
		new ItemData("Acacia Stairs", Material.ACACIA_STAIRS);
		new ItemData("Dark Oak Stairs", Material.DARK_OAK_STAIRS);
		new ItemData("Slime Block", Material.SLIME_BLOCK);
		new ItemData("Barrier", Material.BARRIER);
		new ItemData("Iron Trapdoor", Material.IRON_TRAPDOOR);
		new ItemData("Prismarine", Material.PRISMARINE);
		new ItemData("Prismarine Bricks", Material.PRISMARINE, 1);
		new ItemData("Dark Prismarine", Material.PRISMARINE, 2);
		new ItemData("Sea Lantern", Material.SEA_LANTERN);
		new ItemData("Hay Bale", Material.HAY_BLOCK);
		new ItemData("White Carpet", Material.CARPET);
		new ItemData("Orange Carpet", Material.CARPET, 1);
		new ItemData("Magenta Carpet", Material.CARPET, 2);
		new ItemData("Light Blue Carpet", Material.CARPET, 3);
		new ItemData("Yellow Carpet", Material.CARPET, 4);
		new ItemData("Lime Carpet", Material.CARPET, 5);
		new ItemData("Pink Carpet", Material.CARPET, 6);
		new ItemData("Gray Carpet", Material.CARPET, 7);
		new ItemData("Light Gray Carpet", Material.CARPET, 8);
		new ItemData("Cyan Carpet", Material.CARPET, 9);
		new ItemData("Purple Carpet", Material.CARPET, 10);
		new ItemData("Blue Carpet", Material.CARPET, 11);
		new ItemData("Brown Carpet", Material.CARPET, 12);
		new ItemData("Green Carpet", Material.CARPET, 13);
		new ItemData("Red Carpet", Material.CARPET, 14);
		new ItemData("Black Carpet", Material.CARPET, 15);
		new ItemData("Hardened Clay", Material.HARD_CLAY);
		new ItemData("Block of Coal", Material.COAL_BLOCK);
		new ItemData("Packed Ice", Material.PACKED_ICE);
		new ItemData("Sunflower", Material.DOUBLE_PLANT);
		new ItemData("Lilac", Material.DOUBLE_PLANT, 1);
		new ItemData("Double Tallgrass", Material.DOUBLE_PLANT, 2);
		new ItemData("Large Fern", Material.DOUBLE_PLANT, 3);
		new ItemData("Rose Bush", Material.DOUBLE_PLANT, 4);
		new ItemData("Peony", Material.DOUBLE_PLANT, 5);
		new ItemData("Red Sandstone", Material.RED_SANDSTONE);
		new ItemData("Chiseled Red Sandstone", Material.RED_SANDSTONE, 1);
		new ItemData("Smooth Red Sandstone", Material.RED_SANDSTONE, 2);
		new ItemData("Red Sandstone Stairs", Material.RED_SANDSTONE_STAIRS);
		new ItemData("Spruce Fence Gate", Material.SPRUCE_FENCE_GATE);
		new ItemData("Birch Fence Gates", Material.BIRCH_FENCE_GATE);
		new ItemData("Jungle Fence Gate", Material.JUNGLE_FENCE_GATE);
		new ItemData("Dark Oak Fence Gate", Material.DARK_OAK_FENCE_GATE);
		new ItemData("Acacia Fence Gate", Material.ACACIA_FENCE_GATE);
		new ItemData("Spruce Fence", Material.SPRUCE_FENCE);
		new ItemData("Birch Fence", Material.BIRCH_FENCE);
		new ItemData("Jungle Fence", Material.JUNGLE_FENCE);
		new ItemData("Dark Oak Fence", Material.DARK_OAK_FENCE);
		new ItemData("Acacia Fence", Material.ACACIA_FENCE);
		new ItemData("Iron Shovel", Material.IRON_SPADE);
		new ItemData("Iron Pickaxe", Material.IRON_PICKAXE);
		new ItemData("Iron Axe", Material.IRON_AXE);
		new ItemData("Flint and Steel", Material.FLINT_AND_STEEL);
		new ItemData("Apple", Material.APPLE);
		new ItemData("Bow", Material.BOW);
		new ItemData("Arrow", Material.ARROW);
		new ItemData("Coal", Material.COAL);
		new ItemData("Charcoal", Material.COAL, 1);
		new ItemData("Diamond", Material.DIAMOND);
		new ItemData("Iron Ingot", Material.IRON_INGOT);
		new ItemData("Gold Ingot", Material.GOLD_INGOT);
		new ItemData("Iron Sword", Material.IRON_SWORD);
		new ItemData("Wooden Sword", Material.WOOD_SWORD);
		new ItemData("Wooden Shovel", Material.WOOD_SWORD);
		new ItemData("Wooden Pickaxe", Material.WOOD_PICKAXE);
		new ItemData("Wooden Axe", Material.WOOD_AXE);
		new ItemData("Stone Sword", Material.STONE_SWORD);
		new ItemData("Stone Shovel", Material.STONE_SPADE);
		new ItemData("Stone Axe", Material.STONE_AXE);
		new ItemData("Diamond Sword", Material.DIAMOND_SWORD);
		new ItemData("Diamond Shovel", Material.DIAMOND_SPADE);
		new ItemData("Diamond Pickaxe", Material.DIAMOND_PICKAXE);
		new ItemData("Diamond Axe", Material.DIAMOND_AXE);
		new ItemData("Stick", Material.STICK);
		new ItemData("Bowl", Material.BOWL);
		new ItemData("Mushroom Stew", Material.MUSHROOM_SOUP);
		new ItemData("Golden Sword", Material.GOLD_SWORD);
		new ItemData("Golden Shovel", Material.GOLD_SPADE);
		new ItemData("Golden Pickaxe", Material.GOLD_PICKAXE);
		new ItemData("Golden Axe", Material.GOLD_AXE);
		new ItemData("String", Material.STRING);
		new ItemData("Feather", Material.FEATHER);
		new ItemData("Gunpowder", Material.SULPHUR);
		new ItemData("Wooden Hoe", Material.WOOD_HOE);
		new ItemData("Stone Hoe", Material.STONE_HOE);
		new ItemData("Iron Hoe", Material.IRON_HOE);
		new ItemData("Diamond Hoe", Material.DIAMOND_HOE);
		new ItemData("Golden Hoe", Material.GOLD_HOE);
		new ItemData("Seeds", Material.SEEDS);
		new ItemData("Wheat", Material.WHEAT);
		new ItemData("Bread", Material.BREAD);
		new ItemData("Leather Helmet", Material.LEATHER_HELMET);
		new ItemData("Leather Chestplate", Material.LEATHER_CHESTPLATE);
		new ItemData("Leather Leggings", Material.LEATHER_LEGGINGS);
		new ItemData("Leather Boots", Material.LEATHER_BOOTS);
		new ItemData("Chainmail Helmet", Material.CHAINMAIL_HELMET);
		new ItemData("Chainmail Chestplate", Material.CHAINMAIL_CHESTPLATE);
		new ItemData("Chainmail Leggings", Material.CHAINMAIL_LEGGINGS);
		new ItemData("Chainmail Boots", Material.CHAINMAIL_BOOTS);
		new ItemData("Iron Helmet", Material.IRON_HELMET);
		new ItemData("Iron Chestplate", Material.IRON_CHESTPLATE);
		new ItemData("Iron Leggings", Material.IRON_LEGGINGS);
		new ItemData("Iron Boots", Material.IRON_BOOTS);
		new ItemData("Diamond Helmet", Material.DIAMOND_HELMET);
		new ItemData("Diamond Chestplate", Material.DIAMOND_CHESTPLATE);
		new ItemData("Diamond Leggings", Material.DIAMOND_LEGGINGS);
		new ItemData("Diamond Boots", Material.DIAMOND_BOOTS);
		new ItemData("Gold Helmet", Material.GOLD_HELMET);
		new ItemData("Gold Chestplate", Material.GOLD_CHESTPLATE);
		new ItemData("Gold Leggings", Material.GOLD_LEGGINGS);
		new ItemData("Gold Boots", Material.GOLD_BOOTS);
		new ItemData("Flint", Material.FLINT);
		new ItemData("Raw Porkchop", Material.PORK);
		new ItemData("Cooked Porkchops", Material.GRILLED_PORK);
		new ItemData("Painting", Material.PAINTING);
		new ItemData("Golden Apple", Material.GOLDEN_APPLE);
		new ItemData("Enchanted Golden Apple", Material.GOLDEN_APPLE, 1);
		new ItemData("Sign", Material.SIGN);
		new ItemData("Oak Door", Material.WOOD_DOOR);
		new ItemData("Bucket", Material.BUCKET);
		new ItemData("Water Bucket", Material.WATER_BUCKET);
		new ItemData("Lava Bucket", Material.LAVA_BUCKET);
		new ItemData("Minecart", Material.MINECART);
		new ItemData("Saddle", Material.SADDLE);
		new ItemData("Iron Door", Material.IRON_DOOR);
		new ItemData("Redstone", Material.REDSTONE);
		new ItemData("Snowball", Material.SNOW_BALL);
		new ItemData("Boat", Material.BOAT);
		new ItemData("Leather", Material.LEATHER);
		new ItemData("Milk Bucket", Material.MILK_BUCKET);
		new ItemData("Brick", Material.CLAY_BRICK);
		new ItemData("Clay Ball", Material.CLAY_BALL);
		new ItemData("Sugar Cane", Material.SUGAR_CANE);
		new ItemData("Paper", Material.PAPER);
		new ItemData("Book", Material.BOOK);
		new ItemData("Slimeball", Material.SLIME_BALL);
		new ItemData("Chest Minecart", Material.STORAGE_MINECART);
		new ItemData("Furnace Minecart", Material.POWERED_MINECART);
		new ItemData("Egg", Material.EGG);
		new ItemData("Compass", Material.COMPASS);
		new ItemData("Fishing Rod", Material.FISHING_ROD);
		new ItemData("Clock", Material.WATCH);
		new ItemData("Glowstone Dust", Material.GLOWSTONE_DUST);
		new ItemData("Raw Fish", Material.RAW_FISH);
		new ItemData("Raw Salmon", Material.RAW_FISH, 1);
		new ItemData("Clownfish", Material.RAW_FISH, 2);
		new ItemData("Pufferfish", Material.RAW_FISH, 3);
		new ItemData("Cooked Fish", Material.COOKED_FISH);
		new ItemData("Cooked Salmon", Material.COOKED_FISH, 1);
		new ItemData("Ink Sack", Material.INK_SACK);
		new ItemData("Red Dye", Material.INK_SACK, 1);
		new ItemData("Green Dye", Material.INK_SACK, 2);
		new ItemData("Cocoa Beans", Material.INK_SACK, 3);
		new ItemData("Lapis Lazuli", Material.INK_SACK, 4);
		new ItemData("Purple Dye", Material.INK_SACK, 5);
		new ItemData("Cyan Dye", Material.INK_SACK, 6);
		new ItemData("Light Gray Dye", Material.INK_SACK, 7);
		new ItemData("Gray Dye", Material.INK_SACK, 8);
		new ItemData("Pink Dye", Material.INK_SACK, 9);
		new ItemData("Lime Dye", Material.INK_SACK, 10);
		new ItemData("Yellow Dye", Material.INK_SACK, 11);
		new ItemData("Light Blue Dye", Material.INK_SACK, 12);
		new ItemData("Magenta Dye", Material.INK_SACK, 13);
		new ItemData("Orange Dye", Material.INK_SACK, 14);
		new ItemData("Bone Meal", Material.INK_SACK, 15);
		new ItemData("Bone", Material.BONE);
		new ItemData("Sugar", Material.SUGAR);
		new ItemData("Cake", Material.CAKE);
		new ItemData("Bed", Material.BED);
		new ItemData("Redstone Repeater", Material.DIODE);
		new ItemData("Cookie", Material.COOKIE);
		new ItemData("Map", Material.MAP);
		new ItemData("Shears", Material.SHEARS);
		new ItemData("Melon Slice", Material.MELON);
		new ItemData("Pumpkin Seeds", Material.PUMPKIN_SEEDS);
		new ItemData("Melon Seeds", Material.MELON_SEEDS);
		new ItemData("Raw Beef", Material.RAW_BEEF);
		new ItemData("Steak", Material.COOKED_BEEF);
		new ItemData("Raw Chicken", Material.RAW_CHICKEN);
		new ItemData("Cooked Chicken", Material.COOKED_CHICKEN);
		new ItemData("Rotten Flesh", Material.ROTTEN_FLESH);
		new ItemData("Ender Pearl", Material.ENDER_PEARL);
		new ItemData("Blaze Rod", Material.BLAZE_ROD);
		new ItemData("Ghast Tear", Material.GHAST_TEAR);
		new ItemData("Gold Nugget", Material.GOLD_NUGGET);
		new ItemData("Nether Wart", Material.NETHER_WARTS);
		new ItemData("Potion", Material.POTION);
		new ItemData("Awkward Potion", Material.POTION, 16);
		new ItemData("Thick Potion", Material.POTION, 32);
		new ItemData("Mundane Potion (Extended)", Material.POTION, 64);
		new ItemData("Mundane Potion", Material.POTION, 8192);
		new ItemData("Potion of Regeneration", Material.POTION, 8193);
		new ItemData("Potion of Regeneration (Extended)", Material.POTION, 8257);
		new ItemData("Potion of Regeneration II", Material.POTION, 8225);
		new ItemData("Potion of Swiftness", Material.POTION, 8194);
		new ItemData("Potion of Swiftness (Extended)", Material.POTION, 8258);
		new ItemData("Potion of Swiftness II", Material.POTION, 8226);
		new ItemData("Potion of Fire Resistance", Material.POTION, 8195);
		new ItemData("Potion of Fire Resistance (Extended)", Material.POTION,
				8259);
		new ItemData("Potion of Fire Resistance (Reverted)", Material.POTION,
				8227);
		new ItemData("Potion of Healing", Material.POTION, 8197);
		new ItemData("Potion of Healing (Reverted)", Material.POTION, 8261);
		new ItemData("Potion of Healing II", Material.POTION, 8229);
		new ItemData("Potion of Strength", Material.POTION, 8201);
		new ItemData("Potion of Strength (Extended)", Material.POTION, 8265);
		new ItemData("Potion of Strength II", Material.POTION, 8233);
		new ItemData("Potion of Poison", Material.POTION, 8196);
		new ItemData("Potion of Poison (Extended)", Material.POTION, 8260);
		new ItemData("Potion of Poison II", Material.POTION, 8228);
		new ItemData("Potion of Weakness", Material.POTION, 8200);
		new ItemData("Potion of Weakness (Extended)", Material.POTION, 8264);
		new ItemData("Potion of Weakness (Reverted)", Material.POTION, 8232);
		new ItemData("Potion of Slowness", Material.POTION, 8202);
		new ItemData("Potion of Slowness (Extended)", Material.POTION, 8266);
		new ItemData("Potion of Slowness (Reverted)", Material.POTION, 8234);
		new ItemData("Potion of Harming", Material.POTION, 8204);
		new ItemData("Potion of Harming (Reverted)", Material.POTION, 8268);
		new ItemData("Potion of Harming II", Material.POTION, 8236);
		new ItemData("Splash Mundane Potion", Material.POTION, 16384);
		new ItemData("Splash Potion of Regeneration", Material.POTION, 16385);
		new ItemData("Splash Potion of Regeneration (Extended)",
				Material.POTION, 16449);
		new ItemData("Splash Potion of Regeneration II", Material.POTION, 16417);
		new ItemData("Splash Potion of Swiftness", Material.POTION, 16386);
		new ItemData("Splash Potion of Swiftness (Extended)", Material.POTION,
				16450);
		new ItemData("Splash Potion of Swiftness II", Material.POTION, 16418);
		new ItemData("Splash Potion of Fire Resistance", Material.POTION, 16387);
		new ItemData("Splash Potion of Fire Resistance (Extended)",
				Material.POTION, 16451);
		new ItemData("Splash Potion of Fire Resistance (Reverted)",
				Material.POTION, 16419);
		new ItemData("Splash Potion of Healing", Material.POTION, 16389);
		new ItemData("Splash Potion of Healing (Reverted)", Material.POTION,
				16453);
		new ItemData("Splash Potion of Healing II", Material.POTION, 16421);
		new ItemData("Splash Potion of Strength", Material.POTION, 16393);
		new ItemData("Splash Potion of Strength (Extended)", Material.POTION,
				16457);
		new ItemData("Splash Potion of Strength II", Material.POTION, 16425);
		new ItemData("Splash Potion of Poison", Material.POTION, 16388);
		new ItemData("Splash Potion of Poison (Extended)", Material.POTION,
				16452);
		new ItemData("Splash Potion of Poison II", Material.POTION, 16420);
		new ItemData("Splash Potion of Weakness", Material.POTION, 16392);
		new ItemData("Splash Potion of Weakness (Extended)", Material.POTION,
				16456);
		new ItemData("Splash Potion of Weakness (Reverted)", Material.POTION,
				16424);
		new ItemData("Splash Potion of Slowness", Material.POTION, 16394);
		new ItemData("Splash Potion of Slowness (Extended)", Material.POTION,
				16458);
		new ItemData("Splash Potion of Slowness (Reverted)", Material.POTION,
				16426);
		new ItemData("Splash Potion of Harming", Material.POTION, 16396);
		new ItemData("Splash Potion of Harming (Reverted)", Material.POTION,
				16460);
		new ItemData("Splash Potion of Harming II", Material.POTION, 16428);
		new ItemData("Glass Bottle", Material.GLASS_BOTTLE);
		new ItemData("Spider Eye", Material.SPIDER_EYE);
		new ItemData("Fermented Spider Eye", Material.FERMENTED_SPIDER_EYE);
		new ItemData("Blaze Powder", Material.BLAZE_POWDER);
		new ItemData("Magma Cream", Material.MAGMA_CREAM);
		new ItemData("Brewing Stand", Material.BREWING_STAND_ITEM);
		new ItemData("Cauldron", Material.CAULDRON_ITEM);
		new ItemData("Eye of Ender", Material.EYE_OF_ENDER);
		new ItemData("Glistering Melon", Material.SPECKLED_MELON);
		// Monster Eggs
		new ItemData("Creeper Spawn Egg", Material.MONSTER_EGG, 50);
		new ItemData("Skeleton Spawn Egg", Material.MONSTER_EGG, 51);
		new ItemData("Spider Spawn Egg", Material.MONSTER_EGG, 52);
		new ItemData("Zombie Spawn Egg", Material.MONSTER_EGG, 54);
		new ItemData("Slime Spawn Egg", Material.MONSTER_EGG, 55);
		new ItemData("Ghast Spawn Egg", Material.MONSTER_EGG, 56);
		new ItemData("Zombie Pigman Spawn Egg", Material.MONSTER_EGG, 57);
		new ItemData("Endermen Spawn Egg", Material.MONSTER_EGG, 58);
		new ItemData("Cave Spider Spawn Egg", Material.MONSTER_EGG, 59);
		new ItemData("Silverfish Spawn Egg", Material.MONSTER_EGG, 60);
		new ItemData("Blaze Spawn Egg", Material.MONSTER_EGG, 61);
		new ItemData("Magma Cube Spawn Egg", Material.MONSTER_EGG, 52);
		new ItemData("Bat Spawn Egg", Material.MONSTER_EGG, 65);
		new ItemData("Witch Spawn Egg", Material.MONSTER_EGG, 66);
		new ItemData("Endermite Spawn Egg", Material.MONSTER_EGG, 67);
		new ItemData("Guardian Spawn Egg", Material.MONSTER_EGG, 68);
		new ItemData("Pig Spawn Egg", Material.MONSTER_EGG, 90);
		new ItemData("Sheep Spawn Egg", Material.MONSTER_EGG, 91);
		new ItemData("Cow Spawn Egg", Material.MONSTER_EGG, 92);
		new ItemData("Chicken Spawn Egg", Material.MONSTER_EGG, 93);
		new ItemData("Squid Spawn Egg", Material.MONSTER_EGG, 94);
		new ItemData("Wolf Spawn Egg", Material.MONSTER_EGG, 95);
		new ItemData("Mooshroom Spawn Egg", Material.MONSTER_EGG, 96);
		new ItemData("Ocelot Spawn Egg", Material.MONSTER_EGG, 98);
		new ItemData("Horse Spawn Egg", Material.MONSTER_EGG, 100);
		new ItemData("Rabbit Spawn Egg", Material.MONSTER_EGG, 101);
		new ItemData("Villager Spawn Egg", Material.MONSTER_EGG, 120);
		new ItemData("Bottle o'Enchanting", Material.EXP_BOTTLE);
		new ItemData("Fire Charge", Material.FIREBALL);
		new ItemData("Book and Quill", Material.BOOK_AND_QUILL);
		new ItemData("Written Book", Material.WRITTEN_BOOK);
		new ItemData("Emerald", Material.EMERALD);
		new ItemData("Item Frame", Material.ITEM_FRAME);
		new ItemData("Flower Pot", Material.FLOWER_POT_ITEM);
		new ItemData("Carrot", Material.CARROT_ITEM);
		new ItemData("Potato", Material.POTATO);
		new ItemData("Baked Potato", Material.BAKED_POTATO);
		new ItemData("Poisonous Potato", Material.POISONOUS_POTATO);
		new ItemData("Empty Map", Material.EMPTY_MAP);
		new ItemData("Golden Carrot", Material.GOLDEN_CARROT);
		new ItemData("Carrot on a Stick", Material.CARROT_STICK);
		new ItemData("Nether Star", Material.NETHER_STAR);
		new ItemData("Pumpkin Pie", Material.PUMPKIN_PIE);
		new ItemData("Firework Rocket", Material.FIREWORK);
		new ItemData("Firework Star", Material.FIREWORK_CHARGE);
		new ItemData("Enchanted Book", Material.ENCHANTED_BOOK);
		new ItemData("Redstone Comparator", Material.REDSTONE_COMPARATOR);
		new ItemData("Nether Brick", Material.NETHER_BRICK_ITEM);
		new ItemData("Nether Quartz", Material.QUARTZ);
		new ItemData("TNT Minecart", Material.EXPLOSIVE_MINECART);
		new ItemData("Hopper Minecart", Material.HOPPER_MINECART);
		new ItemData("Prismarine Shard", Material.PRISMARINE_SHARD);
		new ItemData("Prismarine Crystals", Material.PRISMARINE_CRYSTALS);
		new ItemData("Raw Rabbit", Material.RABBIT);
		new ItemData("Cooked Rabbit", Material.COOKED_RABBIT);
		new ItemData("Rabbit Stew", Material.RABBIT_STEW);
		new ItemData("Rabbit's Foot", Material.RABBIT_FOOT);
		new ItemData("Rabbit Hide", Material.RABBIT_HIDE);
		new ItemData("Armor Stand", Material.ARMOR_STAND);
		new ItemData("Iron Horse Armor", Material.IRON_BARDING);
		new ItemData("Golden Horse Armor", Material.GOLD_BARDING);
		new ItemData("Diamond Horse Armor", Material.DIAMOND_BARDING);
		new ItemData("Lead", Material.LEASH);
		new ItemData("Name Tag", Material.NAME_TAG);
		new ItemData("Command Block Minecart", Material.COMMAND_MINECART);
		new ItemData("Raw Mutton", Material.MUTTON);
		new ItemData("Cooked Mutton", Material.COOKED_MUTTON);
		new ItemData("Banner", Material.BANNER);
		new ItemData("Spruce Door", Material.SPRUCE_DOOR_ITEM);
		new ItemData("Birch Door", Material.BIRCH_DOOR_ITEM);
		new ItemData("Jungle Door", Material.JUNGLE_DOOR_ITEM);
		new ItemData("Acacia Door", Material.ACACIA_DOOR_ITEM);
		new ItemData("Dark Oak Door", Material.DARK_OAK_DOOR_ITEM);
		new ItemData("Music Disc (cat)", Material.GREEN_RECORD);
		new ItemData("Music Disc (13)", Material.GOLD_RECORD);
		new ItemData("Music Disc (ward)", Material.RECORD_10);
		new ItemData("Music Disc (11)", Material.RECORD_11);
		new ItemData("Music Disc (wait)", Material.RECORD_12);
		new ItemData("Music Disc (blocks)", Material.RECORD_3);
		new ItemData("Music Disc (chirp)", Material.RECORD_4);
		new ItemData("Music Disc (far)", Material.RECORD_5);
		new ItemData("Music Disc (mall)", Material.RECORD_6);
		new ItemData("Music Disc (mellohi)", Material.RECORD_7);
		new ItemData("Music Disc (stal)", Material.RECORD_8);
		new ItemData("Music Disc (strad)", Material.RECORD_9);
	}

}
