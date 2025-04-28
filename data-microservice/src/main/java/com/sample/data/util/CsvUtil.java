package com.sample.data.util;

import com.opencsv.CSVWriter;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.bean.comparator.LiteralComparator;
import org.apache.commons.collections.CollectionUtils;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.List;

public class CsvUtil {

    public static String writeCsvFromBean(List<?> data, Class reportType, String fileName) throws Exception {

        if (CollectionUtils.isNotEmpty(data)) {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            CSVWriter writer = new CSVWriter(osw);
            StatefulBeanToCsv sbc = (new StatefulBeanToCsvBuilder(writer)).withMappingStrategy(getStrategy(reportType)).withSeparator(',').build();
            sbc.write(data);
            writer.close();
            osw.close();
            OutputStream outStream = new FileOutputStream(fileName);
            os.writeTo(outStream);
        }
        return null;
    }

    private static HeaderColumnNameMappingStrategy getStrategy(Class reportType) {

        HeaderColumnNameMappingStrategy strategy = new HeaderColumnNameMappingStrategy<>();
        strategy.setType(reportType);

        if (reportType.isAnnotationPresent(CsvBindByNameOrder.class)) {
            CsvBindByNameOrder annotation = (CsvBindByNameOrder) reportType.getAnnotation(CsvBindByNameOrder.class);
            strategy.setColumnOrderOnWrite(
                    new LiteralComparator<>(Arrays.stream(annotation.value())
                            .map(String::toUpperCase).toArray(String[]::new)));
        }

        return strategy;
    }


}
