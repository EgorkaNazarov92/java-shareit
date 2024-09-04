package ru.practicum.shareit.comment;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentMapper {
	public static Comment mapToComment(CommentDto commentDto, User user, Item item) {
		Comment comment = new Comment();
		comment.setText(commentDto.getText());
		comment.setItem(item);
		comment.setAuthor(user);
		return comment;
	}

	public static CommentDto mapToCommentDto(Comment comment) {
		return new CommentDto(
				comment.getId(),
				comment.getText(),
				comment.getAuthor().getName(),
				comment.getCreated()
		);
	}
}
