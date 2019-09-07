package drVaccine;

import javax.swing.*;
import java.awt.event.*;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WelcomePage implements ActionListener{
    //Login elements
    JFrame frame;
    JLabel lblLogMail,lblLogPass;
    JTextField txtLogMail;
    JPasswordField txtLogPass;
    JButton btnLogin;
    JSeparator br;
    
    //Register elements
    JLabel lblReg,lblRegMail,lblRegName,lblRegDD,lblRegMM,lblRegYYYY,lblRegPass,lblRegCPass;
    JTextField txtRegMail,txtRegName,txtRegDD,txtRegMM,txtRegYYYY;
    JPasswordField txtRegPass,txtRegCPass;
    JButton btnRegister;
    
    String connectionUrl = "jdbc:sqlserver://DARKWOLF:1433;databaseName=Vax;integratedSecurity=true;";
    Connection con = null;
    Statement stmt = null;
    ResultSet rs = null;
    WelcomePage(){
        //Login elements
        frame=new JFrame("Welcome");
        lblLogMail=new JLabel("Enter email:");
        lblLogMail.setBounds(10,10,180,15);
        txtLogMail=new JTextField();
        txtLogMail.setBounds(10,25,180,30);
        
        lblLogPass=new JLabel("Enter password:");
        lblLogPass.setBounds(200,10,180,15);
        txtLogPass=new JPasswordField();
        txtLogPass.setBounds(200,25,180,30);
        
        btnLogin=new JButton("Login");
        btnLogin.setBounds(390,25,80,30);
        btnLogin.addActionListener(this);
        //end of Login elements
        
        br=new JSeparator();
        br.setBounds(10,70,460,2);
        
        //Register elements
        lblReg=new JLabel("New user..? Register here..");
        lblReg.setBounds(140,90,180,15);
        
        lblRegName=new JLabel("Enter name");
        lblRegName.setBounds(20,120,110,25);
        txtRegName=new JTextField();
        txtRegName.setBounds(20,140,210,30);
        
        lblRegMail=new JLabel("Enter mail");
        lblRegMail.setBounds(240,120,110,25);
        txtRegMail=new JTextField();
        txtRegMail.setBounds(240,140,210,30);
        
        lblRegPass=new JLabel("Enter password");
        lblRegPass.setBounds(20,180,110,25);
        txtRegPass=new JPasswordField();
        txtRegPass.setBounds(20,200,210,30);
        
        lblRegCPass=new JLabel("Confirm password");
        lblRegCPass.setBounds(240,180,110,25);
        txtRegCPass=new JPasswordField();
        txtRegCPass.setBounds(240,200,210,30);

        lblRegDD=new JLabel("DD");
        lblRegDD.setBounds(20,250,50,25);
        txtRegDD=new JTextField();
        txtRegDD.setBounds(20,270,50,30);
        
        lblRegMM=new JLabel("MM");
        lblRegMM.setBounds(80,250,50,25);
        txtRegMM=new JTextField();
        txtRegMM.setBounds(80,270,50,30);
        
        lblRegYYYY=new JLabel("YYYY");
        lblRegYYYY.setBounds(140,250,70,25);
        txtRegYYYY=new JTextField();
        txtRegYYYY.setBounds(140,270,70,30);
        
        btnRegister=new JButton("Register");
        btnRegister.setBounds(300,260,100,50);
        btnRegister.addActionListener(this);
        //end of Register elements
        
        frame.add(lblLogMail);
        frame.add(txtLogMail);
        frame.add(lblLogPass);
        frame.add(txtLogPass);
        frame.add(btnLogin);
        
        frame.add(br);
        
        frame.add(lblReg);
        frame.add(lblRegName);
        frame.add(txtRegName);
        frame.add(lblRegMail);
        frame.add(txtRegMail);
        frame.add(lblRegPass);
        frame.add(txtRegPass);
        frame.add(lblRegCPass);
        frame.add(txtRegCPass);
        frame.add(lblRegDD);
        frame.add(txtRegDD);
        frame.add(lblRegMM);
        frame.add(txtRegMM);
        frame.add(lblRegYYYY);
        frame.add(txtRegYYYY);
        frame.add(btnRegister);
        
        frame.setSize(490,370);
        frame.setLayout(null);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
    
    public void actionPerformed(ActionEvent e) {
        //login button pressed
        if(e.getSource()==btnLogin){
            String mail = txtLogMail.getText().trim();
            String pass = txtLogPass.getText().trim();
            login(mail,pass);
        }
        //register button pressed
        else if(e.getSource()==btnRegister){
            String name = txtRegName.getText().trim();
            String mail = txtRegMail.getText().trim();
            String pass = txtRegPass.getText().trim();
            String cPass = txtRegCPass.getText().trim();
            String dd = txtRegDD.getText().trim();
            String mm = txtRegMM.getText().trim();
            String yyyy = txtRegYYYY.getText().trim();
            
            if(name.length()<3){
                JOptionPane.showMessageDialog(frame, "Name too short..!");
                return;
            }
            
            String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
            Pattern p = Pattern.compile(ePattern);
            Matcher m = p.matcher(mail);
            if(!m.matches()){
                JOptionPane.showMessageDialog(frame, "Invalid email..!");
                return;
            }
            
            if(pass.length()<5){
                JOptionPane.showMessageDialog(frame, "Passwords too short..!");
                return;
            }
            if(!pass.equals(cPass)){
                JOptionPane.showMessageDialog(frame, "Passwords donot match..!");
                return;
            }
            
            String date=yyyy+"-"+mm+"-"+dd;
            try {
                if(Integer.parseInt(yyyy)<1970||Integer.parseInt(mm)>12){
                    JOptionPane.showMessageDialog(frame, "Invalid date..!");
                    return;
                }
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                df.setLenient(false);
                df.parse(date);
            }catch(Exception ex) {
                JOptionPane.showMessageDialog(frame, "Invalid date..!");
                return;
            }
            
            //all good till now
            try {
        	// Establish the connection
        	Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            	con = DriverManager.getConnection(connectionUrl);
                
            	// Create and execute an SQL statement that inserts data
            	String SQL = "insert into baby(baby_name,email,password,dob) values('"+name+"','"+mail+"','"+pass+"','"+date+"')";
            	stmt = con.createStatement();
            	int flag = stmt.executeUpdate(SQL);
                if(flag==1){
                    JOptionPane.showMessageDialog(frame, "User registered succesfully..! Login using the credentials");
                    con.close();
                    login(mail,pass);
                }
                else{
                    JOptionPane.showMessageDialog(frame, "Could not register user..!");
                }
                rs.close();
                stmt.close();
                con.close();
            }
            catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "An error has occured..!");
            }
        }
    }

    private void login(String mail, String pass) {
        try {
            // Establish the connection
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection(connectionUrl);

            // Create and execute an SQL statement that returns some data
            String SQL = "SELECT  baby_id FROM baby where email = '" + mail + "' and password = '" + pass + "'";
            stmt = con.createStatement();
            rs = stmt.executeQuery(SQL);

            //try to fetch the record
            rs.next();
            int baby_id = Integer.parseInt(rs.getString("baby_id"));
            
            //open new form
            frame.setVisible(false);
            new HomePage(baby_id);
            frame.dispose();
        } 
        catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "User not found");
        } 
        finally {
            if (rs != null) {
                try {
                    rs.close();
                } 
                catch (Exception ex) {
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } 
                catch (Exception ex) {
                }
            }
            if (con != null) {
                try {
                    con.close();
                } 
                catch (Exception ex) {
                }
            }
        }        
    }
  
}
