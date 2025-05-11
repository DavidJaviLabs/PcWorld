package src;


import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;



public class Producto 
{
    private String nombre;
    private double precio;
    private String imagen;
    private int contador;

    public Producto(String nombre, double precio, String imagen) 
    {
        this.nombre = nombre;
        this.precio = precio;
        this.imagen = imagen;
        this.contador = 1;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public String getImagen() {
        return imagen;
    }

    public int getContador() 
    {
        try
        {
            java.sql.Connection cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pcworld", "pcworld", "pcworld2024");
            String sql = "SELECT * FROM cesta WHERE `IdProducto` = ? AND `IdCliente` = ?";
            PreparedStatement pstmtProductos = cn.prepareStatement(sql);
            pstmtProductos.setString(1, nombre);
            pstmtProductos.setString(2, InterfazCesta.getInstancia().botonCuenta.getText());
            ResultSet rsProductos = pstmtProductos.executeQuery();
            if (rsProductos.next())
            {              
                
                String sql1 = "SELECT * FROM productos WHERE `IdNombre` = ?";
                PreparedStatement pstmtProducto = cn.prepareStatement(sql1);
                pstmtProducto.setString(1, nombre);
                ResultSet rsProducto = pstmtProducto.executeQuery();
                if(rsProducto.next())
                {
                    int stock = Integer.parseInt(rsProducto.getString("stockProducto"));
                    if(stock < Integer.parseInt(rsProductos.getString("cantidadProducto")))
                    {
                        contador = 1;
                        String sql2 = "UPDATE cesta SET cantidadProducto = ? WHERE `IdProducto` = ? AND `IdCliente` = ?";
                        PreparedStatement pstmtProductos2 = cn.prepareStatement(sql2);
                        pstmtProductos2.setString(1, String.valueOf(contador));
                        pstmtProductos2.setString(2, nombre);
                        pstmtProductos2.setString(3, InterfazPrincipal.getInstancia().botonCuenta.getText());                    
                        pstmtProductos2.executeUpdate();
                    }else
                    {
                        contador = Integer.parseInt(rsProductos.getString("cantidadProducto"));
                    }
                }
            }
            cn.close();
        }catch(SQLException e1)
        {
            System.out.println("Error: " + e1);
        }
        return contador;
    }

    public void incrementarContador() 
    {
        try
        {
            java.sql.Connection cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pcworld", "pcworld", "pcworld2024");
            String sql = "SELECT * FROM cesta WHERE `IdProducto` = ? AND `IdCliente` = ?";
            PreparedStatement pstmtProductos = cn.prepareStatement(sql);
            pstmtProductos.setString(1, nombre);
            pstmtProductos.setString(2, InterfazCesta.getInstancia().botonCuenta.getText());
            ResultSet rsProductos = pstmtProductos.executeQuery();
            if (rsProductos.next())
            {              
                String sql1 = "UPDATE cesta SET cantidadProducto = ? WHERE `IdProducto` = ? AND `IdCliente` = ?";
                PreparedStatement pstmtProductos1 = cn.prepareStatement(sql1);
                pstmtProductos1.setString(1, String.valueOf(1+contador));
                pstmtProductos1.setString(2, nombre);
                pstmtProductos1.setString(3, InterfazCesta.getInstancia().botonCuenta.getText());
                pstmtProductos1.executeUpdate();
            }
            cn.close();
        }catch(SQLException e1)
        {
            System.out.println("Error: " + e1);
        }
        contador++;
    }

    public void decrementarContador() {
        try
        {
            java.sql.Connection cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pcworld", "pcworld", "pcworld2024");
            String sql = "SELECT * FROM cesta WHERE `IdProducto` = ? AND `IdCliente` = ?";
            PreparedStatement pstmtProductos = cn.prepareStatement(sql);
            pstmtProductos.setString(1, nombre);
            pstmtProductos.setString(2, InterfazCesta.getInstancia().botonCuenta.getText());
            ResultSet rsProductos = pstmtProductos.executeQuery();
            if (rsProductos.next())
            {              
                String sql1 = "UPDATE cesta SET cantidadProducto = ? WHERE `IdProducto` = ? AND `IdCliente` = ?";
                PreparedStatement pstmtProductos1 = cn.prepareStatement(sql1);
                pstmtProductos1.setString(1, String.valueOf(contador-1));
                pstmtProductos1.setString(2, nombre);
                pstmtProductos1.setString(3, InterfazCesta.getInstancia().botonCuenta.getText());
                pstmtProductos1.executeUpdate();
            }
            cn.close();
        }catch(SQLException e1)
        {
            System.out.println("Error: " + e1);
        }
        if (contador > 1) {
            contador--;
        }
    }
}
