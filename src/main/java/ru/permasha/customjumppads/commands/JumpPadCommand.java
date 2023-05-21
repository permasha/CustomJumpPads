package ru.permasha.customjumppads.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.permasha.customjumppads.CustomJumpPads;

import java.util.List;

public class JumpPadCommand implements CommandExecutor {

    CustomJumpPads plugin;

    public JumpPadCommand(CustomJumpPads plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 3) {

            if (args[0].equalsIgnoreCase("speed")) {

                if (!plugin.getManager().getJumpPads(player.getWorld().getName()).contains(args[1])) {
                    player.sendMessage("Такого батута не существует! Создайте его");
                    return true;
                }

                plugin.getManager().setSpeedJumpPad(player.getLocation().getWorld(), args[1], Double.parseDouble(args[2]));
                return true;
            }

            if (args[0].equalsIgnoreCase("height")) {

                if (!plugin.getManager().getJumpPads(player.getWorld().getName()).contains(args[1])) {
                    player.sendMessage("Такого батута не существует! Создайте его");
                    return true;
                }

                plugin.getManager().setSpeedJumpHeight(player.getLocation().getWorld(), args[1], Integer.parseInt(args[2]));
                return true;
            }

            return true;
        }

        if (args.length != 2) {
            player.sendMessage("/jumppad <create/setto/teleport> <name>");
            return true;
        }

        String firstArg = args[0];
        String name = args[1];

        String notFound = "Такого батута не существует! Создайте его";

        if (firstArg.equalsIgnoreCase("create")) {

            if (plugin.getManager().getJumpPads(player.getWorld().getName()).contains(name)) {
                player.sendMessage("Такой батут уже есть в этом мире! Назовите подругому");
                return true;
            }

            plugin.getManager().createJumpPad(name, player.getLocation());
            return true;
        }

        if (firstArg.equalsIgnoreCase("setto")) {

            if (!plugin.getManager().getJumpPads(player.getWorld().getName()).contains(name)) {
                player.sendMessage(notFound);
                return true;
            }

            plugin.getManager().setToJumpPad(name, player.getLocation());
            return true;
        }

        if (firstArg.equalsIgnoreCase("teleport")) {

            if (!plugin.getManager().getJumpPads(player.getWorld().getName()).contains(name)) {
                player.sendMessage(notFound);
                return true;
            }

            player.teleport(plugin.getManager().getJumpPadLocation(player.getWorld(), name));
            return true;
        }

        return true;
    }

}
