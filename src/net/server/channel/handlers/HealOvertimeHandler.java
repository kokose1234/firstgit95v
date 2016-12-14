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
import client.autoban.AutobanFactory;
import client.autoban.AutobanManager;
import net.AbstractMaplePacketHandler;
import tools.data.input.SeekableLittleEndianAccessor;

public final class HealOvertimeHandler extends AbstractMaplePacketHandler {

    public final void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        MapleCharacter chr = c.getPlayer();
        AutobanManager abm = chr.getAutobanManager();
        abm.setTimestamp(0, slea.readInt());
        slea.skip(4);
        short healHP = slea.readShort();
        if (healHP != 0) {
            chr.addHP(healHP);
            //chr.getMap().broadcastMessage(chr, MaplePacketCreator.showHpHealed(chr.getId(), healHP), false);
            chr.checkBerserk();
            abm.spam(0);
        }
        short healMP = slea.readShort();
        if (healMP != 0 && healMP < 1000) {
            chr.addMP(healMP);
            abm.spam(1);
        }
    }
}
