package com.example.post.dto.response;

import com.example.post.dto.request.PageRequestDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class PageResponseDTO<E> {
    private List<E> dtoList;
    private List<Integer> pageNumList;
    private PageRequestDTO pageRequestDTO;
    private boolean prev, next; // 이전 다음
    private int totalCount, prevPage, nextPage, totalPage, current;

    // Page Common Object
    @Builder(builderMethodName = "withAll")
    public PageResponseDTO(List<E> dtoList, PageRequestDTO pageRequestDTO, long total) {
        this.dtoList = dtoList;
        this.pageRequestDTO = pageRequestDTO;
        this.totalCount = (int) total;

        // 끝 페이지
        int end = (int) ((Math.ceil(pageRequestDTO.getPage() / 10.0)) * 10);
        // 시작 페이지
        int start = end - 9;
        // 가장 마지막 페이지
        int last = (int) (Math.ceil(totalCount / (double) pageRequestDTO.getSize()));

        end = Math.min(end, last);
        // 이전 <
        this.prev = start > 1;
        // 다음 >
        this.next = totalCount > end * pageRequestDTO.getSize();

        // 전체 페이지 번호 목록
        this.pageNumList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());

        // 이전 페이지 이동 시
        this.prevPage = prev ? start - 1 : 0;
        this.nextPage = next ? end + 1 : 0;

        this.totalPage = this.pageNumList.size();

        this.current = pageRequestDTO.getPage();
    }
}
