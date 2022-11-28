package com.example.springbootredis;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class SpringbootRedisApplicationTests {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    /**
     * 操作String类型的数据
     */
    @Test
    void testString() {

        redisTemplate.opsForValue().set("city","yunnan");

        redisTemplate.opsForValue().set("key1","666",10l, TimeUnit.SECONDS);

        Boolean aBoolean = redisTemplate.opsForValue().setIfAbsent("city1", "yunnan");
        System.out.println(aBoolean);
    }

    /**
     * 操作hash类型数据
     */
    @Test
    void testHash(){

        redisTemplate.opsForHash().put("001","name","xiaoming");
        redisTemplate.opsForHash().put("001","age","18");
        redisTemplate.opsForHash().put("001","address","yn");

        String name = (String) redisTemplate.opsForHash().get("001", "name");
        System.out.println(name);

        //获取所有字段
        Set keys = redisTemplate.opsForHash().keys("001");
        for (Object key : keys) {
            System.out.println(key);
        }

        //获取所有值
        List values = redisTemplate.opsForHash().values("001");
        for (Object value : values) {
            System.out.println(value);
        }
    }

    /**
     * 操作list类型的数据
     */
    @Test
    void testList(){

        ListOperations<String, String> listOperations = redisTemplate.opsForList();

        listOperations.leftPush("mylist","a");
        listOperations.leftPushAll("mylist","b","c");

        List<String> mylist = listOperations.range("mylist", 0, -1);

        //获取
        for (Object o : mylist) {
            System.out.println(o);
        }

        //获得列表长度
        Long size = listOperations.size("mylist");
        int intSize = size.intValue();
        for (int i = 0; i < intSize; i++) {
            //出队列
            String mylist1 = listOperations.rightPop("mylist");
            System.out.println(mylist1);
        }


    }

    /**
     * 操作set类型的数据
     */
    @Test
    void testSet(){

        SetOperations<String, String> setOperations = redisTemplate.opsForSet();

        setOperations.add("myset66","a","b","c","a");

        //取值
        Set<String> myset = setOperations.members("myset66");
        for (Object o : myset) {
            System.out.println(o);
        }

        //删除
        setOperations.remove("myset66","a","b");

        //取值
        myset = setOperations.members("myset66");
        for (Object o : myset) {
            System.out.println(o);
        }
    }

    /**
     * 操作zSet类型的数据
     */
    @Test
    void testZSet(){

        ZSetOperations<String, String> stringStringZSetOperations = redisTemplate.opsForZSet();

        stringStringZSetOperations.add("myZSet","a",10.0);
        stringStringZSetOperations.add("myZSet","b",11.0);
        stringStringZSetOperations.add("myZSet","c",12.0);
        stringStringZSetOperations.add("myZSet","d",13.0);
        stringStringZSetOperations.add("myZSet","a",14.0);

        //分数从小到大输出
        Set<String> myZSet = stringStringZSetOperations.range("myZSet", 0, -1);
        for (String s : myZSet) {
            System.out.println(s);
        }

        //修改分数
        stringStringZSetOperations.incrementScore("myZSet","b",20.0);
        myZSet = stringStringZSetOperations.range("myZSet", 0, -1);
        for (String s : myZSet) {
            System.out.println(s);
        }

        //删除
        stringStringZSetOperations.remove("myZSet","a","b");
        myZSet = stringStringZSetOperations.range("myZSet", 0, -1);
        for (String s : myZSet) {
            System.out.println(s);
        }
    }

    /**
     * 通用操作，针对不同的数据类型都可以操作
     */
    @Test
    void testCommon(){

        //获取redis中的所有key
        Set<String> keys = redisTemplate.keys("*");
        for (String key : keys) {
            System.out.println(key);
        }

        //判断某个key是否存在
        Boolean itcast = redisTemplate.hasKey("itcast");
        System.out.println(itcast);

        //删除指定key
        Boolean myset = redisTemplate.delete("myset");
        System.out.println(myset);

        //获取指定key对应的value的数据类型
        DataType myset66 = redisTemplate.type("myset66");
        System.out.println(myset66);

    }
}
