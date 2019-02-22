package ro.msg.learning.shop.odata.jpa;

import org.apache.olingo.odata2.jpa.processor.api.ODataJPAContext;
import org.apache.olingo.odata2.jpa.processor.api.ODataJPAServiceFactory;
import org.apache.olingo.odata2.jpa.processor.api.exception.ODataJPARuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Component;

@Component("JpaODataServiceFactory")
@Scope("request")
public class JpaServiceFactory extends ODataJPAServiceFactory {
    private final LocalContainerEntityManagerFactoryBean factory;
    private final ShopEdmExtension shopEdmExtension;

    @Autowired
    public JpaServiceFactory(LocalContainerEntityManagerFactoryBean factory, ShopEdmExtension shopEdmExtension) {
        this.factory = factory;
        this.shopEdmExtension = shopEdmExtension;
    }

    @Override
    public ODataJPAContext initializeODataJPAContext() throws ODataJPARuntimeException {
        ODataJPAContext context = this.getODataJPAContext();
        context.setEntityManagerFactory(factory.getObject());
        context.setPersistenceUnitName("shop");
        context.setJPAEdmExtension(shopEdmExtension);
        context.setDefaultNaming(false);
        return context;
    }

}
