package checkout.model

import basket.model.Basket
import basket.model.BasketItem
import basket.toMap
import org.junit.jupiter.api.Test
import product.Inventory
import specialprice.model.SpecialPrice
import specialprice.model.SpecialPriceCondition
import java.math.BigDecimal
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

internal class CheckoutTest {

    @Test
    fun `calculateTotal - returns the basket total adjusted by Meal deal special price`() {

        val items = setOf(
            BasketItem(PRODUCT_ONE, 1),
            BasketItem(PRODUCT_TWO, 2),
            BasketItem(PRODUCT_THREE, 1)
        ).toMap()

        val basket = Basket(items)

        val specialPriceConditionOne = SpecialPriceCondition(1, PRODUCT_ONE.itemSku)
        val specialPriceConditionTwo = SpecialPriceCondition(1, PRODUCT_THREE.itemSku)
        val conditions = setOf(specialPriceConditionOne, specialPriceConditionTwo)
        val specialPriceValue = BigDecimal("3.25")
        val specialPrice = SpecialPrice("Buy D and E for £3.25", conditions) { specialPriceValue }
        val checkout = Checkout(basket, setOf(specialPrice))

        assertEquals(checkout.calculateTotal(), basket.getBasketTotal().minus(specialPriceValue))
    }

    @Test
    fun `calculateTotal - returns the basket total adjusted by Buy n get 1 special price`() {

        val quantity = 4
        val items = setOf(
            BasketItem(PRODUCT_ONE, 2),
            BasketItem(PRODUCT_THREE, quantity)
        ).toMap()

        val basket = Basket(items)

        val specialPriceConditionOne = SpecialPriceCondition(quantity, PRODUCT_THREE.itemSku)
        val conditions = setOf(specialPriceConditionOne)
        val specialPriceValue = PRODUCT_THREE.unitPrice.multiply(BigDecimal(quantity - 1))
        val specialPrice = SpecialPrice("Buy 3, get one free", conditions) { specialPriceValue }
        val checkout = Checkout(basket, setOf(specialPrice))

        assertEquals(checkout.calculateTotal(), basket.getBasketTotal().minus(specialPriceValue))
    }

    @Test
    fun `calculateTotal - returns the basket total adjusted by Buy 2 for 1 special price`() {

        val items = setOf(
            BasketItem(PRODUCT_ONE, 4),
            BasketItem(PRODUCT_THREE, 3)
        ).toMap()

        val basket = Basket(items)

        val specialPriceConditionOne = SpecialPriceCondition(2, PRODUCT_ONE.itemSku)
        val conditions = setOf(specialPriceConditionOne)
        val specialPriceValue = PRODUCT_ONE.unitPrice
        val specialPrice = SpecialPrice("2 for £1", conditions) { specialPriceValue }
        val checkout = Checkout(basket, setOf(specialPrice))

        assertEquals(checkout.calculateTotal(), basket.getBasketTotal().minus(specialPriceValue.times(BigDecimal(2))))
    }

    @Test
    fun `add - returns new checkout with added product`() {
        val items = setOf(BasketItem(PRODUCT_ONE, 1)).toMap()

        val basket = Basket(items)
        val checkout = Checkout(basket, emptySet())
        val updatedCheckout = checkout.add(PRODUCT_TWO.itemSku)

        assertNotEquals(checkout.calculateTotal(), updatedCheckout.calculateTotal())
        assertEquals(1, updatedCheckout.basket.getItem(PRODUCT_ONE.itemSku)?.quantity)
        assertEquals(1, updatedCheckout.basket.getItem(PRODUCT_TWO.itemSku)?.quantity)
    }

    @Test
    fun `add - returns new checkout with updated product quantity`() {

        val productOneInitialQuantity = 1
        val productTwoQuantity = 10
        val items = setOf(
            BasketItem(PRODUCT_ONE, productOneInitialQuantity),
            BasketItem(PRODUCT_TWO, productTwoQuantity),
        ).toMap()

        val basket = Basket(items)
        val checkout = Checkout(basket, emptySet())
        val updatedCheckout = checkout.add(PRODUCT_ONE.itemSku)

        assertNotEquals(checkout.calculateTotal(), updatedCheckout.calculateTotal())
        assertEquals(productOneInitialQuantity + 1, updatedCheckout.basket.getItem(PRODUCT_ONE.itemSku)?.quantity)
        assertEquals(productTwoQuantity, updatedCheckout.basket.getItem(PRODUCT_TWO.itemSku)?.quantity)
    }

    private companion object {
        private val products = Inventory.getProducts().values
        val PRODUCT_ONE = products.toList()[3]
        val PRODUCT_TWO = products.toList()[4]
        val PRODUCT_THREE = products.toList()[2]
    }
}
