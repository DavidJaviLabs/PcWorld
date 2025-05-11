package src;

import ErroresInterfaz.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.*;
import java.util.*;
import java.util.List;
import javax.swing.*;


public class InterfazPrincipal extends javax.swing.JFrame 
{
    /**
     * Creates new form InterfazPrincipal
     */
    public InterfazPrincipal() {
        
        initComponents();
        
        correo = "";
        tipoMostrarProducto = "";
        error = new ErrorCesta();
        colorFondo = Color.WHITE;
        User = " ";
        productoUser = false;
        interfazActual = "";
        
        panelPrincipal.requestFocusInWindow();
        
        campoBusqueda.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Restablecer el texto del campo de búsqueda si está vacío
                if (campoBusqueda.getText().equals("   Buscar...")) {
                    campoBusqueda.setText("");
                }
            }
        });
        
        panelPrincipal.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Restablecer el texto del campo de búsqueda si está vacío
                if (campoBusqueda.getText().equals("")) {
                    campoBusqueda.setText("   Buscar...");
                }
                panelPrincipal.requestFocusInWindow();
            }
        });
        
        UIManager.put("Button.select", new Color(255, 255, 255)); //<--- Added ---
        
        productosAleatorios();
        
        setVisible(true);
    }
    public String getAreaNombreProducto1()
    {
        return areaNombreProducto1.getText();
    }
    public String getAreaNombreProducto2()
    {
        return areaNombreProducto2.getText();
    }
    public String getAreaNombreProducto3()
    {
        return areaNombreProducto3.getText();
    }
    public String getAreaNombreProducto4()
    {
        return areaNombreProducto4.getText();
    }
    
    public static InterfazPrincipal getInstancia()
    {
        if(instancia == null)
        {
            instancia = new InterfazPrincipal();
        }
        return instancia;
    }
    
    public static InterfazPrincipal crearNuevaInstancia() 
    {
        instancia = null;
        return getInstancia();
    }
    
    // Método set para asignar un valor a la variable User
    public void setUser(String user) 
    {
        this.User = user;
    }

    // Método get para obtener el valor actual de la variable User
    public String getUser() 
    {
        return this.User;
    }
    
    public void cerrar()
    {
        dispose();
        InterfazCesta.getInstancia().campoBusqueda.setText("   Buscar...");
        InterfazListaDeseos.getInstancia().campoBusqueda.setText("   Buscar...");
        InterfazProducto.getInstancia().campoBusqueda.setText("   Buscar...");
        InterfazMostrarProductos.getInstancia().campoBusqueda.setText("   Buscar...");
        InterfazConfiguradorPcs.getInstancia().campoBusqueda.setText("   Buscar...");
    }
    
    public void productosAleatorios()
    {
        try {
            // Establecer la conexión a la base de datos
            Connection cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pcworld", "pcworld", "pcworld2024");

            boolean productosDistintos = false;
            
            String rutaImagenPortada = "Imagenes" + File.separator + "Portada" + File.separator;
             
            while (!productosDistintos) {
                // Realizar las consultas para obtener productos aleatorios
                List<String> nombres = new ArrayList<>();
                List<String> Productos = new ArrayList<>();
                for (int i = 0; i < 4; i++) {
                    String queryNombre = "SELECT * FROM productos ORDER BY RAND() LIMIT 1";
                    PreparedStatement pstmtNombre = cn.prepareStatement(queryNombre);
                    ResultSet rsProducto = pstmtNombre.executeQuery();
                    if (rsProducto.next()) {
                        if(Integer.parseInt(rsProducto.getString("oculto")) == 0)
                        {
                            nombres.add(rsProducto.getString("IdNombre"));
                            String im1 = rsProducto.getString("Imagen1Producto").replace("/", File.separator);
                            Productos.add(rutaImagenPortada + im1);
                            Productos.add(rsProducto.getString("IdNombre"));
                            Productos.add(rsProducto.getString("precioProducto"));
                        }
                    }
                }

                // Verificar si todos los nombres son distintos
                Set<String> nombresSet = new HashSet<>(nombres);
                if (nombresSet.size() == 4) {
                    productosDistintos = true;
                    
                    imagen1.setIcon(new javax.swing.ImageIcon(Productos.get(0)));
                    areaNombreProducto1.setText(Productos.get(1));
                    labelPrecioProducto1.setText(Productos.get(2) + " €  ");
                    
                    imagen2.setIcon(new javax.swing.ImageIcon(Productos.get(3)));
                    areaNombreProducto2.setText(Productos.get(4));
                    labelPrecioProducto2.setText(Productos.get(5) + " €  ");
                    
                    imagen3.setIcon(new javax.swing.ImageIcon(Productos.get(6)));
                    areaNombreProducto3.setText(Productos.get(7));
                    labelPrecioProducto3.setText(Productos.get(8) + " €  ");
                    
                    imagen4.setIcon(new javax.swing.ImageIcon(Productos.get(9)));
                    areaNombreProducto4.setText(Productos.get(10));
                    labelPrecioProducto4.setText(Productos.get(11) + " €  ");
                    
                } else {
                    System.out.println("Algunos productos son iguales, intentando nuevamente...");
                }
            }
            cn.close();
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

        popUpMenu = new javax.swing.JPopupMenu();
        jDialog1 = new javax.swing.JDialog();
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
        panelAcciones = new javax.swing.JPanel();
        panelPortatiles = new javax.swing.JPanel();
        labelPortatiles = new javax.swing.JLabel();
        panelComponentes = new javax.swing.JPanel();
        labelComponentes = new javax.swing.JLabel();
        panelPcs = new javax.swing.JPanel();
        labelPcs = new javax.swing.JLabel();
        panelConfigurador = new javax.swing.JPanel();
        labelConfigurador = new javax.swing.JLabel();
        labelPortatil = new javax.swing.JLabel();
        labelComponente = new javax.swing.JLabel();
        labelTorre = new javax.swing.JLabel();
        labelConfig = new javax.swing.JLabel();
        panelProductos = new javax.swing.JPanel();
        panelProducto1 = new javax.swing.JPanel();
        panelFotoProducto1 = new javax.swing.JPanel();
        imagen1 = new javax.swing.JLabel();
        panelNombreProducto1 = new javax.swing.JPanel();
        labelPrecioProducto1 = new javax.swing.JLabel();
        areaNombreProducto1 = new javax.swing.JTextArea();
        panelProducto2 = new javax.swing.JPanel();
        panelFotoProducto2 = new javax.swing.JPanel();
        imagen2 = new javax.swing.JLabel();
        panelNombreProducto2 = new javax.swing.JPanel();
        areaNombreProducto2 = new javax.swing.JTextArea();
        labelPrecioProducto2 = new javax.swing.JLabel();
        panelProducto3 = new javax.swing.JPanel();
        panelFotoProducto3 = new javax.swing.JPanel();
        imagen3 = new javax.swing.JLabel();
        panelNombreProducto3 = new javax.swing.JPanel();
        areaNombreProducto3 = new javax.swing.JTextArea();
        labelPrecioProducto3 = new javax.swing.JLabel();
        panelProducto4 = new javax.swing.JPanel();
        panelFotoProducto4 = new javax.swing.JPanel();
        imagen4 = new javax.swing.JLabel();
        panelNombreProducto4 = new javax.swing.JPanel();
        areaNombreProducto4 = new javax.swing.JTextArea();
        labelPrecioProducto4 = new javax.swing.JLabel();

        popUpMenu.setBackground(new java.awt.Color(0, 0, 0));
        popUpMenu.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        popUpMenu.setForeground(new java.awt.Color(255, 255, 255));
        popUpMenu.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        popUpMenu.setLabel("");
        popUpMenu.setName(""); // NOI18N
        popUpMenu.setPopupSize(new java.awt.Dimension(50, 50));

        javax.swing.GroupLayout jDialog1Layout = new javax.swing.GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(jDialog1Layout);
        jDialog1Layout.setHorizontalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jDialog1Layout.setVerticalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        panelPrincipal.setBackground(new java.awt.Color(255, 255, 255));
        panelPrincipal.setForeground(new java.awt.Color(255, 255, 255));

        panelHome.setBackground(new java.awt.Color(102, 255, 102));

        labelLogo.setIcon(new javax.swing.ImageIcon("ImagenesDecoracion" + File.separator + "logoPrinc.png"));

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 74, Short.MAX_VALUE)
                .addComponent(botonCesta, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(75, Short.MAX_VALUE))
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

        panelAcciones.setBackground(new java.awt.Color(70, 123, 69));
        panelAcciones.setPreferredSize(new java.awt.Dimension(1265, 300));

        panelPortatiles.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        labelPortatiles.setIcon(new javax.swing.ImageIcon("ImagenesDecoracion" + File.separator + "imPortatil.png"));
        labelPortatiles.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                labelPortatilesMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout panelPortatilesLayout = new javax.swing.GroupLayout(panelPortatiles);
        panelPortatiles.setLayout(panelPortatilesLayout);
        panelPortatilesLayout.setHorizontalGroup(
            panelPortatilesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(labelPortatiles, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
        );
        panelPortatilesLayout.setVerticalGroup(
            panelPortatilesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(labelPortatiles, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
        );

        panelComponentes.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panelComponentes.setPreferredSize(new java.awt.Dimension(230, 230));

        labelComponentes.setIcon(new javax.swing.ImageIcon("ImagenesDecoracion" + File.separator + "imComponentes.png"));
        labelComponentes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                labelComponentesMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout panelComponentesLayout = new javax.swing.GroupLayout(panelComponentes);
        panelComponentes.setLayout(panelComponentesLayout);
        panelComponentesLayout.setHorizontalGroup(
            panelComponentesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(labelComponentes, javax.swing.GroupLayout.DEFAULT_SIZE, 228, Short.MAX_VALUE)
        );
        panelComponentesLayout.setVerticalGroup(
            panelComponentesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(labelComponentes, javax.swing.GroupLayout.DEFAULT_SIZE, 228, Short.MAX_VALUE)
        );

        panelPcs.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panelPcs.setPreferredSize(new java.awt.Dimension(230, 230));

        labelPcs.setIcon(new javax.swing.ImageIcon("ImagenesDecoracion" + File.separator + "imPc.png"));
        labelPcs.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                labelPcsMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout panelPcsLayout = new javax.swing.GroupLayout(panelPcs);
        panelPcs.setLayout(panelPcsLayout);
        panelPcsLayout.setHorizontalGroup(
            panelPcsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(labelPcs, javax.swing.GroupLayout.DEFAULT_SIZE, 228, Short.MAX_VALUE)
        );
        panelPcsLayout.setVerticalGroup(
            panelPcsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(labelPcs, javax.swing.GroupLayout.DEFAULT_SIZE, 228, Short.MAX_VALUE)
        );

        panelConfigurador.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panelConfigurador.setPreferredSize(new java.awt.Dimension(230, 230));

        labelConfigurador.setIcon(new javax.swing.ImageIcon("ImagenesDecoracion" + File.separator + "imConfig.png"));
        labelConfigurador.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                labelConfiguradorMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout panelConfiguradorLayout = new javax.swing.GroupLayout(panelConfigurador);
        panelConfigurador.setLayout(panelConfiguradorLayout);
        panelConfiguradorLayout.setHorizontalGroup(
            panelConfiguradorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(labelConfigurador, javax.swing.GroupLayout.DEFAULT_SIZE, 228, Short.MAX_VALUE)
        );
        panelConfiguradorLayout.setVerticalGroup(
            panelConfiguradorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(labelConfigurador, javax.swing.GroupLayout.DEFAULT_SIZE, 228, Short.MAX_VALUE)
        );

        labelPortatil.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        labelPortatil.setForeground(new java.awt.Color(0, 0, 0));
        labelPortatil.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelPortatil.setText("PORTÁTILES");

        labelComponente.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        labelComponente.setForeground(new java.awt.Color(0, 0, 0));
        labelComponente.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelComponente.setText("COMPONENTES");

        labelTorre.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        labelTorre.setForeground(new java.awt.Color(0, 0, 0));
        labelTorre.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelTorre.setText("TORRES");

        labelConfig.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        labelConfig.setForeground(new java.awt.Color(0, 0, 0));
        labelConfig.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelConfig.setText("CONFIGURA TU PC");

        javax.swing.GroupLayout panelAccionesLayout = new javax.swing.GroupLayout(panelAcciones);
        panelAcciones.setLayout(panelAccionesLayout);
        panelAccionesLayout.setHorizontalGroup(
            panelAccionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAccionesLayout.createSequentialGroup()
                .addGap(96, 96, 96)
                .addGroup(panelAccionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(labelPortatil, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelPortatiles, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(96, 96, 96)
                .addGroup(panelAccionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(panelComponentes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelComponente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(96, 96, 96)
                .addGroup(panelAccionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(panelPcs, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelTorre, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(96, 96, 96)
                .addGroup(panelAccionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(panelConfigurador, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelConfig, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(96, 96, 96))
        );
        panelAccionesLayout.setVerticalGroup(
            panelAccionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelAccionesLayout.createSequentialGroup()
                .addContainerGap(33, Short.MAX_VALUE)
                .addGroup(panelAccionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelConfigurador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panelPortatiles, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panelComponentes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panelPcs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelAccionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelAccionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(labelPortatil, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
                        .addComponent(labelComponente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(labelTorre, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(labelConfig))
                .addContainerGap())
        );

        panelProductos.setBackground(new java.awt.Color(255, 255, 255));

        panelProducto1.setBackground(new java.awt.Color(70, 123, 69));
        panelProducto1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        panelProducto1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                panelProducto1MousePressed(evt);
            }
        });

        panelFotoProducto1.setBackground(new java.awt.Color(255, 255, 255));

        imagen1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout panelFotoProducto1Layout = new javax.swing.GroupLayout(panelFotoProducto1);
        panelFotoProducto1.setLayout(panelFotoProducto1Layout);
        panelFotoProducto1Layout.setHorizontalGroup(
            panelFotoProducto1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(imagen1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panelFotoProducto1Layout.setVerticalGroup(
            panelFotoProducto1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(imagen1, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
        );

        panelNombreProducto1.setBackground(new java.awt.Color(255, 255, 255));

        labelPrecioProducto1.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        labelPrecioProducto1.setForeground(new java.awt.Color(255, 0, 0));
        labelPrecioProducto1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labelPrecioProducto1.setText("                              ");

        areaNombreProducto1.setEditable(false);
        areaNombreProducto1.setColumns(20);
        areaNombreProducto1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        areaNombreProducto1.setForeground(new java.awt.Color(0, 0, 0));
        areaNombreProducto1.setLineWrap(true);
        areaNombreProducto1.setRows(20);
        areaNombreProducto1.setWrapStyleWord(true);
        areaNombreProducto1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        areaNombreProducto1.setFocusable(false);

        javax.swing.GroupLayout panelNombreProducto1Layout = new javax.swing.GroupLayout(panelNombreProducto1);
        panelNombreProducto1.setLayout(panelNombreProducto1Layout);
        panelNombreProducto1Layout.setHorizontalGroup(
            panelNombreProducto1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelNombreProducto1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(labelPrecioProducto1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(panelNombreProducto1Layout.createSequentialGroup()
                .addComponent(areaNombreProducto1, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        panelNombreProducto1Layout.setVerticalGroup(
            panelNombreProducto1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelNombreProducto1Layout.createSequentialGroup()
                .addComponent(areaNombreProducto1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelPrecioProducto1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout panelProducto1Layout = new javax.swing.GroupLayout(panelProducto1);
        panelProducto1.setLayout(panelProducto1Layout);
        panelProducto1Layout.setHorizontalGroup(
            panelProducto1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelProducto1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelProducto1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelNombreProducto1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelFotoProducto1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelProducto1Layout.setVerticalGroup(
            panelProducto1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelProducto1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelFotoProducto1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelNombreProducto1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        panelProducto2.setBackground(new java.awt.Color(70, 123, 69));
        panelProducto2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                panelProducto2MousePressed(evt);
            }
        });

        panelFotoProducto2.setBackground(new java.awt.Color(255, 255, 255));

        imagen2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout panelFotoProducto2Layout = new javax.swing.GroupLayout(panelFotoProducto2);
        panelFotoProducto2.setLayout(panelFotoProducto2Layout);
        panelFotoProducto2Layout.setHorizontalGroup(
            panelFotoProducto2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(imagen2, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        panelFotoProducto2Layout.setVerticalGroup(
            panelFotoProducto2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(imagen2, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        panelNombreProducto2.setBackground(new java.awt.Color(255, 255, 255));

        areaNombreProducto2.setEditable(false);
        areaNombreProducto2.setColumns(20);
        areaNombreProducto2.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        areaNombreProducto2.setForeground(new java.awt.Color(0, 0, 0));
        areaNombreProducto2.setLineWrap(true);
        areaNombreProducto2.setRows(20);
        areaNombreProducto2.setWrapStyleWord(true);
        areaNombreProducto2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        areaNombreProducto2.setFocusable(false);

        labelPrecioProducto2.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        labelPrecioProducto2.setForeground(new java.awt.Color(255, 0, 0));
        labelPrecioProducto2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labelPrecioProducto2.setText("                              ");

        javax.swing.GroupLayout panelNombreProducto2Layout = new javax.swing.GroupLayout(panelNombreProducto2);
        panelNombreProducto2.setLayout(panelNombreProducto2Layout);
        panelNombreProducto2Layout.setHorizontalGroup(
            panelNombreProducto2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelNombreProducto2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(labelPrecioProducto2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(areaNombreProducto2)
        );
        panelNombreProducto2Layout.setVerticalGroup(
            panelNombreProducto2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelNombreProducto2Layout.createSequentialGroup()
                .addComponent(areaNombreProducto2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(labelPrecioProducto2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout panelProducto2Layout = new javax.swing.GroupLayout(panelProducto2);
        panelProducto2.setLayout(panelProducto2Layout);
        panelProducto2Layout.setHorizontalGroup(
            panelProducto2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelProducto2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelProducto2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(panelFotoProducto2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelNombreProducto2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelProducto2Layout.setVerticalGroup(
            panelProducto2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelProducto2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelFotoProducto2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelNombreProducto2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelProducto3.setBackground(new java.awt.Color(70, 123, 69));
        panelProducto3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                panelProducto3MousePressed(evt);
            }
        });

        panelFotoProducto3.setBackground(new java.awt.Color(255, 255, 255));

        imagen3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout panelFotoProducto3Layout = new javax.swing.GroupLayout(panelFotoProducto3);
        panelFotoProducto3.setLayout(panelFotoProducto3Layout);
        panelFotoProducto3Layout.setHorizontalGroup(
            panelFotoProducto3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(imagen3, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        panelFotoProducto3Layout.setVerticalGroup(
            panelFotoProducto3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(imagen3, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        panelNombreProducto3.setBackground(new java.awt.Color(255, 255, 255));

        areaNombreProducto3.setEditable(false);
        areaNombreProducto3.setColumns(20);
        areaNombreProducto3.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        areaNombreProducto3.setForeground(new java.awt.Color(0, 0, 0));
        areaNombreProducto3.setLineWrap(true);
        areaNombreProducto3.setRows(20);
        areaNombreProducto3.setWrapStyleWord(true);
        areaNombreProducto3.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        areaNombreProducto3.setFocusable(false);

        labelPrecioProducto3.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        labelPrecioProducto3.setForeground(new java.awt.Color(255, 0, 0));
        labelPrecioProducto3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labelPrecioProducto3.setText("                                ");

        javax.swing.GroupLayout panelNombreProducto3Layout = new javax.swing.GroupLayout(panelNombreProducto3);
        panelNombreProducto3.setLayout(panelNombreProducto3Layout);
        panelNombreProducto3Layout.setHorizontalGroup(
            panelNombreProducto3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(panelNombreProducto3Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(labelPrecioProducto3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(areaNombreProducto3, javax.swing.GroupLayout.Alignment.LEADING)
        );
        panelNombreProducto3Layout.setVerticalGroup(
            panelNombreProducto3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelNombreProducto3Layout.createSequentialGroup()
                .addComponent(areaNombreProducto3, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelPrecioProducto3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout panelProducto3Layout = new javax.swing.GroupLayout(panelProducto3);
        panelProducto3.setLayout(panelProducto3Layout);
        panelProducto3Layout.setHorizontalGroup(
            panelProducto3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelProducto3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelProducto3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(panelFotoProducto3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelNombreProducto3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelProducto3Layout.setVerticalGroup(
            panelProducto3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelProducto3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelFotoProducto3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelNombreProducto3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelProducto4.setBackground(new java.awt.Color(70, 123, 69));
        panelProducto4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                panelProducto4MousePressed(evt);
            }
        });

        panelFotoProducto4.setBackground(new java.awt.Color(255, 255, 255));

        imagen4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout panelFotoProducto4Layout = new javax.swing.GroupLayout(panelFotoProducto4);
        panelFotoProducto4.setLayout(panelFotoProducto4Layout);
        panelFotoProducto4Layout.setHorizontalGroup(
            panelFotoProducto4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(imagen4, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        panelFotoProducto4Layout.setVerticalGroup(
            panelFotoProducto4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(imagen4, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        panelNombreProducto4.setBackground(new java.awt.Color(255, 255, 255));

        areaNombreProducto4.setEditable(false);
        areaNombreProducto4.setColumns(20);
        areaNombreProducto4.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        areaNombreProducto4.setForeground(new java.awt.Color(0, 0, 0));
        areaNombreProducto4.setLineWrap(true);
        areaNombreProducto4.setRows(20);
        areaNombreProducto4.setWrapStyleWord(true);
        areaNombreProducto4.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        areaNombreProducto4.setFocusable(false);

        labelPrecioProducto4.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        labelPrecioProducto4.setForeground(new java.awt.Color(255, 0, 0));
        labelPrecioProducto4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labelPrecioProducto4.setText("                               ");

        javax.swing.GroupLayout panelNombreProducto4Layout = new javax.swing.GroupLayout(panelNombreProducto4);
        panelNombreProducto4.setLayout(panelNombreProducto4Layout);
        panelNombreProducto4Layout.setHorizontalGroup(
            panelNombreProducto4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(panelNombreProducto4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(labelPrecioProducto4, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(areaNombreProducto4, javax.swing.GroupLayout.Alignment.LEADING)
        );
        panelNombreProducto4Layout.setVerticalGroup(
            panelNombreProducto4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelNombreProducto4Layout.createSequentialGroup()
                .addComponent(areaNombreProducto4, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(labelPrecioProducto4, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout panelProducto4Layout = new javax.swing.GroupLayout(panelProducto4);
        panelProducto4.setLayout(panelProducto4Layout);
        panelProducto4Layout.setHorizontalGroup(
            panelProducto4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelProducto4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelProducto4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelProducto4Layout.createSequentialGroup()
                        .addComponent(panelFotoProducto4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(panelNombreProducto4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelProducto4Layout.setVerticalGroup(
            panelProducto4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelProducto4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelFotoProducto4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelNombreProducto4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelProductosLayout = new javax.swing.GroupLayout(panelProductos);
        panelProductos.setLayout(panelProductosLayout);
        panelProductosLayout.setHorizontalGroup(
            panelProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelProductosLayout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addComponent(panelProducto1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38)
                .addComponent(panelProducto2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38)
                .addComponent(panelProducto3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38)
                .addComponent(panelProducto4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(49, 49, 49))
        );
        panelProductosLayout.setVerticalGroup(
            panelProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelProductosLayout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addGroup(panelProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(panelProducto3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(panelProducto4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(panelProducto1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(panelProducto2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(46, 46, 46))
        );

        javax.swing.GroupLayout panelPrincipalLayout = new javax.swing.GroupLayout(panelPrincipal);
        panelPrincipal.setLayout(panelPrincipalLayout);
        panelPrincipalLayout.setHorizontalGroup(
            panelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelCabecera, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panelAcciones, javax.swing.GroupLayout.DEFAULT_SIZE, 1402, Short.MAX_VALUE)
            .addComponent(panelProductos, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        panelPrincipalLayout.setVerticalGroup(
            panelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPrincipalLayout.createSequentialGroup()
                .addComponent(panelCabecera, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(panelProductos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(panelAcciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
            if(!error.isVisible()) error.setVisible(true);
        }
    }//GEN-LAST:event_botonCestaActionPerformed

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

                        // Verificar si el texto de búsqueda no está vacío

                        // Verificar si el correo electrónico ya existe en la tabla
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
                    Color color = JColorChooser.showDialog(InterfazPrincipal.this, "Selecciona un color", panelProductos.getBackground());
                    if (color != null) 
                    {
                        //INTERFAZ PRINCIPAL
                        panelPrincipal.setBackground(color);
                        panelProductos.setBackground(color);
                        panelAcciones.setBackground(color);
                        JOptionPane.showMessageDialog(InterfazPrincipal.this, "Color de fondo actualizado");
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
                    InterfazMostrarProductos.getInstancia().botonCuenta.setText("Cuenta");
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

    private void campoBusquedaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_campoBusquedaFocusLost
        if(campoBusqueda.getText().equals(""))
        {
            campoBusqueda.setText("   Buscar...");
        }
    }//GEN-LAST:event_campoBusquedaFocusLost

    private void campoBusquedaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_campoBusquedaFocusGained
        if(campoBusqueda.getText().equals("   Buscar..."))
        {
            campoBusqueda.setText("");
        }
    }//GEN-LAST:event_campoBusquedaFocusGained

    private void campoBusquedaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoBusquedaKeyReleased

        try {
            // Establecer la conexión a la base de datos
            Connection cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pcworld", "pcworld", "pcworld2024");

            // Verificar si el texto de búsqueda no está vacío
            if (!campoBusqueda.getText().trim().isEmpty()) {
                // Consulta SQL para obtener todos los productos que contienen la cadena de búsqueda
                String queryProducto = "SELECT * FROM productos WHERE `IdNombre` LIKE ? AND `oculto` = ?";
                PreparedStatement pstmtProducto = cn.prepareStatement(queryProducto);
                pstmtProducto.setString(1, "%" + campoBusqueda.getText().trim() + "%"); // Utiliza % para buscar coincidencias parciales
                pstmtProducto.setString(2, "0");
                ResultSet rsProducto = pstmtProducto.executeQuery();

                // Crear un PopupMenu
                JPopupMenu popupMenu = new JPopupMenu();

                // Establecer el máximo de filas para el PopupMenu
                int maxRows = 5;
                int rowCount = 0;

                // Iterar sobre los resultados de la consulta y agregar elementos al PopupMenu
                while (rsProducto.next() && rowCount < maxRows) {
                    String nombreProducto = rsProducto.getString("IdNombre"); // Reemplaza "nombre_producto" con el nombre real del campo en tu base de datos
                    JMenuItem menuItem = new JMenuItem(nombreProducto);
                    menuItem.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            productoUser = true;
                            //InterfazProducto.crearNuevaInstancia();
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

    private void panelProducto1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelProducto1MousePressed
        InterfazProducto.getInstancia().cargarProducto(areaNombreProducto1.getText());
        InterfazProducto.getInstancia().setVisible(true);
        cerrar();
        InterfazProducto.getInstancia().panelPrincipal.requestFocusInWindow();
    }//GEN-LAST:event_panelProducto1MousePressed

    private void panelProducto2MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelProducto2MousePressed
        InterfazProducto.getInstancia().cargarProducto(areaNombreProducto2.getText());
        InterfazProducto.getInstancia().setVisible(true);
        cerrar();
        InterfazProducto.getInstancia().panelPrincipal.requestFocusInWindow();
    }//GEN-LAST:event_panelProducto2MousePressed

    private void panelProducto3MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelProducto3MousePressed
        InterfazProducto.getInstancia().cargarProducto(areaNombreProducto3.getText());
        InterfazProducto.getInstancia().setVisible(true);
        cerrar();
        InterfazProducto.getInstancia().panelPrincipal.requestFocusInWindow();
    }//GEN-LAST:event_panelProducto3MousePressed

    private void panelProducto4MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelProducto4MousePressed
        InterfazProducto.getInstancia().cargarProducto(areaNombreProducto4.getText());
        InterfazProducto.getInstancia().setVisible(true);
        cerrar();
        InterfazProducto.getInstancia().panelPrincipal.requestFocusInWindow();
    }//GEN-LAST:event_panelProducto4MousePressed

    private void botonListaDeseosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonListaDeseosActionPerformed
        if(!this.botonCuenta.getText().equals("Cuenta"))
        {
            try {
                // Establecer la conexión a la base de datos
                Connection cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pcworld", "pcworld", "pcworld2024");

                // Verificar si el texto de búsqueda no está vacío
            
                // Verificar si el correo electrónico ya existe en la tabla
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
            if(!error.isVisible()) error.setVisible(true);
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

    private void labelPortatilesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelPortatilesMouseClicked
        tipoMostrarProducto = "Portatil";
        InterfazMostrarProductos.getInstancia().reiniciarBotonesAscDesc();
        InterfazMostrarProductos.getInstancia().panelProductos = null;
        InterfazMostrarProductos.getInstancia().panelProductos = new JPanel(new GridLayout(0,3, 20,38));
        InterfazMostrarProductos.getInstancia().panelProductos.setBackground(Color.WHITE);
        InterfazMostrarProductos.getInstancia().panelFiltrosPers.removeAll();
        InterfazMostrarProductos.getInstancia().panelFiltrosPers.add(InterfazPanelFiltrosPortatiles.crearNuevaInstancia().panelPrincipal);
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
    }//GEN-LAST:event_labelPortatilesMouseClicked

    private void labelComponentesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelComponentesMouseClicked
        tipoMostrarProducto = "Componentes";
        InterfazMostrarProductos.getInstancia().reiniciarBotonesAscDesc();
        InterfazMostrarProductos.getInstancia().panelProductos = null;
        InterfazMostrarProductos.getInstancia().panelProductos = new JPanel(new GridLayout(0,3, 20,38));
        InterfazMostrarProductos.getInstancia().panelProductos.setBackground(Color.WHITE);
        InterfazMostrarProductos.getInstancia().panelFiltrosPers.removeAll();
        InterfazMostrarProductos.getInstancia().panelFiltrosPers.add(InterfazPanelFiltrosComponentes.crearNuevaInstancia().panelPrincipal);
        InterfazMostrarProductos.getInstancia().scrollPane.setViewportView(InterfazMostrarProductos.getInstancia().panelProductos);
        InterfazMostrarProductos.getInstancia().campoBusqueda.setText("   Buscar...");
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
    }//GEN-LAST:event_labelComponentesMouseClicked

    private void labelPcsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelPcsMouseClicked
        tipoMostrarProducto = "Torre";
        InterfazMostrarProductos.getInstancia().reiniciarBotonesAscDesc();
        InterfazMostrarProductos.getInstancia().panelProductos = null;
        InterfazMostrarProductos.getInstancia().panelProductos = new JPanel(new GridLayout(0,3, 20,38));
        InterfazMostrarProductos.getInstancia().panelProductos.setBackground(Color.WHITE);
        InterfazMostrarProductos.getInstancia().panelFiltrosPers.removeAll();
        InterfazMostrarProductos.getInstancia().panelFiltrosPers.add(InterfazPanelFiltrosTorres.crearNuevaInstancia().panelPrincipal);
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
    }//GEN-LAST:event_labelPcsMouseClicked

    private void labelConfiguradorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelConfiguradorMouseClicked
        InterfazConfiguradorPcs.crearNuevaInstancia().setVisible(true);
        InterfazConfiguradorPcs.getInstancia().botonCuenta.setText(botonCuenta.getText());
        InterfazConfiguradorPcs.getInstancia().panelPrincipal.requestFocusInWindow();
        cerrar();
    }//GEN-LAST:event_labelConfiguradorMouseClicked

    private void busquedaProducto()
    {
        InterfazMostrarProductos.getInstancia().reiniciarBotonesAscDesc();
        InterfazMostrarProductos.getInstancia().panelProductos = null;
        InterfazMostrarProductos.getInstancia().panelProductos = new JPanel(new GridLayout(0,3, 20,38));
        InterfazMostrarProductos.getInstancia().panelProductos.setBackground(Color.WHITE);
        InterfazMostrarProductos.getInstancia().panelFiltrosPers.removeAll();
        if(campoBusqueda.getText().toLowerCase().equals("portatil") || campoBusqueda.getText().toLowerCase().equals("portátil") || campoBusqueda.getText().toLowerCase().equals("portatiles") || campoBusqueda.getText().toLowerCase().equals("portátiles"))
        {
            tipoMostrarProducto = "Portatil";
            InterfazMostrarProductos.getInstancia().panelFiltrosPers.add(InterfazPanelFiltrosPortatiles.crearNuevaInstancia().panelPrincipal);
        }
        else if (campoBusqueda.getText().toLowerCase().equals("torre") || campoBusqueda.getText().toLowerCase().equals("torres") || campoBusqueda.getText().toLowerCase().equals("pc") || campoBusqueda.getText().toLowerCase().equals("pcs"))
        {
            tipoMostrarProducto = "Torre";
            
            InterfazMostrarProductos.getInstancia().panelFiltrosPers.add(InterfazPanelFiltrosTorres.crearNuevaInstancia().panelPrincipal);
        }
        else if (campoBusqueda.getText().toLowerCase().equals("componentes") || campoBusqueda.getText().toLowerCase().equals("componente"))
        {
            tipoMostrarProducto = "Componentes";
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
    
    private void campoBusquedaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoBusquedaKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER)
        {
            InterfazMostrarProductos.getInstancia().reiniciarBotonesAscDesc();
            interfazActual = "InterfazPrincipal";
            InterfazCesta.getInstancia().campoBusqueda.setText("   Buscar...");
            InterfazListaDeseos.getInstancia().campoBusqueda.setText("   Buscar...");
            InterfazProducto.getInstancia().campoBusqueda.setText("   Buscar...");
            InterfazMostrarProductos.getInstancia().campoBusqueda.setText("   Buscar...");
            InterfazConfiguradorPcs.getInstancia().campoBusqueda.setText("   Buscar...");
            busquedaProducto();
        }
    }//GEN-LAST:event_campoBusquedaKeyPressed

    private void botonLupaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botonLupaMouseClicked
        InterfazMostrarProductos.getInstancia().reiniciarBotonesAscDesc();
        interfazActual = "InterfazPrincipal";
        InterfazCesta.getInstancia().campoBusqueda.setText("   Buscar...");
        InterfazListaDeseos.getInstancia().campoBusqueda.setText("   Buscar...");
        InterfazProducto.getInstancia().campoBusqueda.setText("   Buscar...");
        InterfazMostrarProductos.getInstancia().campoBusqueda.setText("   Buscar...");
        InterfazConfiguradorPcs.getInstancia().campoBusqueda.setText("   Buscar...");
        busquedaProducto();
    }//GEN-LAST:event_botonLupaMouseClicked
  
    public void cambiarTextoBotonCuenta (String user)
    {
        botonCuenta.setText(user);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea areaNombreProducto1;
    private javax.swing.JTextArea areaNombreProducto2;
    private javax.swing.JTextArea areaNombreProducto3;
    private javax.swing.JTextArea areaNombreProducto4;
    private javax.swing.JButton botonCesta;
    public javax.swing.JButton botonCuenta;
    private javax.swing.JButton botonListaDeseos;
    private javax.swing.JButton botonLupa;
    public javax.swing.JTextField campoBusqueda;
    private javax.swing.JLabel imagen1;
    private javax.swing.JLabel imagen2;
    private javax.swing.JLabel imagen3;
    private javax.swing.JLabel imagen4;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JLabel labelComponente;
    private javax.swing.JLabel labelComponentes;
    private javax.swing.JLabel labelConfig;
    private javax.swing.JLabel labelConfigurador;
    private javax.swing.JLabel labelLogo;
    private javax.swing.JLabel labelPcs;
    private javax.swing.JLabel labelPortatil;
    private javax.swing.JLabel labelPortatiles;
    private javax.swing.JLabel labelPrecioProducto1;
    private javax.swing.JLabel labelPrecioProducto2;
    private javax.swing.JLabel labelPrecioProducto3;
    private javax.swing.JLabel labelPrecioProducto4;
    private javax.swing.JLabel labelTorre;
    private javax.swing.JPanel panelAcciones;
    private javax.swing.JPanel panelBuscador;
    private javax.swing.JPanel panelCabecera;
    private javax.swing.JPanel panelComponentes;
    private javax.swing.JPanel panelConfigurador;
    private javax.swing.JPanel panelFotoProducto1;
    private javax.swing.JPanel panelFotoProducto2;
    private javax.swing.JPanel panelFotoProducto3;
    private javax.swing.JPanel panelFotoProducto4;
    private javax.swing.JPanel panelFuncionalidades;
    private javax.swing.JPanel panelHome;
    private javax.swing.JPanel panelNombreProducto1;
    private javax.swing.JPanel panelNombreProducto2;
    private javax.swing.JPanel panelNombreProducto3;
    private javax.swing.JPanel panelNombreProducto4;
    private javax.swing.JPanel panelPcs;
    private javax.swing.JPanel panelPortatiles;
    public javax.swing.JPanel panelPrincipal;
    private javax.swing.JPanel panelProducto1;
    private javax.swing.JPanel panelProducto2;
    private javax.swing.JPanel panelProducto3;
    private javax.swing.JPanel panelProducto4;
    private javax.swing.JPanel panelProductos;
    public javax.swing.JPopupMenu popUpMenu;
    // End of variables declaration//GEN-END:variables
    public String correo;
    public String tipoMostrarProducto;
    private ErrorCesta error;
    private static Color colorFondo;
    private String User;
    private static InterfazPrincipal instancia;
    public boolean productoUser;
    public String interfazActual;
}