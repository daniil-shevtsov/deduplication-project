package com.daniilshevtsov.deduplication.feature.indextable.data

import com.daniilshevtsov.deduplication.ReferenceTable
import com.daniilshevtsov.deduplication.di.scope.ApplicationScope
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import org.sqlite.SQLiteConfig
import java.sql.Connection
import javax.inject.Inject

@ApplicationScope
class DataStore @Inject constructor(

) : DataStoreApi {

    init {
        setupDatabase()
    }

    private fun setupDatabase() {
        val db = Database.connect(
            url = "jdbc:sqlite:./data.db",
            driver = "org.sqlite.JDBC",
            setupConnection = {
                SQLiteConfig().apply {
                    setSharedCache(true)
                    setJournalMode(SQLiteConfig.JournalMode.WAL)
                    apply(it)
                }
            }
        )
        TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE

        transaction {
            addLogger(StdOutSqlLogger)
            SchemaUtils.create(ReferenceTable)
        }
    }

    override fun findReferenceByHash(hash: Int): ReferenceEntity? = transaction {
        ReferenceExposedEntity.find {
            ReferenceTable.segmentHash eq hash
        }.singleOrNull()?.toEntity()
    }


    override fun getReference(id: Int): ReferenceEntity? = transaction {
        ReferenceExposedEntity.findById(id)?.toEntity()
    }

    override suspend fun saveReference(referenceEntity: ReferenceEntity) {
        newSuspendedTransaction {
            referenceEntity.toExposed()
        }
    }

    private fun ReferenceExposedEntity.toEntity() = ReferenceEntity(
        segmentHash = segmentHash,
        fileName = fileName,
        segmentPosition = segmentPosition,
        segmentCount = segmentCount
    )

    private fun ReferenceEntity.toExposed() = let { referenceEntity ->
        ReferenceExposedEntity.new {
            segmentHash = referenceEntity.segmentHash
            fileName = referenceEntity.fileName
            segmentPosition = referenceEntity.segmentPosition
            segmentCount = referenceEntity.segmentCount
        }
    }


}