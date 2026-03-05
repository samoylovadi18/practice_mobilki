package com.example.practice_mobilki.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.practice_mobilki.R
import com.example.practice_mobilki.data.model.ProductResponse
import com.example.practice_mobilki.ui.theme.CustomColors
import com.example.practice_mobilki.ui.theme.TypographyApplication
import kotlin.text.format

@Composable
fun ProductCard(
    modifier: Modifier = Modifier,
    product: ProductResponse,
    isFavourite: Boolean = false,
    onCheckedChange: (Boolean) -> Unit,
    onAddToCart: () -> Unit, 
    isAtCart: Boolean = false
) {
    val priceFormatted = String.format("%.2f", product.cost)

    var localIsFavourite by remember { mutableStateOf(isFavourite) }
    var localIsAtCart by remember { mutableStateOf(isAtCart) }

    if (localIsFavourite != isFavourite) {
        localIsFavourite = isFavourite
    }

    if (localIsAtCart != isAtCart) {
        localIsAtCart = isAtCart
    }

    Column(
        modifier = modifier
            .width(162.dp)
            .height(250.dp)
            .padding(9.dp)
            .clip(androidx.compose.foundation.shape.RoundedCornerShape(10.dp))
            .background(
                CustomColors.block,
                androidx.compose.foundation.shape.RoundedCornerShape(10.dp)
            )
    ) {
        Box(modifier = Modifier.fillMaxWidth().padding(10.dp)) {
            Box(
                modifier = modifier
                    .size(24.dp)
                    .background(
                        color = CustomColors.background,
                        shape = CircleShape,
                    )
                    .clickable {
                        val newValue = !localIsFavourite
                        localIsFavourite = newValue
                        onCheckedChange(newValue)
                    },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = if (localIsFavourite) painterResource(R.drawable.heart_colored)
                    else painterResource(R.drawable.favorite),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    alpha = 1f,
                )
            }
        }
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.sneakers_product_image),
                contentDescription = null,
                modifier = Modifier.size(117.dp, 70.dp)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = if (product.isBestSeller == true) "BEST SELLER" else "",
            style = TypographyApplication.bodyRegular12,
            color = CustomColors.accent,
            modifier = Modifier.padding(horizontal = 10.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = product.title,
            style = TypographyApplication.bodyRegular16,
            color = CustomColors.hint,
            modifier = Modifier.padding(horizontal = 10.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "₽$priceFormatted",
                style = TypographyApplication.bodyRegular14,
                color = CustomColors.text,
                modifier = Modifier.padding(horizontal = 10.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = modifier
                    .size(30.dp)
                    .background(
                        color = CustomColors.accent,
                        shape = RoundedCornerShape(topStart = 10.dp, bottomEnd = 10.dp)
                    )
                    .clickable {
                        if (!localIsAtCart) {
                            localIsAtCart = true
                            onAddToCart()
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = if (localIsAtCart) painterResource(R.drawable.cart_white)
                    else painterResource(R.drawable.add_white),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    alpha = 1f,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProductCardPreview() {
    ProductCard(
        onCheckedChange = { newValue ->
            println("Избранное изменено на: $newValue")
        },
        onAddToCart = {
            println("Добавлено в корзину")
        },
        isAtCart = false,
        product = ProductResponse("id", "PUMA CA PRO CLASSIC", "cat_id", 120.00f, "descr", true)
    )
}