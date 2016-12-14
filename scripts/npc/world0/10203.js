/*
	NPC Name: 		다크로드
	Map(s): 		메이플로드 : 선택의 갈림길
	Description: 		직업영상
*/

var status = -1;

function start() {
    cm.sendNext("도적은 운과 어느정도의 민첩성과 힘을 가진 직업으로 전장에서 상대를 기습하거나 몸을 숨기는 등 특수한 기술을 사용하게 되네. 높은 기동력과 회피율을 가진 도적은 다양한 기술로 컨트롤을 재미를 느끼게 된다네.");
}

function action(mode, type, selection) {
    status++;
    if (mode != 1){
        if(mode == 0)
           cm.sendNext("도적을 체험해 보고 싶다면 다시 나를 찾아오게");
        cm.dispose();
        return;
    }
    if (status == 0) {
        cm.sendYesNo("어때 도적을 체험해 보겠나?");
    } else if (status == 1){
	cm.lockUI();
        cm.warp(1020400, 0);
        cm.dispose();
    }
}