package com.example.screens

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.components.showDemoToast
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.BackgroundColor
import com.example.ui.theme.ButtonTextStyle
import com.example.ui.theme.DangerRed
import com.example.ui.theme.EmeraldInk
import com.example.ui.theme.OnPrimary
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.SurfaceColor
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val BorderDefault = Color(0xFF9CA3AF)
private val BorderFocused = Color(0xFF00D4B8)
private val BorderFilled = Color(0xFF064E3B)
private val BorderError = Color(0xFFDC2626)

@Composable
fun OtpBox(
    digit: String,
    isFocused: Boolean,
    isFilled: Boolean,
    isError: Boolean = false,
    modifier: Modifier = Modifier
) {
    val borderColor = when {
        isError -> BorderError
        isFocused -> BorderFocused
        isFilled -> BorderFilled
        else -> BorderDefault
    }
    val borderWidth = if (isFocused || isError) 2.dp else 1.5.dp

    Box(
        modifier = modifier
            .height(52.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White)
            .border(borderWidth, borderColor, RoundedCornerShape(10.dp))
            .testTag("otp_box"),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = digit,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun OtpVerificationScreen(
    email: String,
    mode: String = "signup",
    onNavigateBack: () -> Unit,
    onVerificationSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current

    var otpText by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }

    // 60-second countdown timer for resend
    var secondsLeft by remember { mutableIntStateOf(60) }
    var isTimerRunning by remember { mutableStateOf(true) }

    // Loading & feedback states
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }

    val decodedEmail = remember(email) {
        try {
            Uri.decode(email)
        } catch (_: Exception) {
            email
        }
    }

    // Auto-focus on screen load
    LaunchedEffect(Unit) {
        delay(300)
        focusRequester.requestFocus()
        keyboardController?.show()
    }

    // 60-second Countdown Timer
    LaunchedEffect(isTimerRunning) {
        if (isTimerRunning) {
            while (secondsLeft > 0) {
                delay(1000)
                secondsLeft--
            }
            isTimerRunning = false
        }
    }

    fun verifyOtp() {
        if (otpText.length != 6) return
        coroutineScope.launch {
            errorMessage = null
            successMessage = null
            isLoading = true
            keyboardController?.hide()
            delay(1200) // Realistic verification latency
            isLoading = false

            // For prototype: any 6 digit code works; show error demo if 000000
            if (otpText == "000000") {
                errorMessage = "Invalid verification code. Please check and try again."
            } else {
                successMessage = "Email verified successfully!"
                showDemoToast(context, "Verification complete! Welcome to WAVES")
                delay(600)
                onVerificationSuccess()
            }
        }
    }

    // Auto-trigger when 6 digits filled
    LaunchedEffect(otpText) {
        if (otpText.length == 6 && !isLoading && successMessage == null) {
            verifyOtp()
        }
    }

    Scaffold(
        topBar = {
            // 1. EMERALD HEADER BAR
            // Height 56dp, Background #064E3B, Left: white back arrow icon, Center/Right: blank
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag("otp_header"),
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
                        modifier = Modifier.testTag("otp_back_button")
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
        modifier = modifier.testTag("otp_verification_screen")
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // a) Lock icon
                Spacer(modifier = Modifier.height(48.dp))
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Security Lock",
                    tint = AccentCyan,
                    modifier = Modifier
                        .size(64.dp)
                        .testTag("otp_lock_icon")
                )

                // b) Title
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Verify Your Email",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF000000),
                    textAlign = TextAlign.Center
                )

                // c) Subtitle
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "We sent a 6-digit code to $decodedEmail",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF000000),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                // Status Message Feedback Banner
                AnimatedVisibility(
                    visible = errorMessage != null || successMessage != null,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Column {
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    if (errorMessage != null) Color(0xFFFEE2E2) else Color(0xFFDCFCE7)
                                )
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (errorMessage != null) Icons.Filled.Error else Icons.Filled.CheckCircle,
                                contentDescription = null,
                                tint = if (errorMessage != null) DangerRed else SuccessGreen,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = errorMessage ?: successMessage ?: "",
                                fontSize = 13.sp,
                                color = if (errorMessage != null) DangerRed else SuccessGreen,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                // d) OTP INPUT — SIX BOXES IN ONE SINGLE ROW
                Spacer(modifier = Modifier.height(40.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            focusRequester.requestFocus()
                            keyboardController?.show()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    // Invisible BasicTextField capturing input & paste
                    BasicTextField(
                        value = otpText,
                        onValueChange = { input ->
                            // Allow only digits 0-9 and max length 6
                            val filtered = input.filter { it.isDigit() }.take(6)
                            otpText = filtered
                            errorMessage = null
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.NumberPassword
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                if (otpText.length == 6) verifyOtp()
                            }
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester)
                            .testTag("otp_hidden_input"),
                        decorationBox = {
                            // Render the 6 individual boxes in a single row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                for (i in 0 until 6) {
                                    val digit = otpText.getOrNull(i)?.toString() ?: ""
                                    val isFocused = otpText.length == i || (i == 5 && otpText.length == 6)
                                    val isFilled = digit.isNotEmpty()

                                    OtpBox(
                                        digit = digit,
                                        isFocused = isFocused && !isLoading,
                                        isFilled = isFilled,
                                        isError = errorMessage != null,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }
                    )
                }

                // e) Resend row
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Didn't receive? ",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF000000)
                    )
                    Text(
                        text = "Resend",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (secondsLeft == 0) EmeraldInk else Color(0xFF000000),
                        modifier = Modifier
                            .testTag("otp_resend_button")
                            .clickable(enabled = secondsLeft == 0 && !isLoading) {
                                secondsLeft = 60
                                isTimerRunning = true
                                otpText = ""
                                errorMessage = null
                                successMessage = "A fresh code was sent to $decodedEmail"
                                showDemoToast(context, "New code sent: 482910")
                            }
                    )
                }

                if (secondsLeft > 0) {
                    Spacer(modifier = Modifier.height(4.dp))
                    val minutes = secondsLeft / 60
                    val seconds = secondsLeft % 60
                    Text(
                        text = String.format("in %02d:%02d", minutes, seconds),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF000000)
                    )
                }
            }

            // f) Bottom actions (Spacer weight 1f pushes to bottom)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp)
            ) {
                // 3. VERIFY BUTTON
                val isButtonEnabled = otpText.length == 6 && !isLoading
                Button(
                    onClick = { verifyOtp() },
                    enabled = isButtonEnabled,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .testTag("otp_verify_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EmeraldInk,
                        contentColor = OnPrimary,
                        disabledContainerColor = EmeraldInk.copy(alpha = 0.4f),
                        disabledContentColor = OnPrimary.copy(alpha = 0.6f)
                    )
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = OnPrimary,
                            strokeWidth = 2.5.dp,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Text(
                            text = "VERIFY",
                            style = ButtonTextStyle,
                            color = OnPrimary
                        )
                    }
                }

                // 4. "Change email?" link
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Change email?",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF000000),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .clickable { onNavigateBack() }
                        .padding(8.dp)
                        .testTag("otp_change_email_link")
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
