/*
	NPC Name: 		헬레나
	Map(s): 		메이플로드 : 선택의 갈림길
	Description: 		직업영상
*/
var status = -1;

function start() {
    cm.sendNext("궁수는 민첩성과 힘을 지니고 있는 직업으로 전장의 후열에서 원거리 공격을 담당하게 되며, 지형을 이용한 사냥에도 매우 강합니다.");
}

function action(mode, type, selection) {
    status++;
    if (mode != 1){
        if(mode == 0)
           cm.sendNext("궁수를 체험해 보고 싶다면, 저에게 다시 말을 걸어주세요.");
        cm.dispose();
        return;
    }
    if (status == 0) {
        cm.sendYesNo("궁수를 체험해 보시겠어요?");
    } else if (status == 1){
	cm.lockUI();
        cm.warp(1020300, 0);
        cm.dispose();
  }
}