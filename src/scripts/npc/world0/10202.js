/*
	NPC Name: 		�ָ���� �Ͼ
	Map(s): 		�����÷ε� : ������ ������
	Description: 		��������
*/
var status = -1;

function start() {
    cm.sendNext("����� ��û�� ���ݷ°� ���� ü���� ���� ��������, ������ ���������� �� ������ ��������. �⺻ ������ �ſ� ���� �������� ��� ������� ���鼭 ���� ���� ���� ������ �� ����.");
}

function action(mode, type, selection) {
    status++;
    if (mode != 1){
        if(mode == 0)
           cm.sendNext("���縦 ü���� ���� �ʹٸ�, �ٽ� ���� ã�ƿ���.");
        cm.dispose();
        return;
    }
    if (status == 0) {
        cm.sendYesNo("� ���縦 ü���� ���ڳ�?");
    } else if (status == 1){
	cm.lockUI();
        cm.warp(1020100, 0);
	cm.dispose();
    }
}