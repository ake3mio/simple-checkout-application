package specialprice.model

import basket.model.Basket

data class SpecialPriceCondition(
    private val quantity: Int,
    private val itemSku: Char
) {
    fun matches(basket: Basket): Pair<Char, Int>? {
        return null
    }
}
