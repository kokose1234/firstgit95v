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
package client;

import constants.skills.Aran;
import constants.skills.Buccaneer;
import constants.skills.DualBlader;
import constants.skills.Dualmaster;
import constants.skills.Evan;
import constants.skills.Slasher;
import constants.skills.Striker;
import constants.skills.Viper;
import constants.skills.WindBreaker;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import provider.MapleData;
import provider.MapleDataDirectoryEntry;
import provider.MapleDataFileEntry;
import provider.MapleDataProvider;
import provider.MapleDataProviderFactory;
import provider.MapleDataTool;
import server.MapleStatEffect;
import server.life.Element;

public class SkillFactory {

    private static Map<Integer, Skill> skills = new HashMap<>();
    private static MapleDataProvider datasource = MapleDataProviderFactory.getDataProvider(MapleDataProviderFactory.fileInWZPath("Skill.wz"));

    public static Skill getSkill(int id) {
        if (!skills.isEmpty()) {
            return skills.get(Integer.valueOf(id));
        }
        return null;
    }

    public static void loadAllSkills() {
        //System.out.println("Loading Skills:::");
        final MapleDataDirectoryEntry root = datasource.getRoot();

        int skillid;

        for (MapleDataFileEntry topDir : root.getFiles()) { // Loop thru jobs
            if (topDir.getName().length() <= 8) {
                for (MapleData data : datasource.getData(topDir.getName())) { // Loop thru each jobs
                    if (data.getName().equals("skill")) {
                        for (MapleData data2 : data) { // Loop thru each jobs
                            if (data2 != null) {
                                skillid = Integer.parseInt(data2.getName());
                                skills.put(skillid, loadFromData(skillid, data2));
                            }
                        }
                    }
                }
            }
        }
    }

    public static Skill loadFromData(int id, MapleData data) {
        Skill ret = new Skill(id);
        boolean isBuff = false;
        int skillType = MapleDataTool.getInt("skillType", data, -1);
        String elem = MapleDataTool.getString("elemAttr", data, null);
        if (elem != null) {
            ret.element = Element.getFromChar(elem.charAt(0));
        } else {
            ret.element = Element.NEUTRAL;
        }
        MapleData effect = data.getChildByPath("effect");
        if (skillType != -1) {
            if (skillType == 2) {
                isBuff = true;
            }
        } else {
            MapleData action_ = data.getChildByPath("action");
            boolean action = false;
            if (action_ == null) {
                if (data.getChildByPath("prepare/action") != null) {
                    action = true;
                } else {
                    switch (id) {
                        case 5201001:
                        case 5221009:
                            action = true;
                            break;
                    }
                }
            } else {
                action = true;
            }
            ret.action = action;
            MapleData hit = data.getChildByPath("hit");
            MapleData ball = data.getChildByPath("ball");
            isBuff = effect != null && hit == null && ball == null;
            isBuff |= action_ != null && MapleDataTool.getString("0", action_, "").equals("alert2");
            switch (id) {
                case Dualmaster.TORNADO_SPIN:
                case Slasher.OWL_SPIRIT:
                case DualBlader.FINAL_CUT:
                case Buccaneer.TRANSFORMATION:
                case Viper.SUPER_TRANSFORMATION:
                case WindBreaker.EAGLE_EYE:
                case Striker.TRANSFORMATION:
                case Striker.SPARK:
                case Aran.BODY_PRESSURE:
                case Evan.ELEMENTAL_RESET:
                case Evan.MAGIC_SHIELD:
                case Evan.MAGIC_BOOSTER:
                case Evan.MAPLE_WARRIOR:
                case Evan.BLESSING_OF_THE_ONYX:
                    isBuff = true;
                    break;
            }
        }
        for (MapleData level : data.getChildByPath("level")) {
            ret.effects.add(MapleStatEffect.loadSkillEffectFromData(level, id, isBuff));
        }
        ret.animationTime = 0;
        if (effect != null) {
            for (MapleData effectEntry : effect) {
                ret.animationTime += MapleDataTool.getIntConvert("delay", effectEntry, 0);
            }
        }
        ret.masterLevel = MapleDataTool.getInt("masterLevel", data, 0);
        return ret;
    }

    public static String getSkillName(int skillid) {
        MapleData data = MapleDataProviderFactory.getDataProvider(new File(System.getProperty("wzpath") + "/" + "String.wz")).getData("Skill.img");
        StringBuilder skill = new StringBuilder();
        skill.append(String.valueOf(skillid));
        if (skill.length() == 4) {
            skill.delete(0, 4);
            skill.append("000").append(String.valueOf(skillid));
        }
        if (data.getChildByPath(skill.toString()) != null) {
            for (MapleData skilldata : data.getChildByPath(skill.toString()).getChildren()) {
                if (skilldata.getName().equals("name")) {
                    return MapleDataTool.getString(skilldata, null);
                }
            }
        }

        return null;
    }
}
