package com.example.data.model

object DefaultMarketingServices {
    val services: List<MarketingService> = listOf(
        MarketingService(
            id = "svc_social_ads",
            title = "Social Media Advertising Campaigns",
            serviceNumber = 1,
            category = ServiceCategory.PAID_ADS,
            supportedPlatforms = listOf(SocialPlatform.FACEBOOK, SocialPlatform.INSTAGRAM, SocialPlatform.TIKTOK, SocialPlatform.YOUTUBE),
            description = "Multi-platform targeted paid media campaigns setup directly through official platform Ad Managers with demographic and interest targeting.",
            basePriceUsd = 49.00,
            priceUnit = "Campaign Setup & Optimization",
            estimatedDeliveryDays = "2-5 Days",
            legalCompliancePledge = "100% compliant with platform advertising policies. Zero bot activity, verified real audience delivery.",
            features = listOf(
                "Precise geo, age, interest & language targeting",
                "Official platform ad manager tracking pixels",
                "A/B creative testing included",
                "Real human click and impression reporting"
            )
        ),
        MarketingService(
            id = "svc_yt_video_promo",
            title = "YouTube Video Promotion (Legitimate Ads)",
            serviceNumber = 2,
            category = ServiceCategory.VIDEO_PROMOTION,
            supportedPlatforms = listOf(SocialPlatform.YOUTUBE),
            description = "Promote your YouTube video via Google Ads In-Feed and In-Stream TrueView video placements to organic video viewers.",
            basePriceUsd = 35.00,
            priceUnit = "Per 2,500+ Verified Organic Impressions",
            estimatedDeliveryDays = "3-4 Days",
            legalCompliancePledge = "Strictly runs through Google Ads API. No view manipulation or watch-time bots. Safe for monetization.",
            features = listOf(
                "Official Google Ads TrueView placement",
                "Audience targeting by channel niche and topics",
                "Retention tracking & audience analytics",
                "100% safe for YouTube Partner Program"
            )
        ),
        MarketingService(
            id = "svc_fb_ig_ads",
            title = "Facebook & Instagram Ad Campaigns",
            serviceNumber = 3,
            category = ServiceCategory.PAID_ADS,
            supportedPlatforms = listOf(SocialPlatform.FACEBOOK, SocialPlatform.INSTAGRAM),
            description = "Direct Meta Ads Manager campaigns optimizing for Feed, Stories, and Reels to maximize brand discovery and conversion.",
            basePriceUsd = 39.00,
            priceUnit = "Per Campaign Run",
            estimatedDeliveryDays = "2-3 Days",
            legalCompliancePledge = "Executed via Meta Business Partner standards. Never violates Facebook Page or Instagram Community Guidelines.",
            features = listOf(
                "Custom audience matching & lookalikes",
                "Interactive Reels & Story ad placements",
                "Meta Pixel conversion tracking",
                "Daily spend and engagement audit"
            )
        ),
        MarketingService(
            id = "svc_tiktok_promo",
            title = "TikTok Promotional Campaigns (Officially Permitted)",
            serviceNumber = 4,
            category = ServiceCategory.PAID_ADS,
            supportedPlatforms = listOf(SocialPlatform.TIKTOK),
            description = "TikTok In-Feed Video Ads and Spark Ads targeting active regional audiences looking for entertaining viral content.",
            basePriceUsd = 45.00,
            priceUnit = "Per Target Segment",
            estimatedDeliveryDays = "2-4 Days",
            legalCompliancePledge = "Run exclusively through TikTok For Business Ads platform. Zero fake likes, strictly genuine FYP audience.",
            features = listOf(
                "Official TikTok For Business Ads delivery",
                "Soundtrack & hashtag optimization",
                "Audience segmentation by device and region",
                "Verified video completion rate metrics"
            )
        ),
        MarketingService(
            id = "svc_telegram_promo",
            title = "Telegram Channel Legitimate Advertising",
            serviceNumber = 5,
            category = ServiceCategory.PAID_ADS,
            supportedPlatforms = listOf(SocialPlatform.TELEGRAM),
            description = "Legitimate sponsored messages placed in verified public Telegram channels and official Telegram Ads platform.",
            basePriceUsd = 29.00,
            priceUnit = "Per Channel Broadcast",
            estimatedDeliveryDays = "1-2 Days",
            legalCompliancePledge = "Strictly manual channel placements and official Telegram Ads. No spam scraping or mass automated DM bots.",
            features = listOf(
                "Niche-relevant verified channels (Crypto, Tech, News)",
                "Transparent forward and view counts",
                "Compliant with Telegram Terms of Service",
                "Permanent placement retention guarantees"
            )
        ),
        MarketingService(
            id = "svc_creator_collab",
            title = "Creator & Influencer Promotional Campaigns",
            serviceNumber = 6,
            category = ServiceCategory.CREATOR_COLLAB,
            supportedPlatforms = listOf(SocialPlatform.INSTAGRAM, SocialPlatform.TIKTOK, SocialPlatform.YOUTUBE),
            description = "Connect directly with vetted micro and macro influencers for organic authentic product endorsements and shoutouts.",
            basePriceUsd = 89.00,
            priceUnit = "Per Verified Creator",
            estimatedDeliveryDays = "4-7 Days",
            legalCompliancePledge = "All creators undergo audience quality audits. Guaranteed 0% automated bot followers.",
            features = listOf(
                "Manual creator vetting & audience audit",
                "Custom content briefing & script review",
                "FTC / Legal sponsored disclosure tags",
                "High engagement rate guarantees"
            )
        ),
        MarketingService(
            id = "svc_sponsored_posts",
            title = "Sponsored Posts & Co-Branded Media",
            serviceNumber = 7,
            category = ServiceCategory.CREATOR_COLLAB,
            supportedPlatforms = listOf(SocialPlatform.FACEBOOK, SocialPlatform.INSTAGRAM, SocialPlatform.TIKTOK),
            description = "Publish sponsored content co-authored with recognized industry pages and verified content hubs.",
            basePriceUsd = 59.00,
            priceUnit = "Per Sponsored Post",
            estimatedDeliveryDays = "2-4 Days",
            legalCompliancePledge = "Uses official Paid Partnership tagging on Instagram and Facebook. Full compliance with platform commercial terms.",
            features = listOf(
                "Official 'Paid Partnership' badge integration",
                "High-resolution graphic or video creative",
                "Pinned post option for 48 hours",
                "Detailed comment & sentiment report"
            )
        ),
        MarketingService(
            id = "svc_content_promotion",
            title = "Content Promotion & Social Syndication",
            serviceNumber = 8,
            category = ServiceCategory.CONTENT_DISTRIBUTION,
            supportedPlatforms = listOf(SocialPlatform.FACEBOOK, SocialPlatform.INSTAGRAM, SocialPlatform.TELEGRAM, SocialPlatform.YOUTUBE),
            description = "Distribute high-quality blog posts, podcast episodes, or creative portfolios across curated social communities.",
            basePriceUsd = 35.00,
            priceUnit = "Per Content Package",
            estimatedDeliveryDays = "3-5 Days",
            legalCompliancePledge = "Organic community sharing following group and channel rules. No spamming or duplicate post bombing.",
            features = listOf(
                "Multi-channel syndication schedule",
                "Curated group moderator approval",
                "SEO-friendly canonical linking",
                "Direct traffic click-through tracking"
            )
        ),
        MarketingService(
            id = "svc_brand_awareness",
            title = "Brand Awareness & Reach Campaigns",
            serviceNumber = 9,
            category = ServiceCategory.BRANDING,
            supportedPlatforms = listOf(SocialPlatform.FACEBOOK, SocialPlatform.INSTAGRAM, SocialPlatform.TIKTOK, SocialPlatform.YOUTUBE),
            description = "High-frequency impression campaigns designed to maximize brand recall, name recognition, and market visibility.",
            basePriceUsd = 65.00,
            priceUnit = "Per Awareness Tier",
            estimatedDeliveryDays = "5-7 Days",
            legalCompliancePledge = "Verified unique reach verified through ad network frequency capping algorithms.",
            features = listOf(
                "Frequency capping to prevent ad fatigue",
                "Demographic brand-lift measurement",
                "Cross-platform unified storytelling",
                "Audience sentiment analysis report"
            )
        ),
        MarketingService(
            id = "svc_web_app_promo",
            title = "Website & Mobile App Promotion",
            serviceNumber = 10,
            category = ServiceCategory.TRAFFIC,
            supportedPlatforms = listOf(SocialPlatform.FACEBOOK, SocialPlatform.INSTAGRAM, SocialPlatform.TIKTOK, SocialPlatform.MESSENGER),
            description = "Drive genuine, high-intent visitors and qualified app installs through targeted conversion-focused social ads.",
            basePriceUsd = 55.00,
            priceUnit = "Per Traffic Batch",
            estimatedDeliveryDays = "3-6 Days",
            legalCompliancePledge = "Real human traffic only. Monitored with Google Analytics 4 and AppsFlyer/Adjust compatibility.",
            features = listOf(
                "App Store / Play Store direct deep-linking",
                "High-intent audience interest layering",
                "UTM parameter generation & tracking",
                "Bot-filter verification on all clicks"
            )
        ),
        MarketingService(
            id = "svc_real_creators",
            title = "Real Human Creator Campaigns",
            serviceNumber = 11,
            category = ServiceCategory.CREATOR_COLLAB,
            supportedPlatforms = listOf(SocialPlatform.TIKTOK, SocialPlatform.INSTAGRAM, SocialPlatform.YOUTUBE),
            description = "UGC (User Generated Content) produced by genuine on-camera content creators showcasing your product authentically.",
            basePriceUsd = 79.00,
            priceUnit = "Per UGC Video Asset",
            estimatedDeliveryDays = "4-6 Days",
            legalCompliancePledge = "Real people filmed in natural settings. Full commercial usage rights granted. No AI-generated or spoofed avatars.",
            features = listOf(
                "Authentic smartphone vertical format (9:16)",
                "Full commercial license included",
                "Hooks, voiceover, and captions provided",
                "Delivered in 4K / 1080p source format"
            )
        ),
        MarketingService(
            id = "svc_campaign_analytics",
            title = "Campaign Analytics & Audit Reporting",
            serviceNumber = 12,
            category = ServiceCategory.REPORTING,
            supportedPlatforms = listOf(SocialPlatform.ALL),
            description = "Independent audit and comprehensive performance report analyzing reach validity, CPM efficiency, and audience authenticity.",
            basePriceUsd = 25.00,
            priceUnit = "Per Audit Report",
            estimatedDeliveryDays = "1-2 Days",
            legalCompliancePledge = "Cryptographically signed audit hash proving organic integrity and platform ad spend verification.",
            features = listOf(
                "Impression & reach authenticity verification",
                "CPM, CPC, and CTR comparative benchmarks",
                "Audience geography and device breakdown",
                "Cryptographic tamper-proof audit certificate"
            )
        )
    )
}
