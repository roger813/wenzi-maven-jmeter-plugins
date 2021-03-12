package pers.wenzi.jmeter.plugins.samplers;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

/**
 * @Title: IfrsSamplerTest
 * @Description: TODO
 * @Author: liwenhuan
 * @Date: 2020/9/19
 */
public class ExcelSamplerTest {

    private static Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    @Test
    public void runTest() {
        Arguments arg0 = new Arguments();
        arg0.addArgument("file", "/Users/wenzi/Documents/_git/work/_IFRS17/script/template/CF_INPUT_Template.xlsx");
        arg0.addArgument("sheet name", "CF Input");
        arg0.addArgument("query area", "2:1,4");
        arg0.addArgument("variable names", "Category,Group,Product,Pol_M,Acc_M,Index,p1,p2,p3,p4,p5,p6,p7,p8,p9,p10,p11,p12");
        arg0.addArgument("iteration no", "4");

        ExcelSampler sampler = new ExcelSampler();
        sampler.setupTest(new JavaSamplerContext(arg0));
        SampleResult result = sampler.runTest(new JavaSamplerContext(arg0));
        logger.info("Test Finished! Result: {}", result.getResponseDataAsString());
    }

}
