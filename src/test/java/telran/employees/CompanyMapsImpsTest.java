package telran.employees;

import org.junit.jupiter.api.BeforeEach;

public class CompanyMapsImpsTest extends CompanyTest{
    @Override
    @BeforeEach
    void setCompany(){
        company = new CompanyMapsImpl();
        super.setCompany();
    }

    @Override
    protected Company getEmptyCompany() {
        return new CompanyMapsImpl();
    }
}
