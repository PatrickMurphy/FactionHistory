
package com.patrickmurphoto.factionhistory;

import com.avaje.ebean.validation.Length;
import com.avaje.ebean.validation.NotEmpty;
import com.avaje.ebean.validation.NotNull;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

@Entity()
@Table(name="fh_playerhistory")
public class PlayerHistory {
	@Id
	private int id;

	@NotNull
	private String playerName;

	@NotNull
	private String lastFactionsList; // comma seperated, first item is oldest

	@NotNull
	private String membershipChangeTimeList;

	@NotNull
	private String membershipChangeReasonList;

	public void setId(int id) {
			this.id = id;
	}

	public int getId() {
			return id;
	}

	public String getPlayerName() {
			return playerName;
	}

	public void setPlayerName(String playerName) {
			this.playerName = playerName;
	}

	public Player getPlayer() {
			return Bukkit.getServer().getPlayer(playerName);
	}

	public void setPlayer(Player player) {
			this.playerName = player.getName();
	}

	public String[] getLastFactionsList(){
		// return array
		return lastFactionsList.split(",");
	}

	public void setLastFactionsList(String[] newFactions){
		// parse array into string
		String[] tempArr;

		// if big trim
		if(newFactions.length > 5){
			int startIndex = newFactions.length - 5;
			tempArr = Arrays.copyOfRange(newFactions, startIndex, newFactions.length);
		}else{
			tempArr = newFactions;
		}

		// to string
		String arrStr = String.join(',', tempArr);

		// save
		lastFactionList = arrStr;
	}

	public String[] getMembershipChangeTimeList(){
		// return array
		return membershipChangeTimeList.split(",");
	}

	public void setMembershipChangeTimeList(String[] newTimes){
		// parse array into string
		String[] tempArr;

		// if big trim
		if(newTimes.length > 5){
			int startIndex = newTimes.length - 5;
			tempArr = Arrays.copyOfRange(newTimes, startIndex, newTimes.length);
		}else{
			tempArr = newTimes;
		}

		// to string
		String arrStr = String.join(',', tempArr);

		// save
		membershipChangeTimeList = arrStr;

	}

	public String[] getMembershipChangeReasonList(){
		// return array
		return membershipChangeReasonList.split(",");
	}

	public void setMembershipChangeReasonList(String[] newReasons){
				// parse array into string
		String[] tempArr;

		// if big trim
		if(newReasons.length > 5){
			int startIndex = newReasons.length - 5;
			tempArr = Arrays.copyOfRange(newReasons, startIndex, newReasons.length);
		}else{
			tempArr = newReasons;
		}

		// to string
		String arrStr = String.join(',', tempArr);

		// save
		membershipChangeReasonList = arrStr;
	}

	public void addNewMembershipChange(String Faction, String reason, string time){
		String[] tempFactionList = getLastFactionsList();
		String[] tempReasons = getMembershipChangeReasonList();
		String[] tempTimes = getMembershipChangeTimeList();
		if(reason.equals("join")){
			// check if wilderness
			if(tempFactionList[tempFactionList.length-1].equals('Wilderness')){
				// if so remove
				tempFactionList[tempFactionList.length-1] = Faction;
				tempReasons[tempReasons.length-1] = "      ";
				tempTimes[tempTimes.length-1] = time;
			}
		}else{
			// faction
			tempFactionList.add(Faction);

			// reason
			if(!reason.equals("initial")){
				tempReasons[tempReasons.length-1] = reason;
			}
			tempReasons.add("      ");

			// time in seconds since epoch
			tempTimes.add(time);
		}
		setLastFactionsList(tempFactionList);
		setMembershipChangeReasonList(tempReasons);
		setMembershipChangeTimeList(tempTimes);
	}

	public void addNewMembershipChange(String Faction, String reason){
		addNewMembershipChange(Faction,reason, (String)Date.getTime());
	}

	public void addNewMembershipChange(String Faction){
		addNewMembershipChange(Faction, "Unspecified", (String)Date.getTime());
	}

}
