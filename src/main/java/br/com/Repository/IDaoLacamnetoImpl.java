package br.com.Repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.Entidades.Lancamento;
import br.com.JPAUtil.JPAUtil;

public class IDaoLacamnetoImpl implements IDaoLancamento {

	@SuppressWarnings("unchecked")
	@Override
	public List<Lancamento> consulta(Long codigoUser) {
		List<Lancamento> lista = null;
		EntityManager entityManager = JPAUtil.getEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		lista = entityManager.createQuery("from Lancamento where usuario.id = " + codigoUser).getResultList();
		transaction.commit();
		entityManager.close();
		return lista;
	}

}
