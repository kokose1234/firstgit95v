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

import java.util.ArrayList;
import java.util.List;
import server.MapleStatEffect;
import server.life.Element;

public class Skill {

    public int id;
    public List<MapleStatEffect> effects = new ArrayList<>();
    public Element element;
    public int animationTime;
    public int masterLevel;
    public boolean action;

    public Skill(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public MapleStatEffect getEffect(int level) {
        return effects.get(level - 1);
    }

    public int getMaxLevel() {
        return effects.size();
    }

    public int getMasterLevel() {
        return masterLevel;
    }

    public boolean isFourthJob() {
        if (id / 10000 >= 430 && id / 10000 <= 434) { //db skill
            return ((id / 10000) % 10) == 4 || getMasterLevel() > 0;
        }
        if ((id / 10000) == 2312) { //all 10 skills.
            return true;
        }
        if (id / 10000 >= 2212 && id / 10000 < 3000) { //evan skill
            return ((id / 10000) % 10) >= 7;
        }
        return (id / 10000 % 10 == 2) && (id < 90000000) && (!isBeginnerSkill());
    }

    public Element getElement() {
        return element;
    }

    public int getAnimationTime() {
        return animationTime;
    }

    public boolean isBeginnerSkill() {
        return id % 10000000 < 10000;
    }

    public boolean getAction() {
        return action;
    }
}
