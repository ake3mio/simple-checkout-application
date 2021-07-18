package checkout.model

import basket.model.Basket
import specialprice.model.SpecialPrice
import java.math.BigDecimal

data class Checkout(
    val basket: Basket,
    private val specialPrices: Set<SpecialPrice>
) {
    fun calculateTotal(): BigDecimal {

        if (specialPrices.isEmpty()) {
            return basket.getBasketTotal()
        }

        val reductions = specialPrices.map { it.calculate(basket) }.reduce(BigDecimal::add)

        return BigDecimal.ZERO.max(basket.getBasketTotal().minus(reductions))
    }

    fun add(itemSku: Char): Checkout = copy(basket = basket.addItem(itemSku))
}
