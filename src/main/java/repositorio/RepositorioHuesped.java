package repositorio;

import modelo.Huesped;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

public class RepositorioHuesped {

    private EntityManager entityManager;

    public RepositorioHuesped(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void guardar(Huesped huesped){
        entityManager.persist(huesped);
        /*entityManager.getTransaction().begin();
        entityManager.getTransaction().commit();
        entityManager.close();*/
    }

    public void actualizar(Huesped huesped){
        this.entityManager.merge(huesped);
    }


    public void eliminar(Huesped huesped){
        huesped = this.entityManager.merge(huesped);
        this.entityManager.remove(huesped);
    }

    public Huesped porID(Long id){
        return entityManager.find(Huesped.class, id);
    }

    public List<Huesped> findAll(){
        String jpql = "SELECT p FROM Huesped p";
        Query query = entityManager.createQuery(jpql, Huesped.class);
        return query.getResultList();
    }

    public List<Huesped> consultarPorParametrosConAPICriteria(String nombre, String apellido){
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Huesped> query = builder.createQuery(Huesped.class);
        Root<Huesped> from = query.from(Huesped.class);

        Predicate filtro = builder.and();
        if(nombre!=null && !nombre.trim().isEmpty()) {
            filtro=builder.and(filtro,builder.equal(from.get("nombre"), nombre));
        }

        if(apellido!=null && !apellido.trim().isEmpty()) {
            filtro=builder.and(filtro,builder.equal(from.get("apellido"), apellido));
        }

        query=query.where(filtro);
        return entityManager.createQuery(query).getResultList();
    }
}
