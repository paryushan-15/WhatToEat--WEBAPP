package com.bytebites.controller;

import java.util.List;

import com.bytebites.dao.FamilyMemberDao;
import com.bytebites.model.FamilyMember;

public class FamilyController {

    private FamilyMemberDao dao = new FamilyMemberDao();

    public boolean saveFamilyMember(FamilyMember member) {

        return dao.saveFamilyMember(member);
    }

    public List<FamilyMember> getFamilyMembersByUserId(String userId) {

        return dao.getFamilyMembersByUserId(userId);
    }

    public boolean deleteFamilyMember(String memberId) {

    return dao.deleteFamilyMember(memberId);
}

public boolean updateFamilyMember(FamilyMember member) {

    return dao.updateFamilyMember(member);
}
}