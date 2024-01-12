-- products
-- product 1 is a piano
insert into Product(productID, uniqueID)
VALUES(1, 100);

insert into Product(productID, uniqueID)
VALUES(1, 102);

insert into Product(productID, uniqueID)
VALUES(1, 103);

-- product 2 is a violin
insert into Product(productID, uniqueID)
VALUES(2, 104);

insert into Product(productID, uniqueID)
VALUES(2, 105);

insert into Product(productID, uniqueID)
VALUES(2, 106);

-- product 3 is a flute
insert into Product(productID, uniqueID)
VALUES(3, 107);

insert into Product(productID, uniqueID)
VALUES(3, 108);

insert into Product(productID, uniqueID)
VALUES(3, 109);

-- Inventory
insert into Inventory(productID, productName, price, quantity)
VALUES(1, "Piano", 40000, 3);

insert into Inventory(productID, productName, price, quantity)
VALUES(2, "Violin", 12000, 3);

insert into Inventory(productID, productName, price, quantity)
VALUES(3, "Flute", 500, 3);


-- employees
insert into Employee(employeeID, employeeName, dateOfJoining, isOwner)
VALUES(1000, "Harry Potter", "2023-01-01", true);

insert into Employee(employeeID, employeeName, dateOfJoining, isOwner)
VALUES(1002, "Hermione Granger", "2023-02-01", false);

insert into Employee(employeeID, employeeName, dateOfJoining, isOwner)
VALUES(1003, "Ron Weasley", "2023-02-01", false);

