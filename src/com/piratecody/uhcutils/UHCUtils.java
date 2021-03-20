package com.piratecody.uhcutils;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import com.piratecody.uhcutils.commands.DistributePlayers;

public class UHCUtils extends JavaPlugin{
	
	@Override
	public void onEnable() {
		
		getCommand("distributeplayers").setExecutor(new DistributePlayers());
		getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "UHCUtils Enabled");
		
	}
	
	@Override
	public void onDisable() {
		
		getServer().getConsoleSender().sendMessage(ChatColor.RED + "UHCUtils Disabled");
		
	}

}
