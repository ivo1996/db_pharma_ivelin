package org.uniruse.data.contragents;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author ivelin.dimitrov
 */
public class Customer extends Contragent {
    private String firstName;
    private String lastName;

    public Customer() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Customer(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Customer(Integer id, String phone, String email, String bankNum, String creditCard, Address address,
                    String firstName, String lastName) {
        super(id, phone, email, bankNum, creditCard, address);
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "Customer{" + super.toString() + "firstName='" + firstName + '\'' + ", lastName='" + lastName + '\''
                + '}';
    }

    public void decorateStatement(PreparedStatement preparedStatement, boolean create) throws SQLException {
        super.decorateStatement(preparedStatement, create);
        preparedStatement.setString(index.getAndIncrement(), firstName);
        preparedStatement.setString(index.getAndIncrement(), lastName);

        if (!create) {
            preparedStatement.setLong(index.getAndIncrement(), getId());
        }
    }
}
