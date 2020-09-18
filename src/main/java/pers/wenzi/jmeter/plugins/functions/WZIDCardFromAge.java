package pers.wenzi.jmeter.plugins.functions;

import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.functions.AbstractFunction;
import org.apache.jmeter.functions.InvalidVariableException;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.threads.JMeterVariables;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * 随机生成身份证号码
 */
public class WZIDCardFromAge extends AbstractFunction {

    private static final List<String> desc = new LinkedList<>();
    private static final String KEY = "__WZIDCardFromAge";
    private static final String[] areas = new String[] {
            // 北京
            "110101","110102","110105","110106","110107","110108","110109","110111","110112","110113","110114", "110115", "110116","110117","110118","110119",
            // 上海
            "310101","310104","310105","310106","310107","310109","310110","310112","310113","310114","310115", "310116", "310117","310118","310120","310151",
            // 广州
            "440103","440104","440105","440106","440111","440112","440113","440114","440115","440117","440118",
            // 深圳
            "440301","440303","440304","440305","440306","440307","440308","440309","440310","440311",
            // 天津
            "120101","120102","120103","120104","120105","120106","120110","120111","120112","120113","120114", "120115", "120116","120117","120118","120119",
            // 南京
            "320101","320102","320104","320105","320106","320111","320113","320114","320115","320116","320117","320118",
            // 重庆
            "500101","500102","500103","500104","500105","500106","500107","500108","500109","500110","500111","500112", "500113","500114","500115","500116","500117","500118","500119","500120","500151","500152","500153","500154", "500155","500156",
            // 石家庄
            "130101","130102","130104","130105","130107","130108","130109","130110","130111","130121","130123","130125", "130126","130127","130128","130129","130130","130131","130132","130133","130171","130172","130181","130183", "130184",
            // 太原
            "140101","140105","140106","140107","140108","140109","140110","140121","140122","140123","140171","140181",
            // 呼和浩特
            "150102","150103","150104","150105","150121","150122","150123","150124","150125","150171","150172",
            // 长沙
            "430101","430102","430103","430104","430105","430111","430112","430121","430181","430182",
            // 沈阳
            "210102","210103","210104","210105","210106","210112","210114","210115","210123","210124","210181","210113","210111",
            // 长春
            "220102","220103","220104","220105","220106","220112","220113","220122","220182","220183","220171","220172","220173","220174",
            // 哈尔滨
            "230102","230103","230104","230108","230109","230110","230111","230112","230113","230123","230124","230125","230126","230127","230128","230129","230183","230184",
            // 伊春
            "230702","230703","230704","230705","230706","230707","230708","230709","230710","230711","230712","230713", "230714","230715","230716","230722","230781",
            // 杭州
            "330102","330103","330104","330105","330106","330108","330109","330110","330111","330112","330122","330127","330182",
            // 合肥
            "340102","340103","340104","340111","340121","340122","340123","340124","340181","340171","340172","340173",
            // 厦门
            "350203","350205","350206","350211","350212","350213",
            // 南昌
            "360102","360103","360105","360112","360121","360123","360124","360104","360111",
            // 青岛
            "370202","370203","370211","370212","370213","370214","370215","370281","370283","370285","370271",
            // 郑州
            "410102","410103","410105","410106","410108","410122","410181","410182","410183","410184","410185","410104","410171","410172","410173",
            // 佛山
            "440604","440605","440606","440607","440608",
            // 南宁
            "450102","450103","450105","450108","450109","450110","450123","450124","450125","450126","450127","450107",
            // 三亚
            "460202","460203","460204","460205",
            // 成都
            "510104","510105","510106","510107","510108","510114","510115","510116","510117","510121","510129","510131","510132","510182","510183","510184","510185","510112","510113","510181",
            // 遵义
            "520303","520304","520322","520323","520324","520327","520328","520329","520330","520381","520382","520302","520325","520326",
            // 昆明
            "530102","530103","530111","530112","530113","530114","530115","530124","530125","530127","530181","530126","530128","530129"
    };

    private String sex;
    private String minAge;
    private String maxAge;
    private String varname;

    static {
        desc.add("Sex [M/F]. Default random (optional)");
        desc.add("Min age. Default 18 (optional)");
        desc.add("Max age. Default 60 (optional)");
        desc.add("Name of variable in which to store the result (optional)");
    }

    private String randomSex(final String sex) {
        int i;
        while (true) {
            i = new Random().nextInt(10);
            if ("M".equalsIgnoreCase(sex)) {
                if (i%2 == 1) break;
            } else if ("F".equalsIgnoreCase(sex)) {
                if (i%2 == 0) break;
            } else {
                break;
            }
        }
        return String.valueOf(i);
    }

    private String randomBirth(final int minAge, final int maxAge) {
        final SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        final Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        long min, max;
        calendar.add(Calendar.YEAR, -minAge);
        min = calendar.getTime().getTime();
        calendar.add(Calendar.YEAR, -(maxAge - minAge + 1));
        calendar.add(Calendar.DATE, 1);
        max = calendar.getTime().getTime();
        return fmt.format(
            new Date(((long) (new Random().nextDouble() * (max - min))) + min));
    }

    private String getVerCode(final String suff) {
        final char[] pszSrc = suff.toCharArray();
        int iS = 0;
        final int[] iW = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
        final char[] szVerCode = new char[]{'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};
        for (int i = 0; i < 17; i++) {
            iS += (pszSrc[i] - '0') * iW[i];
        }
        final int iY = iS % 11;
        return String.valueOf(szVerCode[iY]);
    }

    public String execute(final SampleResult sampleResult, final Sampler sampler) {
        String result;
        final String area = areas[
                new Random().nextInt(areas.length - 1)];
        final int _minAge = (minAge.length() < 1)
                ? 18 : Integer.parseInt(minAge);
        final int _maxAge = (maxAge.length() < 1)
                ? 60 : Integer.parseInt(maxAge);
        final String birth = randomBirth(_minAge, _maxAge);
        final String order = String.valueOf(
                (new Random().nextInt(90) + 10));
        final String _sex = (sex.length() < 1)
                ? randomSex(null) : randomSex(sex);
        result = area + birth + order + _sex;
        result = result + getVerCode(result);
        if (varname != null) {
            final JMeterVariables vars = getVariables();
            if (vars != null && varname.length() > 0) {
                vars.put(varname, result);
            }
        }
        // System.out.println("{" +
        //         "area: "    + area      + ", " +
        //         "birth: "   + birth    + ", " +
        //         "order: "   + order     + ", " +
        //         "sex: "     + _sex      + ", " +
        //         "result: "  + result    + "}");
        return result;
    }

    public void setParameters(final Collection<CompoundVariable> collection)
            throws InvalidVariableException {
        checkParameterCount(collection, 0, 4);
        final Object[] values = collection.toArray();
        if (values.length > 0)
            sex = ((CompoundVariable) values[0]).execute().trim();
        if (values.length > 1)
            minAge = ((CompoundVariable) values[1]).execute().trim();
        if (values.length > 2)
            maxAge = ((CompoundVariable) values[2]).execute().trim();
        if (values.length > 3)
            varname = ((CompoundVariable) values[3]).execute().trim();
    }

    public String getReferenceKey() {
        return KEY;
    }

    public List<String> getArgumentDesc() {
        return desc;
    }

}
