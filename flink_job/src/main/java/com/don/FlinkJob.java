package com.don;

import com.ververica.cdc.debezium.DebeziumDeserializationSchema;
import com.ververica.cdc.connectors.base.source.jdbc.JdbcIncrementalSource;
import com.ververica.cdc.connectors.postgres.source.PostgresSourceBuilder;
import com.ververica.cdc.debezium.JsonDebeziumDeserializationSchema;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.connector.elasticsearch.sink.Elasticsearch7SinkBuilder;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.elasticsearch.ElasticsearchSinkFunction;
import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Requests;

import java.util.HashMap;
import java.util.Map;

public class FlinkJob {
    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        JdbcIncrementalSource<String> postgresIncrementalSource =
                PostgresSourceBuilder.PostgresIncrementalSource.<String>builder()
                        .hostname("db")
                        .port(5435)
                        .database("app_dbi")
                        .schemaList("app_dbi")
                        .tableList("app_dbi.transaction")
                        .username("postgres")
                        .password("1356")
                        .slotName("flink")
                        .decodingPluginName("decoderbufs")
                        .deserializer(new JsonDebeziumDeserializationSchema())
                        .includeSchemaChanges(true)
                        .splitSize(2)
                        .build();

        env.enableCheckpointing(3000);

        DataStream<String> postgresStream = env.fromSource(
                        postgresIncrementalSource,
                        WatermarkStrategy.noWatermarks(),
                        "PostgresSource")
                .setParallelism(2);

        postgresStream.print();

        ElasticsearchSinkFunction<String> elasticsearchSinkFunction = (element, ctx, indexer) -> {
            try {
                IndexRequest request = createIndexRequest(element);
                indexer.add(request);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        Elasticsearch7SinkBuilder elasticsearchSink = new Elasticsearch7SinkBuilder<String>()
                .setBulkFlushMaxActions(1)
                .setHosts(new HttpHost("elasticsearch", 9200, "http"))
                .setEmitter(
                        (element, context, indexer) ->
                                indexer.add(createIndexRequest(element)));


        postgresStream.sinkTo(elasticsearchSink.build());

        env.execute("Output Postgres Snapshot");
    }

    private static IndexRequest createIndexRequest(String element) {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("data", element);
        return Requests.indexRequest()
                .index("transactions")
                .id(element)
                .source(jsonMap);
    }

}