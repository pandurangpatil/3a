SELECT 
    P.CODE
FROM
    UserAdmin.USERAUTHSESSION us
        JOIN
    UserAdmin.USER u ON u.ID = us.USERID AND us.ID = 'session'
        JOIN
    UserAdmin.USERAPPLICATIONREGISTRATION aur ON aur.USER_ID = u.ID AND aur.APP_ID = (SELECT 
            ID
        FROM
            UserAdmin.APPLICATION app
        WHERE
            app.DOMAIN = 'domain')
        JOIN
    UserAdmin.USERAPPLICATIONCTXREGISTRATION uacr ON uacr.USAPPLICATIONREGISTRATION_ID = aur.ID AND uacr.CONTEXT_ID = (SELECT 
            ID
        FROM
            UserAdmin.CONTEXT ctx
        WHERE
            ctx.NAME = 'context')
        JOIN
    UserAdmin.ADMINCONTEXTROLE ACR ON ACR.USAPPLICATIONCTXREGISTRATION_ID = uacr.ID
        JOIN
    UserAdmin.ADMINCONTEXTROLE_ROLE ACRR ON ACRR.AdminContextRole_ID = ACR.ID
        JOIN
    UserAdmin.ROLE R ON R.ID = ACRR.roles_ID
        JOIN
    UserAdmin.ROLE_PERMISSION RP ON RP.Role_ID = R.ID
        JOIN
    UserAdmin.PERMISSION P ON P.ID = RP.permissions_ID