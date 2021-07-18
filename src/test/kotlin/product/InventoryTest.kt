package product

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import kotlin.test.assertNull

/**
 * I normally wouldn't test something like this.
 * However, I added this test suit to make sure that the contract of the products mentioned in `Lending Works Technical Task.pdf` was met.
 *
 * In reality products would be in a database. So - an integration test would be more suitable for this.
 */
internal class InventoryTest {

    @Test
    fun `getProducts - returns the expected products`() {

        val products = Inventory.getProducts()

        assertEquals(products.size, 5)
        assertEquals(products.values.map { it.itemSku }.toSet(), Inventory.itemSkus)
        assertEquals(
            products.values.map { it.unitPrice }.toSet(),
            setOf(
                BigDecimal("0.50"),
                BigDecimal("0.60"),
                BigDecimal("0.25"),
                BigDecimal("1.50"),
                BigDecimal("2.00"),
            )
        )
    }

    @Test
    fun `findProductBySku - returns null for non existent product`() {
        val product = Inventory.findProductBySku('z')
        assertNull(product)
    }

    @Test
    fun `findProductBySku - returns a product for accounted skus`() {
        val products = Inventory.itemSkus.mapNotNull(Inventory::findProductBySku)
        assertEquals(products.size, Inventory.itemSkus.size)
    }
}
