import com.fantacg.upload.SfUploadService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

//SpringBoot1.4版本之前用的是SpringJUnit4ClassRunner.class
//如果查看源码你会发现，SpringRunner.class就是继承的SpringJUnit4ClassRunner.class，而且没修改任何东西
@RunWith(SpringRunner.class)
//SpringBoot1.4版本之前用的是@SpringApplicationConfiguration(classes = Application.class)
@SpringBootTest(classes = SfUploadService.class)
//测试环境使用，用来表示测试环境使用的ApplicationContext将是WebApplicationContext类型的
@WebAppConfiguration
public class JunitTest {

    @Autowired
    StringRedisTemplate redisTemplate;






    @Test
    public void getHello(){

        // 在变量左边添加元素值
//        redisTemplate.opsForList().leftPush("list","a");
//        redisTemplate.opsForList().leftPush("list","b");
//        redisTemplate.opsForList().leftPush("list","c");

        //把最后一个参数值放到指定集合的第一个出现中间参数的前面，如果中间参数值存在的话
//        redisTemplate.opsForList().leftPush("list","a","n");
        redisTemplate.opsForList().leftPushAll("list","w","x","y");

//        获取集合指定位置的值
        String listValue = redisTemplate.opsForList().index("list",1) + "";
        System.out.println("通过index(K key, long index)方法获取指定位置的值:" + listValue);
    }

//企业基本信息
//    "corpName":"XXXXXXXX",
//    "corpCode":"XXXXXXXX",
//    "corpType":"XXXXXXXX",
//    "licenseNum":"XXXXXXXX",
//    "areaCode":"XXXXXXXX",
//    "address":"XXXXXXXX",
//    "zipCode":"XXXXXXXX",
//    "legalMan":"XXXXXXXX",
//    "legalManDuty":"XXXXXXXX",
//    "legalManProTitle":"XXXXXXXX",
//    "legalManIdCardType":"XXXXXXXX",
//    "legalManIdCardNumber":"XXXXXXXX",
//    "regCapital":"XXXXXXXX",
//    "factRegCapital":"XXXXXXXX",
//    "capitalCurrencyType":"XXXXXXXX",
//    "webSite":"XXXXXXXX",
//    "registerDate":"XXXXXXXX",
//    "establishDate":"XXXXXXXX",
//    "officePhone":"XXXXXXXX",
//    "faxNumber":"XXXXXXXX",
//    "linkMan":"XXXXXXXX",
//    "linkPhone":"XXXXXXXX",
//    "email":"XXXXXXXX"

//企业资质信息
//    "certTypeNum":"XXXXXXXX",
//    "certTradeStatusNum":"XXXXXXXX",
//    "certId":"XXXXXXXX",
//    "tradeBoundNum":"XXXXXXXX",
//    "tradeTypeBoundChildMark":"XXXXXXXX",
//    "titleLevelNum":"XXXXXXXX",
//    "mark":"XXXXXXXX",
//    "limitContent":"XXXXXXXX",
//    "noteNumber":"XXXXXXXX",
//    "noteDate":"XXXXXXXX",
//    "addTypeNum":"XXXXXXXX",
//    "certTradeModifyDate":"XXXXXXXX",
//    "certTradeModifyMark":"XXXXXXXX"

//项目基本信息
//    "name":"XXXXXXXX",
//    "code":"XXXXXXXX",
//    "contractorCorpName":"XXXXXXXX",
//    "contractorCorpCode":"XXXXXXXX",
//    "category}":"XXXXXXXX",
//    "prjStatus":"XXXXXXXX",
//    "description":"XXXXXXXX",
//    "address":"XXXXXXXX",
//    "buildCorpName":"XXXXXXXX",
//    "buildCorpCode":"XXXXXXXX",
//    "buildPlanNum":"XXXXXXXX",
//    "prjPlanNum":"XXXXXXXX",
//    "invest":"XXXXXXXX",
//    "buildingArea":"XXXXXXXX",
//    "buildingLength":"XXXXXXXX",
//    "areaCode":"XXXXXXXX",
//    "startDate":"XXXXXXXX",
//    "completeDate":"XXXXXXXX",
//    "lng":"XXXXXXXX",
//    "lat":"XXXXXXXX",
//    "prjSize":"XXXXXXXX",
//    "propertyNum":"XXXXXXXX",
//    "approvaNum":"XXXXXXXX",
//    "approvalLevelNum":"XXXXXXXX",
//    "linkMan":"XXXXXXXX",
//    "linkPhone":"XXXXXXXX",
//    "prjNum":"XXXXXXXX",
//    "nationNum":"XXXXXXXX",
//    "prjName":"XXXXXXXX",
//    "builderLicenseNum":"XXXXXXXX",

//项目参见单位
//    "corpName":"XXXXXXXX",
//    "corpCode":"XXXXXXXX",
//    "corpType":"XXXXXXXX",
//    "entryTime":"XXXXXXXX",
//    "exitTime":"XXXXXXXX",
//    "pmName":"XXXXXXXX",
//    "pmPhone":"XXXXXXXX",
//    "pmIdCardType":"XXXXXXXX",
//    "pmIdCardNumber":"XXXXXXXX",
//    "bankNumber":"XXXXXXXX",
//    "businessType":"XXXXXXXX",
//    "businessSysNo":"XXXXXXXX",
//    "bankName":"XXXXXXXX",
//    "bankLinkNumber":"XXXXXXXX"


//项目班组信息
//    "teamName":"XXXXXXXX",
//    "teamNo":"XXXXXXXX",
//    "entryTime":"XXXXXXXX",
//    "exitTime":"XXXXXXXX",
//    "projectName":"XXXXXXXX",
//    "projectCode":"XXXXXXXX",
//    "corpName":"XXXXXXXX",
//    "corpCode":"XXXXXXXX",
//    "responsiblePersonName":"XXXXXXXX",
//    "responsiblePersonPhone":"XXXXXXXX",
//    "responsibleIdType":"XXXXXXXX",
//    "responsibleIdNumber":"XXXXXXXX",
//    "teamLeaderName":"XXXXXXXX",
//    "teamLeaderPhone":"XXXXXXXX",
//    "teamLeaderIdType":"XXXXXXXX",
//    "teamLeaderIdNumber":"XXXXXXXX",
//    "remark":"XXXXXXXX"





//            "certTypeNum": "XXXXXXXX",
//            "certTradeStatusNum": "XXXXXXXX",
//            "certId": "XXXXXXXX",
//            "tradeBoundNum": "XXXXXXXX",
//            "tradeTypeBoundChildMark": "XXXXXXXX",
//            "titleLevelNum": "XXXXXXXX",
//            "mark": "XXXXXXXX",
//            "limitContent": "XXXXXXXX",
//            "noteNumber": "XXXXXXXX",
//            "noteDate": "XXXXXXXX",
//            "addTypeNum": "XXXXXXXX",
//            "certTradeModifyDate": "XXXXXXXX",
//            "certTradeModifyMark": "XXXXXXXX"


}