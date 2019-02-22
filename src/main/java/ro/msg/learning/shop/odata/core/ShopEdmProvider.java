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

    private static final FullQualifiedName ENTITY_TYPE_1_1 = new FullQualifiedName(NAMESPACE, ENTITY_NAME_PRODUCT);
    private static final FullQualifiedName ENTITY_TYPE_1_2 = new FullQualifiedName(NAMESPACE, ENTITY_NAME_ORDER);
    private static final FullQualifiedName ENTITY_TYPE_1_3 = new FullQualifiedName(NAMESPACE, ENTITY_NAME_ORDER_DETAIL);

    private static final FullQualifiedName COMPLEX_TYPE_ADDRESS = new FullQualifiedName(NAMESPACE, COMPLEX_TYPE_NAME_ADDRESS);

    private static final FullQualifiedName ASSOCIATION_ORDERDETAIL_ORDER = new FullQualifiedName(NAMESPACE, "OrderDetail_Order_Order_OrderDetail");
    private static final FullQualifiedName ASSOCIATION_ORDERDETAIL_PRODUCT = new FullQualifiedName(NAMESPACE, "OrderDetail_Product_Product_OrderDetail");

    private static final String ROLE_1_1 = "Order_OrderDetail";
    private static final String ROLE_1_2 = "OrderDetail_Order";

    private static final String ROLE_2_1 = "Product_OrderDetail";
    private static final String ROLE_2_2 = "OrderDetail_Product";

    private static final String ENTITY_CONTAINER = "ODataShopEntityContainer";

    private static final String ASSOCIATION_SET_ORDERS_ORDERDETAILS = "Orders_OrderDetails";
    private static final String ASSOCIATION_SET_PRODUCTS_ORDERDETAILS = "Products_OrderDetails";

    static final String ENTITY_SET_NAME_ORDERS = "Orders";
    static final String ENTITY_SET_NAME_PRODUCTS = "Products";
    static final String ENTITY_SET_NAME_ORDER_DETAILS = "OrderDetails";

    @Override
    public List<Schema> getSchemas() {
        List<Schema> schemas = new ArrayList<>();

        Schema schema = new Schema();
        schema.setNamespace(NAMESPACE);

        List<EntityType> entityTypes = new ArrayList<>();
        entityTypes.add(getEntityType(ENTITY_TYPE_1_1));
        entityTypes.add(getEntityType(ENTITY_TYPE_1_2));
        entityTypes.add(getEntityType(ENTITY_TYPE_1_3));
        schema.setEntityTypes(entityTypes);

        List<ComplexType> complexTypes = new ArrayList<>();
        complexTypes.add(getComplexType(COMPLEX_TYPE_ADDRESS));
        schema.setComplexTypes(complexTypes);

        List<Association> associations = new ArrayList<>();
        associations.add(getAssociation(ASSOCIATION_ORDERDETAIL_ORDER));
        associations.add(getAssociation(ASSOCIATION_ORDERDETAIL_PRODUCT));
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
        associationSets.add(getAssociationSet(ENTITY_CONTAINER, ASSOCIATION_ORDERDETAIL_ORDER, ENTITY_SET_NAME_ORDER_DETAILS, ROLE_1_2));
        associationSets.add(getAssociationSet(ENTITY_CONTAINER, ASSOCIATION_ORDERDETAIL_PRODUCT, ENTITY_SET_NAME_PRODUCTS, ROLE_2_2));
        entityContainer.setAssociationSets(associationSets);

        entityContainers.add(entityContainer);
        schema.setEntityContainers(entityContainers);

        schemas.add(schema);

        return schemas;
    }

    @Override
    public EntityType getEntityType(FullQualifiedName edmFQName) {
        if (NAMESPACE.equals(edmFQName.getNamespace())) {

            if (ENTITY_TYPE_1_1.getName().equals(edmFQName.getName())) {

                //Properties
                List<Property> properties = new ArrayList<>();
                properties.add(new SimpleProperty().setName("Id").setType(EdmSimpleTypeKind.Int32).setFacets(new Facets().setNullable(false)));
                properties.add(new SimpleProperty().setName("Name").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(false).setMaxLength(100)));
                properties.add(new SimpleProperty().setName("Description").setType(EdmSimpleTypeKind.String));
                properties.add(new SimpleProperty().setName("Price").setType(EdmSimpleTypeKind.Decimal));
                properties.add(new SimpleProperty().setName("Weight").setType(EdmSimpleTypeKind.Double));

                //Navigation Properties
                List<NavigationProperty> navigationProperties =
                        getNavigationProperties(ENTITY_SET_NAME_ORDER_DETAILS, ASSOCIATION_ORDERDETAIL_PRODUCT, ROLE_2_1, ROLE_2_2);

                return new EntityType().setName(ENTITY_TYPE_1_1.getName())
                        .setProperties(properties)
                        .setKey(setIdAsKey())
                        .setNavigationProperties(navigationProperties);

            } else if (ENTITY_TYPE_1_2.getName().equals(edmFQName.getName())) {

                //Properties
                List<Property> properties = new ArrayList<>();
                properties.add(new SimpleProperty().setName("Id").setType(EdmSimpleTypeKind.Int32).setFacets(new Facets().setNullable(false)));
                properties.add(new ComplexProperty().setName("AddressInfo").setType(COMPLEX_TYPE_ADDRESS));
                properties.add(new SimpleProperty().setName("DateTime").setType(EdmSimpleTypeKind.DateTime));

                //Navigation Properties
                List<NavigationProperty> navigationProperties =
                        getNavigationProperties(ENTITY_SET_NAME_ORDER_DETAILS, ASSOCIATION_ORDERDETAIL_ORDER, ROLE_1_1, ROLE_1_2);

                return new EntityType().setName(ENTITY_TYPE_1_2.getName())
                        .setProperties(properties)
                        .setKey(setIdAsKey())
                        .setNavigationProperties(navigationProperties);

            } else if (ENTITY_TYPE_1_3.getName().equals(edmFQName.getName())) {

                //Properties
                List<Property> properties = new ArrayList<>();
                properties.add(new SimpleProperty().setName("Id").setType(EdmSimpleTypeKind.Int32).setFacets(new Facets().setNullable(false)));
                properties.add(new SimpleProperty().setName("Quantity").setType(EdmSimpleTypeKind.Int32));
                properties.add(new SimpleProperty().setName("ProductId").setType(EdmSimpleTypeKind.Int32));
                properties.add(new SimpleProperty().setName("OrderId").setType(EdmSimpleTypeKind.Int32));

                //Navigation Properties
                List<NavigationProperty> navigationProperties =
                        getNavigationProperties(ENTITY_NAME_ORDER, ASSOCIATION_ORDERDETAIL_ORDER, ROLE_1_2, ROLE_1_1);
                navigationProperties.add(new NavigationProperty().setName(ENTITY_NAME_PRODUCT)
                        .setRelationship(ASSOCIATION_ORDERDETAIL_PRODUCT).setFromRole(ROLE_2_2).setToRole(ROLE_2_1));

                return new EntityType().setName(ENTITY_TYPE_1_3.getName())
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
            if (ASSOCIATION_ORDERDETAIL_ORDER.getName().equals(edmFQName.getName())) {
                return new Association().setName(ASSOCIATION_ORDERDETAIL_ORDER.getName())
                        .setEnd1(new AssociationEnd().setType(ENTITY_TYPE_1_2).setRole(ROLE_1_1).setMultiplicity(EdmMultiplicity.ONE))
                        .setEnd2(new AssociationEnd().setType(ENTITY_TYPE_1_3).setRole(ROLE_1_2).setMultiplicity(EdmMultiplicity.MANY));
            } else if (ASSOCIATION_ORDERDETAIL_PRODUCT.getName().equals(edmFQName.getName())) {
                return new Association().setName(ASSOCIATION_ORDERDETAIL_PRODUCT.getName())
                        .setEnd1(new AssociationEnd().setType(ENTITY_TYPE_1_1).setRole(ROLE_2_1).setMultiplicity(EdmMultiplicity.ONE))
                        .setEnd2(new AssociationEnd().setType(ENTITY_TYPE_1_3).setRole(ROLE_2_2).setMultiplicity(EdmMultiplicity.MANY));
            }
        }
        return null;
    }

    @Override
    public EntitySet getEntitySet(String entityContainer, String name) {
        if (ENTITY_CONTAINER.equals(entityContainer)) {
            if (ENTITY_SET_NAME_PRODUCTS.equals(name)) {
                return new EntitySet().setName(name).setEntityType(ENTITY_TYPE_1_1);
            } else if (ENTITY_SET_NAME_ORDERS.equals(name)) {
                return new EntitySet().setName(name).setEntityType(ENTITY_TYPE_1_2);
            } else if (ENTITY_SET_NAME_ORDER_DETAILS.equals(name)) {
                return new EntitySet().setName(name).setEntityType(ENTITY_TYPE_1_3);
            }
        }
        return null;
    }

    @Override
    public AssociationSet getAssociationSet(String entityContainer, FullQualifiedName association,
                                            String sourceEntitySetName, String sourceEntitySetRole) {
        if (ENTITY_CONTAINER.equals(entityContainer)) {
            if (ASSOCIATION_ORDERDETAIL_ORDER.equals(association)) {
                return new AssociationSet().setName(ASSOCIATION_SET_ORDERS_ORDERDETAILS)
                        .setAssociation(ASSOCIATION_ORDERDETAIL_ORDER)
                        .setEnd1(new AssociationSetEnd().setRole(ROLE_1_2).setEntitySet(ENTITY_SET_NAME_ORDER_DETAILS))
                        .setEnd2(new AssociationSetEnd().setRole(ROLE_1_1).setEntitySet(ENTITY_SET_NAME_ORDERS));
            } else if (ASSOCIATION_ORDERDETAIL_PRODUCT.equals(association)) {
                return new AssociationSet().setName(ASSOCIATION_SET_PRODUCTS_ORDERDETAILS)
                        .setAssociation(ASSOCIATION_ORDERDETAIL_PRODUCT)
                        .setEnd1(new AssociationSetEnd().setRole(ROLE_2_2).setEntitySet(ENTITY_SET_NAME_ORDER_DETAILS))
                        .setEnd2(new AssociationSetEnd().setRole(ROLE_2_1).setEntitySet(ENTITY_SET_NAME_PRODUCTS));
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
