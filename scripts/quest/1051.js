/*
 * Quest ID : 1050
 * Quest Name : ������ ���� �ϱ�
 * Quest Progress Info : ������� ������ �� �ֵ��� �����ϾƷ� �̵� ���� �ֽŴٰ� �ϼ̴�.
 * Quest End Info : ���� ������� �����غ���?
 * Start NPC : 9010000
 * 
 * @author T-Sun
 *
 */

var status = -1;

function start(mode, type, selection) {
    if (mode == 1 && type != 1 && type != 11) {
        status++;
    } else {
        if ((type == 1 || type == 11) && mode == 1) {
            status++;
            selection = 1;
        } else if ((type == 1 || type == 11) && mode == 0) {
            status++;
            selection = 0;
        } else {
            qm.dispose();
            return;
        }
    }
    if (status == 0) {
        qm.sendYesNo("������� ������ ������ �Ǽ̱���. ������� �����Ͼƿ��� �����Ͻ� �� ������, Ư���� ������ ���� ������� �����Ͻ� �� �ֵ��� �����ϾƷ� �̵����ѵ帱 �� �ֽ��ϴ�. ���� �̵����� �帱���?");
    }  else if (status == 1) {
        if (selection == 1) {
            qm.warp(101000000);
        }
        qm.forceStartQuest();
        qm.forceCompleteQuest();
        qm.dispose();
    }
}