/*
	NPC Name: 		카이린
	Map(s): 		메이플로드 : 선태의 갈림길
	Description: 		직업영상
*/

var status = -1;

function start() {
    cm.sendNext("해적은 뛰어난 민첩성이나 힘을 바탕으로 적을 향해 백날백중의 총을 발사하거나 적을 한순간에 제압하는 체술을 사용하지. 건슬링거는 속성별 총알을 이용해 효율적으로 공격하거나 배에 탑승해 더 강한 공격을 할 수 있으며, 인파이터는 변신을 통해 강한 체술을 발휘하지.");
}

function action(mode, type, selection) {
    status++;
    if (mode != 1){
        if(mode == 0)
           cm.sendNext("해적을 체험해 보고 싶다면 다시 나를 찾아오게");
        cm.dispose();
        return;
    }
    if (status == 0) {
        cm.sendYesNo("어때 해적을 체험해 보겠나?");
    } else if (status == 1){
	cm.lockUI();
        cm.warp(1020500, 0);
        cm.dispose();
    }
}