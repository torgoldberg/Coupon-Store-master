package db;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


@NamedQueries({
	@NamedQuery(name="getAllIncome",query="SELECT b FROM Income b "),
	@NamedQuery(name="getAllIncomeByCustomer",query="SELECT b FROM Income b WHERE b.name = :name AND b.description = :des1"),
	@NamedQuery(name="getAllIncomeByCompany",query="SELECT b FROM Income b WHERE b.name = :name AND b.description IN (:des1, :des2)")
})

@Entity
@Table(name="INCOME")
public class Income implements Serializable{
		
	private long id;
	private String name;
	private Date date;
	private double amount;
	private Description description;
	
	
	
	public Income(String name, Date date, double amount, Description description) {
		super();
		this.name = name;
		this.date = date;
		this.amount = amount;
		this.description = description;
	}
	
	
	public Income() {	}


	@Id
	@GeneratedValue
	@Column(name="ID", nullable=false, columnDefinition="integer")
	public long getId() {return id;	}
	public void setId(long id) {
		this.id = id;
	}
	
	@Column(name="DATE", nullable=false)
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
		
	@Column(name="DESCRPTION", nullable=false)
	public Description getDescription() {return description;}
	public void setDescription(Description dept) {this.description = dept;	}
	
	@Column(name="NAME", nullable=false)
	public String getName() {return name;}
	public void setName(String name) {this.name = name;}	
	
	@Column(name="AMOUNT", nullable=false, columnDefinition="double")
	public double getAmount() {return amount;}
	public void setAmount(double amount) {this.amount = amount;}
	
	
	

	@Override
	
	public String toString() {
		return "Income [id=" + id + ", nameID=" + name + ", date=" + date + ", amount=" + amount + ", description="
				+ description + "]";
	}
	
}

