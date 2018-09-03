package br.sendlook.yeslap.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class Utils {

    public static final String USERS = "users";
    public static final String USER_IMAGES = "user_images";
    public static final String APP_CONFIG = "app_config";
    public static final String APP_VERSION = "app_version";
    public static final String CURRENT_VERSION = "current_version";
    public static final String PREF_NAME = "preferencesYeslap";

    public static final String URL = "url";
    public static final String USERNAME = "username";
    public static final String EMAIL = "email";
    public static final String EMAIL_USER = "email_user";
    public static final String PASSWORD = "password_user";
    public static final String IMAGE_1 = "image1";
    public static final String IMAGE_2 = "image2";
    public static final String IMAGE_3 = "image3";
    public static final String DISTANCE_APP = "distance_app";
    public static final String DISTANCE = "distance";
    public static final String DIFF_DISTANCE = "diff_distance";
    public static final String SINCE = "since";
    public static final String LAST_ID = "last_id";
    public static final String ID_SENDER_APP = "id_sender_app";
    public static final String ID_RECEIVER_APP = "id_receiver_app";
    public static final String ID_SENDER = "id_sender";
    public static final String ID_RECEIVER = "id_receiver";
    public static final String STATUS = "statususer";
    public static final String STATUS_CHAT_APP = "status_chat_app";
    public static final String STATUS_USER = "status_user";
    public static final String STATUS_CHAT = "status_chat";
    public static final String STATUS_USER_APP = "status_user_app";
    public static final String SPOTLIGHT = "spotlight";
    public static final String DONE = "done";
    public static final String FAVORITES = "favorites";
    public static final String MESSAGE_APP = "message_app";
    public static final String MESSAGES = "messages";
    public static final String CALENDAR = "calendar";
    public static final String CHAT = "chat";
    public static final String MESSAGES_STATUS = "messages_status";
    public static final String DATE = "date_app";
    public static final String LAST_MESSAGE = "last_message";
    public static final String ID_MESSAGES = "id_message_app";
    public static final String USER_SEARCH = "user_search";
    public static final String TURN_APP = "turn_app";
    public static final String UID_SENDER = "uidSender";
    public static final String LAST_SEEN_USER = "last_seen";
    public static final String UID_ADDRESSEE = "uidAddressee";


    public static final String SUNDAY_M = "sun_m";
    public static final String SUNDAY_A = "sun_a";
    public static final String SUNDAY_N = "sun_n";
    public static final String MONDAY_M = "mon_m";
    public static final String MONDAY_A = "mon_a";
    public static final String MONDAY_N = "mon_n";
    public static final String TUESDAY_M = "tue_m";
    public static final String TUESDAY_A = "tue_a";
    public static final String TUESDAY_N = "tue_n";
    public static final String WEDNESDAY_M = "wed_m";
    public static final String WEDNESDAY_A = "wed_a";
    public static final String WEDNESDAY_N = "wed_n";
    public static final String THURSDAY_M = "thu_m";
    public static final String THURSDAY_A = "thu_a";
    public static final String THURSDAY_N = "thu_n";
    public static final String FRIDAY_M = "fri_m";
    public static final String FRIDAY_A = "fri_a";
    public static final String FRIDAY_N = "fri_n";
    public static final String SATURDAY_M = "sat_m";
    public static final String SATURDAY_A = "sat_a";
    public static final String SATURDAY_N = "sat_n";

    public static final String MORNING = "morning";
    public static final String AFTERNOON = "afternoon";
    public static final String NIGHT = "night";

    public static final String PLAYSTORE_LINK = "playstore_link";
    public static final String REPORTS = "reports";

    public static final String OFFLINE = "offline";
    public static final String ONLINE = "online";

    public static final String GENDER_USER = "gender_user";
    public static final String AGE_USER_APP = "age_user_app";
    public static final String AGE_SEARCH_MIN_APP = "age_search_min_app";
    public static final String AGE_SEARCH_MAX_APP = "age_search_max_app";
    public static final String AGE_SEARCH_MIN = "age_search_min";
    public static final String AGE_SEARCH_MAX = "age_search_max";
    public static final String GENDER_SEARCH = "gender_search";
    public static final String GENDER_SEARCH_APP = "gender_search_app";
    public static final String AGE_SEARCH = "age_search";
    public static final String LATITUDE_USER_APP = "latitude_user_app";
    public static final String LONGITUDE_USER_APP = "longitude_user_app";
    public static final String LOCATION_USER = "location_user";
    public static final String AGE_USER = "age_user";
    public static final String IMAGE_APP = "image_app";
    public static final String IMAGE = "image";
    public static final String IMAGE_LOCAL = "image_local_app";

    public static final String URL_UPDATE_ID_USER = "http://yeslap-eu.umbler.net/signup/updateiduser.php";
    public static final String URL_SIGN_UP = "http://yeslap-eu.umbler.net/signup/signup.php";
    public static final String URL_SIGN_IN = "http://yeslap-eu.umbler.net/signin/signin.php";
    public static final String URL_CHECK_COMPLETE_PROFILE = "http://yeslap-eu.umbler.net/userprofile/checkcompleteprofile.php";
    public static final String URL_GET_USER_DATA = "http://yeslap-eu.umbler.net/userprofile/getuserdata.php";
    public static final String URL_UPDATE_USERNAME = "http://yeslap-eu.umbler.net/userprofile/updateusername.php";
    public static final String URL_STATUS_USER = "http://yeslap-eu.umbler.net/userprofile/statususer.php";
    public static final String URL_EXIST_PROFILE = "http://yeslap-eu.umbler.net/userprofile/existprofile.php";
    public static final String URL_LAST_SEEN = "http://yeslap-eu.umbler.net/userprofile/lastseen.php";
    public static final String URL_UPDATE_GENDER_USER = "http://yeslap-eu.umbler.net/userprofile/updategenderuser.php";
    public static final String URL_UPDATE_AGE_USER = "http://yeslap-eu.umbler.net/userprofile/updateageuser.php";
    public static final String URL_UPDATE_AGE_SEARCH = "http://yeslap-eu.umbler.net/userprofile/updateagesearch.php";
    public static final String URL_UPDATE_GENDER_SEARCH = "http://yeslap-eu.umbler.net/userprofile/updategendersearch.php";
    public static final String URL_UPDATE_LOCATION_USER = "http://yeslap-eu.umbler.net/userprofile/updatelocationuser.php";
    public static final String URL_UPDATE_EMAIL_USER = "http://yeslap-eu.umbler.net/userprofile/updateemailuser.php";
    public static final String URL_UPDATE_PASSWORD_USER = "http://yeslap-eu.umbler.net/userprofile/updatepassworduser.php";
    public static final String URL_UPDATE_CALENDAR_USER = "http://yeslap-eu.umbler.net/calendar/updatecalendaruser.php";
    public static final String URL_GET_CALENDAR_USER = "http://yeslap-eu.umbler.net/calendar/getcalendaruser.php";
    public static final String URL_DELETE_CALENDAR_USER = "http://yeslap-eu.umbler.net/calendar/deletecalendaruser.php";
    public static final String URL_FIND_USERS = "http://yeslap-eu.umbler.net/finduser/findusers.php";
    public static final String URL_ADD_NEW_FAVORITE = "http://yeslap-eu.umbler.net/userprofile/addnewfavorite.php";
    public static final String URL_ADD_NEW_REPORT = "http://yeslap-eu.umbler.net/userprofile/addnewreport.php";
    public static final String URL_GET_FAVORITES = "http://yeslap-eu.umbler.net/userprofile/getfavorites.php";
    public static final String URL_DELETE_FAVORITE = "http://yeslap-eu.umbler.net/userprofile/deletefavorite.php";
    public static final String URL_UPDATE_DISTANCE_USER = "http://yeslap-eu.umbler.net/userprofile/updatedistancesearch.php";
    public static final String URL_SEND_MESSAGE = "http://yeslap-eu.umbler.net/chat/sendmessage.php";
    public static final String URL_SAVE_UPDATE_CHAT = "http://yeslap-eu.umbler.net/chat/saveupdatechat.php";
    public static final String URL_CHECK_IF_HAVE_MESSAGE = "http://yeslap-eu.umbler.net/chat/checkifhavemessage.php";
    public static final String URL_UPDATE_STATUS_CHAT = "http://yeslap-eu.umbler.net/chat/updatestatuschat.php";
    public static final String URL_LOAD_MESSAGES = "http://yeslap-eu.umbler.net/chat/loadmessages.php";
    public static final String URL_LOAD_STATUS_USER = "http://yeslap-eu.umbler.net/chat/loadstatususer.php";
    public static final String URL_DELETE_MESSAGE = "http://yeslap-eu.umbler.net/chat/deletemessage.php";
    public static final String URL_LOAD_CHAT_MESSAGES = "http://yeslap-eu.umbler.net/chatmessages/loadchatmessages.php";
    public static final String URL_DELETE_CHAT_MESSAGE = "http://yeslap-eu.umbler.net/chatmessages/deletechatmessage.php";
    public static final String URL_UPDATE_IMAGE_USER_1 = "http://yeslap-eu.umbler.net/userprofile/updateimageuser1.php";
    public static final String URL_UPDATE_IMAGE_USER_2 = "http://yeslap-eu.umbler.net/userprofile/updateimageuser2.php";
    public static final String URL_UPDATE_IMAGE_USER_3 = "http://yeslap-eu.umbler.net/userprofile/updateimageuser3.php";

    public static final String EMAIL_APP = "email_app";
    public static final String PASSWORD_APP = "password_app";
    public static final String ID_USER_APP = "id_user_app";
    public static final String ID_REPORTED_APP = "id_reported_app";
    public static final String MSG_REPORT_APP = "msg_report_app";
    public static final String REASON_REPORT_APP = "reason_report_app";
    public static final String ID_FAVORITE_USER_APP = "id_favorite_user_app";
    public static final String LAST_SEEN = "last_seen_app";
    public static final String SIGN_UP_CODE = "signUp";
    public static final String SIGN_IN_CODE = "signin";
    public static final String GENDER_USER_APP = "gender_user_app";
    public static final String ID_USER = "id_user";
    public static final String CHECK_COMPLETE_PROFILE = "checkcompleteprofile";
    public static final String GET_USER_DATA = "getuserdata";
    public static final String USERNAME_USER = "username_user";
    public static final String UPDATE_USERNAME = "update_username";
    public static final String IMAGE_USER_1 = "image_user_1";
    public static final String IMAGE_USER_2 = "image_user_2";
    public static final String IMAGE_USER_3 = "image_user_3";
    public static final String EXIST_PROFILE = "existprofile";

    public static final String CODE_SUCCESS = "200";
    public static final String CODE_ERROR_EMAIL = "201";
    public static final String CODE_ERROR = "300";
    public static final String CODE_ERROR_USERNAME_EXISTS = "202";
    public static final String CODE_ERROR_USERNAME_NULL = "202";
    public static final String CODE_ERROR_NO_CALENDAR = "206";
    public static final String CODE_FAVORITE = "205";

    public static void toastySuccess(Context context, String msg) {
        Toasty.success(context, msg, Toast.LENGTH_LONG, true).show();
    }

    public static void toastyError(Context context, String msg) {
        Toasty.error(context, msg, Toast.LENGTH_LONG, true).show();
    }

    public static void toastyInfo(Context context, String msg) {
        Toasty.info(context, msg, Toast.LENGTH_LONG, true).show();
    }

    public static void toastyUsual(Context context, String msg, Drawable icon) {
        Toasty.normal(context, msg, Toast.LENGTH_LONG, icon).show();
    }

}
