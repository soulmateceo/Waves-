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
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.components.StatusChip
import com.example.components.WavesCard
import com.example.components.WavesHeader
import com.example.components.WavesPrimaryButton
import com.example.components.WavesSecondaryButton
import com.example.components.showDemoToast
import com.example.data.InvoiceStatus
import com.example.data.SampleData
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.BackgroundColor
import com.example.ui.theme.BorderGray
import com.example.ui.theme.DangerRed
import com.example.ui.theme.EmeraldInk
import com.example.ui.theme.OnPrimary
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WarningAmber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvoiceDetailScreen(
    invoiceId: String,
    onNavigateBack: () -> Unit,
    onNavigateToPreview: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val invoice = SampleData.invoices.find { it.id == invoiceId } ?: SampleData.invoices.last()

    var status by remember { mutableStateOf(invoice.status) }
    var paidAmount by remember { mutableDoubleStateOf(invoice.paidAmount) }
    var showPaymentSheet by remember { mutableStateOf(false) }
    var menuExpanded by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val balanceDue = (invoice.grandTotal - paidAmount).coerceAtLeast(0.0)

    Scaffold(
        topBar = {
            WavesHeader(
                title = invoice.id,
                onBackClick = onNavigateBack,
                actions = {
                    Box {
                        IconButton(
                            onClick = { menuExpanded = true },
                            modifier = Modifier.testTag("invoice_detail_menu_button")
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
                                text = { Text("Preview PDF") },
                                onClick = {
                                    menuExpanded = false
                                    onNavigateToPreview(invoice.id)
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Duplicate Invoice") },
                                onClick = {
                                    menuExpanded = false
                                    showDemoToast(context, "Invoice duplicated as INV-005")
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Delete Invoice") },
                                onClick = {
                                    menuExpanded = false
                                    showDemoToast(context, "Invoice deleted")
                                    onNavigateBack()
                                }
                            )
                        }
                    }
                }
            )
        },
        containerColor = BackgroundColor,
        modifier = modifier.testTag("invoice_detail_screen")
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(6.dp))

            // Status card: ₹8,260 · HALF PAID · ₹4,130 balance due
            WavesCard {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = SampleData.formatCurrency(invoice.grandTotal),
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            if (balanceDue > 0) {
                                Text(
                                    text = "${SampleData.formatCurrency(balanceDue)} balance due",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = DangerRed
                                )
                            } else {
                                Text(
                                    text = "Fully paid on time",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = SuccessGreen
                                )
                            }
                        }

                        StatusChip(status = status)
                    }
                }
            }

            // Client card
            SectionHeader(title = "CLIENT")
            WavesCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(EmeraldInk.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = null,
                            tint = EmeraldInk,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = invoice.clientName,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = invoice.clientEmail ?: "Client Email: N/A",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                }
            }

            // Dates card
            SectionHeader(title = "DATES")
            WavesCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(text = "Issue Date", fontSize = 12.sp, color = TextSecondary)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(text = invoice.issueDate, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(text = "Due Date", fontSize = 12.sp, color = TextSecondary)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(text = invoice.dueDate, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                    }
                }
            }

            // Items card
            SectionHeader(title = "ITEMS (${invoice.items.size})")
            WavesCard {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    invoice.items.forEach { item ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(item.name, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = TextPrimary)
                                Text("${item.quantity} × ${SampleData.formatCurrency(item.unitPrice)}", fontSize = 12.sp, color = TextSecondary)
                            }
                            Text(SampleData.formatCurrency(item.total), fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                        }
                        HorizontalDivider(color = Color(0xFFF1F5F9))
                    }
                }
            }

            // Summary card with balance breakdown
            SectionHeader(title = "PAYMENT SUMMARY")
            WavesCard {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Subtotal", fontSize = 13.sp, color = TextSecondary)
                        Text(SampleData.formatCurrency(invoice.subtotal), fontSize = 13.sp, color = TextPrimary)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Tax (${invoice.items.firstOrNull()?.taxRate?.toInt() ?: 18}%)", fontSize = 13.sp, color = TextSecondary)
                        Text(SampleData.formatCurrency(invoice.taxAmount), fontSize = 13.sp, color = TextPrimary)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Paid so far", fontSize = 13.sp, color = SuccessGreen)
                        Text("-${SampleData.formatCurrency(paidAmount)}", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = SuccessGreen)
                    }
                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), color = BorderGray)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Balance Due", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Text(
                            SampleData.formatCurrency(balanceDue),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (balanceDue > 0) DangerRed else SuccessGreen
                        )
                    }
                }
            }

            // Grid of action buttons (2 columns):
            // [Record Payment] [Mark Paid]
            // [Mark Half Paid] [Download PDF]
            // [Share] [Copy Link]
            // [Write Off] [Cancel Invoice]
            // [Duplicate] [Delete]
            SectionHeader(title = "ACTIONS")
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    ActionButtonItem(
                        text = "Record Payment",
                        icon = Icons.Filled.Payment,
                        isPrimary = true,
                        onClick = { showPaymentSheet = true },
                        modifier = Modifier.weight(1f)
                    )
                    ActionButtonItem(
                        text = "Mark Paid",
                        icon = Icons.Filled.CheckCircle,
                        isPrimary = false,
                        onClick = {
                            status = InvoiceStatus.PAID
                            paidAmount = invoice.grandTotal
                            showDemoToast(context, "Marked as Paid!")
                        },
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    ActionButtonItem(
                        text = "Mark Half Paid",
                        icon = Icons.Filled.Receipt,
                        isPrimary = false,
                        onClick = {
                            status = InvoiceStatus.HALF_PAID
                            paidAmount = invoice.grandTotal / 2
                            showDemoToast(context, "Marked as Half Paid")
                        },
                        modifier = Modifier.weight(1f)
                    )
                    ActionButtonItem(
                        text = "Download PDF",
                        icon = Icons.Filled.Download,
                        isPrimary = false,
                        onClick = { onNavigateToPreview(invoice.id) },
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    ActionButtonItem(
                        text = "Share",
                        icon = Icons.Filled.Share,
                        isPrimary = false,
                        onClick = { showDemoToast(context, "Share sheet opened (demo)") },
                        modifier = Modifier.weight(1f)
                    )
                    ActionButtonItem(
                        text = "Copy Link",
                        icon = Icons.Filled.Link,
                        isPrimary = false,
                        onClick = { showDemoToast(context, "Invoice link copied!") },
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    ActionButtonItem(
                        text = "Write Off",
                        icon = Icons.Filled.Cancel,
                        isPrimary = false,
                        isDestructive = true,
                        onClick = {
                            status = InvoiceStatus.CANCELLED
                            showDemoToast(context, "Invoice written off")
                        },
                        modifier = Modifier.weight(1f)
                    )
                    ActionButtonItem(
                        text = "Cancel Invoice",
                        icon = Icons.Filled.Cancel,
                        isPrimary = false,
                        isDestructive = true,
                        onClick = {
                            status = InvoiceStatus.CANCELLED
                            showDemoToast(context, "Invoice marked as Cancelled")
                        },
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    ActionButtonItem(
                        text = "Duplicate",
                        icon = Icons.Filled.ContentCopy,
                        isPrimary = false,
                        onClick = { showDemoToast(context, "Duplicated as draft") },
                        modifier = Modifier.weight(1f)
                    )
                    ActionButtonItem(
                        text = "Delete",
                        icon = Icons.Filled.Delete,
                        isPrimary = false,
                        isDestructive = true,
                        onClick = {
                            showDemoToast(context, "Invoice deleted")
                            onNavigateBack()
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))
        }

        // Record Payment ModalBottomSheet (Screen 16)
        if (showPaymentSheet) {
            RecordPaymentBottomSheet(
                balanceDue = balanceDue,
                sheetState = sheetState,
                onDismissRequest = { showPaymentSheet = false },
                onPaymentSaved = { amount, method ->
                    paidAmount = (paidAmount + amount).coerceAtMost(invoice.grandTotal)
                    if (paidAmount >= invoice.grandTotal) {
                        status = InvoiceStatus.PAID
                    } else if (paidAmount > 0) {
                        status = InvoiceStatus.HALF_PAID
                    }
                }
            )
        }
    }
}

@Composable
private fun ActionButtonItem(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isPrimary: Boolean = false,
    isDestructive: Boolean = false
) {
    if (isPrimary) {
        WavesPrimaryButton(
            text = text,
            icon = icon,
            onClick = onClick,
            modifier = modifier.height(48.dp)
        )
    } else {
        WavesSecondaryButton(
            text = text,
            icon = icon,
            isDestructive = isDestructive,
            onClick = onClick,
            modifier = modifier.height(48.dp)
        )
    }
}
