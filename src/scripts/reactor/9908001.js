/* 
 *  Witch Tower
 *  Golden Key
 */

function act() {
var eim = rm.getPlayer().getEventInstance();
    if (eim != null) {
	var keys = eim.getProperty("goldkey");
	keys++
	eim.setProperty("goldkey", keys);
	rm.updateWitchtowerScore(keys);
    }
}