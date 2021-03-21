package com.piratecody.uhcutils.commands;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.World;

public class DistributePlayers implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		// gets the list of online players (to be distributed)
		Player[] players = Bukkit.getServer().getOnlinePlayers().toArray(new Player[0]);
		
		// parse arguments
		int xMax = 0, zMax = 0, minimumDistanceBetweenSpawns = 0;
		
		try {
			xMax = Integer.parseInt(args[0]);
			zMax = Integer.parseInt(args[1]);
			minimumDistanceBetweenSpawns = Integer.parseInt(args[2]);
		} catch(Exception e) {
			sender.sendMessage(ChatColor.RED + "Invalid arguments for distributeplayers!");
			e.printStackTrace(System.err);
			return true;
		}
		
		// generate random locations to teleport players
		// must be within arguments, and must be at the surface 
		// minimum distance between spawns (done?)
		// note: didnt crash, will assume working unless proven otherwise
		ArrayList<Location> locations = new ArrayList<Location>();
		for(int i = 0; i < players.length; ++i) {
			
			Location loc = generateRandomSurfaceLocation(players[i].getWorld(), xMax, zMax);
			
			if(locations.isEmpty()) {
				locations.add(loc);
			} else {
				while(!compliesWithMinDistance(loc, locations, minimumDistanceBetweenSpawns)) {
					loc = generateRandomSurfaceLocation(players[i].getWorld(), xMax, zMax);
				}
				locations.add(loc);
			}
			
			//locations.add(generateRandomSurfaceLocation(players[i].getPlayer().getWorld(), xMax, zMax));
			
		}
		
		//load chunks at each location
		for(Location l : locations) {
			l.getChunk().setForceLoaded(true);
			l.getChunk().load();
		}
		
		// teleport players
		for(int i = 0; i < players.length; ++i) {
			players[i].teleport(locations.get(i));
		}
		
		// turn off force load chunks
		for(Location l : locations) {
			l.getChunk().setForceLoaded(false);
		}
		
		//data pack stuff
		
		
		
		return true;
	}
	
	public Location generateRandomSurfaceLocation(World world, int xMax, int zMax) {
		
		Random rand = new Random();
		double x = rand.nextInt(xMax);
		if(rand.nextBoolean()) {
			x = -x;
		}
		
		double z = rand.nextInt(zMax);
		if(rand.nextBoolean()) {
			z = -z;
		}
		
		Location loc = new Location(world, x, 0.0, z);
		loc.setY(world.getHighestBlockYAt(loc)+1);
		
		return loc;
		
	}

	public boolean compliesWithMinDistance(Location loc, ArrayList<Location> locations, int minDistance) {
		
		boolean compliesWithMinDist = true;
		for(Location l : locations) {
			if(loc.distance(l) < minDistance) {
				compliesWithMinDist = false;
				break;
			}
		}
		
		return compliesWithMinDist;
		
	}
}
