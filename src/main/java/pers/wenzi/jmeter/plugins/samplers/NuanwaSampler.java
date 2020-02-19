package pers.wenzi.jmeter.plugins.samplers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

import pers.wenzi.jmeter.plugins.samplers.util.SignUtil;

public class NuanwaSampler extends AbstractJavaSamplerClient {

  protected String appkey = "QrJV2nb6A3JRtXdRrLZjIxhYaL8Ke6";
  protected String secretkey = "Px7n7W8qHCQWLf2AHq94vTUTSiSYg9";

  private String batchNo;
  private String authorAgreement;
  private String authorAgreementUrl;
  private String authorType;
  private String insuranceCertNo;
  private String insuranceCertType;
  private String insuranceName;
  private String insuredRelation;
  private String investigateArea;
  private String liabilityTypeList;
  private String proposalNum;

  @Override
  public Arguments getDefaultParameters() {
    Arguments args = new Arguments();
    args.addArgument("batchNo", "");
    args.addArgument("authorAgreement", "购买本产品即表示投保人同意授权");
    args.addArgument("authorAgreementUrl", "https://www.zylbzn.com/tbsm/V1/statement.htm");
    args.addArgument("authorType", "2");
    args.addArgument("insuranceCertNo", "");
    args.addArgument("insuranceCertType", "01");
    args.addArgument("insuranceName", "");
    args.addArgument("insuredRelation", "1");
    args.addArgument("investigateArea", "4100");
    args.addArgument("liabilityTypeList", "D");
    args.addArgument("proposalNum", "");
    return args;
  }

  @Override
  public SampleResult runTest(JavaSamplerContext arg0) {
    SampleResult results = new SampleResult();

    results.sampleStart();
    batchNo             = arg0.getParameter("batchNo");
    authorAgreement     = arg0.getParameter("authorAgreement");
    authorAgreementUrl  = arg0.getParameter("authorAgreementUrl");
    authorType          = arg0.getParameter("authorType");
    insuranceCertNo     = arg0.getParameter("insuranceCertNo");
    insuranceCertType   = arg0.getParameter("insuranceCertType");
    insuranceName       = arg0.getParameter("insuranceName");
    insuredRelation     = arg0.getParameter("insuredRelation");
    investigateArea     = arg0.getParameter("investigateArea");
    liabilityTypeList   = arg0.getParameter("liabilityTypeList");
    proposalNum         = arg0.getParameter("proposalNum");
    String biz = "{\"batchNo\":\""+ batchNo +"\",\"investigationList\":[{\"authorAgreement\":\""+ authorAgreement +"\",\"authorAgreementUrl\":\""+ authorAgreementUrl +"\",\"authorType\":\""+ authorType +"\",\"insuranceCertNo\":\""+ insuranceCertNo +"\",\"insuranceCertType\":\""+ insuranceCertType +"\",\"insuranceName\":\""+ insuranceName +"\",\"insuredRelation\":\""+ insuredRelation +"\",\"investigateArea\":\""+ investigateArea +"\",\"liabilityTypeList\":[\""+ liabilityTypeList +"\"],\"proposalNum\":\""+ proposalNum +"\"}]}";

    // System.out.println(biz);
    JSONObject bizContent = JSON.parseObject(biz);
    RequestBase rb = new RequestBase();
    String timestamp = String.valueOf(System.currentTimeMillis());
    String signValue = SignUtil.sign(bizContent, timestamp, this.secretkey);
    rb.setAppKey(this.appkey);
    rb.setBizContent(bizContent);
    rb.setSignValue(signValue);
    rb.setTimestamp(timestamp);
    rb.setVersion("1.0.0");

    String res = JSONObject.toJSONString(rb);
    results.setSuccessful(true);
    results.setResponseData(res, null);
    results.setDataType(SampleResult.TEXT);
    results.sampleEnd();
    return results;
  }

  @Override
  public void setupTest(JavaSamplerContext arg0) {
    // TODO Auto-generated method stub

  }

  @Override
  public void teardownTest(JavaSamplerContext arg0) {
    // TODO Auto-generated method stub

  }
  
}