package chat;

public class MessageStorage {
    private String[] messages;
    private int size;
    private static final int DEFAULT_CAPACITY = 100;

    public MessageStorage() {
        messages = new String[DEFAULT_CAPACITY];
        size = 0;
    }

    public void addMessage(String message) {
        if (size >= messages.length) {
            // If the array is full, double its capacity
            String[] newMessages = new String[messages.length * 2];
            System.arraycopy(messages, 0, newMessages, 0, messages.length);
            messages = newMessages;
        }

        messages[size] = message;
        size++;
    }

    public String getMessage(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        return messages[index];
    }

    public int getSize() {
        return size;
    }
}
