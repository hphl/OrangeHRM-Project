package org.orangehrm.tests.data;

import org.orangehrm.tests.utils.TestDataUtil;
import org.testng.annotations.DataProvider;

import static org.orangehrm.tests.utils.EmployeeConstants.VALID_LAST_NAME;
import static org.orangehrm.tests.utils.EmployeeConstants.VALID_NAME;

public class CreateEmployeeProvider {

    @DataProvider(name = "employeeRequiredFields")
    public Object[][] employeeRequiredFields() {
        return new Object[][]{
                {"", VALID_LAST_NAME,  TestDataUtil.uniqueEmployeeId(), 1},
                {VALID_NAME, "", TestDataUtil.uniqueEmployeeId(), 1},
                {VALID_NAME, VALID_LAST_NAME, "",  1},
                {"", "", "", 3}
        };
    }
}
