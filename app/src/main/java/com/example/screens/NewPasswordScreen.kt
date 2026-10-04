package com.example.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.components.WavesTextField
import com.example.components.showDemoToast
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.BackgroundColor
import com.example.ui.theme.ButtonTextStyle
import com.example.ui.theme.EmeraldInk
import com.example.ui.theme.OnPrimary
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun NewPasswordScreen(
    email: String,
    onNavigateBack: () -> Unit,
    onNavigateToLogIn: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isNewPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }

    val isMinLengthMet = newPassword.length >= 6
    val doPasswordsMatch = newPassword.isNotEmpty() && newPassword == confirmPassword
    val isFormValid = isMinLengthMet && doPasswordsMatch

    val newPasswordError = when {
        newPassword.isEmpty() -> null
        newPassword.length < 6 -> "Password must be at least 6 characters"
        else -> null
    }

    val confirmPasswordError = when {
        confirmPassword.isEmpty() -> null
        confirmPassword != newPassword -> "Passwords do not match"
        else -> null
    }

    Scaffold(
        topBar = {
            // Emerald header: ← Back | no title
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag("new_password_header"),
                color = EmeraldInk
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.testTag("new_password_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = OnPrimary
                        )
                    }
                }
            }
        },
        containerColor = BackgroundColor,
        modifier = modifier.testTag("new_password_screen")
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. Lock icon — Cyan #00D4B8, size 64dp, top 48dp
            Spacer(modifier = Modifier.height(48.dp))
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "Lock Icon",
                tint = AccentCyan,
                modifier = Modifier
                    .size(64.dp)
                    .testTag("new_password_icon")
            )

            // 2. Title "Set New Password" — 24sp Bold #0A2540, top 24dp
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Set New Password",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                textAlign = TextAlign.Center
            )

            // 3. Subtitle "Your new password must be at least 6 characters" — 14sp #6B7280, centered, top 8dp
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Your new password must be at least 6 characters",
                fontSize = 14.sp,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            // 4. New Password field (56dp, 12dp radius, 👁 toggle) — top 40dp
            Spacer(modifier = Modifier.height(40.dp))
            WavesTextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                label = "New Password",
                placeholder = "Min 6 characters",
                leadingIcon = Icons.Filled.Lock,
                visualTransformation = if (isNewPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                errorMessage = newPasswordError,
                trailingIcon = {
                    IconButton(onClick = { isNewPasswordVisible = !isNewPasswordVisible }) {
                        Icon(
                            imageVector = if (isNewPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = if (isNewPasswordVisible) "Hide password" else "Show password",
                            tint = AccentCyan
                        )
                    }
                }
            )

            // 5. Confirm Password field (56dp, 12dp radius, 👁 toggle) — top 16dp
            Spacer(modifier = Modifier.height(16.dp))
            WavesTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = "Confirm Password",
                placeholder = "Re-enter new password",
                leadingIcon = Icons.Filled.Lock,
                visualTransformation = if (isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                errorMessage = confirmPasswordError,
                trailingIcon = {
                    IconButton(onClick = { isConfirmPasswordVisible = !isConfirmPasswordVisible }) {
                        Icon(
                            imageVector = if (isConfirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = if (isConfirmPasswordVisible) "Hide password" else "Show password",
                            tint = AccentCyan
                        )
                    }
                }
            )

            // 6. Emerald "RESET PASSWORD" button (56dp, full width, 12dp radius) — top 32dp
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = {
                    if (isFormValid) {
                        showDemoToast(context, "Password reset (demo)")
                        onNavigateToLogIn()
                    }
                },
                enabled = isFormValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag("reset_password_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = EmeraldInk,
                    contentColor = OnPrimary,
                    disabledContainerColor = EmeraldInk.copy(alpha = 0.4f),
                    disabledContentColor = OnPrimary.copy(alpha = 0.6f)
                )
            ) {
                Text(
                    text = "RESET PASSWORD",
                    style = ButtonTextStyle,
                    color = OnPrimary
                )
            }

            // 7. Text link at bottom: "Back to Log In" — top 16dp
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Back to Log In",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = AccentCyan,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .clickable { onNavigateToLogIn() }
                    .padding(8.dp)
                    .testTag("back_to_login_link")
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
