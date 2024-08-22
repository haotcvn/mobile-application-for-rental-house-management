package com.tranconghaoit.homemanager.utils


import android.content.Context
import android.os.Bundle
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.print.PageRange
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintDocumentInfo
import android.print.PrintManager
import com.tranconghaoit.homemanager.R
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class Printer(private val context: Context) {

    fun printPdf(pdfFile: File) {
        val printManager = context.getSystemService(Context.PRINT_SERVICE) as PrintManager

        val jobName = context.getString(R.string.app_name) + " Document"

        val printAttributes = PrintAttributes.Builder()
            .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
            .setColorMode(PrintAttributes.COLOR_MODE_MONOCHROME)
            .build()

        val printDocumentAdapter = object : PrintDocumentAdapter() {
            override fun onLayout(
                oldAttributes: PrintAttributes?,
                newAttributes: PrintAttributes,
                cancellationSignal: android.os.CancellationSignal?,
                callback: LayoutResultCallback,
                extras: Bundle?
            ) {
                if (cancellationSignal?.isCanceled == true) {
                    callback.onLayoutCancelled()
                    return
                }

                val builder = PrintDocumentInfo.Builder("bills.pdf")
                builder.setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                builder.setPageCount(PrintDocumentInfo.PAGE_COUNT_UNKNOWN)
                val info = builder.build()

                callback.onLayoutFinished(info, true)
            }

            override fun onWrite(
                pages: Array<PageRange>?,
                destination: ParcelFileDescriptor?,
                cancellationSignal: CancellationSignal?,
                callback: WriteResultCallback?
            ) {
                try {
                    val input = FileInputStream(pdfFile)
                    val output = FileOutputStream(destination?.fileDescriptor)

                    val buf = ByteArray(16384)
                    var bytesRead: Int
                    while (input.read(buf).also { bytesRead = it } > 0) {
                        output.write(buf, 0, bytesRead)
                    }

                    callback?.onWriteFinished(arrayOf(PageRange.ALL_PAGES))
                } catch (e: Exception) {
                    callback?.onWriteFailed(e.message)
                }
            }
        }

        printManager.print(
            jobName,
            printDocumentAdapter,
            PrintAttributes.Builder().build()
        )
    }
}
