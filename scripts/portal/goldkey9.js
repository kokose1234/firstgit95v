/*
Author : ��Ű��
������ ž
*/

function enter(pi) {
    var reac = pi.getPlayer().getMap().getReactorByName("goldkey9");
    if (reac.getState() == 0) {
	reac.hitReactor(pi.getClient());
    }
    return true;
}