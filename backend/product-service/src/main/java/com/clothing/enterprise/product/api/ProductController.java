package com.clothing.enterprise.product.api;

import com.clothing.enterprise.product.dto.CreateProductRequest;
import com.clothing.enterprise.product.dto.ProductResponse;
import com.clothing.enterprise.product.dto.ProductVariantDto;
import com.clothing.enterprise.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse createProduct(@Valid @RequestBody CreateProductRequest request) {
        return productService.createProduct(request);
    }

    @GetMapping
    public List<ProductResponse> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public ProductResponse getProductById(@PathVariable java.util.UUID id) {
        return productService.getProductById(id);
    }

    @GetMapping("/variant/{sku}")
    public ProductVariantDto getProductVariantBySku(@PathVariable String sku) {
        return productService.getProductVariantBySku(sku);
    }
}