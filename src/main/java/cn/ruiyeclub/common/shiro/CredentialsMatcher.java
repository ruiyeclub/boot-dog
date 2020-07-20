package cn.ruiyeclub.common.shiro;


import cn.ruiyeclub.common.shiro.jwt.JwtToken;
import cn.ruiyeclub.common.util.JwtUtil;
import cn.ruiyeclub.common.util.MD5EncryptUtil;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;

/**
 * @author licoy.cn
 * @version 2017/9/25
 */
public class CredentialsMatcher extends SimpleCredentialsMatcher {

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        JwtToken jwtToken = (JwtToken) token;
        Object accountCredentials = getCredentials(info);
        if(jwtToken.getPassword()!=null){
            Object tokenCredentials = MD5EncryptUtil.encrypt(
                    jwtToken.getPassword()+jwtToken.getUsername());
            if(!accountCredentials.equals(tokenCredentials)){
                throw new DisabledAccountException("密码不正确！");
            }
        }else{
            boolean verify = JwtUtil.verify(jwtToken.getToken(), jwtToken.getUsername(), accountCredentials.toString());
            if(!verify){
                throw new DisabledAccountException("verifyFail");
            }
        }
        return true;
    }

}
