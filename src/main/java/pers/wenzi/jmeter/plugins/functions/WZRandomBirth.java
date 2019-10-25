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

    private String strStart;
    private String strEnd;
    private String format;
    private String varname;

    static {
        desc.add("Start Date, format: yyyy-MM-dd (required)");
        desc.add("End Date, format: yyyy-MM-dd. Current time is default (optional)");
        desc.add("Format, format u want to show. yyyy-MM-dd id default (optional)");
        desc.add("Name of variable in which to store the result (optional)");
    }

    private String getRandomDate() {
        SimpleDateFormat parseFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat outputFormat = new SimpleDateFormat(format);
        Date dateStart;
        Date dateEnd;
        String date = null;
        try {
            dateStart = parseFormat.parse(strStart);
            dateEnd = parseFormat.parse(strEnd);
            long diff = (dateEnd.getTime() - dateStart.getTime());
            long rand = 1000 + ((long) (new Random().nextDouble() * (diff - 1000)));
            date = outputFormat.format(dateStart.getTime() + rand);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public String execute(SampleResult sampleResult, Sampler sampler) {
        String result = getRandomDate();
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
            strStart = ((CompoundVariable) values[0]).execute().trim();
            if (strStart.length() < 1) {
                System.out.println("start date must not be empty");
                System.exit(2);
            }
        }
        if (values.length > 1) {
            strEnd = ((CompoundVariable) values[1]).execute().trim();
            if (strEnd.length() < 1) {
                strEnd = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
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
        }
    }

    public String getReferenceKey() {
        return KEY;
    }

    public List<String> getArgumentDesc() {
        return desc;
    }

}
