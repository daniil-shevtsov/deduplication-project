package com.daniilshevtsov.deduplication.feature.indextable.domain

import com.daniilshevtsov.deduplication.feature.core.Chunk
import java.security.MessageDigest
import javax.inject.Inject

/* https://www.samclarke.com/kotlin-hash-strings/ */
class CalculateHashForChunkUseCase @Inject constructor() {

    operator fun invoke(chunk: Chunk): String {
        return sha256(input = chunk.value.toByteArray().toString(Charsets.UTF_8))
    }

    private fun sha512(input: String) = hashString(HashAlgorithm.SHA_512, input)

    private fun sha256(input: String) = hashString(HashAlgorithm.SHA_256, input)

    private fun sha1(input: String) = hashString(HashAlgorithm.SHA_1, input)

    private fun hashString(type: HashAlgorithm, input: String): String {
        val bytes = MessageDigest
            .getInstance(type.code)
            .digest(input.toByteArray())
        val result = StringBuilder(bytes.size * 2)

        bytes.forEach {
            val i = it.toInt()
            result.append(HEX_CHARS[i shr 4 and 0x0f])
            result.append(HEX_CHARS[i and 0x0f])
        }

        return result.toString()
    }

    enum class HashAlgorithm(val code: String) {
        SHA_512("SHA-512"),
        SHA_256("SHA-256"),
        SHA_1("SHA-1")
    }

    private companion object {
        const val HEX_CHARS = "0123456789ABCDEF"
    }

}