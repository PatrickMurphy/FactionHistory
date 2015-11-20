
package com.patrickmurphywebdesign.factionhistory.commands;

import com.patrickmurphoto.factionhistory.PlayerHistory;
import com.patrickmurphoto.factionhistory.FactionHistory;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.massivecraft.factions.entity.UPlayer;

public class FhistoryCommand implements CommandExecutor {
    private final FactionHistory plugin;

    public FhistoryCommand(FactionHistory plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = FactionHistory.getPlayer(sender, args, 0);

        if (player == null) {
						Sender.sendMessage('Incorrect Sender');
            return true;
        }
			String name;
			if(args.length < 1){
				name = player.getName();
			}else{
				name = args[0];
			}
        List<PlayerHistory> histories = plugin.getDatabase().find(PlayerHistory.class).where().ieq("playerName", name).findList();

        if (histories.isEmpty()) {
					// create init history
					sender.sendMessage("That player has no recorded faction history! Creating one starting now!");
					PlayerHistory newHistory = new PlayerHistory();
					// get player current faction
					UPlayer factionPlayer = UPlayer.get(player);
					newHistory.setPlayer(player);
					newHisotry.setPlayerName(player.getName());
					newHistory.addNewMembershipChange(factionPlayer.getFaction(), "initial");
					plugin.getDatabase().save(newHistory);
        } else {
          String[] result = new String[5];

					if(histories.length == 1){
						PlayerHistory history = histories.get(0);
						String[] factions = Collections.reverse(Arrays.asList(history.getLastFactionsList()));
						String[] times =  Collections.reverse(Arrays.asList(history.getMembershipChangeTimeList()));
						String[] reasons =  Collections.reverse(Arrays.asList(history.getMembershipChangeReasonList()));
						// create result
						sender.sendMessage("Faction history for " + player.getName());
						//sender.sendMessage("Note: Most recent faction first.");
						sender.sendMessage("Faction - Reason - Time");
						String lastTime = "false";
						for(int i = 0; i<5; i++){
							String thisTime = "";
							if(i == 0){
								lastTime = times[0];
								thisTime = "Current";
							}else{
								int timeDiff = (Integer.parseInt(lastTime) - Integer.parseInt(times[i]))/1000; // in seconds
								if(timeDiff >= 60 && timeDiff < 3600){
									// min
									thisTime = timeDiff/60 + " minutes";
								}else if(timeDiff >= 3600 && timeDiff < 86400){
									// hours
									thisTime = timeDiff/3600 + " hours";
								}else if(timeDiff >= 86400 && timeDiff < 604800){
									// days
									thisTime = timeDiff/86400 + " days";
								}else if(timeDiff >= 604800 && timeDiff < 2419200){
									// weeks
									thisTime = timeDiff/604800 + " weeks";
								}else if(timeDiff >= 2419200){
									// months
									thisTime = timeDiff/2419200 + " months";
								}else{
									// seconds
									thisTime = timeDiff + " seconds";
								}
								lastTime = times[i];
							}
							sender.sendMessage((i+1)+". " + factions[i] + " - " + reasons[i] + " - " + thisTime);
						}
					}else{
						sender.sendMessage("Sorry an error has occured, multiple histories");
					}


        }

        return true;
    }
}
