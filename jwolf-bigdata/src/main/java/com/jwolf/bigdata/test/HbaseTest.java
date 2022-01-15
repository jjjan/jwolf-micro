package com.jwolf.bigdata.test;

import lombok.SneakyThrows;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Put;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Description: TODO
 *
 * @author majun
 * @version 1.0
 * @date 2022-01-12 00:19
 */
public class HbaseTest {

    @SneakyThrows
    public static void main(String[] args) {
        org.apache.hadoop.conf.Configuration configuration = HBaseConfiguration.create();
        //这里只指定了ZK的连接参数
        configuration.set("hbase.zookeeper.quorum", "192.168.1.19");
        configuration.set("hbase.zookeeper.port", "2181");
        configuration.set("hbase.zookeeper.znode", "/hbase");
        configuration.set("hbase.client.keyvalue.maxsize", "1572864000");
        configuration.set("hbase.regionserver.port", "1572864000");
        long t = System.currentTimeMillis();
        Connection conn = ConnectionFactory.createConnection(configuration);
        System.out.println("连接耗时" + (System.currentTimeMillis() - t));
        //查看所有表
        // todo:连接不上，docker log显示 Client attempting to establish new session at /192.168.1.11:62303
        String tables = Arrays.stream(conn.getAdmin().listTableNames()).map(TableName::getNameAsString).collect(Collectors.joining());
        System.out.println("已存在表：" + tables);
        //存入数据
        Map<String, String> cloumns = new HashMap<String, String>();
        cloumns.put("name", "zzq");//列名和值
        cloumns.put("age", "22");
        //往表中的第row=1的info族中增加了名为name、age数据
        Put put = new Put("1".getBytes());
        put.addColumn("info".getBytes(), "name".getBytes(), "jwolf".getBytes());
        put.addColumn("info".getBytes(), "age".getBytes(), "18".getBytes());
        conn.getTable(TableName.valueOf("person")).put(put);
        System.out.println("增加成功");

    }

}
