***Summary***

**Framework Structure**

*Main*
- com.api.entity => defines endpoint entities to create objects
- com.api.service => Base class for api initialization, sending request and disposing objects

*Test*

- com.api.tests => API test classes
  - UserTests => created a positive test case for user registration
  - LoginTests => Negative scenario passing an unregistered user to login endpoint should throw error 400
  - Workflow Test => Validating end to end flow : register user, login using the registred user and then create case for logged in user.

- com.ui.tests => UI test case for broken links validation and printing.


**Reporting**
- Allure


