package me.soldado.geradores;

import org.bukkit.scheduler.BukkitRunnable;

public class Cronometro extends BukkitRunnable {

	public Main plugin;
	
	public Cronometro(Main plugin)
	{
		this.plugin = plugin;
	}
	
	@Override
	public void run() {
		for(Gerador ger : plugin.mec.tempo.keySet()){
			int delay = plugin.mec.getDelay(ger);
			int atual = plugin.mec.tempo.get(ger);
			if(atual <= 0){
				plugin.mec.gerar(ger);
				plugin.mec.tempo.put(ger, delay);
			}else{
				atual--;
				plugin.mec.tempo.put(ger, atual);
			}
		}
		plugin.core.atualizarContador();
	}

}
