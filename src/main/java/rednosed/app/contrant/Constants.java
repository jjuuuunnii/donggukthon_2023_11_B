package rednosed.app.contrant;

import java.util.List;

public class Constants {

    public static List<String> NO_NEED_AUTH_URLS = List.of(
            "/login/oauth2/code/kakao",
            "/oauth2/authorization/kakao",
            "/**"
    );

    public static final long TOTAL_CANVAS_MAKING_TIME = 600;
    public static final String T_STAMP = "stamp";
    public static final String T_SEAL = "seal";
    public static final int PIXEL_SIZE = 200;
}
