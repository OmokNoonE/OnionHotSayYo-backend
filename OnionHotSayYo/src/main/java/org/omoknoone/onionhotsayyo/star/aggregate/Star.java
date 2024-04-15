package org.omoknoone.onionhotsayyo.star.aggregate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Entity
@Table(name = "star")
public class Star {

	@Id
	@Column(name = "star_id")
	private int starId;

	@JoinColumn(name = "post_id")
	private Integer postId;

	@JoinColumn(name = "member_id")
	private String memberId;

	// true = 좋아요 , false = 싫어요
	@Column(name = "status")
	private boolean status;
}
