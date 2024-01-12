create table Product(
uniqueID int,
productID int,

constraint pk_product PRIMARY KEY (uniqueID)
);

create table Inventory(
    productID int,
    productName varchar(40),
    price int,  
    quantity int,

    constraint pk_inventory PRIMARY KEY (productID)
);

create table Transaction(
    transactionNo int,
    transactionID int,
    customerID int,
    dateOfPurchase date,
    productID int,
    quantityBought int,

    constraint pk_transaction PRIMARY KEY (transactionNo)
);

create table Customer(
    customerID int,
    customerName varchar(40),

    constraint pk_customer PRIMARY KEY (customerID)
);

create table Employee(
    employeeID int,
    employeeName varchar(40),
    dateOfJoining date,
    isOwner bit,

    constraint pk_employee PRIMARY KEY (employeeID)
);


