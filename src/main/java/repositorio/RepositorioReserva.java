package repositorio;

import modelo.Reserva;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class RepositorioReserva {

    private EntityManager entityManager;

    public RepositorioReserva(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    public void guardar(Reserva reserva){
        entityManager.persist(reserva);
        /*entityManager.getTransaction().begin();
        entityManager.getTransaction().commit();
        entityManager.close();*/
    }

    public void actualizar(Reserva reserva){
        this.entityManager.merge(reserva);
    }

    public void eliminar(Reserva reserva){
        reserva = this.entityManager.merge(reserva);
        this.entityManager.remove(reserva);
    }


    public Reserva porID(Long id){
        return entityManager.find(Reserva.class, id);
    }

    public List<Reserva> findAll(){
        String jpql = "SELECT r FROM Reserva r";
        Query query = entityManager.createQuery(jpql, Reserva.class);
        return query.getResultList();
    }

}
