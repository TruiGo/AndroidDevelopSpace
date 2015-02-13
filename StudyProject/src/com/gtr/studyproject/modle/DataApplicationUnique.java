package com.gtr.studyproject.modle;

/**
 * 
 * @author xiaotian
 * @information 程序单例数据对象
 */
public class DataApplicationUnique {
	private static DataApplicationUnique dau;

	/**
	 * 单例控制构造器
	 */
	private DataApplicationUnique() {

	}

	/**
	 * 
	 * @return 单例实例对象
	 */
	public static synchronized DataApplicationUnique getInstance() {
		if (dau == null)
			dau = new DataApplicationUnique();
		return dau;
	}
}
