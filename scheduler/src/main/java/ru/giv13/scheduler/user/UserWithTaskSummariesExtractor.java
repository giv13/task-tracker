package ru.giv13.scheduler.user;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserWithTaskSummariesExtractor implements ResultSetExtractor<List<User>> {
    @Override
    public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Integer, User> users = new HashMap<>();
        while (rs.next()) {
            Integer userId = rs.getInt("user_id");
            User user = users.get(userId);
            if (user == null) {
                user = new User();
                user.setId(userId);
                user.setName(rs.getString("user_name"));
                user.setEmail(rs.getString("user_email"));
                users.put(userId, user);
            }
            User.Task task = new User.Task();
            task.setId(rs.getInt("id"));
            task.setName(rs.getString("name"));
            task.setNotes(truncateText(rs.getString("notes")));
            if (rs.getBoolean("is_done")) {
                user.getCompleted().add(task);
            } else {
                user.getUncompleted().add(task);
            }
        }
        return new ArrayList<>(users.values());
    }

    private String truncateText(String text) {
        if (text == null) {
            return "";
        }
        String plainText = text.replaceAll("<[^>]+>", "");
        if (plainText.length() > 180) {
            return plainText.substring(0, 180 - 3) + "...";
        }
        return plainText;
    }
}
