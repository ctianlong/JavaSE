package test;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.ConstantScoreQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.cardinality.CardinalityAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.cardinality.ParsedCardinality;
import org.elasticsearch.search.aggregations.metrics.sum.ParsedSum;
import org.elasticsearch.search.aggregations.metrics.tophits.ParsedTopHits;
import org.elasticsearch.search.aggregations.pipeline.PipelineAggregatorBuilders;
import org.elasticsearch.search.aggregations.pipeline.bucketselector.BucketSelectorPipelineAggregationBuilder;
import org.elasticsearch.search.aggregations.pipeline.bucketsort.BucketSortPipelineAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ctl
 * @date 2021/10/20
 */
public class WendingEsTest {


	/**
	 * GET /lable_library/_search
	 * {
	 *   "size": 0,
	 *   "query": {
	 *     "constant_score": {
	 *       "filter": {
	 *         "bool": {
	 *           "should": [
	 *             {
	 *               "term": {
	 *                 "lable": "二婚"
	 *               }
	 *             },
	 *             {
	 *               "term": {
	 *                 "lable": "赘婿"
	 *               }
	 *             }
	 *           ]
	 *         }
	 *       }
	 *     }
	 *   },
	 *   "aggs": {
	 *     "sid": {
	 *       "terms": {
	 *         "field": "sourceUuid",
	 *         "order": {
	 *           "percent_sum": "desc"
	 *         },
	 *         "size": 50000
	 *       },
	 *       "aggs": {
	 *         "percent_sum": {
	 *           "sum": {
	 *             "field": "percent"
	 *           }
	 *         },
	 *         "lable_count_bucket_filter": {
	 *           "bucket_selector": {
	 *             "buckets_path": {
	 *               "cur_doc_cnt": "_count"
	 *             },
	 *             "script": "params.cur_doc_cnt == 2"
	 *           }
	 *         },
	 *         "percent_sum_bucket_sort": {
	 *           "bucket_sort": {
	 *             "from": 0,
	 *             "size": 10000
	 *           }
	 *         },
	 *         "other_field": {
	 *           "top_hits": {
	 *             "_source": {
	 *               "includes": [
	 *                 "lable",
	 *                 "termFrequency"
	 *               ]
	 *             },
	 *             "size": 1
	 *           }
	 *         }
	 *       }
	 *     }
	 *   }
	 * }
	 *
	 */
	@Test
	public void testAgg() throws IOException {
		// 文鼎线上书籍es地址
		// 聚合、排序、分页、获取非聚合字段（topHits）示例
		ClientConfiguration clientConfiguration = ClientConfiguration.builder()
				.connectedTo(StringUtils.split("10.122.133.184:7000,10.122.133.179:7000,10.122.133.180:7000", ","))
				.withConnectTimeout(15000).withSocketTimeout(15000)
				.build();
		RestHighLevelClient client = RestClients.create(clientConfiguration).rest();
		SearchRequest searchRequest = new SearchRequest("lable_library");
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		sourceBuilder.size(0);

		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		boolQueryBuilder.should(QueryBuilders.termQuery("lable", "二婚"));
		boolQueryBuilder.should(QueryBuilders.termQuery("lable", "赘婿"));

		ConstantScoreQueryBuilder constantScoreQueryBuilder = QueryBuilders.constantScoreQuery(boolQueryBuilder);
		sourceBuilder.query(constantScoreQueryBuilder);

		Map<String, String> bucketsPathMap = new HashMap<>();
		bucketsPathMap.put("cur_doc_cnt", "_count");
		BucketSelectorPipelineAggregationBuilder lable_count_bucket_filter =
				PipelineAggregatorBuilders.bucketSelector("lable_count_bucket_filter", bucketsPathMap, new Script(String.format("params.cur_doc_cnt == %d", 2)));
		BucketSortPipelineAggregationBuilder pre_sum_bucket_sort = PipelineAggregatorBuilders.bucketSort("percent_sum_bucket_sort", null).from(0).size(10000);

		TermsAggregationBuilder aggs = AggregationBuilders.terms("sid").field("sourceUuid").order(BucketOrder.aggregation("percent_sum", false)).size(50000)
				.subAggregation(AggregationBuilders.sum("percent_sum").field("percent"))
				.subAggregation(lable_count_bucket_filter)
				.subAggregation(pre_sum_bucket_sort)
				.subAggregation(AggregationBuilders.topHits("other_field").size(1).fetchSource(new String[]{"lable", "termFrequency"}, null));
		sourceBuilder.aggregation(aggs);

		searchRequest.source(sourceBuilder);
		SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
		Aggregations aggregations = response.getAggregations();
		ParsedStringTerms sid = aggregations.get("sid");
		List<ParsedStringTerms.ParsedBucket> buckets = (List<ParsedStringTerms.ParsedBucket>) sid.getBuckets();
		for (int i = 0; i < 5; i++) {
			ParsedStringTerms.ParsedBucket b = buckets.get(i);
			System.out.println(b.getKey());
			Aggregations agg = b.getAggregations();
			ParsedSum percentSum =  agg.get("percent_sum");
			ParsedTopHits otherField = agg.get("other_field");
			System.out.println(percentSum.getValue());
			System.out.println(otherField.getHits().getHits()[0].getSourceAsMap());
			System.out.println("-------------");
		}
	}

	/**
	 * GET /lable_library/_search
	 * {
	 *   "size": 0,
	 *   "query": {
	 *     "constant_score": {
	 *       "filter": {
	 *         "bool": {
	 *           "should": [
	 *             {
	 *               "term": {
	 *                 "lable": "二婚"
	 *               }
	 *             },
	 *             {
	 *               "term": {
	 *                 "lable": "赘婿"
	 *               }
	 *             }
	 *           ]
	 *         }
	 *       }
	 *     }
	 *   },
	 *   "aggs": {
	 *     "book_count":{
	 *       "cardinality": {
	 *         "field": "sourceUuid",
	 *         "precision_threshold": 10000
	 *       }
	 *     }
	 *   }
	 * }
	 */
	@Test
	public void testCountDistinct() throws IOException {
		// 文鼎线上书籍es地址
		// count(distinct 字段) 示例
		ClientConfiguration clientConfiguration = ClientConfiguration.builder()
				.connectedTo(StringUtils.split("10.122.133.184:7000,10.122.133.179:7000,10.122.133.180:7000", ","))
				.withConnectTimeout(15000).withSocketTimeout(15000)
				.build();
		RestHighLevelClient client = RestClients.create(clientConfiguration).rest();
		SearchRequest searchRequest = new SearchRequest("lable_library");
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		sourceBuilder.size(0);

		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		boolQueryBuilder.should(QueryBuilders.termQuery("lable", "二婚"));
		boolQueryBuilder.should(QueryBuilders.termQuery("lable", "赘婿"));

		ConstantScoreQueryBuilder constantScoreQueryBuilder = QueryBuilders.constantScoreQuery(boolQueryBuilder);
		sourceBuilder.query(constantScoreQueryBuilder);

		CardinalityAggregationBuilder aggs = AggregationBuilders.cardinality("book_count").field("sourceUuid").precisionThreshold(10000);
		sourceBuilder.aggregation(aggs);

		searchRequest.source(sourceBuilder);
		SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
		Aggregations aggregations = response.getAggregations();
		ParsedCardinality cardinality = aggregations.get("book_count");
		System.out.println(cardinality.getValue());
	}




	@Test
	public void testAggTest() throws IOException {
		// 文鼎线上书籍es地址
		// 聚合、排序、分页、获取非聚合字段（topHits）示例
		ClientConfiguration clientConfiguration = ClientConfiguration.builder()
				.connectedTo(StringUtils.split("10.194.225.241:9200", ","))
				.withConnectTimeout(15000).withSocketTimeout(15000)
				.build();
		RestHighLevelClient client = RestClients.create(clientConfiguration).rest();

		SearchRequest searchRequest = new SearchRequest("wending_ads_campaign_stat");
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		sourceBuilder.size(0);

		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		boolQueryBuilder.must(QueryBuilders.termQuery("backendAccountId", 10));

		ConstantScoreQueryBuilder constantScoreQueryBuilder = QueryBuilders.constantScoreQuery(boolQueryBuilder);
		sourceBuilder.query(constantScoreQueryBuilder);

		BucketSortPipelineAggregationBuilder pre_sum_bucket_sort = PipelineAggregatorBuilders.bucketSort("sum_bucket_sort", null).from(0).size(20);

		TermsAggregationBuilder aggs = AggregationBuilders.terms("campaign_id_group").field("campaign_id").order(BucketOrder.aggregation("cost_sum", false)).size(10000)
				.subAggregation(AggregationBuilders.sum("cost_sum").field("cost"))
				.subAggregation(AggregationBuilders.sum("show_sum").field("show"))
				.subAggregation(AggregationBuilders.sum("click_sum").field("click"))
				.subAggregation(pre_sum_bucket_sort)
				.subAggregation(AggregationBuilders.topHits("other_field").size(1).fetchSource(new String[]{"groupName", "accountName"}, null));
		sourceBuilder.aggregation(aggs);

		searchRequest.source(sourceBuilder);
		SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);

		Aggregations aggregations = response.getAggregations();

		ParsedLongTerms sid = aggregations.get("campaign_id_group");
		List<ParsedLongTerms.ParsedBucket> buckets = (List<ParsedLongTerms.ParsedBucket>) sid.getBuckets();
		for (int i = 0; i < 5; i++) {
			ParsedLongTerms.ParsedBucket b = buckets.get(i);
			System.out.println(b.getKey());
			Aggregations agg = b.getAggregations();

			ParsedSum click_sum = agg.get("click_sum");
			System.out.println(click_sum.getValue());
			ParsedSum show_sum = agg.get("show_sum");
			System.out.println(show_sum.getValue());
			ParsedSum cost_sum = agg.get("cost_sum");
			System.out.println(cost_sum.getValue());

			ParsedTopHits otherField = agg.get("other_field");
			System.out.println(otherField.getHits().getHits()[0].getSourceAsString());
			System.out.println("-------------");
		}
	}
}
