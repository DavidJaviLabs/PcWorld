package src;


public class Pedidos 
{
    private String idPedido;
    private String fecha;
    private double precioTotal;

    public Pedidos(String idPedido, String fecha, double precioTotal) 
    {
        this.idPedido = idPedido;
        this.fecha = fecha;
        this.precioTotal = precioTotal;
    }

    public String getIdPedido() {
        return idPedido;
    }

    public String getFecha() {
        return fecha;
    }

    public double getPrecioTotal() {
        return precioTotal;
    }
    public void setPrecioTotal(double precio) {
        precioTotal = precio;
    }
}
