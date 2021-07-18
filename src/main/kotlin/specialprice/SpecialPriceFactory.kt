package specialprice

import specialprice.model.SpecialPrice
import specialprice.model.SpecialPriceCondition
import java.math.BigDecimal

object SpecialPriceFactory {

    fun getBuyNGet1free(name: String, itemSku: Char, quantity: Int): SpecialPrice {
        val condition = SpecialPriceCondition(quantity, itemSku)
        return SpecialPrice(
            name,
            setOf(condition)
        ) {
            if (it.isNotEmpty()) {
                val basketItem = it[0]
                val fullPrice = basketItem.product.unitPrice.times(BigDecimal(basketItem.quantity))
                val discount = basketItem.product.unitPrice

                fullPrice.minus(discount)
            } else {
                BigDecimal.ZERO
            }
        }
    }

    fun getMealDeal(name: String, itemSkus: Set<Char>, price: BigDecimal): SpecialPrice {
        val conditions = itemSkus.map { SpecialPriceCondition(1, it) }.toSet()
        return SpecialPrice(name, conditions) { price }
    }
}
