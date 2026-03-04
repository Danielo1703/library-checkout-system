import models.Book;
import models.Member;
import services.Library;

public class Main {
    public static void main(String[] args) {
        Library library = new Library();

        Book b1 = new Book("111", "Clean Code", "Robert Martin");
        Book b2 = new Book("222", "Effective Java", "Joshua Bloch");
        Book b3 = new Book("333", "Design Patterns", "GoF");
        Book b4 = new Book("444", "Refactoring", "Martin Fowler");

        library.addBook(b1);
        library.addBook(b2);
        library.addBook(b3);
        library.addBook(b4);

        Member m1 = new Member("M001", "Alice");
        library.registerMember(m1);

        System.out.println("Borrow b1: " + library.borrowBook("M001", "111"));
        System.out.println("Borrow b2: " + library.borrowBook("M001", "222"));
        System.out.println("Borrow b3: " + library.borrowBook("M001", "333"));
        System.out.println("Borrow b4 (should fail max 3): " + library.borrowBook("M001", "444"));

        System.out.println("Return b2: " + library.returnBook("M001", "222"));
        System.out.println("Borrow b4 (should work now): " + library.borrowBook("M001", "444"));

         System.out.println("Borrow with bad member: " + library.borrowBook( "BAD_ID", "320"));
        System.out.println("Borrow with bad book: " + library.borrowBook("M001", "999"));
        System.out.println("Return book not borrowed: " + library.returnBook("M001", "999"));

        double fine = library.calculateFine("M001", 20);
        System.out.println("Fine for 20 days overdue: $" + fine);

        System.out.println("Available books: " + library.getAvailableBooks());
        System.out.println("History for M001: " + library.getMemberBorrowingHistory("M001"));
    }
}
