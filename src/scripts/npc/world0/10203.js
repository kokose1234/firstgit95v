/*
	NPC Name: 		��ũ�ε�
	Map(s): 		�����÷ε� : ������ ������
	Description: 		��������
*/

var status = -1;

function start() {
    cm.sendNext("������ ��� ��������� ��ø���� ���� ���� �������� ���忡�� ��븦 ����ϰų� ���� ����� �� Ư���� ����� ����ϰ� �ǳ�. ���� �⵿�°� ȸ������ ���� ������ �پ��� ����� ��Ʈ���� ��̸� ������ �ȴٳ�.");
}

function action(mode, type, selection) {
    status++;
    if (mode != 1){
        if(mode == 0)
           cm.sendNext("������ ü���� ���� �ʹٸ� �ٽ� ���� ã�ƿ���");
        cm.dispose();
        return;
    }
    if (status == 0) {
        cm.sendYesNo("� ������ ü���� ���ڳ�?");
    } else if (status == 1){
	cm.lockUI();
        cm.warp(1020400, 0);
        cm.dispose();
    }
}