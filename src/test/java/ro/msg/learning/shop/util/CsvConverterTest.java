package ro.msg.learning.shop.util;

import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import ro.msg.learning.shop.dto.StockDto;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CsvConverterTest {

    private static final String CSV = "id,locationId,productId,quantity\n2,6,31,13\n";

    private CsvConverter csvConverter;

    @Before
    public void setup() {
        csvConverter = new CsvConverter();
    }

    @Test
    @SneakyThrows
    public void fromCsvTest() {
        List<StockDto> expectedStocks = new ArrayList<>();
        expectedStocks.add(new StockDto(2, 13, 6, 31));
        InputStream inputStream = new ByteArrayInputStream(CSV.getBytes());

        assertEquals(expectedStocks, csvConverter.fromCsv(StockDto.class, inputStream));
    }

    @Test
    @SneakyThrows
    public void toCsvTest() {
        List<StockDto> existingStocks = new ArrayList<>();
        existingStocks.add(new StockDto(2, 13, 6, 31));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        csvConverter.toCsv(StockDto.class, existingStocks, outputStream);

        assertEquals(CSV, new String(outputStream.toByteArray()));
    }
}