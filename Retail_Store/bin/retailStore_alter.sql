alter table Productadd constraint fk_Product_productID FOREIGN KEY (productID)
REFERENCES Inventory(productID);
