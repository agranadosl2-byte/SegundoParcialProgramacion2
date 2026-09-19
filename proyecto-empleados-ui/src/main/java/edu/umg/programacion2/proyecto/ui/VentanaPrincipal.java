package edu.umg.programacion2.proyecto.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

import edu.umg.programacion2.proyecto.dao.EmpleadoDAO;
import edu.umg.programacion2.proyecto.modelo.Empleado;

public class VentanaPrincipal extends JFrame {

    // Instancia de nuestro DAO
    private EmpleadoDAO empleadoDAO;
    
    // Componentes visuales de la Tabla
    private JTable tablaEmpleados;
    private DefaultTableModel modeloTabla;

    // Componentes visuales del Formulario
    private JTextField txtNombre;
    private JTextField txtDepartamento;
    private JTextField txtSalario;
    private JTextField txtFecha;
    private JCheckBox chkActivo;
    private JButton btnGuardar;
    private JButton btnEliminar;
    private JButton btnLimpiar;

    // NUEVA VARIABLE: Guarda el ID del empleado que estamos editando
    private Integer idEmpleadoSeleccionado = null;

    public VentanaPrincipal() {
        empleadoDAO = new EmpleadoDAO();

        setTitle("Gestión de Empleados");
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // 1. Panel Formulario
        JPanel panelFormulario = crearPanelFormulario();
        add(panelFormulario, BorderLayout.WEST);

        // 2. Panel Tabla
        JPanel panelTabla = crearPanelTabla();
        add(panelTabla, BorderLayout.CENTER);

        // 3. Iniciar Eventos y Datos
        configurarEventos();
        cargarEmpleados();
    }

    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new GridLayout(7, 2, 5, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Datos del Empleado"));

        txtNombre = new JTextField();
        txtDepartamento = new JTextField();
        txtSalario = new JTextField();
        txtFecha = new JTextField(); 
        chkActivo = new JCheckBox("Empleado Activo", true);

        btnGuardar = new JButton("Guardar");
        btnEliminar = new JButton("Eliminar (Borrado Físico)");
        btnEliminar.setForeground(Color.RED);
        btnEliminar.setEnabled(false); // Lo desactivamos al inicio porque no hay nadie seleccionado
        btnLimpiar = new JButton("Limpiar / Nuevo");

     // Y al final de ese mismo método, agrégalo al panel (debes cambiar el GridLayout a 8 filas en lugar de 7)
     // Cambia esto: JPanel panel = new JPanel(new GridLayout(8, 2, 5, 10)); <-- PONLE 8
        panel.add(new JLabel("")); // Espacio vacío para alinear
        panel.add(btnLimpiar);

        panel.add(new JLabel("Nombre Completo:"));
        panel.add(txtNombre);
        panel.add(new JLabel("Departamento:"));
        panel.add(txtDepartamento);
        panel.add(new JLabel("Salario Mensual:"));
        panel.add(txtSalario);
        panel.add(new JLabel("Fecha (YYYY-MM-DD):"));
        panel.add(txtFecha);
        panel.add(new JLabel("Estado:"));
        panel.add(chkActivo);
        panel.add(btnGuardar);
        panel.add(btnEliminar);

        return panel;
    }

    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Listado de Empleados"));

        String[] columnas = {"ID", "Nombre", "Departamento", "Salario", "Fecha", "Activo"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        
        tablaEmpleados = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaEmpleados);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel; // ¡Aquí faltaba la llave de cierre original!
    }
        
    // --- MÉTODOS DE LÓGICA ---

 // --- MÉTODOS DE LÓGICA ---

    private void configurarEventos() {
        btnGuardar.addActionListener(e -> guardarEmpleado());
        btnEliminar.addActionListener(e -> eliminarEmpleado()); // Conectamos el botón eliminar
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        // Detectar clics en la tabla
        tablaEmpleados.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tablaEmpleados.getSelectedRow() != -1) {
                llenarFormularioDesdeTabla();
            }
        });
    }

    private void llenarFormularioDesdeTabla() {
        int fila = tablaEmpleados.getSelectedRow();
        
        // Extraemos los datos de la fila seleccionada
        idEmpleadoSeleccionado = (Integer) modeloTabla.getValueAt(fila, 0);
        txtNombre.setText(modeloTabla.getValueAt(fila, 1).toString());
        txtDepartamento.setText(modeloTabla.getValueAt(fila, 2).toString());
        txtSalario.setText(modeloTabla.getValueAt(fila, 3).toString());
        txtFecha.setText(modeloTabla.getValueAt(fila, 4).toString());
        
        String estado = modeloTabla.getValueAt(fila, 5).toString();
        chkActivo.setSelected(estado.equals("Activo"));

        // Cambiamos el comportamiento de los botones
        btnGuardar.setText("Actualizar");
        btnEliminar.setEnabled(true);
    }

    private void cargarEmpleados() {
        modeloTabla.setRowCount(0); 
        try {
            java.util.List<Empleado> lista = empleadoDAO.ListarTodos();
            for (Empleado emp : lista) {
                Object[] fila = {
                    emp.getId(),
                    emp.getNombreCompleto(),
                    emp.getDepartamento(),
                    emp.getSalarioMensual(),
                    emp.getFechaContratacion(),
                    emp.isActivo() ? "Activo" : "Inactivo"
                };
                modeloTabla.addRow(fila); 
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar BD: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void guardarEmpleado() {
        String nombre = txtNombre.getText().trim();
        String depto = txtDepartamento.getText().trim();
        String salarioStr = txtSalario.getText().trim();
        String fechaStr = txtFecha.getText().trim();
        boolean activo = chkActivo.isSelected();

        // Validaciones...
        if (nombre.isEmpty() || depto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nombre y departamento no pueden quedar vacíos.", "Aviso", JOptionPane.WARNING_MESSAGE);
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

        Empleado empleado = new Empleado(nombre, depto, salario, fecha, activo);
        
        try {
            // Lógica para decidir si CREAR o ACTUALIZAR
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

        // Cuadro de confirmación obligatorio
        int respuesta = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar físicamente este registro?\nEsta acción no se puede deshacer.",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (respuesta == JOptionPane.YES_OPTION) {
            try {
                empleadoDAO.eliminar(idEmpleadoSeleccionado);
                JOptionPane.showMessageDialog(this, "Empleado eliminado del sistema.");
                limpiarFormulario();
                cargarEmpleados();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error BD", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void limpiarFormulario() {
        idEmpleadoSeleccionado = null; // Olvidamos al empleado
        txtNombre.setText("");
        txtDepartamento.setText("");
        txtSalario.setText("");
        txtFecha.setText("");
        chkActivo.setSelected(true);
        
        btnGuardar.setText("Guardar"); // Regresamos el botón a la normalidad
        btnEliminar.setEnabled(false); // Desactivamos eliminar
        tablaEmpleados.clearSelection(); // Quitamos la selección de la tabla
    }
    } 