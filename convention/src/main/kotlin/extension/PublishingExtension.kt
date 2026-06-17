package extension

import org.gradle.api.Project
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

private const val GIT_SHORT_SHA_LENGTH = 7
private const val TIMESTAMP_PATTERN = "yyyyMMdd.HHmmss"

/**
 * Builds a unique publication name combining a UTC timestamp with the current
 * git short SHA, e.g. `20260617.095700+a1b2c3d`.
 *
 * If git is unavailable the SHA is omitted and only the timestamp is returned.
 */
fun Project.getPublicationName(): String {
    val gitShortSha = runCatching {
        val process = ProcessBuilder("git", "rev-parse", "--short=$GIT_SHORT_SHA_LENGTH", "HEAD")
            .directory(rootDir)
            .redirectErrorStream(true)
            .start()
        val output = process.inputStream.bufferedReader().readText().trim()
        if (process.waitFor() == 0 && output.isNotBlank()) output else null
    }.getOrNull()

    val utcStamp = ZonedDateTime.now(ZoneOffset.UTC)
        .format(DateTimeFormatter.ofPattern(TIMESTAMP_PATTERN))

    return listOfNotNull(utcStamp, gitShortSha).joinToString("+")
}
