var status = 0;

function start() {
    status = -1;
    action(1,0,0);
}

function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else {
        qm.dispose();
        return;
    }
    if (status == 0) {
        qm.sendOk("���� �ڵ����� ���� ����Ʈ�Դϴ�.\r\n����Ʈ�ڵ� : " + qm.getQuest() + "\r\n���ǽ��ڵ� : " + qm.getNpc());
        qm.dispose();
    }
}