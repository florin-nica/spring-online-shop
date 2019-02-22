package ro.msg.learning.shop.odata.jpa;

import lombok.SneakyThrows;
import org.apache.olingo.odata2.jpa.processor.api.model.JPAEdmExtension;
import org.apache.olingo.odata2.jpa.processor.api.model.JPAEdmSchemaView;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
public class ShopEdmExtension implements JPAEdmExtension {


    @Override
    public void extendWithOperation(JPAEdmSchemaView jpaEdmSchemaView) {
        //not used
    }

    @Override
    public void extendJPAEdmSchema(JPAEdmSchemaView jpaEdmSchemaView) {
        //not used
    }

    @Override
    @SneakyThrows
    public InputStream getJPAEdmMappingModelStream() {
        return ShopEdmExtension.class.getClassLoader().getResourceAsStream("JPA_mapping_model.xml");
    }
}
