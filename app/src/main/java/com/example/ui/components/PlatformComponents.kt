package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SocialPlatform

@Composable
fun getPlatformIcon(platform: SocialPlatform): ImageVector {
    return when (platform) {
        SocialPlatform.ALL -> Icons.Default.Public
        SocialPlatform.FACEBOOK -> Icons.Default.Public
        SocialPlatform.INSTAGRAM -> Icons.Default.Campaign
        SocialPlatform.TIKTOK -> Icons.Default.Videocam
        SocialPlatform.YOUTUBE -> Icons.Default.PlayArrow
        SocialPlatform.TELEGRAM -> Icons.Default.Send
        SocialPlatform.WHATSAPP -> Icons.Default.Chat
        SocialPlatform.MESSENGER -> Icons.Default.Chat
    }
}

@Composable
fun PlatformBadge(
    platform: SocialPlatform,
    modifier: Modifier = Modifier,
    small: Boolean = false
) {
    Surface(
        modifier = modifier.testTag("platform_badge_${platform.name.lowercase()}"),
        shape = RoundedCornerShape(12.dp),
        color = platform.primaryColor.copy(alpha = 0.16f),
        contentColor = platform.primaryColor
    ) {
        Row(
            modifier = Modifier.padding(horizontal = if (small) 8.dp else 10.dp, vertical = if (small) 3.dp else 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(if (small) 7.dp else 9.dp)
                    .clip(CircleShape)
                    .background(platform.primaryColor)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = platform.displayName,
                fontSize = if (small) 11.sp else 12.sp,
                fontWeight = FontWeight.Bold,
                color = platform.primaryColor
            )
        }
    }
}

@Composable
fun PlatformFilterChips(
    selectedPlatform: SocialPlatform,
    onPlatformSelected: (SocialPlatform) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
    ) {
        items(SocialPlatform.values()) { platform ->
            val isSelected = platform == selectedPlatform
            FilterChip(
                selected = isSelected,
                onClick = { onPlatformSelected(platform) },
                label = {
                    Text(
                        text = platform.displayName,
                        fontSize = 13.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = getPlatformIcon(platform),
                        contentDescription = platform.displayName,
                        modifier = Modifier.size(16.dp),
                        tint = if (isSelected) MaterialTheme.colorScheme.primary else platform.primaryColor
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                modifier = Modifier.testTag("chip_${platform.name.lowercase()}")
            )
        }
    }
}
