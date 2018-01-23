package me.soldado.geradores;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Dispenser;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Core implements Listener {

	public Main plugin;
	
	public Core(Main plugin)
	{
		this.plugin = plugin;
	}
	
	HashMap<Location, Gerador> geradores = new HashMap<Location, Gerador>();
	HashMap<Inventory, Gerador> invs = new HashMap<Inventory, Gerador>();
	
	@EventHandler
	public void colocar(BlockPlaceEvent event){
		if(event.isCancelled()) return;
		ItemStack item = event.getItemInHand();
		if(!checkGerador(item)) return;
		int nivel = getNivel(item);
		Tipo tipo = getTipo(item);
		Location loc = event.getBlock().getLocation();
		criarGerador(loc, tipo, nivel);
		//event.getPlayer().sendMessage(plugin.msg.geradorcolocado);
	}
	
	@EventHandler
	public void quebrar(BlockBreakEvent event){
		if(event.isCancelled()) return;
		Location loc = event.getBlock().getLocation();
		if(geradores.containsKey(loc)){
			Gerador ger = geradores.get(loc);
			ItemStack item = new ItemStack(Material.DISPENSER);
			ItemMeta meta = item.getItemMeta();
			String nome = "";
			if(ger.getTipo().equals(Tipo.PEDRA)){
				nome = ChatColor.GRAY + "Gerador de Pedra";
			}else if(ger.getTipo().equals(Tipo.MADEIRA)){
				nome = ChatColor.YELLOW + "Gerador de Madeira";
			}else if(ger.getTipo().equals(Tipo.DIAMANTE)){
				nome = ChatColor.AQUA + "Gerador de Diamante";
			}
			String lore = ChatColor.GREEN + "Nivel: " + ChatColor.GRAY;
			for(int i = 0; i < ger.getNivel(); i++) lore += "I";
			meta.setDisplayName(nome);
			meta.setLore(Arrays.asList(lore));
			item.setItemMeta(meta);
			geradores.remove(loc);
			event.setCancelled(true);
			event.getBlock().setType(Material.AIR);
			event.getBlock().getLocation().getWorld().dropItem(event.getBlock().getLocation(), item);
			//event.getPlayer().sendMessage(plugin.msg.geradorquebrado);
		}
	}
	
	@EventHandler
	public void info(PlayerInteractEvent event){
		if(event.isCancelled()) return;
		if(!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
		if(!event.getClickedBlock().getType().equals(Material.DISPENSER)) return;
		Location loc = event.getClickedBlock().getLocation();
		if(geradores.containsKey(loc)){
			Gerador ger = geradores.get(loc);
			Player p = event.getPlayer();
			Inventory inv = Bukkit.getServer().createInventory(null, 
					InventoryType.DISPENSER, ChatColor.AQUA + "Gerador");
			
			inv.setItem(0, separador());
			inv.setItem(1, etiqueta(ger));
			inv.setItem(2, separador());
			inv.setItem(3, separador());
			inv.setItem(4, itemDoMeio(ger));
			inv.setItem(5, separador());
			inv.setItem(6, separador());
			inv.setItem(7, relogio(ger, plugin.mec.tempo.get(ger)));
			inv.setItem(8, separador());
			invs.put(inv, ger);
			p.openInventory(inv);
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void click(InventoryClickEvent event){
		if(event.getInventory().getTitle().toLowerCase().contains("gerador")) event.setCancelled(true);
	}
	
	@EventHandler
	public void fechar(InventoryCloseEvent event){
		if(invs.containsKey(event.getInventory())) invs.remove(event.getInventory());
	}
	
	public void criarGerador(Location loc, Tipo tipo, int nivel){
		Gerador ger = new Gerador(loc, tipo, nivel);
		geradores.put(loc, ger);
		plugin.mec.tempo.put(ger, plugin.mec.getDelay(ger));
	}
    
    public boolean checkGerador(ItemStack item){
    	if(item == null) return false;
    	if(item.getType() != Material.DISPENSER) return false;
    	if(!item.hasItemMeta()) return false;
    	if(!item.getItemMeta().hasLore() || !item.getItemMeta().hasDisplayName()) return false;
    	ItemMeta meta = item.getItemMeta();
    	if(!meta.getDisplayName().contains("Gerador de")) return false;
    	if(!meta.getLore().get(0).contains("Nivel")) return false;
    	if(getTipo(item) == null) return false;
    	if(getNivel(item) == -1) return false;
    	return true;
    }
    
    Tipo getTipo(ItemStack item){
    	String nome = item.getItemMeta().getDisplayName();
    	String tp = nome.replace("Gerador de ", "");
    	if(tp.toLowerCase().contains("pedra")) return Tipo.PEDRA;
    	else if(tp.toLowerCase().contains("madeira")) return Tipo.MADEIRA;
    	else if(tp.toLowerCase().contains("diamante")) return Tipo.DIAMANTE;
    	else return null;
    }
    
    int getNivel(ItemStack item){
    	String lore1 = item.getItemMeta().getLore().get(0);
    	int nivel = StringUtils.countMatches(lore1, "I");
    	if(nivel > 0) return nivel;
    	else return -1;
    }
    
    ItemStack relogio(Gerador ger, int tempo){
    	ItemStack relogio = new ItemStack(Material.WATCH);
    	ItemMeta meta = relogio.getItemMeta();
    	meta.setDisplayName(ChatColor.GREEN + "Cronômetro");
    	meta.setLore(Arrays.asList(ChatColor.RED + "Intervalo: " + ChatColor.GRAY + plugin.mec.tempo.get(ger)
    	+ "/" + plugin.mec.getDelay(ger)+"s"));
    	relogio.setItemMeta(meta);
    	return relogio;
    }
    
    ItemStack itemDoMeio(Gerador ger){
    	ItemStack item = new ItemStack(Material.STONE);
    	ItemMeta meta = item.getItemMeta();
    	meta.setDisplayName(ChatColor.GREEN + "Bloco sendo gerado");
    	if(ger.getTipo().equals(Tipo.PEDRA)){
    		meta.setLore(Arrays.asList(ChatColor.GRAY + "Pedra"));
    		item.setType(Material.STONE);
    	}else if(ger.getTipo().equals(Tipo.MADEIRA)){
    		meta.setLore(Arrays.asList(ChatColor.YELLOW + "Madeira"));
    		item.setType(Material.LOG);
    	}else if(ger.getTipo().equals(Tipo.DIAMANTE)){
    		meta.setLore(Arrays.asList(ChatColor.AQUA + "Minério de Diamante"));
    		item.setType(Material.DIAMOND_ORE);
    	}
    	item.setItemMeta(meta);
    	return item;
    }
    
    ItemStack etiqueta(Gerador ger){
    	ItemStack etiqueta = new ItemStack(Material.NAME_TAG);
    	ItemMeta meta = etiqueta.getItemMeta();
    	meta.setDisplayName(ChatColor.GREEN + "Informações do Gerador");
    	List<String> lore = new ArrayList<String>();
    	String lore1 = ChatColor.RED + "Tipo: ";
    	if(ger.getTipo().equals(Tipo.PEDRA)) lore1 += ChatColor.GRAY + "Pedra";
    	else if(ger.getTipo().equals(Tipo.MADEIRA)) lore1 += ChatColor.YELLOW + "Madeira";
    	else if(ger.getTipo().equals(Tipo.DIAMANTE)) lore1 += ChatColor.AQUA + "Diamante";
    	lore.add(lore1);
    	String lore2 = ChatColor.RED + "Nível: " + ChatColor.GRAY;
    	for(int i = 0; i < ger.getNivel(); i++) lore2 += "I";
    	lore.add(lore2);
    	lore.add(ChatColor.RED + "Intervalo: " + ChatColor.GRAY + plugin.mec.getDelay(ger) + "s");
    	meta.setLore(lore);
    	etiqueta.setItemMeta(meta);
    	return etiqueta;
    }
    
    ItemStack separador(){
    	ItemStack separador = new ItemStack(Material.STAINED_GLASS_PANE);
    	ItemMeta meta = separador.getItemMeta();
    	meta.setDisplayName(ChatColor.GRAY + "Gerador");
    	separador.setItemMeta(meta);
    	return separador;
    }
    
    public void atualizarContador(){
    	for(Inventory inv : invs.keySet()){
    		inv.setItem(7, relogio(invs.get(inv), plugin.mec.tempo.get(invs.get(inv))));
    	}
    }

    public void registrarEventos(){
    	plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
}
