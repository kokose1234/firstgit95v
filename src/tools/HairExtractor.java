package tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import provider.MapleData;
import provider.MapleDataDirectoryEntry;
import provider.MapleDataEntry;
import provider.MapleDataProvider;
import provider.MapleDataProviderFactory;
import provider.MapleDataTool;

/**
 *
 * @author 몽키프
 */
public class HairExtractor {
    
    private static final MapleDataProvider item_data = MapleDataProviderFactory.getDataProvider(new File(System.getProperty("wzpath") + "/Character.wz/Hair"));
    
    public static void main(String[] args) throws FileNotFoundException, IOException {
        StringBuilder malearray = new StringBuilder();
        StringBuilder femalearray = new StringBuilder();
        boolean male = false;
        for (MapleDataEntry mde : item_data.getRoot().getFiles()) {
            switch (Integer.valueOf(mde.getName().substring(3, 5))) {
                case 30:
                case 33:
                case 36:
                case 40:
                    male = true;
                    break;
                default:
                    male = false;
                    break;
            }
            if (Integer.valueOf(mde.getName().substring(3, 8)) != 30000 && male) {
                malearray.append(",");
            } else if (Integer.valueOf(mde.getName().substring(3, 8)) != 31000 && !male) {
                femalearray.append(",");
            }
            if (male) {
                malearray.append(mde.getName().substring(3, 8));
            } else {
                femalearray.append(mde.getName().substring(3, 8));
            }
        }
        
        FileOutputStream malestring = null;
        FileOutputStream femalestring = null;
        malestring = new FileOutputStream("malestring.txt");
        femalestring = new FileOutputStream("femalestring.txt");
        malestring.write(malearray.toString().getBytes());
        femalestring.write(femalearray.toString().getBytes());
        malestring.close();
        femalestring.close();

    }
}
