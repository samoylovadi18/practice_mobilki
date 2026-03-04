package com.example.practice_mobilki.ui.screens

import HomeWithBottomNavigation
import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.practice_mobilki.R
import com.example.practice_mobilki.ui.components.CustomTextSearchField
import com.example.practice_mobilki.ui.components.ProductCard
import com.example.practice_mobilki.ui.theme.CustomColors
import com.example.practice_mobilki.ui.theme.TypographyApplication
import com.example.practice_mobilki.ui.viewmodel.ProductsViewModel

@Composable
fun Home(
    onProfile: () -> Unit = {},
    onFavorite: () -> Unit = {},
    onNotification: () -> Unit = {},
    onCart: () -> Unit = {},
    onNavigateToOutdoor: () -> Unit = {}, // НОВЫЙ ПАРАМЕТР
    viewModel: ProductsViewModel,
    modifier: Modifier = Modifier
) {
    var search by remember { mutableStateOf("") }
    val categories = listOf(stringResource(R.string.see_all), "Outdoor", "Tennis", "Running")
    var selectedCategory by remember { mutableStateOf(0) }
    val uiState by viewModel.uiState.collectAsState()
    val favoriteProducts by viewModel.favoriteProducts.collectAsState()
    val cartProducts by viewModel.cartProducts.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadProducts()
    }

    LaunchedEffect(uiState) {
        Log.d("HomeScreen", "UI State updated:")
        Log.d("HomeScreen", "  isLoading: ${uiState.isLoading}")
        Log.d("HomeScreen", "  error: ${uiState.error}")
        Log.d("HomeScreen", "  total products: ${uiState.products.size}")
        Log.d("HomeScreen", "  best sellers: ${uiState.bestSellers.size}")
        Log.d("HomeScreen", "  new arrivals: ${uiState.newArrivals.size}")
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Верхняя панель с часами и сумкой
        Spacer(modifier = Modifier.height(25.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(20.dp))
            Image(
                painter = painterResource(R.drawable.clock_1),
                contentDescription = null,
                modifier = Modifier.size(25.dp),
                alpha = 1f
            )
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                Text(
                    text = stringResource(R.string.explore),
                    style = TypographyApplication.headingRegular32
                )
            }
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(
                        color = CustomColors.block,
                        shape = CircleShape
                    )
                    .clickable { onCart() },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.bag),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    alpha = 1f
                )
            }
            Spacer(modifier = Modifier.width(20.dp))
        }

        // Поисковая строка
        Spacer(modifier = Modifier.height(21.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.weight(1f))
            CustomTextSearchField(
                value = search,
                onValueChange = { search = it },
                placeholderText = stringResource(R.string.looking_for_shoes)
            )
            Spacer(modifier = Modifier.width(14.dp))
            Box(
                modifier = Modifier
                    .size(45.dp)
                    .background(
                        color = CustomColors.accent,
                        shape = CircleShape
                    )
                    .clickable { /* Фильтр */ },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.sliders),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    alpha = 1f,
                )
            }
            Spacer(modifier = Modifier.weight(1f))
        }

        // Категории
        Spacer(modifier = Modifier.height(24.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.width(20.dp))
            Text(
                text = stringResource(R.string.select_category),
                style = TypographyApplication.bodyMedium16
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(R.string.see_all),
                style = TypographyApplication.bodyRegular12,
                color = CustomColors.accent,
                modifier = Modifier.clickable { /* Показать все категории */ }
            )
            Spacer(modifier = Modifier.width(20.dp))
        }

        Spacer(modifier = Modifier.height(23.dp))
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(categories.size) { index ->
                val category = categories[index]
                val isSelected = selectedCategory == index

                Box(
                    modifier = Modifier
                        .clickable {
                            selectedCategory = index
                            // ПРОВЕРЯЕМ КАТЕГОРИЮ И ПЕРЕХОДИМ
                            if (category == "Outdoor") {
                                onNavigateToOutdoor()
                            }
                        }
                        .background(
                            color = if (isSelected) CustomColors.accent else CustomColors.block,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .height(40.dp)
                        .width(108.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = category,
                        style = TypographyApplication.bodyRegular12.copy(
                            color = if (isSelected) Color.White else Color.Black
                        ),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // Раздел "Популярное"
        Spacer(modifier = Modifier.height(23.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.width(20.dp))
            Text(
                text = stringResource(R.string.popular_shoes),
                style = TypographyApplication.bodyMedium16
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(R.string.see_all),
                style = TypographyApplication.bodyRegular12,
                color = CustomColors.accent,
                modifier = Modifier.clickable { /* Показать все популярные */ }
            )
            Spacer(modifier = Modifier.width(20.dp))
        }

        Spacer(modifier = Modifier.height(34.dp))

        // Отображаем BEST SELLERS (популярные товары)
        if (uiState.bestSellers.isNotEmpty()) {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(uiState.bestSellers.size) { index ->
                    val product = uiState.bestSellers[index]
                    val isFavourite = favoriteProducts.contains(product.id)
                    val isAtCart = cartProducts.containsKey(product.id)

                    ProductCard(
                        product = product,
                        isFavourite = isFavourite,
                        onCheckedChange = {
                            viewModel.toggleFavorite(product.id)
                        },
                        onAddToCart = {
                            viewModel.addToCart(product.id)
                        },
                        isAtCart = isAtCart
                    )
                }
            }
        } else {
            Text(
                text = "No popular shoes",
                style = TypographyApplication.bodyRegular12,
                color = CustomColors.hint,
                modifier = Modifier.padding(20.dp)
            )
        }

        // Раздел "Новинки"
        Spacer(modifier = Modifier.height(21.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.width(20.dp))
            Text(
                text = stringResource(R.string.new_arrivals),
                style = TypographyApplication.bodyMedium16
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(R.string.see_all),
                style = TypographyApplication.bodyRegular12,
                color = CustomColors.accent,
                modifier = Modifier.clickable { /* Показать все новинки */ }
            )
            Spacer(modifier = Modifier.width(20.dp))
        }

        Spacer(modifier = Modifier.height(20.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(91.dp)
                .clickable { /* Открыть акцию */ }
        ) {
            Image(
                painter = painterResource(R.drawable.arrivals_poster),
                contentDescription = "Summer Sale",
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(CustomColors.background),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Loading...")
            }
            return
        }

        // Нижняя навигация
        HomeWithBottomNavigation(
            onProfile = onProfile,
            onNotification = onNotification,
            onFavorite = onFavorite,
            onCart = onCart
        )
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun HomePreview() {
    Home(
        onProfile = {},
        onFavorite = {},
        onNotification = {},
        onCart = {},
        onNavigateToOutdoor = {},
        viewModel = ProductsViewModel()
    )
}