package pers.wenzi.jmeter.plugins.functions;

import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.functions.AbstractFunction;
import org.apache.jmeter.functions.InvalidVariableException;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.threads.JMeterVariables;

import java.io.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class WZRandomAsFile extends AbstractFunction {

    private static final List<String> desc = new LinkedList<>();
    private static final String KEY = "__WZRandomAsFile";

    private CompoundVariable file;
    private CompoundVariable rows;
    private CompoundVariable varName;

    static {
        desc.add("File name or path (required)");
        desc.add("Rows (optional)");
        desc.add("Name of variable in which to store the result (optional)");
    }

    public static String getRandomValue(String url, int row) {
        String text = "";
        FileReader fr = null;
        LineNumberReader lnr = null;
        try {
            fr = new FileReader(url);
            lnr = new LineNumberReader(fr);
            if (row == 0)
                row = WZGetFileRows.getLineCount(url);
            int cnt = (int)(Math.random() * (row - 1 + 1));
            int line = 0;
            while (line <= cnt) {
                line ++;
                text = lnr.readLine();
//                if (line == cnt)
//                    System.out.println("Rows is " + row + " Current Line is " + line + " Current text is " + text);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fr != null) fr.close();
                if (lnr != null) lnr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return text;
    }

    @Override
    public String execute(SampleResult sampleResult, Sampler sampler) throws InvalidVariableException {
        String url = file.execute().trim();
        int row = (rows != null) ? Integer.parseInt(rows.execute().trim()) : 0;
        String value = getRandomValue(url, row);
        if (varName != null) {
            JMeterVariables vars = getVariables();
            final String varTrim = varName.execute().trim();
            if (vars !=null && varTrim.length() > 0) {
                vars.put(varTrim, value);
            }
        }
        return value;
    }

    @Override
    public void setParameters(Collection<CompoundVariable> collection) throws InvalidVariableException {
        checkParameterCount(collection, 1, 3);
        Object[] values = collection.toArray();

        file = (values.length > 0)
                ? ((CompoundVariable) values[0]) : null;
        rows = (values.length > 1)
                ? ((CompoundVariable) values[1]) : null;
        varName = (values.length > 2)
                ? ((CompoundVariable) values[2]) : null;
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
