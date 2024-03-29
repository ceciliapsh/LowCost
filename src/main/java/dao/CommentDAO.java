package dao;

import model.Comment;

import java.sql.*;

public class CommentDAO extends DAO {

	public CommentDAO() { super(); connect(); }

	public void finalize() { close(); }

	public boolean insert(Comment comment) {

		boolean status = false;

		try {

			String sql = "INSERT INTO \"public\".comment (content, user_id_fk, service_id_fk, forum_id_fk, sentiment, submission) VALUES ('"
						 + comment.getContent() + "', "
						 + comment.getUserId() + ", "
						 + comment.getServiceId() + ", "
						 + comment.getForumId() + ", "
						 + comment.getSentiment() + ", ?);";
						 
			PreparedStatement st = connection.prepareStatement(sql);

			st.setTimestamp(1, Timestamp.valueOf(comment.getSubmission()));
			st.executeUpdate();
			st.close();

			status = true;

		} catch (SQLException e	) { throw new RuntimeException(e); }

		return status;
	}
	
	public boolean insertCommentService(Comment comment) {

		boolean status = false;

		try {

			String sql = "INSERT INTO \"public\".comment (content, user_id_fk, service_id_fk, sentiment, forum_id_fk, submission) VALUES ('"
						 + comment.getContent() + "', "
						 + comment.getUserId() + ", "
						 + comment.getServiceId() + ", "
						 + comment.getSentiment() + ", NULL, ?);";

			PreparedStatement st = connection.prepareStatement(sql);

			st.setTimestamp(1, Timestamp.valueOf(comment.getSubmission()));
			st.executeUpdate();
			st.close();

			status = true;

		} catch (SQLException e	) { throw new RuntimeException(e); }

		return status;
	}
	
	public boolean insertCommentForum(Comment comment) {

		boolean status = false;

		try {

			String sql = "INSERT INTO \"public\".comment (content, user_id_fk, service_id_fk, sentiment, forum_id_fk, submission) VALUES ('"
						 + comment.getContent() + "', "
						 + comment.getUserId() + ", "
						 + "NULL, NULL, "
						 + comment.getForumId() + ", ?);";

			PreparedStatement st = connection.prepareStatement(sql);

			st.setTimestamp(1, Timestamp.valueOf(comment.getSubmission()));
			st.executeUpdate();
			st.close();

			status = true;

		} catch (SQLException e	) { throw new RuntimeException(e); }

		return status;
	}

	public Comment get(int user_id, int service_id, int forum_id) {

		Comment comment = null;

		try {

			Statement st = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

			String sql = "SELECT * FROM \"public\"comment WHERE user_id_fk = " + user_id
						 + " AND service_id_fk = " + service_id
						 + " AND forum_id_fk = " + forum_id;

			ResultSet rs = st.executeQuery(sql);

			if (rs.next()) {

				comment = new Comment(rs.getString("content"),
									  rs.getTimestamp("submission").toLocalDateTime(),
									  rs.getInt("service_id_fk"),
									  rs.getInt("forum_id_fk"),
									  rs.getInt("user_id_fk"),
									  rs.getFloat("sentiment"));
			}

			st.close();

		} catch (Exception e) { System.err.println(e.getMessage()); }

		return comment;
	}

	public boolean update(Comment comment) {

		boolean status = false;

		try {

			String sql = "UPDATE \"public\".comment SET content = '" + comment.getContent()
						  + "', submission = ?"
						  + " WHERE user_id_fk = " + comment.getUserId()
						  + " AND service_id_fk = " + comment.getServiceId()
						  + " AND forum_id_fk = " + comment.getForumId();

			PreparedStatement st = connection.prepareStatement(sql);
			
			st.setTimestamp(1, Timestamp.valueOf(comment.getSubmission()));
			st.executeUpdate();
			st.close();

			status = true;

		} catch (SQLException e) { throw new RuntimeException(e); }

		return status;
	}

	public boolean delete(int user_id, int service_id, int forum_id) {

		boolean status = false;

		try {
				
			Statement st = connection.createStatement();
			
			st.executeUpdate("DELETE FROM \"public\".comment WHERE user_id = " + user_id
							 + " AND service_id_fk = " + service_id
							 + " AND forum_id_fk = " + forum_id);
			st.close();

			status = true;

		} catch (SQLException e) { throw new RuntimeException(e); }

		return status;
	}
}