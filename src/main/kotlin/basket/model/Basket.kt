package basket.model

import product.Inventory
import java.math.BigDecimal

data class Basket(val items: Map<Char, BasketItem> = emptyMap()) {

    fun getItem(itemSku: Char): BasketItem? {
        return items[itemSku]
    }

    fun addItem(itemSku: Char): Basket {
        if (items.containsKey(itemSku)) {
            return addQuantity(itemSku)
        }
        return addNew(itemSku)
    }

    fun getBasketTotal(): BigDecimal {
        if (items.isEmpty()) {
            return BigDecimal.ZERO
        }

        return items
            .values
            .map { it.product.unitPrice.multiply(BigDecimal(it.quantity)) }
            .reduce { prev, curr -> prev.add(curr) }
    }

    private fun addNew(itemSku: Char): Basket {
        return Inventory
            .findProductBySku(itemSku)
            ?.let { product ->
                val items = items
                    .map { it.key to copyItem(it.value) }
                    .toMap() +
                    mapOf(itemSku to BasketItem(product, 1))

                copy(items = items)
            } ?: copy()
    }

    private fun addQuantity(itemSku: Char): Basket {
        if (items.containsKey(itemSku)) {
            val items = items.map {
                it.key to if (itemSku == it.value.product.itemSku) {
                    updateItemQty(it.value)
                } else {
                    copyItem(it.value)
                }
            }.toMap()
            return copy(items = items)
        }
        return copy()
    }

    private fun copyItem(basketItem: BasketItem): BasketItem {
        return basketItem.copy(
            product = basketItem.product.copy(),
            quantity = basketItem.quantity
        )
    }

    private fun updateItemQty(basketItem: BasketItem): BasketItem {
        return basketItem.copy(
            product = basketItem.product.copy(),
            quantity = basketItem.quantity + 1
        )
    }
}
