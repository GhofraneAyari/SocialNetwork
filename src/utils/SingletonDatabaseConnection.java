package utils;
import java.sql.*;

public class SingletonDatabaseConnection {
	public Connection cnx ;
	private static SingletonDatabaseConnection singleton = null;
	private SingletonDatabaseConnection() {
		try{  
			Class.forName("com.mysql.jdbc.Driver");  
			cnx=DriverManager.getConnection(  
			"jdbc:mysql://localhost:3306/socialnetwork","root","");  
			
		}catch(Exception e) {
			System.out.println(e.getMessage());}
		
		
	}
	public static SingletonDatabaseConnection getInstance(){
		if (singleton == null)
		{
			singleton = new SingletonDatabaseConnection();
		}
			
		
		return singleton;
		
	}
}
