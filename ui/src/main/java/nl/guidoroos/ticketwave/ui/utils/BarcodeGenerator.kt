package nl.guidoroos.ticketwave.ui.utils

import android.graphics.Bitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import androidx.core.graphics.createBitmap
import androidx.core.graphics.set

fun generateBarcodeBitmap(barcodeText: String, width: Int = 600, height: Int = 200): Bitmap? {
    try {
        val bitMatrix: BitMatrix = MultiFormatWriter().encode(
            barcodeText,
            BarcodeFormat.CODE_128,
            width,
            height
        )
        val bmp = createBitmap(width, height, Bitmap.Config.RGB_565)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bmp[x, y] =
                    if (bitMatrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE
            }
        }
        return bmp
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
}
