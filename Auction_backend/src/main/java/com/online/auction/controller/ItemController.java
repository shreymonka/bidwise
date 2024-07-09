package com.online.auction.controller;

import com.online.auction.dto.ItemDTO;
import com.online.auction.dto.SuccessResponse;
import com.online.auction.exception.ServiceException;
import com.online.auction.model.User;
import com.online.auction.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import static com.online.auction.constant.AuctionConstants.API_VERSION_V1;
import static com.online.auction.constant.AuctionConstants.ITEM;

@RestController
@CrossOrigin(origins = "http://172.17.3.242:4200")
@RequestMapping(API_VERSION_V1 + ITEM)
@RequiredArgsConstructor
public class ItemController {
    @Autowired
    private ItemService itemService;

    @PostMapping("/additem")
    public ResponseEntity<SuccessResponse<String>> register(
            @RequestPart("itemDTO") ItemDTO itemDTO,
            @RequestPart("file") MultipartFile files,
            @AuthenticationPrincipal User user
    ) throws ServiceException {
        String addResponseItem = itemService.addItem(itemDTO,files, user);
        SuccessResponse<String> response = new SuccessResponse<>(200, HttpStatus.OK, addResponseItem);
        return ResponseEntity.ok(response);
    }
}
