package de.pjog.prinzJuliano.asteroids.io;

import java.util.ArrayList;
import java.util.HashMap;

public class InputHandler {
	HashMap<Integer, ArrayList<InputListener>> listeners;
	
	public InputHandler(){
		listeners = new HashMap<Integer, ArrayList<InputListener>>();
	}
	
	public void register(int i, InputListener l)
	{
		if(!listeners.containsKey(i))
			listeners.put(i, new ArrayList<InputListener>());
		listeners.get(i).add(l);
	}
	
	public void handleEvent(char c, int code, boolean press)
	{
		if(listeners.containsKey(code))
		{
			for(int i = 0; i < this.listeners.get(code).size(); i++)
			{
				this.listeners.get(code).get(i).on(c, code, press);
			}
		}
	}
	
	public void reset(){
		listeners = new HashMap<Integer, ArrayList<InputListener>>();
	}
}
