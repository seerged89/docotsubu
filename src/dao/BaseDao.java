package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DAO(オートコミット)の基底クラス
 */
public class BaseDao {

	/** JDBCドライバ名 */
	private static final String driver_name = "org.mariadb.jdbc.Driver";

	/** DBの接続先URL */
	private static final String jdbc_url = "jdbc:mariadb://192.168.64.2/docotsubu?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=GMT%2B9&rewriteBatchedStatements=true";

	/** DBへ接続時のユーザーID */
	private static final String db_user = "ichiro";

	/** DBへ接続時のパスワード */
	private static final String db_pass = "********";//need to fix

	/** DBへの接続 */
	protected Connection conn = null;

	/**
	 * DBへ接続する
	 */
	protected void open() throws ClassNotFoundException, SQLException {
		System.out.println(this.getClass().getSimpleName() + "#open");

		Class.forName(driver_name);
		conn = DriverManager.getConnection(jdbc_url, db_user, db_pass);
//		conn.setAutoCommit(true);
	}

	/**
	 * DBへの接続を終了する
	 */
	protected void close() throws SQLException {
		System.out.println(this.getClass().getSimpleName() + "#close");

		if (conn != null) {
			conn.close();
			conn = null;
		}
	}
}
