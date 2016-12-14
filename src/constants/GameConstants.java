package constants;

import client.MapleCharacter;
import client.inventory.Item;
import client.inventory.MapleInventoryType;
import constants.skills.Aran;
import constants.skills.Beginner;
import constants.skills.Evan;
import constants.skills.Legend;
import constants.skills.Noblesse;

/**
 *
 * @author 몽키프
 */
public class GameConstants {

    public static int getHiddenSkill(final int skill) {
        switch (skill) {
            case Aran.HIDDEN_FULL_SWING_DOUBLE_SWING:
            case Aran.HIDDEN_FULL_SWING_TRIPLE_SWING:
                return Aran.FULL_SWING;
            case Aran.HIDDEN_OVER_SWING_DOUBLE_SWING:
            case Aran.HIDDEN_OVER_SWING_TRIPLE_SWING:
                return Aran.OVER_SWING;
        }
        return skill;
    }

    public static boolean isAran(final int job) {
        return job == 2000 || (job >= 2100 && job <= 2112);
    }

    public static boolean isEvan(final int job) {
        return job == 2200 || (job >= 2210 && job <= 2218);
    }

    public static int getMountId(MapleCharacter chr, final int sourceid) {
        switch (sourceid) {
            case Beginner.MONSTER_RIDER:
            case Noblesse.MONSTER_RIDER:
            case Legend.MONSTER_RIDER:
            case Evan.MONSTER_RIDER:
                Item mount = chr.getInventory(MapleInventoryType.EQUIPPED).getItem((byte) -18);
                if (mount != null) {
                    return chr.getInventory(MapleInventoryType.EQUIPPED).getItem((byte) -18).getItemId();
                }
            case Beginner.SPACESHIP:
            case Noblesse.SPACESHIP:
                return 1932000;
            case Beginner.YETI_RIDER:
            case Noblesse.YETI_RIDER:
            case Legend.YETI_RIDER:
            case Evan.YETI_RIDER:
                return 1932003;
            case Beginner.YETI_MOUNT:
            case Noblesse.YETI_MOUNT:
            case Legend.YETI_MOUNT:
                return 1932004;
            case Beginner.WITCH_S_BROOMSTICK:
            case Noblesse.WITCH_S_BROOMSTICK:
            case Legend.WITCH_S_BROOMSTICK:
            case Evan.WITCH_S_BROOMSTICK:
                return 1932005;
            case Beginner.CHARGEWOODEN_PONY:
            case Noblesse.CHARGEWOODEN_PONY:
            case Legend.CHARGEWOODEN_PONY:
            case Evan.CHARGEWOODEN_PONY:
                return 1932006;
            case Beginner.CROCO:
            case Noblesse.CROCO:
            case Legend.CROCO:
            case Evan.CROCO:
                return 1932007;
            case Beginner.BLACK_SCOOTER:
            case Noblesse.BLACK_SCOOTER:
            case Legend.BLACK_SCOOTER:
            case Evan.BLACK_SCOOTER:
                return 1932008;
            case Beginner.PINK_SCOOTER:
            case Noblesse.PINK_SCOOTER:
            case Legend.PINK_SCOOTER:
            case Evan.PINK_SCOOTER:
                return 1932009;
            case Beginner.NIMBUS_CLOUD:
            case Noblesse.NIMBUS_CLOUD:
            case Legend.NIMBUS_CLOUD:
            case Evan.NIMBUS_CLOUD:
                return 1932011;
            case Beginner.BALROG:
            case Noblesse.BALROG:
            case Legend.BALROG:
            case Evan.BALROG:
                return 1932010;
            case Beginner.RACING_KART:
            case Noblesse.RACING_KART:
            case Legend.RACING_KART:
            case Evan.RACING_KART:
                return 1932013;
            case Beginner.ZD_TIGER:
            case Noblesse.ZD_TIGER:
            case Legend.ZD_TIGER:
            case Evan.ZD_TIGER:
                return 1932014;
            case Beginner.MIST_BALROG:
            case Noblesse.MIST_BALROG:
            case Legend.MIST_BALROG:
            case Evan.MIST_BALROG:
                return 1932012;
            case Beginner.SHINJO:
            case Noblesse.SHINJO:
            case Legend.SHINJO:
            case Evan.SHINJO:
                return 1932022;
        }
        return 0;
    }
}
