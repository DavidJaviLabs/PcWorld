package src;

import java.sql.*;

public class InterfazMisDatos extends javax.swing.JFrame 
{
    /**
     * Creates new form InterfazMisDatos
     */
    public InterfazMisDatos() 
    {
        initComponents();
        
        cargarDatos();
        
        setVisible(true);
    }
    
    public void cargarDatos()
    {
        try {
            // Establecer la conexión a la base de datos
            Connection cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pcworld", "pcworld", "pcworld2024");

            // Verificar si el correo electrónico ya existe en la tabla
            String queryUser = "SELECT * FROM datos_clientes WHERE `Usuario` = ?";
            PreparedStatement pstmtUser = cn.prepareStatement(queryUser);
            pstmtUser.setString(1, InterfazPrincipal.getInstancia().botonCuenta.getText());
            ResultSet rsUser = pstmtUser.executeQuery();

            if (rsUser.next()) 
            {                
                labelUser.setText(rsUser.getString("Usuario"));
                labelGmail.setText(rsUser.getString("E-mail"));
            }
        } catch(Exception e) {
            System.out.println("Error: " + e);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        labelNombre = new javax.swing.JLabel();
        labelUser = new javax.swing.JLabel();
        labelCorreo = new javax.swing.JLabel();
        labelGmail = new javax.swing.JLabel();
        labelContraseña = new javax.swing.JLabel();
        labelAsteriscos = new javax.swing.JLabel();
        botonCambiarContraseña = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        labelNombre.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        labelNombre.setText("Nombre de Usuario: ");

        labelUser.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        labelUser.setText("                                        ");

        labelCorreo.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        labelCorreo.setText("Correo electrónico:");

        labelGmail.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        labelGmail.setText("                                        ");

        labelContraseña.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        labelContraseña.setText("Contraseña:");

        labelAsteriscos.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        labelAsteriscos.setText("*************************");

        botonCambiarContraseña.setText("Cambiar Contraseña");
        botonCambiarContraseña.setFocusPainted(false);
        botonCambiarContraseña.setFocusable(false);
        botonCambiarContraseña.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCambiarContraseñaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(labelContraseña, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelCorreo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelNombre, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelGmail, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelUser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelAsteriscos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(botonCambiarContraseña)
                .addGap(28, 28, 28))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelUser, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelGmail, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelContraseña, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelAsteriscos, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(botonCambiarContraseña, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(46, 46, 46))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void botonCambiarContraseñaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCambiarContraseñaActionPerformed
        InterfazConfirmarCorreo.crearNuevaInstancia();
        dispose();
    }//GEN-LAST:event_botonCambiarContraseñaActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton botonCambiarContraseña;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel labelAsteriscos;
    private javax.swing.JLabel labelContraseña;
    private javax.swing.JLabel labelCorreo;
    private javax.swing.JLabel labelGmail;
    private javax.swing.JLabel labelNombre;
    private javax.swing.JLabel labelUser;
    // End of variables declaration//GEN-END:variables
}
