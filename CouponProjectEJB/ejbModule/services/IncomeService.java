package services;

import java.util.Collection;
import java.util.List;

import javax.ejb.Remote;

import db.Income;

@Remote
public interface IncomeService {
	public void storeIncome(Income income);
	public Collection<Income> viewAllIncome();
	public Collection<Income> viewIncomeByCustomer(String customerID);
	public Collection<Income> viewIncomeByCompany(String companyID);
	public void removeAllIncomes();
}
