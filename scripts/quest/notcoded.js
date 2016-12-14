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
        qm.sendOk("아직 코딩되지 않은 퀘스트입니다.\r\n퀘스트코드 : " + qm.getQuest() + "\r\n엔피시코드 : " + qm.getNpc());
        qm.dispose();
    }
}