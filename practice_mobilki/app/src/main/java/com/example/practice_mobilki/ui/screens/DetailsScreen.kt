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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.practice_mobilki.R
import com.example.practice_mobilki.data.model.ProductResponse
import com.example.practice_mobilki.ui.theme.CustomColors
import java.text.NumberFormat
import java.util.Locale

@Composable
fun DetailsScreen(
    product: ProductResponse,
    onBackClick: () -> Unit,
    onAddToCart: () -> Unit,
    onToggleFavorite: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var showMore by remember { mutableStateOf(false) }
    var isFavorite by remember { mutableStateOf(false) }

    // Форматирование цены
    val priceFormatted = NumberFormat.getCurrencyInstance(Locale("ru", "RU"))
        .format(product.cost)
        .replace("RUB", "₽")
        .replace("руб", "₽")

    // Основной текст описания
    val mainDescription = "Ретро кроссовки в стиле баскетбола отличаются массивной подошвой и спортивным силуэтом."

    // Дополнительный текст
    val additionalDescription = "Прочный нейлоновый верх с замшевыми вставками обеспечивает поддержку и воздухопроницаемость. Эта модель идеально подходит для повседневной носки и сочетается с различными стилями одежды. Кроссовки имеют амортизирующую подошву, которая обеспечивает комфорт при ходьбе в течение всего дня."

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Верхняя панель
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(CustomColors.block, CircleShape)
                    .clickable { onBackClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.back),
                    contentDescription = "Назад",
                    tint = Color.Black,
                    modifier = Modifier.size(20.dp)
                )
            }

            Text(
                text = "Sneaker Shop",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(CustomColors.block, CircleShape)
                    .clickable { },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.bag),
                    contentDescription = "Корзина",
                    tint = Color.Black,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            // Изображение
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .padding(vertical = 8.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(CustomColors.block)
            ) {
                Image(
                    painter = painterResource(R.drawable.sneakers_product_image),
                    contentDescription = product.title,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Название и цена
            Text(
                text = product.title,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Essential Men's Shoes",
                fontSize = 16.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = priceFormatted,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = CustomColors.accent
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Основное описание
            Text(
                text = mainDescription,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                color = Color.DarkGray
            )

            // Дополнительное описание (показывается только если showMore = true)
            if (showMore) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = additionalDescription,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    color = Color.DarkGray
                )
            }

            // Кнопка Подробнее/Свернуть
            Text(
                text = if (showMore) "Свернуть" else "Подробнее",
                fontSize = 14.sp,
                color = CustomColors.accent,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 24.dp)
                    .clickable {
                        showMore = !showMore
                    }
            )
        }

        // Нижняя панель с кнопками
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Кнопка избранного
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(CustomColors.block)
                    .clickable {
                        isFavorite = !isFavorite
                        onToggleFavorite()
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = if (isFavorite)
                        painterResource(R.drawable.heart_colored)
                    else
                        painterResource(R.drawable.favorite),
                    contentDescription = "В избранное",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Кнопка "В корзину"
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(CustomColors.accent)
                    .clickable { onAddToCart() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "В Корзину",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailsScreenPreview() {
    val sampleProduct = ProductResponse(
        id = "1",
        title = "PUMA CA Pro Classic",
        categoryId = "cat1",
        cost = 13999.0f,
        description = "",
        isBestSeller = true
    )

    DetailsScreen(
        product = sampleProduct,
        onBackClick = {},
        onAddToCart = {}
    )
}