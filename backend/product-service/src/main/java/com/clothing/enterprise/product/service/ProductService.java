package com.clothing.enterprise.product.service;

import com.clothing.enterprise.product.dto.CreateProductRequest;
import com.clothing.enterprise.product.dto.ProductResponse;
import com.clothing.enterprise.product.dto.ProductVariantDto;

import java.util.List;

public interface ProductService {
    public ProductResponse createProduct(CreateProductRequest request);
    public List<ProductResponse> getAllProducts();
    public ProductResponse getProductById(java.util.UUID id);
    public ProductVariantDto getProductVariantBySku(String sku);
}
