package util;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.dbcp.BasicDataSource;

/*
 * 使用当前类来管理数据库连接
 */
public class DBUtil {

	/*
	 * ThreadLocal内部是一个Map
	 * key是某个线程，而value是要保存的值
	 * 这样可以讲某个值绑定到一个线程上，随时通过该线程获取这个值
	 */
	private static ThreadLocal<Connection> tl;
	/*
	 * 数据库连接池
	 */
	private static BasicDataSource ds;
	static{
		//加载配置文件
		try{
			tl = new ThreadLocal<Connection>();
			Properties prop = new Properties();
			prop.load(DBUtil.class.getClassLoader().getResourceAsStream("config.properties"));
			String driver = prop.getProperty("driver");
			String url = prop.getProperty("url");
			String username = prop.getProperty("username");
			String password = prop.getProperty("password");
			//连接池最大连接数
			int maxActive = Integer.parseInt(prop.getProperty("maxActive"));
			//当没有可用连接时的最大等待时间
			int maxWait = Integer.parseInt(prop.getProperty("maxWait"));
//			System.out.println(driver);
//			System.out.println(url);
//			System.out.println(username);
//			System.out.println(password);
//			System.out.println(maxActive);
//			System.out.println(maxWait);
			ds = new BasicDataSource();
			ds.setDriverClassName(driver);
			ds.setUrl(url);
			ds.setUsername(username);
			ds.setPassword(password);
			//连接池的最大连接数（池内连接的最大数量）
			ds.setMaxActive(maxActive);
			//获取连接时的最大等待时间
			ds.setMaxWait(maxWait);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/*
	 * 获取一个数据库连接
	 */
	public static synchronized Connection getConnection() throws ClassNotFoundException, SQLException{
//		//加载驱动
//		Class.forName(driver);
//		//获取连接
//		Connection conn = DriverManager.getConnection(url, username, password);
		/*
		 * 将连接设置到ThreadLocal中
		 * 设置的时候是将调用当前方法的线程作为key，set方法传入的参数作为value
		 * 存入到ThreadLocal内部的Map中保存
		 */
		/*
		 * 向连接池要连接。若连接池没有可用连接时
		 * 该方法会进入阻塞状态，阻塞时间有maxWait设置的最大等待时间决定
		 * 在等待过程中一旦有可用连接，那么连接池会立刻返回。若等待时间经过
		 * 依然没有可用连接时，该方法抛出超时异常。
		 */
		Connection conn = ds.getConnection();
		tl.set(conn);
		return conn;
	}
	/*
	 * 关闭数据库连接
	 */
	public static void closeConnection(Connection conn){
		if(conn!=null){
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	public static void closeConnection(){
		/*
		 * 哪个线程调用ThreadLocal的get方法，
		 * 就用这个线程作为key去ThreadLocal内部的Map中取出对应的value
		 */
		Connection conn = tl.get();
		if(conn!=null){
			try {
				conn.close();
				tl.remove();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
//	public static void main(String[] args) throws ClassNotFoundException, SQLException {
//		Connection conn = DBUtil.getConnection();
//		System.out.println(conn);
//	}
	
}
