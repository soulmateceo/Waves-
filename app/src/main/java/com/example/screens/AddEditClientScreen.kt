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
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
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

@Composable
fun AddEditClientScreen(
    clientId: String?,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isEditMode = clientId != null && clientId != "new"
    val existingClient = SampleData.clients.find { it.id == clientId }

    var name by remember { mutableStateOf(existingClient?.name ?: "") }
    var email by remember { mutableStateOf(existingClient?.email ?: "") }
    var phone by remember { mutableStateOf(existingClient?.phone ?: "") }
    var addressLine1 by remember { mutableStateOf(existingClient?.address ?: "") }
    var city by remember { mutableStateOf(existingClient?.city ?: "") }
    var state by remember { mutableStateOf(existingClient?.state ?: "") }
    var postalCode by remember { mutableStateOf(existingClient?.postalCode ?: "") }
    var country by remember { mutableStateOf(existingClient?.country ?: "India") }
    var countryDropdownOpen by remember { mutableStateOf(false) }
    val countries = listOf("India", "USA", "UK", "UAE", "Australia", "Canada", "Singapore")

    var taxNumber by remember { mutableStateOf(existingClient?.taxNumber ?: "") }
    var notes by remember { mutableStateOf(existingClient?.notes ?: "") }

    val screenTitle = if (isEditMode) "Edit Client" else "Add Client"

    Scaffold(
        topBar = {
            WavesHeader(
                title = screenTitle,
                onBackClick = onNavigateBack,
                actions = {
                    TextButton(
                        onClick = {
                            showDemoToast(context, if (isEditMode) "Client updated!" else "Client added!")
                            onNavigateBack()
                        },
                        modifier = Modifier.testTag("save_client_header_button")
                    ) {
                        Text("Save", color = OnPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
            )
        },
        containerColor = BackgroundColor,
        modifier = modifier.testTag("add_edit_client_screen")
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

            // Section BASIC
            SectionHeader(title = "BASIC INFORMATION")
            WavesCard {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    WavesTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = "Client Name *",
                        placeholder = "e.g. Rahul Sharma",
                        leadingIcon = Icons.Filled.Person
                    )
                    WavesTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = "Email Address",
                        placeholder = "rahul@email.com",
                        leadingIcon = Icons.Filled.Email,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                    )
                    WavesTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = "Phone Number",
                        placeholder = "+91 98765 43210",
                        leadingIcon = Icons.Filled.Phone,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                    )
                }
            }

            // Section ADDRESS
            SectionHeader(title = "BILLING ADDRESS")
            WavesCard {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    WavesTextField(
                        value = addressLine1,
                        onValueChange = { addressLine1 = it },
                        label = "Address Line 1",
                        placeholder = "123 Commercial St"
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        WavesTextField(
                            value = city,
                            onValueChange = { city = it },
                            label = "City",
                            placeholder = "Mumbai",
                            modifier = Modifier.weight(1f)
                        )
                        WavesTextField(
                            value = state,
                            onValueChange = { state = it },
                            label = "State",
                            placeholder = "Maharashtra",
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        WavesTextField(
                            value = postalCode,
                            onValueChange = { postalCode = it },
                            label = "Postal Code",
                            placeholder = "400001",
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
            SectionHeader(title = "TAX DETAILS")
            WavesCard {
                WavesTextField(
                    value = taxNumber,
                    onValueChange = { taxNumber = it },
                    label = "Tax / GSTIN Number",
                    placeholder = "27AAAAA0000A1Z5"
                )
            }

            // Section NOTES
            SectionHeader(title = "NOTES")
            WavesCard {
                WavesTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = "Client Notes",
                    placeholder = "Special billing instructions, payment terms, or client preferences",
                    singleLine = false,
                    maxLines = 4
                )
            }

            // Emerald "SAVE CLIENT" button
            WavesPrimaryButton(
                text = "SAVE CLIENT",
                onClick = {
                    showDemoToast(context, "Client saved successfully!")
                    onNavigateBack()
                }
            )

            // Red outlined "DELETE CLIENT" button (edit mode only)
            if (isEditMode) {
                WavesSecondaryButton(
                    text = "DELETE CLIENT",
                    icon = Icons.Filled.Delete,
                    isDestructive = true,
                    onClick = {
                        showDemoToast(context, "Client deleted (demo)")
                        onNavigateBack()
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
