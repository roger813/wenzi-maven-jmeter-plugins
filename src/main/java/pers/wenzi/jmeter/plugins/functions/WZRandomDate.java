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

public class WZRandomDate extends AbstractFunction {

    private static final List<String> desc = new LinkedList<>();
    private static final String KEY = "__WZRandomDate";

    private CompoundVariable age;
    private CompoundVariable varName;

    static {
        desc.add("Number of Age, if null, return a date range in 18-60 age");
        desc.add("Name of variable in which to store the result (optional)");
    }

    public static String getRandomDate(int age) {
        long curTime = System.currentTimeMillis();
        long diff = (86400000L * 366L * (long)age)
                + ((long)(Math.random() * 300) * 86400000L);
        long resultTime = curTime - diff;
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        String date = fmt.format(resultTime);
        return date;
    }

    public String execute(SampleResult sampleResult, Sampler sampler) throws InvalidVariableException {
        String date = (age == null)
                ? getRandomDate((int)(Math.random() * (60 - 18 + 1) + 18))
                : getRandomDate(Integer.parseInt(age.execute().trim()));
//        System.out.println(date);
        if (varName != null) {
            JMeterVariables vars = getVariables();
            final String varTrim = varName.execute().trim();
            if (vars != null && varTrim.length() > 0) {
                vars.put(varTrim, date);
            }
        }
        return date;
    }

    public void setParameters(Collection<CompoundVariable> collection) throws InvalidVariableException {
        checkParameterCount(collection, 0, 2);
        Object[] values = collection.toArray();

        age = (values.length > 0)
                ? ((CompoundVariable) values[0]) : null;
        varName = (values.length > 1)
                ? ((CompoundVariable) values[1]) : null;
    }

    public String getReferenceKey() {
        return KEY;
    }

    public List<String> getArgumentDesc() {
        return desc;
    }

}
