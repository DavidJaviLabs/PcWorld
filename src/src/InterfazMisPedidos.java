package src;

import ErroresInterfaz.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.*;
import java.text.*;
import javax.swing.*;
import javax.swing.border.*;



public class InterfazMisPedidos extends javax.swing.JFrame 
{
    /**
     * Creates new form InterfazMisPedidos
     */
    public InterfazMisPedidos(String idPedido) 
    {
        initComponents();
        IdPedido = "";
        Border border = BorderFactory.createLineBorder(Color.BLACK, 2);
        panelPrincipal.setBorder(border);
        labelLogo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelLogo.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
        labelOtroPedido.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        
        panelPrincipal.requestFocusInWindow();
        panelProductos = new JPanel(new GridLayout(0,1));
        panelProductos.setBackground(Color.WHITE);
        
        cargarIdPedido(idPedido);
        cargarProductosDesdeBD();
        
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setViewportView(panelProductos);

        // Obtener la vista del JViewport
        JViewport viewport = scrollPane.getViewport();

        // Establecer la posición de la vista en la parte superior
        SwingUtilities.invokeLater(() -> {
            viewport.setViewPosition(new Point(0, 0));
        });
    }
    
    public void cargarIdPedido(String idPedido)
    {
        IdPedido = idPedido;
    }
    
    public void cargarProductosDesdeBD() {
        try {
            Connection cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pcworld", "pcworld", "pcworld2024");
            String sql = "SELECT * FROM pedidos WHERE `IdPedido` = ?";
            PreparedStatement pstmtPedido = cn.prepareStatement(sql);
            pstmtPedido.setString(1, IdPedido);
            ResultSet rsPedido = pstmtPedido.executeQuery();
            
            while (rsPedido.next())
            {
                String nombre = rsPedido.getString("nombreProducto");
                double precio = 0.0;
                String imagen = "";
                int cantidadProducto = Integer.parseInt(rsPedido.getString("cantidadProducto"));

                String sql2 = "SELECT * FROM productos WHERE `IdNombre` = ?";
                PreparedStatement pstmtProducto = cn.prepareStatement(sql2);
                pstmtProducto.setString(1, nombre);
                ResultSet rsProducto = pstmtProducto.executeQuery();
                
                while (rsProducto.next())
                {
                    if(Integer.parseInt(rsProducto.getString("oculto")) == 1)
                    {
                        nombre += " (FUERA DE CATÁLOGO)";
                    } 
                    precio = rsProducto.getDouble("precioProducto");
                    
                    String rutaImagenPeque = "Imagenes" + File.separator + "Pequenas" + File.separator;
                    
                    imagen = rutaImagenPeque + rsProducto.getString("Imagen1Producto").replace("/", File.separator);
                }
                Producto producto = new Producto(nombre, precio, imagen);
                crearPanelProducto(producto, cantidadProducto);
                panelProductos.revalidate();
                panelProductos.repaint();
            }
            cn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void crearPanelProducto(Producto producto, int cantidadProducto) 
    {
        // Creación del panel para el producto
        JPanel panelProducto = new JPanel(new GridBagLayout());
        panelProducto.setBackground(new Color(255, 255, 255));

        // Creación de los elementos del producto
        JTextArea nombreProducto = new JTextArea(producto.getNombre());
        nombreProducto.setEditable(false);
        nombreProducto.setLineWrap(true);
        nombreProducto.setWrapStyleWord(true);
        nombreProducto.setAutoscrolls(false);
        nombreProducto.setFocusable(false);
        
        DecimalFormat df = new DecimalFormat("#,###,###.##");
        String subTotalFormateado = df.format(producto.getPrecio());
        
        JLabel labelImagenProducto = new JLabel(new ImageIcon(producto.getImagen()));
        
        labelImagenProducto.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    // Establecer la conexión a la base de datos
                    Connection cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pcworld", "pcworld", "pcworld2024");

                    // Verificar si el correo electrónico ya existe en la tabla
                    String queryNombre = "SELECT * FROM productos WHERE `IdNombre` = ?";
                    PreparedStatement pstmtNombre = cn.prepareStatement(queryNombre);
                    pstmtNombre.setString(1, nombreProducto.getText().replace(" (FUERA DE CATÁLOGO)", ""));
                    ResultSet rsProducto = pstmtNombre.executeQuery();

                    if (rsProducto.next()) {
                        if(Integer.parseInt(rsProducto.getString("oculto")) == 1)
                        {
                            new ErrorProductoNoExiste();
                            dispose();
                        }
                        else
                        {
                            InterfazProducto.getInstancia().cargarProducto(nombreProducto.getText());
                            InterfazProducto.getInstancia().setVisible(true);
                            dispose();
                            InterfazPrincipal.getInstancia().setVisible(false);
                            InterfazProducto.getInstancia().panelPrincipal.requestFocusInWindow();
                        }
                    }
                    cn.close();
                } catch(Exception e1) {
                    System.out.println("Error: " + e1);
                }
            }
        });
        JLabel labelCantidadProducto = new JLabel("Unidades: " + cantidadProducto);
        JLabel labelPrecioProducto = new JLabel(subTotalFormateado + " €");
        labelPrecioProducto.setFont(new Font("Arial", Font.BOLD, 14));

        // Configuración de las dimensiones de los elementos
        nombreProducto.setPreferredSize(new Dimension(350, 30));
        
        // Configuración de las restricciones del GridBagLayout para la primera columna
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 40, 5, 5);
        panelProducto.add(labelImagenProducto, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(5, 10, 0, 5);
        panelProducto.add(nombreProducto, gbc);
        
        gbc.gridx = 4;
        gbc.insets = new Insets(5, 0, 5, 50);
        panelProducto.add(labelPrecioProducto, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 15, 5, 5);
        panelProducto.add(labelCantidadProducto, gbc);

        // Ajustar el tamaño del panelProducto
        Dimension preferredSize = panelProducto.getPreferredSize();
        int preferredHeight = preferredSize.height;
        preferredHeight += 10;
        panelProducto.setPreferredSize(new Dimension(preferredSize.width, preferredHeight));

        // Agregar borde al panelProducto
        Border border = BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1);
        panelProducto.setBorder(border);
        
        // Agregar el panelProducto a la primera columna del panelPrueba
        panelProductos.add(panelProducto);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelPrincipal = new javax.swing.JPanel();
        scrollPane = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        labelLogo = new javax.swing.JLabel();
        labelOtroPedido = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        panelPrincipal.setBackground(new java.awt.Color(102, 255, 102));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 697, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 497, Short.MAX_VALUE)
        );

        scrollPane.setViewportView(jPanel2);

        jPanel1.setBackground(new java.awt.Color(102, 255, 102));

        labelLogo.setIcon(new javax.swing.ImageIcon("ImagenesDecoracion" + File.separator + "logoPrinc.png"));
        labelLogo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                labelLogoMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(labelLogo, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(labelLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 14, Short.MAX_VALUE))
        );

        labelOtroPedido.setIcon(new javax.swing.ImageIcon("ImagenesDecoracion" + File.separator + "volver.png"));
        labelOtroPedido.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                labelOtroPedidoMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                labelOtroPedidoMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                labelOtroPedidoMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout panelPrincipalLayout = new javax.swing.GroupLayout(panelPrincipal);
        panelPrincipal.setLayout(panelPrincipalLayout);
        panelPrincipalLayout.setHorizontalGroup(
            panelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollPane)
            .addGroup(panelPrincipalLayout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(labelOtroPedido)
                .addContainerGap())
        );
        panelPrincipalLayout.setVerticalGroup(
            panelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelPrincipalLayout.createSequentialGroup()
                .addGroup(panelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panelPrincipalLayout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(labelOtroPedido, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(scrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 420, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void labelOtroPedidoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelOtroPedidoMousePressed
        labelOtroPedido.setIcon(new javax.swing.ImageIcon("ImagenesDecoracion" + File.separator + "volverRelleno.png"));
    }//GEN-LAST:event_labelOtroPedidoMousePressed

    private void labelOtroPedidoMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelOtroPedidoMouseReleased
        labelOtroPedido.setIcon(new javax.swing.ImageIcon("ImagenesDecoracion" + File.separator + "volver.png"));
    }//GEN-LAST:event_labelOtroPedidoMouseReleased

    private void labelLogoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelLogoMouseClicked
        InterfazPrincipal.getInstancia().setVisible(true);
        dispose();
    }//GEN-LAST:event_labelLogoMouseClicked

    private void labelOtroPedidoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelOtroPedidoMouseClicked
        new InterfazElegirPedido().setVisible(true);
        dispose();
    }//GEN-LAST:event_labelOtroPedidoMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel labelLogo;
    private javax.swing.JLabel labelOtroPedido;
    private javax.swing.JPanel panelPrincipal;
    private javax.swing.JScrollPane scrollPane;
    // End of variables declaration//GEN-END:variables
    private String IdPedido;
    private javax.swing.JPanel panelProductos;
}
