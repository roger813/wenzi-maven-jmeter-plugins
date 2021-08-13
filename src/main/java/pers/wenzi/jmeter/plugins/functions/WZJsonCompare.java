package pers.wenzi.jmeter.plugins.functions;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonArray;
import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.functions.AbstractFunction;
import org.apache.jmeter.functions.InvalidVariableException;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.threads.JMeterVariables;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * @Title: WZCompareJson
 * @Description: TODO
 * @Author: liwenhuan
 * @Date: 2021/8/13
 */
public class WZJsonCompare extends AbstractFunction {

    private static final String KEY = "__WZJsonCompare";
    private static final List<String> DESC = new LinkedList<String>() {{
        add("first json (required)");
        add("second json (required)");
        add("Name of variable in which to store the result (optional)");
    }};

    private String json1;
    private String json2;
    private String varname;

    private List<String> errorList = new LinkedList<>();

    @Override
    public String execute(SampleResult sampleResult, Sampler sampler) throws InvalidVariableException {
        compareJson(json1, json2);
        String result = errorList.toString();
        if (varname != null) {
            JMeterVariables vars = getVariables();
            if (vars != null && varname.length() > 0) {
                vars.put(varname, result);
            }
        }
        return result;
    }

    @Override
    public void setParameters(Collection<CompoundVariable> collection) throws InvalidVariableException {
        checkParameterCount(collection, 2, 3);
        Object[] values = collection.toArray();
        json1 = (values.length > 0)
                ? ((CompoundVariable) values[0]).execute().trim() : null;
        json2 = (values.length > 1)
                ? ((CompoundVariable) values[1]).execute().trim() : null;
        varname = (values.length > 2)
                ? ((CompoundVariable) values[2]).execute().trim() : null;
    }

    @Override
    public String getReferenceKey() {
        return KEY;
    }

    @Override
    public List<String> getArgumentDesc() {
        return DESC;
    }

    private boolean isJsonArray(String target) {
        try {
            JSONArray.parseArray(target);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isJsonObject(String target) {
        try {
            JSONObject.parseObject(target);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void compare(String str1, String str2) {
        if (!str1.equals(str2)) {
            String error = String.format("[str1: %s, str2: %s] 比对失败", str1, str2);
            errorList.add(error);
        }
    }

    private void compareJson(String json1, String json2) {
        if (json1 == null || json2 == null) return;
        else if (isJsonArray(json1) && isJsonArray(json2)) {
            JSONArray a1 = JSONArray.parseArray(json1);
            JSONArray a2 = JSONArray.parseArray(json2);
            compareArray(a1, a2);
        } else if (isJsonObject(json1) && isJsonObject(json2)) {
            JSONObject o1 = JSONObject.parseObject(json1);
            JSONObject o2 = JSONObject.parseObject(json2);
            compareObject(o1, o2);
        } else if (json1 instanceof String && json2 instanceof String) {
            compare(json1, json2);
        } else {
            throw new RuntimeException("无法识别的对象！");
        }
    }

    private void compareArray(JSONArray array1, JSONArray array2) {
        if (array1.size() == array2.size()) {
            for (int i = 0; i < array1.size(); i++) {
                String s1 = String.valueOf(array1.get(i));
                String s2 = String.valueOf(array2.get(i));
                if (isJsonArray(s1) && isJsonArray(s2)) {
                    JSONArray a1 = JSONArray.parseArray(s1);
                    JSONArray a2 = JSONArray.parseArray(s2);
                    compareArray(a1, a2);
                } else if (isJsonObject(s1) && isJsonObject(s2)) {
                    JSONObject o1 = JSONObject.parseObject(s1);
                    JSONObject o2 = JSONObject.parseObject(s2);
                    compareObject(o1, o2);
                } else if (s1 instanceof String && s2 instanceof String) {
                    compare(s1, s2);
                } else {
                    throw new RuntimeException("无法识别的对象！");
                }
            }
        } else {
            throw new RuntimeException("列表长度不同！");
        }
    }

    private void compareObject(JSONObject object1, JSONObject object2) {
        for (String key : object1.keySet()) {
            if (object2.containsKey(key)) {
                String s1 = String.valueOf(object1.get(key));
                String s2 = String.valueOf(object2.get(key));
                if (isJsonArray(s1) && isJsonArray(s2)) {
                    JSONArray a1 = JSONArray.parseArray(s1);
                    JSONArray a2 = JSONArray.parseArray(s2);
                    compareArray(a1, a2);
                } else if (isJsonObject(s1) && isJsonObject(s2)) {
                    JSONObject o1 = JSONObject.parseObject(s1);
                    JSONObject o2 = JSONObject.parseObject(s2);
                    compareObject(o1, o2);
                } else {
                    compareJson(s1, s2);
                }
            } else {
                throw new RuntimeException("两个json对象内容不同！");
            }
        }
    }

}
