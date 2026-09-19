package edu.umg.programacion2.proyecto.dao;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import edu.umg.programacion2.proyecto.modelo.Empleado;

public class EmpleadoDAO {
	

	//Credenciales de conexion
	
	private static final String URL = "jdbc:mysql://127.0.0.1:3306/empresa_db";
	private static final String USER = "root";
	private static final String PASSWORD = "alva_granados12852";
	
	//Metodo auxiliar para obtener la conexion
	
	private Connection getConnection() throws SQLException{
		return DriverManager.getConnection(URL, USER, PASSWORD);
	}
	
	//1. Crear 
	
	public Empleado crear(Empleado empleado) throws SQLException {
		String sql = "INSERT INTO empleados (nombre_completo, departamento, salario_mensual, fecha_contratacion, activo) VALUES (?, ?, ?, ?, ?)";
		
		//Usamos RETURN_GENERATED_KEYS para recuperar el ID que mysql le asigne
		
		try(Connection conn = getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
			stmt.setString(1, empleado.getNombreCompleto());
			stmt.setString(2, empleado.getDepartamento());
			stmt.setDouble(3, empleado.getSalarioMensual());
			
			//Convertimos el localdate de java al date de SQL
			stmt.setDate(4, Date.valueOf(empleado.getFechaContratacion()));
			stmt.setBoolean(5, empleado.isActivo());
			
			stmt.executeUpdate();
			
			//Recuperamos el ID generado
			
			try(ResultSet rs = stmt.getGeneratedKeys()){
				if(rs.next()) {
					empleado.setId(rs.getInt(1));
				}
			}
			return empleado;
		}
		}

		
		
		//2. Leer / listar
		
		public List<Empleado> ListarTodos() throws SQLException{
			List<Empleado> lista = new ArrayList<>();
			String sql = "SELECT * FROM empleados";
			
			try(Connection conn = getConnection();
					PreparedStatement stmt = conn.prepareStatement(sql);
					ResultSet rs = stmt.executeQuery()){
				
				while(rs.next()) {
					Empleado emp = new Empleado(
							rs.getInt("id"),
							rs.getString("nombre_completo"),
							rs.getString("departamento"),
							rs.getDouble("salario_mensual"),
							rs.getDate("fecha_contratacion").toLocalDate(),
							rs.getBoolean("activo"));
					lista.add(emp);
				}
				
				
			}
					
			return lista;
		}
		
		//3. Buscar por ID 
		
	
		
	
		
		public Optional<Empleado> buscarPorId(int id) throws SQLException { 
			String sql = "SELECT * FROM empleados WHERE id = ?";
			try (Connection conn = getConnection();
					PreparedStatement stmt = conn.prepareStatement(sql)){
				stmt.setInt(1, id);
				try(ResultSet rs = stmt.executeQuery()){
					if(rs.next()) {
						Empleado emp = new Empleado(
								rs.getInt("id"),
								rs.getString("nombre_completo"),
								rs.getString("departamento"),
								rs.getDouble("salario_mensual"),
								rs.getDate("fecha_contratacion").toLocalDate(),
								rs.getBoolean("activo"));
						return Optional.of(emp);
					}
				}
			}
			return Optional.empty();
		}
		
		//Actualizar
		
		public boolean actualizar(Empleado empleado) throws SQLException{
			String sql = "UPDATE empleados SET nombre_completo = ?, departamento = ?, salario_mensual = ?, fecha_contratacion = ?, activo = ? WHERE id = ?";
			try(Connection conn = getConnection();
					PreparedStatement stmt = conn.prepareStatement(sql)){
				stmt.setString(1, empleado.getNombreCompleto());
				stmt.setString(2, empleado.getDepartamento());
				stmt.setDouble(3, empleado.getSalarioMensual());
				stmt.setDate(4, Date.valueOf(empleado.getFechaContratacion()));
				stmt.setBoolean(5, empleado.isActivo());
				stmt.setInt(6, empleado.getId());
				
				int filasAfectadas = stmt.executeUpdate();
				return filasAfectadas > 0;
			}
		}
		
		//5. Elilminar - Borrado fisico segun la variante A
		
		public boolean eliminar(int id) throws SQLException{
			String sql = "DELETE FROM empleados WHERE id = ?";
			try (Connection conn = getConnection();
					PreparedStatement stmt = conn.prepareStatement(sql)){
				stmt.setInt(1, id);
				int filasAfectadas = stmt.executeUpdate();
				return filasAfectadas > 0;
			}
		}
		
		
		
		
	
}
