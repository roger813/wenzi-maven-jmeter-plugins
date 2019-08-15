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

    private CompoundVariable src;
    private CompoundVariable key;
    private CompoundVariable varName;

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
        if (src == null) {
            System.out.println("Error: Clear text cannot be empty");
//            System.exit(1);
        }
        if (key == null) {
            System.out.println("Error: Cipher string cannot be empty");
//            System.exit(1);
        }
        String k = key.execute().trim();
        if (k.length() < 32) {
            System.out.println("Error: Cipher string cannot be less then 32bit");
//            System.exit(1);
        }
        String result = decrypt(src.execute().trim(), k);
        if (varName != null) {
            JMeterVariables vars = getVariables();
            final String varTrim = varName.execute().trim();
            if (vars != null && varTrim.length() > 0) {
                vars.put(varTrim, result);
            }
        }
        return result;
    }

    public void setParameters(Collection<CompoundVariable> collection) throws InvalidVariableException {
        checkParameterCount(collection, 2, 3);
        Object[] values = collection.toArray();
        src = (values.length > 0)
                ? ((CompoundVariable) values[0]) : null;
        key = (values.length > 1)
                ? ((CompoundVariable) values[1]) : null;
        varName = (values.length > 2)
                ? ((CompoundVariable) values[1]) : null;
    }

    public String getReferenceKey() {
        return KEY;
    }

    public List<String> getArgumentDesc() {
        return desc;
    }

}
