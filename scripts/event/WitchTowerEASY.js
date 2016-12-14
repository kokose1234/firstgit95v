load('nashorn:mozilla_compat.js');
importPackage(Packages.tools);
importPackage(Packages.tools.packet);

var returnMap;
var players;
var eim;

function init() {
    em.setProperty("goldkey","0");
}

function setup(player) {
var eim = em.newInstance("WitchTowerEASY");
    em.setProperty("goldkey","0");
    eim.startEventTimer(180000);
return eim;
}

function scheduledTimeout(eim) {
    eim.disposeIfPlayerBelow(100, 980040000);
    em.setProperty("goldkey","0");
}

function changedMap(eim, player, mapid) {
    switch (mapid) {
	case 980041000: // Stage 1
        case 980041001:
        case 980041002:
        case 980041003:
        case 980041004:
        case 980041005:
        case 980041006:
        case 980041007:
        case 980041008:
        case 980041009:
        case 980041010:
        case 980041100:
        case 980041101:
        case 980041102:
        case 980041103:
        case 980041104:
        case 980041105:
        case 980041106:
        case 980041107:
        case 980041108:
        case 980041109:
        case 980041110:
	    return;
    }
player.changeMap(returnMap, returnMap.getPortal(0));
    eim.unregisterPlayer(player);
    em.cancel();
    em.disposeInstance("WitchTowerEASY");
    em.setProperty("goldkey","0");
}

function playerEntry(eim, player) {
    returnMap = em.getChannelServer().getMapFactory().getMap(980040000);
    for (var i = 980041000; i <= 980041009; i++) {
	var map = em.getChannelServer().getMapFactory().getMap(i);
	if (map.getCharacters().size() == 0) {
            map.resetReactors();
	    player.changeMap(map, map.getPortal(0));
	    player.getClient().getSession().write(WitchtowerPacket.WitchtowerScoreUpdate(eim.getProperty("goldkey")));
            em.schedule("timeOut", 10000);
	    break;
	}
    }
}

function playerExit(eim, player) {
player.changeMap(returnMap, returnMap.getPortal(0));
    eim.unregisterPlayer(player);
    em.cancel();
    em.disposeInstance("WitchTowerEASY");
    em.setProperty("goldkey","0");
}

function timeOut() {
    var player = eim.getPlayers().get(0);
player.changeMap(returnMap, returnMap.getPortal(0));
    eim.unregisterPlayer(player);
    em.cancel();
    em.disposeInstance("WitchTowerEASY");
    em.setProperty("goldkey","0");
}

function playerDisconnected(eim, player) {
    em.setProperty("goldkey","0");
    player.getMap().removePlayer(player);
    player.setMap(returnMap);
    eim.unregisterPlayer(player);
    em.cancel();
    em.disposeInstance("WitchTowerEASY");
    eim.dispose();
}

function clear(eim) {
    em.setProperty("goldkey","0");
    var player = eim.getPlayers().get(0);
    player.changeMap(returnMap, returnMap.getPortal(0));
    eim.unregisterPlayer(player);
    em.cancel();
    em.disposeInstance("WitchTowerEASY");
    eim.dispose();
}

function cancelSchedule() {
}

function dispose() {
}
