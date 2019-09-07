
package drVaccine;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class HomePage implements ActionListener,ListSelectionListener {
    JFrame frame;
    JLabel txtWelcome;
    JButton btnLogout;
    DefaultListModel vaxModel;
    JList listVaccine;
    JComboBox chooseTip;
    DefaultListModel tipModel;
    JList listTip;
    
    int baby_id;
    
    String connectionUrl = "jdbc:sqlserver://DARKWOLF:1433;databaseName=Vax;integratedSecurity=true;";
    Connection con = null;
    Statement stmt = null;
    ResultSet rs = null;
    
    HomePage(int id){
        baby_id=id;
        frame=new JFrame("Welcome user..!");
        
        txtWelcome=new JLabel("Welcome..!");
        txtWelcome.setBounds(150,10,180,25);
        
        btnLogout=new JButton("Logout");
        btnLogout.setBounds(370,10,100,30);
        btnLogout.addActionListener(this);
        
        vaxModel=new DefaultListModel();
        listVaccine=new JList(vaxModel);
        listVaccine.setBounds(10,40,200,280);
        listVaccine.addListSelectionListener(this);
        
        chooseTip=new JComboBox();
        chooseTip.setBounds(230,50,200,25);
        tipModel=new DefaultListModel();
        listTip=new JList(tipModel);
        listTip.setBounds(230,80,240,240);
        
        frame.add(txtWelcome);
        frame.add(btnLogout);
        frame.add(listVaccine);
        frame.add(chooseTip);
        frame.add(listTip);
        
        frame.setSize(490,370);
        frame.setLayout(null);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        loadData();
    }

    private void loadData(){
        //load name
        try{
            // Establish the connection
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection(connectionUrl);

            // Create and execute an SQL statement that returns some data
            String SQL = "SELECT  baby_name from baby where baby_id = "+baby_id;
            
            stmt = con.createStatement();
            rs = stmt.executeQuery(SQL);

            rs.next();
            txtWelcome.setText("Welcome "+rs.getString("baby_name"));
            
            rs.close();
            stmt.close();
            con.close();
        }
        catch(Exception ex){
            JOptionPane.showMessageDialog(frame, "An error has occured..!"+ex.getMessage());
        }
        
        //load data in list
        try{
            // Establish the connection
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection(connectionUrl);

            // Create and execute an SQL statement that returns some data
            String SQL = "SELECT  * FROM vaccines";
            stmt = con.createStatement();
            rs = stmt.executeQuery(SQL);

            while(rs.next()){
                vaxModel.addElement(new Vaccine(rs.getInt("vac_id"),rs.getInt("vac_age"),rs.getString("vac_name"),rs.getString("vac_description")));
            }
            rs.close();
            stmt.close();
            con.close();
        }
        catch(Exception ex){
            JOptionPane.showMessageDialog(frame, "An error has occured..!"+ex.getMessage());
        }
        
        //load data in combobox
        try{
            // Establish the connection
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection(connectionUrl);

            // Create and execute an SQL statement that returns some data
            String SQL = "SELECT distinct category from childCare";
            stmt = con.createStatement();
            rs = stmt.executeQuery(SQL);

            while(rs.next()){
                chooseTip.addItem(rs.getString("category"));
            }
            chooseTip.addActionListener(this);
            if(chooseTip.getItemCount()>0)
                chooseTip.setSelectedIndex(0);
            rs.close();
            stmt.close();
            con.close();
        }
        catch(Exception ex){
            JOptionPane.showMessageDialog(frame, "An error has occured..!"+ex.getMessage());
        }

    }
    @Override
    public void actionPerformed(ActionEvent ae) {
        if(ae.getSource()==btnLogout){
            baby_id=-1;
            frame.setVisible(false);
            new WelcomePage();
            frame.dispose();
        }
        if(ae.getSource()==chooseTip){
            String cat=(String)chooseTip.getSelectedItem();
            tipModel.clear();
            //load data in combobox
            try{
                // Establish the connection
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                con = DriverManager.getConnection(connectionUrl);

                // Create and execute an SQL statement that returns some data
                String SQL = "SELECT tips_desc from childCare where category='"+cat+"'";
                stmt = con.createStatement();
                rs = stmt.executeQuery(SQL);

                while(rs.next()){
                    tipModel.addElement(rs.getString("tips_desc"));
                }
                try{
                    rs.close();
                    stmt.close();
                    con.close();
                }
                catch(Exception ex){}
            }
            catch(Exception ex){
                JOptionPane.showMessageDialog(frame, "An error has occured..!"+ex.getMessage());
            }
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent lse) {
        if(!lse.getValueIsAdjusting()){
            int index = listVaccine.getSelectedIndex();
            Vaccine vax = (Vaccine)vaxModel.getElementAt(index);
            String msg="Details about "+vax.getVacName()+":"
                    +"\nDescription : "+vax.getVacDesc()
                    +"\nFirst dose at age : "+vax.getVacMonth()+" months";
            
            try{
                // Establish the connection
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                con = DriverManager.getConnection(connectionUrl);

                // Create and execute an SQL statement that returns some data
                String SQL = "SELECT  month FROM doses where vac_id="+vax.getVacId();
                stmt = con.createStatement();
                rs = stmt.executeQuery(SQL);

                while(rs.next()){
                    msg+="\n\nGet a dose at age :"+rs.getInt("month")+" months";
                }
                rs.close();
                stmt.close();
                con.close();
            }
            catch(Exception ex){
                JOptionPane.showMessageDialog(frame, "An error has occured..!"+ex.getMessage());
            }
            JOptionPane.showMessageDialog(frame, msg);
        }
    }
}
