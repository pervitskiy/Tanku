package Utils;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static BufferedImage resize(BufferedImage image , int width , int height) {
        BufferedImage newImage = new BufferedImage(width , height , BufferedImage.TYPE_INT_ARGB);
        //перерисовали старовую картинку на новую, т.е увиличили картинку или уменьшили
        newImage.getGraphics().drawImage(image , 0 , 0 , width , height , null);
        return newImage;
    }

    public static Integer[][] levelParser(String fileName) {
        Integer[][] result = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)))) {
            String line;
            List<Integer[]> lvlLines = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                lvlLines.add(strToIntArr(line.trim().split(" ")));
            }
            result = new Integer[lvlLines.size()][lvlLines.get(0).length];
            for (int i = 0; i < lvlLines.size(); i++)
                result[i] = lvlLines.get(i);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static final Integer[] strToIntArr(String[] strArr) {
        Integer[] result = new Integer[strArr.length];
        for (int i = 0; i < strArr.length; i++) {

            result[i] = Integer.parseInt(strArr[i]);

        }
        return result;
    }

}
