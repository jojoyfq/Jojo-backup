package PermissionManagedBean;

import CommonEntity.Permission;
import java.io.Serializable;


public class PermissionDataTableRow implements Serializable
{
    private Permission permission;
    private Boolean validity;

    
    
    public PermissionDataTableRow() 
    {
    }

    
    
    public PermissionDataTableRow(Permission permission) 
    {
        this.permission = permission;
        
        if(permission != null)
            validity = permission.isValidity();
    }
    
    

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    public Boolean getValidity() {
        return validity;
    }

    public void setValidity(Boolean validity) {
        this.validity = validity;
    }
}