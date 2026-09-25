package com.bytebites.controller;

import java.util.List;

import com.bytebites.dao.FollowDao;

public class FollowController {

    private FollowDao followDao =
            new FollowDao();

    // =====================================================
    // FOLLOW USER
    // =====================================================

    public void followUser(
            String followerUid,
            String followingUid) {

        followDao.followUser(
                followerUid,
                followingUid);
    }

    // =====================================================
    // UNFOLLOW USER
    // =====================================================

    public void unfollowUser(
            String followerUid,
            String followingUid) {

        followDao.unfollowUser(
                followerUid,
                followingUid);
    }

    // =====================================================
    // CHECK FOLLOWING
    // =====================================================

    public boolean isFollowing(
            String followerUid,
            String followingUid) {

        return followDao.isFollowing(
                followerUid,
                followingUid);
    }

    // =====================================================
    // GET FOLLOWING USERS
    // =====================================================

    public List<String> getFollowingUsers(
            String followerUid) {

        return followDao.getFollowingUsers(
                followerUid);
    }
}