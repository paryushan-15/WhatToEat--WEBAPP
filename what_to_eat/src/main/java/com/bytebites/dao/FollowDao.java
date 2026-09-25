package com.bytebites.dao;

import java.util.ArrayList;
import java.util.List;

import com.bytebites.config.FirebaseConfig;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;

public class FollowDao {

    private Firestore db =
            FirebaseConfig.getFirestore();

    // =====================================================
    // FOLLOW USER
    // =====================================================

    public void followUser(
            String followerUid,
            String followingUid) {

        try {

            String documentId =
                    followerUid + "_" + followingUid;

            db.collection("follows")
                    .document(documentId)
                    .set(
                            new FollowData(
                                    followerUid,
                                    followingUid))
                    .get();

            System.out.println(
                    "User followed successfully!");

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    // =====================================================
    // UNFOLLOW USER
    // =====================================================

    public void unfollowUser(
            String followerUid,
            String followingUid) {

        try {

            String documentId =
                    followerUid + "_" + followingUid;

            db.collection("follows")
                    .document(documentId)
                    .delete()
                    .get();

            System.out.println(
                    "User unfollowed successfully!");

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    // =====================================================
    // CHECK FOLLOWING
    // =====================================================

    public boolean isFollowing(
            String followerUid,
            String followingUid) {

        try {

            String documentId =
                    followerUid + "_" + followingUid;

            DocumentSnapshot document =
                    db.collection("follows")
                            .document(documentId)
                            .get()
                            .get();

            return document.exists();

        } catch (Exception e) {

            e.printStackTrace();
        }

        return false;
    }

    // =====================================================
    // GET FOLLOWING USER IDS
    // =====================================================

    public List<String> getFollowingUsers(
            String followerUid) {

        List<String> followingUsers =
                new ArrayList<>();

        try {

            QuerySnapshot querySnapshot =
                    db.collection("follows")
                            .whereEqualTo(
                                    "followerUid",
                                    followerUid)
                            .get()
                            .get();

            for (
                    DocumentSnapshot document :
                    querySnapshot.getDocuments()) {

                FollowData followData =
                        document.toObject(
                                FollowData.class);

                if (followData != null) {

                    followingUsers.add(
                            followData.getFollowingUid());
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return followingUsers;
    }

    // =====================================================
    // FOLLOW DATA MODEL
    // =====================================================

    public static class FollowData {

        private String followerUid;
        private String followingUid;

        public FollowData() {
        }

        public FollowData(
                String followerUid,
                String followingUid) {

            this.followerUid =
                    followerUid;

            this.followingUid =
                    followingUid;
        }

        public String getFollowerUid() {
            return followerUid;
        }

        public void setFollowerUid(
                String followerUid) {

            this.followerUid =
                    followerUid;
        }

        public String getFollowingUid() {
            return followingUid;
        }

        public void setFollowingUid(
                String followingUid) {

            this.followingUid =
                    followingUid;
        }
    }
}