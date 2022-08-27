package id.my.mufidz.security.token

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import id.my.mufidz.model.TokenClaim
import id.my.mufidz.model.TokenConfig
import java.util.*

interface TokenService {
    fun generate(config: TokenConfig, vararg claims: TokenClaim) : String
}

class TokenServiceImpl : TokenService {

    override fun generate(config: TokenConfig, vararg claims: TokenClaim): String {
        val calendar = Calendar.getInstance().also {
            it.time = Date()
            it.add(Calendar.WEEK_OF_YEAR, 2)
        }
        var token = JWT.create()
            .withAudience(config.audience)
            .withIssuer(config.issuer)
            .withExpiresAt(calendar.time)

        claims.forEach {
            token = token.withClaim(it.name, it.value)
        }
        return token.sign(Algorithm.HMAC256(config.secret))
    }

}