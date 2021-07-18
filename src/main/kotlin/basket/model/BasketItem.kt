package basket.model

import product.model.Product

data class BasketItem(val product: Product, val quantity: Int)