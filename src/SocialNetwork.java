import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.Console;
import java.io.IOException;
import java.util.Scanner;

import controllers.*;
import services.MemberService;
import utils.Cache;
import utils.ConsoleColors;


public class SocialNetwork {

    public static void printLogo() {
        int width = 150;
        int height = 30;

        System.out.println(ConsoleColors.LOGO_COLOR);

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        g.setFont(new Font("SansSerif", Font.BOLD, 24));

        Graphics2D graphics = (Graphics2D) g;
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics.drawString("SNS", 10, 20);

        for (int y = 0; y < height; y++) {
            StringBuilder sb = new StringBuilder();
            for (int x = 0; x < width; x++) {

                sb.append(image.getRGB(x, y) == -16777216 ? " " : "$");

            }

            if (sb.toString().trim().isEmpty()) {
                continue;
            }

            System.out.println(sb);
        }
    }

    public static void main(String[] args) {
        printLogo();
        Scanner inputString = new Scanner(System.in);
        Scanner inputInt = new Scanner(System.in);

        System.out.println(ConsoleColors.RESET);
        GroupsController gc = new GroupsController();
        MemberController mc = new MemberController();

        System.out.println(ConsoleColors.TITLE_COLOR + "\t\t\t\t  ---------- ** WELCOME ** ----------\t \n" + ConsoleColors.MENU_COLOR);
        System.out.println("\n");
        System.out.println();
        System.out.println(ConsoleColors.MENU_COLOR);
        System.out.println("Already have an account? If not join us!");
        System.out.println("Please press 1 to sign in, 2 to sign up or 0 to exit.");
        int loginChoice = -1;

        while (loginChoice != 1 && loginChoice != 2 && loginChoice != 0) {
            loginChoice = inputInt.nextInt();
            if (loginChoice != 1 && loginChoice != 2 && loginChoice != 0) {
                System.out.println(ConsoleColors.WARNING_COLOR + "Please make sure to choose a valid option!" + ConsoleColors.MENU_COLOR);
            }
        }
        if (loginChoice == 1) {
            mc.login(inputInt);
        } else if (loginChoice == 2) {
            mc.signup(inputInt);
        } else {
            System.exit(0);
        }
        if (loginChoice == 2) {
            mc.login(inputInt);
        }
        int operator = -99;
        System.out.println(ConsoleColors.CYAN_BRIGHT + "Welcome " + Cache.member.getFirstName() + " " + Cache.member.getLastName() + "!" + ConsoleColors.MENU_COLOR);

        while (operator != 4) {
            if (operator != 1 && operator != 2 && operator != 3 && operator != -99) {
                System.out.println(ConsoleColors.WARNING_COLOR + "Please make sure to choose a valid option!");
            }
            System.out.println("\n");
            System.out.println(ConsoleColors.PURPLE_BOLD_BRIGHT + "\t \t\t\t\t--Dashboard--" + ConsoleColors.MENU_COLOR);
            System.out.println("\n");
            System.out.println(" 1)Friends       2)Groups       3)Pages       4)Exit");
            System.out.println("\n");
            System.out.println("What would you like to consult?");
            System.out.println("\nChoice: ");

            operator = inputInt.nextInt();
            switch (operator) {
                case 1:
                    String choice = "";
                    while (!choice.equals("e")) {

                        System.out.println(ConsoleColors.TITLE_COLOR + "\t \t--Friends--" + ConsoleColors.MENU_COLOR);
                        System.out.println("\n");
                        mc.friendsLt();
                        System.out.println("\n");
                        System.out.println(" a) Consult Friends Requests \t \t b) Send Friend Request");
                        System.out.println(" c) Write On A Friend's Timeline\t d)Search");
                        System.out.println(" e) Exit");
                        System.out.println("\nChoice: ");
                        choice = inputString.nextLine();
                        switch (choice) {
                            case "a":
                                mc.acceptDenyRequest();
                                break;
                            case "b":
                                mc.sendFriendRequest();
                                break;
                            case "c":
                                mc.addPostToWall();
                                mc.consultFriendTimeline();
                                break;
                            case "d":
                                //Search Method
                                break;
                            default:
                                System.out.println(ConsoleColors.WARNING_COLOR + "Please make sure to choose a valid option!");

                                break;
                        }
                    }
                    break;
                case 2:
                    String choice2 = "";
                    while (!choice2.equals("d")) {

                        System.out.println(ConsoleColors.TITLE_COLOR + "\t \t--Groups--" + ConsoleColors.MENU_COLOR);
                        System.out.println("\n");
                        System.out.println(" a) Join A Group \t \t b) Create A Group");
                        System.out.println(" c) Group Management \t d) Exit");
                        System.out.println("\nChoice: ");
                        choice2 = inputString.nextLine();
                        if (choice2.equals("a")) {
                            gc.joinGroup();
                        }
                        if (choice2.equals("b")) {
                            gc.createGroup();
                        }
                        if (choice2.equals("c")) {
                            String choiceManagement = "";

                            while (!choiceManagement.equals("v")) {

                                System.out.println(ConsoleColors.TITLE_COLOR + "\t \t--Group Management--" + ConsoleColors.MENU_COLOR);
                                System.out.println("\n");
                                System.out.println(" i) Add Admin \t \t \t \t ii) Delete Admin");
                                System.out.println(" iii) Delete Member     \t iv) Delete Group");
                                System.out.println(" v) Exit");
                                System.out.println("\nChoice: ");
                                choiceManagement = inputString.nextLine();
                                switch (choiceManagement) {
                                    case "i":
                                        gc.addAdmin();
                                        break;
                                    case "ii":
                                        gc.deleteAdminFromGroup();
                                        break;
                                    case "iii":
                                        gc.deleteMemberFromGroup();
                                        break;
                                    case "iv":
                                        gc.deleteGroup();
                                        break;
                                    default:
                                        System.out.println(ConsoleColors.WARNING_COLOR + "Please make sure to choose a valid option!\n");
                                        break;
                                }
                            }
                        }
                    }
                    break;
                case 3:
                    System.out.println(ConsoleColors.TITLE_COLOR + "\t \t--Pages--" + ConsoleColors.MENU_COLOR);
                    break;

                default:
                    System.out.println(ConsoleColors.WARNING_COLOR + "Please make sure to choose a valid option!");
            }


        }


    }


}



