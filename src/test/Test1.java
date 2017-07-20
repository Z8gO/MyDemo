package test;

import com.alibaba.fastjson.JSON;

import domain.Customer;

public class Test1 {
	public static void main(String[] args) {
		/*List<Integer> s = new ArrayList<Integer>();
		s.add(1);
		s.add(5);
		s.add(8);
		s.add(2);

		Comparator c = new Comparator<Integer>() {
			@Override
			public int compare(Integer x, Integer y) {
				if (x.intValue() > y.intValue()) {
					return 1;
				} else {
					return -1;
				}
			}

		};

		s.sort(c);
		for (Integer i : s) {
			System.out.println(i.intValue());
		}*/
		Customer c=new Customer("12");
		System.out.println(JSON.toJSONString(c));
		
		
	}
}
