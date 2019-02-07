package ro.msg.learning.shop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ro.msg.learning.shop.dto.StockDto;
import ro.msg.learning.shop.model.Stock;
import ro.msg.learning.shop.service.StockService;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @GetMapping(path = "/stocks/{locationId}", produces = {"text/csv"})
    public List<StockDto> getStockByLocationId(@PathVariable Integer locationId, HttpServletResponse response) {
        response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=stocks.csv");

        return stockService.getStocksByLocationId(locationId)
                .parallelStream()
                .map(this::mapStockToStockDto)
                .collect(Collectors.toList());
    }

    private StockDto mapStockToStockDto(Stock stock) {
        return new StockDto(
                stock.getId(),
                stock.getQuantity(),
                stock.getLocation().getId(),
                stock.getProduct().getId()
        );
    }
}
