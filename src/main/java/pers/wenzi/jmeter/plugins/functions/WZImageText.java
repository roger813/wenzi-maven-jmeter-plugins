package pers.wenzi.jmeter.plugins.functions;

import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.functions.AbstractFunction;
import org.apache.jmeter.functions.InvalidVariableException;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.threads.JMeterVariables;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class WZImageText extends AbstractFunction {

    private static final List<String>   desc    = new LinkedList<>();
    private static final String         KEY     = "__WZImageText";
    private static final String         URL     = "/Users/wenzi/Desktop/";
    private static final String         START   = "2019-11-01";
    private static final String         END     = "2019-11-11";

    private CompoundVariable file;
    private CompoundVariable channel;
    private CompoundVariable imgtype;
    private CompoundVariable varName;

    static {
        desc.add("file, must be absolute path");
        desc.add("text that you want to add");
        desc.add("placeX, the x position of text");
        desc.add("placeY, the y position of text");
        desc.add("Name of variable in which to store the result (optional)");
    }

    private String getRandomString(int length, String range) {
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(range.length());
            sb.append(range.charAt(index));
        }
        return sb.toString();
    }

    private String getRandomTime() {
        SimpleDateFormat f = new SimpleDateFormat("HH:mm:ss");
        Date time = new Date();
        return f.format(time);
    }

    private String getRandomDate(String start, String end) throws ParseException {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = f.parse(start);
        Date endDate = f.parse(end);
        long diff = (endDate.getTime() - startDate.getTime());
        long rand = 1000 + ((long) (new Random().nextDouble() * (diff - 1000)));
        return f.format(startDate.getTime() + rand);
    }

    private String getTaoOrderNo() {
        return "588" + getRandomString(15, "1234567890");
    }

    private String getPddOrderNO() {
        return "191111-589" + getRandomString(12, "1234567890");
    }

    private String ranDate() {
        String date;
        try {
            date = getRandomDate(START, END);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        return date;
    }

    private String ranTime() {
        return getRandomTime();
    }

    private String editImage(String file,
                             String text,
                             Font font,
                             Color color,
                             int placeX, int placeY) {
        String outfile = new StringBuilder()
                .append(URL)
                .append(getRandomString(
                        8, "abcdefghijklmnopqrstuvwxyz0123456789"))
                .append(".jpg").toString();
        try {
            File _file = new File(file);
            Image image = ImageIO.read(_file);
            int imgWidth = image.getWidth(null);
            int imgHeight = image.getHeight(null);
            BufferedImage imgBuff = new BufferedImage(
                    imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = imgBuff.createGraphics();
            g.drawImage(image, 0, 0, imgWidth, imgHeight, null);
            g.setFont(font);
            g.setColor(color);
            g.drawString(text, placeX, placeY);
            FileOutputStream output = new FileOutputStream(outfile);
            ImageIO.write(imgBuff, "jpg", output);
            output.flush();
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return outfile;
    }

    private String editTaoOrderImage() {
        final String inFile = file.execute().trim();
        String outFile;
        Font font1 = new Font("Hei", Font.PLAIN, 38);
        Font font2 = new Font("Hei", Font.PLAIN, 37);
        Color color = new Color(149, 149, 149);
        // 添加订单号
        outFile = editImage(inFile,
                getTaoOrderNo(), font1, color, 310, 998);
        // 添加付款时间
        outFile = editImage(outFile,
                ranDate() + " " + ranTime(),
                font2, color, 310, 1246);
        return outFile;
    }

    private String editTaoExpressImage() {
        final String inFile = file.execute().trim();
        String  outFile;
        Font font1 = new Font("Hei", Font.PLAIN, 38);
        Font font2 = new Font("Hei", Font.PLAIN, 30);
        Color color1 = new Color(128, 128, 128);
        Color color2 = new Color(179, 179, 179);
        // 添加日期
        outFile = editImage(inFile,
                ranDate().substring(5), font1, color1, 56, 1832);
        // 添加时间
        outFile = editImage(outFile,
                ranTime().substring(0, 5), font2, color2, 84, 1878);
        return outFile;
    }

    private String editPddOrderImage() {
        final String inFile = file.execute().trim();
        String outFile;
        Font font1 = new Font("Hei", Font.PLAIN, 40);
        Font font2 = new Font("Hei", Font.PLAIN, 40);
        Color color = new Color(185, 185, 185);
        // 添加订单号
        outFile = editImage(inFile,
                getPddOrderNO(), font1, color, 260, 898);
        // 添加付款时间
        outFile = editImage(outFile,
                ranDate() + " " + ranTime(),
                font2, color, 262, 1104);
        return outFile;
    }

    private String editPddExpressImage() {
        final String inFile = file.execute().trim();
        String outFile;
        Font font = new Font("Hei", Font.PLAIN, 38);
        Color color = new Color(79, 176, 108);
        // 添加签收时间
        outFile = editImage(inFile,
                ranDate() + " " + ranTime(),
                font, color, 180, 446);
        return outFile;
    }

    @Override
    public String execute(SampleResult sampleResult, Sampler sampler) {
        final String _channel = channel.execute().trim();
        final String _imgtype = imgtype.execute().trim();
        String result = null;
        if ("tao".equalsIgnoreCase(_channel)) {
            // 淘宝渠道
            if ("order".equalsIgnoreCase(_imgtype)) {
                // 订单修改
                result = editTaoOrderImage();
            }
            if ("express".equalsIgnoreCase(_imgtype)) {
                // 快递单修改
                result = editTaoExpressImage();
            }
        }
        if ("pdd".equalsIgnoreCase(_channel)) {
            // 拼多多渠道
            if ("order".equalsIgnoreCase(_imgtype)) {
                // 订单修改
                result = editPddOrderImage();
            }
            if ("express".equalsIgnoreCase(_imgtype)) {
                // 快递单修改
                result = editPddExpressImage();
            }
        }
        if (varName != null) {
            JMeterVariables vars = getVariables();
            final String varTrim = varName.execute().trim();
            if (vars != null && varTrim.length() > 0) {
                vars.put(varTrim, result);
            }
        }
        return result;
    }

    @Override
    public void setParameters(Collection<CompoundVariable> collection)
            throws InvalidVariableException {
        checkParameterCount(collection, 3, 4);
        Object[] values = collection.toArray();
        file = (values.length > 0)
                ? ((CompoundVariable) values[0]) : null;
        channel = (values.length > 1)
                ? ((CompoundVariable) values[1]) : null;
        imgtype = (values.length > 2)
                ? ((CompoundVariable) values[2]) : null;
        varName = (values.length > 4)
                ? ((CompoundVariable) values[1]) : null;
    }

    @Override
    public String getReferenceKey() {
        return KEY;
    }

    @Override
    public List<String> getArgumentDesc() {
        return desc;
    }
}
