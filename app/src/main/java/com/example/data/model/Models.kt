package com.example.data.model

import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Supported legitimate social media platforms.
 * All services strictly comply with platform advertising and terms of service.
 */
enum class SocialPlatform(
    val displayName: String,
    val primaryColor: Color,
    val iconKey: String,
    val adNetworkName: String
) {
    ALL("All Platforms", Color(0xFF6366F1), "globe", "Multi-Platform Network"),
    FACEBOOK("Facebook", Color(0xFF1877F2), "facebook", "Meta Ads & Business Suite"),
    INSTAGRAM("Instagram", Color(0xFFE1306C), "instagram", "Meta Instagram Ads"),
    TIKTOK("TikTok", Color(0xFF00F2FE), "tiktok", "TikTok For Business & Spark Ads"),
    YOUTUBE("YouTube", Color(0xFFFF0000), "youtube", "Google Ads & YouTube Video"),
    TELEGRAM("Telegram", Color(0xFF229ED9), "telegram", "Telegram Ad Platform & PR"),
    WHATSAPP("WhatsApp", Color(0xFF25D366), "whatsapp", "Meta WhatsApp Business API"),
    MESSENGER("Messenger", Color(0xFF0084FF), "messenger", "Meta Messenger Sponsored")
}

/**
 * 12 Legitimate Marketing Services specified in the requirements.
 */
enum class ServiceCategory(val categoryName: String) {
    PAID_ADS("Paid Advertising"),
    VIDEO_PROMOTION("Video & Rich Media"),
    CREATOR_COLLAB("Creator & Influencers"),
    CONTENT_DISTRIBUTION("Content & PR"),
    BRANDING("Brand Awareness"),
    TRAFFIC("Website & App Growth"),
    REPORTING("Auditing & Analytics")
}

data class MarketingService(
    val id: String,
    val title: String,
    val serviceNumber: Int,
    val category: ServiceCategory,
    val supportedPlatforms: List<SocialPlatform>,
    val description: String,
    val basePriceUsd: Double,
    val priceUnit: String,
    val estimatedDeliveryDays: String,
    val legalCompliancePledge: String,
    val features: List<String>,
    val isActive: Boolean = true
)

/**
 * Campaign status lifecycle
 */
enum class CampaignStatus(val label: String, val colorHex: Long) {
    PENDING_REVIEW("Pending Admin Review", 0xFFF59E0B),
    APPROVED("Approved & Scheduled", 0xFF3B82F6),
    RUNNING("Live Campaign Running", 0xFF10B981),
    COMPLETED("Successfully Completed", 0xFF059669),
    REJECTED("Rejected (Policy/Terms)", 0xFFEF4444),
    REFUNDED("Refunded to Wallet", 0xFF8B5CF6)
}

/**
 * Campaign order entity persisted in Room
 */
@Entity(tableName = "campaign_orders")
data class CampaignOrder(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val orderNumber: String,
    val serviceId: String,
    val serviceTitle: String,
    val platform: SocialPlatform,
    val campaignTitle: String,
    val targetUrl: String,
    val targetAudience: String,
    val targetCountry: String,
    val budgetUsd: Double,
    val status: CampaignStatus,
    val createdAt: Long = System.currentTimeMillis(),
    val approvedAt: Long? = null,
    val completedAt: Long? = null,
    val verifiedImpressions: Long = 0,
    val verifiedReach: Long = 0,
    val verifiedClicks: Long = 0,
    val engagementRate: Double = 0.0,
    val adminNotes: String = "",
    val rejectionReason: String = "",
    val auditHash: String = "",
    val creatorCollabName: String? = null
)

/**
 * Verified Creator Marketplace entity persisted in Room
 */
@Entity(tableName = "creators")
data class CreatorProfile(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val handle: String,
    val platform: SocialPlatform,
    val category: String,
    val startingPriceUsd: Double,
    val estimatedReach: String,
    val deliveryDays: String,
    val rating: Double,
    val totalCampaigns: Int,
    val contentType: String,
    val isVerified: Boolean = true,
    val zeroBotAuditPassed: Boolean = true,
    val bio: String
)

/**
 * Payment Transaction entity persisted in Room
 */
@Entity(tableName = "transactions")
data class PaymentTransaction(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val transactionRef: String,
    val orderId: Long,
    val orderTitle: String,
    val amountUsd: Double,
    val paymentMethod: String,
    val status: String, // SUCCESS, REFUNDED, PENDING
    val timestamp: Long = System.currentTimeMillis(),
    val receiptId: String
)

/**
 * System and Campaign Notifications persisted in Room
 */
@Entity(tableName = "notifications")
data class AppNotification(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val message: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false,
    val type: String // ORDER, SECURITY, ADMIN, CREATOR
)

/**
 * Role-based app access
 */
enum class UserRole(val label: String) {
    ADVERTISER("Advertiser / Client"),
    CREATOR("Verified Creator"),
    ADMIN("Platform Admin")
}

data class UserSession(
    val userId: String = "usr_fst_88291",
    val fullName: String = "Alex Vance",
    val email: String = "alex.marketing@brandpro.com",
    val role: UserRole = UserRole.ADVERTISER,
    val walletBalanceUsd: Double = 350.00,
    val isIdentityVerified: Boolean = true
)

/**
 * Anti-fraud detection result
 */
data class FraudCheckResult(
    val isLegitimate: Boolean,
    val flaggedKeywords: List<String> = emptyList(),
    val reason: String = ""
)
