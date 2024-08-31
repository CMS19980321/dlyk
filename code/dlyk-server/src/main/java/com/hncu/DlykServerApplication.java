package com.hncu;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hncu.model.TUser;
import jakarta.annotation.Resource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

@MapperScan(basePackages = {"com.hncu.mapper"})
@SpringBootApplication
public class DlykServerApplication implements CommandLineRunner {

	public static final Map<String,Object> casheMap = new HashMap<>();
	@Resource
	private RedisTemplate<String,Object> redisTemplate;

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(DlykServerApplication.class, args);
		// jdk提供的线程池:import java.util.concurrent.Executor;
		String[] beanNamesForType = context.getBeanNamesForType(Executor.class);
		for (String s : beanNamesForType) {
			System.out.println(s);
		}

		Object object = context.getBean("applicationTaskExecutor");
		System.out.println(object);
	}

	@Override
	public void run(String... args) throws Exception {
		//redis序列化

		//设置key序列化
		redisTemplate.setKeySerializer(new StringRedisSerializer());

		//对象映射工具，java对象与json对象相互转换
		ObjectMapper mapper = new ObjectMapper();
		//设置可见性
		mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		//激活类型
		mapper.activateDefaultTyping(mapper.getPolymorphicTypeValidator(),ObjectMapper.DefaultTyping.EVERYTHING);
		//设置value序列化
		redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<Object>(mapper,Object.class));

		//设置HashKey序列化
		redisTemplate.setHashKeySerializer(new StringRedisSerializer());

		//设置HashValue序列化
		redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<Object>(mapper,Object.class));




	}
}
