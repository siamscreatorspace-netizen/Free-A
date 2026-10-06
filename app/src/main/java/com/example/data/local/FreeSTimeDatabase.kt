package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.AppNotification
import com.example.data.model.CampaignOrder
import com.example.data.model.CampaignStatus
import com.example.data.model.CreatorProfile
import com.example.data.model.PaymentTransaction
import com.example.data.model.SocialPlatform
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        CampaignOrder::class,
        CreatorProfile::class,
        PaymentTransaction::class,
        AppNotification::class
    ],
    version = 1,
    exportSchema = false
)
abstract class FreeSTimeDatabase : RoomDatabase() {
    abstract fun orderDao(): OrderDao
    abstract fun creatorDao(): CreatorDao
    abstract fun transactionDao(): TransactionDao
    abstract fun notificationDao(): NotificationDao

    companion object {
        @Volatile
        private var INSTANCE: FreeSTimeDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): FreeSTimeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FreeSTimeDatabase::class.java,
                    "free_s_time_database"
                )
                    .addCallback(DatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateInitialData(database)
                }
            }
        }

        private suspend fun populateInitialData(db: FreeSTimeDatabase) {
            // Seed verified creators
            val initialCreators = listOf(
                CreatorProfile(
                    name = "Elena Rostova",
                    handle = "@elena.techpulse",
                    platform = SocialPlatform.YOUTUBE,
                    category = "Technology & AI",
                    startingPriceUsd = 120.0,
                    estimatedReach = "85K+ Organic Viewers",
                    deliveryDays = "3 Days",
                    rating = 4.96,
                    totalCampaigns = 48,
                    contentType = "Dedicated Review & In-Depth Demo",
                    isVerified = true,
                    zeroBotAuditPassed = true,
                    bio = "Independent tech reviewer with verified high-retention human audience. Official Google & YouTube verified creator."
                ),
                CreatorProfile(
                    name = "Marcus Sterling",
                    handle = "@marcus_reels",
                    platform = SocialPlatform.INSTAGRAM,
                    category = "Business & Productivity",
                    startingPriceUsd = 85.0,
                    estimatedReach = "140K+ Reel Impressions",
                    deliveryDays = "2 Days",
                    rating = 4.92,
                    totalCampaigns = 62,
                    contentType = "9:16 Short-Form Reel + Story Swipe",
                    isVerified = true,
                    zeroBotAuditPassed = true,
                    bio = "Meta Verified creator creating dynamic business case studies. Compliant with Meta Paid Partnership guidelines."
                ),
                CreatorProfile(
                    name = "Chloe Chen",
                    handle = "@chloetrendz",
                    platform = SocialPlatform.TIKTOK,
                    category = "Lifestyle & Apps",
                    startingPriceUsd = 95.0,
                    estimatedReach = "210K+ FYP Reach",
                    deliveryDays = "3 Days",
                    rating = 4.89,
                    totalCampaigns = 91,
                    contentType = "Authentic UGC Trend Spark Ad",
                    isVerified = true,
                    zeroBotAuditPassed = true,
                    bio = "Top TikTok Creator Marketplace creator. High completion rates and authentic real audience resonance."
                ),
                CreatorProfile(
                    name = "Tech Digest Daily",
                    handle = "@techdigestchannel",
                    platform = SocialPlatform.TELEGRAM,
                    category = "Developer & Tech",
                    startingPriceUsd = 45.0,
                    estimatedReach = "38K Active Subscribers",
                    deliveryDays = "1 Day",
                    rating = 4.98,
                    totalCampaigns = 114,
                    contentType = "Sponsored Editorial Post (24h Pinned)",
                    isVerified = true,
                    zeroBotAuditPassed = true,
                    bio = "Curated tech channel with verified human forward metrics. Active since 2021 with zero bot subscribers."
                ),
                CreatorProfile(
                    name = "David Miller",
                    handle = "@davidfitnesspro",
                    platform = SocialPlatform.FACEBOOK,
                    category = "Health & Fitness",
                    startingPriceUsd = 60.0,
                    estimatedReach = "65K Engaged Followers",
                    deliveryDays = "2 Days",
                    rating = 4.85,
                    totalCampaigns = 37,
                    contentType = "Video Tutorial with Brand Tag",
                    isVerified = true,
                    zeroBotAuditPassed = true,
                    bio = "Certified trainer page with organic community discussion and high average watch durations."
                )
            )
            db.creatorDao().insertAll(initialCreators)

            // Seed initial sample campaign orders demonstrating active & completed states
            val order1 = CampaignOrder(
                orderNumber = "FST-2026-9041",
                serviceId = "svc_yt_video_promo",
                serviceTitle = "YouTube Video Promotion (Legitimate Ads)",
                platform = SocialPlatform.YOUTUBE,
                campaignTitle = "NextGen AI Productivity App Walkthrough",
                targetUrl = "https://youtube.com/watch?v=fst-promo-demo",
                targetAudience = "Tech Enthusiasts, Product Managers (Ages 22-45)",
                targetCountry = "United States, Canada, UK",
                budgetUsd = 120.00,
                status = CampaignStatus.RUNNING,
                createdAt = System.currentTimeMillis() - 86400000L * 2,
                approvedAt = System.currentTimeMillis() - 86400000L * 1,
                verifiedImpressions = 14250,
                verifiedReach = 11800,
                verifiedClicks = 492,
                engagementRate = 4.18,
                adminNotes = "Approved via Google Ads Partner API placement. Frequency cap = 2/day.",
                auditHash = "FST-AUDIT-SHA256-4c91a0b329e18",
                creatorCollabName = null
            )
            val order1Id = db.orderDao().insertOrder(order1)

            val order2 = CampaignOrder(
                orderNumber = "FST-2026-8812",
                serviceId = "svc_fb_ig_ads",
                serviceTitle = "Facebook & Instagram Ad Campaigns",
                platform = SocialPlatform.INSTAGRAM,
                campaignTitle = "Summer Apparel Launch - Story Carousel",
                targetUrl = "https://instagram.com/fashionbrand_official",
                targetAudience = "Fashion, Streetwear, Gen-Z / Millennial",
                targetCountry = "Global / Top Metro Cities",
                budgetUsd = 75.00,
                status = CampaignStatus.COMPLETED,
                createdAt = System.currentTimeMillis() - 86400000L * 5,
                approvedAt = System.currentTimeMillis() - 86400000L * 4,
                completedAt = System.currentTimeMillis() - 86400000L * 1,
                verifiedImpressions = 28400,
                verifiedReach = 24100,
                verifiedClicks = 1104,
                engagementRate = 4.58,
                adminNotes = "Completed with 100% policy compliance. Zero bounce anomalies detected.",
                auditHash = "FST-AUDIT-SHA256-78b9e11fc340",
                creatorCollabName = null
            )
            val order2Id = db.orderDao().insertOrder(order2)

            // Seed initial transactions
            db.transactionDao().insertTransaction(
                PaymentTransaction(
                    transactionRef = "TXN-SECURE-9041",
                    orderId = order1Id,
                    orderTitle = "YouTube Video Promotion (Legitimate Ads)",
                    amountUsd = 120.00,
                    paymentMethod = "Google Pay (Tokenized)",
                    status = "SUCCESS",
                    timestamp = System.currentTimeMillis() - 86400000L * 2,
                    receiptId = "REC-FST-90412"
                )
            )
            db.transactionDao().insertTransaction(
                PaymentTransaction(
                    transactionRef = "TXN-SECURE-8812",
                    orderId = order2Id,
                    orderTitle = "Facebook & Instagram Ad Campaigns",
                    amountUsd = 75.00,
                    paymentMethod = "Credit Card (Gateway SSL)",
                    status = "SUCCESS",
                    timestamp = System.currentTimeMillis() - 86400000L * 5,
                    receiptId = "REC-FST-88129"
                )
            )

            // Seed initial notifications
            db.notificationDao().insertNotification(
                AppNotification(
                    title = "Welcome to Free S Time",
                    message = "Real Promotion. Real Reach. All marketing is delivered through official advertising platforms and vetted human creators.",
                    timestamp = System.currentTimeMillis() - 86400000L * 3,
                    isRead = false,
                    type = "SECURITY"
                )
            )
            db.notificationDao().insertNotification(
                AppNotification(
                    title = "Campaign Live & Verified",
                    message = "Your campaign 'NextGen AI Productivity App Walkthrough' is actively running via Google Ads TrueView.",
                    timestamp = System.currentTimeMillis() - 86400000L * 1,
                    isRead = false,
                    type = "ORDER"
                )
            )
        }
    }
}
