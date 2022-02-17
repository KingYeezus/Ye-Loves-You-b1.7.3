package net.minecraft.src.MEDMEX.Modules.World;

import org.lwjgl.input.Keyboard;

import net.minecraft.src.Material;
import net.minecraft.src.Vec3D;
import net.minecraft.src.MEDMEX.Client;
import net.minecraft.src.MEDMEX.Event.Event;
import net.minecraft.src.MEDMEX.Event.listeners.EventUpdate;
import net.minecraft.src.MEDMEX.Modules.Module;
import net.minecraft.src.MEDMEX.Utils.InventoryUtils;
import net.minecraft.src.de.Hero.settings.Setting;

public class AutoTNT extends Module {
	public static AutoTNT instance;
	int offsetX = 0, offsetZ = 0;

	public AutoTNT() {
		super("AutoTNT", Keyboard.KEY_NONE, Category.WORLD);
		instance = this;
	}

	public void setup() {
		Client.settingsmanager.rSetting(new Setting("Place Frequency", this, 3, 0, 5, true));
	}

	public void onEvent(Event e) {
		if (e instanceof EventUpdate) {
			if (e.isPre()) {
				int slot = InventoryUtils.getHotbarslotItem(46);
				if (slot != -1)
					mc.thePlayer.inventory.currentItem = slot;

				if (mc.thePlayer.inventory.getCurrentItem() != null
						&& mc.thePlayer.inventory.getCurrentItem().itemID == 46) {
					offsets();
					for (int i = -3; i < 4; i++) {
						for (int j = -3; j < 4; j++) {
							for (int k = -3; k < 4; k++) {

								int pX = (int) mc.thePlayer.posX + offsetX;
								int pY = (int) mc.thePlayer.posY;
								int pZ = (int) mc.thePlayer.posZ + offsetZ;

								int x = pX
										- pX % (int) Client.settingsmanager.getSettingByName("Place Frequency")
												.getValDouble()
										- i * (int) Client.settingsmanager.getSettingByName("Place Frequency")
												.getValDouble();
								int y = pY + k;
								int z = pZ
										- pZ % (int) Client.settingsmanager.getSettingByName("Place Frequency")
												.getValDouble()
										- j * (int) Client.settingsmanager.getSettingByName("Place Frequency")
												.getValDouble();
								if (mc.thePlayer.getDistance(x, y, z) <= 4) {
									if (!mc.theWorld.getBlockMaterial(x, y, z).isSolid()
											&& !mc.theWorld.getBlockMaterial(x, y + 1, z).equals(Material.tnt)
											&& !mc.theWorld.getBlockMaterial(x, y - 1, z).equals(Material.tnt)) {
										int[] values = getPlace(new Vec3D(x, y, z));

										if (!(values[0] == 0 && values[1] == 0 && values[2] == 0 && values[3] == 0)) {
											mc.playerController.sendPlaceBlock(mc.thePlayer, mc.theWorld,
													mc.thePlayer.inventory.getCurrentItem(), values[0], values[1],
													values[2], values[3]);
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

	public void offsets() {
		if (mc.thePlayer.posX < 0 && mc.thePlayer.posZ < 0) {
			offsetX = -1;
			offsetZ = -1;
		}
		if (mc.thePlayer.posX > 0 && mc.thePlayer.posZ > 0) {
			offsetX = 0;
			offsetZ = 0;
		}
		if (mc.thePlayer.posX > 0 && mc.thePlayer.posZ < 0) {
			offsetX = 0;
			offsetZ = -1;
		}
		if (mc.thePlayer.posX < 0 && mc.thePlayer.posZ > 0) {
			offsetX = -1;
			offsetZ = 0;
		}
	}

	public int[] getPlace(Vec3D blockpos) {
		if (!mc.theWorld.isAirBlock((int) blockpos.xCoord, (int) blockpos.yCoord - 1, (int) blockpos.zCoord)) {
			int[] values = { (int) blockpos.xCoord + 0, (int) blockpos.yCoord + -1, (int) blockpos.zCoord + 0, 1 };
			return values;
		}
		if (!mc.theWorld.isAirBlock((int) blockpos.xCoord + 1, (int) blockpos.yCoord, (int) blockpos.zCoord)) {
			int[] values = { (int) blockpos.xCoord + 1, (int) blockpos.yCoord + 0, (int) blockpos.zCoord + 0, 4 };
			return values;
		}
		if (!mc.theWorld.isAirBlock((int) blockpos.xCoord - 1, (int) blockpos.yCoord, (int) blockpos.zCoord)) {
			int[] values = { (int) blockpos.xCoord - 1, (int) blockpos.yCoord + 0, (int) blockpos.zCoord + 0, 5 };
			return values;
		}
		if (!mc.theWorld.isAirBlock((int) blockpos.xCoord, (int) blockpos.yCoord, (int) blockpos.zCoord + 1)) {
			int[] values = { (int) blockpos.xCoord + 0, (int) blockpos.yCoord + 0, (int) blockpos.zCoord + 1, 2 };
			return values;
		}
		if (!mc.theWorld.isAirBlock((int) blockpos.xCoord, (int) blockpos.yCoord, (int) blockpos.zCoord - 1)) {
			int[] values = { (int) blockpos.xCoord + 0, (int) blockpos.yCoord + 0, (int) blockpos.zCoord - 1, 3 };
			return values;
		}
		if (!mc.theWorld.isAirBlock((int) blockpos.xCoord, (int) blockpos.yCoord - 1, (int) blockpos.zCoord)) {

			int[] values = { (int) blockpos.xCoord + 0, (int) blockpos.yCoord - 1, (int) blockpos.zCoord, 0 };
			return values;
		}

		int[] values = { 0, 0, 0, 0 };
		return values;

	}

}