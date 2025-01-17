package dev.svks.lifestealPlugin;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
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

    public static ItemStack heart() {
        ItemStack heart = new ItemStack(Material.POISONOUS_POTATO);
        ItemMeta meta = heart.getItemMeta();
        meta.setCustomModelData(1);
        meta.setDisplayName("§cHeart");
        meta.setLore(List.of("§7Right click to consume."));
        meta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        heart.setItemMeta(meta);
        return heart;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        saveDefaultConfig();

        getCommand("withdraw").setExecutor(new WithdrawCommand());
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(), this);
        getServer().getPluginManager().registerEvents(new RightClickListener(), this);

        ItemStack reviveBook = new ItemStack(Material.BOOK);
        ItemMeta meta = reviveBook.getItemMeta();
        meta.setCustomModelData(2);
        meta.setDisplayName("§cRevive Book");
        meta.setLore(List.of("§7Rename this to a player's name and right click to revive them."));
        reviveBook.setItemMeta(meta);

        NamespacedKey reviveKey = new NamespacedKey(this,"revive");

        ShapelessRecipe reviveRecipe = new ShapelessRecipe(reviveKey,reviveBook);
        reviveRecipe.addIngredient(heart());
        reviveRecipe.addIngredient(heart());
        reviveRecipe.addIngredient(heart());
        reviveRecipe.addIngredient(Material.BOOK);
        Bukkit.addRecipe(reviveRecipe);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
