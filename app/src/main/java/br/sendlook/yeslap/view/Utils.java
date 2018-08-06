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

    public static final String USERNAME = "username";
    public static final String EMAIL = "email";
    public static final String IMAGE_1 = "image1";
    public static final String IMAGE_2 = "image2";
    public static final String IMAGE_3 = "image3";
    public static final String SINCE = "since";
    public static final String UID = "uid";
    public static final String STATUS = "status";
    public static final String SPOTLIGHT = "spotlight";
    public static final String FAVORITES = "favorites";
    public static final String MESSAGE = "message";
    public static final String MESSAGES = "messages";
    public static final String CALENDAR = "calendar";
    public static final String LAST_SEEN = "lastSeen";
    public static final String CHAT_MESSAGES = "chatmessages";
    public static final String MESSAGES_STATUS = "messages_status";
    public static final String DATE = "date";
    public static final String KEY = "key";
    public static final String UID_SENDER = "uidSender";
    public static final String UID_ADDRESSEE = "uidAddressee";

    public static final String SUNDAY = "1";
    public static final String MONDAY = "2";
    public static final String TUESDAY = "3";
    public static final String WEDNESDAY = "4";
    public static final String THURSDAY = "5";
    public static final String FRIDAY = "6";
    public static final String SATURDAY = "7";

    public static final String MORNING = "morning";
    public static final String AFTERNOON = "afternoon";
    public static final String NIGHT = "night";

    public static final String PLAYSTORE_LINK = "playstore_link";
    public static final String REPORTS = "reports";

    public static final String GENDER_USER = "genderUser";
    public static final String GENDER_SEARCH = "genderSearch";
    public static final String AGE_SEARCH_MIN = "ageSearchMin";
    public static final String AGE_SEARCH_MAX = "ageSearchMax";
    public static final String LATITUDE_USER = "latitudeUser";
    public static final String LONGITUDE_USER = "longitudeUser";
    public static final String AGE_USER = "ageUser";

    //TODO: Colocar a URL de onde esta o arquivo
    public static final String URL_SIGN_UP = "";
    public static final String EMAIL_APP = "email_app";
    public static final String PASSWORD_APP = "password_app";
    public static final String SIGN_UP_CODE = "signUp";

    public static final String CODE_SUCCESS = "200";
    public static final String CODE_ERROR_EMAIL = "201";
    public static final String CODE_ERROR = "300";

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
