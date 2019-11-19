package pers.wenzi.jmeter.plugins.functions;

import org.apache.commons.codec.binary.Base64;
import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.functions.AbstractFunction;
import org.apache.jmeter.functions.InvalidVariableException;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.threads.JMeterVariables;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class WZEncryptAES extends AbstractFunction {

    private static final List<String> desc = new LinkedList<>();
    private static final String KEY = "__WZEncryptAES";
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

    private String src;
    private String key;
    private String varname;

    static {
        desc.add("Clear text");
        desc.add("Cipher key");
        desc.add("Name of variable in which to store the result (optional)");
    }

    /**
     * 将任意长度密钥填充为标准128位密钥
     * 若有需要请重写该方法
     * @param key 原始密钥
     * @return AES标准密钥
     * @throws Exception
     */
    private SecretKeySpec secretKey(String key) throws Exception {
        KeyGenerator kg = KeyGenerator.getInstance("AES");
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        sr.setSeed(key.getBytes());
        kg.init(128, sr);
        SecretKey secretKey = kg.generateKey();
        return new SecretKeySpec(secretKey.getEncoded(), "AES");
    }

    /**
     * AES 加密操作, 模式为ECB
     * @param src
     * @param key
     * @return 返回Base64编码后的密文
     * @throws Exception
     */
    private String encypt(String src, String key) throws Exception {
        Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
        byte[] byteContent = src.getBytes(StandardCharsets.UTF_8);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey(key));
        byte[] result = cipher.doFinal(byteContent);
        return Base64.encodeBase64String(result);
    }

    public String execute(SampleResult sampleResult, Sampler sampler) {
        String result;
        if (src == null || src.length() < 1) {
            System.out.println("Clear text is required, and size must be > 1");
            return null;
        }
        if (key == null || key.length() < 1) {
            System.out.println("Chiper key is required, and size must be > 1");
            return null;
        }
        try {
            result = encypt(src, key);
        } catch (Exception e) {
            System.out.println("AES加密失败! 失败原因: " + e.getMessage());
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
        checkParameterCount(collection, 2, 3);
        Object[] values = collection.toArray();
        src = (values.length > 0)
                ? ((CompoundVariable) values[0]).execute().trim()
                : null;
        key = (values.length > 1)
                ? ((CompoundVariable) values[1]).execute().trim()
                : null;
        varname = (values.length > 2)
                ? ((CompoundVariable) values[2]).execute().trim()
                : null;
    }

    public String getReferenceKey() {
        return KEY;
    }

    public List<String> getArgumentDesc() {
        return desc;
    }

}
