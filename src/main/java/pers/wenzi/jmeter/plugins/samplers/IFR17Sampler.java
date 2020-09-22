package pers.wenzi.jmeter.plugins.samplers;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import pers.wenzi.jmeter.plugins.utils.IFR17DBUtil;

import java.util.List;

public class IFR17Sampler extends AbstractJavaSamplerClient {

    @Override
    public Arguments getDefaultParameters() {
        Arguments args = new Arguments();
        args.addArgument("valDate", "");
        args.addArgument("disCtrl", "");
        args.addArgument("rskCtrl", "");
        return args;
    }

    @Override
    public void setupTest(JavaSamplerContext arg0) {}

    @Override
    public void teardownTest(JavaSamplerContext arg0) {}
    
    @Override
    public SampleResult runTest(JavaSamplerContext arg0) {
        String valDate = arg0.getParameter("valDate");
        String disCtrl = arg0.getParameter("disCtrl");
        String rskCtrl = arg0.getParameter("rskCtrl");
        SampleResult result = new SampleResult();

        IFR17DBUtil util = new IFR17DBUtil();
        List<Object> list = util.selectList(
                "select * from i17_lic_data where query_label = '车险_2020_A_Product1_43861_43861'",
                null);

        result.setSuccessful(true);
        result.setDataType(SampleResult.TEXT);
        result.setResponseData(list.toString(), "UTF-8");
        return result;
    }



}
