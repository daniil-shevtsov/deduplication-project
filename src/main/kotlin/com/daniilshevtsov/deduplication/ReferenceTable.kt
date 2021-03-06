package com.daniilshevtsov.deduplication

import org.jetbrains.exposed.dao.id.IntIdTable


object ReferenceTable : IntIdTable() {
    override val primaryKey by lazy { PrimaryKey(id) }
    val segmentHash = varchar("segment_hash", 256)
    val fileName = varchar("file_name", 50)
    val segmentPosition = long("segment_position")
    val segmentCount = long("segment_count")
}