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
import client.MapleClient;
import java.util.Arrays;
import net.AbstractMaplePacketHandler;
import server.events.gm.MapleSnowball;
import server.life.MapleMonster;
import server.maps.MapleMap;
import server.maps.MapleMapObjectType;
import tools.MaplePacketCreator;
import tools.data.input.SeekableLittleEndianAccessor;

/**
 *
 * @author kevintjuh93
 */
public final class ShowBombHandler extends AbstractMaplePacketHandler {
    
    public void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        //B1 00 80 00 00 00 
        //44 FF FF FF
        //12 01 00 00
        final MapleMap map = c.getPlayer().getMap();
        final MapleMonster mobfrom = map.getMonsterByOid(slea.readInt());
        final int read = slea.readInt();
        final int read2 = slea.readInt();
        boolean dmg = false;
        map.broadcastMessage(MaplePacketCreator.showbomb(mobfrom.getObjectId()));
        map.broadcastMessage(MaplePacketCreator.showbomb2(mobfrom.getObjectId(), c.getPlayer().getId()));
        if (!map.getMapObjectsInRange(mobfrom.getPosition(), 28000, Arrays.asList(MapleMapObjectType.PLAYER)).isEmpty()) {  
            dmg = true;
        }
        map.broadcastMessage(MaplePacketCreator.getTimeBombAttack(mobfrom, dmg));
    }
}
