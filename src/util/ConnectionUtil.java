package util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;



import java.util.Set;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

public class ConnectionUtil {
	private static final String SERVER1="192.168.60.29";
	private static final int PORT1=6379;
	
	
	/**
	 * @desc 集群方式
	 * @return
	 * @return:ShardedJedis
	 */
	public static ShardedJedis getShardedJedis(){
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxIdle(5);
		config.setMaxWaitMillis(1000l);
		config.setTestOnBorrow(false);
		List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
		shards.add(new JedisShardInfo(SERVER1, PORT1));
		ShardedJedisPool shardedJedisPool = new ShardedJedisPool(config,shards);
		ShardedJedis shardedJedis = shardedJedisPool.getResource();
		return shardedJedis;
	}
	
	/**
	 * @desc 集群方式
	 * @return
	 * @return:JedisCluster
	 */
	public static JedisCluster getConns(){
		Set<HostAndPort> nodes=new HashSet<HostAndPort>();
		nodes.add(new HostAndPort(SERVER1, PORT1));
		JedisCluster j=new JedisCluster(nodes);
		return j;
	}


}
