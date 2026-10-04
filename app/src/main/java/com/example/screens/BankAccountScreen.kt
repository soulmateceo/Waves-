package com.example.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
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
import com.example.data.SampleData
import com.example.ui.theme.BackgroundColor
import com.example.ui.theme.EmeraldInk
import com.example.ui.theme.OnPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun BankAccountScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val initial = SampleData.defaultBusiness

    var bankName by remember { mutableStateOf(initial.bankName) }
    var accountHolder by remember { mutableStateOf(initial.accountHolder) }
    var accountNumber by remember { mutableStateOf(initial.accountNumber) }
    var ifscCode by remember { mutableStateOf(initial.ifscCode) }
    var branch by remember { mutableStateOf(initial.branch) }
    var upiId by remember { mutableStateOf(initial.upiId) }

    Scaffold(
        topBar = {
            WavesHeader(
                title = "Bank Account",
                onBackClick = onNavigateBack,
                actions = {
                    TextButton(
                        onClick = {
                            showDemoToast(context, "Bank account details saved!")
                            onNavigateBack()
                        },
                        modifier = Modifier.testTag("bank_settings_save_button")
                    ) {
                        Text("Save", color = OnPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
            )
        },
        containerColor = BackgroundColor,
        modifier = modifier.testTag("bank_account_screen")
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

            // Info note: "These details appear on every invoice PDF."
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
                        text = "These details appear on every invoice PDF to make it easy for your clients to pay directly via Bank Transfer or UPI.",
                        fontSize = 13.sp,
                        color = TextSecondary,
                        lineHeight = 18.sp
                    )
                }
            }

            SectionHeader(title = "BANK ACCOUNT DETAILS")
            WavesCard {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    WavesTextField(
                        value = bankName,
                        onValueChange = { bankName = it },
                        label = "Bank Name",
                        placeholder = "e.g. HDFC Bank",
                        leadingIcon = Icons.Filled.AccountBalance
                    )

                    WavesTextField(
                        value = accountHolder,
                        onValueChange = { accountHolder = it },
                        label = "Account Holder Name",
                        placeholder = "e.g. Waves Studio Pvt Ltd"
                    )

                    WavesTextField(
                        value = accountNumber,
                        onValueChange = { accountNumber = it },
                        label = "Account Number",
                        placeholder = "e.g. 50200012345678",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )

                    WavesTextField(
                        value = ifscCode,
                        onValueChange = { ifscCode = it },
                        label = "IFSC / SWIFT / IBAN",
                        placeholder = "e.g. HDFC0001234"
                    )

                    WavesTextField(
                        value = branch,
                        onValueChange = { branch = it },
                        label = "Branch Name",
                        placeholder = "e.g. Bandra West, Mumbai"
                    )

                    WavesTextField(
                        value = upiId,
                        onValueChange = { upiId = it },
                        label = "UPI ID / VPA",
                        placeholder = "e.g. company@okhdfcbank"
                    )
                }
            }

            WavesPrimaryButton(
                text = "SAVE BANK DETAILS",
                onClick = {
                    showDemoToast(context, "Bank details saved successfully!")
                    onNavigateBack()
                }
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
