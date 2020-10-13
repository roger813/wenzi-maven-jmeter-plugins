package pers.wenzi.jmeter.plugins.samplers;

import com.alibaba.fastjson.JSON;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ExcelSampler extends AbstractJavaSamplerClient {

    private static final Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    private String sheetName;
    private String[] varNames;

    private List<String> listValue;

    @Override
    public Arguments getDefaultParameters() {
        Arguments args = new Arguments();
        args.addArgument("file", "");
        args.addArgument("sheet name", "");
        args.addArgument("variable names", "");
        args.addArgument("iteration no", "${__counter(true,)}");
        return args;
    }

    @Override
    public void setupTest(JavaSamplerContext arg0) {
        // 获取文件绝对路径
        String file = arg0.getParameter("file");
        // 获取sheet名称
        sheetName = arg0.getParameter("sheet name");
        // 获取变量列表
        String names = arg0.getParameter("variable names");
        if (null == names || names.trim().length() < 1) {
            logger.error("缺少变量名列表，请检查！");
            System.exit(1);
        }
        varNames = names.split(",");
        // 预加载表数据
        listValue = preLoadFile(file);
//        logger.info("listValue: {}", listValue.toString());
    }

    @Override
    public void teardownTest(JavaSamplerContext arg0) {}
    
    @Override
    public SampleResult runTest(JavaSamplerContext arg0) {
        SampleResult result = new SampleResult();
        int iterationNo = arg0.getIntParameter("iteration no");

        String json = mergeOne(varNames, listValue, iterationNo);

        result.setSuccessful(true);
        result.setDataType(SampleResult.TEXT);
        result.setResponseData(json, "UTF-8");
        return result;
    }

    private List<String> preLoadFile(String filePath) {
        Workbook wb = null;
        FileInputStream is = null;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                logger.error("文件不存在，请检查！");
                return null;
            }
            is = new FileInputStream(file);
            wb = getWorkbook(is, file);
            return (wb == null) ? null : fetchExcel(wb);
        } catch (IOException e) {
            logger.error("文件预加载错误，错误原因：{}", e.getMessage());
        } finally {
            try {
                if (null != wb) wb.close();
                if (null != is) is.close();
            } catch (Exception e) {
                logger.error("关闭文件流错误，错误原因：{}", e.getMessage());
            }
        }
        return null;
    }

    private Workbook getWorkbook(InputStream is, File file) {
        Workbook wb = null;
        String fName = file.getName();
        String fType = fName.substring(fName.lastIndexOf(".") + 1);
        try {
            if ("xls".equalsIgnoreCase(fType))
                wb = new HSSFWorkbook(is);
            if ("xlsx".equalsIgnoreCase(fType))
                wb = new XSSFWorkbook(is);
        } catch (IOException e) {
            logger.error("创建实例错误，错误原因：{}", e.getMessage());
            return null;
        }
        return wb;
    }

    private List<String> fetchExcel(Workbook wb) {
        // 选择工作表，若无指定取第1张表，否则按指定表取
        Sheet sheet = (sheetName.length() < 1)
                ? wb.getSheetAt(0) : wb.getSheet(sheetName);
        // 检查是否存在有效行，若没有一行有数据，返回空
        int rows = sheet.getPhysicalNumberOfRows();
        if (rows < 1) {
            logger.error("文件中不存在有效数据行，请检查！");
            return null;
        }
        // 检查有效列数，若小于变量列表
        int cols = sheet.getRow(0).getPhysicalNumberOfCells();
        if (cols < varNames.length) {
            logger.error("文件中列总数小于变量数，请检查！");
            return null;
        }

        List<String> list = new LinkedList<>();
        for (int curRow = sheet.getFirstRowNum() + 1; curRow < rows; curRow++) {
            StringBuilder sb = new StringBuilder();
            for (int curCol = 0; curCol < varNames.length; curCol ++) {
                Row row = sheet.getRow(curRow);
                Cell cell = row.getCell(curCol);
                sb.append(celltoString(cell, cell.getCellType()));
                if (curCol < varNames.length - 1)
                    sb.append(",");
            }
            list.add(sb.toString());
        }
        return list;
    }

    private String celltoString(Cell cell, CellType type) {
        switch (type) {
            case BLANK: return "";
            case ERROR: return ErrorEval.getText(cell.getErrorCellValue());
            case STRING: return cell.getRichStringCellValue().toString();
            case BOOLEAN: return cell.getBooleanCellValue() ? "True" : "False";
            case FORMULA: return cell.getCellFormula();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    return sdf.format(cell.getDateCellValue());
                }
                return cell.getNumericCellValue() + "";
            default: return "未知格式，请检查！";
        }
    }

    private String mergeOne(String[] varNames, List<String> listValue, int iterationNo) {
        Map<String, String> map = new LinkedHashMap<>();
        int row = Math.abs((iterationNo - 1) % listValue.size());
        String record = listValue.get(row);
        String[] values = record.split(",");
        for (int j = 0; j < varNames.length && j < values.length; j++) {
            map.put(varNames[j], values[j]);
        }
        return JSON.toJSONString(map);
    }

}
