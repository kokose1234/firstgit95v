package tools.packet;

import client.MapleCharacter;
import client.MapleClient;
import constants.ServerConstants;
import java.net.InetAddress;
import java.util.Iterator;
import java.util.List;
import net.SendOpcode;
import net.SendOpcodes;
import net.server.channel.Channel;
import tools.Pair;
import tools.data.output.MaplePacketLittleEndianWriter;

/**
 *
 * @author 몽키프
 */
public class CLogin {

    public static final byte[] getHello(short mapleVersion, byte[] sendIv, byte[] recvIv) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        byte kms = 1;
        int ret = 0;
        ret ^= (mapleVersion & 0x7FFF);
        ret ^= (1 << 15);
        ret ^= ((1 & 0xFF) << 16); //마이너버전
        String version = String.valueOf(ret);
        int packetsize = 13 + version.length();
        short checkclient = 291;
        mplew.writeShort(packetsize);
        mplew.writeShort(checkclient);
        mplew.writeMapleAsciiString(version);
        mplew.write(recvIv);
        mplew.write(sendIv);
        mplew.write(kms);
        return mplew.getPacket();
    }

    public static byte[] CheckPasswordResult(MapleClient c, byte loginbyte) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendOpcodes.CheckPasswordResult.getValue());
        mplew.write(loginbyte);
        if (loginbyte == 2) {
            mplew.write(1);
            mplew.writeLong(0);
            return mplew.getPacket();
        } else if (loginbyte != 0 && loginbyte != 2 && loginbyte != 12) {
            return mplew.getPacket();
        }
        mplew.writeInt(c.getAccID());
        mplew.write(c.getGender());
        mplew.write(0);
        short toWrite = (short) (c.gmLevel() * 32);
        //toWrite = toWrite |= 0x100; only in higher versions
        mplew.write(toWrite > 0x80 ? 0x80 : toWrite);//0x80 is admin, 0x20 and 0x40 = subgm
        mplew.writeMapleAsciiString(c.getAccountName());
        mplew.writeInt(1234567);
        mplew.write(1);
        mplew.write(0);
        mplew.write(0);
        mplew.writeLong(0);
        mplew.writeMapleAsciiString("");
        return mplew.getPacket();
    }

    public static byte[] AccountInfoResult(MapleClient c, byte loginbyte) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendOpcodes.AccountInfoResult.getValue());
        mplew.write(loginbyte); //�α��ΰ��
        mplew.writeInt(c.getAccID());
        mplew.write(c.getGender());
        mplew.writeBool(c.gmLevel() > 0); //admin byte
        short toWrite = (short) (c.gmLevel() * 32);
        //toWrite = toWrite |= 0x100; only in higher versions
        mplew.write(toWrite > 0x80 ? 0x80 : toWrite);//0x80 is admin, 0x20 and 0x40 = subgm
        mplew.writeMapleAsciiString(c.getAccountName());
        mplew.writeInt(1234567);
        mplew.write(1);
        mplew.write(0);
        mplew.write(0);
        mplew.writeLong(0);
        mplew.writeMapleAsciiString("");
        mplew.writeMapleAsciiString("");
        return mplew.getPacket();
    }

    public static byte[] WorldInfomation(int serverId, String serverName, int flag, String eventmsg, List<Channel> channelLoad) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendOpcodes.WorldInfomation.getValue());
        mplew.write(serverId);
        mplew.writeMapleAsciiString(serverName);
        mplew.write(flag);
        mplew.writeMapleAsciiString(eventmsg);
        mplew.writeShort(100);
        mplew.writeShort(100);
        mplew.write(channelLoad.size());
        for (Channel ch : channelLoad) {
            mplew.writeMapleAsciiString(serverName + "-" + ch.getId());
            mplew.writeInt((ch.getConnectedClients() * 1200) / ServerConstants.CHANNEL_LOAD);
            mplew.write(1);
            mplew.writeShort(ch.getId() - 1);
        }
        mplew.writeShort(0);
        return mplew.getPacket();
    }

    public static byte[] EndOfWorldInfomation() {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(3);
        mplew.writeShort(SendOpcodes.WorldInfomation.getValue());
        mplew.write(0xFF);
        return mplew.getPacket();
    }

    public static byte[] SelectWorldResult(MapleClient c, int serverId) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendOpcodes.SelectWorldResult.getValue());
        mplew.write(0); //(BAN ? 2 : 0)
        mplew.writeInt(1234567); //�ֹε�Ϲ�ȣ
        List<MapleCharacter> chars = c.loadCharacters(serverId);
        mplew.write((byte) chars.size());
        for (MapleCharacter chr : chars) {
            PacketHelper.addCharEntry(mplew, chr, false);
        }
        mplew.write(2);
        mplew.write(0);
        mplew.writeInt(c.getCharacterSlots());
        return mplew.getPacket();
    }

    public static byte[] SelectCharacterResult(InetAddress inetAddr, int port, int clientId) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendOpcodes.SelectCharacterResult.getValue());
        mplew.write(0);
        mplew.write(0);
        byte[] addr = inetAddr.getAddress();
        mplew.write(addr);
        mplew.writeShort(port);
        mplew.writeInt(clientId);
        mplew.write(0);
        mplew.writeInt(0);
        return mplew.getPacket();
    }

    public static byte[] CheckDuplicatedIDResult(String charname, boolean nameUsed) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendOpcodes.CheckDuplicatedIDResult.getValue());
        mplew.writeMapleAsciiString(charname);
        mplew.write(nameUsed ? 1 : 0);
        return mplew.getPacket();
    }

    public static byte[] CreateNewCharacterResult(MapleCharacter chr) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendOpcodes.CreateNewCharacterResult.getValue());
        mplew.write(0);
        PacketHelper.addCharEntry(mplew, chr, false);
        return mplew.getPacket();
    }

    public static byte[] DeleteCharacterResult(int cid, int state) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendOpcodes.DeleteCharacterResult.getValue());
        mplew.writeInt(cid);
        mplew.write(state);
        return mplew.getPacket();
    }

    public static byte[] LatestConnectedWorld(int world) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendOpcodes.LatestConnectedWorld.getValue());
        mplew.writeInt(world);
        return mplew.getPacket();
    }

    public static byte[] RecommendWorldMessage(List<Pair<Integer, String>> worlds) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendOpcodes.RecommendWorldMessage.getValue());
        mplew.write(worlds.size());//size
        for (Iterator<Pair<Integer, String>> it = worlds.iterator(); it.hasNext();) {
            Pair<Integer, String> world = it.next();
            mplew.writeInt(world.getLeft());
            mplew.writeMapleAsciiString(world.getRight());
        }
        return mplew.getPacket();
    }

    public static byte[] CheckSPWOnCreateNewCharacterResult() {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(3);
        mplew.writeShort(SendOpcodes.CheckSPWOnCreateNewCharacterResult.getValue());
        mplew.write(0);
        return mplew.getPacket();
    }
}
