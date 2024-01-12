import java.sql.Date;

public class Employee {

    public Employee(Integer employeeID, String employeeName)
    {
        this.employeeID = employeeID;
        this.employeeName = employeeName;
    }

    private Integer employeeID;
    private String employeeName;
    private Date dateOfJoining;
    private Boolean isOwner;

    public Integer getEmployeeID() {return employeeID;}
    public String getEmployeeName() {return employeeName;}
    public Date getDate() {return dateOfJoining;}
    public Boolean getIsOwner() {return isOwner;}
    
    public void setEmployeeID(Integer empID) {this.employeeID = empID;}
    public void setEmployeeName(String empName) {this.employeeName = empName;}
    public void setDate(Date date) {this.dateOfJoining = date;}
    public void setIsOwner(Boolean isOwner) {this.isOwner = isOwner;}
}
