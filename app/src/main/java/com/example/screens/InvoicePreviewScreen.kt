package com.example.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.components.WavesHeader
import com.example.components.WavesPrimaryButton
import com.example.components.WavesSecondaryButton
import com.example.components.showDemoToast
import com.example.data.SampleData
import com.example.ui.theme.BackgroundColor
import com.example.ui.theme.BorderGray
import com.example.ui.theme.EmeraldInk
import com.example.ui.theme.OnPrimary
import com.example.ui.theme.SurfaceColor
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun InvoicePreviewScreen(
    invoiceId: String,
    onNavigateBack: () -> Unit,
    onNavigateToInvoiceDetail: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val invoice = SampleData.invoices.find { it.id == invoiceId }
        ?: SampleData.invoices.first()
    val business = SampleData.defaultBusiness
    val client = SampleData.clients.find { it.id == invoice.clientId }

    Scaffold(
        topBar = {
            WavesHeader(
                title = "Preview",
                onBackClick = onNavigateBack,
                actions = {
                    IconButton(
                        onClick = { showDemoToast(context, "Sharing PDF...") },
                        modifier = Modifier.testTag("preview_share_header_button")
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Share,
                            contentDescription = "Share",
                            tint = OnPrimary
                        )
                    }
                }
            )
        },
        containerColor = BackgroundColor,
        modifier = modifier.testTag("invoice_preview_screen")
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(6.dp))

            // ===== PDF MOCKUP CARD =====
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 6.dp,
                        shape = RoundedCornerShape(8.dp),
                        spotColor = Color(0x33000000)
                    )
                    .border(
                        BorderStroke(1.dp, BorderGray),
                        RoundedCornerShape(8.dp)
                    ),
                shape = RoundedCornerShape(8.dp),
                color = SurfaceColor
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    // ===== HEADER =====
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFE6F4EA)),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.app_logo),
                                    contentDescription = "Logo",
                                    modifier = Modifier.size(34.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    business.name,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = EmeraldInk
                                )
                                Text(
                                    business.addressLine1,
                                    fontSize = 11.sp,
                                    color = TextSecondary
                                )
                                if (business.addressLine2.isNotBlank()) {
                                    Text(
                                        business.addressLine2,
                                        fontSize = 11.sp,
                                        color = TextSecondary
                                    )
                                }
                                Text(
                                    "${business.city}, ${business.country}",
                                    fontSize = 11.sp,
                                    color = TextSecondary
                                )
                                Text(
                                    business.phone,
                                    fontSize = 11.sp,
                                    color = TextSecondary
                                )
                            }
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                "INVOICE",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldInk
                            )
                            Text(
                                "#${invoice.id}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                "Date: ${invoice.issueDate}",
                                fontSize = 10.sp,
                                color = TextSecondary
                            )
                            Text(
                                "Due: ${invoice.dueDate}",
                                fontSize = 10.sp,
                                color = TextSecondary
                            )
                        }
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 12.dp),
                        color = BorderGray
                    )

                    // ===== BILL TO =====
                    Text(
                        "BILL TO:",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextSecondary
                    )
                    Text(
                        invoice.clientName,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        invoice.clientEmail ?: "Email not provided",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                    if (client != null && client.city.isNotBlank()) {
                        Text(
                            "${client.city}${if (client.country.isNotBlank()) ", ${client.country}" else ""}",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // ===== ITEMS TABLE HEADER =====
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color(0xFFF1F5F9),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "ITEM",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                modifier = Modifier.weight(2f)
                            )
                            Text(
                                "QTY",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                modifier = Modifier.weight(0.7f),
                                textAlign = TextAlign.Center
                            )
                            Text(
                                "RATE",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.End
                            )
                            Text(
                                "AMOUNT",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                modifier = Modifier.weight(1.2f),
                                textAlign = TextAlign.End
                            )
                        }
                    }

                    // ===== ITEMS ROWS =====
                    invoice.items.forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(2f)) {
                                Text(
                                    item.name,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = TextPrimary
                                )
                                Text(
                                    "Tax: ${item.taxRate.toInt()}%",
                                    fontSize = 10.sp,
                                    color = TextSecondary
                                )
                            }
                            Text(
                                "${item.quantity.toInt()}",
                                fontSize = 12.sp,
                                color = TextPrimary,
                                modifier = Modifier.weight(0.7f),
                                textAlign = TextAlign.Center
                            )
                            Text(
                                SampleData.formatCurrency(item.unitPrice),
                                fontSize = 12.sp,
                                color = TextPrimary,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.End
                            )
                            Text(
                                SampleData.formatCurrency(item.total),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary,
                                modifier = Modifier.weight(1.2f),
                                textAlign = TextAlign.End
                            )
                        }
                        HorizontalDivider(color = Color(0xFFF1F5F9))
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // ===== TOTALS =====
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.End
                    ) {
                        Row(
                            modifier = Modifier.width(200.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Subtotal:", fontSize = 12.sp, color = TextSecondary)
                            Text(
                                SampleData.formatCurrency(invoice.subtotal),
                                fontSize = 12.sp,
                                color = TextPrimary
                            )
                        }
                        Row(
                            modifier = Modifier.width(200.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Tax:", fontSize = 12.sp, color = TextSecondary)
                            Text(
                                SampleData.formatCurrency(invoice.taxAmount),
                                fontSize = 12.sp,
                                color = TextPrimary
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.width(200.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "TOTAL:",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                SampleData.formatCurrency(invoice.grandTotal),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldInk
                            )
                        }
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 12.dp),
                        color = BorderGray
                    )

                    // ===== BANK DETAILS =====
                    Text(
                        "PAYMENT DETAILS",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextSecondary
                    )
                    Text(
                        "Bank: ${business.bankName} · A/C: ${business.accountNumber}",
                        fontSize = 11.sp,
                        color = TextPrimary
                    )
                    Text(
                        "IFSC: ${business.ifscCode} · UPI: ${business.upiId}",
                        fontSize = 11.sp,
                        color = TextPrimary
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // ===== FOOTER =====
                    Text(
                        text = "Made with WAVES — Free Invoice & Accounting",
                        fontSize = 11.sp,
                        color = TextSecondary.copy(alpha = 0.5f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // ===== ACTION BUTTONS =====
            WavesPrimaryButton(
                text = "DOWNLOAD PDF",
                icon = Icons.Filled.Download,
                onClick = {
                    showDemoToast(context, "PDF saved to Downloads folder!")
                }
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                WavesSecondaryButton(
                    text = "SHARE",
                    icon = Icons.Filled.Share,
                    onClick = { showDemoToast(context, "Sharing PDF with client...") },
                    modifier = Modifier.weight(1f)
                )

                WavesSecondaryButton(
                    text = "COPY LINK",
                    icon = Icons.Filled.Link,
                    onClick = { showDemoToast(context, "Link copied to clipboard!") },
                    modifier = Modifier.weight(1f)
                )
            }

            WavesSecondaryButton(
                text = "Watch ad to remove watermark",
                icon = Icons.Filled.PlayCircle,
                onClick = {
                    showDemoToast(context, "Ad completed! Watermark removed from PDF")
                }
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}