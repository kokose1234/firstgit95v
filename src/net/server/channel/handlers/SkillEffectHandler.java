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

import client.MapleClient;
import constants.skills.Bishop;
import constants.skills.Bowmaster;
import constants.skills.Captain;
import constants.skills.ChiefMaster;
import constants.skills.DarkKnight;
import constants.skills.FPArchMage;
import constants.skills.FPMage;
import constants.skills.Gunslinger;
import constants.skills.Hero;
import constants.skills.ILArchMage;
import constants.skills.Infighter;
import constants.skills.Marksman;
import constants.skills.NightWalker;
import constants.skills.Paladin;
import constants.skills.Striker;
import constants.skills.WindBreaker;
import net.AbstractMaplePacketHandler;
import tools.MaplePacketCreator;
import tools.data.input.SeekableLittleEndianAccessor;

public final class SkillEffectHandler extends AbstractMaplePacketHandler {
    public final void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        int skillId = slea.readInt();
        int level = slea.readByte();
        byte flags = slea.readByte();
        int speed = slea.readByte();
        byte aids = slea.readByte();//Mmmk
        switch (skillId) {
            case FPMage.EXPLOSION:
            case FPArchMage.BIG_BANG:
            case ILArchMage.BIG_BANG:
            case Bishop.BIG_BANG:
            case Bowmaster.HURRICANE:
            case Marksman.PIERCING_ARROW:
            case ChiefMaster.CHAKRA:
            case Infighter.CORKSCREW_BLOW:
            case Gunslinger.GRENADE:
            case Captain.RAPID_FIRE:
            case WindBreaker.HURRICANE:
            case NightWalker.POISON_BOMB:
            case Striker.CORKSCREW_BLOW:
            case Paladin.MONSTER_MAGNET:
            case DarkKnight.MONSTER_MAGNET:
            case Hero.MONSTER_MAGNET:
            case 4341003:
                c.getPlayer().getMap().broadcastMessage(c.getPlayer(), MaplePacketCreator.skillEffect(c.getPlayer(), skillId, level, flags, speed, aids), false);
                return;
            default:
                System.out.println(c.getPlayer() + " entered SkillEffectHandler without being handled using " + skillId + ".");
                return;
        }
    }
}