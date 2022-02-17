package net.minecraft.src.MEDMEX.Modules.Player;

import java.util.List;

import org.lwjgl.input.Keyboard;

import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.Packet10Flying;
import net.minecraft.src.Packet28EntityVelocity;
import net.minecraft.src.Packet3Chat;
import net.minecraft.src.Packet70Bed;
import net.minecraft.src.MEDMEX.Client;
import net.minecraft.src.MEDMEX.Event.Event;
import net.minecraft.src.MEDMEX.Event.EventPacket;
import net.minecraft.src.MEDMEX.Event.listeners.EventUpdate;
import net.minecraft.src.MEDMEX.Modules.Module;
import net.minecraft.src.de.Hero.settings.Setting;


public class SecretChat extends Module{
	public static SecretChat instance;
	  String keys; 
	  public String signalCode;
	  public String spaceCode;
	  public String junk;
	
	public SecretChat() {
		super("SecretChat", Keyboard.KEY_NONE, Category.PLAYER);
	    this.keys = "";
	    this.signalCode = "cd:";
	    this.spaceCode = ":spd:";
	    this.junk = "fgfe";
		instance = this;
	}
	
	public void setup() {
		Client.settingsmanager.rSetting(new Setting("Frequency", this, 87, 10, 1000, true));
	}
	
	public int getValueFromFrequency(int i) {
	    char[] a = (new StringBuilder(String.valueOf(Client.settingsmanager.getSettingByName("Frequency").getValDouble()))).toString().toCharArray();
	    int v = Integer.parseInt((new StringBuilder(String.valueOf(a[i]))).toString());
	    return v;
	  }
	
	String toDeobf = "";
	boolean startListening = false;
	boolean stopListening = false;
	
	public void getPacket(EventPacket e) {
		if(this.isEnabled()) {
			if(mc.thePlayer != null && mc.theWorld != null) {
				if(e.getPacket() instanceof Packet3Chat) {
					Packet3Chat p = (Packet3Chat)e.getPacket();
					String m = p.message;
				    generateCodes();
				    if (m.startsWith("<") && m.contains(this.signalCode)) {
				    	startListening = true;
				    }
				    if(startListening) {
				    	toDeobf = toDeobf + m;
				    }
				    if(m.endsWith("$$")) {
				    	stopListening = true;
				    }
				    if(stopListening) {
				    	System.out.println(toDeobf);
				    toDeobf = toDeobf.replace("$$", "");
				    String start = toDeobf.substring(0, toDeobf.indexOf(this.signalCode));
				    String end = toDeobf.substring(toDeobf.indexOf(this.signalCode) + this.signalCode.length());
				    String res = Shift(end.replace(" ", "").replace(this.junk, ""), -getValueFromFrequency(0), -getValueFromFrequency(1));
				    Client.addChatMessage(("§9[SC]§r" + start + res.replace(this.spaceCode, " ") + " §6| §c" + toDeobf.substring(toDeobf.indexOf(this.signalCode))));
				    toDeobf = "";
				    startListening = false;
				    stopListening = false;
				    }
				}
			}
				
			}
				
			}
	
	public void generateCodes() {
	    this.signalCode = String.valueOf(generateString(getValueFromFrequency(1) * 0.5F, 4)) + generateString(getValueFromFrequency(0) * 0.6F, 3);
	    this.spaceCode = generateString(getValueFromFrequency(1) * 0.3F, 4);
	    this.junk = generateString(getValueFromFrequency(0) * 0.7F, 4);
	    String s = "mbvwxfghijcyaopqrskdentulz";
	    this.keys = "";
	    while (s.length() != 0) {
	      int i = Math.min((int)(s.length() / getValueFromFrequency(0)), s.length() - 1);
	      this.keys = String.valueOf(this.keys) + s.charAt(i);
	      s = s.replace((new StringBuilder(String.valueOf(s.charAt(i)))).toString(), "");
	    } 
	  }
	
	String generateString(float seed, int l) {
	    StringBuilder result = new StringBuilder();
	    for (int i = 0; i < l; i++) {
	      char newCharacter = (char)(99 + (int)(i * seed * 0.8F));
	      result.append(newCharacter);
	    } 
	    return result.toString();
	  }
	
	public String onMessage(String t) {
	    if (!this.isEnabled())
	      return t; 
	    String m = t;
	    m = m.replace("$$", "");
	    generateCodes();
	    if (!m.contains(this.signalCode))
	      return t; 
	    String start = m.substring(0, m.indexOf(this.signalCode));
	    String end = m.substring(m.indexOf(this.signalCode) + this.signalCode.length());
	    String res = Shift(end.replace(" ", "").replace(this.junk, ""), -getValueFromFrequency(0), -getValueFromFrequency(1));
	    return ("§9[SC]§r" + start + res.replace(this.spaceCode, " ") + " §6| §c" + m.substring(m.indexOf(this.signalCode)));
	  }
	
	public String Obfuscate(String m) {
	    if (m.startsWith("/"))
	      return m; 
	    generateCodes();
	    m = String.valueOf(this.signalCode) + Shift(m.replace(" ", this.spaceCode), getValueFromFrequency(0), getValueFromFrequency(1));
	    return m;
	  }
	
	public String Shift(String m, float shiftStart, float shiftJump) {
	    double offset = shiftStart;
	    StringBuilder result = new StringBuilder();
	    byte b;
	    int i;
	    char[] arrayOfChar;
	    for (i = (arrayOfChar = m.toCharArray()).length, b = 0; b < i; ) {
	      char character = arrayOfChar[b];
	      if (this.keys.contains(String.valueOf(character))) {
	        int ind = this.keys.indexOf(character);
	        ind = (int)(ind + offset);
	        while (ind < 0 || ind >= this.keys.length()) {
	          if (ind < 0)
	            ind += this.keys.length(); 
	          if (ind >= this.keys.length())
	            ind -= this.keys.length(); 
	        } 
	        result.append(this.keys.charAt(ind));
	        offset += shiftJump;
	        if (shiftStart > 0.0F && Math.random() * 10.0D > 8.0D)
	          result.append(" "); 
	        if (shiftStart > 0.0F && Math.random() * 10.0D > 9.0D)
	          result.append(this.junk); 
	      } 
	      b++;
	    } 
	    return result.toString();
	  }
	
	public static SecretChat getInstance() {
	    return instance;
	  }

}
