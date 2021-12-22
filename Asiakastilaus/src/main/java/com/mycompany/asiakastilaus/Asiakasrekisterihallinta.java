/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.asiakastilaus;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author Kone1
 */
public class Asiakasrekisterihallinta extends javax.swing.JFrame {
    
    

    /**
     * Creates new form Asiakasrekisterihallinta
     */
    class Asiakas {

        private int ASIAKASNUMERO;
        private String ASIAKKAAKSITULOPAIVA;
        private String ETUNIMI;
        private String SUKUNIMI;
        private String YRITYS;
        private String KATUOSOITE;
        private String POSTINUMERO;
        private String POSTITOIMIPAIKKA;
        private String PUHELIN;
        private String EMAIL;

        public Asiakas(int id, String paiva, String etunimi, String sukunimi, String yritys, String katuosoite, String postinumero, String postitoimipaikka, String puhelin, String email) {
            this.ASIAKASNUMERO = id;
            this.ASIAKKAAKSITULOPAIVA = paiva;
            this.ETUNIMI = etunimi;
            this.SUKUNIMI = sukunimi;
            this.YRITYS = yritys;
            this.KATUOSOITE = katuosoite;
            this.POSTINUMERO = postinumero;
            this.POSTITOIMIPAIKKA = postitoimipaikka;
            this.PUHELIN = puhelin;
            this.EMAIL = email;
        }

        public int HaeAsiakasnumero() {
            return this.ASIAKASNUMERO;
        }

        public String HaePaiva() {
            return this.ASIAKKAAKSITULOPAIVA;
        }
        
        public String HaeEtunimi() {
            return this.ETUNIMI;
        }

        public String HaeSukunimi() {
            return this.SUKUNIMI;
        }

        public String HaeYritys() {
            return this.YRITYS;
        }
        
        public String HaeKatuosoite() {
            return this.KATUOSOITE;
        }
        
        public String HaePostinumero() {
            return this.POSTINUMERO;
        }
        
        public String HaePostitoimipaikka() {
            return this.POSTITOIMIPAIKKA;
        }
        
        public String HaePuhelin() {
            return this.PUHELIN;
        }
        
        public String HaeEmail() {
            return this.EMAIL;
        }
    }

    public Connection luoYhteys() {
        Connection cn = null;
        // Muuta oman Amazon palvelimesi osoite, tietokannan nimi sekä käyttäjätunnus ja salasana, jolla yhteys muodostetaan
        try {
            cn = DriverManager.
                    getConnection("jdbc:mysql://" + "ec2-16-170-133-46.eu-north-1.compute.amazonaws.com" + ":3306/ASIAKASTILAUS", "kehittaja", "Koira123!");
            return cn;
        } catch (SQLException e) {
            System.out.println("Yhteyden luominen epäonnistui!:\n" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<Asiakas> HaeAsiakastaulukko() {
        ArrayList<Asiakas> Asiakastaulukko = new ArrayList<Asiakas>();

        Connection yhteys = luoYhteys();
        // SQL kysely, joka hakee tarvittavat kentät
        String query = "SELECT ASIAKASNUMERO, ASIAKKAAKSITULOPAIVA , ETUNIMI, SUKUNIMI, YRITYS, KATUOSOITE, POSTINUMERO, POSTITOIMIPAIKKA, PUHELIN, EMAIL FROM ASIAKAS";
        Statement st;
        ResultSet rs;

        try {
            st = yhteys.createStatement();
            rs = st.executeQuery(query);
            Asiakas a;
            // Lisätään kaikki asiakkaat taulukkoon
            while (rs.next()) {
                a = new Asiakas(rs.getInt("ASIAKASNUMERO"), rs.getString("ASIAKKAAKSITULOPAIVA"), rs.getString("ETUNIMI"), rs.getString("SUKUNIMI"), rs.getString("YRITYS"), rs.getString("kATUOSOITE"), rs.getString("POSTINUMERO"), rs.getString("POSTITOIMIPAIKKA"), rs.getString("PUHELIN"), rs.getString("EMAIL"));
                Asiakastaulukko.add(a);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Asiakastaulukko;
    }

    public void Naytaasiakkaat() {
        
        ArrayList<Asiakas> list = HaeAsiakastaulukko();
        DefaultTableModel model = (DefaultTableModel) jtblAsiakkaat.getModel();
        // Luodaan sarakeotsikot
        model.setColumnIdentifiers(new Object[]{"ASIAKASNUMERO", "ASIAKKAAKSITULOPAIVA", "ETUNIMI", "SUKUNIMI", "YRITYS", "KATUOSOITE", "POSTINUMERO", "POSTITOIMIPAIKKA", "PUHELIN", "EMAIL"});
        Object[] row = new Object[10];

        // Putsataan taulukko ennen täyttämistä
        for (int i = jtblAsiakkaat.getRowCount() - 1; i >= 0; i--) {
            model.removeRow(i);
        }
        // Täytetään taulukko uudella datalla
        for (int i = 0; i < list.size(); i++) {
            row[0] = list.get(i).HaeAsiakasnumero();
            row[1] = list.get(i).HaePaiva();
            row[2] = list.get(i).HaeEtunimi();
            row[3] = list.get(i).HaeSukunimi();
            row[4] = list.get(i).HaeYritys();
            row[5] = list.get(i).HaeKatuosoite();
            row[6] = list.get(i).HaePostinumero();
            row[7] = list.get(i).HaePostitoimipaikka();
            row[8] = list.get(i).HaePuhelin();
            row[9] = list.get(i).HaeEmail();
            model.addRow(row);
        }
    }

    // SQL-kyselyt
    public void suoritaSQLKysely(String query, String message) {
        // Luodaan yhteys tietokantaan
        Connection yhteys = luoYhteys();
        Statement st;
        try {
            st = yhteys.createStatement();
            if ((st.executeUpdate(query)) == 1) {
                // päivitetään jtblAsiakkaat taulukko
                DefaultTableModel model = (DefaultTableModel) jtblAsiakkaat.getModel();
                model.setRowCount(0);
                Naytaasiakkaat();

                JOptionPane.showMessageDialog(null, "Data " + message + " onnistuneesti");
            } else {
                JOptionPane.showMessageDialog(null, "Data ei " + message);
            }
            // Suljetaan tietokantayhteys
            yhteys.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Asiakasrekisterihallinta() {
        initComponents();
        //metodi käyttöön avautuessa
        Naytaasiakkaat();
        //asetetaan avautumaan keskelle näyttöö
        this.setLocationRelativeTo(null);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jlblAsiakasrekisteri = new javax.swing.JLabel();
        jlblAsiakasnumero = new javax.swing.JLabel();
        jlblEtunimi = new javax.swing.JLabel();
        jlblSukunimi = new javax.swing.JLabel();
        jlblYritys = new javax.swing.JLabel();
        jtxtAsiakasnumero = new javax.swing.JTextField();
        jtxtEtunimi = new javax.swing.JTextField();
        jtxtSukunimi = new javax.swing.JTextField();
        jtxtYritys = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtblAsiakkaat = new javax.swing.JTable();
        jbtnUusi = new javax.swing.JButton();
        jbtnPaivita = new javax.swing.JButton();
        jbtnPoista = new javax.swing.JButton();
        jlblKatuosoite = new javax.swing.JLabel();
        jtxtKatuosoite = new javax.swing.JTextField();
        jlblPostinumero = new javax.swing.JLabel();
        jtxtPostinumero = new javax.swing.JTextField();
        jlblPostitoimipaikka = new javax.swing.JLabel();
        jtxtPostitoimipaikka = new javax.swing.JTextField();
        jlblPuhelin = new javax.swing.JLabel();
        jtxtPuhelin = new javax.swing.JTextField();
        jlblEmail = new javax.swing.JLabel();
        jtxtEmail = new javax.swing.JTextField();
        jbtnTyhjenna = new javax.swing.JButton();
        jtxtSQL = new javax.swing.JTextField();
        jlblpaiva = new javax.swing.JLabel();
        jtxtpaiva = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jlblAsiakasrekisteri.setText("Asiakasrekisteri");

        jlblAsiakasnumero.setText("Asiakasnumero:");

        jlblEtunimi.setText("Etunimi:");

        jlblSukunimi.setText("Sukunimi:");

        jlblYritys.setText("Yritys:");

        jtxtAsiakasnumero.setEditable(false);

        jtblAsiakkaat.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jtblAsiakkaat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtblAsiakkaatMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jtblAsiakkaat);

        jbtnUusi.setText("Uusi");
        jbtnUusi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnUusiActionPerformed(evt);
            }
        });

        jbtnPaivita.setText("Päivitä");
        jbtnPaivita.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnPaivitaActionPerformed(evt);
            }
        });

        jbtnPoista.setText("Poista");
        jbtnPoista.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnPoistaActionPerformed(evt);
            }
        });

        jlblKatuosoite.setText("Katuosoite:");

        jlblPostinumero.setText("Postinumero:");

        jlblPostitoimipaikka.setText("Postitoimipaikka:");

        jlblPuhelin.setText("Puhelin:");

        jlblEmail.setText("Email:");

        jbtnTyhjenna.setText("Tyhjennä");
        jbtnTyhjenna.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnTyhjennaActionPerformed(evt);
            }
        });

        jlblpaiva.setText("Asiakkaaksitulopvä");

        jtxtpaiva.setEditable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(19, 19, 19)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jlblAsiakasrekisteri)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jlblEtunimi)
                                            .addComponent(jlblSukunimi)
                                            .addComponent(jlblYritys)
                                            .addComponent(jlblKatuosoite)
                                            .addComponent(jlblPostinumero))
                                        .addGap(40, 40, 40)))
                                .addGap(18, 18, 18))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(19, 19, 19)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jlblPostitoimipaikka)
                                            .addComponent(jlblPuhelin)
                                            .addComponent(jlblEmail)))
                                    .addComponent(jlblpaiva))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jtxtKatuosoite, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jtxtPostitoimipaikka, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jtxtPuhelin)
                                        .addComponent(jtxtEmail, javax.swing.GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
                                        .addComponent(jtxtEtunimi, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jtxtSukunimi, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jtxtYritys, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jtxtPostinumero, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jtxtpaiva, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(29, 29, 29))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jlblAsiakasnumero)
                        .addGap(30, 30, 30)
                        .addComponent(jtxtAsiakasnumero, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(69, 69, 69)))
                .addComponent(jScrollPane1)
                .addGap(14, 14, 14))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jbtnUusi)
                .addGap(32, 32, 32)
                .addComponent(jbtnPaivita)
                .addGap(35, 35, 35)
                .addComponent(jbtnPoista)
                .addGap(26, 26, 26)
                .addComponent(jbtnTyhjenna)
                .addGap(18, 18, 18)
                .addComponent(jtxtSQL, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(39, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jlblAsiakasrekisteri)
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jlblAsiakasnumero)
                            .addComponent(jtxtAsiakasnumero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(19, 19, 19)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jlblpaiva)
                            .addComponent(jtxtpaiva, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jtxtEtunimi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jlblEtunimi, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jtxtSukunimi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jlblSukunimi, javax.swing.GroupLayout.PREFERRED_SIZE, 8, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jlblYritys)
                            .addComponent(jtxtYritys, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jlblKatuosoite)
                            .addComponent(jtxtKatuosoite, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jlblPostinumero)
                            .addComponent(jtxtPostinumero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jlblPostitoimipaikka)
                            .addComponent(jtxtPostitoimipaikka, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jlblPuhelin)
                            .addComponent(jtxtPuhelin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jlblEmail)
                            .addComponent(jtxtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jbtnUusi)
                        .addComponent(jbtnPaivita)
                        .addComponent(jbtnPoista)
                        .addComponent(jbtnTyhjenna))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jtxtSQL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jtblAsiakkaatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtblAsiakkaatMouseClicked
        // TODO add your handling code here:
        // Haetaan käyttöliittymästä klikattu rivinumero
        int i = jtblAsiakkaat.getSelectedRow();
        TableModel model = jtblAsiakkaat.getModel();
        // Asetetaan käyttöliittymään tiedot
        jtxtAsiakasnumero.setText((model.getValueAt(i, 0).toString()));
        jtxtpaiva.setText((model.getValueAt(i, 1).toString()));
        jtxtEtunimi.setText((model.getValueAt(i, 2).toString()));
        jtxtSukunimi.setText((model.getValueAt(i, 3).toString()));
        jtxtYritys.setText((model.getValueAt(i, 4).toString()));
        jtxtKatuosoite.setText((model.getValueAt(i, 5).toString()));
        jtxtPostinumero.setText((model.getValueAt(i, 6).toString()));
        jtxtPostitoimipaikka.setText((model.getValueAt(i, 7).toString()));
        jtxtPuhelin.setText((model.getValueAt(i, 8).toString()));
        jtxtEmail.setText((model.getValueAt(i, 9).toString()));
    }//GEN-LAST:event_jtblAsiakkaatMouseClicked

    private void jbtnUusiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnUusiActionPerformed
        // esitellään mistä kentistä haetaan tiedot
        String Etunimi = jtxtEtunimi.getText();
        String Sukunimi = jtxtSukunimi.getText();
        String Yritys = jtxtYritys.getText();
        String Katuosoite = jtxtKatuosoite.getText();
        String Postinumero = jtxtPostinumero.getText();
        String Postitoimipaikka = jtxtPostitoimipaikka.getText();
        String Puhelin = jtxtPuhelin.getText();
        String Email = jtxtEmail.getText();
        //tarkastetaan onko joku ylläolevista kentistä tyhjä ja jos on niin huomautus ja keskeytys
         if (Etunimi.isEmpty() || Sukunimi.isEmpty() || Yritys.isEmpty() || Katuosoite.isEmpty() || Postinumero.isEmpty() || Postitoimipaikka.isEmpty() || Puhelin.isEmpty() || Email.isEmpty()){
             JOptionPane.showMessageDialog(null, " Jokin kenttä on tyhjä. Korjaa! ","virhe", JOptionPane.ERROR_MESSAGE);
             return;
        }
        // muutetaan vastaus vaihtoehdot kyllä ja ei
        UIManager.put("OptionPane.yesButtonText","Kyllä");
        UIManager.put("OptionPane.noButtonText","Ei");
        // varmistetaan JOptionPane-ikkunalla halutaanko varmasti suorittaa valittu    
        int kysy = JOptionPane.showConfirmDialog(null, "Haluatko lisätä uuden asiakkaan" , "Lisäys", JOptionPane.YES_NO_OPTION);
        if (kysy == JOptionPane.NO_OPTION) {
           return;
        }
        // Muotoillaan päivämäärä muotoon yyyy/MM/dd
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        // Haetaan järjestelmän päiväys
        Date tamapaiva = new Date();
        // Muodostetaan INSERT lause, huomaa + ja ' merkkien käyttäminen 
        /*String query = "INSERT INTO `ASIAKAS`(`ASIAKKAAKSITULOPAIVA`, `YRITYS`, `ETUNIMI`, `SUKUNIMI`,`KATUOSOITE`, `POSTINUMERO`, `POSTITOIMIPAIKKA`, `PUHELIN`, `EMAIL`)";
        query = query + " VALUES('" + dateFormat.format(tamapaiva) + "','" +jtxtYritys.getText()+"','"+jtxtEtunimi.getText()+"', '"+jtxtSukunimi.getText() + "', '" + jtxtKatuosoite.getText() + "', '" + jtxtPostinumero.getText() + "', '" + jtxtPostitoimipaikka.getText() + "', '" + jtxtPuhelin.getText() + "', '" + jtxtEmail.getText()+"')";
        //query = query + "','','','','','')";
        //JOptionPane.showMessageDialog(null, query);
        // luomisvaiheessa SQL merkkijonoa kannattaa testata ja kehittää phpMyAdmin-ohjelmassa, välituloksia, joita voi kopioida, kannattaa tulostaa sovelluksen käyttöliittymän tai Debug-ikkunaan
        jtxtSQL.setText(query);
        suoritaSQLKysely(query, "lisätty");*/
        
        //turvallisempi tapa tehdä uusi asiakas
        try {
            Connection yhteys = luoYhteys();
            String sqluusi = "INSERT INTO ASIAKAS (ASIAKKAAKSITULOPAIVA, ETUNIMI, SUKUNIMI, YRITYS, KATUOSOITE, POSTINUMERO, POSTITOIMIPAIKKA, PUHELIN, EMAIL) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement ps = yhteys.prepareStatement(sqluusi);
            ps.setString(1, dateFormat.format(tamapaiva));
            ps.setString(2, jtxtEtunimi.getText());
            ps.setString(3, jtxtSukunimi.getText());
            ps.setString(4, jtxtYritys.getText());
            ps.setString(5, jtxtKatuosoite.getText());
            ps.setString(6, jtxtPostinumero.getText());
            ps.setString(7, jtxtPostitoimipaikka.getText());
            ps.setString(8, jtxtPuhelin.getText());
            ps.setString(9, jtxtEmail.getText());
            
            int vastaus = ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Yhteyden luominen epäonnistui ta jotain muuta outoa sattui!" + e.getMessage());
        }
        Naytaasiakkaat();
    }//GEN-LAST:event_jbtnUusiActionPerformed

    private void jbtnPaivitaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnPaivitaActionPerformed
        // TODO add your handling code here:
        String Etunimi = jtxtEtunimi.getText();
        String Sukunimi = jtxtSukunimi.getText();
        String Yritys = jtxtYritys.getText();
        String Katuosoite = jtxtKatuosoite.getText();
        String Postinumero = jtxtPostinumero.getText();
        String Postitoimipaikka = jtxtPostitoimipaikka.getText();
        String Puhelin = jtxtPuhelin.getText();
        String Email = jtxtEmail.getText();
        
         if (Etunimi.isEmpty() || Sukunimi.isEmpty() || Yritys.isEmpty() || Katuosoite.isEmpty() || Postinumero.isEmpty() || Postitoimipaikka.isEmpty() || Puhelin.isEmpty() || Email.isEmpty()){
             JOptionPane.showMessageDialog(null, " Jokin kenttä on tyhjä. Korjaa! ","virhe", JOptionPane.ERROR_MESSAGE);
             return;
        }
                
        // muutetaan vastaus vaihtoehdot kyllä ja ei
        UIManager.put("OptionPane.yesButtonText","Kyllä");
        UIManager.put("OptionPane.noButtonText","Ei");
        // varmistetaan JOptionPane-ikkunalla halutaanko varmasti suorittaa valittu    
        int kysy = JOptionPane.showConfirmDialog(null, "Haluatko varmasti päivittää tiedot" , "Päivitys", JOptionPane.YES_NO_OPTION);
        if (kysy == JOptionPane.NO_OPTION) {
           return;
        }
        /*String query = "UPDATE ASIAKAS SET ETUNIMI='" + jtxtEtunimi.getText() + "',SUKUNIMI='" + jtxtSukunimi.getText() + "', YRITYS='" + jtxtYritys.getText() + "',KATUOSOITE='" + jtxtKatuosoite.getText() + "',POSTINUMERO='" + jtxtPostinumero.getText() + "',POSTITOIMIPAIKKA='" + jtxtPostitoimipaikka.getText() + "',PUHELIN='" + jtxtPuhelin.getText() + "',EMAIL='" + jtxtEmail.getText() + "' WHERE ASIAKASNUMERO =" + jtxtAsiakasnumero.getText();
        JOptionPane.showMessageDialog(null, query);
        jtxtSQL.setText(query);
        suoritaSQLKysely(query, "Päivitetty");*/
        
        //turvallisempi tapa muokata asiakasta
        try {
            Connection yhteys = luoYhteys();
            String sqlpaivita = "UPDATE ASIAKAS SET ETUNIMI=?, SUKUNIMI=?, YRITYS=?, KATUOSOITE=?, POSTINUMERO=?, POSTITOIMIPAIKKA=?, PUHELIN=?, EMAIL=? WHERE ASIAKASNUMERO=?";
            PreparedStatement ps = yhteys.prepareStatement(sqlpaivita);
            ps.setString(1, jtxtEtunimi.getText());
            ps.setString(2, jtxtSukunimi.getText());
            ps.setString(3, jtxtYritys.getText());
            ps.setString(4, jtxtKatuosoite.getText());
            ps.setString(5, jtxtPostinumero.getText());
            ps.setString(6, jtxtPostitoimipaikka.getText());
            ps.setString(7, jtxtPuhelin.getText());
            ps.setString(8, jtxtEmail.getText());
            ps.setString(9, jtxtAsiakasnumero.getText());
            int vastaus = ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Yhteyden luominen epäonnistui ta jotain muuta outoa sattui!" + e.getMessage());
        }
        Naytaasiakkaat();
    }//GEN-LAST:event_jbtnPaivitaActionPerformed

    private void jbtnPoistaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnPoistaActionPerformed
        // TODO add your handling code here:
        // muutetaan vastaus vaihtoehdot kyllä ja ei
        UIManager.put("OptionPane.yesButtonText","Kyllä");
        UIManager.put("OptionPane.noButtonText","Ei");
        // varmistetaan JOptionPane-ikkunalla halutaanko varmasti suorittaa valittu    
        int kysy = JOptionPane.showConfirmDialog(null, "Haluatko varmasti poistaa asiakkaan" , "Poisto", JOptionPane.YES_NO_OPTION);
        if (kysy == JOptionPane.NO_OPTION) {
           return;
        }
        /*String query = "DELETE FROM ASIAKAS WHERE ASIAKASNUMERO=" + jtxtAsiakasnumero.getText();
        ////JOptionPane.showMessageDialog(null, query);
        jtxtSQL.setText(query);
        suoritaSQLKysely(query, "Poistettu");*/
        
        //turvallisempi tapa poistaa asiakas
        try {
            Connection yhteys = luoYhteys();
            String sqlpois = "DELETE FROM ASIAKAS WHERE ASIAKASNUMERO=?";
            PreparedStatement ps = yhteys.prepareStatement (sqlpois);
            ps.setString(1, jtxtAsiakasnumero.getText());
            
            int vastaus = ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Yhteyden luominen epäonnistui ta jotain muuta outoa sattui!" + e.getMessage());            
        }
        Naytaasiakkaat();
    }//GEN-LAST:event_jbtnPoistaActionPerformed
   
    private void jbtnTyhjennaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnTyhjennaActionPerformed
        // tyhjennetään kentät
        jtxtAsiakasnumero.setText("");
        jtxtpaiva.setText("");
        jtxtEtunimi.setText("");
        jtxtSukunimi.setText("");
        jtxtYritys.setText("");
        jtxtKatuosoite.setText("");
        jtxtPostinumero.setText("");
        jtxtPostitoimipaikka.setText("");
        jtxtPuhelin.setText("");
        jtxtEmail.setText("");
        
    }//GEN-LAST:event_jbtnTyhjennaActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Asiakasrekisterihallinta.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Asiakasrekisterihallinta.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Asiakasrekisterihallinta.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Asiakasrekisterihallinta.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Asiakasrekisterihallinta().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jbtnPaivita;
    private javax.swing.JButton jbtnPoista;
    private javax.swing.JButton jbtnTyhjenna;
    private javax.swing.JButton jbtnUusi;
    private javax.swing.JLabel jlblAsiakasnumero;
    private javax.swing.JLabel jlblAsiakasrekisteri;
    private javax.swing.JLabel jlblEmail;
    private javax.swing.JLabel jlblEtunimi;
    private javax.swing.JLabel jlblKatuosoite;
    private javax.swing.JLabel jlblPostinumero;
    private javax.swing.JLabel jlblPostitoimipaikka;
    private javax.swing.JLabel jlblPuhelin;
    private javax.swing.JLabel jlblSukunimi;
    private javax.swing.JLabel jlblYritys;
    private javax.swing.JLabel jlblpaiva;
    private javax.swing.JTable jtblAsiakkaat;
    private javax.swing.JTextField jtxtAsiakasnumero;
    private javax.swing.JTextField jtxtEmail;
    private javax.swing.JTextField jtxtEtunimi;
    private javax.swing.JTextField jtxtKatuosoite;
    private javax.swing.JTextField jtxtPostinumero;
    private javax.swing.JTextField jtxtPostitoimipaikka;
    private javax.swing.JTextField jtxtPuhelin;
    private javax.swing.JTextField jtxtSQL;
    private javax.swing.JTextField jtxtSukunimi;
    private javax.swing.JTextField jtxtYritys;
    private javax.swing.JTextField jtxtpaiva;
    // End of variables declaration//GEN-END:variables
}
