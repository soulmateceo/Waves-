package com.example.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.components.WavesBottomNav
import com.example.components.WavesCard
import com.example.components.WavesHeader
import com.example.components.WavesNavTab
import com.example.components.WavesSecondaryButton
import com.example.components.showDemoToast
import com.example.data.SampleData
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.BackgroundColor
import com.example.ui.theme.DangerRed
import com.example.ui.theme.EmeraldInk
import com.example.ui.theme.NeutralGray
import com.example.ui.theme.OnPrimary
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WarningAmber

@Composable
fun ReportsScreen(
    onNavigateBack: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToTab: (WavesNavTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val months = listOf("Aug 2026", "Sep 2026", "Oct 2026", "Nov 2026")
    var currentMonthIndex by remember { mutableIntStateOf(2) }

    Scaffold(
        topBar = {
            WavesHeader(
                title = "Reports",
                onBackClick = onNavigateBack,
                actions = {
                    IconButton(
                        onClick = onNavigateToSettings,
                        modifier = Modifier.testTag("reports_settings_button")
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
        modifier = modifier.testTag("reports_screen")
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(4.dp))

            // Month selector: ◀ Oct 2026 ▶
            WavesCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            if (currentMonthIndex > 0) currentMonthIndex--
                        },
                        enabled = currentMonthIndex > 0
                    ) {
                        Icon(Icons.Filled.ArrowBackIosNew, contentDescription = "Previous Month", tint = EmeraldInk, modifier = Modifier.size(18.dp))
                    }

                    Text(
                        text = months[currentMonthIndex],
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    IconButton(
                        onClick = {
                            if (currentMonthIndex < months.size - 1) currentMonthIndex++
                        },
                        enabled = currentMonthIndex < months.size - 1
                    ) {
                        Icon(Icons.Filled.ArrowForwardIos, contentDescription = "Next Month", tint = EmeraldInk, modifier = Modifier.size(18.dp))
                    }
                }
            }

            // 2x2 stat grid cards:
            // ₹1,25,000 Invoiced | ₹98,500 Collected
            // ₹26,500 Outstanding | 12 Invoices
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatGridItem(
                    label = "Invoiced",
                    value = "₹1,25,000",
                    valueColor = TextPrimary,
                    modifier = Modifier.weight(1f)
                )
                StatGridItem(
                    label = "Collected",
                    value = "₹98,500",
                    valueColor = SuccessGreen,
                    modifier = Modifier.weight(1f)
                )
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatGridItem(
                    label = "Outstanding",
                    value = "₹26,500",
                    valueColor = DangerRed,
                    modifier = Modifier.weight(1f)
                )
                StatGridItem(
                    label = "Total Invoices",
                    value = "12 Invoices",
                    valueColor = EmeraldInk,
                    modifier = Modifier.weight(1f)
                )
            }

            // Status breakdown with horizontal bars:
            // Paid ████████ 8
            // Half Paid ███ 2
            // Pending ██ 1
            // Overdue █ 1
            // Cancelled ▏1
            SectionHeader(title = "STATUS BREAKDOWN")
            WavesCard {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    BreakdownBarRow(label = "Paid", count = 8, total = 13, barColor = SuccessGreen)
                    BreakdownBarRow(label = "Half Paid", count = 2, total = 13, barColor = WarningAmber)
                    BreakdownBarRow(label = "Pending", count = 1, total = 13, barColor = NeutralGray)
                    BreakdownBarRow(label = "Overdue", count = 1, total = 13, barColor = DangerRed)
                    BreakdownBarRow(label = "Cancelled", count = 1, total = 13, barColor = Color.LightGray)
                }
            }

            // Top Clients list (numbered)
            SectionHeader(title = "TOP CLIENTS")
            WavesCard {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    SampleData.clients.forEachIndexed { index, client ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(EmeraldInk.copy(alpha = 0.1f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "${index + 1}",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = EmeraldInk
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = client.name,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = "${client.invoiceCount} invoices",
                                        fontSize = 11.sp,
                                        color = TextSecondary
                                    )
                                }
                            }
                            Text(
                                text = SampleData.formatCurrency(client.totalBilled),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }
                    }
                }
            }

            // Export buttons: [Export CSV] [Export PDF]
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                WavesSecondaryButton(
                    text = "Export CSV",
                    icon = Icons.Filled.Download,
                    onClick = { showDemoToast(context, "CSV Report exported to Downloads") },
                    modifier = Modifier.weight(1f)
                )

                WavesSecondaryButton(
                    text = "Export PDF",
                    icon = Icons.Filled.PictureAsPdf,
                    onClick = { showDemoToast(context, "PDF Report generated") },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun StatGridItem(
    label: String,
    value: String,
    valueColor: Color,
    modifier: Modifier = Modifier
) {
    WavesCard(modifier = modifier) {
        Column {
            Text(text = label, fontSize = 12.sp, color = TextSecondary)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = valueColor
            )
        }
    }
}

@Composable
private fun BreakdownBarRow(
    label: String,
    count: Int,
    total: Int,
    barColor: Color
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, fontSize = 13.sp, color = TextPrimary, fontWeight = FontWeight.Medium)
            Text(text = "$count", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        }
        Spacer(modifier = Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Color(0xFFF1F5F9))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction = (count.toFloat() / total.toFloat()).coerceIn(0.04f, 1f))
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(4.dp))
                    .background(barColor)
            )
        }
    }
}
