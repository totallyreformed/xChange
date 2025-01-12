package com.example.xchange.request;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.xchange.Item;
import com.example.xchange.Request;
import com.example.xchange.SimpleCalendar;
import com.example.xchange.User;
import com.example.xchange.database.UserRepository;
import com.example.xchange.xChanger;
import com.example.xchange.Notification;

import java.util.List;

/**
 * Presenter class for handling user item fetching and request creation in the xChange application.
 * <p>
 * This class acts as an intermediary between the data layer ({@link UserRepository}) and the {@link RequestViewModel},
 * facilitating the loading of user items and the creation of requests.
 * </p>
 */
public class RequestPresenter {

    private final UserRepository userRepository;
    private final RequestViewModel viewModel;
    private final Handler mainThreadHandler = new Handler(Looper.getMainLooper());

    /**
     * Constructor for initializing the RequestPresenter.
     *
     * @param context   The application context.
     * @param viewModel The ViewModel to be updated by the presenter.
     */
    public RequestPresenter(Context context, RequestViewModel viewModel) {
        this.userRepository = new UserRepository(context);
        this.viewModel = viewModel;
    }

    /**
     * Loads the items belonging to a user and updates the ViewModel.
     *
     * @param username The username of the user whose items are to be loaded.
     */
    public void loadUserItems(String username) {
        userRepository.getItemsByUsername(username, new UserRepository.UserItemsCallback() {
            @Override
            public void onSuccess(List<Item> items) {
                mainThreadHandler.post(() -> viewModel.updateUserItems(items));
            }

            @Override
            public void onFailure(String message) {
                mainThreadHandler.post(() -> viewModel.updateUserItems(null));
            }
        });
    }

    /**
     * Creates a new request and saves it in the repository.
     *
     * @param requester     The {@link xChanger} creating the request.
     * @param requestee     The {@link xChanger} receiving the request.
     * @param offeredItem   The item being offered by the requester.
     * @param requestedItem The item being requested from the requestee.
     */
    public void createRequest(xChanger requester, xChanger requestee, Item offeredItem, Item requestedItem) {
        Request request = new Request(
                requester,
                requestee,
                offeredItem,
                requestedItem,null);

        userRepository.saveRequest(request, new UserRepository.SaveRequestCallback() {
            @Override
            public void onSuccess() {
                // After saving the request, send a notification to the requestee
                String notificationMessage = "Your item '" + requestedItem.getItemName() + "' has been requested by " + requester.getUsername() + ".";

                // Create a Notification for the requestee. Here, we assume that the Request object has an id.
                // If the Request id is not available at this point, you can pass a default/dummy id.
                Notification notification = new Notification(
                        requestee.getUsername(),
                        notificationMessage,
                        SimpleCalendar.today(),
                        request.getRequestId(), // or a dummy value (e.g., -1) if request id is unavailable
                        request.getRequestedItem().getItemId() // or a dummy value (e.g., -1) if item id is unavailable
                );

                userRepository.addNotification(notification, new UserRepository.OperationCallback() {
                    @Override
                    public void onSuccess() {
                        Log.d("RequestPresenter", "Notification added successfully");
                    }

                    @Override
                    public void onFailure(String message) {
                        Log.e("RequestPresenter", "Failed to add notification: " + message);
                    }
                });

                mainThreadHandler.post(() -> {
                });
            }

            @Override
            public void onFailure(String message) {
                mainThreadHandler.post(() -> {
                });
            }
        });
    }
}