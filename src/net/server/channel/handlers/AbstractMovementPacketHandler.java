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

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import net.AbstractMaplePacketHandler;
import server.maps.AnimatedMapleMapObject;
import server.movement.AbsoluteLifeMovement;
import server.movement.BounceMovement;
import server.movement.ChairMovement;
import server.movement.ChangeEquip;
import server.movement.JumpDownMovement;
import server.movement.LifeMovement;
import server.movement.LifeMovementFragment;
import server.movement.RelativeLifeMovement;
import server.movement.TeleportMovement;
import tools.data.input.LittleEndianAccessor;

public abstract class AbstractMovementPacketHandler extends AbstractMaplePacketHandler {

    protected List<LifeMovementFragment> parseMovement(LittleEndianAccessor lea) {
        List<LifeMovementFragment> res = new ArrayList<>();
        byte numCommands = lea.readByte();
        for (byte i = 0; i < numCommands; i++) {
            byte command = lea.readByte();
            switch (command) {
                case 0:
                case 5:
                case 15:
                case 17:
                case 30:
                case 31: {
                    final short xpos = lea.readShort();
                    final short ypos = lea.readShort();
                    final short xwobble = lea.readShort();
                    final short ywobble = lea.readShort();
                    final short unk = lea.readShort();
                    short fh = 0, xoffset = 0, yoffset = 0;
                    if (command == 15) {
                        fh = lea.readShort();
                    }
                    xoffset = lea.readShort();
                    yoffset = lea.readShort();
                    final byte newstate = lea.readByte();
                    final short duration = lea.readShort();
                    final AbsoluteLifeMovement alm = new AbsoluteLifeMovement(command, new Point(xpos, ypos), duration, newstate);
                    alm.setUnk(unk);
                    alm.setFh(fh);
                    alm.setPixelsPerSecond(new Point(xwobble, ywobble));
                    alm.setOffset(new Point(xoffset, yoffset));
                    res.add(alm);
                    break;
                }
                case 1:
                case 2:
                case 6:
                case 12:
                case 13:
                case 16:
                case 18:
                case 19:
                case 20:
                case 22:
                case 24:
                case 26:
                case 27:
                case 28:
                case 29: { // Float
                    short xpos = lea.readShort();
                    short ypos = lea.readShort();
                    byte newstate = lea.readByte();
                    short duration = lea.readShort();
                    RelativeLifeMovement rlm = new RelativeLifeMovement(command, new Point(xpos, ypos), duration, newstate);
                    res.add(rlm);
                    break;
                }
                case 3:
                case 4:
                case 7:
                case 8:
                case 9:
                case 11: { // Before Jump Down - fixes item/mobs dissappears
                    final short xpos = lea.readShort();
                    final short ypos = lea.readShort();
                    final short fh = lea.readShort();
                    final byte newstate = lea.readByte();
                    final short duration = lea.readShort();
                    final TeleportMovement tm = new TeleportMovement(command, new Point(xpos, ypos), duration, newstate);
                    tm.setFh(fh);                    
                    res.add(tm);
                    break;
                }
                case 14: { // Chair
                    short xpos = lea.readShort();
                    short ypos = lea.readShort();
                    short unk = lea.readShort();
                    byte newstate = lea.readByte();
                    short duration = lea.readShort();
                    ChairMovement cm = new ChairMovement(command, new Point(xpos, ypos), duration, newstate);
                    cm.setUnk(unk);
                    res.add(cm);
                    break;
                }
                case 23: {
                    final short xpos = lea.readShort();
                    final short ypos = lea.readShort();
                    final short xoffset = lea.readShort();
                    final short yoffset = lea.readShort();
                    final byte newstate = lea.readByte();
                    final short duration = lea.readShort();
                    final BounceMovement bm = new BounceMovement(command, new Point(xpos, ypos), duration, newstate);
                    bm.setOffset(new Point(xoffset, yoffset));
                    res.add(bm);
                    break;
                }
                case 10: // Change Equip
                    res.add(new ChangeEquip(lea.readByte()));
                    break;
                default:
                    return null;
            }
        }
        return res;
    }

    protected void updatePosition(List<LifeMovementFragment> movement, AnimatedMapleMapObject target, int yoffset) {
        for (LifeMovementFragment move : movement) {
            if (move instanceof LifeMovement) {
                if (move instanceof AbsoluteLifeMovement) {
                    Point position = ((LifeMovement) move).getPosition();
                    position.y += yoffset;
                    target.setPosition(position);
                }
                target.setStance(((LifeMovement) move).getNewstate());
            }
        }
    }
}
