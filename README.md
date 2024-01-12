# Retail-Store-DBMS
Implemented a Retail Store Management System using Java and MySQL

Here are the steps to get the project running:
1. Create a new database called retailStore (or the name of your choice).
CREATE DATABASE retailStore;
2. Use the SOURCE command to run all the sql statements stored in the .sql files in the
project.
SOURCE (pathname of src/retailStore_create.sql)
SOURCE (pathname of src/retailStore_alter.sql)
SOURCE (pathname of src/retailStore_insert.sql)
3. On VS Code, open the project folder. In the file named DAO_Factory.java, change the
value of the variable PASS to your sql password. (line 17)
4. Run the project by clicking on the run button (in the top right corner of the VS Code
window), and run DAO_Main.java, which is the driver code.
5. The project should now run!

There are two kinds of users of this software : Customers and Employees.
Use cases for the customers:
1)Check Available Items
2)Order items by price
3)Buy products
4)Generate Receipt
5)Exit

Use cases for the employee:
1)Check employee details  (a functionality, only available to the owner)
2)Check the max sold product
3)Check inventory
4)Add a product
5)Exit
