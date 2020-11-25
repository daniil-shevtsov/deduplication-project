package com.daniilshevtsov.deduplication.feature.indextable.data

import com.daniilshevtsov.deduplication.ReferenceTable
import com.daniilshevtsov.deduplication.di.scope.ApplicationScope
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection
import javax.inject.Inject

@ApplicationScope
class DataStore @Inject constructor(

) : DataStoreApi {

    init {
        setupDatabase()
    }

    private fun setupDatabase() {
        val dataSource = HikariDataSource().apply {
//            dataSourceClassName = "org.sqlite.SQLiteDataSource"
            driverClassName = "org.sqlite.JDBC"
            jdbcUrl = "jdbc:sqlite:./data.db"
            maximumPoolSize = 3
            transactionIsolation = "TRANSACTION_SERIALIZABLE"
            validate()
        }

        Database.connect(dataSource)

        TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE

        transaction {
            addLogger(StdOutSqlLogger)
            SchemaUtils.create(ReferenceTable)
        }
    }

    override fun deleteEverything() {
        transaction {
            ReferenceTable.dropStatement()
        }
    }

    override fun saveReferences(references: List<ReferenceEntity>) {
        transaction {
            references.forEach { reference ->
                reference.toExposed()
            }
        }
    }

    override fun saveReference(reference: ReferenceEntity) {
        transaction {
            reference.toExposed()
        }
    }

    override fun findReferenceByHash(hash: String): ReferenceEntity? = transaction {
        ReferenceExposedEntity.find {
            ReferenceTable.segmentHash eq hash
        }.singleOrNull()?.toEntity()
    }


    override fun getReference(id: String): ReferenceEntity? = transaction {
        ReferenceExposedEntity.find {
            ReferenceTable.segmentHash eq id
        }.singleOrNull()?.toEntity()
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