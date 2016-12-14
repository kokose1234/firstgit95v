/*
 * Quest ID : 1050
 * Quest Name : 마법사 전직 하기
 * Quest Progress Info : 마법사로 전직할 수 있도록 엘리니아로 이동 시켜 주신다고 하셨다.
 * Quest End Info : 이제 마법사로 전직해볼까?
 * Start NPC : 9010000
 * 
 * @author T-Sun
 *
 */

var status = -1;

function start(mode, type, selection) {
qm.getPlayer().dropMessage(mode + " " + type + " " + selection); 
    if (mode == 1 && type == 0) {
        status++;
    } else {
        if (type == 2 && mode == 1) {
            status++;
            selection = 1;
        } else if (type == 2 && mode == 0) {
            status++;
            selection = 0;
        } else {
            qm.dispose();
            return;
        }
    }
    if (status == 0) {
        qm.sendYesNo("마법사로 전직할 레벨이 되셨군요. 마법사는 엘리니아에서 전직하실 수 있으며, 특별히 지금은 제가 마법사로 전직하실 수 있도록 엘리니아로 이동시켜드릴 수 있습니다. 지금 이동시켜 드릴까요?");
    }  else if (status == 1) {
        if (selection == 1) {
            qm.warp(101000000);
        }
	qm.forceStartQuest();
        qm.forceCompleteQuest();
        qm.dispose();
    }
}
