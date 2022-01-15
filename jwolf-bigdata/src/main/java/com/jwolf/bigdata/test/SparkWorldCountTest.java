package com.jwolf.bigdata.test;

import org.apache.hadoop.shaded.org.eclipse.jetty.util.ajax.JSON;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.SparkSession;
import scala.Tuple2;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;


public class SparkWorldCountTest {
    public static void main(String[] args) {

        //输入文件与输出文件可以通过启动命令传入：args[0]
        String input = args.length >= 2 ? args[0] : "D:/test.txt";
        String output = args.length >= 2 ? args[1] : "D:/result";
        SparkConf conf = new SparkConf()
                .setMaster("local")  //linux设置为dashboard的master地址即可被监控，设置错也不会报错
                .setAppName(SparkWorldCountTest.class.getSimpleName());
        JavaSparkContext sparkContext = new JavaSparkContext(conf);
        JavaRDD<String> textFile = sparkContext.textFile("hdfs://192.168.1.18:9000///hdfs/d1/d2/local.txt");

        //测试1：读取文件wordcount后输出32
        List<Tuple2<Integer, String>> topK = sparkContext.textFile(input)
                .flatMap(str -> Arrays.asList(str.split("\n| ")).iterator())
                .mapToPair(word -> new Tuple2<String, Integer>(word, 1))
                .reduceByKey((integer1, integer2) -> integer1 + integer2)
                .filter(tuple2 -> tuple2._1.length() > 0)
                .mapToPair(tuple2 -> new Tuple2<>(tuple2._2, tuple2._1))  //单词与频数倒过来为新二元组，按频数倒排序取途topK
                .sortByKey(false)
                .take(10);
        for (Tuple2<Integer, String> tuple2 : topK) {
            System.out.println(JSON.toString(tuple2));
        }
        ;
        sparkContext.parallelize(topK).coalesce(1).saveAsTextFile(output);
        //测试2：通过spark-sql,sqlContext读取DB，json,csv文件等获取Dataset->获取RDD再进行各种处理
        SQLContext sqlContext = new SQLContext(SparkSession.builder().getOrCreate());
        Properties prop = new Properties();
        prop.setProperty("user", "root");
        prop.setProperty("password", "123456");
        prop.setProperty("driver", "com.mysql.cj.jdbc.Driver");
        Dataset<Row> dataFrame = sqlContext.read().jdbc("jdbc:mysql://192.168.1.19:3306/jwolf_user", "t_user", prop);
        System.out.println(JSON.toString(dataFrame.rdd().take(10)));
        dataFrame.createOrReplaceTempView("temp_t_user");
        sqlContext.sql("select * from temp_t_user where id > 1").show();
        //关闭资源
        sparkContext.close();
    }
}
