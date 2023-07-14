package hvalfangst.kafka.service

import hvalfangst.kafka.db.Roles
import hvalfangst.kafka.db.Users
import hvalfangst.kafka.db.UsersRoles
import hvalfangst.kafka.dto.CreateUserRequest
import hvalfangst.kafka.model.Role
import hvalfangst.kafka.model.User
import hvalfangst.kafka.security.BCryptHasher
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Service

@Service
class UserService {

    fun createUser(request: CreateUserRequest) {
        return transaction {
            Users.insert {
                it[email] = request.email
                it[fullName] = request.fullName
                it[password] = BCryptHasher.encodePassword(request.password)
                it[enabled] = true
            }
        }
    }

    fun assignRoleToUser(userId: Int, roleId: Int) {
        transaction {
            UsersRoles.insert  {
                it[UsersRoles.userId] = userId
                it[UsersRoles.roleId] = roleId
            }
        }
    }

    fun getUserRoles(userId: Int): List<Role> {
        return transaction {
            (Roles innerJoin UsersRoles innerJoin Users)
                .select { Users.id eq userId }
                .map { row ->
                    Role(
                        row[Roles.id],
                        row[Roles.name]
                    )
                }
        }
    }

    fun getUserByEmail(email: String): User? {
        return transaction {
            Users.select { Users.email eq email }
                .map { row ->
                    User(
                        row[Users.id],
                        row[Users.email],
                        row[Users.fullName],
                        row[Users.password],
                        row[Users.enabled]
                    )
                }
                .singleOrNull()
        }
    }

}
