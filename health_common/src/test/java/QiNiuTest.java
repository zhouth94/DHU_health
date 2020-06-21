import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.junit.Test;

/**
 * @author zhou
 * @create 2020/5/2
 */
public class QiNiuTest {
    /**
     * 使用七牛云提供的SDK实现将本地图片上传到七牛云服务器
     */
    @Test
    public void test1() {
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone0()); //华南区
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
        String accessKey = "P9hEdlDMSyaGfDlCZo7bV8vRBI2q57U9Mi6u1sG4"; //AK
        String secretKey = "mVDOcMCNuUaarNDtooBHhsM_5SJPE8cnZRpIAUTv"; //SK
        String bucket = "jiangx"; //存储空间名称
        //如果是Windows情况下，格式是 D:\\qiniu\\test.png
        String localFilePath = "D:\\ORACLE\\JAVA26\\2019最新版传智黑马\\传智健康\\项目资料\\day04\\素材\\图片资源\\ac3b5a4d-33a5-4f37-bd49-99e06ce17d202.jpg";
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        //String key = null;
        String key = "abc.jpg"; //指定文件名
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
            Response response = uploadManager.put(localFilePath, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }
    }

    /**
     * 删除服务器中图片
     */
    @Test
    public void test2(){
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Zone.zone2());
        String accessKey = "5WDHiEbcDel2EWJje5WMPKsKTTr4kAvINdgsw0Wo";
        String secretKey = "GMx3_4tfOn8nSnh3vfWzQjO6Hplo2Xg7ilB0RVqV";
        String bucket = "dhuhealthspace01";
        String key = "abc.jpg   ";
        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            bucketManager.delete(bucket, key);
        } catch (QiniuException ex) {
//如果遇到异常，说明删除失败
            System.err.println(ex.code());
            System.err.println(ex.response.toString());
        }
    }
}
