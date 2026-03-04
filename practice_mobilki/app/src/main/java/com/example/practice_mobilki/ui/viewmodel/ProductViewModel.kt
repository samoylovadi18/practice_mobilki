package com.example.practice_mobilki.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practice_mobilki.data.model.ProductResponse
import com.example.practice_mobilki.data.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ProductsUiState(
    val products: List<ProductResponse> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val bestSellers: List<ProductResponse> = emptyList(),
    val newArrivals: List<ProductResponse> = emptyList()
)

class ProductsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ProductsUiState(isLoading = true))
    val uiState: StateFlow<ProductsUiState> = _uiState.asStateFlow()

    private val _favoriteProducts = MutableStateFlow<Set<String>>(emptySet())
    val favoriteProducts: StateFlow<Set<String>> = _favoriteProducts.asStateFlow()

    private val _cartProducts = MutableStateFlow<Map<String, Int>>(emptyMap())
    val cartProducts: StateFlow<Map<String, Int>> = _cartProducts.asStateFlow()

    init {
        loadProducts()
    }

    fun loadProducts() {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)

        viewModelScope.launch {
            try {
                val allProductsResponse = RetrofitInstance.dataManagementService.getProducts(
                    select = "*"
                )

                Log.d("ProductsViewModel", "Response code: ${allProductsResponse.code()}")

                if (allProductsResponse.isSuccessful) {
                    val allProducts = allProductsResponse.body() ?: emptyList()

                    Log.d("ProductsViewModel", "Total products loaded: ${allProducts.size}")

                    // Фильтруем бестселлеры (упрощенно)
                    val bestSellers = allProducts.filter { it.isBestSeller == true }

                    // Берем первые 5 как новые поступления
                    val newArrivals = allProducts.take(5)

                    _uiState.value = ProductsUiState(
                        products = allProducts,
                        bestSellers = bestSellers,
                        newArrivals = newArrivals,
                        isLoading = false
                    )

                    Log.d("ProductsViewModel", "Best sellers count: ${bestSellers.size}")
                } else {
                    _uiState.value = ProductsUiState(
                        error = "Ошибка: ${allProductsResponse.code()}",
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                Log.e("ProductsViewModel", "Exception: ${e.message}", e)
                _uiState.value = ProductsUiState(
                    error = "Ошибка сети: ${e.message}",
                    isLoading = false
                )
            }
        }
    }

    fun toggleFavorite(productId: String) {
        val currentFavorites = _favoriteProducts.value.toMutableSet()
        if (currentFavorites.contains(productId)) {
            currentFavorites.remove(productId)
        } else {
            currentFavorites.add(productId)
        }
        _favoriteProducts.value = currentFavorites
    }

    fun addToCart(productId: String) {
        val currentCart = _cartProducts.value.toMutableMap()
        currentCart[productId] = currentCart.getOrDefault(productId, 0) + 1
        _cartProducts.value = currentCart
    }

    fun removeFromCart(productId: String) {
        val currentCart = _cartProducts.value.toMutableMap()
        currentCart.remove(productId)
        _cartProducts.value = currentCart
    }
}