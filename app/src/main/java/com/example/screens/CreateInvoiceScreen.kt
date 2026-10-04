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
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.components.StateScreen
import com.example.components.StateType
import com.example.components.WavesCard
import com.example.components.WavesHeader
import com.example.components.WavesPrimaryButton
import com.example.components.WavesSecondaryButton
import com.example.components.WavesTextField
import com.example.components.showDemoToast
import com.example.data.Client
import com.example.data.InvoiceItem
import com.example.data.SampleData
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.BackgroundColor
import com.example.ui.theme.BorderGray
import com.example.ui.theme.DangerRed
import com.example.ui.theme.EmeraldInk
import com.example.ui.theme.InputBorderGray
import com.example.ui.theme.OnPrimary
import com.example.ui.theme.SurfaceColor
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CreateInvoiceScreen(
    onNavigateBack: () -> Unit,
    onNavigateToPreview: (String) -> Unit,
    onNavigateToAddClient: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var invoiceNumber by remember { mutableStateOf("INV-004") }
    var issueDate by remember { mutableStateOf("04 Oct 2026") }
    var dueDate by remember { mutableStateOf("19 Oct 2026") }

    var isSaving by remember { mutableStateOf(false) }
    var showSuccess by remember { mutableStateOf(false) }

    if (isSaving) {
        StateScreen(
            type = StateType.LOADING,
            message = "Generating and saving invoice..."
        )
        return
    }

    if (showSuccess) {
        StateScreen(
            type = StateType.SUCCESS,
            title = "Invoice Created!",
            message = "Invoice $invoiceNumber was generated successfully.",
            primaryButtonText = "VIEW PREVIEW",
            onPrimaryClick = {
                showSuccess = false
                onNavigateToPreview(invoiceNumber)
            }
        )
        return
    }

    val clients = SampleData.clients
    var selectedClient by remember { mutableStateOf<Client?>(clients.firstOrNull()) }
    var clientDropdownOpen by remember { mutableStateOf(false) }

    val items = remember {
        mutableStateListOf(
            InvoiceItem("i1", "Website Design", 1, 5000.0, 18.0),
            InvoiceItem("i2", "Hosting (annual)", 1, 2000.0, 18.0)
        )
    }

    var discountStr by remember { mutableStateOf("0") }
    var notes by remember { mutableStateOf("Thank you for your business! Payment is due within 15 days.") }
    var terms by remember { mutableStateOf("Net 15 Days. Standard late payment fees apply.") }

    val subtotal = items.sumOf { it.subtotal }
    val discount = discountStr.toDoubleOrNull() ?: 0.0
    val taxAmount = items.sumOf { it.taxAmount }
    val grandTotal = (subtotal - discount) + taxAmount

    Scaffold(
        topBar = {
            WavesHeader(
                title = "New Invoice",
                onBackClick = onNavigateBack,
                actions = {
                    TextButton(
                        onClick = {
                            showDemoToast(context, "Draft saved!")
                            onNavigateToPreview(invoiceNumber)
                        },
                        modifier = Modifier.testTag("invoice_save_header_button")
                    ) {
                        Text("Save", color = OnPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
            )
        },
        containerColor = BackgroundColor,
        modifier = modifier.testTag("create_invoice_screen")
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Spacer(modifier = Modifier.height(6.dp))

            // Section CLIENT
            SectionHeader(title = "CLIENT DETAILS")
            WavesCard {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (selectedClient != null) "Bill To" else "Select Client",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextSecondary
                        )
                        Text(
                            text = "+ Add New Client",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldInk,
                            modifier = Modifier.clickable { onNavigateToAddClient() }
                        )
                    }

                    Box {
                        OutlinedTextField(
                            value = selectedClient?.let { "${it.name} (${it.city})" } ?: "Choose Client",
                            onValueChange = {},
                            readOnly = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .clickable { clientDropdownOpen = true },
                            shape = RoundedCornerShape(12.dp),
                            leadingIcon = {
                                Icon(Icons.Filled.Person, contentDescription = null, tint = EmeraldInk)
                            },
                            trailingIcon = {
                                IconButton(onClick = { clientDropdownOpen = true }) {
                                    Icon(Icons.Filled.ArrowDropDown, contentDescription = null)
                                }
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = SurfaceColor,
                                unfocusedContainerColor = SurfaceColor,
                                focusedBorderColor = AccentCyan,
                                unfocusedBorderColor = InputBorderGray
                            )
                        )

                        DropdownMenu(
                            expanded = clientDropdownOpen,
                            onDismissRequest = { clientDropdownOpen = false }
                        ) {
                            clients.forEach { client ->
                                DropdownMenuItem(
                                    text = { Text("${client.name} — ${client.city}") },
                                    onClick = {
                                        selectedClient = client
                                        clientDropdownOpen = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // Section DETAILS
            SectionHeader(title = "INVOICE DETAILS")
            WavesCard {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    WavesTextField(
                        value = invoiceNumber,
                        onValueChange = { invoiceNumber = it },
                        label = "Invoice Number"
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        WavesTextField(
                            value = issueDate,
                            onValueChange = { issueDate = it },
                            label = "Issue Date",
                            modifier = Modifier.weight(1f)
                        )
                        WavesTextField(
                            value = dueDate,
                            onValueChange = { dueDate = it },
                            label = "Due Date",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Section ITEMS
            SectionHeader(title = "ITEMS & SERVICES")
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items.forEachIndexed { index, item ->
                    WavesCard {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = item.name,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Row {
                                    IconButton(
                                        onClick = { showDemoToast(context, "Editing ${item.name}") },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.Edit,
                                            contentDescription = "Edit item",
                                            tint = TextSecondary,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                    IconButton(
                                        onClick = {
                                            if (items.size > 1) {
                                                items.removeAt(index)
                                            } else {
                                                showDemoToast(context, "Invoice must have at least one item")
                                            }
                                        },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.Delete,
                                            contentDescription = "Delete item",
                                            tint = DangerRed,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "${item.quantity} × ${SampleData.formatCurrency(item.unitPrice)} (Tax: ${item.taxRate.toInt()}%)",
                                    fontSize = 13.sp,
                                    color = TextSecondary
                                )
                                Text(
                                    text = SampleData.formatCurrency(item.total),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    WavesSecondaryButton(
                        text = "+ From Products",
                        onClick = {
                            val nextProduct = SampleData.products.getOrNull(items.size % SampleData.products.size)
                                ?: SampleData.products.first()
                            items.add(
                                InvoiceItem(
                                    id = "i_${System.currentTimeMillis()}",
                                    name = nextProduct.name,
                                    quantity = 1,
                                    unitPrice = nextProduct.unitPrice,
                                    taxRate = nextProduct.taxRate
                                )
                            )
                            showDemoToast(context, "Added ${nextProduct.name}")
                        },
                        modifier = Modifier.weight(1f)
                    )

                    WavesSecondaryButton(
                        text = "+ Custom Item",
                        onClick = {
                            items.add(
                                InvoiceItem(
                                    id = "i_${System.currentTimeMillis()}",
                                    name = "Consulting & Retainer",
                                    quantity = 1,
                                    unitPrice = 1500.0,
                                    taxRate = 18.0
                                )
                            )
                            showDemoToast(context, "Added custom item")
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Section SUMMARY (card)
            SectionHeader(title = "SUMMARY")
            WavesCard {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Subtotal", fontSize = 14.sp, color = TextSecondary)
                        Text(SampleData.formatCurrency(subtotal), fontSize = 14.sp, color = TextPrimary)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Discount (₹)", fontSize = 14.sp, color = TextSecondary)
                        Text(SampleData.formatCurrency(discount), fontSize = 14.sp, color = TextPrimary)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Tax (18%)", fontSize = 14.sp, color = TextSecondary)
                        Text(SampleData.formatCurrency(taxAmount), fontSize = 14.sp, color = TextPrimary)
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), color = BorderGray)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Grand Total", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Text(
                            SampleData.formatCurrency(grandTotal),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldInk
                        )
                    }
                }
            }

            // Section NOTES & TERMS
            SectionHeader(title = "NOTES & TERMS")
            WavesCard {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    WavesTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = "Notes for Client",
                        singleLine = false,
                        maxLines = 3
                    )
                    WavesTextField(
                        value = terms,
                        onValueChange = { terms = it },
                        label = "Payment Terms",
                        singleLine = false,
                        maxLines = 3
                    )
                }
            }

            // Full-width emerald "PREVIEW & SAVE" button
            WavesPrimaryButton(
                text = "PREVIEW & SAVE",
                onClick = {
                    isSaving = true
                    coroutineScope.launch {
                        delay(600)
                        isSaving = false
                        showSuccess = true
                    }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
