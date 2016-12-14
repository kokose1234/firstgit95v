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
package server;

import client.MapleBuffStat;
import client.MapleCharacter;
import client.MapleDisease;
import client.MapleJob;
import client.MapleMount;
import client.MapleStat;
import client.Skill;
import client.SkillFactory;
import client.inventory.Item;
import client.inventory.MapleInventory;
import client.inventory.MapleInventoryType;
import client.status.MonsterStatus;
import client.status.MonsterStatusEffect;
import constants.GameConstants;
import constants.ItemConstants;
import constants.skills.Addition;
import constants.skills.Aran;
import constants.skills.Archer;
import constants.skills.Assassin;
import constants.skills.Beginner;
import constants.skills.Bishop;
import constants.skills.Bowmaster;
import constants.skills.Buccaneer;
import constants.skills.Captain;
import constants.skills.Chief;
import constants.skills.ChiefMaster;
import constants.skills.Cleric;
import constants.skills.Crossbowman;
import constants.skills.Crusader;
import constants.skills.DarkKnight;
import constants.skills.DragonKnight;
import constants.skills.DualBlader;
import constants.skills.Dualer;
import constants.skills.Dualmaster;
import constants.skills.Evan;
import constants.skills.FPArchMage;
import constants.skills.FPMage;
import constants.skills.FPWizard;
import constants.skills.Fighter;
import constants.skills.FlameWizard;
import constants.skills.GM;
import constants.skills.Gunslinger;
import constants.skills.Hermit;
import constants.skills.Hero;
import constants.skills.Hunter;
import constants.skills.ILArchMage;
import constants.skills.ILMage;
import constants.skills.ILWizard;
import constants.skills.Infighter;
import constants.skills.Knight;
import constants.skills.Legend;
import constants.skills.Magician;
import constants.skills.Marksman;
import constants.skills.NightLord;
import constants.skills.NightWalker;
import constants.skills.Noblesse;
import constants.skills.Page;
import constants.skills.Paladin;
import constants.skills.Pirate;
import constants.skills.Priest;
import constants.skills.Ranger;
import constants.skills.Rogue;
import constants.skills.SemiDualer;
import constants.skills.Shadower;
import constants.skills.Slasher;
import constants.skills.Sniper;
import constants.skills.SoulMaster;
import constants.skills.Spearman;
import constants.skills.Striker;
import constants.skills.Swordman;
import constants.skills.Valkyrie;
import constants.skills.Viper;
import constants.skills.WindBreaker;
import java.awt.Point;
import java.awt.Rectangle;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import net.server.PlayerCoolDownValueHolder;
import provider.MapleData;
import provider.MapleDataTool;
import server.life.MapleMonster;
import server.maps.FieldLimit;
import server.maps.MapleDoor;
import server.maps.MapleMap;
import server.maps.MapleMapObject;
import server.maps.MapleMapObjectType;
import server.maps.MapleMist;
import server.maps.MapleSummon;
import server.maps.SummonMovementType;
import tools.ArrayMap;
import tools.MaplePacketCreator;
import tools.Pair;

/**
 * @author Matze
 * @author Frz
 */
public class MapleStatEffect {

    private short watk, matk, wdef, mdef, acc, avoid, speed, jump;
    private short hp, mp;
    private double hpR, mpR;
    private short mpCon, hpCon;
    private short dot, dotTime;
    private int duration;
    private boolean overTime, repeatEffect;
    private int sourceid;
    private int moveTo;
    private boolean skill;
    private List<Pair<MapleBuffStat, Integer>> statups;
    private Map<MonsterStatus, Integer> monsterStatus;
    private int x, y, z, mobCount, moneyCon, cooldown, morphId = 0, ghost, fatigue, berserk, booster;
    private double prop;
    private int itemCon, itemConNo;
    private int damage, attackCount, fixdamage;
    private Point lt, rb;
    private byte bulletCount, bulletConsume;

    public static MapleStatEffect loadSkillEffectFromData(MapleData source, int skillid, boolean overtime) {
        return loadFromData(source, skillid, true, overtime);
    }

    public static MapleStatEffect loadItemEffectFromData(MapleData source, int itemid) {
        return loadFromData(source, itemid, false, false);
    }

    private static void addBuffStatPairToListIfNotZero(List<Pair<MapleBuffStat, Integer>> list, MapleBuffStat buffstat, Integer val) {
        if (val.intValue() != 0) {
            list.add(new Pair<>(buffstat, val));
        }
    }

    private static MapleStatEffect loadFromData(MapleData source, int sourceid, boolean skill, boolean overTime) {
        MapleStatEffect ret = new MapleStatEffect();
        ret.duration = MapleDataTool.getIntConvert("time", source, -1);
        ret.hp = (short) MapleDataTool.getInt("hp", source, 0);
        ret.hpR = MapleDataTool.getInt("hpR", source, 0) / 100.0;
        ret.mp = (short) MapleDataTool.getInt("mp", source, 0);
        ret.mpR = MapleDataTool.getInt("mpR", source, 0) / 100.0;
        ret.mpCon = (short) MapleDataTool.getInt("mpCon", source, 0);
        ret.hpCon = (short) MapleDataTool.getInt("hpCon", source, 0);
        int iprop = MapleDataTool.getInt("prop", source, 100);
        ret.prop = iprop / 100.0;
        ret.mobCount = MapleDataTool.getInt("mobCount", source, 1);
        ret.cooldown = MapleDataTool.getInt("cooltime", source, 0);
        ret.morphId = MapleDataTool.getInt("morph", source, 0);
        ret.ghost = MapleDataTool.getInt("ghost", source, 0);
        ret.fatigue = MapleDataTool.getInt("incFatigue", source, 0);
        ret.repeatEffect = MapleDataTool.getInt("repeatEffect", source, 0) > 0;

        ret.sourceid = sourceid;
        ret.skill = skill;
        if (!ret.skill && ret.duration > -1) {
            ret.overTime = true;
        } else {
            ret.duration *= 1000; // items have their times stored in ms, of course
            ret.overTime = overTime;
        }
        ArrayList<Pair<MapleBuffStat, Integer>> statups = new ArrayList<>();
        ret.watk = (short) MapleDataTool.getInt("pad", source, 0);
        ret.wdef = (short) MapleDataTool.getInt("pdd", source, 0);
        ret.matk = (short) MapleDataTool.getInt("mad", source, 0);
        ret.mdef = (short) MapleDataTool.getInt("mdd", source, 0);
        ret.acc = (short) MapleDataTool.getIntConvert("acc", source, 0);
        ret.avoid = (short) MapleDataTool.getInt("eva", source, 0);
        ret.speed = (short) MapleDataTool.getInt("speed", source, 0);
        ret.jump = (short) MapleDataTool.getInt("jump", source, 0);
        ret.berserk = MapleDataTool.getInt("berserk", source, 0);
        ret.booster = MapleDataTool.getInt("booster", source, 0);
        //������ �ö󰡸� ���������� �ϴ��ּ�
        /*
        if (ret.overTime && ret.getSummonMovementType() == null) {
            addBuffStatPairToListIfNotZero(statups, MapleBuffStat.WATK, Integer.valueOf(ret.watk));
            addBuffStatPairToListIfNotZero(statups, MapleBuffStat.WDEF, Integer.valueOf(ret.wdef));
            addBuffStatPairToListIfNotZero(statups, MapleBuffStat.MATK, Integer.valueOf(ret.matk));
            addBuffStatPairToListIfNotZero(statups, MapleBuffStat.MDEF, Integer.valueOf(ret.mdef));
            addBuffStatPairToListIfNotZero(statups, MapleBuffStat.ACC, Integer.valueOf(ret.acc));
            addBuffStatPairToListIfNotZero(statups, MapleBuffStat.AVOID, Integer.valueOf(ret.avoid));
            addBuffStatPairToListIfNotZero(statups, MapleBuffStat.SPEED, Integer.valueOf(ret.speed));
            addBuffStatPairToListIfNotZero(statups, MapleBuffStat.JUMP, Integer.valueOf(ret.jump));
            addBuffStatPairToListIfNotZero(statups, MapleBuffStat.PYRAMID_PQ, Integer.valueOf(ret.berserk));
            addBuffStatPairToListIfNotZero(statups, MapleBuffStat.BOOSTER, Integer.valueOf(ret.booster));
        }
         */
        MapleData ltd = source.getChildByPath("lt");
        if (ltd != null) {
            ret.lt = (Point) ltd.getData();
            ret.rb = (Point) source.getChildByPath("rb").getData();
        }
        ret.x = MapleDataTool.getInt("x", source, 0);
        ret.y = MapleDataTool.getInt("y", source, 0);
        ret.z = MapleDataTool.getInt("z", source, 0);
        ret.damage = MapleDataTool.getIntConvert("damage", source, 100);
        ret.fixdamage = MapleDataTool.getIntConvert("fixdamage", source, -1);
        ret.attackCount = MapleDataTool.getIntConvert("attackCount", source, 1);
        ret.bulletCount = (byte) MapleDataTool.getIntConvert("bulletCount", source, 1);
        ret.bulletConsume = (byte) MapleDataTool.getIntConvert("bulletConsume", source, 0);
        ret.moneyCon = MapleDataTool.getIntConvert("moneyCon", source, 0);
        ret.itemCon = MapleDataTool.getInt("itemCon", source, 0);
        ret.itemConNo = MapleDataTool.getInt("itemConNo", source, 0);
        ret.moveTo = MapleDataTool.getInt("moveTo", source, -1);
        ret.dot = (short) MapleDataTool.getInt("dot", source, 0);
        ret.dotTime = (short) MapleDataTool.getInt("dotTime", source, 0);
        Map<MonsterStatus, Integer> monsterStatus = new ArrayMap<>();
        if (skill) { //95..초보자부터 차근차근
            switch (sourceid) {
                case Beginner.RECOVERY:
                case Noblesse.RECOVERY:
                case Legend.RECOVERY:
                case Evan.RECOVERY:
                    statups.add(new Pair<>(MapleBuffStat.RECOVERY, ret.x));
                    break;
                case Beginner.NIMBLE_FEET:
                case Noblesse.NIMBLE_FEET:
                case Legend.AGILE_BODY:
                case Evan.NIMBLE_FEET:
                    statups.add(new Pair<>(MapleBuffStat.SPEED, (int) ret.speed));
                    break;
                case Beginner.MONSTER_RIDER:
                case Noblesse.MONSTER_RIDER:
                case Legend.MONSTER_RIDER:
                case Evan.MONSTER_RIDER:
                case Beginner.SPACESHIP:
                case Noblesse.SPACESHIP:
                case Beginner.YETI_RIDER:
                case Noblesse.YETI_RIDER:
                case Legend.YETI_RIDER:
                case Evan.YETI_RIDER:
                case Beginner.YETI_MOUNT:
                case Noblesse.YETI_MOUNT:
                case Legend.YETI_MOUNT:
                case Beginner.WITCH_S_BROOMSTICK:
                case Noblesse.WITCH_S_BROOMSTICK:
                case Legend.WITCH_S_BROOMSTICK:
                case Evan.WITCH_S_BROOMSTICK:
                case Beginner.CHARGEWOODEN_PONY:
                case Noblesse.CHARGEWOODEN_PONY:
                case Legend.CHARGEWOODEN_PONY:
                case Evan.CHARGEWOODEN_PONY:
                case Beginner.CROCO:
                case Noblesse.CROCO:
                case Legend.CROCO:
                case Evan.CROCO:
                case Beginner.BLACK_SCOOTER:
                case Noblesse.BLACK_SCOOTER:
                case Legend.BLACK_SCOOTER:
                case Evan.BLACK_SCOOTER:
                case Beginner.PINK_SCOOTER:
                case Noblesse.PINK_SCOOTER:
                case Legend.PINK_SCOOTER:
                case Evan.PINK_SCOOTER:
                case Beginner.NIMBUS_CLOUD:
                case Noblesse.NIMBUS_CLOUD:
                case Legend.NIMBUS_CLOUD:
                case Evan.NIMBUS_CLOUD:
                case Beginner.BALROG:
                case Noblesse.BALROG:
                case Legend.BALROG:
                case Evan.BALROG:
                case Beginner.RACING_KART:
                case Noblesse.RACING_KART:
                case Legend.RACING_KART:
                case Evan.RACING_KART:
                case Beginner.ZD_TIGER:
                case Noblesse.ZD_TIGER:
                case Legend.ZD_TIGER:
                case Evan.ZD_TIGER:
                case Beginner.MIST_BALROG:
                case Noblesse.MIST_BALROG:
                case Legend.MIST_BALROG:
                case Evan.MIST_BALROG:
                case Beginner.SHINJO:
                case Noblesse.SHINJO:
                case Legend.SHINJO:
                case Evan.SHINJO:
                    statups.add(new Pair<>(MapleBuffStat.MONSTER_RIDING, sourceid));
                    break;
                case Beginner.ECHO_OF_HERO:
                case Noblesse.ECHO_OF_HERO:
                case Legend.ECHO_OF_HERO:
                case Evan.ECHO_OF_HERO:
                    statups.add(new Pair<>(MapleBuffStat.ECHO_OF_HERO, ret.x));
                    break;
                case Beginner.INVINCIBILITY:
                case Noblesse.INVINCIBLE_BARRIER:
                case Legend.INVINCIBLE_BARRIER:
                case Evan.INVINCIBLE_BARRIER:
                    statups.add(new Pair<>(MapleBuffStat.DIVINE_BODY, 1));
                    break;
                case Beginner.POWER_EXPLOSION:
                case Noblesse.METEO_SHOWER:
                case Legend.METEO_SHOWER:
                case Evan.METEO_SHOWER:
                    statups.add(new Pair<>(MapleBuffStat.BERSERK_FURY, 1));
                    break;
                case Swordman.IRON_BODY:
                case SoulMaster.IRON_BODY:
                case Magician.MAGIC_ARMOR:
                case FlameWizard.MAGIC_ARMOR:
                    statups.add(new Pair<>(MapleBuffStat.WDEF, (int) ret.wdef));
                    break;
                case Fighter.SWORD_BOOSTER:
                case Fighter.AXE_BOOSTER:
                case Page.SWORD_BOOSTER:
                case Page.BW_BOOSTER:
                case Spearman.SPEAR_BOOSTER:
                case Spearman.POLE_ARM_BOOSTER:
                case Hunter.BOW_BOOSTER:
                case Crossbowman.CROSSBOW_BOOSTER:
                case Assassin.CLAW_BOOSTER:
                case Chief.DAGGER_BOOSTER:
                case SemiDualer.KATARA_BOOSTER:
                case FPMage.SPELL_BOOSTER:
                case ILMage.SPELL_BOOSTER:
                case Infighter.KNUCKLER_BOOSTER:
                case Gunslinger.GUN_BOOSTER:
                case SoulMaster.SWORD_BOOSTER:
                case FlameWizard.SPELL_BOOSTER:
                case WindBreaker.BOW_BOOSTER:
                case NightWalker.CLAW_BOOSTER:
                case Striker.KNUCKLE_BOOSTER:
                case Aran.POLEARM_BOOSTER:
                case Evan.MAGIC_BOOSTER:
                    statups.add(new Pair<>(MapleBuffStat.BOOSTER, ret.x));
                    break;
                case Fighter.RAGE:
                case SoulMaster.RAGE:
                    statups.add(new Pair<>(MapleBuffStat.WATK, (int) ret.watk));
                    statups.add(new Pair<>(MapleBuffStat.WDEF, (int) ret.wdef));
                    break;
                case Fighter.POWER_GUARD:
                case Page.POWER_GUARD:
                    statups.add(new Pair<>(MapleBuffStat.POWERGUARD, ret.x));
                    break;
                case Crusader.COMBO_ATTACK:
                case SoulMaster.COMBO_ATTACK:
                    statups.add(new Pair<>(MapleBuffStat.COMBO, 1));
                    break;
                case Crusader.PANIC_SWORD:
                case Crusader.PANIC_AXE:
                case SoulMaster.PANIC:
                    monsterStatus.put(MonsterStatus.DARKNESS, 1);
                    break;
                case Crusader.COMASWORD:
                case Crusader.COMAAXE:
                case SoulMaster.COMA:
                case Crusader.SHOUT:
                case ChiefMaster.ASSAULTER:
                    monsterStatus.put(MonsterStatus.STUN, 1);
                    break;
                case Hero.MAPLE_WARRIOR:
                case Paladin.MAPLE_WARRIOR:
                case DarkKnight.MAPLE_WARRIOR:
                case FPArchMage.MAPLE_WARRIOR:
                case ILArchMage.MAPLE_WARRIOR:
                case Bishop.MAPLE_WARRIOR:
                case Bowmaster.MAPLE_WARRIOR:
                case Marksman.MAPLE_WARRIOR:
                case Shadower.MAPLE_WARRIOR:
                case NightLord.MAPLE_WARRIOR:
                case DualBlader.MAPLE_WARRIOR:
                case Viper.MAPLE_WARRIOR:
                case Captain.MAPLE_WARRIOR:
                case Aran.MAPLE_WARRIOR:
                case Evan.MAPLE_WARRIOR:
                    statups.add(new Pair<>(MapleBuffStat.MAPLE_WARRIOR, ret.x));
                    break;
                case Hero.POWER_STANCE:
                case Paladin.POWER_STANCE:
                case DarkKnight.POWER_STANCE:
                case Aran.FREEZE_STANDING:
                    statups.add(new Pair<>(MapleBuffStat.STANCE, iprop));
                    break;
                case Hero.ENRAGE:
                    statups.add(new Pair<>(MapleBuffStat.ENHANCED_WATK, (int) ret.watk));
                    break;
                case Page.THREATEN:
                    monsterStatus.put(MonsterStatus.WATK, ret.x);
                    monsterStatus.put(MonsterStatus.WDEF, ret.y);
                    monsterStatus.put(MonsterStatus.DARKNESS, ret.z);
                    break;
                case Knight.FIRE_CHARGESWORD:
                case Knight.FLAME_CHARGEBW:
                case Knight.ICE_CHARGESWORD:
                case Knight.BLIZZARD_CHARGEBW:
                case Knight.THUNDER_CHARGESWORD:
                case Knight.LIGHTNING_CHARGEBW:
                case Paladin.HOLY_CHARGE_SWORD:
                case Paladin.DIVINE_CHARGE_BW:
                case SoulMaster.SOUL_CHARGE:
                case Striker.LIGHTNING_CHARGE:
                case Aran.SNOW_CHARGE:
                    statups.add(new Pair<>(MapleBuffStat.WK_CHARGE, ret.x));
                    break;
                case Spearman.IRON_WALL:
                    statups.add(new Pair<>(MapleBuffStat.WDEF, (int) ret.wdef));
                    statups.add(new Pair<>(MapleBuffStat.MDEF, (int) ret.mdef));
                    break;
                case Spearman.HYPER_BODY:
                case GM.HYPER_BODY:
                    statups.add(new Pair<>(MapleBuffStat.HYPERBODYHP, ret.x));
                    statups.add(new Pair<>(MapleBuffStat.HYPERBODYMP, ret.y));
                    break;
                case DragonKnight.DRAGON_BLOOD:
                    statups.add(new Pair<>(MapleBuffStat.ENHANCED_WATK, (int) ret.watk));
                    statups.add(new Pair<>(MapleBuffStat.DRAGONBLOOD, ret.x));
                    break;
                case Magician.MAGIC_GUARD:
                case FlameWizard.MAGIC_GUARD:
                case Evan.MAGIC_GUARD:
                    statups.add(new Pair<>(MapleBuffStat.MAGIC_GUARD, (int) ret.x));
                    break;
                case FPWizard.MEDITATION:
                case ILWizard.MEDITATION:
                case FlameWizard.MEDITATION:
                    statups.add(new Pair<>(MapleBuffStat.MATK, (int) ret.matk));
                    break;
                case FPWizard.SLOW:
                case ILWizard.SLOW:
                case FlameWizard.SLOW:
                case Addition.슬로우:
                    monsterStatus.put(MonsterStatus.SPEED, ret.x);
                    break;
                case FPMage.SEAL:
                case ILMage.SEAL:
                case FlameWizard.SEAL:
                    monsterStatus.put(MonsterStatus.SEAL, 1);
                    break;
                case FPArchMage.MANA_REFLECTION:
                case ILArchMage.MANA_REFLECTION:
                case Bishop.MANA_REFLECTION:
                    statups.add(new Pair<>(MapleBuffStat.MANA_REFLECTION, (int) ret.x));
                    break;
                case FPArchMage.INFINITY:
                case ILArchMage.INFINITY:
                case Bishop.INFINITY:
                    statups.add(new Pair<>(MapleBuffStat.INFINITY, (int) ret.x));
                    break;
                case FPArchMage.PARALYZE:
                case ILWizard.COLD_BEAM:
                case ILMage.ICE_STRIKE:
                case ILMage.ELEMENT_COMPOSITION:
                case ILArchMage.BLIZZARD:
                case Sniper.BLIZZARD:
                case Valkyrie.ICE_SPLITTER:
                case Aran.COMBO_TEMPEST:
                case Evan.ICE_BREATH:
                    monsterStatus.put(MonsterStatus.FREEZE, 1);
                    break;
                case Cleric.INVINCIBLE:
                    statups.add(new Pair<>(MapleBuffStat.INVINCIBLE, ret.x));
                    break;
                case Cleric.BLESS:
                case GM.BLESS:
                    if (sourceid == GM.BLESS) {
                        statups.add(new Pair<>(MapleBuffStat.WATK, (int) ret.watk));
                        statups.add(new Pair<>(MapleBuffStat.MATK, (int) ret.matk));
                    }
                    statups.add(new Pair<>(MapleBuffStat.ACC, (int) ret.acc));
                    statups.add(new Pair<>(MapleBuffStat.AVOID, (int) ret.avoid));
                    statups.add(new Pair<>(MapleBuffStat.WDEF, (int) ret.wdef));
                    statups.add(new Pair<>(MapleBuffStat.MDEF, (int) ret.mdef));
                    break;
                case Priest.HOLY_SYMBOL:
                case GM.HOLY_SYMBOL: // GM hs
                    statups.add(new Pair<>(MapleBuffStat.HOLY_SYMBOL, ret.x));
                    break;
                case Priest.DOOM:
                    monsterStatus.put(MonsterStatus.DOOM, 1);
                    break;
                case Bishop.HOLY_SHIELD:
                    statups.add(new Pair<>(MapleBuffStat.HOLY_SHIELD, ret.x));
                    break;
                case Archer.FOCUS:
                case WindBreaker.FOCUS:
                    statups.add(new Pair<>(MapleBuffStat.ACC, (int) ret.acc));
                    statups.add(new Pair<>(MapleBuffStat.AVOID, (int) ret.avoid));
                    break;
                case Hunter.SOUL_ARROW_BOW:
                case Crossbowman.SOUL_ARROW_CROSSBOW:
                case WindBreaker.SOUL_ARROW:
                    statups.add(new Pair<>(MapleBuffStat.SOULARROW, ret.x));
                    break;
                case Bowmaster.SHARP_EYES:
                case Marksman.SHARP_EYES:
                    statups.add(new Pair<>(MapleBuffStat.SHARP_EYES, ret.x << 8 | ret.y));
                    break;
                case Bowmaster.HAMSTRING:
                    statups.add(new Pair<>(MapleBuffStat.HAMSTRING, ret.x));
                    monsterStatus.put(MonsterStatus.SPEED, ret.x);
                    break;
                case Bowmaster.CONCENTRATE:
                    statups.add(new Pair<>(MapleBuffStat.ENHANCED_WATK, (int) ret.watk));
                    statups.add(new Pair<>(MapleBuffStat.CONCENTRATE, ret.x));
                    break;
                case Marksman.BLIND:
                    statups.add(new Pair<>(MapleBuffStat.BLIND, ret.x));
                    monsterStatus.put(MonsterStatus.DARKNESS, ret.x);
                    break;
                case Rogue.DISORDER:
                case NightWalker.DISORDER:
                    monsterStatus.put(MonsterStatus.WATK, ret.x);
                    monsterStatus.put(MonsterStatus.WDEF, ret.y);
                    break;
                case Rogue.DARK_SIGHT:
                case NightWalker.DARK_SIGHT:
                    statups.add(new Pair<>(MapleBuffStat.SPEED, (int) ret.speed));
                    statups.add(new Pair<>(MapleBuffStat.DARKSIGHT, ret.x));
                    break;
                case Assassin.HASTE:
                case Chief.HASTE:
                case Dualer.SELF_HASTE:
                case GM.HASTE_SUPER:
                case NightWalker.HASTE:
                    statups.add(new Pair<>(MapleBuffStat.SPEED, (int) ret.speed));
                    statups.add(new Pair<>(MapleBuffStat.JUMP, (int) ret.jump));
                    break;
                case Hermit.MESO_UP:
                    statups.add(new Pair<>(MapleBuffStat.MESOUP, ret.x));
                    break;
                case Hermit.SHADOW_PARTNER:
                case NightWalker.SHADOW_PARTNER:
                    statups.add(new Pair<>(MapleBuffStat.SHADOWPARTNER, ret.x));
                    break;
                case Hermit.SHADOW_WEB:
                case NightWalker.SHADOW_WEB:
                    monsterStatus.put(MonsterStatus.SHADOW_WEB, 1);
                    break;
                case NightLord.TAUNT:
                case Shadower.TAUNT:
                    monsterStatus.put(MonsterStatus.SHOWDOWN, ret.x);
                    monsterStatus.put(MonsterStatus.WDEF, ret.x);
                    monsterStatus.put(MonsterStatus.MDEF, ret.x);
                    break;
                case NightLord.NINJA_AMBUSH:
                case Shadower.NINJA_AMBUSH:
                    monsterStatus.put(MonsterStatus.NINJA_AMBUSH, ret.damage);
                    break;
                case NightLord.SHADOW_STARS:
                    statups.add(new Pair<>(MapleBuffStat.SHADOW_CLAW, 0));
                    break;
                case ChiefMaster.PICKPOCKET:
                    statups.add(new Pair<>(MapleBuffStat.PICKPOCKET, ret.x));
                    break;
                case ChiefMaster.MESO_GUARD:
                    statups.add(new Pair<>(MapleBuffStat.MESOGUARD, ret.x));
                    break;
                case Dualmaster.TORNADO_SPIN:
                    ret.duration = 1000;
                    statups.add(new Pair<>(MapleBuffStat.DASH_SPEED, 100 + ret.x));
                    statups.add(new Pair<>(MapleBuffStat.DASH_JUMP, ret.y));
                    break;
                case Dualmaster.FLASHBANG:
                    monsterStatus.put(MonsterStatus.DARKNESS, ret.x);
                    break;
                case Slasher.MIRROR_IMAGE:
                    statups.add(new Pair<>(MapleBuffStat.MIRROR_IMAGE, ret.x));
                    break;
                case Slasher.OWL_SPIRIT:
                    ret.duration = 60 * 1000; //�ӽ�ó�� 
                    statups.add(new Pair<>(MapleBuffStat.OWL_SPIRIT, ret.x));
                    break;
                case DualBlader.FINAL_CUT: //�ӽ�ó��
                    ret.duration = 60 * 1000;
                    ret.hpR = -ret.x / 100.0;
                    statups.add(new Pair<>(MapleBuffStat.FINAL_CUT, ret.y));
                    break;
                case DualBlader.MONSTER_BOMB:
                    ret.mobCount = 1;
                    monsterStatus.put(MonsterStatus.MONSTER_BOMB, 1);
                    break;
                case DualBlader.THORNS:
                    statups.add(new Pair<>(MapleBuffStat.THORNS, ret.x << 8 | ret.y));
                    break;
                case Pirate.DASH:
                case Striker.DASH:
                    statups.add(new Pair<>(MapleBuffStat.DASH_SPEED, ret.x));
                    statups.add(new Pair<>(MapleBuffStat.DASH_JUMP, ret.y));
                    break;
                case Buccaneer.TRANSFORMATION:
                case Viper.SUPER_TRANSFORMATION:
                case WindBreaker.EAGLE_EYE:
                case Striker.TRANSFORMATION:
                    statups.add(new Pair<>(MapleBuffStat.SPEED, (int) ret.speed));
                    statups.add(new Pair<>(MapleBuffStat.JUMP, (int) ret.jump));
                    statups.add(new Pair<>(MapleBuffStat.ENHANCED_WDEF, (int) ret.wdef));
                    statups.add(new Pair<>(MapleBuffStat.ENHANCED_MDEF, (int) ret.mdef));
                    break;
                case Viper.SPEED_INFUSION:
                case Striker.SPEED_INFUSION:
                    statups.add(new Pair<>(MapleBuffStat.SPEED_INFUSION, ret.x));
                    break;
                case Valkyrie.HOMING_BEACON:
                case Captain.BULLSEYE:
                case Evan.KILLER_WINGS:
                    ret.duration = 2100000000;
                    statups.add(new Pair<>(MapleBuffStat.HOMING_BEACON, ret.x));
                    break;
                case Captain.HYPNOTIZE:
                    monsterStatus.put(MonsterStatus.INERTMOB, 1);
                    break;
                case FlameWizard.ELEMENTAL_RESET:
                case Evan.ELEMENTAL_RESET:
                    statups.add(new Pair<>(MapleBuffStat.ELEMENTAL_RESET, ret.x));
                    break;
                case WindBreaker.WIND_WALK:
                    statups.add(new Pair<>(MapleBuffStat.WIND_WALK, ret.x));
                    break;
                case Striker.SPARK:
                    statups.add(new Pair<>(MapleBuffStat.SPARK, ret.x));
                    break;
                case Aran.BODY_PRESSURE:
                    statups.add(new Pair<>(MapleBuffStat.BODY_PRESSURE, ret.x));
                    break;
                case Aran.COMBO_DRAIN:
                    statups.add(new Pair<>(MapleBuffStat.COMBO_DRAIN, ret.x));
                    break;
                case Aran.SMART_KNOCKBACK:
                    statups.add(new Pair<>(MapleBuffStat.SMART_KNOCKBACK, ret.x));
                    break;
                case Aran.COMBO_BARRIER:
                    statups.add(new Pair<>(MapleBuffStat.COMBO_BARRIER, ret.x));
                    break;
                case Evan.MAGIC_SHIELD:
                    statups.add(new Pair<>(MapleBuffStat.MAGIC_SHIELD, ret.x));
                    break;
                case Evan.SLOW:
                    statups.add(new Pair<>(MapleBuffStat.EVAN_SLOW, ret.x));
                    break;
                case Evan.MAGIC_RESISTANCE:
                    statups.add(new Pair<>(MapleBuffStat.MAGIC_RESISTANCE, ret.x));
                    break;
                case Evan.PHANTOM_IMPRINT:
                    monsterStatus.put(MonsterStatus.IMPRINT, ret.x);
                    break;
                case Evan.BLESSING_OF_THE_ONYX:
                    statups.add(new Pair<>(MapleBuffStat.MATK, (int) ret.matk));
                    statups.add(new Pair<>(MapleBuffStat.ENHANCED_WDEF, (int) ret.wdef));
                    statups.add(new Pair<>(MapleBuffStat.ENHANCED_MDEF, (int) ret.wdef));
                    break;
                case Evan.SOUL_STONE:
                    statups.add(new Pair<>(MapleBuffStat.SOUL_STONE, ret.x));
                    break;
                //
                case Evan.SOARING:
                    ret.duration = 2100000000;
                    statups.add(new Pair<>(MapleBuffStat.SOARING, 1));
                    break;
            }
        }
        if (ret.isPoison()) {
            monsterStatus.put(MonsterStatus.POISON, 1);
        }
        if (ret.isMorph()) {
            statups.add(new Pair<>(MapleBuffStat.MORPH, ret.getMorph()));
        }
        if (ret.ghost > 0 && !skill) {
            statups.add(new Pair<>(MapleBuffStat.GHOST_MORPH, ret.ghost));
        }
        ret.monsterStatus = monsterStatus;
        statups.trimToSize();
        ret.statups = statups;
        return ret;
    }

    /**
     * @param applyto
     * @param obj
     * @param attack damage done by the skill
     */
    public void applyPassive(MapleCharacter applyto, MapleMapObject obj, int attack) {
        if (makeChanceResult()) {
            switch (sourceid) { // MP eater
                case FPWizard.MP_EATER:
                case ILWizard.MP_EATER:
                case Cleric.MP_EATER:
                    if (obj == null || obj.getType() != MapleMapObjectType.MONSTER) {
                        return;
                    }
                    MapleMonster mob = (MapleMonster) obj; // x is absorb percentage
                    if (!mob.isBoss()) {
                        int absorbMp = Math.min((int) (mob.getMaxMp() * (getX() / 100.0)), mob.getMp());
                        if (absorbMp > 0) {
                            mob.setMp(mob.getMp() - absorbMp);
                            applyto.addMP(absorbMp);
                            applyto.getClient().announce(MaplePacketCreator.showOwnBuffEffect(sourceid, 1, applyto.getLevel(), applyto.getSkillLevel(sourceid), (byte) 3));
                            applyto.getMap().broadcastMessage(applyto, MaplePacketCreator.showBuffeffect(applyto.getId(), sourceid, applyto.getLevel(), applyto.getSkillLevel(sourceid), 1), false);
                        }
                    }
                    break;
            }
        }
    }

    public boolean applyTo(MapleCharacter chr) {
        return applyTo(chr, chr, true, null);
    }

    public boolean applyTo(MapleCharacter chr, Point pos) {
        return applyTo(chr, chr, true, pos);
    }

    private boolean applyTo(MapleCharacter applyfrom, MapleCharacter applyto, boolean primary, Point pos) {
        if (skill && sourceid == GM.HIDE) {
            applyto.toggleHide(false);
            return true;
        }
        int hpchange = calcHPChange(applyfrom, primary);
        int mpchange = calcMPChange(applyfrom, primary);
        if (primary) {
            if (itemConNo != 0) {
                MapleInventoryManipulator.removeById(applyto.getClient(), MapleItemInformationProvider.getInstance().getInventoryType(itemCon), itemCon, itemConNo, false, true);
            }
        }
        List<Pair<MapleStat, Integer>> hpmpupdate = new ArrayList<>(2);
        if (!primary && isResurrection()) {
            hpchange = applyto.getMaxHp();
            applyto.setStance(0);
            applyto.getMap().broadcastMessage(applyto, MaplePacketCreator.removePlayerFromMap(applyto.getId()), false);
            applyto.getMap().broadcastMessage(applyto, MaplePacketCreator.spawnPlayerMapobject(applyto), false);
        }
        if (isDispel() && makeChanceResult()) {
            applyto.dispelDebuffs();
        } else if (isHeroWill()) {
            applyto.dispelDebuff(MapleDisease.SEDUCE);
        }
        if (isComboReset()) {
            applyto.setCombo((short) 0);
        }
        if (hpchange != 0) {
            if (hpchange < 0 && (-hpchange) > applyto.getHp()) {
                return false;
            }
            int newHp = applyto.getHp() + hpchange;
            if (newHp < 1) {
                newHp = 1;
            }
            applyto.setHp(newHp);
            hpmpupdate.add(new Pair<>(MapleStat.HP, Integer.valueOf(applyto.getHp())));
        }
        int newMp = applyto.getMp() + mpchange;
        if (mpchange != 0) {
            if (mpchange < 0 && -mpchange > applyto.getMp()) {
                return false;
            }

            applyto.setMp(newMp);
            hpmpupdate.add(new Pair<>(MapleStat.MP, Integer.valueOf(applyto.getMp())));
        }
        applyto.getClient().announce(MaplePacketCreator.updatePlayerStats(hpmpupdate, true, applyto.getJob().getId()));
        if (moveTo != -1) {
            if (applyto.getMap().getReturnMapId() != applyto.getMapId()) {
                MapleMap target;
                if (moveTo == 999999999) {
                    target = applyto.getMap().getReturnMap();
                } else {
                    target = applyto.getClient().getWorldServer().getChannel(applyto.getClient().getChannel()).getMapFactory().getMap(moveTo);
                    int targetid = target.getId() / 10000000;
                    if (targetid != 60 && applyto.getMapId() / 10000000 != 61 && targetid != applyto.getMapId() / 10000000 && targetid != 21 && targetid != 20) {
                        return false;
                    }
                }
                applyto.changeMap(target);
            } else {
                return false;
            }

        }
        if (isShadowClaw()) {
            int projectile = 0;
            MapleInventory use = applyto.getInventory(MapleInventoryType.USE);
            for (int i = 0; i < 97; i++) { // impose order...
                Item item = use.getItem((byte) i);
                if (item != null) {
                    if (ItemConstants.isThrowingStar(item.getItemId()) && item.getQuantity() >= 200) {
                        projectile = item.getItemId();
                        break;
                    }
                }
            }
            if (projectile == 0) {
                return false;
            } else {
                MapleInventoryManipulator.removeById(applyto.getClient(), MapleInventoryType.USE, projectile, 200, false, true);
            }

        }
        SummonMovementType summonMovementType = getSummonMovementType();
        if (overTime || isCygnusFA() || summonMovementType != null) {
            applyBuffEffect(applyfrom, applyto, primary);
        }

        if (primary && (overTime || isHeal())) {
            applyBuff(applyfrom);
        }

        if (primary && isMonsterBuff()) {
            applyMonsterBuff(applyfrom);
        }

        if (this.getFatigue() != 0) {
            applyto.getMount().setTiredness(applyto.getMount().getTiredness() + this.getFatigue());
        }
        if (summonMovementType != null && pos != null) {
            final MapleSummon tosummon = new MapleSummon(applyfrom, sourceid, pos, summonMovementType);
            applyfrom.getMap().spawnSummon(tosummon);
            applyfrom.addSummon(sourceid, tosummon);
            tosummon.addHP(x);
            if (isBeholder()) {
                tosummon.addHP(1);
            }
        }
        if (isMagicDoor() && !FieldLimit.DOOR.check(applyto.getMap().getFieldLimit())) { // Magic Door
            Point doorPosition = new Point(applyto.getPosition());
            MapleDoor door = new MapleDoor(applyto, doorPosition);
            applyto.getMap().spawnDoor(door);
            applyto.addDoor(door);
            door = new MapleDoor(door);
            applyto.addDoor(door);
            door.getTown().spawnDoor(door);
            if (applyto.getParty() != null) {// update town doors
                applyto.silentPartyUpdate();
            }
            applyto.disableDoor();
        } else if (isMist()) {
            Rectangle bounds = calculateBoundingBox(applyfrom.getPosition(), applyfrom.isFacingLeft());
            MapleMist mist = new MapleMist(bounds, applyfrom, this);
            applyfrom.getMap().spawnMist(mist, getDuration(), sourceid != Shadower.SMOKESCREEN, false);
        } else if (isTimeLeap()) { // Time Leap
            for (PlayerCoolDownValueHolder i : applyto.getAllCooldowns()) {
                if (i.skillId != Viper.TIME_LEAP) {
                    applyto.removeCooldown(i.skillId);
                }
            }
        }
        return true;
    }

    private void applyBuff(MapleCharacter applyfrom) {
        if (isPartyBuff() && (applyfrom.getParty() != null || isGmBuff())) {
            Rectangle bounds = calculateBoundingBox(applyfrom.getPosition(), applyfrom.isFacingLeft());
            List<MapleMapObject> affecteds = applyfrom.getMap().getMapObjectsInRect(bounds, Arrays.asList(MapleMapObjectType.PLAYER));
            List<MapleCharacter> affectedp = new ArrayList<>(affecteds.size());
            for (MapleMapObject affectedmo : affecteds) {
                MapleCharacter affected = (MapleCharacter) affectedmo;
                if (affected != applyfrom && (isGmBuff() || applyfrom.getParty().equals(affected.getParty()))) {
                    if ((isResurrection() && !affected.isAlive()) || (!isResurrection() && affected.isAlive())) {
                        affectedp.add(affected);
                    }
                    if (isTimeLeap()) {
                        for (PlayerCoolDownValueHolder i : affected.getAllCooldowns()) {
                            affected.removeCooldown(i.skillId);
                        }
                    }
                }
            }
            for (MapleCharacter affected : affectedp) {
                applyTo(applyfrom, affected, false, null);
                affected.getClient().announce(MaplePacketCreator.showOwnBuffEffect(sourceid, 2, applyfrom.getLevel(), applyfrom.getSkillLevel(sourceid), (byte) 3));
                affected.getMap().broadcastMessage(affected, MaplePacketCreator.showBuffeffect(affected.getId(), sourceid, applyfrom.getLevel(), applyfrom.getSkillLevel(sourceid), 2), false);
            }
        }
    }

    private void applyMonsterBuff(MapleCharacter applyfrom) {
        Rectangle bounds = calculateBoundingBox(applyfrom.getPosition(), applyfrom.isFacingLeft());
        List<MapleMapObject> affected = applyfrom.getMap().getMapObjectsInRect(bounds, Arrays.asList(MapleMapObjectType.MONSTER));
        Skill skill_ = SkillFactory.getSkill(sourceid);
        int i = 0;
        for (MapleMapObject mo : affected) {
            MapleMonster monster = (MapleMonster) mo;
            if (makeChanceResult()) {
                monster.applyStatus(applyfrom, new MonsterStatusEffect(getMonsterStati(), skill_, null, false), isPoison(), getDuration());
            }
            i++;
            if (i >= mobCount) {
                break;
            }
        }
    }

    private Rectangle calculateBoundingBox(Point posFrom, boolean facingLeft) {
        Point mylt;
        Point myrb;
        if (facingLeft) {
            mylt = new Point(lt.x + posFrom.x, lt.y + posFrom.y);
            myrb = new Point(rb.x + posFrom.x, rb.y + posFrom.y);
        } else {
            myrb = new Point(-lt.x + posFrom.x, rb.y + posFrom.y);
            mylt = new Point(-rb.x + posFrom.x, lt.y + posFrom.y);
        }
        Rectangle bounds = new Rectangle(mylt.x, mylt.y, myrb.x - mylt.x, myrb.y - mylt.y);
        return bounds;
    }

    public void silentApplyBuff(MapleCharacter chr, long starttime) {
        int localDuration = duration;
        localDuration = alchemistModifyVal(chr, localDuration, false);
        CancelEffectAction cancelAction = new CancelEffectAction(chr, this, starttime);
        ScheduledFuture<?> schedule = TimerManager.getInstance().schedule(cancelAction, ((starttime + localDuration) - System.currentTimeMillis()));
        chr.registerEffect(this, starttime, schedule);
        SummonMovementType summonMovementType = getSummonMovementType();
        if (summonMovementType != null) {
            final MapleSummon tosummon = new MapleSummon(chr, sourceid, chr.getPosition(), summonMovementType);
            if (!tosummon.isStationary()) {
                chr.addSummon(sourceid, tosummon);
                tosummon.addHP(x);
            }
        }
        if (sourceid == Captain.BATTLESHIP) {
            chr.announce(MaplePacketCreator.skillCooldown(5221999, chr.getBattleshipHp()));
        }
    }

    public final void applyComboBuff(final MapleCharacter applyto, int combo) {
        final List<Pair<MapleBuffStat, Integer>> stat = Collections.singletonList(new Pair<>(MapleBuffStat.ARAN_COMBO, combo));
        applyto.getClient().announce(MaplePacketCreator.giveBuff(sourceid, 99999, stat));

        final long starttime = System.currentTimeMillis();
//	final CancelEffectAction cancelAction = new CancelEffectAction(applyto, this, starttime);
//	final ScheduledFuture<?> schedule = TimerManager.getInstance().schedule(cancelAction, ((starttime + 99999) - System.currentTimeMillis()));
        applyto.registerEffect(this, starttime, null);
    }

    private void applyBuffEffect(MapleCharacter applyfrom, MapleCharacter applyto, boolean primary) {
        if (!isMonsterRiding()) {
            applyto.cancelEffect(this, true, -1);
        }

        List<Pair<MapleBuffStat, Integer>> localstatups = statups;
        int localDuration = duration;
        int localsourceid = sourceid;
        int seconds = localDuration / 1000;
        MapleMount givemount = null;
        boolean showEffect = primary;
        if (isMonsterRiding()) {
            //localstatups.clear();
            int ridingLevel = 0;
            switch (sourceid) {
                case Captain.BATTLESHIP:
                    ridingLevel = 1932000;
                    givemount = new MapleMount(applyto, 1932000, sourceid);
                    break;
                case Beginner.SPACESHIP:
                case Noblesse.SPACESHIP:
                    ridingLevel = GameConstants.getMountId(applyfrom, sourceid) + applyto.getSkillLevel(sourceid);
                    givemount = new MapleMount(applyto, GameConstants.getMountId(applyfrom, sourceid) + applyto.getSkillLevel(sourceid), sourceid);
                    break;
                default:
                    ridingLevel = GameConstants.getMountId(applyfrom, sourceid);
                    givemount = new MapleMount(applyto, GameConstants.getMountId(applyfrom, sourceid), sourceid);
                    break;
            }
            localDuration = sourceid;
            localsourceid = ridingLevel;
            //localstatups.add(new Pair<>(MapleBuffStat.ENHANCED_WDEF, (int) wdef));
            //localstatups.add(new Pair<>(MapleBuffStat.ENHANCED_MDEF, (int) mdef));
            localstatups = Collections.singletonList(new Pair<>(MapleBuffStat.MONSTER_RIDING, 0));
        } else if (isSkillMorph()) {
            localstatups.clear();
            switch (sourceid) {
                case Buccaneer.TRANSFORMATION:
                case Viper.SUPER_TRANSFORMATION:
                case WindBreaker.EAGLE_EYE:
                case Striker.TRANSFORMATION:
                    localstatups.add(new Pair<>(MapleBuffStat.SPEED, (int) speed));
                    localstatups.add(new Pair<>(MapleBuffStat.JUMP, (int) jump));
                    localstatups.add(new Pair<>(MapleBuffStat.ENHANCED_WDEF, (int) wdef));
                    localstatups.add(new Pair<>(MapleBuffStat.ENHANCED_MDEF, (int) mdef));
                    break;
            }
            localstatups.add(new Pair<>(MapleBuffStat.MORPH, getMorph(applyto)));
        }
        if (primary) {
            localDuration = alchemistModifyVal(applyfrom, localDuration, false);
        }
        if (localstatups.size() > 0) {
            byte[] buff = null;
            byte[] mbuff = null;
            if (getSummonMovementType() == null) {
                buff = MaplePacketCreator.giveBuff((skill ? sourceid : -sourceid), localDuration, localstatups);
            }
            if (isDash()) {
                buff = MaplePacketCreator.givePirateBuff(statups, sourceid, seconds, isInfusion());
                mbuff = MaplePacketCreator.giveForeignDash(applyto.getId(), sourceid, seconds, localstatups);
            } else if (isInfusion()) {
                buff = MaplePacketCreator.givePirateBuff(statups, sourceid, seconds, isInfusion());
                mbuff = MaplePacketCreator.giveForeignInfusion(applyto.getId(), x, localDuration);
            } else if (isDs()) {
                List<Pair<MapleBuffStat, Integer>> dsstat = Collections.singletonList(new Pair<>(MapleBuffStat.DARKSIGHT, 0));
                mbuff = MaplePacketCreator.giveForeignBuff(applyto.getId(), dsstat);
            } else if (isCombo()) {
                mbuff = MaplePacketCreator.giveForeignBuff(applyto.getId(), statups);
            } else if (isMonsterRiding()) {
                buff = MaplePacketCreator.giveBuff(localsourceid, sourceid, localstatups);
                mbuff = MaplePacketCreator.showMonsterRiding(applyto.getId(), givemount);
                localDuration = duration;
                if (sourceid == Captain.BATTLESHIP) {//hp
                    if (applyto.getBattleshipHp() == 0) {
                        applyto.resetBattleshipHp();
                    }
                }
            } else if (isShadowPartner()) {
                List<Pair<MapleBuffStat, Integer>> stat = Collections.singletonList(new Pair<>(MapleBuffStat.SHADOWPARTNER, 0));
                mbuff = MaplePacketCreator.giveForeignBuff(applyto.getId(), stat);
            } else if (isSoulArrow()) {
                List<Pair<MapleBuffStat, Integer>> stat = Collections.singletonList(new Pair<>(MapleBuffStat.SOULARROW, 0));
                mbuff = MaplePacketCreator.giveForeignBuff(applyto.getId(), stat);
            } else if (isEnrage()) {
                applyto.handleOrbconsume();
            } else if (isMorph()) {
                List<Pair<MapleBuffStat, Integer>> stat = Collections.singletonList(new Pair<>(MapleBuffStat.MORPH, getMorph(applyto)));
                mbuff = MaplePacketCreator.giveForeignBuff(applyto.getId(), stat);
            } else if (isTimeLeap()) {
                for (PlayerCoolDownValueHolder i : applyto.getAllCooldowns()) {
                    if (i.skillId != Viper.TIME_LEAP) {
                        applyto.removeCooldown(i.skillId);
                    }
                }
            }
            long starttime = System.currentTimeMillis();
            CancelEffectAction cancelAction = new CancelEffectAction(applyto, this, starttime);
            ScheduledFuture<?> schedule = TimerManager.getInstance().schedule(cancelAction, localDuration);
            applyto.registerEffect(this, starttime, schedule);

            if (buff != null) {
                applyto.getMap().broadcastMessage(applyto, MaplePacketCreator.showBuffeffect(applyto.getId(), sourceid, applyto.getLevel(), applyto.getSkillLevel(sourceid), 1), false);
                applyto.getClient().announce(buff);
            }
            if (mbuff != null) {
                applyto.getMap().broadcastMessage(applyto, mbuff, false);
            }
            if (sourceid == Captain.BATTLESHIP) {
                applyto.announce(MaplePacketCreator.skillCooldown(5221999, applyto.getBattleshipHp() / 10));
            }
        }
    }

    private int calcHPChange(MapleCharacter applyfrom, boolean primary) {
        int hpchange = 0;
        if (hp != 0) {
            if (!skill) {
                if (primary) {
                    hpchange += alchemistModifyVal(applyfrom, hp, true);
                } else {
                    hpchange += hp;
                }
            } else {
                hpchange += makeHealHP(hp / 100.0, applyfrom.getTotalMagic(), 3, 5);
            }
        }
        if (hpR != 0) {
            hpchange += (int) (applyfrom.getCurrentMaxHp() * hpR);
            applyfrom.checkBerserk();
        }
        if (primary) {
            if (hpCon != 0) {
                hpchange -= hpCon;
            }
        }
        if (isChakra()) {
            hpchange += makeHealHP(getY() / 100.0, applyfrom.getTotalLuk(), 2.3, 3.5);
        } else if (sourceid == GM.HEAL_DISPEL) {
            hpchange += (applyfrom.getMaxHp() - applyfrom.getHp());
        }

        return hpchange;
    }

    private int makeHealHP(double rate, double stat, double lowerfactor, double upperfactor) {
        return (int) ((Math.random() * ((int) (stat * upperfactor * rate) - (int) (stat * lowerfactor * rate) + 1)) + (int) (stat * lowerfactor * rate));
    }

    private int calcMPChange(MapleCharacter applyfrom, boolean primary) {
        int mpchange = 0;
        if (mp != 0) {
            if (primary) {
                mpchange += alchemistModifyVal(applyfrom, mp, true);
            } else {
                mpchange += mp;
            }
        }
        if (mpR != 0) {
            mpchange += (int) (applyfrom.getCurrentMaxMp() * mpR);
        }
        if (primary) {
            if (mpCon != 0) {
                double mod = 1.0;
                boolean isAFpMage = applyfrom.getJob().isA(MapleJob.FP_MAGE);
                boolean isCygnus = applyfrom.getJob().isA(MapleJob.BLAZEWIZARD2);
                if (isAFpMage || isCygnus || applyfrom.getJob().isA(MapleJob.IL_MAGE)) {
                    Skill amp = isAFpMage ? SkillFactory.getSkill(FPMage.ELEMENT_AMPLIFICATION) : (isCygnus ? SkillFactory.getSkill(FlameWizard.ELEMENT_AMPLIFICATION) : SkillFactory.getSkill(ILMage.ELEMENT_AMPLIFICATION));
                    int ampLevel = applyfrom.getSkillLevel(amp);
                    if (ampLevel > 0) {
                        mod = amp.getEffect(ampLevel).getX() / 100.0;
                    }
                }
                mpchange -= mpCon * mod;
                if (applyfrom.getBuffedValue(MapleBuffStat.INFINITY) != null) {
                    mpchange = 0;
                } else if (applyfrom.getBuffedValue(MapleBuffStat.CONCENTRATE) != null) {
                    mpchange -= (int) (mpchange * (applyfrom.getBuffedValue(MapleBuffStat.CONCENTRATE).doubleValue() / 100));
                }
            }
        }
        if (sourceid == GM.HEAL_DISPEL) {
            mpchange += (applyfrom.getMaxMp() - applyfrom.getMp());
        }

        return mpchange;
    }

    private int alchemistModifyVal(MapleCharacter chr, int val, boolean withX) {
        if (!skill && (chr.getJob().isA(MapleJob.HERMIT) || chr.getJob().isA(MapleJob.NIGHTWALKER3))) {
            MapleStatEffect alchemistEffect = getAlchemistEffect(chr);
            if (alchemistEffect != null) {
                return (int) (val * ((withX ? alchemistEffect.getX() : alchemistEffect.getY()) / 100.0));
            }
        }
        return val;
    }

    private MapleStatEffect getAlchemistEffect(MapleCharacter chr) {
        int id = Hermit.ALCHEMIST;
        if (chr.isCygnus()) {
            id = NightWalker.ALCHEMIST;
        }
        int alchemistLevel = chr.getSkillLevel(SkillFactory.getSkill(id));
        return alchemistLevel == 0 ? null : SkillFactory.getSkill(id).getEffect(alchemistLevel);
    }

    private boolean isGmBuff() {
        switch (sourceid) {
            case Beginner.ECHO_OF_HERO:
            case Noblesse.ECHO_OF_HERO:
            case Legend.ECHO_OF_HERO:
            case GM.HEAL_DISPEL:
            case GM.HASTE_SUPER:
            case GM.HOLY_SYMBOL:
            case GM.BLESS:
            case GM.RESURRECTION:
            case GM.HYPER_BODY:
                return true;
            default:
                return false;
        }
    }

    private boolean isMonsterBuff() {
        if (!skill) {
            return false;
        }
        switch (sourceid) {
            case Page.THREATEN:
            case FPWizard.SLOW:
            case ILWizard.SLOW:
            case FPMage.SEAL:
            case ILMage.SEAL:
            case Priest.DOOM:
            case Hermit.SHADOW_WEB:
            case NightLord.NINJA_AMBUSH:
            case Shadower.NINJA_AMBUSH:
            case FlameWizard.SLOW:
            case FlameWizard.SEAL:
            case NightWalker.SHADOW_WEB:
            case DualBlader.MONSTER_BOMB:
                return true;
        }
        return false;
    }

    private boolean isPartyBuff() {
        if (lt == null || rb == null) {
            return false;
        }
        if ((sourceid >= 1211003 && sourceid <= 1211008) || sourceid == Paladin.HOLY_CHARGE_SWORD || sourceid == Paladin.DIVINE_CHARGE_BW || sourceid == SoulMaster.SOUL_CHARGE) {// wk charges have lt and rb set but are neither player nor monster buffs
            return false;
        }
        return true;
    }

    private boolean isHeal() {
        return sourceid == Cleric.HEAL || sourceid == GM.HEAL_DISPEL;
    }

    private boolean isResurrection() {
        return sourceid == Bishop.RESURRECTION || sourceid == GM.RESURRECTION;
    }

    private boolean isTimeLeap() {
        return sourceid == Viper.TIME_LEAP;
    }

    public boolean isDragonBlood() {
        return skill && sourceid == DragonKnight.DRAGON_BLOOD;
    }

    public boolean isBerserk() {
        return skill && sourceid == DarkKnight.BERSERK;
    }

    public boolean isRecovery() {
        return sourceid == Beginner.RECOVERY || sourceid == Noblesse.RECOVERY || sourceid == Legend.RECOVERY;
    }

    private boolean isDs() {
        return skill && (sourceid == Rogue.DARK_SIGHT || sourceid == WindBreaker.WIND_WALK || sourceid == NightWalker.DARK_SIGHT);
    }

    private boolean isCombo() {
        return skill && (sourceid == Crusader.COMBO_ATTACK || sourceid == SoulMaster.COMBO_ATTACK);
    }

    private boolean isEnrage() {
        return skill && sourceid == Hero.ENRAGE;
    }

    public boolean isBeholder() {
        return skill && sourceid == DarkKnight.BEHOLDER;
    }

    private boolean isShadowPartner() {
        return skill && (sourceid == Hermit.SHADOW_PARTNER || sourceid == NightWalker.SHADOW_PARTNER/* || sourceid == DualBlade.�̷�_�̹�¡*/);
    }

    private boolean isChakra() {
        return skill && sourceid == ChiefMaster.CHAKRA;
    }

    public boolean isMonsterRiding() {
        switch (sourceid) {
            case Beginner.MONSTER_RIDER:
            case Noblesse.MONSTER_RIDER:
            case Legend.MONSTER_RIDER:
            case Evan.MONSTER_RIDER:
            case Beginner.SPACESHIP:
            case Noblesse.SPACESHIP:
            case Beginner.YETI_RIDER:
            case Noblesse.YETI_RIDER:
            case Legend.YETI_RIDER:
            case Evan.YETI_RIDER:
            case Beginner.YETI_MOUNT:
            case Noblesse.YETI_MOUNT:
            case Legend.YETI_MOUNT:
            case Beginner.WITCH_S_BROOMSTICK:
            case Noblesse.WITCH_S_BROOMSTICK:
            case Legend.WITCH_S_BROOMSTICK:
            case Evan.WITCH_S_BROOMSTICK:
            case Beginner.CHARGEWOODEN_PONY:
            case Noblesse.CHARGEWOODEN_PONY:
            case Legend.CHARGEWOODEN_PONY:
            case Evan.CHARGEWOODEN_PONY:
            case Beginner.CROCO:
            case Noblesse.CROCO:
            case Legend.CROCO:
            case Evan.CROCO:
            case Beginner.BLACK_SCOOTER:
            case Noblesse.BLACK_SCOOTER:
            case Legend.BLACK_SCOOTER:
            case Evan.BLACK_SCOOTER:
            case Beginner.PINK_SCOOTER:
            case Noblesse.PINK_SCOOTER:
            case Legend.PINK_SCOOTER:
            case Evan.PINK_SCOOTER:
            case Beginner.NIMBUS_CLOUD:
            case Noblesse.NIMBUS_CLOUD:
            case Legend.NIMBUS_CLOUD:
            case Evan.NIMBUS_CLOUD:
            case Beginner.BALROG:
            case Noblesse.BALROG:
            case Legend.BALROG:
            case Evan.BALROG:
            case Beginner.RACING_KART:
            case Noblesse.RACING_KART:
            case Legend.RACING_KART:
            case Evan.RACING_KART:
            case Beginner.ZD_TIGER:
            case Noblesse.ZD_TIGER:
            case Legend.ZD_TIGER:
            case Evan.ZD_TIGER:
            case Beginner.MIST_BALROG:
            case Noblesse.MIST_BALROG:
            case Legend.MIST_BALROG:
            case Evan.MIST_BALROG:
            case Beginner.SHINJO:
            case Noblesse.SHINJO:
            case Legend.SHINJO:
            case Evan.SHINJO:
                return true;
            default:
                return false;
        }
    }

    public boolean isMagicDoor() {
        return skill && sourceid == Priest.MYSTIC_DOOR;
    }

    public boolean isPoison() {
        switch (sourceid) {
            case FPWizard.POISON_BREATH:
            case FPMage.ELEMENT_COMPOSITION:
            case Ranger.INFERNO:
            case Valkyrie.FLAMETHROWER:
                return true;
        }
        return false;
    }

    private boolean isMist() {
        return skill && (sourceid == FPMage.POISON_MIST || sourceid == Shadower.SMOKESCREEN || sourceid == FlameWizard.FLAME_GEAR || sourceid == NightWalker.POISON_BOMB);
    }

    private boolean isSoulArrow() {
        return skill && (sourceid == Hunter.SOUL_ARROW_BOW || sourceid == Crossbowman.SOUL_ARROW_CROSSBOW || sourceid == WindBreaker.SOUL_ARROW);
    }

    private boolean isShadowClaw() {
        return skill && sourceid == NightLord.SHADOW_STARS;
    }

    private boolean isDispel() {
        return skill && (sourceid == Priest.DISPEL || sourceid == GM.HEAL_DISPEL);
    }

    private boolean isHeroWill() {
        if (skill) {
            switch (sourceid) {
                case Hero.HERO_S_WILL:
                case Paladin.HERO_S_WILL:
                case DarkKnight.HERO_S_WILL:
                case FPArchMage.HERO_S_WILL:
                case ILArchMage.HERO_S_WILL:
                case Bishop.HERO_S_WILL:
                case Bowmaster.HERO_S_WILL:
                case Marksman.HERO_S_WILL:
                case NightLord.HERO_S_WILL:
                case Shadower.HERO_S_WILL:
                case Viper.PIRATE_S_RAGE:
                case Aran.HERO_S_WILL:
                case Evan.HERO_S_WILL:
                case DualBlader.HERO_S_WILL:
                    return true;
                default:
                    return false;
            }
        }
        return false;
    }

    private boolean isDash() {
        return skill && (sourceid == Pirate.DASH || sourceid == Striker.DASH || sourceid == Beginner.SPACE_DASH || sourceid == Noblesse.SPACE_DASH || sourceid == Dualmaster.TORNADO_SPIN);
    }

    private boolean isSkillMorph() {
        return skill && (sourceid == Viper.SUPER_TRANSFORMATION || sourceid == Buccaneer.TRANSFORMATION || sourceid == WindBreaker.EAGLE_EYE || sourceid == Striker.TRANSFORMATION);
    }

    private boolean isInfusion() {
        return skill && (sourceid == Viper.SPEED_INFUSION || sourceid == Striker.SPEED_INFUSION);
    }

    private boolean isCygnusFA() {
        return skill && (sourceid == SoulMaster.FINAL_ATTACK || sourceid == WindBreaker.FINAL_ATTACK);
    }

    private boolean isMorph() {
        return morphId > 0;
    }

    private boolean isComboReset() {
        return sourceid == Aran.COMBO_BARRIER || sourceid == Aran.COMBO_DRAIN;
    }

    private int getFatigue() {
        return fatigue;
    }

    private int getMorph() {
        return morphId;
    }

    private int getMorph(MapleCharacter chr) {
        if (morphId % 10 == 0) {
            return morphId + chr.getGender();
        }
        return morphId + 100 * chr.getGender();
    }

    private SummonMovementType getSummonMovementType() {
        if (!skill) {
            return null;
        }
        switch (sourceid) {
            case Ranger.PUPPET:
            case Sniper.PUPPET:
            case WindBreaker.PUPPET:
            case Valkyrie.OCTOPUS:
            case Captain.WRATH_OF_THE_OCTOPI:
            case DualBlader.MIRRORED_TARGET:
                return SummonMovementType.STATIONARY;
            case Ranger.SILVER_HAWK:
            case Sniper.GOLDEN_EAGLE:
            case Priest.SUMMON_DRAGON:
            case Marksman.FROSTPREY:
            case Bowmaster.PHOENIX:
            case Valkyrie.GAVIOTA:
                return SummonMovementType.CIRCLE_FOLLOW;
            case DarkKnight.BEHOLDER:
            case FPArchMage.ELQUINES:
            case ILArchMage.IFRIT:
            case Bishop.BAHAMUT:
            case SoulMaster.SOUL:
            case FlameWizard.FLAME:
            case FlameWizard.IFRIT:
            case WindBreaker.STORM:
            case NightWalker.DARKNESS:
            case Striker.LIGHTNING:
                return SummonMovementType.FOLLOW;
        }
        return null;
    }

    public boolean isSkill() {
        return skill;
    }

    public int getSourceId() {
        return sourceid;
    }

    public boolean makeChanceResult() {
        return prop == 1.0 || Math.random() < prop;
    }

    private static class CancelEffectAction implements Runnable {

        private MapleStatEffect effect;
        private WeakReference<MapleCharacter> target;
        private long startTime;

        public CancelEffectAction(MapleCharacter target, MapleStatEffect effect, long startTime) {
            this.effect = effect;
            this.target = new WeakReference<>(target);
            this.startTime = startTime;
        }

        @Override
        public void run() {
            MapleCharacter realTarget = target.get();
            if (realTarget != null) {
                realTarget.cancelEffect(effect, false, startTime);
            }
        }
    }

    public short getHp() {
        return hp;
    }

    public short getMp() {
        return mp;
    }

    public short getHpCon() {
        return hpCon;
    }

    public short getMpCon() {
        return mpCon;
    }

    public short getMatk() {
        return matk;
    }

    public int getDuration() {
        return duration;
    }

    public List<Pair<MapleBuffStat, Integer>> getStatups() {
        return statups;
    }

    public boolean sameSource(MapleStatEffect effect) {
        return this.sourceid == effect.sourceid && this.skill == effect.skill;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getDamage() {
        return damage;
    }

    public int getAttackCount() {
        return attackCount;
    }

    public int getMobCount() {
        return mobCount;
    }

    public int getFixDamage() {
        return fixdamage;
    }

    public byte getBulletCount() {
        return bulletCount;
    }

    public byte getBulletConsume() {
        return bulletConsume;
    }

    public int getMoneyCon() {
        return moneyCon;
    }

    public int getCooldown() {
        return cooldown;
    }

    public Map<MonsterStatus, Integer> getMonsterStati() {
        return monsterStatus;
    }
}
