package pers.wenzi.jmeter.plugins.samplers;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

/**
 * @Title: IfrsSamplerTest
 * @Description: TODO
 * @Author: liwenhuan
 * @Date: 2020/9/19
 */
public class IFR17SamplerTest {

    private final static Logger log = LoggerFactory.getLogger(IFR17SamplerTest.class);

    @Test
    public void runTest() {
        Arguments arg0 = new Arguments();
        arg0.addArgument("valDate", "1");
        arg0.addArgument("disCtrl", "1");
        arg0.addArgument("rskCtrl", "1");

        IFR17Sampler sampler = new IFR17Sampler();
        SampleResult result = sampler.runTest(new JavaSamplerContext(arg0));

        log.info("IFR17SamplerTest Started, Args: {}", arg0.getArguments());
        log.info("IFR17SameperTest Finished, Result: {}", result.getResponseData());
    }

}
