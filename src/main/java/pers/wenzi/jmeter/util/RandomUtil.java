package pers.wenzi.jmeter.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class RandomUtil {

    public static String getRandomString(int length, String range) {
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(range.length());
            sb.append(range.charAt(index));
        }
        return sb.toString();
    }

    public static String getRandomDate(String start, String end) throws ParseException {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = f.parse(start);
        Date endDate = f.parse(end);
        long diff = (endDate.getTime() - startDate.getTime());
        long rand = 1000 + ((long) (new Random().nextDouble() * (diff - 1000)));
        return f.format(startDate.getTime() + rand);


    }

    public static String getRandomTime() {
        SimpleDateFormat f = new SimpleDateFormat("HH:mm:ss");
        Date time = new Date();
        return f.format(time);
    }

}
