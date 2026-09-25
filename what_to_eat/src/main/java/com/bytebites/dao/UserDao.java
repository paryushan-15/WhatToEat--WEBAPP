package com.bytebites.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bytebites.config.FirebaseConfig;
import com.bytebites.model.User;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

public class UserDao {

    // =====================================================
    // FIRESTORE
    // =====================================================

    private final Firestore db;


    // =====================================================
    // COLLECTION
    // =====================================================

    private static final String USER_COLLECTION =
            "Users";


    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public UserDao() {

        this.db =
                FirebaseConfig.getFirestore();
    }


    // =====================================================
    // SAVE USER
    // =====================================================

    public boolean saveUser(
            User user
    ) {

        if (
                user == null
                        ||
                isBlank(user.getUid())
        ) {

            return false;
        }


        try {

            /*
             * Make sure newly created users always
             * receive the correct role/status.
             */

            if (isBlank(user.getRole())) {

                user.setRole(
                        "USER"
                );
            }


            if (isBlank(user.getStatus())) {

                user.setStatus(
                        "ACTIVE"
                );
            }


            if (user.getCreatedAt() <= 0) {

                user.setCreatedAt(
                        System.currentTimeMillis()
                );
            }


            user.setUpdatedAt(
                    System.currentTimeMillis()
            );


            db.collection(
                            USER_COLLECTION
                    )
                    .document(
                            user.getUid()
                    )
                    .set(user)
                    .get();


            System.out.println(
                    "User saved successfully: "
                            + user.getUid()
            );


            return true;

        } catch (Exception e) {

            System.out.println(
                    "Unable to save user."
            );

            e.printStackTrace();

            return false;
        }
    }


    // =====================================================
    // GET USER BY UID
    // =====================================================

    public User getUserByUid(
            String uid
    ) {

        if (isBlank(uid)) {

            return null;
        }


        try {

            DocumentSnapshot document =
                    db.collection(
                                    USER_COLLECTION
                            )
                            .document(
                                    uid.trim()
                            )
                            .get()
                            .get();


            if (!document.exists()) {

                return null;
            }


            User user =
                    document.toObject(
                            User.class
                    );


            /*
             * Backward compatibility for old Users
             * that don't contain these fields yet.
             */

            if (user != null) {

                if (isBlank(user.getUid())) {

                    user.setUid(
                            document.getId()
                    );
                }


                if (isBlank(user.getRole())) {

                    user.setRole(
                            "USER"
                    );
                }


                if (isBlank(user.getStatus())) {

                    user.setStatus(
                            "ACTIVE"
                    );
                }
            }


            return user;

        } catch (Exception e) {

            System.out.println(
                    "Unable to load user: "
                            + uid
            );

            e.printStackTrace();

            return null;
        }
    }


    // =====================================================
    // GET ALL USERS
    // =====================================================

    public List<User> getAllUsers() {

        List<User> users =
                new ArrayList<>();


        try {

            QuerySnapshot snapshot =
                    db.collection(
                                    USER_COLLECTION
                            )
                            .get()
                            .get();


            for (
                    QueryDocumentSnapshot document :
                    snapshot.getDocuments()
            ) {

                try {

                    User user =
                            document.toObject(
                                    User.class
                            );


                    if (user == null) {

                        continue;
                    }


                    if (isBlank(user.getUid())) {

                        user.setUid(
                                document.getId()
                        );
                    }


                    if (isBlank(user.getRole())) {

                        user.setRole(
                                "USER"
                        );
                    }


                    if (isBlank(user.getStatus())) {

                        user.setStatus(
                                "ACTIVE"
                        );
                    }


                    users.add(
                            user
                    );

                } catch (Exception e) {

                    System.out.println(
                            "Unable to map user: "
                                    + document.getId()
                    );

                    e.printStackTrace();
                }
            }


        } catch (Exception e) {

            System.out.println(
                    "Unable to load Users collection."
            );

            e.printStackTrace();
        }


        return users;
    }


    // =====================================================
    // UPDATE USER NAME
    //
    // Existing operation preserved.
    // =====================================================

    public boolean updateUserName(
            String uid,
            String newName
    ) {

        if (
                isBlank(uid)
                        ||
                isBlank(newName)
        ) {

            return false;
        }


        try {

            Map<String, Object> updates =
                    new HashMap<>();


            updates.put(
                    "name",
                    newName.trim()
            );

            updates.put(
                    "updatedAt",
                    System.currentTimeMillis()
            );


            db.collection(
                            USER_COLLECTION
                    )
                    .document(
                            uid.trim()
                    )
                    .update(updates)
                    .get();


            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }


    // =====================================================
    // UPDATE WHATSAPP NUMBER
    //
    // Existing operation preserved.
    // =====================================================

    public boolean updateWhatsappNumber(
            String uid,
            String whatsappNumber
    ) {

        if (isBlank(uid)) {

            return false;
        }


        try {

            Map<String, Object> updates =
                    new HashMap<>();


            updates.put(
                    "whatsappNumber",
                    safeTrim(
                            whatsappNumber
                    )
            );

            updates.put(
                    "updatedAt",
                    System.currentTimeMillis()
            );


            db.collection(
                            USER_COLLECTION
                    )
                    .document(
                            uid.trim()
                    )
                    .update(updates)
                    .get();


            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }


    // =====================================================
    // UPDATE USER PROFILE
    //
    // Useful for Admin User Details screen.
    // =====================================================

    public boolean updateUserProfile(
            String uid,
            String name,
            String whatsappNumber
    ) {

        if (
                isBlank(uid)
                        ||
                isBlank(name)
        ) {

            return false;
        }


        try {

            Map<String, Object> updates =
                    new HashMap<>();


            updates.put(
                    "name",
                    name.trim()
            );

            updates.put(
                    "whatsappNumber",
                    safeTrim(
                            whatsappNumber
                    )
            );

            updates.put(
                    "updatedAt",
                    System.currentTimeMillis()
            );


            db.collection(
                            USER_COLLECTION
                    )
                    .document(
                            uid.trim()
                    )
                    .update(updates)
                    .get();


            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }


    // =====================================================
    // UPDATE ACCOUNT STATUS
    // =====================================================

    public boolean updateUserStatus(
            String uid,
            String status
    ) {

        if (
                isBlank(uid)
                        ||
                isBlank(status)
        ) {

            return false;
        }


        String normalizedStatus =
                status
                        .trim()
                        .toUpperCase();


        /*
         * Only allow statuses supported by
         * the current Admin system.
         */

        if (
                !normalizedStatus.equals("ACTIVE")
                        &&
                !normalizedStatus.equals("SUSPENDED")
        ) {

            System.out.println(
                    "Invalid User status: "
                            + status
            );

            return false;
        }


        try {

            Map<String, Object> updates =
                    new HashMap<>();


            updates.put(
                    "status",
                    normalizedStatus
            );

            updates.put(
                    "updatedAt",
                    System.currentTimeMillis()
            );


            db.collection(
                            USER_COLLECTION
                    )
                    .document(
                            uid.trim()
                    )
                    .update(updates)
                    .get();


            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }


    // =====================================================
    // SUSPEND USER
    // =====================================================

    public boolean suspendUser(
            String uid
    ) {

        return updateUserStatus(
                uid,
                "SUSPENDED"
        );
    }


    // =====================================================
    // ACTIVATE USER
    // =====================================================

    public boolean activateUser(
            String uid
    ) {

        return updateUserStatus(
                uid,
                "ACTIVE"
        );
    }


    // =====================================================
    // CHECK USER ACTIVE
    // =====================================================

    public boolean isUserActive(
            String uid
    ) {

        User user =
                getUserByUid(
                        uid
                );


        if (user == null) {

            return false;
        }


        return user.isActive();
    }


    // =====================================================
    // UPDATE LAST LOGIN
    // =====================================================

    public boolean updateLastLogin(
            String uid
    ) {

        if (isBlank(uid)) {

            return false;
        }


        try {

            long now =
                    System.currentTimeMillis();


            Map<String, Object> updates =
                    new HashMap<>();


            updates.put(
                    "lastLoginAt",
                    now
            );

            updates.put(
                    "updatedAt",
                    now
            );


            db.collection(
                            USER_COLLECTION
                    )
                    .document(
                            uid.trim()
                    )
                    .update(updates)
                    .get();


            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }


    // =====================================================
    // SEARCH USERS
    //
    // Firestore does not provide simple partial,
    // case-insensitive contains search, so for your
    // Admin UI we filter the loaded User list.
    // =====================================================

    public List<User> searchUsers(
            String searchText
    ) {

        List<User> users =
                getAllUsers();


        if (isBlank(searchText)) {

            return users;
        }


        String query =
                searchText
                        .trim()
                        .toLowerCase();


        List<User> results =
                new ArrayList<>();


        for (User user : users) {

            if (user == null) {

                continue;
            }


            if (
                    contains(
                            user.getName(),
                            query
                    )
                            ||
                    contains(
                            user.getEmail(),
                            query
                    )
                            ||
                    contains(
                            user.getUid(),
                            query
                    )
                            ||
                    contains(
                            user.getWhatsappNumber(),
                            query
                    )
                            ||
                    contains(
                            user.getStatus(),
                            query
                    )
            ) {

                results.add(
                        user
                );
            }
        }


        return results;
    }


    // =====================================================
    // GET USERS BY STATUS
    // =====================================================

    public List<User> getUsersByStatus(
            String status
    ) {

        if (
                isBlank(status)
                        ||
                status.equalsIgnoreCase(
                        "ALL"
                )
        ) {

            return getAllUsers();
        }


        List<User> result =
                new ArrayList<>();


        for (
                User user :
                getAllUsers()
        ) {

            if (
                    user != null
                            &&
                    user.getStatus() != null
                            &&
                    user.getStatus()
                            .equalsIgnoreCase(
                                    status.trim()
                            )
            ) {

                result.add(
                        user
                );
            }
        }


        return result;
    }


    // =====================================================
    // TOTAL USER COUNT
    // =====================================================

    public int getTotalUserCount() {

        try {

            QuerySnapshot snapshot =
                    db.collection(
                                    USER_COLLECTION
                            )
                            .get()
                            .get();


            return snapshot.size();

        } catch (Exception e) {

            e.printStackTrace();

            return 0;
        }
    }


    // =====================================================
    // ACTIVE USER COUNT
    // =====================================================

    public int getActiveUserCount() {

        int count = 0;


        for (
                User user :
                getAllUsers()
        ) {

            if (
                    user != null
                            &&
                    user.isActive()
            ) {

                count++;
            }
        }


        return count;
    }


    // =====================================================
    // SUSPENDED USER COUNT
    // =====================================================

    public int getSuspendedUserCount() {

        int count = 0;


        for (
                User user :
                getAllUsers()
        ) {

            if (
                    user != null
                            &&
                    user.isSuspended()
            ) {

                count++;
            }
        }


        return count;
    }


    // =====================================================
    // DELETE USER PROFILE
    //
    // IMPORTANT:
    // This deletes the Users/{uid} Firestore profile.
    //
    // We will later build the Admin account deletion
    // service that also cleans related data.
    // =====================================================

    public boolean deleteUser(
            String uid
    ) {

        if (isBlank(uid)) {

            return false;
        }


        try {

            db.collection(
                            USER_COLLECTION
                    )
                    .document(
                            uid.trim()
                    )
                    .delete()
                    .get();


            System.out.println(
                    "User profile deleted: "
                            + uid
            );


            return true;

        } catch (Exception e) {

            System.out.println(
                    "Unable to delete User profile."
            );

            e.printStackTrace();

            return false;
        }
    }


    // =====================================================
    // CHECK IF USER EXISTS
    // =====================================================

    public boolean userExists(
            String uid
    ) {

        if (isBlank(uid)) {

            return false;
        }


        try {

            DocumentSnapshot document =
                    db.collection(
                                    USER_COLLECTION
                            )
                            .document(
                                    uid.trim()
                            )
                            .get()
                            .get();


            return document.exists();

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }


    // =====================================================
    // =====================================================
    //
    // UTILITY METHODS
    //
    // =====================================================
    // =====================================================


    private boolean isBlank(
            String value
    ) {

        return value == null
                ||
                value.trim().isEmpty();
    }


    private String safeTrim(
            String value
    ) {

        if (value == null) {

            return "";
        }


        return value.trim();
    }


    private boolean contains(
            String value,
            String query
    ) {

        if (
                value == null
                        ||
                query == null
        ) {

            return false;
        }


        return value
                .toLowerCase()
                .contains(
                        query
                );
    }
}