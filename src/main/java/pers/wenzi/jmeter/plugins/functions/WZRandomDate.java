package pers.wenzi.jmeter.plugins.functions;

import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.functions.AbstractFunction;
import org.apache.jmeter.functions.InvalidVariableException;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.threads.JMeterVariables;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class WZRandomDate extends AbstractFunction {

    private static final List<String> desc = new LinkedList<>();
    private static final String KEY = "__WZRandomDate";

    private String dateS;
    private String dateE;
    private String format;
    private String varname;

    static {
        desc.add("Start Date, format is yyyy-MM-dd");
        desc.add("End Date, format is yyyy-MM-dd. Current Date is default (optional)");
        desc.add("Format, the format u want to show. yyyy-MM-dd id default (optional)");
        desc.add("Name of variable in which to store the result (optional)");
    }

    private String getRandomDate() throws Exception {
        SimpleDateFormat parseFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat outputFormat = new SimpleDateFormat(format);
        Date date1, date2;
        date1 = parseFormat.parse(dateS);
        date2 = parseFormat.parse(dateE);
        long diff = (date2.getTime() - date1.getTime());
        long rand = 1000 + ((long) (new Random().nextDouble() * (diff - 1000)));
        return outputFormat.format(date1.getTime() + rand);
    }

    public String execute(SampleResult sampleResult, Sampler sampler) {
        String result;
        if (dateS == null || dateS.length() < 1) {
            System.out.println("Start Date is required, and text cannot be empty");
            return null;
        }
        try {
            result = getRandomDate();
        } catch (Exception e) {
            System.out.println("随机日期生成失败! 失败原因: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
        if (varname != null) {
            JMeterVariables vars = getVariables();
            if (vars != null && varname.length() > 0) {
                vars.put(varname, result);
            }
        }
        return result;
    }

    public void setParameters(Collection<CompoundVariable> collection) throws InvalidVariableException {
        checkParameterCount(collection, 1, 4);
        Object[] values = collection.toArray();
        dateS = (values.length > 0)
                ? ((CompoundVariable) values[0]).execute().trim()
                : null;
        if (values.length > 1) {
            dateE = ((CompoundVariable) values[1]).execute().trim();
            if (dateE.length() < 1) {
                dateE = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            }
        }
        if (values.length > 2) {
            format = ((CompoundVariable) values[2]).execute().trim();
            if (format.length() < 1) {
                format = "yyyy-MM-dd";
            }
        }
        varname = (values.length > 3)
                ? ((CompoundVariable) values[3]).execute().trim()
                : null;
    }

    public String getReferenceKey() {
        return KEY;
    }

    public List<String> getArgumentDesc() {
        return desc;
    }

}
