package net.minecraft.src.MEDMEX.Commands.impl;

import org.lwjgl.input.Keyboard;

import net.minecraft.src.Packet11PlayerPosition;
import net.minecraft.src.Packet13PlayerLookMove;
import net.minecraft.src.Packet15Place;
import net.minecraft.src.Packet19EntityAction;
import net.minecraft.src.Packet255KickDisconnect;
import net.minecraft.src.Packet7UseEntity;
import net.minecraft.src.Packet9Respawn;
import net.minecraft.src.MEDMEX.Client;
import net.minecraft.src.MEDMEX.Commands.Command;
import net.minecraft.src.MEDMEX.Modules.Module;

public class SpawnTP extends Command {

	public SpawnTP() {
		super("SpawnTP", "SpawnTp", "SpawnTP", "stp");

	}

	@Override
	public void onCommand(String[] args, String command) {
		try {
			Client.sendPacket(new Packet11PlayerPosition(Double.NaN, Double.NaN, Double.NaN, Double.NaN, true));
		} catch (Exception e) {

		}

	}

}
