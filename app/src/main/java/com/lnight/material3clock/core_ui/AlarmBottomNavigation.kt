package com.lnight.material3clock.core_ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigationDefaults
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Surface
import androidx.compose.material.contentColorFor
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.lnight.material3clock.R
import com.lnight.material3clock.core.BottomNavItem
import com.lnight.material3clock.ui.theme.Material3ClockTheme

@Composable
fun AlarmBottomNavigation(navController: NavHostController) {
    val items = listOf(
        BottomNavItem.Alarm,
        BottomNavItem.Clock,
        BottomNavItem.Timer,
        BottomNavItem.Stopwatch
    )
    CustomBottomNavigation(
        backgroundColor = MaterialTheme.colorScheme.onSecondary,
        contentColor = MaterialTheme.colorScheme.secondaryContainer,
        modifier = Modifier
            .height(96.dp)
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        var currentRoute = navBackStackEntry?.destination?.route
        if(items.find { it.route == currentRoute } == null) {
            currentRoute = items.first().route
        }
        items.forEach { item ->
            val isSelected = currentRoute == item.route
            CustomNavItem(
                modifier = Modifier.size(width = 100.dp, height = 65.dp),
                icon = painterResource(id = item.icon),
                text = item.title,
                onClick = {
                    navController.navigate(item.route) {

                        navController.graph.startDestinationRoute?.let { screen_route ->
                            popUpTo(screen_route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                isSelected = isSelected
            )
        }
    }
}

@Composable
fun CustomNavItem(
    modifier: Modifier = Modifier,
    icon: Painter,
    text: String,
    onClick: () -> Unit,
    isSelected: Boolean
) {
    val interactionSource = remember { MutableInteractionSource() }
    val itemSectionAlpha by animateFloatAsState(
        targetValue = if (isSelected) ContentAlpha.high else ContentAlpha.medium
    )
    Column(
        modifier = modifier
            .clickable(
                onClick = onClick,
                interactionSource = interactionSource,
                indication = null
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .clip(RoundedCornerShape(50.dp))
                .background(
                    color = if (isSelected) MaterialTheme.colorScheme.secondaryContainer else Color.Transparent
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = icon,
                contentDescription = text,
                modifier = Modifier
                    .size(32.dp)
                    .alpha(itemSectionAlpha)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = text,
            textAlign = TextAlign.Center,
            fontSize = 14.sp,
            modifier = Modifier.alpha(itemSectionAlpha),
            fontWeight = if (isSelected) FontWeight.SemiBold else null
        )
        Spacer(modifier = Modifier.height(19.dp))
    }
}

@Composable
fun CustomBottomNavigation(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = contentColorFor(backgroundColor),
    elevation: Dp = BottomNavigationDefaults.Elevation,
    content: @Composable RowScope.() -> Unit
) {
    Surface(
        color = backgroundColor,
        contentColor = contentColor,
        elevation = elevation,
        modifier = modifier
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .height(56.dp)
                .selectableGroup(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            content = content
        )
    }
}

@Preview
@Composable
fun CustomNavItemPreview() {
    Material3ClockTheme {
        CustomNavItem(
            modifier = Modifier.size(width = 40.dp, height = 45.dp),
            icon = painterResource(id = R.drawable.ic_alarm),
            text = "Alarm",
            onClick = {},
            isSelected = true
        )
    }
}