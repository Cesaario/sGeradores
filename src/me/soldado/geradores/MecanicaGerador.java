package me.soldado.geradores;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.material.Dispenser;
import org.bukkit.material.MaterialData;
import org.bukkit.scheduler.BukkitRunnable;

public class MecanicaGerador extends BukkitRunnable {

	public Main plugin;
	
	public MecanicaGerador(Main plugin)
	{
		this.plugin = plugin;
	}
	
	HashMap<Gerador, Integer> tempo = new HashMap<Gerador, Integer>();
	
//	public void cronometro(){
//		new BukkitRunnable(){
//			public void run() {
//				for(Gerador ger : tempo.keySet()){
//					int delay = getDelay(ger);
//					int atual = tempo.get(ger);
//					if(atual <= 0){
//						gerar(ger);
//						tempo.put(ger, delay);
//					}else{
//						atual--;
//						tempo.put(ger, atual);
//					}
//				}
//				plugin.core.atualizarContador();
//			}
//		}.runTaskTimer(plugin, 20, 20);
//	}
//	public void cronometro(){
//		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
//			public void run() {
//				for(Gerador ger : tempo.keySet()){
//					int delay = getDelay(ger);
//					int atual = tempo.get(ger);
//					if(atual <= 0){
//						gerar(ger);
//						tempo.put(ger, delay);
//					}else{
//						atual--;
//						tempo.put(ger, atual);
//					}
//				}
//				plugin.core.atualizarContador();
//			}
//		}, 20L, 20L);
//	}
	
	void gerar(Gerador ger){
		Location loc = ger.getLoc().clone();
		Location alvo = ger.getLoc().clone();
		Block b = loc.getBlock();
		if(!b.getType().equals(Material.DISPENSER)) return;
		MaterialData d = b.getState().getData();
		Dispenser disp = (Dispenser) d;
		BlockFace face = disp.getFacing();
		if(face.equals(BlockFace.NORTH)) alvo.add(0, 0, -1);
		else if(face.equals(BlockFace.SOUTH)) alvo.add(0, 0, 1);
		else if(face.equals(BlockFace.EAST)) alvo.add(1, 0, 0);
		else if(face.equals(BlockFace.WEST)) alvo.add(-1, 0, 0);
		else if(face.equals(BlockFace.UP)) alvo.add(0, 1, 0);
		else if(face.equals(BlockFace.DOWN)) alvo.add(0, -1, 0);
		if(alvo.getBlock().getType().equals(Material.AIR)){
			if(ger.getTipo().equals(Tipo.PEDRA)){
				alvo.getBlock().setType(Material.STONE);
				alvo.getWorld().playSound(alvo, Sound.STEP_STONE, 1, 1);
			}else if(ger.getTipo().equals(Tipo.MADEIRA)){
				alvo.getBlock().setType(Material.LOG);
				alvo.getWorld().playSound(alvo, Sound.STEP_WOOD, 1, 1);
			}else if(ger.getTipo().equals(Tipo.DIAMANTE)){
				alvo.getBlock().setType(Material.DIAMOND_ORE);
				alvo.getWorld().playSound(alvo, Sound.STEP_STONE, 1, 1);
			}
		}
	}
	
	int getDelay(Gerador ger){
		Tipo tipo = ger.getTipo();
		int nivel = ger.getNivel();
		if(tipo.equals(Tipo.PEDRA)){
			if(nivel == 1) return 5;
			if(nivel == 2) return 3;
			if(nivel == 3) return 1;
		}else if(tipo.equals(Tipo.MADEIRA)){
			if(nivel == 1) return 120;
			if(nivel == 2) return 90;
			if(nivel == 3) return 30;
		}else if(tipo.equals(Tipo.DIAMANTE)){
			if(nivel == 1) return 7200;
			if(nivel == 2) return 2700;
			if(nivel == 3) return 1800;
		}
		return 999;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
