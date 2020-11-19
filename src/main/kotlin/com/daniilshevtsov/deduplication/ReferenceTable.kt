package com.daniilshevtsov.deduplication

import org.jetbrains.exposed.sql.Table


object ReferenceTable : Table() {
    override val primaryKey by lazy { PrimaryKey(id) }
    val id = integer("id").autoIncrement()
    val fullName = varchar("name", 50)
    val lastName = varchar("last_name", 50)
}