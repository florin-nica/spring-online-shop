package ro.msg.learning.shop.util;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

@Component
public class CsvConverter {

    private final CsvMapper mapper = new CsvMapper();

    public <T> List<T> fromCsv(Class<T> clazz, InputStream inputStream) throws IOException {
        CsvSchema schema = mapper.schemaFor(clazz).withHeader();

        MappingIterator<T> it = mapper.readerFor(clazz).with(schema)
                .readValues(inputStream);

        return it.readAll();
    }

    public <T> void toCsv(Class<T> clazz, List<T> pojos, OutputStream outputStream) throws IOException {
        CsvSchema schema = mapper.schemaFor(clazz).withHeader();

        mapper.writer(schema).writeValue(outputStream, pojos);
    }

}
