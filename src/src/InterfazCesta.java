package src;

import Confirmaciones.*;
import ErroresInterfaz.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.*;
import java.text.*;
import java.util.Date;
import java.util.Random;
import javax.swing.*;
import javax.swing.border.*;

public class InterfazCesta extends javax.swing.JFrame {
    
    /**
     * Creates new form InterfazCesta
     */
    public InterfazCesta() 
    {
        initComponents();
        
        precioLabel = new JLabel("    " + "€");
        cliente = false;
        subTotal = 0;
                
        panelPrincipal.requestFocusInWindow();
        panelProductos = new JPanel(new GridLayout(0,1));
        panelProductos.setBackground(Color.YELLOW);
        panelProductos.setBounds(0, 0, 1000, 700);
        
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
    
    public static InterfazCesta getInstancia()
    {
        if(instancia == null)
        {
            instancia = new InterfazCesta();
        }
        return instancia;
    }
    
    public static InterfazCesta crearNuevaInstancia() 
    {
        instancia = null;
        return getInstancia();
    }
    
    private void cerrar()
    {
        dispose();
        InterfazPrincipal.getInstancia().campoBusqueda.setText("   Buscar...");
        InterfazListaDeseos.getInstancia().campoBusqueda.setText("   Buscar...");
        InterfazProducto.getInstancia().campoBusqueda.setText("   Buscar...");
        InterfazMostrarProductos.getInstancia().campoBusqueda.setText("   Buscar...");
        InterfazConfiguradorPcs.getInstancia().campoBusqueda.setText("   Buscar...");
    }
    
    private void panelPrecioTotal() 
    {
        Font font = new Font("Arial", Font.BOLD, 18);
        // Creación del panel para el producto
        JPanel panelProducto = new JPanel(new GridBagLayout());
        panelProducto.setBackground(new Color(255, 255, 255));

        // Creación de los elementos del producto
        JLabel subtotalLabel = new JLabel("Subtotal:");
        subtotalLabel.setHorizontalAlignment(SwingConstants.CENTER);
        subtotalLabel.setFont(font);
        subtotalLabel.setPreferredSize(new Dimension(100, 35)); // Ajustar el tamaño en el eje x según tus necesidades
        precioLabel.setPreferredSize(new Dimension(100, 35)); // Ajustar el tamaño en el eje x según tus necesidades
        precioLabel.setHorizontalAlignment(SwingConstants.CENTER);
        precioLabel.setFont(font);

        // Crear borde conjunto para subtotalLabel y precioLabel
        Border border = BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1);
        precioLabel.setBorder(border);

        JLabel atencionClienteLabel = new JLabel("Atención al Cliente");
        atencionClienteLabel.setIcon(new javax.swing.ImageIcon("ImagenesDecoracion" + File.separator + "atencion.png"));
        atencionClienteLabel.setHorizontalAlignment(SwingConstants.CENTER);
        atencionClienteLabel.setFont(font);
        atencionClienteLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1)); // Añadir borde a atencionClienteLabel

        JLabel realizarPedidoLabel = new JLabel("Realizar pedido");
        realizarPedidoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        realizarPedidoLabel.setFont(font);
        realizarPedidoLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1)); // Añadir borde a realizarPedidoLabel

        // Configuración de las dimensiones de los elementos
        atencionClienteLabel.setPreferredSize(new Dimension(210, 35));
        realizarPedidoLabel.setPreferredSize(new Dimension(170, 35));

        // Configuración de las restricciones del GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 20, 5, 10); // Ajustar el espacio entre elementos
        panelProducto.add(subtotalLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(5, 10, 5, 665); // Ajustar el espacio entre elementos
        panelProducto.add(precioLabel, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0; // Restaurar el peso
        gbc.insets = new Insets(5, 20, 5, 10); // Ajustar el espacio entre elementos
        panelProducto.add(atencionClienteLabel, gbc);

        gbc.gridx = 3;
        gbc.insets = new Insets(5, 20, 5, 20); // Ajustar el espacio entre elementos
        panelProducto.add(realizarPedidoLabel, gbc);

        // Ajustar el tamaño del panelProducto
        Dimension preferredSize = panelProducto.getPreferredSize();
        int preferredHeight = preferredSize.height;
        preferredHeight += 10;
        panelProducto.setPreferredSize(new Dimension(preferredSize.width, preferredHeight));
        
        atencionClienteLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                atencionClienteLabel.setIcon(new javax.swing.ImageIcon("ImagenesDecoracion" + File.separator + "atencionRelleno.png"));
                if(botonCuenta.getText().equals("Admin"))
                {
                    new InterfazElegirChat();
                }else
                {
                    InterfazAtencionCliente.crearNuevaInstancia();
                }
            }
        });
        atencionClienteLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                atencionClienteLabel.setIcon(new javax.swing.ImageIcon("ImagenesDecoracion" + File.separator + "atencion.png"));
            }
        });
        
        realizarPedidoLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Random random = new Random();
                int numeroAleatorio = random.nextInt(1000000);
                String numeroPedido = String.format("%06d", numeroAleatorio);
                try {
                    Connection cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pcworld", "pcworld", "pcworld2024");
                    String sql = "SELECT * FROM cesta WHERE `IdCliente` = ?";
                    PreparedStatement pstmtProductos = cn.prepareStatement(sql);
                    pstmtProductos.setString(1, botonCuenta.getText());
                    ResultSet rsProductos = pstmtProductos.executeQuery();
                    // Obtener la fecha y hora actual
                    Date fechaActual = new Date();

                    // Formatear la fecha y hora en el formato adecuado para MySQL
                    SimpleDateFormat formatoFechaHora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String fechaHoraFormateada = formatoFechaHora.format(fechaActual);
                    
                    while(rsProductos.next())
                    {
                        
                        String queryPedido = "SELECT * FROM pedidos WHERE IdPedido = ?";
                        PreparedStatement pstmtPedido = cn.prepareStatement(queryPedido);
                        pstmtPedido.setString(1, numeroPedido);
                        ResultSet rsPedido = pstmtPedido.executeQuery();
                        
                        while(rsPedido.next())
                        {
                            String queryFecha = "SELECT * FROM pedidos WHERE Fecha = ?";
                            PreparedStatement pstmtFecha = cn.prepareStatement(queryFecha);
                            pstmtFecha.setString(1, fechaHoraFormateada);
                            ResultSet rsFecha = pstmtFecha.executeQuery();
                            if(!rsFecha.next())
                            {
                                numeroAleatorio = random.nextInt(1000000);
                                numeroPedido = String.format("%06d", numeroAleatorio); 
                            }
                        } 
                        
                        PreparedStatement pstmtInsertar = cn.prepareStatement("insert into pedidos values(?,?,?,?,?)");
                        pstmtInsertar.setString(1, numeroPedido);
                        pstmtInsertar.setString(2, botonCuenta.getText());
                        pstmtInsertar.setString(3, fechaHoraFormateada);
                        pstmtInsertar.setString(4, rsProductos.getString("IdProducto"));
                        pstmtInsertar.setString(5, rsProductos.getString("cantidadProducto"));
                        pstmtInsertar.executeUpdate();
                        
                        String sql3 = "SELECT * FROM productos WHERE `IdNombre` = ?";
                        PreparedStatement pstmtProductos3 = cn.prepareStatement(sql3);
                        pstmtProductos3.setString(1, rsProductos.getString("IdProducto"));
                        ResultSet rsProductos3 = pstmtProductos3.executeQuery();
                        if (rsProductos3.next())
                        {              
                            int Stock = 0;
                            String sql2 = "UPDATE productos SET stockProducto = ? WHERE `IdNombre` = ?";
                            PreparedStatement pstmtProductos2 = cn.prepareStatement(sql2);
                            Stock = Integer.parseInt(rsProductos3.getString("stockProducto")) - Integer.parseInt(rsProductos.getString("cantidadProducto"));
                            pstmtProductos2.setString(1, String.valueOf(Stock));
                            pstmtProductos2.setString(2, rsProductos3.getString("IdNombre"));                    
                            pstmtProductos2.executeUpdate();
                        }
                    }
                    
                    String sql1 = "DELETE FROM cesta WHERE `IdCliente` = ?";
                    PreparedStatement pstmtProductos1 = cn.prepareStatement(sql1);
                    pstmtProductos1.setString(1, botonCuenta.getText());
                    pstmtProductos1.executeUpdate();
                    
                    InterfazPrincipal.getInstancia().setVisible(true);
                    new InterfazPedidoRealizado();
                    cerrar();
                    cn.close();
                } catch (SQLException ex) {
                    System.err.println(ex.getMessage());
                }
            }
        });
        // Agregar el panelProducto a panelProductos
        panelProductos.add(panelProducto);
    }

    public void noCargarProductosDesdeBD()
    {
        if(!cliente)
        {
            new ErrorCestaSinProductos();
            cerrar();
        }
    } 

    public void cargarProductosDesdeBD() {
        try {
            subTotal = 0;
            Connection cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pcworld", "pcworld", "pcworld2024");
            String sql = "SELECT * FROM cesta WHERE `IdCliente` = ?";
            PreparedStatement pstmtCliente = cn.prepareStatement(sql);
            pstmtCliente.setString(1, this.botonCuenta.getText());
            ResultSet rsCliente = pstmtCliente.executeQuery();
            boolean clienteOf = false, total = true;
            
            cliente = rsCliente.next();
            clienteOf = cliente;
            
            while (clienteOf) {
                String nombre = rsCliente.getString("IdProducto");
                double precio = 0.0;
                String imagen = "";

                String sql2 = "SELECT * FROM productos WHERE `IdNombre` = ?";
                PreparedStatement pstmtProducto = cn.prepareStatement(sql2);
                pstmtProducto.setString(1, nombre);
                ResultSet rsProducto = pstmtProducto.executeQuery();
                while (rsProducto.next()) {
                    precio = rsProducto.getDouble("precioProducto");
                    
                    String rutaImagenPeque = "Imagenes" + File.separator + "Pequenas" + File.separator;
                    
                    imagen = rutaImagenPeque + rsProducto.getString("Imagen1Producto").replace("/", File.separator);
                }
                Producto producto = new Producto(nombre, precio, imagen);
                if(total)
                {
                    panelPrecioTotal();
                    total = false;
                }
                crearPanelProducto(producto);
                panelProductos.revalidate();
                panelProductos.repaint();
                clienteOf = rsCliente.next();
            }
            cn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DecimalFormat df = new DecimalFormat("#,###,###.##");
        String subTotalFormateado = df.format(subTotal);
        precioLabel.setText(subTotalFormateado + " €");
    }


    private void crearPanelProducto(Producto producto) {
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
        
        
        JLabel botonMas = new JLabel(new ImageIcon("ImagenesDecoracion" + File.separator + "plus.png"));
        botonMas.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                botonMas.setIcon(new javax.swing.ImageIcon("ImagenesDecoracion" + File.separator + "plusRelleno.png"));
            }
        });
        botonMas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                botonMas.setIcon(new javax.swing.ImageIcon("ImagenesDecoracion" + File.separator + "plus.png"));
            }
        });
        
        
        JLabel botonMenos = new JLabel(new ImageIcon("ImagenesDecoracion" + File.separator + "minus.png"));
        botonMenos.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                botonMenos.setIcon(new javax.swing.ImageIcon("ImagenesDecoracion" + File.separator + "minusRelleno.png"));
            }
        });
        botonMenos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                botonMenos.setIcon(new javax.swing.ImageIcon("ImagenesDecoracion" + File.separator + "minus.png"));
            }
        });
        
        
        JLabel botonEliminar = new JLabel(new ImageIcon("ImagenesDecoracion" + File.separator + "trash.png"));
        botonEliminar.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                botonEliminar.setIcon(new javax.swing.ImageIcon("ImagenesDecoracion" + File.separator + "trashRelleno.png"));
            }
        });
        botonEliminar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                botonEliminar.setIcon(new javax.swing.ImageIcon("ImagenesDecoracion" + File.separator + "trash.png"));
            }
        });
        
        DecimalFormat df = new DecimalFormat("#,###,###.##");
        String subTotalFormateado = df.format(producto.getPrecio());
        
        JLabel labelImagenProducto = new JLabel(new ImageIcon(producto.getImagen()));
        
        labelImagenProducto.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                InterfazProducto.getInstancia().cargarProducto(nombreProducto.getText());
        
                InterfazProducto.getInstancia().setVisible(true);
                cerrar();
                InterfazProducto.getInstancia().panelPrincipal.requestFocusInWindow();
            }
        });        
        JLabel labelContadorProducto = new JLabel(String.valueOf(producto.getContador()));
        JLabel labelPrecioProducto = new JLabel(subTotalFormateado + " €");
        labelPrecioProducto.setFont(new Font("Arial", Font.BOLD, 14));
        
        if(producto.getContador() == 1)
        {
            botonMenos.setEnabled(false);
        }
        
        try
        {
            Connection cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pcworld", "pcworld", "pcworld2024");
            String sql = "SELECT * FROM productos WHERE `IdNombre` = ?";
            PreparedStatement pstmtProductos = cn.prepareStatement(sql);
            pstmtProductos.setString(1, nombreProducto.getText());
            ResultSet rsProductos = pstmtProductos.executeQuery();
            int stock = 0; 
            while (rsProductos.next()) {
                        
                stock = (int) rsProductos.getDouble("stockProducto");
                    
            }
            if(stock == Integer.parseInt(labelContadorProducto.getText()))
                botonMas.setEnabled(false);
            cn.close();
        }catch(SQLException e1)
        {
            System.out.println("Error: " + e1);
        }

        // Configuración de las dimensiones de los elementos
        nombreProducto.setPreferredSize(new Dimension(400, 35));
        botonMas.setPreferredSize(new Dimension(0, 35));
        botonMenos.setPreferredSize(new Dimension(0, 35));
        labelContadorProducto.setPreferredSize(new Dimension(0, 35));
        botonEliminar.setPreferredSize(new Dimension(0, 35));
        labelContadorProducto.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        // Creación del panel para los botones de contador
        JPanel panelContador = new JPanel(new FlowLayout());
        panelContador.setBackground(Color.WHITE);
        panelContador.add(botonMenos);
        panelContador.add(labelContadorProducto);
        panelContador.add(botonMas);

        botonEliminar.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) 
            {
                try {
                    Connection cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pcworld", "pcworld", "pcworld2024");
                    String sql = "DELETE FROM cesta WHERE `IdProducto` = ? AND `IdCliente` = ?";
                    PreparedStatement pstmtProductos = cn.prepareStatement(sql);
                    pstmtProductos.setString(1, nombreProducto.getText());
                    pstmtProductos.setString(2, botonCuenta.getText());
                    pstmtProductos.executeUpdate();
                    
                    String sqlCount = "SELECT COUNT(*) AS total FROM cesta WHERE `IdCliente` = ?";
                    PreparedStatement pstmtCount = cn.prepareStatement(sqlCount);
                    pstmtCount.setString(1, botonCuenta.getText());
                    ResultSet rs = pstmtCount.executeQuery();

                    rs.next();
                    int totalRegistros = rs.getInt("total");
                    if (totalRegistros == 0)
                    {
                        InterfazPrincipal.getInstancia().setVisible(true);
                        dispose();
                    }
                    else
                    {
                        panelProductos = null;
                        panelProductos = new JPanel(new GridLayout(0,1));
                        scrollPane.setViewportView(panelProductos);
                        cargarProductosDesdeBD();
                        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                        scrollPane.setViewportView(panelProductos);

                        // Obtener la vista del JViewport
                        JViewport viewport = scrollPane.getViewport();

                        // Establecer la posición de la vista en la parte superior
                        SwingUtilities.invokeLater(() -> {
                            viewport.setViewPosition(new Point(0, 0));
                        });
                        cn.close();
                    }
                } catch (SQLException ex) {
                    System.err.println("Error al eliminar el producto de la cesta: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });        
        // Eventos de clic para los botones de incrementar y decrementar contador
        botonMas.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) 
            {
                if(botonMas.isEnabled())
                {
                    try {
                        Connection cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pcworld", "pcworld", "pcworld2024");
                        String sql = "SELECT * FROM productos WHERE `IdNombre` = ?";
                        PreparedStatement pstmtProductos = cn.prepareStatement(sql);
                        pstmtProductos.setString(1, nombreProducto.getText());
                        ResultSet rsProductos = pstmtProductos.executeQuery();
                        int stock = 0;
                        while (rsProductos.next()) {

                            stock = (int) rsProductos.getDouble("stockProducto");

                        }
                        if(Integer.parseInt(labelContadorProducto.getText()) < stock)
                        {
                            producto.incrementarContador();
                            labelContadorProducto.setText(String.valueOf(producto.getContador()));
                            if(Integer.parseInt(labelContadorProducto.getText()) == stock)
                            {
                                botonMas.setEnabled(false);
                                if(!botonMenos.isEnabled())
                                {
                                    botonMenos.setEnabled(true);
                                }
                            }else if(Integer.parseInt(labelContadorProducto.getText()) > 1)
                            {
                                botonMenos.setEnabled(true);
                            }
                        }
                        cn.close();
                    }catch(SQLException e1)
                    {
                        System.out.println("Error: " + e1);
                    }
                    DecimalFormat df = new DecimalFormat("#,###,###.##");
                    subTotal += producto.getPrecio();
                    String subTotalFormateado = df.format(subTotal);
                    precioLabel.setText(subTotalFormateado + " €");
                }
            }
        });

        botonMenos.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if(botonMenos.isEnabled())
                {
                    try
                    {
                        Connection cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pcworld", "pcworld", "pcworld2024");
                        String sql = "SELECT * FROM productos WHERE `IdNombre` = ?";
                        PreparedStatement pstmtProductos = cn.prepareStatement(sql);
                        pstmtProductos.setString(1, nombreProducto.getText());
                        ResultSet rsProductos = pstmtProductos.executeQuery();
                        int stock = 0;
                        while (rsProductos.next()) {

                        stock = (int) rsProductos.getDouble("stockProducto");

                        }
                        if(Integer.parseInt(labelContadorProducto.getText()) <= stock)
                        {
                            producto.decrementarContador();
                            // Actualizar la etiqueta del contador
                            labelContadorProducto.setText(String.valueOf(producto.getContador()));
                            if(Integer.parseInt(labelContadorProducto.getText()) == 1)
                            {
                                botonMenos.setEnabled(false);
                                if(!botonMas.isEnabled())
                                {
                                    botonMas.setEnabled(true);
                                }
                            }else if(Integer.parseInt(labelContadorProducto.getText()) < stock)
                            {
                                botonMas.setEnabled(true);
                            }
                        }
                        cn.close();
                    }catch(SQLException e1)
                    {
                        System.out.println("Error: " + e1);
                    }
                    DecimalFormat df = new DecimalFormat("#,###,###.##");
                    subTotal -= producto.getPrecio();
                    String subTotalFormateado = df.format(subTotal);
                    precioLabel.setText(subTotalFormateado + " €");
                }
            }
        });

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
        
        gbc.gridx = 2;
        gbc.insets = new Insets(5, 75, 5, 0);
        panelProducto.add(botonMenos, gbc);
        
        gbc.gridx = 3;
        gbc.insets = new Insets(5, 0, 5, 0);
        panelProducto.add(labelContadorProducto, gbc);
        
        gbc.gridx = 4;
        gbc.insets = new Insets(5, 0, 5, 50);
        panelProducto.add(botonMas, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 15, 5, 5);
        panelProducto.add(labelPrecioProducto, gbc);
        
        gbc.gridx = 6;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 50);
        panelProducto.add(botonEliminar, gbc);

        // Ajustar el tamaño del panelProducto
        Dimension preferredSize = panelProducto.getPreferredSize();
        int preferredHeight = preferredSize.height;
        preferredHeight += 10;
        panelProducto.setPreferredSize(new Dimension(preferredSize.width, preferredHeight));

        
        // Agregar borde al panelProducto
        Border border = BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1);
        panelProducto.setBorder(border);
        labelContadorProducto.setBorder(border);
        botonMas.setBorder(border);
        botonMenos.setBorder(border);
        botonEliminar.setBorder(border);
        subTotal += producto.getPrecio() * producto.getContador();
        
        // Agregar el panelProducto a la primera columna del panelProductos
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
        panelCabecera = new javax.swing.JPanel();
        panelHome = new javax.swing.JPanel();
        labelLogo = new javax.swing.JLabel();
        panelBuscador = new javax.swing.JPanel();
        campoBusqueda = new javax.swing.JTextField();
        botonLupa = new javax.swing.JButton();
        panelFuncionalidades = new javax.swing.JPanel();
        botonCuenta = new javax.swing.JButton();
        botonListaDeseos = new javax.swing.JButton();
        botonCesta = new javax.swing.JButton();
        panelCuerpo = new javax.swing.JPanel();
        scrollPane = new javax.swing.JScrollPane(panelProductos);
        jPanel3 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        panelPrincipal.setBackground(new java.awt.Color(255, 255, 255));
        panelPrincipal.setForeground(new java.awt.Color(255, 255, 255));

        panelHome.setBackground(new java.awt.Color(102, 255, 102));

        labelLogo.setIcon(new javax.swing.ImageIcon("ImagenesDecoracion" + File.separator + "logoPrinc.png"));
        labelLogo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                labelLogoMousePressed(evt);
            }
        });

        javax.swing.GroupLayout panelHomeLayout = new javax.swing.GroupLayout(panelHome);
        panelHome.setLayout(panelHomeLayout);
        panelHomeLayout.setHorizontalGroup(
            panelHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelHomeLayout.createSequentialGroup()
                .addContainerGap(25, Short.MAX_VALUE)
                .addComponent(labelLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panelHomeLayout.setVerticalGroup(
            panelHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelHomeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelBuscador.setBackground(new java.awt.Color(102, 255, 102));

        campoBusqueda.setForeground(new java.awt.Color(153, 153, 153));
        campoBusqueda.setText("   Buscar...");
        campoBusqueda.setBorder(null);
        campoBusqueda.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                campoBusquedaFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                campoBusquedaFocusLost(evt);
            }
        });
        campoBusqueda.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                campoBusquedaKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                campoBusquedaKeyReleased(evt);
            }
        });

        botonLupa.setBackground(new java.awt.Color(255, 255, 255));
        botonLupa.setForeground(new java.awt.Color(255, 255, 255));
        botonLupa.setIcon(new javax.swing.ImageIcon("ImagenesDecoracion" + File.separator + "lupa.png"));
        botonLupa.setBorder(null);
        botonLupa.setBorderPainted(false);
        botonLupa.setFocusPainted(false);
        botonLupa.setFocusable(false);
        botonLupa.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                botonLupaMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                botonLupaMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                botonLupaMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout panelBuscadorLayout = new javax.swing.GroupLayout(panelBuscador);
        panelBuscador.setLayout(panelBuscadorLayout);
        panelBuscadorLayout.setHorizontalGroup(
            panelBuscadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBuscadorLayout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addComponent(campoBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, 538, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(botonLupa, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(41, Short.MAX_VALUE))
        );
        panelBuscadorLayout.setVerticalGroup(
            panelBuscadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBuscadorLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(panelBuscadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(campoBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(botonLupa, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(37, Short.MAX_VALUE))
        );

        panelFuncionalidades.setBackground(new java.awt.Color(102, 255, 102));

        botonCuenta.setText("Cuenta");
        botonCuenta.setFocusable(false);
        botonCuenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCuentaActionPerformed(evt);
            }
        });

        botonListaDeseos.setIcon(new javax.swing.ImageIcon("ImagenesDecoracion" + File.separator + "listaDeseos.png"));
        botonListaDeseos.setFocusable(false);
        botonListaDeseos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                botonListaDeseosMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                botonListaDeseosMouseReleased(evt);
            }
        });
        botonListaDeseos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonListaDeseosActionPerformed(evt);
            }
        });

        botonCesta.setIcon(new javax.swing.ImageIcon("ImagenesDecoracion" + File.separator + "carrito.png"));
        botonCesta.setFocusPainted(false);
        botonCesta.setFocusable(false);
        botonCesta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                botonCestaMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                botonCestaMouseReleased(evt);
            }
        });
        botonCesta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCestaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelFuncionalidadesLayout = new javax.swing.GroupLayout(panelFuncionalidades);
        panelFuncionalidades.setLayout(panelFuncionalidadesLayout);
        panelFuncionalidadesLayout.setHorizontalGroup(
            panelFuncionalidadesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFuncionalidadesLayout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addComponent(botonCuenta, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(57, 57, 57)
                .addComponent(botonListaDeseos, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(73, 73, 73)
                .addComponent(botonCesta, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(74, 74, 74))
        );
        panelFuncionalidadesLayout.setVerticalGroup(
            panelFuncionalidadesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFuncionalidadesLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(panelFuncionalidadesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(botonCuenta, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(botonListaDeseos, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(botonCesta, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(37, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelCabeceraLayout = new javax.swing.GroupLayout(panelCabecera);
        panelCabecera.setLayout(panelCabeceraLayout);
        panelCabeceraLayout.setHorizontalGroup(
            panelCabeceraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCabeceraLayout.createSequentialGroup()
                .addComponent(panelHome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(panelBuscador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(panelFuncionalidades, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelCabeceraLayout.setVerticalGroup(
            panelCabeceraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCabeceraLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(panelCabeceraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelHome, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelBuscador, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panelFuncionalidades, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        panelCuerpo.setBackground(new java.awt.Color(255, 255, 255));

        scrollPane.setBackground(new java.awt.Color(153, 0, 153));
        scrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setOpaque(false);

        jPanel3.setPreferredSize(new java.awt.Dimension(1400, 700));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1400, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 700, Short.MAX_VALUE)
        );

        scrollPane.setViewportView(jPanel3);

        javax.swing.GroupLayout panelCuerpoLayout = new javax.swing.GroupLayout(panelCuerpo);
        panelCuerpo.setLayout(panelCuerpoLayout);
        panelCuerpoLayout.setHorizontalGroup(
            panelCuerpoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        panelCuerpoLayout.setVerticalGroup(
            panelCuerpoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCuerpoLayout.createSequentialGroup()
                .addComponent(scrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 692, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout panelPrincipalLayout = new javax.swing.GroupLayout(panelPrincipal);
        panelPrincipal.setLayout(panelPrincipalLayout);
        panelPrincipalLayout.setHorizontalGroup(
            panelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelCabecera, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panelCuerpo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panelPrincipalLayout.setVerticalGroup(
            panelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPrincipalLayout.createSequentialGroup()
                .addComponent(panelCabecera, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(panelCuerpo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

    private void labelLogoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelLogoMousePressed

        InterfazPrincipal.getInstancia().setVisible(true);
        InterfazPrincipal.getInstancia().panelPrincipal.requestFocusInWindow();
        cerrar();
    }//GEN-LAST:event_labelLogoMousePressed

    private void campoBusquedaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_campoBusquedaFocusGained
        if(campoBusqueda.getText().equals("   Buscar..."))
        {
            campoBusqueda.setText("");
        }
    }//GEN-LAST:event_campoBusquedaFocusGained

    private void campoBusquedaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_campoBusquedaFocusLost
        if(campoBusqueda.getText().equals(""))
        {
            campoBusqueda.setText("   Buscar...");
        }
    }//GEN-LAST:event_campoBusquedaFocusLost

    private void campoBusquedaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoBusquedaKeyReleased

        try {
            // Establecer la conexión a la base de datos
            Connection cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pcworld", "pcworld", "pcworld2024");

            // Verificar si el texto de búsqueda no está vacío
            if (!campoBusqueda.getText().trim().isEmpty()) 
            {
                // Consulta SQL para obtener todos los productos que contienen la cadena de búsqueda
                String queryProducto = "SELECT * FROM productos WHERE `IdNombre` LIKE ?  AND `oculto` = ?";
                PreparedStatement pstmtProducto = cn.prepareStatement(queryProducto);
                pstmtProducto.setString(1, "%" + campoBusqueda.getText().trim() + "%"); // Utiliza % para buscar coincidencias parciales
                pstmtProducto.setString(2, "0");
                ResultSet rsProducto = pstmtProducto.executeQuery();

                JPopupMenu popupMenu = new JPopupMenu();

                // Establecer el máximo de filas para el PopupMenu
                int maxRows = 5;
                int rowCount = 0;

                // Iterar sobre los resultados de la consulta y agregar elementos al PopupMenu
                while (rsProducto.next() && rowCount < maxRows) {
                    String nombreProducto = rsProducto.getString("IdNombre");
                    JMenuItem menuItem = new JMenuItem(nombreProducto);
                    menuItem.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            InterfazProducto.getInstancia().cargarProducto(nombreProducto);
                            InterfazProducto.getInstancia().panelPrincipal.requestFocusInWindow();
                            InterfazProducto.getInstancia().setVisible(true);
                            campoBusqueda.setText("");
                            cerrar();
                        }
                    });
                    popupMenu.add(menuItem);
                    rowCount++;
                }

                // Establecer el ancho del PopupMenu para que coincida con el ancho del TextField
                popupMenu.setPreferredSize(new Dimension(campoBusqueda.getWidth() + botonLupa.getWidth(), popupMenu.getPreferredSize().height));

                // Mostrar el PopupMenu en la ubicación del campo de búsqueda
                popupMenu.show(campoBusqueda, 0, campoBusqueda.getHeight());

                // Mantener el foco en el TextField
                campoBusqueda.requestFocusInWindow();
            }
            cn.close();
        } catch(Exception e) {
            System.out.println("Error: " + e);
        }
    }//GEN-LAST:event_campoBusquedaKeyReleased

    private void botonCuentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCuentaActionPerformed
        if(botonCuenta.getText().equals("Cuenta"))
            new InterfazLogin().setVisible(true);
        else
        {
            popUpMenu = new JPopupMenu();

            Font fontNegrita = new Font("Arial", Font.BOLD, 14);
            Font fontNormal = new Font("Arial", Font.PLAIN, 11);

            Color colorNegro = Color.BLACK;
            Color colorGris = Color.GRAY;

            Color colorFondo = new Color(255, 255, 255);
            popUpMenu.setBackground(colorFondo);

            JLabel labelMiCuenta = new JLabel("Mi cuenta");
            labelMiCuenta.setFont(fontNegrita);
            labelMiCuenta.setForeground(colorNegro);
            labelMiCuenta.setIcon(new javax.swing.ImageIcon("ImagenesDecoracion" + File.separator + "cuenta.png"));

            JMenuItem misDatos = new JMenuItem("Mis datos");
            misDatos.setFont(fontNormal);
            misDatos.setForeground(colorGris);
            misDatos.setBackground(colorFondo);

            JMenuItem listaDeseos = new JMenuItem("Lista de deseos");
            listaDeseos.setFont(fontNormal);
            listaDeseos.setForeground(colorGris);
            listaDeseos.setBackground(colorFondo);

            JLabel labelMisPedidos = new JLabel("Mis pedidos");
            labelMisPedidos.setFont(fontNegrita);
            labelMisPedidos.setForeground(colorNegro);
            labelMisPedidos.setIcon(new javax.swing.ImageIcon("ImagenesDecoracion" + File.separator + "pedidos.png"));

            JMenuItem pedidosFacturas;
            if(botonCuenta.getText().equals("Admin"))
                pedidosFacturas = new JMenuItem("Pedidos clientes");
            else
                pedidosFacturas = new JMenuItem("Pedidos y facturas");
            pedidosFacturas.setFont(fontNormal);
            pedidosFacturas.setForeground(colorGris);
            pedidosFacturas.setBackground(colorFondo);

            JLabel labelEspacio = new JLabel("            ");
            labelEspacio.setFont(fontNormal);
            labelEspacio.setForeground(colorGris);
            labelEspacio.setBackground(colorFondo);

            JMenuItem atenciónCliente = new JMenuItem("Atención al cliente");
            atenciónCliente.setFont(fontNegrita);
            atenciónCliente.setForeground(colorNegro);
            atenciónCliente.setBackground(colorFondo);
            atenciónCliente.setIcon(new javax.swing.ImageIcon("ImagenesDecoracion" + File.separator + "atCliente.png"));

            JMenuItem cerrarSesión = new JMenuItem("Cerrar sesión");
            cerrarSesión.setFont(fontNegrita);
            cerrarSesión.setForeground(colorNegro);
            cerrarSesión.setBackground(colorFondo);
            cerrarSesión.setIcon(new javax.swing.ImageIcon("ImagenesDecoracion" + File.separator + "logout.png"));

            misDatos.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    new InterfazMisDatos();
                }
            });
            listaDeseos.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        Connection cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pcworld", "pcworld", "pcworld2024");

                        String queryCliente = "SELECT * FROM lista_deseos WHERE `IdCliente` = ?";
                        PreparedStatement pstmtCliente = cn.prepareStatement(queryCliente);
                        pstmtCliente.setString(1, botonCuenta.getText());

                        ResultSet rsCliente = pstmtCliente.executeQuery();

                        if(rsCliente.next())
                        {
                            InterfazListaDeseos.getInstancia().setVisible(true);
                            InterfazListaDeseos.getInstancia().panelPrueba = null;
                            InterfazListaDeseos.getInstancia().panelPrueba = new JPanel(new GridLayout(0,1));
                            InterfazListaDeseos.getInstancia().scrollPane.setViewportView(InterfazListaDeseos.getInstancia().panelPrueba);
                            InterfazListaDeseos.getInstancia().cargarProductosDesdeBD();
                            InterfazListaDeseos.getInstancia().scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                            InterfazListaDeseos.getInstancia().scrollPane.setViewportView(InterfazListaDeseos.getInstancia().panelPrueba);
                            InterfazListaDeseos.getInstancia().panelPrincipal.requestFocusInWindow();
                            JViewport viewport = InterfazListaDeseos.getInstancia().scrollPane.getViewport();

                            // Establecer la posición de la vista en la parte superior
                            SwingUtilities.invokeLater(() -> {
                                viewport.setViewPosition(new Point(0, 0));
                            });
                            cerrar();
                        }else
                        {
                            new ErrorListaDeseosSinProductos();
                        }
                        cn.close();
                    } catch(Exception e1) {
                        System.out.println("Error: " + e1);
                    }
                }
            });
            pedidosFacturas.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if(!botonCuenta.getText().equals("Admin"))
                    {
                        try {
                            Connection cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pcworld", "pcworld", "pcworld2024");

                            String queryPedido = "SELECT * FROM pedidos WHERE `Usuario` = ?";
                            PreparedStatement pstmtPedido = cn.prepareStatement(queryPedido);
                            pstmtPedido.setString(1, botonCuenta.getText());

                            ResultSet rsPedido = pstmtPedido.executeQuery();
                            if(rsPedido.next())
                            {
                                new InterfazElegirPedido().setVisible(true);
                            }else
                            {
                                new ErrorSinPedidos();
                            }
                            cn.close();
                        } catch (SQLException ex) {
                            System.out.println("Error: " + e);
                        }
                    }
                    else
                    {
                        new InterfazElegirPedido().setVisible(true);
                    }
                }
            });
            atenciónCliente.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if(botonCuenta.getText().equals("Admin"))
                    {
                        new InterfazElegirChat();
                    }else
                    {
                        new InterfazAtencionCliente();
                    }
                }
            });
            cerrarSesión.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    botonCuenta.setText("Cuenta");
                    InterfazPrincipal.getInstancia().botonCuenta.setText("Cuenta");
                    InterfazProducto.getInstancia().botonCuenta.setText("Cuenta");
                    InterfazMostrarProductos.getInstancia().botonCuenta.setText("Cuenta");
                    InterfazPrincipal.getInstancia().setVisible(true);
                    cerrar();
                }
            });

            popUpMenu.add(labelMiCuenta);
            popUpMenu.add(misDatos);
            popUpMenu.add(listaDeseos);
            popUpMenu.add(labelMisPedidos);
            popUpMenu.add(pedidosFacturas);
            popUpMenu.add(labelEspacio);
            popUpMenu.add(atenciónCliente);
            popUpMenu.add(cerrarSesión);

            popUpMenu.show(botonCuenta, -30, botonCuenta.getHeight() + 3);
        }
    }//GEN-LAST:event_botonCuentaActionPerformed

    private void botonCestaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCestaActionPerformed
        if(this.botonCuenta.getText().equals("Cuenta"))
        {
            
            InterfazPrincipal.getInstancia().setVisible(true);
            new ErrorCesta();
            cerrar();
        }
    }//GEN-LAST:event_botonCestaActionPerformed

    private void botonListaDeseosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonListaDeseosActionPerformed
        if(!this.botonCuenta.getText().equals("Cuenta"))
        {
            try {
                // Establecer la conexión a la base de datos
                Connection cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pcworld", "pcworld", "pcworld2024");

                String queryCliente = "SELECT * FROM lista_deseos WHERE `IdCliente` = ?";
                PreparedStatement pstmtCliente = cn.prepareStatement(queryCliente);
                pstmtCliente.setString(1, botonCuenta.getText());
                
                ResultSet rsCliente = pstmtCliente.executeQuery();
                
                if(rsCliente.next())
                {
                    InterfazListaDeseos.getInstancia().setVisible(true);
                    InterfazListaDeseos.getInstancia().panelPrueba = null;
                    InterfazListaDeseos.getInstancia().panelPrueba = new JPanel(new GridLayout(0,1));
                    InterfazListaDeseos.getInstancia().scrollPane.setViewportView(InterfazListaDeseos.getInstancia().panelPrueba);
                    InterfazListaDeseos.getInstancia().cargarProductosDesdeBD();
                    InterfazListaDeseos.getInstancia().scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                    InterfazListaDeseos.getInstancia().scrollPane.setViewportView(InterfazListaDeseos.getInstancia().panelPrueba);
                    InterfazListaDeseos.getInstancia().panelPrincipal.requestFocusInWindow();
                    // Obtener la vista del JViewport
                    JViewport viewport = InterfazListaDeseos.getInstancia().scrollPane.getViewport();

                    // Establecer la posición de la vista en la parte superior
                    SwingUtilities.invokeLater(() -> {
                        viewport.setViewPosition(new Point(0, 0));
                    });
                    cerrar();
                }else
                {
                    new ErrorListaDeseosSinProductos();
                }
                cn.close();
            } catch(Exception e) {
                System.out.println("Error: " + e);
            }
        }else
        {
            new ErrorCesta();
        }
    }//GEN-LAST:event_botonListaDeseosActionPerformed

    private void botonLupaMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botonLupaMousePressed
        botonLupa.setIcon(new javax.swing.ImageIcon("ImagenesDecoracion" + File.separator + "lupaRelleno.png"));
    }//GEN-LAST:event_botonLupaMousePressed

    private void botonLupaMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botonLupaMouseReleased
        botonLupa.setIcon(new javax.swing.ImageIcon("ImagenesDecoracion" + File.separator + "lupa.png"));
    }//GEN-LAST:event_botonLupaMouseReleased

    private void botonListaDeseosMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botonListaDeseosMousePressed
        botonListaDeseos.setIcon(new javax.swing.ImageIcon("ImagenesDecoracion" + File.separator + "listaDeseosRelleno.png"));
    }//GEN-LAST:event_botonListaDeseosMousePressed

    private void botonListaDeseosMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botonListaDeseosMouseReleased
        botonListaDeseos.setIcon(new javax.swing.ImageIcon("ImagenesDecoracion" + File.separator + "listaDeseos.png"));
    }//GEN-LAST:event_botonListaDeseosMouseReleased

    private void botonCestaMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botonCestaMousePressed
        botonCesta.setIcon(new javax.swing.ImageIcon("ImagenesDecoracion" + File.separator + "carritoRelleno.png"));
    }//GEN-LAST:event_botonCestaMousePressed

    private void botonCestaMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botonCestaMouseReleased
        botonCesta.setIcon(new javax.swing.ImageIcon("ImagenesDecoracion" + File.separator + "carrito.png"));
    }//GEN-LAST:event_botonCestaMouseReleased

    private void botonLupaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botonLupaMouseClicked
        InterfazPrincipal.getInstancia().interfazActual = "InterfazCesta";
        InterfazPrincipal.getInstancia().campoBusqueda.setText("   Buscar...");
        InterfazListaDeseos.getInstancia().campoBusqueda.setText("   Buscar...");
        InterfazProducto.getInstancia().campoBusqueda.setText("   Buscar...");
        InterfazMostrarProductos.getInstancia().campoBusqueda.setText("   Buscar...");
        InterfazConfiguradorPcs.getInstancia().campoBusqueda.setText("   Buscar...");
        busquedaProducto();
    }//GEN-LAST:event_botonLupaMouseClicked

    private void campoBusquedaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoBusquedaKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER)
        {
            InterfazPrincipal.getInstancia().interfazActual = "InterfazCesta";
            InterfazPrincipal.getInstancia().campoBusqueda.setText("   Buscar...");
            InterfazListaDeseos.getInstancia().campoBusqueda.setText("   Buscar...");
            InterfazProducto.getInstancia().campoBusqueda.setText("   Buscar...");
            InterfazMostrarProductos.getInstancia().campoBusqueda.setText("   Buscar...");
            InterfazConfiguradorPcs.getInstancia().campoBusqueda.setText("   Buscar...");
            busquedaProducto();
        }
    }//GEN-LAST:event_campoBusquedaKeyPressed

    private void busquedaProducto()
    {
        InterfazMostrarProductos.getInstancia().panelProductos = null;
        InterfazMostrarProductos.getInstancia().panelProductos = new JPanel(new GridLayout(0,3, 20,38));
        InterfazMostrarProductos.getInstancia().panelProductos.setBackground(Color.YELLOW);
        InterfazMostrarProductos.getInstancia().panelFiltrosPers.removeAll();
        if(campoBusqueda.getText().toLowerCase().equals("portatil") || campoBusqueda.getText().toLowerCase().equals("portátil") || campoBusqueda.getText().toLowerCase().equals("portatiles") || campoBusqueda.getText().toLowerCase().equals("portátiles"))
        {
            InterfazPrincipal.getInstancia().tipoMostrarProducto = "Portatil";
            InterfazMostrarProductos.getInstancia().panelFiltrosPers.add(InterfazPanelFiltrosPortatiles.crearNuevaInstancia().panelPrincipal);
        }
        else if (campoBusqueda.getText().toLowerCase().equals("torre") || campoBusqueda.getText().toLowerCase().equals("torres") || campoBusqueda.getText().toLowerCase().equals("pc") || campoBusqueda.getText().toLowerCase().equals("pcs"))
        {
            InterfazPrincipal.getInstancia().tipoMostrarProducto = "Torre";
            InterfazMostrarProductos.getInstancia().panelFiltrosPers.add(InterfazPanelFiltrosTorres.crearNuevaInstancia().panelPrincipal);
        }
        else if (campoBusqueda.getText().toLowerCase().equals("componentes") || campoBusqueda.getText().toLowerCase().equals("componente"))
        {
            InterfazPrincipal.getInstancia().tipoMostrarProducto = "Componentes";
            InterfazMostrarProductos.getInstancia().panelFiltrosPers.add(InterfazPanelFiltrosComponentes.crearNuevaInstancia().panelPrincipal);
        }
        else 
        {
            InterfazMostrarProductos.getInstancia().panelFiltrosPers.add(InterfazPanelFiltrosNombres.crearNuevaInstancia().panelPrincipal);
        }

        InterfazMostrarProductos.getInstancia().scrollPane.setViewportView(InterfazMostrarProductos.getInstancia().panelProductos);
        InterfazMostrarProductos.getInstancia().cargarProductosDesdeBD();
        InterfazMostrarProductos.getInstancia().scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        InterfazMostrarProductos.getInstancia().scrollPane.setViewportView(InterfazMostrarProductos.getInstancia().panelProductos);
        // Obtener la vista del JViewport
        JViewport viewport = InterfazMostrarProductos.getInstancia().scrollPane.getViewport();

        // Establecer la posición de la vista en la parte superior
        SwingUtilities.invokeLater(() -> {
            viewport.setViewPosition(new Point(0, 0));
        });
        InterfazMostrarProductos.getInstancia().setVisible(true);
        InterfazMostrarProductos.getInstancia().panelPrincipal.requestFocusInWindow();
        cerrar();
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton botonCesta;
    public javax.swing.JButton botonCuenta;
    private javax.swing.JButton botonListaDeseos;
    private javax.swing.JButton botonLupa;
    public javax.swing.JTextField campoBusqueda;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel labelLogo;
    private javax.swing.JPanel panelBuscador;
    private javax.swing.JPanel panelCabecera;
    private javax.swing.JPanel panelCuerpo;
    private javax.swing.JPanel panelFuncionalidades;
    private javax.swing.JPanel panelHome;
    public javax.swing.JPanel panelPrincipal;
    public javax.swing.JScrollPane scrollPane;
    // End of variables declaration//GEN-END:variables
    private javax.swing.JPopupMenu popUpMenu;
    private static InterfazCesta instancia;
    private double subTotal;
    private JLabel precioLabel;
    public boolean cliente;
    public JPanel panelProductos, panelDineroTotal;
}
