package service;

import dao.*;
import model.Comment;
import spark.Request;
import spark.Response;

import java.time.LocalDateTime;

import com.azure.ai.textanalytics.*;
import com.azure.ai.textanalytics.models.DocumentSentiment;
import com.azure.ai.textanalytics.models.SentimentConfidenceScores;
import com.azure.core.credential.AzureKeyCredential;

public class CommentService {
	
	private TextAnalyticsClient client = new TextAnalyticsClientBuilder()
            .credential(new AzureKeyCredential("ca8de3c4ea004787a0ed003953975170"))
            .endpoint("https://textrecognitionjava.cognitiveservices.azure.com/")
            .buildClient();
	
	private CommentDAO commentDao = new CommentDAO();
	private UserDAO userDao = new UserDAO();
	private ServiceDAO serviceDao = new ServiceDAO();
	private ForumDAO forumDao = new ForumDAO();
	
	public CommentService() {}
	
	public boolean insert(Request request, Response response) {
		
		String content = request.queryParams("comment");
		LocalDateTime submission = LocalDateTime.now();
		int userId = userDao.getId((String)request.session().raw().getAttribute("login"));
		
		if (request.queryParams("forum") == null) {
		
			int serviceId = serviceDao.getId((String)request.queryParams("description"));
			
			final DocumentSentiment documentSentiment = client.analyzeSentiment(content);
        	
	        SentimentConfidenceScores scores = documentSentiment.getConfidenceScores();
			
			Comment comment = new Comment(content, submission, serviceId, 0, userId, (float)scores.getPositive());
			
			return commentDao.insertCommentService(comment);
		}
			
		else if (request.queryParams("description") == null) {
			
			int forumId = forumDao.get((String)request.queryParams("forum"));
			
			Comment comment = new Comment(content, submission, 0, forumId, userId, 0);
			
			return commentDao.insertCommentForum(comment);
		}
		
		return false;
	}

}
