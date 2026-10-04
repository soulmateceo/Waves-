package com.example.screens

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Info
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.components.WavesCard
import com.example.components.WavesHeader
import com.example.components.WavesPrimaryButton
import com.example.components.WavesTextField
import com.example.components.showDemoToast
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.BackgroundColor
import com.example.ui.theme.EmeraldInk
import com.example.ui.theme.InputBorderGray
import com.example.ui.theme.OnPrimary
import com.example.ui.theme.SurfaceColor
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun TaxSettingsScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val countries = listOf(
        "India", "USA", "UK", "UAE", "Australia",
        "Canada", "Germany", "Singapore", "Nigeria", "Kenya",
        "South Africa", "Brazil"
    )

    var selectedCountry by remember { mutableStateOf("India") }
    var countryDropdownOpen by remember { mutableStateOf(false) }

    var taxLabel by remember { mutableStateOf("GSTIN") }
    var taxNumber by remember { mutableStateOf("27AAAAA0000A1Z5") }
    var defaultTaxRate by remember { mutableStateOf("18") }
    var pricesIncludeTax by remember { mutableStateOf(false) }

    fun updateDefaultsForCountry(country: String) {
        selectedCountry = country
        when (country) {
            "India" -> {
                taxLabel = "GSTIN"
                defaultTaxRate = "18"
            }
            "USA" -> {
                taxLabel = "Sales Tax / EIN"
                defaultTaxRate = "8.25"
            }
            "UK" -> {
                taxLabel = "VAT Number"
                defaultTaxRate = "20"
            }
            "UAE" -> {
                taxLabel = "TRN (VAT)"
                defaultTaxRate = "5"
            }
            "Australia" -> {
                taxLabel = "ABN / GST"
                defaultTaxRate = "10"
            }
            "Canada" -> {
                taxLabel = "GST / HST"
                defaultTaxRate = "13"
            }
            "Germany" -> {
                taxLabel = "MwSt / USt-IdNr"
                defaultTaxRate = "19"
            }
            "Singapore" -> {
                taxLabel = "GST Number"
                defaultTaxRate = "9"
            }
            else -> {
                taxLabel = "Tax / VAT"
                defaultTaxRate = "15"
            }
        }
    }

    Scaffold(
        topBar = {
            WavesHeader(
                title = "Tax Settings",
                onBackClick = onNavigateBack,
                actions = {
                    TextButton(
                        onClick = {
                            showDemoToast(context, "Tax settings updated!")
                            onNavigateBack()
                        },
                        modifier = Modifier.testTag("tax_settings_save_button")
                    ) {
                        Text("Save", color = OnPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
            )
        },
        containerColor = BackgroundColor,
        modifier = modifier.testTag("tax_settings_screen")
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

            // Info card: "Country selection auto-fills tax label, rate, and currency. You can override any value."
            WavesCard {
                Row(verticalAlignment = Alignment.Top) {
                    Icon(
                        imageVector = Icons.Filled.Info,
                        contentDescription = null,
                        tint = EmeraldInk,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Country selection auto-fills tax label, rate, and currency. You can override any value.",
                        fontSize = 13.sp,
                        color = TextSecondary,
                        lineHeight = 18.sp
                    )
                }
            }

            // Section TAX CONFIG
            SectionHeader(title = "TAX CONFIGURATION")
            WavesCard {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Box {
                        Column {
                            Text(
                                text = "Country",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = TextSecondary,
                                modifier = Modifier.padding(bottom = 6.dp)
                            )
                            OutlinedTextField(
                                value = selectedCountry,
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
                                            updateDefaultsForCountry(item)
                                            countryDropdownOpen = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    WavesTextField(
                        value = taxLabel,
                        onValueChange = { taxLabel = it },
                        label = "Tax Label"
                    )

                    WavesTextField(
                        value = taxNumber,
                        onValueChange = { taxNumber = it },
                        label = "Tax Number / ID"
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
                            colors = CheckboxDefaults.colors(
                                checkedColor = EmeraldInk,
                                checkmarkColor = AccentCyan
                            )
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Prices include tax",
                            fontSize = 14.sp,
                            color = TextPrimary
                        )
                    }
                }
            }

            WavesPrimaryButton(
                text = "SAVE TAX SETTINGS",
                onClick = {
                    showDemoToast(context, "Tax settings saved successfully!")
                    onNavigateBack()
                }
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
