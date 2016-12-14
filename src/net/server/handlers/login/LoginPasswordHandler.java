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
package net.server.handlers.login;

import client.MapleClient;
import constants.LoginAutoRegister;
import constants.ServerConstants;
import java.util.Calendar;
import net.MaplePacketHandler;
import net.server.Server;
import net.server.world.World;
import server.TimerManager;
import tools.MaplePacketCreator;
import tools.data.input.SeekableLittleEndianAccessor;
import tools.packet.CLogin;

public final class LoginPasswordHandler implements MaplePacketHandler {

    @Override
    public boolean validateState(MapleClient c) {
        return !c.isLoggedIn();
    }

    @Override
    public final void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        int loginok;
        String login = slea.readMapleAsciiString();
        String pwd = slea.readMapleAsciiString();
        c.setAccountName(login);
        loginok = c.login(login, pwd);
        if (loginok == 5) {
            LoginAutoRegister.registerAccount(login, pwd);
            c.announce(MaplePacketCreator.serverNotice(1, "[자동가입] 자동가입이 완료되었습니다."));
            c.announce(MaplePacketCreator.getLoginFailed(20));
            return;
        }
        if (c.hasBannedIP() || c.hasBannedMac()) {
            c.announce(MaplePacketCreator.getLoginFailed(3));
        }
        Calendar tempban = c.getTempBanCalendar();
        if (tempban != null) {
            if (tempban.getTimeInMillis() > System.currentTimeMillis()) {
                c.announce(MaplePacketCreator.getTempBan(tempban.getTimeInMillis(), c.getGReason()));
                return;
            }
        }
        if (loginok == 3) {
            c.announce(MaplePacketCreator.getPermBan(c.getGReason()));//crashes but idc :D
            return;
        } else if (loginok != 0) {
            c.announce(MaplePacketCreator.getLoginFailed(loginok));
            return;
        }
        if (c.finishLogin() == 0) {
            c.announce(CLogin.CheckPasswordResult(c, (byte) loginok));//why the fk did I do c.getAccountName()?
            Server server = Server.getInstance();
            for (World world : server.getWorlds()) { //��Ƽ����
                c.announce(CLogin.WorldInfomation(world.getId(), ServerConstants.WORLD_NAMES[world.getId()], world.getFlag(), world.getEventMessage(), world.getChannels()));
            }
            c.announce(CLogin.EndOfWorldInfomation());
            c.announce(CLogin.LatestConnectedWorld(0));//too lazy to make a check lol
            c.announce(CLogin.RecommendWorldMessage(server.worldRecommendedList()));
            final MapleClient client = c;
            c.setIdleTask(TimerManager.getInstance().schedule(new Runnable() {
                @Override
                public void run() {
                    client.disconnect(false, false);
                }
            }, 600000));
        } else {
            c.announce(MaplePacketCreator.getLoginFailed(7));
        }
    }
}
