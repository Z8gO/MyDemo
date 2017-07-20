package test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSON;

import redis.clients.jedis.ShardedJedis;
import util.ConnectionUtil;
import domain.Customer;

public class TestRedis {

	public static void main(String[] args) {
		ShardedJedis shardedJedis = ConnectionUtil.getShardedJedis();
		Set<String> custSet = listTest(shardedJedis, "G");
		List<Customer> list = new ArrayList<Customer>();
		for (String key : custSet) {
			String custInfo = getCustInfo(shardedJedis, key);
			StringBuilder strBox = new StringBuilder(key).append(":").append(custInfo);
			list.add(new Customer(strBox.toString()));
		}

		String jsonStr = JSON.toJSONString(list);
		System.out.println(jsonStr);
	}

	/**
	 * @desc 有序Set列表的操作
	 * @param shardedJedis
	 * @author zhanghuang
	 */
	public static Set<String> listTest(ShardedJedis shardedJedis, String userName) {
		//在有序的集合中。得到user这个用户的下标，，zrevrank  和 zrank 的区别在于前者对集合进行了排序
		Long rankNum = shardedJedis.zrevrank("test", userName);
		rankNum = rankNum == null ? 0 : rankNum;
		Long start = 0l;
		Long end = 11l;
		if (rankNum > 5) {
			start = rankNum - 5l;
			end = rankNum + 5l;
		}
		//注意这里使用的是zrevrange
		Set<String> zrange = shardedJedis.zrevrange("test", start.intValue(), end.intValue());
		return zrange;
	}

	//客户信息存储在无序的Hash中
	//获取客户信息
	public static String getCustInfo(ShardedJedis shardedJedis, String field) {
		return shardedJedis.hget("maptest", field);
	}

	public static void listTest1(ShardedJedis shardedJedis) {
		/*shardedJedis.zadd("test", 3.23, "B");
		shardedJedis.zadd("test", 5.72, "D");
		shardedJedis.zadd("test", 7.93, "F");
		shardedJedis.zadd("test", 4.41, "C");
		shardedJedis.zadd("test", 6.47, "E");
		shardedJedis.zadd("test", 8.25, "G");
		shardedJedis.zadd("test", 3.1, "A");
		shardedJedis.zadd("test", 10.64, "I");
		shardedJedis.zadd("test", 11.46, "J");
		shardedJedis.zadd("test", 9.84, "H");
		shardedJedis.zadd("test", 17.54, "P");
		shardedJedis.zadd("test", 12.27, "K");
		shardedJedis.zadd("test", 13.26, "L");
		shardedJedis.zadd("test", 14.12, "M");
		shardedJedis.zadd("test", 20.46, "S");
		shardedJedis.zadd("test", 16.21, "O");
		shardedJedis.zadd("test", 19.34, "R");
		shardedJedis.zadd("test", 15.63, "N");
		shardedJedis.zadd("test", 18.53, "Q");
		shardedJedis.zadd("test", 20.62, "T");*/
		Map<String, String> map = new HashMap<String, String>();
		map.put("A", "男:67");
		map.put("B", "女:23");
		map.put("C", "男:13");
		map.put("D", "女:35");
		map.put("E", "男:93");
		map.put("F", "男:13");
		map.put("G", "女:38");
		map.put("H", "男:13");
		map.put("I", "男:13");
		map.put("J", "女:35");
		map.put("K", "男:21");
		map.put("L", "女:13");
		map.put("M", "男:46");
		map.put("N", "女:45");
		map.put("O", "男:12");
		map.put("P", "女:34");
		map.put("Q", "男:13");
		map.put("R", "女:24");
		map.put("S", "男:13");
		map.put("T", "女:34");

		shardedJedis.hmset("maptest", map);
	}

	/**
	 * @desc  无序Hash列表
	 * @param shardedJedis
	 */
	public static Map<Integer, String> mapTest(ShardedJedis shardedJedis) {
		/*map.put("S", "男:13");
		shardedJedis.hmset("maptest", map);*/
		Map<String, String> hgetAll = shardedJedis.hgetAll("maptest");

		Set<String> keySet = hgetAll.keySet();
		Iterator<String> iterator = keySet.iterator();
		Map<Integer, String> custInfo = new HashMap<Integer, String>();
		while (iterator.hasNext()) {
			String key = iterator.next();
			custInfo.put(Integer.parseInt(key), hgetAll.get(key));
		}
		return custInfo;
		/*String string = hgetAll.get("zh1");
		System.out.println(string);
		StopWatch watch = new StopWatch();
		watch.start();
		watch.stop();
		System.out.println("执行时间:"+watch.getTime()+"毫秒");*/
	}

	static Comparator<Integer> c = new Comparator<Integer>() {
			 @Override
			 public int compare(Integer x, Integer y) {
				 if (x.intValue() > y.intValue()) {
					 return 1;
				 } else {
					 return -1;
				}
			}
		};

	public static List<Customer> bulidCustList(int index, List<Integer> keys, Map<Integer, String> infos) {
		List<Customer> custList = new ArrayList<Customer>();
		int keyNum = keys.size();
		if (keyNum <= 11 || index <= 6) {
			for (int i = 0; i < keyNum; i++) {
				StringBuilder strBox = new StringBuilder(infos.get(keys.get(i))).append(":").append(keys.get(i));
				Customer c = new Customer(strBox.toString());
				custList.add(c);
			}
		} else if (keyNum - index <= 5) {
			for (int j = keyNum - 11; j < keyNum; j++) {
				StringBuilder strBox = new StringBuilder(infos.get(keys.get(j))).append(":").append(keys.get(j));
				Customer c = new Customer(strBox.toString());
				custList.add(c);
			}
		} else {
			int i = index - 5 - 1;
			for (int j = i; j <= i + 10; j++) {
				StringBuilder strBox = new StringBuilder(infos.get(keys.get(j))).append(":").append(keys.get(i));
				Customer c = new Customer(strBox.toString());
				custList.add(c);
			}
		}
		return custList;
	}

}
