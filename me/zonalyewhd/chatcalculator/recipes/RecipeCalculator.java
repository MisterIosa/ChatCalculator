package me.zonalyewhd.chatcalculator.recipes;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class RecipeCalculator {

	private static Map<Integer, RecipeCalculator> allCalcs = Maps
			.newLinkedHashMap();

	private ItemStack item;
	private int amount, id;
	private boolean smelt;

	public RecipeCalculator(ItemStack result, int amount, boolean smelt) {
		this.item = result;
		this.amount = amount;
		this.smelt = smelt;

		this.id = allCalcs.size();

		allCalcs.put(this.id, this);
	}

	public RecipeCalculator(ItemData result, int amount, boolean smelt) {
		this(result.toItem(), amount, smelt);
	}

	public List<Map<ItemData, Integer>> calculate() {
		List<Map<ItemData, Integer>> list = Lists.newArrayList();

		Map<ItemData, Integer> map;
		for (Recipe rec : Bukkit.getRecipesFor(item)) {
			map = Maps.newHashMap();
			if (smelt && rec instanceof FurnaceRecipe) {
				map.put(ItemData.fromMaterial(((FurnaceRecipe) rec).getInput()
						.getType(), (short) 0), amount);
				list.add(map);
			} else if (rec instanceof ShapelessRecipe) {
				for (ItemStack ingredient : ((ShapelessRecipe) rec)
						.getIngredientList()) {
					map.put(ItemData.fromItemStack(ingredient),
							ingredient.getAmount() * amount);
				}
				list.add(map);
			} else if (rec instanceof ShapedRecipe) {
				for (ItemStack ingredient : ((ShapedRecipe) rec)
						.getIngredientMap().values()) {
					if (ingredient != null && ingredient.getType() != null
							&& ingredient.getType() != Material.AIR)
						map.put(ItemData.fromItemStack(ingredient),
								ingredient.getAmount() * amount);
				}
				list.add(map);
			} else {
				Bukkit.getPluginManager()
						.getPlugin("ChatCalculator")
						.getLogger()
						.severe("Unknown recipe type: "
								+ rec.getClass().getSimpleName());
			}
		}

		return list;
	}

	public int getRecipeID() {
		return this.id;
	}

	public String[] getMessage() {
		List<Map<ItemData, Integer>> list = calculate();
		if (list.size() == 0)
			return new String[] { "§cThere are no recipes for "
					+ ItemData.fromItemStack(item).getFriendlyName() + "." };
		String[] ret = new String[list.size() + 1];
		ret[0] = "§7There"
				+ (list.size() > 1 ? " are §e" + list.size() + "§7 recipes"
						: " is §e1§7 recipe") + " for §a"
				+ ItemData.fromItemStack(item).getFriendlyName();
		for (int i = 1; i < ret.length; i++) {
			ret[i] = toString(list.get(i - 1));
		}
		return ret;
	}

	String toString(Map<ItemData, Integer> map) {
		if (map == null || map.isEmpty())
			return "";
		if (map.size() == 1) {
			return map.entrySet().iterator().next().getKey().getFriendlyName()
					+ " x" + map.entrySet().iterator().next().getValue();
		} else {
			StringBuilder str = new StringBuilder(map.entrySet().iterator()
					.next().getKey().getFriendlyName()
					+ " x" + map.entrySet().iterator().next().getValue());
			int i = 0;
			for (Entry<ItemData, Integer> entry : map.entrySet()) {
				if (i == 0)
					continue;
				i++;
				str.append(',').append(
						" " + entry.getKey().getFriendlyName() + "x "
								+ entry.getValue());
			}
			return str.toString();
		}
	}

	public RecipeCalculator multiply(int mult) {
		if (mult != 0 && mult > 0)
			this.amount *= mult;
		return this;
	}

	public RecipeCalculator divide(int div) {
		if (div != 0)
			this.amount /= div;

		return this;
	}

	public static RecipeCalculator fromID(int id) {
		try {
			return allCalcs.get(new Integer(id));
		} catch (Exception e) {
			return null;
		}
	}

}
