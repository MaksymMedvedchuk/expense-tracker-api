package com.example.expensetracker.category;

import java.util.UUID;

/**
 * Tells whether other parts of the system still reference a category.
 * Declared here and implemented by the packages that hold those references, so this package
 * does not depend on them and no package cycle appears.
 */
public interface CategoryUsage {

    boolean isInUse(UUID categoryId);
}
