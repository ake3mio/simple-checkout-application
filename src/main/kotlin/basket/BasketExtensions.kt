package basket

import basket.model.BasketItem

fun Set<BasketItem>.toMap() = associateBy { it.product.itemSku }
