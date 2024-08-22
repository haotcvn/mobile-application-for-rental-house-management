package com.tranconghaoit.homemanager.utils

object NumberToWords {
    private val numberWords = arrayOf(
        "", "một", "hai", "ba", "bốn", "năm", "sáu", "bảy", "tám", "chín"
    )

    private val unitWords = arrayOf(
        "", "nghìn", "triệu", "tỷ"
    )

    fun convertNumberToWords(number: Int): String {
        if (number == 0) {
            return "không"
        }

        var result = ""
        var index = 0

        var tempNumber = number
        while (tempNumber > 0) {
            val segment = tempNumber % 1000
            if (segment != 0) {
                val segmentWords = convertSegmentToWords(segment)
                result = segmentWords + unitWords[index] + " " + result
            }
            index++
            tempNumber /= 1000
        }

        return result.trim()
    }

    private fun convertSegmentToWords(segment: Int): String {
        var result = ""
        val hundreds = (segment / 100)
        val tens = (segment % 100 / 10)
        val ones = (segment % 10)

        if (hundreds > 0) {
            result += numberWords[hundreds] + " trăm "
        }

        if (tens > 1) {
            result += numberWords[tens] + " mươi "
            if (ones > 0) {
                result += numberWords[ones] + " "
            }
        } else if (tens == 1) {
            result += "mười "
            if (ones > 0) {
                result += numberWords[ones] + " "
            }
        } else if (ones > 0) {
            result += numberWords[ones] + " "
        }

        return result
    }
}