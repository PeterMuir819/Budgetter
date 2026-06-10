package budgetapp;
import java.util.ArrayList;
import java.util.List;

public enum Category {
    FOOD(TransactionType.EXPENSE),
    RENT(TransactionType.EXPENSE),
    TRANSPORTATION(TransactionType.EXPENSE),
    ENTERTAINMENT(TransactionType.EXPENSE),
    OTHER_EXPENSE(TransactionType.EXPENSE),
    PAYCHECK(TransactionType.INCOME),
    GIFT(TransactionType.INCOME),
    OTHER_INCOME(TransactionType.INCOME);

    private final TransactionType type;

    Category(TransactionType type) {
        this.type = type;
    }

    public TransactionType getType() {
        return this.type;
    }
    
    public static List<Category> getCategoriesByType(TransactionType type) {
        List<Category> categories = new ArrayList<>();
        for (Category category : Category.values()) {
            if (category.getType() == type) {
                categories.add(category);
            }
        }
        return categories;
    }
}
