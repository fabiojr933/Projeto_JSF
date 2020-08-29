package br.com.converter;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.Entidades.Estados;
import br.com.JPAUtil.JPAUtil;

@SuppressWarnings("serial")
@FacesConverter(forClass = Estados.class, value = "converterEstado")
public class EstadoConverter implements Converter, Serializable {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String codigoEstado) {
		// TODO Auto-generated method stub

		EntityManager entityManager = JPAUtil.getEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		Estados estado = (Estados) entityManager.find(Estados.class, Long.parseLong(codigoEstado));
		return estado;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object estado) {
		// TODO Auto-generated method stub
		if (estado == null) {
			return null;
		}
		if (estado instanceof Estados) {
			return ((Estados) estado).getId().toString();
		}else {
			return estado.toString();
		}
	}

}
