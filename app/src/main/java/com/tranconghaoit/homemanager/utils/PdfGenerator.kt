package com.tranconghaoit.homemanager.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Environment
import com.itextpdf.io.font.PdfEncodings
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.kernel.colors.DeviceRgb
import com.itextpdf.kernel.font.PdfFontFactory
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.borders.Border
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Image
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.property.TextAlignment
import com.tranconghaoit.homemanager.R
import com.tranconghaoit.homemanager.models.BillModel
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.DecimalFormat
import kotlin.jvm.Throws

class PdfGenerator(private val context: Context) {

    @SuppressLint("UseCompatLoadingForDrawables")
    fun createPdf(bill: BillModel): File {

        val issueDate = DateFormatUtils.convertDateFormat(bill.issueDate)
        val fromDate = DateFormatUtils.convertDateFormat(bill.fromDate)
        val toDate = DateFormatUtils.convertDateFormat(bill.toDate)
        val decimalFormat = DecimalFormat("###,###,###")

        val fontPath = "assets/fonts/arial.ttf"
        val customFont = PdfFontFactory.createFont(fontPath, PdfEncodings.IDENTITY_H, true)

        val pdfPath = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        val file = File(pdfPath, "myPDF.pdf")
        val outputStream = FileOutputStream(file)
        val writer = PdfWriter(file)
        val pdfDocument = com.itextpdf.kernel.pdf.PdfDocument(writer)
        val document = Document(pdfDocument)
        document.setFont(customFont)

        val colorGray = DeviceRgb(220, 220, 220)

        val columnWidth = floatArrayOf(140f, 140f, 140f, 140f)
        val table1 = Table(columnWidth)

        val d1 = context.getDrawable(R.drawable.ic_building_96)
        val bitmap1 = (d1 as BitmapDrawable).bitmap
        val stream1 = ByteArrayOutputStream()
        bitmap1.compress(Bitmap.CompressFormat.PNG, 100, stream1)
        val bitmapData1 = stream1.toByteArray()
        val imageData1 = ImageDataFactory.create(bitmapData1)
        val image1 = Image(imageData1)
        image1.setWidth(100f)

        // Table1 ----- 01
        table1.addCell(Cell(3, 1).add(image1).setBorder(Border.NO_BORDER))
        table1.addCell(Cell().add(Paragraph("")).setBorder(Border.NO_BORDER))
        table1.addCell(
            Cell(1, 2).add(Paragraph("HOÁ ĐƠN TIỀN NHÀ").setBold().setFontSize(14f))
                .setBorder(Border.NO_BORDER)
        )

        // Table1 ----- 02
        table1.addCell(Cell().add(Paragraph("")).setBorder(Border.NO_BORDER))
        table1.addCell(Cell(1, 2).add(Paragraph("Mã hoá đơn: #${bill.billID}")).setBorder(Border.NO_BORDER))


        // Table1 ----- 03
        table1.addCell(Cell().add(Paragraph("")).setBorder(Border.NO_BORDER))
        table1.addCell(
            Cell(1, 2).add(Paragraph("Ngày lập hoá đơn: $issueDate").setFont(customFont))
                .setBorder(Border.NO_BORDER)
        )

        // Table1 ----- 06
        table1.addCell(Cell().add(Paragraph("").setBold()).setBorder(Border.NO_BORDER))
        table1.addCell(Cell().add(Paragraph("")).setBorder(Border.NO_BORDER))
        table1.addCell(Cell().add(Paragraph("").setBold()).setBorder(Border.NO_BORDER))
        table1.addCell(Cell().add(Paragraph("")).setBorder(Border.NO_BORDER))

        // Table1 ----- 07
        table1.addCell(
            Cell(1, 2).add(Paragraph("Trần Công Hào").setBold()).setBorder(Border.NO_BORDER)
        )
        //table1.addCell(Cell().add(Paragraph("")))
        table1.addCell(Cell(1, 2).add(Paragraph("Khách hàng: ${bill.renterName}")).setBorder(Border.NO_BORDER))
        //table1.addCell(Cell().add(Paragraph("")))

        // Table1 ----- 08
        table1.addCell(Cell(1, 2).add(Paragraph("Địa chỉ: Sóc Trăng")).setBorder(Border.NO_BORDER))
        //table1.addCell(Cell().add(Paragraph("")))
        table1.addCell(Cell(1, 2).add(Paragraph("Điện thoại: ${bill.renterPhone}")).setBorder(Border.NO_BORDER))
        //table1.addCell(Cell().add(Paragraph("")))

        // Table1 ----- 09
        table1.addCell(Cell(1, 2).add(Paragraph("Điện thoại: 0377723874")).setBorder(Border.NO_BORDER))
        //table1.addCell(Cell().add(Paragraph("")))
        table1.addCell(Cell(1, 2).add(Paragraph("Phòng: ${bill.roomName}")).setBorder(Border.NO_BORDER))
        //table1.addCell(Cell().add(Paragraph("")))

        val columnWidth2 = floatArrayOf(162f, 62f, 84f, 84f, 84f, 84f)
        val table2 = Table(columnWidth2)

        // Table2 ----- 01
        table2.addCell(
            Cell().add(Paragraph("Dịch vụ").setBold()).setBackgroundColor(colorGray)
                .setTextAlignment(TextAlignment.CENTER)
        )
        table2.addCell(
            Cell().add(Paragraph("Số lượng").setBold()).setBackgroundColor(colorGray)
                .setTextAlignment(TextAlignment.CENTER)
        )
        table2.addCell(
            Cell().add(Paragraph("Đơn vị").setBold()).setBackgroundColor(colorGray)
                .setTextAlignment(TextAlignment.CENTER)
        )
        table2.addCell(
            Cell().add(Paragraph("Đơn giá").setBold()).setBackgroundColor(colorGray)
                .setTextAlignment(TextAlignment.CENTER)
        )
        table2.addCell(
            Cell().add(Paragraph("Thành tiền").setBold()).setBackgroundColor(colorGray)
                .setTextAlignment(TextAlignment.CENTER)
        )
        table2.addCell(
            Cell().add(Paragraph("Ghi chú").setBold()).setBackgroundColor(colorGray)
                .setTextAlignment(TextAlignment.CENTER)
        )

        // Table2 ----- 02
        table2.addCell(Cell().add(Paragraph("Tiền thuê phòng")))
        table2.addCell(Cell().add(Paragraph("1")).setTextAlignment(TextAlignment.CENTER))
        table2.addCell(Cell().add(Paragraph("Phòng")).setTextAlignment(TextAlignment.CENTER))
        table2.addCell(Cell().add(Paragraph("${decimalFormat.format(bill.roomPrice)} ")).setTextAlignment(TextAlignment.CENTER))
        table2.addCell(Cell().add(Paragraph("${decimalFormat.format(bill.roomPrice)} ")).setTextAlignment(TextAlignment.CENTER))
        table2.addCell(Cell().add(Paragraph("")))

        // Table2 ----- 02
        table2.addCell(Cell().add(Paragraph("Tiền dịch vụ")))
        table2.addCell(Cell().add(Paragraph("1")).setTextAlignment(TextAlignment.CENTER))
        table2.addCell(Cell().add(Paragraph("")).setTextAlignment(TextAlignment.CENTER))
        table2.addCell(Cell().add(Paragraph("${decimalFormat.format(bill.servicePrice)} ")).setTextAlignment(TextAlignment.CENTER))
        table2.addCell(Cell().add(Paragraph("${decimalFormat.format(bill.servicePrice)} ")).setTextAlignment(TextAlignment.CENTER))
        table2.addCell(Cell().add(Paragraph("")))

        val columnWidth3 = floatArrayOf(162f, 62f, 84f, 84f, 84f, 84f)
        val table3 = Table(columnWidth3)

        // Table3 ----- 01
        table3.addCell(Cell(1, 4).add(Paragraph("Tổng tiền").setBold()).setBorder(Border.NO_BORDER))
        table3.addCell(Cell().add(Paragraph("${decimalFormat.format(bill.total)} ")).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER))
        table3.addCell(Cell().add(Paragraph("")).setBorder(Border.NO_BORDER))

        // Table3 ----- 02
        table3.addCell(
            Cell(1, 4).add(Paragraph("Trả thêm/phạt").setBold()).setBorder(Border.NO_BORDER)
        )
        table3.addCell(Cell().add(Paragraph("0")).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER))
        table3.addCell(Cell().add(Paragraph("")).setBorder(Border.NO_BORDER))

        // Table3 ----- 03
        table3.addCell(Cell(1, 4).add(Paragraph("Đã trả").setBold()).setBorder(Border.NO_BORDER))
        table3.addCell(Cell().add(Paragraph("0")).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER))
        table3.addCell(Cell().add(Paragraph("")).setBorder(Border.NO_BORDER))

        // Table3 ----- 04
        table3.addCell(Cell(1, 4).add(Paragraph("Còn lại").setBold()).setBorder(Border.NO_BORDER))
        table3.addCell(Cell().add(Paragraph("${decimalFormat.format(bill.total)} ")).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER))
        table3.addCell(Cell().add(Paragraph("")).setBorder(Border.NO_BORDER))

        // Table3 ----- 05
        table3.addCell(
            Cell(1, 6).add(Paragraph("Bằng chữ: ${NumberToWords.convertNumberToWords(bill.total)}").setBold()).setBorder(Border.NO_BORDER)
        )

        // Table3 ----- 06
        table3.addCell(Cell(1, 6).add(Paragraph("Lưu ý").setItalic()).setBorder(Border.NO_BORDER))

        // Table3 ----- 07
        table3.addCell(
            Cell(
                1,
                6
            ).add(Paragraph("Ngày thanh toán từ ngày $issueDate đến ngày $issueDate").setItalic())
                .setBorder(Border.NO_BORDER)
        )

        // Table3 ----- 08
        table3.addCell(
            Cell(1, 6).add(Paragraph("Ngày   , tháng   , năm 2023").setItalic())
                .setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER)
        )

        // Table3 ----- 09
        table3.addCell(
            Cell().add(Paragraph("Khách hàng").setItalic()).setTextAlignment(TextAlignment.CENTER)
                .setBorder(Border.NO_BORDER)
        )
        table3.addCell(Cell().add(Paragraph("")).setBorder(Border.NO_BORDER))
        table3.addCell(Cell().add(Paragraph("")).setBorder(Border.NO_BORDER))
        table3.addCell(Cell().add(Paragraph("")).setBorder(Border.NO_BORDER))
        table3.addCell(
            Cell(1, 2).add(Paragraph("Người lập phiếu").setItalic())
                .setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER)
        )

        document.add(table1)
        document.add(Paragraph("\n"))
        document.add(table2)
        document.add(Paragraph("\n"))
        document.add(table3)

        document.close()

        return file
    }

    data class InvoiceInfo(
        val date: String,
        val invoiceId: String,
        val customerName: String,
        val address: String,
        val phone: String,
        val room: String,
        val items: List<InvoiceItem>,
        val total: String
    )

    data class InvoiceItem(
        val name: String,
        val unitPrice: String,
        val quantity: String,
        val total: String
    )

}