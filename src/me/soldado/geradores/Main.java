package me.soldado.geradores;

import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	
	Core core;
	Comandos cmd;
	Config cfg;
	Mensagens msg;
	MecanicaGerador mec;
	Backup back;
	Cronometro crn;
	
	public void onEnable(){
		core = new Core(this);
		cmd = new Comandos(this);
		cfg = new Config(this);
		msg = new Mensagens(this);
		mec = new MecanicaGerador(this);
		back = new Backup(this);
		crn = new Cronometro(this);
		crn.runTaskTimer(this, 20L, 20L);

		core.registrarEventos();
		cmd.registrarComandos();
		
		cfg.iniciarConfig();
		msg.iniciarMensagens();
		back.iniciarBackup();

		this.getLogger().info("sGeradores ativado!!!");
		this.getLogger().info("Autor: Soldado_08");
		
		//mec.cronometro();
		back.carregar();
	}
	
	public void onDisable(){
		this.getLogger().info("sGeradores desativado!!!");
		this.getLogger().info("Autor: Soldado_08");
		back.salvar();
	}
}
