package pers.wenzi.jmeter.plugins.samplers.util;

import com.alibaba.fastjson.JSONObject;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SignUtil
{
  private static final Logger log = LoggerFactory.getLogger(SignUtil.class);
  
  public static String sign(final Map<String, Object> bizContent, final String timestamp, final String secret) {
    String genSign = "";
    try {
      final Map<String, Object> map = new HashMap(bizContent);
      final StringBuilder stringBuilder = new StringBuilder("");
      map.put("timestamp", timestamp);

      final TreeMap<String, Object> treeMap = sortMap(map);
      final String treeMapStr = JSONObject.toJSONString(treeMap);
      stringBuilder.append(treeMapStr);

      final String orgStr = secret;
      log.info("���������������{}", orgStr);
      genSign = DigestUtils.md5Hex(orgStr.getBytes("UTF-8")).toLowerCase();
      log.info("���������������{}", genSign);
    } catch (final Exception e) {
      log.error("������������", e);
    }
    return genSign;
  }

  public static boolean verify(final Map<String, Object> map, final String secret, final String sign) {
    boolean flag = false;
    try {
      final StringBuilder stringBuilder = new StringBuilder("");

      final TreeMap<String, Object> treeMap = sortMap(map);
      final String treeMapStr = JSONObject.toJSONString(treeMap);
      stringBuilder.append(treeMapStr);

      final String orgStr = secret;
      log.info("���������������{}", orgStr);
      final String genSign = DigestUtils.md5Hex(orgStr.getBytes("UTF-8")).toLowerCase();
      log.info("���������������{}", genSign);
      if (genSign.equals(sign)) {
        flag = true;
      }
    } catch (final Exception e) {
      log.error("������������", e);
    }
    return flag;
  }

  private static TreeMap<String, Object> sortMap(final Map<String, Object> map) {
    final TreeMap<String, Object> treeMap = new TreeMap();
    for (final Map.Entry<String, Object> entry : map.entrySet()) {
      if ((entry.getValue() instanceof Map)) {
        final TreeMap<String, Object> subTreeMap = sortMap((Map) entry.getValue());
        treeMap.put(entry.getKey(), subTreeMap);
      }
      else
      {
        treeMap.put(entry.getKey(), entry.getValue());
      }
    }
    return treeMap;
  }
}