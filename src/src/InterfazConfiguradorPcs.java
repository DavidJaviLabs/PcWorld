package src;

import ErroresInterfaz.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.*;
import java.text.*;
import java.util.*;
import javax.swing.*;


public class InterfazConfiguradorPcs extends javax.swing.JFrame {

    /**
     * Creates new form InterfazCofiguradorPcs
     */
    public InterfazConfiguradorPcs() 
    {
        initComponents();   
        
        productoUser = false;
        precioAnterior = 0.0;
        precioTotal = 0.0;
        preciosAnteriores = new HashMap<>();
        
        cargarComponentes();
    }
    
    public static InterfazConfiguradorPcs getInstancia()
    {
        if(instancia == null)
        {
            instancia = new InterfazConfiguradorPcs();
        }
        return instancia;
    }
    
    public static InterfazConfiguradorPcs crearNuevaInstancia() 
    {
        instancia = null;
        return getInstancia();
    }
    
    private void cerrar()
    {
        dispose();
        InterfazCesta.getInstancia().campoBusqueda.setText("   Buscar...");
        InterfazListaDeseos.getInstancia().campoBusqueda.setText("   Buscar...");
        InterfazProducto.getInstancia().campoBusqueda.setText("   Buscar...");
        InterfazMostrarProductos.getInstancia().campoBusqueda.setText("   Buscar...");
        InterfazPrincipal.getInstancia().campoBusqueda.setText("   Buscar...");
    }

    private void busquedaProducto()
    {
        InterfazMostrarProductos.getInstancia().reiniciarBotonesAscDesc();
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
    
    private void cargarComponentes()
    {
        modeloProcesador = new DefaultComboBoxModel <>();
        modeloPlacaBase = new DefaultComboBoxModel <>();
        modeloMemoriaRam = new DefaultComboBoxModel <>();
        modeloRefrigeracion = new DefaultComboBoxModel <>();
        modeloFuenteAlimentacion = new DefaultComboBoxModel <>();
        modeloTarjetaGrafica = new DefaultComboBoxModel <>();
        modeloDiscoDuro = new DefaultComboBoxModel <>();
        modeloDiscoDuro2 = new DefaultComboBoxModel <>();
        DecimalFormat df = new DecimalFormat("#,###,###.##");
        String precio = "";
        try 
        {
            Connection cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pcworld", "pcworld", "pcworld2024");
            String sql = "SELECT * FROM productos WHERE (`tipoProducto` NOT IN (?, ?)) AND `oculto` = ?";
            PreparedStatement pstmtProductos = cn.prepareStatement(sql);
            pstmtProductos.setString(1, "Torre");
            pstmtProductos.setString(2, "Portatil");
            pstmtProductos.setString(3, "0");
            ResultSet rsProductos = pstmtProductos.executeQuery();
            
            modeloProcesador.addElement("");
            modeloPlacaBase.addElement("");
            modeloMemoriaRam.addElement("");
            modeloRefrigeracion.addElement("");
            modeloFuenteAlimentacion.addElement("");
            modeloTarjetaGrafica.addElement("");
            modeloDiscoDuro.addElement("");
            modeloDiscoDuro2.addElement("");
            
            while(rsProductos.next())
            {
                if(Integer.parseInt(rsProductos.getString("stockProducto")) >= 1)
                {
                    precio = df.format(rsProductos.getDouble("precioProducto"));
                    switch (rsProductos.getString("tipoProducto")) {
                        case "CPU" -> modeloProcesador.addElement(rsProductos.getString("IdNombre") + " - " + precio + " €");
                        case "Placa base" -> modeloPlacaBase.addElement(rsProductos.getString("IdNombre") + " - " + precio + " €");
                        case "Memoria RAM" -> modeloMemoriaRam.addElement(rsProductos.getString("IdNombre") + " - " + precio + " €");
                        case "Refrigeracion" -> modeloRefrigeracion.addElement(rsProductos.getString("IdNombre") + " - " + precio + " €");
                        case "Fuente alimentación" -> modeloFuenteAlimentacion.addElement(rsProductos.getString("IdNombre") + " - " + precio + " €");
                        case "Tarjeta Grafica" -> modeloTarjetaGrafica.addElement(rsProductos.getString("IdNombre") + " - " + precio + " €");
                        case "Disco duro" -> {
                            modeloDiscoDuro.addElement(rsProductos.getString("IdNombre") + " - " + precio + " €");
                            modeloDiscoDuro2.addElement(rsProductos.getString("IdNombre") + " - " + precio + " €");
                        }
                        default -> {
                        }
                    }
                }
            }
            comboBoxDiscoDuro.setModel(modeloDiscoDuro);
            comboBoxDiscoDuro2.setModel(modeloDiscoDuro2);
            comboBoxFuenteAlimentacion.setModel(modeloFuenteAlimentacion);
            comboBoxMemoriaRam.setModel(modeloMemoriaRam);
            comboBoxPlacaBase.setModel(modeloPlacaBase);
            comboBoxProcesador.setModel(modeloProcesador);
            comboBoxRefrigeracion.setModel(modeloRefrigeracion);
            comboBoxTarjetaGrafica.setModel(modeloTarjetaGrafica);
            cn.close();
        } 
        catch (SQLException ex) 
        {
            System.err.println(ex.getMessage());
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

        popUpMenu = new javax.swing.JPopupMenu();
        popupMenu = new javax.swing.JPopupMenu();
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
        scrollPane = new javax.swing.JScrollPane();
        panelConfig = new javax.swing.JPanel();
        labelProcesador = new javax.swing.JLabel();
        labelPlacaBase = new javax.swing.JLabel();
        labelMemoriaRam = new javax.swing.JLabel();
        labelRefrigeracion = new javax.swing.JLabel();
        labelFuenteAlimentacion = new javax.swing.JLabel();
        labelTarjetaGrafica = new javax.swing.JLabel();
        labelDiscoDuro = new javax.swing.JLabel();
        labelDiscoDuro2 = new javax.swing.JLabel();
        labelMiConfig = new javax.swing.JLabel();
        labelReiniciar = new javax.swing.JLabel();
        labelGuardar = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        comboBoxProcesador = new javax.swing.JComboBox<>();
        comboBoxPlacaBase = new javax.swing.JComboBox<>();
        comboBoxMemoriaRam = new javax.swing.JComboBox<>();
        comboBoxRefrigeracion = new javax.swing.JComboBox<>();
        comboBoxFuenteAlimentacion = new javax.swing.JComboBox<>();
        comboBoxTarjetaGrafica = new javax.swing.JComboBox<>();
        comboBoxDiscoDuro = new javax.swing.JComboBox<>();
        comboBoxDiscoDuro2 = new javax.swing.JComboBox<>();
        panelPrecioFinal = new javax.swing.JPanel();
        labelPrecio = new javax.swing.JLabel();
        labelPrecioTotal = new javax.swing.JLabel();
        labelAñadirCarrito = new javax.swing.JLabel();
        labelErrorCesta = new javax.swing.JLabel();
        labelErrorProcesador = new javax.swing.JLabel();
        labelErrorPlacaBase = new javax.swing.JLabel();
        labelErrorMemoriaRam = new javax.swing.JLabel();
        labelErrorRefrigeracion = new javax.swing.JLabel();
        labelErrorFuenteAlimentacion = new javax.swing.JLabel();
        labelErrorTarjetaGrafica = new javax.swing.JLabel();
        labelErrorDiscoDuro = new javax.swing.JLabel();
        labelErrorDiscoDuro2 = new javax.swing.JLabel();
        labelErrorListaDeDeseos = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        panelPrincipal.setBackground(new java.awt.Color(255, 255, 255));
        panelPrincipal.setForeground(new java.awt.Color(255, 255, 255));

        panelHome.setBackground(new java.awt.Color(102, 255, 102));

        labelLogo.setIcon(new javax.swing.ImageIcon("ImagenesDecoracion" + File.separator + "logoPrinc.png"));
        labelLogo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                labelLogoMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout panelHomeLayout = new javax.swing.GroupLayout(panelHome);
        panelHome.setLayout(panelHomeLayout);
        panelHomeLayout.setHorizontalGroup(
            panelHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelHomeLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(labelLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panelHomeLayout.setVerticalGroup(
            panelHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelHomeLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(labelLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(17, Short.MAX_VALUE))
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
                .addGap(41, 41, 41))
        );
        panelBuscadorLayout.setVerticalGroup(
            panelBuscadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBuscadorLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(panelBuscadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(campoBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(botonLupa, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37))
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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelFuncionalidadesLayout.setVerticalGroup(
            panelFuncionalidadesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFuncionalidadesLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(panelFuncionalidadesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(botonCuenta, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(botonListaDeseos, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(botonCesta, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37))
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
                    .addComponent(panelHome, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panelBuscador, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panelFuncionalidades, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        panelCuerpo.setBackground(new java.awt.Color(255, 255, 0));

        scrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        panelConfig.setBackground(new java.awt.Color(255, 255, 255));

        labelProcesador.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        labelProcesador.setText("PROCESADOR: ");

        labelPlacaBase.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        labelPlacaBase.setText("PLACA BASE: ");

        labelMemoriaRam.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        labelMemoriaRam.setText("MEMORIA RAM:");

        labelRefrigeracion.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        labelRefrigeracion.setText("REFRIGERACIÓN: ");

        labelFuenteAlimentacion.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        labelFuenteAlimentacion.setText("FUENTE DE ALIMENTACIÓN:");

        labelTarjetaGrafica.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        labelTarjetaGrafica.setText("TARJETA GRÁFICA:");

        labelDiscoDuro.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        labelDiscoDuro.setText("DISCO DURO:");

        labelDiscoDuro2.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        labelDiscoDuro2.setText("DISCO DURO 2:");

        labelMiConfig.setFont(new java.awt.Font("Arial", 1, 28)); // NOI18N
        labelMiConfig.setForeground(new java.awt.Color(0, 0, 0));
        labelMiConfig.setText("Mi configuración");

        labelReiniciar.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        labelReiniciar.setForeground(new java.awt.Color(0, 0, 0));
        labelReiniciar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelReiniciar.setIcon(new javax.swing.ImageIcon("ImagenesDecoracion" + File.separator + "trash.png"));
        labelReiniciar.setText("REINICIAR");
        labelReiniciar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        labelReiniciar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                labelReiniciarMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                labelReiniciarMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                labelReiniciarMouseReleased(evt);
            }
        });

        labelGuardar.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        labelGuardar.setForeground(new java.awt.Color(0, 0, 0));
        labelGuardar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelGuardar.setIcon(new javax.swing.ImageIcon("ImagenesDecoracion" + File.separator + "listaDeseos.png"));
        labelGuardar.setText("GUARDAR");
        labelGuardar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        labelGuardar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                labelGuardarMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                labelGuardarMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                labelGuardarMouseReleased(evt);
            }
        });

        jSeparator1.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator1.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator1.setFocusable(true);
        jSeparator1.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N

        comboBoxProcesador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxProcesadorActionPerformed(evt);
            }
        });

        comboBoxPlacaBase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxPlacaBaseActionPerformed(evt);
            }
        });

        comboBoxMemoriaRam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxMemoriaRamActionPerformed(evt);
            }
        });

        comboBoxRefrigeracion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxRefrigeracionActionPerformed(evt);
            }
        });

        comboBoxFuenteAlimentacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxFuenteAlimentacionActionPerformed(evt);
            }
        });

        comboBoxTarjetaGrafica.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxTarjetaGraficaActionPerformed(evt);
            }
        });

        comboBoxDiscoDuro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxDiscoDuroActionPerformed(evt);
            }
        });

        comboBoxDiscoDuro2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxDiscoDuro2ActionPerformed(evt);
            }
        });

        panelPrecioFinal.setBackground(new java.awt.Color(89, 223, 89));
        panelPrecioFinal.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));

        labelPrecio.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        labelPrecio.setText("PRECIO TOTAL: ");

        labelPrecioTotal.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        labelPrecioTotal.setText("0€");

        labelAñadirCarrito.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        labelAñadirCarrito.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelAñadirCarrito.setIcon(new javax.swing.ImageIcon("ImagenesDecoracion" + File.separator + "carrito.png"));
        labelAñadirCarrito.setText(" AÑADIR A LA CESTA");
        labelAñadirCarrito.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        labelAñadirCarrito.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                labelAñadirCarritoMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                labelAñadirCarritoMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                labelAñadirCarritoMouseReleased(evt);
            }
        });

        labelErrorCesta.setText(" ");

        javax.swing.GroupLayout panelPrecioFinalLayout = new javax.swing.GroupLayout(panelPrecioFinal);
        panelPrecioFinal.setLayout(panelPrecioFinalLayout);
        panelPrecioFinalLayout.setHorizontalGroup(
            panelPrecioFinalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPrecioFinalLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(labelPrecio)
                .addGap(18, 18, 18)
                .addComponent(labelPrecioTotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(panelPrecioFinalLayout.createSequentialGroup()
                .addGap(61, 61, 61)
                .addGroup(panelPrecioFinalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(labelAñadirCarrito, javax.swing.GroupLayout.DEFAULT_SIZE, 219, Short.MAX_VALUE)
                    .addComponent(labelErrorCesta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(61, Short.MAX_VALUE))
        );
        panelPrecioFinalLayout.setVerticalGroup(
            panelPrecioFinalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPrecioFinalLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(panelPrecioFinalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(labelPrecio, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(labelPrecioTotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(46, 46, 46)
                .addComponent(labelErrorCesta, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelAñadirCarrito, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(17, Short.MAX_VALUE))
        );

        labelErrorProcesador.setText(" ");

        labelErrorPlacaBase.setText(" ");

        labelErrorMemoriaRam.setText(" ");

        labelErrorRefrigeracion.setText(" ");

        labelErrorFuenteAlimentacion.setText(" ");

        labelErrorTarjetaGrafica.setText(" ");

        labelErrorDiscoDuro.setText(" ");

        labelErrorDiscoDuro2.setText(" ");

        labelErrorListaDeDeseos.setText(" ");

        javax.swing.GroupLayout panelConfigLayout = new javax.swing.GroupLayout(panelConfig);
        panelConfig.setLayout(panelConfigLayout);
        panelConfigLayout.setHorizontalGroup(
            panelConfigLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelConfigLayout.createSequentialGroup()
                .addGroup(panelConfigLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelConfigLayout.createSequentialGroup()
                        .addGap(60, 60, 60)
                        .addGroup(panelConfigLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(panelConfigLayout.createSequentialGroup()
                                .addGap(17, 17, 17)
                                .addComponent(labelMiConfig)
                                .addGap(142, 142, 142)
                                .addComponent(labelReiniciar, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(112, 112, 112)
                                .addComponent(labelGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(labelErrorListaDeDeseos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 928, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panelConfigLayout.createSequentialGroup()
                        .addGap(76, 76, 76)
                        .addGroup(panelConfigLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(panelConfigLayout.createSequentialGroup()
                                .addGroup(panelConfigLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(labelProcesador, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(labelPlacaBase)
                                    .addComponent(labelErrorProcesador, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(labelErrorPlacaBase, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(187, 187, 187)
                                .addComponent(comboBoxPlacaBase, javax.swing.GroupLayout.PREFERRED_SIZE, 542, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelConfigLayout.createSequentialGroup()
                                .addGroup(panelConfigLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(labelErrorMemoriaRam, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(labelMemoriaRam, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(comboBoxMemoriaRam, javax.swing.GroupLayout.PREFERRED_SIZE, 542, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelConfigLayout.createSequentialGroup()
                                .addGroup(panelConfigLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(labelErrorRefrigeracion, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(labelRefrigeracion, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(comboBoxRefrigeracion, javax.swing.GroupLayout.PREFERRED_SIZE, 542, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelConfigLayout.createSequentialGroup()
                                .addGroup(panelConfigLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(labelErrorFuenteAlimentacion, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(labelFuenteAlimentacion, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(comboBoxFuenteAlimentacion, javax.swing.GroupLayout.PREFERRED_SIZE, 542, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelConfigLayout.createSequentialGroup()
                                .addGroup(panelConfigLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(labelErrorTarjetaGrafica, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(labelTarjetaGrafica, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(comboBoxTarjetaGrafica, javax.swing.GroupLayout.PREFERRED_SIZE, 542, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelConfigLayout.createSequentialGroup()
                                .addGroup(panelConfigLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelConfigLayout.createSequentialGroup()
                                        .addComponent(labelDiscoDuro)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(panelConfigLayout.createSequentialGroup()
                                        .addComponent(labelErrorDiscoDuro, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGap(137, 137, 137)))
                                .addComponent(comboBoxDiscoDuro, javax.swing.GroupLayout.PREFERRED_SIZE, 542, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelConfigLayout.createSequentialGroup()
                                .addGroup(panelConfigLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(labelErrorDiscoDuro2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(labelDiscoDuro2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(comboBoxDiscoDuro2, javax.swing.GroupLayout.PREFERRED_SIZE, 542, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelConfigLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(comboBoxProcesador, javax.swing.GroupLayout.PREFERRED_SIZE, 542, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                .addComponent(panelPrecioFinal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(428, 428, 428))
        );
        panelConfigLayout.setVerticalGroup(
            panelConfigLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelConfigLayout.createSequentialGroup()
                .addGap(79, 79, 79)
                .addGroup(panelConfigLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelConfigLayout.createSequentialGroup()
                        .addGroup(panelConfigLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(labelMiConfig, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(labelReiniciar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(labelGuardar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(labelErrorListaDeDeseos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(26, 26, 26)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addGroup(panelConfigLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelProcesador)
                            .addComponent(comboBoxProcesador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelErrorProcesador, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(39, 39, 39)
                        .addGroup(panelConfigLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelPlacaBase)
                            .addComponent(comboBoxPlacaBase, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(panelPrecioFinal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelErrorPlacaBase, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39)
                .addGroup(panelConfigLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelMemoriaRam)
                    .addComponent(comboBoxMemoriaRam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelErrorMemoriaRam, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39)
                .addGroup(panelConfigLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelRefrigeracion)
                    .addComponent(comboBoxRefrigeracion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelErrorRefrigeracion, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addGroup(panelConfigLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelFuenteAlimentacion)
                    .addComponent(comboBoxFuenteAlimentacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelErrorFuenteAlimentacion, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39)
                .addGroup(panelConfigLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelTarjetaGrafica)
                    .addComponent(comboBoxTarjetaGrafica, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelErrorTarjetaGrafica, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39)
                .addGroup(panelConfigLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelDiscoDuro)
                    .addComponent(comboBoxDiscoDuro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelErrorDiscoDuro, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39)
                .addGroup(panelConfigLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelDiscoDuro2)
                    .addComponent(comboBoxDiscoDuro2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelErrorDiscoDuro2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        scrollPane.setViewportView(panelConfig);

        javax.swing.GroupLayout panelCuerpoLayout = new javax.swing.GroupLayout(panelCuerpo);
        panelCuerpo.setLayout(panelCuerpoLayout);
        panelCuerpoLayout.setHorizontalGroup(
            panelCuerpoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCuerpoLayout.createSequentialGroup()
                .addComponent(scrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 1399, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        panelCuerpoLayout.setVerticalGroup(
            panelCuerpoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCuerpoLayout.createSequentialGroup()
                .addComponent(scrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 700, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout panelPrincipalLayout = new javax.swing.GroupLayout(panelPrincipal);
        panelPrincipal.setLayout(panelPrincipalLayout);
        panelPrincipalLayout.setHorizontalGroup(
            panelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPrincipalLayout.createSequentialGroup()
                .addGroup(panelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(panelCabecera, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelCuerpo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0))
        );
        panelPrincipalLayout.setVerticalGroup(
            panelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPrincipalLayout.createSequentialGroup()
                .addComponent(panelCabecera, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(panelCuerpo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void labelLogoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelLogoMouseClicked
        InterfazPrincipal.getInstancia().setVisible(true);
        InterfazPrincipal.getInstancia().panelPrincipal.requestFocusInWindow();
        cerrar();
    }//GEN-LAST:event_labelLogoMouseClicked

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

    private void campoBusquedaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoBusquedaKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER)
        {
            InterfazMostrarProductos.getInstancia().reiniciarBotonesAscDesc();
            InterfazMostrarProductos.getInstancia().panelFiltrosPers.removeAll();
            InterfazPrincipal.getInstancia().tipoMostrarProducto = "";
            InterfazPrincipal.getInstancia().interfazActual = "InterfazCofiguradorPcs";
            InterfazCesta.getInstancia().campoBusqueda.setText("   Buscar...");
            InterfazListaDeseos.getInstancia().campoBusqueda.setText("   Buscar...");
            InterfazProducto.getInstancia().campoBusqueda.setText("   Buscar...");
            InterfazPrincipal.getInstancia().campoBusqueda.setText("   Buscar...");
            busquedaProducto();
            popupMenu.setVisible(false);
            panelPrincipal.requestFocusInWindow();
        }
    }//GEN-LAST:event_campoBusquedaKeyPressed

    private void campoBusquedaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoBusquedaKeyReleased

        try {
            // Establecer la conexión a la base de datos
            Connection cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pcworld", "pcworld", "pcworld2024");

            // Verificar si el texto de búsqueda no está vacío
            if (!campoBusqueda.getText().trim().isEmpty()) 
            {
                // Consulta SQL para obtener todos los productos que contienen la cadena de búsqueda
                String queryProducto = "SELECT * FROM productos WHERE `IdNombre` LIKE ? AND `oculto` = ?";
                PreparedStatement pstmtProducto = cn.prepareStatement(queryProducto);
                pstmtProducto.setString(1, "%" + campoBusqueda.getText().trim() + "%"); // Utiliza % para buscar coincidencias parciales
                pstmtProducto.setString(2, "0");
                ResultSet rsProducto = pstmtProducto.executeQuery();

                popupMenu = new JPopupMenu();

                // Establecer el máximo de filas para el PopupMenu
                int maxRows = 5;
                int rowCount = 0;

                // Iterar sobre los resultados de la consulta y agregar elementos al PopupMenu
                while (rsProducto.next() && rowCount < maxRows) {
                    String nombreProducto = rsProducto.getString("IdNombre");
                    JMenuItem menuItem = new JMenuItem(nombreProducto);
                    menuItem.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            productoUser = true;
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

    private void botonLupaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botonLupaMouseClicked
        InterfazMostrarProductos.getInstancia().reiniciarBotonesAscDesc();
        InterfazPrincipal.getInstancia().interfazActual = "InterfazPrincipal";
        InterfazCesta.getInstancia().campoBusqueda.setText("   Buscar...");
        InterfazListaDeseos.getInstancia().campoBusqueda.setText("   Buscar...");
        InterfazProducto.getInstancia().campoBusqueda.setText("   Buscar...");
        InterfazMostrarProductos.getInstancia().campoBusqueda.setText("   Buscar...");
        busquedaProducto();
    }//GEN-LAST:event_botonLupaMouseClicked

    private void botonLupaMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botonLupaMousePressed
        botonLupa.setIcon(new javax.swing.ImageIcon("ImagenesDecoracion" + File.separator + "lupaRelleno.png"));
    }//GEN-LAST:event_botonLupaMousePressed

    private void botonLupaMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botonLupaMouseReleased
        botonLupa.setIcon(new javax.swing.ImageIcon("ImagenesDecoracion" + File.separator + "lupa.png"));
    }//GEN-LAST:event_botonLupaMouseReleased

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

            JMenuItem configuracion = new JMenuItem("Configuración");
            configuracion.setFont(fontNormal);
            configuracion.setForeground(colorGris);
            configuracion.setBackground(colorFondo);

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
                    } catch(Exception e1) {
                        System.out.println("Error: " + e1);
                    }
                }
            });
            configuracion.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // Cambiar el color de fondo
                    Color color = JColorChooser.showDialog(InterfazConfiguradorPcs.this, "Selecciona un color", panelPrincipal.getBackground());
                    if (color != null) 
                    {
                        //INTERFAZ PRINCIPAL
                        panelPrincipal.setBackground(color);
                        panelCuerpo.setBackground(color);
                        panelConfig.setBackground(color);
                        panelPrecioFinal.setBackground(color);
                        JOptionPane.showMessageDialog(InterfazConfiguradorPcs.this, "Color de fondo actualizado");
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
                        InterfazAtencionCliente.crearNuevaInstancia();
                    }
                }
            });
            cerrarSesión.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    botonCuenta.setText("Cuenta");
                    InterfazProducto.getInstancia().botonCuenta.setText("Cuenta");
                    InterfazCesta.getInstancia().botonCuenta.setText("Cuenta");
                    InterfazPrincipal.getInstancia().botonCuenta.setText("Cuenta");
                }
            });

            popUpMenu.add(labelMiCuenta);
            popUpMenu.add(misDatos);
            popUpMenu.add(listaDeseos);
            if(botonCuenta.getText().equals("Admin"))
                popUpMenu.add(configuracion);
            popUpMenu.add(labelMisPedidos);
            popUpMenu.add(pedidosFacturas);
            popUpMenu.add(labelEspacio);
            popUpMenu.add(atenciónCliente);
            popUpMenu.add(cerrarSesión);

            popUpMenu.show(botonCuenta, -30, botonCuenta.getHeight() + 3);
        }
    }//GEN-LAST:event_botonCuentaActionPerformed

    private void botonListaDeseosMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botonListaDeseosMousePressed
        botonListaDeseos.setIcon(new javax.swing.ImageIcon("ImagenesDecoracion" + File.separator + "listaDeseosRelleno.png"));
    }//GEN-LAST:event_botonListaDeseosMousePressed

    private void botonListaDeseosMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botonListaDeseosMouseReleased
        botonListaDeseos.setIcon(new javax.swing.ImageIcon("ImagenesDecoracion" + File.separator + "listaDeseos.png"));
    }//GEN-LAST:event_botonListaDeseosMouseReleased

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

    private void botonCestaMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botonCestaMousePressed
        botonCesta.setIcon(new javax.swing.ImageIcon("ImagenesDecoracion" + File.separator + "carritoRelleno.png"));
    }//GEN-LAST:event_botonCestaMousePressed

    private void botonCestaMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botonCestaMouseReleased
        botonCesta.setIcon(new javax.swing.ImageIcon("ImagenesDecoracion" + File.separator + "carrito.png"));
    }//GEN-LAST:event_botonCestaMouseReleased

    private void botonCestaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCestaActionPerformed
        if(!this.botonCuenta.getText().equals("Cuenta"))
        {
            InterfazCesta.getInstancia().panelProductos = null;
            InterfazCesta.getInstancia().panelProductos = new JPanel(new GridLayout(0,1));
            InterfazCesta.getInstancia().scrollPane.setViewportView(InterfazCesta.getInstancia().panelProductos);
            InterfazCesta.getInstancia().cargarProductosDesdeBD();
            InterfazCesta.getInstancia().noCargarProductosDesdeBD();
            InterfazCesta.getInstancia().scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            InterfazCesta.getInstancia().scrollPane.setViewportView(InterfazCesta.getInstancia().panelProductos);

            // Obtener la vista del JViewport
            JViewport viewport = InterfazCesta.getInstancia().scrollPane.getViewport();

            // Establecer la posición de la vista en la parte superior
            SwingUtilities.invokeLater(() -> {
                viewport.setViewPosition(new Point(0, 0));
            });

            if(InterfazCesta.getInstancia().cliente)
            {
                InterfazCesta.getInstancia().setVisible(true);
                cerrar();
                InterfazCesta.getInstancia().cliente = false;
            }
            InterfazCesta.getInstancia().panelPrincipal.requestFocusInWindow();
        }else
        {
            new ErrorCesta();
        }
    }//GEN-LAST:event_botonCestaActionPerformed

    private void labelReiniciarMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelReiniciarMousePressed
        labelReiniciar.setIcon(new javax.swing.ImageIcon("ImagenesDecoracion" + File.separator + "trashRelleno.png"));
    }//GEN-LAST:event_labelReiniciarMousePressed

    private void labelReiniciarMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelReiniciarMouseReleased
        labelReiniciar.setIcon(new javax.swing.ImageIcon("ImagenesDecoracion" + File.separator + "trash.png"));
    }//GEN-LAST:event_labelReiniciarMouseReleased

    private void labelGuardarMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelGuardarMousePressed
        labelGuardar.setIcon(new javax.swing.ImageIcon("ImagenesDecoracion" + File.separator + "listaDeseosRelleno.png"));
    }//GEN-LAST:event_labelGuardarMousePressed

    private void labelGuardarMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelGuardarMouseReleased
        labelGuardar.setIcon(new javax.swing.ImageIcon("ImagenesDecoracion" + File.separator + "listaDeseos.png"));
    }//GEN-LAST:event_labelGuardarMouseReleased
      
    private void comboBoxProcesadorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxProcesadorActionPerformed
        String elementoSeleccionado = (String) comboBoxProcesador.getSelectedItem();
        manejarSeleccionComponente(elementoSeleccionado, "Procesador");
        if(!(comboBoxProcesador.getSelectedItem() == null || comboBoxProcesador.getSelectedItem().equals("")))
            labelErrorProcesador.setText(" ");
        panelPrincipal.requestFocusInWindow();
    }//GEN-LAST:event_comboBoxProcesadorActionPerformed

    private void comboBoxPlacaBaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxPlacaBaseActionPerformed
        String elementoSeleccionado = (String) comboBoxPlacaBase.getSelectedItem();
        manejarSeleccionComponente(elementoSeleccionado, "PlacaBase");
        if(!(comboBoxPlacaBase.getSelectedItem() == null || comboBoxPlacaBase.getSelectedItem().equals("")))
            labelErrorPlacaBase.setText(" ");
        panelPrincipal.requestFocusInWindow();
    }//GEN-LAST:event_comboBoxPlacaBaseActionPerformed

    private void comboBoxMemoriaRamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxMemoriaRamActionPerformed
        String elementoSeleccionado = (String) comboBoxMemoriaRam.getSelectedItem();
        manejarSeleccionComponente(elementoSeleccionado, "MemoriaRam");
        if(!(comboBoxMemoriaRam.getSelectedItem() == null || comboBoxMemoriaRam.getSelectedItem().equals("")))
            labelErrorMemoriaRam.setText(" ");
        panelPrincipal.requestFocusInWindow();
    }//GEN-LAST:event_comboBoxMemoriaRamActionPerformed

    private void comboBoxRefrigeracionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxRefrigeracionActionPerformed
        String elementoSeleccionado = (String) comboBoxRefrigeracion.getSelectedItem();
        manejarSeleccionComponente(elementoSeleccionado, "Refrigeracion");
        if(!(comboBoxRefrigeracion.getSelectedItem() == null || comboBoxRefrigeracion.getSelectedItem().equals("")))
            labelErrorRefrigeracion.setText(" ");
        panelPrincipal.requestFocusInWindow();
    }//GEN-LAST:event_comboBoxRefrigeracionActionPerformed

    private void comboBoxFuenteAlimentacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxFuenteAlimentacionActionPerformed
        String elementoSeleccionado = (String) comboBoxFuenteAlimentacion.getSelectedItem();
        manejarSeleccionComponente(elementoSeleccionado, "FuenteAlimentacion");
        if(!(comboBoxFuenteAlimentacion.getSelectedItem() == null || comboBoxFuenteAlimentacion.getSelectedItem().equals("")))
            labelErrorFuenteAlimentacion.setText(" ");
        panelPrincipal.requestFocusInWindow();
    }//GEN-LAST:event_comboBoxFuenteAlimentacionActionPerformed

    private void comboBoxTarjetaGraficaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxTarjetaGraficaActionPerformed
        String elementoSeleccionado = (String) comboBoxTarjetaGrafica.getSelectedItem();
        manejarSeleccionComponente(elementoSeleccionado, "TarjetaGrafica");
        if(!(comboBoxTarjetaGrafica.getSelectedItem() == null || comboBoxTarjetaGrafica.getSelectedItem().equals("")))
            labelErrorTarjetaGrafica.setText(" ");
        panelPrincipal.requestFocusInWindow();
    }//GEN-LAST:event_comboBoxTarjetaGraficaActionPerformed

    private void comboBoxDiscoDuroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxDiscoDuroActionPerformed
        String elementoSeleccionado = (String) comboBoxDiscoDuro.getSelectedItem();
        manejarSeleccionComponente(elementoSeleccionado, "DiscoDuro");
        if(!(comboBoxDiscoDuro.getSelectedItem() == null || comboBoxDiscoDuro.getSelectedItem().equals("")))
            labelErrorDiscoDuro.setText(" ");
        panelPrincipal.requestFocusInWindow();
    }//GEN-LAST:event_comboBoxDiscoDuroActionPerformed

    private void comboBoxDiscoDuro2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxDiscoDuro2ActionPerformed
        String elementoSeleccionado = (String) comboBoxDiscoDuro2.getSelectedItem();
        manejarSeleccionComponente(elementoSeleccionado, "DiscoDuro2");
        if(!(comboBoxDiscoDuro2.getSelectedItem() == null || comboBoxDiscoDuro2.getSelectedItem().equals("")))
            labelErrorDiscoDuro2.setText(" ");
        panelPrincipal.requestFocusInWindow();
    }//GEN-LAST:event_comboBoxDiscoDuro2ActionPerformed

    private void labelReiniciarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelReiniciarMouseClicked
        comboBoxProcesador.setSelectedItem(null);
        comboBoxPlacaBase.setSelectedItem(null);
        comboBoxMemoriaRam.setSelectedItem(null);
        comboBoxRefrigeracion.setSelectedItem(null);
        comboBoxFuenteAlimentacion.setSelectedItem(null);
        comboBoxTarjetaGrafica.setSelectedItem(null);
        comboBoxDiscoDuro.setSelectedItem(null);
        comboBoxDiscoDuro2.setSelectedItem(null);
        labelPrecioTotal.setText("0€");
        panelPrincipal.requestFocusInWindow();
    }//GEN-LAST:event_labelReiniciarMouseClicked

    private void labelAñadirCarritoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelAñadirCarritoMousePressed
        labelAñadirCarrito.setIcon(new javax.swing.ImageIcon("ImagenesDecoracion" + File.separator + "carritoRelleno.png"));
    }//GEN-LAST:event_labelAñadirCarritoMousePressed

    private void labelAñadirCarritoMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelAñadirCarritoMouseReleased
        labelAñadirCarrito.setIcon(new javax.swing.ImageIcon("ImagenesDecoracion" + File.separator + "carrito.png"));
    }//GEN-LAST:event_labelAñadirCarritoMouseReleased

    private void labelAñadirCarritoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelAñadirCarritoMouseClicked
        if (comboBoxDiscoDuro.getSelectedItem() == null || comboBoxDiscoDuro.getSelectedItem().equals("")) 
        {
            labelErrorDiscoDuro.setText("Falta el componente");
            labelErrorDiscoDuro.setFont(new Font("Arial", Font.PLAIN, 12));
            labelErrorDiscoDuro.setForeground(Color.RED);
        }
        else
        {
            labelErrorDiscoDuro.setText(" ");
        }
        
        if (comboBoxTarjetaGrafica.getSelectedItem() == null || comboBoxTarjetaGrafica.getSelectedItem().equals("")) 
        {
            labelErrorTarjetaGrafica.setText("Falta el componente");
            labelErrorTarjetaGrafica.setFont(new Font("Arial", Font.PLAIN, 12));
            labelErrorTarjetaGrafica.setForeground(Color.RED);
        }
        else
        {
            labelErrorTarjetaGrafica.setText(" ");
        }
        
        if (comboBoxProcesador.getSelectedItem() == null || comboBoxProcesador.getSelectedItem().equals("")) 
        {
            labelErrorProcesador.setText("Falta el componente");
            labelErrorProcesador.setFont(new Font("Arial", Font.PLAIN, 12));
            labelErrorProcesador.setForeground(Color.RED);
        }
        else
        {
            labelErrorProcesador.setText(" ");
        }
                
        if (comboBoxPlacaBase.getSelectedItem() == null || comboBoxPlacaBase.getSelectedItem().equals("")) 
        {
            labelErrorPlacaBase.setText("Falta el componente");
            labelErrorPlacaBase.setFont(new Font("Arial", Font.PLAIN, 12));
            labelErrorPlacaBase.setForeground(Color.RED);
        }
        else
        {
            labelErrorPlacaBase.setText(" ");
        }
                
        if (comboBoxMemoriaRam.getSelectedItem() == null || comboBoxMemoriaRam.getSelectedItem().equals("")) 
        {
            labelErrorMemoriaRam.setText("Falta el componente");
            labelErrorMemoriaRam.setFont(new Font("Arial", Font.PLAIN, 12));
            labelErrorMemoriaRam.setForeground(Color.RED);
        }
        else
        {
            labelErrorMemoriaRam.setText(" ");
        }
                
        if (comboBoxRefrigeracion.getSelectedItem() == null || comboBoxRefrigeracion.getSelectedItem().equals("")) 
        {
            labelErrorRefrigeracion.setText("Falta el componente");
            labelErrorRefrigeracion.setFont(new Font("Arial", Font.PLAIN, 12));
            labelErrorRefrigeracion.setForeground(Color.RED);
        }
        else
        {
            labelErrorRefrigeracion.setText(" ");
        }
                
        if (comboBoxFuenteAlimentacion.getSelectedItem() == null || comboBoxFuenteAlimentacion.getSelectedItem().equals("")) 
        {
            labelErrorFuenteAlimentacion.setText("Falta el componente");
            labelErrorFuenteAlimentacion.setFont(new Font("Arial", Font.PLAIN, 12));
            labelErrorFuenteAlimentacion.setForeground(Color.RED);
        }
        else
        {
            labelErrorFuenteAlimentacion.setText(" ");
        }
        
        try 
        {
            // Establecer la conexión a la base de datos
            Connection cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pcworld", "pcworld", "pcworld2024");

            if (!this.botonCuenta.getText().equals("Cuenta"))
            {
                labelErrorCesta.setText(" ");
                if(labelErrorProcesador.getText().equals(" ") && labelErrorPlacaBase.getText().equals(" ") && labelErrorMemoriaRam.getText().equals(" ") &&
                   labelErrorRefrigeracion.getText().equals(" ") && labelErrorFuenteAlimentacion.getText().equals(" ") && labelErrorTarjetaGrafica.getText().equals(" ") &&
                   labelErrorDiscoDuro.getText().equals(" "))
                {
                    labelErrorCesta.setText(" ");
                    // Crear un array para almacenar los valores de los JComboBox
                    ArrayList<String> componentes = new ArrayList<>();
                    componentes.add(obtenerNombre(comboBoxProcesador.getSelectedItem().toString()));
                    componentes.add(obtenerNombre(comboBoxPlacaBase.getSelectedItem().toString()));
                    componentes.add(obtenerNombre(comboBoxMemoriaRam.getSelectedItem().toString()));
                    componentes.add(obtenerNombre(comboBoxRefrigeracion.getSelectedItem().toString()));
                    componentes.add(obtenerNombre(comboBoxFuenteAlimentacion.getSelectedItem().toString()));
                    componentes.add(obtenerNombre(comboBoxTarjetaGrafica.getSelectedItem().toString()));
                    componentes.add(obtenerNombre(comboBoxDiscoDuro.getSelectedItem().toString()));
                    if(!(comboBoxDiscoDuro2.getSelectedItem() == null || comboBoxDiscoDuro2.getSelectedItem().equals("")))
                        componentes.add(obtenerNombre(comboBoxDiscoDuro2.getSelectedItem().toString()));

                    for(int i = 0; i < componentes.size(); i++)
                    {
                        PreparedStatement pstmtInsertar = cn.prepareStatement("INSERT INTO cesta VALUES (?, ?, ?)");
                        pstmtInsertar.setString(1, botonCuenta.getText());
                        pstmtInsertar.setString(2, componentes.get(i));
                        pstmtInsertar.setString(3, "1");
                        pstmtInsertar.executeUpdate();
                    }
                    
                    comboBoxProcesador.setSelectedItem(null);
                    comboBoxPlacaBase.setSelectedItem(null);
                    comboBoxMemoriaRam.setSelectedItem(null);
                    comboBoxRefrigeracion.setSelectedItem(null);
                    comboBoxFuenteAlimentacion.setSelectedItem(null);
                    comboBoxTarjetaGrafica.setSelectedItem(null);
                    comboBoxDiscoDuro.setSelectedItem(null);
                    comboBoxDiscoDuro2.setSelectedItem(null);
                    labelPrecioTotal.setText("0€");
                }
                else
                {
                    labelErrorCesta.setText("Falta algún componente");
                }
                cn.close();
            }
            else
            {
                labelErrorCesta.setText("No has iniciado sesión");
            }
        }
        catch(Exception e) 
        {
            System.out.println("Error: " + e);
        }
        panelPrincipal.requestFocusInWindow();
    }//GEN-LAST:event_labelAñadirCarritoMouseClicked

    private void labelGuardarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelGuardarMouseClicked
        if (comboBoxDiscoDuro.getSelectedItem() == null || comboBoxDiscoDuro.getSelectedItem().equals("")) 
        {
            labelErrorDiscoDuro.setText("Falta el componente");
            labelErrorDiscoDuro.setFont(new Font("Arial", Font.PLAIN, 12));
            labelErrorDiscoDuro.setForeground(Color.RED);
        }
        else
        {
            labelErrorDiscoDuro.setText(" ");
        }
        
        if (comboBoxTarjetaGrafica.getSelectedItem() == null || comboBoxTarjetaGrafica.getSelectedItem().equals("")) 
        {
            labelErrorTarjetaGrafica.setText("Falta el componente");
            labelErrorTarjetaGrafica.setFont(new Font("Arial", Font.PLAIN, 12));
            labelErrorTarjetaGrafica.setForeground(Color.RED);
        }
        else
        {
            labelErrorTarjetaGrafica.setText(" ");
        }
        
        if (comboBoxProcesador.getSelectedItem() == null || comboBoxProcesador.getSelectedItem().equals("")) 
        {
            labelErrorProcesador.setText("Falta el componente");
            labelErrorProcesador.setFont(new Font("Arial", Font.PLAIN, 12));
            labelErrorProcesador.setForeground(Color.RED);
        }
        else
        {
            labelErrorProcesador.setText(" ");
        }
                
        if (comboBoxPlacaBase.getSelectedItem() == null || comboBoxPlacaBase.getSelectedItem().equals("")) 
        {
            labelErrorPlacaBase.setText("Falta el componente");
            labelErrorPlacaBase.setFont(new Font("Arial", Font.PLAIN, 12));
            labelErrorPlacaBase.setForeground(Color.RED);
        }
        else
        {
            labelErrorPlacaBase.setText(" ");
        }
                
        if (comboBoxMemoriaRam.getSelectedItem() == null || comboBoxMemoriaRam.getSelectedItem().equals("")) 
        {
            labelErrorMemoriaRam.setText("Falta el componente");
            labelErrorMemoriaRam.setFont(new Font("Arial", Font.PLAIN, 12));
            labelErrorMemoriaRam.setForeground(Color.RED);
        }
        else
        {
            labelErrorMemoriaRam.setText(" ");
        }
                
        if (comboBoxRefrigeracion.getSelectedItem() == null || comboBoxRefrigeracion.getSelectedItem().equals("")) 
        {
            labelErrorRefrigeracion.setText("Falta el componente");
            labelErrorRefrigeracion.setFont(new Font("Arial", Font.PLAIN, 12));
            labelErrorRefrigeracion.setForeground(Color.RED);
        }
        else
        {
            labelErrorRefrigeracion.setText(" ");
        }
                
        if (comboBoxFuenteAlimentacion.getSelectedItem() == null || comboBoxFuenteAlimentacion.getSelectedItem().equals("")) 
        {
            labelErrorFuenteAlimentacion.setText("Falta el componente");
            labelErrorFuenteAlimentacion.setFont(new Font("Arial", Font.PLAIN, 12));
            labelErrorFuenteAlimentacion.setForeground(Color.RED);
        }
        else
        {
            labelErrorFuenteAlimentacion.setText(" ");
        }
        
        try 
        {
            // Establecer la conexión a la base de datos
            Connection cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pcworld", "pcworld", "pcworld2024");

            if (!this.botonCuenta.getText().equals("Cuenta"))
            {
                labelErrorListaDeDeseos.setText(" ");
                if(labelErrorProcesador.getText().equals(" ") && labelErrorPlacaBase.getText().equals(" ") && labelErrorMemoriaRam.getText().equals(" ") &&
                   labelErrorRefrigeracion.getText().equals(" ") && labelErrorFuenteAlimentacion.getText().equals(" ") && labelErrorTarjetaGrafica.getText().equals(" ") &&
                   labelErrorDiscoDuro.getText().equals(" "))
                {
                    labelErrorListaDeDeseos.setText(" ");
                    // Crear un array para almacenar los valores de los JComboBox
                    ArrayList<String> componentes = new ArrayList<>();
                    componentes.add(obtenerNombre(comboBoxProcesador.getSelectedItem().toString()));
                    componentes.add(obtenerNombre(comboBoxPlacaBase.getSelectedItem().toString()));
                    componentes.add(obtenerNombre(comboBoxMemoriaRam.getSelectedItem().toString()));
                    componentes.add(obtenerNombre(comboBoxRefrigeracion.getSelectedItem().toString()));
                    componentes.add(obtenerNombre(comboBoxFuenteAlimentacion.getSelectedItem().toString()));
                    componentes.add(obtenerNombre(comboBoxTarjetaGrafica.getSelectedItem().toString()));
                    componentes.add(obtenerNombre(comboBoxDiscoDuro.getSelectedItem().toString()));
                    if(!(comboBoxDiscoDuro2.getSelectedItem() == null || comboBoxDiscoDuro2.getSelectedItem().equals("")))
                        componentes.add(obtenerNombre(comboBoxDiscoDuro2.getSelectedItem().toString()));

                    for(int i = 0; i < componentes.size(); i++)
                    {
                        PreparedStatement pstmtInsertar = cn.prepareStatement("INSERT INTO lista_deseos VALUES (?, ?)");
                        pstmtInsertar.setString(1, botonCuenta.getText());
                        pstmtInsertar.setString(2, componentes.get(i));
                        pstmtInsertar.executeUpdate();
                    }
                    
                    comboBoxProcesador.setSelectedItem(null);
                    comboBoxPlacaBase.setSelectedItem(null);
                    comboBoxMemoriaRam.setSelectedItem(null);
                    comboBoxRefrigeracion.setSelectedItem(null);
                    comboBoxFuenteAlimentacion.setSelectedItem(null);
                    comboBoxTarjetaGrafica.setSelectedItem(null);
                    comboBoxDiscoDuro.setSelectedItem(null);
                    comboBoxDiscoDuro2.setSelectedItem(null);
                    labelPrecioTotal.setText("0€");
                }
                else
                {
                    labelErrorListaDeDeseos.setText("Falta algún componente");
                }
                cn.close();
            }
            else
            {
                labelErrorListaDeDeseos.setText("No has iniciado sesión");
            }
        }
        catch(Exception e) 
        {
            System.out.println("Error: " + e);
        }
        panelPrincipal.requestFocusInWindow();
    }//GEN-LAST:event_labelGuardarMouseClicked

    // Método para obtener solo el nombre antes del guion
    private String obtenerNombre(String item) 
    {
        int index = item.indexOf(" - ");
        if (index != -1) 
        {
            return item.substring(0, index);
        } 
        else 
        {
            return item;
        }
    }
    
    private void calcularPrecioTotal() {
        precioTotal = 0.0;
        DecimalFormat df = new DecimalFormat("#,###,###.##");

        // Suma todos los precios anteriores
        for (double precio : preciosAnteriores.values()) {
            precioTotal += precio;
        }

        // Actualiza el texto del precio total
        labelPrecioTotal.setText(df.format(precioTotal) + "€");
    }
    
    // Método para manejar la selección de un componente
    private void manejarSeleccionComponente(String elementoSeleccionado, String tipoComponente) {
        if (elementoSeleccionado == null || elementoSeleccionado.equals("")) 
        {
            // Si se selecciona un componente vacío, elimina el precio
            preciosAnteriores.remove(tipoComponente);
        } 
        else 
        {
            // Obtén el precio del componente seleccionado
            String[] partes = elementoSeleccionado.split(" - ");
            String textoAntesDelGuion = partes[0];
            try 
            {
                Connection cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pcworld", "pcworld", "pcworld2024");
                String sql = "SELECT * FROM productos WHERE IdNombre = ?";
                PreparedStatement pstmtProductos = cn.prepareStatement(sql);
                pstmtProductos.setString(1, textoAntesDelGuion);
                ResultSet rsProductos = pstmtProductos.executeQuery();

                if (rsProductos.next()) 
                {
                    double nuevoPrecio = rsProductos.getDouble("precioProducto");

                    // Obtén el precio anterior
                    Double precioAnterior = preciosAnteriores.get(tipoComponente);

                    // Si hay un precio anterior, resta este precio del total
                    if (precioAnterior != null) {
                        precioTotal -= precioAnterior;
                    }

                    // Agrega el nuevo precio al total
                    precioTotal += nuevoPrecio;

                    // Actualiza el precio anterior del componente
                    preciosAnteriores.put(tipoComponente, nuevoPrecio);
                }
                cn.close();
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            }
        }

        // Actualiza el precio total
        calcularPrecioTotal();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton botonCesta;
    public javax.swing.JButton botonCuenta;
    private javax.swing.JButton botonListaDeseos;
    private javax.swing.JButton botonLupa;
    public javax.swing.JTextField campoBusqueda;
    private javax.swing.JComboBox<String> comboBoxDiscoDuro;
    private javax.swing.JComboBox<String> comboBoxDiscoDuro2;
    private javax.swing.JComboBox<String> comboBoxFuenteAlimentacion;
    private javax.swing.JComboBox<String> comboBoxMemoriaRam;
    private javax.swing.JComboBox<String> comboBoxPlacaBase;
    private javax.swing.JComboBox<String> comboBoxProcesador;
    private javax.swing.JComboBox<String> comboBoxRefrigeracion;
    private javax.swing.JComboBox<String> comboBoxTarjetaGrafica;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel labelAñadirCarrito;
    private javax.swing.JLabel labelDiscoDuro;
    private javax.swing.JLabel labelDiscoDuro2;
    private javax.swing.JLabel labelErrorCesta;
    private javax.swing.JLabel labelErrorDiscoDuro;
    private javax.swing.JLabel labelErrorDiscoDuro2;
    private javax.swing.JLabel labelErrorFuenteAlimentacion;
    private javax.swing.JLabel labelErrorListaDeDeseos;
    private javax.swing.JLabel labelErrorMemoriaRam;
    private javax.swing.JLabel labelErrorPlacaBase;
    private javax.swing.JLabel labelErrorProcesador;
    private javax.swing.JLabel labelErrorRefrigeracion;
    private javax.swing.JLabel labelErrorTarjetaGrafica;
    private javax.swing.JLabel labelFuenteAlimentacion;
    private javax.swing.JLabel labelGuardar;
    private javax.swing.JLabel labelLogo;
    private javax.swing.JLabel labelMemoriaRam;
    private javax.swing.JLabel labelMiConfig;
    private javax.swing.JLabel labelPlacaBase;
    private javax.swing.JLabel labelPrecio;
    private javax.swing.JLabel labelPrecioTotal;
    private javax.swing.JLabel labelProcesador;
    private javax.swing.JLabel labelRefrigeracion;
    private javax.swing.JLabel labelReiniciar;
    private javax.swing.JLabel labelTarjetaGrafica;
    private javax.swing.JPanel panelBuscador;
    private javax.swing.JPanel panelCabecera;
    private javax.swing.JPanel panelConfig;
    private javax.swing.JPanel panelCuerpo;
    private javax.swing.JPanel panelFuncionalidades;
    private javax.swing.JPanel panelHome;
    private javax.swing.JPanel panelPrecioFinal;
    public javax.swing.JPanel panelPrincipal;
    private javax.swing.JPopupMenu popUpMenu;
    private javax.swing.JPopupMenu popupMenu;
    private javax.swing.JScrollPane scrollPane;
    // End of variables declaration//GEN-END:variables
    private static InterfazConfiguradorPcs instancia;
    public boolean productoUser;
    private DefaultComboBoxModel <String> modeloProcesador;
    private DefaultComboBoxModel <String> modeloPlacaBase;
    private DefaultComboBoxModel <String> modeloMemoriaRam;
    private DefaultComboBoxModel <String> modeloRefrigeracion;
    private DefaultComboBoxModel <String> modeloFuenteAlimentacion;
    private DefaultComboBoxModel <String> modeloTarjetaGrafica;
    private DefaultComboBoxModel <String> modeloDiscoDuro;
    private DefaultComboBoxModel <String> modeloDiscoDuro2;
    private double precioAnterior, precioTotal;
    private Map<String, Double> preciosAnteriores;
}
