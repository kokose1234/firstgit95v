package tools;

import provider.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author 몽키프
 */
public class SkillListExtractor {

    private static final MapleDataProvider item_data = MapleDataProviderFactory.getDataProvider(new File(System.getProperty("wzpath") + "/String.wz"));

    public static void main(String[] args) throws FileNotFoundException, IOException {
        StringBuilder mh = new StringBuilder();
        MapleData pData = item_data.getData("Skill.img");
        for (MapleData ddd : pData.getChildren()) {
            for (MapleData dd : ddd) {
                if (dd.getName().equals("bookName")) {
                    mh.append(MapleDataTool.getString("bookName", ddd)).append("\r\n");
                    System.out.println(MapleDataTool.getString("bookName", ddd));
                }
                if (dd.getName().equals("name")) {
                    mh.append("public static final int ").append(MapleDataTool.getString("name", ddd).replaceAll(": ", "").replaceAll("! ", "").replaceAll("- ", "").replaceAll(" ", "_")).append(" = ").append(ddd.getName()).append(";").append("\r\n");
                    System.out.println(MapleDataTool.getString("name", ddd).toUpperCase());
                }
            }
        }
        FileOutputStream cash_string = null;
        cash_string = new FileOutputStream("frostyWeaponExtractor.txt");
        cash_string.write(mh.toString().getBytes());
        cash_string.close();
    }
}
