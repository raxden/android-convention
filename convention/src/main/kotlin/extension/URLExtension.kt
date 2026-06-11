package extension

import java.io.File
import java.net.HttpURLConnection
import java.net.URL

private const val BUFFER_SIZE_BYTES = 8 * 1024
private const val BYTES_PER_KB = 1024.0
private const val BYTES_PER_MB = BYTES_PER_KB * BYTES_PER_KB
private const val PROGRESS_STEP_PERCENT = 5
private const val MAX_PERCENT = 100

typealias ProgressListener = (percent: Int, downloadedMb: Double, totalMb: Double) -> Unit

fun URL.downloadTo(destination: File, onProgress: ProgressListener? = null) {
    val connection = openConnection() as HttpURLConnection
    connection.instanceFollowRedirects = true
    connection.connect()
    val totalBytes = connection.contentLengthLong
    var downloadedBytes = 0L
    val buffer = ByteArray(BUFFER_SIZE_BYTES)
    var lastPrintedPercent = -1

    try {
        connection.inputStream.use { input ->
            destination.outputStream().use { output ->
                while (true) {
                    val read = input.read(buffer)
                    if (read == -1) break
                    output.write(buffer, 0, read)
                    downloadedBytes += read
                    if (totalBytes > 0 && onProgress != null) {
                        val percent = (downloadedBytes * MAX_PERCENT / totalBytes).toInt()
                        if (percent != lastPrintedPercent && percent % PROGRESS_STEP_PERCENT == 0) {
                            lastPrintedPercent = percent
                            val mb = downloadedBytes / BYTES_PER_MB
                            val totalMb = totalBytes / BYTES_PER_MB
                            onProgress(percent, mb, totalMb)
                        }
                    }
                }
            }
        }
        if (totalBytes > 0 && onProgress != null) {
            val totalMb = totalBytes / BYTES_PER_MB
            onProgress(MAX_PERCENT, totalMb, totalMb)
        }
    } finally {
        connection.disconnect()
    }
}
