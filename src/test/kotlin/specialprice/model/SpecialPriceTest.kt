package specialprice.model

import basket.model.Basket
import basket.model.BasketItem
import basket.toMap
import org.junit.jupiter.api.Test
import product.Inventory
import java.math.BigDecimal
import kotlin.test.assertEquals

internal class SpecialPriceTest {

    @Test
    fun `apply - returns 0 when there are no special price conditions met`() {

        val requiredQuantity = 20

        val items = setOf(
            BasketItem(PRODUCT_ONE, 1),
            BasketItem(PRODUCT_TWO, 1),
            BasketItem(PRODUCT_THREE, 1)
        ).toMap()

        val basket = Basket(items)

        val specialPriceCondition = SpecialPriceCondition(requiredQuantity, PRODUCT_ONE.itemSku)

        val specialPrice = SpecialPrice("Buy one get one free", setOf(specialPriceCondition)) { PRICE }

        assertEquals(BigDecimal("0.00"), specialPrice.calculate(basket))
    }

    @Test
    fun `apply - returns 0 when there are some special price conditions not met`() {

        val requiredQuantity = 2

        val items = setOf(
            BasketItem(PRODUCT_TWO, requiredQuantity),
            BasketItem(PRODUCT_THREE, 1)
        ).toMap()

        val basket = Basket(items)

        val specialPriceConditionOne = SpecialPriceCondition(requiredQuantity, PRODUCT_ONE.itemSku)
        val specialPriceConditionTwo = SpecialPriceCondition(requiredQuantity, PRODUCT_TWO.itemSku)
        val conditions = setOf(specialPriceConditionOne, specialPriceConditionTwo)
        val specialPrice = SpecialPrice("Buy one get one free", conditions) { PRICE }

        assertEquals(BigDecimal.ZERO, specialPrice.calculate(basket))
    }

    @Test
    fun `apply - returns the adjusted basket total when all condition is met`() {
        val requiredQuantity = 2
        val basketItemOne = BasketItem(PRODUCT_ONE, requiredQuantity)
        val basketItemTwo = BasketItem(PRODUCT_TWO, requiredQuantity)
        val items = setOf(basketItemOne, basketItemTwo, BasketItem(PRODUCT_THREE, 1)).toMap()
        val basket = Basket(items)

        val specialPriceConditionOne = SpecialPriceCondition(requiredQuantity, PRODUCT_ONE.itemSku)
        val specialPriceConditionTwo = SpecialPriceCondition(requiredQuantity, PRODUCT_TWO.itemSku)
        val conditions = setOf(specialPriceConditionOne, specialPriceConditionTwo)
        val specialPrice = SpecialPrice("Buy one get one free", conditions) { PRICE }

        assertEquals(
            PRICE,
            specialPrice.calculate(basket)
        )
    }

    @Test
    fun `apply - returns the adjusted basket total by the multiplier of matched conditions`() {
        val requiredQuantity = 1
        val smallerQuantity = 3
        val basketItemOne = BasketItem(PRODUCT_ONE, 4)
        val basketItemTwo = BasketItem(PRODUCT_TWO, smallerQuantity)
        val items = setOf(
            basketItemOne,
            basketItemTwo,
            BasketItem(PRODUCT_THREE, 1)
        ).toMap()

        val basket = Basket(items)

        val specialPriceConditionOne = SpecialPriceCondition(requiredQuantity, PRODUCT_ONE.itemSku)
        val specialPriceConditionTwo = SpecialPriceCondition(requiredQuantity, PRODUCT_TWO.itemSku)
        val conditions = setOf(specialPriceConditionOne, specialPriceConditionTwo)
        val specialPrice = SpecialPrice("Deal!!", conditions) { PRICE }

        assertEquals(
            PRICE.multiply(BigDecimal(smallerQuantity)),
            specialPrice.calculate(basket)
        )
    }

    private companion object {
        private val products = Inventory.getProducts().values
        val PRODUCT_ONE = products.toList()[3]
        val PRODUCT_TWO = products.toList()[0]
        val PRODUCT_THREE = products.toList()[2]
        val PRICE = BigDecimal("5.59")
    }
}
