package com.socialanalytics.dto.response;

import java.util.List;
import java.util.function.Function;

import org.springframework.data.domain.Page;

// Response phân trang: { data, total, page, size, totalPages }, page bắt đầu từ 1
public record PageResponse<T>(List<T> data, long total, int page, int size, int totalPages) {

	// Page của Spring Data bắt đầu từ 0 -> đổi sang 1
	public static <E, T> PageResponse<T> from(Page<E> page, Function<? super E, ? extends T> mapper) {
		List<T> data = page.getContent().stream().<T>map(mapper).toList();
		return new PageResponse<>(data, page.getTotalElements(), page.getNumber() + 1, page.getSize(),
				page.getTotalPages());
	}
}
