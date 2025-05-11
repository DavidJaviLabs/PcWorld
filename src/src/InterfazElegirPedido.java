package src;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.*;
import java.text.*;
import java.util.ArrayList;
import java.util.*;
import javax.swing.*;
import javax.swing.border.Border;

public class InterfazElegirPedido extends javax.swing.JFrame 
{
    /**
     * Creates new form InterfazElegirPedido
     */
    public InterfazElegirPedido() 
    {
        initComponents();
        
        panelPrincipal.requestFocusInWindow();
        panelPedidos = new JPanel(new GridLayout(0,1));
        panelPedidos.setBackground(Color.WHITE);
        panelPedidos.setBounds(0, 0, 600, 400);
        
        cargarProductosDesdeBD();
        
        scrollpane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollpane.setViewportView(panelPedidos);

        // Obtener la vista del JViewport
        JViewport viewport = scrollpane.getViewport();

        // Establecer la posición de la vista en la parte superior
        SwingUtilities.invokeLater(() -> {
            viewport.setViewPosition(new Point(0, 0));
        });
    }
    
    public void cargarProductosDesdeBD() 
    {
        try 
        {
            Connection cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pcworld", "pcworld", "pcworld2024");
            PreparedStatement pstmtCliente = null;
            if(InterfazPrincipal.getInstancia().botonCuenta.getText().equals("Admin"))
            {
                String sql = "SELECT * FROM pedidos ORDER BY Fecha DESC";
                pstmtCliente = cn.prepareStatement(sql);
            }
            else
            {
                String sql = "SELECT * FROM pedidos WHERE `Usuario` = ? ORDER BY Fecha DESC";
                pstmtCliente = cn.prepareStatement(sql);
                pstmtCliente.setString(1, InterfazPrincipal.getInstancia().botonCuenta.getText());
            }
            ResultSet rsCliente = pstmtCliente.executeQuery();

            HashMap<String, Pedidos> mapPedidos = new HashMap<>();
            // Usamos una lista para mantener las fechas de los pedidos en orden
            ArrayList<String> fechasOrdenadas = new ArrayList<>();

            while (rsCliente.next()) 
            {
                String idPedido = rsCliente.getString("IdPedido");
                String fecha = rsCliente.getString("Fecha");

                // Verificar si ya existe un objeto Pedidos para este IdPedido en el HashMap
                Pedidos pedido = mapPedidos.get(idPedido);

                // Si no existe, creamos uno nuevo y lo agregamos al HashMap
                if (pedido == null) 
                {
                    pedido = new Pedidos(idPedido, fecha, 0.0);
                    mapPedidos.put(idPedido, pedido);
                    // Agregar la fecha a la lista ordenada
                    fechasOrdenadas.add(fecha);
                }

                // Obtener el nombre del producto y su cantidad
                String nombreProducto = rsCliente.getString("nombreProducto");
                int cantidadProducto = rsCliente.getInt("cantidadProducto");

                // Obtener el precio del producto de la tabla de productos
                String sqlPrecio = "SELECT `precioProducto` FROM productos WHERE `IdNombre` = ?";
                PreparedStatement pstmtPrecio = cn.prepareStatement(sqlPrecio);
                pstmtPrecio.setString(1, nombreProducto);
                ResultSet rsPrecio = pstmtPrecio.executeQuery();

                if (rsPrecio.next()) {
                    double precioProducto = rsPrecio.getDouble("precioProducto");
                    double precioTotalProducto = precioProducto * cantidadProducto;

                    // Actualizar el precio total del pedido en el objeto Pedidos
                    pedido.setPrecioTotal(pedido.getPrecioTotal() + precioTotalProducto);
                }
            }

            // Ordenar la lista de fechas en orden descendente
            Collections.sort(fechasOrdenadas, Collections.reverseOrder());

            // Crear los paneles de productos en función de las fechas ordenadas
            for (String fecha : fechasOrdenadas) 
            {
                Pedidos pedido = null;
                // Obtener el pedido correspondiente a esta fecha
                for (Pedidos p : mapPedidos.values()) 
                {
                    if (p.getFecha().equals(fecha)) 
                    {
                        pedido = p;
                        break;
                    }
                }
                // Crear el panel del producto solo si se encontró el pedido correspondiente
                if (pedido != null) 
                {
                    crearPanelProducto(pedido);
                }
            }
            
            cn.close();
            panelPedidos.revalidate();
            panelPedidos.repaint();
        } 
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void crearPanelProducto(Pedidos pedidos) 
    {
        // Creación del panel para el producto
        JPanel panelProducto = new JPanel(new GridBagLayout());
        panelProducto.setBackground(new Color(255, 255, 255));

        // Creación de los elementos del producto
        JLabel idPedido = new JLabel("IdPedido: " + pedidos.getIdPedido());
        idPedido.setFont(new Font("Arial", Font.PLAIN, 12));
        idPedido.setPreferredSize(new Dimension(100, 60));
        
        DecimalFormat df = new DecimalFormat("#,###,###.##");
        String subTotalFormateado = df.format(pedidos.getPrecioTotal());
        
        JLabel labelPrecioTotal = new JLabel("Precio del pedido: " + subTotalFormateado + " €");
        labelPrecioTotal.setFont(new Font("Arial", Font.PLAIN, 12));
        labelPrecioTotal.setPreferredSize(new Dimension(200, 60));
        
        JLabel labelFecha = new JLabel("Fecha: " + pedidos.getFecha());
        labelFecha.setFont(new Font("Arial", Font.PLAIN, 12));
        labelFecha.setPreferredSize(new Dimension(200, 60));
        
        JLabel labelImagenProducto = new JLabel("");
        labelImagenProducto.setIcon(new javax.swing.ImageIcon("ImagenesDecoracion" + File.separator + "caja.jpg"));
        labelImagenProducto.setFont(new Font("Arial", Font.PLAIN, 12));
        labelImagenProducto.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        
        labelImagenProducto.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                InterfazMisPedidos interfazMisPedidos = new InterfazMisPedidos(pedidos.getIdPedido());
                interfazMisPedidos.setVisible(true);
                dispose();
            }
        });

        // Configuración de las restricciones del GridBagLayout para la primera columna
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 10);
        panelProducto.add(labelImagenProducto, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 15, 5, 5);
        panelProducto.add(idPedido, gbc);
        
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 15, 5, 0);
        panelProducto.add(labelFecha, gbc);
        
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 0);
        panelProducto.add(labelPrecioTotal, gbc);

        // Ajustar el tamaño del panelProducto
        Dimension preferredSize = panelProducto.getPreferredSize();
        int preferredHeight = preferredSize.height;
        preferredHeight += 10;
        panelProducto.setPreferredSize(new Dimension(preferredSize.width, preferredHeight));

        // Agregar borde al panelProducto
        Border border = BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1);
        panelProducto.setBorder(border);

        panelPedidos.add(panelProducto);
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
        scrollpane = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 647, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 397, Short.MAX_VALUE)
        );

        scrollpane.setViewportView(jPanel2);

        javax.swing.GroupLayout panelPrincipalLayout = new javax.swing.GroupLayout(panelPrincipal);
        panelPrincipal.setLayout(panelPrincipalLayout);
        panelPrincipalLayout.setHorizontalGroup(
            panelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollpane)
        );
        panelPrincipalLayout.setVerticalGroup(
            panelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollpane)
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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel panelPrincipal;
    private javax.swing.JScrollPane scrollpane;
    // End of variables declaration//GEN-END:variables
    private javax.swing.JPanel panelPedidos;
}
