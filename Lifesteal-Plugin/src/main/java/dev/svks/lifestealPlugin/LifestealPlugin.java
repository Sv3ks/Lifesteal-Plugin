package dev.svks.lifestealPlugin;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class LifestealPlugin extends JavaPlugin {
    private static Plugin plugin;
    public static Plugin getPlugin() {
        return plugin;
    }

    public static ItemStack getHeartItem() {
        ItemStack heart = new ItemStack(Material.POISONOUS_POTATO);
        ItemMeta meta = heart.getItemMeta();
        meta.setCustomModelData(1);
        meta.setDisplayName("§cHeart");
        meta.setLore(List.of("§7Right click to consume."));
        meta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        heart.setItemMeta(meta);
        return heart;
    }

    private void initHeartRecipe() {
        NamespacedKey key = new NamespacedKey(this,"heart");

        ShapedRecipe recipe = new ShapedRecipe(key, getHeartItem());
        recipe.shape("ABA",
                     "BCB",
                     "ABA");
        recipe.setIngredient('A',Material.DIAMOND_BLOCK);
        recipe.setIngredient('B',Material.NETHERITE_INGOT);
        recipe.setIngredient('C',Material.NETHER_STAR);
        Bukkit.addRecipe(recipe);
    }

    private void initReviveRecipe() {
        ItemStack item = new ItemStack(Material.BOOK);
        ItemMeta meta = item.getItemMeta();
        meta.setCustomModelData(2);
        meta.setDisplayName("§cRevive Book");
        meta.setLore(List.of("§7Rename this to a player's name and right click to revive them."));
        item.setItemMeta(meta);

        NamespacedKey key = new NamespacedKey(this,"revive");

        ShapelessRecipe recipe = new ShapelessRecipe(key,item);
        recipe.addIngredient(getHeartItem());
        recipe.addIngredient(getHeartItem());
        recipe.addIngredient(getHeartItem());
        recipe.addIngredient(Material.BOOK);
        Bukkit.addRecipe(recipe);
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        saveDefaultConfig();

        getCommand("withdraw").setExecutor(new WithdrawCommand());
        getServer().getPluginManager().registerEvents(new ItemSystem(), this);
        getServer().getPluginManager().registerEvents(new LifeStealSystem(), this);

        getServer().getPluginManager().registerEvents(new CombatLog(), this);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new CombatLog(),0L,20L);

        initReviveRecipe();
        initHeartRecipe();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
