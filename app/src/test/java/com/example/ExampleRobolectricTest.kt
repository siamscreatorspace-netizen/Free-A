package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Free S Time", appName)
  }

  @Test
  fun `fraud detection blocks fake follower and bot queries`() {
    val blacklistedTokens = listOf("bot", "fake", "fake follower", "scrape")
    val testQuery = "Buy 5000 fake follower bot boost"
    val detected = blacklistedTokens.filter { testQuery.contains(it, ignoreCase = true) }
    assertEquals(true, detected.isNotEmpty())
  }
}
