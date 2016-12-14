/*
	NPC Name: 		주먹펴고 일어서
	Map(s): 		메이플로드 : 선택의 갈림길
	Description: 		직업영상
*/
var status = -1;

function start() {
    cm.sendNext("전사는 엄청난 공격력과 강한 체력을 지닌 직업으로, 전장의 최전선에서 그 진가를 발휘하지. 기본 공격이 매우 강한 직업으로 고급 기술들을 배우면서 더욱 강한 힘을 발휘할 수 있지.");
}

function action(mode, type, selection) {
    status++;
    if (mode != 1){
        if(mode == 0)
           cm.sendNext("전사를 체험해 보고 싶다면, 다시 나를 찾아오게.");
        cm.dispose();
        return;
    }
    if (status == 0) {
        cm.sendYesNo("어때 전사를 체험해 보겠나?");
    } else if (status == 1){
	cm.lockUI();
        cm.warp(1020100, 0);
	cm.dispose();
    }
}