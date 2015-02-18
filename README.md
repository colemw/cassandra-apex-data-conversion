# cassandra-apex-data-conversion
Data conversion application to be used for a one-time data migration and conversion to a new Cassandra schema.

Used to read existing data from Cassandra column families, convertion to new obects, and written using batch statements to a target Cassandra cluster.

For main application procedure, please refer to cassandra.util.DataConverter. For old and new schema, please refer to ApexCourseStructureSchemaPre and ApexCourseStructureSchemaPost under resources.
