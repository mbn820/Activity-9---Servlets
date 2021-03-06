package com.exist.ecc.core.dao;

import java.util.List;
import com.exist.ecc.core.model.Person;
import com.exist.ecc.core.model.Role;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Order;

public class PersonDao implements PersonDaoInterface {

	public Integer addPerson(Person person) {
		return (Integer) new HibernateUtil().transact(session -> session.save(person));
	}

	public Person getPerson(int id) {
		return (Person) new HibernateUtil().transact(session -> session.get(Person.class, id));
	}

	public List<Person> getAllPerson(String orderBy) {
		return (List<Person>) new HibernateUtil().transact(session ->
		 	session.createCriteria(Person.class)
				   .addOrder( Order.asc(orderBy) )
				   .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				   .list()
		);
	}

	public List<Person> getPersonsByLastName(String lastName) {
		return (List<Person>) new HibernateUtil().transact(session ->
			session.createCriteria(Person.class)
			       .add( Restrictions.ilike("name.lastName", lastName + "%") )
				   .addOrder( Order.asc("name.lastName") )
				   .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				   .list()
	    );
	}

	public List<Person> getPersonsByLastName(String lastName, String orderBy, String orderType) {
		Criterion filterByLastName = Restrictions.ilike("name.lastName", lastName + "%");
		Order order = ( orderType.equals("desc") ) ? Order.desc(orderBy) : Order.asc(orderBy);

		return (List<Person>) new HibernateUtil().transact(session ->
			session.createCriteria(Person.class)
				   .add(filterByLastName)
				   .addOrder(order)
				   .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				   .list()
		);
	}

	public void updatePerson(Person person) {
		new HibernateUtil().transact(session -> { session.update(person); return null; });
	}

	public void deletePerson(int id) {
		new HibernateUtil().transact( session -> { session.delete(getPerson(id)); return null; } );
	}

	public void deleteAllRecords() {
		List<Person> allPersons = getAllPerson("id");
		allPersons.forEach(person -> deletePerson(person.getId()));
	}
}
