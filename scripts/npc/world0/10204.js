/*
	NPC Name: 		ī�̸�
	Map(s): 		�����÷ε� : ������ ������
	Description: 		��������
*/

var status = -1;

function start() {
    cm.sendNext("������ �پ ��ø���̳� ���� �������� ���� ���� �鳯������ ���� �߻��ϰų� ���� �Ѽ����� �����ϴ� ü���� �������. �ǽ����Ŵ� �Ӽ��� �Ѿ��� �̿��� ȿ�������� �����ϰų� �迡 ž���� �� ���� ������ �� �� ������, �������ʹ� ������ ���� ���� ü���� ��������.");
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
        cm.warp(1020500, 0);
        cm.dispose();
    }
}