/*
	NPC Name: 		�ﷹ��
	Map(s): 		�����÷ε� : ������ ������
	Description: 		��������
*/
var status = -1;

function start() {
    cm.sendNext("�ü��� ��ø���� ���� ���ϰ� �ִ� �������� ������ �Ŀ����� ���Ÿ� ������ ����ϰ� �Ǹ�, ������ �̿��� ��ɿ��� �ſ� ���մϴ�.");
}

function action(mode, type, selection) {
    status++;
    if (mode != 1){
        if(mode == 0)
           cm.sendNext("�ü��� ü���� ���� �ʹٸ�, ������ �ٽ� ���� �ɾ��ּ���.");
        cm.dispose();
        return;
    }
    if (status == 0) {
        cm.sendYesNo("�ü��� ü���� ���ðھ��?");
    } else if (status == 1){
	cm.lockUI();
        cm.warp(1020300, 0);
        cm.dispose();
  }
}