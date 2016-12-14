package tools.packet;

import client.Dragon;
import java.awt.Point;
import java.util.List;
import net.SendOpcodes;
import server.movement.LifeMovementFragment;
import tools.data.output.LittleEndianWriter;
import tools.data.output.MaplePacketLittleEndianWriter;

/**
 *
 * @author 몽키프
 */
public class DragonPacket {

    private static void serializeMovementList(LittleEndianWriter lew, List<LifeMovementFragment> moves) {
        lew.write(moves.size());
        for (LifeMovementFragment move : moves) {
            move.serialize(lew);
        }
    }

    public static byte[] OnDragonEnterField(Dragon pDragon) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendOpcodes.DragonCreate.getValue());
        mplew.writeInt(pDragon.getOwner());
        mplew.writeInt(pDragon.getPosition().x);
        mplew.writeInt(pDragon.getPosition().y);
        mplew.write(pDragon.getStance());
        mplew.writeShort(/*pDragon.m_nFootholdSN*/0);
        mplew.writeShort(pDragon.getJobId());
        return mplew.getPacket();
    }

    public static byte[] OnDragonLeaveField(Dragon pDragon) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendOpcodes.DragonDelete.getValue());
        mplew.writeInt(pDragon.getOwner());
        return mplew.getPacket();
    }

    public static byte[] OnDragonMove(int cid, List<LifeMovementFragment> moves, Point startPos) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendOpcodes.DragonMove.getValue());
        mplew.writeInt(cid);
        mplew.writePos(startPos);//StartPos.x, StartPos.y
        serializeMovementList(mplew, moves);
        return mplew.getPacket();
    }
}
