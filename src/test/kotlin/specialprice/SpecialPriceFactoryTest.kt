package specialprice

import basket.model.Basket
import basket.model.BasketItem
import basket.toMap
import org.junit.jupiter.api.Test
import product.Inventory
import java.math.BigDecimal
import kotlin.test.assertEquals

internal class SpecialPriceFactoryTest {

    @Test
    fun `getBuyNGet1free - returns a SpecialPrice which calculates the (quantity of the required products - 1) * the unit price`() {

        val products = Inventory.getProducts().values.toList()

        val basketItem = BasketItem(products[0], 5)
        val items = setOf(basketItem).toMap()

        assertEquals(
            products[0].unitPrice.times(BigDecimal("4")),
            SpecialPriceFactory.getBuyNGet1free("Buy 5, get 1 free", products[0].itemSku, 5).calculate(Basket(items))
        )
    }

    @Test
    fun `getMealDeal - returns a SpecialPrice which calculates a special price when a basket contains all the required products`() {

        val quantity = 5
        val products = Inventory.getProducts().values.toList()
        val basketItemOne = BasketItem(products[3], quantity)
        val basketItemTwo = BasketItem(products[4], quantity)
        val items = setOf(basketItemOne, basketItemTwo).toMap()

        val price = BigDecimal("3.25")

        assertEquals(
            price.multiply(BigDecimal(quantity)),
            SpecialPriceFactory.getMealDeal(
                "Buy D and E for £3.25",
                setOf(basketItemOne.product.itemSku, basketItemTwo.product.itemSku),
                price
            ).calculate(Basket(items))
        )
    }
}