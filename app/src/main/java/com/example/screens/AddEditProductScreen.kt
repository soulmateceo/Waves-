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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Inventory2
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
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
import com.example.data.SampleData
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.BackgroundColor
import com.example.ui.theme.InputBorderGray
import com.example.ui.theme.OnPrimary
import com.example.ui.theme.SurfaceColor
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AddEditProductScreen(
    productId: String?,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val isEditMode = productId != null && productId != "new"
    val existingProduct = SampleData.products.find { it.id == productId }

    var isSaving by remember { mutableStateOf(false) }
    var showSuccess by remember { mutableStateOf(false) }
    var hasSubmitted by remember { mutableStateOf(false) }

    if (isSaving) {
        StateScreen(
            type = StateType.LOADING,
            message = "Saving product details..."
        )
        return
    }

    if (showSuccess) {
        StateScreen(
            type = StateType.SUCCESS,
            title = "Product Saved!",
            message = "Product details have been recorded successfully.",
            primaryButtonText = "DONE",
            onPrimaryClick = onNavigateBack
        )
        return
    }

    var name by remember { mutableStateOf(existingProduct?.name ?: "") }
    var description by remember { mutableStateOf(existingProduct?.description ?: "") }
    var sku by remember { mutableStateOf(existingProduct?.sku ?: "") }

    var unitPrice by remember { mutableStateOf(existingProduct?.unitPrice?.toString() ?: "") }
    var unit by remember { mutableStateOf(existingProduct?.unit ?: "pcs") }
    var unitDropdownOpen by remember { mutableStateOf(false) }
    val units = listOf("pcs", "hrs", "kg", "yr", "mo", "day")

    var defaultQuantity by remember { mutableStateOf(existingProduct?.defaultQuantity?.toString() ?: "1") }
    var taxRate by remember { mutableStateOf(existingProduct?.taxRate?.toString() ?: "18") }

    val screenTitle = if (isEditMode) "Edit Product" else "Add Product"

    Scaffold(
        topBar = {
            WavesHeader(
                title = screenTitle,
                onBackClick = onNavigateBack,
                actions = {
                    TextButton(
                        onClick = {
                            showDemoToast(context, if (isEditMode) "Product updated!" else "Product added!")
                            onNavigateBack()
                        },
                        modifier = Modifier.testTag("save_product_header_button")
                    ) {
                        Text("Save", color = OnPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
            )
        },
        containerColor = BackgroundColor,
        modifier = modifier.testTag("add_edit_product_screen")
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

            // Section BASIC: Name*, Description, SKU
            SectionHeader(title = "BASIC INFORMATION")
            WavesCard {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    val nameError = if (hasSubmitted && name.isBlank()) "Product name is required" else null
                    WavesTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = "Product / Service Name *",
                        placeholder = "e.g. Website Design",
                        leadingIcon = Icons.Filled.Inventory2,
                        errorMessage = nameError
                    )
                    WavesTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = "Description",
                        placeholder = "Brief explanation of deliverables",
                        singleLine = false,
                        maxLines = 3
                    )
                    WavesTextField(
                        value = sku,
                        onValueChange = { sku = it },
                        label = "Item Code / SKU",
                        placeholder = "e.g. SRV-001"
                    )
                }
            }

            // Section PRICING: Unit Price*, Unit dropdown, Default Quantity
            SectionHeader(title = "PRICING")
            WavesCard {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    WavesTextField(
                        value = unitPrice,
                        onValueChange = { unitPrice = it },
                        label = "Unit Price (₹) *",
                        placeholder = "0.00",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Box(modifier = Modifier.weight(1f)) {
                            Column {
                                Text(
                                    text = "Unit",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = TextSecondary,
                                    modifier = Modifier.padding(bottom = 6.dp)
                                )
                                OutlinedTextField(
                                    value = unit,
                                    onValueChange = {},
                                    readOnly = true,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(56.dp)
                                        .clickable { unitDropdownOpen = true },
                                    shape = RoundedCornerShape(12.dp),
                                    trailingIcon = {
                                        IconButton(onClick = { unitDropdownOpen = true }) {
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
                                    expanded = unitDropdownOpen,
                                    onDismissRequest = { unitDropdownOpen = false }
                                ) {
                                    units.forEach { item ->
                                        DropdownMenuItem(
                                            text = { Text(item) },
                                            onClick = {
                                                unit = item
                                                unitDropdownOpen = false
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        WavesTextField(
                            value = defaultQuantity,
                            onValueChange = { defaultQuantity = it },
                            label = "Default Quantity",
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Section TAX: Tax Rate % (blank = use default)
            SectionHeader(title = "TAX")
            WavesCard {
                WavesTextField(
                    value = taxRate,
                    onValueChange = { taxRate = it },
                    label = "Tax Rate % (blank = use default)",
                    placeholder = "18",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

            // Emerald "SAVE PRODUCT" button
            WavesPrimaryButton(
                text = "SAVE PRODUCT",
                onClick = {
                    hasSubmitted = true
                    if (name.isNotBlank()) {
                        isSaving = true
                        coroutineScope.launch {
                            delay(600)
                            isSaving = false
                            showSuccess = true
                        }
                    }
                }
            )

            // Outlined "ARCHIVE PRODUCT" button
            WavesSecondaryButton(
                text = "ARCHIVE PRODUCT",
                icon = Icons.Filled.Archive,
                onClick = {
                    showDemoToast(context, "Product archived (demo)")
                    onNavigateBack()
                }
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
