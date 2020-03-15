package com.traffic.jedis;

import java.util.Properties;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisPoolUtils {
	private static JedisPool jedisPool = null;
	static {
		Properties prop = InitProperties.getProperties();
		JedisPoolConfig jPoolConfig = new JedisPoolConfig();

		jPoolConfig.setMaxIdle(Integer.parseInt(prop.getProperty("redis.maxIdle")));
		jPoolConfig.setMaxTotal(Integer.valueOf(prop.getProperty("redis.maxTotal")));
		jPoolConfig.setMinIdle(Integer.valueOf(prop.getProperty("redis.minIdle")));
		jedisPool = new JedisPool(prop.getProperty("redis.url"), Integer.valueOf(prop.getProperty("redis.port")));
	}
	
	public static Jedis getJedis() {
		return jedisPool.getResource();
	}
	
	public static void closeJedisResource(Jedis jedis) {
		if(jedis != null) {
			jedis.close();
		}
	}
	
	public static void main(String[] args) {
		Jedis jedis = getJedis();
		jedis.set("username", "wkx");
		System.out.println(jedis.get("username"));
	}
	
	
	
	
	
}
