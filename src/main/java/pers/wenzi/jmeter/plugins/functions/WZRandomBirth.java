package pers.wenzi.jmeter.plugins.functions;

import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.functions.AbstractFunction;
import org.apache.jmeter.functions.InvalidVariableException;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.threads.JMeterVariables;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class WZRandomBirth extends AbstractFunction {

    private static final List<String> desc = new LinkedList<>();
    private static final String KEY = "__WZRandomBirth";

    private String dateS    = null;
    private String dateE    = null;
    private String format   = null;
    private String varname  = null;

    static {
        desc.add("Start Date, format: yyyy-MM-dd. required");
        desc.add("End Date, format: yyyy-MM-dd. Current time is default (optional)");
        desc.add("Format, format u want to show. yyyy-MM-dd id default (optional)");
        desc.add("Name of variable in which to store the result (optional)");
    }

    private String getRandomDate(
            String start, String end, String format) {
        SimpleDateFormat of = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat tf = new SimpleDateFormat(format);
        Date dateS;
        Date dateE;
        String date = null;
        try {
            dateS = of.parse(start);
            dateE = of.parse(end);
            long diff = (dateE.getTime() - dateS.getTime());
            long rand = 1000 + ((long) (new Random().nextDouble() * (diff - 1000)));
            date = tf.format(dateS.getTime() + rand);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public String execute(SampleResult sampleResult, Sampler sampler) {
        // System.out.println("start: " + dateS + ", end: " + dateE + ", format: " + format);
        String result = getRandomDate(dateS, dateE, format);
        // System.out.println("date: " + result);
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
        if (values.length > 0) {
            dateS = ((CompoundVariable) values[0]).execute().trim();
        }
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
