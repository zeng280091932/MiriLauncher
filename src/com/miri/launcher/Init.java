/* 
 * 文件名：Init.java
 * 版权：Copyright
 */
package com.miri.launcher;

import com.miri.launcher.http.NetWorkInfoException;
import com.miri.launcher.json.JsonParserException;
import com.miri.launcher.msg.MsgParser;
import com.miri.launcher.msg.exception.MsgInfoException;
import com.miri.launcher.utils.Logger;
import com.miri.launcher.utils.Toolkit;

/**
 * @author penglin
 * @version TVLAUNCHER001, 2013-8-1
 */
public class Init {

    private static State initState = State.PENDING;

    public enum State {
        PENDING, //未执行
        RUNNING, //正在执行中
        SUCCESS, //执行成功
        FAILURE, //执行失败
    }

    /**
     * 初始化消息接口，认证登录成功后，请求应用推荐和广告推荐数据，并建立心跳任务
     * @throws MsgInfoException
     */
    public static void initMsg() throws NetWorkInfoException, JsonParserException, MsgInfoException {
        try {
            initState = State.RUNNING;
            //检测是否已分配认证码
            String authCode = MsgParser.getAuthCode();
            //1.进行认证
            if (Toolkit.isEmpty(authCode)) {
                MsgParser.auth();
                Logger.getLogger().d("Msg auth success!");
            }
            //2.进行登陆
            MsgParser.login();
            Logger.getLogger().d("Msg login success!");
            initState = State.SUCCESS;
        } catch (NetWorkInfoException e) {
            initState = State.FAILURE;
        } catch (JsonParserException e) {
            initState = State.FAILURE;
        } catch (MsgInfoException e) {
            initState = State.FAILURE;
        }
    }

    public static State getInitState() {
        return initState;
    }

}
