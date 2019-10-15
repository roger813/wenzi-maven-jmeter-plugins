package pers.wenzi.jmeter.plugins.functions;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.util.TextUtils;
import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.functions.AbstractFunction;
import org.apache.jmeter.functions.InvalidVariableException;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.threads.JMeterVariables;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class WZImageBase64 extends AbstractFunction {

    private static final List<String> desc = new LinkedList<>();
    private static final String KEY = "__WZImageBase64";

    private CompoundVariable file;
    private CompoundVariable varName;

    static {
        desc.add("file, must be absolute path");
        desc.add("Name of variable in which to store the result (optional)");
    }

    private String image2Base64(String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        InputStream is;
        byte[] data     = null;
        try {
            is = new FileInputStream(url);
            data = new byte[is.available()];
            is.read(data);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Base64.encodeBase64String(data);
    }

    @Override
    public String execute(SampleResult sampleResult, Sampler sampler)
            throws InvalidVariableException {
        String result = image2Base64(file.execute().trim());
        // 结果保存至变量
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
    public void setParameters(Collection<CompoundVariable> collection) throws InvalidVariableException {
        checkParameterCount(collection, 1, 2);
        Object[] values = collection.toArray();
        file = (values.length > 0)
                ? ((CompoundVariable) values[0]) : null;
        varName = (values.length > 4)
                ? ((CompoundVariable) values[3]) : null;
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
