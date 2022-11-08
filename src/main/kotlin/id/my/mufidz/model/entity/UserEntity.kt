package id.my.mufidz.model.entity

import id.my.mufidz.model.Register
import id.my.mufidz.model.User
import id.my.mufidz.model.table.UserTable
import id.my.mufidz.utils.localeDateNow
import io.ktor.util.*
import kotlinx.datetime.Clock
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID

class UserEntity(id: EntityID<String>) : Entity<String>(id) {
    companion object : EntityClass<String, UserEntity>(UserTable)

    var name by UserTable.name
    var username by UserTable.username
    var password by UserTable.password
    var salt by UserTable.salt
    var createdAt by UserTable.createdAt
    private var updateAt by UserTable.updatedAt

    fun toUser(token: String = "") = User(id.value, name, username, token, createdAt, updateAt)

    fun fromInsert(register: Register) {
        name = register.name
        username = register.username.toLowerCasePreservingASCIIRules()
        password = register.saltedHash.hash
        salt = register.saltedHash.salt
        createdAt = Clock.System.localeDateNow()
    }
}