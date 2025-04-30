package top.fusuccess.aidemo.demos.module.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.fusuccess.aidemo.demos.common.ApiResponse;
import top.fusuccess.aidemo.demos.utils.RsaUtil;

@RestController
@RequestMapping("/rsa")
public class RsaController {

    @Autowired
    private RsaUtil rsaUtil;

    @GetMapping("/publicKey")
    public ResponseEntity<ApiResponse> getRsaPublicKey(){
        return ResponseEntity.ok(new ApiResponse("success", rsaUtil.getPublicKey()));
    }
}
