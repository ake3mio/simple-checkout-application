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

        val basketTotal = basket.getBasketTotal()
        val conditionMatches = conditions.mapNotNull { it.matches(basket) }

        if (conditionMatches.isEmpty() || conditionMatches.size != conditions.size) {
            return BigDecimal.ZERO
        }

        val products = conditionMatches.mapNotNull { basket.getItem(it.first) }
        val matches = conditionMatches.map { it.second }

        val minMatch = matches.minOrNull() ?: return basketTotal

        return price(products).multiply(BigDecimal(minMatch))
    }
}
