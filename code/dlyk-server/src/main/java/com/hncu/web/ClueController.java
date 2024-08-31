package com.hncu.web;

import com.github.pagehelper.PageInfo;
import com.hncu.model.TClue;
import com.hncu.query.ClueQuery;
import com.hncu.result.R;
import com.hncu.service.ClueService;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @Author caimeisahng
 * @Date 2024/7/22 21:50
 * @Version 1.0
 */

@RestController
public class ClueController {

    @Resource
    private ClueService clueService;

    @PreAuthorize(value = "hasAnyAuthority('clue:list')")
    @GetMapping(value = "/api/clues")
    public R cluePage(@RequestParam(value = "current" ,required = false) Integer current){
        // 传入参数为非必须传入
        if (current == null) {
            current = 1;
        }

        PageInfo<TClue> pageInfo = clueService.getClueByPage(current);
        return R.OK(pageInfo);
    }

    @PreAuthorize(value = "hasAnyAuthority('clue:import')")
    @PostMapping(value = "/api/importExcel")
    public R importExcel(MultipartFile file , @RequestHeader(value = "Authorization") String token) throws IOException {//file名字要和formData中的名字相同
        //System.out.println(file);
        clueService.importExcel(file.getInputStream(),token);
        return R.OK();
    }

    @GetMapping(value = "/api/clue/{phone}")
    public R checkPhone(@PathVariable(value = "phone") String phone){
        Boolean check= clueService.checkPhone(phone);
        return check ? R.OK() : R.FAIL();
    }

    @PreAuthorize(value = "hasAnyAuthority('clue:add')")
    @PostMapping(value = "/api/clue")
    public R addClue(ClueQuery clueQuery, @RequestHeader(value = "Authorization") String token) {
        clueQuery.setToken(token);
        int save = clueService.saveClue(clueQuery);

        return save >= 1 ? R.OK() : R.FAIL();
    }

    @PreAuthorize(value = "hasAnyAuthority('clue:view')")
    @GetMapping(value = "/api/clue/detail/{id}")
    public R loadClue(@PathVariable(value = "id") Integer id){
        TClue clue = clueService.getClueById(id);
        return R.OK(clue);
    }

    @PreAuthorize(value = "hasAnyAuthority('clue:edit')")
    @PutMapping(value = "/api/clue")
    public R editClue(ClueQuery clueQuery, @RequestHeader(value = "Authorization") String token) {
        clueQuery.setToken(token);
        int save = clueService.updateClue(clueQuery);

        return save >= 1 ? R.OK() : R.FAIL();
    }

    /**
     *  删除用户
     * @return R
     */
    @PreAuthorize(value = "hasAnyAuthority('clue:delete')")
    @DeleteMapping("/api/clue/{id}")
    public R delClue(@PathVariable(value = "id") Integer id){
        //int del= userService.delUserById(id);
        //int del= 1;
        return R.OK();
    }

}
