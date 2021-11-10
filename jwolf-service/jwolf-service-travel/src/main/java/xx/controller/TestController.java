package xx.controller;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.admin.indices.settings.put.UpdateSettingsRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.PutMappingRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * Description: TODO
 *
 * @author majun
 * @version 1.0
 * @date 2021-11-08 0:35
 */
@RestController
@Slf4j
public class TestController {

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;
    @Autowired
    private RestHighLevelClient esClient;

    @GetMapping("/test0")
    public boolean existIndex(String indexName) throws IOException {
        GetIndexRequest request = new GetIndexRequest().indices(indexName);
        return esClient.indices().exists(request, RequestOptions.DEFAULT);
    }

    @GetMapping("/test1")
    public void createIndex(String indexName, int numberOfShards, int numberOfReplicas) throws IOException {
        CreateIndexRequest request = new CreateIndexRequest(indexName)
                .settings(Settings.builder()
                        .put("index.number_of_shards", numberOfReplicas)
                        .put("index.number_of_replicas", numberOfReplicas)
                )
                .mapping("{\n" +
                        "  \"properties\": {\n" +
                        "    \"message\": {\n" +
                        "      \"type\": \"text\"\n" +
                        "    }\n" +
                        "  }\n" +
                        "}", XContentType.JSON);
        esClient.indices().createAsync(request, RequestOptions.DEFAULT, new ActionListener<CreateIndexResponse>() {
            @Override
            public void onResponse(CreateIndexResponse createIndexResponse) {
                log.info("执行情况:" + createIndexResponse);
            }

            @Override
            public void onFailure(Exception e) {
                log.info("执行失败的原因:" + e.getMessage());
            }
        });

    }

    @GetMapping("/test2")
    public void updateIndexSettings(String indexName) throws IOException {
        UpdateSettingsRequest request = new UpdateSettingsRequest(indexName)
                .settings(Settings.builder().put("index.number_of_replicas", 2))
                .setPreserveExisting(true);
        esClient.indices().putSettingsAsync(request, RequestOptions.DEFAULT, new ActionListener<AcknowledgedResponse>() {
            @Override
            public void onResponse(AcknowledgedResponse acknowledgedResponse) {
                log.info("执行情况:" + acknowledgedResponse);
            }

            @Override
            public void onFailure(Exception e) {
                log.error("执行失败的原因:" + e.getMessage());
            }
        });
    }

    @GetMapping("/test3")
    public void putIndexMapping(String indexName) {
        PutMappingRequest request = new PutMappingRequest(indexName)
                .source("{\"properties\":{\"new_parameter\":{\"type\":\"text\"}}}", XContentType.JSON);
        esClient.indices().putMappingAsync(request, RequestOptions.DEFAULT, new ActionListener<AcknowledgedResponse>() {
            @Override
            public void onResponse(AcknowledgedResponse acknowledgedResponse) {
                log.info("执行情况:" + acknowledgedResponse);
            }

            @Override
            public void onFailure(Exception e) {
                log.error("执行失败的原因:" + e.getMessage());
            }
        });
    }

    @GetMapping("/test4")
    public void addDocument1(String indexName) throws IOException {
        IndexRequest request = new IndexRequest(indexName)
                .id("1")
                .source("{" +
                        "\"user\":\"kimchy\"," +
                        "\"postDate\":\"2020-03-28\"," +
                        "\"message\":\"trying out Elasticsearch\"" +
                        "}")
                .routing("routing");
        IndexResponse response = esClient.index(request, RequestOptions.DEFAULT);
        log.info("同步返回的状态码" + response.status().getStatus());
    }

    @GetMapping("/test5")
    public void updateDocument(String indexName) throws IOException {
        UpdateRequest request = new UpdateRequest(indexName, "1")
                .doc("updated", LocalDateTime.now(), "reason", "daily update");
        UpdateResponse response = esClient.update(request, RequestOptions.DEFAULT);
        log.info("同步返回的状态码" + response.status().getStatus());

    }

    @GetMapping("/test6")
    public void deleteDocument(String indexName) throws IOException {
        DeleteByQueryRequest deleteByQueryRequest = new DeleteByQueryRequest("_all")
                .setQuery(QueryBuilders.termQuery("user", "kimchy"));
        BulkByScrollResponse response = esClient.deleteByQuery(deleteByQueryRequest, RequestOptions.DEFAULT);
        log.info("同步返回的删除数量" + response.getBatches());

    }

    @GetMapping("/test7")
    public void bulkDocument(String indexName) throws IOException {
        BulkRequest request = new BulkRequest()
                .add(new DeleteRequest(indexName, "3"))
                .add(new UpdateRequest(indexName, "2")
                        .doc(XContentType.JSON, "other", "test"))
                .add(new IndexRequest(indexName).id("4")
                        .source(XContentType.JSON, "field", "baz"));
        BulkResponse responses = esClient.bulk(request, RequestOptions.DEFAULT);
        log.info("同步返回的状态码" + responses.status().getStatus());
    }


    @GetMapping("/test8")
    public void searchDocument(String indexNmae) throws IOException {

        BoolQueryBuilder booleanQueryBuilder = QueryBuilders.boolQuery()
                .filter(QueryBuilders.rangeQuery("age").from(15).to(40))
                .must(QueryBuilders.matchQuery("description", "Dubbo"));
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder()
                .query(booleanQueryBuilder)
                .from(0)
                .size(5)
                .timeout(new TimeValue(60, TimeUnit.SECONDS));
        SearchRequest searchRequest = new SearchRequest(indexNmae);
        searchRequest.source(sourceBuilder);
        SearchResponse response = esClient.search(searchRequest, RequestOptions.DEFAULT);
        log.info("同步返回的状态码" + response.status().getStatus());

    }

}
