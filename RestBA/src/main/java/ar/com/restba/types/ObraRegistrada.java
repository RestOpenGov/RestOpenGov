package ar.com.restba.types;

/**
 * Representa cada obra registrada en la ciudad de Buenos Aires. 
 * Permite acceder a los datos de cada obra registrada.
 * 
 * 
 * @author Nicolás Meléndez |
 * Email: nfmelendez@gmail.com |
 * Twitter: @nfmelendez
 *
 */
public class ObraRegistrada {
	
	/** ID del resource dentro de Buenos Aires Data. Identifica univocamente este recurso. */
	public static final String OBRAS_REGISTRADAS_ID = "ce067ee1-1a4c-46c2-a269-d01e21a7fa4d";
	
	private String nExpediente;
	private String direccion;
	private String smp;
	private String estadoTramite;
	private String fechaEstado;
	private String tipoObra;
	private String nombreProfesional;

	public ObraRegistrada() {
	}

	public String getnExpediente() {
		return nExpediente;
	}

	public void setnExpediente(String nExpediente) {
		this.nExpediente = nExpediente;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getSmp() {
		return smp;
	}

	public void setSmp(String smp) {
		this.smp = smp;
	}

	public String getEstadoTramite() {
		return estadoTramite;
	}

	public void setEstadoTramite(String estadoTramite) {
		this.estadoTramite = estadoTramite;
	}

	public String getFechaEstado() {
		return fechaEstado;
	}

	public void setFechaEstado(String fechaEstado) {
		this.fechaEstado = fechaEstado;
	}

	public String getTipoObra() {
		return tipoObra;
	}

	public void setTipoObra(String tipoObra) {
		this.tipoObra = tipoObra;
	}

	public String getNombreProfesional() {
		return nombreProfesional;
	}

	public void setNombreProfesional(String nombreProfesional) {
		this.nombreProfesional = nombreProfesional;
	}

}
