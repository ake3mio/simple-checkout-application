package basket.model

import java.math.BigDecimal

data class Basket(val items: Map<Char, BasketItem> = emptyMap()) {

    fun getItem(itemSku: Char): BasketItem? {
        return items[itemSku]
    }

    fun addItem(itemSku: Char): Basket {
        return copy()
    }

    fun getBasketTotal(): BigDecimal {
        return BigDecimal.ZERO
    }
}
