package telran.employees;

import employees.CompanyMapsImpl;
import org.junit.jupiter.api.BeforeEach;

public class CompanyMapsImpsTest extends CompanyTest{
    @Override
    @BeforeEach
    void setCompany(){
        company = new CompanyMapsImpl();
        super.setCompany();
    }
}
