package basket.model

import basket.toMap
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import product.Inventory
import java.math.BigDecimal

internal class BasketTest {

    @Test
    fun `getBasketTotal - returns 0 when no items are in the basket`() {
        assertEquals(Basket().getBasketTotal(), BigDecimal.ZERO)
    }

    @Test
    fun `getBasketTotal - returns the accumulated sum of items in the basket`() {
        val products = Inventory.getProducts().values

        val productOne = products.toList()[3]
        val productOneQty = 10

        val productTwo = products.toList()[0]
        val productTwoQty = 1

        val productThree = products.toList()[2]
        val productThreeQty = 0

        val items = setOf(
            BasketItem(productOne, productOneQty),
            BasketItem(productTwo, productTwoQty),
            BasketItem(productThree, productThreeQty)
        ).toMap()

        assertEquals(
            Basket(items).getBasketTotal(),
            productOne.unitPrice.multiply(BigDecimal(productOneQty))
                .add(productTwo.unitPrice.multiply(BigDecimal(productTwoQty)))
                .add(productThree.unitPrice.multiply(BigDecimal(productThreeQty)))
        )
    }

    @Test
    fun `addItem - adds a new item to the basket if it is not in there`() {
        val products = Inventory.getProducts().values

        val productOne = products.toList()[3]
        val productOneQty = 10

        val productTwo = products.toList()[0]
        val productTwoQty = 1

        val productThree = products.toList()[2]

        val items = setOf(
            BasketItem(productOne, productOneQty),
            BasketItem(productTwo, productTwoQty),
        ).toMap()

        val updatedBasket = Basket(items).addItem(productThree.itemSku)
        assertTrue(updatedBasket.items.containsKey(productThree.itemSku))
    }

    @Test
    fun `addItem - increases the quantity of an item in the basket`() {
        val products = Inventory.getProducts().values

        val productOne = products.toList()[3]
        val productOneQty = 10

        val productTwo = products.toList()[0]
        val productTwoQty = 1

        val items = setOf(
            BasketItem(productOne, productOneQty),
            BasketItem(productTwo, productTwoQty),
        ).toMap()

        val updatedBasket = Basket(items).addItem(productTwo.itemSku)
        assertEquals(productTwoQty + 1, updatedBasket.items[productTwo.itemSku]?.quantity)
    }
}
