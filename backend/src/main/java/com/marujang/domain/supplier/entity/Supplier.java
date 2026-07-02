package com.marujang.domain.supplier.entity;

import com.marujang.domain.user.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "supplier")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Supplier {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "user_id", nullable = false, unique = true)
	private User user;

	private String companyName;

	private String businessNumber;

	private String phone;

	@Builder
	public Supplier(User user, String companyName, String businessNumber, String phone) {
		this.user = user;
		this.companyName = companyName;
		this.businessNumber = businessNumber;
		this.phone = phone;
	}
}
