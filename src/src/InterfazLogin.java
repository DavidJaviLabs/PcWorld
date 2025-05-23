package src;

import java.awt.event.*;
import java.io.*;
import java.sql.*;

public class InterfazLogin extends javax.swing.JFrame 
{
    /**
     * Creates new form InterfazLogin
     */
    public InterfazLogin() {
        initComponents();
        panelLogin.requestFocusInWindow();
        
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                // Al cerrar la ventana, establece el foco en el panel principal de la interfaz principal
                InterfazPrincipal.getInstancia().panelPrincipal.requestFocusInWindow();
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelLogin = new javax.swing.JPanel();
        campoEmail = new javax.swing.JTextField();
        labelEmail = new javax.swing.JLabel();
        labelContraseña = new javax.swing.JLabel();
        butonIniciarSesion = new javax.swing.JButton();
        botonCrearCuenta = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        labelNuevoCliente = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        labelOlvidadoContraseña = new javax.swing.JLabel();
        labelErrorEmail = new javax.swing.JLabel();
        labelErrorContraseña = new javax.swing.JLabel();
        campoContraseña = new javax.swing.JPasswordField();
        labelOjo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        panelLogin.setBackground(new java.awt.Color(255, 255, 255));
        panelLogin.setForeground(new java.awt.Color(255, 255, 255));
        panelLogin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mouseClickedPanelActionEvent(evt);
            }
        });

        campoEmail.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        campoEmail.setForeground(new java.awt.Color(153, 153, 153));
        campoEmail.setText("E-mail*");
        campoEmail.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        campoEmail.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                campoEmailFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                focusLostEmailActionEvent(evt);
            }
        });
        campoEmail.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                campoEmailKeyPressed(evt);
            }
        });

        labelEmail.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        labelEmail.setText("E-mail");

        labelContraseña.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        labelContraseña.setText("Contraseña");

        butonIniciarSesion.setText("Iniciar sesión");
        butonIniciarSesion.setFocusPainted(false);
        butonIniciarSesion.setFocusable(false);
        butonIniciarSesion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butonIniciarSesionActionPerformed(evt);
            }
        });

        botonCrearCuenta.setText("Crear cuenta");
        botonCrearCuenta.setFocusPainted(false);
        botonCrearCuenta.setFocusable(false);
        botonCrearCuenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCrearCuentaActionPerformed(evt);
            }
        });

        jSeparator1.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N

        labelNuevoCliente.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        labelNuevoCliente.setText("¿Eres nuevo cliente?");

        jSeparator2.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator2.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N

        labelOlvidadoContraseña.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        labelOlvidadoContraseña.setText("¿Has olvidado la contraseña?");
        labelOlvidadoContraseña.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                labelOlvidadoContraseñaActionPerformed(evt);
            }
        });

        labelErrorEmail.setFont(new java.awt.Font("Arial", 0, 9)); // NOI18N
        labelErrorEmail.setForeground(new java.awt.Color(255, 0, 0));
        labelErrorEmail.setText("                            ");

        labelErrorContraseña.setFont(new java.awt.Font("Arial", 0, 9)); // NOI18N
        labelErrorContraseña.setForeground(new java.awt.Color(255, 0, 0));
        labelErrorContraseña.setText("                            ");

        campoContraseña.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        campoContraseña.setText("Contraseña*");
        campoContraseña.setEchoChar('*');
        campoContraseña.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                campoContraseñaFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                campoContraseñaFocusLost(evt);
            }
        });
        campoContraseña.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                campoContraseñaKeyPressed(evt);
            }
        });

        labelOjo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelOjo.setIcon(new javax.swing.ImageIcon("ImagenesDecoracion" + File.separator + "ojoVisible.png"));
        labelOjo.setText(" ");
        labelOjo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                labelOjoMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout panelLoginLayout = new javax.swing.GroupLayout(panelLogin);
        panelLogin.setLayout(panelLoginLayout);
        panelLoginLayout.setHorizontalGroup(
            panelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLoginLayout.createSequentialGroup()
                .addGroup(panelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelLoginLayout.createSequentialGroup()
                        .addGap(90, 90, 90)
                        .addGroup(panelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelLoginLayout.createSequentialGroup()
                                .addGroup(panelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(labelErrorEmail, javax.swing.GroupLayout.DEFAULT_SIZE, 253, Short.MAX_VALUE)
                                    .addComponent(labelErrorContraseña, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(30, 30, 30))
                            .addComponent(labelContraseña)
                            .addComponent(labelEmail)
                            .addComponent(campoEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelLoginLayout.createSequentialGroup()
                                .addComponent(campoContraseña, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(labelOjo, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelLoginLayout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addGroup(panelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelLoginLayout.createSequentialGroup()
                                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(labelNuevoCliente)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelLoginLayout.createSequentialGroup()
                                .addGap(42, 42, 42)
                                .addGroup(panelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(botonCrearCuenta, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(butonIniciarSesion, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(labelOlvidadoContraseña)))))))
                .addContainerGap(27, Short.MAX_VALUE))
        );
        panelLoginLayout.setVerticalGroup(
            panelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLoginLayout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addComponent(labelEmail)
                .addGap(0, 0, 0)
                .addComponent(campoEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelErrorEmail)
                .addGap(10, 10, 10)
                .addComponent(labelContraseña)
                .addGap(0, 0, 0)
                .addGroup(panelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(campoContraseña, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelOjo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                .addComponent(labelErrorContraseña)
                .addGap(10, 10, 10)
                .addComponent(butonIniciarSesion, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(labelOlvidadoContraseña)
                .addGroup(panelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelLoginLayout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(labelNuevoCliente)
                        .addGap(43, 43, 43))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLoginLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(29, 29, 29)))
                .addComponent(botonCrearCuenta, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(69, 69, 69))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelLogin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelLogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void butonIniciarSesionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butonIniciarSesionActionPerformed
        inicioSesion();
        InterfazProducto.getInstancia().labelErrorCesta.setText(" ");
        InterfazProducto.getInstancia().labelErrorListaDeDeseos.setText(" ");
    }//GEN-LAST:event_butonIniciarSesionActionPerformed

    private void inicioSesion()
    {
        try {
            // Establecer la conexión a la base de datos
            Connection cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pcworld", "pcworld", "pcworld2024");

            // Verificar si el correo electrónico ya existe en la tabla
            String queryCorreo = "SELECT * FROM datos_clientes WHERE `E-mail` = ?";
            PreparedStatement pstmtCorreo = cn.prepareStatement(queryCorreo);
            pstmtCorreo.setString(1, campoEmail.getText().trim());
            ResultSet rsCorreo = pstmtCorreo.executeQuery();

            if (rsCorreo.next()) {
                labelErrorEmail.setText("                                                   ");
                // El correo electrónico existe en la base de datos
                String contraseñaDB = rsCorreo.getString("Contraseña");
                String contraseñaUsuario = campoContraseña.getText().trim();

                if (contraseñaDB.equals(contraseñaUsuario)) {
                    // La contraseña coincide
                    labelErrorContraseña.setText("                                                   ");
                    InterfazPrincipal.getInstancia().setUser(rsCorreo.getString("Usuario"));
                    InterfazPrincipal.getInstancia().cambiarTextoBotonCuenta(InterfazPrincipal.getInstancia().getUser());
                    InterfazPrincipal.getInstancia().correo = campoEmail.getText().trim();
                    
                    // RESTO DE INTERFACES
                    InterfazProducto.getInstancia().botonCuenta.setText(rsCorreo.getString("Usuario"));
                    if(InterfazProducto.getInstancia().isVisible())
                    {
                        InterfazProducto.getInstancia().cargarBotonesCarritoListaDeseos(InterfazProducto.getInstancia().getNombreProducto());
                        InterfazProducto.getInstancia().cargarModificarStock();
                    }
                    
                    InterfazMostrarProductos.getInstancia().botonCuenta.setText(rsCorreo.getString("Usuario"));
                    InterfazCesta.getInstancia().botonCuenta.setText(rsCorreo.getString("Usuario"));
                    InterfazListaDeseos.getInstancia().botonCuenta.setText(rsCorreo.getString("Usuario"));
                    InterfazMostrarProductos.getInstancia().botonCuenta.setText(rsCorreo.getString("Usuario"));
                    InterfazConfiguradorPcs.getInstancia().botonCuenta.setText(rsCorreo.getString("Usuario"));
                    
                    this.dispose();
                } 
                else {
                    // La contraseña no coincide
                    labelErrorContraseña.setText("La contraseña no es correcta");
                }
            } else {
                // El correo electrónico no existe en la base de datos
                if(!campoEmail.getText().matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"))
                {
                    labelErrorEmail.setText("El correo electrónico está mal escrito");
                    if(!campoContraseña.getText().equals("Contraseña*"))
                        labelErrorContraseña.setText("                                                   ");
                }
                else{
                    labelErrorEmail.setText("El correo electrónico no está registrado, pruebe a crear una cuenta");
                    if(!campoContraseña.getText().equals("Contraseña*"))
                        labelErrorContraseña.setText("                                                   ");
                }
            }
            if(campoEmail.getText().equals("E-mail*"))
                labelErrorEmail.setText("Rellena el E-mail");
            if(campoContraseña.getText().equals("Contraseña*"))
                labelErrorContraseña.setText("El campo está vacío");

        } catch(Exception e) {
            System.out.println("Error: " + e);
        }
    }
    
    
    private void botonCrearCuentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCrearCuentaActionPerformed
        InterfazCrearCuenta.crearNuevaInstancia().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_botonCrearCuentaActionPerformed

    private void labelOlvidadoContraseñaActionPerformed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelOlvidadoContraseñaActionPerformed
        InterfazConfirmarCorreo.crearNuevaInstancia();
        this.dispose();        
    }//GEN-LAST:event_labelOlvidadoContraseñaActionPerformed

    private void focusLostEmailActionEvent(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_focusLostEmailActionEvent
        if(campoEmail.getText().equals(""))
        {
            campoEmail.setText("E-mail*");
        }
    }//GEN-LAST:event_focusLostEmailActionEvent

    private void mouseClickedPanelActionEvent(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mouseClickedPanelActionEvent
        panelLogin.requestFocusInWindow();
    }//GEN-LAST:event_mouseClickedPanelActionEvent

    private void campoEmailFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_campoEmailFocusGained
        if(campoEmail.getText().equals("E-mail*"))
        {
            campoEmail.setText("");
        }
    }//GEN-LAST:event_campoEmailFocusGained

    private void campoEmailKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoEmailKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER)
        {
            inicioSesion();
        }
    }//GEN-LAST:event_campoEmailKeyPressed

    private void campoContraseñaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoContraseñaKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER)
        {
            inicioSesion();
        }
    }//GEN-LAST:event_campoContraseñaKeyPressed

    private void campoContraseñaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_campoContraseñaFocusGained
        if(campoContraseña.getText().equals("Contraseña*"))
        {
            campoContraseña.setText("");
        }
    }//GEN-LAST:event_campoContraseñaFocusGained

    private void campoContraseñaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_campoContraseñaFocusLost
        if(campoContraseña.getText().equals(""))
        {
            campoContraseña.setText("Contraseña*");
        }
    }//GEN-LAST:event_campoContraseñaFocusLost

    private void labelOjoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelOjoMouseClicked
        if(campoContraseña.getEchoChar() == '*')
        {
            labelOjo.setIcon(new javax.swing.ImageIcon("ImagenesDecoracion" + File.separator + "ojoNoVisible.png"));
            campoContraseña.setEchoChar((char) 0);
        }
        else
        {
            labelOjo.setIcon(new javax.swing.ImageIcon("ImagenesDecoracion" + File.separator + "ojoVisible.png"));
            campoContraseña.setEchoChar('*');
        }    
    }//GEN-LAST:event_labelOjoMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton botonCrearCuenta;
    private javax.swing.JButton butonIniciarSesion;
    private javax.swing.JPasswordField campoContraseña;
    private javax.swing.JTextField campoEmail;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel labelContraseña;
    private javax.swing.JLabel labelEmail;
    private javax.swing.JLabel labelErrorContraseña;
    private javax.swing.JLabel labelErrorEmail;
    private javax.swing.JLabel labelNuevoCliente;
    private javax.swing.JLabel labelOjo;
    private javax.swing.JLabel labelOlvidadoContraseña;
    private javax.swing.JPanel panelLogin;
    // End of variables declaration//GEN-END:variables
}
