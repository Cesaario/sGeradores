package me.soldado.geradores;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Comandos implements CommandExecutor {

	public Main plugin;
	
	public Comandos(Main plugin)
	{
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("gerador")){
			boolean continuar = false;
			if(sender instanceof ConsoleCommandSender) continuar = true;
			if(sender instanceof Player){
				Player p = (Player) sender;
				if(p.hasPermission("gerador.pegar")) continuar = true;
			}
			if(continuar){
				if(args.length == 2 || args.length == 3){
					boolean valido = true;
					String tipos = args[0].toLowerCase();
					String nome = "";
					String lore = "";
					switch(tipos){
						case "pedra":
							nome = ChatColor.GRAY + "Gerador de Pedra";
							break;
						case "madeira":
							nome = ChatColor.YELLOW + "Gerador de Madeira";
							break;
						case "diamante":
							nome = ChatColor.AQUA + "Gerador de Diamante";
							break;
						default:
							nome = "invalido";
					}
					int nivel = 0;
					try{
						nivel = Integer.parseInt(args[1]);
						lore = ChatColor.GREEN + "Nivel: " + ChatColor.GRAY;
						for(int i = 0; i < nivel; i++) lore += "I";
					}catch(Exception e){valido = false;}
					if(nome == "invalido") valido = false;
					if(valido){
						ItemStack item = new ItemStack(Material.DISPENSER);
						ItemMeta meta = item.getItemMeta();
						meta.setDisplayName(nome);
						meta.setLore(Arrays.asList(lore));
						item.setItemMeta(meta);
						if(args.length == 2){
							if(sender instanceof ConsoleCommandSender){
								sender.sendMessage("Você não especificou o jogador");
							}else if(sender instanceof Player){
								((Player) sender).getInventory().addItem(item);
							}
						}else{
							try{
								Player a = Bukkit.getServer().getPlayer(args[2]);
								a.getInventory().addItem(item);
							}catch(Exception e){
								sender.sendMessage("Jogador offline ou inxeistente");
							}
						}
					}else sender.sendMessage("erro");
				}else sender.sendMessage("Use: /gerador <tipo> <nivel> ~jogador~");
			}else sender.sendMessage("Sem permissão...");
			return true;
		}
		return false;
	}
	
	public void registrarComandos(){
		plugin.getCommand("gerador").setExecutor(this);
	}
	
}
