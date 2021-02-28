package org.uniruse.repository;

import org.uniruse.data.contragents.Address;
import org.uniruse.data.contragents.Contragent;
import org.uniruse.data.contragents.Customer;
import org.uniruse.data.contragents.Shipper;
import org.uniruse.data.contragents.Supplier;
import org.uniruse.util.DBUtil;
import org.uniruse.util.enums.PharmaTypes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.uniruse.repository.TransactionsRepository.getAddressObjectFromResultset;
import static org.uniruse.util.StaticQueries.DELETE_CONTRAGENT;
import static org.uniruse.util.StaticQueries.INSERT_CUSTOMER;
import static org.uniruse.util.StaticQueries.INSERT_SHIPPER;
import static org.uniruse.util.StaticQueries.INSERT_SUPPLIER;
import static org.uniruse.util.StaticQueries.SELECT_ALL_CUSTOMERS;
import static org.uniruse.util.StaticQueries.SELECT_ALL_SHIPPERS;
import static org.uniruse.util.StaticQueries.SELECT_ALL_SUPPLIERS;
import static org.uniruse.util.StaticQueries.UPDATE_CUSTOMER;
import static org.uniruse.util.StaticQueries.UPDATE_SHIPPER;
import static org.uniruse.util.StaticQueries.UPDATE_SUPPLIER;

/**
 * @author ivelin.dimitrov
 */
public class ContragentsRepository {

    Connection conn;

    public ContragentsRepository(DBUtil dbUtil) {
        this.conn = dbUtil.getConnection();
    }

    public List<Customer> findAllCustomers() throws SQLException {
        try (PreparedStatement statement = conn.prepareStatement(SELECT_ALL_CUSTOMERS)) {
            return buildContragentCollectionFromResultSet(statement.executeQuery(), PharmaTypes.CUSTOMER).stream().map(it ->
                    (Customer) it
            ).collect(Collectors.toList());
        }
    }

    public List<Shipper> findAllShippers() throws SQLException {
        try (PreparedStatement statement = conn.prepareStatement(SELECT_ALL_SHIPPERS)) {
            return buildContragentCollectionFromResultSet(statement.executeQuery(), PharmaTypes.SHIPPER).stream().map(it ->
                    (Shipper) it
            ).collect(Collectors.toList());
        }
    }

    public List<Supplier> findAllSuppliers() throws SQLException {
        try (PreparedStatement statement = conn.prepareStatement(SELECT_ALL_SUPPLIERS)) {
            return buildContragentCollectionFromResultSet(statement.executeQuery(), PharmaTypes.SUPPLIER).stream().map(it ->
                    (Supplier) it
            ).collect(Collectors.toList());
        }
    }

    public Customer findCustomerById(long id) throws SQLException {
        String query = SELECT_ALL_CUSTOMERS + " AND c.id = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setLong(1, id);
            return (Customer) buildContragentCollectionFromResultSet(statement.executeQuery(), PharmaTypes.CUSTOMER).get(0);
        }
    }

    public Shipper findShipperById(long id) throws SQLException {
        String query = SELECT_ALL_SHIPPERS + " AND c.id = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setLong(1, id);
            return (Shipper) buildContragentCollectionFromResultSet(statement.executeQuery(), PharmaTypes.SHIPPER).get(0);
        }
    }

    public Supplier findSupplierById(long id) throws SQLException {
        String query = SELECT_ALL_SUPPLIERS + " AND c.id = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setLong(1, id);
            return (Supplier) buildContragentCollectionFromResultSet(statement.executeQuery(), PharmaTypes.SUPPLIER).get(0);
        }
    }

    public void insertCustomer(Customer customer) throws SQLException {
        PreparedStatement statement = conn.prepareStatement(INSERT_CUSTOMER);
        customer.decorateStatement(statement, true);
        System.out.println("Insert statement executed. Result present: " + statement.execute());
    }

    public void insertShipper(Shipper shipper) throws SQLException {
        PreparedStatement statement = conn.prepareStatement(INSERT_SHIPPER);
        shipper.decorateStatement(statement, true);
        System.out.println("Insert statement executed. Result present: " + statement.execute());
    }

    public void insertSupplier(Supplier supplier) throws SQLException {
        PreparedStatement statement = conn.prepareStatement(INSERT_SUPPLIER);
        supplier.decorateStatement(statement, true);
        System.out.println("Insert statement executed. Result present: " + statement.execute());
    }

    public void updateCustomer(Customer customer) throws SQLException {
        PreparedStatement statement = conn.prepareStatement(UPDATE_CUSTOMER);
        customer.decorateStatement(statement, false);
        System.out.println("Update statement executed. Result present: " + statement.execute());
    }

    public void updateShipper(Shipper shipper) throws SQLException {
        PreparedStatement statement = conn.prepareStatement(UPDATE_SHIPPER);
        shipper.decorateStatement(statement, false);
        System.out.println("Update statement executed. Result present: " + statement.execute());
    }

    public void updateSupplier(Supplier supplier) throws SQLException {
        PreparedStatement statement = conn.prepareStatement(UPDATE_SUPPLIER);
        supplier.decorateStatement(statement, false);
        System.out.println("Update statement executed. Result present: " + statement.execute());
    }

    public void deleteContragent(Contragent contragent) throws SQLException {
        try (PreparedStatement statement = conn.prepareStatement(DELETE_CONTRAGENT)) {
            statement.setLong(1, contragent.getId());
            System.out.println("Delete statement executed. Result present: " + statement.execute());
        }
    }

    /**
     * Set superclass fields for contragents
     *
     * @param resultset
     * @param contragent
     * @throws SQLException
     */
    private static void setContragentFields(ResultSet resultset, Contragent contragent) throws SQLException {

        String creditCard = resultset.getString("credit_card");
        contragent.setId(resultset.getInt("id"));
        contragent.setPhone(resultset.getString("phone"));
        contragent.setBankNum(resultset.getString("bank_account"));
        contragent.setEmail(resultset.getString("email"));
        contragent.setCreditCard(creditCard);
        Address address = getAddressObjectFromResultset(resultset);
        contragent.setAddress(address);
    }

    /**
     * Build list with {@link Contragent} from result set
     *
     * @param resultset
     * @param type
     * @return
     */
    private static List<Contragent> buildContragentCollectionFromResultSet(ResultSet resultset, PharmaTypes type) {
        List<Contragent> contragents = new ArrayList<>();
        try {
            while (resultset.next()) {
                Contragent contragent = null;
                switch (type) {
                    case CUSTOMER:
                        contragent = new Customer();
                        ((Customer) contragent).setFirstName(resultset.getString("first_name"));
                        ((Customer) contragent).setLastName(resultset.getString("last_name"));
                        break;
                    case SHIPPER:
                        contragent = new Shipper();
                        ((Shipper) contragent).setCompanyName(resultset.getString("company_name"));
                        ((Shipper) contragent).setVat(resultset.getString("vat"));
                        break;
                    case SUPPLIER:
                        contragent = new Supplier();
                        ((Supplier) contragent).setCompanyName(resultset.getString("company_name"));
                        ((Supplier) contragent).setVat(resultset.getString("vat"));
                        break;
                    default:
                        break;
                }
                if (contragent != null) {
                    setContragentFields(resultset, contragent);
                    contragents.add(contragent);
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contragents;
    }

}
