package iDPS.gear;


import iDPS.gear.Gem.GemColor;
import iDPS.gear.Socket.SocketType;
import iDPS.model.Calculations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;


public class GemComparison {
	
	private Gear gear;
	private int slot, index;
	private float defaultDPS;
	private ArrayList<Gem> comparedGems;
	
	public GemComparison(Gear gear, int slot, int index) {
		this(gear, slot, index, true);
	}
	
	public GemComparison(Gear gear, int slot, int index, boolean anyColor) {
		this.gear = gear.clone();
		this.slot = slot;
		this.index = index;
		this.gear.setGem(slot, index, null);
		comparedGems = new ArrayList<Gem>();
		runComparison(anyColor);
	}
	
	private void runComparison(boolean anyColor) {
		Calculations m = Calculations.createInstance();
		m.calculate(gear);
		defaultDPS = m.getTotalDPS();
		
		Collection<Gem> gems;
		Socket socket = gear.getItem(slot).getSocket(index);
		if (anyColor && gear.getItem(slot).getSocket(index).getType() != SocketType.Meta)
			gems = Gem.getAll();
		else
			gems = Gem.findSocket(socket);
		
		for (Gem gem: gems)  {
			if (gem.getColor() == GemColor.Meta && socket.getType() != SocketType.Meta)
				continue;
			if (!gear.canAdd(gem))
				continue;
			gear.setGem(slot, index, gem);
			m.calculate(gear);
			gem.setComparedDPS(m.getTotalDPS()-defaultDPS);
			comparedGems.add(gem);
		}
		Collections.sort(comparedGems);
	}
	
	public ArrayList<Gem> getComparedGems() {
		return comparedGems;
	}
	
	public Gem getBestGem() {
		if (comparedGems.size()>0)
			return comparedGems.get(0);
		return null;
	}
	
	public float getDefaultDPS() {
		return defaultDPS;
	}
	
	public float getMaxDPS() {
		if (comparedGems.size()>0)
			return comparedGems.get(0).getComparedDPS();
		return 0;
	}

}
