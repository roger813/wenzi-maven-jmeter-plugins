package pers.wenzi.jmeter.plugins.functions;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.functions.AbstractFunction;
import org.apache.jmeter.functions.InvalidVariableException;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.threads.JMeterVariables;

public class WZDecryptAES extends AbstractFunction {

    private static final List<String> desc = new LinkedList<>();
    private static final String KEY = "__WZAesDecrypt";

    // private CompoundVariable src;
    // private CompoundVariable key;
    // private CompoundVariable varName;

    private String src;
    private String key;
    private String varname;

    static {
        desc.add("Clear text");
        desc.add("Cipher string");
        desc.add("Name of variable in which to store the result (optional)");
    }

    public static String decrypt(String src, String key) {
        String ki = key.substring(0, 16);
        String ve = key.substring(16, 32);
        try {
            byte[] raw = ki.getBytes();
            SecretKeySpec spec = new SecretKeySpec(raw, "AES");
            IvParameterSpec iv = new IvParameterSpec(ve.getBytes());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, spec, iv);
            byte[] decrypted = Base64.getMimeDecoder().decode(src);
            return new String(cipher.doFinal(decrypted), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String execute(SampleResult sampleResult, Sampler sampler) throws InvalidVariableException {
        String result = decrypt(src, key);
        if (varname != null) {
            JMeterVariables vars = getVariables();
            if (vars != null && varname.length() > 0) {
                vars.put(varname, result);
            }
        }
        return result;
    }

    public void setParameters(Collection<CompoundVariable> collection) throws InvalidVariableException {
        checkParameterCount(collection, 2, 3);
        Object[] values = collection.toArray();
        if (values.length > 0) {
            src = ((CompoundVariable) values[0]).execute().trim();
            if (src.length() < 1) {
                System.out.println("clear text must not be empty");
                System.exit(2);
            }
        }
        if (values.length > 1) {
            key = ((CompoundVariable) values[1]).execute().trim();
            if (key.length() < 32) {
                System.out.println("cipher string must not be less then 32bit");
                System.exit(2);
            }
        }
        if (values.length > 2) {
            varname = ((CompoundVariable) values[2]).execute().trim();
        }
    }

    public String getReferenceKey() {
        return KEY;
    }

    public List<String> getArgumentDesc() {
        return desc;
    }

}
