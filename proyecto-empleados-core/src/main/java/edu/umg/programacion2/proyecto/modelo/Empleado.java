package edu.umg.programacion2.proyecto.modelo;
import java.time.LocalDate;

public class Empleado {

	//Atributos igual a que los la base de datos
	
	private int id;
	private String nombreCompleto;
	private String departamento;
	private double salarioMensual; 
	private LocalDate fechaContratacion;
	private boolean activo;
	
	
	//Constructor vacio, necesario para alguna herramienta
	
	public Empleado() {
		
	}

	//Contructor sin id (ideal para crear un empleado nuevo ya que mysql pone el id automaticamente)

	public Empleado(String nombreCompleto, String departamento, double salarioMensual, LocalDate fechaContratacion,
			boolean activo) {
		super();
		this.nombreCompleto = nombreCompleto;
		this.departamento = departamento;
		this.salarioMensual = salarioMensual;
		this.fechaContratacion = fechaContratacion;
		this.activo = activo;
	}
	
	//Contructor con todos los datos, ideal para leer todos los datos de la base de datos

	public Empleado(int id, String nombreCompleto, String departamento, double salarioMensual,
			LocalDate fechaContratacion, boolean activo) {
		super();
		this.id = id;
		this.nombreCompleto = nombreCompleto;
		this.departamento = departamento;
		this.salarioMensual = salarioMensual;
		this.fechaContratacion = fechaContratacion;
		this.activo = activo;
	}
	
	//Generar getters and setters ideal para acceder y modificar los datos

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombreCompleto() {
		return nombreCompleto;
	}

	public void setNombreCompleto(String nombreCompleto) {
		this.nombreCompleto = nombreCompleto;
	}

	public String getDepartamento() {
		return departamento;
	}

	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}

	public double getSalarioMensual() {
		return salarioMensual;
	}

	public void setSalarioMensual(double salarioMensual) {
		this.salarioMensual = salarioMensual;
	}

	public LocalDate getFechaContratacion() {
		return fechaContratacion;
	}

	public void setFechaContratacion(LocalDate fechaContratacion) {
		this.fechaContratacion = fechaContratacion;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	//Metodo to string para imprimir todos los datos por consola si es necesario
	 
	@Override
	public String toString() {
		return "Empleado [id=" + id + ", nombreCompleto=" + nombreCompleto + ", departamento=" + departamento
				+ ", salarioMensual=" + salarioMensual + ", fechaContratacion=" + fechaContratacion + ", activo="
				+ activo + "]";
	}
	
	
	
	
	
	
	
	
	
	
}
