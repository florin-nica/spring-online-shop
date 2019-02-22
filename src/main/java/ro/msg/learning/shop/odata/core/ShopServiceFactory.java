package ro.msg.learning.shop.odata.core;

import org.apache.olingo.odata2.api.ODataService;
import org.apache.olingo.odata2.api.ODataServiceFactory;
import org.apache.olingo.odata2.api.processor.ODataContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("CoreODataServiceFactory")
public class ShopServiceFactory extends ODataServiceFactory {

    private final ShopEdmProvider edmProvider;
    private final ShopProcessor shopProcessor;

    @Autowired
    public ShopServiceFactory(ShopEdmProvider edmProvider, ShopProcessor shopProcessor) {
        this.edmProvider = edmProvider;
        this.shopProcessor = shopProcessor;
    }

    @Override
    public ODataService createService(ODataContext ctx) {
        return createODataSingleProcessorService(edmProvider, shopProcessor);
    }
}
