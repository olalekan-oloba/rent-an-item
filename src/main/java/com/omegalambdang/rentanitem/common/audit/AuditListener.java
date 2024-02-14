package com.omegalambdang.rentanitem.common.audit;

import com.omegalambdang.rentanitem.util.FellowshipDateUtils;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class AuditListener {

  @PrePersist
  public void onSave(Object o) {
    if (o instanceof Auditable) {
      Auditable audit = (Auditable) o;
      AuditSection auditSection = audit.getAuditSection();
      auditSection.setDateModified(FellowshipDateUtils.now());
      String createdBy = getUsernameOfAuthenticatedUser();
      auditSection.setCreatedBy(createdBy);
      auditSection.setModifiedBy(createdBy);
      if (auditSection.getDateCreated() == null) {
        auditSection.setDateCreated(FellowshipDateUtils.now());
      }
      audit.setAuditSection(auditSection);
    }
  }

  @PreUpdate
  public void onUpdate(Object o) {
    if (o instanceof Auditable) {
      Auditable audit = (Auditable) o;
      AuditSection auditSection = audit.getAuditSection();
      if(auditSection!=null) {
        auditSection.setDateModified(FellowshipDateUtils.now());
        auditSection.setModifiedBy(getUsernameOfAuthenticatedUser());

        if (auditSection.getDateCreated() == null) {
          auditSection.setDateCreated(FellowshipDateUtils.now());
        }
        audit.setAuditSection(auditSection);
      }
    }
  }

  private String getUsernameOfAuthenticatedUser() {

    try {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      if (authentication == null || !authentication.isAuthenticated()) {
        return "anonymous user";
      }
      Object principal = authentication.getPrincipal();
      if (principal.getClass().equals(String.class)) {
        return (String) principal;
      }

      UserDetails userDetails = (UserDetails) authentication.getPrincipal();
     /* if (userDetails.getClass().equals(SecuredAdminUserInfo.class)) {
        return ((SecuredAdminUserInfo) userDetails).getUser().getEmail();
      }*/
    }catch(Exception e){
      e.printStackTrace();
    }
    return "anonymous user";
  }
}
