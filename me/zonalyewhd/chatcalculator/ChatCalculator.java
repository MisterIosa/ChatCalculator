 package me.zonalyewhd.chatcalculator;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import me.zonalyewhd.chatcalculator.recipes.ItemData;
import me.zonalyewhd.chatcalculator.recipes.RecipeCalculator;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class ChatCalculator extends JavaPlugin implements Listener {

	public static String PREFIX = "§6§l[§a§lChatCalc§r§6§l]§r ";

	private static HashMap<UUID, String[]> answers;

	private Random rand;
	private String CURRENT_VERSION;

	public void onEnable() {

		rand = new Random();

		CURRENT_VERSION = getDescription().getVersion();

		answers = new HashMap<UUID, String[]>();

		try {
			Metrics metrics = new Metrics(this);
			metrics.start();
		} catch (IOException e) {
			getLogger().severe("Could not enable Metrics!");
		}

		Bukkit.getPluginManager().registerEvents(this, this);

		getLogger().severe(
				new RecipeCalculator(new ItemStack(Material.WORKBENCH), 12,
						false).calculate().toString());
	}

	public void onDisable() {
		answers.clear();
	}

	@EventHandler(ignoreCancelled = false)
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		if (e.getMessage().equalsIgnoreCase("e=")) {
			if (!e.getPlayer().hasPermission("chatcalc.e"))
				return;
			e.setCancelled(true);
			sendMessage(e.getPlayer(),
					"§7The approximate value of §eEuler's number (e) §7is §a"
							+ Math.E);
			return;
		} else if (e.getMessage().equalsIgnoreCase("pi=")) {
			if (!e.getPlayer().hasPermission("chatcalc.pi"))
				return;
			e.setCancelled(true);
			sendMessage(e.getPlayer(),
					"§7The approximate value of §ePi (π)§7 is §a" + Math.PI);
			return;
		} else if (e.getMessage().equalsIgnoreCase("ans=")) {
			if (!e.getPlayer().hasPermission("chatcalc.ans"))
				return;
			e.setCancelled(true);
			if (!answers.containsKey(e.getPlayer().getUniqueId()))
				sendMessage(e.getPlayer(), "§cYou do not have a stored answer!");
			else
				sendMessage(
						e.getPlayer(),
						"§7Your stored answer is "
								+ ((decimalPlaces(Double.parseDouble(answers
										.get(e.getPlayer().getUniqueId())[0])) >= 14) ? "approximately §a"
										: "§a")
								+ format(Double.parseDouble(answers.get(e
										.getPlayer().getUniqueId())[0]), false)
								+ "§7. You entered §a"
								+ answers.get(e.getPlayer().getUniqueId())[1]
								+ "§7.");
			return;
		} else if (e.getMessage().equalsIgnoreCase("f:ans=")) {
			if (!e.getPlayer().hasPermission("chatcalc.ans"))
				return;
			e.setCancelled(true);
			if (!answers.containsKey(e.getPlayer().getUniqueId()))
				sendMessage(e.getPlayer(), "§cYou do not have a stored answer!");
			else
				sendMessage(
						e.getPlayer(),
						"§7Your stored answer is "
								+ ((decimalPlaces(Double.parseDouble(answers
										.get(e.getPlayer().getUniqueId())[0])) >= 14) ? "approximately §a"
										: "§a")
								+ niceFormat(Double.parseDouble(answers.get(e
										.getPlayer().getUniqueId())[0]))
								+ "§7. You entered §a"
								+ answers.get(e.getPlayer().getUniqueId())[1]
								+ "§7.");
		} else if (e.getMessage().equalsIgnoreCase("chatcalc help")
				|| e.getMessage().equalsIgnoreCase("chatcalc ?")) {
			if (!e.getPlayer().hasPermission("chatcalc.help"))
				return;
			e.setCancelled(true);
			e.getPlayer()
					.sendMessage(
							new String[] {
									"§6§m§l--------§6§l[§a§l ChatCalculator §6§l]§6§l§m--------§r",
									"",
									"  §cDeveloper: §eZonalYewHD",
									"  §cVersion: §e" + CURRENT_VERSION,
									"  §cSyntax: §e# [operator] #§7",
									"",
									"   §aAddition          §83 + 4        §7(§d7§7)",
									"   §aSubtraction     §810 - 7      §7(§d3§7)",
									"   §aMultiplication    §83 * 5      §7(§d15§7)",
									"   §aDivision          §830 / 6      §7(§d5§7)",
									"   §aRemainders     §820 % 3      §7(§d2§7)",
									"   §aPowers          §85 ^ 2      §7(§d25§7)",
									"   §aLogarithms     §82 log 32    §7(§d5§7)",
									"   §aRoots             §82 rt 16      §7(§d4§7)",
									"   §aRandom           §85 randi 10     §7(§d9§7)",
									"   §aTrigonometry   §890 sin d    §7(§d1§7)",
									"",
									"  §aChatCalculator§7 also supports §aPi§7 (type §ePi§7), §aEuler's number§7 (type §ee§7), and your §aprevious answer§7 (type §eAns§7). "
											+ "If using a trigonometric function, use §er§7 (radians) or §ed§7 (degrees)." });
		} else if (isDouble(e.getMessage().split(" ")[0])) {
			if (isOperator(e.getMessage().split(" ")[1])) {
				if (e.getMessage().split(" ").length == 3) {
					e.setCancelled(true);
					String[] array = e.getMessage().split(" ");
					Double a = toDouble(array[0], e.getPlayer().getUniqueId());
					if (a == null) {
						sendMessage(e.getPlayer(),
								"§cYou do not have a stored answer!");
						return;
					}
					Double b = toDouble(array[2], e.getPlayer().getUniqueId());
					if (b == null) {
						sendMessage(
								e.getPlayer(),
								"§cEither you do not have a stored answer, or you did not enter a supported variable/number.");
						return;
					}
					double c;
					String hint;
					String message;
					if (array[1].equalsIgnoreCase("+")) {
						if (!e.getPlayer().hasPermission("chatcalc.use.add"))
							return;
						c = a + b;
						message = "§7The sum of §a" + format(a, false)
								+ " §7and §a" + format(b, false) + "§7 (§8"
								+ format(a, false) + " + " + format(b, false)
								+ "§7) is §a" + format(c, true);
						hint = format(a, false) + " + " + format(b, false);
					} else if (array[1].equalsIgnoreCase("-")) {
						if (!e.getPlayer().hasPermission("chatcalc.use.sub")
								&& !e.getPlayer().hasPermission("chatcalc.use"))
							return;
						c = a - b;
						message = "§7The difference of §a" + format(a, false)
								+ " §7and §a" + format(b, false) + "§7 (§8"
								+ format(a, false) + " - " + format(b, false)
								+ "§7) is §a" + format(c, true);
						hint = format(a, false) + " - " + format(b, false);
					} else if (array[1].equalsIgnoreCase("*")) {
						if (!e.getPlayer().hasPermission("chatcalc.use.mult")
								&& !e.getPlayer().hasPermission("chatcalc.use"))
							return;
						c = a * b;
						message = "§7The product of §a" + format(a, false)
								+ " §7and §a" + format(b, false) + "§7 (§8"
								+ format(a, false) + " * " + format(b, false)
								+ "§7) is §a" + format(c, true);
						hint = format(a, false) + " * " + format(b, false);
					} else if (array[1].equalsIgnoreCase("/")) {
						if (!e.getPlayer().hasPermission("chatcalc.use.div")
								&& !e.getPlayer().hasPermission("chatcalc.use"))
							return;
						if (b == 0) {
							sendMessage(e.getPlayer(),
									"§cCannot divide by zero!");
							return;
						}
						c = a / b;
						message = "§7The quotient of §a" + format(a, false)
								+ " §7and §a" + format(b, false) + "§7 (§8"
								+ format(a, false) + " / " + format(b, false)
								+ "§7) is §a" + format(c, true);
						hint = format(a, false) + " / " + format(b, false);
					} else if (array[1].equalsIgnoreCase("%")) {
						if (!e.getPlayer().hasPermission("chatcalc.use.mod")
								&& !e.getPlayer().hasPermission("chatcalc.use"))
							return;
						c = a % b;
						message = "§7The remainder of §a" + format(a, false)
								+ " §7and §a" + format(b, false) + "§7 (§8"
								+ format(a, false) + " % " + format(b, false)
								+ "§7) is §a" + format(c, true);
						hint = format(a, false) + " % " + format(b, false);
					} else if (array[1].equalsIgnoreCase("^")) {
						if (!e.getPlayer().hasPermission("chatcalc.use.pow")
								&& !e.getPlayer().hasPermission("chatcalc.use"))
							return;
						c = Math.pow(a, b);
						message = "§7The §a" + formatPow(b) + "§7 power of §a"
								+ format(a, false) + "§7 (§8"
								+ format(a, false) + " ^ " + format(b, false)
								+ "§7) is §a" + format(c, true);
						hint = format(a, false) + " ^ " + format(b, false);
					} else if (array[1].equalsIgnoreCase("log")) {
						if (!e.getPlayer().hasPermission("chatcalc.use.log")
								&& !e.getPlayer().hasPermission("chatcalc.use"))
							return;
						c = ((Math.log10(b)) / (Math.log10(a)));
						message = "§7The logarithm base §a" + format(a, false)
								+ "§7 of §a" + format(b, false) + "§7 is §a"
								+ format(c, true);
						hint = format(a, false) + " log " + format(b, false);
					} else if (array[1].equalsIgnoreCase("rt")) {
						if (!e.getPlayer().hasPermission("chatcalc.use.rts")
								&& !e.getPlayer().hasPermission("chatcalc.use"))
							return;
						c = Math.pow(b, ((double) 1 / a));
						message = "§7The §a" + formatPow(a) + "§7 root of §a"
								+ format(b, false) + " §7(§8"
								+ format(a, false) + "√" + format(b, false)
								+ "§7) is §a" + format(c, true);
						hint = format(a, false) + " rt " + format(b, false);
					} else if (array[1].equalsIgnoreCase("randi")) {
						if (!e.getPlayer().hasPermission("chatcalc.use.rndi")
								&& !e.getPlayer().hasPermission("chatcalc.use"))
							return;
						if (a > b) {
							sendMessage(e.getPlayer(),
									"§cThe first value must be §aless§c than the second value!");
							return;
						}
						c = rand.nextInt((int) (b - a) + 1) + a;
						message = "§7A random §einteger§7 between §a"
								+ format(b, false) + " §7and §a"
								+ format(a, false) + " §7(§8"
								+ format(a, false) + " randi "
								+ format(b, false) + "§7) is §a"
								+ format(c, true);
						hint = a + " randi " + b;
					} else if (array[1].equalsIgnoreCase("randd")) {
						if (!e.getPlayer().hasPermission("chatcalc.use.rndd")
								&& !e.getPlayer().hasPermission("chatcalc.use"))
							return;
						if (a > b) {
							sendMessage(e.getPlayer(),
									"§cThe first value must be §aless§c than the second value!");
							return;
						}
						c = a + (b - a) * rand.nextDouble();
						message = "§7A random §edouble§7 between §a"
								+ format(b, false) + " §7and §a"
								+ format(a, false) + " §7(§8"
								+ format(a, false) + " randd "
								+ format(b, false) + "§7) is §a"
								+ format(c, true);
						hint = a + " randd " + b;
					} else if (array[1].equalsIgnoreCase("sin")) {
						if (!e.getPlayer().hasPermission("chatcalc.use.sin")
								&& !e.getPlayer().hasPermission("chatcalc.use"))
							return;
						if (b == 0)
							c = Math.sin(a);
						else if (b == 1)
							c = Math.sin(Math.toRadians(a));
						else {
							sendMessage(e.getPlayer(),
									"§cYou must use either §er§c for radians or §ed§c for degrees!");
							return;
						}
						message = "§7The trigonometric sine of §a"
								+ format(a, false) + "§7 "
								+ (b == 0 ? "radians (" : "degrees (") + "§8"
								+ format(a, false) + " sin "
								+ (b == 0 ? "r" : "d") + "§7) is §a"
								+ format(c, true);
						hint = format(a, false) + " sin "
								+ (b == 0 ? "r" : "d");
					} else if (array[1].equalsIgnoreCase("csc")) {
						if (!e.getPlayer().hasPermission("chatcalc.use.csc")
								&& !e.getPlayer().hasPermission("chatcalc.use"))
							return;
						if (b == 0)
							c = 1 / Math.sin(a);
						else if (b == 1)
							c = 1 / Math.sin(Math.toRadians(a));
						else {
							sendMessage(e.getPlayer(),
									"§cYou must use either §er§c for radians or §ed§c for degrees!");
							return;
						}
						message = "§7The trigonometric cosecant of §a"
								+ format(a, false) + "§7 "
								+ (b == 0 ? "radians (" : "degrees (") + "§8"
								+ format(a, false) + " csc "
								+ (b == 0 ? "r" : "d") + "§7) is §a"
								+ format(c, true);
						hint = format(a, false) + " csc "
								+ (b == 0 ? "r" : "d");
					} else if (array[1].equalsIgnoreCase("cos")) {
						if (!e.getPlayer().hasPermission("chatcalc.use.cos")
								&& !e.getPlayer().hasPermission("chatcalc.use"))
							return;
						if (b == 0)
							c = Math.cos(a);
						else if (b == 1)
							c = Math.cos(Math.toRadians(a));
						else {
							sendMessage(e.getPlayer(),
									"§cYou must use either §er§c for radians or §ed§c for degrees!");
							return;
						}
						message = "§7The trigonometric cosine of §a"
								+ format(a, false) + "§7 "
								+ (b == 0 ? "radians (" : "degrees (") + "§8"
								+ format(a, false) + " cos "
								+ (b == 0 ? "r" : "d") + "§7) is §a"
								+ format(c, true);
						hint = format(a, false) + " cos "
								+ (b == 0 ? "r" : "d");
					} else if (array[1].equalsIgnoreCase("sec")) {
						if (!e.getPlayer().hasPermission("chatcalc.use.sec")
								&& !e.getPlayer().hasPermission("chatcalc.use"))
							return;
						if (b == 0)
							c = 1 / Math.cos(a);
						else if (b == 1)
							c = 1 / Math.cos(Math.toRadians(a));
						else {
							sendMessage(e.getPlayer(),
									"§cYou must use either §er§c for radians or §ed§c for degrees!");
							return;
						}
						message = "§7The trigonometric secant of §a"
								+ format(a, false) + "§7 "
								+ (b == 0 ? "radians (" : "degrees (") + "§8"
								+ format(a, false) + " sec "
								+ (b == 0 ? "r" : "d") + "§7) is §a"
								+ format(c, true);
						hint = format(a, false) + " sec "
								+ (b == 0 ? "r" : "d");
					} else if (array[1].equalsIgnoreCase("tan")) {
						if (!e.getPlayer().hasPermission("chatcalc.use.tan")
								&& !e.getPlayer().hasPermission("chatcalc.use"))
							return;
						if (b == 0)
							c = Math.tan(a);
						else if (b == 1)
							c = Math.tan(Math.toRadians(a));
						else {
							sendMessage(e.getPlayer(),
									"§cYou must use either §er§c for radians or §ed§c for degrees!");
							return;
						}
						message = "§7The trigonometric tangent of §a"
								+ format(a, false) + "§7 "
								+ (b == 0 ? "radians (" : "degrees (") + "§8"
								+ format(a, false) + " tan "
								+ (b == 0 ? "r" : "d") + "§7) is §a"
								+ format(c, true);
						hint = format(a, false) + " tan "
								+ (b == 0 ? "r" : "d");
					} else if (array[1].equalsIgnoreCase("cot")) {
						if (!e.getPlayer().hasPermission("chatcalc.use.cot")
								&& !e.getPlayer().hasPermission("chatcalc.use"))
							return;
						if (b == 0)
							c = 1 / Math.tan(a);
						else if (b == 1)
							c = 1 / Math.tan(Math.toRadians(a));
						else {
							sendMessage(e.getPlayer(),
									"§cYou must use either §er§c for radians or §ed§c for degrees!");
							return;
						}
						message = "§7The trigonometric cotangent of §a"
								+ format(a, false) + "§7 "
								+ (b == 0 ? "radians (" : "degrees (") + "§8"
								+ format(a, false) + " cot "
								+ (b == 0 ? "r" : "d") + "§7) is §a"
								+ format(c, true);
						hint = format(a, false) + " cot "
								+ (b == 0 ? "r" : "d");
					} else {
						throw new IllegalStateException("Invalid operator: "
								+ array[1]);
					}
					sendMessage(e.getPlayer(), message);
					answers.put(e.getPlayer().getUniqueId(), new String[] {
							String.valueOf(c), hint });
				}
			}
		}
	}

	@EventHandler(ignoreCancelled = false)
	public void onRecipeCalc(AsyncPlayerChatEvent e) {
		if (e.getMessage().split(" ").length == 3
				&& e.getMessage().split(" ")[1].equalsIgnoreCase("rec")
				&& isInteger(e.getMessage().split(" ")[2])) {
			e.setCancelled(true);
			ItemData data = ItemData.fromString(e.getMessage().split(" ")[0]);
			RecipeCalculator calc = new RecipeCalculator(data,
					Integer.parseInt(e.getMessage().split(" ")[2]), false);
			sendMessage(e.getPlayer(), calc.getMessage());
		}
	}

	private static boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private static boolean isDouble(String s) {
		try {
			Double.parseDouble(s);
			return true;
		} catch (Exception e) {
			return (s.equalsIgnoreCase("pi") || s.equalsIgnoreCase("e")
					|| s.equalsIgnoreCase("ans") || s.endsWith("%"));
		}
	}

	private static Double toDouble(String s, UUID player) {
		try {
			return Double.parseDouble(s);
		} catch (Exception e) {
			if (s.equalsIgnoreCase("pi"))
				return Math.PI;
			else if (s.equalsIgnoreCase("e"))
				return Math.E;
			else if (s.equalsIgnoreCase("ans"))
				return (answers.containsKey(player) ? Double
						.parseDouble(answers.get(player)[0]) : null);
			else if (s.equalsIgnoreCase("r"))
				return new Double(0);
			else if (s.equalsIgnoreCase("d"))
				return new Double(1);
			else if (s.equalsIgnoreCase("ans%"))
				return toDouble(s.substring(0, s.length() - 1), player) / 100;
			else if (s.equalsIgnoreCase("pi%"))
				return Math.PI / 100;
			else if (s.equalsIgnoreCase("e%"))
				return Math.E / 100;
			else if (s.endsWith("%"))
				return toDouble(s.substring(0, s.length() - 1), player) / 100;
		}
		return null;
	}

	private boolean isOperator(String s) {
		return (s.equalsIgnoreCase("+") || s.equalsIgnoreCase("-")
				|| s.equalsIgnoreCase("*") || s.equalsIgnoreCase("/")
				|| s.equalsIgnoreCase("%") || s.equalsIgnoreCase("^")
				|| s.equalsIgnoreCase("log") || s.equalsIgnoreCase("randi")
				|| s.equalsIgnoreCase("randd") || s.equalsIgnoreCase("rt")
				|| s.equalsIgnoreCase("sin") || s.equalsIgnoreCase("csc")
				|| s.equalsIgnoreCase("cos") || s.equalsIgnoreCase("sec")
				|| s.equalsIgnoreCase("tan") || s.equalsIgnoreCase("cot"));
	}

	private String format(double number, boolean value) {
		if (number == Math.PI)
			if (value)
				return String.valueOf(Math.PI) + "...";
			else
				return "Pi (π)";
		if (number == Math.E)
			if (value)
				return String.valueOf(Math.E) + "...";
			else
				return "Euler's number (e)";
		if ((long) number == number)
			return String.valueOf((long) number);
		return String.valueOf(number)
				+ (decimalPlaces(number) < 14 ? "" : "...");
	}

	private String niceFormat(double number) {
		if (number == Math.PI)
			return "Pi (π)";
		if (number == Math.E)
			return "Euler's number (e)";
		if ((long) number == number)
			return new DecimalFormat("###,###.###").format((long) number);
		return new DecimalFormat("###,###.###").format(number);
	}

	private void sendMessage(Player player, String... message) {
		if (player != null)
			for (int i = 0; i < message.length; i++)
				player.sendMessage(PREFIX + message[i]);
	}

	private String formatPow(double num) {
		if (num == Math.PI)
			return "Pi-th";
		if (num == Math.E)
			return "e-th";
		int a = Integer.parseInt(String.valueOf(String.valueOf(num)
				.split("\\.")[0].charAt(String.valueOf(num).split("\\.")[0]
				.length() - 1)));
		return format(num, true)
				+ (a == 1 ? "st" : (a == 2 ? "nd" : (a == 3 ? "rd" : "th")));
	}

	private int decimalPlaces(double num) {
		if ((long) num == num)
			return 0;
		return String.valueOf(num).split("\\.")[1].length();
	}

}
