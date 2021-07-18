package specialprice.model

import basket.model.Basket
import basket.model.BasketItem
import java.math.BigDecimal

data class SpecialPrice(
    val name: String,
    private val conditions: Set<SpecialPriceCondition>,
    val price: (List<BasketItem>) -> BigDecimal,
) {
    fun calculate(basket: Basket): BigDecimal {
        return BigDecimal.ZERO
    }
}
