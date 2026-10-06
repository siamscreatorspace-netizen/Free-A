package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.AppNotification
import com.example.data.model.CampaignOrder
import com.example.data.model.CampaignStatus
import com.example.data.model.CreatorProfile
import com.example.data.model.PaymentTransaction
import com.example.data.model.SocialPlatform
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {
    @Query("SELECT * FROM campaign_orders ORDER BY createdAt DESC")
    fun getAllOrders(): Flow<List<CampaignOrder>>

    @Query("SELECT * FROM campaign_orders WHERE status = :status ORDER BY createdAt DESC")
    fun getOrdersByStatus(status: CampaignStatus): Flow<List<CampaignOrder>>

    @Query("SELECT * FROM campaign_orders WHERE id = :id")
    suspend fun getOrderById(id: Long): CampaignOrder?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: CampaignOrder): Long

    @Update
    suspend fun updateOrder(order: CampaignOrder)

    @Query("UPDATE campaign_orders SET status = :newStatus, adminNotes = :notes WHERE id = :id")
    suspend fun updateOrderStatus(id: Long, newStatus: CampaignStatus, notes: String)

    @Query("UPDATE campaign_orders SET status = 'REJECTED', rejectionReason = :reason WHERE id = :id")
    suspend fun rejectOrder(id: Long, reason: String)

    @Query("DELETE FROM campaign_orders WHERE id = :id")
    suspend fun deleteOrder(id: Long)

    @Query("SELECT COUNT(*) FROM campaign_orders")
    fun getOrderCount(): Flow<Int>

    @Query("SELECT SUM(budgetUsd) FROM campaign_orders WHERE status != 'REJECTED'")
    fun getTotalSpent(): Flow<Double?>
}

@Dao
interface CreatorDao {
    @Query("SELECT * FROM creators ORDER BY rating DESC")
    fun getAllCreators(): Flow<List<CreatorProfile>>

    @Query("SELECT * FROM creators WHERE platform = :platform ORDER BY rating DESC")
    fun getCreatorsByPlatform(platform: SocialPlatform): Flow<List<CreatorProfile>>

    @Query("SELECT * FROM creators WHERE category = :category ORDER BY rating DESC")
    fun getCreatorsByCategory(category: String): Flow<List<CreatorProfile>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCreator(creator: CreatorProfile): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(creators: List<CreatorProfile>)

    @Update
    suspend fun updateCreator(creator: CreatorProfile)

    @Delete
    suspend fun deleteCreator(creator: CreatorProfile)

    @Query("SELECT COUNT(*) FROM creators")
    fun getCreatorCount(): Flow<Int>
}

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions ORDER BY timestamp DESC")
    fun getAllTransactions(): Flow<List<PaymentTransaction>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: PaymentTransaction): Long

    @Query("UPDATE transactions SET status = 'REFUNDED' WHERE orderId = :orderId")
    suspend fun markRefunded(orderId: Long)
}

@Dao
interface NotificationDao {
    @Query("SELECT * FROM notifications ORDER BY timestamp DESC")
    fun getAllNotifications(): Flow<List<AppNotification>>

    @Query("SELECT COUNT(*) FROM notifications WHERE isRead = 0")
    fun getUnreadCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: AppNotification): Long

    @Query("UPDATE notifications SET isRead = 1 WHERE id = :id")
    suspend fun markAsRead(id: Long)

    @Query("UPDATE notifications SET isRead = 1")
    suspend fun markAllAsRead()
}
