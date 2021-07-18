package product.model

import java.math.BigDecimal

data class Product(
    val itemSku: Char,
    val unitPrice: BigDecimal
)
