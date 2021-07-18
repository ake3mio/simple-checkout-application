import basket.model.Basket
import checkout.model.Checkout
import specialprice.SpecialPriceFactory
import java.math.BigDecimal

object Promotions {
    val TWO_FOR_ONE = SpecialPriceFactory.getBuyNGet1free("2 for £1", 'B', 2)
    val THREE_MINUS_ONE = SpecialPriceFactory.getBuyNGet1free("Buy 3, get one free", 'C', 3)
    val MEAL_DEAL = SpecialPriceFactory.getMealDeal("Buy D and E for £3.25", setOf('D', 'E'), BigDecimal("3.25"))
}

fun main(args: Array<String>) {

    val basket = Basket()
    val checkout = Checkout(
        basket = basket,
        specialPrices = setOf(Promotions.TWO_FOR_ONE, Promotions.THREE_MINUS_ONE, Promotions.MEAL_DEAL)
    )

    println(
        """No special prices
            |${
        checkout
            .add('A')
            .add('A')
            .calculateTotal()
        }
        """.trimMargin()
    )

    println()

    println(
        """2 for £1
            |${
        checkout
            .add('B')
            .add('B')
            .add('B')
            .add('B')
            .calculateTotal()
        }
        """.trimMargin()
    )

    println()

    println(
        """Buy 3, get one free
            |${
        checkout
            .add('C')
            .add('C')
            .add('C')
            .calculateTotal()
        }
        """.trimMargin()
    )

    println()

    println(
        """Buy D and E for £3.25
            |${
        checkout
            .add('D')
            .add('E')
            .calculateTotal()
        }
        """.trimMargin()
    )
}
