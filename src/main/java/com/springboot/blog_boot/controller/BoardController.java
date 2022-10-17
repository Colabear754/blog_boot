package com.springboot.blog_boot.controller;

import com.springboot.blog_boot.domain.BoardVO;
import com.springboot.blog_boot.mapper.BoardMapper;
import io.swagger.annotations.*;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "블로그 API")
@RestController
@RequestMapping("/blog/*")
public class BoardController {
    private static final int PAGESIZE = 6;
    private static final int BLOCKSIZE = 10;
    @Autowired
    private BoardMapper boardDao;

    @ApiOperation(value = "블로그 글 목록 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "category_id", value = "글을 조회할 카테고리 번호(0은 전체 조회)", required = true),
            @ApiImplicitParam(name = "pageNum", value = "글을 조회할 페이지", defaultValue = "1")
    })
    @GetMapping("/document-list")
    public List<BoardVO> document_list(@RequestParam("category_id") String id, @RequestParam(required = false) String pageNum, Model model) {
        // 블로그 글 목록 조회
        int category_id = id == null ? -1 : Integer.parseInt(id);
        Map<String, Object> input = new HashMap<>();
        int count = boardDao.getDocumentCount(category_id); // 글 전체 개수

        if (pageNum == null) {
            pageNum = "1";
        }

        int currentPage = Integer.parseInt(pageNum);    // 현재 페이지
        int lastPage = count / PAGESIZE + (count % PAGESIZE > 0 ? 1 : 0);   // 마지막 페이지
        int currentBlock = currentPage / BLOCKSIZE + (currentPage % BLOCKSIZE > 0 ? 1 : 0); // 현재 페이지 번호가 있는 블록(1~10페이지라면 1블록, 11~20페이지라면 2블록...)
        int start = (currentPage - 1) * PAGESIZE + 1;   // 불러오기 시작할 번호
        int end = currentPage * PAGESIZE;   // 불러오기 끝낼 번호

        input.put("category_id", category_id);
        input.put("start", start);
        input.put("end", end);

        List<BoardVO> documentList = boardDao.getDocuments(input);

        model.addAttribute("count", count);
        model.addAttribute("documentList", documentList);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("lastPage", lastPage);
        model.addAttribute("currentBlock", currentBlock);

        return documentList;
    }

    @ApiOperation(value = "블로그 글 보기")
    @ApiImplicitParam(name = "seq", value = "조회할 글 번호", required = true, paramType = "path", example = "0")
    @GetMapping("/document/{seq}")
    public BoardVO document(@PathVariable int seq, Model model) {
        // 블로그 글 보기
        BoardVO document = boardDao.getDocument(seq);
        if (document != null) {
            boardDao.increaseViewCnt(seq);
            document.setView_cnt(document.getView_cnt() + 1);
        }

        model.addAttribute("document", document);

        return document;
    }

    @ApiOperation(value = "글 추천")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "추천자"),
            @ApiImplicitParam(name = "seq", value = "추천할 글 번호", required = true, example = "0")
    })
    @PostMapping("/document/{seq}/like")
    public String post_like(@PathVariable int seq, @RequestParam(required = false) String id, HttpServletRequest request, Model model) {
        /*
         * 글 추천
         * result 값에 따른 결과
         * -1: 작업 실패
         * 0: 이미 추천된 게시물
         * 1: 추천 성공
         */
        int result = -1;
        Map<String, Object> input = new HashMap<>();

        input.put("seq", seq);

        if (id == null) {   // 로그인 중이 아닐 때
            input.put("id", request.getRemoteAddr());
        } else {    // 로그인 중일 때
            input.put("id", id);
        }

        if (boardDao.isLike(input)) {
            result = 0;
        } else {
            result = boardDao.like(input);
        }

        boardDao.updateLikeCnt(seq);
        model.addAttribute("result", result);

        return "추천 결과 : " + result;
    }

    @ApiOperation("글 추천 취소")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "추천자"),
            @ApiImplicitParam(name = "seq", value = "추천 취소할 글 번호", required = true, example = "0")
    })
    @DeleteMapping("/document/{seq}/like")
    public String delete_like(@PathVariable int seq, @RequestParam(required = false) String id, HttpServletRequest request, Model model) {
        /*
         * 글 추천 취소
         * result 값에 따른 결과
         * -1: 작업 실패
         * 0: 추천하지 않은 게시물
         * 1: 추천 취소 성공
         */
        int result = -1;
        Map<String, Object> input = new HashMap<>();

        input.put("seq", seq);

        if (id == null) {   // 로그인 중이 아닐 때
            input.put("id", request.getRemoteAddr());   // ip 가져오기
        } else {    // 로그인 중일 때
            input.put("id", id);
        }

        result = boardDao.cancelLike(input);

        boardDao.updateLikeCnt(seq);
        model.addAttribute("result", result);

        return "추천 취소 결과 : " + result;
    }

    @ApiOperation("글 작성")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "subject", value = "글 제목", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "content", value = "글 내용", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "category_id", value = "글을 분류할 카테고리의 일련번호", dataType = "int", paramType = "query")

    })
    @PostMapping(value = "/write", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BoardVO write(
            @RequestParam String subject,
            @RequestParam String content,
            @RequestParam(required = false) String category_id,
            @ApiParam("업로드 할 썸네일") @RequestBody(required = false) MultipartFile uploadFile,
            HttpServletRequest request,
            Model model) throws IOException {
        // 글 작성
        int result = -1;
        StringBuilder dir = new StringBuilder("D:\\blog\\img\\"); // 썸네일 저장경로 / jar로 배포하는 springboot 프로젝트는 context.getRealPath() 사용 시 임시 폴더에 저장되어 절대경로로 설정
        String filename = null; // 썸네일 파일 명

        Files.createDirectories(Paths.get(dir.toString()));    // 썸네일을 저장할 디렉토리가 없으면 생성

        if (uploadFile != null && !uploadFile.isEmpty()) {
            String extension = FilenameUtils.getExtension(uploadFile.getOriginalFilename());    // 확장자
            long timeMillis = System.currentTimeMillis();   // 무작위 파일명을 위한 현재시간
            int random = (int) (Math.random() * 90000) + 10000;    // 무작위 파일명을 위한 10000~99999의 무작위 정수
            filename = String.valueOf(random) + timeMillis + "." + extension;   // 무작위 정수와 현재시간을 연결한 새로운 파일명

            uploadFile.transferTo(new File(dir + filename));    // 파일을 서버에 등록
        }

        BoardVO document = new BoardVO();
        document.setSubject(subject);
        document.setContent(content);
        document.setCategory_id(category_id != null ? Integer.parseInt(category_id) : 0);
        document.setThumbnail(filename);
        boardDao.write(document);
        result = document.getSeq();
        document = boardDao.getDocument(result);

        model.addAttribute("result", result);

        return document;
    }

    @ApiOperation(value = "글 수정")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "seq", value = "수정할 글 번호", required = true, paramType = "path"),
            @ApiImplicitParam(name = "subject", value = "글 제목", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "content", value = "글 내용", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "category_id", value = "글을 분류할 카테고리의 일련번호", dataType = "int", paramType = "query", example = "0")

    })
    @PostMapping(value = "/document/update/{seq}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BoardVO update(
            @PathVariable int seq,
            @RequestParam String subject,
            @RequestParam String content,
            @RequestParam(required = false) String category_id,
            @ApiParam("업로드 할 썸네일") @RequestBody(required = false) MultipartFile uploadFile,
            HttpServletRequest request,
            Model model) throws IOException {
        // 글 수정
        int result = -1;
        BoardVO document = boardDao.getDocument(seq);
        StringBuilder dir = new StringBuilder("D:\\blog\\img\\"); // 썸네일 저장경로 / jar로 배포하는 springboot 프로젝트는 context.getRealPath() 사용 시 임시 폴더에 저장되어 절대경로로 설정
        String filename; // 썸네일 파일 명

        Files.createDirectories(Paths.get(dir.toString()));    // 썸네일을 저장할 디렉토리가 없으면 생성

        if (uploadFile != null && !uploadFile.isEmpty()) {
            dir.append("\\");
            File pre_thumb = new File(dir + document.getThumbnail());   // 기존 썸네일
            String extension = FilenameUtils.getExtension(uploadFile.getOriginalFilename());    // 확장자
            long timeMillis = System.currentTimeMillis();   // 무작위 파일명을 위한 현재시간
            int random = (int) (Math.random() * 90000) + 10000;    // 무작위 파일명을 위한 10000~99999의 무작위 정수
            filename = String.valueOf(random) + timeMillis + "." + extension;   // 무작위 정수와 현재시간을 연결한 새로운 파일명

            uploadFile.transferTo(new File(dir + filename));    // 파일을 서버에 등록
            pre_thumb.delete(); // 기존 썸네일 파일 삭제
            document.setThumbnail(filename);    // 새 썸네일 지정
        }

        document.setSubject(subject);
        document.setContent(content);
        document.setCategory_id(category_id != null ? Integer.parseInt(category_id) : 0);
        result = boardDao.update(document);
        document = boardDao.getDocument(seq);

        model.addAttribute("result", result);

        return document;
    }

    @ApiOperation("글 삭제")
    @ApiImplicitParam(name = "seq", value = "삭제할 글 번호", required = true, example = "0")
    @DeleteMapping("/document/delete/{seq}")
    public String delete(@PathVariable int seq, HttpServletRequest request, Model model) {
        // 글 삭제
        String thumbnail = boardDao.getDocument(seq).getThumbnail();
        if (thumbnail != null && !thumbnail.isBlank()) {
            File realFile = new File(request.getServletContext().getRealPath("\\resources\\blog\\img") + "\\" + thumbnail);
            realFile.delete();
        }

        model.addAttribute("result", boardDao.delete(seq) > 0);

        return "글 삭제 결과 : " + model.getAttribute("result");
    }
}
