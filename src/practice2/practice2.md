Write a program to manage the supermarket's inventory including food, crockery and electronics
Each type of goods has a product code, name, inventory quantity (>=0) and unit price
For food products, care should be taken with the date of manufacture and the expiration date (theexpiration date must be after or the date of manufacture) and the supplier.
Electrical goods need to know how many months warranty period (>= 0), how much capacity KW (>=0)
For crockery, you know the manufacturer's information and the date of arrival
In addition, the manager needs to know the inventory quantity of the 3 types of goods, and the VATamount for each type of goods. (VAT for electronics and crockery is 10% and food is 5%)

Requirement 1:
➢ Based on the above information determine:
➢ Possible classes (abstract and concrete)
➢ Attributes and methods of each class
➢ Relationship design (inheritance and polymorphism if applicable)

Requirement 2:
➢ Create a method to measure consumption
○ Electronic goods: If the inventory quantity < 3 - it is considered to be sold
○ Food goods: If still in stock and expired - rated as hard to sell
○ Crockery: If inventory quantity > 50 and storage time > 10 days - evaluate as slow sale
○ The remaining cases are not evaluated

Requirement 3:
➢ Initialize DSHH management class (Use array to store list)
➢ Write a method to add goods to the list
○ More success if there is no duplicate product code
○ Allow users to choose the type of goods to add
