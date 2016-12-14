/*
	This file is part of the OdinMS Maple Story Server
    Copyright (C) 2008 Patrick Huy <patrick.huy@frz.cc>
		       Matthias Butz <matze@odinms.de>
		       Jan Christian Meyer <vimes@odinms.de>

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation version 3 as published by
    the Free Software Foundation. You may not use, modify or distribute
    this program under any other version of the GNU Affero General Public
    License.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.server.channel.handlers;

import client.MapleCharacter;
import java.awt.Point;
import java.io.File;
import java.sql.PreparedStatement;
import client.MapleClient;
import client.inventory.MapleInventoryType;
import client.inventory.MaplePet;
import client.inventory.PetDataFactory;
import client.SkillFactory;
import java.sql.SQLException;
import tools.DatabaseConnection;
import net.AbstractMaplePacketHandler;
import provider.MapleDataProvider;
import provider.MapleDataProviderFactory;
import provider.MapleDataTool;
import server.MapleInventoryManipulator;
import tools.MaplePacketCreator;
import tools.data.input.SeekableLittleEndianAccessor;

public final class SpawnPetHandler extends AbstractMaplePacketHandler {

    private static MapleDataProvider dataRoot = MapleDataProviderFactory.getDataProvider(new File(System.getProperty("wzpath") + "/Item.wz"));

    public final void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        MapleCharacter chr = c.getPlayer();
        slea.readInt();
        byte slot = slea.readByte();
        slea.readByte();
        MaplePet pet = chr.getInventory(MapleInventoryType.CASH).getItem(slot).getPet();
        if (pet == null) {
            return;
        }
        if (chr.getPetIndex(pet) != -1) {
            chr.unequipPet(pet, true);
        } else {
            if (chr.getSkillLevel(SkillFactory.getSkill(8)) == 0 && chr.getPet(0) != null) {
                chr.unequipPet(chr.getPet(0), false);
            }
            Point pos = chr.getPosition();
            pos.y -= 12;
            pet.setPos(pos);
            pet.setFh(chr.getMap().getFootholds().findBelow(pet.getPos()).getId());
            pet.setStance(0);
            pet.setSummoned(true);
            pet.saveToDb();
            chr.addPet(pet);
            c.announce(MaplePacketCreator.updatePet(pet, c.getPlayer().getInventory(MapleInventoryType.CASH).getItem(slot), slot, true));
            chr.getMap().broadcastMessage(c.getPlayer(), MaplePacketCreator.showPet(c.getPlayer(), pet, false, false), true);
            //c.announce(MaplePacketCreator.petStatUpdate(c.getPlayer()));
            c.announce(MaplePacketCreator.enableActions());
            chr.startFullnessSchedule(PetDataFactory.getHunger(pet.getItemId()), pet, chr.getPetIndex(pet));
        }
    }
}
