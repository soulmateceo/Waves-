package com.example.data

import android.content.ContentValues
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.util.Locale
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument

object DocumentExports {
    fun createInvoicePdf(
        context: Context,
        invoice: Invoice,
        business: BusinessProfile,
        client: Client?
    ): File = createPdf(
        context,
        "Invoice ${invoice.id}",
        buildList {
            add(business.name)
            addAll(listOf(business.addressLine1, business.addressLine2, "${business.city}, ${business.country}")
                .filter(String::isNotBlank))
            add("Email: ${business.email}    Phone: ${business.phone}")
            add("Tax number: ${business.taxLabel} ${business.taxNumber}")
            add("")
            add("INVOICE  #${invoice.id}")
            add("Issue date: ${invoice.issueDate}    Due date: ${invoice.dueDate}")
            add("")
            add("BILL TO")
            add(invoice.clientName)
            addAll(listOf(client?.email.orEmpty(), client?.address.orEmpty(), "${client?.city.orEmpty()}, ${client?.country.orEmpty()}")
                .filter(String::isNotBlank))
            add("")
            add("ITEMS")
            add("Description | Qty | Unit price | Tax | Total")
            invoice.items.forEach { item ->
                add("${item.name} | ${item.quantity} | ${money(item.unitPrice)} | ${item.taxRate}% | ${money(item.total)}")
            }
            add("")
            add("Subtotal: ${money(invoice.subtotal)}")
            add("Discount: ${money(invoice.discount)}")
            add("Tax: ${money(invoice.taxAmount)}")
            add("Total: ${money(invoice.grandTotal)}")
            add("Paid: ${money(invoice.paidAmount)}")
            add("Balance due: ${money(invoice.balanceDue)}")
            if (invoice.payments.isNotEmpty()) {
                add("")
                add("PAYMENTS")
                invoice.payments.forEach { payment ->
                    add("${payment.date} | ${payment.method} | ${money(payment.amount)} | ${payment.reference}")
                }
            }
            if (invoice.notes.isNotBlank()) {
                add("")
                add("Notes: ${invoice.notes}")
            }
            if (invoice.terms.isNotBlank()) add("Terms: ${invoice.terms}")
            if (business.bankName.isNotBlank()) {
                add("")
                add("PAYMENT DETAILS")
                add("${business.bankName} | ${business.accountHolder} | ${business.accountNumber} | ${business.ifscCode}")
                if (business.upiId.isNotBlank()) add("UPI: ${business.upiId}")
            }
        }
    )

    fun createReportPdf(context: Context, month: String, invoices: List<Invoice>): File {
        val monthInvoices = invoices.filter { ReportDateUtils.monthKey(it.issueDate) == month }
        val activeInvoices = monthInvoices.filterNot {
            it.status == InvoiceStatus.CANCELLED || it.status == InvoiceStatus.WRITTEN_OFF
        }
        val collected = invoices.sumOf { invoice ->
            invoice.payments.filter { ReportDateUtils.monthKey(it.date) == month }.sumOf { it.amount }
        }
        return createPdf(
            context,
            "Invoice report $month",
            buildList {
                add("Invoice report - ${ReportDateUtils.displayMonth(month) ?: month}")
                add("Invoiced: ${money(activeInvoices.sumOf { it.grandTotal })}")
                add("Collected this month: ${money(collected)}")
                add("Outstanding: ${money(activeInvoices.sumOf { it.balanceDue })}")
                add("Invoice count: ${activeInvoices.size}")
                add("")
                add("Invoice | Client | Issue date | Due date | Status | Total | Paid | Balance")
                activeInvoices.forEach { invoice ->
                    add("${invoice.id} | ${invoice.clientName} | ${invoice.issueDate} | ${invoice.dueDate} | ${invoice.status.label} | ${money(invoice.grandTotal)} | ${money(invoice.paidAmount)} | ${money(invoice.balanceDue)}")
                }
            }
        )
    }

    fun createReportCsv(context: Context, month: String, invoices: List<Invoice>): File {
        val monthInvoices = invoices.filter { ReportDateUtils.monthKey(it.issueDate) == month }
        val activeInvoices = monthInvoices.filterNot {
            it.status == InvoiceStatus.CANCELLED || it.status == InvoiceStatus.WRITTEN_OFF
        }
        val file = File.createTempFile("waves-report-", ".csv", context.cacheDir)
        file.writeText(
            buildString {
                appendLine(listOf("Invoice", "Client", "Issue date", "Due date", "Status", "Subtotal", "Discount", "Tax", "Total", "Paid", "Balance due").joinToString(",") { csv(it) })
                activeInvoices.forEach { invoice ->
                    appendLine(
                        listOf(
                            invoice.id,
                            invoice.clientName,
                            invoice.issueDate,
                            invoice.dueDate,
                            invoice.status.label,
                            money(invoice.subtotal),
                            money(invoice.discount),
                            money(invoice.taxAmount),
                            money(invoice.grandTotal),
                            money(invoice.paidAmount),
                            money(invoice.balanceDue)
                        ).joinToString(",") { csv(it) }
                    )
                }
                appendLine()
                appendLine("${csv("Report month")},${csv(ReportDateUtils.displayMonth(month) ?: month)}")
            },
            Charsets.UTF_8
        )
        return file
    }

    fun createClientStatementPdf(context: Context, client: Client, invoices: List<Invoice>): File =
        createPdf(
            context,
            "Statement ${client.name}",
            buildList {
                add("Account statement")
                add(client.name)
                addAll(listOf(client.email.orEmpty(), client.phone, client.address, "${client.city}, ${client.country}")
                    .filter(String::isNotBlank))
                add("")
                add("Invoice | Issue date | Due date | Status | Total | Paid | Balance")
                invoices.forEach { invoice ->
                    add("${invoice.id} | ${invoice.issueDate} | ${invoice.dueDate} | ${invoice.status.label} | ${money(invoice.grandTotal)} | ${money(invoice.paidAmount)} | ${money(invoice.balanceDue)}")
                }
                add("")
                add("Total billed: ${money(invoices.filterNot { it.status == InvoiceStatus.CANCELLED || it.status == InvoiceStatus.WRITTEN_OFF }.sumOf { it.grandTotal })}")
                add("Total paid: ${money(invoices.sumOf { it.paidAmount })}")
                add("Balance due: ${money(invoices.sumOf { it.balanceDue })}")
            }
        )

    fun saveToDownloads(context: Context, file: File, displayName: String, mimeType: String): String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val values = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, displayName)
                put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                put(MediaStore.MediaColumns.IS_PENDING, 1)
            }
            val uri = context.contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
                ?: error("Unable to create a Downloads file.")
            try {
                val output = context.contentResolver.openOutputStream(uri)
                    ?: error("Unable to open the Downloads file.")
                output.use { file.inputStream().use { input -> input.copyTo(output) } }
                context.contentResolver.update(
                    uri,
                    ContentValues().apply { put(MediaStore.MediaColumns.IS_PENDING, 0) },
                    null,
                    null
                )
                return "Downloads/$displayName"
            } catch (exception: Exception) {
                context.contentResolver.delete(uri, null, null)
                throw exception
            }
        }

        val directory = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
            ?: error("Downloads storage is unavailable.")
        if (!directory.exists() && !directory.mkdirs()) error("Unable to create the Downloads folder.")
        file.copyTo(File(directory, displayName), overwrite = true)
        return directory.resolve(displayName).absolutePath
    }

    fun share(context: Context, file: File, mimeType: String, chooserTitle: String) {
        val uri: Uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = mimeType
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, chooserTitle))
    }

    fun copyInvoiceNumber(context: Context, invoiceId: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(ClipData.newPlainText("Invoice number", invoiceId))
    }

    private fun createPdf(context: Context, title: String, lines: List<String>): File {
        val document = PdfDocument()
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            textSize = 11f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        }
        val titlePaint = Paint(paint).apply {
            textSize = 17f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
        var pageNumber = 0
        var page: PdfDocument.Page? = null
        var canvas: android.graphics.Canvas? = null
        var y = 0f
        fun newPage() {
            page?.let(document::finishPage)
            pageNumber++
            page = document.startPage(PdfDocument.PageInfo.Builder(595, 842, pageNumber).create())
            canvas = page!!.canvas
            canvas!!.drawText(title, 42f, 48f, titlePaint)
            y = 78f
        }
        newPage()
        lines.forEach { line ->
            if (y > 795f) newPage()
            val printable = line
            val maxWidth = 510f
            var remaining = printable
            while (remaining.isNotEmpty()) {
                val count = paint.breakText(remaining, true, maxWidth, null).coerceAtLeast(1)
                canvas!!.drawText(remaining.substring(0, count), 42f, y, paint)
                remaining = remaining.substring(count)
                y += 16f
                if (y > 795f && remaining.isNotEmpty()) newPage()
            }
            y += 4f
        }
        page?.let(document::finishPage)

        val file = File.createTempFile("waves-export-", ".pdf", context.cacheDir)
        try {
            FileOutputStream(file).use(document::writeTo)
        } catch (exception: Exception) {
            file.delete()
            throw exception
        } finally {
            document.close()
        }
        return file
    }

    private fun csv(value: String): String {
        val safeValue = if (value.firstOrNull()?.let { it in setOf('=', '+', '-', '@', '\t', '\r') } == true) {
            "'$value"
        } else {
            value
        }
        return "\"${safeValue.replace("\"", "\"\"")}\""
    }

    private fun money(value: Double): String = String.format(Locale.US, "%.2f", value)
}
