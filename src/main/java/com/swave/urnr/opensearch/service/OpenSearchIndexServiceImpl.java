package com.swave.urnr.opensearch.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenSearchIndexServiceImpl implements OpenSearchIndexService{

    private final ElasticsearchOperations elasticsearchOperations;

    public String createProjectIndex() throws IOException {
        //index name
        String indexName = "project";

        //mapping info
        XContentBuilder indexBuilder = XContentFactory.jsonBuilder()
                .startObject()

                .startObject("properties")

                .startObject("project_id")
                .field("type", "integer")
                .endObject()

                .startObject("project_name")
                .field("type", "text")
                .field("analyzer", "standard")
                .field("search_analyzer", "standard")
                .startObject("fields")
                .startObject("nori")
                .field("type", "text")
                .field("analyzer", "my_nori_analyzer")
                .field("search_analyzer", "my_nori_analyzer")
                .endObject()
                .startObject("ngram")
                .field("type", "text")
                .field("analyzer", "my_ngram_analyzer")
                .field("search_analyzer", "my_ngram_analyzer")
                .endObject()
                .endObject()
                .endObject()

                .startObject("description")
                .field("type", "text")
                .field("analyzer", "standard")
                .field("search_analyzer", "standard")
                .startObject("fields")
                .startObject("nori")
                .field("type", "text")
                .field("analyzer", "my_nori_analyzer")
                .field("search_analyzer", "my_nori_analyzer")
                .endObject()
                .startObject("ngram")
                .field("type", "text")
                .field("analyzer", "my_ngram_analyzer")
                .field("search_analyzer", "my_ngram_analyzer")
                .endObject()
                .endObject()
                .endObject()

                .startObject("create_date")
                .field("type", "date")
                .field("format", "strict_date_optional_time||epoch_second||yyyy-MM-dd HH:mm:ss")
                .endObject()

                .endObject()

                .endObject();

        Document mapping = Document.create().fromJson(Strings.toString(indexBuilder));
        Document setting = Document.create().fromJson(Strings.toString(makeSettingJson()));

        elasticsearchOperations.indexOps(IndexCoordinates.of(indexName)).create(setting,mapping);

        return "index_created";
    }

    public String createUserInProjectIndex() throws IOException {
        //index name
        String indexName = "user_in_project";
        //mapping info
        XContentBuilder indexBuilder = XContentFactory.jsonBuilder()
                .startObject()

                .startObject("properties")

                .startObject("user_in_project_id")
                .field("type", "integer")
                .endObject()

                .startObject("user_id")
                .field("type", "integer")
                .endObject()

                .startObject("project_id")
                .field("type", "integer")
                .endObject()

                .startObject("role")
                .field("type", "integer")
                .endObject()

                .endObject()

                .endObject();

        Document mapping = Document.create().fromJson(Strings.toString(indexBuilder));
        Document setting = Document.create().fromJson(Strings.toString(makeSettingJson()));

        elasticsearchOperations.indexOps(IndexCoordinates.of(indexName)).create(setting,mapping);

        return "index_created";
    }

    public String createUserIndex() throws IOException {
        //index name
        String indexName = "user";

        //mapping info
        XContentBuilder indexBuilder = XContentFactory.jsonBuilder()
                .startObject()

                .startObject("properties")

                .startObject("user_id")
                .field("type", "integer")
                .endObject()

                .startObject("username")
                .field("type", "text")
                .field("analyzer", "standard")
                .field("search_analyzer", "standard")
                .startObject("fields")
                .startObject("nori")
                .field("type", "text")
                .field("analyzer", "my_nori_analyzer")
                .field("search_analyzer", "my_nori_analyzer")
                .endObject()
                .startObject("ngram")
                .field("type", "text")
                .field("analyzer", "my_ngram_analyzer")
                .field("search_analyzer", "my_ngram_analyzer")
                .endObject()
                .endObject()
                .endObject()

                .startObject("provider")
                .field("type", "text")
                .field("analyzer", "standard")
                .field("search_analyzer", "standard")
                .startObject("fields")
                .startObject("nori")
                .field("type", "text")
                .field("analyzer", "my_nori_analyzer")
                .field("search_analyzer", "my_nori_analyzer")
                .endObject()
                .startObject("ngram")
                .field("type", "text")
                .field("analyzer", "my_ngram_analyzer")
                .field("search_analyzer", "my_ngram_analyzer")
                .endObject()
                .endObject()
                .endObject()

                .startObject("email")
                .field("type", "text")
                .field("analyzer", "standard")
                .field("search_analyzer", "standard")
                .startObject("fields")
                .startObject("nori")
                .field("type", "text")
                .field("analyzer", "my_nori_analyzer")
                .field("search_analyzer", "my_nori_analyzer")
                .endObject()
                .startObject("ngram")
                .field("type", "text")
                .field("analyzer", "my_ngram_analyzer")
                .field("search_analyzer", "my_ngram_analyzer")
                .endObject()
                .endObject()
                .endObject()

                .endObject()

                .endObject();

        Document mapping = Document.create().fromJson(Strings.toString(indexBuilder));
        Document setting = Document.create().fromJson(Strings.toString(makeSettingJson()));

        elasticsearchOperations.indexOps(IndexCoordinates.of(indexName)).create(setting,mapping);

        return "index_created";
    }

    private XContentBuilder makeSettingJson() throws IOException {
        return XContentFactory.jsonBuilder()
                .startObject()

                .startObject("index")

                .startObject("analysis")

                .startObject("tokenizer")

                .startObject("my_nori_tokenizer")
                .field("type", "nori_tokenizer")
                .field("decompound_mode", "mixed")
                .field("discard_punctuation", "false")
                .endObject()

                .startObject("my_ngram_tokenizer")
                .field("type", "ngram")
                .field("min_gram", "2")
                .field("max_gram", "3")
                .endObject()

                .endObject()

                .startObject("filter")

                .startObject("stopwords")
                .field("type", "stop")
                .array("stopwords", " ")
                .endObject()

                .endObject()


                .startObject("analyzer")

                .startObject("my_nori_analyzer")
                .field("type", "custom")
                .field("tokenizer", "my_nori_tokenizer")
                .array("filter", new String[]{
                                "lowercase",
                                "stop",
                                "trim",
                                "stopwords",
                                "nori_part_of_speech"
                        }
                )
                .array("char_filter", new String[]{"html_strip"})
                .endObject()

                .startObject("my_ngram_analyzer")
                .field("type", "custom")
                .field("tokenizer", "my_ngram_tokenizer")
                .array("filter", new String[]{
                                "lowercase",
                                "stop",
                                "trim",
                                "stopwords",
                                "nori_part_of_speech"
                        }
                )
                .array("char_filter", new String[]{"html_strip"})
                .endObject()

                .endObject()

                .endObject()

                .endObject()

                .endObject();
    }

}



