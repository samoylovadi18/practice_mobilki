import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.practice_mobilki.R
import com.example.practice_mobilki.ui.theme.CustomColors
import kotlin.collections.forEach

@Composable
fun HomeWithBottomNavigation(selectedNumber: Int = 0, onProfile: () -> Unit = {}, onHome: () -> Unit = {}, onFavorite: () -> Unit = {}, onNotification: () -> Unit = {}, onCart: () -> Unit = {}) {
    var selectedItem by remember { mutableStateOf(selectedNumber) }

    val items = listOf(
        NavigationItem.Home,
        NavigationItem.Notifications,
        NavigationItem.Cart,
        NavigationItem.Favorites,
        NavigationItem.Profile
    )

    NavigationBar(
        containerColor = Color.White,
        contentColor = CustomColors.accent,
        modifier = Modifier
    ) {
        items.forEach { item ->
            val index = items.indexOf(item)
            val selected = selectedItem == index

            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(item.icon),
                        contentDescription = stringResource(item.title),
                        tint = if (selected) CustomColors.accent else Color.Gray,
                        modifier = Modifier.size(24.dp)
                    )
                },
                selected = selected,
                onClick = {
                    selectedItem = index
                    if (item is NavigationItem.Profile) {
                        onProfile()
                    } else if (item is NavigationItem.Home) {
                        onHome()
                    } else if (item is NavigationItem.Favorites) {
                        onFavorite()
                    } else if (item is NavigationItem.Notifications) {
                        onNotification()
                    } else if (item is NavigationItem.Cart) {
                        onCart()
                    }
                },
                alwaysShowLabel = false
            )
        }
    }
}

sealed class NavigationItem(val title: Int, val icon: Int) {
    object Home : NavigationItem(R.string.explore, R.drawable.home)
    object Notifications : NavigationItem(R.string.favourite, R.drawable.notification)
    object Cart : NavigationItem(R.string.cart, R.drawable.bag)
    object Favorites : NavigationItem(R.string.favourite, R.drawable.favorite)
    object Profile : NavigationItem(R.string.profile, R.drawable.profile)
}

@Preview
@Composable
private fun HomeWithBottomNavigationPreview() {
    HomeWithBottomNavigation(1)
}
