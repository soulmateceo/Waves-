package com.example.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.components.BannerAdPlaceholder
import com.example.components.StatusChip
import com.example.components.WavesBottomNav
import com.example.components.WavesCard
import com.example.components.WavesHeader
import com.example.components.WavesNavTab
import com.example.components.WavesPrimaryButton
import com.example.components.showDemoToast
import com.example.data.InvoiceStatus
import com.example.data.SampleData
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.BackgroundColor
import com.example.ui.theme.EmeraldInk
import com.example.ui.theme.OnPrimary
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun DashboardScreen(
    onNavigateToCreateInvoice: () -> Unit,
    onNavigateToInvoiceList: () -> Unit,
    onNavigateToInvoiceDetail: (String) -> Unit,
    onNavigateToBusinessProfile: () -> Unit,
    onNavigateToReports: () -> Unit,
    onNavigateToProducts: () -> Unit,
    onNavigateToTab: (WavesNavTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            WavesHeader(
                title = "WAVES",
                actions = {
                    IconButton(
                        onClick = { showDemoToast(context, "No unread notifications") },
                        modifier = Modifier.testTag("dashboard_notifications_button")
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Notifications,
                            contentDescription = "Notifications",
                            tint = OnPrimary
                        )
                    }
                    IconButton(
                        onClick = { onNavigateToTab(WavesNavTab.SETTINGS) },
                        modifier = Modifier.testTag("dashboard_settings_button")
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = "Settings",
                            tint = OnPrimary
                        )
                    }
                }
            )
        },
        bottomBar = {
            WavesBottomNav(
                currentTab = WavesNavTab.HOME,
                onTabSelected = onNavigateToTab
            )
        },
        containerColor = BackgroundColor,
        modifier = modifier.testTag("dashboard_screen")
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Greeting: "Good morning, Rahul 👋" 20sp bold
            Text(
                text = "Good morning, Rahul 👋",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            // Big emerald button: "+ NEW INVOICE"
            WavesPrimaryButton(
                text = "+ NEW INVOICE",
                icon = Icons.Filled.Add,
                onClick = onNavigateToCreateInvoice
            )

            // Two stat cards side-by-side: [12 Invoices] [₹45,200 This Month]
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                WavesCard(
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToInvoiceList
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Total Invoices",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                            Icon(
                                imageVector = Icons.Filled.ReceiptLong,
                                contentDescription = null,
                                tint = EmeraldInk,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "12 Invoices",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "3 pending",
                            fontSize = 11.sp,
                            color = AccentCyan
                        )
                    }
                }

                WavesCard(
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToReports
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Revenue",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                            Icon(
                                imageVector = Icons.Filled.Assessment,
                                contentDescription = null,
                                tint = AccentCyan,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "₹45,200",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "This Month",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                }
            }

            // Quick Actions Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                QuickActionChip(
                    label = "Business",
                    icon = Icons.Filled.Business,
                    onClick = onNavigateToBusinessProfile,
                    modifier = Modifier.weight(1f)
                )
                QuickActionChip(
                    label = "Products",
                    icon = Icons.Filled.Inventory2,
                    onClick = onNavigateToProducts,
                    modifier = Modifier.weight(1f)
                )
                QuickActionChip(
                    label = "Reports",
                    icon = Icons.Filled.Assessment,
                    onClick = onNavigateToReports,
                    modifier = Modifier.weight(1f)
                )
            }

            // Section "Recent Invoices" with "See All" link
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Recent Invoices",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )
                Text(
                    text = "See All",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = EmeraldInk,
                    modifier = Modifier
                        .clickable { onNavigateToInvoiceList() }
                        .padding(4.dp)
                )
            }

            // 3 invoice cards:
            // INV-001 · Rahul Sharma · ₹8,260 · PAID (green chip)
            // INV-002 · Priya Mehta · ₹12,500 · HALF PAID (amber chip)
            // INV-003 · Amit Verma · ₹3,200 · PENDING (gray chip)
            val recentInvoices = listOf(
                Triple("INV-001", "Rahul Sharma", 8260.0 to InvoiceStatus.PAID),
                Triple("INV-002", "Priya Mehta", 12500.0 to InvoiceStatus.HALF_PAID),
                Triple("INV-003", "Amit Verma", 3200.0 to InvoiceStatus.PENDING)
            )

            recentInvoices.forEach { (invId, client, amountStatus) ->
                val (amount, status) = amountStatus
                WavesCard(
                    onClick = { onNavigateToInvoiceDetail(invId) }
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = invId,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "· $client",
                                    fontSize = 14.sp,
                                    color = TextSecondary
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = SampleData.formatCurrency(amount),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary
                            )
                        }

                        StatusChip(status = status)
                    }
                }
            }

            // Banner ad placeholder at bottom
            BannerAdPlaceholder()

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun QuickActionChip(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(10.dp),
        color = androidx.compose.ui.graphics.Color(0xFFE6F4EA)
    ) {
        Row(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = EmeraldInk,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = EmeraldInk
            )
        }
    }
}
