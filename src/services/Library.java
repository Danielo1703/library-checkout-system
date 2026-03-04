package services;

import models.Book;
import models.Member;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Library {
    private Map<String, Book> books = new HashMap<>();
    private Map<String, Member> members = new HashMap<>();
    private Map<String, List<String>> borrowingHistory = new HashMap<>();

    public void addBook(Book book) {
        books.put(book.getIsbn(), book);
    }

    public void registerMember(Member member) {
        members.put(member.getMemberId(), member);
        borrowingHistory.put(member.getMemberId(), new ArrayList<>());
    }

    public boolean borrowBook(String memberId, String isbn) {
        Member member = members.get(memberId);
        Book book = books.get(isbn);

        if (member == null || book == null || !book.isAvailable()) return false;
        if (member.getBorrowedBooks().size() >= 3) return false;

        member.borrowBook(book);
        book.setAvailable(false);
        borrowingHistory.get(memberId).add("Borrowed: " + book.getTitle());
        return true;
    }

    public boolean returnBook(String memberId, String isbn) {
        Member member = members.get(memberId);
        Book book = books.get(isbn);

        if (member == null || book == null) return false;
        if (!member.getBorrowedBooks().contains(book)) return false;

        member.returnBook(book);
        book.setAvailable(true);
        borrowingHistory.get(memberId).add("Returned: " + book.getTitle());
        return true;
    }

    public double calculateFine(String memberId, int overdueDays) {
        if (!members.containsKey(memberId)) return 0.0;
        if (overdueDays <= 14) return 0.0;
        return (overdueDays - 14) * 0.50;
    }

    public List<Book> getAvailableBooks() {
        List<Book> available = new ArrayList<>();
        for (Book book : books.values()) {
            if (book.isAvailable()) available.add(book);
        }
        return available;
    }

    public List<String> getMemberBorrowingHistory(String memberId) {
        return borrowingHistory.getOrDefault(memberId, new ArrayList<>());
    }
}
