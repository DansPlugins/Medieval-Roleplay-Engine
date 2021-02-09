package dansplugins.rpsystem;

import dansplugins.rpsystem.commands.*;
import dansplugins.rpsystem.data.EphemeralData;
import dansplugins.rpsystem.data.PersistentData;
import dansplugins.rpsystem.utils.ArgumentParser;
import dansplugins.rpsystem.utils.ColorChecker;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandInterpreter {

    private static CommandInterpreter instance;

    private CommandInterpreter() {

    }

    public static CommandInterpreter getInstance() {
        if (instance == null) {
            instance = new CommandInterpreter();
        }
        return instance;
    }

    public boolean interpretCommand(CommandSender sender, String label, String[] args) {

        // help command
        if (label.equalsIgnoreCase("rphelp")) {
            HelpCommand command = new HelpCommand();
            command.showListOfCommands(sender);
            return true;
        }

        // card command
        if (label.equalsIgnoreCase("card")) {
            CardCommand command = new CardCommand();
            if (args.length == 0) {
                command.showCard(sender, args, PersistentData.getInstance().getCards());
                return true;
            } else {

                if (args[0].equalsIgnoreCase("help")) {
                    command.showHelpMessage(sender);
                    return true;
                }

                if (args[0].equalsIgnoreCase("name")) {
                    command.changeName(sender, args, PersistentData.getInstance().getCards());
                    return true;
                }
                if (args[0].equalsIgnoreCase("race")) {
                    command.changeRace(sender, args, PersistentData.getInstance().getCards());
                    return true;
                }
                if (args[0].equalsIgnoreCase("subculture")) {
                    command.changeSubculture(sender, args, PersistentData.getInstance().getCards());
                    return true;
                }
                if (args[0].equalsIgnoreCase("religion")) {
                    command.changeReligion(sender, args, PersistentData.getInstance().getCards());
                    return true;
                }
                if (args[0].equalsIgnoreCase("age")) {
                    command.changeAge(sender, args, PersistentData.getInstance().getCards());
                    return true;
                }
                if (args[0].equalsIgnoreCase("gender")) {
                    command.changeGender(sender, args, PersistentData.getInstance().getCards());
                    return true;
                }

                if (args[0].equalsIgnoreCase("forcesave")) {
                    command.forceSave(sender);
                }

                if (args[0].equalsIgnoreCase("forceload")) {
                    command.forceLoad(sender);
                }

                command.showPlayerInfo(sender, args, PersistentData.getInstance().getCards());
                return true;
            }
        }

        if (label.equalsIgnoreCase("bird")) {
            BirdCommand command = new BirdCommand();
            command.sendBird(sender, args);
            return true;
        }

        if (label.equalsIgnoreCase("local") || label.equalsIgnoreCase("rp")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.hasPermission("rp.local") || player.hasPermission("rp.rp") || player.hasPermission("rp.default")) {
                    if (!EphemeralData.getInstance().getPlayersSpeakingInLocalChat().contains(player.getUniqueId())) {
                        EphemeralData.getInstance().getPlayersSpeakingInLocalChat().add(player.getUniqueId());
                        player.sendMessage(ChatColor.GREEN + "You are now talking in local chat.");
                        return true;
                    }
                    else {
                        player.sendMessage(ChatColor.RED + "You're already talking in local chat!");
                        return false;
                    }
                }
                else {
                    player.sendMessage(ChatColor.RED + "Sorry! In order to use this command, you need one the following permissions: 'rp.local', 'rp.rp'");
                    return false;
                }

            }
        }

        if (label.equalsIgnoreCase("global") || label.equalsIgnoreCase("ooc")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.hasPermission("rp.global") || player.hasPermission("rp.ooc") || player.hasPermission("rp.default")) {
                    if (EphemeralData.getInstance().getPlayersSpeakingInLocalChat().contains(player.getUniqueId())) {
                        EphemeralData.getInstance().getPlayersSpeakingInLocalChat().remove(player.getUniqueId());
                        player.sendMessage(ChatColor.GREEN + "You are now talking in global chat.");
                    }
                    else {
                        player.sendMessage(ChatColor.RED + "You're already talking in global chat!");
                    }
                }
                else {
                    player.sendMessage(ChatColor.RED + "Sorry! In order to use this command, you need one the following permissions: 'rp.global', 'rp.ooc'");
                }

            }
        }

        if (label.equalsIgnoreCase("emote") || label.equalsIgnoreCase("me")) {

            int emoteRadius = MedievalRoleplayEngine.getInstance().getConfig().getInt("emoteRadius");
            String emoteColor = MedievalRoleplayEngine.getInstance().getConfig().getString("emoteColor");

            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.hasPermission("rp.emote") || player.hasPermission("rp.me") || player.hasPermission("rp.default")) {
                    if (args.length > 0) {
                        String message = ArgumentParser.getInstance().createStringFromFirstArgOnwards(args, 0);
                        String characterName = PersistentData.getInstance().getCard(player.getUniqueId()).getName();

                        Messenger.getInstance().sendMessageToPlayersWithinDistance(player, ColorChecker.getInstance().getColorByName(emoteColor) + "" + ChatColor.ITALIC + characterName + " " + message, emoteRadius);
                    }
                }
                else {
                    player.sendMessage(ChatColor.RED + "Sorry! In order to use this command, you need one the following permissions: 'rp.emote', 'rp.me'");
                    return false;
                }

            }
        }

        if (label.equalsIgnoreCase("roll") || label.equalsIgnoreCase("dice")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.hasPermission("rp.roll") || player.hasPermission("rp.dice") || player.hasPermission("rp.default")) {
                    if (args.length > 0) {
                        try {
                            int max = Integer.parseInt(args[0]);
                            Messenger.getInstance().sendMessageToPlayersWithinDistance(player,ChatColor.AQUA + "" + ChatColor.ITALIC + player.getName() + " has rolled a " + rollDice(max) + " out of " + max + ".", 25);
                        }
                        catch(Exception ignored) {

                        }
                    }
                }
                else {
                    player.sendMessage(ChatColor.RED + "Sorry! In order to use this command, you need one the following permissions: 'rp.roll', 'rp.dice'");
                }

            }
        }

        if (label.equalsIgnoreCase("title")) {
            TitleCommand command = new TitleCommand();
            command.titleBook(sender, args);
            return true;
        }

        if (label.equalsIgnoreCase("yell")) {
            YellCommand command = new YellCommand();
            command.sendLoudMessage(sender, args);
            return true;
        }

        if (label.equalsIgnoreCase("whisper")) {
            WhisperCommand command = new WhisperCommand();
            command.sendQuietMessage(sender, args);
            return true;
        }

        if (label.equalsIgnoreCase("rpconfig")) {
            ConfigCommand command = new ConfigCommand();
            command.handleConfigAccess(sender, args);
            return true;
        }

        return false;
    }

    private static int rollDice(int max) {
        return (int)(Math.random() * max + 1);
    }

}
