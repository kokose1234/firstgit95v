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
package net;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import static net.SendOpcodes.values;

public enum RecvOpcode implements WritableIntValueHolder {
    LOGIN_PASSWORD,
    CHARLIST_REQUEST,
    CHAR_SELECT,
    PLAYER_LOGGEDIN,    
    CHECK_CHAR_NAME,
    CREATE_CHAR,
    DELETE_CHAR,
    PONG,
    CHECK_ID_CODE,
    
    RELOG,
    CHANGE_MAP,
    CHANGE_CHANNEL,
    ENTER_CASHSHOP,
    MOVE_PLAYER,
    CANCEL_CHAIR,
    USE_CHAIR,
    CLOSE_RANGE_ATTACK,
    RANGED_ATTACK,
    MAGIC_ATTACK,
    TOUCH_MONSTER_ATTACK,
    TAKE_DAMAGE,
    GENERAL_CHAT,
    CLOSE_CHALKBOARD,
    FACE_EXPRESSION,
    USE_ITEMEFFECT,
    USE_DEATHITEM,
    MONSTER_BOOK_COVER,
    NPC_TALK,
    REMOTE_STORE,
    NPC_TALK_MORE,
    NPC_SHOP,
    STORAGE,
    HIRED_MERCHANT_REQUEST,
    FREDRICK_ACTION,
    DUEY_ACTION,
    ADMIN_SHOP,//oh lol
    ITEM_SORT,
    ITEM_SORT2,
    ITEM_MOVE,
    USE_ITEM,
    CANCEL_ITEM_EFFECT,
    USE_SUMMON_BAG,
    PET_FOOD,
    USE_MOUNT_FOOD,
    SCRIPTED_ITEM,
    USE_CASH_ITEM,
    USE_CATCH_ITEM,
    USE_SKILL_BOOK,
    USE_TELEPORT_ROCK,
    USE_RETURN_SCROLL,
    USE_UPGRADE_SCROLL,
    DISTRIBUTE_AP,
    AUTO_DISTRIBUTE_AP,
    HEAL_OVER_TIME,
    DISTRIBUTE_SP,
    SPECIAL_MOVE,
    CANCEL_BUFF,
    SKILL_EFFECT,
    MESO_DROP,
    GIVE_FAME,
    CHAR_INFO_REQUEST,
    SPAWN_PET,
    CANCEL_DEBUFF,
    CHANGE_MAP_SPECIAL,
    USE_INNER_PORTAL,
    TROCK_ADD_MAP,
    REPORT,
    QUEST_ACTION,
    //lolno
    SKILL_MACRO,
    SPOUSE_CHAT,
    USE_ITEM_REWARD,
    MAKER_SKILL,
    USE_REMOTE,
    ADMIN_CHAT,
    PARTYCHAT,
    WHISPER,
    MESSENGER,
    PLAYER_INTERACTION,
    PARTY_OPERATION,
    DENY_PARTY_REQUEST,
    GUILD_OPERATION,
    DENY_GUILD_REQUEST,
    ADMIN_COMMAND,
    ADMIN_LOG,
    BUDDYLIST_MODIFY,
    NOTE_ACTION,
    USE_DOOR,
    CHANGE_KEYMAP,
    RPS_ACTION,
    RING_ACTION,
    WEDDING_ACTION,
    OPEN_FAMILY,
    ADD_FAMILY,
    ACCEPT_FAMILY,
    USE_FAMILY,
    ALLIANCE_OPERATION,
    BBS_OPERATION,
    ENTER_MTS,
    USE_SOLOMON_ITEM,
    USE_GACHA_EXPE,
    CLICK_GUIDE,
    ARAN_COMBO_COUNTER,
    MOVE_PET,
    PET_CHAT,
    PET_COMMAND,
    PET_LOOT,
    PET_AUTO_POT,
    PET_EXCLUDE_ITEMS,
    MOVE_SUMMON,
    SUMMON_ATTACK,
    DAMAGE_SUMMON,
    MOVE_DRAGON,
    BEHOLDER,
    MOVE_LIFE,
    AUTO_AGGRO,
    MOB_DAMAGE_MOB_FRIENDLY,
    MONSTER_BOMB,
    MOB_DAMAGE_MOB,
    NPC_ACTION,
    ITEM_PICKUP,
    DAMAGE_REACTOR,
    TOUCHING_REACTOR,
    TEMP_SKILL,
    MAPLETVFFE,//Don't know
    SNOWBALL,
    LEFT_KNOCKBACK,
    COCONUT,
    MATCH_TABLE,//Would be cool if I ever get it to work :)
    MONSTER_CARNIVAL,
    PARTY_SEARCH_REGISTER,
    PARTY_SEARCH_START,
    PLAYER_UPDATE,
    CHECK_CASH,
    CASHSHOP_OPERATION,
    COUPON_CODE,
    OPEN_ITEMUI,
    CLOSE_ITEMUI,
    USE_ITEMUI,
    MTS_OPERATION,
    USE_MAPLELIFEE,
    SHOW_BOMB,
    USE_HAMMER;
    private int code = -2;

    @Override
    public int getValue() {
        return code;
    }

    @Override
    public void setValue(int value) {
        code = value;
    }
    
    static {
        reloadValues();
    }

    public static Properties getDefaultProperties() throws FileNotFoundException, IOException {
        Properties props = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream("RecvOpcode.properties")) {
            props.load(fileInputStream);
        }
        return props;
    }

    public static final void reloadValues() {
        try {
            ExternalCodeTableGetter.populateValues(getDefaultProperties(), values());
        } catch (IOException e) {
            throw new RuntimeException("Failed to load sendops", e);
        }
    }   
    
}
