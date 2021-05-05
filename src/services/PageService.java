package services;

import java.util.Scanner;


import models.Page;
import utils.Cache;
import utils.SingletonDatabaseConnection;

import java.sql.*;

public class PageService {
    public void likePage() {
        try {

            Connection cnx = SingletonDatabaseConnection.getInstance().cnx;
            Statement stmt2 = cnx.createStatement();
            //	Select the existing pages to like (Not liked yet)
            ResultSet rs = stmt2.executeQuery("select p.pageId, p.pageName, p.pageCreationDate, p.pageCreator, p.genre "
                    + "from pages p "
                    + "where NOT EXISTS ( SELECT 1 " +
                    "                      FROM pagemembers pm" +
                    "                     WHERE pm.userId = " + Cache.member.getMember_id() +
                    "                       AND pm.pageId = p.pageId" +
                    "                  ) " + ";");
            System.out.println("Here is the list of pages available to you:\n");
            System.out.println("ID " + "Page Name" + "  " + "Creation Date" + "    " + "Genre");
            while (rs.next()) {
                System.out.println(" " + rs.getInt(1) + "   " + rs.getString(2) + "      " + rs.getDate(3) + "     " + rs.getString(4));
            }
            System.out.println("---------------------------------------");
            System.out.println("Do you want to like a page? (Y/N)");
            Scanner input = new Scanner(System.in);
            String yesNo = input.nextLine();
            if (yesNo.equals("Y") || yesNo.equals("y") || yesNo.equals("yes") || yesNo.equals("YES")) {
                System.out.println("Enter the ID of the page that you want to like...");
                int response = input.nextInt();
                Statement stmt3 = cnx.createStatement();
                int test = stmt3.executeUpdate("INSERT INTO pagemembers( pageId , userID ) VALUES(" + response + "," + MemberService.currentUser.getMember_id() + ");");
                if (test > 0) {
                    System.out.println("You have joined the page number " + response + " successfully!");
                }


            } else {

                System.out.println("Maybe next time!");
            }


        } catch (Exception e) {
            System.out.println(e.getMessage());

        }


    }

//    public void addAdmin(int idUser, int idPage) throws SQLException {
//        Connection cnx= SingletonDatabaseConnection.getInstance().cnx;
//        try (Statement stmt1 = cnx.createStatement()) {
//            stmt1.executeUpdate("INSERT INTO pagemanager( pageId , userId ) " +
//                    "VALUES(" + idPage + "," + idUser + ");");
//        }
//
//    }

    public void addOtherAdmin() {
        try {

            Connection cnx = SingletonDatabaseConnection.getInstance().cnx;
            Statement stmt1 = cnx.createStatement();
            Statement stmt2 = cnx.createStatement();
            ResultSet rs1 = stmt1.executeQuery("select p.pageId, p.pageName, p.pageCreationDate, p.genre "
                    + "from pages p "
                    + "where EXISTS ( SELECT 1 " +
                    "                      FROM pagemanager pm" +
                    "                     WHERE pm.userId = " + Cache.member.getMember_id() +
                    "                       AND pm.pageId = p.pageId" +
                    "                  ) " + ";");
            System.out.println("Here is the list of pages you manage:\n");
            System.out.println("ID " + "Page Name" + "  " + "Creation Date" + "    " + "Genre");
            while (rs1.next()) {
                System.out.println(" " + rs1.getInt(1) + "   " + rs1.getString(2) + "      " + rs1.getDate(3) + "     " + rs1.getString(4));
            }
            System.out.println("---------------------------------------");
            ResultSet rs2 = stmt2.executeQuery("select userId, firstName, lastName, birthday, gender, country, city "
                    + "from user "
                    + "where userId != " + Cache.member.getMember_id() + " ;");
            System.out.println("Here is the list of users you can add as managers:\n");
            System.out.println("ID " + "firstName" + "  " + "lastName" + "    " + "Birthday" + "     " + "Gender" + "  " + "Country" + "    " + "City");
            while (rs2.next()) {
                System.out.println(" " + rs2.getInt(1) + "   " + rs2.getString(2) + "      " + rs2.getString(3) + "     " + rs2.getDate(5) + "      " + rs2.getString(6) + "     " + rs2.getString(7) + "     " + rs2.getString(8));
            }
            System.out.println("---------------------------------------");
            System.out.println("Enter the ID of the page you chose...");
            Scanner input = new Scanner(System.in);
            int pageid = input.nextInt();
            System.out.println("Enter the ID of the user you want to manage your page ...");
            int userid = input.nextInt();
            Statement stmt3 = cnx.createStatement();
            int test = stmt3.executeUpdate("INSERT INTO pagemanager( pageId , userId ) VALUES(" + pageid + "," + userid + ");");
            if (test > 0) {
                System.out.println("User with ID " + userid + " is now admin to page with ID " + pageid);
            } else {

                System.out.println("Maybe next time!");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());

        }
    }

    public void deleteAdmin() {
        try {
            Connection cnx = SingletonDatabaseConnection.getInstance().cnx;
            Statement stmt1 = cnx.createStatement();
            Statement stmt2 = cnx.createStatement();
            ResultSet rs1 = stmt1.executeQuery("select p.pageId, p.pageName, p.pageCreationDate, p.genre "
                    + "from pages p "
                    + "where EXISTS ( SELECT 1 " +
                    "                      FROM pagemanager pm" +
                    "                     WHERE pm.userId = " + Cache.member.getMember_id() +
                    "                       AND pm.pageId = p.pageId" +
                    "                  ) " + ";");
            System.out.println("Here is the list of pages you manage:\n");
            System.out.println("ID " + "Page Name" + "  " + "Creation Date" + "    " + "Genre");
            while (rs1.next()) {
                System.out.println(" " + rs1.getInt(1) + "   " + rs1.getString(2) + "      " + rs1.getDate(3) + "     " + rs1.getString(4));
            }
            System.out.println("---------------------------------------");
            System.out.println("Enter the ID of the page you chose to delete an admin from ");
            Scanner input = new Scanner(System.in);
            int pageid = input.nextInt();
            System.out.println("---------------------------------------");
            ResultSet rs2 = stmt2.executeQuery("select userId"
                    + "from pagemanager "
                    + "where pageId = " + pageid + " ;");
            System.out.println("Here is the list of users you can stop from managing your page:\n");
            System.out.println("ID ");
            while (rs2.next()) {
                System.out.println(" " + rs2.getInt(1));
            }
            System.out.println(" Enter the ID of the user you want to fire as page manager ");
            int userid = input.nextInt();
            Statement stmt3 = cnx.createStatement();
            int test = stmt3.executeUpdate("DELETE  FROM pagemanager( pageId , userId ) " +
                    "VALUES(" + pageid + "," + userid + ");");
            if (test > 0) {
                System.out.println("User with ID " + userid + " is no longer managing the page with ID " + pageid);
            } else {

                System.out.println("Maybe next time!");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());

        }
    }

//    public int deleteAdmin(int idUser, int idPage) throws SQLException {
//        Connection cnx = SingletonDatabaseConnection.getInstance().cnx;
//        try (Statement stmt1 = cnx.createStatement()) {
//            return stmt1.executeUpdate("DELETE  FROM pagemanager( pageId , userId ) " +
//                    "VALUES(" + idPage + "," + idUser + ");");
//        }
//
//    }

    public void deletePage() {
        Connection cnx = SingletonDatabaseConnection.getInstance().cnx;
        try {
            Statement stmt1 = cnx.createStatement();
            Statement stmt2 = cnx.createStatement();

            ResultSet rs1 = stmt1.executeQuery("select p.pageId, p.pageName, p.pageCreationDate, p.genre "
                    + "from pages p "
                    + "where EXISTS ( SELECT 1 " +
                    "                      FROM pagemanager pm" +
                    "                     WHERE pm.userId = " + Cache.member.getMember_id() +
                    "                       AND pm.pageId = p.pageId" +
                    "                  ) " + ";");
            System.out.println("Here is the list of pages you manage:\n");
            System.out.println("ID " + "Page Name" + "  " + "Creation Date" + "    " + "Genre");
            while (rs1.next()) {
                System.out.println(" " + rs1.getInt(1) + "   " + rs1.getString(2) + "      " + rs1.getDate(3) + "     " + rs1.getString(4));
            }
            System.out.println("---------------------------------------");
            System.out.println("Enter the ID of the page you chose to delete");
            Scanner input = new Scanner(System.in);
            int pageid = input.nextInt();
            stmt2.executeUpdate("DELETE FROM pages WHERE pageId=" + pageid + ";");
            System.out.println("Page wit Id " + pageid + " has been deleted.");


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }




    public void createPage(Page page) throws SQLException {
        Connection cnx = SingletonDatabaseConnection.getInstance().cnx;
        try {
            Statement stmt1 = cnx.createStatement();
            Statement stmt2 = cnx.createStatement();

            stmt1.executeUpdate("INSERT INTO pages ( pageName, pageCreationDate, pageCreator, genre) " +
                    "VALUES ('" + page.getPageName() + "', '" + page.getCreationDate() + "', '" + page.getCreator().getMember_id() + "', '" + page.getGenre() + "');");


            stmt2.executeUpdate("INSERT INTO pagemanager( pageId , userId ) " +
                    "VALUES(" + page.getPageId() + "," + page.getCreator().getMember_id() + ");");


        } catch (Exception e) {
            System.out.println(e.getMessage());

        }

    }
}
