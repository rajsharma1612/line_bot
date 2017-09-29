package line.bot;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class DBManager {

	ArrayList<String> ad_accounts_list = new ArrayList<>();

	public ArrayList<String> get_all_adAccounts() {
		ResultSet rs = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ibiza_development", "root", "");
			Statement stmt = con.createStatement();
			rs = stmt.executeQuery("select * from advertisers where media_type = 'line' and deleted_at is null");
			while (rs.next()) {
				ad_accounts_list.add(rs.getString(13));
				break;
			}
			// System.out.println(rs.getInt(1)+","+rs.getString(13));
			con.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		return ad_accounts_list;
	}
}
