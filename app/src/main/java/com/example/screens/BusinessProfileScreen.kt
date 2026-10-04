package com.example.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.components.WavesCard
import com.example.components.WavesHeader
import com.example.components.WavesPrimaryButton
import com.example.components.WavesTextField
import com.example.components.showDemoToast
import com.example.data.SampleData
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.BackgroundColor
import com.example.ui.theme.EmeraldInk
import com.example.ui.theme.InputBorderGray
import com.example.ui.theme.OnPrimary
import com.example.ui.theme.SurfaceColor
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun BusinessProfileScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val initial = SampleData.defaultBusiness

    var businessName by remember { mutableStateOf(initial.name) }
    var tagline by remember { mutableStateOf(initial.tagline) }
    var email by remember { mutableStateOf(initial.email) }
    var phone by remember { mutableStateOf(initial.phone) }
    var website by remember { mutableStateOf(initial.website) }

    var addressLine1 by remember { mutableStateOf(initial.addressLine1) }
    var addressLine2 by remember { mutableStateOf(initial.addressLine2) }
    var city by remember { mutableStateOf(initial.city) }
    var state by remember { mutableStateOf(initial.state) }
    var postalCode by remember { mutableStateOf(initial.postalCode) }

    var country by remember { mutableStateOf(initial.country) }
    var countryDropdownOpen by remember { mutableStateOf(false) }
    val countries = listOf("India", "USA", "UK", "UAE", "Australia", "Canada", "Germany", "Singapore", "Nigeria", "Kenya", "South Africa", "Brazil")

    var taxLabel by remember { mutableStateOf(initial.taxLabel) }
    var taxDropdownOpen by remember { mutableStateOf(false) }
    val taxLabels = listOf("Tax", "VAT", "Sales Tax", "GSTIN", "Custom")

    var taxNumber by remember { mutableStateOf(initial.taxNumber) }
    var defaultTaxRate by remember { mutableStateOf(initial.defaultTaxRate.toString()) }
    var pricesIncludeTax by remember { mutableStateOf(initial.pricesIncludeTax) }

    var bankName by remember { mutableStateOf(initial.bankName) }
    var accountHolder by remember { mutableStateOf(initial.accountHolder) }
    var accountNumber by remember { mutableStateOf(initial.accountNumber) }
    var ifscCode by remember { mutableStateOf(initial.ifscCode) }
    var branch by remember { mutableStateOf(initial.branch) }
    var upiId by remember { mutableStateOf(initial.upiId) }

    var prefix by remember { mutableStateOf(initial.prefix) }
    var nextNumber by remember { mutableStateOf(initial.nextNumber) }
    var currency by remember { mutableStateOf(initial.currency) }
    var currencyDropdownOpen by remember { mutableStateOf(false) }
    val currencies = listOf("INR (₹)", "USD ($)", "EUR (€)", "GBP (£)", "AED (د.إ)", "AUD ($)", "CAD ($)", "SGD ($)")

    var paymentTerms by remember { mutableStateOf(initial.paymentTerms) }
    var defaultNotes by remember { mutableStateOf(initial.defaultNotes) }

    Scaffold(
        topBar = {
            WavesHeader(
                title = "Business Setup",
                onBackClick = onNavigateBack,
                actions = {
                    TextButton(
                        onClick = {
                            showDemoToast(context, "Business profile saved!")
                            onNavigateBack()
                        },
                        modifier = Modifier.testTag("business_save_header_button")
                    ) {
                        Text("Save", color = OnPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
            )
        },
        containerColor = BackgroundColor,
        modifier = modifier.testTag("business_profile_screen")
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Section LOGO: circle 100dp placeholder + "Tap to upload"
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE6F4EA))
                        .clickable { showDemoToast(context, "Tap to upload logo") },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.app_logo),
                        contentDescription = "Business Logo",
                        modifier = Modifier.size(72.dp)
                    )
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(EmeraldInk.copy(alpha = 0.85f))
                            .align(Alignment.BottomEnd),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CameraAlt,
                            contentDescription = "Upload",
                            tint = OnPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Tap to upload business logo",
                    fontSize = 13.sp,
                    color = EmeraldInk,
                    fontWeight = FontWeight.Medium
                )
            }

            // Section BUSINESS INFO
            SectionHeader(title = "BUSINESS INFO")
            WavesCard {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    WavesTextField(
                        value = businessName,
                        onValueChange = { businessName = it },
                        label = "Business Name *"
                    )
                    WavesTextField(
                        value = tagline,
                        onValueChange = { tagline = it },
                        label = "Tagline"
                    )
                }
            }

            // Section CONTACT
            SectionHeader(title = "CONTACT")
            WavesCard {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    WavesTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = "Email *",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                    )
                    WavesTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = "Mobile Number *",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                    )
                    WavesTextField(
                        value = website,
                        onValueChange = { website = it },
                        label = "Website"
                    )
                }
            }

            // Section ADDRESS
            SectionHeader(title = "ADDRESS")
            WavesCard {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    WavesTextField(
                        value = addressLine1,
                        onValueChange = { addressLine1 = it },
                        label = "Address Line 1 *"
                    )
                    WavesTextField(
                        value = addressLine2,
                        onValueChange = { addressLine2 = it },
                        label = "Address Line 2"
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        WavesTextField(
                            value = city,
                            onValueChange = { city = it },
                            label = "City *",
                            modifier = Modifier.weight(1f)
                        )
                        WavesTextField(
                            value = state,
                            onValueChange = { state = it },
                            label = "State",
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        WavesTextField(
                            value = postalCode,
                            onValueChange = { postalCode = it },
                            label = "Postal Code *",
                            modifier = Modifier.weight(1f)
                        )
                        Box(modifier = Modifier.weight(1f)) {
                            Column {
                                Text(
                                    text = "Country",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = TextSecondary,
                                    modifier = Modifier.padding(bottom = 6.dp)
                                )
                                OutlinedTextField(
                                    value = country,
                                    onValueChange = {},
                                    readOnly = true,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(56.dp)
                                        .clickable { countryDropdownOpen = true },
                                    shape = RoundedCornerShape(12.dp),
                                    trailingIcon = {
                                        IconButton(onClick = { countryDropdownOpen = true }) {
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
                                    expanded = countryDropdownOpen,
                                    onDismissRequest = { countryDropdownOpen = false }
                                ) {
                                    countries.forEach { item ->
                                        DropdownMenuItem(
                                            text = { Text(item) },
                                            onClick = {
                                                country = item
                                                countryDropdownOpen = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Section TAX
            SectionHeader(title = "TAX")
            WavesCard {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Box {
                        Column {
                            Text(
                                text = "Tax Label",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = TextSecondary,
                                modifier = Modifier.padding(bottom = 6.dp)
                            )
                            OutlinedTextField(
                                value = taxLabel,
                                onValueChange = {},
                                readOnly = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp)
                                    .clickable { taxDropdownOpen = true },
                                shape = RoundedCornerShape(12.dp),
                                trailingIcon = {
                                    IconButton(onClick = { taxDropdownOpen = true }) {
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
                                expanded = taxDropdownOpen,
                                onDismissRequest = { taxDropdownOpen = false }
                            ) {
                                taxLabels.forEach { item ->
                                    DropdownMenuItem(
                                        text = { Text(item) },
                                        onClick = {
                                            taxLabel = item
                                            taxDropdownOpen = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    WavesTextField(
                        value = taxNumber,
                        onValueChange = { taxNumber = it },
                        label = "Tax Number / GSTIN"
                    )

                    WavesTextField(
                        value = defaultTaxRate,
                        onValueChange = { defaultTaxRate = it },
                        label = "Default Tax Rate %",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { pricesIncludeTax = !pricesIncludeTax }
                    ) {
                        Checkbox(
                            checked = pricesIncludeTax,
                            onCheckedChange = { pricesIncludeTax = it },
                            colors = CheckboxDefaults.colors(checkedColor = EmeraldInk, checkmarkColor = AccentCyan)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Prices include tax", fontSize = 14.sp, color = TextPrimary)
                    }
                }
            }

            // Section BANK ACCOUNT
            SectionHeader(title = "BANK ACCOUNT")
            WavesCard {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    WavesTextField(
                        value = bankName,
                        onValueChange = { bankName = it },
                        label = "Bank Name"
                    )
                    WavesTextField(
                        value = accountHolder,
                        onValueChange = { accountHolder = it },
                        label = "Account Holder Name"
                    )
                    WavesTextField(
                        value = accountNumber,
                        onValueChange = { accountNumber = it },
                        label = "Account Number"
                    )
                    WavesTextField(
                        value = ifscCode,
                        onValueChange = { ifscCode = it },
                        label = "IFSC / SWIFT / IBAN"
                    )
                    WavesTextField(
                        value = branch,
                        onValueChange = { branch = it },
                        label = "Branch"
                    )
                    WavesTextField(
                        value = upiId,
                        onValueChange = { upiId = it },
                        label = "UPI ID"
                    )
                }
            }

            // Section INVOICE DEFAULTS
            SectionHeader(title = "INVOICE DEFAULTS")
            WavesCard {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        WavesTextField(
                            value = prefix,
                            onValueChange = { prefix = it },
                            label = "Prefix",
                            modifier = Modifier.weight(1f)
                        )
                        WavesTextField(
                            value = nextNumber,
                            onValueChange = { nextNumber = it },
                            label = "Next Number",
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Box {
                        Column {
                            Text(
                                text = "Currency",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = TextSecondary,
                                modifier = Modifier.padding(bottom = 6.dp)
                            )
                            OutlinedTextField(
                                value = currency,
                                onValueChange = {},
                                readOnly = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp)
                                    .clickable { currencyDropdownOpen = true },
                                shape = RoundedCornerShape(12.dp),
                                trailingIcon = {
                                    IconButton(onClick = { currencyDropdownOpen = true }) {
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
                                expanded = currencyDropdownOpen,
                                onDismissRequest = { currencyDropdownOpen = false }
                            ) {
                                currencies.forEach { item ->
                                    DropdownMenuItem(
                                        text = { Text(item) },
                                        onClick = {
                                            currency = item
                                            currencyDropdownOpen = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    WavesTextField(
                        value = paymentTerms,
                        onValueChange = { paymentTerms = it },
                        label = "Payment Terms"
                    )

                    WavesTextField(
                        value = defaultNotes,
                        onValueChange = { defaultNotes = it },
                        label = "Default Notes",
                        singleLine = false,
                        maxLines = 3
                    )
                }
            }

            // Full-width emerald "SAVE CHANGES" button at bottom
            WavesPrimaryButton(
                text = "SAVE CHANGES",
                onClick = {
                    showDemoToast(context, "All changes saved successfully!")
                    onNavigateBack()
                }
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = EmeraldInk,
        modifier = Modifier.padding(top = 4.dp)
    )
}
