package com.marujang.domain.merchant.entity;

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
@Table(name = "merchant")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Merchant {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "user_id", nullable = false, unique = true)
	private User user;

	private String marketName;

	private String businessType;

	private String phone;

	@Builder
	public Merchant(User user, String marketName, String businessType, String phone) {
		this.user = user;
		this.marketName = marketName;
		this.businessType = businessType;
		this.phone = phone;
	}
}
