package ro.msg.learning.shop;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractGenericHttpMessageConverter;
import org.springframework.stereotype.Component;
import ro.msg.learning.shop.util.CsvConverter;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

@Component
public class CsvHttpMessageConverter<T> extends AbstractGenericHttpMessageConverter<List<T>> {

    private static final MediaType SUPPORTED_MEDIA_TYPE = new MediaType("text", "csv");
    private final CsvConverter csvConverter;

    @Autowired
    public CsvHttpMessageConverter(CsvConverter csvConverter) {
        super(SUPPORTED_MEDIA_TYPE);
        this.csvConverter = csvConverter;
    }

    @Override
    @SneakyThrows
    public boolean canRead(Type type, Class<?> contextClass, MediaType mediaType) {
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;

            return canRead(mediaType) &&
                    mediaType != null &&
                    mediaType.equals(SUPPORTED_MEDIA_TYPE) &&
                    Class.forName(parameterizedType.getRawType().getTypeName()).isAssignableFrom(List.class);
        }

        return false;
    }

    @Override
    @SneakyThrows
    public boolean canWrite(Type type, Class<?> clazz, MediaType mediaType) {
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            return canWrite(mediaType) &&
                    mediaType != null &&
                    mediaType.equals(SUPPORTED_MEDIA_TYPE) &&
                    Class.forName(parameterizedType.getRawType().getTypeName()).isAssignableFrom(List.class);
        }

        return false;
    }

    @Override
    @SuppressWarnings("unchecked")
    @SneakyThrows
    protected void writeInternal(List<T> list, Type type, HttpOutputMessage httpOutputMessage) {
        ParameterizedType parameterizedType = (ParameterizedType) type;
        Class<T> clazz = (Class<T>) parameterizedType.getActualTypeArguments()[0];
        csvConverter.toCsv(clazz, list, httpOutputMessage.getBody());
    }

    @Override
    protected List<T> readInternal(Class<? extends List<T>> aClass, HttpInputMessage httpInputMessage) {
        return Collections.emptyList();
    }

    @Override
    @SuppressWarnings("unchecked")
    @SneakyThrows
    public List<T> read(Type type, Class<?> aClass, HttpInputMessage httpInputMessage) {
        ParameterizedType parameterizedType = (ParameterizedType) type;
        Class<T> clazz = (Class<T>) parameterizedType.getActualTypeArguments()[0];
        return csvConverter.fromCsv(clazz, httpInputMessage.getBody());
    }
}
