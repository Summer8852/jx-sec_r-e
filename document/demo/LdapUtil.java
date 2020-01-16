package com.fantacg.data.controller;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.Control;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

import com.fantacg.data.vo.LdapUser;


public class LdapUtil {
    public static void main(String[] args) throws NamingException {
        String url = "ldap://192.168.2.2:389/";
        String basedn = "dc=jx,dc=co";  // basedn
        String root = "jx-admin@jx.co";  // 用户
        String pwd ="Wenbin!@1521";  // pwd

        LdapContext ctx = ldapConnect(url,root,pwd);//ldap认证
        LdapUser ldapUser = new LdapUser();
        ldapUser.setCn("test123");
        ldapUser.setSn("test123");
        ldapUser.setUid("test123");
        ldapUser.setUserPassword("123456789");
        ldapUser.setDisplayName("Test5");
        ldapUser.setMail("1@qq.con");
        ldapUser.setDescription("1");
        ldapUser.setUidNumber("test123");
        ldapUser.setGidNumber("Users");
        addUser(ldapUser,ctx);

//        ldapUser = new LdapUser();
//        ldapUser.setGidNumber("123");
//        ldapUser.setCn("test4");
//        addGoups(ldapUser,ctx);
    }
    /**
     * 添加组
     * @param lu
     * @param ctx
     * @return
     */
    public static boolean addGoups(LdapUser lu,LdapContext ctx) {
        BasicAttributes attrsbu = new BasicAttributes();
        BasicAttribute objclassSet = new BasicAttribute("objectClass");
        objclassSet.add("posixGroup");
        objclassSet.add("top");
        attrsbu.put(objclassSet);
        attrsbu.put("cn", lu.getCn());//显示账号
        attrsbu.put("userPassword", "{crypt}x");//显示

        attrsbu.put("gidNumber",lu.getGidNumber());/*显示组id */
        attrsbu.put("memberUid", lu.getCn());//显示账号
        try {
            String cn="cn="+lu.getCn()+",dc=jx,dc=co";
            System.out.println(cn);
            ctx.createSubcontext(cn, attrsbu);
            System.out.println("添加用户group成功");
            return true;
        } catch (Exception e) {
            System.out.println("添加用户group失败");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 添加用户
     * @param lu
     * @param ctx
     * @return
     */
    public static boolean addUser(LdapUser lu,LdapContext ctx) {
        BasicAttributes attrsbu = new BasicAttributes();
        BasicAttribute objclassSet = new BasicAttribute("objectClass");
        // objclassSet.add("account");
        objclassSet.add("posixAccount");
        objclassSet.add("inetOrgPerson");
        objclassSet.add("top");
        objclassSet.add("shadowAccount");
        attrsbu.put(objclassSet);
        attrsbu.put("uid",  lu.getUid());//显示账号
        attrsbu.put("sn", lu.getSn());//显示姓名
        attrsbu.put("cn", lu.getCn());//显示账号
        attrsbu.put("gecos", lu.getCn());//显示账号
        attrsbu.put("userPassword", lu.getUserPassword());//显示密码
        attrsbu.put("displayName", lu.getDisplayName());//显示描述
        attrsbu.put("mail", lu.getMail());//显示邮箱
        attrsbu.put("homeDirectory", "/home/" + lu.getCn());//显示home地址
        attrsbu.put("loginShell", "/bin/bash");//显示shell方式
        attrsbu.put("uidNumber", lu.getUidNumber());/*显示id */
        attrsbu.put("gidNumber", lu.getGidNumber());/*显示组id */

        try {
            String dn="uid="+lu.getCn()+",dc=jx,dc=co";
            System.out.println(dn);
            ctx.createSubcontext(dn, attrsbu);
            System.out.println("添加用户成功");
            return true;
        } catch (Exception e) {
            System.out.println("添加用户失败");
            e.printStackTrace();
            return false;
        }
    }
    /**
     * 修改属性
     * @param ctx
     * @return
     */
    public static boolean modifyInformation(LdapUser lu,LdapContext ctx) {
        try {
            ModificationItem[] mods = new ModificationItem[1];
            String dn="uid="+lu.getCn()+",ou=People,dc=tcjf,dc=com";
            /*添加属性*/
            //  Attribute attr0 = new BasicAttribute("description", "测试");
            //  mods[0] = new ModificationItem(DirContext.ADD_ATTRIBUTE,attr0);

            /*修改属性*/
            Attribute attr0 = new BasicAttribute("userPassword", lu.getUserPassword());
            mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attr0);

            /*删除属性*/
            //  Attribute attr0 = new BasicAttribute("description", "测试");
            //  mods[0] = new ModificationItem(DirContext.REMOVE_ATTRIBUTE, attr0);
            ctx.modifyAttributes(dn, mods);
            System.out.println("修改成功");
            return true;
        } catch (NamingException ne) {
            System.out.println("修改失败");
            ne.printStackTrace();
            return false;
        }

    }

    /**
     * 删除
     * @param dn
     * @param ctx
     * @return
     */
    public  static boolean delete(String dn,LdapContext ctx) {
        try {
            ctx.destroySubcontext(dn);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * 获取ldap认证
     * @param url
     * @param root
     * @param pwd
     * @return
     */
    public static LdapContext ldapConnect(String url,String root,String pwd){
        String factory = "com.sun.jndi.ldap.LdapCtxFactory";
        String simple="simple";
        Hashtable<String, String> env = new Hashtable<String, String>();
        env.put(Context.INITIAL_CONTEXT_FACTORY,factory);
        env.put(Context.PROVIDER_URL, url);
        env.put(Context.SECURITY_AUTHENTICATION, simple);
        env.put(Context.SECURITY_PRINCIPAL, root);
        env.put(Context.SECURITY_CREDENTIALS, pwd);
        LdapContext ctx = null;
        Control[] connCtls = null;
        try {
            ctx = new InitialLdapContext(env, connCtls);
            System.out.println( "认证成功:"+url);
        }catch (javax.naming.AuthenticationException e) {
            System.out.println("认证失败：");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("认证出错：");
            e.printStackTrace();
        }
        return ctx;
    }

    /**
     * 获取用户信息
     * @param ctx
     * @param basedn
     * @return
     */
    public static List<LdapUser> readLdap(LdapContext ctx, String basedn){

        List<LdapUser> lm=new ArrayList<LdapUser>();
        try {
            if(ctx!=null){
                //过滤条件
                String filter = "(&(objectClass=*)(uid=*))";
                String[] attrPersonArray = { "uid", "userPassword", "displayName", "cn", "sn", "mail", "description" };
                SearchControls searchControls = new SearchControls();//搜索控件
                searchControls.setSearchScope(2);//搜索范围
                searchControls.setReturningAttributes(attrPersonArray);
                //1.要搜索的上下文或对象的名称；2.过滤条件，可为null，默认搜索所有信息；3.搜索控件，可为null，使用默认的搜索控件
                NamingEnumeration<SearchResult> answer = ctx.search(basedn, filter.toString(),searchControls);
                while (answer.hasMore()) {
                    SearchResult result = (SearchResult) answer.next();
                    NamingEnumeration<? extends Attribute> attrs = result.getAttributes().getAll();
                    LdapUser lu=new LdapUser();
                    while (attrs.hasMore()) {
                        Attribute attr = (Attribute) attrs.next();
                        if("userPassword".equals(attr.getID())){
                            Object value = attr.get();
                            lu.setUserPassword(new String((byte [])value));
                        }else if("uid".equals(attr.getID())){
                            lu.setUid(attr.get().toString());
                        }else if("displayName".equals(attr.getID())){
                            lu.setDisplayName(attr.get().toString());
                        }else if("cn".equals(attr.getID())){
                            lu.setCn(attr.get().toString());
                        }else if("sn".equals(attr.getID())){
                            lu.setSn(attr.get().toString());
                        }else if("mail".equals(attr.getID())){
                            lu.setMail(attr.get().toString());
                        }else if("description".equals(attr.getID())){
                            lu.setDescription(attr.get().toString());
                        }
                    }
                    if(lu.getUid()!=null) {
                        lm.add(lu);
                    }

                }
            }
        }catch (Exception e) {
            System.out.println("获取用户信息异常:");
            e.printStackTrace();
        }

        return lm;
    }

    /**
     * string 转 数值
     * @param m
     * @return
     */
    public static String strToint(String m){
        if(m==null || "".equals(m)) {
            return "-1";
        }
        char [] a=m.toCharArray();
        StringBuffer sbu = new StringBuffer();
        for(char c:a) {
            sbu.append((int)c);
        }
        System.out.println(sbu.toString());
        return sbu.toString();
    }
    /**
     * 获取时间戳
     * @return
     */
    public static long getNowTimeStamp() {
        long time = System.currentTimeMillis();
        time =  time / 1000;
        return time;
    }

}
