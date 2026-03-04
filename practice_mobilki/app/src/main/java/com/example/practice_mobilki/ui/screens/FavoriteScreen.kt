package com.example.practice_mobilki.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.practice_mobilki.R
import com.example.practice_mobilki.ui.theme.CustomColors
import com.example.practice_mobilki.ui.theme.TypographyApplication
import java.util.Locale

// Модель для товара в избранном
data class FavoriteProduct(
    val id: String,
    val title: String,
    val price: Float,
    val isBestSeller: Boolean = true
)

@Composable
fun FavoriteScreen(
    onBackClick: () -> Unit,
    onProductClick: (String) -> Unit = {},
    onRemoveFromFavorite: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    // Заглушки для избранного (6 товаров как на скрине)
    val favoriteProducts = remember {
        List(6) { index ->
            FavoriteProduct(
                id = "favorite_${index + 1}",
                title = "Nike Air Max",
                price = 752.00f,
                isBestSeller = true
            )
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Верхняя панель с часами и заголовком
        Spacer(modifier = Modifier.height(25.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Часы
            Image(
                painter = painterResource(R.drawable.clock_1),
                contentDescription = "Clock",
                modifier = Modifier.size(25.dp)
            )

            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                Text(
                    text = "Избранное",
                    style = TypographyApplication.headingRegular32
                )
            }

            // Кнопка корзины
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(
                        color = CustomColors.block,
                        shape = CircleShape
                    )
                    .clickable { /* Перейти в корзину */ },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.bag),
                    contentDescription = "Cart",
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Счетчик товаров
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${favoriteProducts.size} товаров",
                style = TypographyApplication.bodyMedium16,
                color = CustomColors.text
            )

            Spacer(modifier = Modifier.weight(1f))

            // Кнопка редактирования
            Text(
                text = "Редактировать",
                style = TypographyApplication.bodyRegular12,
                color = CustomColors.accent,
                modifier = Modifier.clickable { /* Режим редактирования */ }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Сетка избранных товаров (2 колонки)
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 16.dp, top = 8.dp)
        ) {
            items(favoriteProducts) { product ->
                FavoriteProductCard(
                    product = product,
                    onClick = { onProductClick(product.id) },
                    onRemove = { onRemoveFromFavorite(product.id) }
                )
            }
        }
    }
}

// Карточка товара в избранном
@Composable
fun FavoriteProductCard(
    product: FavoriteProduct,
    onClick: () -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isFavourite by remember { mutableStateOf(true) } // В избранном всегда true
    var isInCart by remember { mutableStateOf(false) }

    val priceFormatted = String.format(Locale.US, "%.2f", product.price)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(250.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(
                CustomColors.block,
                RoundedCornerShape(10.dp)
            )
            .clickable { onClick() }
    ) {
        // Кнопка удаления из избранного
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(
                        color = CustomColors.background,
                        shape = CircleShape,
                    )
                    .clickable {
                        isFavourite = !isFavourite
                        if (!isFavourite) {
                            onRemove() // Удаляем при повторном нажатии
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = if (isFavourite) painterResource(R.drawable.heart_colored)
                    else painterResource(R.drawable.favorite),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        // Изображение товара
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.sneakers_product_image),
                contentDescription = null,
                modifier = Modifier.size(117.dp, 70.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // BEST SELLER
        Text(
            text = if (product.isBestSeller) "BEST SELLER" else "",
            style = TypographyApplication.bodyRegular12,
            color = CustomColors.accent,
            modifier = Modifier.padding(horizontal = 10.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Название товара
        Text(
            text = product.title,
            style = TypographyApplication.bodyRegular16,
            color = CustomColors.hint,
            modifier = Modifier.padding(horizontal = 10.dp),
            maxLines = 1
        )

        Spacer(modifier = Modifier.weight(1f))

        // Цена и кнопка добавления в корзину
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 0.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "₽$priceFormatted",
                style = TypographyApplication.bodyRegular14,
                color = CustomColors.text,
                modifier = Modifier.padding(horizontal = 10.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            // Кнопка добавления в корзину
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .background(
                        color = CustomColors.accent,
                        shape = RoundedCornerShape(topStart = 10.dp, bottomEnd = 10.dp)
                    )
                    .clickable {
                        isInCart = !isInCart
                    },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = if (isInCart) painterResource(R.drawable.cart_white)
                    else painterResource(R.drawable.add_white),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FavoriteScreenPreview() {
    FavoriteScreen(
        onBackClick = {},
        onProductClick = {},
        onRemoveFromFavorite = {}
    )
}
