package com.online.auction.controller;

import com.online.auction.dto.InvoiceDTO;
import com.online.auction.dto.ItemDTO;
import com.online.auction.dto.SuccessResponse;
import com.online.auction.dto.TradebookDTO;
import com.online.auction.exception.ServiceException;
import com.online.auction.model.User;
import com.online.auction.service.TradebookService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.online.auction.constant.AuctionConstants.API_VERSION_V1;
import static com.online.auction.constant.AuctionConstants.USER;

@RestController
//@CrossOrigin(origins = "http://172.17.3.242:4200")
//@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(API_VERSION_V1 + USER)
@RequiredArgsConstructor
public class TradebookController {

    @Autowired
    private TradebookService tradebookService;

    @GetMapping("/getTradebook")
    public ResponseEntity<SuccessResponse<List<TradebookDTO>>> getAllItems(@AuthenticationPrincipal User user) throws ServiceException {
        List<TradebookDTO> items = tradebookService.getAllTradesByUser(user);
        SuccessResponse<List<TradebookDTO>> response = new SuccessResponse<>(200, HttpStatus.OK, items);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/getInvoice")
    public ResponseEntity<SuccessResponse<InvoiceDTO>> getInvoice(@RequestParam("auctionId") int auctionId, @AuthenticationPrincipal User user) throws ServiceException {
        InvoiceDTO invoice = tradebookService.getInvoiceByAuctionId(auctionId);
        SuccessResponse<InvoiceDTO> response = new SuccessResponse<>(200, HttpStatus.OK, invoice);
        return ResponseEntity.ok(response);
    }
}
