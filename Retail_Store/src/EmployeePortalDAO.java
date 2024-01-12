public interface EmployeePortalDAO {
    public void checkEmployeeDetails();
    public void addProduct(String name,Integer price,Integer quantity);
    public String maxSoldProduct(java.sql.Date beg,java.sql.Date end);
    public void checkInventory();
    public Employee getEmployee(Integer empID);

    public Boolean isOwner(Employee emp);
    public String getProductName(Integer prodID);
}
