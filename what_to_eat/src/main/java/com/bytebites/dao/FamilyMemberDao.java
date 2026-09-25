package com.bytebites.dao;

import java.util.ArrayList;
import java.util.List;

import com.bytebites.config.FirebaseConfig;
import com.bytebites.model.FamilyMember;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;

public class FamilyMemberDao {

    private Firestore db = FirebaseConfig.getFirestore();

    public boolean saveFamilyMember(FamilyMember member) {

        try {

            System.out.println("Saving Member...");
            System.out.println(member.getMemberId());
            System.out.println(member.getName());

            db.collection("familyMembers")
                    .document(member.getMemberId())
                    .set(member)
                    .get();

            System.out.println("Family Member Saved");

            return true;

        } catch (Exception e) {

            e.printStackTrace();
            return false;
        }
    }

    public List<FamilyMember> getFamilyMembersByUserId(String userId) {

        List<FamilyMember> members = new ArrayList<>();

        try {

            ApiFuture<QuerySnapshot> future = db.collection("familyMembers")
                    .whereEqualTo("userId", userId)
                    .get();

            QuerySnapshot snapshot = future.get();

            for (DocumentSnapshot doc : snapshot.getDocuments()) {

                FamilyMember member = doc.toObject(FamilyMember.class);

                members.add(member);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return members;
    }

    public boolean deleteFamilyMember(String memberId) {

        try {

            db.collection("familyMembers")
                    .document(memberId)
                    .delete()
                    .get();

            System.out.println("Family Member Deleted");
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }public boolean updateFamilyMember(FamilyMember member) {

    try {

        db.collection("familyMembers")
                .document(member.getMemberId())
                .set(member)
                .get();

        System.out.println("Family Member Updated");
        return true;

    } catch (Exception e) {
        e.printStackTrace();
    }

    return false;
}
}