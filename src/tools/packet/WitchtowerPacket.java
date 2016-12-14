package tools.packet;

import net.SendOpcodes;
import tools.data.output.MaplePacketLittleEndianWriter;

/**
 *
 * @author 몽키프
 */
public class WitchtowerPacket {
    public static byte[] WitchtowerScoreUpdate(int type) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendOpcodes.WitchtowerScoreUpdate.getValue());
        mplew.write(type);
        return mplew.getPacket();
    }
}
