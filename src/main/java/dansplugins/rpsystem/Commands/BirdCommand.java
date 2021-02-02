package dansplugins.rpsystem.Commands;

import dansplugins.rpsystem.MedievalRoleplayEngine;
import dansplugins.rpsystem.Utilities;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.bukkit.Bukkit.getServer;

public class BirdCommand {

    public void sendBird(CommandSender sender, String[] args) {
        // player check
        if (!(sender instanceof Player)) {
            return;
        }

        Player player = (Player) sender;

        if (player.hasPermission("rp.bird") || player.hasPermission("rp.default")) {
            if (MedievalRoleplayEngine.getInstance().playersWithBusyBirds.contains(player.getUniqueId())) {
                player.sendMessage(ChatColor.RED + "Your bird is already on a mission!");
                return;
            }

            // zero args check
            if (args.length < 2) {
                player.sendMessage(ChatColor.RED + "Usage: /bird (player-name) (message)");
                return;
            }

            Player targetPlayer = getServer().getPlayer(args[0]);

            if (targetPlayer == null) {
                player.sendMessage(ChatColor.RED + "That player isn't online!");
                return;
            }

            String message = Utilities.createStringFromFirstArgOnwards(args, 1);

            if (!(player.getLocation().getWorld().getName().equalsIgnoreCase(targetPlayer.getLocation().getWorld().getName()))) {
                player.sendMessage(ChatColor.RED + "You can't send a bird to a player in another world.");
                return;
            }

            double distance = player.getLocation().distance(targetPlayer.getLocation());
            int blocksPerSecond = 20;
            int seconds = (int)distance/blocksPerSecond;

            getServer().getScheduler().runTaskLater(MedievalRoleplayEngine.getInstance(), new Runnable() {
                @Override
                public void run() {
                    targetPlayer.sendMessage(ChatColor.GREEN + "A bird lands nearby and drops a message at your feet! It was sent by " + player.getName() + ". It reads:");
                    targetPlayer.sendMessage(ChatColor.GREEN + "" + ChatColor.ITALIC + "'" + message + "'");
                    player.sendMessage(ChatColor.GREEN + "Your bird has reached " + targetPlayer.getName() + "!");
                    MedievalRoleplayEngine.getInstance().playersWithBusyBirds.remove(player.getUniqueId());
                    Utilities.getInstance().sendMessageToPlayersWithinDistanceExcludingTarget(targetPlayer, ChatColor.AQUA + String.format("A bird lands nearby and drops a message at the feet of %s!", targetPlayer.getName()), 10);
                }
            }, seconds * 20);

            player.sendMessage(ChatColor.GREEN + "The bird flies off with your message.");
            MedievalRoleplayEngine.getInstance().playersWithBusyBirds.add(player.getUniqueId());
        }
        else {
            player.sendMessage(ChatColor.RED + "Sorry! In order to use this command, you need the following permission: 'rp.bird'");
        }

    }
}
