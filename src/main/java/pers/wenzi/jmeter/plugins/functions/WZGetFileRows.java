package pers.wenzi.jmeter.plugins.functions;

import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.functions.AbstractFunction;
import org.apache.jmeter.functions.InvalidVariableException;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.threads.JMeterVariables;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class WZGetFileRows extends AbstractFunction {

    private static final List<String> desc = new LinkedList<>();
    private static final String KEY = "__WZGetFileRows";

    private CompoundVariable file;
    private CompoundVariable varName;

    static {
        desc.add("File name or path (required)");
        desc.add("Name of variable in which to store the result (optional)");
    }

    public static int getLineCount(String url) {
        FileReader fr = null;
        LineNumberReader lnr = null;
        int cnt = 0;
        try {
            fr = new FileReader(url);
            lnr = new LineNumberReader(fr);
            lnr.skip(Long.MAX_VALUE);
            cnt = lnr.getLineNumber() + 1;
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
        return cnt;
    }

    @Override
    public String execute(SampleResult sampleResult, Sampler sampler) throws InvalidVariableException {
        String url = file.execute().trim();
        String cnt = String.valueOf(getLineCount(url));
        if (varName != null) {
            JMeterVariables vars = getVariables();
            final String varTrim = varName.execute().trim();
            if (vars != null && varTrim.length() > 0) {
                vars.put(varTrim, cnt);
            }
        }
        return cnt;
    }

    @Override
    public void setParameters(Collection<CompoundVariable> collection) throws InvalidVariableException {
        checkParameterCount(collection, 1, 2);
        Object[] values = collection.toArray();

        file = (values.length > 0)
                ? ((CompoundVariable) values[0]) : null;
        varName = (values.length > 1)
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
