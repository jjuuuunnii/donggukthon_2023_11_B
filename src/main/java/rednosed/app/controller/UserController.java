package rednosed.app.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rednosed.app.dto.common.ResponseDto;
import rednosed.app.security.oauth.info.PrincipalDetails;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {



}
