    package com.example.xchange.EditItem;

    import android.util.Log;

    import com.example.xchange.Category;
    import com.example.xchange.Item;
    import com.example.xchange.database.dao.ItemDao;

    import java.util.concurrent.Executor;
    import java.util.concurrent.ExecutorService;

    public class EditItemPresenter {

        private final ItemDao itemDao;
        private final Executor executor;

        public EditItemPresenter(ItemDao itemDao, Executor executor) {
            this.itemDao = itemDao;
            this.executor = executor;
        }

        public void updateItem(Item item, String name, String description, String condition, String category) {
            if (((ExecutorService) executor).isShutdown()) {
                Log.d("TEST","YES");
                return;
            }

            executor.execute(() -> {
                try {
                    System.out.println("Executor is running in Presenter.");
                    Category itemCategory = Category.valueOf(category.toUpperCase());
                    item.setItemName(name);
                    item.setItemDescription(description);
                    item.setItemCondition(condition);
                    item.setItemCategory(itemCategory);
                    itemDao.updateItem(item);
                } catch (IllegalArgumentException e) {
                    System.err.println("Invalid category: " + category);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

