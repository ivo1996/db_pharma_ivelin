package org.uniruse.util;


/**
 * @author ivelin.dimitrov
 */
public class StaticQueries {

    public static final String MEDICINE_CTOR = "medicine_t(?,?,?,?,?,?,?,?)";
    public static final String SUPPLMENT_CTOR = "suplement_t(?,?,?,?,?,?)";
    public static final String CONTRAGENTS_ADDRES_PATTERN = " c.ADDRESS.city as city, c.ADDRESS.street as street, c.ADDRESS.province as province, c.ADDRESS.postal_code as postal_code ";


    public static final String INSERT_CUSTOMER = "INSERT INTO CONTRAGENTS VALUES(customer_t(contr_seq.NEXTVAL, ?, ?, ?, ?, address_t(?, ?, ?, ?), ?, ?))";
    public static final String INSERT_SUPPLIER = "INSERT INTO CONTRAGENTS VALUES(supplier_t(contr_seq.NEXTVAL, ?, ?, ?, ?, address_t(?, ?, ?, ?), ?, ?))";
    public static final String INSERT_SHIPPER = "INSERT INTO CONTRAGENTS VALUES(shipper_t(contr_seq.NEXTVAL, ?, ?, ?, ?, address_t(?, ?, ?, ?), ?, ?))";
    public static final String INSERT_SUPPLEMENT = "INSERT INTO items VALUES(" + SUPPLMENT_CTOR + ")";
    public static final String INSERT_MEDICINE = "INSERT INTO items VALUES(" + MEDICINE_CTOR + ")";
    public static final String INSERT_SUPPLY = "INSERT INTO SUPPLY VALUES(DEFAULT, (SELECT TREAT(REF(c) as ref supplier_t) FROM CONTRAGENTS c WHERE VALUE(c) IS OF TYPE(supplier_t) AND ID=? ), ?, ?)";
    public static final String INSERT_SUPPLY_LINE = "INSERT INTO SUPPLY_LINE VALUES(DEFAULT, ?, (SELECT REF(i) FROM items i where i.sku = ? ),?,?)";
    public static final String INSERT_ORDER = "INSERT INTO ORDERS VALUES(DEFAULT, (SELECT TREAT(REF(c) as ref customer_t) FROM CONTRAGENTS c WHERE VALUE(c) is OF TYPE(customer_t) AND ID=?), ?, ?, ?, address_t(?, ?, ?, ?), ?)";
    public static final String INSERT_ORDER_LINE = "INSERT INTO ORDER_LINES VALUES(?, (SELECT REF(i) FROM items i where i.sku = ? ), ?, ?)";
    public static final String INSERT_SHIPPING = "INSERT INTO SHIPPINGS VALUES(DEFAULT, ?, ?, (SELECT TREAT(REF(c) as ref shipper_t) FROM CONTRAGENTS c WHERE VALUE(c) is OF TYPE(shipper_t) AND ID=?), ?, ?)";

    public static final String SELECT_ALL_CUSTOMERS = "select c.*, TREAT(VALUE(c) AS customer_t).first_name as first_name,TREAT(VALUE(c) AS customer_t).last_name as last_name, " + CONTRAGENTS_ADDRES_PATTERN + " from CONTRAGENTS c WHERE VALUE(c) IS OF TYPE(customer_t)";
    public static final String SELECT_ALL_SUPPLIERS = "select c.*, TREAT(VALUE(c) AS supplier_t).company_name as company_name,TREAT(VALUE(c) AS supplier_t).vat as vat, " + CONTRAGENTS_ADDRES_PATTERN + " from CONTRAGENTS c WHERE VALUE(c) IS OF TYPE(supplier_t)";
    public static final String SELECT_ALL_SHIPPERS = "select c.*, TREAT(VALUE(c) AS shipper_t).company_name as company_name,TREAT(VALUE(c) AS shipper_t).vat as vat, " + CONTRAGENTS_ADDRES_PATTERN + " from CONTRAGENTS c WHERE VALUE(c) IS OF TYPE(shipper_t)";

    public static final String SELECT_ALL_SUPPLEMENTS = "select c.*, TREAT(VALUE(c) AS suplement_t).useful_for as useful_for from items c WHERE VALUE(c) IS OF TYPE(suplement_t)";
    public static final String SELECT_ALL_MEDICINES = "select c.*, TREAT(VALUE(c) AS medicine_t).contraindications as contraindications, TREAT(VALUE(c) AS medicine_t).min_age as min_age, TREAT(VALUE(c) AS medicine_t).resipe_required as resipe_required  from items c WHERE VALUE(c) IS OF TYPE(medicine_t) ";

    public static final String SELECT_ALL_SUPPLIES = "SELECT a.*,TREAT(a.SUPPLIER AS REF CONTRAGENT_T).id as supplier_id FROM SUPPLY a";
    public static final String SELECT_ALL_ORDERS = "select o.ID,o.CUSTOMER as customer, o.created_at,o.shipping_id,o.shipping_time,o.total_price,o.ADDRESS.city as city, o.ADDRESS.street as street, o.ADDRESS.province as province, o.ADDRESS.postal_code as postal_code from ORDERS o ";
    public static final String SELECT_ALL_SHIPPINGS = "SELECT s.*,TREAT(s.SHIPPER AS REF CONTRAGENT_T).id as shipper_id FROM SHIPPINGS s ";

    public static final String UPDATE_CUSTOMER = "UPDATE contragents c set VALUE(c) = customer_t(?, ?, ?, ?, ?, address_t(?, ?, ?, ?),?, ?) where c.id=?";
    public static final String UPDATE_SHIPPER = "UPDATE contragents c set VALUE(c) = shipper_t(?, ?, ?, ?, ?, address_t(?, ?, ?, ?),?, ?) where c.id=?";
    public static final String UPDATE_SUPPLIER = "UPDATE contragents c set VALUE(c) = supplier_t(?, ?, ?, ?, ?, address_t(?, ?, ?, ?),?, ?) where c.id=?";
    public static final String UPDATE_ORDER = "UPDATE ORDERS o SET o.customer=(SELECT TREAT(REF(c) as ref customer_t) FROM CONTRAGENTS c WHERE VALUE(c) is OF TYPE(customer_t) AND ID=?), o.shipping_id=?, o.SHIPPING_TIME=?, o.address=address_t(?,?,?,?), o.total_price=? where o.id=?";
    public static final String UPDATE_SUPPLY = "UPDATE SUPPLY s SET s.SUPPLIER=(SELECT TREAT(REF(c) as ref supplier_t) FROM CONTRAGENTS c WHERE VALUE(c) IS OF TYPE(supplier_t) AND ID=?), s.SUPPLIED_AT=?,s.TOTAL_PRICE=? where s.id=?";
    public static final String UPDATE_SHIPPING = "UPDATE SHIPPINGS s SET s.DISTRICT=?, s.SHIPPER=(SELECT TREAT(REF(c) as ref shipper_t) FROM CONTRAGENTS c WHERE VALUE(c) IS OF TYPE(shipper_t) AND ID=?), s.SHIPP_DATE=?, s.DELIVERY_DATE=? where s.id=?";
    public static final String UPDATE_MEDICINE = "UPDATE items i SET VALUE(i) = " + MEDICINE_CTOR + " where i.sku = ? ";
    public static final String UPDATE_SUPPLEMENT = "UPDATE items i SET VALUE(i) = " + SUPPLMENT_CTOR + " where i.sku = ? ";

    public static final String DELETE_ITEM = "DELETE FROM items where sku = ? ";
    public static final String DELETE_ORDER = "DELETE FROM orders where id = ? ";
    public static final String DELETE_SHIPPING = "DELETE FROM shippings where id = ? ";
    public static final String DELETE_SUPPLY = "DELETE FROM supply where id = ? ";
    public static final String DELETE_CONTRAGENT = "DELETE FROM contragents where id = ? ";
    public static final String DELETE_SUPPLY_LINE = "DELETE FROM supply_line where id = ? ";
    public static final String DELETE_ORDER_LINE = "DELETE FROM order_lines where order_id = ? ";

    public static final String SELECT_SUPPLY_LINES_BY_SUPPLY_ID = "SELECT sl.id, sl.item as item, sl.supply_id as supply_id, sl.quantity as qty, sl.price as price FROM SUPPLY_LINE sl WHERE sl.SUPPLY_ID = ? ";
    public static final String SELECT_ORDER_LINES_BY_ORDER_ID = "select ol.item as item, ol.PRICE as price, ol.QUANTITY as qty from ORDER_LINES ol where ol.ORDER_ID = ?";

}
