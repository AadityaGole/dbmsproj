//Steps to run the code
// 1. Open terminal
// 2. cd to the directory where the file is saved
// 3. Run the following command:
// export CLASSPATH='/mnt/c/Users/aadit/work/clg/DBMS/proj/JDBC-tutorial/JDBC-tutorial/mysql-connector-j-8.3.0.jar:.'
// note: make classpath according to your system and path of the .jar file
// 4. Run the following command
// javac IMT2022087_JDBC_PROJECT.java
// 5. Run the following command
// java IMT2022087_JDBC_PROJECT


//showing players,teams,which team which player plays for gives slect from multiple tables 
//agents are exclusive to players so if an agent is re assigned they can't be an agent for another player- INSERT UPDATE
//rolling back in assigning agentid if agentid is invalid

//STEP 1. Import required packages
import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class IMT2022087_JDBC_PROJECT  {

  // Set JDBC driver name and database URL
  static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
  static final String DB_URL = "jdbc:mysql://localhost:3306/sampleproj";

  // Database credentials
  static final String USER = "root"; // add your user
  static final String PASSWORD = "aA1!qwerty"; // add password

  public static void showplayers(Statement stmt) {
    try {
        String query = "SELECT p.playerid, p.name, p.jersey, p.agentid, p.teamid, p.salary, t.name AS teamname, a.name AS agentname FROM players p LEFT JOIN teams t ON p.teamid = t.teamid LEFT JOIN agent a ON p.agentid = a.agentid";
        ResultSet rs = stmt.executeQuery(query);

        // Extract data from result set
        while (rs.next()) {
            Integer playerid = rs.getInt("playerid");
            String name = rs.getString("name");
            Integer jersey = rs.getInt("jersey");
            Integer teamid = rs.getInt("teamid");
            Integer agentid = rs.getObject("agentid") != null ? rs.getInt("agentid") : null;
            Integer salary = rs.getInt("salary");
            String teamname = rs.getString("teamname");
            String agentname = rs.getObject("agentname") != null ? rs.getString("agentname") : "null";

            System.out.print("playerid: " + playerid);
            System.out.print(", name: " + name);
            System.out.print(", jersey no: " + jersey);
            System.out.print(", teamid: " + teamid);
            System.out.print(", agentid: " + agentid);
            System.out.print(", salary: " + salary);
            System.out.print(", teamname: " + teamname);
            System.out.print(", agentname: " + agentname);
            System.out.println();
        }
        rs.close();
    } catch(SQLException se) {
        //Handle errors for JDBC
        se.printStackTrace();
    }
}

public static void showteams(Statement stmt) {
  try {
      Integer id = 0;
      String name = "";
      Integer coachid = 0;
      Integer champid = 0;
      String coachname = "";

      String query = "SELECT t.teamid, t.name, t.coachid, t.champid, coach.name AS coachname FROM (SELECT * FROM teams) AS t LEFT JOIN coach ON t.coachid = coach.coachid";
      ResultSet rs = stmt.executeQuery(query);

      // Extract data from result set
      while (rs.next()) {
          id = rs.getInt("teamid");
          name = rs.getString("name");
          coachid = rs.getInt("coachid");
          champid = rs.getInt("champid");
          coachname = rs.getString("coachname");

          System.out.print("id: " + id);
          System.out.print(", name: " + name);
          System.out.print(", coachid: " + coachid);
          System.out.print(", championship: " + champid);
          if (coachname != null && !coachname.isEmpty()) {
              System.out.print(", coachname: " + coachname);
          } else {
              System.out.print(", coachname: no coach");
          }
          System.out.println();
      }

      rs.close();
  } catch(SQLException se) {
      //Handle errors for JDBC
      se.printStackTrace();
  }
}

public static void showgames(Statement stmt) {
  try {
      String query = "SELECT * FROM games";
      ResultSet rs = stmt.executeQuery(query);

      // Extract data from result set
      while (rs.next()) {
          Integer id = rs.getInt("gameid");
          Integer team1 = rs.getInt("team1");
          Integer team2 = rs.getInt("team2");
          Integer winnerid = rs.getInt("winnerid");
          String winnername = rs.getString("winnername");
          Integer motm = rs.getInt("motm");
          String motmname = rs.getString("motmname");
          Date date = rs.getDate("date");

          System.out.print("id: " + id);
          System.out.print(", team1: " + team1);
          System.out.print(", team2: " + team2);
          System.out.print(", winner: " + winnername);
          System.out.println(", motm: " + motmname);
          System.out.println();
      }
      rs.close();
  } catch(SQLException se) {
      //Handle errors for JDBC
      se.printStackTrace();
  }
}



  public static void showfullschema(Statement stmt) throws SQLException {
    String query = "SHOW TABLES";
    ResultSet rs = stmt.executeQuery(query);
    while (rs.next()) {
      String name = rs.getString("Tables_in_sampleproj");
      System.out.println(name);
    }
    rs.close();
  }

  public static void addplayers(Connection conn, Scanner sc) {
    Statement stmt = null;
    try {
        System.out.println("Enter playerid, name, jersey, agentid, teamid, salary");
        int id = sc.nextInt();
        String name = sc.next();
        int jersey = sc.nextInt();
        int agentid = sc.nextInt();
        int teamid = sc.nextInt();
        int salary = sc.nextInt();

        String query = "INSERT INTO players VALUES (" + id + ",'" + name + "'," + jersey + "," + agentid + "," + teamid + "," + salary + ")";

        stmt = conn.createStatement();
        conn.setAutoCommit(false); // Start transaction
        stmt.executeUpdate(query);
        conn.commit(); // Commit transaction
        System.out.println("Player added successfully (committed)");

    } catch(SQLException se) {
        // If there is an error then rollback the changes.
        if (conn != null) {
            try {
                System.err.println("Transaction is being rolled back");
                conn.rollback();
            } catch(SQLException excep) {
                excep.printStackTrace();
            }
        }
        se.printStackTrace();
    } finally {
        // Close resources
        if (stmt != null) {
            try {
                stmt.close();
            } catch(SQLException se) {
                se.printStackTrace();
            }
        }
        try {
            conn.setAutoCommit(true);
        } catch(SQLException se) {
            se.printStackTrace();
        }

    }
}

public static void deleteplayers(Connection conn, Scanner sc) {
  Statement stmt = null;
  try {
      System.out.println("Enter playerid to delete");
      int id = sc.nextInt();
      System.out.println("Deleting player with playerid = " + id+"...\nare you sure u want to delete? enter 0 to rollback otherwise it will commit and be deleted");
      String confirm = sc.next();
      if (confirm == "0") {
          System.out.println("Rolling back data here....");
          conn.rollback();
          return;
      }
      String query = "DELETE FROM players WHERE playerid = " + id;
      

      stmt = conn.createStatement();
      conn.setAutoCommit(false); // Start transaction
      stmt.executeUpdate(query);
      conn.commit(); // Commit transaction
      System.out.println("Player deleted successfully (committed)");

  } catch(SQLException se) {
      // If there is an error then rollback the changes.
      if (conn != null) {
          try {
              System.err.println("Transaction is being rolled back");
              conn.rollback();
          } catch(SQLException excep) {
              excep.printStackTrace();
          }
      }
      se.printStackTrace();
  } finally {
      // Close resources
      if (stmt != null) {
          try {
              stmt.close();
          } catch(SQLException se) {
              se.printStackTrace();
          }
      }
      try {
          conn.setAutoCommit(true);
      } catch(SQLException se) {
          se.printStackTrace();
      }

  }
}

public static void updateplayers(Connection conn, Scanner sc) {
  Statement stmt = null;
  try {
      System.out.println("Enter playerid, name, jersey, agentid, teamid, salary");
      int id = sc.nextInt();
      String name = sc.next();
      int jersey = sc.nextInt();
      int agentid = sc.nextInt();
      int teamid = sc.nextInt();
      int salary = sc.nextInt();

      String query = "UPDATE players SET name = '" + name + "', jersey = " + jersey + ", agentid = " + agentid + ", teamid = " + teamid + ", salary = " + salary + " WHERE playerid = " + id;

      stmt = conn.createStatement();
      conn.setAutoCommit(false); // Start transaction
      stmt.executeUpdate(query);
      conn.commit(); // Commit transaction
      System.out.println("Player updated successfully (committed)");

  } catch(SQLException se) {
      // If there is an error then rollback the changes.
      if (conn != null) {
          try {
              System.err.println("Transaction is being rolled back");
              conn.rollback();
          } catch(SQLException excep) {
              excep.printStackTrace();
          }
      }
      se.printStackTrace();
  } finally {
      // Close resources
      if (stmt != null) {
          try {
              stmt.close();
          } catch(SQLException se) {
              se.printStackTrace();
          }
      }
      try {
          conn.setAutoCommit(true);
      } catch(SQLException se) {
          se.printStackTrace();
      }
  }
}

  public static void salaryplayers(Statement stmt, Scanner sc)
    throws SQLException {
    System.out.println(
      "Enter 1 for playerid to get salary, 2 for greater than an amout all players, 3 for less than an amount, 0 to exit"
    );
    int q1 = sc.nextInt();
    if (q1 == 1) {
      System.out.println("Enter playerid");
      int id = sc.nextInt();
      String query = "SELECT salary FROM players WHERE playerid = " + id;
      ResultSet rs = stmt.executeQuery(query);
      while (rs.next()) {
        Integer salary = rs.getInt("salary");
        System.out.println("Salary: " + salary);
      }
    } else if (q1 == 2) {
      System.out.println("Enter amount");
      int amount = sc.nextInt();
      String query = "SELECT * FROM players WHERE salary > " + amount;
      ResultSet rs = stmt.executeQuery(query);
      while (rs.next()) {
        Integer id = rs.getInt("playerid");
        String name = rs.getString("name");
        Integer jersey = rs.getInt("jersey");
        Integer agentid = rs.getInt("agentid");
        Integer teamid = rs.getInt("teamid");
        Integer salary = rs.getInt("salary");
        System.out.print("id: " + id);
        System.out.print(", name: " + name);
        System.out.print(", jersey no: " + jersey);
        System.out.print(", teamid: " + teamid);
        System.out.print(", agentid: " + agentid);
        System.out.print(", salary: " + salary);
        System.out.println();
      }
    } else if (q1 == 3) {
      System.out.println("Enter amount");
      int amount = sc.nextInt();
      String query = "SELECT * FROM players WHERE salary < " + amount;
      ResultSet rs = stmt.executeQuery(query);
      while (rs.next()) {
        Integer id = rs.getInt("playerid");
        String name = rs.getString("name");
        Integer jersey = rs.getInt("jersey");
        Integer agentid = rs.getInt("agentid");
        Integer teamid = rs.getInt("teamid");
        Integer salary = rs.getInt("salary");
        System.out.print("id: " + id);
        System.out.print(", name: " + name);
        System.out.print(", jersey no: " + jersey);
        System.out.print(", teamid: " + teamid);
        System.out.print(", agentid: " + agentid);
        System.out.print(", salary: " + salary);
        System.out.println();
      }
    }
  }

  // public static void alterplayers(Statement stmt, Scanner sc) throws
  // SQLException
  // {
  // System.out.println("Enter 1 to add column, 2 to delete column, 3 to rename
  // column, 4 to change datatype, 0 to exit");
  // int q1 = sc.nextInt();
  // if(q1==1)
  // {
  // System.out.println("Enter column name, datatype");
  // String colname = sc.next();
  // String datatype = sc.next();
  // String query = "ALTER TABLE players ADD "+colname+" "+datatype;
  // stmt.executeUpdate(query);
  // }
  // else if(q1==2)
  // {
  // System.out.println("Enter column name");
  // String colname = sc.next();
  // String query = "ALTER TABLE players DROP COLUMN "+colname;
  // stmt.executeUpdate(query);
  // }
  // else if(q1==3)
  // {
  // System.out.println("Enter old column name, new column name");
  // String oldcolname = sc.next();
  // String newcolname = sc.next();
  // String query = "ALTER TABLE players CHANGE "+oldcolname+" "+newcolname;
  // stmt.executeUpdate(query);
  // }
  // else if(q1==4)
  // {
  // System.out.println("Enter column name, new datatype");
  // String colname = sc.next();
  // String datatype = sc.next();
  // String query = "ALTER TABLE players MODIFY "+colname+" "+datatype;
  // stmt.executeUpdate(query);
  // }
  // else
  // {
  // System.out.println("Exiting");
  // }
  // }

  public static void addteams(Connection conn, Scanner sc) {
    Statement stmt = null;
    try {
        System.out.println("Enter teamid, name, coachid, champid");
        int id = sc.nextInt();
        String name = sc.next();
        int coachid = sc.nextInt();
        int champid = sc.nextInt();

        String query = "INSERT INTO teams VALUES (" + id + ",'" + name + "'," + coachid + "," + champid + ")";

        stmt = conn.createStatement();
        conn.setAutoCommit(false); // Start transaction
        stmt.executeUpdate(query);
        conn.commit(); // Commit transaction
        System.out.println("Team added successfully (committed)");

    } catch(SQLException se) {
        // If there is an error then rollback the changes.
        if (conn != null) {
            try {
                System.err.println("Transaction is being rolled back");
                conn.rollback();
            } catch(SQLException excep) {
                excep.printStackTrace();
            }
        }
        se.printStackTrace();
    } finally {
        // Close resources
        if (stmt != null) {
            try {
                stmt.close();
            } catch(SQLException se) {
                se.printStackTrace();
            }
        }
        try {
            conn.setAutoCommit(true);
        } catch(SQLException se) {
            se.printStackTrace();
        }
    }
}

public static void deleteteams(Connection conn, Scanner sc) {
  Statement stmt = null;
  try {
      System.out.println("Enter teamid to delete");
      int id = sc.nextInt();
      System.out.println("Deleting team with teamid = " + id+"...\nare you sure u want to delete? enter 0 to rollback otherwise it will commit and be deleted");
      String confirm = sc.next();
      if (confirm == "0") {
          System.out.println("Rolling back data here....");
          conn.rollback();
          return;
      }
      String query = "DELETE FROM teams WHERE teamid = " + id;

      stmt = conn.createStatement();
      conn.setAutoCommit(false); // Start transaction
      stmt.executeUpdate(query);
      conn.commit(); // Commit transaction
      System.out.println("Team deleted successfully (committed)");

  } catch(SQLException se) {
      // If there is an error then rollback the changes.
      if (conn != null) {
          try {
              System.err.println("Transaction is being rolled back");
              conn.rollback();
          } catch(SQLException excep) {
              excep.printStackTrace();
          }
      }
      se.printStackTrace();
  } finally {
      // Close resources
      if (stmt != null) {
          try {
              stmt.close();
          } catch(SQLException se) {
              se.printStackTrace();
          }
      }
      try {
          conn.setAutoCommit(true);
      } catch(SQLException se) {
          se.printStackTrace();
      }
  }
}

public static void updateteams(Connection conn, Scanner sc) {
  Statement stmt = null;
  try {
      System.out.println("Enter teamid, name, coachid, champid");
      int id = sc.nextInt();
      String name = sc.next();
      int coachid = sc.nextInt();
      int champid = sc.nextInt();

      String query = "UPDATE teams SET name = '" + name + "', coachid = " + coachid + ", champid = " + champid + " WHERE teamid = " + id;

      stmt = conn.createStatement();
      conn.setAutoCommit(false); // Start transaction
      stmt.executeUpdate(query);
      conn.commit(); // Commit transaction
      System.out.println("Team updated successfully (committed)");

  } catch(SQLException se) {
      // If there is an error then rollback the changes.
      if (conn != null) {
          try {
              System.err.println("Transaction is being rolled back");
              conn.rollback();
          } catch(SQLException excep) {
              excep.printStackTrace();
          }
      }
      se.printStackTrace();
  } finally {
      // Close resources
      if (stmt != null) {
          try {
              stmt.close();
          } catch(SQLException se) {
              se.printStackTrace();
          }
      }
      try {
          conn.setAutoCommit(true);
      } catch(SQLException se) {
          se.printStackTrace();
      }
  }
}

  // public static void alterteams(Statement stmt, Scanner sc) throws SQLException
  // {
  // if (stmt == null) {
  // throw new IllegalArgumentException("Statement cannot be null");
  // }
  // System.out.println("Enter 1 to add column, 2 to delete column, 3 to rename
  // column, 4 to change datatype, 0 to exit");
  // int q1 = sc.nextInt();
  // if(q1==1)
  // {
  // System.out.println("Enter column name:");
  // String colname = sc.next();
  // System.out.println("Enter datatype:");
  // String datatype = sc.next();
  // String query = "ALTER TABLE teams ADD "+colname+" "+datatype+";";
  // stmt.executeUpdate(query);
  // }
  // else if(q1==2)
  // {
  // System.out.println("Enter column name");
  // String colname = sc.next();
  // String query = "ALTER TABLE teams DROP COLUMN "+colname;
  // stmt.executeUpdate(query);
  // }
  // else if(q1==3)
  // {
  // System.out.println("Enter old column name, new column name");
  // String oldcolname = sc.next();
  // String newcolname = sc.next();
  // String query = "ALTER TABLE teams CHANGE "+oldcolname+" "+newcolname;
  // stmt.executeUpdate(query);
  // }
  // else if(q1==4)
  // {
  // System.out.println("Enter column name, new datatype");
  // String colname = sc.next();
  // String datatype = sc.next();
  // String query = "ALTER TABLE teams MODIFY "+colname+" "+datatype;
  // stmt.executeUpdate(query);
  // }
  // else
  // {
  // System.out.println("Exiting");
  // }
  // }

  public static void addgames(Connection conn, Scanner sc) {
    Statement stmt = null;
    try {
        System.out.println("Enter gameid, team1, team2, champid, date, motmid, winnerid");
        int id = sc.nextInt();
        int team1 = sc.nextInt();
        int team2 = sc.nextInt();
        int champid = sc.nextInt();
        Date date = Date.valueOf(sc.next());
        int motmid = sc.nextInt();
        int winnerid = sc.nextInt();

        stmt = conn.createStatement();
        conn.setAutoCommit(false); // Start transaction

        String query = "SELECT name FROM teams WHERE teamid = " + team1;
        ResultSet rs = stmt.executeQuery(query);
        String team1name = "";
        while (rs.next()) {
            team1name = rs.getString("name");
        }

        query = "SELECT name FROM teams WHERE teamid = " + team2;
        rs = stmt.executeQuery(query);
        String team2name = "";
        while (rs.next()) {
            team2name = rs.getString("name");
        }

        query = "SELECT name FROM players WHERE playerid = " + motmid;
        rs = stmt.executeQuery(query);
        String motmname = "";
        while (rs.next()) {
            motmname = rs.getString("name");
        }

        query = "SELECT name FROM teams WHERE teamid = " + winnerid;
        rs = stmt.executeQuery(query);
        String winnername = "";
        while (rs.next()) {
            winnername = rs.getString("name");
        }

        query = "INSERT INTO games VALUES (" + id + "," + team1 + "," + team2 + "," + champid + ",'" + date + "'," + motmid + "," + winnerid + ")";
        stmt.executeUpdate(query);

        conn.commit(); // Commit transaction
        System.out.println("Game added successfully (committed)");

    } catch(SQLException se) {
        // If there is an error then rollback the changes.
        if (conn != null) {
            try {
                System.err.println("Transaction is being rolled back");
                conn.rollback();
            } catch(SQLException excep) {
                excep.printStackTrace();
            }
        }
        se.printStackTrace();
    } finally {
        // Close resources
        if (stmt != null) {
            try {
                stmt.close();
            } catch(SQLException se) {
                se.printStackTrace();
            }
        }
        try {
            conn.setAutoCommit(true);
        } catch(SQLException se) {
            se.printStackTrace();
        }
    }
}

public static void deletegames(Connection conn, Scanner sc) {
  Statement stmt = null;
  try {
      System.out.println("Enter gameid to delete");
      int id = sc.nextInt();
      System.out.println("Deleting game with gameid = " + id+"...\nare you sure u want to delete? enter 0 to rollback otherwise it will commit and be deleted");
      String confirm = sc.next();
      if (confirm == "0") {
          System.out.println("Rolling back data here....");
          conn.rollback();
          return;
      }
      String query = "DELETE FROM games WHERE gameid = " + id;

      stmt = conn.createStatement();
      conn.setAutoCommit(false); // Start transaction
      stmt.executeUpdate(query);
      conn.commit(); // Commit transaction
      System.out.println("Game deleted successfully (committed)");

  } catch(SQLException se) {
      // If there is an error then rollback the changes.
      if (conn != null) {
          try {
              System.err.println("Transaction is being rolled back");
              conn.rollback();
          } catch(SQLException excep) {
              excep.printStackTrace();
          }
      }
      se.printStackTrace();
  } finally {
      // Close resources
      if (stmt != null) {
          try {
              stmt.close();
          } catch(SQLException se) {
              se.printStackTrace();
          }
      }
      try {
          conn.setAutoCommit(true);
      } catch(SQLException se) {
          se.printStackTrace();
      }
  }
}

  // public static void updategames(Statement stmt, Scanner sc) throws
  // SQLException
  // {
  // System.out.println("Enter gameid, team1, team2, winner, mom");
  // int id = sc.nextInt();
  // int team1 = sc.nextInt();
  // int team2 = sc.nextInt();
  // int winner = sc.nextInt();
  // int mom = sc.nextInt();
  // String query = "UPDATE games SET team1 = "+team1+", team2 = "+team2+",
  // winnerid = "+winner+", mom = "+mom+" WHERE gameid = "+id;
  // stmt.executeUpdate(query);
  // }

  // public static void altergames(Statement stmt, Scanner sc) throws SQLException
  // {
  // System.out.println("Enter 1 to add column, 2 to delete column, 3 to rename
  // column, 4 to change datatype, 0 to exit");
  // int q1 = sc.nextInt();
  // if(q1==1)
  // {
  // System.out.println("Enter column name then datatype");
  // String colname = sc.next();
  // String datatype = sc.next();
  // String query = "ALTER TABLE games ADD "+colname+" "+datatype;
  // stmt.executeUpdate(query);
  // }
  // else if(q1==2)
  // {
  // System.out.println("Enter column name");
  // String colname = sc.next();
  // String query = "ALTER TABLE games DROP COLUMN "+colname;
  // stmt.executeUpdate(query);
  // }
  // else if(q1==3)
  // {
  // System.out.println("Enter old column name, new column name");
  // String oldcolname = sc.next();
  // String newcolname = sc.next();
  // String query = "ALTER TABLE games CHANGE "+oldcolname+" "+newcolname;
  // stmt.executeUpdate(query);
  // }
  // else if(q1==4)
  // {
  // System.out.println("Enter column name, new datatype");
  // String colname = sc.next();
  // String datatype = sc.next();
  // String query = "ALTER TABLE games MODIFY "+colname+" "+datatype;
  // stmt.executeUpdate(query);
  // }
  // else
  // {
  // System.out.println("Exiting");
  // }
  // }
  public static void showmotm(Statement stmt) throws SQLException {
    String query =
      "SELECT games.gameid, players.name\r\n" + //
      "FROM games\r\n" + //
      "INNER JOIN players\r\n" + //
      "ON games.motm = players.playerid;";
    // get name of motm for each game too

    ResultSet rs = stmt.executeQuery(query);
    while (rs.next()) {
      Integer gameid = rs.getInt("games.gameid");
      String name = rs.getString("players.name");
      System.out.println("motm for gameid: " + gameid + " is " + name);
    }
    rs.close();
  }

  public static void showplayerteam(Statement stmt) throws SQLException {
    String query =
      "SELECT players.*, agent.name AS agentname FROM players LEFT JOIN agent ON players.agentid = agent.agentid;";
    ResultSet rs = stmt.executeQuery(query);
    while (rs.next()) {
      String teamname = rs.getString("teams.name");
      String playername = rs.getString("players.name");
      System.out.println(
        "Player: " + playername + " plays for team: " + teamname
      );
      System.out.println();
    }
    rs.close();
  }

  public static void showcoach(Statement stmt) throws SQLException {
    String query = "SELECT * FROM coach";
    ResultSet rs = stmt.executeQuery(query);
    while (rs.next()) {
      Integer id = rs.getInt("coachid");
      String name = rs.getString("name");
      Integer age = rs.getInt("age");

      System.out.print("id: " + id);
      System.out.print(", name: " + name);
      System.out.print(", age: " + age);
      System.out.println();
    }
    rs.close();
  }

//   public static void addcoach(Connection conn, Scanner sc) {
//     Statement stmt = null;
//     try {
//         System.out.println("Enter coachid, name, age");
//         int id = sc.nextInt();
//         String name = sc.next();
//         int age = sc.nextInt();

//         String query = "INSERT INTO coach VALUES (" + id + ",'" + name + "'," + age + ")";

//         stmt = conn.createStatement();
//         conn.setAutoCommit(false); // Start transaction
//         stmt.executeUpdate(query);
//         conn.commit(); // Commit transaction
//         System.out.println("Committed successfully");
//     } catch(SQLException se) {
//         // If there is an error then rollback the changes.
//         if (conn != null) {
//             try {
//                 System.err.println("Transaction is being rolled back");
//                 conn.rollback();
//             } catch(SQLException excep) {
//                 excep.printStackTrace();
//             }
//         }
//         se.printStackTrace();
//     } finally {
//         // Close resources
//         if (stmt != null) {
//             try {
//                 stmt.close();
//             } catch(SQLException se) {
//                 se.printStackTrace();
//             }
//         }
//         try {
//             conn.setAutoCommit(true);
//         } catch(SQLException se) {
//             se.printStackTrace();
//         }
//         System.out.println("Coach added successfully (committed)");
//     }


// }

public static void deletecoach(Connection conn, Scanner sc) {
  Statement stmt = null;
  try {
      System.out.println("Enter coachid to delete");
      int id = sc.nextInt();
      System.out.println("Deleting coach with coachid = " + id+"...\nare you sure u want to delete? enter 0 to rollback otherwise it will commit and be deleted");
      String confirm = sc.next();
      if (confirm == "0") {
          System.out.println("Rolling back data here....");
          conn.rollback();
          return;
      }
      String query = "DELETE FROM coach WHERE coachid = " + id;

      stmt = conn.createStatement();
      conn.setAutoCommit(false); // Start transaction
      stmt.executeUpdate(query);
      conn.commit(); // Commit transaction
      System.out.println("Committed successfully");
  } catch(SQLException se) {
      // If there is an error then rollback the changes.
      if (conn != null) {
          try {
              System.err.println("Transaction is being rolled back");
              conn.rollback();
          } catch(SQLException excep) {
              excep.printStackTrace();
          }
      }
      se.printStackTrace();
  } finally {
      // Close resources
      if (stmt != null) {
          try {
              stmt.close();
          } catch(SQLException se) {
              se.printStackTrace();
          }
      }
      try {
          conn.setAutoCommit(true);
      } catch(SQLException se) {
          se.printStackTrace();
      }
      System.out.println("Coach deleted successfully (committed)");
  }
}
public static void addAndAssignCoach(Connection conn, Scanner sc) {
  PreparedStatement stmt = null;
  try {
      System.out.println("Enter coachid, name");
      int coachid = sc.nextInt();
      String name = sc.next();
      String addCoachQuery = "INSERT INTO coach (coachid, name) VALUES (?, ?)";

      System.out.println("Enter teamid to assign coach");
      int teamid = sc.nextInt();
      String assignCoachQuery = "UPDATE teams SET coachid = ? WHERE teamid = ?";
      

      conn.setAutoCommit(false); // Start transaction

      stmt = conn.prepareStatement(addCoachQuery); // Prepare statement to add coach
      stmt.setInt(1, coachid);
      stmt.setString(2, name);
      stmt.executeUpdate(); // Add coach

      stmt = conn.prepareStatement(assignCoachQuery); // Prepare statement to assign coach to player
      stmt.setInt(1, coachid);
      stmt.setInt(2, teamid);
      int rowsAffected = stmt.executeUpdate(); // Assign coach to player

      if (rowsAffected == 0) {
          throw new SQLException("Team does not exist, rolling back transaction\n Coach not added");
      }

      conn.commit(); // Commit transaction
      System.out.println("Coach added and assigned successfully (committed)");
  } catch(SQLException se) {
      // If there is an error then rollback the changes.
      if (conn != null) {
          try {
              System.err.println("Transaction is being rolled back");
              conn.rollback();
          } catch(SQLException excep) {
              excep.printStackTrace();
          }
      }
      se.printStackTrace();
  } finally {
      // Close resources
      if (stmt != null) {
          try {
              stmt.close();
          } catch(SQLException se) {
              se.printStackTrace();
          }
      }
      try {
          conn.setAutoCommit(true);
      } catch(SQLException se) {
          se.printStackTrace();
      }
  }
}

// public static void assigncoach(Connection conn, Scanner sc) {
//   Statement stmt = null;
//   try {
//       System.out.println("Enter teamid, coachid");
//       int teamid = sc.nextInt();
//       int coachid = sc.nextInt();
//       String query = "UPDATE teams SET coachid = " + coachid + " WHERE teamid = " + teamid;

//       stmt = conn.createStatement();
//       conn.setAutoCommit(false); // Start transaction
//       stmt.executeUpdate(query);
//       conn.commit(); // Commit transaction
//       System.out.println("Committed successfully");
//   } catch(SQLException se) {
//       // If there is an error then rollback the changes.
//       if (conn != null) {
//           try {
//               System.err.println("Transaction is being rolled back");
//               conn.rollback();
//           } catch(SQLException excep) {
//               excep.printStackTrace();
//           }
//       }
//       se.printStackTrace();
//   } finally {
//       // Close resources
//       if (stmt != null) {
//           try {
//               stmt.close();
//           } catch(SQLException se) {
//               se.printStackTrace();
//           }
//       }
//       try {
//           conn.setAutoCommit(true);
//       } catch(SQLException se) {
//           se.printStackTrace();
//       }
//       System.out.println("Coach assigned successfully (committed)");
//   }
// }

public static void addAndAssignAgent(Connection conn, Scanner sc) {
  PreparedStatement stmt = null;
  try {
      System.out.println("Enter agentid, name");
      int agentid = sc.nextInt();
      String name = sc.next();
      String addAgentQuery = "INSERT INTO agent VALUES (?, ?)";

      System.out.println("Enter playerid to assign agent");
      int playerid = sc.nextInt();
      String assignAgentQuery = "UPDATE players SET agentid = ? WHERE playerid = ?";

      conn.setAutoCommit(false); // Start transaction

      stmt = conn.prepareStatement(addAgentQuery); // Prepare statement to add agent
      stmt.setInt(1, agentid);
      stmt.setString(2, name);
      stmt.executeUpdate(); // Add agent

      stmt = conn.prepareStatement(assignAgentQuery); // Prepare statement to assign agent to player
      stmt.setInt(1, agentid);
      stmt.setInt(2, playerid);
      int rowsAffected = stmt.executeUpdate(); // Assign agent to player

      if (rowsAffected == 0) {
          throw new SQLException("Player does not exist, rolling back transaction\n Agent not added");
      }

      conn.commit(); // Commit transaction
      System.out.println("Agent added and assigned successfully (committed)");
  } catch(SQLException se) {
      // If there is an error then rollback the changes.
      if (conn != null) {
          try {
              System.err.println("Transaction is being rolled back");
              conn.rollback();
          } catch(SQLException excep) {
              excep.printStackTrace();
          }
      }
      se.printStackTrace();
  } finally {
      // Close resources
      if (stmt != null) {
          try {
              stmt.close();
          } catch(SQLException se) {
              se.printStackTrace();
          }
      }
      try {
          conn.setAutoCommit(true);
      } catch(SQLException se) {
          se.printStackTrace();
      }
  }
}


  public static void showagent(Statement stmt) throws SQLException {
    String query = "SELECT * FROM agent";
    ResultSet rs = stmt.executeQuery(query);
    while (rs.next()) {
      Integer id = rs.getInt("agentid");
      String name = rs.getString("name");

      System.out.print("id: " + id);
      System.out.print(", name: " + name);
      System.out.println();
    }
    rs.close();
  }

//   public static void addagent(Connection conn, Scanner sc) {
//     Statement stmt = null;
//     try {
//         System.out.println("Enter agentid, name");
//         int agentid = sc.nextInt();
//         String name = sc.next();
//         String query = "INSERT INTO agent VALUES (" + agentid + ", '" + name + "')";

//         stmt = conn.createStatement();
//         conn.setAutoCommit(false); // Start transaction
//         stmt.executeUpdate(query);
//         conn.commit(); // Commit transaction
//         System.out.println("Committed successfully");
//     } catch(SQLException se) {
//         // If there is an error then rollback the changes.
//         if (conn != null) {
//             try {
//                 System.err.println("Transaction is being rolled back");
//                 conn.rollback();
//             } catch(SQLException excep) {
//                 excep.printStackTrace();
//             }
//         }
//         se.printStackTrace();
//     } finally {
//         // Close resources
//         if (stmt != null) {
//             try {
//                 stmt.close();
//             } catch(SQLException se) {
//                 se.printStackTrace();
//             }
//         }
//         try {
//             conn.setAutoCommit(true);
//         } catch(SQLException se) {
//             se.printStackTrace();
//         }
//     }
// }

public static void deleteagent(Connection conn, Scanner sc) {
  Statement stmt = null;
  try {
      System.out.println("Enter agentid to delete");
      int id = sc.nextInt();
      System.out.println("Deleting agent with agentid = " + id+"...\nare you sure u want to delete? enter 0 to rollback otherwise it will commit and be deleted");
      String confirm = sc.next();
      if (confirm == "0") {
          System.out.println("Rolling back data here....");
          conn.rollback();
          return;
      }

      String query = "DELETE FROM agent WHERE agentid = " + id;

      stmt = conn.createStatement();
      conn.setAutoCommit(false); // Start transaction
      stmt.executeUpdate(query);
      conn.commit(); // Commit transaction
      System.out.println("Committed successfully");
  } catch(SQLException se) {
      // If there is an error then rollback the changes.
      if (conn != null) {
          try {
              System.err.println("Transaction is being rolled back");
              conn.rollback();
          } catch(SQLException excep) {
              excep.printStackTrace();
          }
      }
      se.printStackTrace();
  } finally {
      // Close resources
      if (stmt != null) {
          try {
              stmt.close();
          } catch(SQLException se) {
              se.printStackTrace();
          }
      }
      try {
          conn.setAutoCommit(true);
      } catch(SQLException se) {
          se.printStackTrace();
      }
  }
}

public static void updateagent(Connection conn, Scanner sc) {
  Statement stmt = null;
  try {
      System.out.println("Enter agentid to update");
      int id = sc.nextInt();
      System.out.println("Enter new value for column_to_update");
      String new_value = sc.next();
      String query = "UPDATE agent SET column_to_update = '" + new_value + "' WHERE agentid = " + id;

      stmt = conn.createStatement();
      conn.setAutoCommit(false); // Start transaction
      stmt.executeUpdate(query);
      conn.commit(); // Commit transaction
      System.out.println("Committed successfully");
  } catch(SQLException se) {
      // If there is an error then rollback the changes.
      if (conn != null) {
          try {
              System.err.println("Transaction is being rolled back");
              conn.rollback();
          } catch(SQLException excep) {
              excep.printStackTrace();
          }
      }
      se.printStackTrace();
  } finally {
      // Close resources
      if (stmt != null) {
          try {
              stmt.close();
          } catch(SQLException se) {
              se.printStackTrace();
          }
      }
      try {
          conn.setAutoCommit(true);
      } catch(SQLException se) {
          se.printStackTrace();
      }
  }
}

// public static void assignagent(Connection conn, Scanner sc) {
//   Statement stmt = null;
//   try {
//       System.out.println("Enter playerid, agentid");
//       int playerid = sc.nextInt();
//       int agentid = sc.nextInt();
//       String query = "UPDATE players SET agentid = " + agentid + " WHERE playerid = " + playerid;

//       stmt = conn.createStatement();
//       conn.setAutoCommit(false); // Start transaction
//       stmt.executeUpdate(query);
//       conn.commit(); // Commit transaction
//       System.out.println("Committed successfully");
//   } catch(SQLException se) {
//       // If there is an error then rollback the changes.
//       if (conn != null) {
//           try {
//               System.err.println("Transaction is being rolled back");
//               conn.rollback();
//           } catch(SQLException excep) {
//               excep.printStackTrace();
//           }
//       }
//       se.printStackTrace();
//   } finally {
//       // Close resources
//       if (stmt != null) {
//           try {
//               stmt.close();
//           } catch(SQLException se) {
//               se.printStackTrace();
//           }
//       }
//       try {
//           conn.setAutoCommit(true);
//       } catch(SQLException se) {
//           se.printStackTrace();
//       }
//   }
// }

  public static void main(String[] args) {
    Connection conn = null;
    Statement stmt = null;
    Scanner sc = new Scanner(System.in);

    // String createEmployee = "CREATE TABLE employee (" +
    // "fname VARCHAR(30), " +
    // "minit CHAR(1), " +
    // "lname VARCHAR(30), " +
    // "ssn CHAR(9), " +
    // "bdate DATE, " +
    // "address VARCHAR(30), " +
    // "sex CHAR(1), " +
    // "salary DECIMAL(10,2), " +
    // "super_ssn CHAR(9), " +
    // "dno SMALLINT, " +
    // "CONSTRAINT pk_employee PRIMARY KEY (ssn)" +
    // ")";

    // STEP 2. Connecting to the Database
    try {
      // STEP 2a: Register JDBC driver
      Class.forName(JDBC_DRIVER);
      // STEP 2b: Open a connection
      System.out.println("Connecting to database...");
      conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
      // STEP 2c: Execute a query
      System.out.println("Creating statement...");
      stmt = conn.createStatement();

      // // INSERT, UPDATE, DELETE
      // stmt.executeUpdate(createEmployee);

      // STEP 3: Query to database
      int p = -1;
      // int p=0;
      while (p != 0) {
        System.out.println();
        System.out.println(
          "Enter 1 for player operations\n2 for team operations\n3 for games operations\n4 to display all the tables\n5 for displaying which team which player plays for\n6 for coach operations\n7 for agent operations\n0 to exit\n"
        );
        // p = sc.nextInt();
        // if missmactch in input, it will keep on asking for input
        do {
          try {
            System.out.print("Enter an integer: ");
            System.out.println();
            p = sc.nextInt();
            break; // if input is valid, break the loop
          } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter an integer.");
            sc.next(); // discard the invalid input
          }
        } while (true);

        System.out.println(p);

        if (p == 1) {
          System.out.println(
            "Enter 1 for showing player\n2 to add player\n3 to delete player\n4 to UPDATE players\n5 for salary\0 to exit\n"
          );
          int q;
          do {
            try {
              System.out.print("Enter an integer: ");
              System.out.println();
              q = sc.nextInt();
              break; // if input is valid, break the loop
            } catch (InputMismatchException e) {
              System.out.println("Invalid input. Please enter an integer.");
              sc.next(); // discard the invalid input
            }
          } while (true);
          if (q == 1) showplayers(stmt); else if (q == 2) addplayers(
            conn,
            sc);
           else if (q == 3) deleteplayers(conn,sc); else if (
            q == 4
          ) updateplayers(conn, sc); else if (q == 5) salaryplayers(stmt, sc);
          // else if(q==6)alterplayers(stmt, sc);
          else if (q == 0) {
            System.out.println("Exiting");
          } else {
            System.out.println("Invalid input");
          }
        } else if (p == 2) {
          System.out.println(
            "Enter 1 for showing team\n2 to add team\n3 to delete team\n4 to update team\n0 to exit\n"
          );
          int q;
          do {
            try {
              System.out.print("Enter an integer: ");
              System.out.println();
              q = sc.nextInt();
              break; // if input is valid, break the loop
            } catch (InputMismatchException e) {
              System.out.println("Invalid input. Please enter an integer.");
              sc.next(); // discard the invalid input
            }
          } while (true);
          if (q == 1) showteams(stmt); else if (q == 2) 
            addteams(conn, sc);
          
          else if (q == 3) deleteteams(conn,sc); else if (
            q == 4
          ) updateteams(conn,sc);
          // else if(q==5)alterteams(stmt, sc);
          else if (q == 0) {
            System.out.println("Exiting");
          } else {
            System.out.println("Invalid input");
          }
        } else if (p == 3) {
          System.out.println(
            "Enter 1 for showing games\n2 to add games\n3 to delete games\n4 to show motm for a game\n0 to exit\n"
          );
          int q;
          do {
            try {
              System.out.print("Enter an integer: ");
              System.out.println();
              q = sc.nextInt();
              break; // if input is valid, break the loop
            } catch (InputMismatchException e) {
              System.out.println("Invalid input. Please enter an integer.");
              sc.next(); // discard the invalid input
            }
          } while (true);
          if (q == 1) showgames(stmt); else if (q == 2) addgames(
            conn,
            sc
          ); else if (q == 3) deletegames(conn, sc); else if (q == 4) showmotm(
            stmt
          ); else if (q == 0) {
            System.out.println("Exiting");
          } else {
            System.out.println("Invalid input");
          }
        } else if (p == 4) {
          showfullschema(stmt);
        } else if (p == 5) {
          showplayerteam(stmt);
        } else if (p == 6) {
          System.out.println(
            "Enter 1 for showing coach\n2 to add and assign coach\n3 to delete coach\n0 to exit\n"
          );
          int q;
          do {
            try {
              System.out.print("Enter an integer: ");
              System.out.println();
              q = sc.nextInt();
              break; // if input is valid, break the loop
            } catch (InputMismatchException e) {
              System.out.println("Invalid input. Please enter an integer.");
              sc.next(); // discard the invalid input
            }
          } while (true);
          if (q == 1) showcoach(stmt); else if (q == 2) addAndAssignCoach(conn, sc);
          else if (q == 3) deletecoach(conn, sc);
           else if (q == 0) {
            System.out.println("Exiting");
          } else {
            System.out.println("Invalid input");
          }
        } else if (p == 7) {
          System.out.println(
            "Enter 1 for showing agent\n2 to add and assign agent\n3 to delete agent\n4 to update agent\n0 to exit"
          );
          int q;
          do {
            try {
              System.out.print("Enter an integer: ");
              System.out.println();
              q = sc.nextInt();
              break; // if input is valid, break the loop
            } catch (InputMismatchException e) {
              System.out.println("Invalid input. Please enter an integer.");
              sc.next(); // discard the invalid input
            }
          } while (true);
          if (q == 1) showagent(stmt);
           else if (q == 2) addAndAssignAgent(conn, sc);
          else if (q == 3) deleteagent(conn, sc); 
          else if (q == 4) 
            updateagent(conn, sc);
           else if (q ==0) {
            System.out.println("Exiting");
          } else {
            System.out.println("Invalid input");
          }
        } else if (p == 0) {
          System.out.println("Exiting");
        } else {
          System.out.println("Invalid input");
        }
        // STEP 5: Clean-up environment
        // rs.close();

      } // end while
      stmt.close();
      conn.close();
    } catch (SQLException se) { // Handle errors for JDBC
      se.printStackTrace();
    } catch (Exception e) { // Handle errors for Class.forName
      e.printStackTrace();
    } finally { // finally block used to close resources regardless of whether an exception was
      // thrown or not
      try {
        if (stmt != null) stmt.close();
      } catch (SQLException se2) {}
      try {
        if (conn != null) conn.close();
      } catch (SQLException se) {
        se.printStackTrace();
      } // end finally try
    } // end try

    System.out.println("End of Code");
  } // end main
} // end class
// Note : By default autocommit is on. you can set to false using
// con.setAutoCommit(false)
