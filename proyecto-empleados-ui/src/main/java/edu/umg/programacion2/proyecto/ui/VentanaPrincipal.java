package edu.umg.programacion2.proyecto.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.Period;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import edu.umg.programacion2.proyecto.dao.EmpleadoDAO;
import edu.umg.programacion2.proyecto.modelo.Empleado;

public class VentanaPrincipal extends JFrame {
    private EmpleadoDAO empleadoDAO;
    private JTable tablaEmpleados;
    private DefaultTableModel modeloTabla;

    private Integer idEmpleadoSeleccionado = null;
    private JTextField txtNombre;
    private JTextField txtDepartamento;
    private JTextField txtSalario;
    private JTextField txtFecha;
    private JTextField txtCorreo; 
    private JCheckBox chkActivo;
    private JButton btnGuardar;
    private JButton btnEliminar;
    private JButton btnLimpiar; 

    public VentanaPrincipal() {
        empleadoDAO = new EmpleadoDAO();
        setTitle("Gestión de Empleados");
        setSize(1050, 500); // Lo hacemos un poco más ancho para la nueva columna
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        add(crearPanelFormulario(), BorderLayout.WEST);
        add(crearPanelTabla(), BorderLayout.CENTER);

        configurarEventos();
        cargarEmpleados();
    }

    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new GridLayout(8, 2, 5, 10)); 
        panel.setBorder(BorderFactory.createTitledBorder("Datos del Empleado"));

        txtNombre = new JTextField();
        txtDepartamento = new JTextField();
        txtSalario = new JTextField();
        txtFecha = new JTextField(); 
        txtCorreo = new JTextField(); 
        chkActivo = new JCheckBox("Empleado Activo", true);

        btnGuardar = new JButton("Guardar");
        btnEliminar = new JButton("Eliminar (Borrado Físico)");
        btnEliminar.setForeground(Color.RED);
        btnEliminar.setEnabled(false);
        btnLimpiar = new JButton("Limpiar / Nuevo");

        panel.add(new JLabel("Nombre Completo:"));
        panel.add(txtNombre);
        panel.add(new JLabel("Departamento:"));
        panel.add(txtDepartamento);
        panel.add(new JLabel("Correo Electrónico:")); 
        panel.add(txtCorreo);                         
        panel.add(new JLabel("Salario Mensual:"));
        panel.add(txtSalario);
        panel.add(new JLabel("Fecha (YYYY-MM-DD):"));
        panel.add(txtFecha);
        panel.add(new JLabel("Estado:"));
        panel.add(chkActivo);
        panel.add(btnGuardar);
        panel.add(btnEliminar);
        panel.add(new JLabel(""));
        panel.add(btnLimpiar);

        return panel;
    }

    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Listado de Empleados"));

        // 1. MEJORA: Agregamos la columna "Antigüedad" al final del arreglo
        String[] columnas = {"ID", "Nombre", "Departamento", "Salario", "Fecha", "Activo", "Correo", "Antigüedad"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        tablaEmpleados = new JTable(modeloTabla);
        panel.add(new JScrollPane(tablaEmpleados), BorderLayout.CENTER);
        return panel;
    }

    private void configurarEventos() {
        btnGuardar.addActionListener(e -> guardarEmpleado());
        btnEliminar.addActionListener(e -> eliminarEmpleado());
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        tablaEmpleados.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tablaEmpleados.getSelectedRow() != -1) {
                llenarFormularioDesdeTabla();
            }
        });
    }

    private void llenarFormularioDesdeTabla() {
        int fila = tablaEmpleados.getSelectedRow();
        idEmpleadoSeleccionado = (Integer) modeloTabla.getValueAt(fila, 0);
        txtNombre.setText(modeloTabla.getValueAt(fila, 1).toString());
        txtDepartamento.setText(modeloTabla.getValueAt(fila, 2).toString());
        txtSalario.setText(modeloTabla.getValueAt(fila, 3).toString());
        txtFecha.setText(modeloTabla.getValueAt(fila, 4).toString());
        
        String estado = modeloTabla.getValueAt(fila, 5).toString();
        chkActivo.setSelected(estado.equals("Activo"));
        
        Object correoValor = modeloTabla.getValueAt(fila, 6);
        txtCorreo.setText(correoValor != null ? correoValor.toString() : "");

        // Nota: No extraemos la antigüedad porque no hay un campo de texto para ella, 
        // ya que es solo calculada para mostrarse en la tabla.

        btnGuardar.setText("Actualizar");
        btnEliminar.setEnabled(true);
    }

    private void cargarEmpleados() {
        modeloTabla.setRowCount(0); 
        try {
            java.util.List<Empleado> lista = empleadoDAO.ListarTodos();
            LocalDate hoy = LocalDate.now(); // Fecha actual del sistema

            for (Empleado emp : lista) {
                // 2. MEJORA: Calculamos la antigüedad en años
                Period periodo = Period.between(emp.getFechaContratacion(), hoy);
                String antiguedad = periodo.getYears() + " años";

                Object[] fila = {
                    emp.getId(), 
                    emp.getNombreCompleto(), 
                    emp.getDepartamento(),
                    emp.getSalarioMensual(), 
                    emp.getFechaContratacion(),
                    emp.isActivo() ? "Activo" : "Inactivo",
                    emp.getCorreoElectronico(),
                    antiguedad // NUEVO DATO CALCULADO AL VUELO
                };
                modeloTabla.addRow(fila); 
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error BD: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void guardarEmpleado() {
        String nombre = txtNombre.getText().trim();
        String depto = txtDepartamento.getText().trim();
        String correo = txtCorreo.getText().trim(); 
        String salarioStr = txtSalario.getText().trim();
        String fechaStr = txtFecha.getText().trim();
        boolean activo = chkActivo.isSelected();

        if (nombre.isEmpty() || depto.isEmpty() || correo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nombre, departamento y correo son obligatorios.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!correo.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            JOptionPane.showMessageDialog(this, "El correo electrónico no es válido.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double salario = 0;
        try {
            salario = Double.parseDouble(salarioStr);
            if (salario <= 0) {
                JOptionPane.showMessageDialog(this, "El salario debe ser mayor a cero.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Salario inválido.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        java.time.LocalDate fecha = null;
        try {
            fecha = java.time.LocalDate.parse(fechaStr);
            if (fecha.isAfter(java.time.LocalDate.now())) {
                JOptionPane.showMessageDialog(this, "La fecha no puede ser futura.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Use el formato YYYY-MM-DD.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Empleado empleado = new Empleado(nombre, depto, salario, fecha, activo, correo);
        
        try {
            if (idEmpleadoSeleccionado == null) {
                empleadoDAO.crear(empleado);
                JOptionPane.showMessageDialog(this, "Empleado registrado con éxito.");
            } else {
                empleado.setId(idEmpleadoSeleccionado);
                empleadoDAO.actualizar(empleado);
                JOptionPane.showMessageDialog(this, "Empleado actualizado con éxito.");
            }
            limpiarFormulario();
            cargarEmpleados(); 
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error BD", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarEmpleado() {
        if (idEmpleadoSeleccionado == null) return;
        if (JOptionPane.showConfirmDialog(this, "¿Seguro de eliminar físicamente este registro?", "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            try {
                empleadoDAO.eliminar(idEmpleadoSeleccionado);
                limpiarFormulario();
                cargarEmpleados();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error BD", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void limpiarFormulario() {
        idEmpleadoSeleccionado = null; 
        txtNombre.setText("");
        txtDepartamento.setText("");
        txtSalario.setText("");
        txtFecha.setText("");
        txtCorreo.setText(""); 
        chkActivo.setSelected(true);
        
        btnGuardar.setText("Guardar"); 
        btnEliminar.setEnabled(false); 
        tablaEmpleados.clearSelection(); 
    }
}