package dao;

import db.DBConnection;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CrudUtil {
    public static <T> T execute(String sql, Object...params) throws SQLException {
        PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement(sql);
        int i = 0;
        for (Object param : params) {
            i++;
            preparedStatement.setObject(i,param);
        }
        if(sql.startsWith("SELECT")){
            return (T) preparedStatement.executeQuery();
        }
        return (T) (Boolean) (preparedStatement.executeUpdate()>0);
    }
}
