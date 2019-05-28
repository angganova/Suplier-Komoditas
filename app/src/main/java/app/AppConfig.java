package app;

/**
 * Created by A10 on 16/01/2017.
 */
public class AppConfig {
    //Main Server

    public static String URL_MAIN = "http://www.sastroman.empatenam.com/";

    // Server user login url
    public static String URL_LOGIN = URL_MAIN + "android_login_api/Login.php";
    // Server user register url
    public static String URL_REGISTER = URL_MAIN + "android_login_api/Register.php";
    // Server Order
    public static String URL_ORDERIN = URL_MAIN + "android_login_api/OrderIn.php";
    public static String URL_ORTO = URL_MAIN + "android_login_api/ListOrder.php";
    public static String URL_LIST_ORTO = URL_MAIN + "android_login_api/ListOrderToday.php";
    public static String URL_LIST_TOTO = URL_MAIN + "android_login_api/TotalToday.php";
    public static String URL_ORTOTAL = URL_MAIN + "android_login_api/Ortotal.php";
    public static String URL_HISTORY = URL_MAIN + "android_login_api/ListRiwayat.php";
    public static String URL_CP = URL_MAIN + "android_login_api/ChangePassword.php";


    // Server user data url
    public static String URL_CHECKPOIN = URL_MAIN + "android_login_api/CheckPoin.php";
    public static String URL_SETTING = URL_MAIN + "android_login_api/ListSetting.php";
    public static String URL_PRODUCTS = URL_MAIN + "android_login_api/ListProducts.php";
    public static String URL_POIN = URL_MAIN + "android_login_api/ListPoinItems.php";
    public static String URL_CHPOIN = URL_MAIN + "android_login_api/ChangePoin.php";
    public static String URL_SYARAT = URL_MAIN + "android_login_api/ListSyarat.php";
    public static String URL_UPLOAD = URL_MAIN + "android_login_api/UserUpload.php";


    // Server user data image url
    public static String URL_IMAGE= URL_MAIN + "android_login_api/image/";
    public static String URL_PIC= URL_MAIN + "android_login_api/user_image/";
}
