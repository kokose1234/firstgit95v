/*
	NPC Name: 		하인즈
	Map(s): 		메이플로드 : 선택의 갈림길
	Description: 		직업영상
*/

var status = -1;

function start() {
    cm.sendNext("마법사는 화려한 효과의 속성마법과 파티사냥의 유용한 다양한 보조마법을 가지고 있다네. 게다가 2차 전직이후에 배우게 되는 속성마법은 반대속성의 적에게 치명적인 데미지를 준다네.");
}

function action(mode, type, selection) {
    status++;
    if (mode != 1){
        if(mode == 0)
           cm.sendNext("마법사를 체험해 보고 싶다면, 다시 나를 찾아오게.");
        cm.dispose();
        return;
    }
    if (status == 0) {
        cm.sendYesNo("어때 마법사를 체험해 보겠나?");
    } else if (status == 1){
	cm.lockUI();
        cm.warp(1020200, 0);
        cm.dispose();
    }
}