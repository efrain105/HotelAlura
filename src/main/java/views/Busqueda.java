package views;

import modelo.Huesped;
import modelo.Reserva;
import repositorio.RepositorioHuesped;
import repositorio.RepositorioReserva;
import util.JPAUtils;

import java.awt.EventQueue;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("serial")
public class Busqueda extends JFrame {

    private JPanel contentPane;
    private JTextField txtBuscar;
    private JTable tbHuespedes;
    private JTable tbReservas;
    private DefaultTableModel modelo;
    private DefaultTableModel modeloHuesped;
    private JLabel labelAtras;
    private JLabel labelExit;
    int xMouse, yMouse;

    EntityManager em = JPAUtils.getEntityManager();
    private RepositorioReserva repositorioReserva = new RepositorioReserva(em);
    private RepositorioHuesped repositorioHuesped = new RepositorioHuesped(em);

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Busqueda frame = new Busqueda();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public Busqueda() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(Busqueda.class.getResource("/imagenes/lupa2.png")));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 910, 571);
        contentPane = new JPanel();
        contentPane.setBackground(Color.WHITE);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        setLocationRelativeTo(null);
        setUndecorated(true);

        txtBuscar = new JTextField();
        txtBuscar.setBounds(536, 127, 193, 31);
        txtBuscar.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        contentPane.add(txtBuscar);
        txtBuscar.setColumns(10);


        JLabel lblNewLabel_4 = new JLabel("SISTEMA DE BÚSQUEDA");
        lblNewLabel_4.setForeground(new Color(12, 138, 199));
        lblNewLabel_4.setFont(new Font("Roboto Black", Font.BOLD, 24));
        lblNewLabel_4.setBounds(331, 62, 280, 42);
        contentPane.add(lblNewLabel_4);

        JTabbedPane panel = new JTabbedPane(JTabbedPane.TOP);
        panel.setBackground(new Color(12, 138, 199));
        panel.setFont(new Font("Roboto", Font.PLAIN, 16));
        panel.setBounds(20, 169, 865, 328);
        contentPane.add(panel);


        tbReservas = new JTable();
        tbReservas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tbReservas.setFont(new Font("Roboto", Font.PLAIN, 16));
        modelo = (DefaultTableModel) tbReservas.getModel();
        modelo.addColumn("Numero de Reserva");
        modelo.addColumn("Fecha Check In");
        modelo.addColumn("Fecha Check Out");
        modelo.addColumn("Valor");
        modelo.addColumn("Forma de Pago");
        JScrollPane scroll_table = new JScrollPane(tbReservas);
        panel.addTab("Reservas", new ImageIcon(Busqueda.class.getResource("/imagenes/reservado.png")), scroll_table, null);
        scroll_table.setVisible(true);

        cargarTablaReserva();


        tbHuespedes = new JTable();
        tbHuespedes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tbHuespedes.setFont(new Font("Roboto", Font.PLAIN, 16));
        modeloHuesped = (DefaultTableModel) tbHuespedes.getModel();
        modeloHuesped.addColumn("Número de Huesped");
        modeloHuesped.addColumn("Nombre");
        modeloHuesped.addColumn("Apellido");
        modeloHuesped.addColumn("Fecha de Nacimiento");
        modeloHuesped.addColumn("Nacionalidad");
        modeloHuesped.addColumn("Telefono");
        modeloHuesped.addColumn("Número de Reserva");
        JScrollPane scroll_tableHuespedes = new JScrollPane(tbHuespedes);
        panel.addTab("Huéspedes", new ImageIcon(Busqueda.class.getResource("/imagenes/pessoas.png")), scroll_tableHuespedes, null);
        scroll_tableHuespedes.setVisible(true);

        cargarTablaHuesped();


        JLabel lblNewLabel_2 = new JLabel("");
        lblNewLabel_2.setIcon(new ImageIcon(Busqueda.class.getResource("/imagenes/Ha-100px.png")));
        lblNewLabel_2.setBounds(56, 51, 104, 107);
        contentPane.add(lblNewLabel_2);

        JPanel header = new JPanel();
        header.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                headerMouseDragged(e);

            }
        });
        header.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                headerMousePressed(e);
            }
        });
        header.setLayout(null);
        header.setBackground(Color.WHITE);
        header.setBounds(0, 0, 910, 36);
        contentPane.add(header);

        JPanel btnAtras = new JPanel();
        btnAtras.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                MenuUsuario usuario = new MenuUsuario();
                usuario.setVisible(true);
                dispose();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                btnAtras.setBackground(new Color(12, 138, 199));
                labelAtras.setForeground(Color.white);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnAtras.setBackground(Color.white);
                labelAtras.setForeground(Color.black);
            }
        });
        btnAtras.setLayout(null);
        btnAtras.setBackground(Color.WHITE);
        btnAtras.setBounds(0, 0, 53, 36);
        header.add(btnAtras);

        labelAtras = new JLabel("<");
        labelAtras.setHorizontalAlignment(SwingConstants.CENTER);
        labelAtras.setFont(new Font("Roboto", Font.PLAIN, 23));
        labelAtras.setBounds(0, 0, 53, 36);
        btnAtras.add(labelAtras);

        JPanel btnexit = new JPanel();
        btnexit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                MenuUsuario usuario = new MenuUsuario();
                usuario.setVisible(true);
                dispose();
            }

            @Override
            public void mouseEntered(MouseEvent e) { //Al usuario pasar el mouse por el botón este cambiará de color
                btnexit.setBackground(Color.red);
                labelExit.setForeground(Color.white);
            }

            @Override
            public void mouseExited(MouseEvent e) { //Al usuario quitar el mouse por el botón este volverá al estado original
                btnexit.setBackground(Color.white);
                labelExit.setForeground(Color.black);
            }
        });
        btnexit.setLayout(null);
        btnexit.setBackground(Color.WHITE);
        btnexit.setBounds(857, 0, 53, 36);
        header.add(btnexit);

        labelExit = new JLabel("X");
        labelExit.setHorizontalAlignment(SwingConstants.CENTER);
        labelExit.setForeground(Color.BLACK);
        labelExit.setFont(new Font("Roboto", Font.PLAIN, 18));
        labelExit.setBounds(0, 0, 53, 36);
        btnexit.add(labelExit);

        JSeparator separator_1_2 = new JSeparator();
        separator_1_2.setForeground(new Color(12, 138, 199));
        separator_1_2.setBackground(new Color(12, 138, 199));
        separator_1_2.setBounds(539, 159, 193, 2);
        contentPane.add(separator_1_2);

        JPanel btnbuscar = new JPanel();
        btnbuscar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                //List<Huesped> huespeds = repositorioHuesped.consultarPorParametrosConAPICriteria("efrain", "cardona");

                List<Huesped> allHuesped = repositorioHuesped.findAll();
                Stream<Huesped> huespedStream = allHuesped.stream().filter(huesped -> huesped.toString().contains(txtBuscar.getText()));
                List<Huesped> a = huespedStream.collect(Collectors.toList());
                List<Reserva> allReserva = repositorioReserva.findAll();
                Stream<Reserva> reservaStream = allReserva.stream().filter(reserva -> reserva.getHuesped().toString().contains(txtBuscar.getText()));
                List<Reserva> b = reservaStream.collect(Collectors.toList());

                if (!a.isEmpty()){
                    modeloHuesped.getDataVector().clear();
                    a.forEach(huespedes -> {
                        Object[] fila = {huespedes.getId(), huespedes.getNombre(), huespedes.getApellido(), huespedes.getFechaDeNacimiento(), huespedes.getNacionalidad(),
                                huespedes.getTelefono(), (huespedes.getReserva() != null) ? huespedes.getReserva().getId() : null};
                        modeloHuesped.addRow(fila);
                    });
                    modelo.getDataVector().clear();
                    b.forEach(reservas -> {
                        Object[] fila = {reservas.getId(), reservas.getFechaCheckIn(), reservas.getFechaCheckOut(), reservas.getValor(), reservas.getFormaDePago()};
                        modelo.addRow(fila);
                    });
                }else {
                    modeloHuesped.getDataVector().clear();
                    modelo.getDataVector().clear();
                    cargarTablaHuesped();
                    cargarTablaReserva();
                    JOptionPane.showMessageDialog(null,"no se encontraron resultados");
                }


                /*List<Reserva> allReserva = repositorioReserva.findAll();
                Stream<Reserva> reservaStream = allReserva.stream().filter(reserva -> reserva.getHuesped().toString().contains(txtBuscar.getText()));
                List<Reserva> b = reservaStream.collect(Collectors.toList());

                if (!b.isEmpty()){
                    modelo.getDataVector().clear();
                    b.forEach(reservas -> {
                        Object[] fila = {reservas.getId(), reservas.getFechaCheckIn(), reservas.getFechaCheckOut(), reservas.getValor(), reservas.getFormaDePago()};
                        modelo.addRow(fila);
                    });
                }else {
                    modeloHuesped.getDataVector().clear();
                    cargarTablaReserva();
                    JOptionPane.showMessageDialog(null,"no se encontraron resultados");
                }*/


            }
        });
        btnbuscar.setLayout(null);
        btnbuscar.setBackground(new Color(12, 138, 199));
        btnbuscar.setBounds(748, 125, 122, 35);
        btnbuscar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        contentPane.add(btnbuscar);

        JLabel lblBuscar = new JLabel("BUSCAR");
        lblBuscar.setBounds(0, 0, 122, 35);
        btnbuscar.add(lblBuscar);
        lblBuscar.setHorizontalAlignment(SwingConstants.CENTER);
        lblBuscar.setForeground(Color.WHITE);
        lblBuscar.setFont(new Font("Roboto", Font.PLAIN, 18));

        JPanel btnEditar = new JPanel();
        btnEditar.setLayout(null);
        btnEditar.setBackground(new Color(12, 138, 199));
        btnEditar.setBounds(635, 508, 122, 35);
        btnEditar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        contentPane.add(btnEditar);

        JLabel lblEditar = new JLabel("EDITAR");
        lblEditar.setHorizontalAlignment(SwingConstants.CENTER);
        lblEditar.setForeground(Color.WHITE);
        lblEditar.setFont(new Font("Roboto", Font.PLAIN, 18));
        lblEditar.setBounds(0, 0, 122, 35);
        btnEditar.add(lblEditar);


        btnEditar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (tieneFilaElegidaHuesped() && tieneFilaElegidaReserva()) {
                    JOptionPane.showMessageDialog(e.getComponent(), "Por favor, elije un item");
                    return;
                }
                if (!tieneFilaElegidaHuesped()) {
                    modificar();
                } else if (!tieneFilaElegidaReserva()) {
                    modificarReserva();
                }

            }
        });

        JPanel btnEliminar = new JPanel();
        btnEliminar.setLayout(null);
        btnEliminar.setBackground(new Color(12, 138, 199));
        btnEliminar.setBounds(767, 508, 122, 35);
        btnEliminar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        contentPane.add(btnEliminar);

        btnEliminar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                eliminar();
            }
        });

        JLabel lblEliminar = new JLabel("ELIMINAR");
        lblEliminar.setHorizontalAlignment(SwingConstants.CENTER);
        lblEliminar.setForeground(Color.WHITE);
        lblEliminar.setFont(new Font("Roboto", Font.PLAIN, 18));
        lblEliminar.setBounds(0, 0, 122, 35);
        btnEliminar.add(lblEliminar);
        setResizable(false);
    }

    private void cargarTablaHuesped() {
        List<Huesped> huesped = this.repositorioHuesped.findAll();

        huesped.forEach(huespedes -> {
            Object[] fila = {huespedes.getId(), huespedes.getNombre(), huespedes.getApellido(), huespedes.getFechaDeNacimiento(), huespedes.getNacionalidad(),
                    huespedes.getTelefono(), (huespedes.getReserva() != null) ? huespedes.getReserva().getId() : null};
            modeloHuesped.addRow(fila);
        });
    }

    private void eliminar() {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        try {
            if (!tieneFilaElegidaHuesped()) {
                Optional.ofNullable(modeloHuesped.getValueAt(tbHuespedes.getSelectedRow(), tbHuespedes.getSelectedColumn()))
                        .ifPresentOrElse(fila -> {
                            Huesped huesped = em.find(Huesped.class, modeloHuesped.getValueAt(tbHuespedes.getSelectedRow(), 0));
                            if (huesped != null) {
                                repositorioHuesped.eliminar(huesped);
                                transaction.commit();
                            }
                        }, () -> {
                            throw new RuntimeException("Ocurrió un error");
                        });
            } else if (!tieneFilaElegidaReserva()){
                Optional.ofNullable(modelo.getValueAt(tbReservas.getSelectedRow(), tbReservas.getSelectedColumn()))
                        .ifPresentOrElse(fila -> {
                            Reserva reserva = em.find(Reserva.class, modelo.getValueAt(tbReservas.getSelectedRow(), 0));
                            if (reserva != null) {
                                repositorioReserva.eliminar(reserva);
                                transaction.commit();
                            }
                        }, () -> {
                            throw new RuntimeException("Ocurrió un error");
                        });
            }else {
                JOptionPane.showMessageDialog(this,"Error");
                throw new RuntimeException("Ocurrió un error");

            }
            Exito exito = new Exito();
            exito.setVisible(true);
            em.close();
            dispose();
        }catch(Exception exception){
                if (transaction != null && transaction.isActive()) {
                    transaction.rollback();
                }
                exception.printStackTrace();
            }
    }



    private void cargarTablaReserva() {
        List<Reserva> reserva = this.repositorioReserva.findAll();

        reserva.forEach(reservas -> {
            Object[] fila = {reservas.getId(), reservas.getFechaCheckIn(), reservas.getFechaCheckOut(), reservas.getValor(), reservas.getFormaDePago()};
            modelo.addRow(fila);
        });
    }

    private boolean tieneFilaElegidaHuesped() {
        return tbHuespedes.getSelectedRowCount() == 0 || tbHuespedes.getSelectedColumnCount() == 0;
    }

    private boolean tieneFilaElegidaReserva() {
        return tbReservas.getSelectedRowCount() == 0 || tbReservas.getSelectedColumnCount() == 0;
    }



    private void modificar() {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            // Realizar las operaciones con la base de datos
            Optional.ofNullable(modeloHuesped.getValueAt(tbHuespedes.getSelectedRow(), tbHuespedes.getSelectedColumn()))
                    .ifPresentOrElse(fila -> {
                        String nombre = (String) modeloHuesped.getValueAt(tbHuespedes.getSelectedRow(), 1);
                        String apellido = (String) modeloHuesped.getValueAt(tbHuespedes.getSelectedRow(), 2);
                        Date fechaNa = (Date) modeloHuesped.getValueAt(tbHuespedes.getSelectedRow(), 3);
                        String nacionalidad = (String) modeloHuesped.getValueAt(tbHuespedes.getSelectedRow(), 4);
                        BigDecimal telefono = (BigDecimal) modeloHuesped.getValueAt(tbHuespedes.getSelectedRow(), 5);

                        Huesped huesped = em.find(Huesped.class, modeloHuesped.getValueAt(tbHuespedes.getSelectedRow(), 0));
                        if (huesped != null) {
                            huesped.setNombre(nombre);
                            huesped.setApellido(apellido);
                            huesped.setFechaDeNacimiento(fechaNa);
                            huesped.setNacionalidad(nacionalidad);
                            huesped.setTelefono(telefono);
                        }
                        repositorioHuesped.actualizar(huesped);
                        transaction.commit();
                        Exito exito = new Exito();
                        exito.setVisible(true);


                    }, () -> JOptionPane.showMessageDialog(this, "Por favor, elije un item"));
        }catch (Exception exception){
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            exception.printStackTrace();
        } finally {
            em.close();
            dispose();
        }
    }





    private void modificarReserva() {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            // Realizar las operaciones con la base de datos
            Optional.ofNullable(modelo.getValueAt(tbReservas.getSelectedRow(), tbReservas.getSelectedColumn()))
                    .ifPresentOrElse(fila -> {
                        Date fechaIn = (Date) modelo.getValueAt(tbReservas.getSelectedRow(), 1);
                        Date fechaOut = (Date) modelo.getValueAt(tbReservas.getSelectedRow(), 2);
                        BigDecimal valor = new BigDecimal(String.valueOf(modelo.getValueAt(tbReservas.getSelectedRow(), 3)));
                        String formaPago = (String) modelo.getValueAt(tbReservas.getSelectedRow(), 4);
                        Reserva reserva = em.find(Reserva.class, modelo.getValueAt(tbReservas.getSelectedRow(), 0));
                        if (reserva != null){
                            reserva.setFechaCheckIn(fechaIn);
                            reserva.setFechaCheckOut(fechaOut);
                            reserva.setValor(valor);
                            reserva.setFormaDePago(formaPago);
                        }
                        repositorioReserva.actualizar(reserva);
                        transaction.commit();
                        Exito exito = new Exito();
                        exito.setVisible(true);

                    }, () -> JOptionPane.showMessageDialog(this, "Por favor, elije un item"));

        }catch (Exception exception){
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            exception.printStackTrace();
        } finally {
            em.close();
            dispose();
        }
    }



    //Código que permite mover la ventana por la pantalla según la posición de "x" y "y"
    private void headerMousePressed(MouseEvent evt) {
        xMouse = evt.getX();
        yMouse = evt.getY();
    }

    private void headerMouseDragged(MouseEvent evt) {
        int x = evt.getXOnScreen();
        int y = evt.getYOnScreen();
        this.setLocation(x - xMouse, y - yMouse);
    }
}
