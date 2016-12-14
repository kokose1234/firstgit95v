/*
	Fairy Tale Fairy Crackers - Every Town()
*/

var fromWitch = false;

function start() {
    if (cm.getMapId() == 980040000) {
	fromWitch = true;
	cm.sendSimple("#b(움직이는 과자 벽이나 올라오는 초콜렛 원액에 닿으면 밖으로 튕기는구먼..흠..)#k엇!! 넌 누구냐? 이곳에 보물이 있는 것을 어떤 입싼 고양이가 가르쳐줬어? 흠... 뭐 좋아. 일단 이곳에 왔으니 한번 도전해봐. 어떤 난이도로 도전해 볼래?\r\n\r\n#b#L0# #v03994115##l #L1# #v03994116##l #L2# #v03994117##l");
    } else {
	cm.sendSimple("당신은 누군가? 호..혹시 보물을 노리는 첩자는 아니겠지? \r\n #L0#보물이라고요? \r\n #L1##b(몰래)마녀의 탑으로 이동#k#l");
    }
}

function action(mode, type, selection) {
    if (!fromWitch) {
	switch (selection) {
	    case 0:
		cm.sendOk("T...treasures? Who...who said that? Do you think I'd tell you that the Pink Bean Hat made by the witch with Pink Bean that looks like this #v01002971:# can be obtained after clearing normal or hard mode 5 times, and Pink Bean Suit looks like this #v01052202:# can be obtained after getting Pink Bean Hat and go to grave yard through portal in the top right of Witch Tower Entrance? As if!..");
		break;
	    case 1:
		cm.warp(980040000, 0);
		break;
	}
    } else {
	switch (selection) {
	    case 0: {
		var we = cm.getEventManager("WitchTowerEASY");
                if (we == null) { 
cm.sendOk("we == null");      
} else {
var eim = we.newInstance("WitchTowerEASY");
		we.startInstance(eim,cm.getPlayer().getName());
}
		break;
	    }
	    case 1: {
		var aa = cm.getEventManager("WitchTower_Med");
		aa.newInstance(cm.getName()).registerPlayer(cm.getPlayer());
		break;
	    }
	    case 2: {
		var dd = cm.getEventManager("WitchTower_Hard");
		dd.newInstance(cm.getName()).registerPlayer(cm.getPlayer());
		break;
	    }
	}
    }
    cm.dispose();
}