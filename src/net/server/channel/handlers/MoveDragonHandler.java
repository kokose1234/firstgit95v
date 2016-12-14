package net.server.channel.handlers;

import client.MapleCharacter;
import client.MapleClient;
import java.awt.Point;
import java.util.List;
import client.Dragon;
import server.movement.LifeMovementFragment;
import tools.data.input.SeekableLittleEndianAccessor;
import tools.packet.DragonPacket;

public class MoveDragonHandler extends AbstractMovementPacketHandler {

    @Override
    public void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        final MapleCharacter chr = c.getPlayer();
        final Point startPos = new Point(slea.readShort(), slea.readShort());
        List<LifeMovementFragment> res = parseMovement(slea);
        Dragon dragon = chr.getDragon();
        if (dragon != null && res != null && res.size() > 0) {
            if (chr.isHidden()) {
                chr.getMap().broadcastGMMessage(chr, DragonPacket.OnDragonMove(chr.getId(), res, startPos));
            } else {
                chr.getMap().broadcastMessage(chr, DragonPacket.OnDragonMove(chr.getId(), res, startPos), dragon.getPosition());
            }
            updatePosition(res, dragon, 0);
        }
    }
}
