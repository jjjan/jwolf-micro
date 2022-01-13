package com.jwolf.bigdata;

import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.ivy.util.FileUtil;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.clustering.KMeans;
import org.apache.spark.mllib.clustering.KMeansModel;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.pmml.export.KMeansPMMLModelExport;
import org.apache.spark.sql.SparkSession;
import org.sparkproject.dmg.pmml.PMML;
import org.sparkproject.jpmml.model.PMMLUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Arrays;

/**
 * 官网kmeans聚类Java版本修订的
 * https://spark.apache.org/docs/latest/mllib-clustering.html
 *
 * @author majun
 * @version 1.0
 * @date 2022-01-13 23:01
 */
public class SparkmllibKmeansTest {
    @SneakyThrows
    public static void main(String[] args) {
        //创建SparkSession
        SparkSession session = SparkSession
                .builder()
                .appName("svm_example")
                .master("local")
                .getOrCreate();
        JavaSparkContext jsc = JavaSparkContext.fromSparkContext(session.sparkContext());
        JavaRDD<String> data = jsc.textFile("./jwolf-bigdata/dataset/kmeans_data.txt");
        JavaRDD<Vector> parsedData = data.map(s -> {
            String[] sarray = s.split(" +");
            double[] values = new double[sarray.length];
            for (int i = 0; i < sarray.length; i++) {
                values[i] = Double.parseDouble(sarray[i]);
            }
            return Vectors.dense(values);
        });
        parsedData.cache();

        // Cluster the data into two classes using KMeans
        KMeansModel clusters = KMeans.train(parsedData.rdd(), 3, 20);

        System.out.println("Cluster centers:");
        Arrays.stream(clusters.clusterCenters()).forEach(vector -> System.out.println(vector + ""));
        double cost = clusters.computeCost(parsedData.rdd());
        System.out.println("Cost: " + cost);

        // Evaluate clustering by computing Within Set Sum of Squared Errors
        double WSSSE = clusters.computeCost(parsedData.rdd());
        System.out.println("Within Set Sum of Squared Errors = " + WSSSE);

        // Save and load model
        FileUtil.forceDelete(new File("./jwolf-bigdata/model/KMeansModel"));
        clusters.save(jsc.sc(), "./jwolf-bigdata/model/KMeansModel");
        KMeansModel sameModel = KMeansModel.load(jsc.sc(), "./jwolf-bigdata/model/KMeansModel");
        int predict1 = sameModel.predict(Vectors.dense(1, 1, 1, 1, 5, 1, 1, 2, 2, 1));
        int predict2 = sameModel.predict(Vectors.dense(2, 5, 5, 5, 5, 5, 6, 6, 2, 1));
        int predict3 = sameModel.predict(Vectors.dense(8, 5, 9, 9, 9, 9, 9, 6, 8, 1));
        System.out.println(String.format("%s|%s|%s", predict1, predict2, predict3));
        //保存为.pmml格式，方便与python sklearn等异构使用
        File file = new File("./jwolf-bigdata/model/KMeansModelPMML.pmml");
        FileOutputStream out = FileUtils.openOutputStream(file);
        PMMLUtil.marshal(new KMeansPMMLModelExport(clusters).getPmml(), out);
        IOUtils.closeQuietly(out, e -> e.printStackTrace());
        FileInputStream in = FileUtils.openInputStream(file);
        PMML pmml = PMMLUtil.unmarshal(in);
        //pmml无法导入？官方也只说明了导出，没导入
        IOUtils.closeQuietly(in, e -> e.printStackTrace());

    }
}
