package specialprice.model

import basket.model.Basket
import basket.model.BasketItem
import basket.toMap
import org.junit.jupiter.api.Test
import product.Inventory
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class SpecialPriceConditionTest {

    @Test
    fun `matches - returns 1 for a basket that contains an expected sku with the desired quantity`() {

        val requiredQuantity = 2

        val items = setOf(
            BasketItem(PRODUCT_ONE, requiredQuantity),
            BasketItem(PRODUCT_TWO, 1),
            BasketItem(PRODUCT_THREE, 1)
        ).toMap()

        val basket = Basket(items)

        val specialPrice = SpecialPriceCondition(requiredQuantity, PRODUCT_ONE.itemSku)

        assertEquals(1, specialPrice.matches(basket)?.second)
    }

    @Test
    fun `matches - returns 3 for a basket that contains an expected sku with triple the desired quantity`() {

        val requiredQuantity = 2

        val multiplier = 3
        val items = setOf(
            BasketItem(PRODUCT_ONE, requiredQuantity * multiplier),
            BasketItem(PRODUCT_TWO, 1),
            BasketItem(PRODUCT_THREE, 1)
        ).toMap()

        val basket = Basket(items)

        val specialPrice = SpecialPriceCondition(requiredQuantity, PRODUCT_ONE.itemSku)

        assertEquals(multiplier, specialPrice.matches(basket)?.second)
    }

    @Test
    fun `matches - returns 0 for a basket that contains an expected sku with less than the desired quantity`() {

        val requiredQuantity = 2

        val items = setOf(
            BasketItem(PRODUCT_ONE, 1),
            BasketItem(PRODUCT_TWO, 1),
            BasketItem(PRODUCT_THREE, 1)
        ).toMap()

        val basket = Basket(items)

        val specialPrice = SpecialPriceCondition(requiredQuantity, PRODUCT_ONE.itemSku)

        assertEquals(0, specialPrice.matches(basket)?.second)
    }

    @Test
    fun `matches - returns null for a basket that does not contain the expected sku`() {

        val requiredQuantity = 2

        val items = setOf(
            BasketItem(PRODUCT_TWO, 1),
            BasketItem(PRODUCT_THREE, 1)
        ).toMap()

        val basket = Basket(items)

        val specialPrice = SpecialPriceCondition(requiredQuantity, PRODUCT_ONE.itemSku)

        assertNull(specialPrice.matches(basket)?.second)
    }

    private companion object {
        private val products = Inventory.getProducts().values
        val PRODUCT_ONE = products.toList()[3]
        val PRODUCT_TWO = products.toList()[0]
        val PRODUCT_THREE = products.toList()[2]
    }
}
