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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.components.StatusChip
import com.example.components.WavesCard
import com.example.components.WavesChip
import com.example.components.WavesFAB
import com.example.components.WavesHeader
import com.example.components.showDemoToast
import com.example.data.SampleData
import com.example.ui.theme.BackgroundColor
import com.example.ui.theme.DangerRed
import com.example.ui.theme.EmeraldInk
import com.example.ui.theme.OnPrimary
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun ClientDetailScreen(
    clientId: String,
    onNavigateBack: () -> Unit,
    onNavigateToEditClient: (String) -> Unit,
    onNavigateToCreateInvoice: () -> Unit,
    onNavigateToInvoiceDetail: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val client = SampleData.clients.find { it.id == clientId } ?: SampleData.clients.first()
    var selectedTab by remember { mutableStateOf("Invoices") }
    var menuExpanded by remember { mutableStateOf(false) }

    val clientInvoices = SampleData.invoices.filter { it.clientId == client.id }

    Scaffold(
        topBar = {
            WavesHeader(
                title = client.name,
                onBackClick = onNavigateBack,
                actions = {
                    Box {
                        IconButton(
                            onClick = { menuExpanded = true },
                            modifier = Modifier.testTag("client_detail_menu_button")
                        ) {
                            Icon(
                                imageVector = Icons.Filled.MoreVert,
                                contentDescription = "Menu",
                                tint = OnPrimary
                            )
                        }
                        DropdownMenu(
                            expanded = menuExpanded,
                            onDismissRequest = { menuExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Edit Client") },
                                onClick = {
                                    menuExpanded = false
                                    onNavigateToEditClient(client.id)
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Send Statement") },
                                onClick = {
                                    menuExpanded = false
                                    showDemoToast(context, "Statement sent via email")
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Archive Client") },
                                onClick = {
                                    menuExpanded = false
                                    showDemoToast(context, "Client archived")
                                    onNavigateBack()
                                }
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            WavesFAB(
                onClick = onNavigateToCreateInvoice,
                contentDescription = "New Invoice"
            )
        },
        containerColor = BackgroundColor,
        modifier = modifier.testTag("client_detail_screen")
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

            // Profile card: avatar, name, email, phone, city
            WavesCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(EmeraldInk.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = null,
                            tint = EmeraldInk,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = client.name,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Email, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = client.email ?: "No email provided", fontSize = 12.sp, color = TextSecondary)
                        }
                        Spacer(modifier = Modifier.height(3.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Phone, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = client.phone, fontSize = 12.sp, color = TextSecondary)
                        }
                        Spacer(modifier = Modifier.height(3.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.LocationOn, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "${client.city}, ${client.country}", fontSize = 12.sp, color = TextSecondary)
                        }
                    }
                }
            }

            // Stat row: ₹25,760 billed | ₹20,760 paid | ₹5,000 due
            WavesCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Billed", fontSize = 12.sp, color = TextSecondary)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = SampleData.formatCurrency(client.totalBilled),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }

                    Box(
                        modifier = Modifier
                            .height(32.dp)
                            .width(1.dp)
                            .background(BackgroundColor)
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Paid", fontSize = 12.sp, color = TextSecondary)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = SampleData.formatCurrency(client.totalPaid),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = SuccessGreen
                        )
                    }

                    Box(
                        modifier = Modifier
                            .height(32.dp)
                            .width(1.dp)
                            .background(BackgroundColor)
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Due", fontSize = 12.sp, color = TextSecondary)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = SampleData.formatCurrency(client.totalDue),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (client.totalDue > 0) DangerRed else SuccessGreen
                        )
                    }
                }
            }

            // Tabs: [Invoices] [Payments] [Notes]
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("Invoices", "Payments", "Notes").forEach { tab ->
                    WavesChip(
                        text = tab,
                        isSelected = selectedTab == tab,
                        onClick = { selectedTab = tab }
                    )
                }
            }

            when (selectedTab) {
                "Invoices" -> {
                    if (clientInvoices.isEmpty()) {
                        Text("No invoices yet for this client.", fontSize = 14.sp, color = TextSecondary)
                    } else {
                        clientInvoices.forEach { invoice ->
                            WavesCard(
                                onClick = { onNavigateToInvoiceDetail(invoice.id) }
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text(text = invoice.id, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                        Text(text = "Due ${invoice.dueDate}", fontSize = 12.sp, color = TextSecondary)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = SampleData.formatCurrency(invoice.grandTotal),
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = TextPrimary
                                        )
                                    }
                                    StatusChip(status = invoice.status)
                                }
                            }
                        }
                    }
                }

                "Payments" -> {
                    val allPayments = clientInvoices.flatMap { it.payments }
                    if (allPayments.isEmpty()) {
                        Text("No payment records found.", fontSize = 14.sp, color = TextSecondary)
                    } else {
                        allPayments.forEach { payment ->
                            WavesCard {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(text = payment.method, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                        Text(text = "${payment.date} · Ref: ${payment.reference}", fontSize = 12.sp, color = TextSecondary)
                                        Text(text = payment.notes, fontSize = 12.sp, color = TextSecondary)
                                    }
                                    Text(
                                        text = "+${SampleData.formatCurrency(payment.amount)}",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = SuccessGreen
                                    )
                                }
                            }
                        }
                    }
                }

                "Notes" -> {
                    WavesCard {
                        Column {
                            Text(text = "Client Notes", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = client.notes,
                                fontSize = 14.sp,
                                color = TextSecondary,
                                lineHeight = 20.sp
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "GST / Tax ID: ${client.taxNumber}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = TextPrimary
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(72.dp))
        }
    }
}
