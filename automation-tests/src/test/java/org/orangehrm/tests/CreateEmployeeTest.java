package org.orangehrm.tests;

import io.qameta.allure.*;
import org.apache.logging.log4j.Logger;
import org.orangehrm.core.logging.LoggerFactory;
import org.orangehrm.pages.*;
import org.orangehrm.tests.utils.TestDataUtil;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.orangehrm.tests.utils.LoginConstants.VALID_PASSWORD;
import static org.orangehrm.tests.utils.LoginConstants.VALID_USERNAME;

@Epic("Employee Management")
@Feature("Create Employee")
public class CreateEmployeeTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(CreateEmployeeTest.class);

    @Story("Admin can create a new employee successfully")
    @Description("Validates successful employee creation via PIM module")
    @Test
    public void createEmployeeSuccessfully() {

        LoginPage loginPage = new LoginPage(driver);
        loginPage.open();
        loginPage.login(VALID_USERNAME, VALID_PASSWORD);

        DashboardPage dashboardPage = new DashboardPage(driver);
        Assert.assertTrue(dashboardPage.isDashboardDisplayed(), "Dashboard not visible");

        PimPage pimPage = new PimPage(driver);
        pimPage.openAddEmployee();

        String firstName = "John";
        String lastName = "Doe";
        String employeeId = TestDataUtil.uniqueEmployeeId();

        AddEmployeePage addEmployeePage = new AddEmployeePage(driver);
        addEmployeePage.createEmployee(firstName, lastName, employeeId);

        EmployeeDetailsPage detailsPage = new EmployeeDetailsPage(driver);
        String fullName = detailsPage.getEmployeeName();

        Assert.assertTrue(
                detailsPage.isEmployeeCreated(),
                "Employee was not created successfully"
        );

        Assert.assertTrue(fullName.contains(firstName),"Employee name does not match");

        Assert.assertTrue(fullName.contains(lastName), "Last name not present");

        log.info("Employee created successfully: {} {}", firstName, lastName);
    }
}
