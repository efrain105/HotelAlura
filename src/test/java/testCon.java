import util.JPAUtils;

import javax.persistence.EntityManager;

public class testCon {
    public static void main(String[] args) {
        EntityManager entityManager = JPAUtils.getEntityManager();
    }
}
