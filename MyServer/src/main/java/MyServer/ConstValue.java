package MyServer;

public class ConstValue {
//    public static String img_dir="/www/wwwroot/deepin/brilliantPic/";
//    public static String img_dir_user="/www/wwwroot/deepin/brilliantPic/user/";
//    public static String img_dir_pre="/www/wwwroot/deepin/brilliantPic/pre/";
//    public static String img_dir_after="/www/wwwroot/deepin/brilliantPic/after/";
//    public static String img_url_after="http://148.70.149.235:6001/images/after/";
//    public static String img_model_url="http://148.70.149.235:6001/images/models/";
//    public static final String transformPath="/media/gaoch/Data/Linux/fast-neural-style-keras-master";
//    public static final String python="python3.5";



    public static String img_dir_user="/root/BrilliantPaint/pic/user/";
    public static String img_dir_pre="/root/BrilliantPaint/pic/pre/";
    public static String img_dir_after="/root/BrilliantPaint/pic/after/";
    public static String img_url_after="http://148.70.149.235:1195/images/after/";
    public static String img_model_url="http://148.70.149.235:1195/images/models/";
    public static final String transformPath="/root/BrilliantPaint/pythonstage";
    public static final String python="python3";

    public volatile static boolean isProcessing=false;
    public static final String table_userinfo="userinfo";

    public static final int exp_processpic=10;
    public static final int exp_comments=10;
    public static final int exp_likes=2;
}
