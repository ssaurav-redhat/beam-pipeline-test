package com.example;


import com.example.model.DataFile;
import com.example.model.LogTable;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.coders.SerializableCoder;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.io.jdbc.JdbcIO;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.Distinct;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.GroupByKey;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.SerializableFunction;
import org.apache.beam.sdk.transforms.WithKeys;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.TypeDescriptors;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;

public class WordCount {
    public static void main(String[] args) {
        WordCountOptions options =
                PipelineOptionsFactory.fromArgs(args).withValidation().as(WordCountOptions.class);

        runWordCount(options);
    }

    public static void runWordCount(WordCountOptions options) {

        Pipeline p = Pipeline.create(options);

        PCollection<LogTable> dbReadData = p.apply("ReadFromMySQL", JdbcIO.<LogTable>read()
                .withDataSourceConfiguration(JdbcIO.DataSourceConfiguration.create(
                                "com.mysql.cj.jdbc.Driver", "jdbc:mysql://localhost:3307/exampledb")
                        .withUsername("exampleuser")
                        .withPassword("examplepass"))
                .withQuery("SELECT * FROM logs_table")
                .withCoder(SerializableCoder.of(LogTable.class))
                .withRowMapper(new JdbcIO.RowMapper<LogTable>() {
                    @Override
                    public LogTable mapRow(ResultSet resultSet) throws Exception {
                        String uuid = resultSet.getString("uuid");
                        Timestamp time = resultSet.getTimestamp("time");
                        return new LogTable(uuid, time);
                    }
                }));

        PCollection<String> distinctData = dbReadData.apply(ParDo.of(new DoFn<LogTable, String>() {
            @ProcessElement
            public void processElement(@Element LogTable data, OutputReceiver<String> receiver) {
                receiver.output(data.getUuid());
            }
        })).apply(Distinct.create());

        PCollection<KV<String, LogTable>> parsedData = dbReadData.apply(WithKeys.of(new SerializableFunction<LogTable, String>() {
            @Override
            public String apply(LogTable input) {
                return input.getUuid();
            }
        }));

        PCollection<String> formatResults = parsedData.apply(GroupByKey.create())
                .apply("FormatResults", MapElements
                .into(TypeDescriptors.strings())
                .via((KV<String, Iterable<LogTable>> wc) -> {
                    String key = wc.getKey();
                    Iterator<LogTable> iterator = wc.getValue().iterator();
                    ArrayList<LogTable> array = new ArrayList<>();

                    while (iterator.hasNext()) {
                        array.add(iterator.next());
                    }

                    DataFile data = new DataFile(key, array);
                    ObjectMapper obM =  new ObjectMapper();

                    try {
                        return obM.writeValueAsString(data);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }

                }));

        formatResults.apply(TextIO.write().to("output.txt"));
        p.run().waitUntilFinish();

    }

    public interface WordCountOptions extends PipelineOptions {

    }
}