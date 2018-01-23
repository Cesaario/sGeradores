package me.soldado.geradores;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Backup {

	public Main plugin;
	
	public Backup(Main plugin)
	{
		this.plugin = plugin;
	}
	
	File bdFile;
	FileConfiguration bd;
	
	public void recarregarGerador(String s){
		String[] param = s.split(";");
		boolean suc = true;
		try{
			if(param.length == 3){
				Tipo tipo = null;
				switch(param[0]){
					case "pedra":
						tipo = Tipo.PEDRA;
						break;
					case "madeira":
						tipo = Tipo.MADEIRA;
						break;
					case "diamante":
						tipo = tipo.DIAMANTE;
						break;
				}
				int nivel = Integer.parseInt(param[1]);
				String[] locs = param[2].split(":");
				if(locs.length == 4){ 
					String mundo = locs[0];
					int x = Integer.parseInt(locs[1]);
					int y = Integer.parseInt(locs[2]);
					int z = Integer.parseInt(locs[3]);
					WorldCreator wc = new WorldCreator(mundo);
					World md = Bukkit.createWorld(wc);
					Location loc = new Location(md, x, y, z);
					
					//criar
					if(loc.getBlock().getType().equals(Material.DISPENSER)){
						plugin.core.criarGerador(loc, tipo, nivel);
					}
					
				}else suc = false;
			}else suc = false;
		}catch(Exception e){
			e.printStackTrace();
			plugin.getLogger().info("Erro ao carregar gerador!!!");
			suc = false;
		}
		if(!suc) plugin.getLogger().info("Erro ao carregar gerador!!!");
		else plugin.getLogger().info("Gerador carregado com sucesso!!!");
	}
	
	public void carregar(){
		if(bd.getStringList("geradores") != null){
			List<String> s = bd.getStringList("geradores");
			for(String str : s){
				recarregarGerador(str);
			}
		}
	}
	
	public void salvar(){
		ArrayList<Gerador> geradores = new ArrayList<Gerador>();
		geradores.addAll(plugin.core.geradores.values());
		if(bd.getStringList("geradores") != null){
			List<String> s = bd.getStringList("geradores");
			s.clear();
			for(Gerador l : geradores){
				if(l != null){
					String str = l.getString();
					s.add(str);
					plugin.getLogger().info("Gerador salvo!");
				}else plugin.getLogger().info("Gerador Nulo!");
			}
			bd.set("geradores", s);
			try {
				bd.save(bdFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void iniciarBackup(){

		if (bdFile == null) {
			bdFile = new File(plugin.getDataFolder(), "geradores.dat");
		}
		if (!bdFile.exists()) {
			plugin.saveResource("geradores.dat", false);
		}
		bd = YamlConfiguration.loadConfiguration(bdFile);
	}
	
}
