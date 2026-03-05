# Build Challenge in Java

## Assignment 1: Library Book Checkout System
Implemented:
- `Book` (isbn, title, author, availability)
- `Member` (memberId, name, borrowedBooks)
- `Library` service with:
  - `addBook`
  - `registerMember`
  - `borrowBook`
  - `returnBook`
  - `calculateFine`
  - `getAvailableBooks`
  - `getMemberBorrowingHistory`

Rules handled:
- Max 3 borrowed books per member
- 14-day borrowing period
- $0.50/day fine after 14 days
- Edge cases: member not found, book not found, unavailable/invalid operations

Run Assignment 1:
```bash
javac -d out src/Main.java src/models/*.java src/services/*.java
java -cp out Main
```
## Assignment 2: Order Processing + Invoice Summary
Implemented:

Reads delimited records from orders.txt
Validates malformed lines and invalid data
Logs errors with line numbers
Aggregates by customer:
total items
gross total
discount
net total
Prints grand totals at end

Run Assignment 2:
```bash
javac -d out src/assignment2/*.java
java -cp out assignment2.MainAssignment2
```

# Notes
Language: Java

Data structures: Map, TreeMap, ArrayList

Input file for Assignment 2: orders.txt
