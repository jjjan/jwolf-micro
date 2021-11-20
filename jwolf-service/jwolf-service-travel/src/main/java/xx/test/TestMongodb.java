package xx.test;

import cn.hutool.core.map.MapBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Description: TODO
 *
 * @author majun
 * @version 1.0
 * @date 2021-11-20 22:53
 */
@Component
@Slf4j
@EnableScheduling
public class TestMongodb {
    @Autowired
    private MongoTemplate mongoTemplate;


    @Scheduled(cron = "0/10 * * * * ?")
    public void testMongodb() throws Exception {

        for (int i = 0; i < 10; i++) {
            Map<Object, Object> map = MapBuilder.create().put("a", 1).put("b", 2).build();
            mongoTemplate.save(map, "test");
        }
        Criteria criteria1 = Criteria.where("a").is(1);
        Criteria criteria2 = Criteria.where("b").is(1);
        Criteria criteria3 = Criteria.where("b").gt(1).lt(10);
        Criteria criteria = new Criteria().orOperator(criteria3, criteria1, criteria2);
        Query query = new Query()
                .addCriteria(criteria)
                .with(Sort.by(Sort.Order.desc("b")));
        query.fields().include("a").include("_id");
        mongoTemplate.executeQuery(query, "test", document -> {
            System.out.println(document.toJson());
        });


    }

}
