package org.uniruse.data.item;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author ivelin.dimitrov
 */
public class Medicine extends Item {
	private int resipeRequired;
	private int minAge;
	private String contraindications;

	public Medicine() {
	}

	public Medicine(String sku, String description, double price, double salePrice, double deleted, int resipeRequired,
			int minAge, String contraindications) {

		super(sku, description, price, salePrice, deleted);
		this.resipeRequired = resipeRequired;
		this.minAge = minAge;
		this.contraindications = contraindications;
	}

	public int getResipeRequired() {
		return resipeRequired;
	}

	public void setResipeRequired(int resipeRequired) {
		this.resipeRequired = resipeRequired;
	}

	public int getMinAge() {
		return minAge;
	}

	public void setMinAge(int minAge) {
		this.minAge = minAge;
	}

	public String getContraindications() {
		return contraindications;
	}

	public void setContraindications(String contraindications) {
		this.contraindications = contraindications;
	}

	@Override
	public String toString() {
		return "Medicine{ " + super.toString() + "Medicine [resipeRequired=" + resipeRequired + ", minAge=" + minAge
				+ ", contraindications=" + contraindications + "]";
	}

	public void decorateStatement(PreparedStatement statement, boolean create) throws SQLException {
		super.decorateStatement(statement);
		statement.setInt(index.getAndIncrement(), getResipeRequired());
		statement.setInt(index.getAndIncrement(), getMinAge());
		statement.setString(index.getAndIncrement(), getContraindications());

		if (!create) { // on update
			statement.setString(index.getAndIncrement(), getSku());
		}
	}
}
