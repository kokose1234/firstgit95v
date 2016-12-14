/*
 * Quest ID : 1048
 * Quest Name : ���� ��õ
 * Quest Progress Info : ���� �ͼ����� ���� ���� ��ڴԲ��� ���� ������ ��õ���̴ּ�.
 * Quest End Info : ���� �ͼ����� ���� ���� ��ڴԲ��� ���� ������ ��õ���̴ּ�.
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
        score = 0;
        qm.sendSimple("�ȳ��ϼ���? #b#h0##k��. ���� �� ������ �ϰ� �Ǽ̳׿�. ���� #b#h0##k�Կ��� ���� ������ ��õ�� �帮�� ���ؼ� �� ���� �˾ƾ� �� ���� �־ ������ �帮���� �մϴ�. #bȤ�� �����ý��丮 ���踦 ó�� ���غ��̳���?\r\n #L0#��. ó���̿���.#l\r\n #L1#�� �� �غ��� ������ ���� �� �𸣰ھ��.#l\r\n #L2#������ �ͼ����� �� ���ƿ�.#l\r\n #L3#�̹� ��� �� �� �˰� �־��.#l");
    } else if (status == 1) {
        var str="";
        if (selection == 0) {
            str += "�����ý��丮�� ó�� ���ϼ̱���.";
            score += 1;
        } else if (selection == 1) {
            str += "�����ý��丮�� ��� �� ���� ������ �����ñ���.";
            score += 3;
        } else if (selection == 2) {
            str += "�����ý��丮�� ��� �� ���� ������ �����ñ���.";
            score += 5;
        } else {
            str += "�����ý��丮�� �������� ����ּ̱���.";
            score += 7;
        }
        str += " ������ ������ �����Ͻø� �پ��� ������ �Ͻ� �� ���� �ſ���. ���� �ϵ� ������ ��Ȥ ���� ���� ������� �־��. Ȥ�� #b������ �����ϴٰ� ����� ���� ����ٸ� ��� �Ͻðھ��?\r\n #L0#�� ������ �� �ذ��� �� �־��.#l\r\n #L1#������ ����鼭 ��� �� �ƴѰ���?#l\r\n #L2#������ û�ϰ� ������ �������� ���� Ÿ��...#l";
        qm.sendSimple(str);
    } else if (status == 2) {
        var str="";
        if (selection == 0) {
            str += "����� ���� ������ �ذ��ϴ� ���� �����Ͻó�����.";
            score += 1;
        } else if (selection == 1) {
            str += "�׷���. ����� ���� ����� ���� ���°��� ������.";
            score += 3;
        } else {
            str += "����� ���ϼ��� ��Ź�ϱⰡ ���� �δ㽺���� ��찡 ������.";
            score += 5;
        }
        str += " �̰� ���� ������ �ϴٺ��� ���� ���Ͱ� ��Ÿ����ϴ�. #b���Ͱ� ��Ÿ���ٸ� ��� �Ͻðھ��?\r\n #L0#���͸� ���ؼ� �ָ� ������ �ſ���.#l\r\n #L1#�ָ��� ������� ���� ������ �ſ���.#l\r\n #L2#������ �ʰ� �ο�ſ���.#l";
        qm.sendSimple(str);
    } else if (status == 3) {
        var str="";
        if (selection == 0) {
            score += 1;
            str += "������ ���͸� ���ϰ� ���� ���� �ִ�ϴ�.";
        } else if (selection == 1) {
            str += "���͸� ������ ���� ������ ���� ���� �� ì�⼼��.";
            score += 3;
        } else {
            str += "���͸� ������ ���� ������ ���� ���� �� ì�⼼��.";
            score += 5;
        }
        str += " ���� �� ������ �Ͻð� �˴ϴ�. ������ �ϰ� ���� �پ��� ��ų�� ����Ͻ� �� �־��. ���� ��ų �߿� #b� ��ų�� �����ϼ���?\r\n #L0#ȭ���ϰ� ���ִ� ��ų�� �ְ���.#l\r\n #L1#������ ������ ��ų�� ���ƿ�.#l\r\n #L2#�� ���� �����.#l";
        qm.sendSimple(str);
    } else if (status == 4) {
        if (selection == 0) {
            score += 1;
        } else if (selection == 1) {
            score += 3;
        } else {
            score += 5;
        }
        var job = "������";
        if (score <= 5) {
            job = "������";
            qm.updateQuest(7631, "200");
        } else if (score <= 9) {
            job = "����";
            qm.updateQuest(7631, "100");
        } else if (score <= 13) {
            job = "����";
            qm.updateQuest(7631, "400");
        } else if (score <= 17) {
            job = "�ü�";
            qm.updateQuest(7631, "300");
        } else if (score <= 22) {
            job = "����";
            qm.updateQuest(7631, "500");
        }
        var str = "������ �����ּż� �����մϴ�. #b#h0##k�Կ��� ��õ�� �帱 ������ " + job + " �Դϴ�. ���� �� �� ���� ������� �ƾ����� �մϴ�.";
        //������ : 4
        //���� : 7
        //���� : 8
        //�ü� : 9
        //���� : 11
        qm.sendOk(str);
        qm.forceStartQuest();
        qm.forceCompleteQuest();
        qm.dispose();
    }
}
