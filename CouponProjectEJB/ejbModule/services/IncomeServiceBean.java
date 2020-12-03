package services;

import java.util.Collection;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import db.Description;
import db.Income;

@Stateless(name="IncomeService")
public class IncomeServiceBean implements IncomeService {
	
	@PersistenceContext(unitName="couponSystem") 
	private EntityManager manager;

	public void storeIncome(Income income) {
		manager.persist(income);
	}

	public Collection<Income> viewAllIncome() {
		Query query = manager.createNamedQuery("getAllIncome");
		List<Income> incomes = query.getResultList();
		return incomes;
	}

	public Collection<Income> viewIncomeByCustomer(String customerName) {
		Query query = manager.createNamedQuery("getAllIncomeByCustomer");
		query.setParameter("name", customerName);
		query.setParameter("des1", Description.CUSTOMER_PURCHASE);
		List<Income> incomes = query.getResultList();		
		return incomes;
	}
	
	public Collection<Income> viewIncomeByCompany(String companyName) {
		Query query = manager.createNamedQuery("getAllIncomeByCompany");
		query.setParameter("name", companyName);
		query.setParameter("des1", Description.COMPANY_NEW_COUPON);
		query.setParameter("des2", Description.COMPANY_UPDATE_COUPON);
		List<Income> incomes = query.getResultList();		
		return incomes;
	}
	
	public void removeAllIncomes() {
		Query query = manager.createNamedQuery("getAllIncome");
		List<Income> incomes = query.getResultList();		
		for (Income income: incomes)  {
			manager.remove(income);;
		}
	}
	

}
