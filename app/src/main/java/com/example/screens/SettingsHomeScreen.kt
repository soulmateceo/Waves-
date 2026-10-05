package com.example.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.components.WavesBottomNav
import com.example.components.WavesCard
import com.example.components.WavesHeader
import com.example.components.WavesNavTab
import com.example.components.showDemoToast
import com.example.data.FirebaseAuthRepository
import com.example.data.authErrorMessage
import com.example.ui.theme.BackgroundColor
import com.example.ui.theme.BorderGray
import com.example.ui.theme.DangerRed
import com.example.ui.theme.EmeraldInk
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.launch

@Composable
fun SettingsHomeScreen(
    onNavigateToBusinessProfile: () -> Unit,
    onNavigateToTaxSettings: () -> Unit,
    onNavigateToBankSettings: () -> Unit,
    onNavigateToProducts: () -> Unit,
    onNavigateToReports: () -> Unit,
    onNavigateToLogIn: () -> Unit,
    onNavigateToTab: (WavesNavTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val accountEmail = FirebaseAuthRepository.currentUser?.email.orEmpty()

    Scaffold(
        topBar = {
            WavesHeader(title = "Settings")
        },
        bottomBar = {
            WavesBottomNav(
                currentTab = WavesNavTab.SETTINGS,
                onTabSelected = onNavigateToTab
            )
        },
        containerColor = BackgroundColor,
        modifier = modifier.testTag("settings_home_screen")
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

            // BUSINESS SECTION
            SectionHeader(title = "BUSINESS")
            WavesCard {
                Column {
                    SettingsRowItem(
                        icon = Icons.Filled.Business,
                        label = "Business Profile",
                        onClick = onNavigateToBusinessProfile
                    )
                    HorizontalDivider(color = BorderGray)
                    SettingsRowItem(
                        icon = Icons.Filled.Receipt,
                        label = "Tax Settings",
                        onClick = onNavigateToTaxSettings
                    )
                    HorizontalDivider(color = BorderGray)
                    SettingsRowItem(
                        icon = Icons.Filled.AccountBalance,
                        label = "Bank Account",
                        onClick = onNavigateToBankSettings
                    )
                    HorizontalDivider(color = BorderGray)
                    SettingsRowItem(
                        icon = Icons.Filled.Inventory2,
                        label = "Products & Services",
                        onClick = onNavigateToProducts
                    )
                }
            }

            // INVOICE DEFAULTS SECTION
            SectionHeader(title = "INVOICE DEFAULTS")
            WavesCard {
                Column {
                    SettingsRowItem(
                        icon = Icons.Filled.Tag,
                        label = "Prefix & Numbering",
                        subtitle = "INV- / 005",
                        onClick = onNavigateToBusinessProfile
                    )
                    HorizontalDivider(color = BorderGray)
                    SettingsRowItem(
                        icon = Icons.Filled.MonetizationOn,
                        label = "Currency",
                        subtitle = "INR (₹)",
                        onClick = onNavigateToBusinessProfile
                    )
                    HorizontalDivider(color = BorderGray)
                    SettingsRowItem(
                        icon = Icons.Filled.Schedule,
                        label = "Payment Terms",
                        subtitle = "Net 15 Days",
                        onClick = onNavigateToBusinessProfile
                    )
                    HorizontalDivider(color = BorderGray)
                    SettingsRowItem(
                        icon = Icons.Filled.Assessment,
                        label = "Reports & Analytics",
                        onClick = onNavigateToReports
                    )
                }
            }

            // ACCOUNT SECTION
            SectionHeader(title = "ACCOUNT")
            WavesCard {
                Column {
                    SettingsRowItem(
                        icon = Icons.Filled.Email,
                        label = "Email",
                        subtitle = accountEmail,
                        onClick = { showDemoToast(context, accountEmail) }
                    )
                    HorizontalDivider(color = BorderGray)
                    SettingsRowItem(
                        icon = Icons.Filled.Lock,
                        label = "Change Password",
                        onClick = {
                            if (accountEmail.isNotBlank()) {
                                coroutineScope.launch {
                                    try {
                                        FirebaseAuthRepository.sendPasswordResetEmail(accountEmail)
                                        showDemoToast(context, "If an account exists for this email, reset instructions are on their way.")
                                    } catch (exception: Exception) {
                                        showDemoToast(context, authErrorMessage(exception))
                                    }
                                }
                            }
                        }
                    )
                    HorizontalDivider(color = BorderGray)
                    SettingsRowItem(
                        icon = Icons.AutoMirrored.Filled.ExitToApp,
                        label = "Log Out",
                        iconTint = DangerRed,
                        labelColor = DangerRed,
                        onClick = onNavigateToLogIn
                    )
                }
            }

            // OTHER SECTION
            SectionHeader(title = "OTHER")
            WavesCard {
                Column {
                    SettingsRowItem(
                        icon = Icons.Filled.Star,
                        label = "Remove Ads (₹99)",
                        subtitle = "One-time purchase",
                        onClick = { showDemoToast(context, "Payment gateway demo — ₹99") }
                    )
                    HorizontalDivider(color = BorderGray)
                    SettingsRowItem(
                        icon = Icons.Filled.Policy,
                        label = "Privacy Policy",
                        onClick = { showDemoToast(context, "Privacy Policy: Waves adheres to standard data privacy rules.") }
                    )
                    HorizontalDivider(color = BorderGray)
                    SettingsRowItem(
                        icon = Icons.Filled.StarRate,
                        label = "Rate the App",
                        onClick = { showDemoToast(context, "Thank you for rating 5 stars!") }
                    )
                    HorizontalDivider(color = BorderGray)
                    SettingsRowItem(
                        icon = Icons.Filled.Info,
                        label = "Version",
                        subtitle = "1.0.0 (Production)",
                        showArrow = false,
                        onClick = {}
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun SettingsRowItem(
    icon: ImageVector,
    label: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    iconTint: androidx.compose.ui.graphics.Color = EmeraldInk,
    labelColor: androidx.compose.ui.graphics.Color = TextPrimary,
    showArrow: Boolean = true,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text(
                    text = label,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = labelColor
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
            }
        }

        if (showArrow) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = null,
                tint = TextSecondary.copy(alpha = 0.6f),
                modifier = Modifier.size(14.dp)
            )
        }
    }
}
