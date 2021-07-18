package checkout.model

import basket.model.Basket
import specialprice.model.SpecialPrice
import java.math.BigDecimal

data class Checkout(
    val basket: Basket,
    private val specialPrices: Set<SpecialPrice>
) {
    fun calculateTotal(): BigDecimal {
        return BigDecimal.ZERO
    }

    fun add(itemSku: Char): Checkout = copy(basket = basket.addItem(itemSku))
}
