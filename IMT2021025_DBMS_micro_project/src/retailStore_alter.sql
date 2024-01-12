alter table Product 
add constraint fk_Product_productID FOREIGN KEY (productID)
REFERENCES Inventory(productID);
