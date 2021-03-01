set serveroutput on;
-------------------------------------------------------------------------------- 
-- CONTRAGENTS
--------------------------------------------------------------------------------
CREATE TYPE address_t AS OBJECT (
  street      NVARCHAR2(100),
  city        NVARCHAR2(20),
  province    NVARCHAR2(20),
  postal_code VARCHAR2(6)
);
  
--contragent objects id sequence
CREATE SEQUENCE contr_seq
START WITH 1
INCREMENT BY 1;

CREATE TYPE contragent_t as OBJECT(
  ID NUMBER(10) ,
  phone NVARCHAR2(12),  
  email NVARCHAR2(40),
  bank_account NVARCHAR2(30),
  credit_card NVARCHAR2(30),
  address address_t
)NOT INSTANTIABLE NOT FINAL;

-- subtypes that inherit contragent_t 
CREATE TYPE supplier_t
UNDER contragent_t(
company_name NVARCHAR2(100),
vat NVARCHAR2(12)
);

CREATE TYPE customer_t
UNDER contragent_t(
first_name NVARCHAR2(30),
last_name NVARCHAR2(30)
);

CREATE TYPE shipper_t
UNDER contragent_t(
company_name NVARCHAR2(100),
vat NVARCHAR2(12) 
);

CREATE TABLE contragents
OF contragent_t; 
ALTER TABLE contragents ADD CONSTRAINT contragent_id PRIMARY KEY(ID);

--adding contragents
INSERT INTO contragents VALUES(customer_t(contr_seq.NEXTVAL,'0897020290','pesho02@abv,bg','UBBS 0000000000', '1234 5678 9123 4566',address_t('Lozengrad 8','Ruse','Ruse','7000'),'Petar','Ivanov'));
INSERT INTO contragents VALUES(customer_t(contr_seq.NEXTVAL,'0897020202','ivan_example@abv,bg','UBBS 0000000000', '1234 5678 9123 4566',address_t('Lozengrad 8','Ruse','Ruse','7000'),'Ivan','Ivanov'));
INSERT INTO contragents VALUES(supplier_t(contr_seq.NEXTVAL,'0897030303','the_best_supplier@abv,bg','UBBS 0000000000', '1234 5678 9123 4566',address_t('Slivnica','Plovdiv','Plovdiv','4000'),'MEGA LTD','202202222'));
INSERT INTO contragents VALUES(supplier_t(contr_seq.NEXTVAL,'0897030225','suepr_supplier@abv,bg','UBBS 0000000000', '1234 5678 9123 9999',address_t('Grivica','Pleven','Pleven','0000'),'TOTAL SUP LTD','202202222'));
INSERT INTO contragents VALUES(shipper_t(contr_seq.NEXTVAL,'0897111111','kargo_master@abv,bg','UBBS 0000000000', '1234 5678 9123 4566',address_t('Logistic park 230','Sofia','Sofia','1000'),'KARGOBULL LTD','200200200'));

-- checks supplier fields
SELECT s.*, TREAT(VALUE(s) AS supplier_t).company_name from CONTRAGENTS s;
-- checks customer field
SELECT c.*, TREAT(VALUE(c) AS customer_t).first_name from contragents c;
 
--select all shippers
--select * from CONTRAGENTS c WHERE VALUE(c) IS OF TYPE(shipper_t);

-------------------------------------------------------------------------------- 
-- ITEMS
--------------------------------------------------------------------------------
-- item super class
CREATE TYPE item_t as OBJECT(
sku NVARCHAR2(100),
description NVARCHAR2(300),
price NUMBER(7,2),
sale_price NUMBER(7,2),
deleted NUMBER(1)
)NOT FINAL;

--suplements
CREATE TYPE suplement_t
UNDER item_t(
useful_for NVARCHAR2(200) 
);
--medicines
CREATE TYPE medicine_t
UNDER item_t(
resipe_required NUMBER(1),
min_age number(3),
contraindications NVARCHAR2(1000)
)

CREATE TABLE items of item_t;
ALTER TABLE items
ADD CONSTRAINT sku_unique UNIQUE (sku);
--------------------------------------------------------------------------------
--Adding medicines
INSERT INTO items VALUES(medicine_t('Aspirin','aspirin description',12.25,13.15,0,1,16,'aspirirn contraindicators'));
INSERT INTO items VALUES(medicine_t('Analgin','analdgin description',9.25,10.00,0,1,18,'analgin contraindicators'));

--select it.*, TREAT(VALUE(it) AS medicine_t).contraindications from items it;

--adding suplements
INSERT INTO items VALUES(suplement_t('Ginkobiloba','ginko description',7.45,8.00,0,'ginkobiloba is useful for: ...'));
INSERT INTO items VALUES(suplement_t('Valeriana','veleriana description',8.15,9.00,0,'Valeriana is useful for: ...'));
INSERT INTO items VALUES(suplement_t('Devils claw','Divils claw description',13.10,14.00,0,'Devils claw is useful for: ...'));

--select it.*, TREAT(VALUE(it) AS suplement_t).useful_for as useful from items it;
--SELECT * FROM v$version; 
-------------------------------------------------------------------------------- 
-- END ITEMS 
-------------------------------------------------------------------------------- 
-- SUPPLY
--------------------------------------------------------------------------------
-- create supplies table with reference approach
CREATE TABLE SUPPLY (
  ID  NUMBER GENERATED ALWAYS AS IDENTITY,
  supplier REF supplier_t,
  supplied_at TIMESTAMP (2) NOT NULL,
  total_price NUMBER(7,2),
  CONSTRAINT supply_pk PRIMARY KEY (ID)
);

-- I will use a non-reference approach for this table
CREATE TABLE SUPPLY_LINE(
id  NUMBER GENERATED ALWAYS AS IDENTITY,
supply_id NUMBER,
item REF item_t,
quantity NUMBER(5),
price NUMBER(7,2),
CONSTRAINT fk_supply
    FOREIGN KEY (supply_id)
    REFERENCES SUPPLY (ID)
    ON DELETE CASCADE
);

-- creating supply
DECLARE   
supply_num number;  
totalPrice number;
BEGIN
  INSERT INTO SUPPLY VALUES(DEFAULT, (SELECT TREAT(REF(c) as ref supplier_t) FROM CONTRAGENTS c WHERE VALUE(c) IS OF TYPE(supplier_t) FETCH FIRST ROW ONLY ), current_timestamp,20.25) returning ID into supply_num; 
  --add supply lines
  INSERT INTO SUPPLY_LINE VALUES(DEFAULT,supply_num,(SELECT REF(i) FROM items i where i.sku = 'Aspirin' ),50,15.50);
  INSERT INTO SUPPLY_LINE VALUES(DEFAULT,supply_num,(SELECT REF(i) FROM items i where i.sku = 'Valeriana' ),200,13.00);
  INSERT INTO SUPPLY_LINE VALUES(DEFAULT,supply_num,(SELECT REF(i) FROM items i where i.sku = 'Devils claw' ),300,8.20); 
  --calculate and update total sum
  SELECT sum(sl.QUANTITY * sl.PRICE )as sum into totalPrice FROM SUPPLY_LINE sl where sl.SUPPLY_ID = supply_num;  
  UPDATE SUPPLY sup SET sup.total_price = totalPrice  WHERE sup.ID = supply_num; 
END;

------------------------------------------------------------------------------- 
-- END SUPPLY
-------------------------------------------------------------------------------
-- ORDERS
-------------------------------------------------------------------------------
-- create order table
CREATE TABLE orders(
  ID  NUMBER GENERATED ALWAYS AS IDENTITY,
  customer REF customer_t,
  created_at TIMESTAMP (2) NOT NULL,  
  shipping_id NUMBER, -- null while not assigned for shipping
  shipping_time TIMESTAMP (2), -- will update after shipping actions  
  address address_t,
  total_price NUMBER(7,2) DEFAULT 0.00,
  CONSTRAINT order_pk PRIMARY KEY (ID)   
);
-- create order lines with item reference
CREATE TABLE order_lines(
order_id NUMBER,
item REF item_t,   
quantity NUMBER(5),
price NUMBER(7,2),
CONSTRAINT fk_order_id
    FOREIGN KEY (order_id)
    REFERENCES orders (ID)
    ON DELETE CASCADE
);

-- create an order
DECLARE   
order_num number; 
totalPrice number;
BEGIN
   INSERT INTO orders (customer, created_at,address) VALUES( ( SELECT TREAT(REF(c) as ref customer_t) FROM CONTRAGENTS c WHERE VALUE(c) IS OF TYPE(customer_t) FETCH FIRST ROWS ONLY),current_timestamp, address_t('Loznica 18','Pleven','Pleven','7000')  ) returning ID into order_num;    
   INSERT INTO order_lines VALUES(order_num,(SELECT REF(i) FROM items i where i.sku = 'Aspirin' ),10,(SELECT i.sale_price FROM items i where i.sku = 'Aspirin'));
   INSERT INTO order_lines VALUES(order_num,(SELECT REF(i) FROM items i where i.sku = 'Devils claw' ),3,(SELECT i.sale_price FROM items i where i.sku = 'Devils claw'));     
   SELECT sum(ol.QUANTITY * ol.PRICE) as sum into totalPrice FROM order_lines ol where ol.ORDER_ID = order_num;  
   UPDATE orders SET total_price = totalPrice WHERE ID = order_num;
  -- dbms_output.Put_line(order_num); 
END;
/
------------------------------------------------------------------------------- 
-- END ORDERS
-------------------------------------------------------------------------------
-- SHIPPING
-------------------------------------------------------------------------------  
-- in one shipping can be added many orders  
CREATE TABLE shippings(
  ID  NUMBER GENERATED ALWAYS AS IDENTITY,
  created_at TIMESTAMP (2) NOT NULL,
  district NVARCHAR2(100),
  shipper REF shipper_t,
  shipp_date TIMESTAMP (2),
  delivery_date TIMESTAMP (2)
);
 
-- finding order without assigned shiping in given district
CREATE OR REPLACE FUNCTION getOrder (districtName IN NVARCHAR2) --throws NO_DATA_FOUND
RETURN ORDERS%ROWTYPE
is foundOne ORDERS%ROWTYPE;
BEGIN
   SELECT * INTO foundOne FROM ORDERS o WHERE o.SHIPPING_ID IS NULL AND LOWER(o.ADDRESS.province) = LOWER(districtName) FETCH FIRST ROWS ONLY;   
   RETURN (foundOne);  
END;
/
-- Trying to find a delivery for an given area. 
--If such a delivery is available, looks for an order in the same area and include it in that delivery. 
--If not available for this area, creates a new shipping and adds the order
DECLARE
  row_order ORDERS%ROWTYPE;
  ship_number NUMBER; 
  m_district NVARCHAR2(20) := 'pleven';
BEGIN  
  SELECT sh.ID INTO ship_number FROM shippings sh WHERE LOWER(sh.district) = LOWER(m_district) FETCH FIRST ROWS ONLY; 
  row_order := getOrder(m_district);  
  UPDATE orders SET shipping_id = ship_number WHERE ID = row_order.ID; 
 EXCEPTION
   WHEN NO_DATA_FOUND THEN
   DBMS_OUTPUT.PUT_LINE(ship_number);
   INSERT INTO shippings (created_at, district) VALUES ( current_timestamp, m_district) returning ID into ship_number;
   row_order := getOrder(m_district);   
   UPDATE orders SET shipping_id = ship_number WHERE ID = row_order.ID; 
END;