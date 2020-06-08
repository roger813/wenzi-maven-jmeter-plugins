package pers.wenzi.jmeter.plugins.samplers;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

public class HttpPostSampler extends AbstractJavaSamplerClient {
    
    @Override
    public Arguments getDefaultParameters() {
        Arguments args = new Arguments();
        args.addArgument("url", "");
        args.addArgument("data", "");
        args.addArgument("Authorization", "");
        return args;
    }

    @Override
    public void setupTest(JavaSamplerContext arg0) {}

    @Override
    public void teardownTest(JavaSamplerContext arg0) {}
    
    @Override
    public SampleResult runTest(JavaSamplerContext arg0) {
        String resultData = null;
        String requestHead = null;
        String url = arg0.getParameter("url");
        String data = arg0.getParameter("data");
        String auth = arg0.getParameter("Authorization");
        SampleResult result = new SampleResult();
        try {
            result.sampleStart();
            resultData = httpPost(url, data, auth);
            requestHead = "Get " + url + 
                          "\n\n\nData:\n" + data;
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccessful(false);
            result.setRequestHeaders(requestHead);
            result.setDataType(SampleResult.TEXT);
            result.setResponseData(e.toString(), null);
            return result;
        } finally {
            result.sampleEnd();
        }
        result.setSuccessful(true);
        result.setRequestHeaders(requestHead);
        result.setDataType(SampleResult.TEXT);
        result.setResponseData(resultData, null);
        return result;
    }
    
    public String httpPost(String url, String data, String auth) throws Exception {
        String result = "";
        BufferedReader in = null;
        DataOutputStream out = null;
        
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestProperty("accept", "*/*");
        conn.setRequestProperty("Authorization", auth);
        conn.setRequestProperty("connection", "Keep-Alive");
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setRequestMethod("POST");
        conn.setInstanceFollowRedirects(true);
        conn.connect();
        
        out = new DataOutputStream(conn.getOutputStream());
        out.writeBytes(data);
        out.flush();
        out.close();
        
        in = new BufferedReader(
                new InputStreamReader(
                        conn.getInputStream(), "utf-8"));
        String line;
        while ((line = in.readLine()) != null) {
            result += line;
        }
        
        in.close();
        conn.disconnect();
        return result;
    }

}
