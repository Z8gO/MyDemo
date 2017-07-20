package domain;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;

public class Customer implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String sort;
	
	private String name;
	
	private String sex;
	
	private String age;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}
	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	
	
	
	public Customer(String s) {
		String[] sArr=s.split(":");
		if (sArr.length!=3){
			this.name=null;
			this.sex=null; 
			this.age=null;
		}else{
    		this.name = sArr[0];
    		this.sex = sArr[1];
    		this.age = sArr[2];
		}
	}

}
