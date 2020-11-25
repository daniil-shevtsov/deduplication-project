package com.daniilshevtsov.deduplication.feature.indextable.data

import com.daniilshevtsov.deduplication.ReferenceTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class ReferenceExposedEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ReferenceExposedEntity>(ReferenceTable)

    var segmentHash by ReferenceTable.segmentHash
    var fileName by ReferenceTable.fileName
    var segmentPosition by ReferenceTable.segmentPosition
    var segmentCount by ReferenceTable.segmentCount
}