package me.soldado.geradores;

import org.bukkit.Location;

public class Gerador {

	Location loc;
	Tipo tipo;
	int nivel;
	
	public Gerador(Location loc, Tipo tipo, int nivel){
		this.loc = loc;
		this.tipo = tipo;
		this.nivel = nivel;
	}

	public Location getLoc() {
		return loc;
	}

	public void setLoc(Location loc) {
		this.loc = loc;
	}

	public Tipo getTipo() {
		return tipo;
	}

	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}

	public int getNivel() {
		return nivel;
	}

	public void setNivel(int nivel) {
		this.nivel = nivel;
	}
	
	public String getString(){
		String val = "";
		val = getTipo().toString().toLowerCase() + ";";
		val += getNivel() + ";";
		Location loc = getLoc();
		val += loc.getWorld().getName()+":"+loc.getBlockX()+":"+loc.getBlockY()+":"+loc.getBlockZ();
		return val;
	}
}
