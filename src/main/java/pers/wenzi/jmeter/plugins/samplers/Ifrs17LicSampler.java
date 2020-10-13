package pers.wenzi.jmeter.plugins.samplers;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @Title: Ifrs17Sampler
 * @Description: TODO
 * @Author: liwenhuan
 * @Date: 2020/10/12
 */
public class Ifrs17LicSampler extends AbstractJavaSamplerClient {

    private static final Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    @Override
    public Arguments getDefaultParameters() {
        Arguments args = new Arguments();
        args.addArgument("group code", "");
        args.addArgument("discountCtrl", "");
        args.addArgument("riskAmountCtrl", "");
        args.addArgument("valuation Date", "");
        return args;
    }

    @Override
    public SampleResult runTest(JavaSamplerContext javaSamplerContext) {
        return null;
    }

}
