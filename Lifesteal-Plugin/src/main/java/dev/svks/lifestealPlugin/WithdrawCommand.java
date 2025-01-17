package dev.svks.lifestealPlugin;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WithdrawCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if(!(commandSender instanceof Player p)) {
            commandSender.sendMessage("§cOnly players can execute this command.");
            return true;
        }

        if(strings.length!=1) {
            commandSender.sendMessage("§c/withdraw <amount>");
            return true;
        }

        int amount = Integer.parseInt(strings[0]);

        if(amount>=(p.getAttribute(Attribute.MAX_HEALTH).getBaseValue()/2)) {
            commandSender.sendMessage("§cYou can't withdraw that amount.");
            return true;
        }


        AttributeInstance maxHealth = p.getAttribute(Attribute.MAX_HEALTH);
        maxHealth.setBaseValue(maxHealth.getBaseValue()-amount*2);

        ItemStack heart = LifestealPlugin.heart();
        heart.setAmount(amount);
        p.getInventory().addItem(heart);

        return true;
    }
}
