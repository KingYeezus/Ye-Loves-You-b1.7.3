package net.minecraft.src.MEDMEX.Modules.Player;

import java.lang.reflect.Field;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.GuiConnecting;
import net.minecraft.src.GuiDownloadTerrain;
import net.minecraft.src.ItemFood;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Packet0KeepAlive;
import net.minecraft.src.Packet101CloseWindow;
import net.minecraft.src.Packet103SetSlot;
import net.minecraft.src.Packet10Flying;
import net.minecraft.src.Packet11PlayerPosition;
import net.minecraft.src.Packet12PlayerLook;
import net.minecraft.src.Packet13PlayerLookMove;
import net.minecraft.src.Packet15Place;
import net.minecraft.src.Packet16BlockItemSwitch;
import net.minecraft.src.Vec3D;
import net.minecraft.src.MEDMEX.Client;
import net.minecraft.src.MEDMEX.Event.Event;
import net.minecraft.src.MEDMEX.Event.EventPacket;
import net.minecraft.src.MEDMEX.Event.listeners.EventUpdate;
import net.minecraft.src.MEDMEX.Modules.Module;
import net.minecraft.src.MEDMEX.Utils.Timer;
import net.minecraft.src.de.Hero.settings.Setting;

public class AntiDesync extends Module{
	public static AntiDesync instance;
	public AntiDesync() {
		super("AntiDesync", Keyboard.KEY_NONE, Category.PLAYER);
		instance = this;
	}
}
