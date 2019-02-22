package ro.msg.learning.shop.odata.core;

import org.apache.olingo.odata2.api.edm.EdmMultiplicity;
import org.apache.olingo.odata2.api.edm.EdmSimpleTypeKind;
import org.apache.olingo.odata2.api.edm.FullQualifiedName;
import org.apache.olingo.odata2.api.edm.provider.Association;
import org.apache.olingo.odata2.api.edm.provider.AssociationEnd;
import org.apache.olingo.odata2.api.edm.provider.AssociationSet;
import org.apache.olingo.odata2.api.edm.provider.AssociationSetEnd;
import org.apache.olingo.odata2.api.edm.provider.ComplexProperty;
import org.apache.olingo.odata2.api.edm.provider.ComplexType;
import org.apache.olingo.odata2.api.edm.provider.EdmProvider;
import org.apache.olingo.odata2.api.edm.provider.EntityContainer;
import org.apache.olingo.odata2.api.edm.provider.EntityContainerInfo;
import org.apache.olingo.odata2.api.edm.provider.EntitySet;
import org.apache.olingo.odata2.api.edm.provider.EntityType;
import org.apache.olingo.odata2.api.edm.provider.Facets;
import org.apache.olingo.odata2.api.edm.provider.Key;
import org.apache.olingo.odata2.api.edm.provider.NavigationProperty;
import org.apache.olingo.odata2.api.edm.provider.Property;
import org.apache.olingo.odata2.api.edm.provider.PropertyRef;
import org.apache.olingo.odata2.api.edm.provider.Schema;
import org.apache.olingo.odata2.api.edm.provider.SimpleProperty;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ShopEdmProvider extends EdmProvider {

    private static final String ENTITY_NAME_PRODUCT = "Product";
    private static final String ENTITY_NAME_ORDER = "Order";
    private static final String ENTITY_NAME_ORDER_DETAIL = "OrderDetail";

    private static final String COMPLEX_TYPE_NAME_ADDRESS = "Address";

    private static final String NAMESPACE = "ro.msg.learning.shop.OData";

    private static final FullQualifiedName ENTITY_TYPE_PRODUCT = new FullQualifiedName(NAMESPACE, ENTITY_NAME_PRODUCT);
    private static final FullQualifiedName ENTITY_TYPE_ORDER = new FullQualifiedName(NAMESPACE, ENTITY_NAME_ORDER);
    private static final FullQualifiedName ENTITY_TYPE_ORDER_DETAIL = new FullQualifiedName(NAMESPACE, ENTITY_NAME_ORDER_DETAIL);

    private static final FullQualifiedName COMPLEX_TYPE_ADDRESS = new FullQualifiedName(NAMESPACE, COMPLEX_TYPE_NAME_ADDRESS);

    private static final FullQualifiedName ASSOCIATION_ORDER_DETAIL_ORDER = new FullQualifiedName(NAMESPACE, "OrderDetail_Order_Order_OrderDetail");
    private static final FullQualifiedName ASSOCIATION_ORDER_DETAIL_PRODUCT = new FullQualifiedName(NAMESPACE, "OrderDetail_Product_Product_OrderDetail");

    private static final String ROLE_ORDER_ORDER_DETAIL = "Order_OrderDetail";
    private static final String ROLE_ORDER_DETAIL_ORDER = "OrderDetail_Order";

    private static final String ROLE_PRODUCT_ORDER_DETAIL = "Product_OrderDetail";
    private static final String ROLE_ORDER_DETAIL_PRODUCT = "OrderDetail_Product";

    private static final String ENTITY_CONTAINER = "ODataShopEntityContainer";

    private static final String ASSOCIATION_SET_ORDERS_ORDER_DETAILS = "Orders_OrderDetails";
    private static final String ASSOCIATION_SET_PRODUCTS_ORDER_DETAILS = "Products_OrderDetails";

    static final String ENTITY_SET_NAME_ORDERS = "Orders";
    static final String ENTITY_SET_NAME_PRODUCTS = "Products";
    static final String ENTITY_SET_NAME_ORDER_DETAILS = "OrderDetails";

    @Override
    public List<Schema> getSchemas() {
        List<Schema> schemas = new ArrayList<>();

        Schema schema = new Schema();
        schema.setNamespace(NAMESPACE);

        List<EntityType> entityTypes = new ArrayList<>();
        entityTypes.add(getEntityType(ENTITY_TYPE_PRODUCT));
        entityTypes.add(getEntityType(ENTITY_TYPE_ORDER));
        entityTypes.add(getEntityType(ENTITY_TYPE_ORDER_DETAIL));
        schema.setEntityTypes(entityTypes);

        List<ComplexType> complexTypes = new ArrayList<>();
        complexTypes.add(getComplexType(COMPLEX_TYPE_ADDRESS));
        schema.setComplexTypes(complexTypes);

        List<Association> associations = new ArrayList<>();
        associations.add(getAssociation(ASSOCIATION_ORDER_DETAIL_ORDER));
        associations.add(getAssociation(ASSOCIATION_ORDER_DETAIL_PRODUCT));
        schema.setAssociations(associations);

        List<EntityContainer> entityContainers = new ArrayList<>();
        EntityContainer entityContainer = new EntityContainer();
        entityContainer.setName(ENTITY_CONTAINER).setDefaultEntityContainer(true);

        List<EntitySet> entitySets = new ArrayList<>();
        entitySets.add(getEntitySet(ENTITY_CONTAINER, ENTITY_SET_NAME_ORDERS));
        entitySets.add(getEntitySet(ENTITY_CONTAINER, ENTITY_SET_NAME_PRODUCTS));
        entitySets.add(getEntitySet(ENTITY_CONTAINER, ENTITY_SET_NAME_ORDER_DETAILS));
        entityContainer.setEntitySets(entitySets);

        List<AssociationSet> associationSets = new ArrayList<>();
        associationSets.add(getAssociationSet(ENTITY_CONTAINER, ASSOCIATION_ORDER_DETAIL_ORDER, ENTITY_SET_NAME_ORDER_DETAILS, ROLE_ORDER_DETAIL_ORDER));
        associationSets.add(getAssociationSet(ENTITY_CONTAINER, ASSOCIATION_ORDER_DETAIL_PRODUCT, ENTITY_SET_NAME_PRODUCTS, ROLE_ORDER_DETAIL_PRODUCT));
        entityContainer.setAssociationSets(associationSets);

        entityContainers.add(entityContainer);
        schema.setEntityContainers(entityContainers);

        schemas.add(schema);

        return schemas;
    }

    @Override
    public EntityType getEntityType(FullQualifiedName edmFQName) {
        if (NAMESPACE.equals(edmFQName.getNamespace())) {

            if (ENTITY_TYPE_PRODUCT.getName().equals(edmFQName.getName())) {

                //Properties
                List<Property> properties = new ArrayList<>();
                properties.add(new SimpleProperty().setName("Id").setType(EdmSimpleTypeKind.Int32).setFacets(new Facets().setNullable(false)));
                properties.add(new SimpleProperty().setName("Name").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(false).setMaxLength(100)));
                properties.add(new SimpleProperty().setName("Description").setType(EdmSimpleTypeKind.String));
                properties.add(new SimpleProperty().setName("Price").setType(EdmSimpleTypeKind.Decimal));
                properties.add(new SimpleProperty().setName("Weight").setType(EdmSimpleTypeKind.Double));

                //Navigation Properties
                List<NavigationProperty> navigationProperties =
                        getNavigationProperties(ENTITY_SET_NAME_ORDER_DETAILS, ASSOCIATION_ORDER_DETAIL_PRODUCT, ROLE_PRODUCT_ORDER_DETAIL, ROLE_ORDER_DETAIL_PRODUCT);

                return new EntityType().setName(ENTITY_TYPE_PRODUCT.getName())
                        .setProperties(properties)
                        .setKey(setIdAsKey())
                        .setNavigationProperties(navigationProperties);

            } else if (ENTITY_TYPE_ORDER.getName().equals(edmFQName.getName())) {

                //Properties
                List<Property> properties = new ArrayList<>();
                properties.add(new SimpleProperty().setName("Id").setType(EdmSimpleTypeKind.Int32).setFacets(new Facets().setNullable(false)));
                properties.add(new ComplexProperty().setName("AddressInfo").setType(COMPLEX_TYPE_ADDRESS));
                properties.add(new SimpleProperty().setName("DateTime").setType(EdmSimpleTypeKind.DateTime));

                //Navigation Properties
                List<NavigationProperty> navigationProperties =
                        getNavigationProperties(ENTITY_SET_NAME_ORDER_DETAILS, ASSOCIATION_ORDER_DETAIL_ORDER, ROLE_ORDER_ORDER_DETAIL, ROLE_ORDER_DETAIL_ORDER);

                return new EntityType().setName(ENTITY_TYPE_ORDER.getName())
                        .setProperties(properties)
                        .setKey(setIdAsKey())
                        .setNavigationProperties(navigationProperties);

            } else if (ENTITY_TYPE_ORDER_DETAIL.getName().equals(edmFQName.getName())) {

                //Properties
                List<Property> properties = new ArrayList<>();
                properties.add(new SimpleProperty().setName("Id").setType(EdmSimpleTypeKind.Int32).setFacets(new Facets().setNullable(false)));
                properties.add(new SimpleProperty().setName("Quantity").setType(EdmSimpleTypeKind.Int32));
                properties.add(new SimpleProperty().setName("ProductId").setType(EdmSimpleTypeKind.Int32));
                properties.add(new SimpleProperty().setName("OrderId").setType(EdmSimpleTypeKind.Int32));

                //Navigation Properties
                List<NavigationProperty> navigationProperties =
                        getNavigationProperties(ENTITY_NAME_ORDER, ASSOCIATION_ORDER_DETAIL_ORDER, ROLE_ORDER_DETAIL_ORDER, ROLE_ORDER_ORDER_DETAIL);
                navigationProperties.add(new NavigationProperty().setName(ENTITY_NAME_PRODUCT)
                        .setRelationship(ASSOCIATION_ORDER_DETAIL_PRODUCT).setFromRole(ROLE_ORDER_DETAIL_PRODUCT).setToRole(ROLE_PRODUCT_ORDER_DETAIL));

                return new EntityType().setName(ENTITY_TYPE_ORDER_DETAIL.getName())
                        .setProperties(properties)
                        .setKey(setIdAsKey())
                        .setNavigationProperties(navigationProperties);
            }
        }

        return null;
    }

    @Override
    public ComplexType getComplexType(FullQualifiedName edmFQName) {
        if (NAMESPACE.equals(edmFQName.getNamespace()) &&
                COMPLEX_TYPE_ADDRESS.getName().equals(edmFQName.getName())) {
            List<Property> properties = new ArrayList<>();
            properties.add(new SimpleProperty().setName("Street").setType(EdmSimpleTypeKind.String));
            properties.add(new SimpleProperty().setName("City").setType(EdmSimpleTypeKind.String));
            properties.add(new SimpleProperty().setName("County").setType(EdmSimpleTypeKind.String));
            properties.add(new SimpleProperty().setName("Country").setType(EdmSimpleTypeKind.String));
            return new ComplexType().setName(COMPLEX_TYPE_ADDRESS.getName()).setProperties(properties);
        }

        return null;
    }

    @Override
    public Association getAssociation(FullQualifiedName edmFQName) {
        if (NAMESPACE.equals(edmFQName.getNamespace())) {
            if (ASSOCIATION_ORDER_DETAIL_ORDER.getName().equals(edmFQName.getName())) {
                return new Association().setName(ASSOCIATION_ORDER_DETAIL_ORDER.getName())
                        .setEnd1(new AssociationEnd().setType(ENTITY_TYPE_ORDER).setRole(ROLE_ORDER_ORDER_DETAIL).setMultiplicity(EdmMultiplicity.ONE))
                        .setEnd2(new AssociationEnd().setType(ENTITY_TYPE_ORDER_DETAIL).setRole(ROLE_ORDER_DETAIL_ORDER).setMultiplicity(EdmMultiplicity.MANY));
            } else if (ASSOCIATION_ORDER_DETAIL_PRODUCT.getName().equals(edmFQName.getName())) {
                return new Association().setName(ASSOCIATION_ORDER_DETAIL_PRODUCT.getName())
                        .setEnd1(new AssociationEnd().setType(ENTITY_TYPE_PRODUCT).setRole(ROLE_PRODUCT_ORDER_DETAIL).setMultiplicity(EdmMultiplicity.ONE))
                        .setEnd2(new AssociationEnd().setType(ENTITY_TYPE_ORDER_DETAIL).setRole(ROLE_ORDER_DETAIL_PRODUCT).setMultiplicity(EdmMultiplicity.MANY));
            }
        }
        return null;
    }

    @Override
    public EntitySet getEntitySet(String entityContainer, String name) {
        if (ENTITY_CONTAINER.equals(entityContainer)) {
            if (ENTITY_SET_NAME_PRODUCTS.equals(name)) {
                return new EntitySet().setName(name).setEntityType(ENTITY_TYPE_PRODUCT);
            } else if (ENTITY_SET_NAME_ORDERS.equals(name)) {
                return new EntitySet().setName(name).setEntityType(ENTITY_TYPE_ORDER);
            } else if (ENTITY_SET_NAME_ORDER_DETAILS.equals(name)) {
                return new EntitySet().setName(name).setEntityType(ENTITY_TYPE_ORDER_DETAIL);
            }
        }
        return null;
    }

    @Override
    public AssociationSet getAssociationSet(String entityContainer, FullQualifiedName association,
                                            String sourceEntitySetName, String sourceEntitySetRole) {
        if (ENTITY_CONTAINER.equals(entityContainer)) {
            if (ASSOCIATION_ORDER_DETAIL_ORDER.equals(association)) {
                return new AssociationSet().setName(ASSOCIATION_SET_ORDERS_ORDER_DETAILS)
                        .setAssociation(ASSOCIATION_ORDER_DETAIL_ORDER)
                        .setEnd1(new AssociationSetEnd().setRole(ROLE_ORDER_DETAIL_ORDER).setEntitySet(ENTITY_SET_NAME_ORDER_DETAILS))
                        .setEnd2(new AssociationSetEnd().setRole(ROLE_ORDER_ORDER_DETAIL).setEntitySet(ENTITY_SET_NAME_ORDERS));
            } else if (ASSOCIATION_ORDER_DETAIL_PRODUCT.equals(association)) {
                return new AssociationSet().setName(ASSOCIATION_SET_PRODUCTS_ORDER_DETAILS)
                        .setAssociation(ASSOCIATION_ORDER_DETAIL_PRODUCT)
                        .setEnd1(new AssociationSetEnd().setRole(ROLE_ORDER_DETAIL_PRODUCT).setEntitySet(ENTITY_SET_NAME_ORDER_DETAILS))
                        .setEnd2(new AssociationSetEnd().setRole(ROLE_PRODUCT_ORDER_DETAIL).setEntitySet(ENTITY_SET_NAME_PRODUCTS));
            }
        }
        return null;
    }

    @Override
    public EntityContainerInfo getEntityContainerInfo(String name) {
        if (name == null || ENTITY_CONTAINER.equals(name)) {
            return new EntityContainerInfo().setName(ENTITY_CONTAINER).setDefaultEntityContainer(true);
        }

        return null;
    }

    private Key setIdAsKey() {
        List<PropertyRef> keyProperties = new ArrayList<>();
        keyProperties.add(new PropertyRef().setName("Id"));
        return new Key().setKeys(keyProperties);
    }

    private List<NavigationProperty> getNavigationProperties(String entityName, FullQualifiedName associationName,
                                                             String role1, String role2) {
        List<NavigationProperty> navigationProperties = new ArrayList<>();
        navigationProperties.add(new NavigationProperty().setName(entityName)
                .setRelationship(associationName).setFromRole(role1).setToRole(role2));
        return navigationProperties;
    }
}
