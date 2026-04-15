package co.edu.poli.examen2_lopez.modelo;

public abstract class Inmueble {

    private String numero;
	private String fechaCompra;
	private boolean estado;
	private Propietario propietario;

	public Inmueble(String numero, String fechaCompra, boolean estado, Propietario propietario) {
		this.numero = numero;
		this.fechaCompra = fechaCompra;
		this.estado = estado;
		this.propietario = propietario;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getFechaCompra() {
		return fechaCompra;
	}

	public void setFechaCompra(String fechaCompra) {
		this.fechaCompra = fechaCompra;
	}

	public boolean isEstado() {
		return estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}

	public Propietario getPropietario() {
		return propietario;
	}

	public void setPropietario(Propietario propietario) {
		this.propietario = propietario;
	}

	public String vender() {
		this.estado = false;
		return "Inmueble " + numero + " VENDIDO.";
	}

	public String alquilar() {
		this.estado = true;
		return "Inmueble " + numero + "ALQUILADO.";
	}

	@Override
	public String toString() {
		return "numero=" + numero + ", fechaCompra=" + fechaCompra + ", estado=" + estado + ", propietario=" + propietario;
	}
}