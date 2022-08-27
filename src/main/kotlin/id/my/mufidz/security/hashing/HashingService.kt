package id.my.mufidz.security.hashing

import id.my.mufidz.model.SaltedHash
import org.apache.commons.codec.binary.Hex
import org.apache.commons.codec.digest.DigestUtils
import java.security.SecureRandom

interface HashingService {
    fun generatePasswordHashed(value: String) : SaltedHash
    fun verify(value: String, saltedHash: SaltedHash) : Boolean
}

class HashingServiceImpl : HashingService {

    override fun generatePasswordHashed(value: String): SaltedHash {
        val salt = SecureRandom.getInstance("SHA1PRNG").generateSeed(32)
        val saltAsHex = Hex.encodeHexString(salt)
        val hash = DigestUtils.sha256Hex("$saltAsHex$value")
        return SaltedHash(hash, saltAsHex)
    }

    override fun verify(value: String, saltedHash: SaltedHash): Boolean =
        DigestUtils.sha256Hex("${saltedHash.salt}$value") == saltedHash.hash
}