package com.example.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.components.BannerAdPlaceholder
import com.example.components.EmptyStateView
import com.example.components.StatusChip
import com.example.components.WavesBottomNav
import com.example.components.WavesCard
import com.example.components.WavesChip
import com.example.components.WavesFAB
import com.example.components.WavesHeader
import com.example.components.WavesNavTab
import com.example.components.WavesTextField
import com.example.data.InvoiceStatus
import com.example.data.SampleData
import com.example.ui.theme.BackgroundColor
import com.example.ui.theme.DangerRed
import com.example.ui.theme.OnPrimary
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun InvoiceListScreen(
    onNavigateToCreateInvoice: () -> Unit,
    onNavigateToInvoiceDetail: (String) -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToTab: (WavesNavTab) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedFilter by remember { mutableStateOf("All") }
    var searchQuery by remember { mutableStateOf("") }
    var isSearchVisible by remember { mutableStateOf(false) }

    val statusTabs = listOf("All", "Pending", "Partial", "Paid", "Overdue", "Cancelled")

    val allInvoices = SampleData.invoices
    val filteredInvoices = allInvoices.filter { invoice ->
        val matchesFilter = when (selectedFilter) {
            "Pending" -> invoice.status == InvoiceStatus.PENDING
            "Partial" -> invoice.status == InvoiceStatus.HALF_PAID
            "Paid" -> invoice.status == InvoiceStatus.PAID
            "Overdue" -> invoice.status == InvoiceStatus.OVERDUE
            "Cancelled" -> invoice.status == InvoiceStatus.CANCELLED
            else -> true
        }
        val matchesSearch = invoice.id.contains(searchQuery, ignoreCase = true) ||
                invoice.clientName.contains(searchQuery, ignoreCase = true)
        matchesFilter && matchesSearch
    }

    Scaffold(
        topBar = {
            WavesHeader(
                title = "Invoices",
                actions = {
                    IconButton(
                        onClick = { isSearchVisible = !isSearchVisible },
                        modifier = Modifier.testTag("invoice_search_button")
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search",
                            tint = OnPrimary
                        )
                    }
                    IconButton(
                        onClick = onNavigateToSettings,
                        modifier = Modifier.testTag("invoice_settings_header_button")
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
                currentTab = WavesNavTab.INVOICES,
                onTabSelected = onNavigateToTab
            )
        },
        floatingActionButton = {
            WavesFAB(
                onClick = onNavigateToCreateInvoice,
                contentDescription = "New Invoice"
            )
        },
        containerColor = BackgroundColor,
        modifier = modifier.testTag("invoice_list_screen")
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (isSearchVisible) {
                Box(modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)) {
                    WavesTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        label = "Search Invoices",
                        placeholder = "Invoice number or client name...",
                        leadingIcon = Icons.Filled.Search
                    )
                }
            }

            // Horizontal scroll status tabs: [All][Pending][Partial][Paid][Overdue][Cancelled]
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                statusTabs.forEach { tab ->
                    WavesChip(
                        text = tab,
                        isSelected = selectedFilter == tab,
                        onClick = { selectedFilter = tab }
                    )
                }
            }

            if (filteredInvoices.isEmpty()) {
                EmptyStateView(
                    icon = Icons.Filled.Description,
                    title = "No invoices found",
                    message = "No invoices found for '$selectedFilter'. Create a new invoice to get started.",
                    buttonText = "+ New Invoice",
                    onButtonClick = onNavigateToCreateInvoice
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredInvoices) { invoice ->
                        WavesCard(
                            onClick = { onNavigateToInvoiceDetail(invoice.id) }
                        ) {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = invoice.id,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextPrimary
                                        )
                                        Text(
                                            text = " · ${invoice.clientName}",
                                            fontSize = 14.sp,
                                            color = TextSecondary
                                        )
                                    }
                                    StatusChip(status = invoice.status)
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text(
                                            text = SampleData.formatCurrency(invoice.grandTotal),
                                            fontSize = 17.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextPrimary
                                        )
                                        Text(
                                            text = "Due ${invoice.dueDate}",
                                            fontSize = 12.sp,
                                            color = TextSecondary
                                        )
                                    }

                                    if (invoice.balanceDue > 0 && invoice.status == InvoiceStatus.HALF_PAID) {
                                        Text(
                                            text = "${SampleData.formatCurrency(invoice.balanceDue)} due",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = DangerRed
                                        )
                                    }
                                }
                            }
                        }
                    }

                    item {
                        BannerAdPlaceholder(modifier = Modifier.padding(vertical = 8.dp))
                    }

                    item {
                        Spacer(modifier = Modifier.height(72.dp))
                    }
                }
            }
        }
    }
}
