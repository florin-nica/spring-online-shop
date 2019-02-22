package ro.msg.learning.shop.odata.core;

import org.apache.olingo.odata2.api.edm.EdmEntitySet;
import org.apache.olingo.odata2.api.edm.EdmLiteralKind;
import org.apache.olingo.odata2.api.edm.EdmProperty;
import org.apache.olingo.odata2.api.edm.EdmSimpleType;
import org.apache.olingo.odata2.api.ep.EntityProvider;
import org.apache.olingo.odata2.api.ep.EntityProviderReadProperties;
import org.apache.olingo.odata2.api.ep.EntityProviderWriteProperties;
import org.apache.olingo.odata2.api.ep.EntityProviderWriteProperties.ODataEntityProviderPropertiesBuilder;
import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.apache.olingo.odata2.api.exception.ODataException;
import org.apache.olingo.odata2.api.exception.ODataNotFoundException;
import org.apache.olingo.odata2.api.exception.ODataNotImplementedException;
import org.apache.olingo.odata2.api.processor.ODataResponse;
import org.apache.olingo.odata2.api.processor.ODataSingleProcessor;
import org.apache.olingo.odata2.api.uri.KeyPredicate;
import org.apache.olingo.odata2.api.uri.info.GetEntitySetUriInfo;
import org.apache.olingo.odata2.api.uri.info.GetEntityUriInfo;
import org.apache.olingo.odata2.api.uri.info.PostUriInfo;
import org.apache.olingo.odata2.core.ep.feed.ODataDeltaFeedImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ro.msg.learning.shop.model.Address;
import ro.msg.learning.shop.odata.data.ODataOrder;
import ro.msg.learning.shop.odata.data.ODataOrderDetail;

import java.io.InputStream;
import java.net.URI;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import static ro.msg.learning.shop.odata.core.ShopEdmProvider.ENTITY_SET_NAME_ORDERS;
import static ro.msg.learning.shop.odata.core.ShopEdmProvider.ENTITY_SET_NAME_ORDER_DETAILS;
import static ro.msg.learning.shop.odata.core.ShopEdmProvider.ENTITY_SET_NAME_PRODUCTS;

@Component
public class ShopProcessor extends ODataSingleProcessor {
    private final ShopDataStore dataStore;

    @Autowired
    public ShopProcessor(ShopDataStore shopDataStore) {
        dataStore = shopDataStore;
    }

    @Override
    public ODataResponse readEntitySet(GetEntitySetUriInfo uriInfo, String contentType) throws ODataException {

        EdmEntitySet entitySet;

        if (uriInfo.getNavigationSegments().isEmpty()) {
            entitySet = uriInfo.getStartEntitySet();

            if (ENTITY_SET_NAME_ORDERS.equals(entitySet.getName())) {
                return EntityProvider
                        .writeFeed(contentType, entitySet, dataStore.getOrders(), EntityProviderWriteProperties
                                .serviceRoot(getContext().getPathInfo().getServiceRoot()).build());
            } else if (ENTITY_SET_NAME_PRODUCTS.equals(entitySet.getName())) {
                return EntityProvider
                        .writeFeed(contentType, entitySet, dataStore.getProducts(), EntityProviderWriteProperties
                                .serviceRoot(getContext().getPathInfo().getServiceRoot()).build());
            } else if (ENTITY_SET_NAME_ORDER_DETAILS.equals(entitySet.getName())) {
                return EntityProvider
                        .writeFeed(contentType, entitySet, dataStore.getOrderDetails(), EntityProviderWriteProperties
                                .serviceRoot(getContext().getPathInfo().getServiceRoot()).build());
            }

            throw new ODataNotFoundException(ODataNotFoundException.ENTITY);

        }

        throw new ODataNotImplementedException();
    }

    @Override
    public ODataResponse readEntity(GetEntityUriInfo uriInfo, String contentType) throws ODataException {

        EdmEntitySet entitySet;

        if (uriInfo.getNavigationSegments().isEmpty()) {
            entitySet = uriInfo.getStartEntitySet();
            Map<String, Object> data = null;

            if (ENTITY_SET_NAME_ORDERS.equals(entitySet.getName())) {
                int id = getKeyValue(uriInfo.getKeyPredicates().get(0));
                data = dataStore.getOrder(id);

            } else if (ENTITY_SET_NAME_PRODUCTS.equals(entitySet.getName())) {
                int id = getKeyValue(uriInfo.getKeyPredicates().get(0));
                data = dataStore.getProduct(id);

            } else if (ENTITY_SET_NAME_ORDER_DETAILS.equals(entitySet.getName())) {
                int id = getKeyValue(uriInfo.getKeyPredicates().get(0));
                data = dataStore.getOrderDetail(id);
            }

            if (data != null) {
                return getODataResponse(contentType, entitySet, data);
            }

            throw new ODataNotFoundException(ODataNotFoundException.ENTITY);

        } else if (uriInfo.getNavigationSegments().size() == 1) {
            entitySet = uriInfo.getTargetEntitySet();

            if (ENTITY_SET_NAME_ORDER_DETAILS.equals(entitySet.getName())) {
                int orderKey = getKeyValue(uriInfo.getKeyPredicates().get(0));

                List<Map<String, Object>> orderDetails = new ArrayList<>(dataStore.getOrderDetailsForOrder(orderKey));

                return EntityProvider.writeFeed(contentType, entitySet, orderDetails, EntityProviderWriteProperties
                        .serviceRoot(getContext().getPathInfo().getServiceRoot()).build());
            }

            throw new ODataNotFoundException(ODataNotFoundException.ENTITY);
        }

        throw new ODataNotImplementedException();
    }

    @Override
    public ODataResponse createEntity(PostUriInfo uriInfo, InputStream content,
                                      String requestContentType, String contentType) throws ODataException {
        EdmEntitySet entitySet = uriInfo.getStartEntitySet();

        if (ENTITY_SET_NAME_ORDERS.equals(entitySet.getName())) {
            ODataOrder oDataOrder = new ODataOrder();
            EntityProviderReadProperties properties = EntityProviderReadProperties.init().mergeSemantic(false).build();
            ODataEntry entry = EntityProvider.readEntry(requestContentType, uriInfo.getTargetEntitySet(), content, properties);

            Map<String, Object> data = entry.getProperties();

            //set order address
            Map<String, Object> orderAddress = (Map<String, Object>) data.get("AddressInfo");
            Address address = new Address();
            address.setStreet((String) orderAddress.get("Street"));
            address.setCity((String) orderAddress.get("City"));
            address.setCounty((String) orderAddress.get("County"));
            address.setCountry((String) orderAddress.get("Country"));
            oDataOrder.setAddress(address);

            //set order date
            Instant dateTime = ((Calendar) data.get("DateTime")).toInstant();
            LocalDateTime orderDate = LocalDateTime.ofInstant(dateTime, ZoneId.systemDefault());
            oDataOrder.setDateTime(orderDate);

            //set order details
            List<ODataOrderDetail> oDataOrderDetails = new ArrayList<>();
            for (ODataEntry oDataEntry : ((ODataDeltaFeedImpl) data.get("OrderDetails")).getEntries()) {
                ODataOrderDetail oDataOrderDetail = new ODataOrderDetail();
                oDataOrderDetail.setProductId((Integer) oDataEntry.getProperties().get("ProductId"));
                oDataOrderDetail.setQuantity((Integer) oDataEntry.getProperties().get("Quantity"));
                oDataOrderDetails.add(oDataOrderDetail);
            }
            oDataOrder.setODataOrderDetails(oDataOrderDetails);

            Map<String, Object> savedOrder = dataStore.createOrder(oDataOrder);

            return EntityProvider.writeEntry(contentType, uriInfo.getStartEntitySet(), savedOrder,
                    EntityProviderWriteProperties.serviceRoot(getContext().getPathInfo().getServiceRoot()).build());
        }

        throw new ODataNotImplementedException();
    }

    private ODataResponse getODataResponse(String contentType, EdmEntitySet entitySet, Map<String, Object> data) throws ODataException {
        URI serviceRoot = getContext().getPathInfo().getServiceRoot();
        ODataEntityProviderPropertiesBuilder propertiesBuilder =
                EntityProviderWriteProperties.serviceRoot(serviceRoot);

        return EntityProvider.writeEntry(contentType, entitySet, data, propertiesBuilder.build());
    }

    private int getKeyValue(KeyPredicate key) throws ODataException {
        EdmProperty property = key.getProperty();
        EdmSimpleType type = (EdmSimpleType) property.getType();
        return type.valueOfString(key.getLiteral(), EdmLiteralKind.DEFAULT, property.getFacets(), Integer.class);
    }
}