package pers.wenzi.jmeter.java.sampler;

import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

public class WZCSVReader extends AbstractJavaSamplerClient { 
	
	private int getCounts(File file) {
		int count = 0;
		try {
			FileReader fr = new FileReader(file);
			LineNumberReader lr = new LineNumberReader(fr);
			lr.skip(Long.MAX_VALUE);
			count = lr.getLineNumber() + 1;
			lr.close();
			fr.close();
		} catch (Exception e) {
			System.out.println( "Error! Cause by: " + e.getMessage() );
		}
		return count;
	}
	
	private String randomRead(String uri, int count) {
		String str = "";
		try {
			File file = new File(uri);
			FileReader fr = new FileReader(file);
			LineNumberReader lr = new LineNumberReader(fr);
			int sum = (count > 0) ? count : getCounts(file);
			while (true) {
				int random = (int)(Math.random() * sum + 1);
				int line = 0;
				while (line < random) {
					line ++;
					str = lr.readLine();
				}
//				System.out.println("random = " + random + " line = " + line + " str = " + str);
				break;
			}
			lr.close();
			fr.close();
		} catch (Exception e) {
			System.out.println( "Error! Cause by: " + e.getMessage() );
		}
		return str;
	}
	
	public Arguments getDefaultParameters() {
		Arguments params = new Arguments();
		params.addArgument("FileURI", "");
		params.addArgument("Count", "0");
		return params;
	}
	
	@Override
	public SampleResult runTest(JavaSamplerContext jsc) {
		SampleResult sr = new SampleResult();
		
		// 写入事务开始
		sr.sampleStart();
		
		String fileURI = jsc.getParameter("FileURI", "");
		int count = jsc.getIntParameter("Count", 0);
		if (fileURI != null && fileURI.length() > 0)
			sr.setSamplerData(fileURI);
		String result = null;
		while( (result = randomRead(fileURI, count)) == null ) {
			result = randomRead(fileURI, count);
		}
		
		// 写入事务结束
		sr.sampleEnd();
		
		sr.setSuccessful(true);
		sr.setResponseData(result, "UTF-8");
		return sr;
	}
	
	public void teardownTest(JavaSamplerContext arg0) {
		
	}

}
