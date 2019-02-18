package ro.msg.learning.shop.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ro.msg.learning.shop.repository.LocationRepository;
import ro.msg.learning.shop.repository.ProductRepository;
import ro.msg.learning.shop.strategy.ClosestLocationStrategy;
import ro.msg.learning.shop.strategy.LocationStrategy;
import ro.msg.learning.shop.strategy.LocationStrategyType;
import ro.msg.learning.shop.strategy.SingleLocationStrategy;

@Configuration
@RequiredArgsConstructor
public class LocationStrategyConfiguration {

    @Value("${ro.msg.shop.location-strategy}")
    private LocationStrategyType strategy;

    private final LocationRepository locationRepository;
    private final ProductRepository productRepository;

    @Bean
    public LocationStrategy singleLocationStrategy() {
        switch (strategy) {
            case SINGLE_LOCATION:
                return new SingleLocationStrategy(locationRepository, productRepository);
            case CLOSEST_LOCATION:
                return new ClosestLocationStrategy(locationRepository, productRepository);
            default:
                return new SingleLocationStrategy(locationRepository, productRepository);
        }
    }
}
