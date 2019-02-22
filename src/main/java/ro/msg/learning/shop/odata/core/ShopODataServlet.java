package ro.msg.learning.shop.odata.core;

import org.apache.olingo.odata2.api.ODataServiceFactory;
import org.apache.olingo.odata2.core.servlet.ODataServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
class ShopODataServlet extends ODataServlet {
    private final transient ODataServiceFactory factory;

    @Autowired
    public ShopODataServlet(@Qualifier("CoreODataServiceFactory") ODataServiceFactory factory) {
        this.factory = factory;
    }

    @Override
    protected ODataServiceFactory getServiceFactory(HttpServletRequest request) {
        return factory;
    }
}