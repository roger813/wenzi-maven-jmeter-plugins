package pers.wenzi.jmeter.plugins.tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

public class WZResWriter extends AbstractJavaSamplerClient {
	
	private SampleResult sr;
	private String fileURI;
	private String content;
	
	private String doWrite(String uri, String data) {
		try {
			File file = new File(uri);
			File dirs = file.getParentFile();
			
			if (!dirs.exists())
				dirs.mkdir();
			if (!file.exists())
				file.createNewFile();
			// 开始写入数据
			FileOutputStream fos = new FileOutputStream(file, true);
			OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
			BufferedWriter bw = new BufferedWriter(osw);
			bw.write(data);
			bw.newLine();
			bw.flush();
			bw.close();
		} catch (IOException e) {
			return "Error! Cause by: " + e.getMessage();
		} 
		return uri;
	}
	
	public void setupTest(JavaSamplerContext jsc) {
		sr = new SampleResult();
		fileURI = jsc.getParameter("fileURI", "");
		content = jsc.getParameter("content", "");
		if (fileURI != null & fileURI.length() > 0)
			sr.setSamplerData(fileURI);
		if (content != null & content.length() > 0)
			sr.setSamplerData(content);
	}
	
	public Arguments getDefaultParameters() {
		Arguments params = new Arguments();
		params.addArgument("fileURI", "");
		params.addArgument("content", "");
		return params;
	}
	
	@Override
	public SampleResult runTest(JavaSamplerContext arg0) {
		// 写入事务开始
		sr.sampleStart();
		
		doWrite(fileURI, content);
		
		// 写入事务结束
		sr.sampleEnd();
		sr.setSuccessful(true);
		sr.setResponseData(fileURI, "UTF-8");
		return sr;
	}
	
	public void teardownTest(JavaSamplerContext arg0) {
		
	}

}
