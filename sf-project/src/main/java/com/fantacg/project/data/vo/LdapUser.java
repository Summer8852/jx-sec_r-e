package com.fantacg.project.data.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Dupengfei
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LdapUser {

    public String cn;
    public String sn;
    public String uid;
    public String userPassword;
    public String displayName;
    public String mail;
    public String description;
    public String uidNumber;
    public String gidNumber;
    /**忽略get\set方法**/
}
