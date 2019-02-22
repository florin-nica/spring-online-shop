package ro.msg.learning.shop.strategy;

import lombok.SneakyThrows;
import one.util.streamex.EntryStream;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import ro.msg.learning.shop.dto.DistanceMatrixDto;
import ro.msg.learning.shop.dto.ElementDto;
import ro.msg.learning.shop.dto.in.OrderDtoIn;
import ro.msg.learning.shop.exception.StockNotFoundException;
import ro.msg.learning.shop.model.Address;
import ro.msg.learning.shop.model.Location;
import ro.msg.learning.shop.model.LocationDistance;
import ro.msg.learning.shop.model.LocationProductQuantity;
import ro.msg.learning.shop.repository.LocationRepository;
import ro.msg.learning.shop.repository.ProductRepository;

import javax.net.ssl.SSLContext;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ClosestLocationStrategy extends BaseLocationStrategy implements LocationStrategy {

    private static final String API_KEY = "AIzaSyBPtBPSLKNAwOfu3zq48XaIVyLlscgdz5M";

    public ClosestLocationStrategy(LocationRepository locationRepository, ProductRepository productRepository) {
        super(locationRepository, productRepository);
    }

    @Override
    public List<LocationProductQuantity> getLocationProductQuantity(OrderDtoIn orderDtoIn) {

        setLocationsWithAllProductsAndOrderedProducts(orderDtoIn);

        if (locationsWithAllProducts != null && !locationsWithAllProducts.isEmpty()) {
            Address address = orderDtoIn.getAddress();

            Location closestLocation =
                    getLocationByShortestDistance(locationsWithAllProducts,
                            address.getStreet() + address.getCity() + address.getCountry());

            return orderedProducts
                    .parallelStream()
                    .map(product -> new LocationProductQuantity(closestLocation, product, productIdQuantityMap.get(product.getId())))
                    .collect(Collectors.toList());
        }

        throw new StockNotFoundException("No stocks found");
    }

    private Location getLocationByShortestDistance(List<Location> origins, String destination) {

        final String originsString =
                origins.stream()
                        .map(location -> location.getAddress().getStreet() + location.getAddress().getCity() + location.getAddress().getCountry())
                        .collect(Collectors.joining("|"));

        final String uri = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" +
                originsString + "&destinations=" + destination + "&key=" + API_KEY + "\n";

        RestTemplate restTemplate = getRestTemplateBypassingHostNameVerification();
        DistanceMatrixDto result = restTemplate.getForObject(uri, DistanceMatrixDto.class);

        if (result != null && result.getStatus() != null && result.getStatus().equals("OK")) {
            List<ElementDto> elementDtos = result.getRows().stream()
                    .map(row -> row.getElements().get(0))
                    .collect(Collectors.toList());

            List<LocationDistance> locationDistances =
                    EntryStream.of(elementDtos)
                            .mapKeyValue((index, elementDto) ->
                                    new LocationDistance(origins.get(index), elementDto.getDistance().getValue()))
                            .filter(locationDistance -> locationDistance.getDistance() != null)
                            .sorted(Comparator.comparingInt(LocationDistance::getDistance))
                            .collect(Collectors.toList());

            return locationDistances.get(0).getLocation();
        }

        throw new StockNotFoundException("No location close to customer found");
    }

    @SneakyThrows
    private RestTemplate getRestTemplateBypassingHostNameVerification() {
        SSLContext sslContext = new SSLContextBuilder()
                .loadTrustMaterial(null, (certificate, authType) -> true).build();

        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLContext(sslContext)
                .setSSLHostnameVerifier(new NoopHostnameVerifier())
                .build();
        HttpComponentsClientHttpRequestFactory requestFactory
                = new HttpComponentsClientHttpRequestFactory();

        requestFactory.setHttpClient(httpClient);

        return new RestTemplate(requestFactory);
    }
}
