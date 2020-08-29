package br.com.Dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.Entidades.Pessoa;
import br.com.JPAUtil.JPAUtil;

public class DaoGeneric<E>  {
	
	public void salvar(E entidade) {
		EntityManager entityManager = JPAUtil.getEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		entityManager.persist(entidade);
		transaction.commit();
		entityManager.close();
	}
	
	public E salvar2(E entidade) {
		EntityManager entityManager = JPAUtil.getEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		E retono = entityManager.merge(entidade);
		transaction.commit();
		entityManager.close();
		return retono; 
	}
	
	public void Deletar(E entidade) {
		EntityManager entityManager = JPAUtil.getEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		entityManager.remove(entidade);
		transaction.commit();
		entityManager.close();
	}
	public void DeletarPorId(E entidade) {
		EntityManager entityManager = JPAUtil.getEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		Object id = JPAUtil.getPrimaryKey(entidade);
		entityManager.createQuery("delete from " + entidade.getClass().getName() + " where id = " + id).executeUpdate();
		transaction.commit();
		entityManager.close();
	}
	
	@SuppressWarnings("unchecked")
	public List<E> getLista(Class<E> entidade){
		EntityManager entityManager = JPAUtil.getEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		List<E> retorno = (List<E>) entityManager.createQuery("from " + entidade.getName()).getResultList();
		
		transaction.commit();
		entityManager.close();
		return retorno;
	}
	
	@SuppressWarnings("unchecked")
	public E consultar(Class<E> entidade, String codigo) {
		EntityManager entityManager = JPAUtil.getEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		E objeto = (E) entityManager.find(entidade, Long.parseLong(codigo));
		transaction.commit();
		return objeto;
	}
	
	
	
	
	
}
