package dao;

import model.Forum;

import java.sql.*;

public class ForumDAO extends DAO {
		
	public ForumDAO() { super(); connect(); }

	public void finalize() { close(); }

	public boolean insert(Forum forum) {

		boolean status = false;

		try {

			String sql = "INSERT INTO forum (comments, title, user_id_fk) VALUES ("
						 + forum.getComments() + ", '"
						 + forum.getTitle() + ", "
						 + forum.getUserId() + ");";

			PreparedStatement st = connection.prepareStatement(sql);

			st.executeUpdate();
			st.close();

			status = true;

		} catch (SQLException e) { throw new RuntimeException(e); }

		return status;
	}

	public int get(String name) {

		int id = 0;

		try {

			Statement st = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

			String sql = "SELECT * FROM forum WHERE name = '" + name + "';";

			ResultSet rs = st.executeQuery(sql);

			if (rs.next())
				id = rs.getInt("id");

			st.close();

		} catch (Exception e) { System.err.println(e.getMessage()); }

		return id;
	}

	public boolean update(Forum forum) {

		boolean status = false;

		try {

			String sql = "UPDATE forum SET comments = " + forum.getComments()
						 + ", title = '" + forum.getTitle()
						 + "', user_id_fk = " + forum.getUserId()
						 + " WHERE id = " + forum.getId();

			PreparedStatement st = connection.prepareStatement(sql);

			st.executeUpdate();
			st.close();

			status = true;

		} catch (SQLException e) { throw new RuntimeException(e); }

		return status;
	}

	public boolean delete(int id) {

		boolean status = false;

		try {

			Statement st = connection.createStatement();

			st.executeUpdate("DELETE FROM forum WHERE id = "  + id);
			st.close();

			status = true;

		} catch (SQLException e) { throw new RuntimeException(e); }

		return status;
	}
}