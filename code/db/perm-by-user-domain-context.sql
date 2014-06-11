SELECT P.CODE FROM UserAdmin.USERAUTHSESSION us
        JOIN
    UserAdmin.USER u ON u.ID = us.USERID AND us.ID = 'sess'
        JOIN
    UserAdmin.USERAPPLICATIONREGISTRATION aur ON aur.USER_ID = u.ID AND aur.APP_ID = (SELECT 
            ID
        FROM
            UserAdmin.APPLICATION app
        WHERE
            app.DOMAIN = 'dom')
        JOIN
    UserAdmin.USERAPPLICATIONCTXREGISTRATION uacr ON uacr.USAPPLICATIONREGISTRATION_ID = aur.ID AND uacr.CONTEXT_ID = (SELECT 
            ID
        FROM
            UserAdmin.CONTEXT ctx
        WHERE
            ctx.NAME = 'context')
        JOIN
    UserAdmin.CONTEXTROLE CR ON CR.USAPPLICATIONCTXREGISTRATION_ID = uacr.ID
        JOIN
    UserAdmin.CONTEXTROLE_ROLE CRR ON CRR.ContextRole_ID = CR.ID
        JOIN
    UserAdmin.ROLE R ON R.ID = CRR.roles_ID
        JOIN
    UserAdmin.ROLE_PERMISSION RP ON RP.Role_ID = R.ID
        JOIN
    UserAdmin.PERMISSION P ON P.ID = RP.permissions_ID