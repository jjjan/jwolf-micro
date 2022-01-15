package com.jwolf.bigdata.test;

import org.apache.spark.ml.Pipeline;
import org.apache.spark.ml.PipelineModel;
import org.apache.spark.ml.PipelineStage;
import org.apache.spark.ml.classification.LinearSVC;
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator;
import org.apache.spark.ml.feature.PCA;
import org.apache.spark.ml.feature.PCAModel;
import org.apache.spark.ml.feature.StandardScaler;
import org.apache.spark.ml.feature.StandardScalerModel;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.io.IOException;

/**
 * 参考scala版本改写的java版本
 * https://blog.csdn.net/weixin_41512727/article/details/89851692
 * <p>
 * 数据集：https://github.com/Great1414/spark_ml_learn/tree/master/data
 * spark ml算法官方示例：https://spark.apache.org/docs/latest/ml-classification-regression.html
 *
 * @author majun
 * @version 1.0
 * @date 2022-01-12 22:45
 */
public class SparkmlSvmLrTest {

    public static void main(String[] args) throws IOException {
        //创建SparkSession
        SparkSession session = SparkSession
                .builder()
                .appName("svm_example")
                .master("local")
                .getOrCreate();

        //读取数据集(libsvm是一种特殊的数据集，可通过项目目录tool下的工具制作)
        Dataset<Row> data = session.read().format("libsvm").load("jwolf-bigdata/dataset/sample_libsvm_data.txt");
        data.show(5, 100);
        //数据归一化
        StandardScaler scaler = new StandardScaler()
                .setInputCol("features")
                .setOutputCol("scaledfeatures")
                .setWithMean(true)
                .setWithStd(true);
        StandardScalerModel scalerdata = scaler.fit(data);
        Dataset<Row> scaleddata = scalerdata.transform(data).select("label", "scaledfeatures").toDF("label", "features");
        scaleddata.show(5, 100);
        //PCA降维,保留3-5特征就能达到97%的准确率，过多容易过拟合，训练时间过长
        PCAModel pca = new PCA()
                .setInputCol("features")
                .setOutputCol("pcafeatures")
                .setK(3)
                .fit(scaleddata);
        Dataset<Row> pcadata = pca.transform(scaleddata).select("label", "pcafeatures").toDF("label", "features");
        pcadata.show(5, 100);
        //切分出训练集与验证集
        Dataset<Row>[] split = pcadata.randomSplit(new double[]{0.5, 0.5}, 20);
        Dataset<Row> trainData = split[0];
        Dataset<Row> testData = split[1];
        System.out.println(trainData + "|" + split[1]);
        //1.使用支持向量机SVM算法训练模型,如下参数可达到97%准确率
        LinearSVC lsvc = new LinearSVC().setMaxIter(10).setRegParam(0.1);
        Pipeline pipeline = new Pipeline().setStages(new PipelineStage[]{scaler, pca, lsvc});
        PipelineModel model = pipeline.fit(trainData);

        //2.使用逻辑回归LogisticRegression算法训练模型,如下参数92%准确率,PCA降维调整,算法参数调整或能达到SVM的准确率
        //LogisticRegression logisticRegression = new LogisticRegression()
        //        .setMaxIter(10)
        //        .setRegParam(0.3)
        //        .setElasticNetParam(0)
        //        .setFeaturesCol("features")
        //        .setLabelCol("label")
        //        .setPredictionCol("prediction");
        //LogisticRegressionModel model = logisticRegression.fit(trainData);

        //推理预测,可以看到预测prediction与监督label基本都一致，达到了97%准确率
        Dataset<Row> res = model.transform(testData).select("prediction", "label");
        res.show((int) testData.count(), 100);
        //评估模型
        MulticlassClassificationEvaluator evaluator = new MulticlassClassificationEvaluator()
                .setLabelCol("label")
                .setPredictionCol("prediction")
                .setMetricName("accuracy");
        double accuracy = evaluator.evaluate(res);
        System.out.println(String.format("Accuracy = %s", accuracy));
        //模型保存起来,供直接加载使用（一些业务可能还得python tensorflow/pytorch/mindspore等DL框架才能满足,如果要使用，建议使用sparklib下的一些算法，训练出来的模型可以存为.pmml格式,能够与python sklearn等异构使用）
        //注意window需要配置如下变量并在C:\Windows\System32加入hadoop-3.2.1/bin里面的hadoop.dll，winutils下载地址https://github.com/cdarlint/winutils
        System.setProperty("hadoop.home.dir", "./tool/hadoop-3.2.1");
        model.write().overwrite().save("./jwolf-bigdata/model/sparkml_svm_model");
        PipelineModel sameModel = PipelineModel.load("./jwolf-bigdata/model/sparkml_svm_model");
        Dataset<Row> sameRes = sameModel.transform(testData.limit(3)).select("prediction", "label");
        sameRes.show(10, 100);
        //使用spark-streaming获取数据实时预测或sparkCore/sql离线分析批量预测等,下游mysql/redis/ES/websocket等
        session.stop();
    }

}







