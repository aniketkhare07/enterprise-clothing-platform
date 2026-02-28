package com.clothing.enterprise.product.service.impl;

import com.clothing.enterprise.common.exception.BusinessException;
import com.clothing.enterprise.common.exception.ResourceNotFoundException;
import com.clothing.enterprise.product.domain.CategoryEntity;
import com.clothing.enterprise.product.domain.ProductEntity;
import com.clothing.enterprise.product.domain.ProductVariantEntity;
import com.clothing.enterprise.product.dto.CreateProductRequest;
import com.clothing.enterprise.product.dto.ProductResponse;
import com.clothing.enterprise.product.dto.ProductVariantDto;
import com.clothing.enterprise.product.repository.CategoryRepository;
import com.clothing.enterprise.product.repository.ProductRepository;
import com.clothing.enterprise.product.repository.ProductVariantRepository;
import com.clothing.enterprise.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductVariantRepository productVariantRepository;

    @Transactional
    public ProductResponse createProduct(CreateProductRequest request) {
        // 1. Validate SKUs unique
        if (request.variants() != null) {
            for (CreateProductRequest.VariantRequest v : request.variants()) {
                if (productVariantRepository.existsBySku(v.sku())) {
                    throw new BusinessException("SKU already exists: " + v.sku());
                }
            }
        }

        // 2. Find or Create Category
        CategoryEntity category = categoryRepository.findByName(request.categoryName())
                .orElseGet(() -> categoryRepository.save(
                        CategoryEntity.builder()
                                .name(request.categoryName())
                                .description("Auto-generated")
                                .build()
                ));

        // 3. Build Product
        ProductEntity product = ProductEntity.builder()
                .name(request.name())
                .description(request.description())
                .basePrice(request.basePrice())
                .category(category)
                .imageUrl(request.imageUrl())
                .build();

        // 4. Add Variants
        if (request.variants() != null) {
            request.variants().forEach(v -> {
                ProductVariantEntity variant = ProductVariantEntity.builder()
                        .sku(v.sku())
                        .size(v.size())
                        .color(v.color())
                        .priceAdjustment(v.priceAdjustment() != null ? v.priceAdjustment() : BigDecimal.ZERO)
                        .build();
                product.addVariant(variant);
            });
        }

        ProductEntity savedProduct = productRepository.save(product);
        return mapToResponse(savedProduct);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    private ProductResponse mapToResponse(ProductEntity product) {
        List<ProductResponse.VariantResponse> variantResponses = product.getVariants().stream()
                .map(v -> new ProductResponse.VariantResponse(
                        v.getId(),
                        v.getSku(),
                        v.getSize(),
                        v.getColor(),
                        v.getPriceAdjustment()
                ))
                .toList();

        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getBasePrice(),
                product.getCategory().getName(),
                product.getImageUrl(),
                variantResponses
        );
    }

    @Transactional(readOnly = true)
    public ProductResponse getProductById(java.util.UUID id) {
        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));

        return mapToResponse(product);
    }

    @Transactional(readOnly = true)
    public ProductVariantDto getProductVariantBySku(String sku) {
        ProductVariantEntity variant = productVariantRepository.findBySku(sku)
                .orElseThrow(() -> new ResourceNotFoundException("Variant not found for SKU: " + sku));

        BigDecimal finalPrice = variant.getProduct().getBasePrice();
        if (variant.getPriceAdjustment() != null) {
            finalPrice = finalPrice.add(variant.getPriceAdjustment());
        }

        return new ProductVariantDto(
                variant.getProduct().getId(),
                variant.getProduct().getName(),
                finalPrice,
                variant.getSku(),
                variant.getSize(),
                variant.getColor()
        );
    }
}