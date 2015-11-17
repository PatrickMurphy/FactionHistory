
package com.patrickmurphoto.factionhistory;

import com.patrickmurphoto.factionhistory.commands.FhistoryCommand;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.PersistenceException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
import com.massivecraft.factions.event.EventFactionsMembershipChange;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.UPlayer;

public class FactionHistory extends JavaPlugin implements Listener {
    public void onDisable() {
			System.out.println('Faction History Plugin has been disabled');
    }

    public void onEnable() {
        PluginDescriptionFile desc = getDescription();

        System.out.println(desc.getFullName() + " has been enabled");

        getCommand("fhistory").setExecutor(new FhistoryCommand(this));

        setupDatabase();
    }

    private void setupDatabase() {
        try {
            getDatabase().find(PlayerHistory.class).findRowCount();
        } catch (PersistenceException ex) {
            System.out.println("Installing database for " + getDescription().getName() + " due to first time usage");
            installDDL();
        }
    }

    @Override
    public List<Class<?>> getDatabaseClasses() {
        List<Class<?>> list = new ArrayList<Class<?>>();
        list.add(PlayerHistory.class);
        return list;
    }

		// listen for event factions membership change


    public static boolean anonymousCheck(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Cannot execute that command, I don't know who you are!");
            return true;
        } else {
            return false;
        }
    }

    public static Player getPlayer(CommandSender sender, String[] args, int index) {
        if (args.length > index) {
            List<Player> players = sender.getServer().matchPlayer(args[index]);

            if (players.isEmpty()) {
                sender.sendMessage("I don't know who '" + args[index] + "' is!");
                return null;
            } else {
                return players.get(0);
            }
        } else {
            if (anonymousCheck(sender)) {
                return null;
            } else {
                return (Player)sender;
            }
        }
    }

	@EventHandler
	public void onEventFactionsMembershipChange(EventFactionsMembershipChange event){
			List<PlayerHistory> histories = plugin.getDatabase().find(PlayerHistory.class).where().ieq("playerName", event.getPlayer().getName()).findList();

        if (histories.isEmpty()) {
					// create init history
					PlayerHistory newHistory = new PlayerHistory();
					// get player current faction
					UPlayer factionPlayer = UPlayer.get(player);
					newHistory.setPlayer(player);
					newHisotry.setPlayerName(player.getName());
					newHistory.addNewMembershipChange(event.getNewFaction().getName(), "initial");
					FactionHistory plugin = this;
					plugin.getDatabase().save(newHistory);
        } else {
					boolean save = true;
					String reasonText = "Unspecified";
					switch(event.getReason()){
						case JOIN:
							reasonText = "join";
							// check if prev was wilderness
						break;
						case CREATE:
							reasonText = "join";
							// check if prev was wilderness
						break;
						case LEAVE:
							reasonText = "Left";
						break;
						case KICK:
							reasonText = "Kicked";
						break;
						case DISBAND:
							reasonText = "Disbanded";
						break;
						case RANK:
							save = false;// if change of rank no need to fire event save
						break;
					}
					if(save){
						PlayerHistory playersHistory = histories.get(0);
						playersHistory.addNewMembershipChange(event.getNewFaction().getName(),reasonText);
						getDatabase().save(playersHistory);
					}
				}

	}
}
