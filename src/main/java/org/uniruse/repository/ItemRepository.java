package org.uniruse.repository;

import org.uniruse.data.item.Item;
import org.uniruse.data.item.Medicine;
import org.uniruse.data.item.Supplement;
import org.uniruse.util.DBUtil;
import org.uniruse.util.enums.PharmaTypes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.uniruse.util.StaticQueries.DELETE_ITEM;
import static org.uniruse.util.StaticQueries.INSERT_MEDICINE;
import static org.uniruse.util.StaticQueries.INSERT_SUPPLEMENT;
import static org.uniruse.util.StaticQueries.SELECT_ALL_MEDICINES;
import static org.uniruse.util.StaticQueries.SELECT_ALL_SUPPLEMENTS;
import static org.uniruse.util.StaticQueries.UPDATE_MEDICINE;
import static org.uniruse.util.StaticQueries.UPDATE_SUPPLEMENT;


/**
 * @author ivelin.dimitrov
 */
public class ItemRepository {

    Connection conn;

    public ItemRepository(DBUtil dbUtil) {
        this.conn = dbUtil.getConnection();
    }

    public List<Medicine> findAllMedicine() throws SQLException {
        try (PreparedStatement statement = conn.prepareStatement(SELECT_ALL_MEDICINES)) {
            return buildItemCollectionFromResultSet(statement.executeQuery(), PharmaTypes.MEDICINE).stream().map(it ->
                    (Medicine) it
            ).collect(Collectors.toList());
        }
    }

    public List<Supplement> findAllSupplement() throws SQLException {
        try (PreparedStatement statement = conn.prepareStatement(SELECT_ALL_SUPPLEMENTS)) {
            return buildItemCollectionFromResultSet(statement.executeQuery(), PharmaTypes.SUPPLEMENT).stream().map(it ->
                    (Supplement) it
            ).collect(Collectors.toList());
        }
    }

    public Medicine findMedicineById(String sku) throws SQLException {
        String query = SELECT_ALL_MEDICINES + " AND c.SKU = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, sku);
            return (Medicine) buildItemCollectionFromResultSet(statement.executeQuery(), PharmaTypes.MEDICINE).get(0);
        }
    }

    public Supplement findSupplementById(String sku) throws SQLException {
        String query = SELECT_ALL_SUPPLEMENTS + " AND c.SKU =  ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, sku);
            return (Supplement) buildItemCollectionFromResultSet(statement.executeQuery(), PharmaTypes.SUPPLEMENT).get(0);
        }
    }

    public void insertMedicine(Medicine medicine) throws SQLException {
        PreparedStatement statement = conn.prepareStatement(INSERT_MEDICINE);
        medicine.decorateStatement(statement, true);
        System.out.println("Insert statement executed. Result present: " + statement.execute());
    }

    public void insertSupplement(Supplement supplement) throws SQLException {
        PreparedStatement statement = conn.prepareStatement(INSERT_SUPPLEMENT);
        supplement.decorateStatement(statement, true);
        System.out.println("Insert statement executed. Result present: " + statement.execute());
    }

    public void updateMedicine(Medicine medicine) throws SQLException {
        PreparedStatement statement = conn.prepareStatement(UPDATE_MEDICINE);
        medicine.decorateStatement(statement, false);
        System.out.println("Update statement executed. Result present: " + statement.execute());
    }

    public void updateSupplement(Supplement supplement) throws SQLException {
        PreparedStatement statement = conn.prepareStatement(UPDATE_SUPPLEMENT);
        supplement.decorateStatement(statement, false);
        System.out.println("Update statement executed. Result present: " + statement.execute());
    }

    public void deleteItem(String sku) throws SQLException {
        try (PreparedStatement statement = conn.prepareStatement(DELETE_ITEM)) {
            statement.setString(1, sku);
            System.out.println("Delete statement executed. Result present: " + statement.execute());
        }
    }

    /**
     * Build item subclass collection from ResultSet
     *
     * @param resultset
     * @param type
     * @return
     */
    private static List<Item> buildItemCollectionFromResultSet(ResultSet resultset, PharmaTypes type) {
        List<Item> items = new ArrayList<>();
        try {
            while (resultset.next()) {
                Item item;
                switch (type) {
                    case MEDICINE:
                        item = new Medicine();
                        ((Medicine) item).setContraindications(resultset.getString("contraindications"));
                        ((Medicine) item).setMinAge(resultset.getInt("min_age"));
                        ((Medicine) item).setResipeRequired(resultset.getInt("resipe_required"));
                        setItemFields(resultset, items, item);
                        break;
                    case SUPPLEMENT:
                        item = new Supplement();
                        ((Supplement) item).setUsefulFor(resultset.getString("useful_for"));
                        setItemFields(resultset, items, item);
                        break;
                    default:
                        break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    /**
     * Set superclass fields for items
     *
     * @param resultset
     * @param items
     * @param item
     * @throws SQLException
     */
    private static void setItemFields(ResultSet resultset, List<Item> items, Item item) throws SQLException {
        item.setSku(resultset.getString("sku"));
        item.setDescription(resultset.getString("description"));
        item.setPrice(resultset.getDouble("price"));
        item.setSalePrice(resultset.getDouble("sale_price"));
        item.setDeleted(resultset.getDouble("deleted"));
        items.add(item);
    }
}
