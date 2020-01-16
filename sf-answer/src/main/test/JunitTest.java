import com.fantacg.answer.SfAnswer;
import com.fantacg.answer.mapper.*;
import com.fantacg.common.pojo.answer.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 *
 * @author 杜鹏飞
 * @Classname JunitTest
 * @Created by Dupengfei 2020-01-02 20:47
 * @Version 2.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes={SfAnswer.class})
public class JunitTest {

    @Autowired
    ProjectTrainingMapper projectTrainingMapper;
    @Autowired
    ProjectTrainingDetailMapper projectTrainingDetailMapper;
    @Autowired
    ProjectTrainingAnswerMapper projectTrainingAnswerMapper;
    @Autowired
    ProjectTrainingVideoMapper projectTrainingVideoMapper;
    @Autowired
    ProjectTrainingMemberMapper projectTrainingMemberMapper;
    @Autowired
    AnswerLogMapper answerLogMapper;

    @Test
    public void printSysUserInfo(){
        ProjectTraining projectTraining = new ProjectTraining();
        projectTraining.setInUserName(1184012597539049472l);
        List<ProjectTraining> select = projectTrainingMapper.select(projectTraining);
        for (ProjectTraining training : select) {
            ProjectTrainingDetail projectTrainingDetail = new ProjectTrainingDetail();
            projectTrainingDetail.setProjectTrainingId(training.getId());
            projectTrainingDetailMapper.delete(projectTrainingDetail);

            ProjectTrainingMember projectTrainingMember = new ProjectTrainingMember();
            projectTrainingMember.setProjectTrainingId(training.getId());
            projectTrainingMemberMapper.delete(projectTrainingMember);

            ProjectTrainingVideo projectTrainingVideo = new ProjectTrainingVideo();
            projectTrainingVideo.setProjectTrainingId(training.getId());
            projectTrainingVideoMapper.delete(projectTrainingVideo);

            ProjectTrainingAnswer projectTrainingAnswer = new ProjectTrainingAnswer();
            projectTrainingAnswer.setProjectTrainingId(training.getId());
            projectTrainingAnswerMapper.delete(projectTrainingAnswer);

            AnswerLog answerLog = new AnswerLog();
            answerLog.setProjectTrainingId(training.getId());
            answerLogMapper.delete(answerLog);
        }

        projectTrainingMapper.delete(projectTraining);
    }
}
