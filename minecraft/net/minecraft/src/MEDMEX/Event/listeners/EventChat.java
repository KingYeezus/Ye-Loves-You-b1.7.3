package net.minecraft.src.MEDMEX.Event.listeners;

import net.minecraft.src.MEDMEX.Event.Event;

public class EventChat extends Event<EventChat>{
	
	public String message;

	public EventChat(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	

}
