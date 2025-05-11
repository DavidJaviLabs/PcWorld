package src;

import ErroresInterfaz.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.*;
import javax.swing.*;

public class InterfazProducto extends javax.swing.JFrame 
{
    /**
     * Creates new form InterfazProducto
     */
    public InterfazProducto() {
        initComponents();
        error = new ErrorCesta();
        if (!InterfazPrincipal.getInstancia().botonCuenta.getText().equals("Cuenta"))
            botonCuenta.setText(InterfazPrincipal.getInstancia().getUser());
    }
    
    public static InterfazProducto getInstancia()
    {
        if(instancia == null)
        {
            instancia = new InterfazProducto();
        }
        return instancia;
    }
    
    public static InterfazProducto crearNuevaInstancia() 
    {
        instancia = null;
        return getInstancia();
    }
    
    private void cerrar()
    {
        dispose();
        InterfazCesta.getInstancia().campoBusqueda.setText("   Buscar...");
        InterfazListaDeseos.getInstancia().campoBusqueda.setText("   Buscar...");
        InterfazPrincipal.getInstancia().campoBusqueda.setText("   Buscar...");
        InterfazMostrarProductos.getInstancia().campoBusqueda.setText("   Buscar...");
        InterfazConfiguradorPcs.getInstancia().campoBusqueda.setText("   Buscar...");
    }
    
    public String getNombreProducto()
    {
        return nombreProducto.getText();
    }
    
    public void cargarProducto(String producto)
    {
        cargarNombrePrecioStock(producto);
        cargarLabelStock(nombreProducto.getText());
        cargarImagen(nombreProducto.getText());
        cargarBotonesCarritoListaDeseos(nombreProducto.getText());
        cargarModificarStock();
    }
    
    private void cargarNombrePrecioStock(String nombre)
    {
        try {
            // Establecer la conexión a la base de datos
            Connection cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pcworld", "pcworld", "pcworld2024");

            // Verificar si el correo electrónico ya existe en la tabla
            String queryNombre = "SELECT * FROM productos WHERE `IdNombre` = ? AND `oculto` = ?";
            PreparedStatement pstmtNombre = cn.prepareStatement(queryNombre);
            pstmtNombre.setString(1, nombre);
            pstmtNombre.setString(2, "0");
            ResultSet rsProducto = pstmtNombre.executeQuery();

            if (rsProducto.next()) {
                nombreProducto.setText(nombre);
                descripcionProducto.setText(rsProducto.getString("descripcionProducto"));
                labelPrecioProducto.setText(rsProducto.getString("precioProducto") + " €");
                labelContadorStock.setText(rsProducto.getString("stockProducto"));
                cargarLabelStock(nombreProducto.getText());
            }
            cn.close();
        } catch(Exception e) {
            System.out.println("Error: " + e);
        }
    }
    private void cargarImagen(String nombre)
    {
        try {
            
            // Establecer la conexión a la base de datos
            Connection cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pcworld", "pcworld", "pcworld2024");

            // Verificar si el correo electrónico ya existe en la tabla
            String queryNombre = "SELECT * FROM productos WHERE `IdNombre` = ?";
            PreparedStatement pstmtNombre = cn.prepareStatement(queryNombre);
            pstmtNombre.setString(1, nombre);
            ResultSet rsProducto = pstmtNombre.executeQuery();

            if (rsProducto.next()) 
            {
                String rutaImagenGrande = "Imagenes" + File.separator + "Grandes" + File.separator;
                String rutaImagenPequeña = "Imagenes" + File.separator + "Pequenas" + File.separator;
                im1 = rsProducto.getString("Imagen1Producto").replace("/", File.separator);
                im2 = rsProducto.getString("Imagen2Producto").replace("/", File.separator);
                fotoGrande.setIcon(new javax.swing.ImageIcon(rutaImagenGrande + im1));
                fotoPequeña1.setIcon(new javax.swing.ImageIcon(rutaImagenPequeña + im1));
                fotoPequeña2.setIcon(new javax.swing.ImageIcon(rutaImagenPequeña + im2));

            } 
            cn.close();
        } catch(Exception e) {
            System.out.println("Error: " + e);
        }
    }
   
    public void cargarBotonesCarritoListaDeseos(String nombre)
    {
        try {
            this.botonAñadirCesta.setEnabled(true);
            // Establecer la conexión a la base de datos
            Connection cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pcworld", "pcworld", "pcworld2024");

            // Verificar si el correo electrónico ya existe en la tabla
            String queryNombre = "SELECT * FROM productos WHERE `IdNombre` = ?";
            PreparedStatement pstmtNombre = cn.prepareStatement(queryNombre);
            pstmtNombre.setString(1, nombre);
            ResultSet rsProducto = pstmtNombre.executeQuery();
            
            if(rsProducto.next())
            {
                String queryProductoCesta = "SELECT * FROM cesta WHERE `IdProducto` = ? AND `IdCliente` = ?";
                PreparedStatement pstmtProductoCesta = cn.prepareStatement(queryProductoCesta);
                pstmtProductoCesta.setString(1, nombreProducto.getText());
                pstmtProductoCesta.setString(2, botonCuenta.getText());
                ResultSet rsProductoCesta = pstmtProductoCesta.executeQuery();

                if(rsProductoCesta.next())
                {
                    botonAñadirCesta.setEnabled(false);
                    botonAñadirCesta.setIcon(new javax.swing.ImageIcon("ImagenesDecoracion" + File.separator + "carritoAñadirRelleno.png"));
                }else
                {
                    if(Integer.parseInt(rsProducto.getString("stockProducto")) != 0)
                    {
                        botonAñadirCesta.setEnabled(true);
                        botonAñadirCesta.setIcon(new javax.swing.ImageIcon("ImagenesDecoracion" + File.separator + "carritoAñadir.png"));
                    }
                }

                String queryProductoListaDeseos = "SELECT * FROM lista_deseos WHERE `IdProducto` = ? AND `IdCliente` = ?";
                PreparedStatement pstmtProductoListaDeseos = cn.prepareStatement(queryProductoListaDeseos);
                pstmtProductoListaDeseos.setString(1, nombreProducto.getText());
                pstmtProductoListaDeseos.setString(2, botonCuenta.getText());
                ResultSet rsProductoListaDeseos = pstmtProductoListaDeseos.executeQuery();
                if(rsProductoListaDeseos.next())
                {
                    botonAñadirListaDeseos.setEnabled(false);
                    botonAñadirListaDeseos.setIcon(new javax.swing.ImageIcon("ImagenesDecoracion" + File.separator + "listaDeseosMasRelleno.png"));
                }else
                {
                    botonAñadirListaDeseos.setEnabled(true);
                    botonAñadirListaDeseos.setIcon(new javax.swing.ImageIcon("ImagenesDecoracion" + File.separator + "listaDeseosMas.png"));
                }
            }
            cn.close();
        } catch(Exception e) {
            System.out.println("Error: " + e);
        }
    }
    
    public void cargarModificarStock()
    {
        if(botonCuenta.getText().equals("Admin"))
        {
            panelStock.setVisible(true);
            borrarProductoBD.setVisible(true);
        }
        else
        {
            panelStock.setVisible(false);
            borrarProductoBD.setVisible(false);
        }
    }
    
    private void cargarLabelStock(String nombre)
    {
        try {
            
            // Establecer la conexión a la base de datos
            Connection cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pcworld", "pcworld", "pcworld2024");

            // Verificar si el correo electrónico ya existe en la tabla
            String queryNombre = "SELECT * FROM productos WHERE `IdNombre` = ?";
            PreparedStatement pstmtNombre = cn.prepareStatement(queryNombre);
            pstmtNombre.setString(1, nombre);
            ResultSet rsProducto = pstmtNombre.executeQuery();

            if (rsProducto.next()) {
                if (Integer.parseInt(rsProducto.getString("stockProducto")) > 5)
                {
                    labelStock.setText("Hay stock disponible");
                    labelStock.setForeground(Color.GREEN);
                }
                else if (Integer.parseInt(rsProducto.getString("stockProducto")) == 0)
                {
                    labelStock.setText("No hay stock");
                    labelStock.setForeground(Color.RED);
                    this.botonAñadirCesta.setEnabled(false);
                }
                else if (Integer.parseInt(rsProducto.getString("stockProducto")) == 1)
                {
                    labelStock.setText("Queda 1 unidad");
                    labelStock.setForeground(Color.ORANGE);
                }
                else if (Integer.parseInt(rsProducto.getString("stockProducto")) == 2)
                {
                    labelStock.setText("Quedan 2 unidades");
                    labelStock.setForeground(Color.ORANGE);
                }
                else
                {
                    labelStock.setText("Quedan pocas unidades");
                    labelStock.setForeground(Color.YELLOW);
                }
                            
            cn.close();
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
        panelFotoProducto = new javax.swing.JPanel();
        panelFotoGrande = new javax.swing.JPanel();
        fotoGrande = new javax.swing.JLabel();
        panelFotoPequeña2 = new javax.swing.JPanel();
        fotoPequeña2 = new javax.swing.JLabel();
        panelFotoPequeña1 = new javax.swing.JPanel();
        fotoPequeña1 = new javax.swing.JLabel();
        panelDescripcion = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        descripcionProducto = new javax.swing.JTextArea();
        nombreProducto = new javax.swing.JTextArea();
        panelPrecio = new javax.swing.JPanel();
        labelPrecioProducto = new javax.swing.JLabel();
        labelStock = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        botonAñadirCesta = new javax.swing.JButton();
        botonAñadirListaDeseos = new javax.swing.JButton();
        labelErrorCesta = new javax.swing.JLabel();
        labelErrorListaDeDeseos = new javax.swing.JLabel();
        panelStock = new javax.swing.JPanel();
        labelContadorStock = new javax.swing.JLabel();
        labelMenosStock = new javax.swing.JLabel();
        labelMasStock = new javax.swing.JLabel();
        borrarProductoBD = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setBackground(new java.awt.Color(0, 0, 0));
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

        panelFotoProducto.setBackground(new java.awt.Color(248, 127, 152));
        panelFotoProducto.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        panelFotoGrande.setBackground(new java.awt.Color(255, 255, 255));

        fotoGrande.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout panelFotoGrandeLayout = new javax.swing.GroupLayout(panelFotoGrande);
        panelFotoGrande.setLayout(panelFotoGrandeLayout);
        panelFotoGrandeLayout.setHorizontalGroup(
            panelFotoGrandeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(fotoGrande, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panelFotoGrandeLayout.setVerticalGroup(
            panelFotoGrandeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(fotoGrande, javax.swing.GroupLayout.DEFAULT_SIZE, 368, Short.MAX_VALUE)
        );

        panelFotoPequeña2.setBackground(new java.awt.Color(255, 255, 255));

        fotoPequeña2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                fotoPequeña2MousePressed(evt);
            }
        });

        javax.swing.GroupLayout panelFotoPequeña2Layout = new javax.swing.GroupLayout(panelFotoPequeña2);
        panelFotoPequeña2.setLayout(panelFotoPequeña2Layout);
        panelFotoPequeña2Layout.setHorizontalGroup(
            panelFotoPequeña2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(fotoPequeña2, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
        );
        panelFotoPequeña2Layout.setVerticalGroup(
            panelFotoPequeña2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(fotoPequeña2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
        );

        panelFotoPequeña1.setBackground(new java.awt.Color(255, 255, 255));

        fotoPequeña1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                fotoPequeña1MousePressed(evt);
            }
        });

        javax.swing.GroupLayout panelFotoPequeña1Layout = new javax.swing.GroupLayout(panelFotoPequeña1);
        panelFotoPequeña1.setLayout(panelFotoPequeña1Layout);
        panelFotoPequeña1Layout.setHorizontalGroup(
            panelFotoPequeña1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(fotoPequeña1, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
        );
        panelFotoPequeña1Layout.setVerticalGroup(
            panelFotoPequeña1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(fotoPequeña1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout panelFotoProductoLayout = new javax.swing.GroupLayout(panelFotoProducto);
        panelFotoProducto.setLayout(panelFotoProductoLayout);
        panelFotoProductoLayout.setHorizontalGroup(
            panelFotoProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFotoProductoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelFotoGrande, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(panelFotoProductoLayout.createSequentialGroup()
                .addGap(92, 92, 92)
                .addComponent(panelFotoPequeña1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(panelFotoPequeña2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(92, Short.MAX_VALUE))
        );
        panelFotoProductoLayout.setVerticalGroup(
            panelFotoProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFotoProductoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelFotoGrande, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addGroup(panelFotoProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(panelFotoPequeña1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelFotoPequeña2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(41, Short.MAX_VALUE))
        );

        panelDescripcion.setBackground(new java.awt.Color(248, 127, 152));
        panelDescripcion.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panelDescripcion.setPreferredSize(new java.awt.Dimension(380, 656));

        jSeparator1.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator1.setForeground(new java.awt.Color(0, 0, 0));

        descripcionProducto.setEditable(false);
        descripcionProducto.setBackground(new java.awt.Color(248, 127, 152));
        descripcionProducto.setColumns(20);
        descripcionProducto.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        descripcionProducto.setLineWrap(true);
        descripcionProducto.setRows(20);
        descripcionProducto.setToolTipText("");
        descripcionProducto.setWrapStyleWord(true);
        descripcionProducto.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        descripcionProducto.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        descripcionProducto.setFocusable(false);

        nombreProducto.setEditable(false);
        nombreProducto.setBackground(new java.awt.Color(248, 127, 152));
        nombreProducto.setColumns(20);
        nombreProducto.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        nombreProducto.setForeground(new java.awt.Color(0, 0, 0));
        nombreProducto.setLineWrap(true);
        nombreProducto.setRows(20);
        nombreProducto.setWrapStyleWord(true);
        nombreProducto.setFocusable(false);

        javax.swing.GroupLayout panelDescripcionLayout = new javax.swing.GroupLayout(panelDescripcion);
        panelDescripcion.setLayout(panelDescripcionLayout);
        panelDescripcionLayout.setHorizontalGroup(
            panelDescripcionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDescripcionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelDescripcionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(descripcionProducto, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSeparator1)
                    .addComponent(nombreProducto))
                .addContainerGap())
        );
        panelDescripcionLayout.setVerticalGroup(
            panelDescripcionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDescripcionLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(nombreProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(descripcionProducto)
                .addContainerGap())
        );

        panelPrecio.setBackground(new java.awt.Color(255, 255, 255));
        panelPrecio.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panelPrecio.setPreferredSize(new java.awt.Dimension(380, 656));

        labelPrecioProducto.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        labelPrecioProducto.setForeground(new java.awt.Color(255, 0, 0));

        jSeparator2.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator2.setForeground(new java.awt.Color(0, 0, 0));

        botonAñadirCesta.setIcon(new javax.swing.ImageIcon("ImagenesDecoracion" + File.separator + "carritoAñadir.png"));
        botonAñadirCesta.setText("Añadir a la cesta");
        botonAñadirCesta.setFocusPainted(false);
        botonAñadirCesta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                botonAñadirCestaMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                botonAñadirCestaMouseReleased(evt);
            }
        });
        botonAñadirCesta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonAñadirCestaActionPerformed(evt);
            }
        });

        botonAñadirListaDeseos.setIcon(new javax.swing.ImageIcon("ImagenesDecoracion" + File.separator + "listaDeseosMas.png"));
        botonAñadirListaDeseos.setText("Añadir a la lista de deseos");
        botonAñadirListaDeseos.setFocusPainted(false);
        botonAñadirListaDeseos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                botonAñadirListaDeseosMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                botonAñadirListaDeseosMouseReleased(evt);
            }
        });
        botonAñadirListaDeseos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonAñadirListaDeseosActionPerformed(evt);
            }
        });

        labelErrorCesta.setText("                             ");

        labelErrorListaDeDeseos.setText("                                 ");

        panelStock.setBackground(new java.awt.Color(255, 255, 255));
        panelStock.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        labelContadorStock.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelContadorStock.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        labelMenosStock.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelMenosStock.setIcon(new javax.swing.ImageIcon("ImagenesDecoracion" + File.separator + "Minus.png"));
        labelMenosStock.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        labelMenosStock.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                labelMenosStockMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                labelMenosStockMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                labelMenosStockMouseReleased(evt);
            }
        });

        labelMasStock.setBackground(new java.awt.Color(0, 255, 102));
        labelMasStock.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelMasStock.setIcon(new javax.swing.ImageIcon("ImagenesDecoracion" + File.separator + "Plus.png"));
        labelMasStock.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        labelMasStock.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                labelMasStockMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                labelMasStockMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                labelMasStockMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout panelStockLayout = new javax.swing.GroupLayout(panelStock);
        panelStock.setLayout(panelStockLayout);
        panelStockLayout.setHorizontalGroup(
            panelStockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelStockLayout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(labelMenosStock, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
                .addComponent(labelContadorStock, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44)
                .addComponent(labelMasStock, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33))
        );
        panelStockLayout.setVerticalGroup(
            panelStockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelStockLayout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(panelStockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(labelContadorStock, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelStockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(labelMasStock, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(labelMenosStock, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(9, 9, 9))
        );

        borrarProductoBD.setIcon(new javax.swing.ImageIcon("ImagenesDecoracion" + File.separator + "trash.png"));
        borrarProductoBD.setFocusPainted(false);
        borrarProductoBD.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                borrarProductoBDMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                borrarProductoBDMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                borrarProductoBDMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout panelPrecioLayout = new javax.swing.GroupLayout(panelPrecio);
        panelPrecio.setLayout(panelPrecioLayout);
        panelPrecioLayout.setHorizontalGroup(
            panelPrecioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPrecioLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelPrecioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelPrecioLayout.createSequentialGroup()
                        .addGroup(panelPrecioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelPrecioProducto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(labelStock, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jSeparator2))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelPrecioLayout.createSequentialGroup()
                        .addGap(0, 47, Short.MAX_VALUE)
                        .addGroup(panelPrecioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(panelStock, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(botonAñadirListaDeseos, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE)
                            .addComponent(botonAñadirCesta, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(labelErrorCesta, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(labelErrorListaDeDeseos, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(borrarProductoBD, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(49, 49, 49))))
        );
        panelPrecioLayout.setVerticalGroup(
            panelPrecioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPrecioLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(labelPrecioProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(labelStock, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(botonAñadirCesta, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9)
                .addComponent(labelErrorCesta)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(botonAñadirListaDeseos, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(labelErrorListaDeDeseos)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelStock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(borrarProductoBD, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelCuerpoLayout = new javax.swing.GroupLayout(panelCuerpo);
        panelCuerpo.setLayout(panelCuerpoLayout);
        panelCuerpoLayout.setHorizontalGroup(
            panelCuerpoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCuerpoLayout.createSequentialGroup()
                .addGap(73, 73, 73)
                .addComponent(panelFotoProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(57, 57, 57)
                .addComponent(panelDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(57, 57, 57)
                .addComponent(panelPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(71, Short.MAX_VALUE))
        );
        panelCuerpoLayout.setVerticalGroup(
            panelCuerpoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCuerpoLayout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(panelCuerpoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(panelDescripcion, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 532, Short.MAX_VALUE)
                    .addComponent(panelFotoProducto, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelPrecio, javax.swing.GroupLayout.DEFAULT_SIZE, 532, Short.MAX_VALUE))
                .addContainerGap(10, Short.MAX_VALUE))
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
                .addComponent(panelCuerpo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
            if (!campoBusqueda.getText().trim().isEmpty()) {
                // Consulta SQL para obtener todos los productos que contienen la cadena de búsqueda
                String queryProducto = "SELECT * FROM productos WHERE `IdNombre` LIKE ?";
                PreparedStatement pstmtProducto = cn.prepareStatement(queryProducto);
                pstmtProducto.setString(1, "%" + campoBusqueda.getText().trim() + "%"); // Utiliza % para buscar coincidencias parciales
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
                            InterfazPrincipal.getInstancia().productoUser = true;
                            InterfazProducto.crearNuevaInstancia();
                            cargarProducto(nombreProducto);
                            setVisible(true);
                            panelPrincipal.requestFocusInWindow();
                            campoBusqueda.setText("");
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
                    Color color = JColorChooser.showDialog(InterfazProducto.this, "Selecciona un color", panelPrincipal.getBackground());
                    if (color != null) 
                    {
                        panelPrincipal.setBackground(color);
                        panelCuerpo.setBackground(color);
                        panelFotoProducto.setBackground(color);
                        panelDescripcion.setBackground(color);
                        panelPrecio.setBackground(color);
                        JOptionPane.showMessageDialog(InterfazProducto.this, "Color de fondo actualizado");
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
                    InterfazPrincipal.getInstancia().botonCuenta.setText("Cuenta");
                    InterfazCesta.getInstancia().botonCuenta.setText("Cuenta");
                    InterfazMostrarProductos.getInstancia().botonCuenta.setText("Cuenta");
                    
                    cargarBotonesCarritoListaDeseos(getNombreProducto());
                    cargarModificarStock();
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

    private void botonAñadirListaDeseosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonAñadirListaDeseosActionPerformed
        try {
            // Establecer la conexión a la base de datos
            Connection cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pcworld", "pcworld", "pcworld2024");

            // Verificar si el texto de búsqueda no está vacío
            if (!this.botonCuenta.getText().equals("Cuenta"))
            {
                labelErrorListaDeDeseos.setText("         ");
                // Verificar si el correo electrónico ya existe en la tabla
                String queryProducto = "SELECT * FROM productos WHERE `IdNombre` = ?";
                PreparedStatement pstmtProducto = cn.prepareStatement(queryProducto);
                pstmtProducto.setString(1, nombreProducto.getText());
                ResultSet rsProducto = pstmtProducto.executeQuery();
                
                if(rsProducto.next())
                {
                    String queryProductoListaDeseos = "SELECT * FROM lista_deseos WHERE `IdProducto` = ? AND `IdCliente` = ?";
                    PreparedStatement pstmtProductoListaDeseos = cn.prepareStatement(queryProductoListaDeseos);
                    pstmtProductoListaDeseos.setString(1, nombreProducto.getText());
                    pstmtProductoListaDeseos.setString(2, botonCuenta.getText());
                    ResultSet rsProductoListaDeseos = pstmtProductoListaDeseos.executeQuery();
                    if(!rsProductoListaDeseos.next())
                    {
                        PreparedStatement pstmtInsertar = cn.prepareStatement("insert into lista_deseos values(?,?)");
                        pstmtInsertar.setString(1, botonCuenta.getText());
                        pstmtInsertar.setString(2, nombreProducto.getText());
                        pstmtInsertar.executeUpdate();
                        
                        botonAñadirListaDeseos.setEnabled(false);
                        botonAñadirListaDeseos.setIcon(new javax.swing.ImageIcon("ImagenesDecoracion" + File.separator + "listaDeseosMasRelleno.png"));
                    }
                }
            }
            else
            {
                labelErrorListaDeDeseos.setText("No has iniciado sesión");
            }
            cn.close();
        } catch(Exception e) {
            System.out.println("Error: " + e);
        }
        panelPrincipal.requestFocusInWindow();
    }//GEN-LAST:event_botonAñadirListaDeseosActionPerformed

    private void labelLogoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelLogoMousePressed
        InterfazPrincipal.getInstancia().setVisible(true);
        InterfazPrincipal.getInstancia().panelPrincipal.requestFocusInWindow();
        cerrar();
    }//GEN-LAST:event_labelLogoMousePressed

    private void fotoPequeña1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fotoPequeña1MousePressed
        String rutaImagenGrande = "Imagenes" + File.separator + "Grandes" + File.separator;
        fotoGrande.setIcon(new javax.swing.ImageIcon(rutaImagenGrande + im1));
    }//GEN-LAST:event_fotoPequeña1MousePressed

    private void fotoPequeña2MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fotoPequeña2MousePressed
        String rutaImagenGrande = "Imagenes" + File.separator + "Grandes" + File.separator;
        fotoGrande.setIcon(new javax.swing.ImageIcon(rutaImagenGrande + im2));
    }//GEN-LAST:event_fotoPequeña2MousePressed

    private void botonAñadirCestaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonAñadirCestaActionPerformed
        try 
        {
            // Establecer la conexión a la base de datos
            Connection cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pcworld", "pcworld", "pcworld2024");

            // Verificar si el texto de búsqueda no está vacío
            if (!this.botonCuenta.getText().equals("Cuenta"))
            {
                labelErrorCesta.setText("         ");
                // Verificar si el correo electrónico ya existe en la tabla
                String queryProducto = "SELECT * FROM productos WHERE `IdNombre` = ?";
                PreparedStatement pstmtProducto = cn.prepareStatement(queryProducto);
                pstmtProducto.setString(1, nombreProducto.getText());
                
                ResultSet rsProducto = pstmtProducto.executeQuery();
                
                if(rsProducto.next())
                {
                    if(Integer.parseInt(rsProducto.getString("stockProducto")) > 0)
                    {
                        String queryProductoCesta = "SELECT * FROM cesta WHERE `IdProducto` = ? AND `IdCliente` = ?";
                        PreparedStatement pstmtProductoCesta = cn.prepareStatement(queryProductoCesta);
                        pstmtProductoCesta.setString(1, nombreProducto.getText());
                        pstmtProductoCesta.setString(2, botonCuenta.getText());
                        ResultSet rsProductoCesta = pstmtProductoCesta.executeQuery();
                        if(!rsProductoCesta.next())
                        {
                            PreparedStatement pstmtInsertar = cn.prepareStatement("insert into cesta values(?,?,?)");
                            pstmtInsertar.setString(1, botonCuenta.getText());
                            pstmtInsertar.setString(2, nombreProducto.getText());
                            pstmtInsertar.setString(3, "1");
                            pstmtInsertar.executeUpdate();
                            if(Integer.parseInt(rsProducto.getString("stockProducto")) != 0)
                                botonAñadirCesta.setIcon(new javax.swing.ImageIcon("ImagenesDecoracion" + File.separator + "carritoAñadirRelleno.png"));
                            botonAñadirCesta.setEnabled(false);
                        }
                    }
                }
            }else
            {
                labelErrorCesta.setText("No has iniciado sesión");
            }
            cn.close();
        } catch(Exception e) {
            System.out.println("Error: " + e);
        }
        panelPrincipal.requestFocusInWindow();
    }//GEN-LAST:event_botonAñadirCestaActionPerformed

    private void botonListaDeseosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonListaDeseosActionPerformed
        if(!this.botonCuenta.getText().equals("Cuenta"))
        {
            try 
            {
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

    private void botonAñadirCestaMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botonAñadirCestaMousePressed
        if(botonAñadirCesta.isEnabled())
            botonAñadirCesta.setIcon(new javax.swing.ImageIcon("ImagenesDecoracion" + File.separator + "carritoAñadirRelleno.png"));
    }//GEN-LAST:event_botonAñadirCestaMousePressed

    private void botonAñadirCestaMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botonAñadirCestaMouseReleased
        if(botonAñadirCesta.isEnabled())
        {
            botonAñadirCesta.setIcon(new javax.swing.ImageIcon("ImagenesDecoracion" + File.separator + "carritoAñadir.png"));
        }
    }//GEN-LAST:event_botonAñadirCestaMouseReleased

    private void botonAñadirListaDeseosMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botonAñadirListaDeseosMousePressed
        botonAñadirListaDeseos.setIcon(new javax.swing.ImageIcon("ImagenesDecoracion" + File.separator + "listaDeseosMasRelleno.png"));
    }//GEN-LAST:event_botonAñadirListaDeseosMousePressed

    private void botonAñadirListaDeseosMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botonAñadirListaDeseosMouseReleased
        if(botonAñadirListaDeseos.isEnabled())
        {
            botonAñadirListaDeseos.setIcon(new javax.swing.ImageIcon("ImagenesDecoracion" + File.separator + "listaDeseosMas.png"));
        }
    }//GEN-LAST:event_botonAñadirListaDeseosMouseReleased

    private void labelMenosStockMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelMenosStockMousePressed
        labelMenosStock.setIcon(new javax.swing.ImageIcon("ImagenesDecoracion" + File.separator + "MinusRelleno.png"));
    }//GEN-LAST:event_labelMenosStockMousePressed

    private void labelMenosStockMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelMenosStockMouseReleased
        labelMenosStock.setIcon(new javax.swing.ImageIcon("ImagenesDecoracion" + File.separator + "Minus.png"));
    }//GEN-LAST:event_labelMenosStockMouseReleased

    private void labelMenosStockMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelMenosStockMouseClicked
        if(Integer.parseInt(labelContadorStock.getText()) > 0)
        {
            try 
            {
                // Establecer la conexión a la base de datos
                Connection cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pcworld", "pcworld", "pcworld2024");

                String queryProcduto = "SELECT * FROM productos WHERE `IdNombre` = ?";
                PreparedStatement pstmtProducto = cn.prepareStatement(queryProcduto);
                pstmtProducto.setString(1, nombreProducto.getText());

                ResultSet rsProducto = pstmtProducto.executeQuery();

                if(rsProducto.next())
                {
                    int stock;
                    String sql = "UPDATE productos SET stockProducto = ? WHERE `IdNombre` = ?";
                    PreparedStatement pstmtProductos2 = cn.prepareStatement(sql);
                    stock = Integer.parseInt(labelContadorStock.getText()) - 1;
                    labelContadorStock.setText(String.valueOf(stock));
                    if(Integer.parseInt(labelContadorStock.getText()) == 0)
                    {
                        labelMenosStock.setEnabled(false);
                    }
                    pstmtProductos2.setString(1, String.valueOf(stock));
                    pstmtProductos2.setString(2, nombreProducto.getText());                    
                    pstmtProductos2.executeUpdate();
                }
                cargarLabelStock(nombreProducto.getText());
                cn.close();
            } catch(Exception e) {
                System.out.println("Error: " + e);
            }
        }
        
    }//GEN-LAST:event_labelMenosStockMouseClicked

    private void borrarProductoBDMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_borrarProductoBDMousePressed
        borrarProductoBD.setIcon(new javax.swing.ImageIcon("ImagenesDecoracion" + File.separator + "trashRelleno.png"));
    }//GEN-LAST:event_borrarProductoBDMousePressed

    private void borrarProductoBDMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_borrarProductoBDMouseReleased
        borrarProductoBD.setIcon(new javax.swing.ImageIcon("ImagenesDecoracion" + File.separator + "trash.png"));
    }//GEN-LAST:event_borrarProductoBDMouseReleased

    private void borrarProductoBDMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_borrarProductoBDMouseClicked
        try 
        {
            // Establecer la conexión a la base de datos
            Connection cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pcworld", "pcworld", "pcworld2024");

            String queryProcduto = "SELECT * FROM productos WHERE `IdNombre` = ?";
            PreparedStatement pstmtProducto = cn.prepareStatement(queryProcduto);
            pstmtProducto.setString(1, nombreProducto.getText());

            ResultSet rsProducto = pstmtProducto.executeQuery();

            if(rsProducto.next())
            {
                String queryCesta = "SELECT * FROM cesta WHERE `IdProducto` = ?";
                PreparedStatement pstmtCesta = cn.prepareStatement(queryCesta);
                pstmtCesta.setString(1, nombreProducto.getText());

                ResultSet rsCesta = pstmtProducto.executeQuery();
                if(rsCesta.next())
                {
                    String sql1 = "DELETE FROM cesta WHERE `IdProducto` = ?";
                    PreparedStatement pstmtProductos1 = cn.prepareStatement(sql1);
                    pstmtProductos1.setString(1, nombreProducto.getText());
                    pstmtProductos1.executeUpdate();
                }

                String queryListaDeseos = "SELECT * FROM lista_deseos WHERE `IdProducto` = ?";
                PreparedStatement pstmtListaDeseos = cn.prepareStatement(queryListaDeseos);
                pstmtListaDeseos.setString(1, nombreProducto.getText());

                ResultSet rsListaDeseos = pstmtProducto.executeQuery();
                if(rsListaDeseos.next())
                {
                    String sql1 = "DELETE FROM lista_deseos WHERE `IdProducto` = ?";
                    PreparedStatement pstmtProductos1 = cn.prepareStatement(sql1);
                    pstmtProductos1.setString(1, nombreProducto.getText());
                    pstmtProductos1.executeUpdate();
                }

                //String sql1 = "DELETE FROM productos WHERE `IdNombre` = ?";
                String sql1 = "UPDATE productos SET oculto = '1' WHERE `IdNombre` = ?";
                PreparedStatement pstmtProductos1 = cn.prepareStatement(sql1);
                pstmtProductos1.setString(1, nombreProducto.getText());
                pstmtProductos1.executeUpdate();

            }
            if( InterfazPrincipal.getInstancia().getAreaNombreProducto1().equals(nombreProducto.getText()) || 
                InterfazPrincipal.getInstancia().getAreaNombreProducto2().equals(nombreProducto.getText()) || 
                InterfazPrincipal.getInstancia().getAreaNombreProducto3().equals(nombreProducto.getText()) || 
                InterfazPrincipal.getInstancia().getAreaNombreProducto4().equals(nombreProducto.getText()))
            {
                InterfazPrincipal.getInstancia().productosAleatorios();
            }

            InterfazPrincipal.getInstancia().setVisible(true);
            cerrar();
            cn.close();
        } catch(Exception e) {
            System.out.println("Error: " + e);
        }
    }//GEN-LAST:event_borrarProductoBDMouseClicked

    private void campoBusquedaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoBusquedaKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER)
        {
            InterfazPrincipal.getInstancia().interfazActual = "InterfazProducto";
            InterfazCesta.getInstancia().campoBusqueda.setText("   Buscar...");
            InterfazListaDeseos.getInstancia().campoBusqueda.setText("   Buscar...");
            InterfazPrincipal.getInstancia().campoBusqueda.setText("   Buscar...");
            InterfazMostrarProductos.getInstancia().campoBusqueda.setText("   Buscar...");
            InterfazConfiguradorPcs.getInstancia().campoBusqueda.setText("   Buscar...");
            busquedaProducto();
        }
    }//GEN-LAST:event_campoBusquedaKeyPressed

    private void botonLupaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botonLupaMouseClicked
        InterfazPrincipal.getInstancia().interfazActual = "InterfazProducto";
        InterfazCesta.getInstancia().campoBusqueda.setText("   Buscar...");
        InterfazListaDeseos.getInstancia().campoBusqueda.setText("   Buscar...");
        InterfazPrincipal.getInstancia().campoBusqueda.setText("   Buscar...");
        InterfazMostrarProductos.getInstancia().campoBusqueda.setText("   Buscar...");
        InterfazConfiguradorPcs.getInstancia().campoBusqueda.setText("   Buscar...");
        busquedaProducto();
    }//GEN-LAST:event_botonLupaMouseClicked

    private void labelMasStockMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelMasStockMouseReleased
        labelMasStock.setIcon(new javax.swing.ImageIcon("ImagenesDecoracion" + File.separator + "Plus.png"));
    }//GEN-LAST:event_labelMasStockMouseReleased

    private void labelMasStockMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelMasStockMousePressed
        labelMasStock.setIcon(new javax.swing.ImageIcon("ImagenesDecoracion" + File.separator + "PlusRelleno.png"));
    }//GEN-LAST:event_labelMasStockMousePressed

    private void labelMasStockMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelMasStockMouseClicked
        if(!labelMenosStock.isEnabled())
        {
            labelMenosStock.setEnabled(true);
        }
        try
        {
            // Establecer la conexión a la base de datos
            Connection cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pcworld", "pcworld", "pcworld2024");

            String queryProcduto = "SELECT * FROM productos WHERE `IdNombre` = ?";
            PreparedStatement pstmtProducto = cn.prepareStatement(queryProcduto);
            pstmtProducto.setString(1, nombreProducto.getText());

            ResultSet rsProducto = pstmtProducto.executeQuery();

            if(rsProducto.next())
            {
                int stock;
                String sql = "UPDATE productos SET stockProducto = ? WHERE `IdNombre` = ?";
                PreparedStatement pstmtProductos2 = cn.prepareStatement(sql);
                stock = Integer.parseInt(labelContadorStock.getText()) + 1;
                labelContadorStock.setText(String.valueOf(stock));
                pstmtProductos2.setString(1, String.valueOf(stock));
                pstmtProductos2.setString(2, nombreProducto.getText());
                pstmtProductos2.executeUpdate();
            }
            if(Integer.parseInt(labelContadorStock.getText()) == 1)
            {
                String queryCesta = "SELECT * FROM cesta WHERE `IdCliente` = ? and `IdProducto` = ?";
                PreparedStatement pstmtCesta = cn.prepareStatement(queryCesta);
                pstmtCesta.setString(1, "Admin");
                pstmtCesta.setString(2, nombreProducto.getText());

                ResultSet rsCesta = pstmtCesta.executeQuery();
                if(!rsCesta.next())
                {
                    botonAñadirCesta.setEnabled(true);
                }
            }
            cargarLabelStock(nombreProducto.getText());
            cn.close();
        } catch(Exception e) {
            System.out.println("Error: " + e);
        }

    }//GEN-LAST:event_labelMasStockMouseClicked

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
    private javax.swing.JButton borrarProductoBD;
    private javax.swing.JButton botonAñadirCesta;
    private javax.swing.JButton botonAñadirListaDeseos;
    private javax.swing.JButton botonCesta;
    public javax.swing.JButton botonCuenta;
    private javax.swing.JButton botonListaDeseos;
    private javax.swing.JButton botonLupa;
    public javax.swing.JTextField campoBusqueda;
    private javax.swing.JTextArea descripcionProducto;
    private javax.swing.JLabel fotoGrande;
    private javax.swing.JLabel fotoPequeña1;
    private javax.swing.JLabel fotoPequeña2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel labelContadorStock;
    public javax.swing.JLabel labelErrorCesta;
    public javax.swing.JLabel labelErrorListaDeDeseos;
    private javax.swing.JLabel labelLogo;
    private javax.swing.JLabel labelMasStock;
    private javax.swing.JLabel labelMenosStock;
    private javax.swing.JLabel labelPrecioProducto;
    private javax.swing.JLabel labelStock;
    private javax.swing.JTextArea nombreProducto;
    private javax.swing.JPanel panelBuscador;
    private javax.swing.JPanel panelCabecera;
    private javax.swing.JPanel panelCuerpo;
    private javax.swing.JPanel panelDescripcion;
    private javax.swing.JPanel panelFotoGrande;
    private javax.swing.JPanel panelFotoPequeña1;
    private javax.swing.JPanel panelFotoPequeña2;
    private javax.swing.JPanel panelFotoProducto;
    private javax.swing.JPanel panelFuncionalidades;
    private javax.swing.JPanel panelHome;
    private javax.swing.JPanel panelPrecio;
    public javax.swing.JPanel panelPrincipal;
    private javax.swing.JPanel panelStock;
    // End of variables declaration//GEN-END:variables
    private javax.swing.JPopupMenu popUpMenu;
    private String im1,im2;
    private ErrorCesta error;
    private static InterfazProducto instancia;
}