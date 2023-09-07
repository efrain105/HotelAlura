package repositorio;

import modelo.Huesped;
import modelo.Reserva;

import javax.persistence.EntityManager;
import java.sql.Connection;
import java.sql.SQLException;

public class RegistroReservaHuesped {

    private EntityManager entityManager;
    private RepositorioHuesped repositorioHuesped;
    private RepositorioReserva repositorioReserva;

    public RegistroReservaHuesped(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.repositorioHuesped = new RepositorioHuesped(entityManager);
        this.repositorioReserva = new RepositorioReserva(entityManager);
    }

    public void guardar(Reserva r, Huesped h) throws SQLException {
        repositorioHuesped.guardar(h);
        repositorioReserva.guardar(r);
    }

}
