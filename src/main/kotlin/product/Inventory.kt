package product

import product.model.Product
import java.math.BigDecimal

object Inventory {

    private val PRODUCTS = setOf(
        Product(
            'A',
            BigDecimal("0.50")
        ),
        Product(
            'B',
            BigDecimal("0.60")
        ),
        Product(
            'C',
            BigDecimal("0.25")
        ),
        Product(
            'D',
            BigDecimal("1.50")
        ),
        Product(
            'E',
            BigDecimal("2.00")
        ),
    )

    val itemSkus: Set<Char> = getProducts().values.map { it.itemSku }.toSet()

    fun getProducts(): Map<Char, Product> = PRODUCTS.associateBy { it.itemSku }

    fun findProductBySku(itemSku: Char): Product? = getProducts()[itemSku]
}
