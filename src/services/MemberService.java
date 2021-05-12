package services;

import models.Member;
import utils.Cache;
import utils.ConsoleColors;
import utils.Password;
import utils.SingletonDatabaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Scanner;


public class MemberService {

    public static Member currentUser = new Member();

    public Boolean login(String login, String password) {

        try {
            Connection cnx = SingletonDatabaseConnection.getInstance().cnx;
            Statement stmt = cnx.createStatement();
            ResultSet rs = stmt.executeQuery("select * from user where login='" + login + "';");

            if (rs.next()) {
                String hashedPwd = rs.getString("password");
                if (!Password.checkPassword(password, hashedPwd)) {
                    System.out.println("password incorrect");
                    return false;
                }
                currentUser.setMember_id(rs.getInt(1));
                currentUser.setFirstName(rs.getString(2));
                currentUser.setLastName(rs.getString(3));
                Cache.member = currentUser;
                return true;

            }

        } catch (Exception e) {
            System.out.println(e.getMessage());


        }

        return false;


    }

    public Boolean insert(Member member) {


        try {


            Connection cnx = SingletonDatabaseConnection.getInstance().cnx;
            //use prepared statement to prevent sql injection
            Statement stmt = cnx.createStatement();
            member.setPassword(Password.hashPassword(member.getPassword()));
            stmt.executeUpdate("INSERT INTO user (firstName, lastName, birthday, gender, country, city, login, password)" + " VALUES (" + "\"" + member.getFirstName() + "\"" + "," + "\"" + member.getLastName() + "\"" + "," + "\"" + member.getBirthday() + "\"" + "," + "\"" + member.getGender() + "\"" + "," + "\"" + member.getCountry() + "\"" + ", " + "\"" + member.getCity() + "\"" + ", " + "\"" + member.getLogin() + "\"" + ", " + "\"" + member.getPassword() + "\"" + ");");
            System.out.println("Your account has been created successfully! \n");


        } catch (Exception e) {
            System.out.println(e.getMessage());


        }

        return false;

    }


    public void sendFriendRequest() {
        try {
            Connection cnx = SingletonDatabaseConnection.getInstance().cnx;
            Statement stmt = cnx.createStatement();

            ResultSet rs = stmt.executeQuery("select u.userId, u.firstName, u.lastName, u.birthday, u.city "
                    + "from user u "
                    + "where NOT EXISTS ( SELECT 1 " +
                    "                      FROM relationship r" +
                    "                     WHERE r.sender = u.userId" +
                    "                       AND r.receiver = " + currentUser.getMember_id() +
                    "                  )" +
                    "   AND NOT EXISTS ( SELECT 1\n" +
                    "                      FROM relationship g" +
                    "                     WHERE g.receiver = u.userId" +
                    "                       AND g.sender = " + currentUser.getMember_id() +
                    "                  )" +
                    "   AND u.userId <> " + currentUser.getMember_id() + ";");
            System.out.println("Here is the list of members available on the network:\n");
            System.out.println("ID" + "\t" + "First Name" + "\t" + "Last Name" + "\t" + "Birthday" + "\t " + "City");

            int count = 0;

            while (rs.next()) {
                count++;
                System.out.println(rs.getInt(1) + "\t " + rs.getString(2) + "\t \t " + rs.getString(3) + "\t \t " + rs.getDate(4) + "\t " + rs.getString(5));
            }
            if (count == 0) {

                System.out.println(ConsoleColors.ACC_COLOR+"There are not any members to send friends request to!"+ConsoleColors.MENU_COLOR);
            } else {

                System.out.println("Would you like to send a friend request? (Y/N)");
                Scanner input = new Scanner(System.in);
                String yesNo = input.nextLine();
                if (yesNo.equals("Y") || yesNo.equals("y")) {
                    System.out.println("Enter the ID of the member you would like to send a friend request to...");
                    input = new Scanner(System.in);
                    int response = input.nextInt();
                    Statement stmt2 = cnx.createStatement();
                    int test = stmt2.executeUpdate("INSERT INTO relationship(sender, receiver, status) VALUES(" + currentUser.getMember_id() + "," + response + "," + 0 + ");");
                    if (test > 0) {
                        System.out.println(ConsoleColors.ACC_COLOR+"Your friend request was sent successfully!"+ConsoleColors.MENU_COLOR);
                    }
                } else {
                    System.out.println("Maybe another time!");
                }

            }

            System.out.println("\n");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void acceptDenyFriendRequest() {

        try {
            Connection cnx = SingletonDatabaseConnection.getInstance().cnx;
            Statement stmt = cnx.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT relationshipId, user.firstName, user.lastName, status from relationship, user WHERE relationship.sender = user.userId and status=0 and receiver =" + currentUser.getMember_id());
            Scanner input = new Scanner(System.in);
            if (rs.next() == false) {
                System.out.println(ConsoleColors.ACC_COLOR + "You do not have any pending friend requests!\n" + ConsoleColors.MENU_COLOR);

            } else {
                System.out.println("ID" + "     " + "Sender Name" + "       " + "Status");
                do {
                    System.out.println(rs.getInt(1) + "      " + rs.getString(2) + " " + rs.getString(3) + "  \t " + "Pending..");
                } while (rs.next());

                System.out.println("Please enter the ID of the friend request you would like to approve or deny: ");
                int reqID = input.nextInt();
                System.out.println("Would you like to approve or deny this friend request? (1: Approve  2: Deny)");
                int response = input.nextInt();
                Statement stmt2 = cnx.createStatement();
                int test = stmt2.executeUpdate("Update relationship set status = " + response + " where relationshipId=" + reqID);
                if (test > 0 && response == 1) {
					System.out.println(ConsoleColors.ACC_COLOR + "This invitation has been approved." + ConsoleColors.MENU_COLOR);

                } else {
                    if (test > 0 && response == 2) {
                        System.out.println(ConsoleColors.ACC_COLOR + "This invitation has been declined." + ConsoleColors.MENU_COLOR);
                    }

                }

            }


        } catch (Exception e) {
            // TODO: handle exception
        }


    }


    public void membersFriends() {
        try {
            Connection cnx = SingletonDatabaseConnection.getInstance().cnx;
            ResultSet rs;
            Statement stmt = cnx.createStatement();
            String req = "SELECT user.userId, user.firstName, user.lastName FROM user, relationship WHERE (relationship.status =1 AND ((relationship.receiver = user.userId AND relationship.sender= " + Cache.member.getMember_id() + ") OR (relationship.receiver= " + Cache.member.getMember_id() + " AND relationship.sender = user.userId ))) ;";
            rs = stmt.executeQuery(req);
            System.out.println(" ID " + "\t \t" + "First Name " + "\t \t" + "Last Name ");
            while (rs.next()) {
                System.out.println(" " + rs.getInt(1) + "\t \t  " + rs.getString(2) + "\t \t  " + rs.getString(3));
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void friendsNotAdminInGroup(int idGroup) {
        try {
            Connection cnx = SingletonDatabaseConnection.getInstance().cnx;
            ResultSet rs;
            Statement stmt = cnx.createStatement();
            String req = "SELECT user.userId, user.firstName, user.lastName FROM user, relationship WHERE (relationship.status =1 AND ((relationship.receiver = user.userId AND relationship.sender= " + Cache.member.getMember_id() + ") OR (relationship.receiver= " + Cache.member.getMember_id() + " AND relationship.sender = user.userId" +
                    " AND NOT EXISTS (SELECT 1 FROM groupmanager WHERE groupId = " + idGroup + " AND 	userId = " + Cache.member.getMember_id() + " ) ))) ;";

            rs = stmt.executeQuery(req);
            while (rs.next()) {
                System.out.println(rs.getInt(1) + "\t \t " + rs.getString(2) + "\t \t " + rs.getString(3));
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //add message on the wall
    public int addToWall(int userWallId, String content) {
        Connection cnx = SingletonDatabaseConnection.getInstance().cnx;
        try (Statement stmt1 = cnx.createStatement()) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            java.util.Date utilDate = new java.util.Date();
            String dateTime = format.format(utilDate.getTime());
            return stmt1.executeUpdate("INSERT INTO post(creator, wall_user_id, creationTime, content) " +
                    "VALUES(" + Cache.member.getMember_id() + ", " + userWallId + " ,  '" + dateTime + "', '" + content + "');");
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return 0;
    }

    public void consultFriendTimelineById(int userwallId) {
        try {
            Connection cnx = SingletonDatabaseConnection.getInstance().cnx;
            ResultSet rs;
            Statement stmt = cnx.createStatement();
            String req = "SELECT post.creationTime, user.firstName, user.lastName, post.content FROM post, user WHERE user.userId = post.creator AND post.wall_user_id = " + userwallId;
            rs = stmt.executeQuery(req);
            int count = 0;
            System.out.println("  Date" + "\t \t  \t " + "  Sender" + "\t  \t  \t" + "Message");
            while (rs.next()) {
                count++;
                System.out.println(rs.getDate(1) + "\t \t " + rs.getString(2) + " " + rs.getString(3) + "\t \t " + rs.getString(4));
            }
            if (count == 0) {
                System.out.println(ConsoleColors.WARNING_COLOR+"Sorry, this timeline is empty!\n"+ConsoleColors.MENU_COLOR	);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}