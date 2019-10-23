package pers.wenzi.jmeter.plugins.functions;

import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.functions.AbstractFunction;
import org.apache.jmeter.functions.InvalidVariableException;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.threads.JMeterVariables;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class WZRandomIDCard extends AbstractFunction {

    private static final List<String> desc = new LinkedList<>();
    private static final String KEY = "__WZRandomIDCard";
    private static final String[] areas = new String[] {
            // 北京
            "110101","110102","110105","110106","110107","110108","110109","110111","110112",
            "110113","110114","110115","110116","110117","110118","110119",
            // 上海
            "310101","310104","310105","310106","310107","310109","310110","310112","310113",
            "310114","310115","310116","310117","310118","310120","310151",
            // 天津
            "120101","120102","120103","120104","120105","120106","120110","120111","120112",
            "120113","120114","120115","120116","120117","120118","120119",
            // 南京
            "320101","320102","320104","320105","320106","320111","320113","320114","320115",
            "320116","320117","320118",
            // 重庆
            "500101","500102","500103","500104","500105","500106","500107","500108","500109",
            "500110","500111","500112","500113","500114","500115","500116","500117","500118",
            "500119","500120","500151","500152","500153","500154","500155","500156",
            // 广州
            "440101","440103","440104","440105","440106","440111","440112","440113","440114",
            "440115","440117","440118",
            // 深圳
            "440301","440303","440304","440305","440306","440307","440308","440309","440310",
            "440311",
            // 石家庄
            "130101","130102","130104","130105","130107","130108","130109","130110","130111",
            "130121","130123","130125","130126","130127","130128","130129","130130","130131",
            "130132","130133","130171","130172","130181","130183","130184",
            // 太原
            "140101","140105","140106","140107","140108","140109","140110","140121","140122",
            "140123","140171","140181",
            // 长沙
            "430101","430102","430103","430104","430105","430111","430112","430121","430181",
            "430182"
    };

    private String area = areas[(int)(Math.random() * areas.length)];
    private String sex  = "";
    private String age  = String.valueOf((int)(Math.random() * (60 - 18 + 1) + 18));
    private String varname;

    static {
        desc.add("Area code, eg. 310104, random is default (optional)");
        desc.add("Age that u want to generate, 18-60 is default (optional)");
        desc.add("Sex that u want to generate, eg. M or F, random is default (optional)");
    }

    private String getRandomDate(int age) {
        long curTime = System.currentTimeMillis();
        long diff = (86400000L * 366L * (long)age)
                + ((long)(Math.random() * 300) * 86400000L);
        long resultTime = curTime - diff;
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        return fmt.format(resultTime);
    }

    private String getGender() {
        int i;
        while (true) {
            i = (int) (Math.random() * 9);
            if ("M".equals(sex)) {
                if (i % 2 != 0) break;
            } else if ("F".equals(sex)) {
                if (i % 2 == 0) break;
            } else {
                break;
            }
        }
        return String.valueOf(i);
    }

    private String getVerCode(String id) {
        char[] pszSrc = id.toCharArray();
        int iS = 0;
        int[] iW = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
        char[] szVerCode = new char[]{'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};
        for (int i = 0; i < 17; i++) {
            iS += (pszSrc[i] - '0') * iW[i];
        }
        int iY = iS % 11;
        return String.valueOf(szVerCode[iY]);
    }

    public String execute(SampleResult sampleResult, Sampler sampler) {
        String date = getRandomDate(Integer.parseInt(age));
        String code = String.valueOf((int)(Math.random() * (99 - 10 + 1) + 10));
        String sexs = getGender();
        String tmpCard = area + date + code + sexs;
        String verCode = getVerCode(tmpCard);
        String result = tmpCard + verCode;
        if (varname != null) {
            JMeterVariables vars = getVariables();
            if (vars != null && varname.length() > 0) {
                vars.put(varname, result);
            }
        }
        return result;
    }

    public void setParameters(Collection<CompoundVariable> collection) throws InvalidVariableException {
        checkParameterCount(collection, 0, 4);
        Object[] values = collection.toArray();
        if (values.length > 0) {
            area = ((CompoundVariable) values[0]).execute().trim();
            if (area.length() < 1) {
                area = areas[(int)(Math.random() * areas.length)];
            }
        }
        if (values.length > 1) {
            age = ((CompoundVariable) values[1]).execute().trim();
            if (age.length() < 1) {
                age = String.valueOf((int)(Math.random() * (60 - 18 + 1) + 18));
            }
        }
        if (values.length > 2) {
            sex = ((CompoundVariable) values[2]).execute().trim();
            if (sex.length() < 1) {
                sex = "";
            }
        }
        if (values.length > 3) {
            varname = ((CompoundVariable) values[3]).execute().trim();
            if (varname.length() < 1) {
                varname = null;
            }
        }
    }

    public String getReferenceKey() {
        return KEY;
    }

    public List<String> getArgumentDesc() {
        return desc;
    }

}
