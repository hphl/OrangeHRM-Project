package org.orangehrm.tests;

import io.qameta.allure.*;
import org.apache.logging.log4j.Logger;
import org.orangehrm.core.logging.LoggerFactory;
import org.orangehrm.pages.*;
import org.orangehrm.tests.data.CreateEmployeeProvider;
import org.orangehrm.tests.utils.TestDataUtil;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.orangehrm.tests.utils.EmployeeConstants.VALID_LAST_NAME;
import static org.orangehrm.tests.utils.EmployeeConstants.VALID_NAME;
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

        String firstName = VALID_NAME;
        String lastName = VALID_LAST_NAME;
        String employeeId = TestDataUtil.uniqueEmployeeId();

        AddEmployeePage addEmployeePage = new AddEmployeePage(driver);
        addEmployeePage.fillEmployee(firstName, lastName, employeeId);
        addEmployeePage.clickSave();

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

    @Story("Create employee required fields")
    @Description("Validates required fields (First Name, Last Name, Employee ID) when creating a new employee.")
    @Test(dataProvider = "employeeRequiredFields", dataProviderClass = CreateEmployeeProvider.class)
    public void employeeCreationShouldValidateRequiredFields(
            String first,
            String last,
            String employeeId,
            int expectedRequiredErrors
    ) {
        log.info("Starting test: employeeCreationShouldValidateRequiredFields");

        LoginPage loginPage = new LoginPage(driver);
        loginPage.open();
        loginPage.login(VALID_USERNAME, VALID_PASSWORD);

        DashboardPage dashboardPage = new DashboardPage(driver);
        Assert.assertTrue(dashboardPage.isDashboardDisplayed(), "Dashboard is not visible after login.");

        PimPage pimPage = new PimPage(driver);
        pimPage.openAddEmployee();

        AddEmployeePage addEmployeePage = new AddEmployeePage(driver);

        addEmployeePage.fillEmployee(first, last, employeeId);

        addEmployeePage.clickSave();

        int actualRequiredErrors = addEmployeePage.getRequiredFieldErrorCount();

        Assert.assertEquals(
                actualRequiredErrors,
                expectedRequiredErrors,
                "Required field error count mismatch."
        );
    }
}
