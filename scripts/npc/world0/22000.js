/* 
	NPC Name: 		��ũ��
	Map(s): 		�����ý��丮 : ��콺�丮 (60000)
	Description: 		���丮�� ���Ϸ����
*/
var status = 0;

var status = 0;

function start() {
 status = -1;
 action(1, 0, 0);
}

function action(mode, type, selection) {
if (mode == -1) {
    cm.dispose();
} else {
    if (status >= 0 && mode == 0) {
        cm.sendOk("��.. ���� �� ������ �� ���� ���ҳ� ����?");
        cm.dispose();
        return;
    }
    if (mode == 1)
        status++;
    else
        status--;
    if (status == 0) {
        cm.sendYesNo("�� �踦 Ÿ�� �� ���� ������� ������ �� ����. #e150�޼�#n�� #b���丮�� ���Ϸ���#k�� ������ �ٰ�. ��� �� �� ���� ������ �ٽô� ���ƿ� �� ����. �? ���丮�� ���Ϸ���� ���� �;�? ���� �ʹٸ� ���� �ٷ� ������ �� �� ����.");
    } else if (status == 1) {
        if (cm.haveItem(4031801)) {
            cm.sendNext("����. �׷� � 150�޼Ҹ� ��... ��? �װ� ���㽺Ʈ�� ��� ��ī������ ��õ���ݾ�? ����, �̷� �� �־����� ���� ���� �߾����. ��ī������ ��õ�� ������ ��� �ִ� ���谡���� ���� ���� ������ �� ��ũ���� ���������� �ʴٰ�!");
        } else {
            cm.sendNext("���� ����������? �׷� �ϴ� 150 �޼Һ��� �ް�..."); 
        } 
    } else if (status == 2) {
        if (cm.haveItem(4031801)) {
            cm.sendNextPrev("��õ���� ������ ������, Ư���� ����� ������ �ٰ�. ���� ���丮�� ���Ϸ���� ����Ѵ�! ��鸱���� �𸣴� �� ���!");
    } else {
        if (cm.haveItem(4031801, 0)) {
            if (cm.getMeso() < 150) {
                cm.sendOk("����? ���� �����鼭 ���ڴٰ� �Ѱž�? �̻��� �༮�̷α�!");
                cm.dispose();
            } else {
                cm.sendNext("������! 150�޼ҵ� �޾Ұڴ�. ���� �ٷ� ����Ѵ�, �� ���!"); 
            }
        } else {
            cm.sendOk("���� �����ϱ⿣ �ٶ��� �ʹ� ���� �δ°�?");
            cm.dispose();
        }
    }
        } else if (status == 3) {
            if (cm.haveItem(4031801)) {
                cm.gainItem(4031801, -1);
                cm.warp(2010000, 0);
                cm.dispose();
            } else {
                cm.gainMeso(-150);
                cm.warp(2010000, 0);
                cm.dispose();
            }
        }
    }
}