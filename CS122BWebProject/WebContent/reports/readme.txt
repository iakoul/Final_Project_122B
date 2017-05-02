This project was created in Eclipse Neon.

Our web project uses the mysql driver JAR file along with two other external libraries, the jBCrypt library which handles Bcrypt encryption used in the website's passwords, and JSTL which is used for the JSP parts of the site. These should all be included in the build path of the project and they should also be included in the Deployment Assembly list so that Eclipse includes them in the WAR file it generates.

The following files are part of the main class and thus belong in a main folder within the src folder:

AddToCart.java
BCrypt.java
BrowseItems.java
BrowseStores.java
Checkout.java
DetailBiz.java
DetailCity.java
DetailItem.java
DetailPlaza.java
Login.java
Logout.java
MainPage.java
ShoppingCart.java

The following files are part of the default package class so they belong in the root of the src folder:

Business.java
City.java
CityResult.java
Item.java
ItemResult.java
Owner.java
Plaza.java
PlazaResult.java
SerachResult.java
StoreResult.java

The following files belong in the WebContent folder:

AdvancedSearch.jsp
index.html
LICENSE.txt
ResultTable.jsp
ShowCity.jsp
ShowItem.jsp
ShowPlaza.jsp
ShowStore.jsp

And finally, the following file belongs in a folder called css within the WebContent folder:

basicStyle.css


Assuming everything is set properly, you should be able to right click the project and Export as a WAR file.


The WAR file exported from Eclipse can then be put in the webapps folder of the Tomcat installation and Tomcat should then be restarted. Once Tomcat is restarted, the project shouls show up in the list of applications on Tomcat's web interface.

Logging into the webpage can be done by using the following credentials:

Username: admin
Password: admin

A valid credit card that the system can be tested with is:

First name: Alex
Last Name: Bustelo
CC Number: 123456789
Exp Date: 2011-11-01



