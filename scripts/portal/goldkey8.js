/*
Author : ��Ű��
������ ž
*/

function enter(pi) {
    var reac = pi.getPlayer().getMap().getReactorByName("goldkey8");
    if (reac.getState() == 0) {
	reac.hitReactor(pi.getClient());
    }
    return true;
}