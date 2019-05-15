package com.example.util;

import com.apps.realestate.BuildConfig;
import com.example.item.ItemProperty;

import java.io.Serializable;
import java.util.ArrayList;

public class Constant implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;


    public static String SERVER_URL = BuildConfig.server_url;
    public static String SERVER_URL2 = "http://shivedic.in/cokarma/";

    public static final String IMAGE_PATH = SERVER_URL + "images/";

    public static final String LATEST_URL = SERVER_URL2 + "api.php?latest";
    public static final String FEATURE_URL = SERVER_URL2 + "api.php?feature";

    //public static final String ALL_PROPERTIES_URL = SERVER_URL + "api.php?all_properties";
    public static final String ALL_PROPERTIES_URL = SERVER_URL2 + "api.php?all_properties";

    public static final String PROPERTIES_TYPE = SERVER_URL2 + "api.php?type_list";

    public static final String REGISTER_URL = SERVER_URL2 + "JSON/" + "user_register_api.php?name=";

    public static final String LOGIN_URL = SERVER_URL2 + "JSON/" + "user_login_api.php?email=";

    public static final String FORGOT_PASSWORD_URL = SERVER_URL2 + "JSON/"  + "user_forgot_pass_api.php?email=";

    public static final String ABOUT_URL = SERVER_URL + "api.php?app_details";

    public static final String SEARCH_URL = SERVER_URL + "api.php?search_text=";
    public static final String SEARCHBASIC_URL = SERVER_URL2 +   "/JSON/basic_search.php";

    public static final String USER_PROFILE_URL = SERVER_URL + "user_profile_api.php?id=";

    public static final String USER_PROFILE_UPDATE_URL = SERVER_URL + "user_profile_update_api.php";

    public static final String SINGLE_PROPERTY_URL = SERVER_URL + "api.php?property_id=";

    public static final String HOME_URL = SERVER_URL2 + "api.php?home";

    public static final String RATING_URL = SERVER_URL + "api_rating.php?property_id=";

    public static final String MOST_POPULAR_URL = SERVER_URL2 + "api.php?popular";

    public static final String CONTACT_US_URL = SERVER_URL + "api_contact.php?name=";

    public static final String UPLOAD_PROPERTIES_URL = SERVER_URL + "user_property_add_api.php";

    public static final String MY_PROPERTIES_URL = SERVER_URL + "user_property_list_api.php?user_id=";

    public static final String ADVANCE_SEARCH_URL = SERVER_URL + "api.php?all_properties&verified=";

    public static final String DISTANCE_URL = SERVER_URL + "api.php?properties_distance&user_lat=";

    public static final String PRICE_URL = SERVER_URL + "api.php?properties_min_max_price=";

    public static final String FLEXIDP_INFO_URL = SERVER_URL2 + "/JSON/getflexidpinfo.php";

    public static final String propdetails_URL = SERVER_URL2 + "/JSON/property_detail.php";
    public static final String LOCATIONVAL_URL = SERVER_URL2 + "/JSON/locationvalues.php";

    public static final String ARRAY_NAME = "REAL_ESTATE_APP";


    public static final String PROPERTY_ID = "p_id";
    public static final String PROPERTY_TITLE = "property_name";
    public static final String PROPERTY_DESC = "property_description";
    public static final String PROPERTY_PHONE = "property_phone";
    public static final String PROPERTY_ADDRESS = "property_address";
    public static final String PROPERTY_LATITUDE = "property_map_latitude";
    public static final String PROPERTY_LONGITUDE = "property_map_longitude";
    public static final String PROPERTY_IMAGE = "property_thumbnail_b";
    public static final String PROPERTY_BED = "property_bed";
    public static final String PROPERTY_BATH = "property_bath";
    public static final String PROPERTY_AREA = "property_area";
    public static final String PROPERTY_AMENITIES = "property_amenities";
    public static final String PROPERTY_PRICE = "property_price";
    public static final String PROPERTY_RATE = "rate_avg";
    public static final String PROPERTY_PURPOSE = "property_purpose";
    public static final String PROPERTY_FLOOR_PLAN = "property_floor_plan";
    public static final String PROPERTY_TOTAL_RATE= "total_rate";
    public static final String PROPERTY_FUR= "furnishing";
    public static final String PROPERTY_VERY= "verified";

    public static final String PLACE_ID = "p_id";
    public static final String PLACE_TITLE = "place_name";
    public static final String PLACE_WDSTART = "pl_wd_start";
    public static final String PLACE_WDEND = "pl_wd_end";
    public static final String PLACE_TIME_START = "pl_timing_start";
    public static final String PLACE_TIME_END = "pl_timing_end";
    public static final String PLACE_DESC = "pl_overview";
    public static final String PLACE_OWNER_MSG = "pl_owner_msg";
    public static final String PLACE_OFFER = "pl_offers";
    public static final String PLACE_PHONE = "place_phone";
    public static final String PLACE_ADDRESS = "place_address";
    public static final String PLACE_LOCALITY = "place_localty";
    public static final String PLACE_CITY = "place_city";
    public static final String PLACE_STATE = "place_state";
    public static final String PLACE_LATITUDE = "place_map_latitude";
    public static final String PLACE_LONGITUDE = "place_map_longitude";
    public static final String PLACE_IMAGE = "place_thumbnail_b";
    //public static final String PROPERTY_AMENITIES = "property_amenities";
    //public static final String PROPERTY_PRICE = "property_price";
    public static final String PLACE_RATE = "rate_avg";
    public static final String PLACE_PURPOSE = "type_name";
    public static final String PLACE_FLOOR_PLAN = "place_floor_plan";
    public static final String PLACE_TOTAL_RATE= "total_rate";
    //public static final String PROPERTY_FUR= "furnishing";
    //public static final String PROPERTY_VERY= "verified";


    public static final String TYPE_ID = "tid";
    public static final String TYPE_NAME = "type_name";

    public static final String DP_TYPE = "tid";
    public static final String DP_DUR = "duration";
    public static final String DP_PRICE_INR = "price_inr";
    public static final String DP_PRICE_USD = "price_usd";


    public static final String PACK_ID = "p_id";
    public static final String PACK_NAME = "pack_name";
    public static final String PACK_DESC = "pack_description";
    public static final String PACK_DURATION = "pack_duration";
    public static final String PACK_CATEGORY = "pack_category";
    public static final String PACK_SEATS = "pack_seats";
    public static final String PACK_QUANTITY = "pack_qty";
    public static final String PACK_STARTTIME = "pack_start_time";
    public static final String PACK_ENDTIME = "pack_end_time";
    public static final String PACK_PRICE = "pack_price";



    public static final String APP_NAME = "app_name";
    public static final String APP_IMAGE = "app_logo";
    public static final String APP_VERSION = "app_version";
    public static final String APP_AUTHOR = "app_author";
    public static final String APP_CONTACT = "app_contact";
    public static final String APP_EMAIL = "app_email";
    public static final String APP_WEBSITE = "app_website";
    public static final String APP_DESC = "app_description";
    public static final String APP_PRIVACY_POLICY = "app_privacy_policy";

    public static final String GALLERY_ARRAY_NAME = "galley_image";
    public static final String GALLERY_IMAGE_NAME = "image_name";

    public static final String USER_NAME = "name";
    public static final String USER_ID = "user_id";
    public static final String USER_EMAIL = "email";
    public static final String USER_PHONE = "phone";
    public static final String USER_IMAGE = "user_image";
    public static final String USER_ADDRESS = "address";

    public static final String HOME_LATEST_ARRAY = "latest_property";
    public static final String HOME_FEATURED_ARRAY = "featured_property";
    public static final String HOME_POPULAR_ARRAY="popular_property";

    public static int GET_SUCCESS_MSG;
    public static final String MSG = "msg";
    public static final String SUCCESS = "success";
    public static final String ADS_BANNER_ID="banner_ad_id";
    public static final String ADS_FULL_ID="interstital_ad_id";
    public static final String ADS_BANNER_ON_OFF="banner_ad";
    public static final String ADS_FULL_ON_OFF="interstital_ad";
    public static final String ADS_PUB_ID="publisher_id";
    public static final String ADS_CLICK="interstital_ad_click";
    public static String SAVE_ADS_BANNER_ID,SAVE_ADS_FULL_ID,SAVE_ADS_BANNER_ON_OFF,SAVE_ADS_FULL_ON_OFF,SAVE_ADS_PUB_ID,SAVE_ADS_CLICK;
    public static String SEARCH_FIL_ID="";
    public static ArrayList<ItemProperty> mList=new ArrayList<>();
    public static String USER_LATITUDE ;
    public static String USER_LONGITUDE;
}
