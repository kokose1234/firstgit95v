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
package client.status;

public enum MonsterStatus {
    NEUTRALISE(0x02),
    WATK(0x1),
    WDEF(0x2),
    MATK(0x4),
    MDEF(0x8),
    ACC(0x10),
    AVOID(0x20),
    SPEED(0x40),
    STUN(0x80),
    FREEZE(0x100),
    POISON(0x200),
    SEAL(0x400),
    SHOWDOWN(0x800),
    WEAPON_ATTACK_UP(0x1000),
    WEAPON_DEFENSE_UP(0x2000),
    MAGIC_ATTACK_UP(0x4000),
    MAGIC_DEFENSE_UP(0x8000),
    DOOM(0x10000),
    SHADOW_WEB(0x20000),
    WEAPON_IMMUNITY(0x40000),
    MAGIC_IMMUNITY(0x80000),
    DAMAGE_IMMUNITY(0x200000),
    NINJA_AMBUSH(0x400000),
    VENOMOUS_WEAPON(0x1000000),
    DARKNESS(0x2000000),
    INERTMOB(0x10000000),
    WEAPON_REFLECT(0x20000000),
    MAGIC_REFLECT(0x40000000),
    summonbag(0x80000000),
    RISE_BY_TOSS(0x1, true),
    IMPRINT(0x4, true),
    MONSTER_BOMB(0x8, true);
    private final long i;
    private final boolean first;

    private MonsterStatus(long i) {
        this.i = i;
        this.first = false;
    }

    private MonsterStatus(long i, boolean first) {
        this.i = i;
        this.first = first;
    }

    public boolean isFirst() {
        return first;
    }

    public long getValue() {
        return i;
    }
}
