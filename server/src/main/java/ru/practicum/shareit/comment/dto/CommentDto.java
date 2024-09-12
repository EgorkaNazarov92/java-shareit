package ru.practicum.shareit.comment.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CommentDto {
	private Long id;
	@NonNull
	private String text;
	private String authorName;
	private LocalDateTime created;
}
