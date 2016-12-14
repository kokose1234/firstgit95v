/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.packet;

import client.MapleCharacter;
import client.inventory.Item;
import client.inventory.MapleInventory;
import client.inventory.MapleInventoryType;
import constants.GameConstants;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import server.MapleItemInformationProvider;
import tools.data.output.MaplePacketLittleEndianWriter;

/**
 *
 * @author monke
 */
public class PacketHelper {

    public static void addCharEntry(final MaplePacketLittleEndianWriter mplew, MapleCharacter chr, boolean viewall) {
        addCharStats(mplew, chr);
        addCharLook(mplew, chr, false);
        mplew.write(0);
        mplew.write(1); // world rank enabled (next 4 ints are not sent if disabled) Short??
        mplew.writeInt(chr.getRank()); // world rank
        mplew.writeInt(chr.getRankMove()); // move (negative is downwards)
        mplew.writeInt(chr.getJobRank()); // job rank
        mplew.writeInt(chr.getJobRankMove()); // move (negative is downwards)
    }

    public static void addCharStats(final MaplePacketLittleEndianWriter mplew, MapleCharacter chr) {
        mplew.writeInt(chr.getId()); // character id
        mplew.writeAsciiString(chr.getName(), 13);
        mplew.write(chr.getGender()); // gender (0 = male, 1 = female)
        mplew.write(chr.getSkinColor().getId()); // skin color
        mplew.writeInt(chr.getFace()); // face
        mplew.writeInt(chr.getHair()); // hair
        mplew.writeLong(0);
        mplew.write(chr.getLevel()); // level
        mplew.writeShort(chr.getJob().getId()); // job
        mplew.writeShort(chr.getStr()); // str
        mplew.writeShort(chr.getDex()); // dex
        mplew.writeShort(chr.getInt()); // int
        mplew.writeShort(chr.getLuk()); // luk
        mplew.writeShort(chr.getHp()); // hp (?)
        mplew.writeShort(chr.getMaxHp()); // maxhp
        mplew.writeShort(chr.getMp()); // mp (?)
        mplew.writeShort(chr.getMaxMp()); // maxmp
        mplew.writeShort(chr.getRemainingAp()); // remaining ap
        if (GameConstants.isEvan(chr.getJob().getId())) {
            //chr.getExtendedSP().Encode(mplew);
            mplew.write(0);
        } else {
            mplew.writeShort(chr.getRemainingSp()); // remaining sp
        }
        mplew.writeInt(chr.getExp()); // current exp
        mplew.writeShort(chr.getFame()); // fame
        mplew.writeInt(chr.getMapId()); // current map id
        mplew.write(chr.getInitialSpawnpoint()); // spawnpoint
        mplew.writeShort(0);
    }

    public static void addCharLook(final MaplePacketLittleEndianWriter mplew, MapleCharacter chr, boolean mega) {
        mplew.write(chr.getGender());
        mplew.write(chr.getSkinColor().getId()); // skin color
        mplew.writeInt(chr.getFace()); // face
        mplew.write(mega ? 0 : 1);
        mplew.writeInt(chr.getHair()); // hair
        addCharEquips(mplew, chr);
    }

    public static void addCharEquips(final MaplePacketLittleEndianWriter mplew, MapleCharacter chr) {
        MapleInventory equip = chr.getInventory(MapleInventoryType.EQUIPPED);
        Collection<Item> ii = MapleItemInformationProvider.getInstance().canWearEquipment(chr, equip.list());
        Map<Byte, Integer> myEquip = new LinkedHashMap<>();
        Map<Byte, Integer> maskedEquip = new LinkedHashMap<>();
        for (Item item : ii) {
            byte pos = (byte) (item.getPosition() * -1);
            if (pos < 100 && myEquip.get(pos) == null) {
                myEquip.put(pos, item.getItemId());
            } else if (pos > 100 && pos != 111) { // don't ask. o.o
                pos -= 100;
                if (myEquip.get(pos) != null) {
                    maskedEquip.put(pos, myEquip.get(pos));
                }
                myEquip.put(pos, item.getItemId());
            } else if (myEquip.get(pos) != null) {
                maskedEquip.put(pos, item.getItemId());
            }
        }
        for (Map.Entry<Byte, Integer> entry : myEquip.entrySet()) {
            mplew.write(entry.getKey());
            mplew.writeInt(entry.getValue());
        }
        mplew.write(0xFF);
        for (Map.Entry<Byte, Integer> entry : maskedEquip.entrySet()) {
            mplew.write(entry.getKey());
            mplew.writeInt(entry.getValue());
        }
        mplew.write(0xFF);
        Item cWeapon = equip.getItem((byte) -111);
        mplew.writeInt(cWeapon != null ? cWeapon.getItemId() : 0);
        mplew.writeInt(0);
    }

}
