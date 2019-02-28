package ro.msg.learning.shop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ro.msg.learning.shop.service.ProductService;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping(path = "/products")
    public String getStockByLocationId(
            @PageableDefault(value = 9) Pageable pageable,
            Model model) {

        model.addAttribute("page", productService.getPaginatedProducts(pageable));

        return "products";
    }
}
