package specialprice.model

import basket.model.Basket
import basket.model.BasketItem

data class SpecialPriceCondition(
    private val quantity: Int,
    private val itemSku: Char
) {
    fun matches(basket: Basket): Pair<Char, Int>? {
        return basket.items.values
            .find { it.product.itemSku == itemSku }
            ?.let(this::getMatches)
    }

    private fun getMatches(basketItem: BasketItem): Pair<Char, Int> {
        return if (basketItem.quantity > 0 && quantity > 0) {
            Pair(
                basketItem.product.itemSku,
                Math.floorDiv(basketItem.quantity, quantity)
            )
        } else if (basketItem.quantity == quantity) {
            Pair(
                basketItem.product.itemSku,
                1
            )
        } else {
            Pair(
                basketItem.product.itemSku,
                0
            )
        }
    }
}
