/*
 * Database.java
 * 
 * Modified on 2016-06-09
 */
package esame.lbd.pkg2016;

import java.sql.*;
import javax.swing.JOptionPane;
import oracle.jdbc.pool.OracleDataSource;

/**
 * Classe principale di connessione al database.
 *
 * @author Massimo
 * @author ADeLuca
 * @version 2016
 */
public class Database {

   /**
    * Indirizzo del server Oracle.
    */
   static public String host = "143.225.117.238";
   /**
    * Nome del servizio.
    */
   static public String servizio = "xe";
   /**
    * Porta utilizzata per la connessione.
    */
   static public int porta = 1521;
   /**
    * Nome utente per la connessione.
    */
   static public String user = "";
   /**
    * Password corrispondente all'utente specificato.
    */
   static public String password = "";
   /**
    * Nome dello schema contenente le tabelle/viste/procedure cui si vuole
    * accedere; coincide di solito con il nome utente.
    */
   static public String schema = "LAB16";
   /**
    * Oggetto DataSource utilizzato nella connessione al DB
    */
   static private OracleDataSource ods;
   /**
    * Variabile che contiene la connessione attiva, se esiste
    */
   static private Connection defaultConnection;

   /**
    * Creates a new instance of Database
    */
   public Database() {
   }

   /**
    * Restituisce la connessione di default al DB.
    *
    * @return Connessione di default (quella gi&agrave; attiva, o una nuova
    * ottenuta in base ai parametri di connessione attualmente impostati
    * @throws SQLException In caso di problemi di connessione
    */
   static public Connection getDefaultConnection() throws SQLException {
      if (defaultConnection == null || defaultConnection.isClosed()) {
         defaultConnection = nuovaConnessione();
//         System.out.println("nuova connessione");
//      } else {
//         System.out.println("ricicla connessione");
      }

      return defaultConnection;
   }

   /**
    * Imposta una connessione specificata in input come default.
    *
    * @param c Connessione al DB
    */
   static public void setDefaultConnection(Connection c) {
      defaultConnection = c;
   }

   /**
    * Restituisce una nuova connessione al DB.
    *
    * @return Connessione al DB secondo i parametri attualmente impostati
    * @throws java.sql.SQLException in caso di problemi di connessione
    */
   static public Connection nuovaConnessione() throws SQLException {
      ods = new OracleDataSource();
      ods.setDriverType("thin");
      ods.setServerName(host);
      ods.setPortNumber(porta);
      ods.setUser(user);
      ods.setPassword(password);
      ods.setDatabaseName(servizio);
      return ods.getConnection();
   }

   /**
    * Metodo per mostrare una finestra di dialogo con una (scarna, perch&eacute;
    * ottenuta da Oracle) descrizione dell'errore avvenuto. &Egrave; buona norma
    * usare descrizioni pi&uacute; informative quando si conoscono le possibili
    * eccezioni sollevate da un'applicazione.
    *
    * @param thrower Oggetto che ha lanciato l'eccezione
    * @param e Eccezione SQL da gestire
    */
   static public void mostraErroriSwing(java.awt.Component thrower,
           SQLException e) {
      String msg;
      msg = e.getMessage() + "\n";
      msg += "SQLState= " + e.getSQLState() + "\n";

      JOptionPane.showMessageDialog(thrower, msg, "Errore " + e.getErrorCode(),
              JOptionPane.ERROR_MESSAGE);
   }

   /**
    * Effettua una query e restituisce il primo valore.
    * 
    * @param query String contenente l'interrogazione
    * @return oggetto contenente la prima colonna della prima riga del risultato
    */
   static public Object leggiValore(String query) {
      Object ret;
      Connection con;
      Statement st;
      ResultSet rs;
      ret = null;
      try {
         con = getDefaultConnection();
         st = con.createStatement();
         rs = st.executeQuery(query);
         rs.next();
         ret = rs.getObject(1);
      } catch (SQLException e) {  //nessuna azione
      }
      return ret;
   }

   /**
    * Effettua una query e restituisce il primo valore.
    * 
    * @param query String contenente l'interrogazione con segnaposto
    * @param codice int per rimpiazzare il segnaposto
    * @return oggetto contenente la prima colonna della prima riga del risultato
    */
   static public Object leggiValore(String query, int codice) {
      Object ret;
      Connection con;
      PreparedStatement st;
      ResultSet rs;
      ret = null;
      try {
         con = getDefaultConnection();
         st = con.prepareStatement(query);
         st.setInt(1, codice);
         rs = st.executeQuery();
         rs.next();
         ret = rs.getObject(1);
      } catch (SQLException e) {
      }
      return ret;
   }
}
