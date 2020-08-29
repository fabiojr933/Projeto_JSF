package br.com.Repository;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.Entidades.Estados;
import br.com.Entidades.Pessoa;
import br.com.JPAUtil.JPAUtil;

public class IDaoPessoaImpl implements IDaoPessoa {

	@Override
	public Pessoa consultarUsuario(String Login, String Senha) {
		Pessoa pessoa = null;
		 
		 EntityManager entityManager = JPAUtil.getEntityManager();
		 EntityTransaction transaction = entityManager.getTransaction();
		 transaction.begin();
		 
		 pessoa = (Pessoa) entityManager.createQuery("select p from Pessoa p where p.login = '" + Login + "'and p.senha = '" + Senha + "'").getSingleResult();
		 
		 transaction.commit();
		 entityManager.close();
		 return pessoa;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SelectItem> listaEstados() {
		List<SelectItem> selectItems = new ArrayList<SelectItem>();
		EntityManager entityManager = JPAUtil.getEntityManager();
		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();
		List<Estados> estados = entityManager.createQuery("from Estados").getResultList();
		for (Estados estados2 : estados) {
			selectItems.add(new SelectItem(estados2, estados2.getNome()));
		}
		return selectItems;
	}


}
