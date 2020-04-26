package com.asimgasimzade.mastermind.domain

import com.asimgasimzade.mastermind.domain.Product
import com.asimgasimzade.mastermind.domain.ProductListHeader

data class ProductListResponse(
    val header: ProductListHeader,
    val filters: List<String>,
    val products: List<Product>
)
