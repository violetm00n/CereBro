package com.example.dyslexiaanalyzer

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import java.io.File
import java.io.OutputStream

fun generatePDF(context: Context, userInput: String, result: String) {
    try {
        val fileName = "Dyslexia_Report.pdf"
        val outputStream: OutputStream?

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Android 10+ (Scoped Storage) - Save using MediaStore
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }

            val resolver = context.contentResolver
            val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

            outputStream = uri?.let { resolver.openOutputStream(it) }

        } else {
            // Android 9 and below - Save using FileOutputStream
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val filePath = File(downloadsDir, fileName)
            outputStream = filePath.outputStream()
        }

        outputStream?.use { os ->
            val writer = PdfWriter(os)
            val pdfDoc = PdfDocument(writer)
            val document = Document(pdfDoc)

            document.add(Paragraph("Dyslexia Analysis Report"))
            document.add(Paragraph("User Input: $userInput"))
            document.add(Paragraph("Analysis Result: $result"))
            document.close()
        }

        Toast.makeText(context, "PDF saved in Downloads", Toast.LENGTH_LONG).show()

    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "Failed to generate PDF", Toast.LENGTH_SHORT).show()
    }
}