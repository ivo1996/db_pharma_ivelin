package org.uniruse.data.contragents;

import org.uniruse.data.AtomicIndex;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author ivelin.dimitrov
 */
public class Contragent extends AtomicIndex {
    private long id;
    private String phone;
    private String email;
    private String bankNum;
    private String creditCard;
    private Address address;

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Contragent() {
    }

    public Contragent(Integer id, String phone, String email, String bankNum, String creditCard, Address address) {
        this.id = id;
        this.phone = phone;
        this.email = email;
        this.bankNum = bankNum;
        this.creditCard = creditCard;
        this.address = address;
    }

    public String getCity() {
        return this.address.getCity();
    }

    public String getStreet() {
        return this.address.getStreet();
    }

    public String getZipCode() {
        return this.address.getZipCode();
    }

    public String getProvince() {
        return this.address.getProvince();
    }


    public void setCity(String city) {
        this.address.setCity(city);
    }

    public void setStreet(String street) {
        this.address.setStreet(street);
    }

    public void setZipCode(String zipCode) {
        this.address.setZipCode(zipCode);
    }

    public void setProvince(String province) {
        this.address.setProvince(province);
    }

    public long getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBankNum() {
        return bankNum;
    }

    public void setBankNum(String bankNum) {
        this.bankNum = bankNum;
    }

    public String getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(String creditCard) {
        this.creditCard = creditCard;
    }

    @Override
    public String toString() {
        return "id='" + id + '\'' + "phone='" + phone + '\'' + ", email='" + email + '\'' + ", bankNum='" + bankNum
                + '\'' + ", creditCard='" + creditCard + '\'' + ", address=" + address;
    }

    protected void decorateStatement(PreparedStatement statement, boolean create) throws SQLException {
        if (!create) {
            statement.setLong(index.getAndIncrement(), id);
        }
        statement.setString(index.getAndIncrement(), phone);
        statement.setString(index.getAndIncrement(), email);
        statement.setString(index.getAndIncrement(), bankNum);
        statement.setString(index.getAndIncrement(), creditCard);
        statement.setString(index.getAndIncrement(), address.getStreet());
        statement.setString(index.getAndIncrement(), address.getCity());
        statement.setString(index.getAndIncrement(), address.getProvince());
        statement.setString(index.getAndIncrement(), address.getZipCode());
    }
}
