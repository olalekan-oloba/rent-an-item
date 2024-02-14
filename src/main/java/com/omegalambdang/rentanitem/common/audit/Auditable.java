package com.omegalambdang.rentanitem.common.audit;

public interface Auditable {
	
	AuditSection getAuditSection();
	
	void setAuditSection(AuditSection audit);
}
