package id.my.mufidz.model.entity

import id.my.mufidz.model.User
import id.my.mufidz.model.table.UserTable
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
    var updateAt by UserTable.updatedAt

    fun toUser(token: String = "") = User(id.value, name, username, token, createdAt, updateAt)
}