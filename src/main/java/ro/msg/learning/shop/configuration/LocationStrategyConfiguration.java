package ro.msg.learning.shop.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ro.msg.learning.shop.repository.LocationRepository;
import ro.msg.learning.shop.repository.ProductRepository;
import ro.msg.learning.shop.strategy.LocationStrategies;
import ro.msg.learning.shop.strategy.LocationStrategy;
import ro.msg.learning.shop.strategy.SingleLocationStrategy;

@Configuration
@RequiredArgsConstructor
public class LocationStrategyConfiguration {

    @Value("${ro.msg.shop.location-strategy}")
    private LocationStrategies strategy;

    private final LocationRepository locationRepository;
    private final ProductRepository productRepository;

    @Bean
    public LocationStrategy singleLocationStrategy() {
        if (strategy == LocationStrategies.SINGLE_LOCATION) {
            return new SingleLocationStrategy(locationRepository, productRepository);
        }
        return new SingleLocationStrategy(locationRepository, productRepository);
    }
}
