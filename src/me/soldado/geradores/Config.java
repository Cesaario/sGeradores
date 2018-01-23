package me.soldado.geradores;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Config{
	
	public Main plugin;
	
	public Config(Main plugin)
	{
		this.plugin = plugin;
	}
	
	File configFile;
	FileConfiguration config;
	
	private void iniciarValores(){
		
	}
	
	public void iniciarConfig(){

		if (configFile == null) {
			configFile = new File(plugin.getDataFolder(), "config.yml");
		}
		if (!configFile.exists()) {
			plugin.saveResource("config.yml", false);
		}
		config = YamlConfiguration.loadConfiguration(configFile);
		iniciarValores();
	}
	
	private String getString(String s){
		return config.getString(s).replace("&", "§");
	}
	
	private int getInt(String s){
		return config.getInt(s);
	}
	
	private boolean getBoolean(String s){
		return config.getBoolean(s);
	}
}
